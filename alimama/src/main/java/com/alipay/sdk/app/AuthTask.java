package com.alipay.sdk.app;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import com.alipay.sdk.data.a;
import com.alipay.sdk.data.c;
import com.alipay.sdk.sys.b;
import com.alipay.sdk.util.e;
import com.alipay.sdk.util.l;
import com.alipay.sdk.util.n;
import com.alipay.sdk.widget.a;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AuthTask {
    static final Object a = e.class;
    private Activity b;
    private a c;

    public AuthTask(Activity activity) {
        this.b = activity;
        b.a().a(this.b, c.b());
        com.alipay.sdk.app.statistic.a.a(activity);
        this.c = new a(activity, a.c);
    }

    private e.a a() {
        return new a(this);
    }

    private void b() {
        if (this.c != null) {
            this.c.b();
        }
    }

    /* access modifiers changed from: private */
    public void c() {
        if (this.c != null) {
            this.c.c();
        }
    }

    public synchronized Map<String, String> authV2(String str, boolean z) {
        return l.a(auth(str, z));
    }

    public synchronized String auth(String str, boolean z) {
        String c2;
        if (z) {
            try {
                b();
            } catch (Throwable th) {
                throw th;
            }
        }
        b.a().a(this.b, c.b());
        c2 = j.c();
        i.a("");
        try {
            String a2 = a(this.b, str);
            com.alipay.sdk.data.a.g().a((Context) this.b);
            c();
            com.alipay.sdk.app.statistic.a.b(this.b, str);
            c2 = a2;
        } catch (Exception e) {
            com.alipay.sdk.util.c.a(e);
            com.alipay.sdk.data.a.g().a((Context) this.b);
            c();
            com.alipay.sdk.app.statistic.a.b(this.b, str);
        }
        return c2;
    }

    private String a(Activity activity, String str) {
        String a2 = new com.alipay.sdk.sys.a(this.b).a(str);
        List<a.C0001a> f = com.alipay.sdk.data.a.g().f();
        if (!com.alipay.sdk.data.a.g().q || f == null) {
            f = i.a;
        }
        if (n.b((Context) this.b, f)) {
            String a3 = new e(activity, a()).a(a2);
            if (!TextUtils.equals(a3, e.a) && !TextUtils.equals(a3, e.b)) {
                return TextUtils.isEmpty(a3) ? j.c() : a3;
            }
            com.alipay.sdk.app.statistic.a.a(com.alipay.sdk.app.statistic.c.b, com.alipay.sdk.app.statistic.c.M, "");
            return b(activity, a2);
        }
        com.alipay.sdk.app.statistic.a.a(com.alipay.sdk.app.statistic.c.b, com.alipay.sdk.app.statistic.c.N, "");
        return b(activity, a2);
    }

    private String b(Activity activity, String str) {
        k kVar;
        b();
        try {
            List<com.alipay.sdk.protocol.b> a2 = com.alipay.sdk.protocol.b.a(new com.alipay.sdk.packet.impl.a().a((Context) activity, str).c().optJSONObject(com.alipay.sdk.cons.c.c).optJSONObject(com.alipay.sdk.cons.c.d));
            c();
            for (int i = 0; i < a2.size(); i++) {
                if (a2.get(i).b() == com.alipay.sdk.protocol.a.WapPay) {
                    String a3 = a(a2.get(i));
                    c();
                    return a3;
                }
            }
        } catch (IOException e) {
            k b2 = k.b(k.NETWORK_ERROR.a());
            com.alipay.sdk.app.statistic.a.a(com.alipay.sdk.app.statistic.c.a, (Throwable) e);
            c();
            kVar = b2;
        } catch (Throwable th) {
            c();
            throw th;
        }
        c();
        kVar = null;
        if (kVar == null) {
            kVar = k.b(k.FAILED.a());
        }
        return j.a(kVar.a(), kVar.b(), "");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(4:10|11|12|13) */
    /* JADX WARNING: Code restructure failed: missing block: B:11:?, code lost:
        r0 = com.alipay.sdk.app.j.c();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0041, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        return com.alipay.sdk.app.j.c();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:?, code lost:
        return r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x002b, code lost:
        r4 = com.alipay.sdk.app.j.a();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x0033, code lost:
        if (android.text.TextUtils.isEmpty(r4) == false) goto L_?;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:10:0x003c */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String a(com.alipay.sdk.protocol.b r4) {
        /*
            r3 = this;
            java.lang.String[] r4 = r4.c()
            android.os.Bundle r0 = new android.os.Bundle
            r0.<init>()
            r1 = 0
            r4 = r4[r1]
            java.lang.String r1 = "url"
            r0.putString(r1, r4)
            android.content.Intent r4 = new android.content.Intent
            android.app.Activity r1 = r3.b
            java.lang.Class<com.alipay.sdk.app.H5AuthActivity> r2 = com.alipay.sdk.app.H5AuthActivity.class
            r4.<init>(r1, r2)
            r4.putExtras(r0)
            android.app.Activity r0 = r3.b
            r0.startActivity(r4)
            java.lang.Object r4 = a
            monitor-enter(r4)
            java.lang.Object r0 = a     // Catch:{ InterruptedException -> 0x003c }
            r0.wait()     // Catch:{ InterruptedException -> 0x003c }
            monitor-exit(r4)     // Catch:{ all -> 0x003a }
            java.lang.String r4 = com.alipay.sdk.app.j.a()
            boolean r0 = android.text.TextUtils.isEmpty(r4)
            if (r0 == 0) goto L_0x0039
            java.lang.String r4 = com.alipay.sdk.app.j.c()
        L_0x0039:
            return r4
        L_0x003a:
            r0 = move-exception
            goto L_0x0042
        L_0x003c:
            java.lang.String r0 = com.alipay.sdk.app.j.c()     // Catch:{ all -> 0x003a }
            monitor-exit(r4)     // Catch:{ all -> 0x003a }
            return r0
        L_0x0042:
            monitor-exit(r4)     // Catch:{ all -> 0x003a }
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.app.AuthTask.a(com.alipay.sdk.protocol.b):java.lang.String");
    }
}
