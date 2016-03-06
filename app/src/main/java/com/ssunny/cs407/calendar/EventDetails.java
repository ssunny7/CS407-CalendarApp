package com.ssunny.cs407.calendar;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ssunny7 on 3/2/2016.
 */
public class EventDetails {

    public String id = null;
    private String title;
    private Date date;

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy hh:mm a");
        return "\"" + title + " " + sdf.format(date) + "\"";
    }
}
