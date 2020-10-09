package com.huawei.hianalytics.a;

import com.huawei.hianalytics.e.b;
import com.huawei.hianalytics.e.c;
import com.huawei.hianalytics.e.e;

public abstract class a {
    public static String a(String str, String str2) {
        b k = k(str, str2);
        return k != null ? k.e() : "";
    }

    public static String b(String str, String str2) {
        b k = k(str, str2);
        return k != null ? k.f() : "";
    }

    public static String c(String str, String str2) {
        b k = k(str, str2);
        return k != null ? k.g() : "";
    }

    public static String d(String str, String str2) {
        b k = k(str, str2);
        return k != null ? k.h() : "";
    }

    public static boolean e(String str, String str2) {
        c a;
        e a2 = com.huawei.hianalytics.e.a.a().a(str);
        if (a2 == null || (a = a2.a(str2)) == null) {
            return false;
        }
        return a.d();
    }

    public static boolean f(String str, String str2) {
        c a;
        e a2 = com.huawei.hianalytics.e.a.a().a(str);
        if (a2 == null || (a = a2.a(str2)) == null) {
            return false;
        }
        return a.e();
    }

    public static boolean g(String str, String str2) {
        b k = k(str, str2);
        return k != null && k.a();
    }

    public static boolean h(String str, String str2) {
        b k = k(str, str2);
        return k != null && k.c();
    }

    public static boolean i(String str, String str2) {
        b k = k(str, str2);
        return k != null && k.d();
    }

    public static boolean j(String str, String str2) {
        b k = k(str, str2);
        return k != null && k.b();
    }

    private static b k(String str, String str2) {
        c a;
        e a2 = com.huawei.hianalytics.e.a.a().a(str);
        if (a2 == null || (a = a2.a(str2)) == null) {
            return null;
        }
        return a.a();
    }
}
