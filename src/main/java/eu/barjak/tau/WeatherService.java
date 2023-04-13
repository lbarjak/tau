package eu.barjak.tau;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import java.time.Duration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.Setter;

@Service
@Setter
@Getter
@ConfigurationProperties(prefix = "tau")
public class WeatherService {
    int thermalTimeConstant;
    Double initRoomTemp;
    int omszId;
    String startDateString;
    String startTimeString;
    String endDateString;
    int correction;
    boolean load;

    WeatherService() {
        this.load = true;
    }

    public void weather(Data data) throws IOException {
        if (load) {
            data.setThermalTimeConstant(thermalTimeConstant);
            data.setInitRoomTemp(initRoomTemp);
            data.setOmszId(omszId);
            data.setStartDate(startDateString);
            data.setStartTime(startTimeString);
            data.setEndDate(endDateString);
            data.setCorrection(correction);
        }
        load = false;
        thermalTimeConstant = data.getThermalTimeConstant();
        initRoomTemp = data.getInitRoomTemp();
        omszId = data.getOmszId();
        startDateString = data.getStartDate();
        startTimeString = data.getStartTime();
        endDateString = data.getEndDate();
        correction = data.getCorrection();

        List<Temperature> temperatures = data.getTemperatures();
        int indexOfMeasuredTemperatures = indexOfMeasuredTemps(startDateString, endDateString);
        data.setIndexOfMeasuredTemperatures(indexOfMeasuredTemperatures);

        Map<LocalDate, List<Temperature>> temperaturesMap = new LinkedHashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.");
        LocalDate startDate = LocalDate.parse(startDateString, formatter);
        LocalDate endDate = LocalDate.parse(endDateString, formatter);

        temperatures.clear();

        Dates dates = new Dates(temperaturesMap);
        dates.elapsedDays(startDate, endDate);

        if (data.getOmszId() == 590) {
            ForecastQuery forecastQuery = new ForecastQuery(data, temperaturesMap);
            forecastQuery.queryForecast();
        }

        WeatherQuery weatherQuery = new WeatherQuery(omszId, temperaturesMap, data);
        weatherQuery.steps();

        if (indexOfMeasuredTemperatures > 0) {
            Calculation calculation = new Calculation(thermalTimeConstant, temperaturesMap, temperatures, correction);
            calculation.calculation(startTimeString, initRoomTemp);

            if (indexOfMeasuredTemperatures > 144 && data.getOmszId() != 590) {
                Double last24hAverage = calculation
                        .last24hAverage(indexOfMeasuredTemperatures);
                calculation.forecast(indexOfMeasuredTemperatures, last24hAverage);
            }
            if (data.getOmszId() == 590) {
                int indexOfForecast = data.getIndexOfForecast();
                Double last24hAverage = calculation
                        .last24hAverage(indexOfForecast);
                calculation.forecast(indexOfForecast, last24hAverage);
            }
        }
    }

    public int indexOfMeasuredTemps(String startDateString, String endDateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.");
        LocalDateTime now = LocalDateTime.now();

        LocalDate endDate = LocalDate.parse(endDateString, formatter);
        LocalTime endTime = LocalTime.parse("23:59");
        LocalDateTime endDateTime = LocalDateTime.of(endDate, endTime);

        if (endDateTime.isBefore(now)) {
            now = endDateTime;
        }

        LocalDate startDate = LocalDate.parse(startDateString, formatter);
        LocalTime zeroTime = LocalTime.parse("00:00");
        LocalDateTime startDateTime = LocalDateTime.of(startDate, zeroTime);
        Duration duration = Duration.between(startDateTime, now);

        int indexOfMeasuredTemperatures = (int) duration.toMinutes() / 10;
        indexOfMeasuredTemperatures = indexOfMeasuredTemperatures - indexOfMeasuredTemperatures % 5;
        return indexOfMeasuredTemperatures - 1;
    }
}
