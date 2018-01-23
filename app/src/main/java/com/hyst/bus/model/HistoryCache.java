package com.hyst.bus.model;

import com.hyst.bus.model.event.SetPointEvent;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/23.
 */

public class HistoryCache implements Serializable{

    private String cacheType;

    private SetPointEvent startPoint;

    private SetPointEvent endPoint;


    public HistoryCache(String cacheType, SetPointEvent startPoint, SetPointEvent endPoint) {
        this.cacheType = cacheType;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }

    public String getCacheType() {
        return cacheType;
    }

    public void setCacheType(String cacheType) {
        this.cacheType = cacheType;
    }

    public SetPointEvent getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(SetPointEvent startPoint) {
        this.startPoint = startPoint;
    }

    public SetPointEvent getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(SetPointEvent endPoint) {
        this.endPoint = endPoint;
    }
}
