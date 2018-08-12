package edu.neu.madcourse.shuwanhuang.nailthedeadline;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static edu.neu.madcourse.shuwanhuang.nailthedeadline.MainActivity.PREF_KEY;
import static edu.neu.madcourse.shuwanhuang.nailthedeadline.MainActivity.PREF_NAME;

public class TaskHistoryActivity extends AppCompatActivity {

    private final ArrayList<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_history);

        initTaskHistoryData();
        initTaskHistoryListView();
    }

    private void initTaskHistoryData() {
        tasks.clear();
        String taskData = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
                .getString(PREF_KEY, null);
        if (taskData != null) {
            String[] tokens = taskData.split("\n");
            Date currentDateTime = Calendar.getInstance().getTime();
            for (String token: tokens) {
                token = token.trim();
                if (!token.equals("")) {
                    Task task = Task.fromString(token);
                    Date dueDateTime = task.getDueDate();
                    if (dueDateTime.before(currentDateTime)) {
                        tasks.add(task);
                    }
                }
            }
        }
    }

    private void initTaskHistoryListView() {
        ArrayAdapter<Task> adapter = new TasksAdapter(this, tasks, true);

        ListView listView = (ListView) findViewById(R.id.history_list);
        listView.setAdapter(adapter);
    }
}
