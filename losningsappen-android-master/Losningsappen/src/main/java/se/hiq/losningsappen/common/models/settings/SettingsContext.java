package se.hiq.losningsappen.common.models.settings;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayList;
import java.util.List;

import se.hiq.losningsappen.common.models.booksinfo.BookInfo;

/**
 * Created by Naknut on 03/08/14.
 */
public class SettingsContext {

    private static final String FILENAME = "active_book";
    private static SettingsContext instance = null;
    private final Handler handler;
    private List<BookInfo> bookInfos;
    private BookInfo activeBook;
    private List<SettingsListener> listeners;

    private SettingsContext() {
        listeners = new ArrayList<SettingsListener>();
        handler = new Handler(Looper.getMainLooper());
    }

    public static SettingsContext getInstance() {
        if (instance == null)
            instance = new SettingsContext();
        return instance;
    }

    public List<BookInfo> getBookInfos() {
        return bookInfos;
    }

    public void setBookInfos(List<BookInfo> bookInfos) {
        this.bookInfos = bookInfos;
        notifyBookInfosChanged();
    }

    public BookInfo getBookInfoWithId(String productId) {
        for (BookInfo bookInfo : bookInfos) {
            if (bookInfo.getProductIdentifier().equals(productId))
                return bookInfo;
        }
        return null;
    }

    public BookInfo getActiveBook() {
        return activeBook;
    }

    public void setActiveBook(BookInfo activeBook) {
        this.activeBook = activeBook;
        notifyActiveBookChanged();
    }

    public void addSettingsListener(SettingsListener listener) {
        listeners.add(listener);
    }

    private void notifyActiveBookChanged() {
        for (final SettingsListener listener : listeners) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onActiveBookInfoChanged(activeBook);
                }
            });
        }
    }

    private void notifyBookInfosChanged() {
        for (final SettingsListener listener : listeners) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onBookInfosChanged(bookInfos);
                }
            });
        }
    }

    public void removeSettingsListener(SettingsListener listener) {
        listeners.remove(listener);
    }

    public interface SettingsListener {
        void onBookInfosChanged(List<BookInfo> bookInfos);

        void onActiveBookInfoChanged(BookInfo activeBook);
    }
}
