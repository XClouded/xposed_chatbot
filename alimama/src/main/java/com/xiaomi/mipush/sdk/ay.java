package com.xiaomi.mipush.sdk;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.Cif;
import com.xiaomi.push.as;
import com.xiaomi.push.ax;
import com.xiaomi.push.da;
import com.xiaomi.push.ev;
import com.xiaomi.push.hg;
import com.xiaomi.push.hh;
import com.xiaomi.push.hk;
import com.xiaomi.push.hl;
import com.xiaomi.push.hq;
import com.xiaomi.push.ht;
import com.xiaomi.push.ic;
import com.xiaomi.push.ig;
import com.xiaomi.push.im;
import com.xiaomi.push.iq;
import com.xiaomi.push.ir;
import com.xiaomi.push.l;
import com.xiaomi.push.service.ag;
import com.xiaomi.push.service.aj;
import com.xiaomi.push.service.ap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ay {
    private static ay a = null;

    /* renamed from: a  reason: collision with other field name */
    private static final ArrayList<a> f41a = new ArrayList<>();
    private static boolean b = false;

    /* renamed from: a  reason: collision with other field name */
    private Context f42a;

    /* renamed from: a  reason: collision with other field name */
    private Intent f43a = null;

    /* renamed from: a  reason: collision with other field name */
    private Handler f44a = null;
    /* access modifiers changed from: private */

    /* renamed from: a  reason: collision with other field name */
    public Messenger f45a;
    /* access modifiers changed from: private */

    /* renamed from: a  reason: collision with other field name */
    public Integer f46a = null;

    /* renamed from: a  reason: collision with other field name */
    private String f47a;

    /* renamed from: a  reason: collision with other field name */
    private List<Message> f48a = new ArrayList();

    /* renamed from: a  reason: collision with other field name */
    private boolean f49a = false;
    /* access modifiers changed from: private */
    public boolean c = false;

    static class a<T extends ir<T, ?>> {
        hg a;

        /* renamed from: a  reason: collision with other field name */
        T f50a;

        /* renamed from: a  reason: collision with other field name */
        boolean f51a;

        a() {
        }
    }

    private ay(Context context) {
        this.f42a = context.getApplicationContext();
        this.f47a = null;
        this.f49a = c();
        b = d();
        this.f44a = new az(this, Looper.getMainLooper());
        Intent b2 = b();
        if (b2 != null) {
            b(b2);
        }
    }

    private synchronized int a() {
        return this.f42a.getSharedPreferences("mipush_extra", 0).getInt(Constants.EXTRA_KEY_BOOT_SERVICE_MODE, -1);
    }

    /* renamed from: a  reason: collision with other method in class */
    private Intent m36a() {
        return (!a() || "com.xiaomi.xmsf".equals(this.f42a.getPackageName())) ? e() : d();
    }

    private Message a(Intent intent) {
        Message obtain = Message.obtain();
        obtain.what = 17;
        obtain.obj = intent;
        return obtain;
    }

    public static synchronized ay a(Context context) {
        ay ayVar;
        synchronized (ay.class) {
            if (a == null) {
                a = new ay(context);
            }
            ayVar = a;
        }
        return ayVar;
    }

    /* renamed from: a  reason: collision with other method in class */
    private String m37a() {
        try {
            return this.f42a.getPackageManager().getPackageInfo("com.xiaomi.xmsf", 4).versionCode >= 106 ? "com.xiaomi.push.service.XMPushService" : "com.xiaomi.xmsf.push.service.XMPushService";
        } catch (Exception unused) {
            return "com.xiaomi.xmsf.push.service.XMPushService";
        }
    }

    /* access modifiers changed from: private */
    public void a(String str, bd bdVar, boolean z, HashMap<String, String> hashMap) {
        Cif ifVar;
        String str2;
        if (d.a(this.f42a).b() && as.b(this.f42a)) {
            Cif ifVar2 = new Cif();
            ifVar2.a(true);
            Intent a2 = a();
            if (TextUtils.isEmpty(str)) {
                str = aj.a();
                ifVar2.a(str);
                ifVar = z ? new Cif(str, true) : null;
                synchronized (ao.class) {
                    ao.a(this.f42a).a(str);
                }
            } else {
                ifVar2.a(str);
                ifVar = z ? new Cif(str, true) : null;
            }
            switch (bc.a[bdVar.ordinal()]) {
                case 1:
                    ifVar2.c(hq.DisablePushMessage.f485a);
                    ifVar.c(hq.DisablePushMessage.f485a);
                    if (hashMap != null) {
                        ifVar2.a((Map<String, String>) hashMap);
                        ifVar.a((Map<String, String>) hashMap);
                    }
                    str2 = "com.xiaomi.mipush.DISABLE_PUSH_MESSAGE";
                    break;
                case 2:
                    ifVar2.c(hq.EnablePushMessage.f485a);
                    ifVar.c(hq.EnablePushMessage.f485a);
                    if (hashMap != null) {
                        ifVar2.a((Map<String, String>) hashMap);
                        ifVar.a((Map<String, String>) hashMap);
                    }
                    str2 = "com.xiaomi.mipush.ENABLE_PUSH_MESSAGE";
                    break;
                case 3:
                case 4:
                case 5:
                case 6:
                    ifVar2.c(hq.ThirdPartyRegUpdate.f485a);
                    if (hashMap != null) {
                        ifVar2.a((Map<String, String>) hashMap);
                        break;
                    }
                    break;
            }
            a2.setAction(str2);
            ifVar2.b(d.a(this.f42a).a());
            ifVar2.d(this.f42a.getPackageName());
            a(ifVar2, hg.Notification, false, (ht) null);
            if (z) {
                ifVar.b(d.a(this.f42a).a());
                ifVar.d(this.f42a.getPackageName());
                byte[] a3 = iq.a(ar.a(this.f42a, ifVar, hg.Notification, false, this.f42a.getPackageName(), d.a(this.f42a).a()));
                if (a3 != null) {
                    da.a(this.f42a.getPackageName(), this.f42a, ifVar, hg.Notification, a3.length);
                    a2.putExtra("mipush_payload", a3);
                    a2.putExtra("com.xiaomi.mipush.MESSAGE_CACHE", true);
                    a2.putExtra("mipush_app_id", d.a(this.f42a).a());
                    a2.putExtra("mipush_app_token", d.a(this.f42a).b());
                    c(a2);
                }
            }
            Message obtain = Message.obtain();
            obtain.what = 19;
            int ordinal = bdVar.ordinal();
            obtain.obj = str;
            obtain.arg1 = ordinal;
            this.f44a.sendMessageDelayed(obtain, 5000);
        }
    }

    private Intent b() {
        if (!"com.xiaomi.xmsf".equals(this.f42a.getPackageName())) {
            return c();
        }
        b.c("pushChannel xmsf create own channel");
        return e();
    }

    private void b(Intent intent) {
        try {
            if (l.a() || Build.VERSION.SDK_INT < 26) {
                this.f42a.startService(intent);
            } else {
                d(intent);
            }
        } catch (Exception e) {
            b.a((Throwable) e);
        }
    }

    private Intent c() {
        if (a()) {
            b.c("pushChannel app start miui china channel");
            return d();
        }
        b.c("pushChannel app start  own channel");
        return e();
    }

    private synchronized void c(int i) {
        this.f42a.getSharedPreferences("mipush_extra", 0).edit().putInt(Constants.EXTRA_KEY_BOOT_SERVICE_MODE, i).commit();
    }

    private void c(Intent intent) {
        int a2 = ag.a(this.f42a).a(hl.ServiceBootMode.a(), hh.START.a());
        int a3 = a();
        boolean z = a2 == hh.BIND.a() && b;
        int a4 = (z ? hh.BIND : hh.START).a();
        if (a4 != a3) {
            a(a4);
        }
        if (z) {
            d(intent);
        } else {
            b(intent);
        }
    }

    /* renamed from: c  reason: collision with other method in class */
    private boolean m38c() {
        try {
            PackageInfo packageInfo = this.f42a.getPackageManager().getPackageInfo("com.xiaomi.xmsf", 4);
            return packageInfo != null && packageInfo.versionCode >= 105;
        } catch (Throwable unused) {
            return false;
        }
    }

    private Intent d() {
        Intent intent = new Intent();
        String packageName = this.f42a.getPackageName();
        intent.setPackage("com.xiaomi.xmsf");
        intent.setClassName("com.xiaomi.xmsf", a());
        intent.putExtra("mipush_app_package", packageName);
        f();
        return intent;
    }

    private synchronized void d(Intent intent) {
        if (this.c) {
            Message a2 = a(intent);
            if (this.f48a.size() >= 50) {
                this.f48a.remove(0);
            }
            this.f48a.add(a2);
            return;
        } else if (this.f45a == null) {
            Context context = this.f42a;
            bb bbVar = new bb(this);
            Context context2 = this.f42a;
            context.bindService(intent, bbVar, 1);
            this.c = true;
            this.f48a.clear();
            this.f48a.add(a(intent));
        } else {
            try {
                this.f45a.send(a(intent));
            } catch (RemoteException unused) {
                this.f45a = null;
                this.c = false;
            }
        }
        return;
    }

    /* renamed from: d  reason: collision with other method in class */
    private boolean m39d() {
        if (a()) {
            try {
                return this.f42a.getPackageManager().getPackageInfo("com.xiaomi.xmsf", 4).versionCode >= 108;
            } catch (Exception unused) {
            }
        }
        return true;
    }

    private Intent e() {
        Intent intent = new Intent();
        String packageName = this.f42a.getPackageName();
        g();
        intent.setComponent(new ComponentName(this.f42a, "com.xiaomi.push.service.XMPushService"));
        intent.putExtra("mipush_app_package", packageName);
        return intent;
    }

    /* renamed from: e  reason: collision with other method in class */
    private boolean m40e() {
        String packageName = this.f42a.getPackageName();
        return packageName.contains("miui") || packageName.contains("xiaomi") || (this.f42a.getApplicationInfo().flags & 1) != 0;
    }

    private void f() {
        try {
            PackageManager packageManager = this.f42a.getPackageManager();
            ComponentName componentName = new ComponentName(this.f42a, "com.xiaomi.push.service.XMPushService");
            if (packageManager.getComponentEnabledSetting(componentName) != 2) {
                packageManager.setComponentEnabledSetting(componentName, 2, 1);
            }
        } catch (Throwable unused) {
        }
    }

    private void g() {
        try {
            PackageManager packageManager = this.f42a.getPackageManager();
            ComponentName componentName = new ComponentName(this.f42a, "com.xiaomi.push.service.XMPushService");
            if (packageManager.getComponentEnabledSetting(componentName) != 1) {
                packageManager.setComponentEnabledSetting(componentName, 1, 1);
            }
        } catch (Throwable unused) {
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m41a() {
        b(a());
    }

    public void a(int i) {
        Intent a2 = a();
        a2.setAction("com.xiaomi.mipush.CLEAR_NOTIFICATION");
        a2.putExtra(ap.z, this.f42a.getPackageName());
        a2.putExtra(ap.A, i);
        c(a2);
    }

    /* access modifiers changed from: package-private */
    public void a(int i, String str) {
        Intent a2 = a();
        a2.setAction("com.xiaomi.mipush.thirdparty");
        a2.putExtra("com.xiaomi.mipush.thirdparty_LEVEL", i);
        a2.putExtra("com.xiaomi.mipush.thirdparty_DESC", str);
        b(a2);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a  reason: collision with other method in class */
    public void m42a(Intent intent) {
        intent.fillIn(a(), 24);
        c(intent);
    }

    public final void a(hk hkVar) {
        Intent a2 = a();
        byte[] a3 = iq.a(hkVar);
        if (a3 == null) {
            b.a("send TinyData failed, because tinyDataBytes is null.");
            return;
        }
        a2.setAction("com.xiaomi.mipush.SEND_TINYDATA");
        a2.putExtra("mipush_payload", a3);
        b(a2);
    }

    public final void a(ig igVar, boolean z) {
        ev.a(this.f42a.getApplicationContext()).a(this.f42a.getPackageName(), "E100003", igVar.a(), 6001, "construct a register message");
        this.f43a = null;
        d.a(this.f42a).f56a = igVar.a();
        Intent a2 = a();
        byte[] a3 = iq.a(ar.a(this.f42a, igVar, hg.Registration));
        if (a3 == null) {
            b.a("register fail, because msgBytes is null.");
            return;
        }
        a2.setAction("com.xiaomi.mipush.REGISTER_APP");
        a2.putExtra("mipush_app_id", d.a(this.f42a).a());
        a2.putExtra("mipush_payload", a3);
        a2.putExtra("mipush_session", this.f47a);
        a2.putExtra("mipush_env_chanage", z);
        a2.putExtra("mipush_env_type", d.a(this.f42a).a());
        if (!as.b(this.f42a) || !b()) {
            this.f43a = a2;
        } else {
            c(a2);
        }
    }

    public final void a(im imVar) {
        byte[] a2 = iq.a(ar.a(this.f42a, imVar, hg.UnRegistration));
        if (a2 == null) {
            b.a("unregister fail, because msgBytes is null.");
            return;
        }
        Intent a3 = a();
        a3.setAction("com.xiaomi.mipush.UNREGISTER_APP");
        a3.putExtra("mipush_app_id", d.a(this.f42a).a());
        a3.putExtra("mipush_payload", a2);
        c(a3);
    }

    public final <T extends ir<T, ?>> void a(T t, hg hgVar, ht htVar) {
        a(t, hgVar, !hgVar.equals(hg.Registration), htVar);
    }

    public <T extends ir<T, ?>> void a(T t, hg hgVar, boolean z) {
        a aVar = new a();
        aVar.f50a = t;
        aVar.a = hgVar;
        aVar.f51a = z;
        synchronized (f41a) {
            f41a.add(aVar);
            if (f41a.size() > 10) {
                f41a.remove(0);
            }
        }
    }

    public final <T extends ir<T, ?>> void a(T t, hg hgVar, boolean z, ht htVar) {
        a(t, hgVar, z, true, htVar, true);
    }

    public final <T extends ir<T, ?>> void a(T t, hg hgVar, boolean z, ht htVar, boolean z2) {
        a(t, hgVar, z, true, htVar, z2);
    }

    public final <T extends ir<T, ?>> void a(T t, hg hgVar, boolean z, boolean z2, ht htVar, boolean z3) {
        a(t, hgVar, z, z2, htVar, z3, this.f42a.getPackageName(), d.a(this.f42a).a());
    }

    public final <T extends ir<T, ?>> void a(T t, hg hgVar, boolean z, boolean z2, ht htVar, boolean z3, String str, String str2) {
        if (d.a(this.f42a).c()) {
            ic a2 = ar.a(this.f42a, t, hgVar, z, str, str2);
            if (htVar != null) {
                a2.a(htVar);
            }
            byte[] a3 = iq.a(a2);
            if (a3 == null) {
                b.a("send message fail, because msgBytes is null.");
                return;
            }
            da.a(this.f42a.getPackageName(), this.f42a, t, hgVar, a3.length);
            Intent a4 = a();
            a4.setAction("com.xiaomi.mipush.SEND_MESSAGE");
            a4.putExtra("mipush_payload", a3);
            a4.putExtra("com.xiaomi.mipush.MESSAGE_CACHE", z3);
            c(a4);
        } else if (z2) {
            a(t, hgVar, z);
        } else {
            b.a("drop the message before initialization.");
        }
    }

    public final void a(String str, bd bdVar, f fVar) {
        ao.a(this.f42a).a(bdVar, "syncing");
        a(str, bdVar, false, j.a(this.f42a, fVar));
    }

    public void a(String str, String str2) {
        Intent a2 = a();
        a2.setAction("com.xiaomi.mipush.CLEAR_NOTIFICATION");
        a2.putExtra(ap.z, this.f42a.getPackageName());
        a2.putExtra(ap.E, str);
        a2.putExtra(ap.F, str2);
        c(a2);
    }

    public final void a(boolean z) {
        a(z, (String) null);
    }

    public final void a(boolean z, String str) {
        bd bdVar;
        if (z) {
            ao.a(this.f42a).a(bd.DISABLE_PUSH, "syncing");
            ao.a(this.f42a).a(bd.ENABLE_PUSH, "");
            bdVar = bd.DISABLE_PUSH;
        } else {
            ao.a(this.f42a).a(bd.ENABLE_PUSH, "syncing");
            ao.a(this.f42a).a(bd.DISABLE_PUSH, "");
            bdVar = bd.ENABLE_PUSH;
        }
        a(str, bdVar, true, (HashMap<String, String>) null);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m43a() {
        return this.f49a && 1 == d.a(this.f42a).a();
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m44a(int i) {
        if (!d.a(this.f42a).b()) {
            return false;
        }
        c(i);
        Cif ifVar = new Cif();
        ifVar.a(aj.a());
        ifVar.b(d.a(this.f42a).a());
        ifVar.d(this.f42a.getPackageName());
        ifVar.c(hq.ClientABTest.f485a);
        ifVar.f625a = new HashMap();
        Map<String, String> map = ifVar.f625a;
        map.put("boot_mode", i + "");
        a(this.f42a).a(ifVar, hg.Notification, false, (ht) null);
        return true;
    }

    /* renamed from: b  reason: collision with other method in class */
    public final void m45b() {
        Intent a2 = a();
        a2.setAction("com.xiaomi.mipush.DISABLE_PUSH");
        c(a2);
    }

    public void b(int i) {
        Intent a2 = a();
        a2.setAction("com.xiaomi.mipush.SET_NOTIFICATION_TYPE");
        a2.putExtra(ap.z, this.f42a.getPackageName());
        a2.putExtra(ap.B, i);
        String str = ap.D;
        a2.putExtra(str, ax.b(this.f42a.getPackageName() + i));
        c(a2);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m46b() {
        if (!a() || !e()) {
            return true;
        }
        if (this.f46a == null) {
            this.f46a = Integer.valueOf(com.xiaomi.push.service.as.a(this.f42a).a());
            if (this.f46a.intValue() == 0) {
                this.f42a.getContentResolver().registerContentObserver(com.xiaomi.push.service.as.a(this.f42a).a(), false, new ba(this, new Handler(Looper.getMainLooper())));
            }
        }
        return this.f46a.intValue() != 0;
    }

    /* renamed from: c  reason: collision with other method in class */
    public void m47c() {
        if (this.f43a != null) {
            c(this.f43a);
            this.f43a = null;
        }
    }

    /* renamed from: d  reason: collision with other method in class */
    public void m48d() {
        synchronized (f41a) {
            Iterator<a> it = f41a.iterator();
            while (it.hasNext()) {
                a next = it.next();
                a(next.f50a, next.a, next.f51a, false, (ht) null, true);
            }
            f41a.clear();
        }
    }

    /* renamed from: e  reason: collision with other method in class */
    public void m49e() {
        Intent a2 = a();
        a2.setAction("com.xiaomi.mipush.SET_NOTIFICATION_TYPE");
        a2.putExtra(ap.z, this.f42a.getPackageName());
        a2.putExtra(ap.D, ax.b(this.f42a.getPackageName()));
        c(a2);
    }
}
