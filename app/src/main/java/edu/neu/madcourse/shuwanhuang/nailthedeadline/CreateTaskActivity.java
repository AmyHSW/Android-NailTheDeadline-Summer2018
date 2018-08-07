package edu.neu.madcourse.shuwanhuang.nailthedeadline;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CreateTaskActivity extends AppCompatActivity {

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

    // TODO: update UI after selecting datetime
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void onSelectDate(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void onSelectTime(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    /**
     * Called when the user taps the Create Task button.
     * @param view the View object that was clicked
     */
    public void onClickCreateTask(View view) {
        EditText nameText = findViewById(R.id.taskNameText);
        String name = nameText.getText().toString();
        // TODO: validation for name & datetime > current
        Task task = Task.create(name, year, month, day, hour, minute);
        SharedPreferences pref = getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        String data = pref.getString(MainActivity.PREF_KEY, null);
        if (data == null) {
            data = task.toString();
        } else {
            data = task.toString() + '\n' + data;
        }
        pref.edit().putString(MainActivity.PREF_KEY, data).commit();
        finish();
    }

    /**
     * Called when the user taps the Cancel button.
     * @param view the View object that was clicked
     */
    public void onClickCancel(View view) {
        finish();
    }
}
