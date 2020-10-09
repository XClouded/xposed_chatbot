package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.BitSet;

public class im implements ir<im, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 11, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f729a = new jh("XmPushActionUnRegistration");
    private static final iz b = new iz("", (byte) 12, 2);
    private static final iz c = new iz("", (byte) 11, 3);
    private static final iz d = new iz("", (byte) 11, 4);
    private static final iz e = new iz("", (byte) 11, 5);
    private static final iz f = new iz("", (byte) 11, 6);
    private static final iz g = new iz("", (byte) 11, 7);
    private static final iz h = new iz("", (byte) 11, 8);
    private static final iz i = new iz("", (byte) 11, 9);
    private static final iz j = new iz("", (byte) 11, 10);
    private static final iz k = new iz("", (byte) 2, 11);
    private static final iz l = new iz("", (byte) 10, 12);

    /* renamed from: a  reason: collision with other field name */
    public long f730a;

    /* renamed from: a  reason: collision with other field name */
    public hv f731a;

    /* renamed from: a  reason: collision with other field name */
    public String f732a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f733a = new BitSet(2);

    /* renamed from: a  reason: collision with other field name */
    public boolean f734a = true;

    /* renamed from: b  reason: collision with other field name */
    public String f735b;

    /* renamed from: c  reason: collision with other field name */
    public String f736c;

    /* renamed from: d  reason: collision with other field name */
    public String f737d;

    /* renamed from: e  reason: collision with other field name */
    public String f738e;

    /* renamed from: f  reason: collision with other field name */
    public String f739f;

    /* renamed from: g  reason: collision with other field name */
    public String f740g;

    /* renamed from: h  reason: collision with other field name */
    public String f741h;

    /* renamed from: i  reason: collision with other field name */
    public String f742i;

    /* renamed from: a */
    public int compareTo(im imVar) {
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
        if (!getClass().equals(imVar.getClass())) {
            return getClass().getName().compareTo(imVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(imVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a13 = is.a(this.f732a, imVar.f732a)) != 0) {
            return a13;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(imVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a12 = is.a((Comparable) this.f731a, (Comparable) imVar.f731a)) != 0) {
            return a12;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(imVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a11 = is.a(this.f735b, imVar.f735b)) != 0) {
            return a11;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(imVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a10 = is.a(this.f736c, imVar.f736c)) != 0) {
            return a10;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(imVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a9 = is.a(this.f737d, imVar.f737d)) != 0) {
            return a9;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(imVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a8 = is.a(this.f738e, imVar.f738e)) != 0) {
            return a8;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(imVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a7 = is.a(this.f739f, imVar.f739f)) != 0) {
            return a7;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(imVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a6 = is.a(this.f740g, imVar.f740g)) != 0) {
            return a6;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(imVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a5 = is.a(this.f741h, imVar.f741h)) != 0) {
            return a5;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(imVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a4 = is.a(this.f742i, imVar.f742i)) != 0) {
            return a4;
        }
        int compareTo11 = Boolean.valueOf(k()).compareTo(Boolean.valueOf(imVar.k()));
        if (compareTo11 != 0) {
            return compareTo11;
        }
        if (k() && (a3 = is.a(this.f734a, imVar.f734a)) != 0) {
            return a3;
        }
        int compareTo12 = Boolean.valueOf(l()).compareTo(Boolean.valueOf(imVar.l()));
        if (compareTo12 != 0) {
            return compareTo12;
        }
        if (!l() || (a2 = is.a(this.f730a, imVar.f730a)) == 0) {
            return 0;
        }
        return a2;
    }

    public im a(String str) {
        this.f735b = str;
        return this;
    }

    public void a() {
        if (this.f735b == null) {
            throw new jd("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f736c == null) {
            throw new jd("Required field 'appId' was not present! Struct: " + toString());
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(com.xiaomi.push.jc r5) {
        /*
            r4 = this;
            r5.a()
        L_0x0003:
            com.xiaomi.push.iz r0 = r5.a()
            byte r1 = r0.a
            if (r1 != 0) goto L_0x0012
            r5.f()
            r4.a()
            return
        L_0x0012:
            short r1 = r0.f784a
            r2 = 1
            r3 = 11
            switch(r1) {
                case 1: goto L_0x00ae;
                case 2: goto L_0x009b;
                case 3: goto L_0x0090;
                case 4: goto L_0x0085;
                case 5: goto L_0x007a;
                case 6: goto L_0x006f;
                case 7: goto L_0x0064;
                case 8: goto L_0x0059;
                case 9: goto L_0x004e;
                case 10: goto L_0x0042;
                case 11: goto L_0x0032;
                case 12: goto L_0x0021;
                default: goto L_0x001a;
            }
        L_0x001a:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r5, r0)
            goto L_0x00b8
        L_0x0021:
            byte r1 = r0.a
            r3 = 10
            if (r1 != r3) goto L_0x001a
            long r0 = r5.a()
            r4.f730a = r0
            r4.b((boolean) r2)
            goto L_0x00b8
        L_0x0032:
            byte r1 = r0.a
            r3 = 2
            if (r1 != r3) goto L_0x001a
            boolean r0 = r5.a()
            r4.f734a = r0
            r4.a((boolean) r2)
            goto L_0x00b8
        L_0x0042:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x001a
            java.lang.String r0 = r5.a()
            r4.f742i = r0
            goto L_0x00b8
        L_0x004e:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x001a
            java.lang.String r0 = r5.a()
            r4.f741h = r0
            goto L_0x00b8
        L_0x0059:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x001a
            java.lang.String r0 = r5.a()
            r4.f740g = r0
            goto L_0x00b8
        L_0x0064:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x001a
            java.lang.String r0 = r5.a()
            r4.f739f = r0
            goto L_0x00b8
        L_0x006f:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x001a
            java.lang.String r0 = r5.a()
            r4.f738e = r0
            goto L_0x00b8
        L_0x007a:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x001a
            java.lang.String r0 = r5.a()
            r4.f737d = r0
            goto L_0x00b8
        L_0x0085:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x001a
            java.lang.String r0 = r5.a()
            r4.f736c = r0
            goto L_0x00b8
        L_0x0090:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x001a
            java.lang.String r0 = r5.a()
            r4.f735b = r0
            goto L_0x00b8
        L_0x009b:
            byte r1 = r0.a
            r2 = 12
            if (r1 != r2) goto L_0x001a
            com.xiaomi.push.hv r0 = new com.xiaomi.push.hv
            r0.<init>()
            r4.f731a = r0
            com.xiaomi.push.hv r0 = r4.f731a
            r0.a((com.xiaomi.push.jc) r5)
            goto L_0x00b8
        L_0x00ae:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x001a
            java.lang.String r0 = r5.a()
            r4.f732a = r0
        L_0x00b8:
            r5.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.im.a(com.xiaomi.push.jc):void");
    }

    public void a(boolean z) {
        this.f733a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m482a() {
        return this.f732a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m483a(im imVar) {
        if (imVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = imVar.a();
        if ((a2 || a3) && (!a2 || !a3 || !this.f732a.equals(imVar.f732a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = imVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f731a.compareTo(imVar.f731a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = imVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f735b.equals(imVar.f735b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = imVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f736c.equals(imVar.f736c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = imVar.e();
        if ((e2 || e3) && (!e2 || !e3 || !this.f737d.equals(imVar.f737d))) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = imVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f738e.equals(imVar.f738e))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = imVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f739f.equals(imVar.f739f))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = imVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f740g.equals(imVar.f740g))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = imVar.i();
        if ((i2 || i3) && (!i2 || !i3 || !this.f741h.equals(imVar.f741h))) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = imVar.j();
        if ((j2 || j3) && (!j2 || !j3 || !this.f742i.equals(imVar.f742i))) {
            return false;
        }
        boolean k2 = k();
        boolean k3 = imVar.k();
        if ((k2 || k3) && (!k2 || !k3 || this.f734a != imVar.f734a)) {
            return false;
        }
        boolean l2 = l();
        boolean l3 = imVar.l();
        if (l2 || l3) {
            return l2 && l3 && this.f730a == imVar.f730a;
        }
        return true;
    }

    public im b(String str) {
        this.f736c = str;
        return this;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f729a);
        if (this.f732a != null && a()) {
            jcVar.a(a);
            jcVar.a(this.f732a);
            jcVar.b();
        }
        if (this.f731a != null && b()) {
            jcVar.a(b);
            this.f731a.b(jcVar);
            jcVar.b();
        }
        if (this.f735b != null) {
            jcVar.a(c);
            jcVar.a(this.f735b);
            jcVar.b();
        }
        if (this.f736c != null) {
            jcVar.a(d);
            jcVar.a(this.f736c);
            jcVar.b();
        }
        if (this.f737d != null && e()) {
            jcVar.a(e);
            jcVar.a(this.f737d);
            jcVar.b();
        }
        if (this.f738e != null && f()) {
            jcVar.a(f);
            jcVar.a(this.f738e);
            jcVar.b();
        }
        if (this.f739f != null && g()) {
            jcVar.a(g);
            jcVar.a(this.f739f);
            jcVar.b();
        }
        if (this.f740g != null && h()) {
            jcVar.a(h);
            jcVar.a(this.f740g);
            jcVar.b();
        }
        if (this.f741h != null && i()) {
            jcVar.a(i);
            jcVar.a(this.f741h);
            jcVar.b();
        }
        if (this.f742i != null && j()) {
            jcVar.a(j);
            jcVar.a(this.f742i);
            jcVar.b();
        }
        if (k()) {
            jcVar.a(k);
            jcVar.a(this.f734a);
            jcVar.b();
        }
        if (l()) {
            jcVar.a(l);
            jcVar.a(this.f730a);
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public void b(boolean z) {
        this.f733a.set(1, z);
    }

    public boolean b() {
        return this.f731a != null;
    }

    public im c(String str) {
        this.f737d = str;
        return this;
    }

    public boolean c() {
        return this.f735b != null;
    }

    public im d(String str) {
        this.f739f = str;
        return this;
    }

    public boolean d() {
        return this.f736c != null;
    }

    public im e(String str) {
        this.f740g = str;
        return this;
    }

    public boolean e() {
        return this.f737d != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof im)) {
            return compareTo((im) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f738e != null;
    }

    public boolean g() {
        return this.f739f != null;
    }

    public boolean h() {
        return this.f740g != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f741h != null;
    }

    public boolean j() {
        return this.f742i != null;
    }

    public boolean k() {
        return this.f733a.get(0);
    }

    public boolean l() {
        return this.f733a.get(1);
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionUnRegistration(");
        if (a()) {
            sb.append("debug:");
            sb.append(this.f732a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f732a);
            z = false;
        } else {
            z = true;
        }
        if (b()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("target:");
            if (this.f731a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f731a);
            }
            z = false;
        }
        if (!z) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
        }
        sb.append("id:");
        sb.append(this.f735b == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f735b);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("appId:");
        sb.append(this.f736c == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f736c);
        if (e()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("regId:");
            sb.append(this.f737d == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f737d);
        }
        if (f()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("appVersion:");
            sb.append(this.f738e == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f738e);
        }
        if (g()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("packageName:");
            sb.append(this.f739f == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f739f);
        }
        if (h()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("token:");
            sb.append(this.f740g == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f740g);
        }
        if (i()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("deviceId:");
            sb.append(this.f741h == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f741h);
        }
        if (j()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("aliasName:");
            sb.append(this.f742i == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f742i);
        }
        if (k()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("needAck:");
            sb.append(this.f734a);
        }
        if (l()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("createdTs:");
            sb.append(this.f730a);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
