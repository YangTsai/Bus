package com.hyst.bus.model.event;

/**
 * Created by Administrator on 2018/1/19.
 */

public class LocationEvent {

    private String aoiName;

    private double Latitude;//获取纬度

    private double Longitude;//获取经度

    public String getAoiName() {
        return aoiName;
    }

    public void setAoiName(String aoiName) {
        this.aoiName = aoiName;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}
