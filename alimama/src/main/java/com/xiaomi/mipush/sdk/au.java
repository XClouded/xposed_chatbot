package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.Intent;

final class au implements Runnable {
    final /* synthetic */ Context a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ Intent f35a;

    au(Context context, Intent intent) {
        this.a = context;
        this.f35a = intent;
    }

    public void run() {
        PushMessageHandler.b(this.a, this.f35a);
    }
}
