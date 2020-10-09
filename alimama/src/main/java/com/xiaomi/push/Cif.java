package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

/* renamed from: com.xiaomi.push.if  reason: invalid class name */
public class Cif implements ir<Cif, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 11, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f619a = new jh("XmPushActionNotification");
    private static final iz b = new iz("", (byte) 12, 2);
    private static final iz c = new iz("", (byte) 11, 3);
    private static final iz d = new iz("", (byte) 11, 4);
    private static final iz e = new iz("", (byte) 11, 5);
    private static final iz f = new iz("", (byte) 2, 6);
    private static final iz g = new iz("", (byte) 11, 7);
    private static final iz h = new iz("", (byte) 13, 8);
    private static final iz i = new iz("", (byte) 11, 9);
    private static final iz j = new iz("", (byte) 11, 10);
    private static final iz k = new iz("", (byte) 11, 12);
    private static final iz l = new iz("", (byte) 11, 13);
    private static final iz m = new iz("", (byte) 11, 14);
    private static final iz n = new iz("", (byte) 10, 15);
    private static final iz o = new iz("", (byte) 2, 20);

    /* renamed from: a  reason: collision with other field name */
    public long f620a;

    /* renamed from: a  reason: collision with other field name */
    public hv f621a;

    /* renamed from: a  reason: collision with other field name */
    public String f622a;

    /* renamed from: a  reason: collision with other field name */
    public ByteBuffer f623a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f624a;

    /* renamed from: a  reason: collision with other field name */
    public Map<String, String> f625a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f626a;

    /* renamed from: b  reason: collision with other field name */
    public String f627b;

    /* renamed from: b  reason: collision with other field name */
    public boolean f628b;

    /* renamed from: c  reason: collision with other field name */
    public String f629c;

    /* renamed from: d  reason: collision with other field name */
    public String f630d;

    /* renamed from: e  reason: collision with other field name */
    public String f631e;

    /* renamed from: f  reason: collision with other field name */
    public String f632f;

    /* renamed from: g  reason: collision with other field name */
    public String f633g;

    /* renamed from: h  reason: collision with other field name */
    public String f634h;

    /* renamed from: i  reason: collision with other field name */
    public String f635i;

    public Cif() {
        this.f624a = new BitSet(3);
        this.f626a = true;
        this.f628b = false;
    }

    public Cif(String str, boolean z) {
        this();
        this.f627b = str;
        this.f626a = z;
        a(true);
    }

    /* renamed from: a */
    public int compareTo(Cif ifVar) {
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
        if (!getClass().equals(ifVar.getClass())) {
            return getClass().getName().compareTo(ifVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(ifVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a16 = is.a(this.f622a, ifVar.f622a)) != 0) {
            return a16;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(ifVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a15 = is.a((Comparable) this.f621a, (Comparable) ifVar.f621a)) != 0) {
            return a15;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(ifVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a14 = is.a(this.f627b, ifVar.f627b)) != 0) {
            return a14;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(ifVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a13 = is.a(this.f629c, ifVar.f629c)) != 0) {
            return a13;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(ifVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a12 = is.a(this.f630d, ifVar.f630d)) != 0) {
            return a12;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(ifVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a11 = is.a(this.f626a, ifVar.f626a)) != 0) {
            return a11;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(ifVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a10 = is.a(this.f631e, ifVar.f631e)) != 0) {
            return a10;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(ifVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a9 = is.a((Map) this.f625a, (Map) ifVar.f625a)) != 0) {
            return a9;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(ifVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a8 = is.a(this.f632f, ifVar.f632f)) != 0) {
            return a8;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(ifVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a7 = is.a(this.f633g, ifVar.f633g)) != 0) {
            return a7;
        }
        int compareTo11 = Boolean.valueOf(k()).compareTo(Boolean.valueOf(ifVar.k()));
        if (compareTo11 != 0) {
            return compareTo11;
        }
        if (k() && (a6 = is.a(this.f634h, ifVar.f634h)) != 0) {
            return a6;
        }
        int compareTo12 = Boolean.valueOf(l()).compareTo(Boolean.valueOf(ifVar.l()));
        if (compareTo12 != 0) {
            return compareTo12;
        }
        if (l() && (a5 = is.a(this.f635i, ifVar.f635i)) != 0) {
            return a5;
        }
        int compareTo13 = Boolean.valueOf(m()).compareTo(Boolean.valueOf(ifVar.m()));
        if (compareTo13 != 0) {
            return compareTo13;
        }
        if (m() && (a4 = is.a((Comparable) this.f623a, (Comparable) ifVar.f623a)) != 0) {
            return a4;
        }
        int compareTo14 = Boolean.valueOf(n()).compareTo(Boolean.valueOf(ifVar.n()));
        if (compareTo14 != 0) {
            return compareTo14;
        }
        if (n() && (a3 = is.a(this.f620a, ifVar.f620a)) != 0) {
            return a3;
        }
        int compareTo15 = Boolean.valueOf(o()).compareTo(Boolean.valueOf(ifVar.o()));
        if (compareTo15 != 0) {
            return compareTo15;
        }
        if (!o() || (a2 = is.a(this.f628b, ifVar.f628b)) == 0) {
            return 0;
        }
        return a2;
    }

    public Cif a(String str) {
        this.f627b = str;
        return this;
    }

    public Cif a(ByteBuffer byteBuffer) {
        this.f623a = byteBuffer;
        return this;
    }

    public Cif a(Map<String, String> map) {
        this.f625a = map;
        return this;
    }

    public Cif a(boolean z) {
        this.f626a = z;
        a(true);
        return this;
    }

    public Cif a(byte[] bArr) {
        a(ByteBuffer.wrap(bArr));
        return this;
    }

    public String a() {
        return this.f627b;
    }

    /* renamed from: a  reason: collision with other method in class */
    public Map<String, String> m447a() {
        return this.f625a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m448a() {
        if (this.f627b == null) {
            throw new jd("Required field 'id' was not present! Struct: " + toString());
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(com.xiaomi.push.jc r6) {
        /*
            r5 = this;
            r6.a()
        L_0x0003:
            com.xiaomi.push.iz r0 = r6.a()
            byte r1 = r0.a
            if (r1 != 0) goto L_0x0033
            r6.f()
            boolean r6 = r5.f()
            if (r6 == 0) goto L_0x0018
            r5.a()
            return
        L_0x0018:
            com.xiaomi.push.jd r6 = new com.xiaomi.push.jd
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Required field 'requireAck' was not found in serialized data! Struct: "
            r0.append(r1)
            java.lang.String r1 = r5.toString()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r6.<init>(r0)
            throw r6
        L_0x0033:
            short r1 = r0.f784a
            r2 = 20
            r3 = 2
            r4 = 1
            if (r1 == r2) goto L_0x011d
            r2 = 11
            switch(r1) {
                case 1: goto L_0x0112;
                case 2: goto L_0x00ff;
                case 3: goto L_0x00f4;
                case 4: goto L_0x00e9;
                case 5: goto L_0x00de;
                case 6: goto L_0x00d0;
                case 7: goto L_0x00c5;
                case 8: goto L_0x0097;
                case 9: goto L_0x008b;
                case 10: goto L_0x007f;
                default: goto L_0x0040;
            }
        L_0x0040:
            switch(r1) {
                case 12: goto L_0x0073;
                case 13: goto L_0x0067;
                case 14: goto L_0x005b;
                case 15: goto L_0x004a;
                default: goto L_0x0043;
            }
        L_0x0043:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r6, r0)
            goto L_0x012a
        L_0x004a:
            byte r1 = r0.a
            r2 = 10
            if (r1 != r2) goto L_0x0043
            long r0 = r6.a()
            r5.f620a = r0
            r5.b((boolean) r4)
            goto L_0x012a
        L_0x005b:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0043
            java.nio.ByteBuffer r0 = r6.a()
            r5.f623a = r0
            goto L_0x012a
        L_0x0067:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0043
            java.lang.String r0 = r6.a()
            r5.f635i = r0
            goto L_0x012a
        L_0x0073:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0043
            java.lang.String r0 = r6.a()
            r5.f634h = r0
            goto L_0x012a
        L_0x007f:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0043
            java.lang.String r0 = r6.a()
            r5.f633g = r0
            goto L_0x012a
        L_0x008b:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0043
            java.lang.String r0 = r6.a()
            r5.f632f = r0
            goto L_0x012a
        L_0x0097:
            byte r1 = r0.a
            r2 = 13
            if (r1 != r2) goto L_0x0043
            com.xiaomi.push.jb r0 = r6.a()
            java.util.HashMap r1 = new java.util.HashMap
            int r2 = r0.f788a
            int r2 = r2 * 2
            r1.<init>(r2)
            r5.f625a = r1
            r1 = 0
        L_0x00ad:
            int r2 = r0.f788a
            if (r1 >= r2) goto L_0x00c1
            java.lang.String r2 = r6.a()
            java.lang.String r3 = r6.a()
            java.util.Map<java.lang.String, java.lang.String> r4 = r5.f625a
            r4.put(r2, r3)
            int r1 = r1 + 1
            goto L_0x00ad
        L_0x00c1:
            r6.h()
            goto L_0x012a
        L_0x00c5:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0043
            java.lang.String r0 = r6.a()
            r5.f631e = r0
            goto L_0x012a
        L_0x00d0:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x0043
            boolean r0 = r6.a()
            r5.f626a = r0
            r5.a((boolean) r4)
            goto L_0x012a
        L_0x00de:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0043
            java.lang.String r0 = r6.a()
            r5.f630d = r0
            goto L_0x012a
        L_0x00e9:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0043
            java.lang.String r0 = r6.a()
            r5.f629c = r0
            goto L_0x012a
        L_0x00f4:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0043
            java.lang.String r0 = r6.a()
            r5.f627b = r0
            goto L_0x012a
        L_0x00ff:
            byte r1 = r0.a
            r2 = 12
            if (r1 != r2) goto L_0x0043
            com.xiaomi.push.hv r0 = new com.xiaomi.push.hv
            r0.<init>()
            r5.f621a = r0
            com.xiaomi.push.hv r0 = r5.f621a
            r0.a((com.xiaomi.push.jc) r6)
            goto L_0x012a
        L_0x0112:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0043
            java.lang.String r0 = r6.a()
            r5.f622a = r0
            goto L_0x012a
        L_0x011d:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x0043
            boolean r0 = r6.a()
            r5.f628b = r0
            r5.c((boolean) r4)
        L_0x012a:
            r6.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.Cif.a(com.xiaomi.push.jc):void");
    }

    public void a(String str, String str2) {
        if (this.f625a == null) {
            this.f625a = new HashMap();
        }
        this.f625a.put(str, str2);
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m449a(boolean z) {
        this.f624a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m450a() {
        return this.f622a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m451a(Cif ifVar) {
        if (ifVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = ifVar.a();
        if ((a2 || a3) && (!a2 || !a3 || !this.f622a.equals(ifVar.f622a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = ifVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f621a.compareTo(ifVar.f621a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = ifVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f627b.equals(ifVar.f627b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = ifVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f629c.equals(ifVar.f629c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = ifVar.e();
        if (((e2 || e3) && (!e2 || !e3 || !this.f630d.equals(ifVar.f630d))) || this.f626a != ifVar.f626a) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = ifVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f631e.equals(ifVar.f631e))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = ifVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f625a.equals(ifVar.f625a))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = ifVar.i();
        if ((i2 || i3) && (!i2 || !i3 || !this.f632f.equals(ifVar.f632f))) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = ifVar.j();
        if ((j2 || j3) && (!j2 || !j3 || !this.f633g.equals(ifVar.f633g))) {
            return false;
        }
        boolean k2 = k();
        boolean k3 = ifVar.k();
        if ((k2 || k3) && (!k2 || !k3 || !this.f634h.equals(ifVar.f634h))) {
            return false;
        }
        boolean l2 = l();
        boolean l3 = ifVar.l();
        if ((l2 || l3) && (!l2 || !l3 || !this.f635i.equals(ifVar.f635i))) {
            return false;
        }
        boolean m2 = m();
        boolean m3 = ifVar.m();
        if ((m2 || m3) && (!m2 || !m3 || !this.f623a.equals(ifVar.f623a))) {
            return false;
        }
        boolean n2 = n();
        boolean n3 = ifVar.n();
        if ((n2 || n3) && (!n2 || !n3 || this.f620a != ifVar.f620a)) {
            return false;
        }
        boolean o2 = o();
        boolean o3 = ifVar.o();
        if (o2 || o3) {
            return o2 && o3 && this.f628b == ifVar.f628b;
        }
        return true;
    }

    /* renamed from: a  reason: collision with other method in class */
    public byte[] m452a() {
        a(is.a(this.f623a));
        return this.f623a.array();
    }

    public Cif b(String str) {
        this.f629c = str;
        return this;
    }

    public String b() {
        return this.f629c;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f619a);
        if (this.f622a != null && a()) {
            jcVar.a(a);
            jcVar.a(this.f622a);
            jcVar.b();
        }
        if (this.f621a != null && b()) {
            jcVar.a(b);
            this.f621a.b(jcVar);
            jcVar.b();
        }
        if (this.f627b != null) {
            jcVar.a(c);
            jcVar.a(this.f627b);
            jcVar.b();
        }
        if (this.f629c != null && d()) {
            jcVar.a(d);
            jcVar.a(this.f629c);
            jcVar.b();
        }
        if (this.f630d != null && e()) {
            jcVar.a(e);
            jcVar.a(this.f630d);
            jcVar.b();
        }
        jcVar.a(f);
        jcVar.a(this.f626a);
        jcVar.b();
        if (this.f631e != null && g()) {
            jcVar.a(g);
            jcVar.a(this.f631e);
            jcVar.b();
        }
        if (this.f625a != null && h()) {
            jcVar.a(h);
            jcVar.a(new jb((byte) 11, (byte) 11, this.f625a.size()));
            for (Map.Entry next : this.f625a.entrySet()) {
                jcVar.a((String) next.getKey());
                jcVar.a((String) next.getValue());
            }
            jcVar.d();
            jcVar.b();
        }
        if (this.f632f != null && i()) {
            jcVar.a(i);
            jcVar.a(this.f632f);
            jcVar.b();
        }
        if (this.f633g != null && j()) {
            jcVar.a(j);
            jcVar.a(this.f633g);
            jcVar.b();
        }
        if (this.f634h != null && k()) {
            jcVar.a(k);
            jcVar.a(this.f634h);
            jcVar.b();
        }
        if (this.f635i != null && l()) {
            jcVar.a(l);
            jcVar.a(this.f635i);
            jcVar.b();
        }
        if (this.f623a != null && m()) {
            jcVar.a(m);
            jcVar.a(this.f623a);
            jcVar.b();
        }
        if (n()) {
            jcVar.a(n);
            jcVar.a(this.f620a);
            jcVar.b();
        }
        if (o()) {
            jcVar.a(o);
            jcVar.a(this.f628b);
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public void b(boolean z) {
        this.f624a.set(1, z);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m453b() {
        return this.f621a != null;
    }

    public Cif c(String str) {
        this.f630d = str;
        return this;
    }

    public String c() {
        return this.f632f;
    }

    public void c(boolean z) {
        this.f624a.set(2, z);
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m454c() {
        return this.f627b != null;
    }

    public Cif d(String str) {
        this.f632f = str;
        return this;
    }

    public boolean d() {
        return this.f629c != null;
    }

    public boolean e() {
        return this.f630d != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof Cif)) {
            return compareTo((Cif) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f624a.get(0);
    }

    public boolean g() {
        return this.f631e != null;
    }

    public boolean h() {
        return this.f625a != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f632f != null;
    }

    public boolean j() {
        return this.f633g != null;
    }

    public boolean k() {
        return this.f634h != null;
    }

    public boolean l() {
        return this.f635i != null;
    }

    public boolean m() {
        return this.f623a != null;
    }

    public boolean n() {
        return this.f624a.get(1);
    }

    public boolean o() {
        return this.f624a.get(2);
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionNotification(");
        if (a()) {
            sb.append("debug:");
            sb.append(this.f622a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f622a);
            z = false;
        } else {
            z = true;
        }
        if (b()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("target:");
            if (this.f621a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f621a);
            }
            z = false;
        }
        if (!z) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
        }
        sb.append("id:");
        sb.append(this.f627b == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f627b);
        if (d()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("appId:");
            sb.append(this.f629c == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f629c);
        }
        if (e()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("type:");
            sb.append(this.f630d == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f630d);
        }
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("requireAck:");
        sb.append(this.f626a);
        if (g()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("payload:");
            sb.append(this.f631e == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f631e);
        }
        if (h()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("extra:");
            if (this.f625a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f625a);
            }
        }
        if (i()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("packageName:");
            sb.append(this.f632f == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f632f);
        }
        if (j()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("category:");
            sb.append(this.f633g == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f633g);
        }
        if (k()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("regId:");
            sb.append(this.f634h == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f634h);
        }
        if (l()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("aliasName:");
            sb.append(this.f635i == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f635i);
        }
        if (m()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("binaryExtra:");
            if (this.f623a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                is.a(this.f623a, sb);
            }
        }
        if (n()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("createdTs:");
            sb.append(this.f620a);
        }
        if (o()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("alreadyLogClickInXmq:");
            sb.append(this.f628b);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
