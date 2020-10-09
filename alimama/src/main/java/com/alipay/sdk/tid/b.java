package com.alipay.sdk.tid;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.alipay.sdk.app.AlipayResultActivity;
import com.alipay.sdk.util.c;
import java.util.Random;
import org.json.JSONObject;

public class b {
    public static final String a = "alipay_tid_storage";
    public static final String b = "tidinfo";
    public static final String c = "upgraded_from_db";
    public static final String d = "tid";
    public static final String e = "client_key";
    public static final String f = "timestamp";
    public static final String g = "vimei";
    public static final String h = "vimsi";
    /* access modifiers changed from: private */
    public static Context i;
    private static b o;
    private String j;
    private String k;
    private long l;
    private String m;
    private String n;
    private boolean p = false;

    private void o() {
    }

    public static synchronized b a(Context context) {
        b bVar;
        synchronized (b.class) {
            if (o == null) {
                c.b("TidStorage", "getInstance");
                o = new b();
            }
            if (i == null) {
                o.b(context);
            }
            bVar = o;
        }
        return bVar;
    }

    private void b(Context context) {
        if (context != null) {
            c.b("TidStorage", "TidStorage.initialize context != null");
            i = context.getApplicationContext();
        }
        if (!this.p) {
            this.p = true;
            k();
            l();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0079, code lost:
        if (r2 != null) goto L_0x0069;
     */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x009d  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00ad  */
    /* JADX WARNING: Removed duplicated region for block: B:48:0x00b4  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void k() {
        /*
            r8 = this;
            android.content.Context r0 = i
            if (r0 != 0) goto L_0x0005
            return
        L_0x0005:
            java.lang.String r1 = "alipay_tid_storage"
            java.lang.String r2 = "upgraded_from_db"
            boolean r1 = com.alipay.sdk.tid.b.a.d(r1, r2)
            if (r1 == 0) goto L_0x0017
            java.lang.String r0 = "TidStorage"
            java.lang.String r1 = "transferTidFromOldDb: already migrated. returning"
            com.alipay.sdk.util.c.b(r0, r1)
            return
        L_0x0017:
            r1 = 0
            java.lang.String r2 = "TidStorage"
            java.lang.String r3 = "transferTidFromOldDb: tid from db: "
            com.alipay.sdk.util.c.b(r2, r3)     // Catch:{ Throwable -> 0x0072, all -> 0x006f }
            com.alipay.sdk.tid.a r2 = new com.alipay.sdk.tid.a     // Catch:{ Throwable -> 0x0072, all -> 0x006f }
            r2.<init>(r0)     // Catch:{ Throwable -> 0x0072, all -> 0x006f }
            com.alipay.sdk.util.a r1 = com.alipay.sdk.util.a.a((android.content.Context) r0)     // Catch:{ Throwable -> 0x006d }
            java.lang.String r1 = r1.b()     // Catch:{ Throwable -> 0x006d }
            com.alipay.sdk.util.a r3 = com.alipay.sdk.util.a.a((android.content.Context) r0)     // Catch:{ Throwable -> 0x006d }
            java.lang.String r3 = r3.a()     // Catch:{ Throwable -> 0x006d }
            java.lang.String r4 = r2.a(r1, r3)     // Catch:{ Throwable -> 0x006d }
            java.lang.String r1 = r2.b(r1, r3)     // Catch:{ Throwable -> 0x006d }
            boolean r3 = android.text.TextUtils.isEmpty(r4)     // Catch:{ Throwable -> 0x006d }
            if (r3 != 0) goto L_0x0069
            boolean r3 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x006d }
            if (r3 != 0) goto L_0x0069
            java.lang.String r3 = "TidStorage"
            java.lang.StringBuilder r5 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x006d }
            r5.<init>()     // Catch:{ Throwable -> 0x006d }
            java.lang.String r6 = "transferTidFromOldDb: tid from db is "
            r5.append(r6)     // Catch:{ Throwable -> 0x006d }
            r5.append(r4)     // Catch:{ Throwable -> 0x006d }
            java.lang.String r6 = ", "
            r5.append(r6)     // Catch:{ Throwable -> 0x006d }
            r5.append(r1)     // Catch:{ Throwable -> 0x006d }
            java.lang.String r5 = r5.toString()     // Catch:{ Throwable -> 0x006d }
            com.alipay.sdk.util.c.b(r3, r5)     // Catch:{ Throwable -> 0x006d }
            r8.a(r4, r1)     // Catch:{ Throwable -> 0x006d }
        L_0x0069:
            r2.close()
            goto L_0x007c
        L_0x006d:
            r1 = move-exception
            goto L_0x0076
        L_0x006f:
            r0 = move-exception
            r2 = r1
            goto L_0x00b2
        L_0x0072:
            r2 = move-exception
            r7 = r2
            r2 = r1
            r1 = r7
        L_0x0076:
            com.alipay.sdk.util.c.a(r1)     // Catch:{ all -> 0x00b1 }
            if (r2 == 0) goto L_0x007c
            goto L_0x0069
        L_0x007c:
            java.lang.String r1 = "TidStorage"
            java.lang.String r3 = "transferTidFromOldDb: removing database table"
            com.alipay.sdk.util.c.b(r1, r3)     // Catch:{ Throwable -> 0x0097 }
            com.alipay.sdk.tid.a r1 = new com.alipay.sdk.tid.a     // Catch:{ Throwable -> 0x0097 }
            r1.<init>(r0)     // Catch:{ Throwable -> 0x0097 }
            r1.a()     // Catch:{ Throwable -> 0x0092, all -> 0x008f }
            r1.close()
            goto L_0x00a0
        L_0x008f:
            r0 = move-exception
            r2 = r1
            goto L_0x00ab
        L_0x0092:
            r0 = move-exception
            r2 = r1
            goto L_0x0098
        L_0x0095:
            r0 = move-exception
            goto L_0x00ab
        L_0x0097:
            r0 = move-exception
        L_0x0098:
            com.alipay.sdk.util.c.a(r0)     // Catch:{ all -> 0x0095 }
            if (r2 == 0) goto L_0x00a0
            r2.close()
        L_0x00a0:
            java.lang.String r0 = "alipay_tid_storage"
            java.lang.String r1 = "upgraded_from_db"
            java.lang.String r2 = "updated"
            r3 = 0
            com.alipay.sdk.tid.b.a.a(r0, r1, r2, r3)
            return
        L_0x00ab:
            if (r2 == 0) goto L_0x00b0
            r2.close()
        L_0x00b0:
            throw r0
        L_0x00b1:
            r0 = move-exception
        L_0x00b2:
            if (r2 == 0) goto L_0x00b7
            r2.close()
        L_0x00b7:
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.tid.b.k():void");
    }

    public String a() {
        c.b("TidStorage", "TidStorage.getTid " + this.j);
        return this.j;
    }

    public String b() {
        c.b("TidStorage", "TidStorage.getClientKey " + this.k);
        return this.k;
    }

    public String c() {
        c.b("TidStorage", "TidStorage.getVirtualImei " + this.m);
        return this.m;
    }

    public String d() {
        c.b("TidStorage", "TidStorage.getVirtualImsi " + this.n);
        return this.n;
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x00a7  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x00ab  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void l() {
        /*
            r9 = this;
            long r0 = java.lang.System.currentTimeMillis()
            java.lang.Long r0 = java.lang.Long.valueOf(r0)
            r1 = 0
            java.lang.String r2 = "alipay_tid_storage"
            java.lang.String r3 = "tidinfo"
            r4 = 1
            java.lang.String r2 = com.alipay.sdk.tid.b.a.a((java.lang.String) r2, (java.lang.String) r3, (boolean) r4)     // Catch:{ Exception -> 0x0061 }
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x0061 }
            if (r3 != 0) goto L_0x005d
            org.json.JSONObject r3 = new org.json.JSONObject     // Catch:{ Exception -> 0x0061 }
            r3.<init>(r2)     // Catch:{ Exception -> 0x0061 }
            java.lang.String r2 = "tid"
            java.lang.String r4 = ""
            java.lang.String r2 = r3.optString(r2, r4)     // Catch:{ Exception -> 0x0061 }
            java.lang.String r4 = "client_key"
            java.lang.String r5 = ""
            java.lang.String r4 = r3.optString(r4, r5)     // Catch:{ Exception -> 0x005a }
            java.lang.String r5 = "timestamp"
            long r6 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x0057 }
            long r5 = r3.optLong(r5, r6)     // Catch:{ Exception -> 0x0057 }
            java.lang.Long r5 = java.lang.Long.valueOf(r5)     // Catch:{ Exception -> 0x0057 }
            java.lang.String r0 = "vimei"
            java.lang.String r6 = ""
            java.lang.String r0 = r3.optString(r0, r6)     // Catch:{ Exception -> 0x0054 }
            java.lang.String r6 = "vimsi"
            java.lang.String r7 = ""
            java.lang.String r3 = r3.optString(r6, r7)     // Catch:{ Exception -> 0x004f }
            r1 = r2
            r2 = r0
            r0 = r5
            goto L_0x006b
        L_0x004f:
            r3 = move-exception
            r8 = r5
            r5 = r0
            r0 = r8
            goto L_0x0065
        L_0x0054:
            r3 = move-exception
            r0 = r5
            goto L_0x0058
        L_0x0057:
            r3 = move-exception
        L_0x0058:
            r5 = r1
            goto L_0x0065
        L_0x005a:
            r3 = move-exception
            r4 = r1
            goto L_0x0064
        L_0x005d:
            r2 = r1
            r3 = r2
            r4 = r3
            goto L_0x006b
        L_0x0061:
            r3 = move-exception
            r2 = r1
            r4 = r2
        L_0x0064:
            r5 = r4
        L_0x0065:
            com.alipay.sdk.util.c.a(r3)
            r3 = r1
            r1 = r2
            r2 = r5
        L_0x006b:
            java.lang.String r5 = "TidStorage"
            java.lang.StringBuilder r6 = new java.lang.StringBuilder
            r6.<init>()
            java.lang.String r7 = "TidStorage.load "
            r6.append(r7)
            r6.append(r1)
            java.lang.String r7 = " "
            r6.append(r7)
            r6.append(r4)
            java.lang.String r7 = " "
            r6.append(r7)
            r6.append(r0)
            java.lang.String r7 = " "
            r6.append(r7)
            r6.append(r2)
            java.lang.String r7 = " "
            r6.append(r7)
            r6.append(r3)
            java.lang.String r6 = r6.toString()
            com.alipay.sdk.util.c.b(r5, r6)
            boolean r5 = r9.a(r1, r4, r2, r3)
            if (r5 == 0) goto L_0x00ab
            r9.m()
            goto L_0x00b9
        L_0x00ab:
            r9.j = r1
            r9.k = r4
            long r0 = r0.longValue()
            r9.l = r0
            r9.m = r2
            r9.n = r3
        L_0x00b9:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alipay.sdk.tid.b.l():void");
    }

    private boolean a(String str, String str2, String str3, String str4) {
        return TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3) || TextUtils.isEmpty(str4);
    }

    public boolean e() {
        return TextUtils.isEmpty(this.j) || TextUtils.isEmpty(this.k) || TextUtils.isEmpty(this.m) || TextUtils.isEmpty(this.n);
    }

    private void m() {
        this.j = "";
        this.k = f();
        this.l = System.currentTimeMillis();
        this.m = n();
        this.n = n();
        a.b(a, b);
    }

    private String n() {
        String hexString = Long.toHexString(System.currentTimeMillis());
        Random random = new Random();
        return hexString + (random.nextInt(AlipayResultActivity.a) + 1000);
    }

    public String f() {
        String hexString = Long.toHexString(System.currentTimeMillis());
        return hexString.length() > 10 ? hexString.substring(hexString.length() - 10) : hexString;
    }

    public void g() {
        String format = String.format("TidStorage::delete > %s，%s，%s，%s，%s", new Object[]{this.j, this.k, Long.valueOf(this.l), this.m, this.n});
        c.b("TidStorage", "TidStorage.delete " + format);
        m();
    }

    public boolean h() {
        return e();
    }

    public Long i() {
        return Long.valueOf(this.l);
    }

    public void a(String str, String str2) {
        c.b("TidStorage", "TidStorage.save " + ("tid=" + str + ",clientKey=" + str2));
        if (!TextUtils.isEmpty(str) && !TextUtils.isEmpty(str2)) {
            this.j = str;
            this.k = str2;
            this.l = System.currentTimeMillis();
            p();
            o();
        }
    }

    private void a(String str, String str2, String str3, String str4, Long l2) {
        if (!a(str, str2, str3, str4)) {
            this.j = str;
            this.k = str2;
            this.m = str3;
            this.n = str4;
            if (l2 == null) {
                this.l = System.currentTimeMillis();
            } else {
                this.l = l2.longValue();
            }
            p();
        }
    }

    private void p() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("tid", this.j);
            jSONObject.put(e, this.k);
            jSONObject.put("timestamp", this.l);
            jSONObject.put(g, this.m);
            jSONObject.put(h, this.n);
            a.a(a, b, jSONObject.toString(), true);
        } catch (Exception e2) {
            c.a(e2);
        }
    }

    public static class a {
        private static String a() {
            return "!@#23457";
        }

        public static boolean a(String str, String str2) {
            if (b.i == null) {
                return false;
            }
            return b.i.getSharedPreferences(str, 0).contains(str2);
        }

        public static void b(String str, String str2) {
            if (b.i != null) {
                b.i.getSharedPreferences(str, 0).edit().remove(str2).apply();
            }
        }

        public static String c(String str, String str2) {
            return a(str, str2, true);
        }

        public static boolean d(String str, String str2) {
            if (b.i == null) {
                return false;
            }
            return b.i.getSharedPreferences(str, 0).contains(str2);
        }

        public static String a(String str, String str2, boolean z) {
            String str3;
            if (b.i == null) {
                return null;
            }
            String string = b.i.getSharedPreferences(str, 0).getString(str2, (String) null);
            if (TextUtils.isEmpty(string) || !z) {
                str3 = string;
            } else {
                String b = b();
                str3 = com.alipay.sdk.encrypt.b.b(string, b);
                if (TextUtils.isEmpty(str3)) {
                    str3 = com.alipay.sdk.encrypt.b.b(string, a());
                    if (!TextUtils.isEmpty(str3)) {
                        a(str, str2, str3, true);
                    }
                }
                if (TextUtils.isEmpty(str3)) {
                    String.format("LocalPreference::getLocalPreferences failed %s，%s", new Object[]{string, b});
                    c.b("TidStorage", "TidStorage.save LocalPreference::getLocalPreferences failed");
                }
            }
            c.b("TidStorage", "TidStorage.save LocalPreference::getLocalPreferences value " + string);
            return str3;
        }

        public static void a(String str, String str2, String str3) {
            a(str, str2, str3, true);
        }

        public static void a(String str, String str2, String str3, boolean z) {
            if (b.i != null) {
                SharedPreferences sharedPreferences = b.i.getSharedPreferences(str, 0);
                if (z) {
                    String b = b();
                    String a = com.alipay.sdk.encrypt.b.a(str3, b);
                    if (TextUtils.isEmpty(a)) {
                        String.format("LocalPreference::putLocalPreferences failed %s，%s", new Object[]{str3, b});
                    }
                    str3 = a;
                }
                sharedPreferences.edit().putString(str2, str3).apply();
            }
        }

        private static String b() {
            String str = "";
            try {
                str = b.i.getApplicationContext().getPackageName();
            } catch (Throwable th) {
                c.a(th);
            }
            if (TextUtils.isEmpty(str)) {
                str = "unknow";
            }
            return (str + "00000000").substring(0, 8);
        }
    }
}
