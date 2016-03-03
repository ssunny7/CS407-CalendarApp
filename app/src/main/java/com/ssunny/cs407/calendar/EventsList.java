package com.ssunny.cs407.calendar;

import android.graphics.Outline;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

public class EventsList extends AppCompatActivity implements NewEventDialog.NewEventAction {

    private RecyclerView recyclerView;
    private EventsListAdapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;
    private ImageButton addEvent;

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

        rvAdapter = new EventsListAdapter(new ArrayList<EventDetails>());
        recyclerView.setAdapter(rvAdapter);

        rvLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rvLayoutManager);
    }

    public void added(EventDetails eventDetails) {
        if(eventDetails != null) {
            rvAdapter.addEvent(eventDetails);
            rvAdapter.notifyDataSetChanged();
        }
    }
}
