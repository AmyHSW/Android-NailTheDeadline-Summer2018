package edu.neu.madcourse.shuwanhuang.nailthedeadline.activity;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.neu.madcourse.shuwanhuang.nailthedeadline.util.DatabaseUtil;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.notification.EndTimerReceiver;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.R;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.object.Task;

public class TaskDisplayActivity extends AppCompatActivity {

    private static final int REQUEST_ACCESS_FINE_LOCATION = 1;
    // Here we set 1 real-world second to 10 minutes in program to make testing easier
    private static final int A_MINUTE_IN_MILLIS = 100;
    // TODO: uncomment the following line for the real app
    // private static final int A_MINUTE_IN_MILLIS = 1000 * 60;
    private static final int AN_HOUR_IN_MILLIS = A_MINUTE_IN_MILLIS * 60;


    private PowerManager.WakeLock wakeLock;

    private Task task;
    private boolean running = false;
    private boolean locationSet = false;
    private LocationListener ll = new LocationListener(){
        @Override
        public void onLocationChanged(Location loc) {
            if (loc == null) return;
            if (locationSet) {
                Toast.makeText(TaskDisplayActivity.this, "You changed your location!",
                        Toast.LENGTH_SHORT).show();
                stopWorking();
                finish();
            } else {
                locationSet = true;
            }
        }

        @Override
        public void onProviderDisabled(String provider) {}

        @Override
        public void onProviderEnabled(String provider) {}

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    private long startTime;
    Handler timerHandler = new Handler();
    Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long currentTime = System.currentTimeMillis();
            if (currentTime >= task.getDueDate().getTime()) {
                Toast.makeText(TaskDisplayActivity.this, "Time is up!",
                        Toast.LENGTH_SHORT).show();
                stopWorking();
                finish();
                return;
            }
            long millis = currentTime - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds %= 60;
            int hours = minutes / 60;
            minutes %= 60;

            TextView stopwatch = findViewById(R.id.stopwatch);
            stopwatch.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            stopwatch.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

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
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        wakeLock.acquire();
        initTaskView();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wakeLock.release();
        if (running) {
            stopWorking();
        }
    }

    private void initTaskView() {
        task = Task.fromString(getIntent().getStringExtra(MainActivity.EXTRA_NAME));

        TextView name = (TextView) findViewById(R.id.task_name);
        name.setText(task.getTaskName());

        Date dueDateTime = task.getDueDate();
        TextView dueDate = (TextView) findViewById(R.id.due_date);
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
        running = true;
        findViewById(R.id.start_task_btn).setVisibility(View.INVISIBLE);
        findViewById(R.id.stop_task_btn).setVisibility(View.VISIBLE);

        scheduleLocationListener();
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 0);

        scheduleNotificationAlarm("You have worked on " + task.getTaskName() + " for an hour!",
                AN_HOUR_IN_MILLIS);
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
        TextView stopwatch = (TextView) findViewById(R.id.stopwatch);
        stopwatch.setText("00:00:00");
        stopwatch.setTextColor(getResources().getColor(android.R.color.darker_gray));
        findViewById(R.id.start_task_btn).setVisibility(View.VISIBLE);
        findViewById(R.id.stop_task_btn).setVisibility(View.INVISIBLE);
        timerHandler.removeCallbacks(timerRunnable);
        addTaskTime((int) ((System.currentTimeMillis() - startTime) / A_MINUTE_IN_MILLIS));
        ((LocationManager) getSystemService(Context.LOCATION_SERVICE)).removeUpdates(ll);
        locationSet = false;
        cancelNotificationAlarm();
    }

    private void addTaskTime(int timeSpentInMinute) {
        if (timeSpentInMinute <= 0) return;
        List<Task> tasks = DatabaseUtil.readOngoingTasksFromDB(this);
        for (Task t: tasks) {
            if (t.getTaskId().equals(task.getTaskId())) {
                t.addWorkedOnInMinute(timeSpentInMinute);
                break;
            }
        }
        DatabaseUtil.writeOngoingTasksToDB(this, tasks);
    }

    private void scheduleLocationListener() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_ACCESS_FINE_LOCATION);
            finish();
        } else {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            // Get the criteria you would like to use
            Criteria criteria = new Criteria();
            criteria.setPowerRequirement(Criteria.POWER_LOW);
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            criteria.setAltitudeRequired(false);
            criteria.setBearingRequired(false);
            criteria.setCostAllowed(false);
            criteria.setSpeedRequired(false);
            // Get the best provider from the criteria specified, and false to say it can turn the provider on if it isn't already
            assert lm != null;
            String bestProvider = lm.getBestProvider(criteria, true);
            // Request location updates
            lm.requestLocationUpdates(bestProvider, 5000, 20, ll);
        }
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
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, EndTimerReceiver.CHANNEL_ID);
        return builder.setContentTitle("Nail the deadline")
                .setContentText(content)
                .setSmallIcon(R.drawable.ic_stat_name)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .build();
    }

    private void cancelNotificationAlarm() {
        Intent intent = new Intent(this, EndTimerReceiver.class);
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.cancel(pendingIntent);
    }
}
