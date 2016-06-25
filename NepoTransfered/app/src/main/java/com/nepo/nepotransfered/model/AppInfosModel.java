package com.nepo.nepotransfered.model;

import java.io.Serializable;

/**
 * Created by lsiuhen on 2016/4/28.
 */
public class AppInfosModel implements Serializable {

    private String id;

    private String downId;

    private int downProgress;

    private int downStatus;

    private String appName;

    private String appUrl;

    private String appIconUrl;

    private String appVersion;

    private String appPkg;

    private String appFileSize;

    private String appExtUrl;

    private String appType;

    public String getDownId() {
        return downId;
    }

    public void setDownId(String downId) {
        this.downId = downId;
    }

    public int getDownProgress() {
        return downProgress;
    }

    public void setDownProgress(int downProgress) {
        this.downProgress = downProgress;
    }

    public int getDownStatus() {
        return downStatus;
    }

    public void setDownStatus(int downStatus) {
        this.downStatus = downStatus;
    }

    public void setId(String id){
        this.id = id;
    }
    public String getId(){
        return this.id;
    }
    public void setAppName(String appName){
        this.appName = appName;
    }
    public String getAppName(){
        return this.appName;
    }
    public void setAppUrl(String appUrl){
        this.appUrl = appUrl;
    }
    public String getAppUrl(){
        return this.appUrl;
    }
    public void setAppIconUrl(String appIconUrl){
        this.appIconUrl = appIconUrl;
    }
    public String getAppIconUrl(){
        return this.appIconUrl;
    }
    public void setAppVersion(String appVersion){
        this.appVersion = appVersion;
    }
    public String getAppVersion(){
        return this.appVersion;
    }
    public void setAppPkg(String appPkg){
        this.appPkg = appPkg;
    }
    public String getAppPkg(){
        return this.appPkg;
    }
    public void setAppFileSize(String appFileSize){
        this.appFileSize = appFileSize;
    }
    public String getAppFileSize(){
        return this.appFileSize;
    }
    public void setAppExtUrl(String appExtUrl){
        this.appExtUrl = appExtUrl;
    }
    public String getAppExtUrl(){
        return this.appExtUrl;
    }
    public void setAppType(String appType){
        this.appType = appType;
    }
    public String getAppType(){
        return this.appType;
    }

}
