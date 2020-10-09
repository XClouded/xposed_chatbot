package com.huawei.hianalytics.e;

import com.huawei.hianalytics.g.b;

public class e {
    private c a;
    private c b;
    private c c;
    private c d;
    private String e;

    public e(String str) {
        this.e = str;
    }

    public c a() {
        return this.a;
    }

    public c a(String str) {
        if ("oper".equals(str)) {
            return b();
        }
        if ("maint".equals(str)) {
            return a();
        }
        if ("diffprivacy".equals(str)) {
            return d();
        }
        if ("preins".equals(str)) {
            return c();
        }
        b.c("HianalyticsSDK", "HiAnalyticsInstData.getConfig(type): wrong type!");
        return null;
    }

    public void a(c cVar) {
        this.a = cVar;
    }

    public c b() {
        return this.b;
    }

    public void b(c cVar) {
        this.b = cVar;
    }

    public c c() {
        return this.d;
    }

    public void c(c cVar) {
        this.d = cVar;
    }

    public c d() {
        return this.c;
    }

    public void d(c cVar) {
        this.c = cVar;
    }
}
