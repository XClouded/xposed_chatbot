package com.alipay.sdk.auth;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import com.alipay.sdk.packet.b;
import com.alipay.sdk.packet.impl.a;
import com.alipay.sdk.util.c;
import java.util.List;

final class h implements Runnable {
    final /* synthetic */ Activity a;
    final /* synthetic */ StringBuilder b;
    final /* synthetic */ APAuthInfo c;

    h(Activity activity, StringBuilder sb, APAuthInfo aPAuthInfo) {
        this.a = activity;
        this.b = sb;
        this.c = aPAuthInfo;
    }

    public void run() {
        b bVar;
        try {
            try {
                bVar = new a().a((Context) this.a, this.b.toString());
            } catch (Throwable th) {
                c.a("msp", th);
                bVar = null;
            }
            if (g.c != null) {
                g.c.c();
                com.alipay.sdk.widget.a unused = g.c = null;
            }
            if (bVar == null) {
                String unused2 = g.d = this.c.getRedirectUri() + "?resultCode=202";
                g.a(this.a, g.d);
                if (g.c != null) {
                    g.c.c();
                    return;
                }
                return;
            }
            List<com.alipay.sdk.protocol.b> a2 = com.alipay.sdk.protocol.b.a(bVar.c().optJSONObject(com.alipay.sdk.cons.c.c).optJSONObject(com.alipay.sdk.cons.c.d));
            int i = 0;
            while (true) {
                if (i >= a2.size()) {
                    break;
                } else if (a2.get(i).b() == com.alipay.sdk.protocol.a.WapPay) {
                    String unused3 = g.d = a2.get(i).c()[0];
                    break;
                } else {
                    i++;
                }
            }
            if (TextUtils.isEmpty(g.d)) {
                String unused4 = g.d = this.c.getRedirectUri() + "?resultCode=202";
                g.a(this.a, g.d);
                if (g.c != null) {
                    g.c.c();
                    return;
                }
                return;
            }
            Intent intent = new Intent(this.a, AuthActivity.class);
            intent.putExtra("params", g.d);
            intent.putExtra("redirectUri", this.c.getRedirectUri());
            this.a.startActivity(intent);
            if (g.c == null) {
                return;
            }
            g.c.c();
        } catch (Exception unused5) {
            if (g.c == null) {
            }
        } catch (Throwable th2) {
            if (g.c != null) {
                g.c.c();
            }
            throw th2;
        }
    }
}
