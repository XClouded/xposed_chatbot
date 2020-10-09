package com.xiaomi.push.service.receivers;

import android.content.Context;

class a implements Runnable {
    final /* synthetic */ Context a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ NetworkStatusReceiver f925a;

    a(NetworkStatusReceiver networkStatusReceiver, Context context) {
        this.f925a = networkStatusReceiver;
        this.a = context;
    }

    public void run() {
        this.f925a.a(this.a);
    }
}
