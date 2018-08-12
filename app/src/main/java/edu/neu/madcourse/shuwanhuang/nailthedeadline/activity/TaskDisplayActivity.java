package edu.neu.madcourse.shuwanhuang.nailthedeadline.activity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.neu.madcourse.shuwanhuang.nailthedeadline.util.DatabaseUtil;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.notification.EndTimerReceiver;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.R;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.object.Task;

public class TaskDisplayActivity extends AppCompatActivity {

    private static final int AN_HOUR_IN_MILLIS = 10000; // TODO: change it

    private Task task;
    private boolean running = false;

    private long startTime;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;

            Log.d("log", String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_display);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initTaskView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (running) {
            stopWorking();
        }
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
        running = true;
        findViewById(R.id.start_task_btn).setVisibility(View.INVISIBLE);
        findViewById(R.id.stop_task_btn).setVisibility(View.VISIBLE);
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);

        // scheduleNotificationAlarm("You have worked on " + task.getTaskName() + " for an hour!",
        //         AN_HOUR_IN_MILLIS);  // TODO: min(1hr, time to ddl)
    }

    /**
     * Called when the user taps the Stop button.
     * @param view the View object that was clicked
     */
    public void onClickStopWorking(View view) {
        stopWorking();
    }

    private void stopWorking() {
        running = false;
        findViewById(R.id.start_task_btn).setVisibility(View.VISIBLE);
        findViewById(R.id.stop_task_btn).setVisibility(View.INVISIBLE);
        timerHandler.removeCallbacks(timerRunnable);
        addTaskTime((int) ((System.currentTimeMillis() - startTime) / 1000)); // TODO: change this
        // cancelNotificationAlarm();
    }

    private void addTaskTime(int timeSpentInMinute) {
        if (timeSpentInMinute <= 0) return;
        List<Task> tasks = DatabaseUtil.getAllTasksFromDB(this);
        for (Task t: tasks) {
            if (t.getTaskId().equals(task.getTaskId())) {
                t.addWorkedOnInMinute(timeSpentInMinute);
                break;
            }
        }
        DatabaseUtil.updateTasksToDB(this, tasks);
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
