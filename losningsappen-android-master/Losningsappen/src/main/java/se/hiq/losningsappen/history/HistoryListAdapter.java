package se.hiq.losningsappen.history;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import se.hiq.losningsappen.R;
import se.hiq.losningsappen.chaptertest.ChapterTestActivity;
import se.hiq.losningsappen.chaptertest.models.ChapterTestResult;
import se.hiq.losningsappen.content.SubtaskActivity;
import se.hiq.losningsappen.content.model.Subtask;

/**
 * Created by petterstenberg on 2014-08-13.
 */
public class HistoryListAdapter extends BaseAdapter implements HistoryFragment.HistoryFilterListener,
        AdapterView.OnItemClickListener {

    private static final int SUBTASK_OR_CHAPTERTEST_COLUMN = 1;
    private final Context context;
    private final LayoutInflater inflater;
    private List<Historyable> historyList;

    public HistoryListAdapter(Context context, List<Historyable> historyList) {
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.historyList = historyList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return historyList.size();
    }

    @Override
    public Historyable getItem(int i) {
        return historyList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.history_list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.nameTextView = (TextView) convertView.findViewById(R.id.history_item_description);
            viewHolder.finishedImageView = (ImageView) convertView.findViewById(R.id.finish_state_image);
            viewHolder.pieChart = (PieChartView) convertView.findViewById(R.id.chapter_pie_chart);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (historyList.get(position).getClass().equals(Subtask.class)) {
            Subtask subtask = (Subtask) historyList.get(position);
            viewHolder.nameTextView.setText(subtask.getParentName() + " " + subtask.getName());
            viewHolder.finishedImageView.setVisibility(View.VISIBLE);
            viewHolder.pieChart.setVisibility(View.GONE);
            viewHolder.finishedImageView.setImageLevel((
                    (Subtask) historyList.get(position)).getStatus().ordinal());
        } else {
            viewHolder.nameTextView.setText(historyList.get(position).getName());
            viewHolder.finishedImageView.setVisibility(View.GONE);
            viewHolder.pieChart.setVisibility(View.VISIBLE);
            viewHolder.pieChart.setCompletedRatio((float) (
                    (ChapterTestResult) historyList.get(position)).getResultRatio());
        }

        return convertView;
    }

    @Override
    public void onFilteringChange(boolean finishedPushed, boolean notFinishedPushed, boolean chapterTestPushed, List<Historyable> originalList) {
        List<Historyable> filteredList = new ArrayList<Historyable>();

        if (!finishedPushed && !notFinishedPushed && !chapterTestPushed) {
            historyList = originalList;
        } else {
            for (Historyable item : originalList) {
                if (item.getClass().equals(Subtask.class)) {
                    if (finishedPushed && ((Subtask) item).getStatus().equals(Subtask.Status.PASSED))
                        filteredList.add(item);
                    if (notFinishedPushed && ((Subtask) item).getStatus().equals(Subtask.Status.UNPASSED))
                        filteredList.add(item);
                } else {
                    if (chapterTestPushed) filteredList.add(item);
                }
            }
            historyList = filteredList;
        }
        notifyDataSetChanged();
    }

    private void openIntent(int position) {
        Intent intent;

        Class intentType = (getItem(position).getClass().equals(Subtask.class))
                ? SubtaskActivity.class : ChapterTestActivity.class;

        intent = new Intent(context, intentType);
        HistoryContext.getInstance().setCurrentHistoryable(getItem(position));
        context.startActivity(intent);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        openIntent(i);
    }

    private enum HistoryItemMode {SUBTASK, CHAPTER_TEST}

    private static class ViewHolder {
        TextView nameTextView;
        ImageView finishedImageView;
        PieChartView pieChart;
    }
}
