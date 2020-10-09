package com.xiaomi.clientreport.manager;

import com.xiaomi.push.bb;

class e implements Runnable {
    final /* synthetic */ a a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ bb f11a;

    e(a aVar, bb bbVar) {
        this.a = aVar;
        this.f11a = bbVar;
    }

    public void run() {
        this.f11a.run();
    }
}
