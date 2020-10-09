package com.xiaomi.push.service;

import android.app.NotificationManager;
import com.xiaomi.push.ai;

final class aa extends ai.a {
    final /* synthetic */ int a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ NotificationManager f835a;

    aa(int i, NotificationManager notificationManager) {
        this.a = i;
        this.f835a = notificationManager;
    }

    public int a() {
        return this.a;
    }

    public void run() {
        this.f835a.cancel(this.a);
    }
}
