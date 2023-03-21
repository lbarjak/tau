package eu.barjak.tau;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Set;

public class Calculation {
    Double thermalTimeConstant;
    Double outdoorTemp;
    LinkedHashMap<LocalDate, ArrayList<Temperature>> temperaturesMap;
    ArrayList<Temperature> temperatures;
    Double exponent;
    Double multiplier;

    Calculation(Double thermalTimeConstant, LinkedHashMap<LocalDate, ArrayList<Temperature>> temperaturesMap,
            ArrayList<Temperature> temperatures) {
        this.temperaturesMap = temperaturesMap;
        this.temperatures = temperatures;
        this.multiplier = Math.exp(-(10.0 / 60) / thermalTimeConstant);// (exponent)
    }

    public Double tau(Double roomTemp) {
        return outdoorTemp + (roomTemp - outdoorTemp) * multiplier;
    }

    public void calculation(String startTimeString, Double initialRoomTemperature) {
        Locale huLoc = new Locale("hu");
        DateTimeFormatter napNeveMagyarul = DateTimeFormatter.ofPattern("EEEE", huLoc);
        String day;
        Double roomTemp = initialRoomTemperature;

        Set<LocalDate> localDates = temperaturesMap.keySet();
        for (LocalDate localDate : localDates) {
            for (Temperature temperature : temperaturesMap.get(localDate)) {
                temperatures.add(temperature);
                temperature.setDate(localDate);
                day = localDate.format(napNeveMagyarul);
                temperature.setDay(day);
            }
        }

        int startTimeIndex = searchStartTimeIndex(startTimeString);

        for (int i = startTimeIndex; i < temperatures.size(); i++) {
            temperatures.get(i).setRoomTemp(roomTemp);
            outdoorTemp = temperatures.get(i).getOutdoorTemp();
            if (outdoorTemp != null) {
                roomTemp = tau(roomTemp);
            }
        }
    }

    public int searchStartTimeIndex(String startTimeString) {
        String timeString;
        int startTimeIndex = 0;
        for (int i = 0; i < 144; i++) {
            timeString = temperatures.get(i).getTime();
            if (timeString.equals(startTimeString)) {
                startTimeIndex = i;
            }
        }
        return startTimeIndex;
    }

    public Double last24hAverage(int indexOfMeasuredTemperatures) {
        Double sum = 0.0;
        Double temp;
        int divider = 144;
        for (int i = indexOfMeasuredTemperatures - 1; i > indexOfMeasuredTemperatures - 145; i--) {
            temp = temperatures.get(i).getOutdoorTemp();
            if (temp != null) {
                sum += temp;
            } else {
                divider--;
            }
        }
        return sum / divider;
    }

    public void forecast(int indexOfMeasuredTemperatures, Double last24hAverage) {
        Double roomTemp = temperatures.get(indexOfMeasuredTemperatures - 1).getRoomTemp();
        for (int i = indexOfMeasuredTemperatures; i < temperatures.size(); i++) {
            temperatures.get(i).setOutdoorTemp(last24hAverage);
            temperatures.get(i).setRoomTemp(roomTemp);
            outdoorTemp = temperatures.get(i).getOutdoorTemp();
            if (outdoorTemp != null) {
                roomTemp = tau(roomTemp);
            }
        }
    }
}
