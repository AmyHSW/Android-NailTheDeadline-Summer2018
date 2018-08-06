package edu.neu.madcourse.shuwanhuang.nailthedeadline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class TasksAdapter extends ArrayAdapter<Task> {

    // View lookup cache
    private static class ViewHolder {
        TextView name;
        TextView deadline;
        TextView timeSpent;
    }

    public TasksAdapter(Context context, ArrayList<Task> tasks) {
        super(context, R.layout.item_task, tasks);
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
            viewHolder.name = (TextView) convertView.findViewById(R.id.taskName);
            viewHolder.deadline = (TextView) convertView.findViewById(R.id.taskDeadline);
            viewHolder.timeSpent = (TextView) convertView.findViewById(R.id.timeSpent);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.name.setText(task.getTaskName());
        viewHolder.deadline.setText(task.getDueDate());
        viewHolder.timeSpent.setText(task.getWorkedOnInMinute());
        // Return the completed view to render on screen
        return convertView;
    }
}
