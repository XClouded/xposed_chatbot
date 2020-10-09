package com.huawei.hianalytics.log.d;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import com.huawei.hianalytics.a.d;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.log.g.c;

public class a {
    @SuppressLint({"StaticFieldLeak"})
    private static a b;
    private static final Object c = new Object();
    private Context a;

    public static a a() {
        if (b == null) {
            b();
        }
        return b;
    }

    private static synchronized void b() {
        synchronized (a.class) {
            if (b == null) {
                b = new a();
            }
        }
    }

    private void c() {
        String h = d.h();
        String i = d.i();
        String[] j = d.j();
        int k = d.k();
        if (TextUtils.isEmpty(h) || TextUtils.isEmpty(i)) {
            com.huawei.hianalytics.log.g.a.b();
            d.a();
            b.c("HiAnalytics/logServer", "log server init failure, Parameter error!");
            return;
        }
        com.huawei.hianalytics.d.a.a().a(this.a);
        com.huawei.hianalytics.log.g.a.a().a(this.a);
        if (k == 1) {
            c.a(this.a).a(j);
        } else {
            c.a(this.a).a();
        }
    }

    public void a(Context context) {
        synchronized (c) {
            if (this.a == null) {
                this.a = context;
                c();
            }
        }
    }

    public void a(boolean z) {
        if (this.a == null) {
            b.c("HiAnalytics/logServer", "refreshLogConfig(): LogServer need to init first!");
            return;
        }
        b.b("HianalyticsSDK", "refreshLogConfig() is executed.");
        if (z) {
            com.huawei.hianalytics.log.e.a.a(this.a);
        }
        c();
    }
}
