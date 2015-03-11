package se.hiq.losningsappen.content;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.List;

import se.hiq.losningsappen.R;
import se.hiq.losningsappen.content.model.BookContext;
import se.hiq.losningsappen.content.model.Chapter;
import se.hiq.losningsappen.content.model.ContentContext;
import se.hiq.losningsappen.content.model.Subtask;
import se.hiq.losningsappen.content.model.Task;

/**
 * Created by Naknut on 11/06/14.
 */
public class TaskFragment extends ListFragment {

    private int chapterPosition;
    private int subChapterPosition;
    private int taskPosition;
    List<Subtask> subtasks;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundResource(R.color.standard_bg);
        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        String subtaskName=subtasks.get(position).getName();

        Intent intent = new Intent(getActivity().getBaseContext(), SubtaskActivity.class);
        intent.putExtra("chapterPosition", chapterPosition);
        intent.putExtra("subChapterPosition", subChapterPosition);
        intent.putExtra("taskPosition", taskPosition);
        intent.putExtra("subtaskPosition", position);
        intent.putExtra("subtaskName",subtaskName);
//        Toast.makeText(getActivity().getBaseContext(),chapterPosition+"-"+subChapterPosition+"-"+taskPosition+"-"+
//                position,Toast.LENGTH_LONG).show();
        getActivity().startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();

        chapterPosition = ContentContext.getInstance().getCurrentChapter();
        subChapterPosition = ContentContext.getInstance().getCurrentSubChapter();
        taskPosition = ContentContext.getInstance().getCurrentTask();

        BookContext.getInstance().getChapters(new BookContext.GetChaptersCallback() {
            @Override
            public void onGetChapters(List<Chapter> chapters) {
                Task task = chapters.get(chapterPosition).getSubChapter(subChapterPosition).getTask(taskPosition);
                subtasks = task.getSubTasks();

                getActivity().setTitle(task.getName());

                SubtaskArrayAdapter adapter = new SubtaskArrayAdapter(getActivity().getApplicationContext(), subtasks);
                setListAdapter(adapter);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();

        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public class SubtaskArrayAdapter extends ArrayAdapter<Subtask> {

        private final List<Subtask> subtasks;
        private final LayoutInflater inflater;

        public SubtaskArrayAdapter(Context context, List<Subtask> subtasks) {
            super(context, R.layout.general_list_item, subtasks);
            this.subtasks = subtasks;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null)
                view = inflater.inflate(R.layout.general_list_item, parent, false);

            ImageView passedImage = (ImageView) view.findViewById(R.id.passed);

            if (subtasks.get(position).getStatus() == Subtask.Status.PASSED) {
                passedImage.setImageResource(R.drawable.passed_xml);
            } else if (subtasks.get(position).getStatus() == Subtask.Status.UNPASSED) {
                passedImage.setImageResource(R.drawable.not_passed_xml);
            }

            TextView taskName = (TextView) view.findViewById(R.id.taskName);
            taskName.setText(subtasks.get(position).getName());

            view.setBackgroundResource(R.color.standard_bg);
            return view;
        }
    }

}
