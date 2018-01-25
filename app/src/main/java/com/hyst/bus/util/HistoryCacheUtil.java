package com.hyst.bus.util;

import android.content.Context;

import com.hyst.bus.model.HistoryCache;
import com.hyst.bus.model.event.SetPointEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/24.
 */

public class HistoryCacheUtil {

    public static void setCache(Context context, SetPointEvent startPoint, SetPointEvent endPoint) {
        HistoryCache historyCache = new HistoryCache();
        historyCache.setType("route");
        historyCache.setStartContent(startPoint.getContent());
        historyCache.setStartLat(startPoint.getLatLonPoint().getLatitude());
        historyCache.setStartLon((startPoint.getLatLonPoint().getLongitude()));
        historyCache.setEndContent(endPoint.getContent());
        historyCache.setEndLat(endPoint.getLatLonPoint().getLatitude());
        historyCache.setEndLon(endPoint.getLatLonPoint().getLongitude());
        //避免重复插入
        if (ACache.get(context).getAsObject("HistoryCache1") != null) {
            HistoryCache cache = (HistoryCache) ACache.get(context).getAsObject("HistoryCache1");
            if (cache.getStartContent().equals(startPoint.getContent()) && cache.getEndContent().equals(endPoint.getContent())) {
                return;
            }
        }
        if (ACache.get(context).getAsObject("HistoryCache2") != null) {
            HistoryCache cache = (HistoryCache) ACache.get(context).getAsObject("HistoryCache2");
            if (cache.getStartContent().equals(startPoint.getContent()) && cache.getEndContent().equals(endPoint.getContent())) {
                return;
            }
        }
        if (ACache.get(context).getAsObject("HistoryCache3") != null) {
            HistoryCache cache = (HistoryCache) ACache.get(context).getAsObject("HistoryCache3");
            if (cache.getStartContent().equals(startPoint.getContent()) && cache.getEndContent().equals(endPoint.getContent())) {
                return;
            }
        }
        if (ACache.get(context).getAsObject("HistoryCache4") != null) {
            HistoryCache cache = (HistoryCache) ACache.get(context).getAsObject("HistoryCache4");
            if (cache.getStartContent().equals(startPoint.getContent()) && cache.getEndContent().equals(endPoint.getContent())) {
                return;
            }
        }
        if (ACache.get(context).getAsObject("HistoryCache5") != null) {
            HistoryCache cache = (HistoryCache) ACache.get(context).getAsObject("HistoryCache5");
            if (cache.getStartContent().equals(startPoint.getContent()) && cache.getEndContent().equals(endPoint.getContent())) {
                return;
            }
        }
        //按最新顺序插入
        if (ACache.get(context).getAsObject("HistoryCache1") == null) {
            ACache.get(context).put("HistoryCache1", historyCache);
        } else if (ACache.get(context).getAsObject("HistoryCache2") == null) {
            ACache.get(context).put("HistoryCache2", (HistoryCache) ACache.get(context).getAsObject("HistoryCache1"));
            ACache.get(context).put("HistoryCache1", historyCache);
        } else if (ACache.get(context).getAsObject("HistoryCache3") == null) {
            ACache.get(context).put("HistoryCache3", (HistoryCache) ACache.get(context).getAsObject("HistoryCache2"));
            ACache.get(context).put("HistoryCache2", (HistoryCache) ACache.get(context).getAsObject("HistoryCache1"));
            ACache.get(context).put("HistoryCache1", historyCache);
        } else if (ACache.get(context).getAsObject("HistoryCache4") == null) {
            ACache.get(context).put("HistoryCache4", (HistoryCache) ACache.get(context).getAsObject("HistoryCache3"));
            ACache.get(context).put("HistoryCache3", (HistoryCache) ACache.get(context).getAsObject("HistoryCache2"));
            ACache.get(context).put("HistoryCache2", (HistoryCache) ACache.get(context).getAsObject("HistoryCache1"));
            ACache.get(context).put("HistoryCache1", historyCache);
        } else if (ACache.get(context).getAsObject("HistoryCache5") == null) {
            ACache.get(context).put("HistoryCache5", (HistoryCache) ACache.get(context).getAsObject("HistoryCache4"));
            ACache.get(context).put("HistoryCache4", (HistoryCache) ACache.get(context).getAsObject("HistoryCache3"));
            ACache.get(context).put("HistoryCache3", (HistoryCache) ACache.get(context).getAsObject("HistoryCache2"));
            ACache.get(context).put("HistoryCache2", (HistoryCache) ACache.get(context).getAsObject("HistoryCache1"));
            ACache.get(context).put("HistoryCache1", historyCache);
        } else {
            ACache.get(context).put("HistoryCache5", (HistoryCache) ACache.get(context).getAsObject("HistoryCache4"));
            ACache.get(context).put("HistoryCache4", (HistoryCache) ACache.get(context).getAsObject("HistoryCache3"));
            ACache.get(context).put("HistoryCache3", (HistoryCache) ACache.get(context).getAsObject("HistoryCache2"));
            ACache.get(context).put("HistoryCache2", (HistoryCache) ACache.get(context).getAsObject("HistoryCache1"));
            ACache.get(context).put("HistoryCache1", historyCache);
        }
    }

    public static List<HistoryCache> getCache(Context context) {
        List<HistoryCache> data = new ArrayList<>();
        if (ACache.get(context).getAsObject("HistoryCache1") != null) {
            data.add((HistoryCache) ACache.get(context).getAsObject("HistoryCache1"));
        }
        if (ACache.get(context).getAsObject("HistoryCache2") != null) {
            data.add((HistoryCache) ACache.get(context).getAsObject("HistoryCache2"));
        }
        if (ACache.get(context).getAsObject("HistoryCache3") != null) {
            data.add((HistoryCache) ACache.get(context).getAsObject("HistoryCache3"));
        }
        if (ACache.get(context).getAsObject("HistoryCache4") != null) {
            data.add((HistoryCache) ACache.get(context).getAsObject("HistoryCache4"));
        }
        if (ACache.get(context).getAsObject("HistoryCache5") != null) {
            data.add((HistoryCache) ACache.get(context).getAsObject("HistoryCache5"));
        }
        return data;
    }

    public static void clearCache(Context context) {
        ACache.get(context).remove("HistoryCache1");
        ACache.get(context).remove("HistoryCache2");
        ACache.get(context).remove("HistoryCache3");
        ACache.get(context).remove("HistoryCache4");
        ACache.get(context).remove("HistoryCache5");
    }
}
