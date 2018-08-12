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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaskDisplayActivity extends AppCompatActivity {

    private static final int AN_HOUR_IN_MILLIS = 10000; // TODO: change it

    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_display);
        initTaskView();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initTaskView() {
        task = Task.fromString(getIntent().getStringExtra(MainActivity.EXTRA_NAME));

        TextView name = (TextView) findViewById(R.id.task_name);
        name.setText(task.getTaskName());

        TextView dueDate = (TextView) findViewById(R.id.due_date);
        Date dueDateTime = task.getDueDate();
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE MM'/'dd'/'y", Locale.US);
        String dueDateText = "Due " + dateFormat.format(dueDateTime);
        dueDate.setText(dueDateText);

        TextView dueTime = (TextView) findViewById(R.id.due_time);
        dateFormat.applyPattern("hh:mm aa");
        String dueTimeText = dateFormat.format(dueDateTime);
        dueTime.setText(dueTimeText);

        findViewById(R.id.stop_task_btn).setVisibility(View.INVISIBLE);
    }

    /**
     * Called when the user taps the Start button.
     * @param view the View object that was clicked
     */
    public void onClickStartWorking(View view) {
        // TODO: show the timer, start timing and detecting usage of other apps
        findViewById(R.id.start_task_btn).setVisibility(View.INVISIBLE);
        findViewById(R.id.stop_task_btn).setVisibility(View.VISIBLE);

        scheduleNotificationAlarm("You have worked on " + task.getTaskName() + " for an hour!",
                AN_HOUR_IN_MILLIS);  // TODO: min(1hr, time to ddl)
    }

    /**
     * Called when the user taps the Stop button.
     * @param view the View object that was clicked
     */
    public void onClickStopWorking(View view) {
        findViewById(R.id.start_task_btn).setVisibility(View.VISIBLE);
        findViewById(R.id.stop_task_btn).setVisibility(View.INVISIBLE);

        cancelNotificationAlarm();
    }

    private void scheduleNotificationAlarm(String content, int delayInMillis) {
        Intent intent = new Intent(this, EndTimerReceiver.class);
        intent.putExtra(EndTimerReceiver.NOTIFICATION_ID, 1);
        intent.putExtra(EndTimerReceiver.NOTIFICATION, getNotification(content));
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        long futureInMillis = SystemClock.elapsedRealtime() + delayInMillis;
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, futureInMillis, pendingIntent);
    }

    private Notification getNotification(String content) {
        Notification.Builder builder = new Notification.Builder(this);
        return builder.setContentTitle("Nail the deadline")
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_stat_name)
                .build();
    }

    // TODO: cancel alarm after timeout
    private void cancelNotificationAlarm() {
        Intent intent = new Intent(this, EndTimerReceiver.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
    }
}
