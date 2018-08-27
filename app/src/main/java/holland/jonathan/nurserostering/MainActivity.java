package holland.jonathan.nurserostering;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import holland.jonathan.nurserostering.db.DecoratedDateDatabase;
import holland.jonathan.nurserostering.db.entity.DecoratedDate;
import holland.jonathan.nurserostering.viewmodels.DecoratedDatesViewModel;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String NIGHT_SHIFT_LABEL = "Night Shift";
    private static final String DAY_SHIFT_LABEL = "Long Day Shift";
    private static final String SHORT_DAY_SHIFT_LABEL = "Short Day Shift";
    private static final String RDO_LABEL = "RDO";
    private static final String DB_NAME = "Rostering";
    private static Map<String, Integer> colourMap = new HashMap<>();

    MaterialCalendarView calendar;
    String currentShift = DAY_SHIFT_LABEL;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;

    private ListView listView;
    private DecoratedDateDatabase db;
    private DecoratedDatesViewModel model;
    private DateColourDecorator nightShiftDecorator;
    private DateColourDecorator dayShiftDecorator;
    private DateColourDecorator shortDayShiftDecorator;
    private DateColourDecorator rdoDecorator;
    private Map<String, DateColourDecorator> decoratorMap = new HashMap<>();
    private List<DecoratedDate> decoratedDatesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = this.findViewById(R.id.calendarView);
        setupAppMaps();
        model = ViewModelProviders.of(this).get(DecoratedDatesViewModel.class);

        final Observer<List<DecoratedDate>> decoratedDateObserver = new Observer<List<DecoratedDate>>() {
            @Override
            public void onChanged(@Nullable final List<DecoratedDate> newDecoratedDates) {
                // Update the UI - in this case, the calendar
                // Find the colours for each shift and add them and then invalidate the decorators
                for (DecoratedDate date : newDecoratedDates) {
                    decoratorMap.get(date.getShift()).addDateIfNotPresent(date.getDate());
                }
                calendar.invalidateDecorators();
            }
        };

        model.getCurrentDecoratedDates().observe(this, decoratedDateObserver);

        db = Room.databaseBuilder(getApplicationContext(),
                DecoratedDateDatabase.class, DB_NAME).build();

        // Initialise with existing data
        List<DecoratedDate> startingDecorators = db.decoratedDateDAO().getAll().getValue();
        if (startingDecorators != null) {
            model.getCurrentDecoratedDates().setValue(db.decoratedDateDAO().getAll().getValue());
        }

        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                DecoratedDate newDecoratedDate = new DecoratedDate();
                newDecoratedDate.setDate(date);
                newDecoratedDate.setShift(currentShift);
                decoratedDatesList.add(newDecoratedDate);
                db.decoratedDateDAO().insert(newDecoratedDate);
                model.getCurrentDecoratedDates().setValue(decoratedDatesList);
            }
        });

        listView = this.findViewById(R.id.rosteredEventView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemAtPosition = (String) listView.getItemAtPosition(position);
                currentShift = itemAtPosition;
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

    private void setupAppMaps() {
        colourMap.put(NIGHT_SHIFT_LABEL, Color.MAGENTA);
        colourMap.put(DAY_SHIFT_LABEL, Color.GREEN);
        colourMap.put(SHORT_DAY_SHIFT_LABEL, Color.CYAN);
        colourMap.put(RDO_LABEL, Color.WHITE);

        // add all 4 colour decorators to the calendar at the beginning and then only add dates to each
        nightShiftDecorator = new DateColourDecorator(colourMap.get(NIGHT_SHIFT_LABEL), new ArrayList<CalendarDay>());
        calendar.addDecorator(nightShiftDecorator);
        decoratorMap.put(NIGHT_SHIFT_LABEL, nightShiftDecorator);

        dayShiftDecorator = new DateColourDecorator(colourMap.get(DAY_SHIFT_LABEL), new ArrayList<CalendarDay>());
        calendar.addDecorator(dayShiftDecorator);
        decoratorMap.put(DAY_SHIFT_LABEL, dayShiftDecorator);

        shortDayShiftDecorator = new DateColourDecorator(colourMap.get(SHORT_DAY_SHIFT_LABEL), new ArrayList<CalendarDay>());
        calendar.addDecorator(shortDayShiftDecorator);
        decoratorMap.put(SHORT_DAY_SHIFT_LABEL, shortDayShiftDecorator);

        rdoDecorator = new DateColourDecorator(colourMap.get(RDO_LABEL), new ArrayList<CalendarDay>());
        calendar.addDecorator(rdoDecorator);
        decoratorMap.put(RDO_LABEL, rdoDecorator);
    }

    private static String getKeyFromValue(Map<String, Integer> map, Integer value) {
        for (String key : map.keySet()) {
            Integer keyValue = map.get(key);
            if (keyValue == value) {
                return key;
            }
        }
        return null;
    }

}
