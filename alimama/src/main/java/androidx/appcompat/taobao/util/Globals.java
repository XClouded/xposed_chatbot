package androidx.appcompat.taobao.util;

import android.app.Application;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Deprecated
public class Globals {
    private static Application sApplication;

    public static synchronized Application getApplication() {
        Application application;
        synchronized (Globals.class) {
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
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
