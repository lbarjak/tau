package eu.barjak.tau;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Service;

@Service
public class WeatherService {
    String startDateString = "2023-03-18";
    String endDateString = "2023-03-26";
    String startTimeString = "16:50";
    Double initialRoomTemperature = 18d;
    Double thermalTimeConstant = 90d;// min. 3.3 max. 122
    LinkedHashMap<LocalDate, ArrayList<Temperature>> temperaturesMap = new LinkedHashMap<>();
    ArrayList<Temperature> temperatures;

    public int weather() {
        LocalDate startDate = LocalDate.parse(startDateString);
        LocalDate endDate = LocalDate.parse(endDateString);

        temperaturesMap.clear();

        Dates dates = new Dates(temperaturesMap);
        try {
            dates.elapsedDays(startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        WeatherQuery weatherQuery = new WeatherQuery(temperaturesMap);
        int indexOfMeasuredTemperatures = weatherQuery.steps();

        if (indexOfMeasuredTemperatures > 0) {
            Calculation calculation = new Calculation(thermalTimeConstant, temperaturesMap);
            temperatures = (ArrayList<Temperature>) calculation.calculation(startTimeString, initialRoomTemperature);
            if (indexOfMeasuredTemperatures > 144) {
                Double last24hAverage = calculation.last24hAverage(indexOfMeasuredTemperatures);
                calculation.forecast(indexOfMeasuredTemperatures, last24hAverage);
            }
        }
        return indexOfMeasuredTemperatures;
    }
}
