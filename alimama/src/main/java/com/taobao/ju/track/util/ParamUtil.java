package com.taobao.ju.track.util;

import android.content.Intent;

public class ParamUtil {
    public static boolean hasExtra(Intent intent, String str) {
        if (intent == null) {
            return false;
        }
        try {
            return intent.hasExtra(str);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String getStringExtra(Intent intent, String str) {
        if (intent == null) {
            return "";
        }
        try {
            return intent.getStringExtra(str);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
