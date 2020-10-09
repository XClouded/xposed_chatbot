package com.alipay.sdk.widget;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

class d extends Handler {
    final /* synthetic */ a a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    d(a aVar, Looper looper) {
        super(looper);
        this.a = aVar;
    }

    public void dispatchMessage(Message message) {
        this.a.c();
    }
}
