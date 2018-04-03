package holland.jonathan.nurserostering;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    CalendarView calendar;
    ListView rosteredEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        calendar = (CalendarView) this.findViewById(R.id.calendarView);
        rosteredEvents = (ListView) this.findViewById(R.id.rosteredEventsView);
    }

    @Override
    public void onClick(View v) {

    }
}
