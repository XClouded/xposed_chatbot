package com.vivo.push;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.SparseArray;
import com.alibaba.ut.abtest.pipeline.StatusCode;
import com.taobao.android.dinamicx.DXError;
import com.vivo.push.b.ab;
import com.vivo.push.b.ac;
import com.vivo.push.b.ad;
import com.vivo.push.b.b;
import com.vivo.push.b.c;
import com.vivo.push.b.d;
import com.vivo.push.b.f;
import com.vivo.push.b.g;
import com.vivo.push.b.h;
import com.vivo.push.b.i;
import com.vivo.push.b.y;
import com.vivo.push.sdk.PushMessageCallback;
import com.vivo.push.util.VivoPushException;
import com.vivo.push.util.s;
import com.vivo.push.util.w;
import com.vivo.push.util.z;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: PushClientManager */
public final class p {
    private static final Object a = new Object();
    private static volatile p b;
    private long c = -1;
    private long d = -1;
    private long e = -1;
    private long f = -1;
    private long g = -1;
    private long h = -1;
    private Context i;
    private boolean j = true;
    /* access modifiers changed from: private */
    public com.vivo.push.util.a k;
    /* access modifiers changed from: private */
    public String l;
    private String m;
    private SparseArray<a> n = new SparseArray<>();
    private int o = 0;
    private Boolean p;
    private Long q;
    private boolean r;
    private IPushClientFactory s = new o();
    private int t;

    private p() {
    }

    public static p a() {
        if (b == null) {
            synchronized (a) {
                if (b == null) {
                    b = new p();
                }
            }
        }
        return b;
    }

    /* access modifiers changed from: protected */
    public final void b() throws VivoPushException {
        if (this.i != null) {
            z.c(this.i);
        }
    }

    public final synchronized void a(Context context) {
        if (this.i == null) {
            this.i = context.getApplicationContext();
            this.r = s.b(context, context.getPackageName());
            w.b().a(this.i);
            a((y) new h());
            this.k = new com.vivo.push.util.a();
            this.k.a(context, "com.vivo.push_preferences.appconfig_v1");
            this.l = f();
            this.m = this.k.a("APP_ALIAS");
        }
    }

    public final List<String> c() {
        String a2 = this.k.a("APP_TAGS");
        ArrayList arrayList = new ArrayList();
        try {
            if (TextUtils.isEmpty(a2)) {
                return arrayList;
            }
            Iterator<String> keys = new JSONObject(a2).keys();
            while (keys.hasNext()) {
                arrayList.add(keys.next());
            }
            return arrayList;
        } catch (JSONException unused) {
            this.k.c("APP_TAGS");
            arrayList.clear();
            com.vivo.push.util.p.d("PushClientManager", "getTags error");
        }
    }

    public final void a(List<String> list) {
        JSONObject jSONObject;
        try {
            if (list.size() > 0) {
                String a2 = this.k.a("APP_TAGS");
                if (TextUtils.isEmpty(a2)) {
                    jSONObject = new JSONObject();
                } else {
                    jSONObject = new JSONObject(a2);
                }
                for (String put : list) {
                    jSONObject.put(put, System.currentTimeMillis());
                }
                String jSONObject2 = jSONObject.toString();
                if (TextUtils.isEmpty(jSONObject2)) {
                    this.k.c("APP_TAGS");
                } else {
                    this.k.a("APP_TAGS", jSONObject2);
                }
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
            this.k.c("APP_TAGS");
        }
    }

    public final void b(List<String> list) {
        JSONObject jSONObject;
        try {
            if (list.size() > 0) {
                String a2 = this.k.a("APP_TAGS");
                if (TextUtils.isEmpty(a2)) {
                    jSONObject = new JSONObject();
                } else {
                    jSONObject = new JSONObject(a2);
                }
                for (String remove : list) {
                    jSONObject.remove(remove);
                }
                String jSONObject2 = jSONObject.toString();
                if (TextUtils.isEmpty(jSONObject2)) {
                    this.k.c("APP_TAGS");
                } else {
                    this.k.a("APP_TAGS", jSONObject2);
                }
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
            this.k.c("APP_TAGS");
        }
    }

    public final boolean d() {
        if (this.i == null) {
            com.vivo.push.util.p.d("PushClientManager", "support:context is null");
            return false;
        }
        this.p = Boolean.valueOf(v());
        return this.p.booleanValue();
    }

    public final void c(List<String> list) {
        if (list.contains(this.m)) {
            t();
        }
    }

    /* access modifiers changed from: private */
    public void t() {
        this.m = null;
        this.k.c("APP_ALIAS");
    }

    public final boolean e() {
        return this.r;
    }

    /* access modifiers changed from: package-private */
    public final String f() {
        String a2 = this.k.a("APP_TOKEN");
        if (TextUtils.isEmpty(a2) || !z.a(this.i, this.i.getPackageName(), a2)) {
            return a2;
        }
        this.k.a();
        return null;
    }

    public final void a(String str) {
        this.l = str;
        this.k.a("APP_TOKEN", this.l);
    }

    /* access modifiers changed from: protected */
    public final void a(boolean z) {
        this.j = z;
    }

    public final boolean g() {
        return this.j;
    }

    public final Context h() {
        return this.i;
    }

    /* access modifiers changed from: package-private */
    public final void i() {
        a((y) new f());
    }

    /* access modifiers changed from: package-private */
    public final void j() {
        a((y) new ac());
    }

    /* access modifiers changed from: package-private */
    public final void a(IPushActionListener iPushActionListener) {
        if (this.i != null) {
            this.l = f();
            if (!TextUtils.isEmpty(this.l)) {
                if (iPushActionListener != null) {
                    iPushActionListener.onStateChanged(0);
                }
            } else if (a(this.c)) {
                this.c = SystemClock.elapsedRealtime();
                String packageName = this.i.getPackageName();
                a aVar = null;
                if (this.i != null) {
                    b bVar = new b(true, packageName);
                    bVar.g();
                    bVar.d();
                    bVar.e();
                    bVar.a(100);
                    if (!this.r) {
                        a((y) bVar);
                        if (iPushActionListener != null) {
                            iPushActionListener.onStateChanged(0);
                        }
                    } else if (v()) {
                        aVar = new a(bVar, iPushActionListener);
                        String a2 = a(aVar);
                        bVar.b(a2);
                        aVar.a((Runnable) new r(this, bVar, a2));
                    } else if (iPushActionListener != null) {
                        iPushActionListener.onStateChanged(101);
                    }
                } else if (iPushActionListener != null) {
                    iPushActionListener.onStateChanged(102);
                }
                if (aVar != null) {
                    aVar.a((IPushActionListener) new q(this, aVar));
                    aVar.a();
                }
            } else if (iPushActionListener != null) {
                iPushActionListener.onStateChanged(1002);
            }
        } else if (iPushActionListener != null) {
            iPushActionListener.onStateChanged(102);
        }
    }

    /* access modifiers changed from: package-private */
    public final void b(IPushActionListener iPushActionListener) {
        if (this.i == null) {
            if (iPushActionListener != null) {
                iPushActionListener.onStateChanged(102);
            }
        } else if ("".equals(this.l)) {
            iPushActionListener.onStateChanged(0);
        } else if (a(this.d)) {
            this.d = SystemClock.elapsedRealtime();
            String packageName = this.i.getPackageName();
            a aVar = null;
            if (this.i != null) {
                b bVar = new b(false, packageName);
                bVar.d();
                bVar.e();
                bVar.g();
                bVar.a(100);
                if (!this.r) {
                    a((y) bVar);
                    if (iPushActionListener != null) {
                        iPushActionListener.onStateChanged(0);
                    }
                } else if (v()) {
                    aVar = new a(bVar, iPushActionListener);
                    String a2 = a(aVar);
                    bVar.b(a2);
                    aVar.a((Runnable) new t(this, bVar, a2));
                } else if (iPushActionListener != null) {
                    iPushActionListener.onStateChanged(101);
                }
            } else if (iPushActionListener != null) {
                iPushActionListener.onStateChanged(102);
            }
            if (aVar != null) {
                aVar.a((IPushActionListener) new s(this));
                aVar.a();
            }
        } else if (iPushActionListener != null) {
            iPushActionListener.onStateChanged(1002);
        }
    }

    public final void a(String str, int i2, Object... objArr) {
        a c2 = c(str);
        if (c2 != null) {
            c2.a(i2, objArr);
        } else {
            com.vivo.push.util.p.d("PushClientManager", "notifyApp token is null");
        }
    }

    public final void k() {
        this.k.a();
    }

    /* access modifiers changed from: package-private */
    public final void a(String str, String str2) {
        if (this.i != null) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(str2);
            com.vivo.push.b.a aVar = new com.vivo.push.b.a(true, str, this.i.getPackageName(), arrayList);
            aVar.a(100);
            a((y) aVar);
        }
    }

    /* access modifiers changed from: package-private */
    public final void a(String str, IPushActionListener iPushActionListener) {
        if (this.i == null) {
            if (iPushActionListener != null) {
                iPushActionListener.onStateChanged(102);
            }
        } else if (TextUtils.isEmpty(this.m) || !this.m.equals(str)) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(str);
            com.vivo.push.b.a aVar = new com.vivo.push.b.a(true, (String) null, this.i.getPackageName(), arrayList);
            aVar.a(100);
            if (!this.r) {
                a((y) aVar);
                if (iPushActionListener != null) {
                    iPushActionListener.onStateChanged(0);
                }
            } else if (!v()) {
                if (iPushActionListener != null) {
                    iPushActionListener.onStateChanged(101);
                }
            } else if (a(this.e)) {
                this.e = SystemClock.elapsedRealtime();
                String a2 = a(new a(aVar, iPushActionListener));
                aVar.b(a2);
                if (TextUtils.isEmpty(this.l)) {
                    a(a2, (int) DXError.DXError_EngineInitEnvException);
                } else if (TextUtils.isEmpty(str)) {
                    a(a2, (int) DXError.DXError_EngineInitException);
                } else if (((long) str.length()) > 70) {
                    a(a2, (int) DXError.DXError_EngineSizeException);
                } else {
                    a((y) aVar);
                    d(a2);
                }
            } else if (iPushActionListener != null) {
                iPushActionListener.onStateChanged(1002);
            }
        } else if (iPushActionListener != null) {
            iPushActionListener.onStateChanged(0);
        }
    }

    private static boolean a(long j2) {
        long elapsedRealtime = SystemClock.elapsedRealtime();
        return j2 == -1 || elapsedRealtime <= j2 || elapsedRealtime >= j2 + 2000;
    }

    /* access modifiers changed from: package-private */
    public final void b(String str, String str2) {
        if (this.i != null) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(str2);
            com.vivo.push.b.a aVar = new com.vivo.push.b.a(false, str, this.i.getPackageName(), arrayList);
            aVar.a(100);
            a((y) aVar);
        }
    }

    /* access modifiers changed from: package-private */
    public final void b(String str, IPushActionListener iPushActionListener) {
        if (this.i == null) {
            if (iPushActionListener != null) {
                iPushActionListener.onStateChanged(102);
            }
        } else if (!TextUtils.isEmpty(this.m)) {
            ArrayList arrayList = new ArrayList();
            arrayList.add(str);
            com.vivo.push.b.a aVar = new com.vivo.push.b.a(false, (String) null, this.i.getPackageName(), arrayList);
            aVar.a(100);
            if (!this.r) {
                a((y) aVar);
                if (iPushActionListener != null) {
                    iPushActionListener.onStateChanged(0);
                }
            } else if (!v()) {
                if (iPushActionListener != null) {
                    iPushActionListener.onStateChanged(101);
                }
            } else if (a(this.f)) {
                this.f = SystemClock.elapsedRealtime();
                String a2 = a(new a(aVar, iPushActionListener));
                aVar.b(a2);
                if (TextUtils.isEmpty(this.l)) {
                    a(a2, (int) DXError.DXError_EngineInitEnvException);
                } else if (TextUtils.isEmpty(str)) {
                    a(a2, (int) DXError.DXError_EngineInitException);
                } else if (((long) str.length()) > 70) {
                    a(a2, (int) DXError.DXError_EngineSizeException);
                } else {
                    a((y) aVar);
                    d(a2);
                }
            } else if (iPushActionListener != null) {
                iPushActionListener.onStateChanged(1002);
            }
        } else if (iPushActionListener != null) {
            iPushActionListener.onStateChanged(0);
        }
    }

    public final String l() {
        return this.m;
    }

    public final void b(String str) {
        this.m = str;
        this.k.a("APP_ALIAS", str);
    }

    public final void a(String str, int i2) {
        a c2 = c(str);
        if (c2 != null) {
            c2.a(i2, new Object[0]);
        } else {
            com.vivo.push.util.p.d("PushClientManager", "notifyStatusChanged token is null");
        }
    }

    private synchronized String a(a aVar) {
        int i2;
        this.n.put(this.o, aVar);
        i2 = this.o;
        this.o = i2 + 1;
        return Integer.toString(i2);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized com.vivo.push.p.a c(java.lang.String r3) {
        /*
            r2 = this;
            monitor-enter(r2)
            if (r3 == 0) goto L_0x0019
            int r3 = java.lang.Integer.parseInt(r3)     // Catch:{ Exception -> 0x0019, all -> 0x0016 }
            android.util.SparseArray<com.vivo.push.p$a> r0 = r2.n     // Catch:{ Exception -> 0x0019, all -> 0x0016 }
            java.lang.Object r0 = r0.get(r3)     // Catch:{ Exception -> 0x0019, all -> 0x0016 }
            com.vivo.push.p$a r0 = (com.vivo.push.p.a) r0     // Catch:{ Exception -> 0x0019, all -> 0x0016 }
            android.util.SparseArray<com.vivo.push.p$a> r1 = r2.n     // Catch:{ Exception -> 0x0019, all -> 0x0016 }
            r1.delete(r3)     // Catch:{ Exception -> 0x0019, all -> 0x0016 }
            monitor-exit(r2)
            return r0
        L_0x0016:
            r3 = move-exception
            monitor-exit(r2)
            throw r3
        L_0x0019:
            r3 = 0
            monitor-exit(r2)
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: com.vivo.push.p.c(java.lang.String):com.vivo.push.p$a");
    }

    /* access modifiers changed from: package-private */
    public final void a(int i2) {
        if (!g.a(i2)) {
            com.vivo.push.util.p.d("PushClientManager", "切换环境失败，非法的环境：" + i2);
            Context context = this.i;
            com.vivo.push.util.p.a(context, "切换环境失败，非法的环境：" + i2);
            return;
        }
        a((y) new ab());
        g gVar = new g();
        gVar.b(i2);
        a((y) gVar);
    }

    /* access modifiers changed from: package-private */
    public final void a(ArrayList<String> arrayList, IPushActionListener iPushActionListener) {
        if (this.i != null) {
            ad adVar = new ad(true, (String) null, this.i.getPackageName(), arrayList);
            adVar.a(500);
            if (!this.r) {
                a((y) adVar);
                if (iPushActionListener != null) {
                    iPushActionListener.onStateChanged(0);
                }
            } else if (!v()) {
                if (iPushActionListener != null) {
                    iPushActionListener.onStateChanged(101);
                }
            } else if (a(this.g)) {
                this.g = SystemClock.elapsedRealtime();
                String a2 = a(new a(adVar, iPushActionListener));
                adVar.b(a2);
                if (TextUtils.isEmpty(this.l)) {
                    a(a2, (int) StatusCode.INTERFACE_LIMIT);
                } else if (arrayList.size() < 0) {
                    a(a2, (int) StatusCode.VERSION_IS_NEWEST);
                } else {
                    if (arrayList.size() + c().size() > 500) {
                        a(a2, 20004);
                        return;
                    }
                    Iterator<String> it = arrayList.iterator();
                    while (it.hasNext()) {
                        if (((long) it.next().length()) > 70) {
                            a(a2, (int) StatusCode.PULL_DISABLE);
                            return;
                        }
                    }
                    a((y) adVar);
                    d(a2);
                }
            } else if (iPushActionListener != null) {
                iPushActionListener.onStateChanged(1002);
            }
        } else if (iPushActionListener != null) {
            iPushActionListener.onStateChanged(102);
        }
    }

    /* access modifiers changed from: package-private */
    public final void a(String str, ArrayList<String> arrayList) {
        if (this.i != null) {
            ad adVar = new ad(true, str, this.i.getPackageName(), arrayList);
            adVar.a(500);
            a((y) adVar);
        }
    }

    /* access modifiers changed from: package-private */
    public final void b(String str, ArrayList<String> arrayList) {
        if (this.i != null) {
            ad adVar = new ad(false, str, this.i.getPackageName(), arrayList);
            adVar.a(500);
            a((y) adVar);
        }
    }

    /* access modifiers changed from: package-private */
    public final void b(ArrayList<String> arrayList, IPushActionListener iPushActionListener) {
        if (this.i != null) {
            ad adVar = new ad(false, (String) null, this.i.getPackageName(), arrayList);
            adVar.a(500);
            if (!this.r) {
                a((y) adVar);
                if (iPushActionListener != null) {
                    iPushActionListener.onStateChanged(0);
                }
            } else if (!v()) {
                if (iPushActionListener != null) {
                    iPushActionListener.onStateChanged(101);
                }
            } else if (a(this.h)) {
                this.h = SystemClock.elapsedRealtime();
                String a2 = a(new a(adVar, iPushActionListener));
                adVar.b(a2);
                if (TextUtils.isEmpty(this.l)) {
                    a(a2, (int) StatusCode.INTERFACE_LIMIT);
                } else if (arrayList.size() < 0) {
                    a(a2, (int) StatusCode.VERSION_IS_NEWEST);
                } else if (arrayList.size() > 500) {
                    a(a2, 20004);
                } else {
                    Iterator<String> it = arrayList.iterator();
                    while (it.hasNext()) {
                        if (((long) it.next().length()) > 70) {
                            a(a2, (int) StatusCode.PULL_DISABLE);
                            return;
                        }
                    }
                    a((y) adVar);
                    d(a2);
                }
            } else if (iPushActionListener != null) {
                iPushActionListener.onStateChanged(1002);
            }
        } else if (iPushActionListener != null) {
            iPushActionListener.onStateChanged(102);
        }
    }

    public final void a(Intent intent, PushMessageCallback pushMessageCallback) {
        y createReceiverCommand = this.s.createReceiverCommand(intent);
        Context context = a().i;
        if (createReceiverCommand == null) {
            com.vivo.push.util.p.a("PushClientManager", "sendCommand, null command!");
            if (context != null) {
                com.vivo.push.util.p.c(context, "[执行指令失败]指令空！");
                return;
            }
            return;
        }
        com.vivo.push.c.ab createReceiveTask = this.s.createReceiveTask(createReceiverCommand);
        if (createReceiveTask == null) {
            com.vivo.push.util.p.a("PushClientManager", "sendCommand, null command task! pushCommand = " + createReceiverCommand);
            if (context != null) {
                com.vivo.push.util.p.c(context, "[执行指令失败]指令" + createReceiverCommand + "任务空！");
                return;
            }
            return;
        }
        if (context != null && !(createReceiverCommand instanceof com.vivo.push.b.p)) {
            com.vivo.push.util.p.a(context, "[接收指令]" + createReceiverCommand);
        }
        createReceiveTask.a(pushMessageCallback);
        w.a((v) createReceiveTask);
    }

    public final void a(y yVar) {
        Context context = a().i;
        if (yVar == null) {
            com.vivo.push.util.p.a("PushClientManager", "sendCommand, null command!");
            if (context != null) {
                com.vivo.push.util.p.c(context, "[执行指令失败]指令空！");
                return;
            }
            return;
        }
        v createTask = this.s.createTask(yVar);
        if (createTask == null) {
            com.vivo.push.util.p.a("PushClientManager", "sendCommand, null command task! pushCommand = " + yVar);
            if (context != null) {
                com.vivo.push.util.p.c(context, "[执行指令失败]指令" + yVar + "任务空！");
                return;
            }
            return;
        }
        com.vivo.push.util.p.d("PushClientManager", "client--sendCommand, command = " + yVar);
        w.a(createTask);
    }

    /* access modifiers changed from: private */
    public void d(String str) {
        w.a((Runnable) new u(this, str));
    }

    public final void m() {
        a((y) new d(true));
    }

    public final void n() {
        a((y) new d(false));
    }

    public final void b(boolean z) {
        com.vivo.push.util.p.a(z);
        com.vivo.push.b.z zVar = new com.vivo.push.b.z();
        zVar.a(z ? 1 : 0);
        a((y) zVar);
    }

    public final void b(int i2) {
        if (i2 < 4 || u() >= 1260) {
            com.vivo.push.util.p.a((i2 & 1) != 0);
            com.vivo.push.b.z zVar = new com.vivo.push.b.z();
            zVar.a(i2);
            a((y) zVar);
            return;
        }
        com.vivo.push.util.p.b("PushClientManager", "current push version " + this.q + " is not support this mode");
    }

    public final void o() {
        a((y) new y());
    }

    public final boolean p() {
        return this.i.getPackageManager().getComponentEnabledSetting(new ComponentName(this.i, "com.vivo.push.sdk.service.PushService")) != 2;
    }

    public final void q() {
        a((y) new i());
    }

    public final int r() {
        return this.t;
    }

    public final void c(int i2) {
        this.t = i2;
    }

    /* access modifiers changed from: package-private */
    public final Map<String, String> s() {
        return z.f(this.i);
    }

    private long u() {
        if (this.i == null) {
            return -1;
        }
        if (this.q == null) {
            this.q = Long.valueOf(z.b(this.i));
        }
        return this.q.longValue();
    }

    private boolean v() {
        if (this.p == null) {
            this.p = Boolean.valueOf(u() >= 1230 && z.e(this.i));
        }
        return this.p.booleanValue();
    }

    /* compiled from: PushClientManager */
    public static class a {
        private IPushActionListener a;
        private c b;
        private IPushActionListener c;
        private Runnable d;
        private Object[] e;

        public a(c cVar, IPushActionListener iPushActionListener) {
            this.b = cVar;
            this.a = iPushActionListener;
        }

        public final void a(int i, Object... objArr) {
            this.e = objArr;
            if (this.c != null) {
                this.c.onStateChanged(i);
            }
            if (this.a != null) {
                this.a.onStateChanged(i);
            }
        }

        public final void a(Runnable runnable) {
            this.d = runnable;
        }

        public final void a() {
            if (this.d == null) {
                com.vivo.push.util.p.a("PushClientManager", "task is null");
            } else {
                this.d.run();
            }
        }

        public final void a(IPushActionListener iPushActionListener) {
            this.c = iPushActionListener;
        }

        public final Object[] b() {
            return this.e;
        }
    }
}
