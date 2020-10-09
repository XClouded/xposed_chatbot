package com.xiaomi.push;

import com.xiaomi.push.ai;
import com.xiaomi.push.bw;
import java.util.ArrayList;

class bx extends ai.a {
    final /* synthetic */ bw a;

    bx(bw bwVar) {
        this.a = bwVar;
    }

    public int a() {
        return 100957;
    }

    public void run() {
        synchronized (this.a.f157a) {
            if (this.a.f157a.size() > 0) {
                if (this.a.f157a.size() > 1) {
                    this.a.a((ArrayList<bw.a>) this.a.f157a);
                } else {
                    this.a.b((bw.a) this.a.f157a.get(0));
                }
                this.a.f157a.clear();
                System.gc();
            }
        }
    }
}
