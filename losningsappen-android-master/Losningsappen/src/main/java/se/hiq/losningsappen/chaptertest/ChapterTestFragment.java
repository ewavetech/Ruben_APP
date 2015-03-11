package se.hiq.losningsappen.chaptertest;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import java.util.List;

import se.hiq.losningsappen.MainActivity;
import se.hiq.losningsappen.R;
import se.hiq.losningsappen.chaptertest.models.ChapterTest;
import se.hiq.losningsappen.chaptertest.models.ChapterTestContext;
import se.hiq.losningsappen.chaptertest.models.ChapterTestResult;
import se.hiq.losningsappen.common.models.BackStackCapability;
import se.hiq.losningsappen.common.models.settings.SettingsContext;
import se.hiq.losningsappen.history.HistoryContext;
import se.hiq.losningsappen.history.Historyable;
import se.hiq.losningsappen.history.PieChartView;

/**
 * Created by Naknut on 04/07/14.
 */
public class ChapterTestFragment extends ListFragment implements BackStackCapability, HistoryContext.HistoryContentListener {

    public static final String SHOULD_CLEAR_KEY = "shouldClear";
    private static final String TAG = "CHAPTER_TEST_FRAGMENT";
    ChapterTestArrayAdapter adapter;

    @Override
    public void onBackPressed() {
        ((MainActivity) getActivity()).setTab(1);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        HistoryContext.getInstance().addHistoryContentListener(this);

        if (SettingsContext.getInstance().getActiveBook() != null) {
            AssetManager assets = getActivity().getApplicationContext().getAssets();
            InputStream stream = null;

            try {
                stream = assets.open(SettingsContext.getInstance().getActiveBook().getBookPath() + "/chapterTests.json");
            } catch (IOException e) {
                e.printStackTrace();
            }

            assert stream != null;
            Reader reader = new InputStreamReader(stream);

            try {
                stream.reset();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Gson gson = new Gson();
            List<ChapterTest> chapterTests = Arrays.asList(gson.fromJson(reader, ChapterTest[].class));
            ChapterTestContext.getInstance().setChapterTests(chapterTests);
            adapter = new ChapterTestArrayAdapter(getActivity(), chapterTests);
            setListAdapter(adapter);
        }
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(), ChapterTestActivity.class);
        intent.putExtra("testPosition", position);
        intent.putExtra(SHOULD_CLEAR_KEY, true);
        startActivity(intent);
    }

    @Override
    public void onHistoryContentChanged() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HistoryContext.getInstance().removeHistoryContentListener(this);
    }

    private static class ChapterTestListViewHolder {
        TextView taskName;
        PieChartView pieChart;
    }

    private static class ChapterTestArrayAdapter extends ArrayAdapter<ChapterTest> {

        Context context;
        List<ChapterTest> list;

        public ChapterTestArrayAdapter(Context context, List<ChapterTest> list) {
            super(context, R.layout.chapter, list);
            this.context = context;
            this.list = list;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            ChapterTestListViewHolder viewHolder = null;

            if (view == null) {
                view = ((Activity) context).getLayoutInflater().inflate(R.layout.chapter_test_list_item, parent, false);
                viewHolder = new ChapterTestListViewHolder();

                viewHolder.taskName = (TextView) view.findViewById(R.id.history_item_description);
                viewHolder.pieChart = (PieChartView) view.findViewById(R.id.chapter_pie_chart);

                view.setTag(viewHolder);
            } else {
                viewHolder = (ChapterTestListViewHolder) view.getTag();
            }
            viewHolder.taskName.setText(list.get(position).getTitle());

            ChapterTestResult previousResult = determineIfPreviousResult(position);
            float completedRatio = (previousResult != null) ? (float) previousResult.getResultRatio() : 0;

            viewHolder.pieChart.setCompletedRatio(completedRatio);


            return view;
        }

        private ChapterTestResult determineIfPreviousResult(int position) {

            ChapterTestResult testResult = null;

            for (Historyable item : HistoryContext.getInstance().getHistoryList()) {
                String testTitle = list.get(position).getTitle();
                if (item.getClass().equals(ChapterTestResult.class)
                        && (item.equals(testTitle))) testResult = (ChapterTestResult) item;
            }
            return testResult;
        }
    }
}
