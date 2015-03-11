package se.hiq.losningsappen.common.search;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import se.hiq.losningsappen.content.SubtaskActivity;
import se.hiq.losningsappen.content.TaskActivity;
import se.hiq.losningsappen.content.model.Task;

/**
 * Created by petterstenberg on 2014-08-14.
 */
public class SearchAdapter extends ArrayAdapter {

    private final LayoutInflater inflater;
    private List<Task> taskList;

    public SearchAdapter(Context context, int textViewResourceId, List<Task> taskList) {
        super(context, textViewResourceId, taskList);
        this.taskList = taskList;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return taskList.size();
    }

    @Override
    public Task getItem(int position) {
        return taskList.get(position);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

        TextView textView = (TextView) convertView.findViewById(android.R.id.text1);

        textView.setText(taskList.get(position).getName());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openTask(getItem(position));
            }
        });

        return convertView;
    }

    private void openTask(Task task) {

        Intent intent;

        if (task.getSubTasks().size() > 1) {
            intent = new Intent(getContext(), TaskActivity.class);
        } else {
            intent = new Intent(getContext(), SubtaskActivity.class);
            intent.putExtra("subTaskPosition", 0);
        }
        intent.putExtra("chapterPosition", task.getChapterPosition());
        intent.putExtra("subChapterPosition", task.getSubChapterPosition());
        intent.putExtra("taskPosition", task.getPosition());

        getContext().startActivity(intent);

    }

}
