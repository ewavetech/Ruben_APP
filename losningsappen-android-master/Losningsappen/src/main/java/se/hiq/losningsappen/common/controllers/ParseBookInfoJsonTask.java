package se.hiq.losningsappen.common.controllers;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import se.hiq.losningsappen.common.models.booksinfo.BookInfo;
import se.hiq.losningsappen.common.models.settings.SettingsContext;

/**
 * Created by petterstenberg on 2014-09-03.
 * <p/>
 * {@link android.os.AsyncTask} that parses json to bookinfo array
 */
public class ParseBookInfoJsonTask extends AsyncTask<Activity, Void, Void> {

    @Override
    protected Void doInBackground(Activity... params) {

        AssetManager assets = params[0].getAssets();
        InputStream stream = null;
        try {
            stream = assets.open("books.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert stream != null;
        Reader reader = new InputStreamReader(stream);

        List<BookInfo> bookInfos;
        Gson gson = new Gson();
        bookInfos = Arrays.asList(gson.fromJson(reader, BookInfo[].class));
        SettingsContext.getInstance().setBookInfos(bookInfos);
        SettingsContext.getInstance().setActiveBook(bookInfos.get(1));

        return null;
    }
}
