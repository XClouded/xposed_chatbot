package com.huawei.hms.update.a;

import com.huawei.hms.update.a.a.c;

/* compiled from: ThreadWrapper */
class e implements Runnable {
    final /* synthetic */ int a;
    final /* synthetic */ c b;
    final /* synthetic */ d c;

    e(d dVar, int i, c cVar) {
        this.c = dVar;
        this.a = i;
        this.b = cVar;
    }

    public void run() {
        this.c.a.a(this.a, this.b);
    }
}
