package se.hiq.losningsappen.content.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import se.hiq.losningsappen.R;
import se.hiq.losningsappen.content.model.Subtask;
import se.hiq.losningsappen.content.model.Task;

/**
 * Created by petterstenberg on 2014-08-18.
 */
public class TaskArrayAdapter extends ArrayAdapter<Task> {

    private final LayoutInflater inflater;
    private List<Task> tasks;

    public TaskArrayAdapter(Context context, List<Task> tasks) {
        super(context, R.layout.general_list_item, tasks);
        this.tasks = tasks;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null)
            view = inflater.inflate(R.layout.general_list_item, parent, false);
        TextView taskName = (TextView) view.findViewById(R.id.taskName);
        ImageView statusImage = (ImageView) view.findViewById(R.id.passed);

        for (Subtask subtask : tasks.get(position).getSubTasks()) {
            if (subtask.getStatus() == Subtask.Status.UNPASSED) {
                statusImage.setImageResource(R.drawable.not_passed_xml);
                break;
            } else if (subtask.getStatus() == Subtask.Status.PASSED)
                statusImage.setImageResource(R.drawable.passed_xml);
            else if (subtask.getStatus() == Subtask.Status.UNTAGGED)
                statusImage.setImageDrawable(null);
        }

        taskName.setText(tasks.get(position).getName());
        return view;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        notifyDataSetChanged();
    }
}
