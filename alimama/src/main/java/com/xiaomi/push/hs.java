package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.BitSet;

public class hs implements ir<hs, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 12, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f495a = new jh("PushMessage");
    private static final iz b = new iz("", (byte) 11, 2);
    private static final iz c = new iz("", (byte) 11, 3);
    private static final iz d = new iz("", (byte) 11, 4);
    private static final iz e = new iz("", (byte) 10, 5);
    private static final iz f = new iz("", (byte) 10, 6);
    private static final iz g = new iz("", (byte) 11, 7);
    private static final iz h = new iz("", (byte) 11, 8);
    private static final iz i = new iz("", (byte) 11, 9);
    private static final iz j = new iz("", (byte) 11, 10);
    private static final iz k = new iz("", (byte) 11, 11);
    private static final iz l = new iz("", (byte) 12, 12);
    private static final iz m = new iz("", (byte) 11, 13);
    private static final iz n = new iz("", (byte) 2, 14);
    private static final iz o = new iz("", (byte) 11, 15);
    private static final iz p = new iz("", (byte) 10, 16);
    private static final iz q = new iz("", (byte) 11, 20);
    private static final iz r = new iz("", (byte) 11, 21);

    /* renamed from: a  reason: collision with other field name */
    public long f496a;

    /* renamed from: a  reason: collision with other field name */
    public ht f497a;

    /* renamed from: a  reason: collision with other field name */
    public hv f498a;

    /* renamed from: a  reason: collision with other field name */
    public String f499a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f500a = new BitSet(4);

    /* renamed from: a  reason: collision with other field name */
    public boolean f501a = false;

    /* renamed from: b  reason: collision with other field name */
    public long f502b;

    /* renamed from: b  reason: collision with other field name */
    public String f503b;

    /* renamed from: c  reason: collision with other field name */
    public long f504c;

    /* renamed from: c  reason: collision with other field name */
    public String f505c;

    /* renamed from: d  reason: collision with other field name */
    public String f506d;

    /* renamed from: e  reason: collision with other field name */
    public String f507e;

    /* renamed from: f  reason: collision with other field name */
    public String f508f;

    /* renamed from: g  reason: collision with other field name */
    public String f509g;

    /* renamed from: h  reason: collision with other field name */
    public String f510h;

    /* renamed from: i  reason: collision with other field name */
    public String f511i;

    /* renamed from: j  reason: collision with other field name */
    public String f512j;

    /* renamed from: k  reason: collision with other field name */
    public String f513k;

    /* renamed from: l  reason: collision with other field name */
    public String f514l;

    /* renamed from: a */
    public int compareTo(hs hsVar) {
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
        int a13;
        int a14;
        int a15;
        int a16;
        int a17;
        int a18;
        int a19;
        if (!getClass().equals(hsVar.getClass())) {
            return getClass().getName().compareTo(hsVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(hsVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a19 = is.a((Comparable) this.f498a, (Comparable) hsVar.f498a)) != 0) {
            return a19;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(hsVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a18 = is.a(this.f499a, hsVar.f499a)) != 0) {
            return a18;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(hsVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a17 = is.a(this.f503b, hsVar.f503b)) != 0) {
            return a17;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(hsVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a16 = is.a(this.f505c, hsVar.f505c)) != 0) {
            return a16;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(hsVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a15 = is.a(this.f496a, hsVar.f496a)) != 0) {
            return a15;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(hsVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a14 = is.a(this.f502b, hsVar.f502b)) != 0) {
            return a14;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(hsVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a13 = is.a(this.f506d, hsVar.f506d)) != 0) {
            return a13;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(hsVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a12 = is.a(this.f507e, hsVar.f507e)) != 0) {
            return a12;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(hsVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a11 = is.a(this.f508f, hsVar.f508f)) != 0) {
            return a11;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(hsVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a10 = is.a(this.f509g, hsVar.f509g)) != 0) {
            return a10;
        }
        int compareTo11 = Boolean.valueOf(k()).compareTo(Boolean.valueOf(hsVar.k()));
        if (compareTo11 != 0) {
            return compareTo11;
        }
        if (k() && (a9 = is.a(this.f510h, hsVar.f510h)) != 0) {
            return a9;
        }
        int compareTo12 = Boolean.valueOf(l()).compareTo(Boolean.valueOf(hsVar.l()));
        if (compareTo12 != 0) {
            return compareTo12;
        }
        if (l() && (a8 = is.a((Comparable) this.f497a, (Comparable) hsVar.f497a)) != 0) {
            return a8;
        }
        int compareTo13 = Boolean.valueOf(m()).compareTo(Boolean.valueOf(hsVar.m()));
        if (compareTo13 != 0) {
            return compareTo13;
        }
        if (m() && (a7 = is.a(this.f511i, hsVar.f511i)) != 0) {
            return a7;
        }
        int compareTo14 = Boolean.valueOf(n()).compareTo(Boolean.valueOf(hsVar.n()));
        if (compareTo14 != 0) {
            return compareTo14;
        }
        if (n() && (a6 = is.a(this.f501a, hsVar.f501a)) != 0) {
            return a6;
        }
        int compareTo15 = Boolean.valueOf(o()).compareTo(Boolean.valueOf(hsVar.o()));
        if (compareTo15 != 0) {
            return compareTo15;
        }
        if (o() && (a5 = is.a(this.f512j, hsVar.f512j)) != 0) {
            return a5;
        }
        int compareTo16 = Boolean.valueOf(p()).compareTo(Boolean.valueOf(hsVar.p()));
        if (compareTo16 != 0) {
            return compareTo16;
        }
        if (p() && (a4 = is.a(this.f504c, hsVar.f504c)) != 0) {
            return a4;
        }
        int compareTo17 = Boolean.valueOf(q()).compareTo(Boolean.valueOf(hsVar.q()));
        if (compareTo17 != 0) {
            return compareTo17;
        }
        if (q() && (a3 = is.a(this.f513k, hsVar.f513k)) != 0) {
            return a3;
        }
        int compareTo18 = Boolean.valueOf(r()).compareTo(Boolean.valueOf(hsVar.r()));
        if (compareTo18 != 0) {
            return compareTo18;
        }
        if (!r() || (a2 = is.a(this.f514l, hsVar.f514l)) == 0) {
            return 0;
        }
        return a2;
    }

    public long a() {
        return this.f496a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m389a() {
        return this.f499a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m390a() {
        if (this.f499a == null) {
            throw new jd("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f503b == null) {
            throw new jd("Required field 'appId' was not present! Struct: " + toString());
        } else if (this.f505c == null) {
            throw new jd("Required field 'payload' was not present! Struct: " + toString());
        }
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
            r2 = 12
            r3 = 10
            r4 = 1
            r5 = 11
            switch(r1) {
                case 1: goto L_0x00fc;
                case 2: goto L_0x00f1;
                case 3: goto L_0x00e6;
                case 4: goto L_0x00db;
                case 5: goto L_0x00cd;
                case 6: goto L_0x00bf;
                case 7: goto L_0x00b4;
                case 8: goto L_0x00a9;
                case 9: goto L_0x009e;
                case 10: goto L_0x0092;
                case 11: goto L_0x0086;
                case 12: goto L_0x0074;
                case 13: goto L_0x0068;
                case 14: goto L_0x0058;
                case 15: goto L_0x004c;
                case 16: goto L_0x003d;
                case 17: goto L_0x001e;
                case 18: goto L_0x001e;
                case 19: goto L_0x001e;
                case 20: goto L_0x0031;
                case 21: goto L_0x0025;
                default: goto L_0x001e;
            }
        L_0x001e:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r7, r0)
            goto L_0x010c
        L_0x0025:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x001e
            java.lang.String r0 = r7.a()
            r6.f514l = r0
            goto L_0x010c
        L_0x0031:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x001e
            java.lang.String r0 = r7.a()
            r6.f513k = r0
            goto L_0x010c
        L_0x003d:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x001e
            long r0 = r7.a()
            r6.f504c = r0
            r6.d(r4)
            goto L_0x010c
        L_0x004c:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x001e
            java.lang.String r0 = r7.a()
            r6.f512j = r0
            goto L_0x010c
        L_0x0058:
            byte r1 = r0.a
            r2 = 2
            if (r1 != r2) goto L_0x001e
            boolean r0 = r7.a()
            r6.f501a = r0
            r6.c(r4)
            goto L_0x010c
        L_0x0068:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x001e
            java.lang.String r0 = r7.a()
            r6.f511i = r0
            goto L_0x010c
        L_0x0074:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x001e
            com.xiaomi.push.ht r0 = new com.xiaomi.push.ht
            r0.<init>()
            r6.f497a = r0
            com.xiaomi.push.ht r0 = r6.f497a
            r0.a((com.xiaomi.push.jc) r7)
            goto L_0x010c
        L_0x0086:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x001e
            java.lang.String r0 = r7.a()
            r6.f510h = r0
            goto L_0x010c
        L_0x0092:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x001e
            java.lang.String r0 = r7.a()
            r6.f509g = r0
            goto L_0x010c
        L_0x009e:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x001e
            java.lang.String r0 = r7.a()
            r6.f508f = r0
            goto L_0x010c
        L_0x00a9:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x001e
            java.lang.String r0 = r7.a()
            r6.f507e = r0
            goto L_0x010c
        L_0x00b4:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x001e
            java.lang.String r0 = r7.a()
            r6.f506d = r0
            goto L_0x010c
        L_0x00bf:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x001e
            long r0 = r7.a()
            r6.f502b = r0
            r6.b((boolean) r4)
            goto L_0x010c
        L_0x00cd:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x001e
            long r0 = r7.a()
            r6.f496a = r0
            r6.a((boolean) r4)
            goto L_0x010c
        L_0x00db:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x001e
            java.lang.String r0 = r7.a()
            r6.f505c = r0
            goto L_0x010c
        L_0x00e6:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x001e
            java.lang.String r0 = r7.a()
            r6.f503b = r0
            goto L_0x010c
        L_0x00f1:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x001e
            java.lang.String r0 = r7.a()
            r6.f499a = r0
            goto L_0x010c
        L_0x00fc:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x001e
            com.xiaomi.push.hv r0 = new com.xiaomi.push.hv
            r0.<init>()
            r6.f498a = r0
            com.xiaomi.push.hv r0 = r6.f498a
            r0.a((com.xiaomi.push.jc) r7)
        L_0x010c:
            r7.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.hs.a(com.xiaomi.push.jc):void");
    }

    public void a(boolean z) {
        this.f500a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m391a() {
        return this.f498a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m392a(hs hsVar) {
        if (hsVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = hsVar.a();
        if ((a2 || a3) && (!a2 || !a3 || !this.f498a.compareTo(hsVar.f498a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = hsVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f499a.equals(hsVar.f499a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = hsVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f503b.equals(hsVar.f503b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = hsVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f505c.equals(hsVar.f505c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = hsVar.e();
        if ((e2 || e3) && (!e2 || !e3 || this.f496a != hsVar.f496a)) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = hsVar.f();
        if ((f2 || f3) && (!f2 || !f3 || this.f502b != hsVar.f502b)) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = hsVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f506d.equals(hsVar.f506d))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = hsVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f507e.equals(hsVar.f507e))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = hsVar.i();
        if ((i2 || i3) && (!i2 || !i3 || !this.f508f.equals(hsVar.f508f))) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = hsVar.j();
        if ((j2 || j3) && (!j2 || !j3 || !this.f509g.equals(hsVar.f509g))) {
            return false;
        }
        boolean k2 = k();
        boolean k3 = hsVar.k();
        if ((k2 || k3) && (!k2 || !k3 || !this.f510h.equals(hsVar.f510h))) {
            return false;
        }
        boolean l2 = l();
        boolean l3 = hsVar.l();
        if ((l2 || l3) && (!l2 || !l3 || !this.f497a.compareTo(hsVar.f497a))) {
            return false;
        }
        boolean m2 = m();
        boolean m3 = hsVar.m();
        if ((m2 || m3) && (!m2 || !m3 || !this.f511i.equals(hsVar.f511i))) {
            return false;
        }
        boolean n2 = n();
        boolean n3 = hsVar.n();
        if ((n2 || n3) && (!n2 || !n3 || this.f501a != hsVar.f501a)) {
            return false;
        }
        boolean o2 = o();
        boolean o3 = hsVar.o();
        if ((o2 || o3) && (!o2 || !o3 || !this.f512j.equals(hsVar.f512j))) {
            return false;
        }
        boolean p2 = p();
        boolean p3 = hsVar.p();
        if ((p2 || p3) && (!p2 || !p3 || this.f504c != hsVar.f504c)) {
            return false;
        }
        boolean q2 = q();
        boolean q3 = hsVar.q();
        if ((q2 || q3) && (!q2 || !q3 || !this.f513k.equals(hsVar.f513k))) {
            return false;
        }
        boolean r2 = r();
        boolean r3 = hsVar.r();
        if (r2 || r3) {
            return r2 && r3 && this.f514l.equals(hsVar.f514l);
        }
        return true;
    }

    public String b() {
        return this.f503b;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f495a);
        if (this.f498a != null && a()) {
            jcVar.a(a);
            this.f498a.b(jcVar);
            jcVar.b();
        }
        if (this.f499a != null) {
            jcVar.a(b);
            jcVar.a(this.f499a);
            jcVar.b();
        }
        if (this.f503b != null) {
            jcVar.a(c);
            jcVar.a(this.f503b);
            jcVar.b();
        }
        if (this.f505c != null) {
            jcVar.a(d);
            jcVar.a(this.f505c);
            jcVar.b();
        }
        if (e()) {
            jcVar.a(e);
            jcVar.a(this.f496a);
            jcVar.b();
        }
        if (f()) {
            jcVar.a(f);
            jcVar.a(this.f502b);
            jcVar.b();
        }
        if (this.f506d != null && g()) {
            jcVar.a(g);
            jcVar.a(this.f506d);
            jcVar.b();
        }
        if (this.f507e != null && h()) {
            jcVar.a(h);
            jcVar.a(this.f507e);
            jcVar.b();
        }
        if (this.f508f != null && i()) {
            jcVar.a(i);
            jcVar.a(this.f508f);
            jcVar.b();
        }
        if (this.f509g != null && j()) {
            jcVar.a(j);
            jcVar.a(this.f509g);
            jcVar.b();
        }
        if (this.f510h != null && k()) {
            jcVar.a(k);
            jcVar.a(this.f510h);
            jcVar.b();
        }
        if (this.f497a != null && l()) {
            jcVar.a(l);
            this.f497a.b(jcVar);
            jcVar.b();
        }
        if (this.f511i != null && m()) {
            jcVar.a(m);
            jcVar.a(this.f511i);
            jcVar.b();
        }
        if (n()) {
            jcVar.a(n);
            jcVar.a(this.f501a);
            jcVar.b();
        }
        if (this.f512j != null && o()) {
            jcVar.a(o);
            jcVar.a(this.f512j);
            jcVar.b();
        }
        if (p()) {
            jcVar.a(p);
            jcVar.a(this.f504c);
            jcVar.b();
        }
        if (this.f513k != null && q()) {
            jcVar.a(q);
            jcVar.a(this.f513k);
            jcVar.b();
        }
        if (this.f514l != null && r()) {
            jcVar.a(r);
            jcVar.a(this.f514l);
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public void b(boolean z) {
        this.f500a.set(1, z);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m393b() {
        return this.f499a != null;
    }

    public String c() {
        return this.f505c;
    }

    public void c(boolean z) {
        this.f500a.set(2, z);
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m394c() {
        return this.f503b != null;
    }

    public void d(boolean z) {
        this.f500a.set(3, z);
    }

    public boolean d() {
        return this.f505c != null;
    }

    public boolean e() {
        return this.f500a.get(0);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof hs)) {
            return compareTo((hs) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f500a.get(1);
    }

    public boolean g() {
        return this.f506d != null;
    }

    public boolean h() {
        return this.f507e != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f508f != null;
    }

    public boolean j() {
        return this.f509g != null;
    }

    public boolean k() {
        return this.f510h != null;
    }

    public boolean l() {
        return this.f497a != null;
    }

    public boolean m() {
        return this.f511i != null;
    }

    public boolean n() {
        return this.f500a.get(2);
    }

    public boolean o() {
        return this.f512j != null;
    }

    public boolean p() {
        return this.f500a.get(3);
    }

    public boolean q() {
        return this.f513k != null;
    }

    public boolean r() {
        return this.f514l != null;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("PushMessage(");
        if (a()) {
            sb.append("to:");
            if (this.f498a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f498a);
            }
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
        }
        sb.append("id:");
        sb.append(this.f499a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f499a);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("appId:");
        sb.append(this.f503b == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f503b);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("payload:");
        sb.append(this.f505c == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f505c);
        if (e()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("createAt:");
            sb.append(this.f496a);
        }
        if (f()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("ttl:");
            sb.append(this.f502b);
        }
        if (g()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("collapseKey:");
            sb.append(this.f506d == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f506d);
        }
        if (h()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("packageName:");
            sb.append(this.f507e == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f507e);
        }
        if (i()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("regId:");
            sb.append(this.f508f == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f508f);
        }
        if (j()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("category:");
            sb.append(this.f509g == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f509g);
        }
        if (k()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("topic:");
            sb.append(this.f510h == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f510h);
        }
        if (l()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("metaInfo:");
            if (this.f497a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f497a);
            }
        }
        if (m()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("aliasName:");
            sb.append(this.f511i == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f511i);
        }
        if (n()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("isOnline:");
            sb.append(this.f501a);
        }
        if (o()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("userAccount:");
            sb.append(this.f512j == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f512j);
        }
        if (p()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("miid:");
            sb.append(this.f504c);
        }
        if (q()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("imeiMd5:");
            sb.append(this.f513k == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f513k);
        }
        if (r()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("deviceId:");
            sb.append(this.f514l == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f514l);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
