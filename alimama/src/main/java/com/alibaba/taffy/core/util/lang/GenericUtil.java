package com.alibaba.taffy.core.util.lang;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class GenericUtil {
    public static Class getGnericType(Class cls, int i) {
        List<Type> genericType = getGenericType(cls);
        if (i < 0 || i >= genericType.size()) {
            return null;
        }
        return (Class) genericType.get(i);
    }

    public static List<Type> getGenericType(Class cls) {
        ArrayList arrayList = new ArrayList();
        Type genericSuperclass = cls.getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType)) {
            return null;
        }
        Type[] actualTypeArguments = ((ParameterizedType) genericSuperclass).getActualTypeArguments();
        if (actualTypeArguments != null) {
            for (Type type : actualTypeArguments) {
                if (type instanceof Class) {
                    arrayList.add(type);
                }
            }
        }
        return arrayList;
    }
}
