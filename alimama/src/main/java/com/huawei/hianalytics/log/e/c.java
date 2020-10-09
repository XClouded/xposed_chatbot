package com.huawei.hianalytics.log.e;

import android.content.Context;
import android.content.SharedPreferences;
import com.huawei.hianalytics.util.d;

public class c {
    public static SharedPreferences a(Context context, String str) {
        return d.a(context, str);
    }

    public static String a(Context context) {
        return "hianalytics_log_" + context.getPackageName();
    }

    public static void a(SharedPreferences sharedPreferences, String str, Object obj) {
        d.a(sharedPreferences, str, obj);
    }

    public static SharedPreferences b(Context context) {
        return context.getSharedPreferences(a(context), 0);
    }

    public static Object b(SharedPreferences sharedPreferences, String str, Object obj) {
        return d.b(sharedPreferences, str, obj);
    }
}
