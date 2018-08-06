package edu.neu.madcourse.shuwanhuang.nailthedeadline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Date;

public class TaskDisplayActivity extends AppCompatActivity {

    private static final String EXTRA_NAME = "TASK_POSITION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_display);
        initTaskView();
    }

    private void initTaskView() {
        int position = Integer.valueOf(getIntent().getStringExtra(EXTRA_NAME));
        // TODO Get task by position
        Task task = Task.create("task1", 2018, 12, 20, 10, 10);

        TextView name = (TextView) findViewById(R.id.task_name);
        name.setText(task.getTaskName());

        TextView deadline = (TextView) findViewById(R.id.deadline);
        Date dueDate = task.getDueDate();
        Time dueTime = task.getDueTime();
        String ddlText = "Due "
                + DateFormat.getDateInstance(DateFormat.MEDIUM).format(dueDate)
                + " " + dueTime.toString();
        deadline.setText(ddlText);
    }

    /**
     * Called when the user taps the Start button.
     * @param view the View object that was clicked
     */
    public void onClickStartWorking(View view) {
        // TODO Hide the Start Working btn, show the timer, start timing and detecting usage
        // TODO of other apps
        Button btn = (Button) findViewById(R.id.start_task_btn);
        btn.setVisibility(View.INVISIBLE);
    }
}
