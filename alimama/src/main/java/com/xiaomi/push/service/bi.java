package com.xiaomi.push.service;

import com.xiaomi.push.fm;
import com.xiaomi.push.service.XMPushService;

class bi extends XMPushService.i {
    final /* synthetic */ XMPushService a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    bi(XMPushService xMPushService, int i) {
        super(i);
        this.a = xMPushService;
    }

    public String a() {
        return "disconnect for service destroy.";
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m590a() {
        if (XMPushService.a(this.a) != null) {
            XMPushService.a(this.a).b(15, (Exception) null);
            fm unused = this.a.f809a = null;
        }
    }
}
