package holland.jonathan.nurserostering.db.converters;

import android.arch.persistence.room.TypeConverter;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Date;

public class DateConverters {
    @TypeConverter
    public static CalendarDay fromTimeStamp(Long value) {
        return value == null ? null : CalendarDay.from(new Date(value));
    }

    @TypeConverter
    public static Long toTimeStamp(CalendarDay date) {
        return date.getDate() == null ? null : date.getDate().getTime();
    }
}
