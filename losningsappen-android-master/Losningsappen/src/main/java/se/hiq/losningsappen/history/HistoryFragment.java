package se.hiq.losningsappen.history;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ToggleButton;

import java.util.List;

import se.hiq.losningsappen.MainActivity;
import se.hiq.losningsappen.R;
import se.hiq.losningsappen.common.models.BackStackCapability;

/**
 * Created by petterstenberg on 13/08/14.
 */

public class HistoryFragment extends Fragment implements CompoundButton.OnCheckedChangeListener, BackStackCapability, HistoryContext.HistoryContentListener {

    private static final int URL_LOADER = 1;

    private static final String TAG = "HISTORY_FRAGMENT";

    private LinearLayout filterMenu;
    private ToggleButton notFinishedFilterButton;
    private ToggleButton finishedFilterButton;
    private ToggleButton chapterTestFilterButton;
    private HistoryFilterListener filterListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        HistoryContext.getInstance().addHistoryContentListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.history_list, container, false);

        ListView historyListView = (ListView) view.findViewById(R.id.history_list);
        filterMenu = (LinearLayout) view.findViewById(R.id.filter_menu);

        notFinishedFilterButton = (ToggleButton) view.findViewById(R.id.not_finished_filter_button);
        notFinishedFilterButton.setOnCheckedChangeListener(this);

        finishedFilterButton = (ToggleButton) view.findViewById(R.id.finished_filter_button);
        finishedFilterButton.setOnCheckedChangeListener(this);

        chapterTestFilterButton = (ToggleButton) view.findViewById(R.id.chapter_test_filter_button);
        chapterTestFilterButton.setOnCheckedChangeListener(this);

        HistoryListAdapter listAdapter = new HistoryListAdapter(getActivity(), HistoryContext.getInstance().getHistoryList());
        filterListener = listAdapter;

        historyListView.setAdapter(listAdapter);
        historyListView.setOnItemClickListener(listAdapter);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HistoryContext.getInstance().removeHistoryContentListener(this);
        filterListener = null;

    }

    public void toggleFilterMenu() {
        if (filterMenu != null) {
            if (filterMenu.getVisibility() == View.GONE) {
                filterMenu.setVisibility(View.VISIBLE);
            } else {
                filterMenu.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        notifyFilterListener();
    }

    private void notifyFilterListener() {
        filterListener.onFilteringChange(finishedFilterButton.isChecked(),
                notFinishedFilterButton.isChecked(), chapterTestFilterButton.isChecked(),
                HistoryContext.getInstance().getHistoryList());
    }

    @Override
    public void onBackPressed() {
        ((MainActivity) getActivity()).setTab(0);
    }

    @Override
    public void onHistoryContentChanged() {
        notifyFilterListener();
    }

    public interface HistoryFilterListener {
        public void onFilteringChange(boolean finishedPushed, boolean notFinishedPushed, boolean chapterTestPushed, List<Historyable> originalList);
    }
}
