package edu.neu.madcourse.shuwanhuang.nailthedeadline.activity;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.util.Locale;

import edu.neu.madcourse.shuwanhuang.nailthedeadline.fragment.DatePickerFragment;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.R;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.object.Task;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.fragment.TimePickerFragment;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.util.DatabaseUtil;

public class CreateTaskActivity extends AppCompatActivity {

    private static final String WARNING_DEADLINE = "Please select valid date and time for deadline.";
    private static final String WARNING_NAME = "Please enter a task name with only letters, "
        + "numbers, underscores and/or dashes.";

    private int year = -1;
    private int month = -1;
    private int day = -1;
    private int hour = -1;
    private int minute = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        if (hour != -1) {
            Bundle args = new Bundle();
            args.putInt(TimePickerFragment.HOUR, hour);
            args.putInt(TimePickerFragment.MINUTE, minute);
            newFragment.setArguments(args);
        }
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        if (year != -1) {
            Bundle args = new Bundle();
            args.putInt(DatePickerFragment.YEAR, year);
            args.putInt(DatePickerFragment.MONTH, month);
            args.putInt(DatePickerFragment.DAY, day);
            newFragment.setArguments(args);
        }
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onSelectDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;

        // Create a Date variable/object with user chosen date
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(0);
        cal.set(year, month, day, 0, 0, 0);
        Date chosenDate = cal.getTime();

        // Format the date using style medium and US locale
        DateFormat df_medium_us = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.US);
        String df_medium_us_str = df_medium_us.format(chosenDate);

        // Display the formatted date
        TextView dateText = findViewById(R.id.date_text);
        dateText.setText(df_medium_us_str);
        dateText.setTextColor(getResources().getColor(android.R.color.holo_blue_dark));
    }

    public void onSelectTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
        TextView timeText = findViewById(R.id.time_text);
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm aa", Locale.US);
        Calendar calendar = Calendar.getInstance();
        calendar.set(0, 0, 0, hour, minute, 0);
        Date time =calendar.getTime();
        String timeStr = dateFormat.format(time);
        timeText.setText(timeStr);
        timeText.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
    }

    /**
     * Called when the user taps the Create Task button.
     * @param view the View object that was clicked
     */
    public void onClickCreateTask(View view) {
        String warning = "";
        if (!dueDateIsValid()) {
            warning += WARNING_DEADLINE;
        }
        EditText nameText = findViewById(R.id.taskNameText);
        String name = nameText.getText().toString();
        if (!nameIsValid(name)) {
            warning += " " + WARNING_NAME;
        }
        if (warning.length() > 0) {
            Toast.makeText(this, warning, Toast.LENGTH_LONG).show();
            return;
        }
        Task task = Task.create(name, year, month, day, hour, minute);
        DatabaseUtil.addNewTaskToDB(this, task);
        finish();
    }

    private boolean dueDateIsValid() {
        Calendar cal = Calendar.getInstance();
        Date now = cal.getTime();
        cal.setTimeInMillis(0);
        cal.set(year, month, day, hour, minute, 0);
        return cal.getTime().after(now);
    }

    private boolean nameIsValid(String name) {
        return name != null && !name.equals("") && name.matches("[a-zA-Z0-9_-]*");
    }

    /**
     * Called when the user taps the Cancel button.
     * @param view the View object that was clicked
     */
    public void onClickCancel(View view) {
        finish();
    }
}
