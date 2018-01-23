package com.hyst.bus.model.event;

import com.amap.api.services.core.LatLonPoint;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/16.
 */

public class SetPointEventCache extends SetPointEvent implements Serializable {

    //标签
    private String tag;

    //起点或终点分类
    private String type;

    //地点名
    private String content;

    //
    private LatLonPoint latLonPoint;

    public SetPointEventCache(String tag, String pointType, String content, LatLonPoint latLonPoint) {
        this.tag = tag;
        this.type = pointType;
        this.content = content;
        this.latLonPoint = latLonPoint;
    }

    public SetPointEventCache() {

    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public String getType() {
        return type;
    }

    public void setLatLonPoint(LatLonPoint latLonPoint) {
        this.latLonPoint = latLonPoint;
    }

    public LatLonPoint getLatLonPoint() {
        return latLonPoint;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

}
