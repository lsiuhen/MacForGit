package com.nepo.nepotransfered.loadsd;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;

import com.nepo.nepotransfered.model.MediaVideoModel;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by lsiuhen on 2016/4/23.
 * <p/>
 * 扫描视频文件
 */
public class SDVideoLoadTask extends ContextWrapper {


    public SDVideoLoadTask(Context mContext) {
        super(mContext);
    }

    public ArrayList<MediaVideoModel> getLocalVideoList() {
    // 存放扫描到的音乐文件
        ArrayList<MediaVideoModel> videoList = new ArrayList<>();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Video.Media.DEFAULT_SORT_ORDER);
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                MediaVideoModel info = new MediaVideoModel();
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.Media._ID));
                info.setPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATA)));
                info.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.TITLE)));
                info.setDisplayName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME)));
                info.setBitmap(MediaStore.Video.Thumbnails.getThumbnail(resolver, id, MediaStore.Images.Thumbnails.MICRO_KIND, options));
                // info.setSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.SIZE)));
                info.setMimeType(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.MIME_TYPE)));
                File file=new File(info.getPath());
                info.setSize(bytes2kb(file.length()));
                videoList.add(info);
            }
        }
        cursor.close();

        //

        return videoList;
    }


    /**
     * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
     *
     * @param bytes
     * @return
     */
    private String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        if (returnValue > 1)
            return (returnValue + "MB");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        return (returnValue + "KB");
    }


}
