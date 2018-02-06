package com.hyst.bus.model;

import com.amap.api.maps.model.LatLng;

/**
 * Created by Administrator on 2018/1/26.
 */

public class BusStationInfo {

    /**
     * 当前站点（站点定位地址）
     */
    private String stationName;

    /**
     * 当前站点距离定位地点距离
     */
    private double distance;

    /**
     * 公交车名称（多少路公交车）
     */
    private String busName;
    /**
     * 下一站站点名称
     */
    private String nextStationName;

    /**
     * 当前公交车还有多少时间到达
     */
    private String comeTime;

    /**
     * 下一辆公交车还有多少时间到达
     */
    private String nextComeTime;

    /**
     * 当前站点的经纬度
     */
    private LatLng latLng;

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getNextStationName() {
        return nextStationName;
    }

    public void setNextStationName(String nextStationName) {
        this.nextStationName = nextStationName;
    }

    public String getComeTime() {
        return comeTime;
    }

    public void setComeTime(String comeTime) {
        this.comeTime = comeTime;
    }

    public String getNextComeTime() {
        return nextComeTime;
    }

    public void setNextComeTime(String nextComeTime) {
        this.nextComeTime = nextComeTime;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public LatLng getLatLng() {
        return latLng;
    }
}
