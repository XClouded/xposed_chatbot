package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.BitSet;
import java.util.Map;

public class ij implements ir<ij, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 11, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f695a = new jh("XmPushActionSendMessage");
    private static final iz b = new iz("", (byte) 12, 2);
    private static final iz c = new iz("", (byte) 11, 3);
    private static final iz d = new iz("", (byte) 11, 4);
    private static final iz e = new iz("", (byte) 11, 5);
    private static final iz f = new iz("", (byte) 11, 6);
    private static final iz g = new iz("", (byte) 11, 7);
    private static final iz h = new iz("", (byte) 12, 8);
    private static final iz i = new iz("", (byte) 2, 9);
    private static final iz j = new iz("", (byte) 13, 10);
    private static final iz k = new iz("", (byte) 11, 11);
    private static final iz l = new iz("", (byte) 11, 12);

    /* renamed from: a  reason: collision with other field name */
    public hs f696a;

    /* renamed from: a  reason: collision with other field name */
    public hv f697a;

    /* renamed from: a  reason: collision with other field name */
    public String f698a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f699a = new BitSet(1);

    /* renamed from: a  reason: collision with other field name */
    public Map<String, String> f700a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f701a = true;

    /* renamed from: b  reason: collision with other field name */
    public String f702b;

    /* renamed from: c  reason: collision with other field name */
    public String f703c;

    /* renamed from: d  reason: collision with other field name */
    public String f704d;

    /* renamed from: e  reason: collision with other field name */
    public String f705e;

    /* renamed from: f  reason: collision with other field name */
    public String f706f;

    /* renamed from: g  reason: collision with other field name */
    public String f707g;

    /* renamed from: h  reason: collision with other field name */
    public String f708h;

    /* renamed from: a */
    public int compareTo(ij ijVar) {
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
        if (!getClass().equals(ijVar.getClass())) {
            return getClass().getName().compareTo(ijVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(ijVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a13 = is.a(this.f698a, ijVar.f698a)) != 0) {
            return a13;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(ijVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a12 = is.a((Comparable) this.f697a, (Comparable) ijVar.f697a)) != 0) {
            return a12;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(ijVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a11 = is.a(this.f702b, ijVar.f702b)) != 0) {
            return a11;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(ijVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a10 = is.a(this.f703c, ijVar.f703c)) != 0) {
            return a10;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(ijVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a9 = is.a(this.f704d, ijVar.f704d)) != 0) {
            return a9;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(ijVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a8 = is.a(this.f705e, ijVar.f705e)) != 0) {
            return a8;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(ijVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a7 = is.a(this.f706f, ijVar.f706f)) != 0) {
            return a7;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(ijVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a6 = is.a((Comparable) this.f696a, (Comparable) ijVar.f696a)) != 0) {
            return a6;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(ijVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a5 = is.a(this.f701a, ijVar.f701a)) != 0) {
            return a5;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(ijVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a4 = is.a((Map) this.f700a, (Map) ijVar.f700a)) != 0) {
            return a4;
        }
        int compareTo11 = Boolean.valueOf(k()).compareTo(Boolean.valueOf(ijVar.k()));
        if (compareTo11 != 0) {
            return compareTo11;
        }
        if (k() && (a3 = is.a(this.f707g, ijVar.f707g)) != 0) {
            return a3;
        }
        int compareTo12 = Boolean.valueOf(l()).compareTo(Boolean.valueOf(ijVar.l()));
        if (compareTo12 != 0) {
            return compareTo12;
        }
        if (!l() || (a2 = is.a(this.f708h, ijVar.f708h)) == 0) {
            return 0;
        }
        return a2;
    }

    public hs a() {
        return this.f696a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m467a() {
        return this.f702b;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m468a() {
        if (this.f702b == null) {
            throw new jd("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f703c == null) {
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
            if (r1 != 0) goto L_0x0012
            r6.f()
            r5.a()
            return
        L_0x0012:
            short r1 = r0.f784a
            r2 = 2
            r3 = 12
            r4 = 11
            switch(r1) {
                case 1: goto L_0x00d2;
                case 2: goto L_0x00c1;
                case 3: goto L_0x00b6;
                case 4: goto L_0x00ab;
                case 5: goto L_0x00a0;
                case 6: goto L_0x0095;
                case 7: goto L_0x008a;
                case 8: goto L_0x0079;
                case 9: goto L_0x006a;
                case 10: goto L_0x003b;
                case 11: goto L_0x002f;
                case 12: goto L_0x0023;
                default: goto L_0x001c;
            }
        L_0x001c:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r6, r0)
            goto L_0x00dc
        L_0x0023:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x001c
            java.lang.String r0 = r6.a()
            r5.f708h = r0
            goto L_0x00dc
        L_0x002f:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x001c
            java.lang.String r0 = r6.a()
            r5.f707g = r0
            goto L_0x00dc
        L_0x003b:
            byte r1 = r0.a
            r3 = 13
            if (r1 != r3) goto L_0x001c
            com.xiaomi.push.jb r0 = r6.a()
            java.util.HashMap r1 = new java.util.HashMap
            int r3 = r0.f788a
            int r3 = r3 * 2
            r1.<init>(r3)
            r5.f700a = r1
            r1 = 0
        L_0x0051:
            int r2 = r0.f788a
            if (r1 >= r2) goto L_0x0065
            java.lang.String r2 = r6.a()
            java.lang.String r3 = r6.a()
            java.util.Map<java.lang.String, java.lang.String> r4 = r5.f700a
            r4.put(r2, r3)
            int r1 = r1 + 1
            goto L_0x0051
        L_0x0065:
            r6.h()
            goto L_0x00dc
        L_0x006a:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x001c
            boolean r0 = r6.a()
            r5.f701a = r0
            r0 = 1
            r5.a((boolean) r0)
            goto L_0x00dc
        L_0x0079:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x001c
            com.xiaomi.push.hs r0 = new com.xiaomi.push.hs
            r0.<init>()
            r5.f696a = r0
            com.xiaomi.push.hs r0 = r5.f696a
            r0.a((com.xiaomi.push.jc) r6)
            goto L_0x00dc
        L_0x008a:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x001c
            java.lang.String r0 = r6.a()
            r5.f706f = r0
            goto L_0x00dc
        L_0x0095:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x001c
            java.lang.String r0 = r6.a()
            r5.f705e = r0
            goto L_0x00dc
        L_0x00a0:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x001c
            java.lang.String r0 = r6.a()
            r5.f704d = r0
            goto L_0x00dc
        L_0x00ab:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x001c
            java.lang.String r0 = r6.a()
            r5.f703c = r0
            goto L_0x00dc
        L_0x00b6:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x001c
            java.lang.String r0 = r6.a()
            r5.f702b = r0
            goto L_0x00dc
        L_0x00c1:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x001c
            com.xiaomi.push.hv r0 = new com.xiaomi.push.hv
            r0.<init>()
            r5.f697a = r0
            com.xiaomi.push.hv r0 = r5.f697a
            r0.a((com.xiaomi.push.jc) r6)
            goto L_0x00dc
        L_0x00d2:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x001c
            java.lang.String r0 = r6.a()
            r5.f698a = r0
        L_0x00dc:
            r6.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.ij.a(com.xiaomi.push.jc):void");
    }

    public void a(boolean z) {
        this.f699a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m469a() {
        return this.f698a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m470a(ij ijVar) {
        if (ijVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = ijVar.a();
        if ((a2 || a3) && (!a2 || !a3 || !this.f698a.equals(ijVar.f698a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = ijVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f697a.compareTo(ijVar.f697a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = ijVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f702b.equals(ijVar.f702b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = ijVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f703c.equals(ijVar.f703c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = ijVar.e();
        if ((e2 || e3) && (!e2 || !e3 || !this.f704d.equals(ijVar.f704d))) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = ijVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f705e.equals(ijVar.f705e))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = ijVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f706f.equals(ijVar.f706f))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = ijVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f696a.compareTo(ijVar.f696a))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = ijVar.i();
        if ((i2 || i3) && (!i2 || !i3 || this.f701a != ijVar.f701a)) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = ijVar.j();
        if ((j2 || j3) && (!j2 || !j3 || !this.f700a.equals(ijVar.f700a))) {
            return false;
        }
        boolean k2 = k();
        boolean k3 = ijVar.k();
        if ((k2 || k3) && (!k2 || !k3 || !this.f707g.equals(ijVar.f707g))) {
            return false;
        }
        boolean l2 = l();
        boolean l3 = ijVar.l();
        if (l2 || l3) {
            return l2 && l3 && this.f708h.equals(ijVar.f708h);
        }
        return true;
    }

    public String b() {
        return this.f703c;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f695a);
        if (this.f698a != null && a()) {
            jcVar.a(a);
            jcVar.a(this.f698a);
            jcVar.b();
        }
        if (this.f697a != null && b()) {
            jcVar.a(b);
            this.f697a.b(jcVar);
            jcVar.b();
        }
        if (this.f702b != null) {
            jcVar.a(c);
            jcVar.a(this.f702b);
            jcVar.b();
        }
        if (this.f703c != null) {
            jcVar.a(d);
            jcVar.a(this.f703c);
            jcVar.b();
        }
        if (this.f704d != null && e()) {
            jcVar.a(e);
            jcVar.a(this.f704d);
            jcVar.b();
        }
        if (this.f705e != null && f()) {
            jcVar.a(f);
            jcVar.a(this.f705e);
            jcVar.b();
        }
        if (this.f706f != null && g()) {
            jcVar.a(g);
            jcVar.a(this.f706f);
            jcVar.b();
        }
        if (this.f696a != null && h()) {
            jcVar.a(h);
            this.f696a.b(jcVar);
            jcVar.b();
        }
        if (i()) {
            jcVar.a(i);
            jcVar.a(this.f701a);
            jcVar.b();
        }
        if (this.f700a != null && j()) {
            jcVar.a(j);
            jcVar.a(new jb((byte) 11, (byte) 11, this.f700a.size()));
            for (Map.Entry next : this.f700a.entrySet()) {
                jcVar.a((String) next.getKey());
                jcVar.a((String) next.getValue());
            }
            jcVar.d();
            jcVar.b();
        }
        if (this.f707g != null && k()) {
            jcVar.a(k);
            jcVar.a(this.f707g);
            jcVar.b();
        }
        if (this.f708h != null && l()) {
            jcVar.a(l);
            jcVar.a(this.f708h);
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m471b() {
        return this.f697a != null;
    }

    public String c() {
        return this.f705e;
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m472c() {
        return this.f702b != null;
    }

    public String d() {
        return this.f706f;
    }

    /* renamed from: d  reason: collision with other method in class */
    public boolean m473d() {
        return this.f703c != null;
    }

    public String e() {
        return this.f707g;
    }

    /* renamed from: e  reason: collision with other method in class */
    public boolean m474e() {
        return this.f704d != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ij)) {
            return compareTo((ij) obj);
        }
        return false;
    }

    public String f() {
        return this.f708h;
    }

    /* renamed from: f  reason: collision with other method in class */
    public boolean m475f() {
        return this.f705e != null;
    }

    public boolean g() {
        return this.f706f != null;
    }

    public boolean h() {
        return this.f696a != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f699a.get(0);
    }

    public boolean j() {
        return this.f700a != null;
    }

    public boolean k() {
        return this.f707g != null;
    }

    public boolean l() {
        return this.f708h != null;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionSendMessage(");
        if (a()) {
            sb.append("debug:");
            sb.append(this.f698a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f698a);
            z = false;
        } else {
            z = true;
        }
        if (b()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("target:");
            if (this.f697a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f697a);
            }
            z = false;
        }
        if (!z) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
        }
        sb.append("id:");
        sb.append(this.f702b == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f702b);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("appId:");
        sb.append(this.f703c == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f703c);
        if (e()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("packageName:");
            sb.append(this.f704d == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f704d);
        }
        if (f()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("topic:");
            sb.append(this.f705e == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f705e);
        }
        if (g()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("aliasName:");
            sb.append(this.f706f == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f706f);
        }
        if (h()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("message:");
            if (this.f696a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f696a);
            }
        }
        if (i()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("needAck:");
            sb.append(this.f701a);
        }
        if (j()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("params:");
            if (this.f700a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f700a);
            }
        }
        if (k()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("category:");
            sb.append(this.f707g == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f707g);
        }
        if (l()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("userAccount:");
            sb.append(this.f708h == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f708h);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
