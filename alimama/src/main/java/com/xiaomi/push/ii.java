package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.BitSet;

public class ii implements ir<ii, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 11, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f686a = new jh("XmPushActionSendFeedbackResult");
    private static final iz b = new iz("", (byte) 12, 2);
    private static final iz c = new iz("", (byte) 11, 3);
    private static final iz d = new iz("", (byte) 11, 4);
    private static final iz e = new iz("", (byte) 10, 6);
    private static final iz f = new iz("", (byte) 11, 7);
    private static final iz g = new iz("", (byte) 11, 8);

    /* renamed from: a  reason: collision with other field name */
    public long f687a;

    /* renamed from: a  reason: collision with other field name */
    public hv f688a;

    /* renamed from: a  reason: collision with other field name */
    public String f689a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f690a = new BitSet(1);

    /* renamed from: b  reason: collision with other field name */
    public String f691b;

    /* renamed from: c  reason: collision with other field name */
    public String f692c;

    /* renamed from: d  reason: collision with other field name */
    public String f693d;

    /* renamed from: e  reason: collision with other field name */
    public String f694e;

    /* renamed from: a */
    public int compareTo(ii iiVar) {
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        int a8;
        if (!getClass().equals(iiVar.getClass())) {
            return getClass().getName().compareTo(iiVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(iiVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a8 = is.a(this.f689a, iiVar.f689a)) != 0) {
            return a8;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(iiVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a7 = is.a((Comparable) this.f688a, (Comparable) iiVar.f688a)) != 0) {
            return a7;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(iiVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a6 = is.a(this.f691b, iiVar.f691b)) != 0) {
            return a6;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(iiVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a5 = is.a(this.f692c, iiVar.f692c)) != 0) {
            return a5;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(iiVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a4 = is.a(this.f687a, iiVar.f687a)) != 0) {
            return a4;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(iiVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a3 = is.a(this.f693d, iiVar.f693d)) != 0) {
            return a3;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(iiVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (!g() || (a2 = is.a(this.f694e, iiVar.f694e)) == 0) {
            return 0;
        }
        return a2;
    }

    public void a() {
        if (this.f691b == null) {
            throw new jd("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f692c == null) {
            throw new jd("Required field 'appId' was not present! Struct: " + toString());
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
            if (r1 != 0) goto L_0x0033
            r4.f()
            boolean r4 = r3.e()
            if (r4 == 0) goto L_0x0018
            r3.a()
            return
        L_0x0018:
            com.xiaomi.push.jd r4 = new com.xiaomi.push.jd
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Required field 'errorCode' was not found in serialized data! Struct: "
            r0.append(r1)
            java.lang.String r1 = r3.toString()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r4.<init>(r0)
            throw r4
        L_0x0033:
            short r1 = r0.f784a
            r2 = 11
            switch(r1) {
                case 1: goto L_0x0090;
                case 2: goto L_0x007d;
                case 3: goto L_0x0072;
                case 4: goto L_0x0067;
                case 5: goto L_0x003a;
                case 6: goto L_0x0056;
                case 7: goto L_0x004b;
                case 8: goto L_0x0040;
                default: goto L_0x003a;
            }
        L_0x003a:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r4, r0)
            goto L_0x009a
        L_0x0040:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x003a
            java.lang.String r0 = r4.a()
            r3.f694e = r0
            goto L_0x009a
        L_0x004b:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x003a
            java.lang.String r0 = r4.a()
            r3.f693d = r0
            goto L_0x009a
        L_0x0056:
            byte r1 = r0.a
            r2 = 10
            if (r1 != r2) goto L_0x003a
            long r0 = r4.a()
            r3.f687a = r0
            r0 = 1
            r3.a((boolean) r0)
            goto L_0x009a
        L_0x0067:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x003a
            java.lang.String r0 = r4.a()
            r3.f692c = r0
            goto L_0x009a
        L_0x0072:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x003a
            java.lang.String r0 = r4.a()
            r3.f691b = r0
            goto L_0x009a
        L_0x007d:
            byte r1 = r0.a
            r2 = 12
            if (r1 != r2) goto L_0x003a
            com.xiaomi.push.hv r0 = new com.xiaomi.push.hv
            r0.<init>()
            r3.f688a = r0
            com.xiaomi.push.hv r0 = r3.f688a
            r0.a((com.xiaomi.push.jc) r4)
            goto L_0x009a
        L_0x0090:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x003a
            java.lang.String r0 = r4.a()
            r3.f689a = r0
        L_0x009a:
            r4.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.ii.a(com.xiaomi.push.jc):void");
    }

    public void a(boolean z) {
        this.f690a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m465a() {
        return this.f689a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m466a(ii iiVar) {
        if (iiVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = iiVar.a();
        if ((a2 || a3) && (!a2 || !a3 || !this.f689a.equals(iiVar.f689a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = iiVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f688a.compareTo(iiVar.f688a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = iiVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f691b.equals(iiVar.f691b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = iiVar.d();
        if (((d2 || d3) && (!d2 || !d3 || !this.f692c.equals(iiVar.f692c))) || this.f687a != iiVar.f687a) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = iiVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f693d.equals(iiVar.f693d))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = iiVar.g();
        if (g2 || g3) {
            return g2 && g3 && this.f694e.equals(iiVar.f694e);
        }
        return true;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f686a);
        if (this.f689a != null && a()) {
            jcVar.a(a);
            jcVar.a(this.f689a);
            jcVar.b();
        }
        if (this.f688a != null && b()) {
            jcVar.a(b);
            this.f688a.b(jcVar);
            jcVar.b();
        }
        if (this.f691b != null) {
            jcVar.a(c);
            jcVar.a(this.f691b);
            jcVar.b();
        }
        if (this.f692c != null) {
            jcVar.a(d);
            jcVar.a(this.f692c);
            jcVar.b();
        }
        jcVar.a(e);
        jcVar.a(this.f687a);
        jcVar.b();
        if (this.f693d != null && f()) {
            jcVar.a(f);
            jcVar.a(this.f693d);
            jcVar.b();
        }
        if (this.f694e != null && g()) {
            jcVar.a(g);
            jcVar.a(this.f694e);
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public boolean b() {
        return this.f688a != null;
    }

    public boolean c() {
        return this.f691b != null;
    }

    public boolean d() {
        return this.f692c != null;
    }

    public boolean e() {
        return this.f690a.get(0);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ii)) {
            return compareTo((ii) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f693d != null;
    }

    public boolean g() {
        return this.f694e != null;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionSendFeedbackResult(");
        if (a()) {
            sb.append("debug:");
            sb.append(this.f689a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f689a);
            z = false;
        } else {
            z = true;
        }
        if (b()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("target:");
            if (this.f688a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f688a);
            }
            z = false;
        }
        if (!z) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
        }
        sb.append("id:");
        sb.append(this.f691b == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f691b);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("appId:");
        sb.append(this.f692c == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f692c);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("errorCode:");
        sb.append(this.f687a);
        if (f()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("reason:");
            sb.append(this.f693d == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f693d);
        }
        if (g()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("category:");
            sb.append(this.f694e == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f694e);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
