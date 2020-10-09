package com.xiaomi.push.service;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import com.alipay.literpc.android.phone.mrpc.core.RpcException;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.Cif;
import com.xiaomi.push.da;
import com.xiaomi.push.ev;
import com.xiaomi.push.ff;
import com.xiaomi.push.fx;
import com.xiaomi.push.g;
import com.xiaomi.push.ga;
import com.xiaomi.push.gc;
import com.xiaomi.push.gd;
import com.xiaomi.push.gr;
import com.xiaomi.push.hg;
import com.xiaomi.push.hq;
import com.xiaomi.push.ht;
import com.xiaomi.push.hw;
import com.xiaomi.push.ic;
import com.xiaomi.push.iq;
import com.xiaomi.push.ir;
import com.xiaomi.push.service.XMPushService;
import com.xiaomi.push.service.al;
import com.xiaomi.push.service.z;
import com.xiaomi.push.t;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONException;

public class p {
    public static Intent a(byte[] bArr, long j) {
        ic a = a(bArr);
        if (a == null) {
            return null;
        }
        Intent intent = new Intent("com.xiaomi.mipush.RECEIVE_MESSAGE");
        intent.putExtra("mipush_payload", bArr);
        intent.putExtra("mrt", Long.toString(j));
        intent.setPackage(a.f613b);
        return intent;
    }

    public static ic a(Context context, ic icVar) {
        hw hwVar = new hw();
        hwVar.b(icVar.a());
        ht a = icVar.a();
        if (a != null) {
            hwVar.a(a.a());
            hwVar.a(a.a());
            if (!TextUtils.isEmpty(a.b())) {
                hwVar.c(a.b());
            }
        }
        hwVar.a(iq.a(context, icVar));
        ic a2 = w.a(icVar.b(), icVar.a(), hwVar, hg.AckMessage);
        ht a3 = icVar.a().a();
        a3.a("mat", Long.toString(System.currentTimeMillis()));
        a2.a(a3);
        return a2;
    }

    public static ic a(byte[] bArr) {
        ic icVar = new ic();
        try {
            iq.a(icVar, bArr);
            return icVar;
        } catch (Throwable th) {
            b.a(th);
            return null;
        }
    }

    private static void a(XMPushService xMPushService, ic icVar) {
        xMPushService.a((XMPushService.i) new q(4, xMPushService, icVar));
    }

    private static void a(XMPushService xMPushService, ic icVar, String str) {
        xMPushService.a((XMPushService.i) new u(4, xMPushService, icVar, str));
    }

    private static void a(XMPushService xMPushService, ic icVar, String str, String str2) {
        xMPushService.a((XMPushService.i) new v(4, xMPushService, icVar, str, str2));
    }

    public static void a(XMPushService xMPushService, String str, byte[] bArr, Intent intent) {
        ev a;
        String b;
        String str2;
        String a2;
        int i;
        String str3;
        ev a3;
        String b2;
        String b3;
        String a4;
        String str4;
        boolean z;
        XMPushService xMPushService2 = xMPushService;
        byte[] bArr2 = bArr;
        Intent intent2 = intent;
        ic a5 = a(bArr);
        ht a6 = a5.a();
        String str5 = null;
        if (bArr2 != null) {
            da.a(a5.b(), xMPushService.getApplicationContext(), (ir) null, a5.a(), bArr2.length);
        }
        if (c(a5) && a((Context) xMPushService, str)) {
            if (z.e(a5)) {
                ev.a(xMPushService.getApplicationContext()).a(a5.b(), z.b(a5), a6.a(), "old message received by new SDK.");
            }
            c(xMPushService2, a5);
        } else if (a(a5) && !a((Context) xMPushService, str) && !b(a5)) {
            if (z.e(a5)) {
                ev.a(xMPushService.getApplicationContext()).a(a5.b(), z.b(a5), a6.a(), "new message received by old SDK.");
            }
            d(xMPushService2, a5);
        } else if ((z.a(a5) && g.b((Context) xMPushService2, a5.f613b)) || a((Context) xMPushService2, intent2)) {
            if (hg.Registration == a5.a()) {
                String b4 = a5.b();
                SharedPreferences.Editor edit = xMPushService2.getSharedPreferences("pref_registered_pkg_names", 0).edit();
                edit.putString(b4, a5.f609a);
                edit.commit();
                ev.a(xMPushService.getApplicationContext()).a(b4, "E100003", a6.a(), 6003, "receive a register message");
                if (!TextUtils.isEmpty(a6.a())) {
                    intent2.putExtra("messageId", a6.a());
                    intent2.putExtra("eventMessageType", RpcException.ErrorCode.SERVER_SERVICENOTFOUND);
                }
            }
            if (z.c(a5)) {
                ev.a(xMPushService.getApplicationContext()).a(a5.b(), z.b(a5), a6.a(), 1001, System.currentTimeMillis(), "receive notification message ");
                if (!TextUtils.isEmpty(a6.a())) {
                    intent2.putExtra("messageId", a6.a());
                    intent2.putExtra("eventMessageType", 1000);
                }
            }
            if (z.b(a5)) {
                ev.a(xMPushService.getApplicationContext()).a(a5.b(), z.b(a5), a6.a(), 2001, System.currentTimeMillis(), "receive passThrough message");
                if (!TextUtils.isEmpty(a6.a())) {
                    intent2.putExtra("messageId", a6.a());
                    intent2.putExtra("eventMessageType", 2000);
                }
            }
            if (z.a(a5)) {
                ev.a(xMPushService.getApplicationContext()).a(a5.b(), z.b(a5), a6.a(), 3001, System.currentTimeMillis(), "receive business message");
                if (!TextUtils.isEmpty(a6.a())) {
                    intent2.putExtra("messageId", a6.a());
                    intent2.putExtra("eventMessageType", 3000);
                }
            }
            if (a6 != null && !TextUtils.isEmpty(a6.c()) && !TextUtils.isEmpty(a6.d()) && a6.f522b != 1 && (z.a((Map<String, String>) a6.a()) || !z.a((Context) xMPushService2, a5.f613b))) {
                if (a6 != null) {
                    if (a6.f520a != null) {
                        str5 = a6.f520a.get("jobkey");
                    }
                    if (TextUtils.isEmpty(str5)) {
                        str5 = a6.a();
                    }
                    z = ab.a(xMPushService2, a5.f613b, str5);
                } else {
                    z = false;
                }
                if (z) {
                    ev.a(xMPushService.getApplicationContext()).c(a5.b(), z.b(a5), a6.a(), "drop a duplicate message");
                    b.a("drop a duplicate message, key=" + str5);
                } else {
                    z.c a7 = z.a((Context) xMPushService2, a5, bArr2);
                    if (a7.a > 0 && !TextUtils.isEmpty(a7.f938a)) {
                        gr.a(xMPushService, a7.f938a, a7.a, true, false, System.currentTimeMillis());
                    }
                    if (!z.a(a5)) {
                        Intent intent3 = new Intent("com.xiaomi.mipush.MESSAGE_ARRIVED");
                        intent3.putExtra("mipush_payload", bArr2);
                        intent3.setPackage(a5.f613b);
                        try {
                            List<ResolveInfo> queryBroadcastReceivers = xMPushService.getPackageManager().queryBroadcastReceivers(intent3, 0);
                            if (queryBroadcastReceivers != null && !queryBroadcastReceivers.isEmpty()) {
                                xMPushService2.sendBroadcast(intent3, w.a(a5.f613b));
                            }
                        } catch (Exception e) {
                            xMPushService2.sendBroadcast(intent3, w.a(a5.f613b));
                            ev.a(xMPushService.getApplicationContext()).b(a5.b(), z.b(a5), a6.a(), e.getMessage());
                        }
                    }
                }
                b(xMPushService2, a5);
            } else if ("com.xiaomi.xmsf".contains(a5.f613b) && !a5.b() && a6 != null && a6.a() != null && a6.a().containsKey("ab")) {
                b(xMPushService2, a5);
                b.c("receive abtest message. ack it." + a6.a());
            } else if (a(xMPushService2, str, a5, a6)) {
                if (a6 != null && !TextUtils.isEmpty(a6.a())) {
                    if (z.b(a5)) {
                        a = ev.a(xMPushService.getApplicationContext());
                        b = a5.b();
                        str2 = z.b(a5);
                        a2 = a6.a();
                        i = 2002;
                        str3 = "try send passThrough message Broadcast";
                    } else {
                        if (z.a(a5)) {
                            a3 = ev.a(xMPushService.getApplicationContext());
                            b2 = a5.b();
                            b3 = z.b(a5);
                            a4 = a6.a();
                            str4 = "try show awake message , but it don't show in foreground";
                        } else if (z.c(a5)) {
                            a3 = ev.a(xMPushService.getApplicationContext());
                            b2 = a5.b();
                            b3 = z.b(a5);
                            a4 = a6.a();
                            str4 = "try show notification message , but it don't show in foreground";
                        } else if (z.d(a5)) {
                            a = ev.a(xMPushService.getApplicationContext());
                            b = a5.b();
                            str2 = "E100003";
                            a2 = a6.a();
                            i = 6004;
                            str3 = "try send register broadcast";
                        }
                        a3.a(b2, b3, a4, str4);
                    }
                    a.a(b, str2, a2, i, str3);
                }
                xMPushService2.sendBroadcast(intent2, w.a(a5.f613b));
            } else {
                ev.a(xMPushService.getApplicationContext()).a(a5.b(), z.b(a5), a6.a(), "passThough message: not permit to send broadcast ");
            }
            if (a5.a() == hg.UnRegistration && !"com.xiaomi.xmsf".equals(xMPushService.getPackageName())) {
                xMPushService.stopSelf();
            }
        } else if (!g.b((Context) xMPushService2, a5.f613b)) {
            if (z.e(a5)) {
                ev.a(xMPushService.getApplicationContext()).b(a5.b(), z.b(a5), a6.a(), "receive a message, but the package is removed.");
            }
            a(xMPushService2, a5);
        } else {
            b.a("receive a mipush message, we can see the app, but we can't see the receiver.");
            if (z.e(a5)) {
                ev.a(xMPushService.getApplicationContext()).b(a5.b(), z.b(a5), a6.a(), "receive a mipush message, we can see the app, but we can't see the receiver.");
            }
        }
    }

    private static void a(XMPushService xMPushService, byte[] bArr, long j) {
        Map a;
        ic a2 = a(bArr);
        if (a2 != null) {
            if (TextUtils.isEmpty(a2.f613b)) {
                b.a("receive a mipush message without package name");
                return;
            }
            Long valueOf = Long.valueOf(System.currentTimeMillis());
            Intent a3 = a(bArr, valueOf.longValue());
            String a4 = z.a(a2);
            gr.a(xMPushService, a4, j, true, true, System.currentTimeMillis());
            ht a5 = a2.a();
            if (a5 != null) {
                a5.a("mrt", Long.toString(valueOf.longValue()));
            }
            if (hg.SendMessage == a2.a() && m.a((Context) xMPushService).a(a2.f613b) && !z.a(a2)) {
                String str = "";
                if (a5 != null) {
                    str = a5.a();
                    if (z.e(a2)) {
                        ev.a(xMPushService.getApplicationContext()).a(a2.b(), z.b(a2), str, "Drop a message for unregistered");
                    }
                }
                b.a("Drop a message for unregistered, msgid=" + str);
                a(xMPushService, a2, a2.f613b);
            } else if (hg.SendMessage == a2.a() && m.a((Context) xMPushService).c(a2.f613b) && !z.a(a2)) {
                String str2 = "";
                if (a5 != null) {
                    str2 = a5.a();
                    if (z.e(a2)) {
                        ev.a(xMPushService.getApplicationContext()).a(a2.b(), z.b(a2), str2, "Drop a message for push closed");
                    }
                }
                b.a("Drop a message for push closed, msgid=" + str2);
                a(xMPushService, a2, a2.f613b);
            } else if (hg.SendMessage != a2.a() || TextUtils.equals(xMPushService.getPackageName(), "com.xiaomi.xmsf") || TextUtils.equals(xMPushService.getPackageName(), a2.f613b)) {
                if (!(a5 == null || a5.a() == null)) {
                    b.a(String.format("receive a message, appid=%1$s, msgid= %2$s", new Object[]{a2.a(), a5.a()}));
                }
                if (a5 == null || (a = a5.a()) == null || !a.containsKey("hide") || !"true".equalsIgnoreCase((String) a.get("hide"))) {
                    if (!(a5 == null || a5.a() == null || !a5.a().containsKey("__miid"))) {
                        String str3 = (String) a5.a().get("__miid");
                        String a6 = t.a(xMPushService.getApplicationContext());
                        if (TextUtils.isEmpty(a6) || !TextUtils.equals(str3, a6)) {
                            if (z.e(a2)) {
                                ev.a(xMPushService.getApplicationContext()).a(a2.b(), z.b(a2), a5.a(), "miid already logout or anther already login");
                            }
                            b.a(str3 + " should be login, but got " + a6);
                            a(xMPushService, a2, "miid already logout or anther already login", str3 + " should be login, but got " + a6);
                            return;
                        }
                    }
                    a(xMPushService, a4, bArr, a3);
                    return;
                }
                b(xMPushService, a2);
            } else {
                b.a("Receive a message with wrong package name, expect " + xMPushService.getPackageName() + ", received " + a2.f613b);
                a(xMPushService, a2, "unmatched_package", "package should be " + xMPushService.getPackageName() + ", but got " + a2.f613b);
                if (a5 != null && z.e(a2)) {
                    ev.a(xMPushService.getApplicationContext()).a(a2.b(), z.b(a2), a5.a(), "Receive a message with wrong package name");
                }
            }
        }
    }

    private static boolean a(Context context, Intent intent) {
        try {
            List<ResolveInfo> queryBroadcastReceivers = context.getPackageManager().queryBroadcastReceivers(intent, 32);
            return queryBroadcastReceivers != null && !queryBroadcastReceivers.isEmpty();
        } catch (Exception unused) {
            return true;
        }
    }

    private static boolean a(Context context, String str) {
        Intent intent = new Intent("com.xiaomi.mipush.miui.CLICK_MESSAGE");
        intent.setPackage(str);
        Intent intent2 = new Intent("com.xiaomi.mipush.miui.RECEIVE_MESSAGE");
        intent2.setPackage(str);
        PackageManager packageManager = context.getPackageManager();
        try {
            return !packageManager.queryBroadcastReceivers(intent2, 32).isEmpty() || !packageManager.queryIntentServices(intent, 32).isEmpty();
        } catch (Exception e) {
            b.a((Throwable) e);
            return false;
        }
    }

    private static boolean a(ic icVar) {
        return "com.xiaomi.xmsf".equals(icVar.f613b) && icVar.a() != null && icVar.a().a() != null && icVar.a().a().containsKey("miui_package_name");
    }

    private static boolean a(XMPushService xMPushService, String str, ic icVar, ht htVar) {
        boolean z = true;
        if (htVar != null && htVar.a() != null && htVar.a().containsKey("__check_alive") && htVar.a().containsKey("__awake")) {
            Cif ifVar = new Cif();
            ifVar.b(icVar.a());
            ifVar.d(str);
            ifVar.c(hq.AwakeSystemApp.f485a);
            ifVar.a(htVar.a());
            ifVar.f625a = new HashMap();
            boolean a = g.a(xMPushService.getApplicationContext(), str);
            ifVar.f625a.put("app_running", Boolean.toString(a));
            if (!a) {
                boolean parseBoolean = Boolean.parseBoolean((String) htVar.a().get("__awake"));
                ifVar.f625a.put("awaked", Boolean.toString(parseBoolean));
                if (!parseBoolean) {
                    z = false;
                }
            }
            try {
                w.a(xMPushService, w.a(icVar.b(), icVar.a(), ifVar, hg.Notification));
            } catch (fx e) {
                b.a((Throwable) e);
            }
        }
        return z;
    }

    private static void b(XMPushService xMPushService, ic icVar) {
        xMPushService.a((XMPushService.i) new r(4, xMPushService, icVar));
    }

    private static boolean b(ic icVar) {
        Map a = icVar.a().a();
        return a != null && a.containsKey("notify_effect");
    }

    private static void c(XMPushService xMPushService, ic icVar) {
        xMPushService.a((XMPushService.i) new s(4, xMPushService, icVar));
    }

    private static boolean c(ic icVar) {
        if (icVar.a() == null || icVar.a().a() == null) {
            return false;
        }
        return "1".equals(icVar.a().a().get("obslete_ads_message"));
    }

    private static void d(XMPushService xMPushService, ic icVar) {
        xMPushService.a((XMPushService.i) new t(4, xMPushService, icVar));
    }

    public void a(Context context, al.b bVar, boolean z, int i, String str) {
        k a;
        if (!z && (a = l.a(context)) != null && "token-expired".equals(str)) {
            try {
                l.a(context, a.f, a.d, a.e);
            } catch (IOException | JSONException e) {
                b.a(e);
            }
        }
    }

    public void a(XMPushService xMPushService, ff ffVar, al.b bVar) {
        try {
            a(xMPushService, ffVar.a(bVar.h), (long) ffVar.c());
        } catch (IllegalArgumentException e) {
            b.a((Throwable) e);
        }
    }

    public void a(XMPushService xMPushService, gd gdVar, al.b bVar) {
        if (gdVar instanceof gc) {
            gc gcVar = (gc) gdVar;
            ga a = gcVar.a("s");
            if (a != null) {
                try {
                    a(xMPushService, au.a(au.a(bVar.h, gcVar.j()), a.c()), (long) gr.a(gdVar.a()));
                } catch (IllegalArgumentException e) {
                    b.a((Throwable) e);
                }
            }
        } else {
            b.a("not a mipush message");
        }
    }
}
