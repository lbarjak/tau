package eu.barjak.tau;

import java.util.ArrayList;
import java.util.List;

public class Data {

    private int thermalTimeConstant = 50;// min. 3.3 max. 122
    private Double initRoomTemp = 20d;
    private int omszId = 590; // MartonOMSZ 590, MartonBambi 444, LagymanyosOMSZ 615
    private String startDate = "2023.03.26.";
    private String startTime = "16:30";
    private String endDate = "2023.04.01.";
    private int correction = 0;

    private List<Temperature> temperatures = new ArrayList<>();
    private int indexOfMeasuredTemperatures;

    public int getCorrection() {
        return this.correction;
    }

    public void setCorrection(int correction) {
        this.correction = correction;
    }

    public int getThermalTimeConstant() {
        return this.thermalTimeConstant;
    }

    public void setThermalTimeConstant(int thermalTimeConstant) {
        this.thermalTimeConstant = thermalTimeConstant;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public Double getInitRoomTemp() {
        return this.initRoomTemp;
    }

    public void setInitRoomTemp(Double initRoomTemp) {
        this.initRoomTemp = initRoomTemp;
    }

    public int getOmszId() {
        return this.omszId;
    }

    public void setOmszId(int omszId) {
        this.omszId = omszId;
    }

    public List<Temperature> getTemperatures() {
        return this.temperatures;
    }

    public void setTemperatures(List<Temperature> temperatures) {
        this.temperatures = temperatures;
    }

    public int getIndexOfMeasuredTemperatures() {
        return this.indexOfMeasuredTemperatures;
    }

    public void setIndexOfMeasuredTemperatures(int indexOfMeasuredTemperatures) {
        this.indexOfMeasuredTemperatures = indexOfMeasuredTemperatures;
    }

}
