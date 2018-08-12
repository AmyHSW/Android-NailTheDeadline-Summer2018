package edu.neu.madcourse.shuwanhuang.nailthedeadline;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class TasksAdapter extends ArrayAdapter<Task> {

    private final boolean isHistory;

    // View lookup cache
    private static class ViewHolderOngoing {
        TextView name;
        TextView deadline;
        TextView timeSpent;
    }

    // View lookup cache
    private static class ViewHolderHistory {
        TextView name;
        TextView deadline;
        TextView timeSpent;
        ImageView catImage;
    }

    public TasksAdapter(Context context, ArrayList<Task> tasks, boolean isHistory) {
        super(context, 0, tasks);
        this.isHistory = isHistory;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Task task = getItem(position);
        if (this.isHistory) {
            return getViewHistory(task, convertView, parent);
        } else {
            return getViewOngoing(task, convertView, parent);
        }
    }

    private View getViewOngoing(Task task, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolderOngoing viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolderOngoing();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_task, parent, false);
            viewHolder.name = convertView.findViewById(R.id.task_name);
            viewHolder.deadline = convertView.findViewById(R.id.task_deadline);
            viewHolder.timeSpent = convertView.findViewById(R.id.task_time_spent);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolderOngoing) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.name.setText(task.getTaskName());
        String ddlText = "Due " + task.getDueDate().toString();
        viewHolder.deadline.setText(ddlText);
        viewHolder.timeSpent.setText(task.getWorkedOnInMinute() + "min done");
        // Return the completed view to render on screen
        return convertView;
    }

    private View getViewHistory(Task task, View convertView, ViewGroup parent) {
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolderHistory viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            // If there's no view to re-use, inflate a brand new view for row
            viewHolder = new ViewHolderHistory();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_task_history, parent, false);
            viewHolder.name = convertView.findViewById(R.id.task_name);
            viewHolder.deadline = convertView.findViewById(R.id.task_deadline);
            viewHolder.timeSpent = convertView.findViewById(R.id.task_time_spent);
            viewHolder.catImage = convertView.findViewById(R.id.cat_image);
            // Cache the viewHolder object inside the fresh view
            convertView.setTag(viewHolder);
        } else {
            // View is being recycled, retrieve the viewHolder object from tag
            viewHolder = (ViewHolderHistory) convertView.getTag();
        }
        // Populate the data from the data object via the viewHolder object
        // into the template view.
        viewHolder.name.setText(task.getTaskName());
        String ddlText = "Due " + task.getDueDate().toString();
        viewHolder.deadline.setText(ddlText);
        String timeSpentText = "Totally spent " + task.getWorkedOnInMinute() + " min";
        viewHolder.timeSpent.setText(timeSpentText);
        Cat cat = task.getCat();
        viewHolder.catImage.setImageDrawable(parent.getResources().getDrawable(cat.getImage()));
        // Return the completed view to render on screen
        return convertView;
    }
}
