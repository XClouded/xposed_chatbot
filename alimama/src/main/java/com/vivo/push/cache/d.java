package com.vivo.push.cache;

import android.content.Context;
import android.text.TextUtils;
import com.vivo.push.util.h;
import com.vivo.push.util.p;
import com.vivo.push.util.y;
import java.util.ArrayList;
import java.util.List;

/* compiled from: ICacheSettings */
public abstract class d<T> {
    public static final byte[] a = {34, 32, Framer.ENTER_FRAME_PREFIX, 37, Framer.ENTER_FRAME_PREFIX, 34, 32, Framer.ENTER_FRAME_PREFIX, Framer.ENTER_FRAME_PREFIX, Framer.ENTER_FRAME_PREFIX, 34, 41, 35, 32, 32, 32};
    public static final byte[] b = {Framer.ENTER_FRAME_PREFIX, 34, 35, 36, 37, 38, 39, 40, 41, 32, 38, 37, 36, 35, 34, Framer.ENTER_FRAME_PREFIX};
    protected static final Object c = new Object();
    protected List<T> d = new ArrayList();
    protected Context e;

    /* access modifiers changed from: protected */
    public abstract String a();

    /* access modifiers changed from: protected */
    public abstract List<T> a(String str);

    /* access modifiers changed from: package-private */
    public abstract String b(String str) throws Exception;

    protected d(Context context) {
        this.e = context.getApplicationContext();
        c();
    }

    public final void c() {
        synchronized (c) {
            h.a(a());
            this.d.clear();
            String a2 = y.b(this.e).a(a(), (String) null);
            if (TextUtils.isEmpty(a2)) {
                p.d("CacheSettings", "ClientManager init " + a() + " strApps empty.");
            } else if (a2.length() > 10000) {
                p.d("CacheSettings", "sync " + a() + " strApps lenght too large");
                d();
            } else {
                try {
                    p.d("CacheSettings", "ClientManager init " + a() + " strApps : " + a2);
                    List a3 = a(b(a2));
                    if (a3 != null) {
                        this.d.addAll(a3);
                    }
                } catch (Exception e2) {
                    d();
                    p.d("CacheSettings", p.a((Throwable) e2));
                }
            }
        }
    }

    public final void d() {
        synchronized (c) {
            this.d.clear();
            y.b(this.e).b(a(), "");
            p.d("CacheSettings", "clear " + a() + " strApps");
        }
    }
}
