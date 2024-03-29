package edu.neu.madcourse.shuwanhuang.nailthedeadline.fragment;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

import edu.neu.madcourse.shuwanhuang.nailthedeadline.activity.CreateTaskActivity;

public class TimePickerFragment extends DialogFragment
    implements TimePickerDialog.OnTimeSetListener {

    public static final String HOUR = "hour";
    public static final String MINUTE = "minute";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int hour, minute;
        Bundle args = getArguments();
        if (args != null) {
            hour = args.getInt(HOUR);
            minute = args.getInt(MINUTE);
        } else {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);
        }

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute,
                DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        ((CreateTaskActivity) getActivity()).onSelectTime(hourOfDay, minute);
    }
}
