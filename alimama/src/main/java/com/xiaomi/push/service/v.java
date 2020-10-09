package com.xiaomi.push.service;

import android.content.Context;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.fx;
import com.xiaomi.push.ic;
import com.xiaomi.push.service.XMPushService;

final class v extends XMPushService.i {
    final /* synthetic */ ic a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ XMPushService f930a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ String f931a;
    final /* synthetic */ String b;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    v(int i, XMPushService xMPushService, ic icVar, String str, String str2) {
        super(i);
        this.f930a = xMPushService;
        this.a = icVar;
        this.f931a = str;
        this.b = str2;
    }

    public String a() {
        return "send wrong message ack for message.";
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m617a() {
        try {
            ic a2 = p.a((Context) this.f930a, this.a);
            a2.f607a.a("error", this.f931a);
            a2.f607a.a("reason", this.b);
            w.a(this.f930a, a2);
        } catch (fx e) {
            b.a((Throwable) e);
            this.f930a.a(10, (Exception) e);
        }
    }
}
