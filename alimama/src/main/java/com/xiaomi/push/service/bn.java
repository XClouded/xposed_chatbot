package com.xiaomi.push.service;

import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.al;

class bn implements al.a {
    final /* synthetic */ XMPushService a;

    bn(XMPushService xMPushService) {
        this.a = xMPushService;
    }

    public void a() {
        XMPushService.a(this.a);
        if (al.a().a() <= 0) {
            this.a.a((XMPushService.i) new XMPushService.f(12, (Exception) null));
        }
    }
}
