package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.BitSet;

public class ih implements ir<ih, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 11, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f666a = new jh("XmPushActionRegistrationResult");
    private static final iz b = new iz("", (byte) 12, 2);
    private static final iz c = new iz("", (byte) 11, 3);
    private static final iz d = new iz("", (byte) 11, 4);
    private static final iz e = new iz("", (byte) 10, 6);
    private static final iz f = new iz("", (byte) 11, 7);
    private static final iz g = new iz("", (byte) 11, 8);
    private static final iz h = new iz("", (byte) 11, 9);
    private static final iz i = new iz("", (byte) 11, 10);
    private static final iz j = new iz("", (byte) 10, 11);
    private static final iz k = new iz("", (byte) 11, 12);
    private static final iz l = new iz("", (byte) 11, 13);
    private static final iz m = new iz("", (byte) 10, 14);
    private static final iz n = new iz("", (byte) 11, 15);
    private static final iz o = new iz("", (byte) 8, 16);
    private static final iz p = new iz("", (byte) 11, 17);
    private static final iz q = new iz("", (byte) 8, 18);
    private static final iz r = new iz("", (byte) 11, 19);

    /* renamed from: a  reason: collision with other field name */
    public int f667a;

    /* renamed from: a  reason: collision with other field name */
    public long f668a;

    /* renamed from: a  reason: collision with other field name */
    public hv f669a;

    /* renamed from: a  reason: collision with other field name */
    public String f670a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f671a = new BitSet(5);

    /* renamed from: b  reason: collision with other field name */
    public int f672b;

    /* renamed from: b  reason: collision with other field name */
    public long f673b;

    /* renamed from: b  reason: collision with other field name */
    public String f674b;

    /* renamed from: c  reason: collision with other field name */
    public long f675c;

    /* renamed from: c  reason: collision with other field name */
    public String f676c;

    /* renamed from: d  reason: collision with other field name */
    public String f677d;

    /* renamed from: e  reason: collision with other field name */
    public String f678e;

    /* renamed from: f  reason: collision with other field name */
    public String f679f;

    /* renamed from: g  reason: collision with other field name */
    public String f680g;

    /* renamed from: h  reason: collision with other field name */
    public String f681h;

    /* renamed from: i  reason: collision with other field name */
    public String f682i;

    /* renamed from: j  reason: collision with other field name */
    public String f683j;

    /* renamed from: k  reason: collision with other field name */
    public String f684k;

    /* renamed from: l  reason: collision with other field name */
    public String f685l;

    /* renamed from: a */
    public int compareTo(ih ihVar) {
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
        if (!getClass().equals(ihVar.getClass())) {
            return getClass().getName().compareTo(ihVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(ihVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a19 = is.a(this.f670a, ihVar.f670a)) != 0) {
            return a19;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(ihVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a18 = is.a((Comparable) this.f669a, (Comparable) ihVar.f669a)) != 0) {
            return a18;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(ihVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a17 = is.a(this.f674b, ihVar.f674b)) != 0) {
            return a17;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(ihVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a16 = is.a(this.f676c, ihVar.f676c)) != 0) {
            return a16;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(ihVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a15 = is.a(this.f668a, ihVar.f668a)) != 0) {
            return a15;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(ihVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a14 = is.a(this.f677d, ihVar.f677d)) != 0) {
            return a14;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(ihVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a13 = is.a(this.f678e, ihVar.f678e)) != 0) {
            return a13;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(ihVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a12 = is.a(this.f679f, ihVar.f679f)) != 0) {
            return a12;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(ihVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a11 = is.a(this.f680g, ihVar.f680g)) != 0) {
            return a11;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(ihVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a10 = is.a(this.f673b, ihVar.f673b)) != 0) {
            return a10;
        }
        int compareTo11 = Boolean.valueOf(k()).compareTo(Boolean.valueOf(ihVar.k()));
        if (compareTo11 != 0) {
            return compareTo11;
        }
        if (k() && (a9 = is.a(this.f681h, ihVar.f681h)) != 0) {
            return a9;
        }
        int compareTo12 = Boolean.valueOf(l()).compareTo(Boolean.valueOf(ihVar.l()));
        if (compareTo12 != 0) {
            return compareTo12;
        }
        if (l() && (a8 = is.a(this.f682i, ihVar.f682i)) != 0) {
            return a8;
        }
        int compareTo13 = Boolean.valueOf(m()).compareTo(Boolean.valueOf(ihVar.m()));
        if (compareTo13 != 0) {
            return compareTo13;
        }
        if (m() && (a7 = is.a(this.f675c, ihVar.f675c)) != 0) {
            return a7;
        }
        int compareTo14 = Boolean.valueOf(n()).compareTo(Boolean.valueOf(ihVar.n()));
        if (compareTo14 != 0) {
            return compareTo14;
        }
        if (n() && (a6 = is.a(this.f683j, ihVar.f683j)) != 0) {
            return a6;
        }
        int compareTo15 = Boolean.valueOf(o()).compareTo(Boolean.valueOf(ihVar.o()));
        if (compareTo15 != 0) {
            return compareTo15;
        }
        if (o() && (a5 = is.a(this.f667a, ihVar.f667a)) != 0) {
            return a5;
        }
        int compareTo16 = Boolean.valueOf(p()).compareTo(Boolean.valueOf(ihVar.p()));
        if (compareTo16 != 0) {
            return compareTo16;
        }
        if (p() && (a4 = is.a(this.f684k, ihVar.f684k)) != 0) {
            return a4;
        }
        int compareTo17 = Boolean.valueOf(q()).compareTo(Boolean.valueOf(ihVar.q()));
        if (compareTo17 != 0) {
            return compareTo17;
        }
        if (q() && (a3 = is.a(this.f672b, ihVar.f672b)) != 0) {
            return a3;
        }
        int compareTo18 = Boolean.valueOf(r()).compareTo(Boolean.valueOf(ihVar.r()));
        if (compareTo18 != 0) {
            return compareTo18;
        }
        if (!r() || (a2 = is.a(this.f685l, ihVar.f685l)) == 0) {
            return 0;
        }
        return a2;
    }

    public long a() {
        return this.f668a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m460a() {
        return this.f674b;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m461a() {
        if (this.f674b == null) {
            throw new jd("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f676c == null) {
            throw new jd("Required field 'appId' was not present! Struct: " + toString());
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
            if (r1 != 0) goto L_0x0033
            r7.f()
            boolean r7 = r6.e()
            if (r7 == 0) goto L_0x0018
            r6.a()
            return
        L_0x0018:
            com.xiaomi.push.jd r7 = new com.xiaomi.push.jd
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Required field 'errorCode' was not found in serialized data! Struct: "
            r0.append(r1)
            java.lang.String r1 = r6.toString()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r7.<init>(r0)
            throw r7
        L_0x0033:
            short r1 = r0.f784a
            r2 = 8
            r3 = 10
            r4 = 1
            r5 = 11
            switch(r1) {
                case 1: goto L_0x0121;
                case 2: goto L_0x010e;
                case 3: goto L_0x0103;
                case 4: goto L_0x00f8;
                case 5: goto L_0x003f;
                case 6: goto L_0x00ea;
                case 7: goto L_0x00df;
                case 8: goto L_0x00d4;
                case 9: goto L_0x00c9;
                case 10: goto L_0x00be;
                case 11: goto L_0x00af;
                case 12: goto L_0x00a3;
                case 13: goto L_0x0097;
                case 14: goto L_0x0088;
                case 15: goto L_0x007c;
                case 16: goto L_0x006d;
                case 17: goto L_0x0061;
                case 18: goto L_0x0052;
                case 19: goto L_0x0046;
                default: goto L_0x003f;
            }
        L_0x003f:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r7, r0)
            goto L_0x012b
        L_0x0046:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x003f
            java.lang.String r0 = r7.a()
            r6.f685l = r0
            goto L_0x012b
        L_0x0052:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x003f
            int r0 = r7.a()
            r6.f672b = r0
            r6.e(r4)
            goto L_0x012b
        L_0x0061:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x003f
            java.lang.String r0 = r7.a()
            r6.f684k = r0
            goto L_0x012b
        L_0x006d:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x003f
            int r0 = r7.a()
            r6.f667a = r0
            r6.d(r4)
            goto L_0x012b
        L_0x007c:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x003f
            java.lang.String r0 = r7.a()
            r6.f683j = r0
            goto L_0x012b
        L_0x0088:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x003f
            long r0 = r7.a()
            r6.f675c = r0
            r6.c(r4)
            goto L_0x012b
        L_0x0097:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x003f
            java.lang.String r0 = r7.a()
            r6.f682i = r0
            goto L_0x012b
        L_0x00a3:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x003f
            java.lang.String r0 = r7.a()
            r6.f681h = r0
            goto L_0x012b
        L_0x00af:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x003f
            long r0 = r7.a()
            r6.f673b = r0
            r6.b((boolean) r4)
            goto L_0x012b
        L_0x00be:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x003f
            java.lang.String r0 = r7.a()
            r6.f680g = r0
            goto L_0x012b
        L_0x00c9:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x003f
            java.lang.String r0 = r7.a()
            r6.f679f = r0
            goto L_0x012b
        L_0x00d4:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x003f
            java.lang.String r0 = r7.a()
            r6.f678e = r0
            goto L_0x012b
        L_0x00df:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x003f
            java.lang.String r0 = r7.a()
            r6.f677d = r0
            goto L_0x012b
        L_0x00ea:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x003f
            long r0 = r7.a()
            r6.f668a = r0
            r6.a((boolean) r4)
            goto L_0x012b
        L_0x00f8:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x003f
            java.lang.String r0 = r7.a()
            r6.f676c = r0
            goto L_0x012b
        L_0x0103:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x003f
            java.lang.String r0 = r7.a()
            r6.f674b = r0
            goto L_0x012b
        L_0x010e:
            byte r1 = r0.a
            r2 = 12
            if (r1 != r2) goto L_0x003f
            com.xiaomi.push.hv r0 = new com.xiaomi.push.hv
            r0.<init>()
            r6.f669a = r0
            com.xiaomi.push.hv r0 = r6.f669a
            r0.a((com.xiaomi.push.jc) r7)
            goto L_0x012b
        L_0x0121:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x003f
            java.lang.String r0 = r7.a()
            r6.f670a = r0
        L_0x012b:
            r7.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.ih.a(com.xiaomi.push.jc):void");
    }

    public void a(boolean z) {
        this.f671a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m462a() {
        return this.f670a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m463a(ih ihVar) {
        if (ihVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = ihVar.a();
        if ((a2 || a3) && (!a2 || !a3 || !this.f670a.equals(ihVar.f670a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = ihVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f669a.compareTo(ihVar.f669a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = ihVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f674b.equals(ihVar.f674b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = ihVar.d();
        if (((d2 || d3) && (!d2 || !d3 || !this.f676c.equals(ihVar.f676c))) || this.f668a != ihVar.f668a) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = ihVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f677d.equals(ihVar.f677d))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = ihVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f678e.equals(ihVar.f678e))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = ihVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f679f.equals(ihVar.f679f))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = ihVar.i();
        if ((i2 || i3) && (!i2 || !i3 || !this.f680g.equals(ihVar.f680g))) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = ihVar.j();
        if ((j2 || j3) && (!j2 || !j3 || this.f673b != ihVar.f673b)) {
            return false;
        }
        boolean k2 = k();
        boolean k3 = ihVar.k();
        if ((k2 || k3) && (!k2 || !k3 || !this.f681h.equals(ihVar.f681h))) {
            return false;
        }
        boolean l2 = l();
        boolean l3 = ihVar.l();
        if ((l2 || l3) && (!l2 || !l3 || !this.f682i.equals(ihVar.f682i))) {
            return false;
        }
        boolean m2 = m();
        boolean m3 = ihVar.m();
        if ((m2 || m3) && (!m2 || !m3 || this.f675c != ihVar.f675c)) {
            return false;
        }
        boolean n2 = n();
        boolean n3 = ihVar.n();
        if ((n2 || n3) && (!n2 || !n3 || !this.f683j.equals(ihVar.f683j))) {
            return false;
        }
        boolean o2 = o();
        boolean o3 = ihVar.o();
        if ((o2 || o3) && (!o2 || !o3 || this.f667a != ihVar.f667a)) {
            return false;
        }
        boolean p2 = p();
        boolean p3 = ihVar.p();
        if ((p2 || p3) && (!p2 || !p3 || !this.f684k.equals(ihVar.f684k))) {
            return false;
        }
        boolean q2 = q();
        boolean q3 = ihVar.q();
        if ((q2 || q3) && (!q2 || !q3 || this.f672b != ihVar.f672b)) {
            return false;
        }
        boolean r2 = r();
        boolean r3 = ihVar.r();
        if (r2 || r3) {
            return r2 && r3 && this.f685l.equals(ihVar.f685l);
        }
        return true;
    }

    public String b() {
        return this.f680g;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f666a);
        if (this.f670a != null && a()) {
            jcVar.a(a);
            jcVar.a(this.f670a);
            jcVar.b();
        }
        if (this.f669a != null && b()) {
            jcVar.a(b);
            this.f669a.b(jcVar);
            jcVar.b();
        }
        if (this.f674b != null) {
            jcVar.a(c);
            jcVar.a(this.f674b);
            jcVar.b();
        }
        if (this.f676c != null) {
            jcVar.a(d);
            jcVar.a(this.f676c);
            jcVar.b();
        }
        jcVar.a(e);
        jcVar.a(this.f668a);
        jcVar.b();
        if (this.f677d != null && f()) {
            jcVar.a(f);
            jcVar.a(this.f677d);
            jcVar.b();
        }
        if (this.f678e != null && g()) {
            jcVar.a(g);
            jcVar.a(this.f678e);
            jcVar.b();
        }
        if (this.f679f != null && h()) {
            jcVar.a(h);
            jcVar.a(this.f679f);
            jcVar.b();
        }
        if (this.f680g != null && i()) {
            jcVar.a(i);
            jcVar.a(this.f680g);
            jcVar.b();
        }
        if (j()) {
            jcVar.a(j);
            jcVar.a(this.f673b);
            jcVar.b();
        }
        if (this.f681h != null && k()) {
            jcVar.a(k);
            jcVar.a(this.f681h);
            jcVar.b();
        }
        if (this.f682i != null && l()) {
            jcVar.a(l);
            jcVar.a(this.f682i);
            jcVar.b();
        }
        if (m()) {
            jcVar.a(m);
            jcVar.a(this.f675c);
            jcVar.b();
        }
        if (this.f683j != null && n()) {
            jcVar.a(n);
            jcVar.a(this.f683j);
            jcVar.b();
        }
        if (o()) {
            jcVar.a(o);
            jcVar.a(this.f667a);
            jcVar.b();
        }
        if (this.f684k != null && p()) {
            jcVar.a(p);
            jcVar.a(this.f684k);
            jcVar.b();
        }
        if (q()) {
            jcVar.a(q);
            jcVar.a(this.f672b);
            jcVar.b();
        }
        if (this.f685l != null && r()) {
            jcVar.a(r);
            jcVar.a(this.f685l);
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public void b(boolean z) {
        this.f671a.set(1, z);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m464b() {
        return this.f669a != null;
    }

    public void c(boolean z) {
        this.f671a.set(2, z);
    }

    public boolean c() {
        return this.f674b != null;
    }

    public void d(boolean z) {
        this.f671a.set(3, z);
    }

    public boolean d() {
        return this.f676c != null;
    }

    public void e(boolean z) {
        this.f671a.set(4, z);
    }

    public boolean e() {
        return this.f671a.get(0);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ih)) {
            return compareTo((ih) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f677d != null;
    }

    public boolean g() {
        return this.f678e != null;
    }

    public boolean h() {
        return this.f679f != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f680g != null;
    }

    public boolean j() {
        return this.f671a.get(1);
    }

    public boolean k() {
        return this.f681h != null;
    }

    public boolean l() {
        return this.f682i != null;
    }

    public boolean m() {
        return this.f671a.get(2);
    }

    public boolean n() {
        return this.f683j != null;
    }

    public boolean o() {
        return this.f671a.get(3);
    }

    public boolean p() {
        return this.f684k != null;
    }

    public boolean q() {
        return this.f671a.get(4);
    }

    public boolean r() {
        return this.f685l != null;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionRegistrationResult(");
        if (a()) {
            sb.append("debug:");
            sb.append(this.f670a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f670a);
            z = false;
        } else {
            z = true;
        }
        if (b()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("target:");
            if (this.f669a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f669a);
            }
            z = false;
        }
        if (!z) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
        }
        sb.append("id:");
        sb.append(this.f674b == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f674b);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("appId:");
        sb.append(this.f676c == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f676c);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("errorCode:");
        sb.append(this.f668a);
        if (f()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("reason:");
            sb.append(this.f677d == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f677d);
        }
        if (g()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("regId:");
            sb.append(this.f678e == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f678e);
        }
        if (h()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("regSecret:");
            sb.append(this.f679f == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f679f);
        }
        if (i()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("packageName:");
            sb.append(this.f680g == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f680g);
        }
        if (j()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("registeredAt:");
            sb.append(this.f673b);
        }
        if (k()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("aliasName:");
            sb.append(this.f681h == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f681h);
        }
        if (l()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("clientId:");
            sb.append(this.f682i == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f682i);
        }
        if (m()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("costTime:");
            sb.append(this.f675c);
        }
        if (n()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("appVersion:");
            sb.append(this.f683j == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f683j);
        }
        if (o()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("pushSdkVersionCode:");
            sb.append(this.f667a);
        }
        if (p()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("hybridPushEndpoint:");
            sb.append(this.f684k == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f684k);
        }
        if (q()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("appVersionCode:");
            sb.append(this.f672b);
        }
        if (r()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("region:");
            sb.append(this.f685l == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f685l);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
