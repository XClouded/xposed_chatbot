package com.vivo.push.c;

import com.vivo.push.b.o;

/* compiled from: OnListTagReceiveTask */
final class m implements Runnable {
    final /* synthetic */ o a;
    final /* synthetic */ l b;

    m(l lVar, o oVar) {
        this.b = lVar;
        this.a = oVar;
    }

    public final void run() {
        this.b.b.onListTags(this.b.a, this.a.h(), this.a.d(), this.a.g());
    }
}
