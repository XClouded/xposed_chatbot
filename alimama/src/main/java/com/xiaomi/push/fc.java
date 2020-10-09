package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.BitSet;

public class fc implements ir<fc, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 3, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f331a = new jh("StatsEvent");
    private static final iz b = new iz("", (byte) 8, 2);
    private static final iz c = new iz("", (byte) 8, 3);
    private static final iz d = new iz("", (byte) 11, 4);
    private static final iz e = new iz("", (byte) 11, 5);
    private static final iz f = new iz("", (byte) 8, 6);
    private static final iz g = new iz("", (byte) 11, 7);
    private static final iz h = new iz("", (byte) 11, 8);
    private static final iz i = new iz("", (byte) 8, 9);
    private static final iz j = new iz("", (byte) 8, 10);

    /* renamed from: a  reason: collision with other field name */
    public byte f332a;

    /* renamed from: a  reason: collision with other field name */
    public int f333a;

    /* renamed from: a  reason: collision with other field name */
    public String f334a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f335a = new BitSet(6);

    /* renamed from: b  reason: collision with other field name */
    public int f336b;

    /* renamed from: b  reason: collision with other field name */
    public String f337b;

    /* renamed from: c  reason: collision with other field name */
    public int f338c;

    /* renamed from: c  reason: collision with other field name */
    public String f339c;

    /* renamed from: d  reason: collision with other field name */
    public int f340d;

    /* renamed from: d  reason: collision with other field name */
    public String f341d;

    /* renamed from: e  reason: collision with other field name */
    public int f342e;

    /* renamed from: a */
    public int compareTo(fc fcVar) {
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
        if (!getClass().equals(fcVar.getClass())) {
            return getClass().getName().compareTo(fcVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(fcVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a11 = is.a(this.f332a, fcVar.f332a)) != 0) {
            return a11;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(fcVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a10 = is.a(this.f333a, fcVar.f333a)) != 0) {
            return a10;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(fcVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a9 = is.a(this.f336b, fcVar.f336b)) != 0) {
            return a9;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(fcVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a8 = is.a(this.f334a, fcVar.f334a)) != 0) {
            return a8;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(fcVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a7 = is.a(this.f337b, fcVar.f337b)) != 0) {
            return a7;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(fcVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a6 = is.a(this.f338c, fcVar.f338c)) != 0) {
            return a6;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(fcVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a5 = is.a(this.f339c, fcVar.f339c)) != 0) {
            return a5;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(fcVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a4 = is.a(this.f341d, fcVar.f341d)) != 0) {
            return a4;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(fcVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a3 = is.a(this.f340d, fcVar.f340d)) != 0) {
            return a3;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(fcVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (!j() || (a2 = is.a(this.f342e, fcVar.f342e)) == 0) {
            return 0;
        }
        return a2;
    }

    public fc a(byte b2) {
        this.f332a = b2;
        a(true);
        return this;
    }

    public fc a(int i2) {
        this.f333a = i2;
        b(true);
        return this;
    }

    public fc a(String str) {
        this.f334a = str;
        return this;
    }

    public void a() {
        if (this.f334a == null) {
            throw new jd("Required field 'connpt' was not present! Struct: " + toString());
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
            if (r1 != 0) goto L_0x0075
            r6.f()
            boolean r6 = r5.a()
            if (r6 == 0) goto L_0x005a
            boolean r6 = r5.b()
            if (r6 == 0) goto L_0x003f
            boolean r6 = r5.c()
            if (r6 == 0) goto L_0x0024
            r5.a()
            return
        L_0x0024:
            com.xiaomi.push.jd r6 = new com.xiaomi.push.jd
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Required field 'value' was not found in serialized data! Struct: "
            r0.append(r1)
            java.lang.String r1 = r5.toString()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r6.<init>(r0)
            throw r6
        L_0x003f:
            com.xiaomi.push.jd r6 = new com.xiaomi.push.jd
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Required field 'type' was not found in serialized data! Struct: "
            r0.append(r1)
            java.lang.String r1 = r5.toString()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r6.<init>(r0)
            throw r6
        L_0x005a:
            com.xiaomi.push.jd r6 = new com.xiaomi.push.jd
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Required field 'chid' was not found in serialized data! Struct: "
            r0.append(r1)
            java.lang.String r1 = r5.toString()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r6.<init>(r0)
            throw r6
        L_0x0075:
            short r1 = r0.f784a
            r2 = 11
            r3 = 8
            r4 = 1
            switch(r1) {
                case 1: goto L_0x00f9;
                case 2: goto L_0x00eb;
                case 3: goto L_0x00dd;
                case 4: goto L_0x00d2;
                case 5: goto L_0x00c7;
                case 6: goto L_0x00b9;
                case 7: goto L_0x00ae;
                case 8: goto L_0x00a3;
                case 9: goto L_0x0095;
                case 10: goto L_0x0086;
                default: goto L_0x007f;
            }
        L_0x007f:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r6, r0)
            goto L_0x0107
        L_0x0086:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x007f
            int r0 = r6.a()
            r5.f342e = r0
            r5.f(r4)
            goto L_0x0107
        L_0x0095:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x007f
            int r0 = r6.a()
            r5.f340d = r0
            r5.e(r4)
            goto L_0x0107
        L_0x00a3:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x007f
            java.lang.String r0 = r6.a()
            r5.f341d = r0
            goto L_0x0107
        L_0x00ae:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x007f
            java.lang.String r0 = r6.a()
            r5.f339c = r0
            goto L_0x0107
        L_0x00b9:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x007f
            int r0 = r6.a()
            r5.f338c = r0
            r5.d((boolean) r4)
            goto L_0x0107
        L_0x00c7:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x007f
            java.lang.String r0 = r6.a()
            r5.f337b = r0
            goto L_0x0107
        L_0x00d2:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x007f
            java.lang.String r0 = r6.a()
            r5.f334a = r0
            goto L_0x0107
        L_0x00dd:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x007f
            int r0 = r6.a()
            r5.f336b = r0
            r5.c((boolean) r4)
            goto L_0x0107
        L_0x00eb:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x007f
            int r0 = r6.a()
            r5.f333a = r0
            r5.b((boolean) r4)
            goto L_0x0107
        L_0x00f9:
            byte r1 = r0.a
            r2 = 3
            if (r1 != r2) goto L_0x007f
            byte r0 = r6.a()
            r5.f332a = r0
            r5.a((boolean) r4)
        L_0x0107:
            r6.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.fc.a(com.xiaomi.push.jc):void");
    }

    public void a(boolean z) {
        this.f335a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m286a() {
        return this.f335a.get(0);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m287a(fc fcVar) {
        if (fcVar == null || this.f332a != fcVar.f332a || this.f333a != fcVar.f333a || this.f336b != fcVar.f336b) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = fcVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f334a.equals(fcVar.f334a))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = fcVar.e();
        if ((e2 || e3) && (!e2 || !e3 || !this.f337b.equals(fcVar.f337b))) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = fcVar.f();
        if ((f2 || f3) && (!f2 || !f3 || this.f338c != fcVar.f338c)) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = fcVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f339c.equals(fcVar.f339c))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = fcVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f341d.equals(fcVar.f341d))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = fcVar.i();
        if ((i2 || i3) && (!i2 || !i3 || this.f340d != fcVar.f340d)) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = fcVar.j();
        if (j2 || j3) {
            return j2 && j3 && this.f342e == fcVar.f342e;
        }
        return true;
    }

    public fc b(int i2) {
        this.f336b = i2;
        c(true);
        return this;
    }

    public fc b(String str) {
        this.f337b = str;
        return this;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f331a);
        jcVar.a(a);
        jcVar.a(this.f332a);
        jcVar.b();
        jcVar.a(b);
        jcVar.a(this.f333a);
        jcVar.b();
        jcVar.a(c);
        jcVar.a(this.f336b);
        jcVar.b();
        if (this.f334a != null) {
            jcVar.a(d);
            jcVar.a(this.f334a);
            jcVar.b();
        }
        if (this.f337b != null && e()) {
            jcVar.a(e);
            jcVar.a(this.f337b);
            jcVar.b();
        }
        if (f()) {
            jcVar.a(f);
            jcVar.a(this.f338c);
            jcVar.b();
        }
        if (this.f339c != null && g()) {
            jcVar.a(g);
            jcVar.a(this.f339c);
            jcVar.b();
        }
        if (this.f341d != null && h()) {
            jcVar.a(h);
            jcVar.a(this.f341d);
            jcVar.b();
        }
        if (i()) {
            jcVar.a(i);
            jcVar.a(this.f340d);
            jcVar.b();
        }
        if (j()) {
            jcVar.a(j);
            jcVar.a(this.f342e);
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public void b(boolean z) {
        this.f335a.set(1, z);
    }

    public boolean b() {
        return this.f335a.get(1);
    }

    public fc c(int i2) {
        this.f338c = i2;
        d(true);
        return this;
    }

    public fc c(String str) {
        this.f339c = str;
        return this;
    }

    public void c(boolean z) {
        this.f335a.set(2, z);
    }

    public boolean c() {
        return this.f335a.get(2);
    }

    public fc d(int i2) {
        this.f340d = i2;
        e(true);
        return this;
    }

    public fc d(String str) {
        this.f341d = str;
        return this;
    }

    public void d(boolean z) {
        this.f335a.set(3, z);
    }

    public boolean d() {
        return this.f334a != null;
    }

    public void e(boolean z) {
        this.f335a.set(4, z);
    }

    public boolean e() {
        return this.f337b != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof fc)) {
            return compareTo((fc) obj);
        }
        return false;
    }

    public void f(boolean z) {
        this.f335a.set(5, z);
    }

    public boolean f() {
        return this.f335a.get(3);
    }

    public boolean g() {
        return this.f339c != null;
    }

    public boolean h() {
        return this.f341d != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f335a.get(4);
    }

    public boolean j() {
        return this.f335a.get(5);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("StatsEvent(");
        sb.append("chid:");
        sb.append(this.f332a);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("type:");
        sb.append(this.f333a);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("value:");
        sb.append(this.f336b);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("connpt:");
        sb.append(this.f334a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f334a);
        if (e()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("host:");
            sb.append(this.f337b == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f337b);
        }
        if (f()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("subvalue:");
            sb.append(this.f338c);
        }
        if (g()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("annotation:");
            sb.append(this.f339c == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f339c);
        }
        if (h()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("user:");
            sb.append(this.f341d == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f341d);
        }
        if (i()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("time:");
            sb.append(this.f340d);
        }
        if (j()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("clientIp:");
            sb.append(this.f342e);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
