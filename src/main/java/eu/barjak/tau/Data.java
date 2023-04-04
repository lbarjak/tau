package eu.barjak.tau;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Data {

    private int thermalTimeConstant = 50;
    private Double initRoomTemp = 20d;
    private int omszId = 590;
    private String startDate = "2023.04.02.";
    private String startTime = "14:15";
    private String endDate = "2023.04.09.";
    private int correction = 0;

    private List<Temperature> temperatures = new ArrayList<>();
    private int indexOfMeasuredTemperatures;

}
