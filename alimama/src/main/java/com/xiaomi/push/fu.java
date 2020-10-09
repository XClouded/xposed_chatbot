package com.xiaomi.push;

import com.xiaomi.push.service.XMPushService;

class fu extends XMPushService.i {
    final /* synthetic */ long a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ ft f389a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    fu(ft ftVar, int i, long j) {
        super(i);
        this.f389a = ftVar;
        this.a = j;
    }

    public String a() {
        return "check the ping-pong." + this.a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m320a() {
        Thread.yield();
        if (this.f389a.c() && !this.f389a.a(this.a)) {
            this.f389a.b.a(22, (Exception) null);
        }
    }
}
