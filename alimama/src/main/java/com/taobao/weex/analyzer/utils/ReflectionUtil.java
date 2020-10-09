package com.taobao.weex.analyzer.utils;

import androidx.annotation.Nullable;
import com.taobao.weex.utils.WXLogUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Locale;

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
    public static Field tryGetDeclaredField(Class<?> cls, String str) {
        try {
            return cls.getDeclaredField(str);
        } catch (NoSuchFieldException unused) {
            WXLogUtils.d(TAG, String.format(Locale.CHINA, "Could not retrieve %s field from %s", new Object[]{str, cls}));
            return null;
        }
    }

    @Nullable
    public static Method tryGetMethod(Class<?> cls, String str, Class... clsArr) {
        try {
            return cls.getMethod(str, clsArr);
        } catch (NoSuchMethodException unused) {
            WXLogUtils.d(TAG, String.format(Locale.CHINA, "Could not retrieve %s method from %s", new Object[]{str, cls}));
            return null;
        }
    }

    public static Object tryInvokeMethod(Object obj, Method method, Object... objArr) {
        if (method == null) {
            return null;
        }
        try {
            return method.invoke(obj, objArr);
        } catch (Exception unused) {
            WXLogUtils.d(TAG, String.format(Locale.CHINA, "Could not invoke %s method from %s", new Object[]{method, obj}));
            return null;
        }
    }

    @Nullable
    public static Object getFieldValue(Field field, Object obj) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
