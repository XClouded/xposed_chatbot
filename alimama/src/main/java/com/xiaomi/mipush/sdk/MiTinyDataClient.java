package com.xiaomi.mipush.sdk;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.text.TextUtils;
import com.taobao.weex.el.parse.Operators;
import com.xiaomi.channel.commonutils.logger.b;
import com.xiaomi.push.Cif;
import com.xiaomi.push.hg;
import com.xiaomi.push.hk;
import com.xiaomi.push.ht;
import com.xiaomi.push.service.aj;
import com.xiaomi.push.service.be;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MiTinyDataClient {
    public static final String PENDING_REASON_APPID = "com.xiaomi.xmpushsdk.tinydataPending.appId";
    public static final String PENDING_REASON_CHANNEL = "com.xiaomi.xmpushsdk.tinydataPending.channel";
    public static final String PENDING_REASON_INIT = "com.xiaomi.xmpushsdk.tinydataPending.init";

    public static class a {
        private static a a;
        /* access modifiers changed from: private */

        /* renamed from: a  reason: collision with other field name */
        public Context f18a;

        /* renamed from: a  reason: collision with other field name */
        private C0031a f19a = new C0031a();

        /* renamed from: a  reason: collision with other field name */
        private Boolean f20a;

        /* renamed from: a  reason: collision with other field name */
        private String f21a;

        /* renamed from: a  reason: collision with other field name */
        private final ArrayList<hk> f22a = new ArrayList<>();

        /* renamed from: com.xiaomi.mipush.sdk.MiTinyDataClient$a$a  reason: collision with other inner class name */
        public class C0031a {

            /* renamed from: a  reason: collision with other field name */
            private final Runnable f23a = new am(this);

            /* renamed from: a  reason: collision with other field name */
            public final ArrayList<hk> f24a = new ArrayList<>();
            /* access modifiers changed from: private */

            /* renamed from: a  reason: collision with other field name */
            public ScheduledFuture<?> f25a;

            /* renamed from: a  reason: collision with other field name */
            private ScheduledThreadPoolExecutor f26a = new ScheduledThreadPoolExecutor(1);

            public C0031a() {
            }

            private void a() {
                if (this.f25a == null) {
                    this.f25a = this.f26a.scheduleAtFixedRate(this.f23a, 1000, 1000, TimeUnit.MILLISECONDS);
                }
            }

            /* access modifiers changed from: private */
            public void b() {
                hk remove = this.f24a.remove(0);
                for (Cif a2 : be.a(Arrays.asList(new hk[]{remove}), a.this.f18a.getPackageName(), d.a(a.this.f18a).a(), 30720)) {
                    b.c("MiTinyDataClient Send item by PushServiceClient.sendMessage(XmActionNotification)." + remove.d());
                    ay.a(a.this.f18a).a(a2, hg.Notification, true, (ht) null);
                }
            }

            public void a(hk hkVar) {
                this.f26a.execute(new al(this, hkVar));
            }
        }

        public static a a() {
            if (a == null) {
                synchronized (a.class) {
                    if (a == null) {
                        a = new a();
                    }
                }
            }
            return a;
        }

        private void a(hk hkVar) {
            synchronized (this.f22a) {
                if (!this.f22a.contains(hkVar)) {
                    this.f22a.add(hkVar);
                    if (this.f22a.size() > 100) {
                        this.f22a.remove(0);
                    }
                }
            }
        }

        private boolean a(Context context) {
            if (!ay.a(context).a()) {
                return true;
            }
            try {
                PackageInfo packageInfo = context.getPackageManager().getPackageInfo("com.xiaomi.xmsf", 4);
                return packageInfo != null && packageInfo.versionCode >= 108;
            } catch (Exception unused) {
                return false;
            }
        }

        private boolean b(Context context) {
            return d.a(context).a() == null && !a(this.f18a);
        }

        private boolean b(hk hkVar) {
            if (be.a(hkVar, false)) {
                return false;
            }
            if (this.f20a.booleanValue()) {
                b.c("MiTinyDataClient Send item by PushServiceClient.sendTinyData(ClientUploadDataItem)." + hkVar.d());
                ay.a(this.f18a).a(hkVar);
                return true;
            }
            this.f19a.a(hkVar);
            return true;
        }

        /* renamed from: a  reason: collision with other method in class */
        public void m28a(Context context) {
            if (context == null) {
                b.a("context is null, MiTinyDataClientImp.init() failed.");
                return;
            }
            this.f18a = context;
            this.f20a = Boolean.valueOf(a(context));
            b(MiTinyDataClient.PENDING_REASON_INIT);
        }

        public synchronized void a(String str) {
            if (TextUtils.isEmpty(str)) {
                b.a("channel is null, MiTinyDataClientImp.setChannel(String) failed.");
                return;
            }
            this.f21a = str;
            b(MiTinyDataClient.PENDING_REASON_CHANNEL);
        }

        /* renamed from: a  reason: collision with other method in class */
        public boolean m29a() {
            return this.f18a != null;
        }

        /* renamed from: a  reason: collision with other method in class */
        public synchronized boolean m30a(hk hkVar) {
            String str;
            boolean z = false;
            if (hkVar == null) {
                return false;
            }
            if (be.a(hkVar, true)) {
                return false;
            }
            boolean z2 = TextUtils.isEmpty(hkVar.a()) && TextUtils.isEmpty(this.f21a);
            boolean z3 = !a();
            if (this.f18a == null || b(this.f18a)) {
                z = true;
            }
            if (!z3 && !z2) {
                if (!z) {
                    b.c("MiTinyDataClient Send item immediately." + hkVar.d());
                    if (TextUtils.isEmpty(hkVar.d())) {
                        hkVar.f(aj.a());
                    }
                    if (TextUtils.isEmpty(hkVar.a())) {
                        hkVar.a(this.f21a);
                    }
                    if (TextUtils.isEmpty(hkVar.c())) {
                        hkVar.e(this.f18a.getPackageName());
                    }
                    if (hkVar.a() <= 0) {
                        hkVar.b(System.currentTimeMillis());
                    }
                    return b(hkVar);
                }
            }
            if (z2) {
                str = "MiTinyDataClient Pending " + hkVar.b() + " reason is " + MiTinyDataClient.PENDING_REASON_CHANNEL;
            } else if (z3) {
                str = "MiTinyDataClient Pending " + hkVar.b() + " reason is " + MiTinyDataClient.PENDING_REASON_INIT;
            } else {
                if (z) {
                    str = "MiTinyDataClient Pending " + hkVar.b() + " reason is " + MiTinyDataClient.PENDING_REASON_APPID;
                }
                a(hkVar);
                return true;
            }
            b.c(str);
            a(hkVar);
            return true;
        }

        public void b(String str) {
            b.c("MiTinyDataClient.processPendingList(" + str + Operators.BRACKET_END_STR);
            ArrayList arrayList = new ArrayList();
            synchronized (this.f22a) {
                arrayList.addAll(this.f22a);
                this.f22a.clear();
            }
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                a((hk) it.next());
            }
        }
    }

    public static void init(Context context, String str) {
        if (context == null) {
            b.a("context is null, MiTinyDataClient.init(Context, String) failed.");
            return;
        }
        a.a().a(context);
        if (TextUtils.isEmpty(str)) {
            b.a("channel is null or empty, MiTinyDataClient.init(Context, String) failed.");
        } else {
            a.a().a(str);
        }
    }

    public static boolean upload(Context context, hk hkVar) {
        b.c("MiTinyDataClient.upload " + hkVar.d());
        if (!a.a().a()) {
            a.a().a(context);
        }
        return a.a().a(hkVar);
    }

    public static boolean upload(Context context, String str, String str2, long j, String str3) {
        hk hkVar = new hk();
        hkVar.d(str);
        hkVar.c(str2);
        hkVar.a(j);
        hkVar.b(str3);
        hkVar.a(true);
        hkVar.a("push_sdk_channel");
        return upload(context, hkVar);
    }

    public static boolean upload(String str, String str2, long j, String str3) {
        hk hkVar = new hk();
        hkVar.d(str);
        hkVar.c(str2);
        hkVar.a(j);
        hkVar.b(str3);
        return a.a().a(hkVar);
    }
}
