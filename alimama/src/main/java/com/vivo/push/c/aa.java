package com.vivo.push.c;

import com.vivo.push.b.t;

/* compiled from: OnPublishReceiveTask */
final class aa implements Runnable {
    final /* synthetic */ t a;
    final /* synthetic */ z b;

    aa(z zVar, t tVar) {
        this.b = zVar;
        this.a = tVar;
    }

    public final void run() {
        this.b.b.onPublish(this.b.a, this.a.h(), this.a.g());
    }
}
