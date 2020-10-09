package com.alimama.moon.usertrack;

import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import java.util.Map;

public class BaseUTHelper {
    public static void sendControlHit(String str, String str2) {
        UTAnalytics.getInstance().getDefaultTracker().send(new UTHitBuilders.UTControlHitBuilder(str, str2).build());
    }

    public static void sendControlHit(String str, String str2, Map<String, String> map) {
        UTHitBuilders.UTControlHitBuilder uTControlHitBuilder = new UTHitBuilders.UTControlHitBuilder(str, str2);
        uTControlHitBuilder.setProperties(map);
        UTAnalytics.getInstance().getDefaultTracker().send(uTControlHitBuilder.build());
    }
}
