package com.nepo.nepotransfered.service;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.Toast;

import com.nepo.nepotransfered.adapter.MediaImageAdapter;
import com.nepo.nepotransfered.adapter.MediaMusicAdapter;
import com.nepo.nepotransfered.adapter.MediaVideoAdapter;
import com.nepo.nepotransfered.app.Constant;
import com.nepo.nepotransfered.db.TransferDB;
import com.nepo.nepotransfered.loadsd.SDImgLoadTask;
import com.nepo.nepotransfered.loadsd.SDMusicLoadTask;
import com.nepo.nepotransfered.loadsd.SDVideoLoadTask;
import com.nepo.nepotransfered.model.MediaImgModel;
import com.nepo.nepotransfered.model.MediaMusicModel;
import com.nepo.nepotransfered.model.MediaVideoModel;
import com.nepo.nepotransfered.model.TransferModel;
import com.nepo.nepotransfered.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsiuhen on 2016/6/1.
 */
public class LocalMediaIntentService extends IntentService {

    private ArrayList<MediaImgModel> mediaImgArrayList;
    private ArrayList<MediaMusicModel> constantMediaMusicInfoList;
    private ArrayList<MediaVideoModel> constantMediaVideoInfoList;

    public static final String EXTRA_PIC="EXTRA_PIC";
    public static final String EXTRA_MUSIC="EXTRA_MUSIC";
    public static final String EXTRA_VIDEO="EXTRA_VIDEO";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *  Used to name the worker thread, important only for debugging.
     */
    public LocalMediaIntentService() {
        super("LocalMediaIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String extFlag = intent.getDataString();

        if(extFlag.equals(EXTRA_PIC)){
            //加载 本地图片
            loadImg();
        }else if(extFlag.equals(EXTRA_MUSIC)){
            //加载 本地音乐
            loadMusic();
        }else if(extFlag.equals(EXTRA_VIDEO)){
            //加载 本地图片
            loadVideo();
        }


    }

    private void loadImg() {
        SDImgLoadTask sdPicLoadTask = new SDImgLoadTask(this);
        mediaImgArrayList = sdPicLoadTask.getLocalImageList();
        // 官方推荐通过LocalBroadcastManager来发送广播进行UI更新
        Intent dataIntent = new Intent(Constant.RECEIVER_LOCALIMG_ACTION);
        dataIntent.putExtra(Constant.RECEIVER_LOCALIMG_ACTION, mediaImgArrayList);
        LocalBroadcastManager.getInstance(this).sendBroadcast(dataIntent);
    }

    private void loadMusic() {
        SDMusicLoadTask sdMusicLoadTask = new SDMusicLoadTask(this);
        constantMediaMusicInfoList = sdMusicLoadTask.getLocalAudioList();
        Intent dataIntent = new Intent(Constant.RECEIVER_LOCALMUSIC_ACTION);
        dataIntent.putExtra(Constant.RECEIVER_LOCALMUSIC_ACTION, constantMediaMusicInfoList);
        LocalBroadcastManager.getInstance(this).sendBroadcast(dataIntent);
    }

    private void loadVideo() {
        SDVideoLoadTask sdVideoLoadTask = new SDVideoLoadTask(this);
        constantMediaVideoInfoList = sdVideoLoadTask.getLocalVideoList();
        Intent dataIntent = new Intent(Constant.RECEIVER_LOCALVIEDO_ACTION);
        dataIntent.putExtra(Constant.RECEIVER_LOCALVIEDO_ACTION, constantMediaVideoInfoList);
        LocalBroadcastManager.getInstance(this).sendBroadcast(dataIntent);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
       // Toast.makeText(this,"LocalMediaIntentService onDestroy", Toast.LENGTH_SHORT).show();
    }

}
