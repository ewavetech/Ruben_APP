package se.hiq.losningsappen.content.model;

import java.io.Serializable;
import java.util.List;

import se.hiq.losningsappen.history.Historyable;

/**
 * Created by Naknut on 10/06/14.
 */

public class Subtask implements Historyable, Serializable {

    private String name;
    private String parentName;
    private List<String> answers;
    private List<String> hints;
    private List<String> solutions;
    private VideoLink videoLink;
    private Status status = Status.UNTAGGED;

    public String getName() {
        return name;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public List<String> getHints() {
        return hints;
    }

    public List<String> getSolutions() {
        return solutions;
    }

    public VideoLink getVideoLink() {
        return videoLink;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        return (parentName + " " + name).equals(o);
    }

    public enum Status {
        UNTAGGED,
        UNPASSED,
        PASSED
    }
}
