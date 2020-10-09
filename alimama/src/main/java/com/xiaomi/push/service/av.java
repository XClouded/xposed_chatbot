package com.xiaomi.push.service;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.gy;
import com.xiaomi.push.service.XMPushService;

class av {
    private static int d = 300000;
    private int a;

    /* renamed from: a  reason: collision with other field name */
    private long f869a;

    /* renamed from: a  reason: collision with other field name */
    private XMPushService f870a;
    private int b = 0;
    private int c = 0;

    public av(XMPushService xMPushService) {
        this.f870a = xMPushService;
        this.a = 500;
        this.f869a = 0;
    }

    private int a() {
        if (this.b > 8) {
            return 300000;
        }
        double random = (Math.random() * 2.0d) + 1.0d;
        if (this.b > 4) {
            return (int) (random * 60000.0d);
        }
        if (this.b > 1) {
            return (int) (random * 10000.0d);
        }
        if (this.f869a == 0) {
            return 0;
        }
        if (System.currentTimeMillis() - this.f869a >= 310000) {
            this.a = 1000;
            this.c = 0;
            return 0;
        } else if (this.a >= d) {
            return this.a;
        } else {
            int i = this.a;
            this.c++;
            if (this.c >= 4) {
                return d;
            }
            double d2 = (double) this.a;
            Double.isNaN(d2);
            this.a = (int) (d2 * 1.5d);
            return i;
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m582a() {
        this.f869a = System.currentTimeMillis();
        this.f870a.a(1);
        this.b = 0;
    }

    public void a(boolean z) {
        if (!this.f870a.a()) {
            b.c("should not reconnect as no client or network.");
        } else if (z) {
            if (!this.f870a.a(1)) {
                this.b++;
            }
            this.f870a.a(1);
            XMPushService xMPushService = this.f870a;
            XMPushService xMPushService2 = this.f870a;
            xMPushService2.getClass();
            xMPushService.a((XMPushService.i) new XMPushService.d());
        } else if (!this.f870a.a(1)) {
            int a2 = a();
            if (!this.f870a.a(1)) {
                this.b++;
            }
            b.a("schedule reconnect in " + a2 + "ms");
            XMPushService xMPushService3 = this.f870a;
            XMPushService xMPushService4 = this.f870a;
            xMPushService4.getClass();
            xMPushService3.a((XMPushService.i) new XMPushService.d(), (long) a2);
            if (this.b == 2 && gy.a().a()) {
                ac.b();
            }
            if (this.b == 3) {
                ac.a();
            }
        }
    }
}
