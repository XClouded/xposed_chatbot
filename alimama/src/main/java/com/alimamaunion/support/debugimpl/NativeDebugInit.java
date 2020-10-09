package com.alimamaunion.support.debugimpl;

import android.app.Application;

public class NativeDebugInit {
    private static Application sApplication;

    public static void init(Application application, String str) {
    }

    public static Application getApplication() {
        return sApplication;
    }
}
