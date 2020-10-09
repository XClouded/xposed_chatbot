package com.xiaomi.push.service;

import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.al;

class ao extends XMPushService.i {
    final /* synthetic */ al.b.c a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    ao(al.b.c cVar, int i) {
        super(i);
        this.a = cVar;
    }

    public String a() {
        return "check peer job";
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m578a() {
        if (al.a().a(this.a.f863a.g, this.a.f863a.f859b).f850a == null) {
            al.b.a(al.b.this).a(this.a.f863a.g, this.a.f863a.f859b, 2, (String) null, (String) null);
        }
    }
}
