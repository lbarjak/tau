package eu.barjak.tau;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Dates {

    private final List<LocalDate> localdates = new ArrayList<>();
    private Map<LocalDate, List<Temperature>> temperaturesMap;

    Dates(Map<LocalDate, List<Temperature>> temperaturesMap) {
        this.temperaturesMap = temperaturesMap;
    }

    public void elapsedDays(LocalDate startDate, LocalDate endDate) {
        LocalDate tmpDate;
        long elapsed = Period.between(startDate, endDate).getDays();
        for (int i = 0; i <= elapsed; i++) {
            tmpDate = startDate.plus(Period.ofDays(i));
            localdates.add(tmpDate);
        }
        for (LocalDate localDate : localdates) {
            temperaturesMap.put(localDate, new ArrayList<>());
            addTimes(localDate);
        }
    }

    public void addTimes(LocalDate actualDate) {
        LocalTime newTime = LocalTime.parse("23:50");
        for (int i = 0; i < 144; i++) {
            temperaturesMap.get(actualDate).add(new Temperature());
            newTime = newTime.plusMinutes(10);
            temperaturesMap.get(actualDate).get(i).setTime(newTime.toString());
        }
    }

}
