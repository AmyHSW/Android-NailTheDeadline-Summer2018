package edu.neu.madcourse.shuwanhuang.nailthedeadline.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

import edu.neu.madcourse.shuwanhuang.nailthedeadline.util.DatabaseUtil;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.R;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.object.Task;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.adapter.TasksAdapter;

public class MainActivity extends AppCompatActivity {

    public static final String PREF_NAME = "NailTheDeadline";
    public static final String PREF_KEY = "Tasks";
    public static final String EXTRA_NAME = "Task";

    private ArrayList<Task> tasks = new ArrayList<>();

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
        tasks = DatabaseUtil.getOngoingTasksFromDB(this);
        // TODO: for task that has passed deadline, collect cat for the task
    }

    private void initTaskListView() {
        ArrayAdapter<Task> adapter = new TasksAdapter(this, tasks, false);
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

    public void onClickCats(View view) {
        Intent intent = new Intent(this, CatsActivity.class);
        startActivity(intent);
    }

    public void onClickHistory(View view) {
        Intent intent = new Intent(this, TaskHistoryActivity.class);
        startActivity(intent);
    }
}
