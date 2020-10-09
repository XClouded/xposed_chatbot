package com.alipay.sdk.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.net.Uri;
import com.alipay.android.app.IAlixPay;
import com.alipay.android.app.IRemoteServiceCallback;
import com.alipay.sdk.app.statistic.c;
import com.alipay.sdk.util.n;

public class e {
    public static final String a = "failed";
    public static final String b = "scheme_failed";
    /* access modifiers changed from: private */
    public Activity c;
    /* access modifiers changed from: private */
    public IAlixPay d;
    /* access modifiers changed from: private */
    public final Object e = IAlixPay.class;
    private boolean f;
    /* access modifiers changed from: private */
    public a g;
    private ServiceConnection h = new f(this);
    /* access modifiers changed from: private */
    public String i = null;
    private IRemoteServiceCallback j = new h(this);

    public interface a {
        void a();

        void b();
    }

    public e(Activity activity, a aVar) {
        this.c = activity;
        this.g = aVar;
    }

    /* JADX WARNING: Removed duplicated region for block: B:25:0x0056 A[Catch:{ Throwable -> 0x0062 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String a(java.lang.String r6) {
        /*
            r5 = this;
            java.lang.String r0 = ""
            r1 = 0
            com.alipay.sdk.data.a r2 = com.alipay.sdk.data.a.g()     // Catch:{ Throwable -> 0x0062 }
            java.util.List r2 = r2.f()     // Catch:{ Throwable -> 0x0062 }
            com.alipay.sdk.data.a r3 = com.alipay.sdk.data.a.g()     // Catch:{ Throwable -> 0x0062 }
            boolean r3 = r3.q     // Catch:{ Throwable -> 0x0062 }
            if (r3 == 0) goto L_0x0015
            if (r2 != 0) goto L_0x0017
        L_0x0015:
            java.util.List<com.alipay.sdk.data.a$a> r2 = com.alipay.sdk.app.i.a     // Catch:{ Throwable -> 0x0062 }
        L_0x0017:
            android.app.Activity r3 = r5.c     // Catch:{ Throwable -> 0x0062 }
            com.alipay.sdk.util.n$a r2 = com.alipay.sdk.util.n.a((android.content.Context) r3, (java.util.List<com.alipay.sdk.data.a.C0001a>) r2)     // Catch:{ Throwable -> 0x0062 }
            if (r2 == 0) goto L_0x005f
            boolean r3 = r2.a()     // Catch:{ Throwable -> 0x0062 }
            if (r3 != 0) goto L_0x005f
            boolean r3 = r2.b()     // Catch:{ Throwable -> 0x0062 }
            if (r3 == 0) goto L_0x002c
            goto L_0x005f
        L_0x002c:
            android.content.pm.PackageInfo r3 = r2.a     // Catch:{ Throwable -> 0x0062 }
            boolean r3 = com.alipay.sdk.util.n.a((android.content.pm.PackageInfo) r3)     // Catch:{ Throwable -> 0x0062 }
            if (r3 == 0) goto L_0x0037
            java.lang.String r2 = "failed"
            return r2
        L_0x0037:
            android.content.pm.PackageInfo r3 = r2.a     // Catch:{ Throwable -> 0x0062 }
            if (r3 == 0) goto L_0x004d
            java.lang.String r3 = "com.eg.android.AlipayGphone"
            android.content.pm.PackageInfo r4 = r2.a     // Catch:{ Throwable -> 0x0062 }
            java.lang.String r4 = r4.packageName     // Catch:{ Throwable -> 0x0062 }
            boolean r3 = r3.equals(r4)     // Catch:{ Throwable -> 0x0062 }
            if (r3 == 0) goto L_0x0048
            goto L_0x004d
        L_0x0048:
            android.content.pm.PackageInfo r3 = r2.a     // Catch:{ Throwable -> 0x0062 }
            java.lang.String r3 = r3.packageName     // Catch:{ Throwable -> 0x0062 }
            goto L_0x0051
        L_0x004d:
            java.lang.String r3 = com.alipay.sdk.util.n.a()     // Catch:{ Throwable -> 0x0062 }
        L_0x0051:
            r0 = r3
            android.content.pm.PackageInfo r3 = r2.a     // Catch:{ Throwable -> 0x0062 }
            if (r3 == 0) goto L_0x005b
            android.content.pm.PackageInfo r3 = r2.a     // Catch:{ Throwable -> 0x0062 }
            int r3 = r3.versionCode     // Catch:{ Throwable -> 0x0062 }
            r1 = r3
        L_0x005b:
            r5.a((com.alipay.sdk.util.n.a) r2)     // Catch:{ Throwable -> 0x0062 }
            goto L_0x006a
        L_0x005f:
            java.lang.String r2 = "failed"
            return r2
        L_0x0062:
            r2 = move-exception
            java.lang.String r3 = "biz"
            java.lang.String r4 = "CheckClientSignEx"
            com.alipay.sdk.app.statistic.a.a((java.lang.String) r3, (java.lang.String) r4, (java.lang.Throwable) r2)
        L_0x006a:
            java.lang.String r6 = r5.a(r6, r0, r1)
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.util.e.a(java.lang.String):java.lang.String");
    }

    private void a(n.a aVar) throws InterruptedException {
        PackageInfo packageInfo;
        if (aVar != null && (packageInfo = aVar.a) != null) {
            String str = packageInfo.packageName;
            Intent intent = new Intent();
            intent.setClassName(str, "com.alipay.android.app.TransProcessPayActivity");
            try {
                this.c.startActivity(intent);
            } catch (Throwable th) {
                com.alipay.sdk.app.statistic.a.a(c.b, c.H, th);
            }
            Thread.sleep(200);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:39:0x0110, code lost:
        r13 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0113, code lost:
        r13 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:58:0x01e3, code lost:
        com.alipay.sdk.app.statistic.a.a(com.alipay.sdk.app.statistic.c.b, "BSPEx", r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x01ed, code lost:
        com.alipay.sdk.app.statistic.a.a(com.alipay.sdk.app.statistic.c.b, "BSPWaiting", (java.lang.Throwable) r13);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:63:?, code lost:
        return com.alipay.sdk.app.j.a(com.alipay.sdk.app.k.g.a(), com.alipay.sdk.app.k.g.b(), "");
     */
    /* JADX WARNING: Code restructure failed: missing block: B:65:?, code lost:
        return b;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x0113 A[ExcHandler: InterruptedException (r13v17 'e' java.lang.InterruptedException A[CUSTOM_DECLARE]), Splitter:B:43:0x011b] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String a(java.lang.String r13, java.lang.String r14, int r15) {
        /*
            r12 = this;
            java.lang.String r0 = r12.a((java.lang.String) r13, (java.lang.String) r14)
            com.alipay.sdk.data.a r1 = com.alipay.sdk.data.a.g()
            boolean r1 = r1.b()
            java.lang.String r2 = "failed"
            boolean r2 = r2.equals(r0)
            r3 = 125(0x7d, float:1.75E-43)
            if (r2 == 0) goto L_0x002b
            java.lang.String r2 = "com.eg.android.AlipayGphone"
            boolean r2 = r2.equals(r14)
            if (r2 == 0) goto L_0x002b
            if (r15 <= r3) goto L_0x002b
            if (r1 != 0) goto L_0x002b
            java.lang.String r2 = "biz"
            java.lang.String r4 = "BSPNotStartByConfig"
            java.lang.String r5 = ""
            com.alipay.sdk.app.statistic.a.a((java.lang.String) r2, (java.lang.String) r4, (java.lang.String) r5)
        L_0x002b:
            java.lang.String r2 = "failed"
            boolean r2 = r2.equals(r0)
            if (r2 == 0) goto L_0x020a
            java.lang.String r2 = "com.eg.android.AlipayGphone"
            boolean r2 = r2.equals(r14)
            if (r2 == 0) goto L_0x020a
            if (r15 <= r3) goto L_0x020a
            if (r1 == 0) goto L_0x020a
            android.app.Activity r15 = r12.c
            if (r15 == 0) goto L_0x0207
            android.app.Activity r15 = r12.c
            boolean r15 = a((java.lang.String) r14, (android.content.Context) r15)
            if (r15 != 0) goto L_0x004d
            goto L_0x0207
        L_0x004d:
            java.util.concurrent.CountDownLatch r15 = new java.util.concurrent.CountDownLatch
            r0 = 1
            r15.<init>(r0)
            r1 = 32
            java.lang.String r1 = com.alipay.sdk.util.n.a((int) r1)
            java.lang.String r2 = "biz"
            java.lang.String r3 = "BSPStart"
            com.alipay.sdk.app.statistic.a.a((java.lang.String) r2, (java.lang.String) r3, (java.lang.String) r1)
            com.alipay.sdk.util.g r2 = new com.alipay.sdk.util.g
            r2.<init>(r12, r15)
            java.util.concurrent.ConcurrentHashMap<java.lang.String, java.lang.ref.WeakReference<com.alipay.sdk.app.AlipayResultActivity$b>> r3 = com.alipay.sdk.app.AlipayResultActivity.d
            java.lang.ref.WeakReference r4 = new java.lang.ref.WeakReference
            r4.<init>(r2)
            r3.put(r1, r4)
            r2 = 2
            java.lang.String r3 = "&"
            r4 = -1
            java.lang.String[] r3 = r13.split(r3, r4)     // Catch:{ Exception -> 0x0116 }
            java.lang.String r4 = ""
            java.lang.String r5 = ""
            int r6 = r3.length     // Catch:{ Exception -> 0x0116 }
            r7 = 0
            r8 = 0
        L_0x007e:
            r9 = 0
            if (r8 >= r6) goto L_0x00d3
            r9 = r3[r8]     // Catch:{ Exception -> 0x0116 }
            java.lang.String r10 = "bizcontext="
            boolean r10 = r9.startsWith(r10)     // Catch:{ Exception -> 0x0116 }
            if (r10 == 0) goto L_0x00d0
            java.lang.String r3 = "{"
            int r3 = r9.indexOf(r3)     // Catch:{ Exception -> 0x0116 }
            java.lang.String r4 = "}"
            int r4 = r9.lastIndexOf(r4)     // Catch:{ Exception -> 0x0116 }
            int r4 = r4 + r0
            java.lang.String r0 = r9.substring(r3, r4)     // Catch:{ Exception -> 0x0116 }
            int r3 = r9.indexOf(r0)     // Catch:{ Exception -> 0x0116 }
            java.lang.String r4 = r9.substring(r7, r3)     // Catch:{ Exception -> 0x0116 }
            int r5 = r0.length()     // Catch:{ Exception -> 0x0116 }
            int r3 = r3 + r5
            java.lang.String r5 = r9.substring(r3)     // Catch:{ Exception -> 0x0116 }
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ Exception -> 0x0116 }
            r3.<init>(r0)     // Catch:{ Exception -> 0x0116 }
            java.lang.String r0 = "sc"
            java.lang.String r0 = r3.optString(r0)     // Catch:{ Exception -> 0x0116 }
            java.lang.String r6 = "h5tonative"
            boolean r0 = r0.equals(r6)     // Catch:{ Exception -> 0x0116 }
            if (r0 == 0) goto L_0x00c8
            java.lang.String r0 = "sc"
            java.lang.String r6 = "h5tonative_scheme"
            r3.put(r0, r6)     // Catch:{ Exception -> 0x0116 }
            goto L_0x00d4
        L_0x00c8:
            java.lang.String r0 = "sc"
            java.lang.String r6 = "-_scheme"
            r3.put(r0, r6)     // Catch:{ Exception -> 0x0116 }
            goto L_0x00d4
        L_0x00d0:
            int r8 = r8 + 1
            goto L_0x007e
        L_0x00d3:
            r3 = r9
        L_0x00d4:
            boolean r0 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Exception -> 0x0116 }
            if (r0 != 0) goto L_0x0108
            int r0 = r13.indexOf(r9)     // Catch:{ Exception -> 0x0116 }
            int r6 = r13.lastIndexOf(r9)     // Catch:{ Exception -> 0x0116 }
            if (r0 != r6) goto L_0x0100
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x0116 }
            r0.<init>()     // Catch:{ Exception -> 0x0116 }
            r0.append(r4)     // Catch:{ Exception -> 0x0116 }
            java.lang.String r3 = r3.toString()     // Catch:{ Exception -> 0x0116 }
            r0.append(r3)     // Catch:{ Exception -> 0x0116 }
            r0.append(r5)     // Catch:{ Exception -> 0x0116 }
            java.lang.String r0 = r0.toString()     // Catch:{ Exception -> 0x0116 }
            java.lang.String r0 = r13.replace(r9, r0)     // Catch:{ Exception -> 0x0116 }
            r13 = r0
            goto L_0x0126
        L_0x0100:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException     // Catch:{ Exception -> 0x0116 }
            java.lang.String r3 = "multi ctx_args"
            r0.<init>(r3)     // Catch:{ Exception -> 0x0116 }
            throw r0     // Catch:{ Exception -> 0x0116 }
        L_0x0108:
            java.lang.RuntimeException r0 = new java.lang.RuntimeException     // Catch:{ Exception -> 0x0116 }
            java.lang.String r3 = "empty ctx_args"
            r0.<init>(r3)     // Catch:{ Exception -> 0x0116 }
            throw r0     // Catch:{ Exception -> 0x0116 }
        L_0x0110:
            r13 = move-exception
            goto L_0x01e3
        L_0x0113:
            r13 = move-exception
            goto L_0x01ed
        L_0x0116:
            r0 = move-exception
            java.lang.String r3 = "biz"
            java.lang.String r4 = "BSPSCReplaceEx"
            byte[] r5 = r13.getBytes()     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            java.lang.String r5 = android.util.Base64.encodeToString(r5, r2)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            com.alipay.sdk.app.statistic.a.a((java.lang.String) r3, (java.lang.String) r4, (java.lang.Throwable) r0, (java.lang.String) r5)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
        L_0x0126:
            org.json.JSONObject r0 = new org.json.JSONObject     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            r0.<init>()     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            java.lang.String r3 = "sourcePid"
            int r4 = android.os.Binder.getCallingPid()     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            r0.put(r3, r4)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            java.lang.String r3 = "external_info"
            r0.put(r3, r13)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            java.lang.String r13 = "pkgName"
            android.app.Activity r3 = r12.c     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            java.lang.String r3 = r3.getPackageName()     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            r0.put(r13, r3)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            java.lang.String r13 = "session"
            r0.put(r13, r1)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            java.lang.String r13 = r0.toString()     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            java.lang.String r0 = "UTF-8"
            byte[] r13 = r13.getBytes(r0)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            java.lang.String r13 = android.util.Base64.encodeToString(r13, r2)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            r0.<init>()     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            java.lang.String r1 = "alipays://platefromapi/startapp?appId=20000125&mqpSchemePay="
            r0.append(r1)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            java.lang.String r13 = android.net.Uri.encode(r13)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            r0.append(r13)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            java.lang.String r13 = r0.toString()     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            android.content.Intent r0 = new android.content.Intent     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            r0.<init>()     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            r0.setPackage(r14)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            r14 = 268435456(0x10000000, float:2.5243549E-29)
            r0.addFlags(r14)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            android.net.Uri r13 = android.net.Uri.parse(r13)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            r0.setData(r13)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            android.app.Activity r13 = r12.c     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            r13.startActivity(r0)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            com.alipay.sdk.data.a r13 = com.alipay.sdk.data.a.g()     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            android.app.Activity r14 = r12.c     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            android.content.Context r14 = r14.getApplicationContext()     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            r13.a((android.content.Context) r14)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            r15.await()     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            java.lang.String r13 = r12.i     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            java.lang.String r14 = "unknown"
            java.util.Map r15 = com.alipay.sdk.util.l.a(r13)     // Catch:{ Throwable -> 0x01b1, InterruptedException -> 0x0113 }
            java.lang.String r0 = "resultStatus"
            java.lang.Object r15 = r15.get(r0)     // Catch:{ Throwable -> 0x01b1, InterruptedException -> 0x0113 }
            java.lang.String r15 = (java.lang.String) r15     // Catch:{ Throwable -> 0x01b1, InterruptedException -> 0x0113 }
            if (r15 != 0) goto L_0x01af
            java.lang.String r14 = "null"
            goto L_0x01b9
        L_0x01aa:
            r14 = move-exception
            r11 = r15
            r15 = r14
            r14 = r11
            goto L_0x01b2
        L_0x01af:
            r14 = r15
            goto L_0x01b9
        L_0x01b1:
            r15 = move-exception
        L_0x01b2:
            java.lang.String r0 = "biz"
            java.lang.String r1 = "BSPStatEx"
            com.alipay.sdk.app.statistic.a.a((java.lang.String) r0, (java.lang.String) r1, (java.lang.Throwable) r15)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
        L_0x01b9:
            java.lang.String r15 = "biz"
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            r0.<init>()     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            java.lang.String r1 = "BSPDone-"
            r0.append(r1)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            r0.append(r14)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            java.lang.String r14 = r0.toString()     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            java.lang.String r0 = ""
            com.alipay.sdk.app.statistic.a.a((java.lang.String) r15, (java.lang.String) r14, (java.lang.String) r0)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            boolean r14 = android.text.TextUtils.isEmpty(r13)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            if (r14 == 0) goto L_0x0206
            java.lang.String r13 = "biz"
            java.lang.String r14 = "BSPEmpty"
            java.lang.String r15 = ""
            com.alipay.sdk.app.statistic.a.a((java.lang.String) r13, (java.lang.String) r14, (java.lang.String) r15)     // Catch:{ InterruptedException -> 0x0113, Throwable -> 0x0110 }
            java.lang.String r13 = "scheme_failed"
            goto L_0x0206
        L_0x01e3:
            java.lang.String r14 = "biz"
            java.lang.String r15 = "BSPEx"
            com.alipay.sdk.app.statistic.a.a((java.lang.String) r14, (java.lang.String) r15, (java.lang.Throwable) r13)
            java.lang.String r13 = "scheme_failed"
            goto L_0x0206
        L_0x01ed:
            java.lang.String r14 = "biz"
            java.lang.String r15 = "BSPWaiting"
            com.alipay.sdk.app.statistic.a.a((java.lang.String) r14, (java.lang.String) r15, (java.lang.Throwable) r13)
            com.alipay.sdk.app.k r13 = com.alipay.sdk.app.k.PAY_WAITTING
            int r13 = r13.a()
            com.alipay.sdk.app.k r14 = com.alipay.sdk.app.k.PAY_WAITTING
            java.lang.String r14 = r14.b()
            java.lang.String r15 = ""
            java.lang.String r13 = com.alipay.sdk.app.j.a(r13, r14, r15)
        L_0x0206:
            return r13
        L_0x0207:
            java.lang.String r13 = "scheme_failed"
            return r13
        L_0x020a:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.util.e.a(java.lang.String, java.lang.String, int):java.lang.String");
    }

    private static boolean a(String str, Context context) {
        try {
            Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
            intent.setClassName(str, "com.alipay.android.msp.ui.views.MspContainerActivity");
            if (intent.resolveActivityInfo(context.getPackageManager(), 0) != null) {
                return true;
            }
            com.alipay.sdk.app.statistic.a.a(c.b, "BSPDetectFail", "");
            return false;
        } catch (Throwable th) {
            com.alipay.sdk.app.statistic.a.a(c.b, "BSPDetectFail", th);
            return false;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:54:0x00ef, code lost:
        if (r7.c != null) goto L_0x00f1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00f1, code lost:
        r7.c.setRequestedOrientation(0);
        r7.f = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0131, code lost:
        if (r7.c != null) goto L_0x00f1;
     */
    /* JADX WARNING: Removed duplicated region for block: B:71:0x012f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String a(java.lang.String r8, java.lang.String r9) {
        /*
            r7 = this;
            android.content.Intent r0 = new android.content.Intent
            r0.<init>()
            r0.setPackage(r9)
            java.lang.String r1 = com.alipay.sdk.util.n.a((java.lang.String) r9)
            r0.setAction(r1)
            android.app.Activity r1 = r7.c
            java.lang.String r1 = com.alipay.sdk.util.n.a((android.content.Context) r1, (java.lang.String) r9)
            android.app.Activity r2 = r7.c     // Catch:{ Throwable -> 0x0174 }
            android.content.Context r2 = r2.getApplicationContext()     // Catch:{ Throwable -> 0x0174 }
            android.content.ServiceConnection r3 = r7.h     // Catch:{ Throwable -> 0x0174 }
            r4 = 1
            boolean r0 = r2.bindService(r0, r3, r4)     // Catch:{ Throwable -> 0x0174 }
            if (r0 == 0) goto L_0x016c
            java.lang.Object r0 = r7.e
            monitor-enter(r0)
            com.alipay.android.app.IAlixPay r2 = r7.d     // Catch:{ all -> 0x0169 }
            if (r2 != 0) goto L_0x0042
            java.lang.Object r2 = r7.e     // Catch:{ InterruptedException -> 0x003a }
            com.alipay.sdk.data.a r3 = com.alipay.sdk.data.a.g()     // Catch:{ InterruptedException -> 0x003a }
            int r3 = r3.a()     // Catch:{ InterruptedException -> 0x003a }
            long r5 = (long) r3     // Catch:{ InterruptedException -> 0x003a }
            r2.wait(r5)     // Catch:{ InterruptedException -> 0x003a }
            goto L_0x0042
        L_0x003a:
            r2 = move-exception
            java.lang.String r3 = "biz"
            java.lang.String r5 = "BindWaitTimeoutEx"
            com.alipay.sdk.app.statistic.a.a((java.lang.String) r3, (java.lang.String) r5, (java.lang.Throwable) r2)     // Catch:{ all -> 0x0169 }
        L_0x0042:
            monitor-exit(r0)     // Catch:{ all -> 0x0169 }
            r0 = 0
            r2 = 0
            com.alipay.android.app.IAlixPay r3 = r7.d     // Catch:{ Throwable -> 0x00fb }
            if (r3 != 0) goto L_0x00a0
            android.app.Activity r8 = r7.c     // Catch:{ Throwable -> 0x00fb }
            java.lang.String r8 = com.alipay.sdk.util.n.a((android.content.Context) r8, (java.lang.String) r9)     // Catch:{ Throwable -> 0x00fb }
            java.lang.String r9 = "biz"
            java.lang.String r3 = "ClientBindFailed"
            java.lang.StringBuilder r4 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x00fb }
            r4.<init>()     // Catch:{ Throwable -> 0x00fb }
            r4.append(r1)     // Catch:{ Throwable -> 0x00fb }
            java.lang.String r1 = "|"
            r4.append(r1)     // Catch:{ Throwable -> 0x00fb }
            r4.append(r8)     // Catch:{ Throwable -> 0x00fb }
            java.lang.String r8 = r4.toString()     // Catch:{ Throwable -> 0x00fb }
            com.alipay.sdk.app.statistic.a.a((java.lang.String) r9, (java.lang.String) r3, (java.lang.String) r8)     // Catch:{ Throwable -> 0x00fb }
            java.lang.String r8 = "failed"
            com.alipay.android.app.IAlixPay r9 = r7.d     // Catch:{ Throwable -> 0x0074 }
            com.alipay.android.app.IRemoteServiceCallback r1 = r7.j     // Catch:{ Throwable -> 0x0074 }
            r9.unregisterCallback(r1)     // Catch:{ Throwable -> 0x0074 }
            goto L_0x0078
        L_0x0074:
            r9 = move-exception
            com.alipay.sdk.util.c.a(r9)
        L_0x0078:
            android.app.Activity r9 = r7.c     // Catch:{ Throwable -> 0x0084 }
            android.content.Context r9 = r9.getApplicationContext()     // Catch:{ Throwable -> 0x0084 }
            android.content.ServiceConnection r1 = r7.h     // Catch:{ Throwable -> 0x0084 }
            r9.unbindService(r1)     // Catch:{ Throwable -> 0x0084 }
            goto L_0x0088
        L_0x0084:
            r9 = move-exception
            com.alipay.sdk.util.c.a(r9)
        L_0x0088:
            r7.g = r2
            r7.j = r2
            r7.h = r2
            r7.d = r2
            boolean r9 = r7.f
            if (r9 == 0) goto L_0x009f
            android.app.Activity r9 = r7.c
            if (r9 == 0) goto L_0x009f
            android.app.Activity r9 = r7.c
            r9.setRequestedOrientation(r0)
            r7.f = r0
        L_0x009f:
            return r8
        L_0x00a0:
            com.alipay.sdk.util.e$a r9 = r7.g     // Catch:{ Throwable -> 0x00fb }
            if (r9 == 0) goto L_0x00a9
            com.alipay.sdk.util.e$a r9 = r7.g     // Catch:{ Throwable -> 0x00fb }
            r9.a()     // Catch:{ Throwable -> 0x00fb }
        L_0x00a9:
            android.app.Activity r9 = r7.c     // Catch:{ Throwable -> 0x00fb }
            int r9 = r9.getRequestedOrientation()     // Catch:{ Throwable -> 0x00fb }
            if (r9 != 0) goto L_0x00b8
            android.app.Activity r9 = r7.c     // Catch:{ Throwable -> 0x00fb }
            r9.setRequestedOrientation(r4)     // Catch:{ Throwable -> 0x00fb }
            r7.f = r4     // Catch:{ Throwable -> 0x00fb }
        L_0x00b8:
            com.alipay.android.app.IAlixPay r9 = r7.d     // Catch:{ Throwable -> 0x00fb }
            com.alipay.android.app.IRemoteServiceCallback r1 = r7.j     // Catch:{ Throwable -> 0x00fb }
            r9.registerCallback(r1)     // Catch:{ Throwable -> 0x00fb }
            com.alipay.android.app.IAlixPay r9 = r7.d     // Catch:{ Throwable -> 0x00fb }
            java.lang.String r8 = r9.Pay(r8)     // Catch:{ Throwable -> 0x00fb }
            com.alipay.android.app.IAlixPay r9 = r7.d     // Catch:{ Throwable -> 0x00cd }
            com.alipay.android.app.IRemoteServiceCallback r1 = r7.j     // Catch:{ Throwable -> 0x00cd }
            r9.unregisterCallback(r1)     // Catch:{ Throwable -> 0x00cd }
            goto L_0x00d1
        L_0x00cd:
            r9 = move-exception
            com.alipay.sdk.util.c.a(r9)
        L_0x00d1:
            android.app.Activity r9 = r7.c     // Catch:{ Throwable -> 0x00dd }
            android.content.Context r9 = r9.getApplicationContext()     // Catch:{ Throwable -> 0x00dd }
            android.content.ServiceConnection r1 = r7.h     // Catch:{ Throwable -> 0x00dd }
            r9.unbindService(r1)     // Catch:{ Throwable -> 0x00dd }
            goto L_0x00e1
        L_0x00dd:
            r9 = move-exception
            com.alipay.sdk.util.c.a(r9)
        L_0x00e1:
            r7.g = r2
            r7.j = r2
            r7.h = r2
            r7.d = r2
            boolean r9 = r7.f
            if (r9 == 0) goto L_0x0134
            android.app.Activity r9 = r7.c
            if (r9 == 0) goto L_0x0134
        L_0x00f1:
            android.app.Activity r9 = r7.c
            r9.setRequestedOrientation(r0)
            r7.f = r0
            goto L_0x0134
        L_0x00f9:
            r8 = move-exception
            goto L_0x0135
        L_0x00fb:
            r8 = move-exception
            java.lang.String r9 = "biz"
            java.lang.String r1 = "ClientBindException"
            com.alipay.sdk.app.statistic.a.a((java.lang.String) r9, (java.lang.String) r1, (java.lang.Throwable) r8)     // Catch:{ all -> 0x00f9 }
            java.lang.String r8 = com.alipay.sdk.app.j.c()     // Catch:{ all -> 0x00f9 }
            com.alipay.android.app.IAlixPay r9 = r7.d     // Catch:{ Throwable -> 0x010f }
            com.alipay.android.app.IRemoteServiceCallback r1 = r7.j     // Catch:{ Throwable -> 0x010f }
            r9.unregisterCallback(r1)     // Catch:{ Throwable -> 0x010f }
            goto L_0x0113
        L_0x010f:
            r9 = move-exception
            com.alipay.sdk.util.c.a(r9)
        L_0x0113:
            android.app.Activity r9 = r7.c     // Catch:{ Throwable -> 0x011f }
            android.content.Context r9 = r9.getApplicationContext()     // Catch:{ Throwable -> 0x011f }
            android.content.ServiceConnection r1 = r7.h     // Catch:{ Throwable -> 0x011f }
            r9.unbindService(r1)     // Catch:{ Throwable -> 0x011f }
            goto L_0x0123
        L_0x011f:
            r9 = move-exception
            com.alipay.sdk.util.c.a(r9)
        L_0x0123:
            r7.g = r2
            r7.j = r2
            r7.h = r2
            r7.d = r2
            boolean r9 = r7.f
            if (r9 == 0) goto L_0x0134
            android.app.Activity r9 = r7.c
            if (r9 == 0) goto L_0x0134
            goto L_0x00f1
        L_0x0134:
            return r8
        L_0x0135:
            com.alipay.android.app.IAlixPay r9 = r7.d     // Catch:{ Throwable -> 0x013d }
            com.alipay.android.app.IRemoteServiceCallback r1 = r7.j     // Catch:{ Throwable -> 0x013d }
            r9.unregisterCallback(r1)     // Catch:{ Throwable -> 0x013d }
            goto L_0x0141
        L_0x013d:
            r9 = move-exception
            com.alipay.sdk.util.c.a(r9)
        L_0x0141:
            android.app.Activity r9 = r7.c     // Catch:{ Throwable -> 0x014d }
            android.content.Context r9 = r9.getApplicationContext()     // Catch:{ Throwable -> 0x014d }
            android.content.ServiceConnection r1 = r7.h     // Catch:{ Throwable -> 0x014d }
            r9.unbindService(r1)     // Catch:{ Throwable -> 0x014d }
            goto L_0x0151
        L_0x014d:
            r9 = move-exception
            com.alipay.sdk.util.c.a(r9)
        L_0x0151:
            r7.g = r2
            r7.j = r2
            r7.h = r2
            r7.d = r2
            boolean r9 = r7.f
            if (r9 == 0) goto L_0x0168
            android.app.Activity r9 = r7.c
            if (r9 == 0) goto L_0x0168
            android.app.Activity r9 = r7.c
            r9.setRequestedOrientation(r0)
            r7.f = r0
        L_0x0168:
            throw r8
        L_0x0169:
            r8 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0169 }
            throw r8
        L_0x016c:
            java.lang.Throwable r8 = new java.lang.Throwable     // Catch:{ Throwable -> 0x0174 }
            java.lang.String r9 = "bindService fail"
            r8.<init>(r9)     // Catch:{ Throwable -> 0x0174 }
            throw r8     // Catch:{ Throwable -> 0x0174 }
        L_0x0174:
            r8 = move-exception
            java.lang.String r9 = "biz"
            java.lang.String r0 = "ClientBindServiceFailed"
            com.alipay.sdk.app.statistic.a.a((java.lang.String) r9, (java.lang.String) r0, (java.lang.Throwable) r8)
            java.lang.String r8 = "failed"
            return r8
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.util.e.a(java.lang.String, java.lang.String):java.lang.String");
    }

    public void a() {
        this.c = null;
    }
}
