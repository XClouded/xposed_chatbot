package com.taobao.ju.track.util;

import android.content.Intent;

public class IntentUtil {
    public static String getComponentSimpleClassName(Intent intent) {
        if (intent == null) {
            return null;
        }
        String className = intent.getComponent().getClassName();
        return className.substring(Math.max(0, className.lastIndexOf(".") + 1));
    }
}
