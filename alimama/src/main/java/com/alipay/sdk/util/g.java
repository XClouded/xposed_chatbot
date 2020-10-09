package com.alipay.sdk.util;

import com.alipay.sdk.app.AlipayResultActivity;
import com.alipay.sdk.app.j;
import java.util.concurrent.CountDownLatch;

class g implements AlipayResultActivity.b {
    final /* synthetic */ CountDownLatch a;
    final /* synthetic */ e b;

    g(e eVar, CountDownLatch countDownLatch) {
        this.b = eVar;
        this.a = countDownLatch;
    }

    public void a(int i, String str, String str2) {
        String unused = this.b.i = j.a(i, str, str2);
        this.a.countDown();
    }
}
