package edu.neu.madcourse.shuwanhuang.nailthedeadline.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.neu.madcourse.shuwanhuang.nailthedeadline.activity.MainActivity;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.object.Task;

public class DatabaseUtil {

    public static List<Task> getAllTasksFromDB(Context context) {
        List<Task> tasks = new ArrayList<>();
        SharedPreferences pref = context.getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        String taskData = pref.getString(MainActivity.PREF_KEY, null);
        if (taskData != null) {
            String[] tokens = taskData.split("\n");
            for (String token: tokens) {
                token = token.trim();
                if (!token.equals("")) {
                    tasks.add(Task.fromString(token));
                }
            }
        }
        return tasks;
    }

    public static ArrayList<Task> getOngoingTasksFromDB(Context context) {
        List<Task> allTasks = getAllTasksFromDB(context);
        ArrayList<Task> results = new ArrayList<>();
        Date currentDateTime = Calendar.getInstance().getTime();
        for (Task task : allTasks) {
            Date dueDateTime = task.getDueDate();
            if (dueDateTime.after(currentDateTime)) {
                results.add(task);
            }
        }
        return results;
    }

    public static ArrayList<Task> getHistoryTasksFromDB(Context context) {
        List<Task> allTasks = getAllTasksFromDB(context);
        ArrayList<Task> results = new ArrayList<>();
        Date currentDateTime = Calendar.getInstance().getTime();
        for (Task task : allTasks) {
            Date dueDateTime = task.getDueDate();
            if (dueDateTime.before(currentDateTime)) {
                results.add(task);
            }
        }
        return results;
    }

    public static void updateTasksToDB(Context context, List<Task> tasks) {
        SharedPreferences pref = context.getSharedPreferences(MainActivity.PREF_NAME, Context.MODE_PRIVATE);
        String data = null;
        for (Task t: tasks) {
            if (data == null) {
                data = t.toString();
            } else {
                data = data + '\n' + t.toString();
            }
        }
        pref.edit().putString(MainActivity.PREF_KEY, data).apply();
    }
}
