package eu.barjak.tau;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Calculation {
    Double thermalTimeConstant;
    Double outdoorTemp;
    Map<LocalDate, List<Temperature>> temperaturesMap;
    List<Temperature> temperatures;
    Double exponent;
    Double multiplier;
    int correction;
    int startTimeIndex;

    Calculation(int thermalTimeConstant, Map<LocalDate, List<Temperature>> temperaturesMap,
            List<Temperature> temperatures, int correction) {
        this.temperaturesMap = temperaturesMap;
        this.temperatures = temperatures;
        this.multiplier = Math.exp(-(10.0 / 60) / thermalTimeConstant);// (exponent)
        this.correction = correction;
    }

    public Double tau(Double roomTemp) {
        return outdoorTemp + (roomTemp - outdoorTemp) * multiplier;
    }

    public void calculation(String startTimeString, Double initialRoomTemperature) {

        Double roomTemp = initialRoomTemperature - correction;

        Set<LocalDate> localDates = temperaturesMap.keySet();
        for (LocalDate localDate : localDates) {
            for (Temperature temperature : temperaturesMap.get(localDate)) {
                temperatures.add(temperature);
            }
        }

        LocalTime startTime = LocalTime.parse(startTimeString);
        startTimeIndex = (startTime.getHour() * 60 + startTime.getMinute()) / 10;

        for (int i = startTimeIndex; i < temperatures.size(); i++) {
            temperatures.get(i).setRoomTemp(roomTemp + correction);
            outdoorTemp = temperatures.get(i).getOutdoorTemp();
            if (outdoorTemp != null) {
                roomTemp = tau(roomTemp);
            }
        }
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
            temperatures.get(i).setOutdoorTemp(last24hAverage + correction);
            temperatures.get(i).setRoomTemp(roomTemp);
            outdoorTemp = temperatures.get(i).getOutdoorTemp();
            if (outdoorTemp != null) {
                roomTemp = tau(roomTemp);
            }
        }
    }
}
