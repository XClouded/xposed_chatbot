package com.xiaomi.push.service;

import android.content.SharedPreferences;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.al;
import com.xiaomi.push.c;
import com.xiaomi.push.ed;
import com.xiaomi.push.ee;
import com.xiaomi.push.gp;
import com.xiaomi.push.i;
import com.xiaomi.push.t;
import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ba {
    private static ba a = new ba();

    /* renamed from: a  reason: collision with other field name */
    private static String f880a;
    /* access modifiers changed from: private */

    /* renamed from: a  reason: collision with other field name */
    public al.b f881a;
    /* access modifiers changed from: private */

    /* renamed from: a  reason: collision with other field name */
    public ed.a f882a;

    /* renamed from: a  reason: collision with other field name */
    private List<a> f883a = new ArrayList();

    public static abstract class a {
        public void a(ed.a aVar) {
        }

        public void a(ee.b bVar) {
        }
    }

    private ba() {
    }

    public static ba a() {
        return a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static synchronized String m586a() {
        String str;
        synchronized (ba.class) {
            if (f880a == null) {
                SharedPreferences sharedPreferences = t.a().getSharedPreferences("XMPushServiceConfig", 0);
                f880a = sharedPreferences.getString("DeviceUUID", (String) null);
                if (f880a == null) {
                    f880a = i.a(t.a(), false);
                    if (f880a != null) {
                        sharedPreferences.edit().putString("DeviceUUID", f880a).commit();
                    }
                }
            }
            str = f880a;
        }
        return str;
    }

    private void b() {
        if (this.f882a == null) {
            d();
        }
    }

    private void c() {
        if (this.f881a == null) {
            this.f881a = new bb(this);
            gp.a(this.f881a);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x004b  */
    /* JADX WARNING: Removed duplicated region for block: B:21:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void d() {
        /*
            r4 = this;
            r0 = 0
            android.content.Context r1 = com.xiaomi.push.t.a()     // Catch:{ Exception -> 0x002b }
            java.lang.String r2 = "XMCloudCfg"
            java.io.FileInputStream r1 = r1.openFileInput(r2)     // Catch:{ Exception -> 0x002b }
            java.io.BufferedInputStream r2 = new java.io.BufferedInputStream     // Catch:{ Exception -> 0x002b }
            r2.<init>(r1)     // Catch:{ Exception -> 0x002b }
            com.xiaomi.push.b r0 = com.xiaomi.push.b.a((java.io.InputStream) r2)     // Catch:{ Exception -> 0x0025, all -> 0x0021 }
            com.xiaomi.push.ed$a r0 = com.xiaomi.push.ed.a.b((com.xiaomi.push.b) r0)     // Catch:{ Exception -> 0x0025, all -> 0x0021 }
            r4.f882a = r0     // Catch:{ Exception -> 0x0025, all -> 0x0021 }
            r2.close()     // Catch:{ Exception -> 0x0025, all -> 0x0021 }
            com.xiaomi.push.y.a((java.io.Closeable) r2)
            goto L_0x0047
        L_0x0021:
            r0 = move-exception
            r1 = r0
            r0 = r2
            goto L_0x0053
        L_0x0025:
            r0 = move-exception
            r1 = r0
            r0 = r2
            goto L_0x002c
        L_0x0029:
            r1 = move-exception
            goto L_0x0053
        L_0x002b:
            r1 = move-exception
        L_0x002c:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ all -> 0x0029 }
            r2.<init>()     // Catch:{ all -> 0x0029 }
            java.lang.String r3 = "load config failure: "
            r2.append(r3)     // Catch:{ all -> 0x0029 }
            java.lang.String r1 = r1.getMessage()     // Catch:{ all -> 0x0029 }
            r2.append(r1)     // Catch:{ all -> 0x0029 }
            java.lang.String r1 = r2.toString()     // Catch:{ all -> 0x0029 }
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r1)     // Catch:{ all -> 0x0029 }
            com.xiaomi.push.y.a((java.io.Closeable) r0)
        L_0x0047:
            com.xiaomi.push.ed$a r0 = r4.f882a
            if (r0 != 0) goto L_0x0052
            com.xiaomi.push.ed$a r0 = new com.xiaomi.push.ed$a
            r0.<init>()
            r4.f882a = r0
        L_0x0052:
            return
        L_0x0053:
            com.xiaomi.push.y.a((java.io.Closeable) r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.ba.d():void");
    }

    private void e() {
        try {
            if (this.f882a != null) {
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(t.a().openFileOutput("XMCloudCfg", 0));
                c a2 = c.a((OutputStream) bufferedOutputStream);
                this.f882a.a(a2);
                a2.a();
                bufferedOutputStream.close();
            }
        } catch (Exception e) {
            b.a("save config failure: " + e.getMessage());
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a  reason: collision with other method in class */
    public int m587a() {
        b();
        if (this.f882a != null) {
            return this.f882a.c();
        }
        return 0;
    }

    /* renamed from: a  reason: collision with other method in class */
    public ed.a m588a() {
        b();
        return this.f882a;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a  reason: collision with other method in class */
    public synchronized void m589a() {
        this.f883a.clear();
    }

    /* access modifiers changed from: package-private */
    public void a(ee.b bVar) {
        a[] aVarArr;
        if (bVar.d() && bVar.d() > a()) {
            c();
        }
        synchronized (this) {
            aVarArr = (a[]) this.f883a.toArray(new a[this.f883a.size()]);
        }
        for (a a2 : aVarArr) {
            a2.a(bVar);
        }
    }

    public synchronized void a(a aVar) {
        this.f883a.add(aVar);
    }
}
