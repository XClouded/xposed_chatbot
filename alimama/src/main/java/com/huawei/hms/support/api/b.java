package com.huawei.hms.support.api;

import com.huawei.hms.support.api.client.ResultCallback;

/* compiled from: ErrorResultImpl */
class b implements Runnable {
    final /* synthetic */ ResultCallback a;
    final /* synthetic */ a b;

    b(a aVar, ResultCallback resultCallback) {
        this.b = aVar;
        this.a = resultCallback;
    }

    public void run() {
        this.a.onResult(this.b.a(this.b.b));
    }
}
