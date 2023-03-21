package eu.barjak.tau;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Service;

@Service
public class WeatherService {

    Data data = Data.getInstance();

    int thermalTimeConstant = data.getThermalTimeConstant();
    String startDateString = data.getStartDateString();
    String endDateString = data.getEndDateString();
    String startTimeString = data.getStartTimeString();
    Double initRoomTemp = data.getInitRoomTemp();
    int omszId = data.getOmszId();
    LinkedHashMap<LocalDate, ArrayList<Temperature>> temperaturesMap = data.getTemperaturesMap();
    ArrayList<Temperature> temperatures = data.getTemperatures();

    public void weather() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd.");
        LocalDate startDate = LocalDate.parse(startDateString, formatter);
        LocalDate endDate = LocalDate.parse(endDateString, formatter);

        data.clearTemperatures();

        Dates dates = new Dates(temperaturesMap);
        try {
            dates.elapsedDays(startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        WeatherQuery weatherQuery = new WeatherQuery(omszId, temperaturesMap);
        int indexOfMeasuredTemperatures = weatherQuery.steps();

        if (indexOfMeasuredTemperatures > 0) {
            Calculation calculation = new Calculation(thermalTimeConstant, temperaturesMap, temperatures);
            calculation.calculation(startTimeString, initRoomTemp);
            if (indexOfMeasuredTemperatures > 144) {
                Double last24hAverage = calculation.last24hAverage(indexOfMeasuredTemperatures);
                calculation.forecast(indexOfMeasuredTemperatures, last24hAverage);
            }
        }
        data.setIndexOfMeasuredTemperatures(indexOfMeasuredTemperatures);
    }
}
