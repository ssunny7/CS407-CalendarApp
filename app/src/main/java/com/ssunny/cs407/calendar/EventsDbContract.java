package com.ssunny.cs407.calendar;

import android.provider.BaseColumns;

/**
 * Created by ssunny7 on 3/3/2016.
 */
public class EventsDbContract {
    public EventsDbContract() {}

    public static final String EVENTSTABLE_CREATE = "CREATE TABLE " + EventsTable.TABLE_NAME + "(" +
                                                    EventsTable._ID + " INTEGER PRiMARY KEY," +
                                                    EventsTable.COLUMN_YEAR + " INTEGER " +
                                                    EventsTable.COLUMN_MONTH + " INTEGER " +
                                                    EventsTable.COLUMN_DOM + " INTEGER " +
                                                    EventsTable.COLUMN_TITLE + " INTEGER " +
                                                    EventsTable.COLUMN_HOUR + " INTEGER " +
                                                    EventsTable.COLUMN_YEAR + " INTEGER " +
                                                    EventsTable.COLUMN_MINUTE + " INTEGER " + " )";

    public static final String EVENTSTABLE_DELETE = "DROP TABLE IF EXISTS " + EventsTable.TABLE_NAME;


    public static abstract class EventsTable implements BaseColumns {
        public static final String TABLE_NAME = "events";
        public static final String COLUMN_YEAR = "year";
        public static final String COLUMN_MONTH = "month";
        public static final String COLUMN_DOM = "dom";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_HOUR = "hour";
        public static final String COLUMN_MINUTE = "minute";
    }
}
