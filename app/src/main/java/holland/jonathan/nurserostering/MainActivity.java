package holland.jonathan.nurserostering;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    MaterialCalendarView calendar;
    int currentColour = Color.RED;
    ArrayList<String> listItems = new ArrayList<>();
    ArrayAdapter<String> adapter;

    private static final String NIGHT_SHIFT_LABEL = "Night Shift";
    private static final String DAY_SHIFT_LABEL = "Long Day Shift";
    private static final String SHORT_DAY_SHIFT_LABEL = "Short Day Shift";
    private static final String RDO_LABEL = "RDO";
    private static Map<String, Integer> colourMap = new HashMap<>();

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendar = this.findViewById(R.id.calendarView);

        calendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                DateColourDecorator decorator = new DateColourDecorator(currentColour, calendar.getSelectedDates());
                calendar.addDecorator(decorator);
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

        colourMap.put(NIGHT_SHIFT_LABEL, Color.MAGENTA);
        colourMap.put(DAY_SHIFT_LABEL, Color.GREEN);
        colourMap.put(SHORT_DAY_SHIFT_LABEL, Color.CYAN);
        colourMap.put(RDO_LABEL, Color.WHITE);

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
}
