package com.alibaba.ha.adapter.service.crash;

import com.alibaba.ha.adapter.service.activity.ActivityNameManager;
import com.alibaba.motu.crashreporter.Constants;
import java.util.HashMap;
import java.util.Map;

public class CrashActivityCallBack implements JavaCrashListener {
    private final String activityListKey = "_controllers";
    private final String activityNameKey = Constants.CONTROLLER;

    public Map<String, Object> onCrashCaught(Thread thread, Throwable th) {
        String lastActivity = ActivityNameManager.getInstance().getLastActivity();
        String activityList = ActivityNameManager.getInstance().getActivityList();
        HashMap hashMap = new HashMap();
        if (lastActivity != null) {
            hashMap.put(Constants.CONTROLLER, lastActivity);
        }
        if (activityList != null) {
            hashMap.put("_controllers", activityList);
        }
        return hashMap;
    }
}
