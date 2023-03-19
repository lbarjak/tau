package eu.barjak.tau;

import java.time.LocalDate;

public class Temperature {

    private LocalDate date;
    private String time;
    private String day;
    private Double outdoorTemp;
    private Double roomTemp;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public Double getOutdoorTemp() {
        return outdoorTemp;
    }

    public void setOutdoorTemp(Double outdoorTemp) {
        this.outdoorTemp = outdoorTemp;
    }

    public Double getRoomTemp() {
        return roomTemp;
    }

    public void setRoomTemp(Double roomTemp) {
        this.roomTemp = roomTemp;
    }
}
