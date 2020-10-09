package com.xiaomi.push.service;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.al;

class an extends XMPushService.i {
    final /* synthetic */ al.b.c a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    an(al.b.c cVar, int i) {
        super(i);
        this.a = cVar;
    }

    public String a() {
        return "clear peer job";
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m577a() {
        if (this.a.a == this.a.f863a.f850a) {
            b.b("clean peer, chid = " + this.a.f863a.g);
            this.a.f863a.f850a = null;
        }
    }
}
