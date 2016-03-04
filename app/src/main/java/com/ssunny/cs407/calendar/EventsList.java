package com.ssunny.cs407.calendar;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class EventsList extends AppCompatActivity implements NewEventDialog.NewEventAction {

    private RecyclerView recyclerView;
    private EventsListAdapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;
    private ImageButton addEvent;
    private TextView emptyView;

    private int year, month, dom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events_list);

        Bundle bundle = getIntent().getExtras();
        year = bundle.getInt("YEAR");
        month = bundle.getInt("MONTH");
        dom = bundle.getInt("DOM");

        addEvent = (ImageButton)findViewById(R.id.btnAddEvent);
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                NewEventDialog newEventDialog = new NewEventDialog();
                newEventDialog.setDate(year, month, dom);
                newEventDialog.show(fragmentManager, "New Event");
            }
        });

        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        emptyView = (TextView)findViewById(R.id.emptyTextView);

        ArrayList<EventDetails> events = new ArrayList<EventDetails>();
        if(events == null || events.size() == 0) {
            recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }

        rvAdapter = new EventsListAdapter(events);
        recyclerView.setAdapter(rvAdapter);

        rvLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rvLayoutManager);
    }

    public void added(EventDetails eventDetails) {
        if(eventDetails != null) {
            rvAdapter.addEvent(eventDetails);
            rvAdapter.notifyDataSetChanged();

            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}
