package com.client.capturephoto;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApplication extends Application
{
    
    @Override
    public void onCreate()
    {
        super.onCreate();
        initImageLoader(this);
    }
    
    public void initImageLoader(Context context)
    {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                // .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
        
//        ImageLoaderConfiguration config1 = new ImageLoaderConfiguration.Builder(
//                this).memoryCacheExtraOptions(480, 800) // default = device screen dimensions
//                .threadPoolSize(3)
//                // default
//                .threadPriority(Thread.NORM_PRIORITY - 1)
//                // default
//                .denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
//                // default
//                .discCache(new UnlimitedDiscCache(cacheDir))
//                // default
//                .discCacheSize(50 * 1024 * 1024)
//                .discCacheFileCount(100)
//                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
//                // default
//                .tasksProcessingOrder(QueueProcessingType.FIFO)
//                // default
//                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
//                // default
//                .enableLogging()
//                .build();
    }
}
