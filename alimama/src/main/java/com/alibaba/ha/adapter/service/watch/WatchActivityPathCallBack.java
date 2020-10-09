package com.alibaba.ha.adapter.service.watch;

import com.alibaba.ha.adapter.service.activity.ActivityNameManager;
import java.util.HashMap;
import java.util.Map;

public class WatchActivityPathCallBack implements WatchListener {
    public Map<String, String> onListener(Map<String, Object> map) {
        return null;
    }

    public void onWatch(Map<String, Object> map) {
    }

    public Map<String, String> onCatch() {
        String activityList = ActivityNameManager.getInstance().getActivityList();
        HashMap hashMap = new HashMap();
        if (activityList != null) {
            try {
                hashMap.put("_controller_path", activityList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            hashMap.put("_controller_path", "-");
        }
        return hashMap;
    }
}
