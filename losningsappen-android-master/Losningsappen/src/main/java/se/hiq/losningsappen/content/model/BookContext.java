package se.hiq.losningsappen.content.model;

import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import se.hiq.losningsappen.content.controller.LoadBookTask;
import se.hiq.losningsappen.content.controller.SaveBookTask;

/**
 * Created by Naknut on 11/06/14.
 */

public class BookContext {

    private static BookContext instance = null;
    private final Handler handler;
    private WeakReference<List<Chapter>> chapters;
    private List<BookContentListener> listeners;

    private BookContext() {
        listeners = new ArrayList<BookContentListener>();
        handler = new Handler(Looper.getMainLooper());
    }

    public static BookContext getInstance() {
        if (instance == null)
            instance = new BookContext();
        return instance;
    }

    public void getChapters(final GetChaptersCallback callback) {

        if (chapters == null) {
            new LoadBookTask(new LoadBookTask.LoadBookListener() {
                @Override
                public void onBookLoaded(ArrayList<Chapter> chapters) {
                    callback.onGetChapters(chapters);
                }
            }).execute();
        } else {
            callback.onGetChapters(chapters.get());
        }
    }

    public void setChapters(List<Chapter> chapters) {
        this.chapters = new WeakReference<List<Chapter>>(chapters);
        notifyListeners();
        new SaveBookTask().execute(chapters);
    }

    public void addListeners(BookContentListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners() {
        for (final BookContentListener listener : listeners) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onBookContentChanged(chapters.get());
                }
            });
        }
    }

    public void removeListener(BookContentListener listener) {
        listeners.remove(listener);
    }

    public interface BookContentListener {
        void onBookContentChanged(List<Chapter> chapters);
    }

    public interface GetChaptersCallback {
        void onGetChapters(List<Chapter> chapters);
    }
}