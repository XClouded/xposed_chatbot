package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.BitSet;

public class ic implements ir<ic, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 8, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f605a = new jh("XmPushActionContainer");
    private static final iz b = new iz("", (byte) 2, 2);
    private static final iz c = new iz("", (byte) 2, 3);
    private static final iz d = new iz("", (byte) 11, 4);
    private static final iz e = new iz("", (byte) 11, 5);
    private static final iz f = new iz("", (byte) 11, 6);
    private static final iz g = new iz("", (byte) 12, 7);
    private static final iz h = new iz("", (byte) 12, 8);

    /* renamed from: a  reason: collision with other field name */
    public hg f606a;

    /* renamed from: a  reason: collision with other field name */
    public ht f607a;

    /* renamed from: a  reason: collision with other field name */
    public hv f608a;

    /* renamed from: a  reason: collision with other field name */
    public String f609a;

    /* renamed from: a  reason: collision with other field name */
    public ByteBuffer f610a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f611a = new BitSet(2);

    /* renamed from: a  reason: collision with other field name */
    public boolean f612a = true;

    /* renamed from: b  reason: collision with other field name */
    public String f613b;

    /* renamed from: b  reason: collision with other field name */
    public boolean f614b = true;

    /* renamed from: a */
    public int compareTo(ic icVar) {
        int a2;
        int a3;
        int a4;
        int a5;
        int a6;
        int a7;
        int a8;
        int a9;
        if (!getClass().equals(icVar.getClass())) {
            return getClass().getName().compareTo(icVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(icVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a9 = is.a((Comparable) this.f606a, (Comparable) icVar.f606a)) != 0) {
            return a9;
        }
        int compareTo2 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(icVar.c()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (c() && (a8 = is.a(this.f612a, icVar.f612a)) != 0) {
            return a8;
        }
        int compareTo3 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(icVar.d()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (d() && (a7 = is.a(this.f614b, icVar.f614b)) != 0) {
            return a7;
        }
        int compareTo4 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(icVar.e()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (e() && (a6 = is.a((Comparable) this.f610a, (Comparable) icVar.f610a)) != 0) {
            return a6;
        }
        int compareTo5 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(icVar.f()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (f() && (a5 = is.a(this.f609a, icVar.f609a)) != 0) {
            return a5;
        }
        int compareTo6 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(icVar.g()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (g() && (a4 = is.a(this.f613b, icVar.f613b)) != 0) {
            return a4;
        }
        int compareTo7 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(icVar.h()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (h() && (a3 = is.a((Comparable) this.f608a, (Comparable) icVar.f608a)) != 0) {
            return a3;
        }
        int compareTo8 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(icVar.i()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (!i() || (a2 = is.a((Comparable) this.f607a, (Comparable) icVar.f607a)) == 0) {
            return 0;
        }
        return a2;
    }

    public hg a() {
        return this.f606a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public ht m432a() {
        return this.f607a;
    }

    public ic a(hg hgVar) {
        this.f606a = hgVar;
        return this;
    }

    public ic a(ht htVar) {
        this.f607a = htVar;
        return this;
    }

    public ic a(hv hvVar) {
        this.f608a = hvVar;
        return this;
    }

    public ic a(String str) {
        this.f609a = str;
        return this;
    }

    public ic a(ByteBuffer byteBuffer) {
        this.f610a = byteBuffer;
        return this;
    }

    public ic a(boolean z) {
        this.f612a = z;
        a(true);
        return this;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m433a() {
        return this.f609a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m434a() {
        if (this.f606a == null) {
            throw new jd("Required field 'action' was not present! Struct: " + toString());
        } else if (this.f610a == null) {
            throw new jd("Required field 'pushAction' was not present! Struct: " + toString());
        } else if (this.f608a == null) {
            throw new jd("Required field 'target' was not present! Struct: " + toString());
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(com.xiaomi.push.jc r7) {
        /*
            r6 = this;
            r7.a()
        L_0x0003:
            com.xiaomi.push.iz r0 = r7.a()
            byte r1 = r0.a
            if (r1 != 0) goto L_0x0054
            r7.f()
            boolean r7 = r6.c()
            if (r7 == 0) goto L_0x0039
            boolean r7 = r6.d()
            if (r7 == 0) goto L_0x001e
            r6.a()
            return
        L_0x001e:
            com.xiaomi.push.jd r7 = new com.xiaomi.push.jd
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Required field 'isRequest' was not found in serialized data! Struct: "
            r0.append(r1)
            java.lang.String r1 = r6.toString()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r7.<init>(r0)
            throw r7
        L_0x0039:
            com.xiaomi.push.jd r7 = new com.xiaomi.push.jd
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Required field 'encryptAction' was not found in serialized data! Struct: "
            r0.append(r1)
            java.lang.String r1 = r6.toString()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r7.<init>(r0)
            throw r7
        L_0x0054:
            short r1 = r0.f784a
            r2 = 1
            r3 = 12
            r4 = 2
            r5 = 11
            switch(r1) {
                case 1: goto L_0x00c5;
                case 2: goto L_0x00b7;
                case 3: goto L_0x00a9;
                case 4: goto L_0x009e;
                case 5: goto L_0x0093;
                case 6: goto L_0x0088;
                case 7: goto L_0x0077;
                case 8: goto L_0x0066;
                default: goto L_0x005f;
            }
        L_0x005f:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r7, r0)
            goto L_0x00d5
        L_0x0066:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x005f
            com.xiaomi.push.ht r0 = new com.xiaomi.push.ht
            r0.<init>()
            r6.f607a = r0
            com.xiaomi.push.ht r0 = r6.f607a
            r0.a((com.xiaomi.push.jc) r7)
            goto L_0x00d5
        L_0x0077:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x005f
            com.xiaomi.push.hv r0 = new com.xiaomi.push.hv
            r0.<init>()
            r6.f608a = r0
            com.xiaomi.push.hv r0 = r6.f608a
            r0.a((com.xiaomi.push.jc) r7)
            goto L_0x00d5
        L_0x0088:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x005f
            java.lang.String r0 = r7.a()
            r6.f613b = r0
            goto L_0x00d5
        L_0x0093:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x005f
            java.lang.String r0 = r7.a()
            r6.f609a = r0
            goto L_0x00d5
        L_0x009e:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x005f
            java.nio.ByteBuffer r0 = r7.a()
            r6.f610a = r0
            goto L_0x00d5
        L_0x00a9:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x005f
            boolean r0 = r7.a()
            r6.f614b = r0
            r6.b((boolean) r2)
            goto L_0x00d5
        L_0x00b7:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x005f
            boolean r0 = r7.a()
            r6.f612a = r0
            r6.a((boolean) r2)
            goto L_0x00d5
        L_0x00c5:
            byte r1 = r0.a
            r2 = 8
            if (r1 != r2) goto L_0x005f
            int r0 = r7.a()
            com.xiaomi.push.hg r0 = com.xiaomi.push.hg.a(r0)
            r6.f606a = r0
        L_0x00d5:
            r7.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.ic.a(com.xiaomi.push.jc):void");
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m435a(boolean z) {
        this.f611a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m436a() {
        return this.f606a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m437a(ic icVar) {
        if (icVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = icVar.a();
        if (((a2 || a3) && (!a2 || !a3 || !this.f606a.equals(icVar.f606a))) || this.f612a != icVar.f612a || this.f614b != icVar.f614b) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = icVar.e();
        if ((e2 || e3) && (!e2 || !e3 || !this.f610a.equals(icVar.f610a))) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = icVar.f();
        if ((f2 || f3) && (!f2 || !f3 || !this.f609a.equals(icVar.f609a))) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = icVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f613b.equals(icVar.f613b))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = icVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f608a.compareTo(icVar.f608a))) {
            return false;
        }
        boolean i = i();
        boolean i2 = icVar.i();
        if (i || i2) {
            return i && i2 && this.f607a.compareTo(icVar.f607a);
        }
        return true;
    }

    /* renamed from: a  reason: collision with other method in class */
    public byte[] m438a() {
        a(is.a(this.f610a));
        return this.f610a.array();
    }

    public ic b(String str) {
        this.f613b = str;
        return this;
    }

    public ic b(boolean z) {
        this.f614b = z;
        b(true);
        return this;
    }

    public String b() {
        return this.f613b;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f605a);
        if (this.f606a != null) {
            jcVar.a(a);
            jcVar.a(this.f606a.a());
            jcVar.b();
        }
        jcVar.a(b);
        jcVar.a(this.f612a);
        jcVar.b();
        jcVar.a(c);
        jcVar.a(this.f614b);
        jcVar.b();
        if (this.f610a != null) {
            jcVar.a(d);
            jcVar.a(this.f610a);
            jcVar.b();
        }
        if (this.f609a != null && f()) {
            jcVar.a(e);
            jcVar.a(this.f609a);
            jcVar.b();
        }
        if (this.f613b != null && g()) {
            jcVar.a(f);
            jcVar.a(this.f613b);
            jcVar.b();
        }
        if (this.f608a != null) {
            jcVar.a(g);
            this.f608a.b(jcVar);
            jcVar.b();
        }
        if (this.f607a != null && i()) {
            jcVar.a(h);
            this.f607a.b(jcVar);
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    /* renamed from: b  reason: collision with other method in class */
    public void m439b(boolean z) {
        this.f611a.set(1, z);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m440b() {
        return this.f612a;
    }

    public boolean c() {
        return this.f611a.get(0);
    }

    public boolean d() {
        return this.f611a.get(1);
    }

    public boolean e() {
        return this.f610a != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ic)) {
            return compareTo((ic) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f609a != null;
    }

    public boolean g() {
        return this.f613b != null;
    }

    public boolean h() {
        return this.f608a != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f607a != null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("XmPushActionContainer(");
        sb.append("action:");
        if (this.f606a == null) {
            sb.append(BuildConfig.buildJavascriptFrameworkVersion);
        } else {
            sb.append(this.f606a);
        }
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("encryptAction:");
        sb.append(this.f612a);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("isRequest:");
        sb.append(this.f614b);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("pushAction:");
        if (this.f610a == null) {
            sb.append(BuildConfig.buildJavascriptFrameworkVersion);
        } else {
            is.a(this.f610a, sb);
        }
        if (f()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("appid:");
            sb.append(this.f609a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f609a);
        }
        if (g()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("packageName:");
            sb.append(this.f613b == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f613b);
        }
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("target:");
        if (this.f608a == null) {
            sb.append(BuildConfig.buildJavascriptFrameworkVersion);
        } else {
            sb.append(this.f608a);
        }
        if (i()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("metaInfo:");
            if (this.f607a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f607a);
            }
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
