package se.hiq.losningsappen.content;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import se.hiq.losningsappen.R;
import se.hiq.losningsappen.content.controller.SubChapterArrayAdapter;
import se.hiq.losningsappen.content.controller.ThumbnailPagerAdapter;
import se.hiq.losningsappen.content.model.BookContext;
import se.hiq.losningsappen.content.model.Chapter;
import se.hiq.losningsappen.content.model.ContentContext;
import se.hiq.losningsappen.content.model.SubChapter;


public class ChapterFragment extends ListFragment {

    SubChapterArrayAdapter adapter;
    private List<SubChapter> subChapters;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        BookContext.getInstance().getChapters(new BookContext.GetChaptersCallback() {
            @Override
            public void onGetChapters(List<Chapter> chapters) {
                subChapters = chapters.get(ContentContext.getInstance()
                        .getCurrentChapter()).getSubChapters();

                adapter = new SubChapterArrayAdapter(getActivity(), subChapters);
                setListAdapter(adapter);
            }
        });

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
        getListView().setScrollingCacheEnabled(true);
        getListView().setPersistentDrawingCache(ViewGroup.PERSISTENT_SCROLLING_CACHE);
    }

    @Override
    public void onResume() {
        super.onResume();

        BookContext.getInstance().getChapters(new BookContext.GetChaptersCallback() {
            @Override
            public void onGetChapters(List<Chapter> chapters) {
                subChapters = chapters.get(ContentContext.getInstance()
                        .getCurrentChapter()).getSubChapters();
                adapter.setList(subChapters);
                ThumbnailPagerAdapter.configImageLoader(getActivity());
                getActivity().setTitle("Kapitel " + (ContentContext.getInstance().getCurrentChapter() + 1));
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        subChapters = null;
        adapter.clearAdapterList();
    }
}