package edu.neu.madcourse.shuwanhuang.nailthedeadline;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
    implements DatePickerDialog.OnDateSetListener {

    public static final String YEAR = "year";
    public static final String MONTH = "month";
    public static final String DAY = "day";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        int year, month, day;
        Bundle args = getArguments();
        if (args != null) {
            year = args.getInt(YEAR);
            month = args.getInt(MONTH);
            day = args.getInt(DAY);
        } else {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        ((CreateTaskActivity) getActivity()).onSelectDate(year, month, day);
    }
}
