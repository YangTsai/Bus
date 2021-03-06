package com.hyst.bus.model.event;

import android.os.Parcel;
import android.os.Parcelable;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.LatLonPoint;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/16.
 */

public class SetPointEvent implements Parcelable,Serializable {

    //标签
    private String tag;

    //起点或终点分类
    private String type;

    //地点名
    private String content;

    //
    private LatLonPoint latLonPoint;
    //位置相关信息
    private AMapLocation aMapLocation;


    public SetPointEvent(String tag,String pointType, String content,LatLonPoint latLonPoint) {
        this.tag = tag;
        this.type = pointType;
        this.content = content;
        this.latLonPoint = latLonPoint;
    }

    public SetPointEvent() {

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

    public void setaMapLocation(AMapLocation aMapLocation) {
        this.aMapLocation = aMapLocation;
    }

    public AMapLocation getaMapLocation() {
        return aMapLocation;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.tag);
        dest.writeString(this.type);
        dest.writeString(this.content);
        dest.writeParcelable(this.latLonPoint, flags);
        dest.writeParcelable(this.aMapLocation, flags);
    }

    protected SetPointEvent(Parcel in) {
        this.tag = in.readString();
        this.type = in.readString();
        this.content = in.readString();
        this.latLonPoint = in.readParcelable(LatLonPoint.class.getClassLoader());
        this.aMapLocation = in.readParcelable(AMapLocation.class.getClassLoader());
    }

    public static final Creator<SetPointEvent> CREATOR = new Creator<SetPointEvent>() {
        @Override
        public SetPointEvent createFromParcel(Parcel source) {
            return new SetPointEvent(source);
        }

        @Override
        public SetPointEvent[] newArray(int size) {
            return new SetPointEvent[size];
        }
    };
}
