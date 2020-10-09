package com.taobao.android.dinamic;

import android.app.Application;
import com.taobao.android.dinamic.dinamic.DinamicAppMonitor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class DinamicDefaultApplication {
    private static Application sApplication;

    public static synchronized Application getApplication() {
        Application application;
        synchronized (DinamicDefaultApplication.class) {
            if (sApplication == null) {
                sApplication = getSystemApp();
            }
            application = sApplication;
        }
        return application;
    }

    private static Application getSystemApp() {
        try {
            Class<?> cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread", new Class[0]);
            Field declaredField = cls.getDeclaredField("mInitialApplication");
            declaredField.setAccessible(true);
            return (Application) declaredField.get(declaredMethod.invoke((Object) null, new Object[0]));
        } catch (Exception unused) {
            DinamicAppMonitor appMonitor = DRegisterCenter.shareCenter().getAppMonitor();
            if (appMonitor != null) {
                appMonitor.counter_commit(Dinamic.TAG, "getApplication", 1.0d);
            }
            return null;
        }
    }
}
