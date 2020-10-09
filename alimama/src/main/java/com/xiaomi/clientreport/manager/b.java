package com.xiaomi.clientreport.manager;

import com.xiaomi.push.bd;

class b implements Runnable {
    final /* synthetic */ a a;

    b(a aVar) {
        this.a = aVar;
    }

    public void run() {
        a.a(this.a).execute(new bd(a.a(this.a), a.a(this.a)));
    }
}
