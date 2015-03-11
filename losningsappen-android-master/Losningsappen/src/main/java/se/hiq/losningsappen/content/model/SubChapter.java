package se.hiq.losningsappen.content.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Naknut on 09/06/14.
 */
public class SubChapter implements Serializable {
    private String name;
    private List<Task> tasks;
    private List<VideoLink> videoLinks;

    public String getName() {
        return name;
    }

    public List<VideoLink> getVideoLinks() {
        return videoLinks;
    }

    public Task getTask(int taskPosition) {
        return tasks.get(taskPosition);
    }

    public List<Task> getTasks() {
        return tasks;
    }
}
