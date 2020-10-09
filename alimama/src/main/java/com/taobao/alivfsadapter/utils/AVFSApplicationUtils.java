package com.taobao.alivfsadapter.utils;

import android.app.Application;
import android.content.Context;
import androidx.annotation.UiThread;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AVFSApplicationUtils {
    private static Application sApplication;

    @UiThread
    public static synchronized Application getApplication() {
        Application application;
        synchronized (AVFSApplicationUtils.class) {
            if (sApplication == null) {
                sApplication = getSystemApp();
            }
            application = sApplication;
        }
        return application;
    }

    @UiThread
    private static Application getSystemApp() {
        try {
            Class<?> cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread", new Class[0]);
            Field declaredField = cls.getDeclaredField("mInitialApplication");
            declaredField.setAccessible(true);
            return (Application) declaredField.get(declaredMethod.invoke((Object) null, new Object[0]));
        } catch (Exception unused) {
            return null;
        }
    }

    @UiThread
    public synchronized Context getApplicationContext() {
        if (sApplication == null) {
            sApplication = getSystemApp();
        }
        return sApplication == null ? null : sApplication.getApplicationContext();
    }
}
