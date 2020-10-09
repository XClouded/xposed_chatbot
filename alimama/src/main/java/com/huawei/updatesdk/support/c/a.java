package com.huawei.updatesdk.support.c;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;
import java.io.File;
import java.lang.reflect.Field;
import java.util.regex.Pattern;

public class a {
    private static Integer a = null;
    private static boolean b = false;
    private static boolean c = false;
    private static Field d;

    /* renamed from: com.huawei.updatesdk.support.c.a$a  reason: collision with other inner class name */
    public enum C0019a {
        NOT_INSTALLED,
        INSTALLED,
        INSTALLED_LOWCODE
    }

    public static PackageInfo a(String str, Context context) {
        try {
            return context.getPackageManager().getPackageInfo(str, 64);
        } catch (PackageManager.NameNotFoundException unused) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.d("PackageUtils", "not found: " + str);
            return null;
        }
    }

    public static C0019a a(Context context, String str) {
        String str2;
        String str3;
        StringBuilder sb;
        C0019a aVar = C0019a.NOT_INSTALLED;
        if (context != null && !TextUtils.isEmpty(str)) {
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, 0);
                return packageInfo != null ? packageInfo.versionCode < 70203000 ? C0019a.INSTALLED_LOWCODE : C0019a.INSTALLED : aVar;
            } catch (PackageManager.NameNotFoundException e) {
                str2 = "PackageUtils";
                sb = new StringBuilder();
                sb.append("isInstallByPackage NameNotFoundException:");
                str3 = e.toString();
                sb.append(str3);
                com.huawei.updatesdk.sdk.a.c.a.a.a.b(str2, sb.toString());
                return aVar;
            } catch (UnsupportedOperationException e2) {
                str2 = "PackageUtils";
                sb = new StringBuilder();
                sb.append("isInstallByPackage UnsupportedOperationException:");
                str3 = e2.toString();
                sb.append(str3);
                com.huawei.updatesdk.sdk.a.c.a.a.a.b(str2, sb.toString());
                return aVar;
            }
        }
        return aVar;
    }

    public static Integer a() {
        String str;
        String str2;
        StringBuilder sb;
        String str3;
        if (b) {
            return a;
        }
        try {
            Class<?> cls = Class.forName("android.content.pm.PackageParser");
            a = Integer.valueOf(cls.getDeclaredField("PARSE_IS_REMOVABLE_PREINSTALLED_APK").getInt(cls));
        } catch (NoSuchFieldException e) {
            str = "PackageUtils";
            sb = new StringBuilder();
            sb.append("isDelApp error NoSuchFieldException:");
            str3 = e.toString();
        } catch (ClassNotFoundException e2) {
            str = "PackageUtils";
            sb = new StringBuilder();
            sb.append("isDelApp error ClassNotFoundException:");
            str3 = e2.toString();
        } catch (IllegalAccessException e3) {
            str = "PackageUtils";
            sb = new StringBuilder();
            sb.append("isDelApp error IllegalAccessException:");
            str3 = e3.toString();
        } catch (IllegalArgumentException e4) {
            str = "PackageUtils";
            sb = new StringBuilder();
            sb.append("isDelApp error IllegalArgumentException:");
            str3 = e4.toString();
        } catch (Exception e5) {
            str = "PackageUtils";
            str2 = e5.toString();
            com.huawei.updatesdk.sdk.a.c.a.a.a.a(str, str2);
            b = true;
            return a;
        }
        b = true;
        return a;
        sb.append(str3);
        str2 = sb.toString();
        com.huawei.updatesdk.sdk.a.c.a.a.a.a(str, str2);
        b = true;
        return a;
    }

    public static boolean a(Context context) {
        String str;
        StringBuilder sb;
        String str2;
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo("com.huawei.appmarket", 0);
            return packageInfo != null && packageInfo.versionCode > 90000000;
        } catch (PackageManager.NameNotFoundException e) {
            str2 = "PackageUtils";
            sb = new StringBuilder();
            sb.append("isInstallByPackage NameNotFoundException:");
            str = e.toString();
            sb.append(str);
            com.huawei.updatesdk.sdk.a.c.a.a.a.b(str2, sb.toString());
        } catch (UnsupportedOperationException e2) {
            str2 = "PackageUtils";
            sb = new StringBuilder();
            sb.append("isInstallByPackage UnsupportedOperationException:");
            str = e2.toString();
            sb.append(str);
            com.huawei.updatesdk.sdk.a.c.a.a.a.b(str2, sb.toString());
        }
    }

    public static boolean a(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (!Pattern.compile("(\\.)+[\\\\/]+").matcher(str).find()) {
            return new File(str).delete();
        }
        com.huawei.updatesdk.sdk.a.c.a.a.a.a("PackageUtils", "remov APK fail. the apk path is not valid");
        return false;
    }

    public static Field b() {
        if (c) {
            return d;
        }
        try {
            d = ApplicationInfo.class.getField("hwFlags");
        } catch (NoSuchFieldException unused) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.a("PackageUtils", "can not find hwFlags");
        }
        c = true;
        return d;
    }

    public static boolean b(Context context, String str) {
        if (context != null && !TextUtils.isEmpty(str)) {
            try {
                return context.getPackageManager().getPackageInfo(str, 0) != null;
            } catch (Exception e) {
                com.huawei.updatesdk.sdk.a.c.a.a.a.b("PackageUtils", "isAppInstalled NameNotFoundException:" + e.toString());
            }
        }
        return false;
    }

    public static boolean c() {
        return Build.VERSION.SDK_INT >= 23 && new ContextWrapper(com.huawei.updatesdk.sdk.service.a.a.a().b()).checkSelfPermission("android.permission.WRITE_EXTERNAL_STORAGE") == 0;
    }
}
