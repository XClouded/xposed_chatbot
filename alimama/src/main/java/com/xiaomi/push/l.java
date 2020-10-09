package com.xiaomi.push;

import android.text.TextUtils;
import com.taobao.weex.common.Constants;
import com.xiaomi.channel.commonutils.logger.b;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class l {
    private static int a = 0;

    /* renamed from: a  reason: collision with other field name */
    private static Map<String, o> f793a = null;
    private static int b = -1;

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0027 A[Catch:{ Throwable -> 0x002c }] */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0028 A[Catch:{ Throwable -> 0x002c }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized int a() {
        /*
            java.lang.Class<com.xiaomi.push.l> r0 = com.xiaomi.push.l.class
            monitor-enter(r0)
            int r1 = a     // Catch:{ all -> 0x004e }
            if (r1 != 0) goto L_0x004a
            r1 = 0
            java.lang.String r2 = "ro.miui.ui.version.code"
            java.lang.String r2 = a((java.lang.String) r2)     // Catch:{ Throwable -> 0x002c }
            boolean r2 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Throwable -> 0x002c }
            r3 = 1
            if (r2 == 0) goto L_0x0024
            java.lang.String r2 = "ro.miui.ui.version.name"
            java.lang.String r2 = a((java.lang.String) r2)     // Catch:{ Throwable -> 0x002c }
            boolean r2 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Throwable -> 0x002c }
            if (r2 != 0) goto L_0x0022
            goto L_0x0024
        L_0x0022:
            r2 = 0
            goto L_0x0025
        L_0x0024:
            r2 = 1
        L_0x0025:
            if (r2 == 0) goto L_0x0028
            goto L_0x0029
        L_0x0028:
            r3 = 2
        L_0x0029:
            a = r3     // Catch:{ Throwable -> 0x002c }
            goto L_0x0034
        L_0x002c:
            r2 = move-exception
            java.lang.String r3 = "get isMIUI failed"
            com.xiaomi.channel.commonutils.logger.b.a((java.lang.String) r3, (java.lang.Throwable) r2)     // Catch:{ all -> 0x004e }
            a = r1     // Catch:{ all -> 0x004e }
        L_0x0034:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder     // Catch:{ all -> 0x004e }
            r1.<init>()     // Catch:{ all -> 0x004e }
            java.lang.String r2 = "isMIUI's value is: "
            r1.append(r2)     // Catch:{ all -> 0x004e }
            int r2 = a     // Catch:{ all -> 0x004e }
            r1.append(r2)     // Catch:{ all -> 0x004e }
            java.lang.String r1 = r1.toString()     // Catch:{ all -> 0x004e }
            com.xiaomi.channel.commonutils.logger.b.b(r1)     // Catch:{ all -> 0x004e }
        L_0x004a:
            int r1 = a     // Catch:{ all -> 0x004e }
            monitor-exit(r0)
            return r1
        L_0x004e:
            r1 = move-exception
            monitor-exit(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.l.a():int");
    }

    public static o a(String str) {
        o b2 = b(str);
        return b2 == null ? o.Global : b2;
    }

    /* renamed from: a  reason: collision with other method in class */
    public static synchronized String m533a() {
        synchronized (l.class) {
            int a2 = t.a();
            return (!a() || a2 <= 0) ? "" : a2 < 2 ? "alpha" : a2 < 3 ? "development" : Constants.Name.STABLE;
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static String m534a(String str) {
        try {
            return (String) at.a("android.os.SystemProperties", "get", str, "");
        } catch (Exception e) {
            b.a((Throwable) e);
        } catch (Throwable unused) {
        }
        return null;
    }

    /* renamed from: a  reason: collision with other method in class */
    private static void m535a() {
        if (f793a == null) {
            f793a = new HashMap();
            f793a.put("CN", o.China);
            f793a.put("FI", o.Europe);
            f793a.put("SE", o.Europe);
            f793a.put("NO", o.Europe);
            f793a.put("FO", o.Europe);
            f793a.put("EE", o.Europe);
            f793a.put("LV", o.Europe);
            f793a.put("LT", o.Europe);
            f793a.put("BY", o.Europe);
            f793a.put("MD", o.Europe);
            f793a.put("UA", o.Europe);
            f793a.put("PL", o.Europe);
            f793a.put("CZ", o.Europe);
            f793a.put("SK", o.Europe);
            f793a.put("HU", o.Europe);
            f793a.put("DE", o.Europe);
            f793a.put("AT", o.Europe);
            f793a.put("CH", o.Europe);
            f793a.put("LI", o.Europe);
            f793a.put("GB", o.Europe);
            f793a.put("IE", o.Europe);
            f793a.put("NL", o.Europe);
            f793a.put("BE", o.Europe);
            f793a.put("LU", o.Europe);
            f793a.put("FR", o.Europe);
            f793a.put("RO", o.Europe);
            f793a.put("BG", o.Europe);
            f793a.put("RS", o.Europe);
            f793a.put("MK", o.Europe);
            f793a.put("AL", o.Europe);
            f793a.put("GR", o.Europe);
            f793a.put("SI", o.Europe);
            f793a.put("HR", o.Europe);
            f793a.put("IT", o.Europe);
            f793a.put("SM", o.Europe);
            f793a.put("MT", o.Europe);
            f793a.put("ES", o.Europe);
            f793a.put("PT", o.Europe);
            f793a.put("AD", o.Europe);
            f793a.put("CY", o.Europe);
            f793a.put("DK", o.Europe);
            f793a.put("RU", o.Russia);
            f793a.put("IN", o.India);
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public static synchronized boolean m536a() {
        boolean z;
        synchronized (l.class) {
            z = true;
            if (a() != 1) {
                z = false;
            }
        }
        return z;
    }

    private static o b(String str) {
        a();
        return f793a.get(str.toUpperCase());
    }

    public static String b() {
        String a2 = s.a("ro.miui.region", "");
        if (TextUtils.isEmpty(a2)) {
            a2 = s.a("persist.sys.oppo.region", "");
        }
        if (TextUtils.isEmpty(a2)) {
            a2 = s.a("ro.oppo.regionmark", "");
        }
        if (TextUtils.isEmpty(a2)) {
            a2 = s.a("ro.hw.country", "");
        }
        if (TextUtils.isEmpty(a2)) {
            a2 = s.a("ro.csc.countryiso_code", "");
        }
        if (TextUtils.isEmpty(a2)) {
            a2 = s.a("ro.product.country.region", "");
        }
        if (TextUtils.isEmpty(a2)) {
            a2 = s.a("gsm.vivo.countrycode", "");
        }
        if (TextUtils.isEmpty(a2)) {
            a2 = s.a("persist.sys.oem.region", "");
        }
        if (TextUtils.isEmpty(a2)) {
            a2 = s.a("ro.product.locale.region", "");
        }
        if (TextUtils.isEmpty(a2)) {
            a2 = s.a("persist.sys.country", "");
        }
        if (!TextUtils.isEmpty(a2)) {
            b.a("get region from system, region = " + a2);
        }
        if (!TextUtils.isEmpty(a2)) {
            return a2;
        }
        String country = Locale.getDefault().getCountry();
        b.a("locale.default.country = " + country);
        return country;
    }

    /* renamed from: b  reason: collision with other method in class */
    public static synchronized boolean m537b() {
        boolean z;
        synchronized (l.class) {
            z = a() == 2;
        }
        return z;
    }

    public static boolean c() {
        if (b < 0) {
            Object a2 = at.a("miui.external.SdkHelper", "isMiuiSystem", new Object[0]);
            b = 0;
            if (a2 != null && (a2 instanceof Boolean) && !Boolean.class.cast(a2).booleanValue()) {
                b = 1;
            }
        }
        return b > 0;
    }

    public static boolean d() {
        return !o.China.name().equalsIgnoreCase(a(b()).name());
    }
}
