package com.xiaomi.push.service;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.ff;
import com.xiaomi.push.fx;
import com.xiaomi.push.service.XMPushService;

class aw extends XMPushService.i {
    private ff a;

    /* renamed from: a  reason: collision with other field name */
    private XMPushService f871a = null;

    public aw(XMPushService xMPushService, ff ffVar) {
        super(4);
        this.f871a = xMPushService;
        this.a = ffVar;
    }

    public String a() {
        return "send a message.";
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m583a() {
        try {
            if (this.a != null) {
                this.f871a.a(this.a);
            }
        } catch (fx e) {
            b.a((Throwable) e);
            this.f871a.a(10, (Exception) e);
        }
    }
}
