package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.BitSet;

public class ip implements ir<ip, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 11, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f763a = new jh("XmPushActionUnSubscriptionResult");
    private static final iz b = new iz("", (byte) 12, 2);
    private static final iz c = new iz("", (byte) 11, 3);
    private static final iz d = new iz("", (byte) 11, 4);
    private static final iz e = new iz("", (byte) 10, 6);
    private static final iz f = new iz("", (byte) 11, 7);
    private static final iz g = new iz("", (byte) 11, 8);
    private static final iz h = new iz("", (byte) 11, 9);
    private static final iz i = new iz("", (byte) 11, 10);

    /* renamed from: a  reason: collision with other field name */
    public long f764a;

    /* renamed from: a  reason: collision with other field name */
    public hv f765a;

    /* renamed from: a  reason: collision with other field name */
    public String f766a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f767a = new BitSet(1);

    /* renamed from: b  reason: collision with other field name */
    public String f768b;

    /* renamed from: c  reason: collision with other field name */
    public String f769c;

    /* renamed from: d  reason: collision with other field name */
    public String f770d;

    /* renamed from: e  reason: collision with other field name */
    public String f771e;

    /* renamed from: f  reason: collision with other field name */
    public String f772f;

    /* renamed from: g  reason: collision with other field name */
    public String f773g;

    /* renamed from: a */
    public int compareTo(ip ipVar) {
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        int a8;
        int a9;
        int a10;
        if (!getClass().equals(ipVar.getClass())) {
            return getClass().getName().compareTo(ipVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(ipVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a10 = is.a(this.f766a, ipVar.f766a)) != 0) {
            return a10;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(ipVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a9 = is.a((Comparable) this.f765a, (Comparable) ipVar.f765a)) != 0) {
            return a9;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(ipVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a8 = is.a(this.f768b, ipVar.f768b)) != 0) {
            return a8;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(ipVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a7 = is.a(this.f769c, ipVar.f769c)) != 0) {
            return a7;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(ipVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a6 = is.a(this.f764a, ipVar.f764a)) != 0) {
            return a6;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(ipVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a5 = is.a(this.f770d, ipVar.f770d)) != 0) {
            return a5;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(ipVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a4 = is.a(this.f771e, ipVar.f771e)) != 0) {
            return a4;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(ipVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a3 = is.a(this.f772f, ipVar.f772f)) != 0) {
            return a3;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(ipVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (!i() || (a2 = is.a(this.f773g, ipVar.f773g)) == 0) {
            return 0;
        }
        return a2;
    }

    public String a() {
        return this.f771e;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m489a() {
        if (this.f768b == null) {
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
            r3.f773g = r0
            goto L_0x0090
        L_0x002b:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r4.a()
            r3.f772f = r0
            goto L_0x0090
        L_0x0036:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r4.a()
            r3.f771e = r0
            goto L_0x0090
        L_0x0041:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r4.a()
            r3.f770d = r0
            goto L_0x0090
        L_0x004c:
            byte r1 = r0.a
            r2 = 10
            if (r1 != r2) goto L_0x0019
            long r0 = r4.a()
            r3.f764a = r0
            r0 = 1
            r3.a((boolean) r0)
            goto L_0x0090
        L_0x005d:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r4.a()
            r3.f769c = r0
            goto L_0x0090
        L_0x0068:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r4.a()
            r3.f768b = r0
            goto L_0x0090
        L_0x0073:
            byte r1 = r0.a
            r2 = 12
            if (r1 != r2) goto L_0x0019
            com.xiaomi.push.hv r0 = new com.xiaomi.push.hv
            r0.<init>()
            r3.f765a = r0
            com.xiaomi.push.hv r0 = r3.f765a
            r0.a((com.xiaomi.push.jc) r4)
            goto L_0x0090
        L_0x0086:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r4.a()
            r3.f766a = r0
        L_0x0090:
            r4.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.ip.a(com.xiaomi.push.jc):void");
    }

    public void a(boolean z) {
        this.f767a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m490a() {
        return this.f766a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m491a(ip ipVar) {
        if (ipVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = ipVar.a();
        if ((a2 || a3) && (!a2 || !a3 || !this.f766a.equals(ipVar.f766a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = ipVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f765a.compareTo(ipVar.f765a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = ipVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f768b.equals(ipVar.f768b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = ipVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f769c.equals(ipVar.f769c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = ipVar.e();
        if ((e2 || e3) && (!e2 || !e3 || this.f764a != ipVar.f764a)) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = ipVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f770d.equals(ipVar.f770d))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = ipVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f771e.equals(ipVar.f771e))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = ipVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f772f.equals(ipVar.f772f))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = ipVar.i();
        if (i2 || i3) {
            return i2 && i3 && this.f773g.equals(ipVar.f773g);
        }
        return true;
    }

    public String b() {
        return this.f773g;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f763a);
        if (this.f766a != null && a()) {
            jcVar.a(a);
            jcVar.a(this.f766a);
            jcVar.b();
        }
        if (this.f765a != null && b()) {
            jcVar.a(b);
            this.f765a.b(jcVar);
            jcVar.b();
        }
        if (this.f768b != null) {
            jcVar.a(c);
            jcVar.a(this.f768b);
            jcVar.b();
        }
        if (this.f769c != null && d()) {
            jcVar.a(d);
            jcVar.a(this.f769c);
            jcVar.b();
        }
        if (e()) {
            jcVar.a(e);
            jcVar.a(this.f764a);
            jcVar.b();
        }
        if (this.f770d != null && f()) {
            jcVar.a(f);
            jcVar.a(this.f770d);
            jcVar.b();
        }
        if (this.f771e != null && g()) {
            jcVar.a(g);
            jcVar.a(this.f771e);
            jcVar.b();
        }
        if (this.f772f != null && h()) {
            jcVar.a(h);
            jcVar.a(this.f772f);
            jcVar.b();
        }
        if (this.f773g != null && i()) {
            jcVar.a(i);
            jcVar.a(this.f773g);
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m492b() {
        return this.f765a != null;
    }

    public boolean c() {
        return this.f768b != null;
    }

    public boolean d() {
        return this.f769c != null;
    }

    public boolean e() {
        return this.f767a.get(0);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ip)) {
            return compareTo((ip) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f770d != null;
    }

    public boolean g() {
        return this.f771e != null;
    }

    public boolean h() {
        return this.f772f != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f773g != null;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionUnSubscriptionResult(");
        if (a()) {
            sb.append("debug:");
            sb.append(this.f766a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f766a);
            z = false;
        } else {
            z = true;
        }
        if (b()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("target:");
            if (this.f765a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f765a);
            }
            z = false;
        }
        if (!z) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
        }
        sb.append("id:");
        sb.append(this.f768b == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f768b);
        if (d()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("appId:");
            sb.append(this.f769c == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f769c);
        }
        if (e()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("errorCode:");
            sb.append(this.f764a);
        }
        if (f()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("reason:");
            sb.append(this.f770d == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f770d);
        }
        if (g()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("topic:");
            sb.append(this.f771e == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f771e);
        }
        if (h()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("packageName:");
            sb.append(this.f772f == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f772f);
        }
        if (i()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("category:");
            sb.append(this.f773g == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f773g);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
