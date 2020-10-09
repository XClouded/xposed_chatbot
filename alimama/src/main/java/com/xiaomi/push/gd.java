package com.xiaomi.push;

import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;

public abstract class gd {
    private static long a = 0;

    /* renamed from: a  reason: collision with other field name */
    protected static final String f402a = Locale.getDefault().getLanguage().toLowerCase();

    /* renamed from: a  reason: collision with other field name */
    public static final DateFormat f403a = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static String b = null;
    private static String c = (go.a(5) + "-");

    /* renamed from: a  reason: collision with other field name */
    private gh f404a = null;

    /* renamed from: a  reason: collision with other field name */
    private List<ga> f405a = new CopyOnWriteArrayList();

    /* renamed from: a  reason: collision with other field name */
    private final Map<String, Object> f406a = new HashMap();
    private String d = b;
    private String e = null;
    private String f = null;
    private String g = null;
    private String h = null;
    private String i = null;

    static {
        f403a.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public gd() {
    }

    public gd(Bundle bundle) {
        this.f = bundle.getString("ext_to");
        this.g = bundle.getString("ext_from");
        this.h = bundle.getString("ext_chid");
        this.e = bundle.getString("ext_pkt_id");
        Parcelable[] parcelableArray = bundle.getParcelableArray("ext_exts");
        if (parcelableArray != null) {
            this.f405a = new ArrayList(parcelableArray.length);
            for (Parcelable parcelable : parcelableArray) {
                ga a2 = ga.a((Bundle) parcelable);
                if (a2 != null) {
                    this.f405a.add(a2);
                }
            }
        }
        Bundle bundle2 = bundle.getBundle("ext_ERROR");
        if (bundle2 != null) {
            this.f404a = new gh(bundle2);
        }
    }

    public static synchronized String i() {
        String sb;
        synchronized (gd.class) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(c);
            long j = a;
            a = 1 + j;
            sb2.append(Long.toString(j));
            sb = sb2.toString();
        }
        return sb;
    }

    public static String q() {
        return f402a;
    }

    public Bundle a() {
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(this.d)) {
            bundle.putString("ext_ns", this.d);
        }
        if (!TextUtils.isEmpty(this.g)) {
            bundle.putString("ext_from", this.g);
        }
        if (!TextUtils.isEmpty(this.f)) {
            bundle.putString("ext_to", this.f);
        }
        if (!TextUtils.isEmpty(this.e)) {
            bundle.putString("ext_pkt_id", this.e);
        }
        if (!TextUtils.isEmpty(this.h)) {
            bundle.putString("ext_chid", this.h);
        }
        if (this.f404a != null) {
            bundle.putBundle("ext_ERROR", this.f404a.a());
        }
        if (this.f405a != null) {
            Bundle[] bundleArr = new Bundle[this.f405a.size()];
            int i2 = 0;
            for (ga a2 : this.f405a) {
                Bundle a3 = a2.a();
                if (a3 != null) {
                    bundleArr[i2] = a3;
                    i2++;
                }
            }
            bundle.putParcelableArray("ext_exts", bundleArr);
        }
        return bundle;
    }

    public ga a(String str) {
        return a(str, (String) null);
    }

    public ga a(String str, String str2) {
        for (ga next : this.f405a) {
            if ((str2 == null || str2.equals(next.b())) && str.equals(next.a())) {
                return next;
            }
        }
        return null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public gh m333a() {
        return this.f404a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized Object m334a(String str) {
        if (this.f406a == null) {
            return null;
        }
        return this.f406a.get(str);
    }

    /* renamed from: a  reason: collision with other method in class */
    public abstract String m335a();

    /* renamed from: a  reason: collision with other method in class */
    public synchronized Collection<ga> m336a() {
        if (this.f405a == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(new ArrayList(this.f405a));
    }

    public void a(ga gaVar) {
        this.f405a.add(gaVar);
    }

    public void a(gh ghVar) {
        this.f404a = ghVar;
    }

    public synchronized Collection<String> b() {
        if (this.f406a == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(new HashSet(this.f406a.keySet()));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        gd gdVar = (gd) obj;
        if (this.f404a == null ? gdVar.f404a != null : !this.f404a.equals(gdVar.f404a)) {
            return false;
        }
        if (this.g == null ? gdVar.g != null : !this.g.equals(gdVar.g)) {
            return false;
        }
        if (!this.f405a.equals(gdVar.f405a)) {
            return false;
        }
        if (this.e == null ? gdVar.e != null : !this.e.equals(gdVar.e)) {
            return false;
        }
        if (this.h == null ? gdVar.h != null : !this.h.equals(gdVar.h)) {
            return false;
        }
        if (this.f406a == null ? gdVar.f406a != null : !this.f406a.equals(gdVar.f406a)) {
            return false;
        }
        if (this.f == null ? gdVar.f != null : !this.f.equals(gdVar.f)) {
            return false;
        }
        if (this.d != null) {
            return this.d.equals(gdVar.d);
        }
        if (gdVar.d == null) {
            return true;
        }
    }

    public int hashCode() {
        int i2 = 0;
        int hashCode = (((((((((((((this.d != null ? this.d.hashCode() : 0) * 31) + (this.e != null ? this.e.hashCode() : 0)) * 31) + (this.f != null ? this.f.hashCode() : 0)) * 31) + (this.g != null ? this.g.hashCode() : 0)) * 31) + (this.h != null ? this.h.hashCode() : 0)) * 31) + this.f405a.hashCode()) * 31) + this.f406a.hashCode()) * 31;
        if (this.f404a != null) {
            i2 = this.f404a.hashCode();
        }
        return hashCode + i2;
    }

    public String j() {
        if ("ID_NOT_AVAILABLE".equals(this.e)) {
            return null;
        }
        if (this.e == null) {
            this.e = i();
        }
        return this.e;
    }

    public String k() {
        return this.h;
    }

    public void k(String str) {
        this.e = str;
    }

    public String l() {
        return this.f;
    }

    public void l(String str) {
        this.h = str;
    }

    public String m() {
        return this.g;
    }

    public void m(String str) {
        this.f = str;
    }

    public String n() {
        return this.i;
    }

    public void n(String str) {
        this.g = str;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't wrap try/catch for region: R(14:32|33|34|35|36|37|38|39|40|41|42|60|62|63) */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:64|(0)|(0)|72|73) */
    /* JADX WARNING: Code restructure failed: missing block: B:59:0x010e, code lost:
        if (r4 == null) goto L_0x0111;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:41:0x00f0 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:72:0x0125 */
    /* JADX WARNING: Removed duplicated region for block: B:56:0x0109 A[SYNTHETIC, Splitter:B:56:0x0109] */
    /* JADX WARNING: Removed duplicated region for block: B:66:0x011b A[SYNTHETIC, Splitter:B:66:0x011b] */
    /* JADX WARNING: Removed duplicated region for block: B:70:0x0122 A[SYNTHETIC, Splitter:B:70:0x0122] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public synchronized java.lang.String o() {
        /*
            r6 = this;
            monitor-enter(r6)
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch:{ all -> 0x0131 }
            r0.<init>()     // Catch:{ all -> 0x0131 }
            java.util.Collection r1 = r6.a()     // Catch:{ all -> 0x0131 }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x0131 }
        L_0x000e:
            boolean r2 = r1.hasNext()     // Catch:{ all -> 0x0131 }
            if (r2 == 0) goto L_0x0022
            java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x0131 }
            com.xiaomi.push.ge r2 = (com.xiaomi.push.ge) r2     // Catch:{ all -> 0x0131 }
            java.lang.String r2 = r2.d()     // Catch:{ all -> 0x0131 }
            r0.append(r2)     // Catch:{ all -> 0x0131 }
            goto L_0x000e
        L_0x0022:
            java.util.Map<java.lang.String, java.lang.Object> r1 = r6.f406a     // Catch:{ all -> 0x0131 }
            if (r1 == 0) goto L_0x012b
            java.util.Map<java.lang.String, java.lang.Object> r1 = r6.f406a     // Catch:{ all -> 0x0131 }
            boolean r1 = r1.isEmpty()     // Catch:{ all -> 0x0131 }
            if (r1 != 0) goto L_0x012b
            java.lang.String r1 = "<properties xmlns=\"http://www.jivesoftware.com/xmlns/xmpp/properties\">"
            r0.append(r1)     // Catch:{ all -> 0x0131 }
            java.util.Collection r1 = r6.b()     // Catch:{ all -> 0x0131 }
            java.util.Iterator r1 = r1.iterator()     // Catch:{ all -> 0x0131 }
        L_0x003b:
            boolean r2 = r1.hasNext()     // Catch:{ all -> 0x0131 }
            if (r2 == 0) goto L_0x0126
            java.lang.Object r2 = r1.next()     // Catch:{ all -> 0x0131 }
            java.lang.String r2 = (java.lang.String) r2     // Catch:{ all -> 0x0131 }
            java.lang.Object r3 = r6.a((java.lang.String) r2)     // Catch:{ all -> 0x0131 }
            java.lang.String r4 = "<property>"
            r0.append(r4)     // Catch:{ all -> 0x0131 }
            java.lang.String r4 = "<name>"
            r0.append(r4)     // Catch:{ all -> 0x0131 }
            java.lang.String r2 = com.xiaomi.push.go.a((java.lang.String) r2)     // Catch:{ all -> 0x0131 }
            r0.append(r2)     // Catch:{ all -> 0x0131 }
            java.lang.String r2 = "</name>"
            r0.append(r2)     // Catch:{ all -> 0x0131 }
            java.lang.String r2 = "<value type=\""
            r0.append(r2)     // Catch:{ all -> 0x0131 }
            boolean r2 = r3 instanceof java.lang.Integer     // Catch:{ all -> 0x0131 }
            if (r2 == 0) goto L_0x0079
            java.lang.String r2 = "integer\">"
            r0.append(r2)     // Catch:{ all -> 0x0131 }
            r0.append(r3)     // Catch:{ all -> 0x0131 }
            java.lang.String r2 = "</value>"
        L_0x0074:
            r0.append(r2)     // Catch:{ all -> 0x0131 }
            goto L_0x0111
        L_0x0079:
            boolean r2 = r3 instanceof java.lang.Long     // Catch:{ all -> 0x0131 }
            if (r2 == 0) goto L_0x0088
            java.lang.String r2 = "long\">"
            r0.append(r2)     // Catch:{ all -> 0x0131 }
            r0.append(r3)     // Catch:{ all -> 0x0131 }
            java.lang.String r2 = "</value>"
            goto L_0x0074
        L_0x0088:
            boolean r2 = r3 instanceof java.lang.Float     // Catch:{ all -> 0x0131 }
            if (r2 == 0) goto L_0x0097
            java.lang.String r2 = "float\">"
            r0.append(r2)     // Catch:{ all -> 0x0131 }
            r0.append(r3)     // Catch:{ all -> 0x0131 }
            java.lang.String r2 = "</value>"
            goto L_0x0074
        L_0x0097:
            boolean r2 = r3 instanceof java.lang.Double     // Catch:{ all -> 0x0131 }
            if (r2 == 0) goto L_0x00a6
            java.lang.String r2 = "double\">"
            r0.append(r2)     // Catch:{ all -> 0x0131 }
            r0.append(r3)     // Catch:{ all -> 0x0131 }
            java.lang.String r2 = "</value>"
            goto L_0x0074
        L_0x00a6:
            boolean r2 = r3 instanceof java.lang.Boolean     // Catch:{ all -> 0x0131 }
            if (r2 == 0) goto L_0x00b5
            java.lang.String r2 = "boolean\">"
            r0.append(r2)     // Catch:{ all -> 0x0131 }
            r0.append(r3)     // Catch:{ all -> 0x0131 }
            java.lang.String r2 = "</value>"
            goto L_0x0074
        L_0x00b5:
            boolean r2 = r3 instanceof java.lang.String     // Catch:{ all -> 0x0131 }
            if (r2 == 0) goto L_0x00ca
            java.lang.String r2 = "string\">"
            r0.append(r2)     // Catch:{ all -> 0x0131 }
            java.lang.String r3 = (java.lang.String) r3     // Catch:{ all -> 0x0131 }
            java.lang.String r2 = com.xiaomi.push.go.a((java.lang.String) r3)     // Catch:{ all -> 0x0131 }
            r0.append(r2)     // Catch:{ all -> 0x0131 }
            java.lang.String r2 = "</value>"
            goto L_0x0074
        L_0x00ca:
            r2 = 0
            java.io.ByteArrayOutputStream r4 = new java.io.ByteArrayOutputStream     // Catch:{ Exception -> 0x0100, all -> 0x00fc }
            r4.<init>()     // Catch:{ Exception -> 0x0100, all -> 0x00fc }
            java.io.ObjectOutputStream r5 = new java.io.ObjectOutputStream     // Catch:{ Exception -> 0x00f9, all -> 0x00f6 }
            r5.<init>(r4)     // Catch:{ Exception -> 0x00f9, all -> 0x00f6 }
            r5.writeObject(r3)     // Catch:{ Exception -> 0x00f4 }
            java.lang.String r2 = "java-object\">"
            r0.append(r2)     // Catch:{ Exception -> 0x00f4 }
            byte[] r2 = r4.toByteArray()     // Catch:{ Exception -> 0x00f4 }
            java.lang.String r2 = com.xiaomi.push.go.a((byte[]) r2)     // Catch:{ Exception -> 0x00f4 }
            r0.append(r2)     // Catch:{ Exception -> 0x00f4 }
            java.lang.String r2 = "</value>"
            r0.append(r2)     // Catch:{ Exception -> 0x00f4 }
            r5.close()     // Catch:{ Exception -> 0x00f0 }
        L_0x00f0:
            r4.close()     // Catch:{ Exception -> 0x0111 }
            goto L_0x0111
        L_0x00f4:
            r2 = move-exception
            goto L_0x0104
        L_0x00f6:
            r0 = move-exception
            r5 = r2
            goto L_0x0119
        L_0x00f9:
            r3 = move-exception
            r5 = r2
            goto L_0x0103
        L_0x00fc:
            r0 = move-exception
            r4 = r2
            r5 = r4
            goto L_0x0119
        L_0x0100:
            r3 = move-exception
            r4 = r2
            r5 = r4
        L_0x0103:
            r2 = r3
        L_0x0104:
            r2.printStackTrace()     // Catch:{ all -> 0x0118 }
            if (r5 == 0) goto L_0x010e
            r5.close()     // Catch:{ Exception -> 0x010d }
            goto L_0x010e
        L_0x010d:
        L_0x010e:
            if (r4 == 0) goto L_0x0111
            goto L_0x00f0
        L_0x0111:
            java.lang.String r2 = "</property>"
            r0.append(r2)     // Catch:{ all -> 0x0131 }
            goto L_0x003b
        L_0x0118:
            r0 = move-exception
        L_0x0119:
            if (r5 == 0) goto L_0x0120
            r5.close()     // Catch:{ Exception -> 0x011f }
            goto L_0x0120
        L_0x011f:
        L_0x0120:
            if (r4 == 0) goto L_0x0125
            r4.close()     // Catch:{ Exception -> 0x0125 }
        L_0x0125:
            throw r0     // Catch:{ all -> 0x0131 }
        L_0x0126:
            java.lang.String r1 = "</properties>"
            r0.append(r1)     // Catch:{ all -> 0x0131 }
        L_0x012b:
            java.lang.String r0 = r0.toString()     // Catch:{ all -> 0x0131 }
            monitor-exit(r6)
            return r0
        L_0x0131:
            r0 = move-exception
            monitor-exit(r6)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.gd.o():java.lang.String");
    }

    public void o(String str) {
        this.i = str;
    }

    public String p() {
        return this.d;
    }
}
