package com.huawei.hms.support.api.push.a;

import android.content.Context;
import android.content.Intent;
import com.huawei.hms.support.api.push.a.c.h;
import org.json.JSONArray;

/* compiled from: SelfShowReceiver */
public class a {
    public void a(Context context, Intent intent) {
        int i;
        if (context == null || intent == null) {
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "enter SelfShowReceiver receiver, context or intent is null");
            return;
        }
        try {
            String action = intent.getAction();
            if ("com.huawei.intent.action.PUSH".equals(action) || "com.huawei.push.msg.NOTIFY_MSG".equals(action) || "com.huawei.intent.action.PUSH_DELAY_NOTIFY".equals(action)) {
                String str = null;
                if (intent.hasExtra("selfshow_info")) {
                    if (intent.hasExtra("selfshow_token")) {
                        byte[] byteArrayExtra = intent.getByteArrayExtra("selfshow_info");
                        byte[] byteArrayExtra2 = intent.getByteArrayExtra("selfshow_token");
                        if (byteArrayExtra != null) {
                            if (byteArrayExtra2 != null) {
                                if (intent.hasExtra("selfshow_event_id")) {
                                    str = intent.getStringExtra("selfshow_event_id");
                                }
                                String str2 = str;
                                if (intent.hasExtra("selfshow_notify_id")) {
                                    int intExtra = intent.getIntExtra("selfshow_notify_id", 0);
                                    com.huawei.hms.support.log.a.b("PushSelfShowLog", "get notifyId:" + intExtra);
                                    i = intExtra;
                                } else {
                                    i = 0;
                                }
                                a(context, intent, byteArrayExtra, byteArrayExtra2, str2, i);
                                return;
                            }
                        }
                        com.huawei.hms.support.log.a.b("PushSelfShowLog", "self show info or token is null.");
                        return;
                    }
                }
                com.huawei.hms.support.log.a.b("PushSelfShowLog", "not contain selfshow info or token, invalid message.");
            }
        } catch (RuntimeException unused) {
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "onReceive RuntimeException.");
        } catch (Exception unused2) {
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "onReceive Exception.");
        }
    }

    private void a(Context context, Intent intent, byte[] bArr, byte[] bArr2, String str, int i) {
        com.huawei.hms.support.api.push.a.b.a aVar = new com.huawei.hms.support.api.push.a.b.a(bArr, bArr2);
        if (!aVar.b()) {
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "parseMessage failed");
            return;
        }
        com.huawei.hms.support.log.a.b("PushSelfShowLog", " onReceive the msg id = " + aVar.a() + ",and cmd is" + aVar.k() + ",and the eventId is " + str);
        if (str == null) {
            a(context, intent, aVar);
        } else {
            a(context, intent, str, aVar, i);
        }
    }

    private void a(Context context, Intent intent, com.huawei.hms.support.api.push.a.b.a aVar) {
        com.huawei.hms.support.log.a.b("PushSelfShowLog", "receive a selfshow message ,the type is" + aVar.k());
        if (com.huawei.hms.support.api.push.a.a.a.a(aVar.k())) {
            long a = com.huawei.hms.support.api.push.a.d.a.a(aVar.h());
            if (a == 0) {
                new h(context, aVar, intent.getStringExtra("extra_encrypt_data")).start();
                return;
            }
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "waiting ……");
            intent.setPackage(context.getPackageName());
            com.huawei.hms.support.api.push.a.d.a.a(context, intent, a);
        }
    }

    private void a(Context context, Intent intent, String str, com.huawei.hms.support.api.push.a.b.a aVar, int i) {
        com.huawei.hms.support.log.a.a("PushSelfShowLog", "receive a selfshow userhandle message");
        if (!"-1".equals(str)) {
            com.huawei.hms.support.api.push.a.d.a.b(context, intent);
        } else {
            com.huawei.hms.support.api.push.a.d.a.a(context, i);
        }
        if ("1".equals(str)) {
            new com.huawei.hms.support.api.push.a.a.a(context, aVar).a();
            if (aVar.j() != null) {
                try {
                    JSONArray jSONArray = new JSONArray(aVar.j());
                    Intent intent2 = new Intent("com.huawei.android.push.intent.CLICK");
                    intent2.putExtra("click", jSONArray.toString()).setPackage(aVar.i()).setFlags(32);
                    context.sendBroadcast(intent2);
                } catch (Exception e) {
                    com.huawei.hms.support.log.a.d("PushSelfShowLog", "message.extras is not a json format,err info " + e.toString());
                }
            }
        }
    }
}
