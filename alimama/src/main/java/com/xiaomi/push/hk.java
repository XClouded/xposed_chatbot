package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.BitSet;
import java.util.Map;

public class hk implements ir<hk, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 11, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f455a = new jh("ClientUploadDataItem");
    private static final iz b = new iz("", (byte) 11, 2);
    private static final iz c = new iz("", (byte) 11, 3);
    private static final iz d = new iz("", (byte) 10, 4);
    private static final iz e = new iz("", (byte) 10, 5);
    private static final iz f = new iz("", (byte) 2, 6);
    private static final iz g = new iz("", (byte) 11, 7);
    private static final iz h = new iz("", (byte) 11, 8);
    private static final iz i = new iz("", (byte) 11, 9);
    private static final iz j = new iz("", (byte) 13, 10);
    private static final iz k = new iz("", (byte) 11, 11);

    /* renamed from: a  reason: collision with other field name */
    public long f456a;

    /* renamed from: a  reason: collision with other field name */
    public String f457a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f458a = new BitSet(3);

    /* renamed from: a  reason: collision with other field name */
    public Map<String, String> f459a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f460a;

    /* renamed from: b  reason: collision with other field name */
    public long f461b;

    /* renamed from: b  reason: collision with other field name */
    public String f462b;

    /* renamed from: c  reason: collision with other field name */
    public String f463c;

    /* renamed from: d  reason: collision with other field name */
    public String f464d;

    /* renamed from: e  reason: collision with other field name */
    public String f465e;

    /* renamed from: f  reason: collision with other field name */
    public String f466f;

    /* renamed from: g  reason: collision with other field name */
    public String f467g;

    /* renamed from: a */
    public int compareTo(hk hkVar) {
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        int a8;
        int a9;
        int a10;
        int a11;
        int a12;
        if (!getClass().equals(hkVar.getClass())) {
            return getClass().getName().compareTo(hkVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(hkVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a12 = is.a(this.f457a, hkVar.f457a)) != 0) {
            return a12;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(hkVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a11 = is.a(this.f462b, hkVar.f462b)) != 0) {
            return a11;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(hkVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a10 = is.a(this.f463c, hkVar.f463c)) != 0) {
            return a10;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(hkVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a9 = is.a(this.f456a, hkVar.f456a)) != 0) {
            return a9;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(hkVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a8 = is.a(this.f461b, hkVar.f461b)) != 0) {
            return a8;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(hkVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a7 = is.a(this.f460a, hkVar.f460a)) != 0) {
            return a7;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(hkVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a6 = is.a(this.f464d, hkVar.f464d)) != 0) {
            return a6;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(hkVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a5 = is.a(this.f465e, hkVar.f465e)) != 0) {
            return a5;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(hkVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a4 = is.a(this.f466f, hkVar.f466f)) != 0) {
            return a4;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(hkVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a3 = is.a((Map) this.f459a, (Map) hkVar.f459a)) != 0) {
            return a3;
        }
        int compareTo11 = Boolean.valueOf(k()).compareTo(Boolean.valueOf(hkVar.k()));
        if (compareTo11 != 0) {
            return compareTo11;
        }
        if (!k() || (a2 = is.a(this.f467g, hkVar.f467g)) == 0) {
            return 0;
        }
        return a2;
    }

    public long a() {
        return this.f461b;
    }

    public hk a(long j2) {
        this.f456a = j2;
        a(true);
        return this;
    }

    public hk a(String str) {
        this.f457a = str;
        return this;
    }

    public hk a(boolean z) {
        this.f460a = z;
        c(true);
        return this;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m366a() {
        return this.f457a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m367a() {
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(com.xiaomi.push.jc r7) {
        /*
            r6 = this;
            r7.a()
        L_0x0003:
            com.xiaomi.push.iz r0 = r7.a()
            byte r1 = r0.a
            if (r1 != 0) goto L_0x0012
            r7.f()
            r6.a()
            return
        L_0x0012:
            short r1 = r0.f784a
            r2 = 2
            r3 = 10
            r4 = 1
            r5 = 11
            switch(r1) {
                case 1: goto L_0x00c0;
                case 2: goto L_0x00b5;
                case 3: goto L_0x00aa;
                case 4: goto L_0x009c;
                case 5: goto L_0x008e;
                case 6: goto L_0x0080;
                case 7: goto L_0x0075;
                case 8: goto L_0x006a;
                case 9: goto L_0x005f;
                case 10: goto L_0x0030;
                case 11: goto L_0x0024;
                default: goto L_0x001d;
            }
        L_0x001d:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r7, r0)
            goto L_0x00ca
        L_0x0024:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x001d
            java.lang.String r0 = r7.a()
            r6.f467g = r0
            goto L_0x00ca
        L_0x0030:
            byte r1 = r0.a
            r3 = 13
            if (r1 != r3) goto L_0x001d
            com.xiaomi.push.jb r0 = r7.a()
            java.util.HashMap r1 = new java.util.HashMap
            int r3 = r0.f788a
            int r3 = r3 * 2
            r1.<init>(r3)
            r6.f459a = r1
            r1 = 0
        L_0x0046:
            int r2 = r0.f788a
            if (r1 >= r2) goto L_0x005a
            java.lang.String r2 = r7.a()
            java.lang.String r3 = r7.a()
            java.util.Map<java.lang.String, java.lang.String> r4 = r6.f459a
            r4.put(r2, r3)
            int r1 = r1 + 1
            goto L_0x0046
        L_0x005a:
            r7.h()
            goto L_0x00ca
        L_0x005f:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x001d
            java.lang.String r0 = r7.a()
            r6.f466f = r0
            goto L_0x00ca
        L_0x006a:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x001d
            java.lang.String r0 = r7.a()
            r6.f465e = r0
            goto L_0x00ca
        L_0x0075:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x001d
            java.lang.String r0 = r7.a()
            r6.f464d = r0
            goto L_0x00ca
        L_0x0080:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x001d
            boolean r0 = r7.a()
            r6.f460a = r0
            r6.c((boolean) r4)
            goto L_0x00ca
        L_0x008e:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x001d
            long r0 = r7.a()
            r6.f461b = r0
            r6.b((boolean) r4)
            goto L_0x00ca
        L_0x009c:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x001d
            long r0 = r7.a()
            r6.f456a = r0
            r6.a((boolean) r4)
            goto L_0x00ca
        L_0x00aa:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x001d
            java.lang.String r0 = r7.a()
            r6.f463c = r0
            goto L_0x00ca
        L_0x00b5:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x001d
            java.lang.String r0 = r7.a()
            r6.f462b = r0
            goto L_0x00ca
        L_0x00c0:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x001d
            java.lang.String r0 = r7.a()
            r6.f457a = r0
        L_0x00ca:
            r7.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.hk.a(com.xiaomi.push.jc):void");
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m368a(boolean z) {
        this.f458a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m369a() {
        return this.f457a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m370a(hk hkVar) {
        if (hkVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = hkVar.a();
        if ((a2 || a3) && (!a2 || !a3 || !this.f457a.equals(hkVar.f457a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = hkVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f462b.equals(hkVar.f462b))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = hkVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f463c.equals(hkVar.f463c))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = hkVar.d();
        if ((d2 || d3) && (!d2 || !d3 || this.f456a != hkVar.f456a)) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = hkVar.e();
        if ((e2 || e3) && (!e2 || !e3 || this.f461b != hkVar.f461b)) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = hkVar.f();
        if ((f2 || f3) && (!f2 || !f3 || this.f460a != hkVar.f460a)) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = hkVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f464d.equals(hkVar.f464d))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = hkVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f465e.equals(hkVar.f465e))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = hkVar.i();
        if ((i2 || i3) && (!i2 || !i3 || !this.f466f.equals(hkVar.f466f))) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = hkVar.j();
        if ((j2 || j3) && (!j2 || !j3 || !this.f459a.equals(hkVar.f459a))) {
            return false;
        }
        boolean k2 = k();
        boolean k3 = hkVar.k();
        if (k2 || k3) {
            return k2 && k3 && this.f467g.equals(hkVar.f467g);
        }
        return true;
    }

    public hk b(long j2) {
        this.f461b = j2;
        b(true);
        return this;
    }

    public hk b(String str) {
        this.f462b = str;
        return this;
    }

    public String b() {
        return this.f463c;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f455a);
        if (this.f457a != null && a()) {
            jcVar.a(a);
            jcVar.a(this.f457a);
            jcVar.b();
        }
        if (this.f462b != null && b()) {
            jcVar.a(b);
            jcVar.a(this.f462b);
            jcVar.b();
        }
        if (this.f463c != null && c()) {
            jcVar.a(c);
            jcVar.a(this.f463c);
            jcVar.b();
        }
        if (d()) {
            jcVar.a(d);
            jcVar.a(this.f456a);
            jcVar.b();
        }
        if (e()) {
            jcVar.a(e);
            jcVar.a(this.f461b);
            jcVar.b();
        }
        if (f()) {
            jcVar.a(f);
            jcVar.a(this.f460a);
            jcVar.b();
        }
        if (this.f464d != null && g()) {
            jcVar.a(g);
            jcVar.a(this.f464d);
            jcVar.b();
        }
        if (this.f465e != null && h()) {
            jcVar.a(h);
            jcVar.a(this.f465e);
            jcVar.b();
        }
        if (this.f466f != null && i()) {
            jcVar.a(i);
            jcVar.a(this.f466f);
            jcVar.b();
        }
        if (this.f459a != null && j()) {
            jcVar.a(j);
            jcVar.a(new jb((byte) 11, (byte) 11, this.f459a.size()));
            for (Map.Entry next : this.f459a.entrySet()) {
                jcVar.a((String) next.getKey());
                jcVar.a((String) next.getValue());
            }
            jcVar.d();
            jcVar.b();
        }
        if (this.f467g != null && k()) {
            jcVar.a(k);
            jcVar.a(this.f467g);
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public void b(boolean z) {
        this.f458a.set(1, z);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m371b() {
        return this.f462b != null;
    }

    public hk c(String str) {
        this.f463c = str;
        return this;
    }

    public String c() {
        return this.f465e;
    }

    public void c(boolean z) {
        this.f458a.set(2, z);
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m372c() {
        return this.f463c != null;
    }

    public hk d(String str) {
        this.f464d = str;
        return this;
    }

    public String d() {
        return this.f466f;
    }

    /* renamed from: d  reason: collision with other method in class */
    public boolean m373d() {
        return this.f458a.get(0);
    }

    public hk e(String str) {
        this.f465e = str;
        return this;
    }

    public String e() {
        return this.f467g;
    }

    /* renamed from: e  reason: collision with other method in class */
    public boolean m374e() {
        return this.f458a.get(1);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof hk)) {
            return compareTo((hk) obj);
        }
        return false;
    }

    public hk f(String str) {
        this.f466f = str;
        return this;
    }

    public boolean f() {
        return this.f458a.get(2);
    }

    public hk g(String str) {
        this.f467g = str;
        return this;
    }

    public boolean g() {
        return this.f464d != null;
    }

    public boolean h() {
        return this.f465e != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f466f != null;
    }

    public boolean j() {
        return this.f459a != null;
    }

    public boolean k() {
        return this.f467g != null;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("ClientUploadDataItem(");
        if (a()) {
            sb.append("channel:");
            sb.append(this.f457a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f457a);
            z = false;
        } else {
            z = true;
        }
        if (b()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("data:");
            sb.append(this.f462b == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f462b);
            z = false;
        }
        if (c()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("name:");
            sb.append(this.f463c == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f463c);
            z = false;
        }
        if (d()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("counter:");
            sb.append(this.f456a);
            z = false;
        }
        if (e()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("timestamp:");
            sb.append(this.f461b);
            z = false;
        }
        if (f()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("fromSdk:");
            sb.append(this.f460a);
            z = false;
        }
        if (g()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("category:");
            sb.append(this.f464d == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f464d);
            z = false;
        }
        if (h()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("sourcePackage:");
            sb.append(this.f465e == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f465e);
            z = false;
        }
        if (i()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("id:");
            sb.append(this.f466f == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f466f);
            z = false;
        }
        if (j()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("extra:");
            if (this.f459a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f459a);
            }
            z = false;
        }
        if (k()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("pkgName:");
            sb.append(this.f467g == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f467g);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
