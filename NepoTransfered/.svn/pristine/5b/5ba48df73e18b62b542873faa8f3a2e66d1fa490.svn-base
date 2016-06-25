package com.nepo.nepotransfered.loadsd;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;


import com.nepo.nepotransfered.model.MediaImgModel;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;

/**
 * Created by lsiuhen on 2016/4/23.
 * <p/>
 * 扫描图片文件
 */
public class SDImgLoadTask extends ContextWrapper {


    public SDImgLoadTask(Context mContext) {
        super(mContext);
    }

    public ArrayList<MediaImgModel> getLocalImageList() {
    // 存放扫描到的音乐文件
        ArrayList<MediaImgModel> mGruopList = new ArrayList<>();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Images.Media.DATE_TAKEN);
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                // 获取图片的路径
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                String id=cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                // 获取该图片的所在文件夹的路径
                File file = new File(path);
                String parentName = "";
                if (file.getParentFile() != null) {
                    parentName = file.getParentFile().getName();
                } else {
                    parentName = file.getName();
                }
                // 构建一个imageGroup对象
                MediaImgModel item = new MediaImgModel();
                // 设置imageGroup的文件夹名称
                item.setDirName(parentName);

                // 寻找该imageGroup是否是其所在的文件夹中的第一张图片
                int searchIdx = mGruopList.indexOf(item);
                if (searchIdx >= 0) {
                    // 如果是，该组的图片数量+1
                    MediaImgModel imageGroup = mGruopList.get(searchIdx);
                    imageGroup.addImage(path);
                } else {
                    // 否则，将该对象加入到groupList中
                    item.addImage(path);
                    mGruopList.add(item);
                }
            }
        }
        cursor.close();
        return mGruopList;
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
