package com.xiaomi.push.service;

import android.content.Context;
import android.os.Messenger;
import android.text.TextUtils;
import com.taobao.android.dinamic.DinamicConstant;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.push.Cif;
import com.xiaomi.push.da;
import com.xiaomi.push.ff;
import com.xiaomi.push.fm;
import com.xiaomi.push.fx;
import com.xiaomi.push.gd;
import com.xiaomi.push.hg;
import com.xiaomi.push.hv;
import com.xiaomi.push.ic;
import com.xiaomi.push.iq;
import com.xiaomi.push.ir;
import com.xiaomi.push.iw;
import com.xiaomi.push.service.al;
import com.xiaomi.push.service.bc;
import java.nio.ByteBuffer;

final class w {
    static ff a(XMPushService xMPushService, byte[] bArr) {
        ic icVar = new ic();
        try {
            iq.a(icVar, bArr);
            return a(l.a((Context) xMPushService), (Context) xMPushService, icVar);
        } catch (iw e) {
            b.a((Throwable) e);
            return null;
        }
    }

    static ff a(k kVar, Context context, ic icVar) {
        try {
            ff ffVar = new ff();
            ffVar.a(5);
            ffVar.c(kVar.f912a);
            ffVar.b(a(icVar));
            ffVar.a("SECMSG", "message");
            String str = kVar.f912a;
            icVar.f608a.f534a = str.substring(0, str.indexOf(DinamicConstant.DINAMIC_PREFIX_AT));
            icVar.f608a.f538c = str.substring(str.indexOf("/") + 1);
            ffVar.a(iq.a(icVar), kVar.c);
            ffVar.a(1);
            b.a("try send mi push message. packagename:" + icVar.f613b + " action:" + icVar.f606a);
            return ffVar;
        } catch (NullPointerException e) {
            b.a((Throwable) e);
            return null;
        }
    }

    static ic a(String str, String str2) {
        Cif ifVar = new Cif();
        ifVar.b(str2);
        ifVar.c("package uninstalled");
        ifVar.a(gd.i());
        ifVar.a(false);
        return a(str, str2, ifVar, hg.Notification);
    }

    static <T extends ir<T, ?>> ic a(String str, String str2, T t, hg hgVar) {
        byte[] a = iq.a(t);
        ic icVar = new ic();
        hv hvVar = new hv();
        hvVar.f533a = 5;
        hvVar.f534a = "fakeid";
        icVar.a(hvVar);
        icVar.a(ByteBuffer.wrap(a));
        icVar.a(hgVar);
        icVar.b(true);
        icVar.b(str);
        icVar.a(false);
        icVar.a(str2);
        return icVar;
    }

    private static String a(ic icVar) {
        if (!(icVar.f607a == null || icVar.f607a.f524b == null)) {
            String str = icVar.f607a.f524b.get("ext_traffic_source_pkg");
            if (!TextUtils.isEmpty(str)) {
                return str;
            }
        }
        return icVar.f613b;
    }

    static String a(String str) {
        return str + ".permission.MIPUSH_RECEIVE";
    }

    static void a(XMPushService xMPushService) {
        k a = l.a(xMPushService.getApplicationContext());
        if (a != null) {
            al.b a2 = l.a(xMPushService.getApplicationContext()).a(xMPushService);
            a(xMPushService, a2);
            al.a().a(a2);
            bc.a((Context) xMPushService).a((bc.a) new x("GAID", 172800, xMPushService, a));
        }
    }

    static void a(XMPushService xMPushService, ic icVar) {
        da.a(icVar.b(), xMPushService.getApplicationContext(), icVar, -1);
        fm a = xMPushService.a();
        if (a == null) {
            throw new fx("try send msg while connection is null.");
        } else if (a.a()) {
            ff a2 = a(l.a((Context) xMPushService), (Context) xMPushService, icVar);
            if (a2 != null) {
                a.b(a2);
            }
        } else {
            throw new fx("Don't support XMPP connection.");
        }
    }

    static void a(XMPushService xMPushService, al.b bVar) {
        bVar.a((Messenger) null);
        bVar.a((al.b.a) new y(xMPushService));
    }

    static void a(XMPushService xMPushService, String str, byte[] bArr) {
        da.a(str, xMPushService.getApplicationContext(), bArr);
        fm a = xMPushService.a();
        if (a == null) {
            throw new fx("try send msg while connection is null.");
        } else if (a.a()) {
            ff a2 = a(xMPushService, bArr);
            if (a2 != null) {
                a.b(a2);
            } else {
                o.a(xMPushService, str, bArr, ErrorCode.ERROR_INVALID_PAYLOAD, "not a valid message");
            }
        } else {
            throw new fx("Don't support XMPP connection.");
        }
    }
}
