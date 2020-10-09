package com.xiaomi.push.service;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.Cif;
import com.xiaomi.push.hg;
import com.xiaomi.push.hq;
import com.xiaomi.push.i;
import com.xiaomi.push.iq;
import com.xiaomi.push.service.bc;
import java.util.HashMap;
import java.util.Map;

final class x extends bc.a {
    final /* synthetic */ XMPushService a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ k f932a;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    x(String str, long j, XMPushService xMPushService, k kVar) {
        super(str, j);
        this.a = xMPushService;
        this.f932a = kVar;
    }

    /* access modifiers changed from: package-private */
    public void a(bc bcVar) {
        String a2 = bcVar.a("GAID", "gaid");
        String b = i.b((Context) this.a);
        b.c("gaid :" + b);
        if (!TextUtils.isEmpty(b) && !TextUtils.equals(a2, b)) {
            bcVar.a("GAID", "gaid", b);
            Cif ifVar = new Cif();
            ifVar.b(this.f932a.d);
            ifVar.c(hq.ClientInfoUpdate.f485a);
            ifVar.a(aj.a());
            ifVar.a((Map<String, String>) new HashMap());
            ifVar.a().put("gaid", b);
            this.a.a(this.a.getPackageName(), iq.a(w.a(this.a.getPackageName(), this.f932a.d, ifVar, hg.Notification)), true);
        }
    }
}
