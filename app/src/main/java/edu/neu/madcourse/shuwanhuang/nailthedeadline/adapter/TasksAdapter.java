package edu.neu.madcourse.shuwanhuang.nailthedeadline.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import edu.neu.madcourse.shuwanhuang.nailthedeadline.object.Cat;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.R;
import edu.neu.madcourse.shuwanhuang.nailthedeadline.object.Task;

public class TasksAdapter extends ArrayAdapter<Task> {

    private final boolean isHistory;

    // View lookup cache
    private static class ViewHolder {
        TextView name;
        TextView deadline;
        TextView timeSpent;
        ImageView imageView;
    }

    public TasksAdapter(Context context, ArrayList<Task> tasks, boolean isHistory) {
        super(context, 0, tasks);
        this.isHistory = isHistory;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Task task = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_task, parent, false);
            viewHolder.name = convertView.findViewById(R.id.task_name);
            viewHolder.deadline = convertView.findViewById(R.id.task_deadline);
            viewHolder.timeSpent = convertView.findViewById(R.id.task_time_spent);
            viewHolder.imageView = convertView.findViewById(R.id.imageView);

            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.name.setText(task.getTaskName());
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM'/'dd'/'y hh:mm aa", Locale.US);
        String ddlText = "Due " + dateFormat.format(task.getDueDate());
        viewHolder.deadline.setText(ddlText);
        if (!isHistory) {
            String timeSpentText = "You've spent " + task.getWorkedOnInMinute() + " min";
            viewHolder.timeSpent.setText(timeSpentText);
        } else {
            String timeSpentText = "Totally spent " + task.getWorkedOnInMinute() + " min";
            viewHolder.timeSpent.setText(timeSpentText);
            Cat cat = task.getCat();
            viewHolder.imageView.getLayoutParams().height = 200;
            viewHolder.imageView.getLayoutParams().width = 200;
            viewHolder.imageView.setImageDrawable(parent.getResources().getDrawable(cat.getImage()));
        }
        // Return the completed view to render on screen
        return convertView;
    }
}
