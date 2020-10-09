package com.huawei.hianalytics.g;

import android.content.Context;

public class c {
    private static f a = d.a();
    private int b = 4;
    private String c;
    private boolean d = false;

    private g b(int i, String str, String str2) {
        g gVar = new g(7, this.c, i, str);
        gVar.a(str2);
        return gVar;
    }

    public void a(int i, String str, String str2) {
        if (a(i)) {
            g b2 = b(i, str, str2);
            a.a(b2.a() + b2.b(), i, str, str2);
        }
    }

    public void a(Context context, int i, String str) {
        this.b = i;
        this.c = str;
        a.a(context, "HiAnalytics");
        this.d = true;
    }

    public void a(String str, String str2) {
        g b2 = b(4, str, str2);
        a.a(b2.a() + 10 + b2.b(), 4, str, str2);
    }

    public boolean a(int i) {
        return this.d && i >= this.b;
    }
}
