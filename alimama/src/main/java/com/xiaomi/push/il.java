package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.BitSet;

public class il implements ir<il, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 11, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f718a = new jh("XmPushActionSubscriptionResult");
    private static final iz b = new iz("", (byte) 12, 2);
    private static final iz c = new iz("", (byte) 11, 3);
    private static final iz d = new iz("", (byte) 11, 4);
    private static final iz e = new iz("", (byte) 10, 6);
    private static final iz f = new iz("", (byte) 11, 7);
    private static final iz g = new iz("", (byte) 11, 8);
    private static final iz h = new iz("", (byte) 11, 9);
    private static final iz i = new iz("", (byte) 11, 10);

    /* renamed from: a  reason: collision with other field name */
    public long f719a;

    /* renamed from: a  reason: collision with other field name */
    public hv f720a;

    /* renamed from: a  reason: collision with other field name */
    public String f721a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f722a = new BitSet(1);

    /* renamed from: b  reason: collision with other field name */
    public String f723b;

    /* renamed from: c  reason: collision with other field name */
    public String f724c;

    /* renamed from: d  reason: collision with other field name */
    public String f725d;

    /* renamed from: e  reason: collision with other field name */
    public String f726e;

    /* renamed from: f  reason: collision with other field name */
    public String f727f;

    /* renamed from: g  reason: collision with other field name */
    public String f728g;

    /* renamed from: a */
    public int compareTo(il ilVar) {
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        int a8;
        int a9;
        int a10;
        if (!getClass().equals(ilVar.getClass())) {
            return getClass().getName().compareTo(ilVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(ilVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a10 = is.a(this.f721a, ilVar.f721a)) != 0) {
            return a10;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(ilVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a9 = is.a((Comparable) this.f720a, (Comparable) ilVar.f720a)) != 0) {
            return a9;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(ilVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a8 = is.a(this.f723b, ilVar.f723b)) != 0) {
            return a8;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(ilVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a7 = is.a(this.f724c, ilVar.f724c)) != 0) {
            return a7;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(ilVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a6 = is.a(this.f719a, ilVar.f719a)) != 0) {
            return a6;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(ilVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a5 = is.a(this.f725d, ilVar.f725d)) != 0) {
            return a5;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(ilVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a4 = is.a(this.f726e, ilVar.f726e)) != 0) {
            return a4;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(ilVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a3 = is.a(this.f727f, ilVar.f727f)) != 0) {
            return a3;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(ilVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (!i() || (a2 = is.a(this.f728g, ilVar.f728g)) == 0) {
            return 0;
        }
        return a2;
    }

    public String a() {
        return this.f726e;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m478a() {
        if (this.f723b == null) {
            throw new jd("Required field 'id' was not present! Struct: " + toString());
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(com.xiaomi.push.jc r4) {
        /*
            r3 = this;
            r4.a()
        L_0x0003:
            com.xiaomi.push.iz r0 = r4.a()
            byte r1 = r0.a
            if (r1 != 0) goto L_0x0012
            r4.f()
            r3.a()
            return
        L_0x0012:
            short r1 = r0.f784a
            r2 = 11
            switch(r1) {
                case 1: goto L_0x0086;
                case 2: goto L_0x0073;
                case 3: goto L_0x0068;
                case 4: goto L_0x005d;
                case 5: goto L_0x0019;
                case 6: goto L_0x004c;
                case 7: goto L_0x0041;
                case 8: goto L_0x0036;
                case 9: goto L_0x002b;
                case 10: goto L_0x0020;
                default: goto L_0x0019;
            }
        L_0x0019:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r4, r0)
            goto L_0x0090
        L_0x0020:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r4.a()
            r3.f728g = r0
            goto L_0x0090
        L_0x002b:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r4.a()
            r3.f727f = r0
            goto L_0x0090
        L_0x0036:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r4.a()
            r3.f726e = r0
            goto L_0x0090
        L_0x0041:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r4.a()
            r3.f725d = r0
            goto L_0x0090
        L_0x004c:
            byte r1 = r0.a
            r2 = 10
            if (r1 != r2) goto L_0x0019
            long r0 = r4.a()
            r3.f719a = r0
            r0 = 1
            r3.a((boolean) r0)
            goto L_0x0090
        L_0x005d:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r4.a()
            r3.f724c = r0
            goto L_0x0090
        L_0x0068:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r4.a()
            r3.f723b = r0
            goto L_0x0090
        L_0x0073:
            byte r1 = r0.a
            r2 = 12
            if (r1 != r2) goto L_0x0019
            com.xiaomi.push.hv r0 = new com.xiaomi.push.hv
            r0.<init>()
            r3.f720a = r0
            com.xiaomi.push.hv r0 = r3.f720a
            r0.a((com.xiaomi.push.jc) r4)
            goto L_0x0090
        L_0x0086:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r4.a()
            r3.f721a = r0
        L_0x0090:
            r4.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.il.a(com.xiaomi.push.jc):void");
    }

    public void a(boolean z) {
        this.f722a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m479a() {
        return this.f721a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m480a(il ilVar) {
        if (ilVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = ilVar.a();
        if ((a2 || a3) && (!a2 || !a3 || !this.f721a.equals(ilVar.f721a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = ilVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f720a.compareTo(ilVar.f720a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = ilVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f723b.equals(ilVar.f723b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = ilVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f724c.equals(ilVar.f724c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = ilVar.e();
        if ((e2 || e3) && (!e2 || !e3 || this.f719a != ilVar.f719a)) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = ilVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f725d.equals(ilVar.f725d))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = ilVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f726e.equals(ilVar.f726e))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = ilVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f727f.equals(ilVar.f727f))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = ilVar.i();
        if (i2 || i3) {
            return i2 && i3 && this.f728g.equals(ilVar.f728g);
        }
        return true;
    }

    public String b() {
        return this.f728g;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f718a);
        if (this.f721a != null && a()) {
            jcVar.a(a);
            jcVar.a(this.f721a);
            jcVar.b();
        }
        if (this.f720a != null && b()) {
            jcVar.a(b);
            this.f720a.b(jcVar);
            jcVar.b();
        }
        if (this.f723b != null) {
            jcVar.a(c);
            jcVar.a(this.f723b);
            jcVar.b();
        }
        if (this.f724c != null && d()) {
            jcVar.a(d);
            jcVar.a(this.f724c);
            jcVar.b();
        }
        if (e()) {
            jcVar.a(e);
            jcVar.a(this.f719a);
            jcVar.b();
        }
        if (this.f725d != null && f()) {
            jcVar.a(f);
            jcVar.a(this.f725d);
            jcVar.b();
        }
        if (this.f726e != null && g()) {
            jcVar.a(g);
            jcVar.a(this.f726e);
            jcVar.b();
        }
        if (this.f727f != null && h()) {
            jcVar.a(h);
            jcVar.a(this.f727f);
            jcVar.b();
        }
        if (this.f728g != null && i()) {
            jcVar.a(i);
            jcVar.a(this.f728g);
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m481b() {
        return this.f720a != null;
    }

    public boolean c() {
        return this.f723b != null;
    }

    public boolean d() {
        return this.f724c != null;
    }

    public boolean e() {
        return this.f722a.get(0);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof il)) {
            return compareTo((il) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f725d != null;
    }

    public boolean g() {
        return this.f726e != null;
    }

    public boolean h() {
        return this.f727f != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f728g != null;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionSubscriptionResult(");
        if (a()) {
            sb.append("debug:");
            sb.append(this.f721a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f721a);
            z = false;
        } else {
            z = true;
        }
        if (b()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("target:");
            if (this.f720a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f720a);
            }
            z = false;
        }
        if (!z) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
        }
        sb.append("id:");
        sb.append(this.f723b == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f723b);
        if (d()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("appId:");
            sb.append(this.f724c == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f724c);
        }
        if (e()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("errorCode:");
            sb.append(this.f719a);
        }
        if (f()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("reason:");
            sb.append(this.f725d == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f725d);
        }
        if (g()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("topic:");
            sb.append(this.f726e == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f726e);
        }
        if (h()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("packageName:");
            sb.append(this.f727f == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f727f);
        }
        if (i()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("category:");
            sb.append(this.f728g == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f728g);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
