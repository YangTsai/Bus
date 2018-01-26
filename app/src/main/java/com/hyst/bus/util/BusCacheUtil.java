package com.hyst.bus.util;

import android.content.Context;

import com.hyst.bus.model.cache.BusCache;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/24.
 */

public class BusCacheUtil {

    public static void setCache(Context context, BusCache busCache) {
       
        //避免重复插入
        if (ACache.get(context).getAsObject("BusCache1") != null) {
            BusCache cache = (BusCache) ACache.get(context).getAsObject("BusCache1");
            if (cache.getBusName().equals(busCache.getBusName())) {
                return;
            }
        }
        if (ACache.get(context).getAsObject("BusCache2") != null) {
            BusCache cache = (BusCache) ACache.get(context).getAsObject("BusCache2");
            if (cache.getBusName().equals(busCache.getBusName())) {
                return;
            }
        }
        if (ACache.get(context).getAsObject("BusCache3") != null) {
            BusCache cache = (BusCache) ACache.get(context).getAsObject("BusCache3");
            if (cache.getBusName().equals(busCache.getBusName())) {
                return;
            }
        }
        if (ACache.get(context).getAsObject("BusCache4") != null) {
            BusCache cache = (BusCache) ACache.get(context).getAsObject("BusCache4");
            if (cache.getBusName().equals(busCache.getBusName())) {
                return;
            }
        }
        if (ACache.get(context).getAsObject("BusCache5") != null) {
            BusCache cache = (BusCache) ACache.get(context).getAsObject("BusCache5");
            if (cache.getBusName().equals(busCache.getBusName())) {
                return;
            }
        }
        //按最新顺序插入
        if (ACache.get(context).getAsObject("BusCache1") == null) {
            ACache.get(context).put("BusCache1", busCache);
        } else if (ACache.get(context).getAsObject("BusCache2") == null) {
            ACache.get(context).put("BusCache2", (BusCache) ACache.get(context).getAsObject("BusCache1"));
            ACache.get(context).put("BusCache1", busCache);
        } else if (ACache.get(context).getAsObject("BusCache3") == null) {
            ACache.get(context).put("BusCache3", (BusCache) ACache.get(context).getAsObject("BusCache2"));
            ACache.get(context).put("BusCache2", (BusCache) ACache.get(context).getAsObject("BusCache1"));
            ACache.get(context).put("BusCache1", busCache);
        } else if (ACache.get(context).getAsObject("BusCache4") == null) {
            ACache.get(context).put("BusCache4", (BusCache) ACache.get(context).getAsObject("BusCache3"));
            ACache.get(context).put("BusCache3", (BusCache) ACache.get(context).getAsObject("BusCache2"));
            ACache.get(context).put("BusCache2", (BusCache) ACache.get(context).getAsObject("BusCache1"));
            ACache.get(context).put("BusCache1", busCache);
        } else if (ACache.get(context).getAsObject("BusCache5") == null) {
            ACache.get(context).put("BusCache5", (BusCache) ACache.get(context).getAsObject("BusCache4"));
            ACache.get(context).put("BusCache4", (BusCache) ACache.get(context).getAsObject("BusCache3"));
            ACache.get(context).put("BusCache3", (BusCache) ACache.get(context).getAsObject("BusCache2"));
            ACache.get(context).put("BusCache2", (BusCache) ACache.get(context).getAsObject("BusCache1"));
            ACache.get(context).put("BusCache1", busCache);
        } else {
            ACache.get(context).put("BusCache5", (BusCache) ACache.get(context).getAsObject("BusCache4"));
            ACache.get(context).put("BusCache4", (BusCache) ACache.get(context).getAsObject("BusCache3"));
            ACache.get(context).put("BusCache3", (BusCache) ACache.get(context).getAsObject("BusCache2"));
            ACache.get(context).put("BusCache2", (BusCache) ACache.get(context).getAsObject("BusCache1"));
            ACache.get(context).put("BusCache1", busCache);
        }
    }

    public static List<BusCache> getCache(Context context) {
        List<BusCache> data = new ArrayList<>();
        if (ACache.get(context).getAsObject("BusCache1") != null) {
            data.add((BusCache) ACache.get(context).getAsObject("BusCache1"));
        }
        if (ACache.get(context).getAsObject("BusCache2") != null) {
            data.add((BusCache) ACache.get(context).getAsObject("BusCache2"));
        }
        if (ACache.get(context).getAsObject("BusCache3") != null) {
            data.add((BusCache) ACache.get(context).getAsObject("BusCache3"));
        }
        if (ACache.get(context).getAsObject("BusCache4") != null) {
            data.add((BusCache) ACache.get(context).getAsObject("BusCache4"));
        }
        if (ACache.get(context).getAsObject("BusCache5") != null) {
            data.add((BusCache) ACache.get(context).getAsObject("BusCache5"));
        }
        return data;
    }

    public static void clearCache(Context context) {
        ACache.get(context).remove("BusCache1");
        ACache.get(context).remove("BusCache2");
        ACache.get(context).remove("BusCache3");
        ACache.get(context).remove("BusCache4");
        ACache.get(context).remove("BusCache5");
    }
}
