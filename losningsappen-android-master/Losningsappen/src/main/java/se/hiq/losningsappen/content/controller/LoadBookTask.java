package se.hiq.losningsappen.content.controller;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import se.hiq.losningsappen.ApplicationContext;
import se.hiq.losningsappen.common.Constants;
import se.hiq.losningsappen.content.model.Chapter;

/**
 * Created by petterstenberg on 2014-09-07.
 * <p/>
 * Deserialize history from disk and puts it in {@link se.hiq.losningsappen.history.HistoryContext}.
 */

public class LoadBookTask extends AsyncTask<Void, Void, Void> {

    public LoadBookTask(LoadBookListener listener) {
        LoadBookListener listener1 = listener;
    }

    @Override
    protected Void doInBackground(Void... voids) {

        ArrayList<Chapter> loadedChapterList = null;
        FileInputStream fis;
        ObjectInputStream in;
        try {
            fis = new FileInputStream(ApplicationContext.getContext().getCacheDir().getAbsolutePath() + File.pathSeparator + new File(Constants.CURRENT_BOOK_FILENAME));
            in = new ObjectInputStream(fis);
            loadedChapterList = (ArrayList<Chapter>) in.readObject();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public interface LoadBookListener {
        void onBookLoaded(ArrayList<Chapter> chapters);
    }
}
