package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.BitSet;

public class in implements ir<in, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 11, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f743a = new jh("XmPushActionUnRegistrationResult");
    private static final iz b = new iz("", (byte) 12, 2);
    private static final iz c = new iz("", (byte) 11, 3);
    private static final iz d = new iz("", (byte) 11, 4);
    private static final iz e = new iz("", (byte) 10, 6);
    private static final iz f = new iz("", (byte) 11, 7);
    private static final iz g = new iz("", (byte) 11, 8);
    private static final iz h = new iz("", (byte) 10, 9);
    private static final iz i = new iz("", (byte) 10, 10);

    /* renamed from: a  reason: collision with other field name */
    public long f744a;

    /* renamed from: a  reason: collision with other field name */
    public hv f745a;

    /* renamed from: a  reason: collision with other field name */
    public String f746a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f747a = new BitSet(3);

    /* renamed from: b  reason: collision with other field name */
    public long f748b;

    /* renamed from: b  reason: collision with other field name */
    public String f749b;

    /* renamed from: c  reason: collision with other field name */
    public long f750c;

    /* renamed from: c  reason: collision with other field name */
    public String f751c;

    /* renamed from: d  reason: collision with other field name */
    public String f752d;

    /* renamed from: e  reason: collision with other field name */
    public String f753e;

    /* renamed from: a */
    public int compareTo(in inVar) {
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        int a8;
        int a9;
        int a10;
        if (!getClass().equals(inVar.getClass())) {
            return getClass().getName().compareTo(inVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(inVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a10 = is.a(this.f746a, inVar.f746a)) != 0) {
            return a10;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(inVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a9 = is.a((Comparable) this.f745a, (Comparable) inVar.f745a)) != 0) {
            return a9;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(inVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a8 = is.a(this.f749b, inVar.f749b)) != 0) {
            return a8;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(inVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a7 = is.a(this.f751c, inVar.f751c)) != 0) {
            return a7;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(inVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a6 = is.a(this.f744a, inVar.f744a)) != 0) {
            return a6;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(inVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a5 = is.a(this.f752d, inVar.f752d)) != 0) {
            return a5;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(inVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a4 = is.a(this.f753e, inVar.f753e)) != 0) {
            return a4;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(inVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a3 = is.a(this.f748b, inVar.f748b)) != 0) {
            return a3;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(inVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (!i() || (a2 = is.a(this.f750c, inVar.f750c)) == 0) {
            return 0;
        }
        return a2;
    }

    public String a() {
        return this.f753e;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m484a() {
        if (this.f749b == null) {
            throw new jd("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f751c == null) {
            throw new jd("Required field 'appId' was not present! Struct: " + toString());
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
            boolean r6 = r5.e()
            if (r6 == 0) goto L_0x0018
            r5.a()
            return
        L_0x0018:
            com.xiaomi.push.jd r6 = new com.xiaomi.push.jd
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Required field 'errorCode' was not found in serialized data! Struct: "
            r0.append(r1)
            java.lang.String r1 = r5.toString()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r6.<init>(r0)
            throw r6
        L_0x0033:
            short r1 = r0.f784a
            r2 = 1
            r3 = 10
            r4 = 11
            switch(r1) {
                case 1: goto L_0x00ad;
                case 2: goto L_0x009a;
                case 3: goto L_0x008f;
                case 4: goto L_0x0084;
                case 5: goto L_0x003d;
                case 6: goto L_0x0076;
                case 7: goto L_0x006b;
                case 8: goto L_0x0060;
                case 9: goto L_0x0052;
                case 10: goto L_0x0044;
                default: goto L_0x003d;
            }
        L_0x003d:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r6, r0)
            goto L_0x00b7
        L_0x0044:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x003d
            long r0 = r6.a()
            r5.f750c = r0
            r5.c(r2)
            goto L_0x00b7
        L_0x0052:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x003d
            long r0 = r6.a()
            r5.f748b = r0
            r5.b((boolean) r2)
            goto L_0x00b7
        L_0x0060:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x003d
            java.lang.String r0 = r6.a()
            r5.f753e = r0
            goto L_0x00b7
        L_0x006b:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x003d
            java.lang.String r0 = r6.a()
            r5.f752d = r0
            goto L_0x00b7
        L_0x0076:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x003d
            long r0 = r6.a()
            r5.f744a = r0
            r5.a((boolean) r2)
            goto L_0x00b7
        L_0x0084:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x003d
            java.lang.String r0 = r6.a()
            r5.f751c = r0
            goto L_0x00b7
        L_0x008f:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x003d
            java.lang.String r0 = r6.a()
            r5.f749b = r0
            goto L_0x00b7
        L_0x009a:
            byte r1 = r0.a
            r2 = 12
            if (r1 != r2) goto L_0x003d
            com.xiaomi.push.hv r0 = new com.xiaomi.push.hv
            r0.<init>()
            r5.f745a = r0
            com.xiaomi.push.hv r0 = r5.f745a
            r0.a((com.xiaomi.push.jc) r6)
            goto L_0x00b7
        L_0x00ad:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x003d
            java.lang.String r0 = r6.a()
            r5.f746a = r0
        L_0x00b7:
            r6.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.in.a(com.xiaomi.push.jc):void");
    }

    public void a(boolean z) {
        this.f747a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m485a() {
        return this.f746a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m486a(in inVar) {
        if (inVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = inVar.a();
        if ((a2 || a3) && (!a2 || !a3 || !this.f746a.equals(inVar.f746a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = inVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f745a.compareTo(inVar.f745a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = inVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f749b.equals(inVar.f749b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = inVar.d();
        if (((d2 || d3) && (!d2 || !d3 || !this.f751c.equals(inVar.f751c))) || this.f744a != inVar.f744a) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = inVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f752d.equals(inVar.f752d))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = inVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f753e.equals(inVar.f753e))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = inVar.h();
        if ((h2 || h3) && (!h2 || !h3 || this.f748b != inVar.f748b)) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = inVar.i();
        if (i2 || i3) {
            return i2 && i3 && this.f750c == inVar.f750c;
        }
        return true;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f743a);
        if (this.f746a != null && a()) {
            jcVar.a(a);
            jcVar.a(this.f746a);
            jcVar.b();
        }
        if (this.f745a != null && b()) {
            jcVar.a(b);
            this.f745a.b(jcVar);
            jcVar.b();
        }
        if (this.f749b != null) {
            jcVar.a(c);
            jcVar.a(this.f749b);
            jcVar.b();
        }
        if (this.f751c != null) {
            jcVar.a(d);
            jcVar.a(this.f751c);
            jcVar.b();
        }
        jcVar.a(e);
        jcVar.a(this.f744a);
        jcVar.b();
        if (this.f752d != null && f()) {
            jcVar.a(f);
            jcVar.a(this.f752d);
            jcVar.b();
        }
        if (this.f753e != null && g()) {
            jcVar.a(g);
            jcVar.a(this.f753e);
            jcVar.b();
        }
        if (h()) {
            jcVar.a(h);
            jcVar.a(this.f748b);
            jcVar.b();
        }
        if (i()) {
            jcVar.a(i);
            jcVar.a(this.f750c);
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public void b(boolean z) {
        this.f747a.set(1, z);
    }

    public boolean b() {
        return this.f745a != null;
    }

    public void c(boolean z) {
        this.f747a.set(2, z);
    }

    public boolean c() {
        return this.f749b != null;
    }

    public boolean d() {
        return this.f751c != null;
    }

    public boolean e() {
        return this.f747a.get(0);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof in)) {
            return compareTo((in) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f752d != null;
    }

    public boolean g() {
        return this.f753e != null;
    }

    public boolean h() {
        return this.f747a.get(1);
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f747a.get(2);
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionUnRegistrationResult(");
        if (a()) {
            sb.append("debug:");
            sb.append(this.f746a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f746a);
            z = false;
        } else {
            z = true;
        }
        if (b()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("target:");
            if (this.f745a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f745a);
            }
            z = false;
        }
        if (!z) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
        }
        sb.append("id:");
        sb.append(this.f749b == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f749b);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("appId:");
        sb.append(this.f751c == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f751c);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("errorCode:");
        sb.append(this.f744a);
        if (f()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("reason:");
            sb.append(this.f752d == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f752d);
        }
        if (g()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("packageName:");
            sb.append(this.f753e == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f753e);
        }
        if (h()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("unRegisteredAt:");
            sb.append(this.f748b);
        }
        if (i()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("costTime:");
            sb.append(this.f750c);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
