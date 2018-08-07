package edu.neu.madcourse.shuwanhuang.nailthedeadline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String PREF_NAME = "NailTheDeadline";
    public static final String PREF_KEY = "Tasks";
    public static final String EXTRA_NAME = "Task";

    private final ArrayList<Task> tasks = new ArrayList<>();

    // Create a message handling object as an anonymous class.
    private AdapterView.OnItemClickListener mMessageClickedHandler = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView parent, View v, int position, long id) {
            // Do something in response to the click
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        initTaskData();
        initTaskListView();
    }

    private void initTaskData() {
        tasks.clear();
        String taskData = getSharedPreferences(PREF_NAME, MODE_PRIVATE)
                .getString(PREF_KEY, null);
        if (taskData != null) {
            String[] tokens = taskData.split("\n");
            for (String token: tokens) {
                token = token.trim();
                if (!token.equals("")) {
                    Task task = Task.fromString(token);
                    tasks.add(task);
                }
            }
        }
    }

    private void initTaskListView() {
        ArrayAdapter<Task> adapter = new TasksAdapter(this, tasks);
        // TODO: only display tasks that have not passed deadline
        // TODO: for task that has passed deadline, collect cat for the task
        ListView listView = (ListView) findViewById(R.id.task_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TaskDisplayActivity.class);
                intent.putExtra(EXTRA_NAME, tasks.get(position).toString());
                startActivity(intent);
            }
        });
    }

    /**
     * Called when the user taps the New Task button.
     * @param view the View object that was clicked
     */
    public void onClickNewTask(View view) {
        Intent intent = new Intent(this, CreateTaskActivity.class);
        startActivity(intent);
    }
}
