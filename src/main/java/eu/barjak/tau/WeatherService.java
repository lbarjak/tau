package eu.barjak.tau;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.time.Duration;
import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    public void weather(Data data) {
        int thermalTimeConstant = data.getThermalTimeConstant();
        Double initRoomTemp = data.getInitRoomTemp();
        int omszId = data.getOmszId();
        String startDateString = data.getStartDate();
        String startTimeString = data.getStartTime();
        String endDateString = data.getEndDate();
        int correction = data.getCorrection();
        List<Temperature> temperatures = data.getTemperatures();
        int indexOfMeasuredTemperatures = indexOfMeasuredTemps(startDateString);

        Map<LocalDate, List<Temperature>> temperaturesMap = new LinkedHashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.");
        LocalDate startDate = LocalDate.parse(startDateString, formatter);
        LocalDate endDate = LocalDate.parse(endDateString, formatter);

        temperatures.clear();

        Dates dates = new Dates(temperaturesMap);
        dates.elapsedDays(startDate, endDate);

        WeatherQuery weatherQuery = new WeatherQuery(omszId, temperaturesMap);
        weatherQuery.steps();

        if (indexOfMeasuredTemperatures > 0) {
            Calculation calculation = new Calculation(thermalTimeConstant, temperaturesMap, temperatures, correction);
            calculation.calculation(startTimeString, initRoomTemp);
            if (indexOfMeasuredTemperatures > 144) {
                Double last24hAverage = calculation.last24hAverage(indexOfMeasuredTemperatures);
                calculation.forecast(indexOfMeasuredTemperatures, last24hAverage);
            }
        }
        data.setIndexOfMeasuredTemperatures(indexOfMeasuredTemperatures);
    }

    public int indexOfMeasuredTemps(String startDateString) {
        startDateString = startDateString.replaceAll("\\.", "-").replaceAll("-$", "");
        LocalDate startDate = LocalDate.parse(startDateString);
        LocalTime nullTime = LocalTime.parse("00:00");
        LocalDateTime startDateTime = LocalDateTime.of(startDate, nullTime);
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(startDateTime, now);
        return (int) (duration.toMinutes() / 10);
    }
}
