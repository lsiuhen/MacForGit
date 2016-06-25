package com.nepo.nepotransfered.net.download;

import com.nepo.nepotransfered.model.BaseModel;

/**
 * Created by lsiuhen on 2016/5/28.
 */
public class DownObserverMsg extends BaseModel {

    public int progress;

    public String downId;

    @Override
    public String toString() {
        return "DownObserverMsg{" +
                "progress=" + progress +
                ", downId='" + downId + '\'' +
                '}';
    }
}
