package com.huawei.hms.update.e;

import com.huawei.hms.update.a.a.b;
import com.huawei.hms.update.a.a.c;

/* compiled from: UpdateWizard */
final class x implements Runnable {
    final /* synthetic */ b a;
    final /* synthetic */ int b;
    final /* synthetic */ c c;

    x(b bVar, int i, c cVar) {
        this.a = bVar;
        this.b = i;
        this.c = cVar;
    }

    public void run() {
        this.a.a(this.b, this.c);
    }
}
