package com.alibaba.aliweex.interceptor.utils;

import java.lang.reflect.Method;
import javax.annotation.Nullable;

public final class ReflectionUtil {
    private static final String TAG = "ReflectionUtil";

    private ReflectionUtil() {
    }

    @Nullable
    public static Class<?> tryGetClassForName(String str) {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException unused) {
            return null;
        }
    }

    @Nullable
    public static Method tryGetMethod(Class<?> cls, String str, Class... clsArr) {
        try {
            return cls.getMethod(str, clsArr);
        } catch (NoSuchMethodException unused) {
            return null;
        }
    }

    public static Object tryInvokeMethod(Object obj, Method method, Object... objArr) {
        if (method == null || obj == null) {
            return null;
        }
        try {
            return method.invoke(obj, objArr);
        } catch (Exception unused) {
            return null;
        }
    }
}
