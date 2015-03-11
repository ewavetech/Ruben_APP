package se.hiq.losningsappen.content;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import se.hiq.losningsappen.R;
import se.hiq.losningsappen.content.controller.SubTaskListadapter;
import se.hiq.losningsappen.content.model.BookContext;
import se.hiq.losningsappen.content.model.Chapter;
import se.hiq.losningsappen.content.model.ContentContext;
import se.hiq.losningsappen.content.model.Subtask;
import se.hiq.losningsappen.content.model.Task;

/**
 * Created by Naknut on 26/06/14.
 */

public class SubtaskFragment extends Fragment implements View.OnClickListener {

    private Subtask subtask;
    private int chapterPosition;
    private int subChapterPosition;
    private int taskPosition;
    private int subtaskPosition;
    private SubTaskListadapter adapter;
    private ArrayList<String> groups;
    private ArrayList<String> children;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.subtask_layout, container, false);

        retrieveContent();

        adapter = new SubTaskListadapter(getActivity(), groups, children);
        // TODO Implement 3 new views

        view.findViewById(R.id.statusButtonsView).findViewById(R.id.notDoneButton).setOnClickListener(this);
        view.findViewById(R.id.statusButtonsView).findViewById(R.id.doneButton).setOnClickListener(this);

        view.setBackgroundResource(R.color.standard_bg);
        return view;
    }

    private void initOnClickListener(int doneNotDone) {

        BookContext.getInstance().getChapters(new BookContext.GetChaptersCallback() {
            @Override
            public void onGetChapters(List<Chapter> chapters) {
                String taskName = chapters.get(chapterPosition).getSubChapter(subtaskPosition).getTask(taskPosition).getName();
                String subtaskName = subtask.getName();
                String fullName = (taskName + " " + subtaskName).trim();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.doneButton:
                initOnClickListener(1);
                break;
            case R.id.notDoneButton:
                initOnClickListener(0);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        retrieveContent();

        adapter.setGroups(groups);
        adapter.setChildren(children);
        adapter.notifyDataSetChanged();
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

    private void retrieveContent() {
        chapterPosition = ContentContext.getInstance().getCurrentChapter();
        subChapterPosition = ContentContext.getInstance().getCurrentSubChapter();
        taskPosition = ContentContext.getInstance().getCurrentTask();
        subtaskPosition = ContentContext.getInstance().getCurrentSubTask();

        BookContext.getInstance().getChapters(new BookContext.GetChaptersCallback() {
            @Override
            public void onGetChapters(List<Chapter> chapters) {
                Task task = chapters.get(chapterPosition).getSubChapter(subChapterPosition).getTask(taskPosition);
                subtask = task.getSubtask(subtaskPosition);

                getActivity().setTitle(task.getName());

                groups = new ArrayList<String>();
                children = new ArrayList<String>();

                List<String> answer = subtask.getAnswers();
                List<String> hint = subtask.getHints();
                List<String> solution = subtask.getSolutions();

                if (answer.size() >= 1) {
                    groups.add("Svar");
                    String answers = "";
                    for (String answerString : answer) {
                        answers += answerString;
                    }
                    children.add(answers);
                }
                if (hint.size() >= 1) {
                    groups.add("Hjälp");
                    String hints = "";
                    for (String hintString : hint) {
                        hints += hintString;
                    }
                    children.add(hints);
                }
                if (solution.size() >= 1) {
                    groups.add("Lösning");
                    String solutions = "";
                    for (String solutionString : solution) {
                        solutions += solutionString;
                    }
                    children.add(solutions);
                }
            }
        });


    }
}
