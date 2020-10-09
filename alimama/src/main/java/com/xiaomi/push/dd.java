package com.xiaomi.push;

import android.content.Context;
import com.xiaomi.push.al;
import com.xiaomi.push.service.ba;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class dd {
    private static volatile dd a;

    /* renamed from: a  reason: collision with other field name */
    private Context f217a;

    /* renamed from: a  reason: collision with other field name */
    private final ConcurrentLinkedQueue<b> f218a = new ConcurrentLinkedQueue<>();

    class a extends b {
        a() {
            super();
        }

        public void b() {
            dd.a(dd.this);
        }
    }

    class b extends al.b {
        long a = System.currentTimeMillis();

        b() {
        }

        public boolean a() {
            return true;
        }

        public void b() {
        }

        /* access modifiers changed from: package-private */
        /* renamed from: b  reason: collision with other method in class */
        public final boolean m174b() {
            return System.currentTimeMillis() - this.a > 172800000;
        }
    }

    class c extends b {
        int a;

        /* renamed from: a  reason: collision with other field name */
        File f220a;

        /* renamed from: a  reason: collision with other field name */
        String f221a;

        /* renamed from: a  reason: collision with other field name */
        boolean f222a;
        String b;

        /* renamed from: b  reason: collision with other field name */
        boolean f223b;

        c(String str, String str2, File file, boolean z) {
            super();
            this.f221a = str;
            this.b = str2;
            this.f220a = file;
            this.f223b = z;
        }

        /* JADX WARNING: Removed duplicated region for block: B:12:0x0039  */
        /* JADX WARNING: Removed duplicated region for block: B:15:0x003e  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private boolean c() {
            /*
                r10 = this;
                com.xiaomi.push.dd r0 = com.xiaomi.push.dd.this
                android.content.Context r0 = com.xiaomi.push.dd.a((com.xiaomi.push.dd) r0)
                java.lang.String r1 = "log.timestamp"
                r2 = 0
                android.content.SharedPreferences r0 = r0.getSharedPreferences(r1, r2)
                java.lang.String r1 = "log.requst"
                java.lang.String r3 = ""
                java.lang.String r1 = r0.getString(r1, r3)
                long r3 = java.lang.System.currentTimeMillis()
                org.json.JSONObject r5 = new org.json.JSONObject     // Catch:{ JSONException -> 0x002b }
                r5.<init>(r1)     // Catch:{ JSONException -> 0x002b }
                java.lang.String r1 = "time"
                long r6 = r5.getLong(r1)     // Catch:{ JSONException -> 0x002b }
                java.lang.String r1 = "times"
                int r1 = r5.getInt(r1)     // Catch:{ JSONException -> 0x002c }
                goto L_0x002d
            L_0x002b:
                r6 = r3
            L_0x002c:
                r1 = 0
            L_0x002d:
                long r3 = java.lang.System.currentTimeMillis()
                long r3 = r3 - r6
                r8 = 86400000(0x5265c00, double:4.2687272E-316)
                int r5 = (r3 > r8 ? 1 : (r3 == r8 ? 0 : -1))
                if (r5 >= 0) goto L_0x003e
                r3 = 10
                if (r1 <= r3) goto L_0x0043
                return r2
            L_0x003e:
                long r6 = java.lang.System.currentTimeMillis()
                r1 = 0
            L_0x0043:
                org.json.JSONObject r2 = new org.json.JSONObject
                r2.<init>()
                r3 = 1
                java.lang.String r4 = "time"
                r2.put(r4, r6)     // Catch:{ JSONException -> 0x0066 }
                java.lang.String r4 = "times"
                int r1 = r1 + r3
                r2.put(r4, r1)     // Catch:{ JSONException -> 0x0066 }
                android.content.SharedPreferences$Editor r0 = r0.edit()     // Catch:{ JSONException -> 0x0066 }
                java.lang.String r1 = "log.requst"
                java.lang.String r2 = r2.toString()     // Catch:{ JSONException -> 0x0066 }
                android.content.SharedPreferences$Editor r0 = r0.putString(r1, r2)     // Catch:{ JSONException -> 0x0066 }
                r0.commit()     // Catch:{ JSONException -> 0x0066 }
                goto L_0x007f
            L_0x0066:
                r0 = move-exception
                java.lang.StringBuilder r1 = new java.lang.StringBuilder
                r1.<init>()
                java.lang.String r2 = "JSONException on put "
                r1.append(r2)
                java.lang.String r0 = r0.getMessage()
                r1.append(r0)
                java.lang.String r0 = r1.toString()
                com.xiaomi.channel.commonutils.logger.b.c(r0)
            L_0x007f:
                return r3
            */
            throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.dd.c.c():boolean");
        }

        public boolean a() {
            return as.d(dd.a(dd.this)) || (this.f223b && as.b(dd.a(dd.this)));
        }

        public void b() {
            try {
                if (c()) {
                    HashMap hashMap = new HashMap();
                    hashMap.put("uid", ba.a());
                    hashMap.put("token", this.b);
                    hashMap.put(com.alipay.sdk.app.statistic.c.a, as.a(dd.a(dd.this)));
                    as.a(this.f221a, hashMap, this.f220a, "file");
                }
                this.f222a = true;
            } catch (IOException unused) {
            }
        }

        /* renamed from: c  reason: collision with other method in class */
        public void m175c() {
            if (!this.f222a) {
                this.a++;
                if (this.a < 3) {
                    dd.a(dd.this).add(this);
                }
            }
            if (this.f222a || this.a >= 3) {
                this.f220a.delete();
            }
            dd.this.a((long) ((1 << this.a) * 1000));
        }
    }

    private dd(Context context) {
        this.f217a = context;
        this.f218a.add(new a());
        b(0);
    }

    public static dd a(Context context) {
        if (a == null) {
            synchronized (dd.class) {
                if (a == null) {
                    a = new dd(context);
                }
            }
        }
        a.f217a = context;
        return a;
    }

    /* access modifiers changed from: private */
    public void a(long j) {
        b peek = this.f218a.peek();
        if (peek != null && peek.a()) {
            b(j);
        }
    }

    private void b() {
        if (!aa.b() && !aa.a()) {
            try {
                File file = new File(this.f217a.getExternalFilesDir((String) null) + "/.logcache");
                if (file.exists() && file.isDirectory()) {
                    for (File delete : file.listFiles()) {
                        delete.delete();
                    }
                }
            } catch (NullPointerException unused) {
            }
        }
    }

    private void b(long j) {
        if (!this.f218a.isEmpty()) {
            gp.a(new df(this), j);
        }
    }

    private void c() {
        while (!this.f218a.isEmpty()) {
            b peek = this.f218a.peek();
            if (peek != null) {
                if (peek.b() || this.f218a.size() > 6) {
                    com.xiaomi.channel.commonutils.logger.b.c("remove Expired task");
                    this.f218a.remove(peek);
                } else {
                    return;
                }
            }
        }
    }

    public void a() {
        c();
        a(0);
    }

    public void a(String str, String str2, Date date, Date date2, int i, boolean z) {
        this.f218a.add(new de(this, i, date, date2, str, str2, z));
        b(0);
    }
}
