package com.ali.telescope.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class Reflector {
    public static Method method(Class cls, String str, Class[] clsArr) {
        try {
            Method declaredMethod = cls.getDeclaredMethod(str, clsArr);
            declaredMethod.setAccessible(true);
            return declaredMethod;
        } catch (Exception e) {
            StrictRuntime.onHandle(e);
            return null;
        }
    }

    public static Method method(ClassLoader classLoader, String str, String str2, Class[] clsArr) {
        try {
            Method declaredMethod = classLoader.loadClass(str).getDeclaredMethod(str2, clsArr);
            declaredMethod.setAccessible(true);
            return declaredMethod;
        } catch (Exception e) {
            StrictRuntime.onHandle(e);
            return null;
        }
    }

    public static Field field(ClassLoader classLoader, String str, String str2) {
        try {
            Field declaredField = classLoader.loadClass(str).getDeclaredField(str2);
            declaredField.setAccessible(true);
            return declaredField;
        } catch (Exception e) {
            StrictRuntime.onHandle(e);
            return null;
        }
    }

    public static Field field(Class cls, String str) {
        try {
            Field declaredField = cls.getDeclaredField(str);
            declaredField.setAccessible(true);
            return declaredField;
        } catch (Exception e) {
            StrictRuntime.onHandle(e);
            return null;
        }
    }

    public static List<Field> fieldWithMatchType(Class cls, Class cls2) {
        ArrayList arrayList = new ArrayList();
        for (Field field : cls.getDeclaredFields()) {
            if (field.getType().equals(cls2)) {
                field.setAccessible(true);
                arrayList.add(field);
            }
        }
        return arrayList;
    }

    public static Class<?>[] getAllInterfaces(Class cls) {
        ArrayList arrayList = new ArrayList();
        for (Class cls2 : cls.getInterfaces()) {
            if (!arrayList.contains(cls2)) {
                arrayList.add(cls2);
            }
        }
        for (Class superclass = cls.getSuperclass(); superclass != null; superclass = superclass.getSuperclass()) {
            for (Class cls3 : superclass.getInterfaces()) {
                if (!arrayList.contains(cls3)) {
                    arrayList.add(cls3);
                }
            }
        }
        return (Class[]) arrayList.toArray(new Class[0]);
    }
}
