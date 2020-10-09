package com.xiaomi.clientreport.manager;

import com.xiaomi.push.bd;

class c implements Runnable {
    final /* synthetic */ a a;

    c(a aVar) {
        this.a = aVar;
    }

    public void run() {
        a.a(this.a).execute(new bd(a.a(this.a), a.a(this.a)));
    }
}
