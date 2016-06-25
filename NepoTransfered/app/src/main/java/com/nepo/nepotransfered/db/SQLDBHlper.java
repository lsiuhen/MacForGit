package com.nepo.nepotransfered.db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.nepo.nepotransfered.app.Constant;


/**
 * SQLDBHlper辅助类
 * 
 * @author Administrator 一个应用只能使用一个SQLDBHlper辅助类 最近修改时间2013-12-31
 */
public class SQLDBHlper extends SQLiteOpenHelper {
	private static SQLDBHlper sqldbHlper;

	/**
	 * 获取SQLDBHlper
	 * 
	 * @param context
	 * @return
	 */
	public static SQLDBHlper getSQLDBHlper(Context context) {
		if (sqldbHlper == null) {
			sqldbHlper = new SQLDBHlper(context);

		}
		return sqldbHlper;
	}

	public SQLDBHlper(Context context) {
		super(context, Constant.DB_NAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		try {
			System.out.println("-------- 建表");

			// 传输队列
			db.execSQL(TransferDB.CREATE_TBL);
			// 传输进程队列
			db.execSQL(TransferDB.CREATE_TBL_PROGRESS);
			// 传输类型
			db.execSQL(TransferDB.CREATE_TBL_TYPE);
			// 下载队列
			db.execSQL(DownloadDB.CREATE_TBL);

		} catch (SQLException e) {
			Log.i("error", "create table failed");
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		try {
			db.execSQL("drop table if exists " + TransferDB.TBL_NAME);
			db.execSQL("drop table if exists " + TransferDB.TBL_TRANSFERTYPE_NAME);
		} catch (SQLException e) {
			Log.i("error", "drop table failed");
		}
		onCreate(db);
	}

}
