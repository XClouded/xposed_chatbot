package com.xiaomi.mipush.sdk;

import android.content.Context;
import com.xiaomi.push.Cif;
import com.xiaomi.push.ai;
import com.xiaomi.push.hg;
import com.xiaomi.push.hm;
import com.xiaomi.push.hq;
import com.xiaomi.push.ht;
import com.xiaomi.push.hy;
import com.xiaomi.push.iq;
import com.xiaomi.push.service.ag;
import com.xiaomi.push.service.ah;

public class an extends ai.a {
    private Context a;

    public an(Context context) {
        this.a = context;
    }

    public int a() {
        return 2;
    }

    public void run() {
        ag a2 = ag.a(this.a);
        hy hyVar = new hy();
        hyVar.a(ah.a(a2, hm.MISC_CONFIG));
        hyVar.b(ah.a(a2, hm.PLUGIN_CONFIG));
        Cif ifVar = new Cif("-1", false);
        ifVar.c(hq.DailyCheckClientConfig.f485a);
        ifVar.a(iq.a(hyVar));
        ay.a(this.a).a(ifVar, hg.Notification, (ht) null);
    }
}
