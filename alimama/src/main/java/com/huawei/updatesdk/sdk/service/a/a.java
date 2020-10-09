package com.huawei.updatesdk.sdk.service.a;

import android.content.Context;
import android.net.ConnectivityManager;

public class a {
    private static a b;
    private static final Object d = new Object();
    private Context a;
    private ConnectivityManager c = null;

    public a(Context context) {
        this.a = context.getApplicationContext();
    }

    public static a a() {
        a aVar;
        synchronized (d) {
            aVar = b;
        }
        return aVar;
    }

    public static void a(Context context) {
        synchronized (d) {
            if (b == null) {
                b = new a(context);
            }
        }
    }

    public Context b() {
        return this.a;
    }

    public ConnectivityManager c() {
        if (this.c == null) {
            this.c = (ConnectivityManager) this.a.getSystemService("connectivity");
        }
        return this.c;
    }
}
