package com.ssunny.cs407.calendar;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

public class EventsList extends AppCompatActivity implements NewEventDialog.NewEventAction {

    private RecyclerView recyclerView;
    private EventsListAdapter rvAdapter;
    private RecyclerView.LayoutManager rvLayoutManager;
    private ImageButton addEvent;
    private TextView emptyView;
    private EventsDbHelper dbHelper;
    private ArrayList<EventDetails> origEventList;

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
        dbHelper = new EventsDbHelper(getApplicationContext());

        rvLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(rvLayoutManager);
    }

    @Override
    protected void onResume() {
        DbReader dbReader = new DbReader();
        dbReader.execute();

        super.onResume();
    }

    @Override
    protected void onPause() {
        DbWriter dbWriter = new DbWriter();
        dbWriter.execute();

        super.onPause();
    }

    public void added(EventDetails eventDetails) {
        if(eventDetails != null) {
            rvAdapter.addEvent(eventDetails);
            rvAdapter.notifyDataSetChanged();

            recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }

    private class DbReader extends AsyncTask<Void, Void, ArrayList<EventDetails>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(ArrayList<EventDetails> eventDetails) {
            origEventList = eventDetails;
            if(origEventList == null || origEventList.size() == 0) {
                recyclerView.setVisibility(View.GONE);
                emptyView.setVisibility(View.VISIBLE);
            }

            rvAdapter = new EventsListAdapter(origEventList);
            recyclerView.setAdapter(rvAdapter);

            super.onPostExecute(eventDetails);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<EventDetails> doInBackground(Void... params) {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            ArrayList<EventDetails> events = new ArrayList<EventDetails>();

            String[] projection = {EventsDbContract.EventsTable._ID, EventsDbContract.EventsTable.COLUMN_TITLE, EventsDbContract.EventsTable.COLUMN_HOUR, EventsDbContract.EventsTable.COLUMN_MINUTE};
            String selection = EventsDbContract.EventsTable.COLUMN_MONTH + "= ? AND " +
                    EventsDbContract.EventsTable.COLUMN_YEAR + " = ? AND " +
                    EventsDbContract.EventsTable.COLUMN_DOM + " = ?";
            String[] selectionArgs = {Integer.toString(month), Integer.toString(year), Integer.toString(dom)};
            Cursor cursor = db.query(EventsDbContract.EventsTable.TABLE_NAME, projection, selection, selectionArgs, null, null, null);

            cursor.moveToFirst();
            while(!cursor.isAfterLast()) {
                EventDetails eventDetails = new EventDetails();

                eventDetails.setTitle(cursor.getString(cursor.getColumnIndex(EventsDbContract.EventsTable.COLUMN_TITLE)));
                eventDetails.setId(Integer.toString(cursor.getInt(cursor.getColumnIndex(EventsDbContract.EventsTable._ID))));

                Date eventDate = new Date();
                eventDate.setYear(year - 1900);
                eventDate.setMonth(month);
                eventDate.setDate(dom);
                eventDate.setHours(cursor.getInt(cursor.getColumnIndex(EventsDbContract.EventsTable.COLUMN_HOUR)));
                eventDate.setMinutes(cursor.getInt(cursor.getColumnIndex(EventsDbContract.EventsTable.COLUMN_MINUTE)));
                eventDetails.setDate(eventDate);

                Log.d("[DB]", "Got event " + eventDetails.toString());

                events.add(eventDetails);

                cursor.moveToNext();
            }

            return events;
        }
    }

    private class DbWriter extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            ArrayList<EventDetails> curEvents = rvAdapter.getCurrentEvents();
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            for(EventDetails event : curEvents) {
                if(event.getId() == null) {
                    Log.d("[DB]", "Adding event " + event.toString());

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(EventsDbContract.EventsTable.COLUMN_TITLE, event.getTitle());
                    contentValues.put(EventsDbContract.EventsTable.COLUMN_YEAR, event.getDate().getYear() + 1900);
                    contentValues.put(EventsDbContract.EventsTable.COLUMN_MONTH, event.getDate().getMonth());
                    contentValues.put(EventsDbContract.EventsTable.COLUMN_DOM, event.getDate().getDate());
                    contentValues.put(EventsDbContract.EventsTable.COLUMN_MINUTE, event.getDate().getMinutes());
                    contentValues.put(EventsDbContract.EventsTable.COLUMN_HOUR, event.getDate().getHours());

                    long ret = db.insert(EventsDbContract.EventsTable.TABLE_NAME, null, contentValues);
                    if(ret == -1)
                        Log.d("[DB]", "Insert failed on" + event.toString());
                }
            }

            for(EventDetails event : origEventList) {
                boolean found = false;
                for(EventDetails event2 : curEvents) {
                    if(event.getId() == event2.getId()) {
                        found = true;
                        break;
                    }
                }

                if(!found) {
                    String selection = EventsDbContract.EventsTable._ID + " LIKE ?";
                    String[] selectionArgs = {event.getId()};
                    Log.d("[DB]", "Removing event " + event.toString());
                    db.delete(EventsDbContract.EventsTable.TABLE_NAME, selection, selectionArgs);
                }
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}
