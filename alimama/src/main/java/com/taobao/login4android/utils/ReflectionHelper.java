package com.taobao.login4android.utils;

import android.util.Log;
import java.lang.reflect.Method;

public class ReflectionHelper {
    public static final String Tag = "login.ReflectionHelper";

    public static <T> T invokeMethod(Class cls, Method method, Object... objArr) {
        if (method == null) {
            return null;
        }
        try {
            return method.invoke(cls, objArr);
        } catch (Exception e) {
            Log.e(Tag, "invokeMethod error", e);
            return null;
        }
    }
}
