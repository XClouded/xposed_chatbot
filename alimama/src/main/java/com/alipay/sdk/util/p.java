package com.alipay.sdk.util;

import android.app.Activity;

final class p implements Runnable {
    final /* synthetic */ Activity a;

    p(Activity activity) {
        this.a = activity;
    }

    public void run() {
        this.a.finish();
    }
}
