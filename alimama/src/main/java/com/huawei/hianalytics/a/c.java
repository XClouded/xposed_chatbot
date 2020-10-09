package com.huawei.hianalytics.a;

import com.huawei.hianalytics.e.a;
import com.huawei.hianalytics.e.e;

public abstract class c {
    public static String a(String str, String str2) {
        com.huawei.hianalytics.e.c j = j(str, str2);
        return j != null ? j.k() : "";
    }

    public static String b(String str, String str2) {
        com.huawei.hianalytics.e.c j = j(str, str2);
        return j != null ? j.f() : "";
    }

    public static String c(String str, String str2) {
        com.huawei.hianalytics.e.c j = j(str, str2);
        return j != null ? j.g() : "";
    }

    public static String d(String str, String str2) {
        com.huawei.hianalytics.e.c j = j(str, str2);
        return j != null ? j.h() : "";
    }

    public static String e(String str, String str2) {
        com.huawei.hianalytics.e.c j = j(str, str2);
        return j != null ? j.i() : "";
    }

    public static String f(String str, String str2) {
        com.huawei.hianalytics.e.c j = j(str, str2);
        return j != null ? j.j() : "";
    }

    public static int g(String str, String str2) {
        com.huawei.hianalytics.e.c j = j(str, str2);
        if (j != null) {
            return j.b();
        }
        return 7;
    }

    public static int h(String str, String str2) {
        com.huawei.hianalytics.e.c j = j(str, str2);
        if (j != null) {
            return j.c();
        }
        return 10;
    }

    public static boolean i(String str, String str2) {
        com.huawei.hianalytics.e.c j = j(str, str2);
        if (j != null) {
            return j.l();
        }
        return true;
    }

    private static com.huawei.hianalytics.e.c j(String str, String str2) {
        e a = a.a().a(str);
        if (a != null) {
            return a.a(str2);
        }
        return null;
    }
}
