package com.ali.user.mobile.app.init;

import android.content.Context;

public class Debuggable {
    private static boolean DEBUG = false;

    public static boolean isDebug() {
        return DEBUG;
    }

    public static void init(Context context) {
        try {
            DEBUG = (context.getApplicationInfo().flags & 2) != 0;
        } catch (Exception unused) {
        }
    }

    public static void setDebug(Boolean bool) {
        DEBUG = bool.booleanValue();
    }
}
