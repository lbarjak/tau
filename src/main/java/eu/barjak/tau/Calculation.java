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
    LinkedHashMap<LocalDate, ArrayList<Temperature>> TEMPERATURES_MAP;
    ArrayList<Temperature> TEMPERATURES = new ArrayList<>();
    Double exponent;
    Double multiplier;

    Calculation(Double thermalTimeConstant, LinkedHashMap<LocalDate, ArrayList<Temperature>> TEMPERATURES_MAP) {
        this.TEMPERATURES_MAP = TEMPERATURES_MAP;
        this.multiplier = Math.exp(-(10.0 / 60) / thermalTimeConstant);// (exponent)
    }

    public Double tau(Double roomTemp) {
        return outdoorTemp + (roomTemp - outdoorTemp) * multiplier;
    }

    public ArrayList<Temperature> calculation(String startTimeString, Double initialRoomTemperature) {
        Locale huLoc = new Locale("hu");
        DateTimeFormatter napNeveMagyarul = DateTimeFormatter.ofPattern("EEEE", huLoc);
        String day;
        Double roomTemp = initialRoomTemperature;

        Set<LocalDate> localDates = TEMPERATURES_MAP.keySet();
        for (LocalDate localDate : localDates) {
            for (Temperature temperature : TEMPERATURES_MAP.get(localDate)) {
                TEMPERATURES.add(temperature);
                temperature.setDate(localDate);
                day = localDate.format(napNeveMagyarul);
                temperature.setDay(day);
            }
        }

        int startTimeIndex = searchStartTimeIndex(startTimeString);

        for (int i = startTimeIndex; i < TEMPERATURES.size(); i++) {
            TEMPERATURES.get(i).setRoomTemp(roomTemp);
            outdoorTemp = TEMPERATURES.get(i).getOutdoorTemp();
            if (outdoorTemp != null) {
                roomTemp = tau(roomTemp);
            }
        }
        return TEMPERATURES;
    }

    public int searchStartTimeIndex(String startTimeString) {
        String timeString;
        int startTimeIndex = 0;
        for (int i = 0; i < 144; i++) {
            timeString = TEMPERATURES.get(i).getTime();
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
            temp = TEMPERATURES.get(i).getOutdoorTemp();
            if (temp != null) {
                sum += temp;
            } else {
                divider--;
            }
        }
        return sum / divider;
    }

    public void forecast(int indexOfMeasuredTemperatures, Double last24hAverage) {
        Double roomTemp = TEMPERATURES.get(indexOfMeasuredTemperatures - 1).getRoomTemp();
        for (int i = indexOfMeasuredTemperatures; i < TEMPERATURES.size(); i++) {
            TEMPERATURES.get(i).setOutdoorTemp(last24hAverage);
            TEMPERATURES.get(i).setRoomTemp(roomTemp);
            outdoorTemp = TEMPERATURES.get(i).getOutdoorTemp();
            if (outdoorTemp != null) {
                roomTemp = tau(roomTemp);
            }
        }
    }
}
