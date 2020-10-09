package com.xiaomi.push.service;

import android.content.Context;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.fx;
import com.xiaomi.push.ic;
import com.xiaomi.push.service.XMPushService;

final class r extends XMPushService.i {
    final /* synthetic */ ic a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ XMPushService f920a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    r(int i, XMPushService xMPushService, ic icVar) {
        super(i);
        this.f920a = xMPushService;
        this.a = icVar;
    }

    public String a() {
        return "send ack message for message.";
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m613a() {
        try {
            w.a(this.f920a, p.a((Context) this.f920a, this.a));
        } catch (fx e) {
            b.a((Throwable) e);
            this.f920a.a(10, (Exception) e);
        }
    }
}
