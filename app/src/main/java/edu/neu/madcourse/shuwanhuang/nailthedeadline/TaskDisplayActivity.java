package edu.neu.madcourse.shuwanhuang.nailthedeadline;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class TaskDisplayActivity extends AppCompatActivity {

    private static final int AN_HOUR_IN_MILLIS = 10000; //TODO: change it

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_display);
        initTaskView();
    }

    private void initTaskView() {
        task = Task.fromString(getIntent().getStringExtra(MainActivity.EXTRA_NAME));

        TextView name = (TextView) findViewById(R.id.task_name);
        name.setText(task.getTaskName());

        TextView deadline = (TextView) findViewById(R.id.deadline);
        String ddlText = "Due " + task.getDueDate().toString();
        deadline.setText(ddlText);
    }

    /**
     * Called when the user taps the Start button.
     * @param view the View object that was clicked
     */
    public void onClickStartWorking(View view) {
        // TODO: Hide the btn, show the timer, start timing and detecting usage of other apps
        Button btn = (Button) findViewById(R.id.start_task_btn);
        btn.setVisibility(View.INVISIBLE);
        scheduleNotification("You have worked on " + task.getTaskName() + " for an hour!",
                AN_HOUR_IN_MILLIS);
    }

    private void scheduleNotification(String content, int delayInMillis) {
        Intent intent = new Intent(this, EndTimerReceiver.class);
        intent.putExtra(EndTimerReceiver.NOTIFICATION_ID, 1);
        intent.putExtra(EndTimerReceiver.NOTIFICATION, getNotification(content));
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delayInMillis;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        return builder.setContentTitle("Nail the deadline")
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_stat_name)
                .build();
    }
}
