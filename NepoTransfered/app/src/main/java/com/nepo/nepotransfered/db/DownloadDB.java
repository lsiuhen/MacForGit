package com.nepo.nepotransfered.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.nepo.nepotransfered.model.AppDownInfoModel;
import com.nepo.nepotransfered.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lsiuhen on 2016/4/28.
 */
public class DownloadDB {

    /**
     * 表名(下载列表)
     */
    public static final String TBL_NAME = "downloadTbl";

    /**
     * 建表(下载列表)语句
     */
    public static final String CREATE_TBL = "create table " +
            TBL_NAME + "(" + "downId text," + "downName text,"+ "downUrl text," + "downStatus int," + "downType text,"
            + "downTotalSize int,"+ "downProgress int," + "downSavePath text," + "downIsComplete text," + "isAddToTransfer text" + ")";

    private SQLiteDatabase db;

    private SQLDBHlper mDBHlper;

    private static DownloadDB _DownInfoDB;

    public DownloadDB(Context context) {
        mDBHlper = SQLDBHlper.getSQLDBHlper(context);
    }

    public static DownloadDB getDownInfoDB(Context context) {
        if (_DownInfoDB == null) {
            _DownInfoDB = new DownloadDB(context);
        }
        return _DownInfoDB;
    }


    /**
     * 添加下载的文件信息
     */

    public long inserDownFileInfo(AppDownInfoModel info) {
        long line = 0;
        int flag = 0;
        ContentValues values = new ContentValues();
        values.put("downId", info.getId());
        values.put("downName", info.getFileName());
        values.put("downType", info.getFileType());
        values.put("downSavePath", info.getFilePath());
        values.put("downStatus", info.getFileDownStatus());
        values.put("downProgress", 0);
        List<AppDownInfoModel> downFileInfoList = queryAll();
        for (int i = 0, len = downFileInfoList.size(); i < len; i++) {
            if (downFileInfoList.get(i).getId().equals(info.getId())) {
                flag++;
            }
        }
        // 下载队列未存在，添加
        if (flag == 0) {
            line = insert(values, TBL_NAME);
        }

        return line;

    }


    /**
     * 插入ContentValues
     */
    private long insert(ContentValues values, String table) {
        db = mDBHlper.getWritableDatabase();
        try {
            long insertLine = db.insert(table, null, values);
            return insertLine;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }


    public List<AppDownInfoModel> queryAll() {
        List<AppDownInfoModel> downFileInfoList = new ArrayList<>();
        db = mDBHlper.getReadableDatabase();
        Cursor cursor = db.query(TBL_NAME, null, null, null, null, null, null);
        while (cursor.moveToNext()) {
            AppDownInfoModel downFileInfo = new AppDownInfoModel();
            downFileInfo.setId(cursor.getString(cursor.getColumnIndex("downId")));
            downFileInfo.setFileName(cursor.getString(cursor.getColumnIndex("downName")));
            downFileInfo.setFileType(cursor.getString(cursor.getColumnIndex("downType")));
            downFileInfo.setFilePath(cursor.getString(cursor.getColumnIndex("downSavePath")));
            downFileInfo.setFileDownStatus(cursor.getInt(cursor.getColumnIndex("downStatus")));
            downFileInfo.setFileDownProgress(cursor.getInt(cursor.getColumnIndex("downProgress")));
            downFileInfoList.add(downFileInfo);
        }

        if (cursor != null && !cursor.isClosed())
            cursor.close();
        return downFileInfoList;
    }

    /**
     * 修改为下载完成的标志
     * downIsComplete = 1
     */
    public void updateIsDowned(AppDownInfoModel downFileInfo) {
        db = mDBHlper.getWritableDatabase();
        try {

            ContentValues values = new ContentValues();
            values.put("downStatus", downFileInfo.getFileDownStatus());
            values.put("downProgress", 100);
            int update = db.update(TBL_NAME, values, "downId = ?", new String[]{downFileInfo.getId()});
            Utils.Sout("up2", update);
        } catch (SQLException e) {
            Utils.Sout("upex2", e);
            e.printStackTrace();
        }
    }







}
