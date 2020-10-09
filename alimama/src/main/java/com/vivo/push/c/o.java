package com.vivo.push.c;

import com.vivo.push.b.p;

/* compiled from: OnLogReceiveTask */
final class o implements Runnable {
    final /* synthetic */ p a;
    final /* synthetic */ n b;

    o(n nVar, p pVar) {
        this.b = nVar;
        this.a = pVar;
    }

    public final void run() {
        this.b.b.onLog(this.b.a, this.a.d(), this.a.e(), this.a.f());
    }
}
