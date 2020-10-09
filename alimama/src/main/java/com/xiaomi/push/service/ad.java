package com.xiaomi.push.service;

import com.xiaomi.push.ha;
import java.util.Iterator;
import java.util.List;

final class ad implements Runnable {
    final /* synthetic */ List a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ boolean f839a;

    ad(List list, boolean z) {
        this.a = list;
        this.f839a = z;
    }

    public void run() {
        int i;
        boolean a2 = ac.a("www.baidu.com:80");
        Iterator it = this.a.iterator();
        while (true) {
            i = 1;
            if (!it.hasNext()) {
                break;
            }
            a2 = a2 || ac.a((String) it.next());
            if (a2 && !this.f839a) {
                break;
            }
        }
        if (!a2) {
            i = 2;
        }
        ha.a(i);
    }
}
