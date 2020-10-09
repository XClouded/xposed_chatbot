package com.vivo.push.c;

import com.vivo.push.model.UPSNotificationMessage;

/* compiled from: OnNotificationClickTask */
final class x implements Runnable {
    final /* synthetic */ UPSNotificationMessage a;
    final /* synthetic */ t b;

    x(t tVar, UPSNotificationMessage uPSNotificationMessage) {
        this.b = tVar;
        this.a = uPSNotificationMessage;
    }

    public final void run() {
        this.b.b.onNotificationMessageClicked(this.b.a, this.a);
    }
}
