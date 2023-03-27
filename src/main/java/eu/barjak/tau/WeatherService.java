package eu.barjak.tau;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

        Map<LocalDate, List<Temperature>> temperaturesMap = new LinkedHashMap<>();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.");
        LocalDate startDate = LocalDate.parse(startDateString, formatter);
        LocalDate endDate = LocalDate.parse(endDateString, formatter);

        temperatures.clear();

        Dates dates = new Dates(temperaturesMap);
        dates.elapsedDays(startDate, endDate);

        WeatherQuery weatherQuery = new WeatherQuery(omszId, temperaturesMap);
        int indexOfMeasuredTemperatures = weatherQuery.steps();

        if (indexOfMeasuredTemperatures > 0) {
            Calculation calculation = new Calculation(thermalTimeConstant, temperaturesMap, temperatures, correction);
            int startTimeIndex = calculation.calculation(startTimeString, initRoomTemp);
            indexOfMeasuredTemperatures = indexOfMeasuredTemperatures - (144 - startTimeIndex);
            if (indexOfMeasuredTemperatures > 144) {
                Double last24hAverage = calculation.last24hAverage(indexOfMeasuredTemperatures);
                calculation.forecast(indexOfMeasuredTemperatures, last24hAverage);
            }
        }
        data.setIndexOfMeasuredTemperatures(indexOfMeasuredTemperatures);
    }
}
