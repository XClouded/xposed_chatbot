package com.xiaomi.clientreport.manager;

import com.xiaomi.push.ba;

class d implements Runnable {
    final /* synthetic */ a a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ ba f10a;

    d(a aVar, ba baVar) {
        this.a = aVar;
        this.f10a = baVar;
    }

    public void run() {
        this.f10a.run();
    }
}
