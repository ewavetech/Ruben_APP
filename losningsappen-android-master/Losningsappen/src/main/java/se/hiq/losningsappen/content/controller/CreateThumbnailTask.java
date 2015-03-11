package se.hiq.losningsappen.content.controller;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import se.hiq.losningsappen.R;
import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * Created by Petter Stenberg on 2014-08-07.
 */

public class CreateThumbnailTask extends AsyncTask<Void, Void, Bitmap> {

    public static final double THUMBNAIL_WIDTH_RATIO = 0.9;
    public static final int THUMBNAIL_WIDTH = 640;
    private static final String TAG = "THUMBNAIL_TASK";
    private final String clipUrl;
    private final PagerAdapter pagerAdapter;
    private ImageView imageView;

    public CreateThumbnailTask(Context context, View containerView, PagerAdapter pagerAdapter, String clipUrl) {
        View containerView1 = containerView;
        this.imageView = (ImageView) containerView.findViewById(R.id.thumbnail_imageview);
        this.clipUrl = clipUrl;
        Context context1 = context;
        String directoryPath = context.getCacheDir().getPath() + "/";
        this.pagerAdapter = pagerAdapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

//        containerView.findViewById(R.id.thumbnail_progressbar).setVisibility(View.VISIBLE);
    }

    @Override
    protected Bitmap doInBackground(Void... voids) {

        Log.d(TAG, "Started bitmapTask");

        Bitmap tempBitmap;
        Bitmap lightweightBitmap = null;
        Bitmap smallerBitmap = null;

        FFmpegMediaMetadataRetriever metadataRetriever = new FFmpegMediaMetadataRetriever();

        try {
            metadataRetriever.setDataSource(clipUrl);

            tempBitmap = metadataRetriever.getFrameAtTime(1000000,
                    FFmpegMediaMetadataRetriever.OPTION_CLOSEST);
            lightweightBitmap = tempBitmap.copy(Bitmap.Config.RGB_565, false);
            tempBitmap.recycle();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            cancel(true);
        } finally {
            metadataRetriever.release();
            metadataRetriever = null;
        }

        if (lightweightBitmap != null) {
            double widthHeightRatio = (double) lightweightBitmap.getHeight() / (double) lightweightBitmap.getWidth();

            smallerBitmap = Bitmap.createScaledBitmap(lightweightBitmap, THUMBNAIL_WIDTH, (int) (THUMBNAIL_WIDTH * widthHeightRatio), false);
            lightweightBitmap.recycle();

//            ApplicationContext.getApplication(context).getCacheManager().addBitmapToCache(hashedClipUrl, smallerBitmap);
        }

        return smallerBitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {

        if (bitmap != null) imageView.setImageBitmap(bitmap);
        pagerAdapter.notifyDataSetChanged();
        Log.d(TAG, "SET BITMAP TO IMAGE_VIEW");
    }

    @Override
    protected void onCancelled(Bitmap bitmap) {
        imageView.setImageResource(R.drawable.placeholder);

    }
}
