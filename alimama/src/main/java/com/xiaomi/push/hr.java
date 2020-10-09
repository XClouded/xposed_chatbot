package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.BitSet;

public class hr implements ir<hr, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 8, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f486a = new jh("OnlineConfigItem");
    private static final iz b = new iz("", (byte) 8, 2);
    private static final iz c = new iz("", (byte) 2, 3);
    private static final iz d = new iz("", (byte) 8, 4);
    private static final iz e = new iz("", (byte) 10, 5);
    private static final iz f = new iz("", (byte) 11, 6);
    private static final iz g = new iz("", (byte) 2, 7);

    /* renamed from: a  reason: collision with other field name */
    public int f487a;

    /* renamed from: a  reason: collision with other field name */
    public long f488a;

    /* renamed from: a  reason: collision with other field name */
    public String f489a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f490a = new BitSet(6);

    /* renamed from: a  reason: collision with other field name */
    public boolean f491a;

    /* renamed from: b  reason: collision with other field name */
    public int f492b;

    /* renamed from: b  reason: collision with other field name */
    public boolean f493b;

    /* renamed from: c  reason: collision with other field name */
    public int f494c;

    public int a() {
        return this.f487a;
    }

    /* renamed from: a */
    public int compareTo(hr hrVar) {
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        int a8;
        if (!getClass().equals(hrVar.getClass())) {
            return getClass().getName().compareTo(hrVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(hrVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a8 = is.a(this.f487a, hrVar.f487a)) != 0) {
            return a8;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(hrVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a7 = is.a(this.f492b, hrVar.f492b)) != 0) {
            return a7;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(hrVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a6 = is.a(this.f491a, hrVar.f491a)) != 0) {
            return a6;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(hrVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a5 = is.a(this.f494c, hrVar.f494c)) != 0) {
            return a5;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(hrVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a4 = is.a(this.f488a, hrVar.f488a)) != 0) {
            return a4;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(hrVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a3 = is.a(this.f489a, hrVar.f489a)) != 0) {
            return a3;
        }
        int compareTo7 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(hrVar.h()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (!h() || (a2 = is.a(this.f493b, hrVar.f493b)) == 0) {
            return 0;
        }
        return a2;
    }

    /* renamed from: a  reason: collision with other method in class */
    public long m382a() {
        return this.f488a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m383a() {
        return this.f489a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m384a() {
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
            if (r1 != 0) goto L_0x0012
            r6.f()
            r5.a()
            return
        L_0x0012:
            short r1 = r0.f784a
            r2 = 2
            r3 = 8
            r4 = 1
            switch(r1) {
                case 1: goto L_0x0076;
                case 2: goto L_0x0068;
                case 3: goto L_0x005a;
                case 4: goto L_0x004c;
                case 5: goto L_0x003c;
                case 6: goto L_0x002f;
                case 7: goto L_0x0021;
                default: goto L_0x001b;
            }
        L_0x001b:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r6, r0)
            goto L_0x0083
        L_0x0021:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x001b
            boolean r0 = r6.a()
            r5.f493b = r0
            r5.f(r4)
            goto L_0x0083
        L_0x002f:
            byte r1 = r0.a
            r2 = 11
            if (r1 != r2) goto L_0x001b
            java.lang.String r0 = r6.a()
            r5.f489a = r0
            goto L_0x0083
        L_0x003c:
            byte r1 = r0.a
            r2 = 10
            if (r1 != r2) goto L_0x001b
            long r0 = r6.a()
            r5.f488a = r0
            r5.e(r4)
            goto L_0x0083
        L_0x004c:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x001b
            int r0 = r6.a()
            r5.f494c = r0
            r5.d(r4)
            goto L_0x0083
        L_0x005a:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x001b
            boolean r0 = r6.a()
            r5.f491a = r0
            r5.c(r4)
            goto L_0x0083
        L_0x0068:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x001b
            int r0 = r6.a()
            r5.f492b = r0
            r5.b((boolean) r4)
            goto L_0x0083
        L_0x0076:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x001b
            int r0 = r6.a()
            r5.f487a = r0
            r5.a((boolean) r4)
        L_0x0083:
            r6.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.hr.a(com.xiaomi.push.jc):void");
    }

    public void a(boolean z) {
        this.f490a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m385a() {
        return this.f490a.get(0);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m386a(hr hrVar) {
        if (hrVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = hrVar.a();
        if ((a2 || a3) && (!a2 || !a3 || this.f487a != hrVar.f487a)) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = hrVar.b();
        if ((b2 || b3) && (!b2 || !b3 || this.f492b != hrVar.f492b)) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = hrVar.c();
        if ((c2 || c3) && (!c2 || !c3 || this.f491a != hrVar.f491a)) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = hrVar.d();
        if ((d2 || d3) && (!d2 || !d3 || this.f494c != hrVar.f494c)) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = hrVar.e();
        if ((e2 || e3) && (!e2 || !e3 || this.f488a != hrVar.f488a)) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = hrVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f489a.equals(hrVar.f489a))) {
            return false;
        }
        boolean h = h();
        boolean h2 = hrVar.h();
        if (h || h2) {
            return h && h2 && this.f493b == hrVar.f493b;
        }
        return true;
    }

    public int b() {
        return this.f492b;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f486a);
        if (a()) {
            jcVar.a(a);
            jcVar.a(this.f487a);
            jcVar.b();
        }
        if (b()) {
            jcVar.a(b);
            jcVar.a(this.f492b);
            jcVar.b();
        }
        if (c()) {
            jcVar.a(c);
            jcVar.a(this.f491a);
            jcVar.b();
        }
        if (d()) {
            jcVar.a(d);
            jcVar.a(this.f494c);
            jcVar.b();
        }
        if (e()) {
            jcVar.a(e);
            jcVar.a(this.f488a);
            jcVar.b();
        }
        if (this.f489a != null && f()) {
            jcVar.a(f);
            jcVar.a(this.f489a);
            jcVar.b();
        }
        if (h()) {
            jcVar.a(g);
            jcVar.a(this.f493b);
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public void b(boolean z) {
        this.f490a.set(1, z);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m387b() {
        return this.f490a.get(1);
    }

    public int c() {
        return this.f494c;
    }

    public void c(boolean z) {
        this.f490a.set(2, z);
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m388c() {
        return this.f490a.get(2);
    }

    public void d(boolean z) {
        this.f490a.set(3, z);
    }

    public boolean d() {
        return this.f490a.get(3);
    }

    public void e(boolean z) {
        this.f490a.set(4, z);
    }

    public boolean e() {
        return this.f490a.get(4);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof hr)) {
            return compareTo((hr) obj);
        }
        return false;
    }

    public void f(boolean z) {
        this.f490a.set(5, z);
    }

    public boolean f() {
        return this.f489a != null;
    }

    public boolean g() {
        return this.f493b;
    }

    public boolean h() {
        return this.f490a.get(5);
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("OnlineConfigItem(");
        if (a()) {
            sb.append("key:");
            sb.append(this.f487a);
            z = false;
        } else {
            z = true;
        }
        if (b()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("type:");
            sb.append(this.f492b);
            z = false;
        }
        if (c()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("clear:");
            sb.append(this.f491a);
            z = false;
        }
        if (d()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("intValue:");
            sb.append(this.f494c);
            z = false;
        }
        if (e()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("longValue:");
            sb.append(this.f488a);
            z = false;
        }
        if (f()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("stringValue:");
            sb.append(this.f489a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f489a);
            z = false;
        }
        if (h()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("boolValue:");
            sb.append(this.f493b);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
