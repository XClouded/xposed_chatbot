package com.ali.user.mobile;

import android.app.Activity;
import android.text.TextUtils;
import com.ali.user.mobile.service.UserTrackService;
import com.alibaba.analytics.utils.MapUtils;
import com.ut.mini.UTAnalytics;
import com.ut.mini.UTHitBuilders;
import java.util.Properties;

public class UserTraceImpl implements UserTrackService {
    public void sendUT(String str, String str2, Properties properties) {
        sendUT(str, str2, (String) null, properties);
    }

    public void sendUT(String str, String str2) {
        sendUT(str, str2, (Properties) null);
    }

    public void sendUT(String str, Properties properties) {
        sendUT((String) null, str, properties);
    }

    public void sendUT(String str) {
        sendUT((String) null, str, (Properties) null);
    }

    public void sendUT(int i) {
        sendUT((String) null, String.valueOf(i), (Properties) null);
    }

    public void sendUT(String str, String str2, String str3, Properties properties) {
        sendUT(str, str2, str3, (String) null, properties);
    }

    public void sendUT(String str, String str2, String str3, String str4, Properties properties) {
        try {
            UTHitBuilders.UTCustomHitBuilder uTCustomHitBuilder = new UTHitBuilders.UTCustomHitBuilder(str2);
            if (!TextUtils.isEmpty(str)) {
                uTCustomHitBuilder.setEventPage(str);
            }
            if (!TextUtils.isEmpty(str3)) {
                uTCustomHitBuilder.setProperty(UTHitBuilders.UTHitBuilder.FIELD_ARG2, str3);
            }
            if (!TextUtils.isEmpty(str4)) {
                uTCustomHitBuilder.setProperty(UTHitBuilders.UTHitBuilder.FIELD_ARG3, str4);
            }
            if (properties != null) {
                uTCustomHitBuilder.setProperties(MapUtils.convertPropertiesToMap(properties));
            }
            UTAnalytics.getInstance().getDefaultTracker().send(uTCustomHitBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendControlUT(String str, String str2) {
        UTAnalytics.getInstance().getDefaultTracker().send(new UTHitBuilders.UTControlHitBuilder(str, str2).build());
    }

    public void sendControlUT(String str, String str2, String str3) {
        UTHitBuilders.UTControlHitBuilder uTControlHitBuilder = new UTHitBuilders.UTControlHitBuilder(str, str2);
        if (!TextUtils.isEmpty(str3)) {
            uTControlHitBuilder.setProperty(UTHitBuilders.UTHitBuilder.FIELD_ARG2, str3);
        }
        UTAnalytics.getInstance().getDefaultTracker().send(uTControlHitBuilder.build());
    }

    public void skipPage(Activity activity) {
        UTAnalytics.getInstance().getDefaultTracker().skipPage(activity);
    }

    public void pageDisAppear(Activity activity) {
        UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(activity);
    }

    public void updatePageName(Activity activity, String str) {
        UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(activity);
        UTAnalytics.getInstance().getDefaultTracker().pageAppearDonotSkip(activity, str);
    }
}
