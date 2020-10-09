package com.alibaba.ut.abtest.internal.adapter;

import com.alibaba.ut.abtest.internal.util.LogUtils;
import java.util.HashMap;
import java.util.Map;

public class AdapterBinder {
    private static final String TAG = "AdapterBinder";
    private static Map<Integer, Class> classMap = new HashMap();
    private static Map<Integer, Object> instanceMap = new HashMap();

    public static void registerAdapter(int i, Class cls) {
        classMap.put(Integer.valueOf(i), cls);
    }

    public static void registerAdapter(int i, Object obj) {
        instanceMap.put(Integer.valueOf(i), obj);
        classMap.put(Integer.valueOf(i), obj.getClass());
    }

    public static <T> T getAdapter(int i) {
        return getInstance(i);
    }

    protected static Object getInstance(int i) {
        Object obj = instanceMap.get(Integer.valueOf(i));
        if (obj != null) {
            return obj;
        }
        Class cls = classMap.get(Integer.valueOf(i));
        if (cls == null) {
            return null;
        }
        try {
            Object newInstance = cls.getConstructor(new Class[0]).newInstance(new Object[0]);
            instanceMap.put(Integer.valueOf(i), newInstance);
            return newInstance;
        } catch (Exception e) {
            LogUtils.logE(TAG, "getInstance", e);
            return null;
        }
    }
}
