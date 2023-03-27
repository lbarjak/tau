package eu.barjak.tau;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Temperature {

    private String date;
    private String time;
    private String day;
    private Double outdoorTemp;
    private Double roomTemp;

}
