package eu.barjak.tau;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.springframework.stereotype.Service;

@Service
public class WeatherService {
    public static final String startDateString = "2023-03-18";
    public static final String endDateString = "2023-03-26";
    public static final String startTimeString = "16:50";
    public static final Double initialRoomTemperature = 18d;
    public static final Double thermalTimeConstant = 90d;// min. 3.3 max. 122
    public static final LinkedHashMap<LocalDate, ArrayList<Temperature>> TEMPERATURES_MAP = new LinkedHashMap<>();
    public static ArrayList<Temperature> TEMPERATURES;

    public int weather() {
        LocalDate startDate = LocalDate.parse(startDateString);
        LocalDate endDate = LocalDate.parse(endDateString);

        TEMPERATURES_MAP.clear();

        Dates dates = new Dates(TEMPERATURES_MAP);
        try {
            dates.elapsedDays(startDate, endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        WeatherQuery weatherQuery = new WeatherQuery(TEMPERATURES_MAP);
        int indexOfMeasuredTemperatures = weatherQuery.steps();

        if (indexOfMeasuredTemperatures > 0) {
            Calculation calculation = new Calculation(thermalTimeConstant, TEMPERATURES_MAP);
            TEMPERATURES = calculation.calculation(startTimeString, initialRoomTemperature);
            if (indexOfMeasuredTemperatures > 144) {
                Double last24hAverage = calculation.last24hAverage(indexOfMeasuredTemperatures);
                calculation.forecast(indexOfMeasuredTemperatures, last24hAverage);
            }
        }
        return indexOfMeasuredTemperatures;
    }
}
