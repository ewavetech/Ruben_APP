package se.hiq.losningsappen.content.controller;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import se.hiq.losningsappen.R;
import se.hiq.losningsappen.content.model.VideoLink;

/**
 * Created by petterstenberg on 2014-08-18.
 * <p/>
 * //TODO Add class description
 */

public class ThumbnailAdapter extends BaseAdapter {

    private final Context context;
    private List<VideoLink> videoLinks;
    private CreateThumbnailTask thumbnailTask;

    public ThumbnailAdapter(Context context, List<VideoLink> videoLinks) {
        this.context = context;
        this.videoLinks = videoLinks;
    }

    @Override
    public int getCount() {
        return videoLinks.size();
    }

    @Override
    public Object getItem(int i) {
        return videoLinks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final ThumbnailViewHolder viewHolder;

        if (view == null) {
            view = ((Activity) context).getLayoutInflater().inflate(R.layout.thumbnail_item, viewGroup, false);

            viewHolder = new ThumbnailViewHolder();
            viewHolder.thumbnailView = (ImageView) view.findViewById(R.id.thumbnail_imageview);
            viewHolder.thumbnailLabel = (TextView) view.findViewById(R.id.thumbnail_label);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ThumbnailViewHolder) view.getTag();
        }
        viewHolder.thumbnailLabel.setText(videoLinks.get(i).getTitle());

        final View finalView = view;
//        ApplicationContext.getApplication(context).getCacheManager().getBitmapFromCache(hashedClipUrl, new CacheManager.ImageCacheListener() {
//                    @Override
//                    public void onNoCachedImage() {
//                        viewHolder.thumbnailView.setImageResource(R.drawable.placeholder);
//                    }
//                }, viewHolder.thumbnailView);

        return view;
    }

    private static class ThumbnailViewHolder {
        public ImageView thumbnailView;
        public TextView thumbnailLabel;
    }
}
