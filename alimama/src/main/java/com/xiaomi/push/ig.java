package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.BitSet;
import java.util.Map;

public class ig implements ir<ig, Object>, Serializable, Cloneable {
    private static final iz A = new iz("", (byte) 2, 101);
    private static final iz B = new iz("", (byte) 11, 102);
    private static final iz a = new iz("", (byte) 11, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f636a = new jh("XmPushActionRegistration");
    private static final iz b = new iz("", (byte) 12, 2);
    private static final iz c = new iz("", (byte) 11, 3);
    private static final iz d = new iz("", (byte) 11, 4);
    private static final iz e = new iz("", (byte) 11, 5);
    private static final iz f = new iz("", (byte) 11, 6);
    private static final iz g = new iz("", (byte) 11, 7);
    private static final iz h = new iz("", (byte) 11, 8);
    private static final iz i = new iz("", (byte) 11, 9);
    private static final iz j = new iz("", (byte) 11, 10);
    private static final iz k = new iz("", (byte) 11, 11);
    private static final iz l = new iz("", (byte) 11, 12);
    private static final iz m = new iz("", (byte) 8, 13);
    private static final iz n = new iz("", (byte) 8, 14);
    private static final iz o = new iz("", (byte) 11, 15);
    private static final iz p = new iz("", (byte) 11, 16);
    private static final iz q = new iz("", (byte) 11, 17);
    private static final iz r = new iz("", (byte) 11, 18);
    private static final iz s = new iz("", (byte) 8, 19);
    private static final iz t = new iz("", (byte) 8, 20);
    private static final iz u = new iz("", (byte) 2, 21);
    private static final iz v = new iz("", (byte) 10, 22);
    private static final iz w = new iz("", (byte) 10, 23);
    private static final iz x = new iz("", (byte) 11, 24);
    private static final iz y = new iz("", (byte) 11, 25);
    private static final iz z = new iz("", (byte) 13, 100);

    /* renamed from: a  reason: collision with other field name */
    public int f637a;

    /* renamed from: a  reason: collision with other field name */
    public long f638a;

    /* renamed from: a  reason: collision with other field name */
    public hu f639a;

    /* renamed from: a  reason: collision with other field name */
    public hv f640a;

    /* renamed from: a  reason: collision with other field name */
    public String f641a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f642a = new BitSet(7);

    /* renamed from: a  reason: collision with other field name */
    public Map<String, String> f643a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f644a = true;

    /* renamed from: b  reason: collision with other field name */
    public int f645b;

    /* renamed from: b  reason: collision with other field name */
    public long f646b;

    /* renamed from: b  reason: collision with other field name */
    public String f647b;

    /* renamed from: b  reason: collision with other field name */
    public boolean f648b = false;

    /* renamed from: c  reason: collision with other field name */
    public int f649c;

    /* renamed from: c  reason: collision with other field name */
    public String f650c;

    /* renamed from: d  reason: collision with other field name */
    public String f651d;

    /* renamed from: e  reason: collision with other field name */
    public String f652e;

    /* renamed from: f  reason: collision with other field name */
    public String f653f;

    /* renamed from: g  reason: collision with other field name */
    public String f654g;

    /* renamed from: h  reason: collision with other field name */
    public String f655h;

    /* renamed from: i  reason: collision with other field name */
    public String f656i;

    /* renamed from: j  reason: collision with other field name */
    public String f657j;

    /* renamed from: k  reason: collision with other field name */
    public String f658k;

    /* renamed from: l  reason: collision with other field name */
    public String f659l;

    /* renamed from: m  reason: collision with other field name */
    public String f660m;

    /* renamed from: n  reason: collision with other field name */
    public String f661n;

    /* renamed from: o  reason: collision with other field name */
    public String f662o;

    /* renamed from: p  reason: collision with other field name */
    public String f663p;

    /* renamed from: q  reason: collision with other field name */
    public String f664q;

    /* renamed from: r  reason: collision with other field name */
    public String f665r;

    public boolean A() {
        return this.f642a.get(6);
    }

    public boolean B() {
        return this.f665r != null;
    }

    /* renamed from: a */
    public int compareTo(ig igVar) {
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
        int a22;
        int a23;
        int a24;
        int a25;
        int a26;
        int a27;
        int a28;
        int a29;
        if (!getClass().equals(igVar.getClass())) {
            return getClass().getName().compareTo(igVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(igVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a29 = is.a(this.f641a, igVar.f641a)) != 0) {
            return a29;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(igVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a28 = is.a((Comparable) this.f640a, (Comparable) igVar.f640a)) != 0) {
            return a28;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(igVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a27 = is.a(this.f647b, igVar.f647b)) != 0) {
            return a27;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(igVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a26 = is.a(this.f650c, igVar.f650c)) != 0) {
            return a26;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(igVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a25 = is.a(this.f651d, igVar.f651d)) != 0) {
            return a25;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(igVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a24 = is.a(this.f652e, igVar.f652e)) != 0) {
            return a24;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(igVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a23 = is.a(this.f653f, igVar.f653f)) != 0) {
            return a23;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(igVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a22 = is.a(this.f654g, igVar.f654g)) != 0) {
            return a22;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(igVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a21 = is.a(this.f655h, igVar.f655h)) != 0) {
            return a21;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(igVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a20 = is.a(this.f656i, igVar.f656i)) != 0) {
            return a20;
        }
        int compareTo11 = Boolean.valueOf(k()).compareTo(Boolean.valueOf(igVar.k()));
        if (compareTo11 != 0) {
            return compareTo11;
        }
        if (k() && (a19 = is.a(this.f657j, igVar.f657j)) != 0) {
            return a19;
        }
        int compareTo12 = Boolean.valueOf(l()).compareTo(Boolean.valueOf(igVar.l()));
        if (compareTo12 != 0) {
            return compareTo12;
        }
        if (l() && (a18 = is.a(this.f658k, igVar.f658k)) != 0) {
            return a18;
        }
        int compareTo13 = Boolean.valueOf(m()).compareTo(Boolean.valueOf(igVar.m()));
        if (compareTo13 != 0) {
            return compareTo13;
        }
        if (m() && (a17 = is.a(this.f637a, igVar.f637a)) != 0) {
            return a17;
        }
        int compareTo14 = Boolean.valueOf(n()).compareTo(Boolean.valueOf(igVar.n()));
        if (compareTo14 != 0) {
            return compareTo14;
        }
        if (n() && (a16 = is.a(this.f645b, igVar.f645b)) != 0) {
            return a16;
        }
        int compareTo15 = Boolean.valueOf(o()).compareTo(Boolean.valueOf(igVar.o()));
        if (compareTo15 != 0) {
            return compareTo15;
        }
        if (o() && (a15 = is.a(this.f659l, igVar.f659l)) != 0) {
            return a15;
        }
        int compareTo16 = Boolean.valueOf(p()).compareTo(Boolean.valueOf(igVar.p()));
        if (compareTo16 != 0) {
            return compareTo16;
        }
        if (p() && (a14 = is.a(this.f660m, igVar.f660m)) != 0) {
            return a14;
        }
        int compareTo17 = Boolean.valueOf(q()).compareTo(Boolean.valueOf(igVar.q()));
        if (compareTo17 != 0) {
            return compareTo17;
        }
        if (q() && (a13 = is.a(this.f661n, igVar.f661n)) != 0) {
            return a13;
        }
        int compareTo18 = Boolean.valueOf(r()).compareTo(Boolean.valueOf(igVar.r()));
        if (compareTo18 != 0) {
            return compareTo18;
        }
        if (r() && (a12 = is.a(this.f662o, igVar.f662o)) != 0) {
            return a12;
        }
        int compareTo19 = Boolean.valueOf(s()).compareTo(Boolean.valueOf(igVar.s()));
        if (compareTo19 != 0) {
            return compareTo19;
        }
        if (s() && (a11 = is.a(this.f649c, igVar.f649c)) != 0) {
            return a11;
        }
        int compareTo20 = Boolean.valueOf(t()).compareTo(Boolean.valueOf(igVar.t()));
        if (compareTo20 != 0) {
            return compareTo20;
        }
        if (t() && (a10 = is.a((Comparable) this.f639a, (Comparable) igVar.f639a)) != 0) {
            return a10;
        }
        int compareTo21 = Boolean.valueOf(u()).compareTo(Boolean.valueOf(igVar.u()));
        if (compareTo21 != 0) {
            return compareTo21;
        }
        if (u() && (a9 = is.a(this.f644a, igVar.f644a)) != 0) {
            return a9;
        }
        int compareTo22 = Boolean.valueOf(v()).compareTo(Boolean.valueOf(igVar.v()));
        if (compareTo22 != 0) {
            return compareTo22;
        }
        if (v() && (a8 = is.a(this.f638a, igVar.f638a)) != 0) {
            return a8;
        }
        int compareTo23 = Boolean.valueOf(w()).compareTo(Boolean.valueOf(igVar.w()));
        if (compareTo23 != 0) {
            return compareTo23;
        }
        if (w() && (a7 = is.a(this.f646b, igVar.f646b)) != 0) {
            return a7;
        }
        int compareTo24 = Boolean.valueOf(x()).compareTo(Boolean.valueOf(igVar.x()));
        if (compareTo24 != 0) {
            return compareTo24;
        }
        if (x() && (a6 = is.a(this.f663p, igVar.f663p)) != 0) {
            return a6;
        }
        int compareTo25 = Boolean.valueOf(y()).compareTo(Boolean.valueOf(igVar.y()));
        if (compareTo25 != 0) {
            return compareTo25;
        }
        if (y() && (a5 = is.a(this.f664q, igVar.f664q)) != 0) {
            return a5;
        }
        int compareTo26 = Boolean.valueOf(z()).compareTo(Boolean.valueOf(igVar.z()));
        if (compareTo26 != 0) {
            return compareTo26;
        }
        if (z() && (a4 = is.a((Map) this.f643a, (Map) igVar.f643a)) != 0) {
            return a4;
        }
        int compareTo27 = Boolean.valueOf(A()).compareTo(Boolean.valueOf(igVar.A()));
        if (compareTo27 != 0) {
            return compareTo27;
        }
        if (A() && (a3 = is.a(this.f648b, igVar.f648b)) != 0) {
            return a3;
        }
        int compareTo28 = Boolean.valueOf(B()).compareTo(Boolean.valueOf(igVar.B()));
        if (compareTo28 != 0) {
            return compareTo28;
        }
        if (!B() || (a2 = is.a(this.f665r, igVar.f665r)) == 0) {
            return 0;
        }
        return a2;
    }

    public ig a(int i2) {
        this.f637a = i2;
        a(true);
        return this;
    }

    public ig a(hu huVar) {
        this.f639a = huVar;
        return this;
    }

    public ig a(String str) {
        this.f647b = str;
        return this;
    }

    public String a() {
        return this.f647b;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m455a() {
        if (this.f647b == null) {
            throw new jd("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f650c == null) {
            throw new jd("Required field 'appId' was not present! Struct: " + toString());
        } else if (this.f653f == null) {
            throw new jd("Required field 'token' was not present! Struct: " + toString());
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
            if (r1 != 0) goto L_0x0012
            r8.f()
            r7.a()
            return
        L_0x0012:
            short r1 = r0.f784a
            r2 = 10
            r3 = 2
            r4 = 8
            r5 = 1
            r6 = 11
            switch(r1) {
                case 1: goto L_0x01a9;
                case 2: goto L_0x0196;
                case 3: goto L_0x018b;
                case 4: goto L_0x0180;
                case 5: goto L_0x0175;
                case 6: goto L_0x016a;
                case 7: goto L_0x015f;
                case 8: goto L_0x0154;
                case 9: goto L_0x0149;
                case 10: goto L_0x013d;
                case 11: goto L_0x0131;
                case 12: goto L_0x0125;
                case 13: goto L_0x0116;
                case 14: goto L_0x0107;
                case 15: goto L_0x00fb;
                case 16: goto L_0x00ef;
                case 17: goto L_0x00e3;
                case 18: goto L_0x00d7;
                case 19: goto L_0x00c8;
                case 20: goto L_0x00b8;
                case 21: goto L_0x00a9;
                case 22: goto L_0x009a;
                case 23: goto L_0x008b;
                case 24: goto L_0x007f;
                case 25: goto L_0x0073;
                default: goto L_0x001f;
            }
        L_0x001f:
            switch(r1) {
                case 100: goto L_0x0044;
                case 101: goto L_0x0035;
                case 102: goto L_0x0029;
                default: goto L_0x0022;
            }
        L_0x0022:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r8, r0)
            goto L_0x01b3
        L_0x0029:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0022
            java.lang.String r0 = r8.a()
            r7.f665r = r0
            goto L_0x01b3
        L_0x0035:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x0022
            boolean r0 = r8.a()
            r7.f648b = r0
            r7.g((boolean) r5)
            goto L_0x01b3
        L_0x0044:
            byte r1 = r0.a
            r2 = 13
            if (r1 != r2) goto L_0x0022
            com.xiaomi.push.jb r0 = r8.a()
            java.util.HashMap r1 = new java.util.HashMap
            int r2 = r0.f788a
            int r2 = r2 * 2
            r1.<init>(r2)
            r7.f643a = r1
            r1 = 0
        L_0x005a:
            int r2 = r0.f788a
            if (r1 >= r2) goto L_0x006e
            java.lang.String r2 = r8.a()
            java.lang.String r3 = r8.a()
            java.util.Map<java.lang.String, java.lang.String> r4 = r7.f643a
            r4.put(r2, r3)
            int r1 = r1 + 1
            goto L_0x005a
        L_0x006e:
            r8.h()
            goto L_0x01b3
        L_0x0073:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0022
            java.lang.String r0 = r8.a()
            r7.f664q = r0
            goto L_0x01b3
        L_0x007f:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0022
            java.lang.String r0 = r8.a()
            r7.f663p = r0
            goto L_0x01b3
        L_0x008b:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0022
            long r0 = r8.a()
            r7.f646b = r0
            r7.f((boolean) r5)
            goto L_0x01b3
        L_0x009a:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0022
            long r0 = r8.a()
            r7.f638a = r0
            r7.e((boolean) r5)
            goto L_0x01b3
        L_0x00a9:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x0022
            boolean r0 = r8.a()
            r7.f644a = r0
            r7.d((boolean) r5)
            goto L_0x01b3
        L_0x00b8:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x0022
            int r0 = r8.a()
            com.xiaomi.push.hu r0 = com.xiaomi.push.hu.a(r0)
            r7.f639a = r0
            goto L_0x01b3
        L_0x00c8:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x0022
            int r0 = r8.a()
            r7.f649c = r0
            r7.c((boolean) r5)
            goto L_0x01b3
        L_0x00d7:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0022
            java.lang.String r0 = r8.a()
            r7.f662o = r0
            goto L_0x01b3
        L_0x00e3:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0022
            java.lang.String r0 = r8.a()
            r7.f661n = r0
            goto L_0x01b3
        L_0x00ef:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0022
            java.lang.String r0 = r8.a()
            r7.f660m = r0
            goto L_0x01b3
        L_0x00fb:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0022
            java.lang.String r0 = r8.a()
            r7.f659l = r0
            goto L_0x01b3
        L_0x0107:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x0022
            int r0 = r8.a()
            r7.f645b = r0
            r7.b((boolean) r5)
            goto L_0x01b3
        L_0x0116:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x0022
            int r0 = r8.a()
            r7.f637a = r0
            r7.a((boolean) r5)
            goto L_0x01b3
        L_0x0125:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0022
            java.lang.String r0 = r8.a()
            r7.f658k = r0
            goto L_0x01b3
        L_0x0131:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0022
            java.lang.String r0 = r8.a()
            r7.f657j = r0
            goto L_0x01b3
        L_0x013d:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0022
            java.lang.String r0 = r8.a()
            r7.f656i = r0
            goto L_0x01b3
        L_0x0149:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0022
            java.lang.String r0 = r8.a()
            r7.f655h = r0
            goto L_0x01b3
        L_0x0154:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0022
            java.lang.String r0 = r8.a()
            r7.f654g = r0
            goto L_0x01b3
        L_0x015f:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0022
            java.lang.String r0 = r8.a()
            r7.f653f = r0
            goto L_0x01b3
        L_0x016a:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0022
            java.lang.String r0 = r8.a()
            r7.f652e = r0
            goto L_0x01b3
        L_0x0175:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0022
            java.lang.String r0 = r8.a()
            r7.f651d = r0
            goto L_0x01b3
        L_0x0180:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0022
            java.lang.String r0 = r8.a()
            r7.f650c = r0
            goto L_0x01b3
        L_0x018b:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0022
            java.lang.String r0 = r8.a()
            r7.f647b = r0
            goto L_0x01b3
        L_0x0196:
            byte r1 = r0.a
            r2 = 12
            if (r1 != r2) goto L_0x0022
            com.xiaomi.push.hv r0 = new com.xiaomi.push.hv
            r0.<init>()
            r7.f640a = r0
            com.xiaomi.push.hv r0 = r7.f640a
            r0.a((com.xiaomi.push.jc) r8)
            goto L_0x01b3
        L_0x01a9:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0022
            java.lang.String r0 = r8.a()
            r7.f641a = r0
        L_0x01b3:
            r8.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.ig.a(com.xiaomi.push.jc):void");
    }

    public void a(boolean z2) {
        this.f642a.set(0, z2);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m456a() {
        return this.f641a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m457a(ig igVar) {
        if (igVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = igVar.a();
        if ((a2 || a3) && (!a2 || !a3 || !this.f641a.equals(igVar.f641a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = igVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f640a.compareTo(igVar.f640a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = igVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f647b.equals(igVar.f647b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = igVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f650c.equals(igVar.f650c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = igVar.e();
        if ((e2 || e3) && (!e2 || !e3 || !this.f651d.equals(igVar.f651d))) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = igVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f652e.equals(igVar.f652e))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = igVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f653f.equals(igVar.f653f))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = igVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f654g.equals(igVar.f654g))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = igVar.i();
        if ((i2 || i3) && (!i2 || !i3 || !this.f655h.equals(igVar.f655h))) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = igVar.j();
        if ((j2 || j3) && (!j2 || !j3 || !this.f656i.equals(igVar.f656i))) {
            return false;
        }
        boolean k2 = k();
        boolean k3 = igVar.k();
        if ((k2 || k3) && (!k2 || !k3 || !this.f657j.equals(igVar.f657j))) {
            return false;
        }
        boolean l2 = l();
        boolean l3 = igVar.l();
        if ((l2 || l3) && (!l2 || !l3 || !this.f658k.equals(igVar.f658k))) {
            return false;
        }
        boolean m2 = m();
        boolean m3 = igVar.m();
        if ((m2 || m3) && (!m2 || !m3 || this.f637a != igVar.f637a)) {
            return false;
        }
        boolean n2 = n();
        boolean n3 = igVar.n();
        if ((n2 || n3) && (!n2 || !n3 || this.f645b != igVar.f645b)) {
            return false;
        }
        boolean o2 = o();
        boolean o3 = igVar.o();
        if ((o2 || o3) && (!o2 || !o3 || !this.f659l.equals(igVar.f659l))) {
            return false;
        }
        boolean p2 = p();
        boolean p3 = igVar.p();
        if ((p2 || p3) && (!p2 || !p3 || !this.f660m.equals(igVar.f660m))) {
            return false;
        }
        boolean q2 = q();
        boolean q3 = igVar.q();
        if ((q2 || q3) && (!q2 || !q3 || !this.f661n.equals(igVar.f661n))) {
            return false;
        }
        boolean r2 = r();
        boolean r3 = igVar.r();
        if ((r2 || r3) && (!r2 || !r3 || !this.f662o.equals(igVar.f662o))) {
            return false;
        }
        boolean s2 = s();
        boolean s3 = igVar.s();
        if ((s2 || s3) && (!s2 || !s3 || this.f649c != igVar.f649c)) {
            return false;
        }
        boolean t2 = t();
        boolean t3 = igVar.t();
        if ((t2 || t3) && (!t2 || !t3 || !this.f639a.equals(igVar.f639a))) {
            return false;
        }
        boolean u2 = u();
        boolean u3 = igVar.u();
        if ((u2 || u3) && (!u2 || !u3 || this.f644a != igVar.f644a)) {
            return false;
        }
        boolean v2 = v();
        boolean v3 = igVar.v();
        if ((v2 || v3) && (!v2 || !v3 || this.f638a != igVar.f638a)) {
            return false;
        }
        boolean w2 = w();
        boolean w3 = igVar.w();
        if ((w2 || w3) && (!w2 || !w3 || this.f646b != igVar.f646b)) {
            return false;
        }
        boolean x2 = x();
        boolean x3 = igVar.x();
        if ((x2 || x3) && (!x2 || !x3 || !this.f663p.equals(igVar.f663p))) {
            return false;
        }
        boolean y2 = y();
        boolean y3 = igVar.y();
        if ((y2 || y3) && (!y2 || !y3 || !this.f664q.equals(igVar.f664q))) {
            return false;
        }
        boolean z2 = z();
        boolean z3 = igVar.z();
        if ((z2 || z3) && (!z2 || !z3 || !this.f643a.equals(igVar.f643a))) {
            return false;
        }
        boolean A2 = A();
        boolean A3 = igVar.A();
        if ((A2 || A3) && (!A2 || !A3 || this.f648b != igVar.f648b)) {
            return false;
        }
        boolean B2 = B();
        boolean B3 = igVar.B();
        if (B2 || B3) {
            return B2 && B3 && this.f665r.equals(igVar.f665r);
        }
        return true;
    }

    public ig b(int i2) {
        this.f645b = i2;
        b(true);
        return this;
    }

    public ig b(String str) {
        this.f650c = str;
        return this;
    }

    public String b() {
        return this.f650c;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f636a);
        if (this.f641a != null && a()) {
            jcVar.a(a);
            jcVar.a(this.f641a);
            jcVar.b();
        }
        if (this.f640a != null && b()) {
            jcVar.a(b);
            this.f640a.b(jcVar);
            jcVar.b();
        }
        if (this.f647b != null) {
            jcVar.a(c);
            jcVar.a(this.f647b);
            jcVar.b();
        }
        if (this.f650c != null) {
            jcVar.a(d);
            jcVar.a(this.f650c);
            jcVar.b();
        }
        if (this.f651d != null && e()) {
            jcVar.a(e);
            jcVar.a(this.f651d);
            jcVar.b();
        }
        if (this.f652e != null && f()) {
            jcVar.a(f);
            jcVar.a(this.f652e);
            jcVar.b();
        }
        if (this.f653f != null) {
            jcVar.a(g);
            jcVar.a(this.f653f);
            jcVar.b();
        }
        if (this.f654g != null && h()) {
            jcVar.a(h);
            jcVar.a(this.f654g);
            jcVar.b();
        }
        if (this.f655h != null && i()) {
            jcVar.a(i);
            jcVar.a(this.f655h);
            jcVar.b();
        }
        if (this.f656i != null && j()) {
            jcVar.a(j);
            jcVar.a(this.f656i);
            jcVar.b();
        }
        if (this.f657j != null && k()) {
            jcVar.a(k);
            jcVar.a(this.f657j);
            jcVar.b();
        }
        if (this.f658k != null && l()) {
            jcVar.a(l);
            jcVar.a(this.f658k);
            jcVar.b();
        }
        if (m()) {
            jcVar.a(m);
            jcVar.a(this.f637a);
            jcVar.b();
        }
        if (n()) {
            jcVar.a(n);
            jcVar.a(this.f645b);
            jcVar.b();
        }
        if (this.f659l != null && o()) {
            jcVar.a(o);
            jcVar.a(this.f659l);
            jcVar.b();
        }
        if (this.f660m != null && p()) {
            jcVar.a(p);
            jcVar.a(this.f660m);
            jcVar.b();
        }
        if (this.f661n != null && q()) {
            jcVar.a(q);
            jcVar.a(this.f661n);
            jcVar.b();
        }
        if (this.f662o != null && r()) {
            jcVar.a(r);
            jcVar.a(this.f662o);
            jcVar.b();
        }
        if (s()) {
            jcVar.a(s);
            jcVar.a(this.f649c);
            jcVar.b();
        }
        if (this.f639a != null && t()) {
            jcVar.a(t);
            jcVar.a(this.f639a.a());
            jcVar.b();
        }
        if (u()) {
            jcVar.a(u);
            jcVar.a(this.f644a);
            jcVar.b();
        }
        if (v()) {
            jcVar.a(v);
            jcVar.a(this.f638a);
            jcVar.b();
        }
        if (w()) {
            jcVar.a(w);
            jcVar.a(this.f646b);
            jcVar.b();
        }
        if (this.f663p != null && x()) {
            jcVar.a(x);
            jcVar.a(this.f663p);
            jcVar.b();
        }
        if (this.f664q != null && y()) {
            jcVar.a(y);
            jcVar.a(this.f664q);
            jcVar.b();
        }
        if (this.f643a != null && z()) {
            jcVar.a(z);
            jcVar.a(new jb((byte) 11, (byte) 11, this.f643a.size()));
            for (Map.Entry next : this.f643a.entrySet()) {
                jcVar.a((String) next.getKey());
                jcVar.a((String) next.getValue());
            }
            jcVar.d();
            jcVar.b();
        }
        if (A()) {
            jcVar.a(A);
            jcVar.a(this.f648b);
            jcVar.b();
        }
        if (this.f665r != null && B()) {
            jcVar.a(B);
            jcVar.a(this.f665r);
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public void b(boolean z2) {
        this.f642a.set(1, z2);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m458b() {
        return this.f640a != null;
    }

    public ig c(int i2) {
        this.f649c = i2;
        c(true);
        return this;
    }

    public ig c(String str) {
        this.f651d = str;
        return this;
    }

    public String c() {
        return this.f653f;
    }

    public void c(boolean z2) {
        this.f642a.set(2, z2);
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m459c() {
        return this.f647b != null;
    }

    public ig d(String str) {
        this.f652e = str;
        return this;
    }

    public void d(boolean z2) {
        this.f642a.set(3, z2);
    }

    public boolean d() {
        return this.f650c != null;
    }

    public ig e(String str) {
        this.f653f = str;
        return this;
    }

    public void e(boolean z2) {
        this.f642a.set(4, z2);
    }

    public boolean e() {
        return this.f651d != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ig)) {
            return compareTo((ig) obj);
        }
        return false;
    }

    public ig f(String str) {
        this.f654g = str;
        return this;
    }

    public void f(boolean z2) {
        this.f642a.set(5, z2);
    }

    public boolean f() {
        return this.f652e != null;
    }

    public ig g(String str) {
        this.f658k = str;
        return this;
    }

    public void g(boolean z2) {
        this.f642a.set(6, z2);
    }

    public boolean g() {
        return this.f653f != null;
    }

    public ig h(String str) {
        this.f659l = str;
        return this;
    }

    public boolean h() {
        return this.f654g != null;
    }

    public int hashCode() {
        return 0;
    }

    public ig i(String str) {
        this.f660m = str;
        return this;
    }

    public boolean i() {
        return this.f655h != null;
    }

    public ig j(String str) {
        this.f661n = str;
        return this;
    }

    public boolean j() {
        return this.f656i != null;
    }

    public ig k(String str) {
        this.f662o = str;
        return this;
    }

    public boolean k() {
        return this.f657j != null;
    }

    public boolean l() {
        return this.f658k != null;
    }

    public boolean m() {
        return this.f642a.get(0);
    }

    public boolean n() {
        return this.f642a.get(1);
    }

    public boolean o() {
        return this.f659l != null;
    }

    public boolean p() {
        return this.f660m != null;
    }

    public boolean q() {
        return this.f661n != null;
    }

    public boolean r() {
        return this.f662o != null;
    }

    public boolean s() {
        return this.f642a.get(2);
    }

    public boolean t() {
        return this.f639a != null;
    }

    public String toString() {
        boolean z2;
        StringBuilder sb = new StringBuilder("XmPushActionRegistration(");
        if (a()) {
            sb.append("debug:");
            sb.append(this.f641a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f641a);
            z2 = false;
        } else {
            z2 = true;
        }
        if (b()) {
            if (!z2) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("target:");
            if (this.f640a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f640a);
            }
            z2 = false;
        }
        if (!z2) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
        }
        sb.append("id:");
        sb.append(this.f647b == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f647b);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("appId:");
        sb.append(this.f650c == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f650c);
        if (e()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("appVersion:");
            sb.append(this.f651d == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f651d);
        }
        if (f()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("packageName:");
            sb.append(this.f652e == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f652e);
        }
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("token:");
        sb.append(this.f653f == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f653f);
        if (h()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("deviceId:");
            sb.append(this.f654g == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f654g);
        }
        if (i()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("aliasName:");
            sb.append(this.f655h == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f655h);
        }
        if (j()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("sdkVersion:");
            sb.append(this.f656i == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f656i);
        }
        if (k()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("regId:");
            sb.append(this.f657j == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f657j);
        }
        if (l()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("pushSdkVersionName:");
            sb.append(this.f658k == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f658k);
        }
        if (m()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("pushSdkVersionCode:");
            sb.append(this.f637a);
        }
        if (n()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("appVersionCode:");
            sb.append(this.f645b);
        }
        if (o()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("androidId:");
            sb.append(this.f659l == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f659l);
        }
        if (p()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("imei:");
            sb.append(this.f660m == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f660m);
        }
        if (q()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("serial:");
            sb.append(this.f661n == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f661n);
        }
        if (r()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("imeiMd5:");
            sb.append(this.f662o == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f662o);
        }
        if (s()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("spaceId:");
            sb.append(this.f649c);
        }
        if (t()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("reason:");
            if (this.f639a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f639a);
            }
        }
        if (u()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("validateToken:");
            sb.append(this.f644a);
        }
        if (v()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("miid:");
            sb.append(this.f638a);
        }
        if (w()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("createdTs:");
            sb.append(this.f646b);
        }
        if (x()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("subImei:");
            sb.append(this.f663p == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f663p);
        }
        if (y()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("subImeiMd5:");
            sb.append(this.f664q == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f664q);
        }
        if (z()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("connectionAttrs:");
            if (this.f643a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f643a);
            }
        }
        if (A()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("cleanOldRegInfo:");
            sb.append(this.f648b);
        }
        if (B()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("oldRegId:");
            sb.append(this.f665r == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f665r);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }

    public boolean u() {
        return this.f642a.get(3);
    }

    public boolean v() {
        return this.f642a.get(4);
    }

    public boolean w() {
        return this.f642a.get(5);
    }

    public boolean x() {
        return this.f663p != null;
    }

    public boolean y() {
        return this.f664q != null;
    }

    public boolean z() {
        return this.f643a != null;
    }
}
