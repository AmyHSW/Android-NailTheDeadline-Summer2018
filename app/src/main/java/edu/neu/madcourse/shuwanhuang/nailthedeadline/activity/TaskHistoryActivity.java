package edu.neu.madcourse.shuwanhuang.nailthedeadline.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import edu.neu.madcourse.shuwanhuang.nailthedeadline.util.DatabaseUtil;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.R;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.object.Task;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.adapter.TasksAdapter;

public class TaskHistoryActivity extends AppCompatActivity {

    private ArrayList<Task> tasks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_history);

        initTaskHistoryData();
        initTaskHistoryListView();
    }

    private void initTaskHistoryData() {
        tasks.clear();
        tasks = DatabaseUtil.getHistoryTasksFromDB(this);
    }

    private void initTaskHistoryListView() {
        if (tasks.size() > 0) {
            TextView noHistoryTextView = findViewById(R.id.no_history_text);
            noHistoryTextView.setVisibility(View.INVISIBLE);
        }
        ArrayAdapter<Task> adapter = new TasksAdapter(this, tasks, true);

        ListView listView = (ListView) findViewById(R.id.history_list);
        listView.setAdapter(adapter);
    }
}
