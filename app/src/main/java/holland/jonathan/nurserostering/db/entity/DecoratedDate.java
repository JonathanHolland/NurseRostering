package holland.jonathan.nurserostering.db.entity;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.prolificinteractive.materialcalendarview.CalendarDay;

@Entity
public class DecoratedDate {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "date")
    private CalendarDay date;

    @ColumnInfo(name="shift")
    private String shift;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public CalendarDay getDate() {
        return date;
    }

    public void setDate(CalendarDay date) {
        this.date = date;
    }

}
