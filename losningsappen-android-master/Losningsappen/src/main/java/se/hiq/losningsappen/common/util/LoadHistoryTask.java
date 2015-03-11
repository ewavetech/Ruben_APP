package se.hiq.losningsappen.common.util;

import android.os.AsyncTask;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import se.hiq.losningsappen.history.HistoryContext;
import se.hiq.losningsappen.history.Historyable;

/**
 * Created by petterstenberg on 2014-09-07.
 * <p/>
 * Deserialize history from disk and puts it in {@link se.hiq.losningsappen.history.HistoryContext}.
 */

public class LoadHistoryTask extends AsyncTask<Void, Void, Void> {
    @Override
    protected Void doInBackground(Void... voids) {

        FileInputStream fis;
        ObjectInputStream in;
        try {
            fis = new FileInputStream("history");
            in = new ObjectInputStream(fis);
            HistoryContext.getInstance().setHistoryList((ArrayList<Historyable>) in.readObject());
            in.close();
        } catch (Exception ignored) {
        }

        return null;
    }
}
