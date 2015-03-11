package se.hiq.losningsappen.content;


import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import se.hiq.losningsappen.R;
import se.hiq.losningsappen.content.model.BookContext;
import se.hiq.losningsappen.content.model.Chapter;
import se.hiq.losningsappen.content.model.Subtask;
import se.hiq.losningsappen.content.model.Task;

/**
 * Created by Naknut on 11/06/14.
 */
public class TaskActivity extends ListActivity {

    private int chapterPosition;
    private int subChapterPosition;
    private int taskPosition;

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getBaseContext(), SubtaskActivity.class);
        intent.putExtra("chapterPosition", chapterPosition);
        intent.putExtra("subChapterPosition", subChapterPosition);
        intent.putExtra("taskPosition", taskPosition);
        intent.putExtra("subtaskPosition", position);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        chapterPosition = getIntent().getExtras().getInt("chapterPosition");
        subChapterPosition = getIntent().getExtras().getInt("subChapterPosition");
        taskPosition = getIntent().getExtras().getInt("taskPosition");

        BookContext.getInstance().getChapters(new BookContext.GetChaptersCallback() {
            @Override
            public void onGetChapters(List<Chapter> chapters) {
                Task task = chapters.get(chapterPosition).getSubChapter(subChapterPosition).getTask(taskPosition);
                List<Subtask> subtasks = task.getSubTasks();

                setTitle(getString(R.string.task_name_prefix) + task.getName());

                SubtaskArrayAdapter adapter = new SubtaskArrayAdapter(TaskActivity.this, subtasks);
                setListAdapter(adapter);
            }
        });
    }

    public class SubtaskArrayAdapter extends ArrayAdapter<Subtask> {

        private final List<Subtask> subtasks;
        private final Context context;
        private final LayoutInflater inflater;

        public SubtaskArrayAdapter(Context context, List<Subtask> subtasks) {
            super(context, R.layout.general_list_item, subtasks);
            this.context = context;
            this.subtasks = subtasks;
            inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null)
                view = inflater.inflate(R.layout.general_list_item, parent, false);
            ImageView passedImage = (ImageView) view.findViewById(R.id.passed);
            if (subtasks.get(position).getStatus() == Subtask.Status.PASSED)
                passedImage.setImageResource(R.drawable.passed_xml);
            else if (subtasks.get(position).getStatus() == Subtask.Status.UNPASSED)
                passedImage.setImageResource(R.drawable.not_passed_xml);
            TextView taskName = (TextView) view.findViewById(R.id.taskName);
            taskName.setText(subtasks.get(position).getName());
            return view;
        }
    }
}