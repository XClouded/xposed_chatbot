package com.vivo.push;

import com.vivo.push.p;

/* compiled from: PushClientManager */
final class u implements Runnable {
    final /* synthetic */ String a;
    final /* synthetic */ p b;

    u(p pVar, String str) {
        this.b = pVar;
        this.a = str;
    }

    public final void run() {
        p.a c = this.b.c(this.a);
        if (c != null) {
            c.a(1003, new Object[0]);
        }
    }
}
