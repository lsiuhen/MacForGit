/**
 * @author Administrator
 * 
 */
package com.nepo.nepotransfered.app;

import android.os.Environment;

import java.io.File;

/**
 * 
 * 项目名称：
 * 类  名  称：Global   
 * 类  描  述：全局文件配置信息   
 */
public class Global {
	
	public static String APP_DIR = getSDcardPathEx() + "/NEPO";
	public static String CACHE_DIR = APP_DIR + "/PhoneTransfer";
	public static String PHONE_INFO_DIR = CACHE_DIR + "/info";
	public static String PHONE_DOWNLOAD_DIR = CACHE_DIR + "/download";
	public static String PHONE_TEMP_DIR = CACHE_DIR + "/temp";
	public static boolean LOGON = false;
	static {
		try {
			// APP_VER = Util.getApkVer(WareHouseApp.instance());
			new File(APP_DIR).mkdirs();
			new File(CACHE_DIR).mkdirs();
			new File(PHONE_INFO_DIR).mkdirs();
			new File(PHONE_DOWNLOAD_DIR).mkdirs();
			new File(PHONE_TEMP_DIR).mkdirs();
//			new File(APP_DIR + File.separator + ".nomedia").createNewFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static final String NORMAL_EX = "us_normal_exception.txt"; // 记录正常异常文件名
	public static final String CRASH_EX = "us_trash_exception.txt"; // 记录异常崩溃文件名
	public static final boolean RECORD_TRASH_EX = Boolean.FALSE; // 开关是否记录异常崩溃错误记录
	public static final boolean RECORD_NORMAL_EX = Boolean.FALSE; // 开关是否记录异常错误记录

	/**
	 *
	 * 获取手机存储卡路径(是/否)存在
	 * @return
	 */
	public static String getSDcardPathEx() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		} else {
			return "/sdcard";
		}

	}
}
