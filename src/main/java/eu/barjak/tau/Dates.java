package eu.barjak.tau;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

//Ha valamilyen lineárisabb időreprezentációt használsz (pl. unixtime), akkor ennek a mágiának
// a nagy része megspórólható, a UI-ban kezelhető.
public class Dates {

    private final ArrayList<LocalDate> localdates = new ArrayList<>();
    private LinkedHashMap<LocalDate, ArrayList<Temperature>> temperaturesMap;

    Dates(LinkedHashMap<LocalDate, ArrayList<Temperature>> temperaturesMap) {
        this.temperaturesMap = temperaturesMap;
    }

    public void elapsedDays(LocalDate startDate, LocalDate endDate) throws ParseException {
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

    public void addTimes(LocalDate actualDate) throws ParseException {
        String initTime = "23:50";
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date d = sdf.parse(initTime);
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        for (int i = 0; i < 144; i++) {
            temperaturesMap.get(actualDate).add(new Temperature());
            cal.add(Calendar.MINUTE, 10);
            String newTime = sdf.format(cal.getTime());
            temperaturesMap.get(actualDate).get(i).setTime(newTime);
        }
    }
}
