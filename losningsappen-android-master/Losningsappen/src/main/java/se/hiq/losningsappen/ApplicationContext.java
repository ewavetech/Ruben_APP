package se.hiq.losningsappen;

import android.content.Context;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import se.hiq.losningsappen.common.util.cache.CacheManager;

/**
 * Created by petterstenberg on 2014-08-18.
 */
public class ApplicationContext extends android.app.Application {

    private static ApplicationContext instance;

    public static ApplicationContext getApplication(Context context) {
        return (ApplicationContext) context.getApplicationContext();
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
//        cacheManager = new CacheManager(this);

        initNostraImageLoader();
    }

    private void initNostraImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        CacheManager cacheManager = null;
    }

//    public CacheManager getCacheManager() {
//        return cacheManager;
//    }
}
