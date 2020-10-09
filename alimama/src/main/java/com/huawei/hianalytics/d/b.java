package com.huawei.hianalytics.d;

import android.content.Context;
import android.text.TextUtils;
import com.huawei.hianalytics.c.a;
import com.huawei.hianalytics.c.c;
import com.huawei.hianalytics.e.d;

public abstract class b {
    private a a(int i, Context context) {
        String str = "";
        if ((i & 4) != 0 && (i & 1) != 0) {
            return new a(com.huawei.hianalytics.c.b.UDID, a(b(context)));
        }
        if ((i & 1) != 0) {
            str = b(context);
            if (!TextUtils.isEmpty(str)) {
                return new a(com.huawei.hianalytics.c.b.SN, str);
            }
        }
        if ((i & 2) == 0) {
            return new a(com.huawei.hianalytics.c.b.EMPTY, str);
        }
        return new a(com.huawei.hianalytics.c.b.IMEI, c(context));
    }

    private a b(int i, Context context) {
        String str = "";
        if (i != 0) {
            str = f();
            if (!TextUtils.isEmpty(str)) {
                return new a(com.huawei.hianalytics.c.b.UDID, str);
            }
        }
        if ((i & 2) != 0) {
            str = c(context);
            if (!TextUtils.isEmpty(str)) {
                return new a(com.huawei.hianalytics.c.b.IMEI, str);
            }
        }
        if ((i & 1) == 0) {
            return new a(com.huawei.hianalytics.c.b.EMPTY, str);
        }
        return new a(com.huawei.hianalytics.c.b.SN, b(context));
    }

    private String b(Context context) {
        d f = com.huawei.hianalytics.e.a.a().f();
        if (TextUtils.isEmpty(f.c())) {
            f.a(c.e(context));
        }
        return f.c();
    }

    private String c(Context context) {
        d f = com.huawei.hianalytics.e.a.a().f();
        if (TextUtils.isEmpty(f.f())) {
            f.d(c.c(context));
        }
        return f.f();
    }

    private boolean e() {
        d f = com.huawei.hianalytics.e.a.a().f();
        if (TextUtils.isEmpty(f.e())) {
            f.c(c.b());
        }
        return !TextUtils.isEmpty(f.e());
    }

    private String f() {
        d f = com.huawei.hianalytics.e.a.a().f();
        if (TextUtils.isEmpty(f.g())) {
            f.e(c.a());
        }
        return f.g();
    }

    public a a(Context context) {
        String a = a();
        if (!TextUtils.isEmpty(a)) {
            return new a(com.huawei.hianalytics.c.b.UDID, a);
        }
        String b = b();
        if (!TextUtils.isEmpty(b)) {
            return new a(com.huawei.hianalytics.c.b.IMEI, b);
        }
        boolean e = e();
        String c = c();
        return !TextUtils.isEmpty(c) ? e ? new a(com.huawei.hianalytics.c.b.SN, c) : new a(com.huawei.hianalytics.c.b.UDID, a(c)) : e ? b(d(), context) : a(d(), context);
    }

    public abstract String a();

    public abstract String a(String str);

    public abstract String b();

    public abstract String c();

    public abstract int d();
}
