package com.nepo.nepotransfered.app;

import android.app.Application;
import android.content.Context;

import com.nepo.nepotransfered.db.TransferDB;
import com.nepo.nepotransfered.utils.SPUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * Created by lsiuhen on 2016/5/26.
 */
public class NepoApplication extends Application {

    private static NepoApplication mInstance = null;

    public static NepoApplication getInstance() {
        if (mInstance == null) {
            mInstance = new NepoApplication();
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        CustomCrashHandler mCustomCrashHandler = CustomCrashHandler.getInstance();
//        mCustomCrashHandler.setCustomCrashHanler(getApplicationContext());
//        x.Ext.init(this);
        initDbUtils();
        initImageLoader(getApplicationContext());


    }


    private void initDbUtils() {

        try {
            int flag = (int) SPUtils.get(this, "createTypes", 0);
            if (flag == 0) {
                long line = TransferDB.getTransferDB(this).inserDefaultType();
                if (line > 0) {
                    SPUtils.put(this, "createTypes", 1);
                }
            }

//            Constant.TRANSMAP = TransferDB.getSongInfoDB(this).querydAll();
//            Constant.getTransferValueList();


        } catch (Exception e) {
            System.out.println("-- e:" + e);
            e.printStackTrace();
        }


    }


    public void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context).threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .memoryCacheSize(2 * 1024 * 1024) //缓存到内存的最大数据
                .memoryCacheSize(50 * 1024 * 1024) //设置内存缓存的大小
                .diskCacheFileCount(200)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }



}
