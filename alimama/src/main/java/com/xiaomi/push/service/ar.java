package com.xiaomi.push.service;

import android.content.Context;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.Cif;
import com.xiaomi.push.ef;
import com.xiaomi.push.ek;
import com.xiaomi.push.eo;
import com.xiaomi.push.he;
import com.xiaomi.push.hg;
import com.xiaomi.push.hq;
import com.xiaomi.push.iq;
import java.util.HashMap;

public class ar implements eo {
    public void a(Context context, HashMap<String, String> hashMap) {
        Cif ifVar = new Cif();
        ifVar.b(ek.a(context).a());
        ifVar.d(ek.a(context).b());
        ifVar.c(hq.AwakeAppResponse.f485a);
        ifVar.a(aj.a());
        ifVar.f625a = hashMap;
        byte[] a = iq.a(w.a(ifVar.c(), ifVar.b(), ifVar, hg.Notification));
        if (context instanceof XMPushService) {
            b.a("MoleInfo : send data directly in pushLayer " + ifVar.a());
            ((XMPushService) context).a(context.getPackageName(), a, true);
            return;
        }
        b.a("MoleInfo : context is not correct in pushLayer " + ifVar.a());
    }

    public void b(Context context, HashMap<String, String> hashMap) {
        he a = he.a(context);
        if (a != null) {
            a.a("category_awake_app", "wake_up_app", 1, ef.a(hashMap));
        }
    }

    public void c(Context context, HashMap<String, String> hashMap) {
        b.a("MoleInfo：　" + ef.b(hashMap));
    }
}
