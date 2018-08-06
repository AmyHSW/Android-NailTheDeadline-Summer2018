package edu.neu.madcourse.shuwanhuang.nailthedeadline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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
        // TODO get task list from somewhere
        List<String> taskList = new ArrayList<>();
        taskList.add("task1");
        taskList.add("task2");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, taskList);
        ListView listView = (ListView) findViewById(R.id.task_list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                // Do something in response to the click
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

