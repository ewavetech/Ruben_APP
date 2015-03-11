package se.hiq.losningsappen.content;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import se.hiq.losningsappen.ApplicationContext;
import se.hiq.losningsappen.R;
import se.hiq.losningsappen.common.models.booksinfo.BookInfo;
import se.hiq.losningsappen.common.models.settings.SettingsContext;
import se.hiq.losningsappen.content.model.BookContext;
import se.hiq.losningsappen.content.model.Chapter;
import se.hiq.losningsappen.content.model.ContentContext;

/**
 * Created by Naknut on 30/07/14.
 */

public class BookFragment extends ListFragment implements SettingsContext.SettingsListener, BookContext.BookContentListener {

    ChapterAdapter adapter;
    BookInfo activeBook = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsContext.getInstance().addSettingsListener(this);
        BookContext.getInstance().addListeners(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        view.setBackgroundResource(R.color.standard_bg);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        if (activeBook != SettingsContext.getInstance().getActiveBook())
            new ParseJson2ChaptersTask().execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        SettingsContext.getInstance().removeSettingsListener(this);
        BookContext.getInstance().removeListener(this);
    }

    @Override
    public void onListItemClick(ListView l, View v, final int position, long id) {
        ContentContext.getInstance().setCurrentChapter(position);

        BookContext.getInstance().getChapters(new BookContext.GetChaptersCallback() {
            @Override
            public void onGetChapters(List<Chapter> chapters) {
                ContentContext.getInstance().setActionBarDescription(
                        chapters.get(position).getName());
                ContentContext.getInstance().setState(ContentContext.ContentState.CHAPTER);
            }
        });

    }

    @Override
    public void onBookInfosChanged(List<BookInfo> bookInfos) {
    }

    @Override
    public void onActiveBookInfoChanged(BookInfo activeBook) {
        new ParseJson2ChaptersTask().execute();
    }

    @Override
    public void onBookContentChanged(List<Chapter> chapters) {

        if (adapter == null) {
            adapter = new ChapterAdapter(getActivity(), chapters);
            setListAdapter(adapter);
        } else {
            adapter.setList(chapters);
        }
    }

    private static class ChapterAdapter extends BaseAdapter {

        private final LayoutInflater inflater;
        Context context;
        private List<Chapter> list;

        public ChapterAdapter(Context context, List<Chapter> chapters) {
            this.list = chapters;
            this.context = context;
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Chapter getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null)
                view = inflater.inflate(R.layout.general_list_item, null);
            TextView taskName = (TextView) view.findViewById(R.id.taskName);
            taskName.setText(getItem(position).getName());
            return view;
        }

        public void setList(List<Chapter> list) {
            this.list = list;
            notifyDataSetChanged();
        }
    }

    private class ParseJson2ChaptersTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            activeBook = SettingsContext.getInstance().getActiveBook();
            AssetManager assets = ApplicationContext.getContext().getAssets();
            InputStream stream = null;
            try {
                stream = assets.open(activeBook.getBookPath() + "/content.json");
            } catch (IOException e) {
                e.printStackTrace();
            }
            assert stream != null;
            Reader reader = new InputStreamReader(stream);

            Gson gson = new Gson();
            List<Chapter> chapters = Arrays.asList(gson.fromJson(reader, Chapter[].class));
            BookContext.getInstance().setChapters(chapters);

            return null;
        }
    }
}
