package com.vivo.push;

import com.vivo.push.p;

/* compiled from: PushClientManager */
final class q implements IPushActionListener {
    final /* synthetic */ p.a a;
    final /* synthetic */ p b;

    q(p pVar, p.a aVar) {
        this.b = pVar;
        this.a = aVar;
    }

    public final void onStateChanged(int i) {
        if (i == 0) {
            Object[] b2 = this.a.b();
            if (b2 == null || b2.length == 0) {
                com.vivo.push.util.p.a("PushClientManager", "bind app result is null");
            } else {
                this.b.a((String) this.a.b()[0]);
            }
        } else {
            String unused = this.b.l = null;
            this.b.k.c("APP_TOKEN");
        }
    }
}
