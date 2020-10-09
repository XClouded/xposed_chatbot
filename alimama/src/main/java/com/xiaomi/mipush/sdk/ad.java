package com.xiaomi.mipush.sdk;

import android.content.Context;

final class ad implements Runnable {
    final /* synthetic */ Context a;

    ad(Context context) {
        this.a = context;
    }

    public void run() {
        MessageHandleService.c(this.a);
    }
}
