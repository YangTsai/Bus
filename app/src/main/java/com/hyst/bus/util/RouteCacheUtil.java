package com.hyst.bus.util;

import android.content.Context;

import com.hyst.bus.model.cache.RouteCache;
import com.hyst.bus.model.event.SetPointEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/24.
 */

public class RouteCacheUtil {

    public static void setCache(Context context, SetPointEvent startPoint, SetPointEvent endPoint) {
        RouteCache routeCache = new RouteCache();
        routeCache.setType("route");
        routeCache.setStartContent(startPoint.getContent());
        routeCache.setStartLat(startPoint.getLatLonPoint().getLatitude());
        routeCache.setStartLon((startPoint.getLatLonPoint().getLongitude()));
        routeCache.setEndContent(endPoint.getContent());
        routeCache.setEndLat(endPoint.getLatLonPoint().getLatitude());
        routeCache.setEndLon(endPoint.getLatLonPoint().getLongitude());
        //避免重复插入
        if (ACache.get(context).getAsObject("HistoryCache1") != null) {
            RouteCache cache = (RouteCache) ACache.get(context).getAsObject("HistoryCache1");
            if (cache.getStartContent().equals(startPoint.getContent()) && cache.getEndContent().equals(endPoint.getContent())) {
                return;
            }
        }
        if (ACache.get(context).getAsObject("HistoryCache2") != null) {
            RouteCache cache = (RouteCache) ACache.get(context).getAsObject("HistoryCache2");
            if (cache.getStartContent().equals(startPoint.getContent()) && cache.getEndContent().equals(endPoint.getContent())) {
                return;
            }
        }
        if (ACache.get(context).getAsObject("HistoryCache3") != null) {
            RouteCache cache = (RouteCache) ACache.get(context).getAsObject("HistoryCache3");
            if (cache.getStartContent().equals(startPoint.getContent()) && cache.getEndContent().equals(endPoint.getContent())) {
                return;
            }
        }
        if (ACache.get(context).getAsObject("HistoryCache4") != null) {
            RouteCache cache = (RouteCache) ACache.get(context).getAsObject("HistoryCache4");
            if (cache.getStartContent().equals(startPoint.getContent()) && cache.getEndContent().equals(endPoint.getContent())) {
                return;
            }
        }
        if (ACache.get(context).getAsObject("HistoryCache5") != null) {
            RouteCache cache = (RouteCache) ACache.get(context).getAsObject("HistoryCache5");
            if (cache.getStartContent().equals(startPoint.getContent()) && cache.getEndContent().equals(endPoint.getContent())) {
                return;
            }
        }
        //按最新顺序插入
        if (ACache.get(context).getAsObject("HistoryCache1") == null) {
            ACache.get(context).put("HistoryCache1", routeCache);
        } else if (ACache.get(context).getAsObject("HistoryCache2") == null) {
            ACache.get(context).put("HistoryCache2", (RouteCache) ACache.get(context).getAsObject("HistoryCache1"));
            ACache.get(context).put("HistoryCache1", routeCache);
        } else if (ACache.get(context).getAsObject("HistoryCache3") == null) {
            ACache.get(context).put("HistoryCache3", (RouteCache) ACache.get(context).getAsObject("HistoryCache2"));
            ACache.get(context).put("HistoryCache2", (RouteCache) ACache.get(context).getAsObject("HistoryCache1"));
            ACache.get(context).put("HistoryCache1", routeCache);
        } else if (ACache.get(context).getAsObject("HistoryCache4") == null) {
            ACache.get(context).put("HistoryCache4", (RouteCache) ACache.get(context).getAsObject("HistoryCache3"));
            ACache.get(context).put("HistoryCache3", (RouteCache) ACache.get(context).getAsObject("HistoryCache2"));
            ACache.get(context).put("HistoryCache2", (RouteCache) ACache.get(context).getAsObject("HistoryCache1"));
            ACache.get(context).put("HistoryCache1", routeCache);
        } else if (ACache.get(context).getAsObject("HistoryCache5") == null) {
            ACache.get(context).put("HistoryCache5", (RouteCache) ACache.get(context).getAsObject("HistoryCache4"));
            ACache.get(context).put("HistoryCache4", (RouteCache) ACache.get(context).getAsObject("HistoryCache3"));
            ACache.get(context).put("HistoryCache3", (RouteCache) ACache.get(context).getAsObject("HistoryCache2"));
            ACache.get(context).put("HistoryCache2", (RouteCache) ACache.get(context).getAsObject("HistoryCache1"));
            ACache.get(context).put("HistoryCache1", routeCache);
        } else {
            ACache.get(context).put("HistoryCache5", (RouteCache) ACache.get(context).getAsObject("HistoryCache4"));
            ACache.get(context).put("HistoryCache4", (RouteCache) ACache.get(context).getAsObject("HistoryCache3"));
            ACache.get(context).put("HistoryCache3", (RouteCache) ACache.get(context).getAsObject("HistoryCache2"));
            ACache.get(context).put("HistoryCache2", (RouteCache) ACache.get(context).getAsObject("HistoryCache1"));
            ACache.get(context).put("HistoryCache1", routeCache);
        }
    }

    public static List<RouteCache> getCache(Context context) {
        List<RouteCache> data = new ArrayList<>();
        if (ACache.get(context).getAsObject("HistoryCache1") != null) {
            data.add((RouteCache) ACache.get(context).getAsObject("HistoryCache1"));
        }
        if (ACache.get(context).getAsObject("HistoryCache2") != null) {
            data.add((RouteCache) ACache.get(context).getAsObject("HistoryCache2"));
        }
        if (ACache.get(context).getAsObject("HistoryCache3") != null) {
            data.add((RouteCache) ACache.get(context).getAsObject("HistoryCache3"));
        }
        if (ACache.get(context).getAsObject("HistoryCache4") != null) {
            data.add((RouteCache) ACache.get(context).getAsObject("HistoryCache4"));
        }
        if (ACache.get(context).getAsObject("HistoryCache5") != null) {
            data.add((RouteCache) ACache.get(context).getAsObject("HistoryCache5"));
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
