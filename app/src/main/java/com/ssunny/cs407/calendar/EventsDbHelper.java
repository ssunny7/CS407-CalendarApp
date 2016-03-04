package com.ssunny.cs407.calendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cygnus on 3/3/2016.
 */
public class EventsDbHelper extends SQLiteOpenHelper {
    public static final int DB_VERSION = 1;
    public static final String DB_NAME = "events.db";

    public EventsDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EventsDbContract.EVENTSTABLE_CREATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(newVersion > oldVersion) {
            db.execSQL(EventsDbContract.EVENTSTABLE_DELETE);
            onCreate(db);
        }
    }
}
