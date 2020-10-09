package com.huawei.hianalytics.a;

import com.huawei.hianalytics.e.a;
import com.huawei.hianalytics.e.f;

public abstract class d {
    public static void a() {
        a.a().c();
    }

    public static void a(String str) {
        a.a().g().b(str);
    }

    public static String b() {
        return a.a().g().b();
    }

    public static void b(String str) {
        a.a().g().c(str);
    }

    public static String c() {
        return a.a().g().c();
    }

    public static void c(String str) {
        a.a().g().a(str);
    }

    public static String d() {
        return a.a().g().a();
    }

    public static int e() {
        f e = a.a().e();
        if (e != null) {
            return e.a();
        }
        return 4;
    }

    public static int f() {
        f e = a.a().e();
        if (e != null) {
            return e.b();
        }
        return 3;
    }

    public static int g() {
        f e = a.a().e();
        if (e != null) {
            return e.c();
        }
        return 5;
    }

    public static String h() {
        f e = a.a().e();
        return e != null ? e.d() : "";
    }

    public static String i() {
        f e = a.a().e();
        return e != null ? e.e() : "";
    }

    public static String[] j() {
        f e = a.a().e();
        return e != null ? e.f() : new String[0];
    }

    public static int k() {
        f e = a.a().e();
        if (e != null) {
            return e.g();
        }
        return 0;
    }

    public static boolean l() {
        f e = a.a().e();
        if (e != null) {
            return e.h();
        }
        return false;
    }

    public static boolean m() {
        f e = a.a().e();
        if (e != null) {
            return e.i();
        }
        return false;
    }

    public static boolean n() {
        f e = a.a().e();
        if (e != null) {
            return e.j();
        }
        return false;
    }

    public static String o() {
        f e = a.a().e();
        return e != null ? e.k() : "";
    }

    public static String p() {
        f e = a.a().e();
        return e != null ? e.l() : "";
    }

    public static String q() {
        f e = a.a().e();
        return e != null ? e.m() : "";
    }
}
