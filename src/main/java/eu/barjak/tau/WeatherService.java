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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    @Autowired
    Data data;

    public void weather() throws IOException {
        int thermalTimeConstant = data.getThermalTimeConstant();
        Double initRoomTemp = data.getInitRoomTemp();
        int omszId = data.getOmszId();
        String startDateString = data.getStartDate();
        String startTimeString = data.getStartTime();
        String endDateString = data.getEndDate();
        int correction = data.getCorrection();

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
        return indexOfMeasuredTemperatures + 5;
    }
}
