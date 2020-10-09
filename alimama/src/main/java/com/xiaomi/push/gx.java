package com.xiaomi.push;

import android.content.Context;
import android.net.TrafficStats;
import android.os.Process;
import android.os.SystemClock;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.XMPushService;

public class gx implements fp {
    private int a;

    /* renamed from: a  reason: collision with other field name */
    private long f432a = 0;

    /* renamed from: a  reason: collision with other field name */
    fm f433a;

    /* renamed from: a  reason: collision with other field name */
    XMPushService f434a;

    /* renamed from: a  reason: collision with other field name */
    private Exception f435a;

    /* renamed from: a  reason: collision with other field name */
    private String f436a;
    private long b = 0;
    private long c = 0;
    private long d = 0;
    private long e = 0;
    private long f = 0;

    gx(XMPushService xMPushService) {
        this.f434a = xMPushService;
        this.f436a = "";
        b();
        int myUid = Process.myUid();
        this.f = TrafficStats.getUidRxBytes(myUid);
        this.e = TrafficStats.getUidTxBytes(myUid);
    }

    private void b() {
        this.b = 0;
        this.d = 0;
        this.f432a = 0;
        this.c = 0;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (as.b(this.f434a)) {
            this.f432a = elapsedRealtime;
        }
        if (this.f434a.c()) {
            this.c = elapsedRealtime;
        }
    }

    private synchronized void c() {
        b.c("stat connpt = " + this.f436a + " netDuration = " + this.b + " ChannelDuration = " + this.d + " channelConnectedTime = " + this.c);
        fc fcVar = new fc();
        fcVar.f332a = 0;
        fcVar.a(fb.CHANNEL_ONLINE_RATE.a());
        fcVar.a(this.f436a);
        fcVar.d((int) (System.currentTimeMillis() / 1000));
        fcVar.b((int) (this.b / 1000));
        fcVar.c((int) (this.d / 1000));
        gy.a().a(fcVar);
        b();
    }

    /* access modifiers changed from: package-private */
    public Exception a() {
        return this.f435a;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0070, code lost:
        return;
     */
    /* renamed from: a  reason: collision with other method in class */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized void m353a() {
        /*
            r11 = this;
            monitor-enter(r11)
            com.xiaomi.push.service.XMPushService r0 = r11.f434a     // Catch:{ all -> 0x0071 }
            if (r0 != 0) goto L_0x0007
            monitor-exit(r11)
            return
        L_0x0007:
            com.xiaomi.push.service.XMPushService r0 = r11.f434a     // Catch:{ all -> 0x0071 }
            java.lang.String r0 = com.xiaomi.push.as.a((android.content.Context) r0)     // Catch:{ all -> 0x0071 }
            com.xiaomi.push.service.XMPushService r1 = r11.f434a     // Catch:{ all -> 0x0071 }
            boolean r1 = com.xiaomi.push.as.b(r1)     // Catch:{ all -> 0x0071 }
            long r2 = android.os.SystemClock.elapsedRealtime()     // Catch:{ all -> 0x0071 }
            long r4 = r11.f432a     // Catch:{ all -> 0x0071 }
            r6 = 0
            int r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r8 <= 0) goto L_0x002b
            long r4 = r11.b     // Catch:{ all -> 0x0071 }
            long r8 = r11.f432a     // Catch:{ all -> 0x0071 }
            r10 = 0
            long r8 = r2 - r8
            long r4 = r4 + r8
            r11.b = r4     // Catch:{ all -> 0x0071 }
            r11.f432a = r6     // Catch:{ all -> 0x0071 }
        L_0x002b:
            long r4 = r11.c     // Catch:{ all -> 0x0071 }
            int r8 = (r4 > r6 ? 1 : (r4 == r6 ? 0 : -1))
            if (r8 == 0) goto L_0x003d
            long r4 = r11.d     // Catch:{ all -> 0x0071 }
            long r8 = r11.c     // Catch:{ all -> 0x0071 }
            r10 = 0
            long r8 = r2 - r8
            long r4 = r4 + r8
            r11.d = r4     // Catch:{ all -> 0x0071 }
            r11.c = r6     // Catch:{ all -> 0x0071 }
        L_0x003d:
            if (r1 == 0) goto L_0x006f
            java.lang.String r1 = r11.f436a     // Catch:{ all -> 0x0071 }
            boolean r1 = android.text.TextUtils.equals(r1, r0)     // Catch:{ all -> 0x0071 }
            if (r1 != 0) goto L_0x004f
            long r4 = r11.b     // Catch:{ all -> 0x0071 }
            r8 = 30000(0x7530, double:1.4822E-319)
            int r1 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r1 > 0) goto L_0x0058
        L_0x004f:
            long r4 = r11.b     // Catch:{ all -> 0x0071 }
            r8 = 5400000(0x5265c0, double:2.6679545E-317)
            int r1 = (r4 > r8 ? 1 : (r4 == r8 ? 0 : -1))
            if (r1 <= 0) goto L_0x005b
        L_0x0058:
            r11.c()     // Catch:{ all -> 0x0071 }
        L_0x005b:
            r11.f436a = r0     // Catch:{ all -> 0x0071 }
            long r0 = r11.f432a     // Catch:{ all -> 0x0071 }
            int r4 = (r0 > r6 ? 1 : (r0 == r6 ? 0 : -1))
            if (r4 != 0) goto L_0x0065
            r11.f432a = r2     // Catch:{ all -> 0x0071 }
        L_0x0065:
            com.xiaomi.push.service.XMPushService r0 = r11.f434a     // Catch:{ all -> 0x0071 }
            boolean r0 = r0.c()     // Catch:{ all -> 0x0071 }
            if (r0 == 0) goto L_0x006f
            r11.c = r2     // Catch:{ all -> 0x0071 }
        L_0x006f:
            monitor-exit(r11)
            return
        L_0x0071:
            r0 = move-exception
            monitor-exit(r11)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.gx.m353a():void");
    }

    public void a(fm fmVar) {
        a();
        this.c = SystemClock.elapsedRealtime();
        ha.a(0, fb.CONN_SUCCESS.a(), fmVar.a(), fmVar.a());
    }

    public void a(fm fmVar, int i, Exception exc) {
        if (this.a == 0 && this.f435a == null) {
            this.a = i;
            this.f435a = exc;
            ha.b(fmVar.a(), exc);
        }
        if (i == 22 && this.c != 0) {
            long a2 = fmVar.a() - this.c;
            if (a2 < 0) {
                a2 = 0;
            }
            this.d += a2 + ((long) (fs.b() / 2));
            this.c = 0;
        }
        a();
        int myUid = Process.myUid();
        long uidRxBytes = TrafficStats.getUidRxBytes(myUid);
        long uidTxBytes = TrafficStats.getUidTxBytes(myUid);
        b.c("Stats rx=" + (uidRxBytes - this.f) + ", tx=" + (uidTxBytes - this.e));
        this.f = uidRxBytes;
        this.e = uidTxBytes;
    }

    public void a(fm fmVar, Exception exc) {
        ha.a(0, fb.CHANNEL_CON_FAIL.a(), 1, fmVar.a(), as.b(this.f434a) ? 1 : 0);
        a();
    }

    public void b(fm fmVar) {
        this.a = 0;
        this.f435a = null;
        this.f433a = fmVar;
        this.f436a = as.a((Context) this.f434a);
        ha.a(0, fb.CONN_SUCCESS.a());
    }
}
