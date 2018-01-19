package com.hyst.bus.util;

import android.app.Activity;
import android.app.Application;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/10.
 */

public class AppManager extends Application {

    private List<Activity> activities = new ArrayList<>();
    private static AppManager instance;

    /**
     * 单例模式
     *
     * @return
     */
    public static AppManager getInstance() {
        if (instance == null) {
            synchronized (AppManager.class) {
                instance = new AppManager();
            }
        }
        return instance;
    }

    public void addActivity(Activity activity) {
        activities.add(activity);
    }

    public void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public void exit() {
        for (Activity act : activities) {
            act.finish();
        }
        System.exit(0);
    }
}
