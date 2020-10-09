package com.xiaomi.push.service;

import android.content.Context;
import android.content.SharedPreferences;
import com.xiaomi.mipush.sdk.Constants;
import com.xiaomi.push.ab;
import com.xiaomi.push.fn;
import com.xiaomi.push.i;
import com.xiaomi.push.o;

public class l {
    private static k a;

    /* renamed from: a  reason: collision with other field name */
    private static a f913a;

    public interface a {
        void a();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00a9, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized com.xiaomi.push.service.k a(android.content.Context r12) {
        /*
            java.lang.Class<com.xiaomi.push.service.l> r0 = com.xiaomi.push.service.l.class
            monitor-enter(r0)
            com.xiaomi.push.service.k r1 = a     // Catch:{ all -> 0x00aa }
            if (r1 == 0) goto L_0x000b
            com.xiaomi.push.service.k r12 = a     // Catch:{ all -> 0x00aa }
            monitor-exit(r0)
            return r12
        L_0x000b:
            java.lang.String r1 = "mipush_account"
            r2 = 0
            android.content.SharedPreferences r1 = r12.getSharedPreferences(r1, r2)     // Catch:{ all -> 0x00aa }
            java.lang.String r2 = "uuid"
            r3 = 0
            java.lang.String r5 = r1.getString(r2, r3)     // Catch:{ all -> 0x00aa }
            java.lang.String r2 = "token"
            java.lang.String r6 = r1.getString(r2, r3)     // Catch:{ all -> 0x00aa }
            java.lang.String r2 = "security"
            java.lang.String r7 = r1.getString(r2, r3)     // Catch:{ all -> 0x00aa }
            java.lang.String r2 = "app_id"
            java.lang.String r8 = r1.getString(r2, r3)     // Catch:{ all -> 0x00aa }
            java.lang.String r2 = "app_token"
            java.lang.String r9 = r1.getString(r2, r3)     // Catch:{ all -> 0x00aa }
            java.lang.String r2 = "package_name"
            java.lang.String r10 = r1.getString(r2, r3)     // Catch:{ all -> 0x00aa }
            java.lang.String r2 = "device_id"
            java.lang.String r2 = r1.getString(r2, r3)     // Catch:{ all -> 0x00aa }
            java.lang.String r4 = "env_type"
            r11 = 1
            int r11 = r1.getInt(r4, r11)     // Catch:{ all -> 0x00aa }
            boolean r4 = android.text.TextUtils.isEmpty(r2)     // Catch:{ all -> 0x00aa }
            if (r4 != 0) goto L_0x0063
            java.lang.String r4 = "a-"
            boolean r4 = r2.startsWith(r4)     // Catch:{ all -> 0x00aa }
            if (r4 == 0) goto L_0x0063
            java.lang.String r2 = com.xiaomi.push.i.k(r12)     // Catch:{ all -> 0x00aa }
            android.content.SharedPreferences$Editor r1 = r1.edit()     // Catch:{ all -> 0x00aa }
            java.lang.String r4 = "device_id"
            android.content.SharedPreferences$Editor r1 = r1.putString(r4, r2)     // Catch:{ all -> 0x00aa }
            r1.commit()     // Catch:{ all -> 0x00aa }
        L_0x0063:
            boolean r1 = android.text.TextUtils.isEmpty(r5)     // Catch:{ all -> 0x00aa }
            if (r1 != 0) goto L_0x00a8
            boolean r1 = android.text.TextUtils.isEmpty(r6)     // Catch:{ all -> 0x00aa }
            if (r1 != 0) goto L_0x00a8
            boolean r1 = android.text.TextUtils.isEmpty(r7)     // Catch:{ all -> 0x00aa }
            if (r1 != 0) goto L_0x00a8
            java.lang.String r1 = com.xiaomi.push.i.k(r12)     // Catch:{ all -> 0x00aa }
            java.lang.String r3 = "com.xiaomi.xmsf"
            java.lang.String r12 = r12.getPackageName()     // Catch:{ all -> 0x00aa }
            boolean r12 = r3.equals(r12)     // Catch:{ all -> 0x00aa }
            if (r12 != 0) goto L_0x009c
            boolean r12 = android.text.TextUtils.isEmpty(r1)     // Catch:{ all -> 0x00aa }
            if (r12 != 0) goto L_0x009c
            boolean r12 = android.text.TextUtils.isEmpty(r2)     // Catch:{ all -> 0x00aa }
            if (r12 != 0) goto L_0x009c
            boolean r12 = r2.equals(r1)     // Catch:{ all -> 0x00aa }
            if (r12 != 0) goto L_0x009c
            java.lang.String r12 = "read_phone_state permission changes."
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r12)     // Catch:{ all -> 0x00aa }
        L_0x009c:
            com.xiaomi.push.service.k r12 = new com.xiaomi.push.service.k     // Catch:{ all -> 0x00aa }
            r4 = r12
            r4.<init>(r5, r6, r7, r8, r9, r10, r11)     // Catch:{ all -> 0x00aa }
            a = r12     // Catch:{ all -> 0x00aa }
            com.xiaomi.push.service.k r12 = a     // Catch:{ all -> 0x00aa }
            monitor-exit(r0)
            return r12
        L_0x00a8:
            monitor-exit(r0)
            return r3
        L_0x00aa:
            r12 = move-exception
            monitor-exit(r0)
            throw r12
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.l.a(android.content.Context):com.xiaomi.push.service.k");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:72:0x0215, code lost:
        return null;
     */
    /* JADX WARNING: Removed duplicated region for block: B:13:0x0052 A[Catch:{ Exception -> 0x0096 }] */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0061 A[Catch:{ Exception -> 0x0096 }] */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x006c A[Catch:{ Exception -> 0x0096 }] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0075 A[Catch:{ Exception -> 0x0096 }] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x007e A[Catch:{ Exception -> 0x0096 }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x009f A[Catch:{ Exception -> 0x0096 }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00a6 A[Catch:{ Exception -> 0x0096 }] */
    /* JADX WARNING: Removed duplicated region for block: B:38:0x00cf A[Catch:{ Exception -> 0x0096 }] */
    /* JADX WARNING: Removed duplicated region for block: B:51:0x0140 A[Catch:{ Exception -> 0x0096 }] */
    /* JADX WARNING: Removed duplicated region for block: B:54:0x0153 A[Catch:{ Exception -> 0x0096 }] */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x0189 A[Catch:{ Exception -> 0x0096 }] */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x0193 A[Catch:{ Exception -> 0x0096 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized com.xiaomi.push.service.k a(android.content.Context r9, java.lang.String r10, java.lang.String r11, java.lang.String r12) {
        /*
            java.lang.Class<com.xiaomi.push.service.l> r0 = com.xiaomi.push.service.l.class
            monitor-enter(r0)
            java.util.TreeMap r1 = new java.util.TreeMap     // Catch:{ all -> 0x0216 }
            r1.<init>()     // Catch:{ all -> 0x0216 }
            java.lang.String r2 = "devid"
            r3 = 0
            java.lang.String r3 = com.xiaomi.push.i.a((android.content.Context) r9, (boolean) r3)     // Catch:{ all -> 0x0216 }
            r1.put(r2, r3)     // Catch:{ all -> 0x0216 }
            java.lang.String r2 = "devid1"
            java.lang.String r3 = com.xiaomi.push.i.a((android.content.Context) r9)     // Catch:{ all -> 0x0216 }
            r1.put(r2, r3)     // Catch:{ all -> 0x0216 }
            com.xiaomi.push.service.k r2 = a     // Catch:{ all -> 0x0216 }
            r3 = 0
            if (r2 == 0) goto L_0x004b
            com.xiaomi.push.service.k r2 = a     // Catch:{ all -> 0x0216 }
            java.lang.String r2 = r2.f912a     // Catch:{ all -> 0x0216 }
            boolean r2 = android.text.TextUtils.isEmpty(r2)     // Catch:{ all -> 0x0216 }
            if (r2 != 0) goto L_0x004b
            java.lang.String r2 = "uuid"
            com.xiaomi.push.service.k r4 = a     // Catch:{ all -> 0x0216 }
            java.lang.String r4 = r4.f912a     // Catch:{ all -> 0x0216 }
            r1.put(r2, r4)     // Catch:{ all -> 0x0216 }
            com.xiaomi.push.service.k r2 = a     // Catch:{ all -> 0x0216 }
            java.lang.String r2 = r2.f912a     // Catch:{ all -> 0x0216 }
            java.lang.String r4 = "/"
            int r2 = r2.lastIndexOf(r4)     // Catch:{ all -> 0x0216 }
            r4 = -1
            if (r2 == r4) goto L_0x004b
            com.xiaomi.push.service.k r4 = a     // Catch:{ all -> 0x0216 }
            java.lang.String r4 = r4.f912a     // Catch:{ all -> 0x0216 }
            int r2 = r2 + 1
            java.lang.String r2 = r4.substring(r2)     // Catch:{ all -> 0x0216 }
            goto L_0x004c
        L_0x004b:
            r2 = r3
        L_0x004c:
            java.lang.String r4 = com.xiaomi.push.i.c(r9)     // Catch:{ all -> 0x0216 }
            if (r4 == 0) goto L_0x0057
            java.lang.String r5 = "vdevid"
            r1.put(r5, r4)     // Catch:{ all -> 0x0216 }
        L_0x0057:
            java.lang.String r4 = com.xiaomi.push.i.b((android.content.Context) r9)     // Catch:{ all -> 0x0216 }
            boolean r5 = android.text.TextUtils.isEmpty(r4)     // Catch:{ all -> 0x0216 }
            if (r5 != 0) goto L_0x0066
            java.lang.String r5 = "gaid"
            r1.put(r5, r4)     // Catch:{ all -> 0x0216 }
        L_0x0066:
            boolean r4 = a((android.content.Context) r9)     // Catch:{ all -> 0x0216 }
            if (r4 == 0) goto L_0x006e
            java.lang.String r11 = "1000271"
        L_0x006e:
            r5 = r11
            boolean r11 = a((android.content.Context) r9)     // Catch:{ all -> 0x0216 }
            if (r11 == 0) goto L_0x0077
            java.lang.String r12 = "420100086271"
        L_0x0077:
            r6 = r12
            boolean r11 = a((android.content.Context) r9)     // Catch:{ all -> 0x0216 }
            if (r11 == 0) goto L_0x0080
            java.lang.String r10 = "com.xiaomi.xmsf"
        L_0x0080:
            r7 = r10
            java.lang.String r10 = "appid"
            r1.put(r10, r5)     // Catch:{ all -> 0x0216 }
            java.lang.String r10 = "apptoken"
            r1.put(r10, r6)     // Catch:{ all -> 0x0216 }
            android.content.pm.PackageManager r10 = r9.getPackageManager()     // Catch:{ Exception -> 0x0096 }
            r11 = 16384(0x4000, float:2.2959E-41)
            android.content.pm.PackageInfo r10 = r10.getPackageInfo(r7, r11)     // Catch:{ Exception -> 0x0096 }
            goto L_0x009b
        L_0x0096:
            r10 = move-exception
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.Throwable) r10)     // Catch:{ all -> 0x0216 }
            r10 = r3
        L_0x009b:
            java.lang.String r11 = "appversion"
            if (r10 == 0) goto L_0x00a6
            int r10 = r10.versionCode     // Catch:{ all -> 0x0216 }
            java.lang.String r10 = java.lang.String.valueOf(r10)     // Catch:{ all -> 0x0216 }
            goto L_0x00a8
        L_0x00a6:
            java.lang.String r10 = "0"
        L_0x00a8:
            r1.put(r11, r10)     // Catch:{ all -> 0x0216 }
            java.lang.String r10 = "sdkversion"
            r11 = 30619(0x779b, float:4.2906E-41)
            java.lang.String r11 = java.lang.Integer.toString(r11)     // Catch:{ all -> 0x0216 }
            r1.put(r10, r11)     // Catch:{ all -> 0x0216 }
            java.lang.String r10 = "packagename"
            r1.put(r10, r7)     // Catch:{ all -> 0x0216 }
            java.lang.String r10 = "model"
            java.lang.String r11 = android.os.Build.MODEL     // Catch:{ all -> 0x0216 }
            r1.put(r10, r11)     // Catch:{ all -> 0x0216 }
            java.lang.String r10 = "board"
            java.lang.String r11 = android.os.Build.BOARD     // Catch:{ all -> 0x0216 }
            r1.put(r10, r11)     // Catch:{ all -> 0x0216 }
            boolean r10 = com.xiaomi.push.l.d()     // Catch:{ all -> 0x0216 }
            if (r10 != 0) goto L_0x011d
            java.lang.String r10 = ""
            java.lang.String r11 = com.xiaomi.push.i.f(r9)     // Catch:{ all -> 0x0216 }
            boolean r12 = android.text.TextUtils.isEmpty(r11)     // Catch:{ all -> 0x0216 }
            if (r12 != 0) goto L_0x00ee
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ all -> 0x0216 }
            r12.<init>()     // Catch:{ all -> 0x0216 }
            r12.append(r10)     // Catch:{ all -> 0x0216 }
            java.lang.String r10 = com.xiaomi.push.ay.a((java.lang.String) r11)     // Catch:{ all -> 0x0216 }
            r12.append(r10)     // Catch:{ all -> 0x0216 }
            java.lang.String r10 = r12.toString()     // Catch:{ all -> 0x0216 }
        L_0x00ee:
            java.lang.String r11 = com.xiaomi.push.i.h(r9)     // Catch:{ all -> 0x0216 }
            boolean r12 = android.text.TextUtils.isEmpty(r10)     // Catch:{ all -> 0x0216 }
            if (r12 != 0) goto L_0x0112
            boolean r12 = android.text.TextUtils.isEmpty(r11)     // Catch:{ all -> 0x0216 }
            if (r12 != 0) goto L_0x0112
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ all -> 0x0216 }
            r12.<init>()     // Catch:{ all -> 0x0216 }
            r12.append(r10)     // Catch:{ all -> 0x0216 }
            java.lang.String r10 = ","
            r12.append(r10)     // Catch:{ all -> 0x0216 }
            r12.append(r11)     // Catch:{ all -> 0x0216 }
            java.lang.String r10 = r12.toString()     // Catch:{ all -> 0x0216 }
        L_0x0112:
            boolean r11 = android.text.TextUtils.isEmpty(r10)     // Catch:{ all -> 0x0216 }
            if (r11 != 0) goto L_0x011d
            java.lang.String r11 = "imei_md5"
            r1.put(r11, r10)     // Catch:{ all -> 0x0216 }
        L_0x011d:
            java.lang.String r10 = "os"
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x0216 }
            r11.<init>()     // Catch:{ all -> 0x0216 }
            java.lang.String r12 = android.os.Build.VERSION.RELEASE     // Catch:{ all -> 0x0216 }
            r11.append(r12)     // Catch:{ all -> 0x0216 }
            java.lang.String r12 = "-"
            r11.append(r12)     // Catch:{ all -> 0x0216 }
            java.lang.String r12 = android.os.Build.VERSION.INCREMENTAL     // Catch:{ all -> 0x0216 }
            r11.append(r12)     // Catch:{ all -> 0x0216 }
            java.lang.String r11 = r11.toString()     // Catch:{ all -> 0x0216 }
            r1.put(r10, r11)     // Catch:{ all -> 0x0216 }
            int r10 = com.xiaomi.push.i.a()     // Catch:{ all -> 0x0216 }
            if (r10 < 0) goto L_0x0149
            java.lang.String r11 = "space_id"
            java.lang.String r10 = java.lang.Integer.toString(r10)     // Catch:{ all -> 0x0216 }
            r1.put(r11, r10)     // Catch:{ all -> 0x0216 }
        L_0x0149:
            java.lang.String r10 = com.xiaomi.push.i.n(r9)     // Catch:{ all -> 0x0216 }
            boolean r11 = android.text.TextUtils.isEmpty(r10)     // Catch:{ all -> 0x0216 }
            if (r11 != 0) goto L_0x015c
            java.lang.String r11 = "mac_address"
            java.lang.String r10 = com.xiaomi.push.ay.a((java.lang.String) r10)     // Catch:{ all -> 0x0216 }
            r1.put(r11, r10)     // Catch:{ all -> 0x0216 }
        L_0x015c:
            java.lang.String r10 = "android_id"
            java.lang.String r11 = com.xiaomi.push.i.e(r9)     // Catch:{ all -> 0x0216 }
            r1.put(r10, r11)     // Catch:{ all -> 0x0216 }
            java.lang.String r10 = "brand"
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ all -> 0x0216 }
            r11.<init>()     // Catch:{ all -> 0x0216 }
            java.lang.String r12 = android.os.Build.BRAND     // Catch:{ all -> 0x0216 }
            r11.append(r12)     // Catch:{ all -> 0x0216 }
            java.lang.String r12 = ""
            r11.append(r12)     // Catch:{ all -> 0x0216 }
            java.lang.String r11 = r11.toString()     // Catch:{ all -> 0x0216 }
            r1.put(r10, r11)     // Catch:{ all -> 0x0216 }
            java.lang.String r10 = a((android.content.Context) r9)     // Catch:{ all -> 0x0216 }
            com.xiaomi.push.aq r10 = com.xiaomi.push.as.a(r9, r10, r1)     // Catch:{ all -> 0x0216 }
            java.lang.String r11 = ""
            if (r10 == 0) goto L_0x018d
            java.lang.String r11 = r10.a()     // Catch:{ all -> 0x0216 }
        L_0x018d:
            boolean r10 = android.text.TextUtils.isEmpty(r11)     // Catch:{ all -> 0x0216 }
            if (r10 != 0) goto L_0x0214
            org.json.JSONObject r10 = new org.json.JSONObject     // Catch:{ all -> 0x0216 }
            r10.<init>(r11)     // Catch:{ all -> 0x0216 }
            java.lang.String r12 = "code"
            int r12 = r10.getInt(r12)     // Catch:{ all -> 0x0216 }
            if (r12 != 0) goto L_0x0202
            java.lang.String r11 = "data"
            org.json.JSONObject r10 = r10.getJSONObject(r11)     // Catch:{ all -> 0x0216 }
            java.lang.String r11 = "ssecurity"
            java.lang.String r4 = r10.getString(r11)     // Catch:{ all -> 0x0216 }
            java.lang.String r11 = "token"
            java.lang.String r3 = r10.getString(r11)     // Catch:{ all -> 0x0216 }
            java.lang.String r11 = "userId"
            java.lang.String r11 = r10.getString(r11)     // Catch:{ all -> 0x0216 }
            boolean r12 = android.text.TextUtils.isEmpty(r2)     // Catch:{ all -> 0x0216 }
            if (r12 == 0) goto L_0x01d4
            java.lang.StringBuilder r12 = new java.lang.StringBuilder     // Catch:{ all -> 0x0216 }
            r12.<init>()     // Catch:{ all -> 0x0216 }
            java.lang.String r1 = "an"
            r12.append(r1)     // Catch:{ all -> 0x0216 }
            r1 = 6
            java.lang.String r1 = com.xiaomi.push.ay.a((int) r1)     // Catch:{ all -> 0x0216 }
            r12.append(r1)     // Catch:{ all -> 0x0216 }
            java.lang.String r2 = r12.toString()     // Catch:{ all -> 0x0216 }
        L_0x01d4:
            com.xiaomi.push.service.k r12 = new com.xiaomi.push.service.k     // Catch:{ all -> 0x0216 }
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x0216 }
            r1.<init>()     // Catch:{ all -> 0x0216 }
            r1.append(r11)     // Catch:{ all -> 0x0216 }
            java.lang.String r11 = "@xiaomi.com/"
            r1.append(r11)     // Catch:{ all -> 0x0216 }
            r1.append(r2)     // Catch:{ all -> 0x0216 }
            java.lang.String r2 = r1.toString()     // Catch:{ all -> 0x0216 }
            int r8 = com.xiaomi.push.ab.a()     // Catch:{ all -> 0x0216 }
            r1 = r12
            r1.<init>(r2, r3, r4, r5, r6, r7, r8)     // Catch:{ all -> 0x0216 }
            a(r9, r12)     // Catch:{ all -> 0x0216 }
            java.lang.String r11 = "vdevid"
            java.lang.String r10 = r10.optString(r11)     // Catch:{ all -> 0x0216 }
            com.xiaomi.push.i.a((android.content.Context) r9, (java.lang.String) r10)     // Catch:{ all -> 0x0216 }
            a = r12     // Catch:{ all -> 0x0216 }
            monitor-exit(r0)
            return r12
        L_0x0202:
            java.lang.String r12 = "code"
            int r12 = r10.getInt(r12)     // Catch:{ all -> 0x0216 }
            java.lang.String r1 = "description"
            java.lang.String r10 = r10.optString(r1)     // Catch:{ all -> 0x0216 }
            com.xiaomi.push.service.o.a(r9, r12, r10)     // Catch:{ all -> 0x0216 }
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r11)     // Catch:{ all -> 0x0216 }
        L_0x0214:
            monitor-exit(r0)
            return r3
        L_0x0216:
            r9 = move-exception
            monitor-exit(r0)
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.service.l.a(android.content.Context, java.lang.String, java.lang.String, java.lang.String):com.xiaomi.push.service.k");
    }

    /* renamed from: a  reason: collision with other method in class */
    public static String m605a(Context context) {
        StringBuilder sb;
        String str;
        String a2 = a.a(context).a();
        if (ab.b()) {
            sb = new StringBuilder();
            sb.append("http://");
            sb.append(fn.b);
            str = ":9085";
        } else if (o.China.name().equals(a2)) {
            sb = new StringBuilder();
            str = "https://cn.register.xmpush.xiaomi.com";
        } else if (o.Global.name().equals(a2)) {
            sb = new StringBuilder();
            str = "https://register.xmpush.global.xiaomi.com";
        } else if (o.Europe.name().equals(a2)) {
            sb = new StringBuilder();
            str = "https://fr.register.xmpush.global.xiaomi.com";
        } else if (o.Russia.name().equals(a2)) {
            sb = new StringBuilder();
            str = "https://ru.register.xmpush.global.xiaomi.com";
        } else if (o.India.name().equals(a2)) {
            sb = new StringBuilder();
            str = "https://idmb.register.xmpush.global.xiaomi.com";
        } else {
            sb = new StringBuilder();
            sb.append("https://");
            str = ab.a() ? "sandbox.xmpush.xiaomi.com" : "register.xmpush.xiaomi.com";
        }
        sb.append(str);
        sb.append("/pass/v2/register");
        return sb.toString();
    }

    public static void a() {
        if (f913a != null) {
            f913a.a();
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static void m606a(Context context) {
        context.getSharedPreferences("mipush_account", 0).edit().clear().commit();
        a = null;
        a();
    }

    public static void a(Context context, k kVar) {
        SharedPreferences.Editor edit = context.getSharedPreferences("mipush_account", 0).edit();
        edit.putString("uuid", kVar.f912a);
        edit.putString("security", kVar.c);
        edit.putString("token", kVar.b);
        edit.putString("app_id", kVar.d);
        edit.putString(Constants.PACKAGE_NAME, kVar.f);
        edit.putString("app_token", kVar.e);
        edit.putString("device_id", i.k(context));
        edit.putInt("env_type", kVar.a);
        edit.commit();
        a();
    }

    public static void a(a aVar) {
        f913a = aVar;
    }

    /* renamed from: a  reason: collision with other method in class */
    private static boolean m607a(Context context) {
        return context.getPackageName().equals("com.xiaomi.xmsf");
    }
}
