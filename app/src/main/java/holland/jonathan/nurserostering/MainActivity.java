package holland.jonathan.nurserostering;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import holland.jonathan.nurserostering.db.DecoratedDateDatabase;
import holland.jonathan.nurserostering.db.entity.DecoratedDate;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MaterialCalendarView calendar;
    int currentColour = Color.GREEN;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;

    private static final String NIGHT_SHIFT_LABEL = "Night Shift";
    private static final String DAY_SHIFT_LABEL = "Long Day Shift";
    private static final String SHORT_DAY_SHIFT_LABEL = "Short Day Shift";
    private static final String RDO_LABEL = "RDO";
    private static final String DB_NAME = "Rostering";
    private static Map<String, Integer> colourMap = new HashMap<>();

    private ListView listView;
    private DecoratedDateDatabase db;
    private MediatorLiveData<List<DecoratedDate>> observableDecoratedDates;
    private DateColourDecorator nightShiftDecorator;
    private DateColourDecorator dayShiftDecorator;
    private DateColourDecorator shortDayShiftDecorator;
    private DateColourDecorator rdoDecorator;
    private Map<Integer, DateColourDecorator> decoratorMap = new HashMap<>();
    private List<DecoratedDate> nightShiftDecoratedDates;
    private List<DecoratedDate> dayShiftDecoratedDates;
    private List<DecoratedDate> shortDayShiftDecoratedDates;
    private List<DecoratedDate> rdoDecoratedDates;
    private Map<Integer, List<DecoratedDate>> decoratedDatesMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = this.findViewById(R.id.calendarView);
        setupAppMaps();

        db = Room.databaseBuilder(getApplicationContext(),
                DecoratedDateDatabase.class, DB_NAME).build();
        observableDecoratedDates = new MediatorLiveData<>();
        final LiveData<List<DecoratedDate>> liveDecoratedDataData = db.decoratedDateDAO().getAll();
        observableDecoratedDates.addSource(liveDecoratedDataData, new Observer() {
            @Override
            public void onChanged(@Nullable Object o) {
                if (db.getDatabaseCreated().getValue() != null) {
                    observableDecoratedDates.postValue((List<DecoratedDate>) liveDecoratedDataData);
                }
            }
        });

        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                decoratorMap.get(currentColour).addDate(date);
                DecoratedDate newDecoratedDate = new DecoratedDate();
                newDecoratedDate.setDate(date);
                newDecoratedDate.setColour(currentColour);
                decoratedDatesMap.get(currentColour).add(newDecoratedDate);
                observableDecoratedDates.setValue(decoratedDatesMap.get(currentColour));
            }
        });

        listView = this.findViewById(R.id.rosteredEventView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemAtPosition = (String) listView.getItemAtPosition(position);
                currentColour = colourMap.get(itemAtPosition);
            }
        });

        listItems.add(NIGHT_SHIFT_LABEL);
        listItems.add(DAY_SHIFT_LABEL);
        listItems.add(SHORT_DAY_SHIFT_LABEL);
        listItems.add(RDO_LABEL);
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);
    }

    @Override
    public void onClick(View v) { }

    protected ListView getListView() {
        if (this.listView == null) {
            this.listView = findViewById(R.id.rosteredEventView);
        }
        return this.listView;
    }

    protected void setListAdapter(ListAdapter adapter) {
        getListView().setAdapter(adapter);
    }

    protected ListAdapter getListAdapter() {
        return getListView().getAdapter();
    }

    protected LiveData<List<DecoratedDate>> getDecoratedDates() {
        return observableDecoratedDates;
    }

    private void setupAppMaps() {
        colourMap.put(NIGHT_SHIFT_LABEL, Color.MAGENTA);
        colourMap.put(DAY_SHIFT_LABEL, Color.GREEN);
        colourMap.put(SHORT_DAY_SHIFT_LABEL, Color.CYAN);
        colourMap.put(RDO_LABEL, Color.WHITE);

        // add all 4 colour decorators to the calendar at the beginning and then only add dates to each
        nightShiftDecorator = new DateColourDecorator(colourMap.get(NIGHT_SHIFT_LABEL), new ArrayList<CalendarDay>());
        calendar.addDecorator(nightShiftDecorator);
        decoratorMap.put(colourMap.get(NIGHT_SHIFT_LABEL), nightShiftDecorator);
        decoratedDatesMap.put(colourMap.get(NIGHT_SHIFT_LABEL), new ArrayList<DecoratedDate>());

        dayShiftDecorator = new DateColourDecorator(colourMap.get(DAY_SHIFT_LABEL), new ArrayList<CalendarDay>());
        calendar.addDecorator(dayShiftDecorator);
        decoratorMap.put(colourMap.get(DAY_SHIFT_LABEL), dayShiftDecorator);
        decoratedDatesMap.put(colourMap.get(DAY_SHIFT_LABEL), new ArrayList<DecoratedDate>());

        shortDayShiftDecorator = new DateColourDecorator(colourMap.get(SHORT_DAY_SHIFT_LABEL), new ArrayList<CalendarDay>());
        calendar.addDecorator(shortDayShiftDecorator);
        decoratorMap.put(colourMap.get(SHORT_DAY_SHIFT_LABEL), shortDayShiftDecorator);
        decoratedDatesMap.put(colourMap.get(SHORT_DAY_SHIFT_LABEL), new ArrayList<DecoratedDate>());

        rdoDecorator = new DateColourDecorator(colourMap.get(RDO_LABEL), new ArrayList<CalendarDay>());
        calendar.addDecorator(rdoDecorator);
        decoratorMap.put(colourMap.get(RDO_LABEL), rdoDecorator);
        decoratedDatesMap.put(colourMap.get(RDO_LABEL), new ArrayList<DecoratedDate>());
    }

}
