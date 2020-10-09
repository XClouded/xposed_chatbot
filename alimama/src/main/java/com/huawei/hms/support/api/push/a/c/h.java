package com.huawei.hms.support.api.push.a.c;

import android.content.Context;
import android.content.Intent;
import com.huawei.hms.support.api.push.a.b.a;

/* compiled from: PushSelfShowThread */
public class h extends Thread {
    private Context a;
    private a b;
    private String c;

    public h(Context context, a aVar, String str) {
        this.a = context;
        this.b = aVar;
        this.c = str;
    }

    private static Intent b(Context context, a aVar) {
        if (aVar == null) {
            return null;
        }
        Intent b2 = com.huawei.hms.support.api.push.a.d.a.b(context, aVar.s());
        if (aVar.g() != null) {
            try {
                Intent parseUri = Intent.parseUri(aVar.g(), 0);
                com.huawei.hms.support.log.a.a("PushSelfShowLog", "Intent.parseUri(msg.intentUri, 0)，" + parseUri.toURI());
                if (com.huawei.hms.support.api.push.a.d.a.a(context, aVar.s(), parseUri).booleanValue()) {
                    return parseUri;
                }
                return b2;
            } catch (RuntimeException unused) {
                com.huawei.hms.support.log.a.c("PushSelfShowLog", "intentUri error");
                return b2;
            } catch (Exception unused2) {
                com.huawei.hms.support.log.a.c("PushSelfShowLog", "intentUri error");
                return b2;
            }
        } else {
            if (aVar.t() != null) {
                Intent intent = new Intent(aVar.t());
                if (com.huawei.hms.support.api.push.a.d.a.a(context, aVar.s(), intent).booleanValue()) {
                    b2 = intent;
                }
            }
            b2.setPackage(aVar.s());
            return b2;
        }
    }

    public void run() {
        com.huawei.hms.support.log.a.b("PushSelfShowLog", "enter run()");
        try {
            if (a(this.a) && !a(this.a, this.b)) {
                d.a(this.a, this.b, this.c);
            }
        } catch (Exception e) {
            com.huawei.hms.support.log.a.d("PushSelfShowLog", e.toString());
        }
    }

    public boolean a(Context context) {
        if ("cosa".equals(this.b.k())) {
            return b(context);
        }
        if ("email".equals(this.b.k())) {
            return c(context);
        }
        return true;
    }

    public boolean b(Context context) {
        return com.huawei.hms.support.api.push.a.d.a.c(context, this.b.s());
    }

    public boolean c(Context context) {
        return com.huawei.hms.support.api.push.a.d.a.a(context);
    }

    public boolean a(Context context, a aVar) {
        boolean z = false;
        if (!"cosa".equals(aVar.k())) {
            return false;
        }
        Intent b2 = b(context, aVar);
        if (b2 == null) {
            com.huawei.hms.support.log.a.a("PushSelfShowLog", "launchCosaApp,intent == null");
            z = true;
        }
        if (com.huawei.hms.support.api.push.a.d.a.a(context, b2)) {
            return z;
        }
        com.huawei.hms.support.log.a.b("PushSelfShowLog", "no permission to start activity");
        return true;
    }
}
