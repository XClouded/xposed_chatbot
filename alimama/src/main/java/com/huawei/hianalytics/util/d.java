package com.huawei.hianalytics.util;

import android.content.Context;
import android.content.SharedPreferences;
import com.huawei.hianalytics.g.b;

public class d {
    public static SharedPreferences a(Context context, String str) {
        return context.getSharedPreferences(b(context, str), 0);
    }

    public static void a(SharedPreferences sharedPreferences, String str, Object obj) {
        if (sharedPreferences != null && str != null && !str.isEmpty()) {
            SharedPreferences.Editor edit = sharedPreferences.edit();
            if (obj instanceof Boolean) {
                edit.putBoolean(str, ((Boolean) obj).booleanValue());
            } else if (obj instanceof Float) {
                edit.putFloat(str, ((Float) obj).floatValue());
            } else if (obj instanceof Integer) {
                edit.putInt(str, ((Integer) obj).intValue());
            } else if (obj instanceof Long) {
                edit.putLong(str, ((Long) obj).longValue());
            } else {
                edit.putString(str, (String) obj);
            }
            edit.commit();
        }
    }

    public static Object b(SharedPreferences sharedPreferences, String str, Object obj) {
        if (sharedPreferences == null || str == null || str.isEmpty()) {
            return "";
        }
        try {
            if (obj instanceof Boolean) {
                return Boolean.valueOf(sharedPreferences.getBoolean(str, ((Boolean) obj).booleanValue()));
            }
            if (obj instanceof Float) {
                return Float.valueOf(sharedPreferences.getFloat(str, ((Float) obj).floatValue()));
            }
            if (obj instanceof Integer) {
                return Integer.valueOf(sharedPreferences.getInt(str, ((Integer) obj).intValue()));
            }
            if (obj instanceof Long) {
                return Long.valueOf(sharedPreferences.getLong(str, ((Long) obj).longValue()));
            }
            if (obj instanceof String) {
                return sharedPreferences.getString(str, (String) obj);
            }
            return obj;
        } catch (ClassCastException unused) {
            b.c("HiAnalyticsSharedPreference", "getInfoFromSP() class cast Exception !");
        }
    }

    public static String b(Context context, String str) {
        String packageName = context.getPackageName();
        return "hianalytics_" + str + "_" + packageName;
    }
}
