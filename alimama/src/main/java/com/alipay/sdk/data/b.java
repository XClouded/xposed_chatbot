package com.alipay.sdk.data;

import android.content.Context;
import com.alipay.sdk.util.c;

class b implements Runnable {
    final /* synthetic */ Context a;
    final /* synthetic */ a b;

    b(a aVar, Context context) {
        this.b = aVar;
        this.a = context;
    }

    public void run() {
        try {
            com.alipay.sdk.packet.b a2 = new com.alipay.sdk.packet.impl.b().a(this.a);
            if (a2 != null) {
                this.b.b(a2.b());
                this.b.i();
            }
        } catch (Throwable th) {
            c.a(th);
        }
    }
}
