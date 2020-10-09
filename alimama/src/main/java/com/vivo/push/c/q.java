package com.vivo.push.c;

import com.vivo.push.model.UnvarnishedMessage;

/* compiled from: OnMessageReceiveTask */
final class q implements Runnable {
    final /* synthetic */ UnvarnishedMessage a;
    final /* synthetic */ p b;

    q(p pVar, UnvarnishedMessage unvarnishedMessage) {
        this.b = pVar;
        this.a = unvarnishedMessage;
    }

    public final void run() {
        this.b.b.onTransmissionMessage(this.b.a, this.a);
    }
}
