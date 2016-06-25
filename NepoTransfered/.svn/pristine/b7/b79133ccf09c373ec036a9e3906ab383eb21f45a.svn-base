package com.nepo.nepotransfered.model;

import android.graphics.Bitmap;


import java.io.Serializable;

/**
 * Created by lsiuhen on 2016/3/10.
 */
public class MediaMusicModel extends BaseModel {

    private String name;
    private String artist;
    private String path;
    private String time;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private Bitmap bitmap;
    public String getTime() {
        return time;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public void setArtist(String artist) {

        if(artist.equals("<unknown>")||artist.equals("未知")){
            artist="群星";
            this.artist = artist;
        }else{
            this.artist = artist;
        }


    }

    public String getArtist() {
        return artist;
    }

    @Override
    public String toString() {
        return "MediaMusicModel{" +
                "name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", path='" + path + '\'' +
                ", time='" + time + '\'' +
                ", bitmap=" + bitmap +
                '}';
    }
}
