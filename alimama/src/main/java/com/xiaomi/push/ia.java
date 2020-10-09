package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class ia implements ir<ia, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 12, 2);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f581a = new jh("XmPushActionCommand");
    private static final iz b = new iz("", (byte) 11, 3);
    private static final iz c = new iz("", (byte) 11, 4);
    private static final iz d = new iz("", (byte) 11, 5);
    private static final iz e = new iz("", (byte) 15, 6);
    private static final iz f = new iz("", (byte) 11, 7);
    private static final iz g = new iz("", (byte) 11, 9);
    private static final iz h = new iz("", (byte) 2, 10);
    private static final iz i = new iz("", (byte) 2, 11);
    private static final iz j = new iz("", (byte) 10, 12);

    /* renamed from: a  reason: collision with other field name */
    public long f582a;

    /* renamed from: a  reason: collision with other field name */
    public hv f583a;

    /* renamed from: a  reason: collision with other field name */
    public String f584a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f585a = new BitSet(3);

    /* renamed from: a  reason: collision with other field name */
    public List<String> f586a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f587a = false;

    /* renamed from: b  reason: collision with other field name */
    public String f588b;

    /* renamed from: b  reason: collision with other field name */
    public boolean f589b = true;

    /* renamed from: c  reason: collision with other field name */
    public String f590c;

    /* renamed from: d  reason: collision with other field name */
    public String f591d;

    /* renamed from: e  reason: collision with other field name */
    public String f592e;

    /* renamed from: a */
    public int compareTo(ia iaVar) {
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
        if (!getClass().equals(iaVar.getClass())) {
            return getClass().getName().compareTo(iaVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(iaVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a11 = is.a((Comparable) this.f583a, (Comparable) iaVar.f583a)) != 0) {
            return a11;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(iaVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a10 = is.a(this.f584a, iaVar.f584a)) != 0) {
            return a10;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(iaVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a9 = is.a(this.f588b, iaVar.f588b)) != 0) {
            return a9;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(iaVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a8 = is.a(this.f590c, iaVar.f590c)) != 0) {
            return a8;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(iaVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a7 = is.a((List) this.f586a, (List) iaVar.f586a)) != 0) {
            return a7;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(iaVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a6 = is.a(this.f591d, iaVar.f591d)) != 0) {
            return a6;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(iaVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a5 = is.a(this.f592e, iaVar.f592e)) != 0) {
            return a5;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(iaVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a4 = is.a(this.f587a, iaVar.f587a)) != 0) {
            return a4;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(iaVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a3 = is.a(this.f589b, iaVar.f589b)) != 0) {
            return a3;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(iaVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (!j() || (a2 = is.a(this.f582a, iaVar.f582a)) == 0) {
            return 0;
        }
        return a2;
    }

    public ia a(String str) {
        this.f584a = str;
        return this;
    }

    public ia a(List<String> list) {
        this.f586a = list;
        return this;
    }

    public String a() {
        return this.f590c;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m423a() {
        if (this.f584a == null) {
            throw new jd("Required field 'id' was not present! Struct: " + toString());
        } else if (this.f588b == null) {
            throw new jd("Required field 'appId' was not present! Struct: " + toString());
        } else if (this.f590c == null) {
            throw new jd("Required field 'cmdName' was not present! Struct: " + toString());
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
            r3 = 1
            r4 = 11
            switch(r1) {
                case 2: goto L_0x00b0;
                case 3: goto L_0x00a5;
                case 4: goto L_0x009a;
                case 5: goto L_0x008f;
                case 6: goto L_0x0067;
                case 7: goto L_0x005c;
                case 8: goto L_0x001b;
                case 9: goto L_0x0051;
                case 10: goto L_0x0042;
                case 11: goto L_0x0033;
                case 12: goto L_0x0022;
                default: goto L_0x001b;
            }
        L_0x001b:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r6, r0)
            goto L_0x00c2
        L_0x0022:
            byte r1 = r0.a
            r2 = 10
            if (r1 != r2) goto L_0x001b
            long r0 = r6.a()
            r5.f582a = r0
            r5.c((boolean) r3)
            goto L_0x00c2
        L_0x0033:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x001b
            boolean r0 = r6.a()
            r5.f589b = r0
            r5.b((boolean) r3)
            goto L_0x00c2
        L_0x0042:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x001b
            boolean r0 = r6.a()
            r5.f587a = r0
            r5.a((boolean) r3)
            goto L_0x00c2
        L_0x0051:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x001b
            java.lang.String r0 = r6.a()
            r5.f592e = r0
            goto L_0x00c2
        L_0x005c:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x001b
            java.lang.String r0 = r6.a()
            r5.f591d = r0
            goto L_0x00c2
        L_0x0067:
            byte r1 = r0.a
            r2 = 15
            if (r1 != r2) goto L_0x001b
            com.xiaomi.push.ja r0 = r6.a()
            java.util.ArrayList r1 = new java.util.ArrayList
            int r2 = r0.f787a
            r1.<init>(r2)
            r5.f586a = r1
            r1 = 0
        L_0x007b:
            int r2 = r0.f787a
            if (r1 >= r2) goto L_0x008b
            java.lang.String r2 = r6.a()
            java.util.List<java.lang.String> r3 = r5.f586a
            r3.add(r2)
            int r1 = r1 + 1
            goto L_0x007b
        L_0x008b:
            r6.i()
            goto L_0x00c2
        L_0x008f:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x001b
            java.lang.String r0 = r6.a()
            r5.f590c = r0
            goto L_0x00c2
        L_0x009a:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x001b
            java.lang.String r0 = r6.a()
            r5.f588b = r0
            goto L_0x00c2
        L_0x00a5:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x001b
            java.lang.String r0 = r6.a()
            r5.f584a = r0
            goto L_0x00c2
        L_0x00b0:
            byte r1 = r0.a
            r2 = 12
            if (r1 != r2) goto L_0x001b
            com.xiaomi.push.hv r0 = new com.xiaomi.push.hv
            r0.<init>()
            r5.f583a = r0
            com.xiaomi.push.hv r0 = r5.f583a
            r0.a((com.xiaomi.push.jc) r6)
        L_0x00c2:
            r6.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.ia.a(com.xiaomi.push.jc):void");
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m424a(String str) {
        if (this.f586a == null) {
            this.f586a = new ArrayList();
        }
        this.f586a.add(str);
    }

    public void a(boolean z) {
        this.f585a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m425a() {
        return this.f583a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m426a(ia iaVar) {
        if (iaVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = iaVar.a();
        if ((a2 || a3) && (!a2 || !a3 || !this.f583a.compareTo(iaVar.f583a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = iaVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f584a.equals(iaVar.f584a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = iaVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f588b.equals(iaVar.f588b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = iaVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f590c.equals(iaVar.f590c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = iaVar.e();
        if ((e2 || e3) && (!e2 || !e3 || !this.f586a.equals(iaVar.f586a))) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = iaVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f591d.equals(iaVar.f591d))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = iaVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f592e.equals(iaVar.f592e))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = iaVar.h();
        if ((h2 || h3) && (!h2 || !h3 || this.f587a != iaVar.f587a)) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = iaVar.i();
        if ((i2 || i3) && (!i2 || !i3 || this.f589b != iaVar.f589b)) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = iaVar.j();
        if (j2 || j3) {
            return j2 && j3 && this.f582a == iaVar.f582a;
        }
        return true;
    }

    public ia b(String str) {
        this.f588b = str;
        return this;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f581a);
        if (this.f583a != null && a()) {
            jcVar.a(a);
            this.f583a.b(jcVar);
            jcVar.b();
        }
        if (this.f584a != null) {
            jcVar.a(b);
            jcVar.a(this.f584a);
            jcVar.b();
        }
        if (this.f588b != null) {
            jcVar.a(c);
            jcVar.a(this.f588b);
            jcVar.b();
        }
        if (this.f590c != null) {
            jcVar.a(d);
            jcVar.a(this.f590c);
            jcVar.b();
        }
        if (this.f586a != null && e()) {
            jcVar.a(e);
            jcVar.a(new ja((byte) 11, this.f586a.size()));
            for (String a2 : this.f586a) {
                jcVar.a(a2);
            }
            jcVar.e();
            jcVar.b();
        }
        if (this.f591d != null && f()) {
            jcVar.a(f);
            jcVar.a(this.f591d);
            jcVar.b();
        }
        if (this.f592e != null && g()) {
            jcVar.a(g);
            jcVar.a(this.f592e);
            jcVar.b();
        }
        if (h()) {
            jcVar.a(h);
            jcVar.a(this.f587a);
            jcVar.b();
        }
        if (i()) {
            jcVar.a(i);
            jcVar.a(this.f589b);
            jcVar.b();
        }
        if (j()) {
            jcVar.a(j);
            jcVar.a(this.f582a);
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public void b(boolean z) {
        this.f585a.set(1, z);
    }

    public boolean b() {
        return this.f584a != null;
    }

    public ia c(String str) {
        this.f590c = str;
        return this;
    }

    public void c(boolean z) {
        this.f585a.set(2, z);
    }

    public boolean c() {
        return this.f588b != null;
    }

    public ia d(String str) {
        this.f591d = str;
        return this;
    }

    public boolean d() {
        return this.f590c != null;
    }

    public ia e(String str) {
        this.f592e = str;
        return this;
    }

    public boolean e() {
        return this.f586a != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ia)) {
            return compareTo((ia) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f591d != null;
    }

    public boolean g() {
        return this.f592e != null;
    }

    public boolean h() {
        return this.f585a.get(0);
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f585a.get(1);
    }

    public boolean j() {
        return this.f585a.get(2);
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionCommand(");
        if (a()) {
            sb.append("target:");
            if (this.f583a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f583a);
            }
            z = false;
        } else {
            z = true;
        }
        if (!z) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
        }
        sb.append("id:");
        sb.append(this.f584a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f584a);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("appId:");
        sb.append(this.f588b == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f588b);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("cmdName:");
        sb.append(this.f590c == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f590c);
        if (e()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("cmdArgs:");
            if (this.f586a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f586a);
            }
        }
        if (f()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("packageName:");
            sb.append(this.f591d == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f591d);
        }
        if (g()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("category:");
            sb.append(this.f592e == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f592e);
        }
        if (h()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("updateCache:");
            sb.append(this.f587a);
        }
        if (i()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("response2Client:");
            sb.append(this.f589b);
        }
        if (j()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("createdTs:");
            sb.append(this.f582a);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
