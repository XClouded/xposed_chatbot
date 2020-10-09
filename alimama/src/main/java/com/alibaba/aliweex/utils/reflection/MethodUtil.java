package com.alibaba.aliweex.utils.reflection;

import android.text.TextUtils;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class MethodUtil {
    private static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];

    private static Class<?>[] toClass(Object... objArr) {
        if (objArr == null) {
            return null;
        }
        if (objArr.length == 0) {
            return EMPTY_CLASS_ARRAY;
        }
        Class<?>[] clsArr = new Class[objArr.length];
        for (int i = 0; i < objArr.length; i++) {
            clsArr[i] = objArr[i] == null ? null : objArr[i].getClass();
        }
        return clsArr;
    }

    private static Method getMethod(Class<?> cls, String str, Class<?>[] clsArr) {
        Method method;
        if (cls == null || TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            method = cls.getDeclaredMethod(str, clsArr);
            try {
                method.setAccessible(true);
            } catch (NoSuchMethodException e) {
                e = e;
            }
        } catch (NoSuchMethodException e2) {
            e = e2;
            method = null;
            e.printStackTrace();
            return method;
        }
        return method;
    }

    private static Object invokeMethod(Object obj, Method method, Object... objArr) {
        try {
            return method.invoke(obj, objArr);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InvocationTargetException e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static Object invokeMethod(Object obj, String str, Object... objArr) {
        Method method = getMethod(obj.getClass(), str, toClass(objArr));
        if (method == null) {
            return null;
        }
        return invokeMethod(obj, method, objArr);
    }

    public static Object invokeStaticMethod(Class cls, String str, Object... objArr) {
        Method method = getMethod(cls, str, toClass(objArr));
        if (method == null) {
            return null;
        }
        return invokeMethod((Object) null, method, objArr);
    }
}
