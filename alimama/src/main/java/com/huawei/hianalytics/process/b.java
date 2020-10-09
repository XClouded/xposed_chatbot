package com.huawei.hianalytics.process;

import android.content.Context;
import com.huawei.hianalytics.f.f.h;
import com.huawei.hianalytics.f.g.i;
import com.huawei.hianalytics.f.h.a.a;
import java.util.LinkedHashMap;

public final class b {
    private static b a;
    private Context b;
    private final Object c = new Object();

    private b() {
    }

    public static b a() {
        if (a == null) {
            c();
        }
        return a;
    }

    private static synchronized void c() {
        synchronized (b.class) {
            if (a == null) {
                a = new b();
            }
        }
    }

    public void a(Context context) {
        synchronized (this.c) {
            if (this.b == null) {
                this.b = context;
                h.a().a(this.b);
                a.a(this.b);
            }
        }
    }

    public void a(String str) {
        a.b(str);
    }

    public void a(String str, int i) {
        h.a().a(str, i);
    }

    public void a(String str, int i, String str2, LinkedHashMap<String, String> linkedHashMap) {
        h.a().a(str, i, str2, linkedHashMap);
    }

    public void a(String str, Context context) {
        h.a().a(str, context);
    }

    public void a(String str, Context context, int i) {
        h.a().a(str, context, i.a(i), com.huawei.hianalytics.a.b.g());
    }

    public void a(String str, Context context, String str2, String str3) {
        h.a().a(str, str2, str3);
    }

    public void a(String str, Context context, LinkedHashMap<String, String> linkedHashMap) {
        h.a().a(str, context, linkedHashMap);
    }

    public void a(String str, String str2) {
        h.a().b(str, str2);
    }

    public void a(String str, String str2, LinkedHashMap<String, String> linkedHashMap) {
        h.a().a(str, str2, linkedHashMap);
    }

    public void a(boolean z) {
        h.a().a(z);
    }

    public void b() {
        h.a().c();
    }

    public void b(String str, int i, String str2, LinkedHashMap<String, String> linkedHashMap) {
        h.a().b(str, i, str2, linkedHashMap);
    }

    public void b(String str, Context context) {
        h.a().b(str, context);
    }

    public void b(String str, Context context, LinkedHashMap<String, String> linkedHashMap) {
        h.a().b(str, context, linkedHashMap);
    }

    public void b(String str, String str2) {
        h.a().c(str, str2);
    }

    public void b(String str, String str2, LinkedHashMap<String, String> linkedHashMap) {
        h.a().b(str, str2, linkedHashMap);
    }
}
