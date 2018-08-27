package holland.jonathan.nurserostering.db.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.List;

import holland.jonathan.nurserostering.db.entity.DecoratedDate;

@Dao
public interface DecoratedDateDAO {
    @Query("SELECT * FROM DecoratedDate")
    LiveData<List<DecoratedDate>> getAll();

    @Query("SELECT * FROM DecoratedDate WHERE date = :date")
    List<DecoratedDate> findByDate(CalendarDay date);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<DecoratedDate> decoratedDates);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(DecoratedDate decoratedDate);

    @Delete
    void delete(DecoratedDate decoratedDate);
}
