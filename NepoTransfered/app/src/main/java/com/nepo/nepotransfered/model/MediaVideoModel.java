package com.nepo.nepotransfered.model;

import android.graphics.Bitmap;

/**
 * Created by lsiuhen on 2016/5/27.
 */
public class MediaVideoModel extends BaseModel {

    private String title;
    private String path;
    private String displayName;
    private Bitmap bitmap;
    private String size;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    private String mimeType;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    @Override
    public String toString() {
        return "VideoGroup{" +
                "title='" + title + '\'' +
                ", path='" + path + '\'' +
                ", displayName='" + displayName + '\'' +
                ", bitmap=" + bitmap +
                ", size='" + size + '\'' +
                ", mimeType='" + mimeType + '\'' +
                '}';
    }

}
