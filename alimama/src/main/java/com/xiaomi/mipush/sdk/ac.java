package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.Intent;
import com.xiaomi.channel.commonutils.logger.b;

final class ac implements Runnable {
    final /* synthetic */ Context a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ Intent f29a;

    ac(Context context, Intent intent) {
        this.a = context;
        this.f29a = intent;
    }

    public void run() {
        try {
            this.a.startService(this.f29a);
        } catch (Exception e) {
            b.a(e.getMessage());
        }
    }
}
