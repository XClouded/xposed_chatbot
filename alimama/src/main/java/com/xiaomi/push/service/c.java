package com.xiaomi.push.service;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.ff;
import com.xiaomi.push.fx;
import com.xiaomi.push.service.XMPushService;

class c extends XMPushService.i {
    private XMPushService a = null;

    /* renamed from: a  reason: collision with other field name */
    private ff[] f899a;

    public c(XMPushService xMPushService, ff[] ffVarArr) {
        super(4);
        this.a = xMPushService;
        this.f899a = ffVarArr;
    }

    public String a() {
        return "batch send message.";
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m594a() {
        try {
            if (this.f899a != null) {
                this.a.a(this.f899a);
            }
        } catch (fx e) {
            b.a((Throwable) e);
            this.a.a(10, (Exception) e);
        }
    }
}
