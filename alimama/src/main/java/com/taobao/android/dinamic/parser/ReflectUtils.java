package com.taobao.android.dinamic.parser;

import androidx.collection.LruCache;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class ReflectUtils {
    private static final LruCache<String, Method> methodsCache = new LruCache<>(8);

    public static Object getValue(Object obj, String str) {
        try {
            if (obj instanceof Class) {
                Field field = ((Class) obj).getField(str);
                field.setAccessible(true);
                return field.get((Object) null);
            }
            Field field2 = obj.getClass().getField(str);
            field2.setAccessible(true);
            return field2.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Object invoke(Object obj, String str, Object... objArr) {
        try {
            Method findMethod = findMethod(obj, str, objArr);
            if (findMethod == null) {
                return null;
            }
            if (obj instanceof Class) {
                return findMethod.invoke((Object) null, objArr);
            }
            return findMethod.invoke(obj, objArr);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Method findMethod(Object obj, String str, Object... objArr) {
        Class<?> cls;
        if (obj instanceof Class) {
            cls = (Class) obj;
        } else {
            cls = obj.getClass();
        }
        String str2 = cls.getName() + "_" + str;
        Method method = methodsCache.get(str2);
        if (method != null) {
            return method;
        }
        while (cls != null) {
            Method[] declaredMethods = cls.getDeclaredMethods();
            int length = declaredMethods.length;
            int i = 0;
            while (i < length) {
                Method method2 = declaredMethods[i];
                if (!str.equals(method2.getName()) || !isMatchParameterTypes(method2.getGenericParameterTypes(), objArr)) {
                    i++;
                } else {
                    method2.setAccessible(true);
                    methodsCache.put(str2, method2);
                    return method2;
                }
            }
            cls = cls.getSuperclass();
        }
        return null;
    }

    private static boolean isMatchParameterTypes(Type[] typeArr, Object... objArr) {
        if (typeArr.length != objArr.length) {
            return false;
        }
        if (typeArr.length == 0) {
            return true;
        }
        for (int i = 0; i < objArr.length; i++) {
            Object obj = objArr[i];
            Class cls = typeArr[i];
            Class cls2 = null;
            if (cls instanceof Class) {
                cls2 = cls;
            }
            if (cls2 == null) {
                return true;
            }
            Class<?> cls3 = obj.getClass();
        }
        return true;
    }
}
