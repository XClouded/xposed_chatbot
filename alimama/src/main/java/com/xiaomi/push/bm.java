package com.xiaomi.push;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.ai;
import java.lang.ref.WeakReference;

class bm extends ai.a {
    final /* synthetic */ bk a;

    bm(bk bkVar) {
        this.a = bkVar;
    }

    public int a() {
        return 10054;
    }

    public void run() {
        b.c("exec== DbSizeControlJob");
        bw.a(bk.a(this.a)).a((Runnable) new bp(bk.a(this.a), new WeakReference(bk.a(this.a))));
        this.a.b("check_time");
    }
}
