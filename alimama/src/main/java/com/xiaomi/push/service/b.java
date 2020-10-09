package com.xiaomi.push.service;

import com.xiaomi.push.Cif;
import com.xiaomi.push.ai;
import com.xiaomi.push.hg;
import com.xiaomi.push.iq;
import java.lang.ref.WeakReference;

public class b extends ai.a {
    private Cif a;

    /* renamed from: a  reason: collision with other field name */
    private WeakReference<XMPushService> f878a;

    /* renamed from: a  reason: collision with other field name */
    private boolean f879a = false;

    public b(Cif ifVar, WeakReference<XMPushService> weakReference, boolean z) {
        this.a = ifVar;
        this.f878a = weakReference;
        this.f879a = z;
    }

    public int a() {
        return 22;
    }

    public void run() {
        XMPushService xMPushService;
        if (this.f878a != null && this.a != null && (xMPushService = (XMPushService) this.f878a.get()) != null) {
            this.a.a(aj.a());
            this.a.a(false);
            com.xiaomi.channel.commonutils.logger.b.c("MoleInfo aw_ping : send aw_Ping msg " + this.a.a());
            try {
                String c = this.a.c();
                xMPushService.a(c, iq.a(w.a(c, this.a.b(), this.a, hg.Notification)), this.f879a);
            } catch (Exception e) {
                com.xiaomi.channel.commonutils.logger.b.d("MoleInfo aw_ping : send help app ping error" + e.toString());
            }
        }
    }
}
