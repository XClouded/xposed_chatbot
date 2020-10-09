package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.BitSet;

public class hv implements ir<hv, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 10, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f532a = new jh("Target");
    private static final iz b = new iz("", (byte) 11, 2);
    private static final iz c = new iz("", (byte) 11, 3);
    private static final iz d = new iz("", (byte) 11, 4);
    private static final iz e = new iz("", (byte) 2, 5);
    private static final iz f = new iz("", (byte) 11, 7);

    /* renamed from: a  reason: collision with other field name */
    public long f533a = 5;

    /* renamed from: a  reason: collision with other field name */
    public String f534a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f535a = new BitSet(2);

    /* renamed from: a  reason: collision with other field name */
    public boolean f536a = false;

    /* renamed from: b  reason: collision with other field name */
    public String f537b = "xiaomi.com";

    /* renamed from: c  reason: collision with other field name */
    public String f538c = "";

    /* renamed from: d  reason: collision with other field name */
    public String f539d;

    /* renamed from: a */
    public int compareTo(hv hvVar) {
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        if (!getClass().equals(hvVar.getClass())) {
            return getClass().getName().compareTo(hvVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(hvVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a7 = is.a(this.f533a, hvVar.f533a)) != 0) {
            return a7;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(hvVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a6 = is.a(this.f534a, hvVar.f534a)) != 0) {
            return a6;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(hvVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a5 = is.a(this.f537b, hvVar.f537b)) != 0) {
            return a5;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(hvVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a4 = is.a(this.f538c, hvVar.f538c)) != 0) {
            return a4;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(hvVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a3 = is.a(this.f536a, hvVar.f536a)) != 0) {
            return a3;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(hvVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (!f() || (a2 = is.a(this.f539d, hvVar.f539d)) == 0) {
            return 0;
        }
        return a2;
    }

    public void a() {
        if (this.f534a == null) {
            throw new jd("Required field 'userId' was not present! Struct: " + toString());
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
            if (r1 != 0) goto L_0x0033
            r5.f()
            boolean r5 = r4.a()
            if (r5 == 0) goto L_0x0018
            r4.a()
            return
        L_0x0018:
            com.xiaomi.push.jd r5 = new com.xiaomi.push.jd
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Required field 'channelId' was not found in serialized data! Struct: "
            r0.append(r1)
            java.lang.String r1 = r4.toString()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r5.<init>(r0)
            throw r5
        L_0x0033:
            short r1 = r0.f784a
            r2 = 7
            r3 = 11
            if (r1 == r2) goto L_0x0084
            r2 = 1
            switch(r1) {
                case 1: goto L_0x0074;
                case 2: goto L_0x0069;
                case 3: goto L_0x005e;
                case 4: goto L_0x0053;
                case 5: goto L_0x0044;
                default: goto L_0x003e;
            }
        L_0x003e:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r5, r0)
            goto L_0x008e
        L_0x0044:
            byte r1 = r0.a
            r3 = 2
            if (r1 != r3) goto L_0x003e
            boolean r0 = r5.a()
            r4.f536a = r0
            r4.b((boolean) r2)
            goto L_0x008e
        L_0x0053:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x003e
            java.lang.String r0 = r5.a()
            r4.f538c = r0
            goto L_0x008e
        L_0x005e:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x003e
            java.lang.String r0 = r5.a()
            r4.f537b = r0
            goto L_0x008e
        L_0x0069:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x003e
            java.lang.String r0 = r5.a()
            r4.f534a = r0
            goto L_0x008e
        L_0x0074:
            byte r1 = r0.a
            r3 = 10
            if (r1 != r3) goto L_0x003e
            long r0 = r5.a()
            r4.f533a = r0
            r4.a((boolean) r2)
            goto L_0x008e
        L_0x0084:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x003e
            java.lang.String r0 = r5.a()
            r4.f539d = r0
        L_0x008e:
            r5.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.hv.a(com.xiaomi.push.jc):void");
    }

    public void a(boolean z) {
        this.f535a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m408a() {
        return this.f535a.get(0);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m409a(hv hvVar) {
        if (hvVar == null || this.f533a != hvVar.f533a) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = hvVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f534a.equals(hvVar.f534a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = hvVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f537b.equals(hvVar.f537b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = hvVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f538c.equals(hvVar.f538c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = hvVar.e();
        if ((e2 || e3) && (!e2 || !e3 || this.f536a != hvVar.f536a)) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = hvVar.f();
        if (f2 || f3) {
            return f2 && f3 && this.f539d.equals(hvVar.f539d);
        }
        return true;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f532a);
        jcVar.a(a);
        jcVar.a(this.f533a);
        jcVar.b();
        if (this.f534a != null) {
            jcVar.a(b);
            jcVar.a(this.f534a);
            jcVar.b();
        }
        if (this.f537b != null && c()) {
            jcVar.a(c);
            jcVar.a(this.f537b);
            jcVar.b();
        }
        if (this.f538c != null && d()) {
            jcVar.a(d);
            jcVar.a(this.f538c);
            jcVar.b();
        }
        if (e()) {
            jcVar.a(e);
            jcVar.a(this.f536a);
            jcVar.b();
        }
        if (this.f539d != null && f()) {
            jcVar.a(f);
            jcVar.a(this.f539d);
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public void b(boolean z) {
        this.f535a.set(1, z);
    }

    public boolean b() {
        return this.f534a != null;
    }

    public boolean c() {
        return this.f537b != null;
    }

    public boolean d() {
        return this.f538c != null;
    }

    public boolean e() {
        return this.f535a.get(1);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof hv)) {
            return compareTo((hv) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f539d != null;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Target(");
        sb.append("channelId:");
        sb.append(this.f533a);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("userId:");
        sb.append(this.f534a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f534a);
        if (c()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("server:");
            sb.append(this.f537b == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f537b);
        }
        if (d()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("resource:");
            sb.append(this.f538c == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f538c);
        }
        if (e()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("isPreview:");
            sb.append(this.f536a);
        }
        if (f()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("token:");
            sb.append(this.f539d == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f539d);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
