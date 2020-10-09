package com.xiaomi.push.service;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

class bl extends Handler {
    final /* synthetic */ XMPushService a;

    bl(XMPushService xMPushService) {
        this.a = xMPushService;
    }

    public void handleMessage(Message message) {
        super.handleMessage(message);
        if (message != null) {
            try {
                switch (message.what) {
                    case 17:
                        if (message.obj != null) {
                            this.a.onStart((Intent) message.obj, XMPushService.a);
                            return;
                        }
                        return;
                    case 18:
                        Message obtain = Message.obtain((Handler) null, 0);
                        obtain.what = 18;
                        Bundle bundle = new Bundle();
                        bundle.putString("xmsf_region", XMPushService.a(this.a));
                        obtain.setData(bundle);
                        message.replyTo.send(obtain);
                        return;
                    default:
                        return;
                }
            } catch (Throwable unused) {
            }
        }
    }
}
