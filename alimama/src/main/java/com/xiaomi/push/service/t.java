package com.xiaomi.push.service;

import android.content.Context;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.fx;
import com.xiaomi.push.ic;
import com.xiaomi.push.service.XMPushService;

final class t extends XMPushService.i {
    final /* synthetic */ ic a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ XMPushService f927a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    t(int i, XMPushService xMPushService, ic icVar) {
        super(i);
        this.f927a = xMPushService;
        this.a = icVar;
    }

    public String a() {
        return "send ack message for unrecognized new miui message.";
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m615a() {
        try {
            ic a2 = p.a((Context) this.f927a, this.a);
            a2.a().a("miui_message_unrecognized", "1");
            w.a(this.f927a, a2);
        } catch (fx e) {
            b.a((Throwable) e);
            this.f927a.a(10, (Exception) e);
        }
    }
}
