package com.xiaomi.push.service;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.fx;
import com.xiaomi.push.service.XMPushService;

class bs extends XMPushService.i {
    final /* synthetic */ XMPushService a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ String f897a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ byte[] f898a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    bs(XMPushService xMPushService, int i, String str, byte[] bArr) {
        super(i);
        this.a = xMPushService;
        this.f897a = str;
        this.f898a = bArr;
    }

    public String a() {
        return "send mi push message";
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m593a() {
        try {
            w.a(this.a, this.f897a, this.f898a);
        } catch (fx e) {
            b.a((Throwable) e);
            this.a.a(10, (Exception) e);
        }
    }
}
