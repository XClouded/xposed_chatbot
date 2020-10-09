package com.taobao.monitor.impl.data.newvisible;

import android.app.Activity;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PageData {
    private static Map<String, Float> sData = new ConcurrentHashMap();

    public static void setPageVisiblePercent(Class<?> cls, String str, float f) {
        String str2;
        check(cls, f);
        if (str == null) {
            str2 = cls.getName();
        } else {
            str2 = cls.getName() + "_" + str;
        }
        sData.put(str2, Float.valueOf(f));
    }

    public static void setPageVisiblePercent(Class<? extends Activity> cls, float f) {
        setPageVisiblePercent(cls, (String) null, f);
    }

    public static float getPageVisiblePercent(Class<?> cls, String str) {
        String str2;
        if (str == null) {
            str2 = cls.getName();
        } else {
            str2 = cls.getName() + "_" + str;
        }
        Float f = sData.get(str2);
        if (f == null) {
            return 1.0f;
        }
        return f.floatValue();
    }

    public static float getPageVisiblePercent(Class<?> cls) {
        return getPageVisiblePercent(cls, (String) null);
    }

    private static void check(Class<?> cls, float f) {
        if (cls == null) {
            throw new IllegalArgumentException("klass must not null");
        } else if (f > 1.0f || f < 0.0f) {
            throw new IllegalArgumentException("percent must in [0,1]");
        }
    }
}
