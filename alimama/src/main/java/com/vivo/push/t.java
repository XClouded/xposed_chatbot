package com.vivo.push;

import com.vivo.push.b.b;

/* compiled from: PushClientManager */
final class t implements Runnable {
    final /* synthetic */ b a;
    final /* synthetic */ String b;
    final /* synthetic */ p c;

    t(p pVar, b bVar, String str) {
        this.c = pVar;
        this.a = bVar;
        this.b = str;
    }

    public final void run() {
        this.c.a((y) this.a);
        this.c.d(this.b);
    }
}
