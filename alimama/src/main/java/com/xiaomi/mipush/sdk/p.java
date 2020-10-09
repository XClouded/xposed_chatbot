package com.xiaomi.mipush.sdk;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import com.alimamaunion.common.listpage.CommonItemInfo;
import com.coloros.mcssdk.mode.Message;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.Cif;
import com.xiaomi.push.ai;
import com.xiaomi.push.ek;
import com.xiaomi.push.em;
import com.xiaomi.push.eo;
import com.xiaomi.push.hl;
import com.xiaomi.push.hq;
import com.xiaomi.push.iq;
import com.xiaomi.push.ir;
import com.xiaomi.push.l;
import com.xiaomi.push.service.ag;
import com.xiaomi.push.service.aj;
import java.util.HashMap;
import java.util.Map;

public class p {
    public static void a(Context context, Intent intent, Uri uri) {
        ek a;
        em emVar;
        if (context != null) {
            ay.a(context).a();
            if (ek.a(context.getApplicationContext()).a() == null) {
                ek.a(context.getApplicationContext()).a(d.a(context.getApplicationContext()).a(), context.getPackageName(), ag.a(context.getApplicationContext()).a(hl.AwakeInfoUploadWaySwitch.a(), 0), (eo) new e());
                ag.a(context).a((ag.a) new r(102, "awake online config", context));
            }
            if ((context instanceof Activity) && intent != null) {
                a = ek.a(context.getApplicationContext());
                emVar = em.ACTIVITY;
            } else if (!(context instanceof Service) || intent == null) {
                if (uri != null && !TextUtils.isEmpty(uri.toString())) {
                    ek.a(context.getApplicationContext()).a(em.PROVIDER, context, (Intent) null, uri.toString());
                    return;
                }
                return;
            } else if ("com.xiaomi.mipush.sdk.WAKEUP".equals(intent.getAction())) {
                a = ek.a(context.getApplicationContext());
                emVar = em.SERVICE_COMPONENT;
            } else {
                a = ek.a(context.getApplicationContext());
                emVar = em.SERVICE_ACTION;
            }
            a.a(emVar, context, intent, (String) null);
        }
    }

    private static void a(Context context, Cif ifVar) {
        boolean a = ag.a(context).a(hl.AwakeAppPingSwitch.a(), false);
        int a2 = ag.a(context).a(hl.AwakeAppPingFrequency.a(), 0);
        if (a2 >= 0 && a2 < 30) {
            b.c("aw_ping: frquency need > 30s.");
            a2 = 30;
        }
        if (a2 < 0) {
            a = false;
        }
        if (!l.a()) {
            a(context, ifVar, a, a2);
        } else if (a) {
            ai.a(context.getApplicationContext()).a((ai.a) new q(ifVar, context), a2);
        }
    }

    public static final <T extends ir<T, ?>> void a(Context context, T t, boolean z, int i) {
        byte[] a = iq.a(t);
        if (a == null) {
            b.a("send message fail, because msgBytes is null.");
            return;
        }
        Intent intent = new Intent();
        intent.setAction("action_help_ping");
        intent.putExtra("extra_help_ping_switch", z);
        intent.putExtra("extra_help_ping_frequency", i);
        intent.putExtra("mipush_payload", a);
        intent.putExtra("com.xiaomi.mipush.MESSAGE_CACHE", true);
        ay.a(context).a(intent);
    }

    public static void a(Context context, String str) {
        b.a("aw_ping : send aw_ping cmd and content to push service from 3rd app");
        HashMap hashMap = new HashMap();
        hashMap.put("awake_info", str);
        hashMap.put("event_type", String.valueOf(CommonItemInfo.FOOT_TYPE));
        hashMap.put(Message.DESCRIPTION, "ping message");
        Cif ifVar = new Cif();
        ifVar.b(d.a(context).a());
        ifVar.d(context.getPackageName());
        ifVar.c(hq.AwakeAppResponse.f485a);
        ifVar.a(aj.a());
        ifVar.f625a = hashMap;
        a(context, ifVar);
    }

    public static void a(Context context, String str, int i, String str2) {
        Cif ifVar = new Cif();
        ifVar.b(str);
        ifVar.a((Map<String, String>) new HashMap());
        ifVar.a().put("extra_aw_app_online_cmd", String.valueOf(i));
        ifVar.a().put("extra_help_aw_info", str2);
        ifVar.a(aj.a());
        byte[] a = iq.a(ifVar);
        if (a == null) {
            b.a("send message fail, because msgBytes is null.");
            return;
        }
        Intent intent = new Intent();
        intent.setAction("action_aw_app_logic");
        intent.putExtra("mipush_payload", a);
        ay.a(context).a(intent);
    }
}
