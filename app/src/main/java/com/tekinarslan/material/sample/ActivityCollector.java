package com.tekinarslan.material.sample;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liujian on 2017/3/31.
 */

public class ActivityCollector {

    public static List<Activity> activities = new ArrayList<Activity>();

    public static void addActivity(Activity activity) {
        activities.add(activity);

    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAllActivity() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }

    }

}


