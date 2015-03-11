package se.hiq.losningsappen;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import se.hiq.losningsappen.common.models.NokActivity;
import se.hiq.losningsappen.content.controller.ThumbnailPagerAdapter;
import se.hiq.losningsappen.content.model.SubChapter;
import se.hiq.losningsappen.content.model.VideoLink;


public class VideoPagerActivity extends NokActivity{
    ViewPager thumbnailList;
    CirclePageIndicator dotIndicator;
    View viewPagerContainer;
    private List<SubChapter> list;
    private SparseArray<ThumbnailPagerAdapter> adapters;
    private ArrayList<String> video_links_list;
    private ArrayList<String> video_titles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_pager);
        this.adapters = new SparseArray<ThumbnailPagerAdapter>();
        viewPagerContainer = findViewById(R.id.view_pager_container);

        thumbnailList = (ViewPager) findViewById(R.id.videoLayout);
        thumbnailList.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        thumbnailList.setDrawingCacheEnabled(true);
        setTitle("filmgenomgångar");
        dotIndicator = (CirclePageIndicator) findViewById(R.id.circle_page_indicator);
        video_links_list=getIntent().getStringArrayListExtra("video_links");
        video_titles=getIntent().getStringArrayListExtra("video_titles");
        final int videoListsize = video_links_list.size();
        Log.i("SizeofVideoLinks",video_links_list.size()+"");
        if (videoListsize > 0) {
            ArrayList<VideoLink> video_list=new ArrayList<VideoLink>();
            for(int k=0;k<video_links_list.size();k++){
                Log.i("video link", video_links_list.get(k) + "-" + video_titles.get(k));
                VideoLink video_link = new VideoLink(video_titles.get(k),video_links_list.get(k));
                video_list.add(video_link);
            }
            viewPagerContainer.setVisibility(View.VISIBLE);
            if (adapters.get(0) == null) {

                adapters.put(0, new ThumbnailPagerAdapter(this,video_list ));
            }
            thumbnailList.setAdapter(adapters.get(0));
            int offScreenLimit = (videoListsize % 2 == 0) ? videoListsize / 2 : (videoListsize - 1) / 2;
            thumbnailList.setOffscreenPageLimit(offScreenLimit);
            dotIndicator.setViewPager(thumbnailList);
        } else {
            viewPagerContainer.setVisibility(View.GONE);
            Toast.makeText(this,"No Videos", Toast.LENGTH_LONG).show();
        }
    }

}
