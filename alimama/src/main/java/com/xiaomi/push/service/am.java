package com.xiaomi.push.service;

import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.al;

class am implements al.b.a {
    final /* synthetic */ al.b a;

    am(al.b bVar) {
        this.a = bVar;
    }

    public void a(al.c cVar, al.c cVar2, int i) {
        if (cVar2 == al.c.binding) {
            al.b.a(this.a).a((XMPushService.i) al.b.a(this.a), 60000);
        } else {
            al.b.a(this.a).b((XMPushService.i) al.b.a(this.a));
        }
    }
}
