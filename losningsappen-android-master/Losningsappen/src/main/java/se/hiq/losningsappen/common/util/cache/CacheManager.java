package se.hiq.losningsappen.common.util.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

/**
 * Created by petterstenberg on 2014-09-02.
 * <p/>
 * This class blablabla //TODO
 */
public class CacheManager {

    public static final String APPLICATION_DISK_CACHE_NAME = "APPLICATION_DISK_CACHE";
    public final static int DISK_CACHE_SIZE = 50 * 1024 * 1024;
    private final MemoryBitmapCache memoryCache;
    private final DiskLruImageCache imageDiskCache;
    private final Handler handler;

    public CacheManager(Context context) {

        Context context1 = context;
        handler = new Handler(Looper.getMainLooper());

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        memoryCache = new MemoryBitmapCache(context);
        imageDiskCache = new DiskLruImageCache(context, APPLICATION_DISK_CACHE_NAME, DISK_CACHE_SIZE,
                Bitmap.CompressFormat.JPEG, 100);
    }

    public void getBitmapFromCache(final String key, final ImageCacheListener listener, ImageView imageView) {
        Bitmap tempBitmap = memoryCache.getBitmapFromMemCache(key);

        if (tempBitmap == null) {
            new GetBitmapFromCacheTask(key, listener, imageView).execute();
        } else {
            imageView.setImageBitmap(tempBitmap);
        }
    }

    public void addBitmapToCache(String key, Bitmap bitmap) {
        new CacheBitmapTask(key, bitmap).execute();
    }

    public void clearMemoryCache() {
        memoryCache.clearCache();
    }

    public interface ImageCacheListener {
        void onNoCachedImage();
    }

    private class GetBitmapFromCacheTask extends AsyncTask<Void, Void, Bitmap> {

        private final ImageView imageView;
        private ImageCacheListener listener;
        private String key;

        public GetBitmapFromCacheTask(String key, ImageCacheListener listener, ImageView imageView) {
            this.key = key;
            this.listener = listener;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... voids) {

            Bitmap tempBitmap = null;
            if (imageDiskCache.containsKey(key)) {
                Bitmap bitmap = imageDiskCache.getBitmap(key);
                memoryCache.addBitmapToMemoryCache(key, bitmap);
                tempBitmap = bitmap;
            } else {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onNoCachedImage();
                        listener = null;
                    }
                });
            }
            return tempBitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }

    private class CacheBitmapTask extends AsyncTask<Void, Void, Void> {

        private final String key;
        private final Bitmap bitmap;

        private CacheBitmapTask(String key, Bitmap bitmap) {
            this.key = key;
            this.bitmap = bitmap;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            imageDiskCache.put(key, bitmap);
            memoryCache.addBitmapToMemoryCache(key, bitmap);

            return null;
        }
    }
}
