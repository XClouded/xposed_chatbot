package com.huawei.updatesdk.support.a;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import com.huawei.updatesdk.sdk.a.d.e;

public final class a {
    private static final String a = "a";
    private static String b;

    private a() {
    }

    public static String a() {
        return "com.huawei.updatesdk";
    }

    private static void a(String str) {
        b = str;
    }

    public static String b() {
        if (b != null) {
            return b;
        }
        try {
            String packageName = com.huawei.updatesdk.sdk.service.a.a.a().b().getPackageName();
            PackageInfo packageInfo = com.huawei.updatesdk.sdk.service.a.a.a().b().getPackageManager().getPackageInfo(com.huawei.updatesdk.sdk.service.a.a.a().b().getPackageName(), 0);
            if (packageInfo != null) {
                packageName = packageName + packageInfo.versionName;
            }
            String str = Build.BRAND;
            if (e.a(str)) {
                str = "other";
            }
            a(packageName + "_" + str);
            return b;
        } catch (PackageManager.NameNotFoundException e) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.d(a, "getUserAgent() " + e.toString());
            return null;
        }
    }
}
