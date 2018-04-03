package holland.jonathan.nurserostering;

import java.io.Serializable;
import java.util.Date;

public class RosteredEvent implements Serializable {

    private Date date;
    private Date startTime;
    private int durationInHours;

    public RosteredEvent(Date newDate, Date startTime, int durationInHours){
        this.date = newDate;
        this.startTime = startTime;
        this.durationInHours = durationInHours;
    }
}