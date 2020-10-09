package com.xiaomi.push.service;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.fx;
import com.xiaomi.push.ic;
import com.xiaomi.push.service.XMPushService;

final class q extends XMPushService.i {
    final /* synthetic */ ic a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ XMPushService f919a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    q(int i, XMPushService xMPushService, ic icVar) {
        super(i);
        this.f919a = xMPushService;
        this.a = icVar;
    }

    public String a() {
        return "send app absent message.";
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m612a() {
        try {
            w.a(this.f919a, w.a(this.a.b(), this.a.a()));
        } catch (fx e) {
            b.a((Throwable) e);
            this.f919a.a(10, (Exception) e);
        }
    }
}
