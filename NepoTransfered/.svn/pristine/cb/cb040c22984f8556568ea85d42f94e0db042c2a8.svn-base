package com.nepo.nepotransfered.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Environment;

import com.nepo.nepotransfered.app.Constant;
import com.nepo.nepotransfered.app.Global;
import com.nepo.nepotransfered.model.AppDownInfoModel;
import com.nepo.nepotransfered.model.AppInfosModel;
import com.nepo.nepotransfered.model.MediaMusicModel;
import com.nepo.nepotransfered.model.MediaVideoModel;
import com.nepo.nepotransfered.model.TransferModel;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsiuhen on 2016/5/27.
 */
public class Utils {

    private static long lastClickTime;


    /**
     * log 输出
     *
     * @param tip
     * @param msg
     */
    public static void Sout(String tip, Object msg) {
        Boolean flag = true;
        if (flag) {
            System.out.println("-- " + tip + ":" + msg);
        }
    }

    public static String subStr(String url) {
        String str = "";
        if (url.contains("=")) {
            str = url.substring(url.lastIndexOf("=") + 1, url.length());
        }
        return str;
    }

    /**
     * 根据传进来的类型，返回下载不同后缀
     * //类型参数暂时为空
     *
     * @return
     */
    public static String getDiskDirByType(String fileName) {

        /**
         * if(type=apk)
         * return ..+.apk
         *
         * if(type=rom)
         * return ..+.zip
         *
         * if(type=map)
         * return ..+.地图后缀
         *
         */


        return Global.PHONE_DOWNLOAD_DIR + "/" + fileName + ".apk";
    }


    /**
     * * 使用md5的算法进行加密
     */
    public static String md5(String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有md5这个算法！");
        }
        String md5code = new BigInteger(1, secretBytes).toString(16);// 16进制数字
        // 如果生成数字未满32位，需要前面补0
        for (int i = 0; i < 32 - md5code.length(); i++) {
            md5code = "0" + md5code;
        }
        return md5code;
    }


    /**
     * 防止连续点击
     */
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 设置存储位置
     */
    public File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    //版本号
    public static String getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return  version;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * dip tp px
     * @param context
     * @param dipValue
     * @return
     */
    public static int dip2px(Context context,float dipValue){
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 转换文件大小
     *
     * @param fileS
     * @return
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }
        /**
         * 筛选未传输过的歌曲
         *
         * @param groupList
         * @param transferModelList
         * @return
         */
    public static ArrayList<MediaMusicModel> siftNoTransMusic(List<MediaMusicModel> groupList, List<TransferModel> transferModelList) {
        ArrayList<MediaMusicModel> musicGroupList = new ArrayList<>();

        for (int j = 0; j < groupList.size(); j++) {
            boolean isAdd = true;
            for (int i = 0; i < transferModelList.size(); i++) {
                // 在传输完成列表中为匹配到
                if (groupList.get(j).getName().equals(transferModelList.get(i).getTransName())) {
                    isAdd = false;
                    break;
                }
            }
            if (isAdd) {
                musicGroupList.add(groupList.get(j));
            }
        }
        return musicGroupList;
    }

    /**
     * 筛选已传输过的歌曲
     *
     * @param groupList
     * @param transferModelList
     * @return
     */
    public static List<MediaMusicModel> siftTransedMusic(List<MediaMusicModel> groupList, List<TransferModel> transferModelList) {
        List<MediaMusicModel> musicGroupList = new ArrayList<>();
        for (int i = 0; i < transferModelList.size(); i++) {
            for (int j = 0; j < groupList.size(); j++) {
                // 在传输完成列表中为匹配到
                if (groupList.get(j).getName().equals(transferModelList.get(i).getTransName())) {
                    musicGroupList.add(groupList.get(j));
                }
            }
        }
        return musicGroupList;
    }

    /**
     * 筛选未传输过的视频
     *
     * @param groupList
     * @param transferModelList
     * @return
     */
    public static ArrayList<MediaVideoModel> siftNoTransVideo(List<MediaVideoModel> groupList, List<TransferModel> transferModelList) {
        ArrayList<MediaVideoModel> videoGroupList = new ArrayList<>();

        for (int j = 0; j < groupList.size(); j++) {
            boolean isAdd = true;
            for (int i = 0; i < transferModelList.size(); i++) {
                // 在传输完成列表中为匹配到
                if (groupList.get(j).getDisplayName().equals(transferModelList.get(i).getTransName())) {
                    isAdd = false;
                    break;
                }
            }
            if (isAdd) {
                videoGroupList.add(groupList.get(j));
            }
        }
        return videoGroupList;
    }

    /**
     * 筛选已传输过的视频
     *
     * @param groupList
     * @param transferModelList
     * @return
     */
    public static List<MediaVideoModel> siftTransedVideo(List<MediaVideoModel> groupList, List<TransferModel> transferModelList) {
        List<MediaVideoModel> videoGroupList = new ArrayList<>();
        for (int i = 0; i < transferModelList.size(); i++) {
            for (int j = 0; j < groupList.size(); j++) {
                // 在传输完成列表中为匹配到
                if (groupList.get(j).getDisplayName().equals(transferModelList.get(i).getTransName())) {
                    videoGroupList.add(groupList.get(j));
                }
            }
        }


        return videoGroupList;
    }


    /**
     * 跟数据库中下载完成的数据对比，筛选出下载完成情况
     */
    public static List<AppInfosModel> shiftData(List<AppInfosModel> appInfoses, List<AppDownInfoModel> downFileInfos) {

        for (int i = 0, len = appInfoses.size(); i < len; i++) {
            for (int j = 0, lenj = downFileInfos.size(); j < lenj; j++) {
                if (downFileInfos.get(j).getId().equals(appInfoses.get(i).getDownId())) {
                    appInfoses.get(i).setDownStatus(Constant.STATE_DOWNLOADED);
                    appInfoses.get(i).setDownProgress(downFileInfos.get(j).getFileDownProgress());
                }
            }
        }

        return appInfoses;

    }


}
