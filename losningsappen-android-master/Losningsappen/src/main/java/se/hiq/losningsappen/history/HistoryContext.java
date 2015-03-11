package se.hiq.losningsappen.history;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by petterstenberg on 2014-08-26.
 * <p/>
 * This singleton context class keeps the state of the
 * history in terms of taken book tasks and chapter tests.
 */

public class HistoryContext {

    private static HistoryContext instance;
    private List<Historyable> historyList;
    private List<HistoryContentListener> listeners;

    private Historyable currentHistoryable;

    public HistoryContext() {
        historyList = new ArrayList<Historyable>();
        listeners = new ArrayList<HistoryContentListener>();
    }

    public static HistoryContext getInstance() {
        if (instance == null) instance = new HistoryContext();
        return instance;
    }

    public void addHistoryContentListener(HistoryContentListener listener) {
        listeners.add(listener);
    }

    public List<Historyable> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<Historyable> historyList) {
        this.historyList = historyList;
        notifyDataSetChanged();
    }

    public void addHistoryItemToList(Historyable item) {
        historyList.add(item);
        notifyDataSetChanged();
    }

    public void notifyDataSetChanged() {
        for (HistoryContentListener listener : listeners) {
            listener.onHistoryContentChanged();
        }
    }

    public void removeHistoryContentListener(HistoryContentListener historyContentListener) {
        listeners.remove(historyContentListener);
    }

    public Historyable getCurrentHistoryable() {
        return currentHistoryable;
    }

    public void setCurrentHistoryable(Historyable currentHistoryable) {
        this.currentHistoryable = currentHistoryable;
    }

    public interface HistoryContentListener {
        void onHistoryContentChanged();
    }
}
