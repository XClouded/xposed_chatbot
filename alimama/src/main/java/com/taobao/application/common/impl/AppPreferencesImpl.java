package com.taobao.application.common.impl;

import com.taobao.application.common.IAppPreferences;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AppPreferencesImpl implements IAppPreferences {
    private final Map<String, Object> map;

    private AppPreferencesImpl() {
        this.map = new ConcurrentHashMap();
    }

    public static AppPreferencesImpl instance() {
        return Holder.INSTANCE;
    }

    public int getInt(String str, int i) {
        Integer num = (Integer) getValue(str);
        return num != null ? num.intValue() : i;
    }

    public void putInt(String str, int i) {
        putValue(str, Integer.valueOf(i));
    }

    public long getLong(String str, long j) {
        Long l = (Long) getValue(str);
        return l != null ? l.longValue() : j;
    }

    public void putLong(String str, long j) {
        putValue(str, Long.valueOf(j));
    }

    public float getFloat(String str, float f) {
        Float f2 = (Float) getValue(str);
        return f2 != null ? f2.floatValue() : f;
    }

    public void putFloat(String str, float f) {
        putValue(str, Float.valueOf(f));
    }

    public String getString(String str, String str2) {
        String str3 = (String) getValue(str);
        return str3 != null ? str3 : str2;
    }

    public void putString(String str, String str2) {
        putValue(str, str2);
    }

    public boolean getBoolean(String str, boolean z) {
        Boolean bool = (Boolean) getValue(str);
        return bool != null ? bool.booleanValue() : z;
    }

    public void putBoolean(String str, boolean z) {
        putValue(str, Boolean.valueOf(z));
    }

    private <T> T getValue(String str) {
        return this.map.get(str);
    }

    private void putValue(String str, Object obj) {
        Object put;
        if (str != null && obj != null && (put = this.map.put(str, obj)) != null && put.getClass() != obj.getClass()) {
            throw new IllegalArgumentException(String.format("new value type:%s != old value type:%s", new Object[]{obj.getClass(), put.getClass()}));
        }
    }

    private static class Holder {
        static final AppPreferencesImpl INSTANCE = new AppPreferencesImpl();

        private Holder() {
        }
    }
}
