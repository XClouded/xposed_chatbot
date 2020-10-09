package com.xiaomi.mipush.sdk;

import com.xiaomi.push.hl;
import com.xiaomi.push.service.ag;

class h extends ag.a {
    final /* synthetic */ g a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    h(g gVar, int i, String str) {
        super(i, str);
        this.a = gVar;
    }

    /* access modifiers changed from: protected */
    public void a() {
        boolean a2 = ag.a(g.a(this.a)).a(hl.AggregatePushSwitch.a(), true);
        if (g.a(this.a) != a2) {
            boolean unused = this.a.f67a = a2;
            j.b(g.a(this.a));
        }
    }
}
