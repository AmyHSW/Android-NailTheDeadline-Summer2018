package edu.neu.madcourse.shuwanhuang.nailthedeadline.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import edu.neu.madcourse.shuwanhuang.nailthedeadline.object.Cat;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.util.CatCollectorUtil;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.util.DatabaseUtil;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.R;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.object.Task;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.adapter.TasksAdapter;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_NAME = "Task";
    private final ArrayList<Task> tasks = new ArrayList<>();
    private AlertDialog dialog;

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
        List<Task> dbTasks = DatabaseUtil.readOngoingTasksFromDB(this);
        List<Task> pastTasks = new ArrayList<>();
        Date currentDateTime = Calendar.getInstance().getTime();
        for (Task task : dbTasks) {
            if (task.getDueDate().after(currentDateTime)) {
                tasks.add(task);
            } else {
                pastTasks.add(task);
            }
        }
        collectCatsForPastTasks(pastTasks);
        DatabaseUtil.addPastTasksToDB(this, pastTasks);
        DatabaseUtil.writeOngoingTasksToDB(this, tasks);
    }

    private void initTaskListView() {
        TextView noTasksTextView = findViewById(R.id.no_tasks_text);
        if (!tasks.isEmpty()) {
            noTasksTextView.setVisibility(View.INVISIBLE);
        } else {
            noTasksTextView.setVisibility(View.VISIBLE);
        }
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

    private void collectCatsForPastTasks(List<Task> pastTasks) {
        if (pastTasks.isEmpty()) return;
        StringBuilder toastMsg = new StringBuilder();
        for (Task task : pastTasks) {
            if (toastMsg.length() > 0) {
                toastMsg.append("\n");
            }
            Cat cat = CatCollectorUtil.collectCatForTask(task);
            DatabaseUtil.addCatToDB(this, cat);
            if (cat.getID() == 0) {
                toastMsg.append("Oop! No cat is collected for task: " + task.getTaskName());
                continue;
            }
            task.setCatID(cat.getID());
            toastMsg.append("Congrats! You got a cat for finishing task: " + task.getTaskName());
        }
        Toast.makeText(this, toastMsg.toString(), Toast.LENGTH_LONG).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.help:
                showAlertDialog(R.string.help_msg);
                return true;
            case R.id.acknowledge:
                showAlertDialog(R.string.acknowledgement_msg);
                return true;
            case R.id.to_graders:
                showAlertDialog(R.string.to_graders_msg);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showAlertDialog(int msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ok_label,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) { }
                });
        dialog = builder.show();
    }
}
