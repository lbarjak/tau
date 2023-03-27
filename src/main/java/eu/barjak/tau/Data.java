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
    private String startDate = "2023.03.18.";
    private String endDate = "2023.03.26.";
    private String startTime = "16:50";
    private Double initRoomTemp = 18d;
    private int omszId = 590; // MartonOMSZ 590, MartonBambi 444, LagymanyosOMSZ 615
    private LinkedHashMap<LocalDate, ArrayList<Temperature>> temperaturesMap = new LinkedHashMap<>();
    private ArrayList<Temperature> temperatures = new ArrayList<>();
    private int indexOfMeasuredTemperatures;

    //Ezt a rengeteg triviális gettert lombok-al meg lehet spórolni.
    public int getThermalTimeConstant() {
        return this.thermalTimeConstant;
    }

    public void setThermalTimeConstant(int thermalTimeConstant) {
        System.out.println("in Data set termikus időállandó: " + thermalTimeConstant);
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

}
