package com.ssunny.cs407.calendar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by ssunny7 on 3/1/2016.
 */
public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.ViewHolder> implements Comparator<EventDetails> {

    private ArrayList<EventDetails> events = null;

    public EventsListAdapter(ArrayList<EventDetails> _events) {
        events = _events;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View detailsView;

        public ViewHolder(View tv) {
            super(tv);
            detailsView = tv;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder vh, int position) {

        EventDetails curEvent = events.get(position);

        TextView evtDetailsTitle = (TextView)vh.detailsView.findViewById(R.id.evtDetailsTitle);
        evtDetailsTitle.setText(curEvent.getTitle());

        TextView evtDetailsDate = (TextView)vh.detailsView.findViewById(R.id.evtDetailsDate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm a");
        evtDetailsDate.setText(sdf.format(curEvent.getDate()).toString());
    }

    @Override
    public int getItemCount() {
        return events == null?0:events.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                               .inflate(R.layout.view_event_details, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    public void addEvent(EventDetails event) {
        if(events != null) {
            events.add(event);
            Collections.sort(events, this);
        }
    }

    @Override
    public int compare(EventDetails first, EventDetails second) {
        return first.getDate().compareTo(second.getDate());
    }
}

