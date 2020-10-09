package com.alibaba.ut.abtest.internal.util;

import android.content.SharedPreferences;
import com.alibaba.ut.abtest.internal.ABConstants;
import com.alibaba.ut.abtest.internal.ABContext;
import java.util.Map;

public final class Preferences {
    private static final String TAG = "Preferences";
    private static Preferences instance;

    private Preferences() {
    }

    private SharedPreferences getSharedPreferences() {
        return ABContext.getInstance().getContext().getSharedPreferences(ABConstants.Preference.NAME, 0);
    }

    public static synchronized Preferences getInstance() {
        Preferences preferences;
        synchronized (Preferences.class) {
            if (instance == null) {
                instance = new Preferences();
            }
            preferences = instance;
        }
        return preferences;
    }

    public Map<String, ?> getAll() {
        try {
            return getSharedPreferences().getAll();
        } catch (Exception e) {
            LogUtils.logW(TAG, e.getMessage(), e);
            return null;
        }
    }

    public String getString(String str, String str2) {
        try {
            return getSharedPreferences().getString(str, str2);
        } catch (Exception e) {
            LogUtils.logW(TAG, e.getMessage(), e);
            return str2;
        }
    }

    public void putString(String str, String str2) {
        try {
            getSharedPreferences().edit().putString(str, str2).commit();
        } catch (Exception e) {
            LogUtils.logW(TAG, e.getMessage(), e);
        }
    }

    public void putStringAsync(String str, String str2) {
        try {
            getSharedPreferences().edit().putString(str, str2).apply();
        } catch (Exception e) {
            LogUtils.logW(TAG, e.getMessage(), e);
        }
    }

    public long getLong(String str, long j) {
        try {
            return getSharedPreferences().getLong(str, j);
        } catch (Exception e) {
            LogUtils.logW(TAG, e.getMessage(), e);
            return j;
        }
    }

    public void putLong(String str, long j) {
        try {
            getSharedPreferences().edit().putLong(str, j).commit();
        } catch (Exception e) {
            LogUtils.logW(TAG, e.getMessage(), e);
        }
    }

    public void putLongAsync(String str, long j) {
        try {
            getSharedPreferences().edit().putLong(str, j).apply();
        } catch (Exception e) {
            LogUtils.logW(TAG, e.getMessage(), e);
        }
    }

    public int getInt(String str, int i) {
        try {
            return getSharedPreferences().getInt(str, i);
        } catch (Exception e) {
            LogUtils.logW(TAG, e.getMessage(), e);
            return i;
        }
    }

    public void putInt(String str, int i) {
        try {
            getSharedPreferences().edit().putInt(str, i).commit();
        } catch (Exception e) {
            LogUtils.logW(TAG, e.getMessage(), e);
        }
    }

    public void putIntAsync(String str, int i) {
        try {
            getSharedPreferences().edit().putInt(str, i).apply();
        } catch (Exception e) {
            LogUtils.logW(TAG, e.getMessage(), e);
        }
    }

    public void remove(String str) {
        try {
            getSharedPreferences().edit().remove(str).commit();
        } catch (Exception e) {
            LogUtils.logW(TAG, e.getMessage(), e);
        }
    }

    public void removeAsync(String str) {
        try {
            getSharedPreferences().edit().remove(str).apply();
        } catch (Exception e) {
            LogUtils.logW(TAG, e.getMessage(), e);
        }
    }
}
