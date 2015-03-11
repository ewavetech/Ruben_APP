package se.hiq.losningsappen.content;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.lang.reflect.Field;
import java.util.List;

import se.hiq.losningsappen.R;
import se.hiq.losningsappen.content.controller.TaskArrayAdapter;
import se.hiq.losningsappen.content.model.BookContext;
import se.hiq.losningsappen.content.model.Chapter;
import se.hiq.losningsappen.content.model.ContentContext;
import se.hiq.losningsappen.content.model.SubChapter;
import se.hiq.losningsappen.content.model.Task;

/**
 * Created by Naknut on 01/07/14.
 */
public class SubChapterFragment extends ListFragment {
    private TaskArrayAdapter adapter;
    private SubChapter subChapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BookContext.getInstance().getChapters(new BookContext.GetChaptersCallback() {
            @Override
            public void onGetChapters(List<Chapter> chapters) {
                subChapter = chapters.get(ContentContext.getInstance().getCurrentChapter())
                        .getSubChapter(ContentContext.getInstance().getCurrentSubChapter());

                List<Task> tasks = subChapter.getTasks();

                adapter = new TaskArrayAdapter(getActivity().getApplicationContext(), tasks);
                setListAdapter(adapter);
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundResource(R.color.standard_bg);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        BookContext.getInstance().getChapters(new BookContext.GetChaptersCallback() {
            @Override
            public void onGetChapters(List<Chapter> chapters) {
                subChapter = chapters.get(ContentContext.getInstance().getCurrentChapter())
                        .getSubChapter(ContentContext.getInstance().getCurrentSubChapter());

                List<Task> tasks = subChapter.getTasks();

                adapter.setTasks(tasks);

                getActivity().setTitle(subChapter.getName());
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        super.onListItemClick(l, v, position, id);

        BookContext.getInstance().getChapters(new BookContext.GetChaptersCallback() {
            @Override
            public void onGetChapters(List<Chapter> chapters) {

                int currentChapter = ContentContext.getInstance().getCurrentChapter();
                int currentSubChapter = ContentContext.getInstance()
                        .getCurrentSubChapter();

                Task task = chapters.get(currentChapter).getSubChapter(currentSubChapter)
                        .getTask(position);

                if (task.getSubTasks().size() == 1) {

                    Intent intent = new Intent(getActivity().getBaseContext(), SubtaskActivity.class);
                    intent.putExtra("subtaskPosition", 0);
                    intent.putExtra("chapterPosition", currentChapter);
                    intent.putExtra("subChapterPosition", currentSubChapter);
                    intent.putExtra("taskPosition", position);
                    startActivity(intent);
                } else {
                    ContentContext.getInstance().setCurrentTask(position);
                    ContentContext.getInstance().setState(ContentContext.ContentState.TASK);
                }
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
}
