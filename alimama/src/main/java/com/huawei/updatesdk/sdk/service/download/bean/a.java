package com.huawei.updatesdk.sdk.service.download.bean;

import java.security.SecureRandom;

public class a {
    private static int a = e();
    private static final Object g = new Object();
    private int b = -1;
    private int c = -1;
    private long d = 0;
    private long e = 0;
    private long f = 0;

    public a() {
    }

    public a(int i, int i2, long j, long j2) {
        this.b = i;
        this.c = i2;
        this.d = j;
        this.e = j2;
    }

    public static int a() {
        int i;
        synchronized (g) {
            a++;
            if (a == Integer.MIN_VALUE) {
                a = e();
            }
            i = a;
        }
        return i;
    }

    public static int e() {
        return new SecureRandom().nextInt();
    }

    public void a(long j) {
        this.f = j;
    }

    public long b() {
        return this.d;
    }

    public long c() {
        return this.e;
    }

    public long d() {
        return this.f;
    }
}
