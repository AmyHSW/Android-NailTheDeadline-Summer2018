package edu.neu.madcourse.shuwanhuang.nailthedeadline.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import edu.neu.madcourse.shuwanhuang.nailthedeadline.object.Cat;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.object.Task;

public class DatabaseUtil {

    private static final String PREF_NAME = "NailTheDeadline";
    private static final String PREF_KEY_ONGOING_TASKS = "OngoingTasks";
    private static final String PREF_KEY_PAST_TASKS = "PastTasks";
    private static final String PREF_KEY_CATS_ID = "CatsID";

    private static ArrayList<Task> readTasksFromDB(Context context, String key) {
        ArrayList<Task> tasks = new ArrayList<>();
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String taskData = pref.getString(key, null);
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

    public static ArrayList<Task> readOngoingTasksFromDB(Context context) {
        return readTasksFromDB(context, PREF_KEY_ONGOING_TASKS);
    }

    public static ArrayList<Task> readHistoryTasksFromDB(Context context) {
        return readTasksFromDB(context, PREF_KEY_PAST_TASKS);
    }

    public static void writeOngoingTasksToDB(Context context, List<Task> tasks) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String data = null;
        for (Task t: tasks) {
            if (data == null) {
                data = t.toString();
            } else {
                data = data + '\n' + t.toString();
            }
        }
        pref.edit().putString(PREF_KEY_ONGOING_TASKS, data).apply();
    }

    public static void addNewTaskToDB(Context context, Task task) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String data = pref.getString(PREF_KEY_ONGOING_TASKS, null);
        if (data == null) {
            data = task.toString();
        } else {
            data = task.toString() + '\n' + data;
        }
        pref.edit().putString(PREF_KEY_ONGOING_TASKS, data).apply();
    }

    public static void addPastTasksToDB(Context context, List<Task> tasks) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String data = pref.getString(PREF_KEY_PAST_TASKS, null);
        for (Task task : tasks) {
            if (data == null) {
                data = task.toString();
            } else {
                data = task.toString() + '\n' + data;
            }
        }
        pref.edit().putString(PREF_KEY_PAST_TASKS, data).apply();
    }

    public static Set<Cat> readCollectedCatsFromDB(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String catData = pref.getString(PREF_KEY_CATS_ID, null);
        Set<Cat> cats = new HashSet<>();
        if (catData != null) {
            String[] tokens = catData.split("\n");
            for (String token: tokens) {
                token = token.trim();
                cats.add(new Cat(Integer.valueOf(token)));
            }
        }
        return cats;
    }

    public static void addCatToDB(Context context, Cat cat) {
        if (cat.getID() == 0) return;
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        String catData = pref.getString(PREF_KEY_CATS_ID, null);
        if (catData == null) {
            catData = cat.getID() + "";
        } else {
            catData = cat.getID() + "\n" + catData;
        }
        pref.edit().putString(PREF_KEY_CATS_ID, catData).apply();
    }
}
