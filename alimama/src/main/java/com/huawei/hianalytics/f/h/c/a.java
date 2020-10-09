package com.huawei.hianalytics.f.h.c;

import android.text.TextUtils;

public class a {
    private String a = "";
    private long b = 0;
    private long c = 0;

    public a(String str, long j) {
        this.a = str;
        this.b = j;
    }

    public a(String str, long j, long j2) {
        this.a = str;
        this.b = j;
        this.c = j2;
    }

    public String a() {
        return this.a;
    }

    public long b() {
        return this.b;
    }

    public long c() {
        return this.c;
    }

    public boolean d() {
        return !TextUtils.isEmpty(this.a) && this.b > 0 && this.c >= 0;
    }
}
