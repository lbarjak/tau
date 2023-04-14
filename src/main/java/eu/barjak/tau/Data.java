package eu.barjak.tau;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Data {

    private int thermalTimeConstant;
    private Double initRoomTemp;
    private int omszId;
    private String startDate;
    private String startTime;
    private String endDate;
    private int correction;
    private String colorIn = "blue";
    private String colorOut = "navy";

    private List<Temperature> temperatures = new ArrayList<>();
    private int indexOfMeasuredTemperatures;
    private int indexOfForecast;

}
