package com.ali.telescope.util;

import android.content.Context;
import android.content.SharedPreferences;

public class TMSharePreferenceUtil {
    private static SharedPreferences sSharedPreferences;

    public static long getLong(Context context, String str, long j) {
        init(context);
        return sSharedPreferences.getLong(str, j);
    }

    public static void putLong(Context context, String str, long j) {
        init(context);
        sSharedPreferences.edit().putLong(str, j).apply();
    }

    private static void init(Context context) {
        if (sSharedPreferences == null) {
            sSharedPreferences = context.getSharedPreferences("telescope_info", 0);
        }
    }
}
