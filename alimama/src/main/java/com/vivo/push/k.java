package com.vivo.push;

/* compiled from: LocalAliasTagsManager */
final class k implements Runnable {
    final /* synthetic */ j a;

    k(j jVar) {
        this.a = jVar;
    }

    public final void run() {
        this.a.b.onTransmissionMessage(this.a.c.mContext, this.a.a);
    }
}
