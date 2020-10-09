package com.ali.user.mobile.log;

import android.text.TextUtils;
import java.util.Properties;

public class LongLifeCycleUserTrack {
    private static String ResultScene;
    private static String sScene;

    public static String getResultScene() {
        return ResultScene;
    }

    public static void setResultScene(String str) {
        ResultScene = str;
    }

    public static void setScene(String str) {
        sScene = str;
    }

    public static void sendUT(String str, Properties properties) {
        if (properties == null) {
            properties = new Properties();
        }
        if (!TextUtils.isEmpty(getResultScene())) {
            properties.put("UTScene", getResultScene());
        }
        if (!TextUtils.isEmpty(sScene)) {
            if (TextUtils.equals(sScene, "login_bar")) {
                properties.put("source", "Page_Login5-AlipaySuc");
            }
            sScene = "";
        }
        UserTrackAdapter.sendUT(str, properties);
        setResultScene((String) null);
    }

    public static void sendUT(String str) {
        sendUT(str, (Properties) null);
    }
}
