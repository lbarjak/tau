package eu.barjak.tau;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Data {

    private static Data instance = null;

    private Data() {
    }

    public static Data getInstance() {
        if (instance == null)
            instance = new Data();

        return instance;
    }

    private int thermalTimeConstant = 90;// min. 3.3 max. 122
    private String startDateString = "2023.03.18.";
    private String endDateString = "2023.03.26.";
    private String startTimeString = "16:50";
    private Double initRoomTemp = 18d;
    private int omszId = 590; // MartonOMSZ 590, MartonBambi 444, LagymanyosOMSZ 615
    private LinkedHashMap<LocalDate, ArrayList<Temperature>> temperaturesMap = new LinkedHashMap<>();
    private ArrayList<Temperature> temperatures = new ArrayList<>();
    private int indexOfMeasuredTemperatures;

    public int getThermalTimeConstant() {
        return this.thermalTimeConstant;
    }

    public void setThermalTimeConstant(int thermalTimeConstant) {
        this.thermalTimeConstant = thermalTimeConstant;
    }

    public String getStartDateString() {
        return this.startDateString;
    }

    public void setStartDateString(String startDateString) {
        this.startDateString = startDateString;
    }

    public String getEndDateString() {
        return this.endDateString;
    }

    public void setEndDateString(String endDateString) {
        this.endDateString = endDateString;
    }

    public String getStartTimeString() {
        return this.startTimeString;
    }

    public void setStartTimeString(String startTimeString) {
        this.startTimeString = startTimeString;
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

    public LinkedHashMap<LocalDate, ArrayList<Temperature>> getTemperaturesMap() {
        return this.temperaturesMap;
    }

    public void setTemperaturesMap(LinkedHashMap<LocalDate, ArrayList<Temperature>> temperaturesMap) {
        this.temperaturesMap = temperaturesMap;
    }

    public ArrayList<Temperature> getTemperatures() {
        return this.temperatures;
    }

    public void setTemperatures(ArrayList<Temperature> temperatures) {
        this.temperatures = temperatures;
    }

    public int getIndexOfMeasuredTemperatures() {
        return this.indexOfMeasuredTemperatures;
    }

    public void setIndexOfMeasuredTemperatures(int indexOfMeasuredTemperatures) {
        this.indexOfMeasuredTemperatures = indexOfMeasuredTemperatures;
    }

    public void clearTemperatures() {
        this.temperatures.clear();
    }

}
