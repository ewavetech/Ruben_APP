package se.hiq.losningsappen.content.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import se.hiq.losningsappen.R;
import se.hiq.losningsappen.VideoPagerActivity;
import se.hiq.losningsappen.content.model.ContentContext;
import se.hiq.losningsappen.content.model.SubChapter;

/**
 * Created by petterstenberg on 2014-08-18.
 */

public class SubChapterArrayAdapter extends ArrayAdapter<SubChapter> {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private List<SubChapter> list;
    private ArrayList<String> video_links_list;
    private ArrayList<String> video_titles;
    private SparseArray<ThumbnailPagerAdapter> adapters;
    ViewHolder viewHolder;
    public int selected_item=-1;
    public SubChapterArrayAdapter(Context context, List<SubChapter> list) {
        super(context, R.layout.chapter, list);
        this.context = context;
        this.list = list;
        this.adapters = new SparseArray<ThumbnailPagerAdapter>();
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final SubChapter subChapter = list.get(position);

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.chapter, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.chapterName = (TextView) convertView.findViewById(R.id.chapterName);
            viewHolder.chapter_options=(LinearLayout)convertView.findViewById(R.id.chapter_options);
            viewHolder.assignment=(TextView)convertView.findViewById(R.id.assignment);
            viewHolder.movie=(TextView)convertView.findViewById(R.id.movie);

//            viewHolder.viewPagerContainer = convertView.findViewById(R.id.view_pager_container);
//
//            viewHolder.thumbnailList = (ViewPager) convertView.findViewById(R.id.videoLayout);
//            viewHolder.thumbnailList.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//            viewHolder.thumbnailList.setDrawingCacheEnabled(true);
//
//            viewHolder.dotIndicator = (CirclePageIndicator) convertView.findViewById(R.id.circle_page_indicator);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

//        final int videoListsize = subChapter.getVideoLinks().size();
//        if (videoListsize > 0) {
//            viewHolder.viewPagerContainer.setVisibility(View.VISIBLE);
//            if (adapters.get(position) == null) {
//                adapters.put(position, new ThumbnailPagerAdapter(context, subChapter.getVideoLinks()));
//            }
//            viewHolder.thumbnailList.setAdapter(adapters.get(position));
//            int offScreenLimit = (videoListsize % 2 == 0) ? videoListsize / 2 : (videoListsize - 1) / 2;
//            viewHolder.thumbnailList.setOffscreenPageLimit(offScreenLimit);
//            viewHolder.dotIndicator.setViewPager(viewHolder.thumbnailList);
//        } else {
//            viewHolder.viewPagerContainer.setVisibility(View.GONE);
//        }

        if(selected_item==position){
            viewHolder.chapter_options.setVisibility(View.VISIBLE);
        }
        else{
            viewHolder.chapter_options.setVisibility(View.GONE);
        }
        viewHolder.chapterName.setText(subChapter.getName());
        viewHolder.chapterName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                changeView(position);
            }
        });
        viewHolder.assignment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentContext.getInstance().setCurrentSubChapter(position);
                ContentContext.getInstance().setActionBarDescription(subChapter.getName());
                ContentContext.getInstance().setState(ContentContext.ContentState.SUBCHAPTER);
            }
        });
        viewHolder.movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startVideo(subChapter);

            }
        });
        return convertView;
    }
    public void changeView(int pos){
        if(selected_item!=pos){
            selected_item=pos;
        }
        else{
            selected_item=-1;
        }

        notifyDataSetChanged();
    }

    public void startVideo(SubChapter subChapter)
    {
        video_links_list=new ArrayList<String>();
        video_titles=new ArrayList<String>();
        Intent intent=new Intent(context, VideoPagerActivity.class);
        Log.i("videoLinksSize", subChapter.getVideoLinks().size()+"");
        for(int j=0; j<subChapter.getVideoLinks().size();j++){
            video_links_list.add(subChapter.getVideoLinks().get(j).getUrl());
            video_titles.add(subChapter.getVideoLinks().get(j).getTitle());
        }

        intent.putStringArrayListExtra("video_links", video_links_list);
        intent.putStringArrayListExtra("video_titles", video_titles);

        context.startActivity(intent);
    }
    public void setList(List<SubChapter> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void clearAdapterList() {
        adapters = null;
    }

    private static class ViewHolder {
        ViewPager thumbnailList;
        TextView chapterName;
        CirclePageIndicator dotIndicator;
        View viewPagerContainer;
        LinearLayout chapter_options;
        TextView assignment, movie;
    }
}
