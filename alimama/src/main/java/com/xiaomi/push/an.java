package com.xiaomi.push;

import com.xiaomi.push.al;

class an implements Runnable {
    final /* synthetic */ al.b a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ al f121a;

    an(al alVar, al.b bVar) {
        this.f121a = alVar;
        this.a = bVar;
    }

    public void run() {
        this.f121a.a(this.a);
    }
}
