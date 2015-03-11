package se.hiq.losningsappen.common.util;

import android.os.AsyncTask;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import se.hiq.losningsappen.history.HistoryContext;

/**
 * Created by petterstenberg on 2014-09-07.
 * <p/>
 * Takes history list from {@link se.hiq.losningsappen.history.HistoryContext} and serialize it on disk.
 */
public class SaveHistoryTask extends AsyncTask<Void, Void, Void> {

    @Override
    protected Void doInBackground(Void... voids) {

        FileOutputStream fos;
        ObjectOutputStream out;
        try {
            fos = new FileOutputStream("history");
            out = new ObjectOutputStream(fos);
            out.writeObject(HistoryContext.getInstance().getHistoryList());

            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
