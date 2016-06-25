package com.nepo.nepotransfered.model;

import android.graphics.Bitmap;
import android.text.TextUtils;


import java.io.File;

/**
 * Created by lsiuhen on 2016/4/5.
 * <p/>
 * 传输基础类
 */
public class TransferModel extends BaseModel {


    private String transName;
    private String transSize;
    private String transLength;
    private String transPath;
    private Integer transProgress = 0;
    private String transHashCode;
    private String currentSize;
    private String transType;
    private String isTransfered;
    private Bitmap transBitmap;

    public Bitmap getTransBitmap() {
        return transBitmap;
    }

    public void setTransBitmap(Bitmap transBitmap) {
        this.transBitmap = transBitmap;
    }

    public String getIsTransfered() {
        return isTransfered;
    }

    public void setIsTransfered(String isTransfered) {
        this.isTransfered = isTransfered;
    }

    public String getTransLength() {
        return String.valueOf(getTransFile().length());
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getCurrentSize() {
        if (currentSize == null || TextUtils.isEmpty(currentSize)) {
            return "0";
        }
        return currentSize;
    }

    public void setCurrentSize(String currentSize) {
        this.currentSize = currentSize;
    }

    private File transFile;

    public File getTransFile() {
        return new File(getTransPath());
    }

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName.substring(transName.lastIndexOf("/") + 1, transName.length());
    }

    public String getTransSize() {
        return String.valueOf(getTransFile().length());
    }

    public String getTransPath() {
        return transPath;
    }

    public void setTransPath(String transPath) {
        this.transPath = transPath;
        setTransName(transPath);
        setTransHashCode(String.valueOf(new File(transPath).hashCode()));
    }

    public Integer getTransProgress() {
        return transProgress;
    }

    public void setTransProgress(Integer transProgress) {
        this.transProgress = transProgress;
    }

    public String getTransHashCode() {
        return transHashCode;
    }

    public void setTransHashCode(String transHashCode) {
        this.transHashCode = transHashCode;
    }

    @Override
    public String toString() {
        return "TransferModel{" +
                "transName='" + transName + '\'' +
                ", transSize='" + transSize + '\'' +
                ", transLength='" + getTransLength() + '\'' +
                ", transPath='" + transPath + '\'' +
                ", transProgress=" + transProgress +
                ", transHashCode='" + transHashCode + '\'' +
                ", currentSize='" + currentSize + '\'' +
                ", transType='" + transType + '\'' +
                ", isTransfered='" + isTransfered + '\'' +
                ", transBitmap=" + transBitmap +
                ", transFile=" + transFile +
                '}';
    }
}
