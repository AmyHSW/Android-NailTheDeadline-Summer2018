package edu.neu.madcourse.shuwanhuang.nailthedeadline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String EXTRA_NAME = "TASK_POSITION";

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
        initTaskListView();
    }

    private void initTaskListView() {
        // TODO get task list from database;
        // TODO only display tasks that have not passed deadline
        // TODO for task that has passed deadline, collect cat for the task
        final ArrayList<Task> taskList = new ArrayList<>();
        taskList.add(Task.create("task1", 2018, 12, 20, 10, 10));
        ArrayAdapter<Task> adapter = new TasksAdapter(this, taskList);
        ListView listView = (ListView) findViewById(R.id.task_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                Intent intent = new Intent(MainActivity.this, TaskDisplayActivity.class);
                intent.putExtra(EXTRA_NAME, Integer.toString(position));
                startActivityForResult(intent, 1000);
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

