package se.hiq.losningsappen.content.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Naknut on 10/06/14.
 */
public class Task implements Serializable {
    private String name;
    private List<Subtask> subTasks;
    private String page;

    private int chapterPosition;
    private int subChapterPosition;
    private int position;

    public String getName() {
        return name;
    }

    public List<Subtask> getSubTasks() {
        return subTasks;
    }

    public String getPage() {
        return page;
    }

    public Subtask getSubtask(int subtaskPosition) {
        return subTasks.get(subtaskPosition);
    }

    public int getChapterPosition() {
        return chapterPosition;
    }

    public void setChapterPosition(int chapterPosition) {
        this.chapterPosition = chapterPosition;
    }

    public int getSubChapterPosition() {
        return subChapterPosition;
    }

    public void setSubChapterPosition(int subChapterPosition) {
        this.subChapterPosition = subChapterPosition;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return name;
    }
}
