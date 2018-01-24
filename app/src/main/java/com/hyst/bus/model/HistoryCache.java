package com.hyst.bus.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/23.
 */

public class HistoryCache implements Serializable{

    private String type;

    private String startContent;

    private double startLat;

    private double startLon;

    private String endContent;

    private double endLat;

    private double endLon;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartContent() {
        return startContent;
    }

    public void setStartContent(String startContent) {
        this.startContent = startContent;
    }

    public double getStartLat() {
        return startLat;
    }

    public void setStartLat(double startLat) {
        this.startLat = startLat;
    }

    public double getStartLon() {
        return startLon;
    }

    public void setStartLon(double startLon) {
        this.startLon = startLon;
    }

    public String getEndContent() {
        return endContent;
    }

    public void setEndContent(String endContent) {
        this.endContent = endContent;
    }

    public void setEndLat(double endLat) {
        this.endLat = endLat;
    }

    public double getEndLat() {
        return endLat;
    }

    public void setEndLon(double endLon) {
        this.endLon = endLon;
    }

    public double getEndLon() {
        return endLon;
    }
}
