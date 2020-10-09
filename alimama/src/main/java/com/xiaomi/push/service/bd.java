package com.xiaomi.push.service;

import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.service.bc;

class bd implements Runnable {
    final /* synthetic */ bc a;

    bd(bc bcVar) {
        this.a = bcVar;
    }

    public void run() {
        try {
            for (bc.a run : bc.a(this.a).values()) {
                run.run();
            }
        } catch (Exception e) {
            b.a("Sync job exception :" + e.getMessage());
        }
        boolean unused = this.a.f889a = false;
    }
}
