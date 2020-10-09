package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.BitSet;
import java.util.List;

public class ib implements ir<ib, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 12, 2);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f593a = new jh("XmPushActionCommandResult");
    private static final iz b = new iz("", (byte) 11, 3);
    private static final iz c = new iz("", (byte) 11, 4);
    private static final iz d = new iz("", (byte) 11, 5);
    private static final iz e = new iz("", (byte) 10, 7);
    private static final iz f = new iz("", (byte) 11, 8);
    private static final iz g = new iz("", (byte) 11, 9);
    private static final iz h = new iz("", (byte) 15, 10);
    private static final iz i = new iz("", (byte) 11, 12);
    private static final iz j = new iz("", (byte) 2, 13);

    /* renamed from: a  reason: collision with other field name */
    public long f594a;

    /* renamed from: a  reason: collision with other field name */
    public hv f595a;

    /* renamed from: a  reason: collision with other field name */
    public String f596a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f597a = new BitSet(2);

    /* renamed from: a  reason: collision with other field name */
    public List<String> f598a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f599a = true;

    /* renamed from: b  reason: collision with other field name */
    public String f600b;

    /* renamed from: c  reason: collision with other field name */
    public String f601c;

    /* renamed from: d  reason: collision with other field name */
    public String f602d;

    /* renamed from: e  reason: collision with other field name */
    public String f603e;

    /* renamed from: f  reason: collision with other field name */
    public String f604f;

    /* renamed from: a */
    public int compareTo(ib ibVar) {
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
        if (!getClass().equals(ibVar.getClass())) {
            return getClass().getName().compareTo(ibVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(ibVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a11 = is.a((Comparable) this.f595a, (Comparable) ibVar.f595a)) != 0) {
            return a11;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(ibVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a10 = is.a(this.f596a, ibVar.f596a)) != 0) {
            return a10;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(ibVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a9 = is.a(this.f600b, ibVar.f600b)) != 0) {
            return a9;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(ibVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a8 = is.a(this.f601c, ibVar.f601c)) != 0) {
            return a8;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(ibVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a7 = is.a(this.f594a, ibVar.f594a)) != 0) {
            return a7;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(ibVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a6 = is.a(this.f602d, ibVar.f602d)) != 0) {
            return a6;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(ibVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a5 = is.a(this.f603e, ibVar.f603e)) != 0) {
            return a5;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(ibVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a4 = is.a((List) this.f598a, (List) ibVar.f598a)) != 0) {
            return a4;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(ibVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a3 = is.a(this.f604f, ibVar.f604f)) != 0) {
            return a3;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(ibVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (!j() || (a2 = is.a(this.f599a, ibVar.f599a)) == 0) {
            return 0;
        }
        return a2;
    }

    public String a() {
        return this.f601c;
    }

    /* renamed from: a  reason: collision with other method in class */
    public List<String> m427a() {
        return this.f598a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m428a() {
        if (this.f596a == null) {
            throw new jd("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f600b == null) {
            throw new jd("Required field 'appId' was not present! Struct: " + toString());
        } else if (this.f601c == null) {
            throw new jd("Required field 'cmdName' was not present! Struct: " + toString());
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
            boolean r5 = r4.e()
            if (r5 == 0) goto L_0x0018
            r4.a()
            return
        L_0x0018:
            com.xiaomi.push.jd r5 = new com.xiaomi.push.jd
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Required field 'errorCode' was not found in serialized data! Struct: "
            r0.append(r1)
            java.lang.String r1 = r4.toString()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r5.<init>(r0)
            throw r5
        L_0x0033:
            short r1 = r0.f784a
            r2 = 1
            r3 = 11
            switch(r1) {
                case 2: goto L_0x00cd;
                case 3: goto L_0x00c2;
                case 4: goto L_0x00b7;
                case 5: goto L_0x00ac;
                case 6: goto L_0x003b;
                case 7: goto L_0x009c;
                case 8: goto L_0x0091;
                case 9: goto L_0x0086;
                case 10: goto L_0x005e;
                case 11: goto L_0x003b;
                case 12: goto L_0x0052;
                case 13: goto L_0x0042;
                default: goto L_0x003b;
            }
        L_0x003b:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r5, r0)
            goto L_0x00df
        L_0x0042:
            byte r1 = r0.a
            r3 = 2
            if (r1 != r3) goto L_0x003b
            boolean r0 = r5.a()
            r4.f599a = r0
            r4.b((boolean) r2)
            goto L_0x00df
        L_0x0052:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x003b
            java.lang.String r0 = r5.a()
            r4.f604f = r0
            goto L_0x00df
        L_0x005e:
            byte r1 = r0.a
            r2 = 15
            if (r1 != r2) goto L_0x003b
            com.xiaomi.push.ja r0 = r5.a()
            java.util.ArrayList r1 = new java.util.ArrayList
            int r2 = r0.f787a
            r1.<init>(r2)
            r4.f598a = r1
            r1 = 0
        L_0x0072:
            int r2 = r0.f787a
            if (r1 >= r2) goto L_0x0082
            java.lang.String r2 = r5.a()
            java.util.List<java.lang.String> r3 = r4.f598a
            r3.add(r2)
            int r1 = r1 + 1
            goto L_0x0072
        L_0x0082:
            r5.i()
            goto L_0x00df
        L_0x0086:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x003b
            java.lang.String r0 = r5.a()
            r4.f603e = r0
            goto L_0x00df
        L_0x0091:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x003b
            java.lang.String r0 = r5.a()
            r4.f602d = r0
            goto L_0x00df
        L_0x009c:
            byte r1 = r0.a
            r3 = 10
            if (r1 != r3) goto L_0x003b
            long r0 = r5.a()
            r4.f594a = r0
            r4.a((boolean) r2)
            goto L_0x00df
        L_0x00ac:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x003b
            java.lang.String r0 = r5.a()
            r4.f601c = r0
            goto L_0x00df
        L_0x00b7:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x003b
            java.lang.String r0 = r5.a()
            r4.f600b = r0
            goto L_0x00df
        L_0x00c2:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x003b
            java.lang.String r0 = r5.a()
            r4.f596a = r0
            goto L_0x00df
        L_0x00cd:
            byte r1 = r0.a
            r2 = 12
            if (r1 != r2) goto L_0x003b
            com.xiaomi.push.hv r0 = new com.xiaomi.push.hv
            r0.<init>()
            r4.f595a = r0
            com.xiaomi.push.hv r0 = r4.f595a
            r0.a((com.xiaomi.push.jc) r5)
        L_0x00df:
            r5.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.ib.a(com.xiaomi.push.jc):void");
    }

    public void a(boolean z) {
        this.f597a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m429a() {
        return this.f595a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m430a(ib ibVar) {
        if (ibVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = ibVar.a();
        if ((a2 || a3) && (!a2 || !a3 || !this.f595a.compareTo(ibVar.f595a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = ibVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f596a.equals(ibVar.f596a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = ibVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f600b.equals(ibVar.f600b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = ibVar.d();
        if (((d2 || d3) && (!d2 || !d3 || !this.f601c.equals(ibVar.f601c))) || this.f594a != ibVar.f594a) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = ibVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f602d.equals(ibVar.f602d))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = ibVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f603e.equals(ibVar.f603e))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = ibVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f598a.equals(ibVar.f598a))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = ibVar.i();
        if ((i2 || i3) && (!i2 || !i3 || !this.f604f.equals(ibVar.f604f))) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = ibVar.j();
        if (j2 || j3) {
            return j2 && j3 && this.f599a == ibVar.f599a;
        }
        return true;
    }

    public String b() {
        return this.f604f;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f593a);
        if (this.f595a != null && a()) {
            jcVar.a(a);
            this.f595a.b(jcVar);
            jcVar.b();
        }
        if (this.f596a != null) {
            jcVar.a(b);
            jcVar.a(this.f596a);
            jcVar.b();
        }
        if (this.f600b != null) {
            jcVar.a(c);
            jcVar.a(this.f600b);
            jcVar.b();
        }
        if (this.f601c != null) {
            jcVar.a(d);
            jcVar.a(this.f601c);
            jcVar.b();
        }
        jcVar.a(e);
        jcVar.a(this.f594a);
        jcVar.b();
        if (this.f602d != null && f()) {
            jcVar.a(f);
            jcVar.a(this.f602d);
            jcVar.b();
        }
        if (this.f603e != null && g()) {
            jcVar.a(g);
            jcVar.a(this.f603e);
            jcVar.b();
        }
        if (this.f598a != null && h()) {
            jcVar.a(h);
            jcVar.a(new ja((byte) 11, this.f598a.size()));
            for (String a2 : this.f598a) {
                jcVar.a(a2);
            }
            jcVar.e();
            jcVar.b();
        }
        if (this.f604f != null && i()) {
            jcVar.a(i);
            jcVar.a(this.f604f);
            jcVar.b();
        }
        if (j()) {
            jcVar.a(j);
            jcVar.a(this.f599a);
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public void b(boolean z) {
        this.f597a.set(1, z);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m431b() {
        return this.f596a != null;
    }

    public boolean c() {
        return this.f600b != null;
    }

    public boolean d() {
        return this.f601c != null;
    }

    public boolean e() {
        return this.f597a.get(0);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ib)) {
            return compareTo((ib) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f602d != null;
    }

    public boolean g() {
        return this.f603e != null;
    }

    public boolean h() {
        return this.f598a != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f604f != null;
    }

    public boolean j() {
        return this.f597a.get(1);
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionCommandResult(");
        if (a()) {
            sb.append("target:");
            if (this.f595a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f595a);
            }
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
        }
        sb.append("id:");
        sb.append(this.f596a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f596a);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("appId:");
        sb.append(this.f600b == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f600b);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("cmdName:");
        sb.append(this.f601c == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f601c);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("errorCode:");
        sb.append(this.f594a);
        if (f()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("reason:");
            sb.append(this.f602d == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f602d);
        }
        if (g()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("packageName:");
            sb.append(this.f603e == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f603e);
        }
        if (h()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("cmdArgs:");
            if (this.f598a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f598a);
            }
        }
        if (i()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("category:");
            sb.append(this.f604f == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f604f);
        }
        if (j()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("response2Client:");
            sb.append(this.f599a);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
