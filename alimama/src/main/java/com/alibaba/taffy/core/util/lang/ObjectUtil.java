package com.alibaba.taffy.core.util.lang;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ObjectUtil {
    public static Map<String, String> getObjectFieldMap(Object obj) {
        if (obj == null) {
            return null;
        }
        Class<?> cls = obj.getClass();
        if (cls.isArray()) {
            return null;
        }
        Field[] declaredFields = cls.getDeclaredFields();
        HashMap hashMap = new HashMap(declaredFields.length);
        AccessibleObject.setAccessible(declaredFields, true);
        for (Field field : declaredFields) {
            String name = field.getName();
            try {
                Object obj2 = field.get(obj);
                if (obj2 != null) {
                    hashMap.put(name, obj2.toString());
                }
            } catch (IllegalAccessException unused) {
            }
        }
        return hashMap;
    }

    public static int hashCode(Object... objArr) {
        return Arrays.hashCode(objArr);
    }

    public static boolean equals(Object obj, Object obj2) {
        if (obj == obj2) {
            return true;
        }
        if (obj == null || obj2 == null) {
            return false;
        }
        return obj.equals(obj2);
    }
}
