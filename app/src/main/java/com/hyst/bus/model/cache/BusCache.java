package com.hyst.bus.model.cache;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/26.
 */

public class BusCache implements Serializable{

    private String busName;

    private String originationStation;

    private String terminusStation;

    private String startTime;

    private String endTime;

    private double price;

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getOriginationStation() {
        return originationStation;
    }

    public void setOriginationStation(String originationStation) {
        this.originationStation = originationStation;
    }

    public String getTerminusStation() {
        return terminusStation;
    }

    public void setTerminusStation(String terminusStation) {
        this.terminusStation = terminusStation;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
