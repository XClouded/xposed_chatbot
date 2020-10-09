package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.BitSet;
import java.util.Map;

public class hw implements ir<hw, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 11, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f540a = new jh("XmPushActionAckMessage");
    private static final iz b = new iz("", (byte) 12, 2);
    private static final iz c = new iz("", (byte) 11, 3);
    private static final iz d = new iz("", (byte) 11, 4);
    private static final iz e = new iz("", (byte) 10, 5);
    private static final iz f = new iz("", (byte) 11, 6);
    private static final iz g = new iz("", (byte) 11, 7);
    private static final iz h = new iz("", (byte) 12, 8);
    private static final iz i = new iz("", (byte) 11, 9);
    private static final iz j = new iz("", (byte) 11, 10);
    private static final iz k = new iz("", (byte) 2, 11);
    private static final iz l = new iz("", (byte) 11, 12);
    private static final iz m = new iz("", (byte) 11, 13);
    private static final iz n = new iz("", (byte) 11, 14);
    private static final iz o = new iz("", (byte) 6, 15);
    private static final iz p = new iz("", (byte) 6, 16);
    private static final iz q = new iz("", (byte) 11, 20);
    private static final iz r = new iz("", (byte) 11, 21);
    private static final iz s = new iz("", (byte) 8, 22);
    private static final iz t = new iz("", (byte) 13, 23);

    /* renamed from: a  reason: collision with other field name */
    public int f541a;

    /* renamed from: a  reason: collision with other field name */
    public long f542a;

    /* renamed from: a  reason: collision with other field name */
    public hv f543a;

    /* renamed from: a  reason: collision with other field name */
    public ij f544a;

    /* renamed from: a  reason: collision with other field name */
    public String f545a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f546a = new BitSet(5);

    /* renamed from: a  reason: collision with other field name */
    public Map<String, String> f547a;

    /* renamed from: a  reason: collision with other field name */
    public short f548a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f549a = false;

    /* renamed from: b  reason: collision with other field name */
    public String f550b;

    /* renamed from: b  reason: collision with other field name */
    public short f551b;

    /* renamed from: c  reason: collision with other field name */
    public String f552c;

    /* renamed from: d  reason: collision with other field name */
    public String f553d;

    /* renamed from: e  reason: collision with other field name */
    public String f554e;

    /* renamed from: f  reason: collision with other field name */
    public String f555f;

    /* renamed from: g  reason: collision with other field name */
    public String f556g;

    /* renamed from: h  reason: collision with other field name */
    public String f557h;

    /* renamed from: i  reason: collision with other field name */
    public String f558i;

    /* renamed from: j  reason: collision with other field name */
    public String f559j;

    /* renamed from: k  reason: collision with other field name */
    public String f560k;

    /* renamed from: l  reason: collision with other field name */
    public String f561l;

    /* renamed from: a */
    public int compareTo(hw hwVar) {
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
        int a20;
        int a21;
        if (!getClass().equals(hwVar.getClass())) {
            return getClass().getName().compareTo(hwVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(hwVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a21 = is.a(this.f545a, hwVar.f545a)) != 0) {
            return a21;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(hwVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a20 = is.a((Comparable) this.f543a, (Comparable) hwVar.f543a)) != 0) {
            return a20;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(hwVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a19 = is.a(this.f550b, hwVar.f550b)) != 0) {
            return a19;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(hwVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a18 = is.a(this.f552c, hwVar.f552c)) != 0) {
            return a18;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(hwVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a17 = is.a(this.f542a, hwVar.f542a)) != 0) {
            return a17;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(hwVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a16 = is.a(this.f553d, hwVar.f553d)) != 0) {
            return a16;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(hwVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a15 = is.a(this.f554e, hwVar.f554e)) != 0) {
            return a15;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(hwVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a14 = is.a((Comparable) this.f544a, (Comparable) hwVar.f544a)) != 0) {
            return a14;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(hwVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a13 = is.a(this.f555f, hwVar.f555f)) != 0) {
            return a13;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(hwVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a12 = is.a(this.f556g, hwVar.f556g)) != 0) {
            return a12;
        }
        int compareTo11 = Boolean.valueOf(k()).compareTo(Boolean.valueOf(hwVar.k()));
        if (compareTo11 != 0) {
            return compareTo11;
        }
        if (k() && (a11 = is.a(this.f549a, hwVar.f549a)) != 0) {
            return a11;
        }
        int compareTo12 = Boolean.valueOf(l()).compareTo(Boolean.valueOf(hwVar.l()));
        if (compareTo12 != 0) {
            return compareTo12;
        }
        if (l() && (a10 = is.a(this.f557h, hwVar.f557h)) != 0) {
            return a10;
        }
        int compareTo13 = Boolean.valueOf(m()).compareTo(Boolean.valueOf(hwVar.m()));
        if (compareTo13 != 0) {
            return compareTo13;
        }
        if (m() && (a9 = is.a(this.f558i, hwVar.f558i)) != 0) {
            return a9;
        }
        int compareTo14 = Boolean.valueOf(n()).compareTo(Boolean.valueOf(hwVar.n()));
        if (compareTo14 != 0) {
            return compareTo14;
        }
        if (n() && (a8 = is.a(this.f559j, hwVar.f559j)) != 0) {
            return a8;
        }
        int compareTo15 = Boolean.valueOf(o()).compareTo(Boolean.valueOf(hwVar.o()));
        if (compareTo15 != 0) {
            return compareTo15;
        }
        if (o() && (a7 = is.a(this.f548a, hwVar.f548a)) != 0) {
            return a7;
        }
        int compareTo16 = Boolean.valueOf(p()).compareTo(Boolean.valueOf(hwVar.p()));
        if (compareTo16 != 0) {
            return compareTo16;
        }
        if (p() && (a6 = is.a(this.f551b, hwVar.f551b)) != 0) {
            return a6;
        }
        int compareTo17 = Boolean.valueOf(q()).compareTo(Boolean.valueOf(hwVar.q()));
        if (compareTo17 != 0) {
            return compareTo17;
        }
        if (q() && (a5 = is.a(this.f560k, hwVar.f560k)) != 0) {
            return a5;
        }
        int compareTo18 = Boolean.valueOf(r()).compareTo(Boolean.valueOf(hwVar.r()));
        if (compareTo18 != 0) {
            return compareTo18;
        }
        if (r() && (a4 = is.a(this.f561l, hwVar.f561l)) != 0) {
            return a4;
        }
        int compareTo19 = Boolean.valueOf(s()).compareTo(Boolean.valueOf(hwVar.s()));
        if (compareTo19 != 0) {
            return compareTo19;
        }
        if (s() && (a3 = is.a(this.f541a, hwVar.f541a)) != 0) {
            return a3;
        }
        int compareTo20 = Boolean.valueOf(t()).compareTo(Boolean.valueOf(hwVar.t()));
        if (compareTo20 != 0) {
            return compareTo20;
        }
        if (!t() || (a2 = is.a((Map) this.f547a, (Map) hwVar.f547a)) == 0) {
            return 0;
        }
        return a2;
    }

    public hw a(long j2) {
        this.f542a = j2;
        a(true);
        return this;
    }

    public hw a(String str) {
        this.f550b = str;
        return this;
    }

    public hw a(short s2) {
        this.f548a = s2;
        c(true);
        return this;
    }

    public void a() {
        if (this.f550b == null) {
            throw new jd("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f552c == null) {
            throw new jd("Required field 'appId' was not present! Struct: " + toString());
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(com.xiaomi.push.jc r8) {
        /*
            r7 = this;
            r8.a()
        L_0x0003:
            com.xiaomi.push.iz r0 = r8.a()
            byte r1 = r0.a
            if (r1 != 0) goto L_0x0033
            r8.f()
            boolean r8 = r7.e()
            if (r8 == 0) goto L_0x0018
            r7.a()
            return
        L_0x0018:
            com.xiaomi.push.jd r8 = new com.xiaomi.push.jd
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Required field 'messageTs' was not found in serialized data! Struct: "
            r0.append(r1)
            java.lang.String r1 = r7.toString()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r8.<init>(r0)
            throw r8
        L_0x0033:
            short r1 = r0.f784a
            r2 = 6
            r3 = 2
            r4 = 12
            r5 = 1
            r6 = 11
            switch(r1) {
                case 1: goto L_0x0165;
                case 2: goto L_0x0154;
                case 3: goto L_0x0149;
                case 4: goto L_0x013e;
                case 5: goto L_0x012e;
                case 6: goto L_0x0123;
                case 7: goto L_0x0118;
                case 8: goto L_0x0107;
                case 9: goto L_0x00fb;
                case 10: goto L_0x00ef;
                case 11: goto L_0x00e0;
                case 12: goto L_0x00d4;
                case 13: goto L_0x00c8;
                case 14: goto L_0x00bc;
                case 15: goto L_0x00ad;
                case 16: goto L_0x009e;
                case 17: goto L_0x003f;
                case 18: goto L_0x003f;
                case 19: goto L_0x003f;
                case 20: goto L_0x0092;
                case 21: goto L_0x0086;
                case 22: goto L_0x0075;
                case 23: goto L_0x0046;
                default: goto L_0x003f;
            }
        L_0x003f:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r8, r0)
            goto L_0x016f
        L_0x0046:
            byte r1 = r0.a
            r2 = 13
            if (r1 != r2) goto L_0x003f
            com.xiaomi.push.jb r0 = r8.a()
            java.util.HashMap r1 = new java.util.HashMap
            int r2 = r0.f788a
            int r2 = r2 * 2
            r1.<init>(r2)
            r7.f547a = r1
            r1 = 0
        L_0x005c:
            int r2 = r0.f788a
            if (r1 >= r2) goto L_0x0070
            java.lang.String r2 = r8.a()
            java.lang.String r3 = r8.a()
            java.util.Map<java.lang.String, java.lang.String> r4 = r7.f547a
            r4.put(r2, r3)
            int r1 = r1 + 1
            goto L_0x005c
        L_0x0070:
            r8.h()
            goto L_0x016f
        L_0x0075:
            byte r1 = r0.a
            r2 = 8
            if (r1 != r2) goto L_0x003f
            int r0 = r8.a()
            r7.f541a = r0
            r7.e(r5)
            goto L_0x016f
        L_0x0086:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x003f
            java.lang.String r0 = r8.a()
            r7.f561l = r0
            goto L_0x016f
        L_0x0092:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x003f
            java.lang.String r0 = r8.a()
            r7.f560k = r0
            goto L_0x016f
        L_0x009e:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x003f
            short r0 = r8.a()
            r7.f551b = r0
            r7.d((boolean) r5)
            goto L_0x016f
        L_0x00ad:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x003f
            short r0 = r8.a()
            r7.f548a = r0
            r7.c((boolean) r5)
            goto L_0x016f
        L_0x00bc:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x003f
            java.lang.String r0 = r8.a()
            r7.f559j = r0
            goto L_0x016f
        L_0x00c8:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x003f
            java.lang.String r0 = r8.a()
            r7.f558i = r0
            goto L_0x016f
        L_0x00d4:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x003f
            java.lang.String r0 = r8.a()
            r7.f557h = r0
            goto L_0x016f
        L_0x00e0:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x003f
            boolean r0 = r8.a()
            r7.f549a = r0
            r7.b((boolean) r5)
            goto L_0x016f
        L_0x00ef:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x003f
            java.lang.String r0 = r8.a()
            r7.f556g = r0
            goto L_0x016f
        L_0x00fb:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x003f
            java.lang.String r0 = r8.a()
            r7.f555f = r0
            goto L_0x016f
        L_0x0107:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x003f
            com.xiaomi.push.ij r0 = new com.xiaomi.push.ij
            r0.<init>()
            r7.f544a = r0
            com.xiaomi.push.ij r0 = r7.f544a
            r0.a((com.xiaomi.push.jc) r8)
            goto L_0x016f
        L_0x0118:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x003f
            java.lang.String r0 = r8.a()
            r7.f554e = r0
            goto L_0x016f
        L_0x0123:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x003f
            java.lang.String r0 = r8.a()
            r7.f553d = r0
            goto L_0x016f
        L_0x012e:
            byte r1 = r0.a
            r2 = 10
            if (r1 != r2) goto L_0x003f
            long r0 = r8.a()
            r7.f542a = r0
            r7.a((boolean) r5)
            goto L_0x016f
        L_0x013e:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x003f
            java.lang.String r0 = r8.a()
            r7.f552c = r0
            goto L_0x016f
        L_0x0149:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x003f
            java.lang.String r0 = r8.a()
            r7.f550b = r0
            goto L_0x016f
        L_0x0154:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x003f
            com.xiaomi.push.hv r0 = new com.xiaomi.push.hv
            r0.<init>()
            r7.f543a = r0
            com.xiaomi.push.hv r0 = r7.f543a
            r0.a((com.xiaomi.push.jc) r8)
            goto L_0x016f
        L_0x0165:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x003f
            java.lang.String r0 = r8.a()
            r7.f545a = r0
        L_0x016f:
            r8.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.hw.a(com.xiaomi.push.jc):void");
    }

    public void a(boolean z) {
        this.f546a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m410a() {
        return this.f545a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m411a(hw hwVar) {
        if (hwVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = hwVar.a();
        if ((a2 || a3) && (!a2 || !a3 || !this.f545a.equals(hwVar.f545a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = hwVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f543a.compareTo(hwVar.f543a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = hwVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f550b.equals(hwVar.f550b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = hwVar.d();
        if (((d2 || d3) && (!d2 || !d3 || !this.f552c.equals(hwVar.f552c))) || this.f542a != hwVar.f542a) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = hwVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f553d.equals(hwVar.f553d))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = hwVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f554e.equals(hwVar.f554e))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = hwVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f544a.compareTo(hwVar.f544a))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = hwVar.i();
        if ((i2 || i3) && (!i2 || !i3 || !this.f555f.equals(hwVar.f555f))) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = hwVar.j();
        if ((j2 || j3) && (!j2 || !j3 || !this.f556g.equals(hwVar.f556g))) {
            return false;
        }
        boolean k2 = k();
        boolean k3 = hwVar.k();
        if ((k2 || k3) && (!k2 || !k3 || this.f549a != hwVar.f549a)) {
            return false;
        }
        boolean l2 = l();
        boolean l3 = hwVar.l();
        if ((l2 || l3) && (!l2 || !l3 || !this.f557h.equals(hwVar.f557h))) {
            return false;
        }
        boolean m2 = m();
        boolean m3 = hwVar.m();
        if ((m2 || m3) && (!m2 || !m3 || !this.f558i.equals(hwVar.f558i))) {
            return false;
        }
        boolean n2 = n();
        boolean n3 = hwVar.n();
        if ((n2 || n3) && (!n2 || !n3 || !this.f559j.equals(hwVar.f559j))) {
            return false;
        }
        boolean o2 = o();
        boolean o3 = hwVar.o();
        if ((o2 || o3) && (!o2 || !o3 || this.f548a != hwVar.f548a)) {
            return false;
        }
        boolean p2 = p();
        boolean p3 = hwVar.p();
        if ((p2 || p3) && (!p2 || !p3 || this.f551b != hwVar.f551b)) {
            return false;
        }
        boolean q2 = q();
        boolean q3 = hwVar.q();
        if ((q2 || q3) && (!q2 || !q3 || !this.f560k.equals(hwVar.f560k))) {
            return false;
        }
        boolean r2 = r();
        boolean r3 = hwVar.r();
        if ((r2 || r3) && (!r2 || !r3 || !this.f561l.equals(hwVar.f561l))) {
            return false;
        }
        boolean s2 = s();
        boolean s3 = hwVar.s();
        if ((s2 || s3) && (!s2 || !s3 || this.f541a != hwVar.f541a)) {
            return false;
        }
        boolean t2 = t();
        boolean t3 = hwVar.t();
        if (t2 || t3) {
            return t2 && t3 && this.f547a.equals(hwVar.f547a);
        }
        return true;
    }

    public hw b(String str) {
        this.f552c = str;
        return this;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f540a);
        if (this.f545a != null && a()) {
            jcVar.a(a);
            jcVar.a(this.f545a);
            jcVar.b();
        }
        if (this.f543a != null && b()) {
            jcVar.a(b);
            this.f543a.b(jcVar);
            jcVar.b();
        }
        if (this.f550b != null) {
            jcVar.a(c);
            jcVar.a(this.f550b);
            jcVar.b();
        }
        if (this.f552c != null) {
            jcVar.a(d);
            jcVar.a(this.f552c);
            jcVar.b();
        }
        jcVar.a(e);
        jcVar.a(this.f542a);
        jcVar.b();
        if (this.f553d != null && f()) {
            jcVar.a(f);
            jcVar.a(this.f553d);
            jcVar.b();
        }
        if (this.f554e != null && g()) {
            jcVar.a(g);
            jcVar.a(this.f554e);
            jcVar.b();
        }
        if (this.f544a != null && h()) {
            jcVar.a(h);
            this.f544a.b(jcVar);
            jcVar.b();
        }
        if (this.f555f != null && i()) {
            jcVar.a(i);
            jcVar.a(this.f555f);
            jcVar.b();
        }
        if (this.f556g != null && j()) {
            jcVar.a(j);
            jcVar.a(this.f556g);
            jcVar.b();
        }
        if (k()) {
            jcVar.a(k);
            jcVar.a(this.f549a);
            jcVar.b();
        }
        if (this.f557h != null && l()) {
            jcVar.a(l);
            jcVar.a(this.f557h);
            jcVar.b();
        }
        if (this.f558i != null && m()) {
            jcVar.a(m);
            jcVar.a(this.f558i);
            jcVar.b();
        }
        if (this.f559j != null && n()) {
            jcVar.a(n);
            jcVar.a(this.f559j);
            jcVar.b();
        }
        if (o()) {
            jcVar.a(o);
            jcVar.a(this.f548a);
            jcVar.b();
        }
        if (p()) {
            jcVar.a(p);
            jcVar.a(this.f551b);
            jcVar.b();
        }
        if (this.f560k != null && q()) {
            jcVar.a(q);
            jcVar.a(this.f560k);
            jcVar.b();
        }
        if (this.f561l != null && r()) {
            jcVar.a(r);
            jcVar.a(this.f561l);
            jcVar.b();
        }
        if (s()) {
            jcVar.a(s);
            jcVar.a(this.f541a);
            jcVar.b();
        }
        if (this.f547a != null && t()) {
            jcVar.a(t);
            jcVar.a(new jb((byte) 11, (byte) 11, this.f547a.size()));
            for (Map.Entry next : this.f547a.entrySet()) {
                jcVar.a((String) next.getKey());
                jcVar.a((String) next.getValue());
            }
            jcVar.d();
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public void b(boolean z) {
        this.f546a.set(1, z);
    }

    public boolean b() {
        return this.f543a != null;
    }

    public hw c(String str) {
        this.f553d = str;
        return this;
    }

    public void c(boolean z) {
        this.f546a.set(2, z);
    }

    public boolean c() {
        return this.f550b != null;
    }

    public hw d(String str) {
        this.f554e = str;
        return this;
    }

    public void d(boolean z) {
        this.f546a.set(3, z);
    }

    public boolean d() {
        return this.f552c != null;
    }

    public void e(boolean z) {
        this.f546a.set(4, z);
    }

    public boolean e() {
        return this.f546a.get(0);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof hw)) {
            return compareTo((hw) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f553d != null;
    }

    public boolean g() {
        return this.f554e != null;
    }

    public boolean h() {
        return this.f544a != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f555f != null;
    }

    public boolean j() {
        return this.f556g != null;
    }

    public boolean k() {
        return this.f546a.get(1);
    }

    public boolean l() {
        return this.f557h != null;
    }

    public boolean m() {
        return this.f558i != null;
    }

    public boolean n() {
        return this.f559j != null;
    }

    public boolean o() {
        return this.f546a.get(2);
    }

    public boolean p() {
        return this.f546a.get(3);
    }

    public boolean q() {
        return this.f560k != null;
    }

    public boolean r() {
        return this.f561l != null;
    }

    public boolean s() {
        return this.f546a.get(4);
    }

    public boolean t() {
        return this.f547a != null;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionAckMessage(");
        if (a()) {
            sb.append("debug:");
            sb.append(this.f545a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f545a);
            z = false;
        } else {
            z = true;
        }
        if (b()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("target:");
            if (this.f543a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f543a);
            }
            z = false;
        }
        if (!z) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
        }
        sb.append("id:");
        sb.append(this.f550b == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f550b);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("appId:");
        sb.append(this.f552c == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f552c);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("messageTs:");
        sb.append(this.f542a);
        if (f()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("topic:");
            sb.append(this.f553d == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f553d);
        }
        if (g()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("aliasName:");
            sb.append(this.f554e == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f554e);
        }
        if (h()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("request:");
            if (this.f544a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f544a);
            }
        }
        if (i()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("packageName:");
            sb.append(this.f555f == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f555f);
        }
        if (j()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("category:");
            sb.append(this.f556g == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f556g);
        }
        if (k()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("isOnline:");
            sb.append(this.f549a);
        }
        if (l()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("regId:");
            sb.append(this.f557h == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f557h);
        }
        if (m()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("callbackUrl:");
            sb.append(this.f558i == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f558i);
        }
        if (n()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("userAccount:");
            sb.append(this.f559j == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f559j);
        }
        if (o()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("deviceStatus:");
            sb.append(this.f548a);
        }
        if (p()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("geoMsgStatus:");
            sb.append(this.f551b);
        }
        if (q()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("imeiMd5:");
            sb.append(this.f560k == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f560k);
        }
        if (r()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("deviceId:");
            sb.append(this.f561l == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f561l);
        }
        if (s()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("passThrough:");
            sb.append(this.f541a);
        }
        if (t()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("extra:");
            if (this.f547a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f547a);
            }
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
