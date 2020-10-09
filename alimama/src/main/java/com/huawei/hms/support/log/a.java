package com.huawei.hms.support.log;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.taobao.weex.el.parse.Operators;

/* compiled from: HMSLog */
public class a {
    private static final b a = new b();

    public static void a(Context context, int i, String str) {
        a.a(context, i, str);
        a.a(str, "============================================================================" + 10 + "====== " + a(context) + 10 + "============================================================================");
    }

    private static String a(Context context) {
        PackageManager packageManager = context.getPackageManager();
        if (packageManager == null) {
            return "HMS-[unknown-version]";
        }
        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return "HMS-" + packageInfo.versionName + Operators.BRACKET_START_STR + packageInfo.versionCode + Operators.BRACKET_END_STR;
        } catch (PackageManager.NameNotFoundException unused) {
            return "HMS-[unknown-version]";
        }
    }

    public static void a(String str, String str2) {
        a.a(3, str, str2);
    }

    public static void b(String str, String str2) {
        a.a(4, str, str2);
    }

    public static void c(String str, String str2) {
        a.a(5, str, str2);
    }

    public static void d(String str, String str2) {
        a.a(6, str, str2);
    }

    public static void a(String str, String str2, Throwable th) {
        a.a(6, str, str2, th);
    }
}
