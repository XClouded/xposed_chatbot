package com.xiaomi.push.service;

import android.content.Context;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.fx;
import com.xiaomi.push.ic;
import com.xiaomi.push.service.XMPushService;

final class s extends XMPushService.i {
    final /* synthetic */ ic a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ XMPushService f926a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    s(int i, XMPushService xMPushService, ic icVar) {
        super(i);
        this.f926a = xMPushService;
        this.a = icVar;
    }

    public String a() {
        return "send ack message for obsleted message.";
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m614a() {
        try {
            ic a2 = p.a((Context) this.f926a, this.a);
            a2.a().a("message_obsleted", "1");
            w.a(this.f926a, a2);
        } catch (fx e) {
            b.a((Throwable) e);
            this.f926a.a(10, (Exception) e);
        }
    }
}
