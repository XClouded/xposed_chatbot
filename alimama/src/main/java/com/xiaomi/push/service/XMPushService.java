package com.xiaomi.push.service;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Messenger;
import android.os.Parcelable;
import android.os.Process;
import android.os.SystemClock;
import android.provider.Settings;
import android.text.TextUtils;
import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.el.parse.Operators;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.push.Cif;
import com.xiaomi.push.ab;
import com.xiaomi.push.ai;
import com.xiaomi.push.ao;
import com.xiaomi.push.as;
import com.xiaomi.push.ax;
import com.xiaomi.push.cu;
import com.xiaomi.push.dd;
import com.xiaomi.push.ek;
import com.xiaomi.push.eo;
import com.xiaomi.push.ev;
import com.xiaomi.push.ew;
import com.xiaomi.push.ff;
import com.xiaomi.push.fk;
import com.xiaomi.push.fm;
import com.xiaomi.push.fn;
import com.xiaomi.push.fp;
import com.xiaomi.push.fq;
import com.xiaomi.push.fr;
import com.xiaomi.push.fs;
import com.xiaomi.push.fx;
import com.xiaomi.push.fz;
import com.xiaomi.push.gc;
import com.xiaomi.push.gd;
import com.xiaomi.push.gr;
import com.xiaomi.push.gy;
import com.xiaomi.push.ha;
import com.xiaomi.push.hb;
import com.xiaomi.push.he;
import com.xiaomi.push.hf;
import com.xiaomi.push.hg;
import com.xiaomi.push.hl;
import com.xiaomi.push.ic;
import com.xiaomi.push.ig;
import com.xiaomi.push.iq;
import com.xiaomi.push.iw;
import com.xiaomi.push.service.al;
import com.xiaomi.push.service.g;
import com.xiaomi.push.service.l;
import com.xiaomi.push.t;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class XMPushService extends Service implements fp {
    public static int a = 1;
    private static final int b = Process.myPid();

    /* renamed from: a  reason: collision with other field name */
    private long f805a = 0;

    /* renamed from: a  reason: collision with other field name */
    private ContentObserver f806a;

    /* renamed from: a  reason: collision with other field name */
    Messenger f807a = null;

    /* renamed from: a  reason: collision with other field name */
    private fk f808a;
    /* access modifiers changed from: private */

    /* renamed from: a  reason: collision with other field name */
    public fm f809a;

    /* renamed from: a  reason: collision with other field name */
    private fn f810a;

    /* renamed from: a  reason: collision with other field name */
    private fr f811a = new bh(this);

    /* renamed from: a  reason: collision with other field name */
    private e f812a;

    /* renamed from: a  reason: collision with other field name */
    private ak f813a = null;

    /* renamed from: a  reason: collision with other field name */
    private av f814a;

    /* renamed from: a  reason: collision with other field name */
    private d f815a;

    /* renamed from: a  reason: collision with other field name */
    private g f816a = null;

    /* renamed from: a  reason: collision with other field name */
    protected Class f817a = XMJobService.class;

    /* renamed from: a  reason: collision with other field name */
    private String f818a;

    /* renamed from: a  reason: collision with other field name */
    private ArrayList<l> f819a = new ArrayList<>();

    /* renamed from: a  reason: collision with other field name */
    private Collection<ae> f820a = Collections.synchronizedCollection(new ArrayList());

    class a extends i {

        /* renamed from: a  reason: collision with other field name */
        al.b f821a = null;

        public a(al.b bVar) {
            super(9);
            this.f821a = bVar;
        }

        public String a() {
            return "bind the client. " + this.f821a.g;
        }

        /* renamed from: a  reason: collision with other method in class */
        public void m554a() {
            String str;
            try {
                if (!XMPushService.this.c()) {
                    com.xiaomi.channel.commonutils.logger.b.d("trying bind while the connection is not created, quit!");
                    return;
                }
                al.b a2 = al.a().a(this.f821a.g, this.f821a.f859b);
                if (a2 == null) {
                    str = "ignore bind because the channel " + this.f821a.g + " is removed ";
                } else if (a2.f854a == al.c.unbind) {
                    a2.a(al.c.binding, 0, 0, (String) null, (String) null);
                    XMPushService.a(XMPushService.this).a(a2);
                    ha.a(XMPushService.this, a2);
                    return;
                } else {
                    str = "trying duplicate bind, ingore! " + a2.f854a;
                }
                com.xiaomi.channel.commonutils.logger.b.a(str);
            } catch (Exception e) {
                com.xiaomi.channel.commonutils.logger.b.a((Throwable) e);
                XMPushService.this.a(10, e);
            } catch (Throwable unused) {
            }
        }
    }

    static class b extends i {
        private final al.b a;

        public b(al.b bVar) {
            super(12);
            this.a = bVar;
        }

        public String a() {
            return "bind time out. chid=" + this.a.g;
        }

        /* renamed from: a  reason: collision with other method in class */
        public void m555a() {
            this.a.a(al.c.unbind, 1, 21, (String) null, (String) null);
        }

        public boolean equals(Object obj) {
            if (!(obj instanceof b)) {
                return false;
            }
            return TextUtils.equals(((b) obj).a.g, this.a.g);
        }

        public int hashCode() {
            return this.a.g.hashCode();
        }
    }

    class c extends i {
        private ff a = null;

        public c(ff ffVar) {
            super(8);
            this.a = ffVar;
        }

        public String a() {
            return "receive a message.";
        }

        /* renamed from: a  reason: collision with other method in class */
        public void m556a() {
            XMPushService.a(XMPushService.this).a(this.a);
        }
    }

    public class d extends i {
        d() {
            super(1);
        }

        public String a() {
            return "do reconnect..";
        }

        /* renamed from: a  reason: collision with other method in class */
        public void m557a() {
            if (XMPushService.this.a()) {
                XMPushService.this.f();
            } else {
                com.xiaomi.channel.commonutils.logger.b.a("should not connect. quit the job.");
            }
        }
    }

    class e extends BroadcastReceiver {
        e() {
        }

        public void onReceive(Context context, Intent intent) {
            XMPushService.this.onStart(intent, XMPushService.a);
        }
    }

    public class f extends i {

        /* renamed from: a  reason: collision with other field name */
        public Exception f823a;
        public int b;

        f(int i, Exception exc) {
            super(2);
            this.b = i;
            this.f823a = exc;
        }

        public String a() {
            return "disconnect the connection.";
        }

        /* renamed from: a  reason: collision with other method in class */
        public void m558a() {
            XMPushService.this.a(this.b, this.f823a);
        }
    }

    class g extends i {
        g() {
            super(65535);
        }

        public String a() {
            return "Init Job";
        }

        /* renamed from: a  reason: collision with other method in class */
        public void m559a() {
            XMPushService.this.c();
        }
    }

    class h extends i {
        private Intent a = null;

        public h(Intent intent) {
            super(15);
            this.a = intent;
        }

        public String a() {
            return "Handle intent action = " + this.a.getAction();
        }

        /* renamed from: a  reason: collision with other method in class */
        public void m560a() {
            XMPushService.this.c(this.a);
        }
    }

    public static abstract class i extends g.b {
        public i(int i) {
            super(i);
        }

        public abstract String a();

        /* renamed from: a  reason: collision with other method in class */
        public abstract void m561a();

        public void run() {
            if (!(this.a == 4 || this.a == 8)) {
                com.xiaomi.channel.commonutils.logger.b.a("JOB: " + a());
            }
            a();
        }
    }

    class j extends i {
        public j() {
            super(5);
        }

        public String a() {
            return "ask the job queue to quit";
        }

        /* renamed from: a  reason: collision with other method in class */
        public void m562a() {
            XMPushService.a(XMPushService.this).a();
        }
    }

    class k extends i {
        private gd a = null;

        public k(gd gdVar) {
            super(8);
            this.a = gdVar;
        }

        public String a() {
            return "receive a message.";
        }

        /* renamed from: a  reason: collision with other method in class */
        public void m563a() {
            XMPushService.a(XMPushService.this).a(this.a);
        }
    }

    public interface l {
        void a();
    }

    class m extends i {

        /* renamed from: a  reason: collision with other field name */
        boolean f826a;

        public m(boolean z) {
            super(4);
            this.f826a = z;
        }

        public String a() {
            return "send ping..";
        }

        /* renamed from: a  reason: collision with other method in class */
        public void m564a() {
            if (XMPushService.this.c()) {
                try {
                    if (!this.f826a) {
                        ha.a();
                    }
                    XMPushService.a(XMPushService.this).b(this.f826a);
                } catch (fx e) {
                    com.xiaomi.channel.commonutils.logger.b.a((Throwable) e);
                    XMPushService.this.a(10, (Exception) e);
                }
            }
        }
    }

    class n extends i {

        /* renamed from: a  reason: collision with other field name */
        al.b f827a = null;

        public n(al.b bVar) {
            super(4);
            this.f827a = bVar;
        }

        public String a() {
            return "rebind the client. " + this.f827a.g;
        }

        /* renamed from: a  reason: collision with other method in class */
        public void m565a() {
            try {
                this.f827a.a(al.c.unbind, 1, 16, (String) null, (String) null);
                XMPushService.a(XMPushService.this).a(this.f827a.g, this.f827a.f859b);
                this.f827a.a(al.c.binding, 1, 16, (String) null, (String) null);
                XMPushService.a(XMPushService.this).a(this.f827a);
            } catch (fx e) {
                com.xiaomi.channel.commonutils.logger.b.a((Throwable) e);
                XMPushService.this.a(10, (Exception) e);
            }
        }
    }

    class o extends i {
        o() {
            super(3);
        }

        public String a() {
            return "reset the connection.";
        }

        /* renamed from: a  reason: collision with other method in class */
        public void m566a() {
            XMPushService.this.a(11, (Exception) null);
            if (XMPushService.this.a()) {
                XMPushService.this.f();
            }
        }
    }

    class p extends i {

        /* renamed from: a  reason: collision with other field name */
        al.b f828a = null;

        /* renamed from: a  reason: collision with other field name */
        String f829a;
        int b;

        /* renamed from: b  reason: collision with other field name */
        String f830b;

        public p(al.b bVar, int i, String str, String str2) {
            super(9);
            this.f828a = bVar;
            this.b = i;
            this.f829a = str;
            this.f830b = str2;
        }

        public String a() {
            return "unbind the channel. " + this.f828a.g;
        }

        /* renamed from: a  reason: collision with other method in class */
        public void m567a() {
            if (!(this.f828a.f854a == al.c.unbind || XMPushService.a(XMPushService.this) == null)) {
                try {
                    XMPushService.a(XMPushService.this).a(this.f828a.g, this.f828a.f859b);
                } catch (fx e) {
                    com.xiaomi.channel.commonutils.logger.b.a((Throwable) e);
                    XMPushService.this.a(10, (Exception) e);
                }
            }
            this.f828a.a(al.c.unbind, this.b, 0, this.f830b, this.f829a);
        }
    }

    static {
        cu.a("cn.app.chat.xiaomi.net", "cn.app.chat.xiaomi.net");
    }

    @TargetApi(11)
    public static Notification a(Context context) {
        Intent intent = new Intent(context, XMPushService.class);
        if (Build.VERSION.SDK_INT >= 11) {
            Notification.Builder builder = new Notification.Builder(context);
            builder.setSmallIcon(context.getApplicationInfo().icon);
            builder.setContentTitle("Push Service");
            builder.setContentText("Push Service");
            builder.setContentIntent(PendingIntent.getActivity(context, 0, intent, 0));
            return builder.getNotification();
        }
        Notification notification = new Notification();
        PendingIntent service = PendingIntent.getService(context, 0, intent, 0);
        try {
            notification.getClass().getMethod("setLatestEventInfo", new Class[]{Context.class, CharSequence.class, CharSequence.class, PendingIntent.class}).invoke(notification, new Object[]{context, "Push Service", "Push Service", service});
        } catch (Exception e2) {
            com.xiaomi.channel.commonutils.logger.b.a((Throwable) e2);
        }
        return notification;
    }

    private gd a(gd gdVar, String str, String str2) {
        StringBuilder sb;
        String str3;
        al a2 = al.a();
        List a3 = a2.a(str);
        if (a3.isEmpty()) {
            sb = new StringBuilder();
            str3 = "open channel should be called first before sending a packet, pkg=";
        } else {
            gdVar.o(str);
            str = gdVar.k();
            if (TextUtils.isEmpty(str)) {
                str = (String) a3.get(0);
                gdVar.l(str);
            }
            al.b a4 = a2.a(str, gdVar.m());
            if (!c()) {
                sb = new StringBuilder();
                str3 = "drop a packet as the channel is not connected, chid=";
            } else if (a4 == null || a4.f854a != al.c.binded) {
                sb = new StringBuilder();
                str3 = "drop a packet as the channel is not opened, chid=";
            } else if (TextUtils.equals(str2, a4.i)) {
                return gdVar;
            } else {
                sb = new StringBuilder();
                sb.append("invalid session. ");
                sb.append(str2);
                com.xiaomi.channel.commonutils.logger.b.a(sb.toString());
                return null;
            }
        }
        sb.append(str3);
        sb.append(str);
        com.xiaomi.channel.commonutils.logger.b.a(sb.toString());
        return null;
    }

    private al.b a(String str, Intent intent) {
        al.b a2 = al.a().a(str, intent.getStringExtra(ap.p));
        if (a2 == null) {
            a2 = new al.b(this);
        }
        a2.g = intent.getStringExtra(ap.r);
        a2.f859b = intent.getStringExtra(ap.p);
        a2.c = intent.getStringExtra(ap.t);
        a2.f856a = intent.getStringExtra(ap.z);
        a2.e = intent.getStringExtra(ap.x);
        a2.f = intent.getStringExtra(ap.y);
        a2.f858a = intent.getBooleanExtra(ap.w, false);
        a2.h = intent.getStringExtra(ap.v);
        a2.i = intent.getStringExtra(ap.C);
        a2.d = intent.getStringExtra(ap.u);
        a2.f855a = this.f815a;
        a2.a((Messenger) intent.getParcelableExtra(ap.G));
        a2.f848a = getApplicationContext();
        al.a().a(a2);
        return a2;
    }

    private String a() {
        String str;
        ao.a();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        Object obj = new Object();
        String str2 = null;
        if ("com.xiaomi.xmsf".equals(getPackageName())) {
            as a2 = as.a(this);
            str = null;
            while (true) {
                if (!TextUtils.isEmpty(str) && a2.a() != 0) {
                    break;
                }
                if (TextUtils.isEmpty(str)) {
                    str = com.xiaomi.push.l.a("ro.miui.region");
                    if (TextUtils.isEmpty(str)) {
                        str = com.xiaomi.push.l.a("ro.product.locale.region");
                    }
                }
                try {
                    synchronized (obj) {
                        obj.wait(100);
                    }
                } catch (InterruptedException unused) {
                }
            }
        } else {
            str = com.xiaomi.push.l.b();
        }
        if (!TextUtils.isEmpty(str)) {
            a.a(getApplicationContext()).b(str);
            str2 = com.xiaomi.push.l.a(str).name();
        }
        com.xiaomi.channel.commonutils.logger.b.a("wait region :" + str2 + " cost = " + (SystemClock.elapsedRealtime() - elapsedRealtime));
        return str2;
    }

    private void a(BroadcastReceiver broadcastReceiver) {
        if (broadcastReceiver != null) {
            try {
                unregisterReceiver(broadcastReceiver);
            } catch (IllegalArgumentException e2) {
                com.xiaomi.channel.commonutils.logger.b.a((Throwable) e2);
            }
        }
    }

    private void a(Intent intent) {
        String stringExtra = intent.getStringExtra(ap.z);
        String stringExtra2 = intent.getStringExtra(ap.C);
        Bundle bundleExtra = intent.getBundleExtra("ext_packet");
        al a2 = al.a();
        ff ffVar = null;
        if (bundleExtra != null) {
            gc gcVar = (gc) a((gd) new gc(bundleExtra), stringExtra, stringExtra2);
            if (gcVar != null) {
                ffVar = ff.a((gd) gcVar, a2.a(gcVar.k(), gcVar.m()).h);
            } else {
                return;
            }
        } else {
            byte[] byteArrayExtra = intent.getByteArrayExtra("ext_raw_packet");
            if (byteArrayExtra != null) {
                long longExtra = intent.getLongExtra(ap.p, 0);
                String stringExtra3 = intent.getStringExtra(ap.q);
                String stringExtra4 = intent.getStringExtra("ext_chid");
                al.b a3 = a2.a(stringExtra4, Long.toString(longExtra));
                if (a3 != null) {
                    ff ffVar2 = new ff();
                    try {
                        ffVar2.a(Integer.parseInt(stringExtra4));
                    } catch (NumberFormatException unused) {
                    }
                    ffVar2.a("SECMSG", (String) null);
                    ffVar2.a(longExtra, "xiaomi.com", stringExtra3);
                    ffVar2.a(intent.getStringExtra("ext_pkt_id"));
                    ffVar2.a(byteArrayExtra, a3.h);
                    ffVar = ffVar2;
                }
            }
        }
        if (ffVar != null) {
            c((i) new aw(this, ffVar));
        }
    }

    private void a(Intent intent, int i2) {
        byte[] byteArrayExtra = intent.getByteArrayExtra("mipush_payload");
        boolean booleanExtra = intent.getBooleanExtra("com.xiaomi.mipush.MESSAGE_CACHE", true);
        Cif ifVar = new Cif();
        try {
            iq.a(ifVar, byteArrayExtra);
            ai.a(getApplicationContext()).a((ai.a) new b(ifVar, new WeakReference(this), booleanExtra), i2);
        } catch (iw unused) {
            com.xiaomi.channel.commonutils.logger.b.d("aw_ping : send help app ping  error");
        }
    }

    private void a(String str, int i2) {
        Collection<al.b> a2 = al.a().a(str);
        if (a2 != null) {
            for (al.b bVar : a2) {
                if (bVar != null) {
                    a((i) new p(bVar, i2, (String) null, (String) null));
                }
            }
        }
        al.a().a(str);
    }

    /* renamed from: a  reason: collision with other method in class */
    private boolean m540a(String str, Intent intent) {
        al.b a2 = al.a().a(str, intent.getStringExtra(ap.p));
        boolean z = false;
        if (a2 == null || str == null) {
            return false;
        }
        String stringExtra = intent.getStringExtra(ap.C);
        String stringExtra2 = intent.getStringExtra(ap.v);
        if (!TextUtils.isEmpty(a2.i) && !TextUtils.equals(stringExtra, a2.i)) {
            com.xiaomi.channel.commonutils.logger.b.a("session changed. old session=" + a2.i + ", new session=" + stringExtra + " chid = " + str);
            z = true;
        }
        if (stringExtra2.equals(a2.h)) {
            return z;
        }
        com.xiaomi.channel.commonutils.logger.b.a("security changed. chid = " + str + " sechash = " + ax.a(stringExtra2));
        return true;
    }

    private void b(Intent intent) {
        String stringExtra = intent.getStringExtra(ap.z);
        String stringExtra2 = intent.getStringExtra(ap.C);
        Parcelable[] parcelableArrayExtra = intent.getParcelableArrayExtra("ext_packets");
        gc[] gcVarArr = new gc[parcelableArrayExtra.length];
        intent.getBooleanExtra("ext_encrypt", true);
        int i2 = 0;
        while (i2 < parcelableArrayExtra.length) {
            gcVarArr[i2] = new gc((Bundle) parcelableArrayExtra[i2]);
            gcVarArr[i2] = (gc) a((gd) gcVarArr[i2], stringExtra, stringExtra2);
            if (gcVarArr[i2] != null) {
                i2++;
            } else {
                return;
            }
        }
        al a2 = al.a();
        ff[] ffVarArr = new ff[gcVarArr.length];
        for (int i3 = 0; i3 < gcVarArr.length; i3++) {
            gc gcVar = gcVarArr[i3];
            ffVarArr[i3] = ff.a((gd) gcVar, a2.a(gcVar.k(), gcVar.m()).h);
        }
        c((i) new c(this, ffVarArr));
    }

    private void b(boolean z) {
        this.f805a = System.currentTimeMillis();
        if (c()) {
            if (this.f809a.d() || this.f809a.e() || as.d(this)) {
                c((i) new m(z));
                return;
            }
            c((i) new f(17, (Exception) null));
        }
        a(true);
    }

    /* access modifiers changed from: private */
    public void c() {
        String str;
        a a2 = a.a(getApplicationContext());
        String a3 = a2.a();
        com.xiaomi.channel.commonutils.logger.b.a("region of cache is " + a3);
        if (TextUtils.isEmpty(a3)) {
            a3 = a();
        }
        if (!TextUtils.isEmpty(a3)) {
            this.f818a = a3;
            a2.a(a3);
            if (com.xiaomi.push.o.Global.name().equals(this.f818a)) {
                str = "app.chat.global.xiaomi.net";
            } else if (com.xiaomi.push.o.Europe.name().equals(this.f818a)) {
                str = "fr.app.chat.global.xiaomi.net";
            } else if (com.xiaomi.push.o.Russia.name().equals(this.f818a)) {
                str = "ru.app.chat.global.xiaomi.net";
            } else if (com.xiaomi.push.o.India.name().equals(this.f818a)) {
                str = "idmb.app.chat.global.xiaomi.net";
            }
            fn.a(str);
        } else {
            this.f818a = com.xiaomi.push.o.China.name();
        }
        if (com.xiaomi.push.o.China.name().equals(this.f818a)) {
            fn.a("cn.app.chat.xiaomi.net");
        }
        if (g()) {
            bp bpVar = new bp(this, 11);
            a((i) bpVar);
            l.a((l.a) new bq(this, bpVar));
        }
        try {
            if (t.a()) {
                this.f815a.a((Context) this);
            }
        } catch (Exception e2) {
            com.xiaomi.channel.commonutils.logger.b.a((Throwable) e2);
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v10, resolved type: com.xiaomi.push.service.XMPushService$n} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v11, resolved type: com.xiaomi.push.service.XMPushService$a} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r0v129, resolved type: java.lang.Object} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v31, resolved type: com.xiaomi.push.service.al$b} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v71, resolved type: com.xiaomi.push.service.XMPushService$o} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v85, resolved type: com.xiaomi.push.service.br} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v86, resolved type: com.xiaomi.push.service.br} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r5v13, resolved type: com.xiaomi.push.service.br} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r12v87, resolved type: com.xiaomi.push.service.br} */
    /* JADX WARNING: type inference failed for: r12v14, types: [com.xiaomi.push.service.XMPushService$i] */
    /* access modifiers changed from: private */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void c(android.content.Intent r12) {
        /*
            r11 = this;
            com.xiaomi.push.service.al r0 = com.xiaomi.push.service.al.a()
            java.lang.String r1 = com.xiaomi.push.service.ap.d
            java.lang.String r2 = r12.getAction()
            boolean r1 = r1.equalsIgnoreCase(r2)
            r2 = 2
            r3 = 1
            r4 = 0
            if (r1 != 0) goto L_0x06c9
            java.lang.String r1 = com.xiaomi.push.service.ap.j
            java.lang.String r5 = r12.getAction()
            boolean r1 = r1.equalsIgnoreCase(r5)
            if (r1 == 0) goto L_0x0021
            goto L_0x06c9
        L_0x0021:
            java.lang.String r1 = com.xiaomi.push.service.ap.i
            java.lang.String r5 = r12.getAction()
            boolean r1 = r1.equalsIgnoreCase(r5)
            if (r1 == 0) goto L_0x0091
            java.lang.String r1 = com.xiaomi.push.service.ap.z
            java.lang.String r1 = r12.getStringExtra(r1)
            java.lang.String r3 = com.xiaomi.push.service.ap.r
            java.lang.String r5 = r12.getStringExtra(r3)
            java.lang.String r3 = com.xiaomi.push.service.ap.p
            java.lang.String r6 = r12.getStringExtra(r3)
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r3 = "Service called close channel chid = "
            r12.append(r3)
            r12.append(r5)
            java.lang.String r3 = " res = "
            r12.append(r3)
            java.lang.String r3 = com.xiaomi.push.service.al.b.a((java.lang.String) r6)
            r12.append(r3)
            java.lang.String r12 = r12.toString()
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r12)
            boolean r12 = android.text.TextUtils.isEmpty(r5)
            if (r12 == 0) goto L_0x007d
            java.util.List r12 = r0.a((java.lang.String) r1)
            java.util.Iterator r12 = r12.iterator()
        L_0x006d:
            boolean r0 = r12.hasNext()
            if (r0 == 0) goto L_0x0745
            java.lang.Object r0 = r12.next()
            java.lang.String r0 = (java.lang.String) r0
            r11.a((java.lang.String) r0, (int) r2)
            goto L_0x006d
        L_0x007d:
            boolean r12 = android.text.TextUtils.isEmpty(r6)
            if (r12 == 0) goto L_0x0088
            r11.a((java.lang.String) r5, (int) r2)
            goto L_0x0745
        L_0x0088:
            r7 = 2
            r8 = 0
            r9 = 0
            r4 = r11
            r4.a(r5, r6, r7, r8, r9)
            goto L_0x0745
        L_0x0091:
            java.lang.String r1 = com.xiaomi.push.service.ap.e
            java.lang.String r2 = r12.getAction()
            boolean r1 = r1.equalsIgnoreCase(r2)
            if (r1 == 0) goto L_0x00a2
            r11.a((android.content.Intent) r12)
            goto L_0x0745
        L_0x00a2:
            java.lang.String r1 = com.xiaomi.push.service.ap.g
            java.lang.String r2 = r12.getAction()
            boolean r1 = r1.equalsIgnoreCase(r2)
            if (r1 == 0) goto L_0x00b3
            r11.b((android.content.Intent) r12)
            goto L_0x0745
        L_0x00b3:
            java.lang.String r1 = com.xiaomi.push.service.ap.f
            java.lang.String r2 = r12.getAction()
            boolean r1 = r1.equalsIgnoreCase(r2)
            if (r1 == 0) goto L_0x00f8
            java.lang.String r1 = com.xiaomi.push.service.ap.z
            java.lang.String r1 = r12.getStringExtra(r1)
            java.lang.String r2 = com.xiaomi.push.service.ap.C
            java.lang.String r2 = r12.getStringExtra(r2)
            java.lang.String r3 = "ext_packet"
            android.os.Bundle r12 = r12.getBundleExtra(r3)
            com.xiaomi.push.gb r3 = new com.xiaomi.push.gb
            r3.<init>(r12)
            com.xiaomi.push.gd r12 = r11.a((com.xiaomi.push.gd) r3, (java.lang.String) r1, (java.lang.String) r2)
            if (r12 == 0) goto L_0x0745
            java.lang.String r1 = r12.k()
            java.lang.String r2 = r12.m()
            com.xiaomi.push.service.al$b r0 = r0.a((java.lang.String) r1, (java.lang.String) r2)
            java.lang.String r0 = r0.h
            com.xiaomi.push.ff r12 = com.xiaomi.push.ff.a((com.xiaomi.push.gd) r12, (java.lang.String) r0)
            com.xiaomi.push.service.aw r0 = new com.xiaomi.push.service.aw
            r0.<init>(r11, r12)
        L_0x00f3:
            r11.c((com.xiaomi.push.service.XMPushService.i) r0)
            goto L_0x0745
        L_0x00f8:
            java.lang.String r1 = com.xiaomi.push.service.ap.h
            java.lang.String r2 = r12.getAction()
            boolean r1 = r1.equalsIgnoreCase(r2)
            if (r1 == 0) goto L_0x0139
            java.lang.String r1 = com.xiaomi.push.service.ap.z
            java.lang.String r1 = r12.getStringExtra(r1)
            java.lang.String r2 = com.xiaomi.push.service.ap.C
            java.lang.String r2 = r12.getStringExtra(r2)
            java.lang.String r3 = "ext_packet"
            android.os.Bundle r12 = r12.getBundleExtra(r3)
            com.xiaomi.push.gf r3 = new com.xiaomi.push.gf
            r3.<init>((android.os.Bundle) r12)
            com.xiaomi.push.gd r12 = r11.a((com.xiaomi.push.gd) r3, (java.lang.String) r1, (java.lang.String) r2)
            if (r12 == 0) goto L_0x0745
            java.lang.String r1 = r12.k()
            java.lang.String r2 = r12.m()
            com.xiaomi.push.service.al$b r0 = r0.a((java.lang.String) r1, (java.lang.String) r2)
            java.lang.String r0 = r0.h
            com.xiaomi.push.ff r12 = com.xiaomi.push.ff.a((com.xiaomi.push.gd) r12, (java.lang.String) r0)
            com.xiaomi.push.service.aw r0 = new com.xiaomi.push.service.aw
            r0.<init>(r11, r12)
            goto L_0x00f3
        L_0x0139:
            java.lang.String r1 = com.xiaomi.push.service.ap.k
            java.lang.String r2 = r12.getAction()
            boolean r1 = r1.equals(r2)
            if (r1 == 0) goto L_0x01a2
            java.lang.String r0 = com.xiaomi.push.service.ap.r
            java.lang.String r0 = r12.getStringExtra(r0)
            java.lang.String r1 = com.xiaomi.push.service.ap.p
            java.lang.String r1 = r12.getStringExtra(r1)
            if (r0 == 0) goto L_0x0745
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "request reset connection from chid = "
            r2.append(r3)
            r2.append(r0)
            java.lang.String r2 = r2.toString()
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r2)
            com.xiaomi.push.service.al r2 = com.xiaomi.push.service.al.a()
            com.xiaomi.push.service.al$b r0 = r2.a((java.lang.String) r0, (java.lang.String) r1)
            if (r0 == 0) goto L_0x0745
            java.lang.String r1 = r0.h
            java.lang.String r2 = com.xiaomi.push.service.ap.v
            java.lang.String r12 = r12.getStringExtra(r2)
            boolean r12 = r1.equals(r12)
            if (r12 == 0) goto L_0x0745
            com.xiaomi.push.service.al$c r12 = r0.f854a
            com.xiaomi.push.service.al$c r0 = com.xiaomi.push.service.al.c.binded
            if (r12 != r0) goto L_0x0745
            com.xiaomi.push.fm r12 = r11.a()
            if (r12 == 0) goto L_0x0198
            long r0 = java.lang.System.currentTimeMillis()
            r2 = 15000(0x3a98, double:7.411E-320)
            long r0 = r0 - r2
            boolean r12 = r12.a((long) r0)
            if (r12 != 0) goto L_0x0745
        L_0x0198:
            com.xiaomi.push.service.XMPushService$o r12 = new com.xiaomi.push.service.XMPushService$o
            r12.<init>()
        L_0x019d:
            r11.c((com.xiaomi.push.service.XMPushService.i) r12)
            goto L_0x0745
        L_0x01a2:
            java.lang.String r1 = com.xiaomi.push.service.ap.l
            java.lang.String r2 = r12.getAction()
            boolean r1 = r1.equals(r2)
            r2 = 0
            if (r1 == 0) goto L_0x0232
            java.lang.String r1 = com.xiaomi.push.service.ap.z
            java.lang.String r1 = r12.getStringExtra(r1)
            java.util.List r3 = r0.a((java.lang.String) r1)
            boolean r5 = r3.isEmpty()
            if (r5 == 0) goto L_0x01d4
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r0 = "open channel should be called first before update info, pkg="
            r12.append(r0)
            r12.append(r1)
            java.lang.String r12 = r12.toString()
        L_0x01d0:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r12)
            return
        L_0x01d4:
            java.lang.String r1 = com.xiaomi.push.service.ap.r
            java.lang.String r1 = r12.getStringExtra(r1)
            java.lang.String r5 = com.xiaomi.push.service.ap.p
            java.lang.String r5 = r12.getStringExtra(r5)
            boolean r6 = android.text.TextUtils.isEmpty(r1)
            if (r6 == 0) goto L_0x01ec
            java.lang.Object r1 = r3.get(r4)
            java.lang.String r1 = (java.lang.String) r1
        L_0x01ec:
            boolean r3 = android.text.TextUtils.isEmpty(r5)
            if (r3 == 0) goto L_0x020a
            java.util.Collection r0 = r0.a((java.lang.String) r1)
            if (r0 == 0) goto L_0x020e
            boolean r1 = r0.isEmpty()
            if (r1 != 0) goto L_0x020e
            java.util.Iterator r0 = r0.iterator()
            java.lang.Object r0 = r0.next()
            r2 = r0
            com.xiaomi.push.service.al$b r2 = (com.xiaomi.push.service.al.b) r2
            goto L_0x020e
        L_0x020a:
            com.xiaomi.push.service.al$b r2 = r0.a((java.lang.String) r1, (java.lang.String) r5)
        L_0x020e:
            if (r2 == 0) goto L_0x0745
            java.lang.String r0 = com.xiaomi.push.service.ap.x
            boolean r0 = r12.hasExtra(r0)
            if (r0 == 0) goto L_0x0220
            java.lang.String r0 = com.xiaomi.push.service.ap.x
            java.lang.String r0 = r12.getStringExtra(r0)
            r2.e = r0
        L_0x0220:
            java.lang.String r0 = com.xiaomi.push.service.ap.y
            boolean r0 = r12.hasExtra(r0)
            if (r0 == 0) goto L_0x0745
            java.lang.String r0 = com.xiaomi.push.service.ap.y
            java.lang.String r12 = r12.getStringExtra(r0)
            r2.f = r12
            goto L_0x0745
        L_0x0232:
            java.lang.String r0 = "com.xiaomi.mipush.REGISTER_APP"
            java.lang.String r1 = r12.getAction()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x02b0
            android.content.Context r0 = r11.getApplicationContext()
            com.xiaomi.push.service.as r0 = com.xiaomi.push.service.as.a(r0)
            boolean r0 = r0.a()
            if (r0 == 0) goto L_0x0273
            android.content.Context r0 = r11.getApplicationContext()
            com.xiaomi.push.service.as r0 = com.xiaomi.push.service.as.a(r0)
            int r0 = r0.a()
            if (r0 != 0) goto L_0x0273
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "register without being provisioned. "
            r0.append(r1)
            java.lang.String r1 = "mipush_app_package"
            java.lang.String r12 = r12.getStringExtra(r1)
            r0.append(r12)
            java.lang.String r12 = r0.toString()
            goto L_0x01d0
        L_0x0273:
            java.lang.String r0 = "mipush_payload"
            byte[] r9 = r12.getByteArrayExtra(r0)
            java.lang.String r0 = "mipush_app_package"
            java.lang.String r10 = r12.getStringExtra(r0)
            java.lang.String r0 = "mipush_env_chanage"
            boolean r0 = r12.getBooleanExtra(r0, r4)
            java.lang.String r1 = "mipush_env_type"
            int r8 = r12.getIntExtra(r1, r3)
            com.xiaomi.push.service.m r12 = com.xiaomi.push.service.m.a((android.content.Context) r11)
            r12.d(r10)
            if (r0 == 0) goto L_0x02ab
            java.lang.String r12 = "com.xiaomi.xmsf"
            java.lang.String r0 = r11.getPackageName()
            boolean r12 = r12.equals(r0)
            if (r12 != 0) goto L_0x02ab
            com.xiaomi.push.service.br r12 = new com.xiaomi.push.service.br
            r7 = 14
            r5 = r12
            r6 = r11
            r5.<init>(r6, r7, r8, r9, r10)
            goto L_0x019d
        L_0x02ab:
            r11.a((byte[]) r9, (java.lang.String) r10)
            goto L_0x0745
        L_0x02b0:
            java.lang.String r0 = "com.xiaomi.mipush.SEND_MESSAGE"
            java.lang.String r1 = r12.getAction()
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L_0x069f
            java.lang.String r0 = "com.xiaomi.mipush.UNREGISTER_APP"
            java.lang.String r1 = r12.getAction()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x02ca
            goto L_0x069f
        L_0x02ca:
            java.lang.String r0 = com.xiaomi.push.service.at.a
            java.lang.String r1 = r12.getAction()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0388
            java.lang.String r0 = "uninstall_pkg_name"
            java.lang.String r12 = r12.getStringExtra(r0)
            if (r12 == 0) goto L_0x0387
            java.lang.String r0 = r12.trim()
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L_0x02ea
            goto L_0x0387
        L_0x02ea:
            android.content.pm.PackageManager r0 = r11.getPackageManager()     // Catch:{ NameNotFoundException -> 0x02f2 }
            r0.getPackageInfo(r12, r4)     // Catch:{ NameNotFoundException -> 0x02f2 }
            r3 = 0
        L_0x02f2:
            java.lang.String r0 = "com.xiaomi.channel"
            boolean r0 = r0.equals(r12)
            if (r0 == 0) goto L_0x0315
            com.xiaomi.push.service.al r0 = com.xiaomi.push.service.al.a()
            java.lang.String r1 = "1"
            java.util.Collection r0 = r0.a((java.lang.String) r1)
            boolean r0 = r0.isEmpty()
            if (r0 != 0) goto L_0x0315
            if (r3 == 0) goto L_0x0315
            java.lang.String r12 = "1"
            r11.a((java.lang.String) r12, (int) r4)
            java.lang.String r12 = "close the miliao channel as the app is uninstalled."
            goto L_0x01d0
        L_0x0315:
            java.lang.String r0 = "pref_registered_pkg_names"
            android.content.SharedPreferences r0 = r11.getSharedPreferences(r0, r4)
            java.lang.String r1 = r0.getString(r12, r2)
            boolean r2 = android.text.TextUtils.isEmpty(r1)
            if (r2 != 0) goto L_0x0745
            if (r3 == 0) goto L_0x0745
            android.content.SharedPreferences$Editor r0 = r0.edit()
            r0.remove(r12)
            r0.commit()
            boolean r0 = com.xiaomi.push.service.z.b((android.content.Context) r11, (java.lang.String) r12)
            if (r0 == 0) goto L_0x033a
            com.xiaomi.push.service.z.b((android.content.Context) r11, (java.lang.String) r12)
        L_0x033a:
            com.xiaomi.push.service.z.a((android.content.Context) r11, (java.lang.String) r12)
            boolean r0 = r11.c()
            if (r0 == 0) goto L_0x0745
            if (r1 == 0) goto L_0x0745
            com.xiaomi.push.ic r0 = com.xiaomi.push.service.w.a((java.lang.String) r12, (java.lang.String) r1)     // Catch:{ fx -> 0x0367 }
            com.xiaomi.push.service.w.a((com.xiaomi.push.service.XMPushService) r11, (com.xiaomi.push.ic) r0)     // Catch:{ fx -> 0x0367 }
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ fx -> 0x0367 }
            r0.<init>()     // Catch:{ fx -> 0x0367 }
            java.lang.String r1 = "uninstall "
            r0.append(r1)     // Catch:{ fx -> 0x0367 }
            r0.append(r12)     // Catch:{ fx -> 0x0367 }
            java.lang.String r12 = " msg sent"
            r0.append(r12)     // Catch:{ fx -> 0x0367 }
            java.lang.String r12 = r0.toString()     // Catch:{ fx -> 0x0367 }
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r12)     // Catch:{ fx -> 0x0367 }
            goto L_0x0745
        L_0x0367:
            r12 = move-exception
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Fail to send Message: "
            r0.append(r1)
            java.lang.String r1 = r12.getMessage()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            com.xiaomi.channel.commonutils.logger.b.d(r0)
            r0 = 10
            r11.a((int) r0, (java.lang.Exception) r12)
            goto L_0x0745
        L_0x0387:
            return
        L_0x0388:
            java.lang.String r0 = "com.xiaomi.mipush.CLEAR_NOTIFICATION"
            java.lang.String r1 = r12.getAction()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x03c0
            java.lang.String r0 = com.xiaomi.push.service.ap.z
            java.lang.String r0 = r12.getStringExtra(r0)
            java.lang.String r1 = com.xiaomi.push.service.ap.A
            r2 = -2
            int r1 = r12.getIntExtra(r1, r2)
            boolean r2 = android.text.TextUtils.isEmpty(r0)
            if (r2 != 0) goto L_0x0745
            r2 = -1
            if (r1 < r2) goto L_0x03af
            com.xiaomi.push.service.z.a((android.content.Context) r11, (java.lang.String) r0, (int) r1)
            goto L_0x0745
        L_0x03af:
            java.lang.String r1 = com.xiaomi.push.service.ap.E
            java.lang.String r1 = r12.getStringExtra(r1)
            java.lang.String r2 = com.xiaomi.push.service.ap.F
            java.lang.String r12 = r12.getStringExtra(r2)
            com.xiaomi.push.service.z.a((android.content.Context) r11, (java.lang.String) r0, (java.lang.String) r1, (java.lang.String) r12)
            goto L_0x0745
        L_0x03c0:
            java.lang.String r0 = "com.xiaomi.mipush.SET_NOTIFICATION_TYPE"
            java.lang.String r1 = r12.getAction()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x042f
            java.lang.String r0 = com.xiaomi.push.service.ap.z
            java.lang.String r0 = r12.getStringExtra(r0)
            java.lang.String r1 = com.xiaomi.push.service.ap.D
            java.lang.String r1 = r12.getStringExtra(r1)
            java.lang.String r2 = com.xiaomi.push.service.ap.B
            boolean r2 = r12.hasExtra(r2)
            if (r2 == 0) goto L_0x03fb
            java.lang.String r2 = com.xiaomi.push.service.ap.B
            int r12 = r12.getIntExtra(r2, r4)
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            r2.append(r0)
            r2.append(r12)
            java.lang.String r2 = r2.toString()
            java.lang.String r2 = com.xiaomi.push.ax.b(r2)
            r3 = 0
            goto L_0x0400
        L_0x03fb:
            java.lang.String r2 = com.xiaomi.push.ax.b(r0)
            r12 = 0
        L_0x0400:
            boolean r4 = android.text.TextUtils.isEmpty(r0)
            if (r4 != 0) goto L_0x0419
            boolean r1 = android.text.TextUtils.equals(r1, r2)
            if (r1 != 0) goto L_0x040d
            goto L_0x0419
        L_0x040d:
            if (r3 == 0) goto L_0x0414
            com.xiaomi.push.service.z.b((android.content.Context) r11, (java.lang.String) r0)
            goto L_0x0745
        L_0x0414:
            com.xiaomi.push.service.z.b(r11, r0, r12)
            goto L_0x0745
        L_0x0419:
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r1 = "invalid notification for "
            r12.append(r1)
            r12.append(r0)
            java.lang.String r12 = r12.toString()
        L_0x042a:
            com.xiaomi.channel.commonutils.logger.b.d(r12)
            goto L_0x0745
        L_0x042f:
            java.lang.String r0 = "com.xiaomi.mipush.DISABLE_PUSH"
            java.lang.String r1 = r12.getAction()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0467
            java.lang.String r0 = "mipush_app_package"
            java.lang.String r12 = r12.getStringExtra(r0)
            boolean r0 = android.text.TextUtils.isEmpty(r12)
            if (r0 != 0) goto L_0x044e
            com.xiaomi.push.service.m r0 = com.xiaomi.push.service.m.a((android.content.Context) r11)
            r0.b((java.lang.String) r12)
        L_0x044e:
            java.lang.String r12 = "com.xiaomi.xmsf"
            java.lang.String r0 = r11.getPackageName()
            boolean r12 = r12.equals(r0)
            if (r12 != 0) goto L_0x0745
            r12 = 19
            r11.a((int) r12, (java.lang.Exception) r2)
            r11.e()
            r11.stopSelf()
            goto L_0x0745
        L_0x0467:
            java.lang.String r0 = "com.xiaomi.mipush.DISABLE_PUSH_MESSAGE"
            java.lang.String r1 = r12.getAction()
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L_0x061c
            java.lang.String r0 = "com.xiaomi.mipush.ENABLE_PUSH_MESSAGE"
            java.lang.String r1 = r12.getAction()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0481
            goto L_0x061c
        L_0x0481:
            java.lang.String r0 = "com.xiaomi.mipush.SEND_TINYDATA"
            java.lang.String r1 = r12.getAction()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x04b0
            java.lang.String r0 = "mipush_app_package"
            java.lang.String r0 = r12.getStringExtra(r0)
            java.lang.String r1 = "mipush_payload"
            byte[] r12 = r12.getByteArrayExtra(r1)
            com.xiaomi.push.hk r1 = new com.xiaomi.push.hk
            r1.<init>()
            com.xiaomi.push.iq.a(r1, (byte[]) r12)     // Catch:{ iw -> 0x04aa }
            com.xiaomi.push.he r12 = com.xiaomi.push.he.a(r11)     // Catch:{ iw -> 0x04aa }
            r12.a((com.xiaomi.push.hk) r1, (java.lang.String) r0)     // Catch:{ iw -> 0x04aa }
            goto L_0x0745
        L_0x04aa:
            r12 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r12)
            goto L_0x0745
        L_0x04b0:
            java.lang.String r0 = "com.xiaomi.push.timer"
            java.lang.String r1 = r12.getAction()
            boolean r0 = r0.equalsIgnoreCase(r1)
            if (r0 == 0) goto L_0x04cf
            java.lang.String r12 = "Service called on timer"
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r12)
            com.xiaomi.push.ew.a((boolean) r4)
            boolean r12 = r11.e()
            if (r12 == 0) goto L_0x0745
        L_0x04ca:
            r11.b((boolean) r4)
            goto L_0x0745
        L_0x04cf:
            java.lang.String r0 = "com.xiaomi.push.check_alive"
            java.lang.String r1 = r12.getAction()
            boolean r0 = r0.equalsIgnoreCase(r1)
            if (r0 == 0) goto L_0x04e7
            java.lang.String r12 = "Service called on check alive."
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r12)
            boolean r12 = r11.e()
            if (r12 == 0) goto L_0x0745
            goto L_0x04ca
        L_0x04e7:
            java.lang.String r0 = "com.xiaomi.mipush.thirdparty"
            java.lang.String r1 = r12.getAction()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0518
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "on thirdpart push :"
            r0.append(r1)
            java.lang.String r1 = "com.xiaomi.mipush.thirdparty_DESC"
            java.lang.String r1 = r12.getStringExtra(r1)
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r0)
            java.lang.String r0 = "com.xiaomi.mipush.thirdparty_LEVEL"
            int r12 = r12.getIntExtra(r0, r4)
            com.xiaomi.push.ew.a(r11, r12)
            goto L_0x0745
        L_0x0518:
            java.lang.String r0 = "android.net.conn.CONNECTIVITY_CHANGE"
            java.lang.String r1 = r12.getAction()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0529
            r11.d()
            goto L_0x0745
        L_0x0529:
            java.lang.String r0 = "action_cr_config"
            java.lang.String r1 = r12.getAction()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x05b2
            java.lang.String r0 = "action_cr_event_switch"
            boolean r0 = r12.getBooleanExtra(r0, r4)
            java.lang.String r1 = "action_cr_event_frequency"
            r5 = 86400(0x15180, double:4.26873E-319)
            long r1 = r12.getLongExtra(r1, r5)
            java.lang.String r7 = "action_cr_perf_switch"
            boolean r4 = r12.getBooleanExtra(r7, r4)
            java.lang.String r7 = "action_cr_perf_frequency"
            long r5 = r12.getLongExtra(r7, r5)
            java.lang.String r7 = "action_cr_event_en"
            boolean r3 = r12.getBooleanExtra(r7, r3)
            java.lang.String r7 = "action_cr_max_file_size"
            r8 = 1048576(0x100000, double:5.180654E-318)
            long r7 = r12.getLongExtra(r7, r8)
            com.xiaomi.clientreport.data.Config$Builder r12 = com.xiaomi.clientreport.data.Config.getBuilder()
            com.xiaomi.clientreport.data.Config$Builder r12 = r12.setEventUploadSwitchOpen(r0)
            com.xiaomi.clientreport.data.Config$Builder r12 = r12.setEventUploadFrequency(r1)
            com.xiaomi.clientreport.data.Config$Builder r12 = r12.setPerfUploadSwitchOpen(r4)
            com.xiaomi.clientreport.data.Config$Builder r12 = r12.setPerfUploadFrequency(r5)
            android.content.Context r0 = r11.getApplicationContext()
            java.lang.String r0 = com.xiaomi.push.be.a((android.content.Context) r0)
            com.xiaomi.clientreport.data.Config$Builder r12 = r12.setAESKey(r0)
            com.xiaomi.clientreport.data.Config$Builder r12 = r12.setEventEncrypted(r3)
            com.xiaomi.clientreport.data.Config$Builder r12 = r12.setMaxFileLength(r7)
            android.content.Context r0 = r11.getApplicationContext()
            com.xiaomi.clientreport.data.Config r12 = r12.build(r0)
            java.lang.String r0 = "com.xiaomi.xmsf"
            java.lang.String r3 = r11.getPackageName()
            boolean r0 = r0.equals(r3)
            if (r0 != 0) goto L_0x0745
            r3 = 0
            int r0 = (r1 > r3 ? 1 : (r1 == r3 ? 0 : -1))
            if (r0 <= 0) goto L_0x0745
            int r0 = (r5 > r3 ? 1 : (r5 == r3 ? 0 : -1))
            if (r0 <= 0) goto L_0x0745
            int r0 = (r7 > r3 ? 1 : (r7 == r3 ? 0 : -1))
            if (r0 <= 0) goto L_0x0745
            android.content.Context r0 = r11.getApplicationContext()
            com.xiaomi.push.eu.a((android.content.Context) r0, (com.xiaomi.clientreport.data.Config) r12)
            goto L_0x0745
        L_0x05b2:
            java.lang.String r0 = "action_help_ping"
            java.lang.String r1 = r12.getAction()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x060b
            java.lang.String r0 = "extra_help_ping_switch"
            boolean r0 = r12.getBooleanExtra(r0, r4)
            java.lang.String r1 = "extra_help_ping_frequency"
            int r1 = r12.getIntExtra(r1, r4)
            r2 = 30
            if (r1 < 0) goto L_0x05d7
            if (r1 >= r2) goto L_0x05d7
            java.lang.String r1 = "aw_ping: frquency need > 30s."
            com.xiaomi.channel.commonutils.logger.b.c(r1)
            r1 = 30
        L_0x05d7:
            if (r1 >= 0) goto L_0x05da
            r0 = 0
        L_0x05da:
            java.lang.StringBuilder r2 = new java.lang.StringBuilder
            r2.<init>()
            java.lang.String r3 = "aw_ping: receive a aw_ping message. switch: "
            r2.append(r3)
            r2.append(r0)
            java.lang.String r3 = " frequency: "
            r2.append(r3)
            r2.append(r1)
            java.lang.String r2 = r2.toString()
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r2)
            if (r0 == 0) goto L_0x0745
            if (r1 <= 0) goto L_0x0745
            java.lang.String r0 = "com.xiaomi.xmsf"
            java.lang.String r2 = r11.getPackageName()
            boolean r0 = r0.equals(r2)
            if (r0 != 0) goto L_0x0745
            r11.a((android.content.Intent) r12, (int) r1)
            goto L_0x0745
        L_0x060b:
            java.lang.String r0 = "action_aw_app_logic"
            java.lang.String r1 = r12.getAction()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0745
            r11.d(r12)
            goto L_0x0745
        L_0x061c:
            java.lang.String r0 = "mipush_app_package"
            java.lang.String r3 = r12.getStringExtra(r0)
            java.lang.String r0 = "mipush_payload"
            byte[] r6 = r12.getByteArrayExtra(r0)
            java.lang.String r0 = "mipush_app_id"
            java.lang.String r4 = r12.getStringExtra(r0)
            java.lang.String r0 = "mipush_app_token"
            java.lang.String r5 = r12.getStringExtra(r0)
            java.lang.String r0 = "com.xiaomi.mipush.DISABLE_PUSH_MESSAGE"
            java.lang.String r1 = r12.getAction()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0647
            com.xiaomi.push.service.m r0 = com.xiaomi.push.service.m.a((android.content.Context) r11)
            r0.c((java.lang.String) r3)
        L_0x0647:
            java.lang.String r0 = "com.xiaomi.mipush.ENABLE_PUSH_MESSAGE"
            java.lang.String r1 = r12.getAction()
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0661
            com.xiaomi.push.service.m r0 = com.xiaomi.push.service.m.a((android.content.Context) r11)
            r0.e(r3)
            com.xiaomi.push.service.m r0 = com.xiaomi.push.service.m.a((android.content.Context) r11)
            r0.f(r3)
        L_0x0661:
            if (r6 != 0) goto L_0x066d
            r12 = 70000003(0x42c1d83, float:2.0232054E-36)
            java.lang.String r0 = "null payload"
            com.xiaomi.push.service.o.a(r11, r3, r6, r12, r0)
            goto L_0x0745
        L_0x066d:
            com.xiaomi.push.service.o.b(r3, r6)
            com.xiaomi.push.service.n r0 = new com.xiaomi.push.service.n
            r1 = r0
            r2 = r11
            r1.<init>(r2, r3, r4, r5, r6)
            r11.a((com.xiaomi.push.service.XMPushService.i) r0)
            java.lang.String r0 = "com.xiaomi.mipush.ENABLE_PUSH_MESSAGE"
            java.lang.String r12 = r12.getAction()
            boolean r12 = r0.equals(r12)
            if (r12 == 0) goto L_0x0745
            com.xiaomi.push.service.XMPushService$e r12 = r11.f812a
            if (r12 != 0) goto L_0x0745
            com.xiaomi.push.service.XMPushService$e r12 = new com.xiaomi.push.service.XMPushService$e
            r12.<init>()
            r11.f812a = r12
            android.content.IntentFilter r12 = new android.content.IntentFilter
            java.lang.String r0 = "android.net.conn.CONNECTIVITY_CHANGE"
            r12.<init>(r0)
            com.xiaomi.push.service.XMPushService$e r0 = r11.f812a
            r11.registerReceiver(r0, r12)
            goto L_0x0745
        L_0x069f:
            java.lang.String r0 = "mipush_app_package"
            java.lang.String r0 = r12.getStringExtra(r0)
            java.lang.String r1 = "mipush_payload"
            byte[] r1 = r12.getByteArrayExtra(r1)
            java.lang.String r2 = "com.xiaomi.mipush.MESSAGE_CACHE"
            boolean r2 = r12.getBooleanExtra(r2, r3)
            java.lang.String r3 = "com.xiaomi.mipush.UNREGISTER_APP"
            java.lang.String r12 = r12.getAction()
            boolean r12 = r3.equals(r12)
            if (r12 == 0) goto L_0x06c4
            com.xiaomi.push.service.m r12 = com.xiaomi.push.service.m.a((android.content.Context) r11)
            r12.a((java.lang.String) r0)
        L_0x06c4:
            r11.a((java.lang.String) r0, (byte[]) r1, (boolean) r2)
            goto L_0x0745
        L_0x06c9:
            java.lang.String r0 = com.xiaomi.push.service.ap.r
            java.lang.String r0 = r12.getStringExtra(r0)
            java.lang.String r1 = com.xiaomi.push.service.ap.v
            java.lang.String r1 = r12.getStringExtra(r1)
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 == 0) goto L_0x06de
            java.lang.String r12 = "security is empty. ignore."
            goto L_0x072e
        L_0x06de:
            if (r0 == 0) goto L_0x0741
            boolean r1 = r11.a((java.lang.String) r0, (android.content.Intent) r12)
            com.xiaomi.push.service.al$b r7 = r11.a((java.lang.String) r0, (android.content.Intent) r12)
            boolean r12 = com.xiaomi.push.as.b(r11)
            if (r12 != 0) goto L_0x06f8
            com.xiaomi.push.service.d r5 = r11.f815a
            r8 = 0
            r9 = 2
        L_0x06f2:
            r10 = 0
            r6 = r11
            r5.a(r6, r7, r8, r9, r10)
            goto L_0x0745
        L_0x06f8:
            boolean r12 = r11.c()
            if (r12 == 0) goto L_0x073d
            com.xiaomi.push.service.al$c r12 = r7.f854a
            com.xiaomi.push.service.al$c r0 = com.xiaomi.push.service.al.c.unbind
            if (r12 != r0) goto L_0x070b
            com.xiaomi.push.service.XMPushService$a r12 = new com.xiaomi.push.service.XMPushService$a
            r12.<init>(r7)
            goto L_0x019d
        L_0x070b:
            if (r1 == 0) goto L_0x0714
            com.xiaomi.push.service.XMPushService$n r12 = new com.xiaomi.push.service.XMPushService$n
            r12.<init>(r7)
            goto L_0x019d
        L_0x0714:
            com.xiaomi.push.service.al$c r12 = r7.f854a
            com.xiaomi.push.service.al$c r0 = com.xiaomi.push.service.al.c.binding
            if (r12 != r0) goto L_0x0732
            java.lang.String r12 = "the client is binding. %1$s %2$s."
            java.lang.Object[] r0 = new java.lang.Object[r2]
            java.lang.String r1 = r7.g
            r0[r4] = r1
            java.lang.String r1 = r7.f859b
            java.lang.String r1 = com.xiaomi.push.service.al.b.a((java.lang.String) r1)
            r0[r3] = r1
            java.lang.String r12 = java.lang.String.format(r12, r0)
        L_0x072e:
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r12)
            goto L_0x0745
        L_0x0732:
            com.xiaomi.push.service.al$c r12 = r7.f854a
            com.xiaomi.push.service.al$c r0 = com.xiaomi.push.service.al.c.binded
            if (r12 != r0) goto L_0x0745
            com.xiaomi.push.service.d r5 = r11.f815a
            r8 = 1
            r9 = 0
            goto L_0x06f2
        L_0x073d:
            r11.a((boolean) r3)
            goto L_0x0745
        L_0x0741:
            java.lang.String r12 = "channel id is empty, do nothing!"
            goto L_0x042a
        L_0x0745:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.XMPushService.c(android.content.Intent):void");
    }

    private void c(i iVar) {
        this.f816a.a((g.b) iVar);
    }

    private void c(boolean z) {
        try {
            if (!t.a()) {
                return;
            }
            if (z) {
                sendBroadcast(new Intent("miui.intent.action.NETWORK_CONNECTED"));
                for (ae a2 : (ae[]) this.f820a.toArray(new ae[0])) {
                    a2.a();
                }
                return;
            }
            sendBroadcast(new Intent("miui.intent.action.NETWORK_BLOCKED"));
        } catch (Exception e2) {
            com.xiaomi.channel.commonutils.logger.b.a((Throwable) e2);
        }
    }

    private void d() {
        NetworkInfo networkInfo;
        try {
            networkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        } catch (Exception e2) {
            com.xiaomi.channel.commonutils.logger.b.a((Throwable) e2);
            networkInfo = null;
        }
        if (networkInfo != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("network changed,");
            sb.append(Operators.ARRAY_START_STR + "type: " + networkInfo.getTypeName() + Operators.ARRAY_START_STR + networkInfo.getSubtypeName() + "], state: " + networkInfo.getState() + "/" + networkInfo.getDetailedState());
            com.xiaomi.channel.commonutils.logger.b.a(sb.toString());
            NetworkInfo.State state = networkInfo.getState();
            if (state == NetworkInfo.State.SUSPENDED || state == NetworkInfo.State.UNKNOWN) {
                return;
            }
        } else {
            com.xiaomi.channel.commonutils.logger.b.a("network changed, no active network");
        }
        if (gy.a() != null) {
            gy.a().a();
        }
        gr.a((Context) this);
        this.f808a.d();
        if (as.b(this)) {
            if (c() && e()) {
                b(false);
            }
            if (!c() && !d()) {
                this.f816a.a(1);
                a((i) new d());
            }
            dd.a((Context) this).a();
        } else {
            a((i) new f(2, (Exception) null));
        }
        e();
    }

    private void d(Intent intent) {
        int i2;
        try {
            ek.a(getApplicationContext()).a((eo) new ar());
            String stringExtra = intent.getStringExtra("mipush_app_package");
            byte[] byteArrayExtra = intent.getByteArrayExtra("mipush_payload");
            if (byteArrayExtra != null) {
                Cif ifVar = new Cif();
                iq.a(ifVar, byteArrayExtra);
                String b2 = ifVar.b();
                Map a2 = ifVar.a();
                if (a2 != null) {
                    String str = (String) a2.get("extra_help_aw_info");
                    String str2 = (String) a2.get("extra_aw_app_online_cmd");
                    if (!TextUtils.isEmpty(str2)) {
                        try {
                            i2 = Integer.parseInt(str2);
                        } catch (NumberFormatException unused) {
                            i2 = 0;
                        }
                        if (!TextUtils.isEmpty(stringExtra) && !TextUtils.isEmpty(b2) && !TextUtils.isEmpty(str)) {
                            ek.a(getApplicationContext()).a(this, str, i2, stringExtra, b2);
                        }
                    }
                }
            }
        } catch (iw e2) {
            com.xiaomi.channel.commonutils.logger.b.d("aw_logic: translate fail. " + e2.getMessage());
        }
    }

    private void e() {
        if (!a()) {
            ew.a();
        } else if (!ew.a()) {
            ew.a(true);
        }
    }

    /* renamed from: e  reason: collision with other method in class */
    private boolean m541e() {
        if (System.currentTimeMillis() - this.f805a < 30000) {
            return false;
        }
        return as.c(this);
    }

    /* access modifiers changed from: private */
    public void f() {
        String str;
        if (this.f809a != null && this.f809a.b()) {
            str = "try to connect while connecting.";
        } else if (this.f809a == null || !this.f809a.c()) {
            this.f810a.b(as.a((Context) this));
            g();
            if (this.f809a == null) {
                al.a().a((Context) this);
                c(false);
                return;
            }
            return;
        } else {
            str = "try to connect while is connected.";
        }
        com.xiaomi.channel.commonutils.logger.b.d(str);
    }

    /* renamed from: f  reason: collision with other method in class */
    private boolean m542f() {
        return "com.xiaomi.xmsf".equals(getPackageName()) && Settings.Secure.getInt(getContentResolver(), "EXTREME_POWER_MODE_ENABLE", 0) == 1;
    }

    private void g() {
        try {
            this.f808a.a(this.f811a, (fz) new bj(this));
            this.f808a.e();
            this.f809a = this.f808a;
        } catch (fx e2) {
            com.xiaomi.channel.commonutils.logger.b.a("fail to create Slim connection", (Throwable) e2);
            this.f808a.b(3, e2);
        }
    }

    /* renamed from: g  reason: collision with other method in class */
    private boolean m543g() {
        return "com.xiaomi.xmsf".equals(getPackageName()) || !m.a((Context) this).b(getPackageName());
    }

    private void h() {
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(b, new Notification());
        } else {
            bindService(new Intent(this, this.f817a), new bk(this), 1);
        }
    }

    /* renamed from: h  reason: collision with other method in class */
    private boolean m544h() {
        if (TextUtils.equals(getPackageName(), "com.xiaomi.xmsf")) {
            return false;
        }
        return ag.a((Context) this).a(hl.ForegroundServiceSwitch.a(), false);
    }

    private void i() {
        synchronized (this.f819a) {
            this.f819a.clear();
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public fm m545a() {
        return this.f809a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public d m546a() {
        return new d();
    }

    /* access modifiers changed from: package-private */
    /* renamed from: a  reason: collision with other method in class */
    public void m547a() {
        if (System.currentTimeMillis() - this.f805a >= ((long) fs.a()) && as.c(this)) {
            b(true);
        }
    }

    public void a(int i2) {
        this.f816a.a(i2);
    }

    public void a(int i2, Exception exc) {
        StringBuilder sb = new StringBuilder();
        sb.append("disconnect ");
        sb.append(hashCode());
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append(this.f809a == null ? null : Integer.valueOf(this.f809a.hashCode()));
        com.xiaomi.channel.commonutils.logger.b.a(sb.toString());
        if (this.f809a != null) {
            this.f809a.b(i2, exc);
            this.f809a = null;
        }
        a(7);
        a(4);
        al.a().a((Context) this, i2);
    }

    public void a(ff ffVar) {
        if (this.f809a != null) {
            this.f809a.b(ffVar);
            return;
        }
        throw new fx("try send msg while connection is null.");
    }

    public void a(fm fmVar) {
        gy.a().a(fmVar);
        c(true);
        this.f814a.a();
        Iterator it = al.a().a().iterator();
        while (it.hasNext()) {
            a((i) new a((al.b) it.next()));
        }
    }

    public void a(fm fmVar, int i2, Exception exc) {
        gy.a().a(fmVar, i2, exc);
        a(false);
    }

    public void a(fm fmVar, Exception exc) {
        gy.a().a(fmVar, exc);
        c(false);
        a(false);
    }

    public void a(i iVar) {
        a(iVar, 0);
    }

    public void a(i iVar, long j2) {
        try {
            this.f816a.a((g.b) iVar, j2);
        } catch (IllegalStateException e2) {
            com.xiaomi.channel.commonutils.logger.b.a("can't execute job err = " + e2.getMessage());
        }
    }

    public void a(l lVar) {
        synchronized (this.f819a) {
            this.f819a.add(lVar);
        }
    }

    public void a(al.b bVar) {
        if (bVar != null) {
            long a2 = bVar.a();
            com.xiaomi.channel.commonutils.logger.b.a("schedule rebind job in " + (a2 / 1000));
            a((i) new a(bVar), a2);
        }
    }

    public void a(String str, String str2, int i2, String str3, String str4) {
        al.b a2 = al.a().a(str, str2);
        if (a2 != null) {
            a((i) new p(a2, i2, str4, str3));
        }
        al.a().a(str, str2);
    }

    /* access modifiers changed from: package-private */
    public void a(String str, byte[] bArr, boolean z) {
        Collection a2 = al.a().a("5");
        if (a2.isEmpty()) {
            if (!z) {
                return;
            }
        } else if (((al.b) a2.iterator().next()).f854a == al.c.binded) {
            a((i) new bs(this, 4, str, bArr));
            return;
        } else if (!z) {
            return;
        }
        o.b(str, bArr);
    }

    public void a(boolean z) {
        this.f814a.a(z);
    }

    public void a(byte[] bArr, String str) {
        if (bArr == null) {
            o.a(this, str, bArr, ErrorCode.ERROR_INVALID_PAYLOAD, "null payload");
            com.xiaomi.channel.commonutils.logger.b.a("register request without payload");
            return;
        }
        ic icVar = new ic();
        try {
            iq.a(icVar, bArr);
            if (icVar.f606a == hg.Registration) {
                ig igVar = new ig();
                try {
                    iq.a(igVar, icVar.a());
                    o.a(icVar.b(), bArr);
                    a((i) new n(this, icVar.b(), igVar.b(), igVar.c(), bArr));
                    ev.a(getApplicationContext()).a(icVar.b(), "E100003", igVar.a(), 6002, "send a register message to server");
                } catch (iw e2) {
                    com.xiaomi.channel.commonutils.logger.b.a((Throwable) e2);
                    o.a(this, str, bArr, ErrorCode.ERROR_INVALID_PAYLOAD, " data action error.");
                }
            } else {
                o.a(this, str, bArr, ErrorCode.ERROR_INVALID_PAYLOAD, " registration action required.");
                com.xiaomi.channel.commonutils.logger.b.a("register request with invalid payload");
            }
        } catch (iw e3) {
            com.xiaomi.channel.commonutils.logger.b.a((Throwable) e3);
            o.a(this, str, bArr, ErrorCode.ERROR_INVALID_PAYLOAD, " data container error.");
        }
    }

    public void a(ff[] ffVarArr) {
        if (this.f809a != null) {
            this.f809a.a(ffVarArr);
            return;
        }
        throw new fx("try send msg while connection is null.");
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m548a() {
        return as.b(this) && al.a().a() > 0 && !b() && g() && !f();
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m549a(int i2) {
        return this.f816a.a(i2);
    }

    public d b() {
        return this.f815a;
    }

    /* access modifiers changed from: package-private */
    /* renamed from: b  reason: collision with other method in class */
    public void m550b() {
        Iterator it = new ArrayList(this.f819a).iterator();
        while (it.hasNext()) {
            ((l) it.next()).a();
        }
    }

    public void b(fm fmVar) {
        com.xiaomi.channel.commonutils.logger.b.c("begin to connect...");
        gy.a().b(fmVar);
    }

    public void b(i iVar) {
        this.f816a.a(iVar.a, (g.b) iVar);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m551b() {
        try {
            Class<?> cls = Class.forName("miui.os.Build");
            return cls.getField("IS_CM_CUSTOMIZATION_TEST").getBoolean((Object) null) || cls.getField("IS_CU_CUSTOMIZATION_TEST").getBoolean((Object) null) || cls.getField("IS_CT_CUSTOMIZATION_TEST").getBoolean((Object) null);
        } catch (Throwable unused) {
            return false;
        }
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m552c() {
        return this.f809a != null && this.f809a.c();
    }

    /* renamed from: d  reason: collision with other method in class */
    public boolean m553d() {
        return this.f809a != null && this.f809a.b();
    }

    public IBinder onBind(Intent intent) {
        return this.f807a.getBinder();
    }

    public void onCreate() {
        Uri uriFor;
        super.onCreate();
        t.a((Context) this);
        k a2 = l.a((Context) this);
        if (a2 != null) {
            ab.a(a2.a);
        }
        this.f807a = new Messenger(new bl(this));
        aq.a(this);
        this.f810a = new bm(this, (Map) null, 5222, "xiaomi.com", (fq) null);
        this.f810a.a(true);
        this.f808a = new fk(this, this.f810a);
        this.f815a = a();
        ew.a((Context) this);
        this.f808a.a((fp) this);
        this.f813a = new ak(this);
        this.f814a = new av(this);
        new e().a();
        gy.a().a(this);
        this.f816a = new g("Connection Controller Thread");
        al a3 = al.a();
        a3.b();
        a3.a((al.a) new bn(this));
        if (h()) {
            h();
        }
        he.a(this).a((hf) new i(this), "UPLOADER_PUSH_CHANNEL");
        a((l) new hb(this));
        a((i) new g());
        this.f820a.add(bc.a((Context) this));
        if (g()) {
            this.f812a = new e();
            registerReceiver(this.f812a, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
        if ("com.xiaomi.xmsf".equals(getPackageName()) && (uriFor = Settings.Secure.getUriFor("EXTREME_POWER_MODE_ENABLE")) != null) {
            this.f806a = new bo(this, new Handler(Looper.getMainLooper()));
            try {
                getContentResolver().registerContentObserver(uriFor, false, this.f806a);
            } catch (Throwable th) {
                com.xiaomi.channel.commonutils.logger.b.a("register observer err:" + th.getMessage());
            }
        }
        com.xiaomi.channel.commonutils.logger.b.a("XMPushService created pid = " + b);
    }

    public void onDestroy() {
        if (this.f812a != null) {
            a((BroadcastReceiver) this.f812a);
            this.f812a = null;
        }
        if ("com.xiaomi.xmsf".equals(getPackageName()) && this.f806a != null) {
            try {
                getContentResolver().unregisterContentObserver(this.f806a);
            } catch (Throwable th) {
                com.xiaomi.channel.commonutils.logger.b.a("unregister observer err:" + th.getMessage());
            }
        }
        this.f820a.clear();
        this.f816a.b();
        a((i) new bi(this, 2));
        a((i) new j());
        al.a().b();
        al.a().a((Context) this, 15);
        al.a().a();
        this.f808a.b((fp) this);
        ba.a().a();
        ew.a();
        i();
        super.onDestroy();
        com.xiaomi.channel.commonutils.logger.b.a("Service destroyed");
    }

    public void onStart(Intent intent, int i2) {
        h hVar;
        long currentTimeMillis = System.currentTimeMillis();
        if (intent == null) {
            com.xiaomi.channel.commonutils.logger.b.d("onStart() with intent NULL");
        } else {
            com.xiaomi.channel.commonutils.logger.b.c(String.format("onStart() with intent.Action = %s, chid = %s, pkg = %s|%s, from-bridge = %s", new Object[]{intent.getAction(), intent.getStringExtra(ap.r), intent.getStringExtra(ap.z), intent.getStringExtra("mipush_app_package"), intent.getStringExtra("ext_is_xmpushservice_bridge")}));
        }
        if (!(intent == null || intent.getAction() == null)) {
            if ("com.xiaomi.push.timer".equalsIgnoreCase(intent.getAction()) || "com.xiaomi.push.check_alive".equalsIgnoreCase(intent.getAction())) {
                if (this.f816a.a()) {
                    com.xiaomi.channel.commonutils.logger.b.d("ERROR, the job controller is blocked.");
                    al.a().a((Context) this, 14);
                    stopSelf();
                } else {
                    hVar = new h(intent);
                }
            } else if (!"com.xiaomi.push.network_status_changed".equalsIgnoreCase(intent.getAction())) {
                hVar = new h(intent);
            }
            a((i) hVar);
        }
        long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
        if (currentTimeMillis2 > 50) {
            com.xiaomi.channel.commonutils.logger.b.c("[Prefs] spend " + currentTimeMillis2 + " ms, too more times.");
        }
    }

    public int onStartCommand(Intent intent, int i2, int i3) {
        onStart(intent, i3);
        return a;
    }
}
