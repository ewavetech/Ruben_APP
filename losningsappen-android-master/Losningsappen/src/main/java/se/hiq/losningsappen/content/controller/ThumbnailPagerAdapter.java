package se.hiq.losningsappen.content.controller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

import se.hiq.losningsappen.R;
import se.hiq.losningsappen.VideoActivity;
import se.hiq.losningsappen.content.model.VideoLink;
import se.hiq.losningsappen.content.ui.ThumbnailPostProcessor;

/**
 * Created by petterstenberg on 2014-09-05.
 * //TODO Add class description
 */

public class ThumbnailPagerAdapter extends PagerAdapter {

    public static List<String> ignoreList = new ArrayList<String>();
    private static ImageLoaderConfiguration configuration;
    private final Context context;
    private final List<VideoLink> videoLinks;
    private final LayoutInflater inflater;

    public ThumbnailPagerAdapter(Context context, List<VideoLink> videoLinks) {
        this.context = context;
        this.videoLinks = videoLinks;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (configuration == null) configImageLoader(context.getApplicationContext());
    }

    public static void configImageLoader(Context context) {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageOnFail(R.drawable.failed_to_load_whole)
                .showImageOnLoading(R.drawable.loading_text_whole)
                .preProcessor(new ThumbnailPostProcessor(context))
                .resetViewBeforeLoading(true)
                .build();
        configuration = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options)
                .imageDownloader(new ThumbnailDownloader(context))
//                    .writeDebugLogs()
                .build();
        ImageLoader.getInstance().destroy();
        ImageLoader.getInstance().init(configuration);
    }

    @Override
    public int getCount() {
        return videoLinks.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        View view = inflater.inflate(R.layout.thumbnail_item, container, false);

        ImageView thumbnailView = (ImageView) view.findViewById(R.id.thumbnail_imageview);
        TextView thumbnailLabel = (TextView) view.findViewById(R.id.thumbnail_label);

        thumbnailLabel.setText(videoLinks.get(position).getTitle());

        final int finalPosition = position;

        if (!ignoreList.contains(videoLinks.get(position).getUrl())) {
            try {
                ImageLoader.getInstance().displayImage(videoLinks.get(position).getUrl(), thumbnailView, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        notifyDataSetChanged();
                    }
                });
            } catch (IllegalStateException e) {
                ImageLoader.getInstance().cancelDisplayTask(thumbnailView);
            }
        } else {
            thumbnailView.setImageResource(R.drawable.failed_to_load_whole);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startVideo(finalPosition);
            }
        });

        container.addView(view, position);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    private void startVideo(int urlId) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("videoLink", videoLinks.get(urlId).getUrl());
        context.startActivity(intent);
    }
}
