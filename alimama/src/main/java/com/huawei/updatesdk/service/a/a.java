package com.huawei.updatesdk.service.a;

import android.content.Context;
import android.text.TextUtils;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public final class a {
    private static final Object a = new Object();
    private static a b;
    private b c;
    private String d;

    private a(Context context) {
        this.c = b.a("DeviceSessionUpdateSDK_V1", context);
    }

    public static a a() {
        a aVar;
        synchronized (a) {
            if (b == null) {
                b = new a(com.huawei.updatesdk.sdk.service.a.a.a().b());
            }
            aVar = b;
        }
        return aVar;
    }

    public void a(long j) {
        this.c.a("updatesdk.signtime", j);
    }

    public void a(String str) {
        this.c.a("updatesdk.signkey", str);
    }

    public long b() {
        return this.c.b("updatesdk.signtime", 0);
    }

    public void b(long j) {
        this.c.a("updatesdk.lastCheckDate", j);
    }

    public void b(String str) {
        if (!TextUtils.isEmpty(str)) {
            a(g());
        }
        this.c.a("appstore.client.sign.param", str);
    }

    public String c() {
        return this.c.b("updatesdk.signkey", "");
    }

    public void c(String str) {
        this.d = str;
    }

    public String d() {
        return this.c.b("appstore.client.sign.param", "");
    }

    public long e() {
        return this.c.b("updatesdk.lastCheckDate", 0);
    }

    public String f() {
        return this.d;
    }

    public long g() {
        try {
            return Long.parseLong(new SimpleDateFormat("yyyyMMdd", Locale.US).format(new Date()));
        } catch (NumberFormatException e) {
            com.huawei.updatesdk.sdk.a.c.a.a.a.d("DeviceSession", "get date error: " + e.toString());
            return 0;
        }
    }
}
