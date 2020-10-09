package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.Cif;
import com.xiaomi.push.ay;
import com.xiaomi.push.g;
import com.xiaomi.push.hg;
import com.xiaomi.push.hq;
import com.xiaomi.push.ht;
import com.xiaomi.push.i;
import com.xiaomi.push.l;
import com.xiaomi.push.n;
import com.xiaomi.push.service.aj;
import java.util.HashMap;
import java.util.Map;

final class bf implements Runnable {
    final /* synthetic */ Context a;

    /* renamed from: a  reason: collision with other field name */
    final /* synthetic */ boolean f53a;

    bf(Context context, boolean z) {
        this.a = context;
        this.f53a = z;
    }

    public void run() {
        Map<String, String> map;
        String str;
        String b;
        b.a("do sync info");
        Cif ifVar = new Cif(aj.a(), false);
        d a2 = d.a(this.a);
        ifVar.c(hq.SyncInfo.f485a);
        ifVar.b(a2.a());
        ifVar.d(this.a.getPackageName());
        ifVar.f625a = new HashMap();
        n.a(ifVar.f625a, "app_version", g.a(this.a, this.a.getPackageName()));
        n.a(ifVar.f625a, Constants.EXTRA_KEY_APP_VERSION_CODE, Integer.toString(g.a(this.a, this.a.getPackageName())));
        n.a(ifVar.f625a, "push_sdk_vn", "3_6_19");
        n.a(ifVar.f625a, "push_sdk_vc", Integer.toString(30619));
        n.a(ifVar.f625a, "token", a2.b());
        if (!l.d()) {
            String a3 = ay.a(i.f(this.a));
            String h = i.h(this.a);
            if (!TextUtils.isEmpty(h)) {
                a3 = a3 + "," + h;
            }
            if (!TextUtils.isEmpty(a3)) {
                n.a(ifVar.f625a, Constants.EXTRA_KEY_IMEI_MD5, a3);
            }
        }
        n.a(ifVar.f625a, Constants.EXTRA_KEY_REG_ID, a2.c());
        n.a(ifVar.f625a, Constants.EXTRA_KEY_REG_SECRET, a2.d());
        n.a(ifVar.f625a, Constants.EXTRA_KEY_ACCEPT_TIME, MiPushClient.getAcceptTime(this.a).replace(",", "-"));
        if (this.f53a) {
            n.a(ifVar.f625a, Constants.EXTRA_KEY_ALIASES_MD5, be.c(MiPushClient.getAllAlias(this.a)));
            n.a(ifVar.f625a, Constants.EXTRA_KEY_TOPICS_MD5, be.c(MiPushClient.getAllTopic(this.a)));
            map = ifVar.f625a;
            str = Constants.EXTRA_KEY_ACCOUNTS_MD5;
            b = be.c(MiPushClient.getAllUserAccount(this.a));
        } else {
            n.a(ifVar.f625a, Constants.EXTRA_KEY_ALIASES, be.d(MiPushClient.getAllAlias(this.a)));
            n.a(ifVar.f625a, Constants.EXTRA_KEY_TOPICS, be.d(MiPushClient.getAllTopic(this.a)));
            map = ifVar.f625a;
            str = Constants.EXTRA_KEY_ACCOUNTS;
            b = be.d(MiPushClient.getAllUserAccount(this.a));
        }
        n.a(map, str, b);
        ay.a(this.a).a(ifVar, hg.Notification, false, (ht) null);
    }
}
