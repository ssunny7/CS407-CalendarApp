package com.ssunny.cs407.calendar;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ssunny7 on 3/2/2016.
 */
public class NewEventDialog extends DialogFragment {

    public interface NewEventAction {
        void added(EventDetails eventDetails);
    }

    private NewEventAction callingActivity = null;

    private EditText newEvtTitle;
    private TimePicker newEvtTime;

    private Date evtDate;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.dialog_new_event, null);
        builder.setView(dialogView);

        newEvtTitle = (EditText)dialogView.findViewById(R.id.newEvtTitle);
        newEvtTitle.setText("New Event");

        newEvtTime = (TimePicker)dialogView.findViewById(R.id.newEvtTime);
        newEvtTime.setCurrentHour(Calendar.HOUR);
        newEvtTime.setCurrentMinute(Calendar.MINUTE);

        Button addButton = (Button)dialogView.findViewById(R.id.newEvtAdd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(callingActivity != null) {
                    EventDetails eventDetails = new EventDetails();

                    String title = newEvtTitle.getText().toString();
                    if(title.isEmpty())
                        title = "New Event";
                    eventDetails.setTitle(title);

                    evtDate.setMinutes(newEvtTime.getCurrentMinute());
                    evtDate.setHours(newEvtTime.getCurrentHour());

                    eventDetails.setDate(evtDate);

                    callingActivity.added(eventDetails);
                    dismiss();
                }
            }
        });

        return builder.create();
    }

    public void setDate(int year, int month, int dom) {
        evtDate = new Date();
        evtDate.setYear(year - 1900);
        evtDate.setMonth(month);
        evtDate.setDate(dom);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            callingActivity = (NewEventAction)activity;
        } catch (ClassCastException cce) {
            //
        }
    }
}
