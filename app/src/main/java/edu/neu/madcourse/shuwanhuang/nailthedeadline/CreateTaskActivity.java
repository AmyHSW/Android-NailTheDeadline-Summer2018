package edu.neu.madcourse.shuwanhuang.nailthedeadline;

import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CreateTaskActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Called when the user taps the Cancel button.
     * @param view the View object that was clicked
     */
    public void onClickCancel(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Called when the user taps the Create Task button.
     * @param view the View object that was clicked
     */
    public void onClickCreateTask(View view) {
        // TODO add new task to the list
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
