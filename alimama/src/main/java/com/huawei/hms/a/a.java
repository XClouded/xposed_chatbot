package com.huawei.hms.a;

import android.util.Log;
import java.lang.reflect.InvocationTargetException;

/* compiled from: HwBuildEx */
public class a {

    /* renamed from: com.huawei.hms.a.a$a  reason: collision with other inner class name */
    /* compiled from: HwBuildEx */
    public static class C0009a {
        public static final int a = a.a("ro.build.hw_emui_api_level", 0);
    }

    public static int a(String str, int i) {
        try {
            Class<?> cls = Class.forName("android.os.SystemProperties");
            return ((Integer) cls.getDeclaredMethod("getInt", new Class[]{String.class, Integer.TYPE}).invoke(cls, new Object[]{str, Integer.valueOf(i)})).intValue();
        } catch (ClassCastException | ClassNotFoundException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | InvocationTargetException unused) {
            Log.e("HwBuildEx", "An exception occurred while reading: EMUI_SDK_INT");
            return i;
        }
    }
}
