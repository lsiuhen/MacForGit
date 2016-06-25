package com.nepo.nepotransfered.loadsd;

import android.content.ContentResolver;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;


import com.nepo.nepotransfered.model.MediaMusicModel;
import com.nepo.nepotransfered.utils.MusicUtils;

import java.util.ArrayList;

/**
 * Created by lsiuhen on 2016/4/23.
 * <p/>
 * 扫描音乐文件
 */
public class SDMusicLoadTask extends ContextWrapper {


    public SDMusicLoadTask(Context mContext) {
        super(mContext);
    }

    public ArrayList<MediaMusicModel> getLocalAudioList() {
    // 存放扫描到的音乐文件
        ArrayList<MediaMusicModel> musicList = new ArrayList<>();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
        if (cursor.moveToFirst()) {
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToPosition(i);
                MediaMusicModel info = new MediaMusicModel();
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                long albumId = cursor.getLong(cursor.getColumnIndex( MediaStore.Audio.Media.ALBUM_ID));
                // 查询音乐的名字
                info.setName(cursor.getString(cursor
                        .getColumnIndex("_display_name")));

                // 查询音乐的歌手
                info.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));

                // 查询音乐的路径
                info.setPath(cursor.getString(cursor
                        .getColumnIndex(MediaStore.Audio.Media.DATA)));
//                info.setBitmap(MediaStore.Video.Thumbnails.getThumbnail(resolver, id, MediaStore.Images.Thumbnails.MICRO_KIND, options));
                info.setBitmap(MusicUtils.getArtwork(this, id, albumId, true));
                long musicTime = cursor.getLong(cursor
                        .getColumnIndex(MediaStore.Audio.Media.DURATION));

                long zmiao = musicTime / 1000;
                long fen = zmiao / 60;
                long smiao = zmiao % 60;
                String tempFen;
                String tempMiao;
                if (fen < 10) {
                    tempFen = "0" + fen;
                } else {
                    tempFen = fen + "";
                }
                if (smiao < 10) {
                    tempMiao = "0" + smiao;
                } else {
                    tempMiao = smiao + "";
                }

                String time = tempFen + ":" + tempMiao;
                info.setTime(time);
                musicList.add(info);
            }
        }
        cursor.close();

        //

        return musicList;
    }





}
