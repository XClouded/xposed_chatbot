package com.xiaomi.push.service;

import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.push.service.al;

final class y implements al.b.a {
    final /* synthetic */ XMPushService a;

    y(XMPushService xMPushService) {
        this.a = xMPushService;
    }

    public void a(al.c cVar, al.c cVar2, int i) {
        if (cVar2 == al.c.binded) {
            o.a(this.a);
            o.b(this.a);
        } else if (cVar2 == al.c.unbind) {
            o.a(this.a, ErrorCode.ERROR_SERVICE_UNAVAILABLE, " the push is not connected.");
        }
    }
}
