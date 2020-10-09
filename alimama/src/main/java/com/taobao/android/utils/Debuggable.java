package com.taobao.android.utils;

import android.app.Application;

public class Debuggable {
    private static boolean DEBUG = false;

    public static boolean isDebug() {
        return DEBUG;
    }

    public static void init(Application application) {
        try {
            DEBUG = (application.getApplicationInfo().flags & 2) != 0;
        } catch (Exception unused) {
        }
    }
}
