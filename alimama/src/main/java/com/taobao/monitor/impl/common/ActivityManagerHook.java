package com.taobao.monitor.impl.common;

import android.app.ActivityManager;
import android.os.Build;
import android.util.Log;
import com.taobao.monitor.impl.logger.Logger;
import com.taobao.monitor.impl.util.FieldUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ActivityManagerHook {
    private static final String TAG = "ActivityManagerHook";

    public static void start() {
        Object obj;
        Log.d(TAG, "start Hook IActivityManager...");
        try {
            Class<?> cls = Class.forName("android.app.ActivityManagerNative");
            if (Build.VERSION.SDK_INT >= 26) {
                obj = FieldUtils.getObjectFromField((Object) null, ActivityManager.class.getDeclaredField("IActivityManagerSingleton"));
            } else {
                obj = FieldUtils.getObjectFromField((Object) null, cls.getDeclaredField("gDefault"));
            }
            Class<?> cls2 = Class.forName("android.util.Singleton");
            try {
                Method declaredMethod = cls2.getDeclaredMethod("get", new Class[0]);
                declaredMethod.setAccessible(true);
                declaredMethod.invoke(obj, new Object[0]);
            } catch (Exception unused) {
            }
            Field declaredField = cls2.getDeclaredField("mInstance");
            Object objectFromField = FieldUtils.getObjectFromField(obj, declaredField);
            if (objectFromField != null) {
                Class<?> cls3 = Class.forName("android.app.IActivityManager");
                FieldUtils.setFieldToObject(obj, declaredField, objectFromField, Proxy.newProxyInstance(ActivityManagerHook.class.getClassLoader(), new Class[]{cls3}, new ActivityManagerProxy(objectFromField)));
                Logger.d(TAG, "Hook IActivityManager success");
            }
        } catch (Exception unused2) {
            Logger.d(TAG, "Hook IActivityManager failed");
        }
    }
}
