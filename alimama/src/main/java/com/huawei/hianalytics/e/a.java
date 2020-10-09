package com.huawei.hianalytics.e;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class a {
    static Map<String, e> a = new HashMap();
    private static final Object e = new Object();
    private static a g;
    private d b = new d();
    private f c = null;
    private g d = new g();
    private boolean f = false;

    private a() {
    }

    public static a a() {
        if (g == null) {
            h();
        }
        return g;
    }

    private static synchronized void h() {
        synchronized (a.class) {
            if (g == null) {
                g = new a();
            }
        }
    }

    public e a(String str) {
        return a.get(str);
    }

    public void a(f fVar) {
        synchronized (e) {
            this.c = fVar;
            this.f = true;
        }
    }

    public void a(String str, e eVar) {
        a.put(str, eVar);
    }

    public Set<String> b() {
        return a.keySet();
    }

    public void c() {
        synchronized (e) {
            this.c = null;
            this.f = false;
        }
    }

    public boolean d() {
        boolean z;
        synchronized (e) {
            z = this.f;
        }
        return z;
    }

    public f e() {
        f fVar;
        synchronized (e) {
            fVar = this.c;
        }
        return fVar;
    }

    public d f() {
        return this.b;
    }

    public g g() {
        return this.d;
    }
}
