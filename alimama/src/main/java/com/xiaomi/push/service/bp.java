package com.xiaomi.push.service;

import com.xiaomi.push.as;
import com.xiaomi.push.service.XMPushService;

class bp extends XMPushService.i {
    final /* synthetic */ XMPushService a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    bp(XMPushService xMPushService, int i) {
        super(i);
        this.a = xMPushService;
    }

    public String a() {
        return "prepare the mi push account.";
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m591a() {
        w.a(this.a);
        if (as.b(this.a)) {
            this.a.a(true);
        }
    }
}
