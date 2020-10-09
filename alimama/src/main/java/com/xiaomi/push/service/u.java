package com.xiaomi.push.service;

import android.content.Context;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.fx;
import com.xiaomi.push.ic;
import com.xiaomi.push.service.XMPushService;

final class u extends XMPushService.i {
    final /* synthetic */ ic a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ XMPushService f928a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ String f929a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    u(int i, XMPushService xMPushService, ic icVar, String str) {
        super(i);
        this.f928a = xMPushService;
        this.a = icVar;
        this.f929a = str;
    }

    public String a() {
        return "send app absent ack message for message.";
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m616a() {
        try {
            ic a2 = p.a((Context) this.f928a, this.a);
            a2.a().a("absent_target_package", this.f929a);
            w.a(this.f928a, a2);
        } catch (fx e) {
            b.a((Throwable) e);
            this.f928a.a(10, (Exception) e);
        }
    }
}
