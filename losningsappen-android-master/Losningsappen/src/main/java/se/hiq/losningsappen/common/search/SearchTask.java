package se.hiq.losningsappen.common.search;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import java.util.List;

import se.hiq.losningsappen.content.model.BookContext;
import se.hiq.losningsappen.content.model.Chapter;
import se.hiq.losningsappen.content.model.SubChapter;
import se.hiq.losningsappen.content.model.Task;

/**
 * Created by petterstenberg on 2014-08-14.
 */
public class SearchTask extends AsyncTask<String, Void, Void> {

    private OnSearchResultListener delegate;
    private Handler handler;

    private int currentChapterPos = 0;


    public SearchTask() {
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    protected Void doInBackground(final String... strings) {

        BookContext.getInstance().getChapters(new BookContext.GetChaptersCallback() {
            @Override
            public void onGetChapters(List<Chapter> chapters) {
                for (Chapter chapter : chapters) {
                    int currentSubChapterPos = 0;
                    for (SubChapter subChapter : chapter.getSubChapters()) {
                        int currentTaskPos = 0;
                        for (final Task task : subChapter.getTasks()) {
                            if (task.getName().startsWith(strings[0])) {
                                task.setChapterPosition(currentChapterPos);
                                task.setSubChapterPosition(currentSubChapterPos);
                                task.setPosition(currentTaskPos);
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        delegate.onSearchResponse(task);
                                    }
                                });
                            }
                            currentTaskPos++;
                        }
                        currentSubChapterPos++;
                    }
                    currentChapterPos++;
                }
            }
        });
        return null;
    }

    public void setDelegate(OnSearchResultListener delegate) {
        this.delegate = delegate;
    }

    public interface OnSearchResultListener {
        public void onSearchResponse(Task responseTask);
    }
}
