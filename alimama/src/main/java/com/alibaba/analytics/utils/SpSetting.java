package com.alibaba.analytics.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpSetting {
    public static String get(Context context, String str) {
        SharedPreferences sharedPreferences;
        if (context == null || (sharedPreferences = context.getSharedPreferences("ut_setting", 4)) == null) {
            return null;
        }
        return sharedPreferences.getString(str, (String) null);
    }

    public static void put(Context context, String str, String str2) {
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor edit;
        if (context != null && (sharedPreferences = context.getSharedPreferences("ut_setting", 4)) != null && (edit = sharedPreferences.edit()) != null) {
            edit.putString(str, str2);
            edit.apply();
        }
    }
}
