package eu.barjak.tau;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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

}
