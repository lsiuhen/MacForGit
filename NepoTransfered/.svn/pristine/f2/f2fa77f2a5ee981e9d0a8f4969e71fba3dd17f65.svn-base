package com.nepo.nepotransfered.net.download;

import java.util.Observable;

/**
 * 观察者，用来观察一些操作，如异步歌词/封面下载等 完成通知主界面更新。
 * 
 */
public class ObserverManage extends Observable {

	private static ObserverManage myobserver = null;

	public static ObserverManage getObserver() {
		if (myobserver == null) {
			myobserver = new ObserverManage();
		}
		return myobserver;
	}

	public void setMessage(Object data) {
		myobserver.setChanged();
		myobserver.notifyObservers(data);
		
	}
}
