package holland.jonathan.nurserostering.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.concurrent.Executors;

import holland.jonathan.nurserostering.db.converters.DateConverters;
import holland.jonathan.nurserostering.db.entity.DecoratedDate;
import holland.jonathan.nurserostering.db.dao.DecoratedDateDAO;

@Database(entities = {DecoratedDate.class}, version = 1)
@TypeConverters({DateConverters.class})
public abstract class DecoratedDateDatabase extends RoomDatabase {
    private static final String DB_NAME = "Rostering";
    private static DecoratedDateDatabase instance;

    public abstract DecoratedDateDAO decoratedDateDAO();

    private final MutableLiveData<Boolean> isDatabaseCreated = new MutableLiveData<>();

    public static DecoratedDateDatabase getInstance(final Context context) {
        if (instance == null) {
            synchronized (DecoratedDateDatabase.class) {
                if (instance == null) {
                    instance = buildDatabase(context.getApplicationContext());
                    instance.updateDatabaseCreated(context.getApplicationContext());
                }
            }
        }
        return instance;
    }

    private static DecoratedDateDatabase buildDatabase(final Context appContext) {
        return Room.databaseBuilder(appContext, DecoratedDateDatabase.class, DB_NAME)
                .addCallback(new Callback() {
                    @Override
                    public void onCreate(@NonNull SupportSQLiteDatabase db) {
                        super.onCreate(db);
                        DecoratedDateDatabase database = DecoratedDateDatabase.getInstance(appContext);
                        database.setDatabaseCreated();
                    }
                }).build();
    }

    private void updateDatabaseCreated(final Context context) {
        if (context.getDatabasePath(DB_NAME).exists()) {
            setDatabaseCreated();
        }
    }

    private void setDatabaseCreated(){
        isDatabaseCreated.postValue(true);
    }

    public static void insertData(final DecoratedDateDatabase database, final DecoratedDate date) {
        Executors.newSingleThreadExecutor().execute(() -> {
            database.decoratedDateDAO().insert(date);
        });
    }

    public LiveData<Boolean> getDatabaseCreated() {
        return isDatabaseCreated;
    }
}
