package com.xiaomi.push.service;

import android.content.Context;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.push.fx;
import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.al;
import java.io.IOException;
import java.util.Collection;
import org.json.JSONException;

public class n extends XMPushService.i {
    private XMPushService a;

    /* renamed from: a  reason: collision with other field name */
    private String f916a;

    /* renamed from: a  reason: collision with other field name */
    private byte[] f917a;
    private String b;
    private String c;

    public n(XMPushService xMPushService, String str, String str2, String str3, byte[] bArr) {
        super(9);
        this.a = xMPushService;
        this.f916a = str;
        this.f917a = bArr;
        this.b = str2;
        this.c = str3;
    }

    public String a() {
        return "register app";
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m611a() {
        al.b bVar;
        k a2 = l.a((Context) this.a);
        if (a2 == null) {
            try {
                a2 = l.a(this.a, this.f916a, this.b, this.c);
            } catch (IOException | JSONException e) {
                b.a(e);
            }
        }
        if (a2 == null) {
            b.d("no account for mipush");
            o.a(this.a, ErrorCode.ERROR_AUTHERICATION_ERROR, "no account.");
            return;
        }
        Collection a3 = al.a().a("5");
        if (a3.isEmpty()) {
            bVar = a2.a(this.a);
            w.a(this.a, bVar);
            al.a().a(bVar);
        } else {
            bVar = (al.b) a3.iterator().next();
        }
        if (this.a.c()) {
            try {
                if (bVar.f854a == al.c.binded) {
                    w.a(this.a, this.f916a, this.f917a);
                } else if (bVar.f854a == al.c.unbind) {
                    XMPushService xMPushService = this.a;
                    XMPushService xMPushService2 = this.a;
                    xMPushService2.getClass();
                    xMPushService.a((XMPushService.i) new XMPushService.a(bVar));
                }
            } catch (fx e2) {
                b.a((Throwable) e2);
                this.a.a(10, (Exception) e2);
            }
        } else {
            this.a.a(true);
        }
    }
}
