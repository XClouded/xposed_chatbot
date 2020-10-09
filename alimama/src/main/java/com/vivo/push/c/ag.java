package com.vivo.push.c;

import com.vivo.push.b.k;

/* compiled from: OnUnBindAppReceiveTask */
final class ag implements Runnable {
    final /* synthetic */ k a;
    final /* synthetic */ af b;

    ag(af afVar, k kVar) {
        this.b = afVar;
        this.a = kVar;
    }

    public final void run() {
        this.b.b.onUnBind(this.b.a, this.a.h(), this.a.d());
    }
}
