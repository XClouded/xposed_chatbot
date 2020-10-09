package com.alipay.sdk.widget;

import android.view.View;

class r implements Runnable {
    final /* synthetic */ View a;
    final /* synthetic */ q b;

    r(q qVar, View view) {
        this.b = qVar;
        this.a = view;
    }

    public void run() {
        this.a.setEnabled(true);
    }
}
