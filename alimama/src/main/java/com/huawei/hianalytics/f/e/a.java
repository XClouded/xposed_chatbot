package com.huawei.hianalytics.f.e;

import java.util.HashMap;
import java.util.Map;

public final class a {
    private static a a;
    private volatile Map<String, b> b = new HashMap();

    private a() {
    }

    public static a a() {
        if (a == null) {
            b();
        }
        return a;
    }

    private b b(String str) {
        if (!this.b.containsKey(str)) {
            this.b.put(str, new b());
        }
        return this.b.get(str);
    }

    private static synchronized void b() {
        synchronized (a.class) {
            if (a == null) {
                a = new a();
            }
        }
    }

    public b a(String str, long j) {
        b b2 = b(str);
        b2.a(j);
        return b2;
    }

    public void a(String str) {
        b(str).c();
    }

    public void b(String str, long j) {
        b(str).b(j);
    }

    public void c(String str, long j) {
        b(str).c(j);
    }
}
