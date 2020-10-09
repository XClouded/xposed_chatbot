package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.List;

public class io implements ir<io, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 11, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f754a = new jh("XmPushActionUnSubscription");
    private static final iz b = new iz("", (byte) 12, 2);
    private static final iz c = new iz("", (byte) 11, 3);
    private static final iz d = new iz("", (byte) 11, 4);
    private static final iz e = new iz("", (byte) 11, 5);
    private static final iz f = new iz("", (byte) 11, 6);
    private static final iz g = new iz("", (byte) 11, 7);
    private static final iz h = new iz("", (byte) 15, 8);

    /* renamed from: a  reason: collision with other field name */
    public hv f755a;

    /* renamed from: a  reason: collision with other field name */
    public String f756a;

    /* renamed from: a  reason: collision with other field name */
    public List<String> f757a;

    /* renamed from: b  reason: collision with other field name */
    public String f758b;

    /* renamed from: c  reason: collision with other field name */
    public String f759c;

    /* renamed from: d  reason: collision with other field name */
    public String f760d;

    /* renamed from: e  reason: collision with other field name */
    public String f761e;

    /* renamed from: f  reason: collision with other field name */
    public String f762f;

    /* renamed from: a */
    public int compareTo(io ioVar) {
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        int a8;
        int a9;
        if (!getClass().equals(ioVar.getClass())) {
            return getClass().getName().compareTo(ioVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(ioVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a9 = is.a(this.f756a, ioVar.f756a)) != 0) {
            return a9;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(ioVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a8 = is.a((Comparable) this.f755a, (Comparable) ioVar.f755a)) != 0) {
            return a8;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(ioVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a7 = is.a(this.f758b, ioVar.f758b)) != 0) {
            return a7;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(ioVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a6 = is.a(this.f759c, ioVar.f759c)) != 0) {
            return a6;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(ioVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a5 = is.a(this.f760d, ioVar.f760d)) != 0) {
            return a5;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(ioVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a4 = is.a(this.f761e, ioVar.f761e)) != 0) {
            return a4;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(ioVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a3 = is.a(this.f762f, ioVar.f762f)) != 0) {
            return a3;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(ioVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (!h() || (a2 = is.a((List) this.f757a, (List) ioVar.f757a)) == 0) {
            return 0;
        }
        return a2;
    }

    public io a(String str) {
        this.f758b = str;
        return this;
    }

    public void a() {
        if (this.f758b == null) {
            throw new jd("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f759c == null) {
            throw new jd("Required field 'appId' was not present! Struct: " + toString());
        } else if (this.f760d == null) {
            throw new jd("Required field 'topic' was not present! Struct: " + toString());
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
            r2 = 11
            switch(r1) {
                case 1: goto L_0x0092;
                case 2: goto L_0x007f;
                case 3: goto L_0x0074;
                case 4: goto L_0x0069;
                case 5: goto L_0x005e;
                case 6: goto L_0x0053;
                case 7: goto L_0x0048;
                case 8: goto L_0x0020;
                default: goto L_0x0019;
            }
        L_0x0019:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r5, r0)
            goto L_0x009c
        L_0x0020:
            byte r1 = r0.a
            r2 = 15
            if (r1 != r2) goto L_0x0019
            com.xiaomi.push.ja r0 = r5.a()
            java.util.ArrayList r1 = new java.util.ArrayList
            int r2 = r0.f787a
            r1.<init>(r2)
            r4.f757a = r1
            r1 = 0
        L_0x0034:
            int r2 = r0.f787a
            if (r1 >= r2) goto L_0x0044
            java.lang.String r2 = r5.a()
            java.util.List<java.lang.String> r3 = r4.f757a
            r3.add(r2)
            int r1 = r1 + 1
            goto L_0x0034
        L_0x0044:
            r5.i()
            goto L_0x009c
        L_0x0048:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r5.a()
            r4.f762f = r0
            goto L_0x009c
        L_0x0053:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r5.a()
            r4.f761e = r0
            goto L_0x009c
        L_0x005e:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r5.a()
            r4.f760d = r0
            goto L_0x009c
        L_0x0069:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r5.a()
            r4.f759c = r0
            goto L_0x009c
        L_0x0074:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r5.a()
            r4.f758b = r0
            goto L_0x009c
        L_0x007f:
            byte r1 = r0.a
            r2 = 12
            if (r1 != r2) goto L_0x0019
            com.xiaomi.push.hv r0 = new com.xiaomi.push.hv
            r0.<init>()
            r4.f755a = r0
            com.xiaomi.push.hv r0 = r4.f755a
            r0.a((com.xiaomi.push.jc) r5)
            goto L_0x009c
        L_0x0092:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r5.a()
            r4.f756a = r0
        L_0x009c:
            r5.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.io.a(com.xiaomi.push.jc):void");
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m487a() {
        return this.f756a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m488a(io ioVar) {
        if (ioVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = ioVar.a();
        if ((a2 || a3) && (!a2 || !a3 || !this.f756a.equals(ioVar.f756a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = ioVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f755a.compareTo(ioVar.f755a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = ioVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f758b.equals(ioVar.f758b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = ioVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f759c.equals(ioVar.f759c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = ioVar.e();
        if ((e2 || e3) && (!e2 || !e3 || !this.f760d.equals(ioVar.f760d))) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = ioVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f761e.equals(ioVar.f761e))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = ioVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f762f.equals(ioVar.f762f))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = ioVar.h();
        if (h2 || h3) {
            return h2 && h3 && this.f757a.equals(ioVar.f757a);
        }
        return true;
    }

    public io b(String str) {
        this.f759c = str;
        return this;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f754a);
        if (this.f756a != null && a()) {
            jcVar.a(a);
            jcVar.a(this.f756a);
            jcVar.b();
        }
        if (this.f755a != null && b()) {
            jcVar.a(b);
            this.f755a.b(jcVar);
            jcVar.b();
        }
        if (this.f758b != null) {
            jcVar.a(c);
            jcVar.a(this.f758b);
            jcVar.b();
        }
        if (this.f759c != null) {
            jcVar.a(d);
            jcVar.a(this.f759c);
            jcVar.b();
        }
        if (this.f760d != null) {
            jcVar.a(e);
            jcVar.a(this.f760d);
            jcVar.b();
        }
        if (this.f761e != null && f()) {
            jcVar.a(f);
            jcVar.a(this.f761e);
            jcVar.b();
        }
        if (this.f762f != null && g()) {
            jcVar.a(g);
            jcVar.a(this.f762f);
            jcVar.b();
        }
        if (this.f757a != null && h()) {
            jcVar.a(h);
            jcVar.a(new ja((byte) 11, this.f757a.size()));
            for (String a2 : this.f757a) {
                jcVar.a(a2);
            }
            jcVar.e();
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public boolean b() {
        return this.f755a != null;
    }

    public io c(String str) {
        this.f760d = str;
        return this;
    }

    public boolean c() {
        return this.f758b != null;
    }

    public io d(String str) {
        this.f761e = str;
        return this;
    }

    public boolean d() {
        return this.f759c != null;
    }

    public io e(String str) {
        this.f762f = str;
        return this;
    }

    public boolean e() {
        return this.f760d != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof io)) {
            return compareTo((io) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f761e != null;
    }

    public boolean g() {
        return this.f762f != null;
    }

    public boolean h() {
        return this.f757a != null;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionUnSubscription(");
        if (a()) {
            sb.append("debug:");
            sb.append(this.f756a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f756a);
            z = false;
        } else {
            z = true;
        }
        if (b()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("target:");
            if (this.f755a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f755a);
            }
            z = false;
        }
        if (!z) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
        }
        sb.append("id:");
        sb.append(this.f758b == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f758b);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("appId:");
        sb.append(this.f759c == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f759c);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("topic:");
        sb.append(this.f760d == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f760d);
        if (f()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("packageName:");
            sb.append(this.f761e == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f761e);
        }
        if (g()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("category:");
            sb.append(this.f762f == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f762f);
        }
        if (h()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("aliases:");
            if (this.f757a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f757a);
            }
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
