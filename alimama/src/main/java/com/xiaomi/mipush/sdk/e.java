package com.xiaomi.mipush.sdk;

import android.content.Context;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.Cif;
import com.xiaomi.push.ef;
import com.xiaomi.push.ek;
import com.xiaomi.push.eo;
import com.xiaomi.push.hg;
import com.xiaomi.push.hq;
import com.xiaomi.push.ht;
import com.xiaomi.push.service.aj;
import java.util.HashMap;

public class e implements eo {
    public void a(Context context, HashMap<String, String> hashMap) {
        Cif ifVar = new Cif();
        ifVar.b(ek.a(context).a());
        ifVar.d(ek.a(context).b());
        ifVar.c(hq.AwakeAppResponse.f485a);
        ifVar.a(aj.a());
        ifVar.f625a = hashMap;
        ay.a(context).a(ifVar, hg.Notification, true, (ht) null, true);
        b.a("MoleInfo：　send data in app layer");
    }

    public void b(Context context, HashMap<String, String> hashMap) {
        MiTinyDataClient.upload("category_awake_app", "wake_up_app", 1, ef.a(hashMap));
        b.a("MoleInfo：　send data in app layer");
    }

    public void c(Context context, HashMap<String, String> hashMap) {
        b.a("MoleInfo：　" + ef.b(hashMap));
        String str = hashMap.get("awake_info");
        if (String.valueOf(1007).equals(hashMap.get("event_type"))) {
            p.a(context, str);
        }
    }
}
