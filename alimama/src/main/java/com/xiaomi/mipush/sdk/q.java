package com.xiaomi.mipush.sdk;

import android.content.Context;
import com.xiaomi.push.Cif;
import com.xiaomi.push.ai;
import com.xiaomi.push.hg;
import com.xiaomi.push.ht;
import com.xiaomi.push.service.aj;

final class q extends ai.a {
    final /* synthetic */ Context a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ Cif f70a;

    q(Cif ifVar, Context context) {
        this.f70a = ifVar;
        this.a = context;
    }

    public int a() {
        return 22;
    }

    public void run() {
        if (this.f70a != null) {
            this.f70a.a(aj.a());
            ay.a(this.a.getApplicationContext()).a(this.f70a, hg.Notification, true, (ht) null, true);
        }
    }
}
