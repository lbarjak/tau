package eu.barjak.tau;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    public void weather(Data data) {
        int thermalTimeConstant = data.getThermalTimeConstant();
        String startDateString = data.getStartDate();
        String endDateString = data.getEndDate();
        String startTimeString = data.getStartTime();
        Double initRoomTemp = data.getInitRoomTemp();
        int omszId = data.getOmszId();
        int correction = data.getCorrection();
        Map<LocalDate, ArrayList<Temperature>> temperaturesMap = data.getTemperaturesMap();
        List<Temperature> temperatures = data.getTemperatures();

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
            calculation.calculation(startTimeString, initRoomTemp);
            if (indexOfMeasuredTemperatures > 144) {
                Double last24hAverage = calculation.last24hAverage(indexOfMeasuredTemperatures);
                calculation.forecast(indexOfMeasuredTemperatures, last24hAverage);
            }
        }
        data.setIndexOfMeasuredTemperatures(indexOfMeasuredTemperatures);
    }
}
