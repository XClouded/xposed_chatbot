package com.alipay.sdk.app;

import android.app.Activity;

final class f implements Runnable {
    final /* synthetic */ Activity a;

    f(Activity activity) {
        this.a = activity;
    }

    public void run() {
        this.a.finish();
    }
}
