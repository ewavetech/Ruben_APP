package se.hiq.losningsappen.content.controller;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.List;

import se.hiq.losningsappen.ApplicationContext;
import se.hiq.losningsappen.common.Constants;
import se.hiq.losningsappen.content.model.Chapter;

/**
 * Created by petterstenberg on 2014-09-07.
 * <p/>
 * Takes chapter list from {@link se.hiq.losningsappen.content.model.BookContext} and serialize it on disk.
 */

public class SaveBookTask extends AsyncTask<List<Chapter>, Void, Void> {

    @Override
    protected Void doInBackground(List<Chapter>... args) {

        FileOutputStream fos;
        ObjectOutputStream out;
        try {
            fos = new FileOutputStream(ApplicationContext.getContext().getCacheDir().getAbsolutePath() + File.pathSeparator + new File(Constants.CURRENT_BOOK_FILENAME));
            out = new ObjectOutputStream(fos);
            out.writeObject(args[0]);

            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
