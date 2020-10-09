package com.huawei.hms.support.log;

import android.content.Context;
import android.util.Log;
import com.huawei.hms.support.log.b.a;

/* compiled from: LogAdaptor */
public class b {
    private static final c a = new a();
    private int b = 4;
    private String c;

    public void a(Context context, int i, String str) {
        this.b = i;
        this.c = str;
        a.a(context, "HMSCore");
    }

    public boolean a(int i) {
        return i >= this.b;
    }

    public void a(int i, String str, String str2, Throwable th) {
        if (a(i)) {
            d b2 = b(i, str, str2, th);
            c cVar = a;
            cVar.a(b2.a() + b2.b(), i, str, str2 + 10 + Log.getStackTraceString(th));
        }
    }

    public void a(int i, String str, String str2) {
        if (a(i)) {
            d b2 = b(i, str, str2, (Throwable) null);
            a.a(b2.a() + b2.b(), i, str, str2);
        }
    }

    public void a(String str, String str2) {
        d b2 = b(4, str, str2, (Throwable) null);
        a.a(b2.a() + 10 + b2.b(), 4, str, str2);
    }

    private d b(int i, String str, String str2, Throwable th) {
        d dVar = new d(8, this.c, i, str);
        dVar.a(str2);
        dVar.a(th);
        return dVar;
    }
}
