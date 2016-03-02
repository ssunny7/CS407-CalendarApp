package com.ssunny.cs407.calendar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Date;

/**
 * Created by ssunny7 on 3/1/2016.
 */
public class EventsListAdapter extends RecyclerView.Adapter<EventsListAdapter.ViewHolder> {

    private EventDetails[] events = null;

    public EventsListAdapter(EventDetails[] _events) {
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

        EventDetails curEvent = events[position];

        TextView evtDetailsTitle = (TextView)vh.detailsView.findViewById(R.id.evtDetailsTitle);
        evtDetailsTitle.setText(curEvent.getTitle());

        TextView evtDetailsDate = (TextView)vh.detailsView.findViewById(R.id.evtDetailsDate);
        evtDetailsDate.setText(curEvent.getDate().toString());
    }

    @Override
    public int getItemCount() {
        return events == null?0:events.length;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                               .inflate(R.layout.view_event_details, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
}

