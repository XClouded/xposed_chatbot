package com.vivo.push.c;

import com.vivo.push.model.UPSNotificationMessage;

/* compiled from: OnNotificationClickTask */
final class u implements Runnable {
    final /* synthetic */ UPSNotificationMessage a;
    final /* synthetic */ t b;

    u(t tVar, UPSNotificationMessage uPSNotificationMessage) {
        this.b = tVar;
        this.a = uPSNotificationMessage;
    }

    public final void run() {
        this.b.b.onNotificationMessageClicked(this.b.a, this.a);
    }
}
