package se.hiq.losningsappen.content.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petterstenberg on 2014-08-18.
 */

public class ContentContext {

    private static ContentContext instance;
    private ContentState state = ContentState.ROOT;
    private int currentChapter;
    private int currentSubChapter;
    private int currentTask;
    private int currentSubTask;
    private String actionBarDescription;
    private List<ContentListener> listeners;

    public ContentContext() {
        listeners = new ArrayList<ContentListener>();
    }

    public static ContentContext getInstance() {
        if (instance == null) {
            instance = new ContentContext();
        }
        return instance;
    }

    public void setState(ContentState state) {
        this.state = state;
        notifyListeners();
    }

    public void addListener(ContentListener listener) {
        listeners.add(listener);
    }

    public void removeListener(ContentListener listener) {
        listeners.remove(listener);
    }

    private void notifyListeners() {
        for (ContentListener listener : listeners) {
            listener.onContentStateChanged(state);
        }
    }

    public int getCurrentChapter() {
        return currentChapter;
    }

    public void setCurrentChapter(int currentChapter) {
        this.currentChapter = currentChapter;
    }

    public int getCurrentSubChapter() {
        return currentSubChapter;
    }

    public void setCurrentSubChapter(int currentSubChapter) {
        this.currentSubChapter = currentSubChapter;
    }

    public int getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(int currentTask) {
        this.currentTask = currentTask;
    }

    public int getCurrentSubTask() {
        return currentSubTask;
    }

    public void setCurrentSubTask(int currentSubTask) {
        this.currentSubTask = currentSubTask;
    }

    public String getActionBarDescription() {
        return actionBarDescription;
    }

    public void setActionBarDescription(String actionBarDescription) {
        this.actionBarDescription = actionBarDescription;
    }

    public enum ContentState {ROOT, CHAPTER, SUBCHAPTER, TASK, SUBTASK}

    public interface ContentListener {
        public void onContentStateChanged(ContentState contentState);
    }
}
