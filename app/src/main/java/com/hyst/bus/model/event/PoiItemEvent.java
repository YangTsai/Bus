package com.hyst.bus.model.event;


import com.amap.api.services.core.PoiItem;

/**
 * Created by Administrator on 2018/2/2.
 */

public class PoiItemEvent {

    private String tag;

    private PoiItem poiItem;

    public PoiItemEvent(String tag,PoiItem poiItem){
        this.tag = tag;
        this.poiItem = poiItem;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public PoiItem getPoiItem() {
        return poiItem;
    }

    public void setPoiItem(PoiItem poiItem) {
        this.poiItem = poiItem;
    }
}
