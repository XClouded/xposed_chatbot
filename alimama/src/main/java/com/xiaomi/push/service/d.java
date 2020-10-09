package com.xiaomi.push.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.ff;
import com.xiaomi.push.gb;
import com.xiaomi.push.gc;
import com.xiaomi.push.gd;
import com.xiaomi.push.gf;
import com.xiaomi.push.l;
import com.xiaomi.push.service.al;
import java.util.Collection;
import java.util.Iterator;

public class d {
    private p a = new p();

    public static String a(al.b bVar) {
        StringBuilder sb;
        String str;
        if (!"9".equals(bVar.g)) {
            sb = new StringBuilder();
            sb.append(bVar.f856a);
            str = ".permission.MIPUSH_RECEIVE";
        } else {
            sb = new StringBuilder();
            sb.append(bVar.f856a);
            str = ".permission.MIMC_RECEIVE";
        }
        sb.append(str);
        return sb.toString();
    }

    private static void a(Context context, Intent intent, al.b bVar) {
        if ("com.xiaomi.xmsf".equals(context.getPackageName())) {
            context.sendBroadcast(intent);
        } else {
            context.sendBroadcast(intent, a(bVar));
        }
    }

    /* access modifiers changed from: package-private */
    public al.b a(ff ffVar) {
        Collection a2 = al.a().a(Integer.toString(ffVar.a()));
        if (a2.isEmpty()) {
            return null;
        }
        Iterator it = a2.iterator();
        if (a2.size() == 1) {
            return (al.b) it.next();
        }
        String g = ffVar.g();
        while (it.hasNext()) {
            al.b bVar = (al.b) it.next();
            if (TextUtils.equals(g, bVar.f859b)) {
                return bVar;
            }
        }
        return null;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:10:0x0034  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.xiaomi.push.service.al.b a(com.xiaomi.push.gd r6) {
        /*
            r5 = this;
            com.xiaomi.push.service.al r0 = com.xiaomi.push.service.al.a()
            java.lang.String r1 = r6.k()
            java.util.Collection r0 = r0.a((java.lang.String) r1)
            boolean r1 = r0.isEmpty()
            r2 = 0
            if (r1 == 0) goto L_0x0014
            return r2
        L_0x0014:
            java.util.Iterator r1 = r0.iterator()
            int r0 = r0.size()
            r3 = 1
            if (r0 != r3) goto L_0x0026
            java.lang.Object r6 = r1.next()
            com.xiaomi.push.service.al$b r6 = (com.xiaomi.push.service.al.b) r6
            return r6
        L_0x0026:
            java.lang.String r0 = r6.m()
            java.lang.String r6 = r6.l()
        L_0x002e:
            boolean r3 = r1.hasNext()
            if (r3 == 0) goto L_0x004b
            java.lang.Object r3 = r1.next()
            com.xiaomi.push.service.al$b r3 = (com.xiaomi.push.service.al.b) r3
            java.lang.String r4 = r3.f859b
            boolean r4 = android.text.TextUtils.equals(r0, r4)
            if (r4 != 0) goto L_0x004a
            java.lang.String r4 = r3.f859b
            boolean r4 = android.text.TextUtils.equals(r6, r4)
            if (r4 == 0) goto L_0x002e
        L_0x004a:
            return r3
        L_0x004b:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.d.a(com.xiaomi.push.gd):com.xiaomi.push.service.al$b");
    }

    @SuppressLint({"WrongConstant"})
    public void a(Context context) {
        Intent intent = new Intent();
        intent.setAction("com.xiaomi.push.service_started");
        if (l.c()) {
            intent.addFlags(16777216);
        }
        context.sendBroadcast(intent);
    }

    public void a(Context context, al.b bVar, int i) {
        if (!"5".equalsIgnoreCase(bVar.g)) {
            Intent intent = new Intent();
            intent.setAction("com.xiaomi.push.channel_closed");
            intent.setPackage(bVar.f856a);
            intent.putExtra(ap.r, bVar.g);
            intent.putExtra("ext_reason", i);
            intent.putExtra(ap.p, bVar.f859b);
            intent.putExtra(ap.C, bVar.i);
            if (bVar.f850a == null || !"9".equals(bVar.g)) {
                a(context, intent, bVar);
                return;
            }
            try {
                bVar.f850a.send(Message.obtain((Handler) null, 17, intent));
            } catch (RemoteException unused) {
                bVar.f850a = null;
                b.a("peer may died: " + bVar.f859b.substring(bVar.f859b.lastIndexOf(64)));
            }
        }
    }

    public void a(Context context, al.b bVar, String str, String str2) {
        if ("5".equalsIgnoreCase(bVar.g)) {
            b.d("mipush kicked by server");
            return;
        }
        Intent intent = new Intent();
        intent.setAction("com.xiaomi.push.kicked");
        intent.setPackage(bVar.f856a);
        intent.putExtra("ext_kick_type", str);
        intent.putExtra("ext_kick_reason", str2);
        intent.putExtra("ext_chid", bVar.g);
        intent.putExtra(ap.p, bVar.f859b);
        intent.putExtra(ap.C, bVar.i);
        a(context, intent, bVar);
    }

    public void a(Context context, al.b bVar, boolean z, int i, String str) {
        if ("5".equalsIgnoreCase(bVar.g)) {
            this.a.a(context, bVar, z, i, str);
            return;
        }
        Intent intent = new Intent();
        intent.setAction("com.xiaomi.push.channel_opened");
        intent.setPackage(bVar.f856a);
        intent.putExtra("ext_succeeded", z);
        if (!z) {
            intent.putExtra("ext_reason", i);
        }
        if (!TextUtils.isEmpty(str)) {
            intent.putExtra("ext_reason_msg", str);
        }
        intent.putExtra("ext_chid", bVar.g);
        intent.putExtra(ap.p, bVar.f859b);
        intent.putExtra(ap.C, bVar.i);
        a(context, intent, bVar);
    }

    public void a(XMPushService xMPushService, String str, ff ffVar) {
        al.b a2 = a(ffVar);
        if (a2 == null) {
            b.d("error while notify channel closed! channel " + str + " not registered");
        } else if ("5".equalsIgnoreCase(str)) {
            this.a.a(xMPushService, ffVar, a2);
        } else {
            String str2 = a2.f856a;
            Intent intent = new Intent();
            intent.setAction("com.xiaomi.push.new_msg");
            intent.setPackage(str2);
            intent.putExtra("ext_chid", str);
            intent.putExtra("ext_raw_packet", ffVar.a(a2.h));
            intent.putExtra(ap.C, a2.i);
            intent.putExtra(ap.v, a2.h);
            if (a2.f850a != null) {
                try {
                    a2.f850a.send(Message.obtain((Handler) null, 17, intent));
                    return;
                } catch (RemoteException unused) {
                    a2.f850a = null;
                    b.a("peer may died: " + a2.f859b.substring(a2.f859b.lastIndexOf(64)));
                }
            }
            if (!"com.xiaomi.xmsf".equals(str2)) {
                a((Context) xMPushService, intent, a2);
            }
        }
    }

    public void a(XMPushService xMPushService, String str, gd gdVar) {
        String str2;
        String str3;
        al.b a2 = a(gdVar);
        if (a2 == null) {
            str3 = "error while notify channel closed! channel " + str + " not registered";
        } else if ("5".equalsIgnoreCase(str)) {
            this.a.a(xMPushService, gdVar, a2);
            return;
        } else {
            String str4 = a2.f856a;
            if (gdVar instanceof gc) {
                str2 = "com.xiaomi.push.new_msg";
            } else if (gdVar instanceof gb) {
                str2 = "com.xiaomi.push.new_iq";
            } else if (gdVar instanceof gf) {
                str2 = "com.xiaomi.push.new_pres";
            } else {
                str3 = "unknown packet type, drop it";
            }
            Intent intent = new Intent();
            intent.setAction(str2);
            intent.setPackage(str4);
            intent.putExtra("ext_chid", str);
            intent.putExtra("ext_packet", gdVar.a());
            intent.putExtra(ap.C, a2.i);
            intent.putExtra(ap.v, a2.h);
            a((Context) xMPushService, intent, a2);
            return;
        }
        b.d(str3);
    }
}
