package com.nepo.nepotransfered.app;

import com.nepo.nepotransfered.R;
import com.nepo.nepotransfered.model.MediaMusicModel;
import com.nepo.nepotransfered.model.TransferModel;
import com.nepo.nepotransfered.model.TransferProgressModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsiuhen on 2016/5/27.
 */
public class Constant {
    /**
     * 数据库名
     */
    public static final String DB_NAME = "nepo_transfer.db";

    /**
     * 传输类型
     */
    public final static String TRANSTYPE_PIC = "图片";
    public final static String TRANSTYPE_VIDEO = "视频";
    public final static String TRANSTYPE_MUSIC = "音乐";
    public final static String TRANSTYPE_APP = "应用";
    public final static String TRANSTYPE_OFFMAP = "离线地图包";
    public final static String TRANSTYPE_ROM = "ROM包";
    public final static String TRANSTYPE_FILE = "文件";

    /**
     * API URL
     */
    public static final String GETAPPLISTURL = "http://www.trehere.com:8899/car/webcar/data?type=init";

    /**
     * 下载状态
     */
    public static final int STATE_NONE = -1;
    /**
     * 开始下载
     */
    public static final int STATE_START = 0;
    /**
     * 等待中
     */
    public static final int STATE_WAITING = 1;
    /**
     * 下载中
     */
    public static final int STATE_DOWNLOADING = 2;
    /**
     * 暂停
     */
    public static final int STATE_PAUSED = 3;
    /**
     * 下载完毕
     */
    public static final int STATE_DOWNLOADED = 4;
    /**
     * 下载失败
     */
    public static final int STATE_ERROR = 5;
    /**
     * 删除下载成功
     */
    public static final int STATE_DELETE = 6;

    /**
     * receiver action
     */
    public static final String RECEIVER_LOCALIMG_ACTION = "RECEIVER_LOCALIMG_ACTION";
    public static final String RECEIVER_LOCALMUSIC_ACTION = "RECEIVER_LOCALMUSIC_ACTION";
    public static final String RECEIVER_LOCALVIEDO_ACTION = "RECEIVER_LOCALVIEDO_ACTION";

    public static final String SP_PICURL = "SP_PICURL";
    public static final String SP_PLATENUMBER = "SP_PLATENUMBER";
    public static final String SP_PLATEOPENNUMBER = "SP_PLATEOPENNUMBER";
    public static final String SP_FRAMENUMBER = "SP_FRAMENUMBER";
    public static final String SP_ENGINENUMBER = "SP_ENGINENUMBER";
    public static final String SP_TRANSPROGRESSNUM = "SP_TRANSPROGRESSNUM";
    //正在传输中标示
    public static final String SP_TRANSPROGRESSSTATUS = "SP_TRANSPROGRESSSTATUS";

    /**
     * dragGridview
     */
    public static final String USER = "user";
    public static final String PROVINCE_ID = "province_id";
    public static final String PROVINCE_NAME = "province_name";
    public static final String PROVINCE_DRAWABLE = "PROVINCE_DRAWABLE";
    public static final String PROVINCE = "province";
    public static final String PROVINCE_ARR = "province_arr";
    public static final String IS_FIRST = "is_first";

    /**
     * fragment 进度是否显示
     */
    public static final String SP_MUSICSEEKCIRCLE = "SP_MUSICSEEKCIRCLE";
    public static final String SP_VIDEOSEEKCIRCLE = "SP_VIDEOSEEKCIRCLE";
    public static final String SP_IMGSEEKCIRCLE = "SP_IMGSEEKCIRCLE";
    public static final String SP_MAPSEEKCIRCLE = "SP_MAPSEEKCIRCLE";
    public static final String SP_UPDATESEEKCIRCLE = "SP_UPDATESEEKCIRCLE";
    public static final String SP_APPSEEKCIRCLE = "SP_APPSEEKCIRCLE";

    /**
     * Observe msgType
     */
    public static final String transComplateMsg="transComplateMsg";
    // 开始文件传输，【首页判断当前显示哪个进度球，】
    public static final String transStartTransMsg="transStartTransMsg";
    public static final String transErrMsg="transErrMsg";
    public static final String transPauseMsg="transPauseMsg";
    public static final String transAddMsg="transAddMsg";
    public static final String transProgressMsg="transProgressMsg";
    public static final String transConnectionMsg="transConnectionMsg";
    // 传输过程中，更新fragment 进度球进度
    public static final String transFragmentMsg="transFragmentMsg";

    /**
     * transfer传输界面，顶部进度显示
     */
    public static final String SP_TRANSFER = "SP_TRANSFER";
    /**
     * 全局数据[扫描到的音乐]
     */
    public static ArrayList<MediaMusicModel> mediaMusicInfoList;

    /**
     * 正在进行的Json传输列表
     * 心跳包检测专属队列[检测是否有数据存在]
     */
    public static List<TransferProgressModel> TRANSPROGRESSINGJSONLIST = new ArrayList<>();

    /**
     * 传输类型，对应的背景图
     *
     * @return
     */
    public static List<Integer> getResourceId() {
        List<Integer> resourceList = new ArrayList<>();
        resourceList.add(R.drawable.transmission_menu_music_nor);
        resourceList.add(R.drawable.transmission_menu_video_nor);
        resourceList.add(R.drawable.transmission_menu_picture_nor);
        resourceList.add(R.drawable.transmission_menu_map_nor);
        resourceList.add(R.drawable.transmission_menu_update_nor);
        resourceList.add(R.drawable.transmission_menu_application_nor);

        return resourceList;
    }

    /**
     * 封装传输列表信息 为JsonArray 发送给车机端
     *
     * @return
     */
    public static String getTransJsonArrayByList() {
        System.out.println("-- list:"+TRANSPROGRESSINGJSONLIST);
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < TRANSPROGRESSINGJSONLIST.size(); i++) {
            JSONObject object = new JSONObject();
            try {
                object.put("transType", TRANSPROGRESSINGJSONLIST.get(i).getTransType());
                object.put("transName", TRANSPROGRESSINGJSONLIST.get(i).getTransName());
                object.put("transSize", TRANSPROGRESSINGJSONLIST.get(i).getTransLength());
                object.put("transHashCode", TRANSPROGRESSINGJSONLIST.get(i).getTransHashCode());
                jsonArray.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
                System.out.println("-- ex"+e);
            }
        }

        return jsonArray.toString();
    }


    /**
     * 添加新的传输文件到传输列表中
     */
    public static void addToTransingList(List<TransferProgressModel> model) {

//        for (int i = 0,len=model.size(); i < len; i++) {
//            TransferProgressModel progressModel=new TransferProgressModel();
//            progressModel.setTransType(model.get(i).getTransType());
//            progressModel.setTransName(model.get(i).getTransName());
//            progressModel.setTransLength(model.get(i).getTransLength());
//            progressModel.setTransHashCode(model.get(i).getTransHashCode());
//            TRANSPROGRESSINGJSONLIST.add(progressModel);
//        }


        TRANSPROGRESSINGJSONLIST.addAll(model);

    }


}
