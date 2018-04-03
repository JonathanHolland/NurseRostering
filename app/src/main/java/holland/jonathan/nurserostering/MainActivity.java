package holland.jonathan.nurserostering;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    CalendarView calendar;
    ArrayList<String> listItems = new ArrayList<String>();
    ArrayAdapter<String> adapter;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendar = (CalendarView) this.findViewById(R.id.calendarView);
        listView = (ListView) this.findViewById(R.id.rosteredEventView);

        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                listItems);
        setListAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

    }

    protected ListView getListView() {
        if (this.listView == null) {
            this.listView = (ListView) findViewById(R.id.rosteredEventView);
        }
        return this.listView;
    }

    protected void setListAdapter(ListAdapter adapter) {
        getListView().setAdapter(adapter);
    }

    protected ListAdapter getListAdapter() {
        ListAdapter adapter = getListView().getAdapter();
        return adapter;
    }
}
