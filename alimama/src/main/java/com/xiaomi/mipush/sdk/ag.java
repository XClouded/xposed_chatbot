package com.xiaomi.mipush.sdk;

import android.text.TextUtils;
import com.xiaomi.push.Cif;
import com.xiaomi.push.ay;
import com.xiaomi.push.hg;
import com.xiaomi.push.ht;
import com.xiaomi.push.i;
import com.xiaomi.push.l;
import com.xiaomi.push.service.aj;
import java.util.HashMap;
import java.util.Map;

final class ag implements Runnable {
    ag() {
    }

    public void run() {
        if (!l.d() && i.f(MiPushClient.sContext) != null) {
            Cif ifVar = new Cif();
            ifVar.b(d.a(MiPushClient.sContext).a());
            ifVar.c("client_info_update");
            ifVar.a(aj.a());
            ifVar.a((Map<String, String>) new HashMap());
            String str = "";
            String f = i.f(MiPushClient.sContext);
            if (!TextUtils.isEmpty(f)) {
                str = str + ay.a(f);
            }
            String h = i.h(MiPushClient.sContext);
            if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(h)) {
                str = str + "," + h;
            }
            if (!TextUtils.isEmpty(str)) {
                ifVar.a().put(Constants.EXTRA_KEY_IMEI_MD5, str);
            }
            int a = i.a();
            if (a >= 0) {
                ifVar.a().put("space_id", Integer.toString(a));
            }
            ay.a(MiPushClient.sContext).a(ifVar, hg.Notification, false, (ht) null);
        }
    }
}
