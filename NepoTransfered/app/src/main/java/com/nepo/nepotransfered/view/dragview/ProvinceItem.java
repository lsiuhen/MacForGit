package com.nepo.nepotransfered.view.dragview;


import com.nepo.nepotransfered.app.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by guolei on 16-3-14.
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 * |        没有神兽，风骚依旧！          |
 * |        QQ:1120832563             |
 * ━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━
 */

public class ProvinceItem extends BaseItem implements Serializable {

    private static final long serialVersionUID = -6465237897027410019L;;

    private JSONObject jsonObject;

    private int id;
    private String name;
    private int resourceId;

    public ProvinceItem(){

    }

    public ProvinceItem(int id, String name, int resourceId){
        this.id = id;
        this.name = name;
        this.resourceId=resourceId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    /**
     * 将bean 转化为　jsonobject
     * @return
     * @throws JSONException
     */
    @Override
    public JSONObject toJson() throws JSONException {
        if (null == jsonObject){
            jsonObject = new JSONObject();
        }
        jsonObject.put(Constant.PROVINCE_ID,id);
        jsonObject.put(Constant.PROVINCE_NAME,name);
        jsonObject.put(Constant.PROVINCE_DRAWABLE,resourceId);
        return jsonObject;
    }

    @Override
    public void fromJson(JSONObject jsonObject){
        if (null == jsonObject){
            return;
        }
        this.id = jsonObject.optInt(Constant.PROVINCE_ID);
        this.name = jsonObject.optString(Constant.PROVINCE_NAME);
        this.resourceId = jsonObject.optInt(Constant.PROVINCE_DRAWABLE);
    }

    @Override
    public String toString() {
        return "ProvinceItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", resourceId=" + resourceId +
                '}';
    }
}
