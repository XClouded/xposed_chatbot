package com.xiaomi.push.service;

import android.database.ContentObserver;
import android.os.Handler;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.XMPushService;

class bo extends ContentObserver {
    final /* synthetic */ XMPushService a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    bo(XMPushService xMPushService, Handler handler) {
        super(handler);
        this.a = xMPushService;
    }

    public void onChange(boolean z) {
        super.onChange(z);
        boolean a2 = XMPushService.a(this.a);
        b.a("ExtremePowerMode:" + a2);
        if (a2) {
            this.a.a((XMPushService.i) new XMPushService.f(23, (Exception) null));
        } else {
            this.a.a(true);
        }
    }
}
