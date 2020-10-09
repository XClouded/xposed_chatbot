package com.ali.user.mobile.log;

import android.app.Activity;
import android.util.Log;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.service.UserTrackService;
import java.util.Properties;

public class UserTrackAdapter {
    public static void sendUT(String str, String str2, Properties properties) {
        sendUT(str, str2, (String) null, properties);
    }

    public static void sendUT(String str, String str2) {
        sendUT(str, str2, (Properties) null);
    }

    public static void sendUT(String str, Properties properties) {
        sendUT((String) null, str, properties);
    }

    public static void sendUT(String str) {
        sendUT((String) null, str, (Properties) null);
    }

    public static void sendUT(int i) {
        sendUT((String) null, String.valueOf(i), (Properties) null);
    }

    public static void sendUT(String str, String str2, String str3, Properties properties) {
        sendUT(str, str2, str3, (String) null, properties);
    }

    public static void sendUT(String str, String str2, String str3, String str4, Properties properties) {
        if (ServiceFactory.getService(UserTrackService.class) != null) {
            ((UserTrackService) ServiceFactory.getService(UserTrackService.class)).sendUT(str, str2, str3, str4, properties);
        } else {
            Log.e("login.UserTrackAdapter", "UserTrackService is null!");
        }
    }

    public static void sendControlUT(String str, String str2) {
        if (ServiceFactory.getService(UserTrackService.class) != null) {
            ((UserTrackService) ServiceFactory.getService(UserTrackService.class)).sendControlUT(str, str2);
        } else {
            Log.e("login.UserTrackAdapter", "UserTrackService is null!");
        }
    }

    public static void sendControlUT(String str, String str2, String str3) {
        if (ServiceFactory.getService(UserTrackService.class) != null) {
            ((UserTrackService) ServiceFactory.getService(UserTrackService.class)).sendControlUT(str, str2, str3);
        } else {
            Log.e("login.UserTrackAdapter", "UserTrackService is null!");
        }
    }

    public static void skipPage(Activity activity) {
        if (ServiceFactory.getService(UserTrackService.class) != null) {
            ((UserTrackService) ServiceFactory.getService(UserTrackService.class)).skipPage(activity);
        } else {
            Log.e("login.UserTrackAdapter", "UserTrackService is null!");
        }
    }

    public static void pageDisAppear(Activity activity) {
        if (ServiceFactory.getService(UserTrackService.class) != null) {
            ((UserTrackService) ServiceFactory.getService(UserTrackService.class)).pageDisAppear(activity);
        } else {
            Log.e("login.UserTrackAdapter", "UserTrackService is null!");
        }
    }

    public static void updatePageName(Activity activity, String str) {
        if (ServiceFactory.getService(UserTrackService.class) != null) {
            ((UserTrackService) ServiceFactory.getService(UserTrackService.class)).updatePageName(activity, str);
        } else {
            Log.e("login.UserTrackAdapter", "UserTrackService is null!");
        }
    }
}
