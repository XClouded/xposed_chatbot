package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.BitSet;
import java.util.Map;

public class hx implements ir<hx, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 11, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f562a = new jh("XmPushActionAckNotification");
    private static final iz b = new iz("", (byte) 12, 2);
    private static final iz c = new iz("", (byte) 11, 3);
    private static final iz d = new iz("", (byte) 11, 4);
    private static final iz e = new iz("", (byte) 11, 5);
    private static final iz f = new iz("", (byte) 10, 7);
    private static final iz g = new iz("", (byte) 11, 8);
    private static final iz h = new iz("", (byte) 13, 9);
    private static final iz i = new iz("", (byte) 11, 10);
    private static final iz j = new iz("", (byte) 11, 11);

    /* renamed from: a  reason: collision with other field name */
    public long f563a = 0;

    /* renamed from: a  reason: collision with other field name */
    public hv f564a;

    /* renamed from: a  reason: collision with other field name */
    public String f565a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f566a = new BitSet(1);

    /* renamed from: a  reason: collision with other field name */
    public Map<String, String> f567a;

    /* renamed from: b  reason: collision with other field name */
    public String f568b;

    /* renamed from: c  reason: collision with other field name */
    public String f569c;

    /* renamed from: d  reason: collision with other field name */
    public String f570d;

    /* renamed from: e  reason: collision with other field name */
    public String f571e;

    /* renamed from: f  reason: collision with other field name */
    public String f572f;

    /* renamed from: g  reason: collision with other field name */
    public String f573g;

    /* renamed from: a */
    public int compareTo(hx hxVar) {
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
        if (!getClass().equals(hxVar.getClass())) {
            return getClass().getName().compareTo(hxVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(hxVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a11 = is.a(this.f565a, hxVar.f565a)) != 0) {
            return a11;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(hxVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a10 = is.a((Comparable) this.f564a, (Comparable) hxVar.f564a)) != 0) {
            return a10;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(hxVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a9 = is.a(this.f568b, hxVar.f568b)) != 0) {
            return a9;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(hxVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a8 = is.a(this.f569c, hxVar.f569c)) != 0) {
            return a8;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(hxVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a7 = is.a(this.f570d, hxVar.f570d)) != 0) {
            return a7;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(hxVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a6 = is.a(this.f563a, hxVar.f563a)) != 0) {
            return a6;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(hxVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a5 = is.a(this.f571e, hxVar.f571e)) != 0) {
            return a5;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(hxVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a4 = is.a((Map) this.f567a, (Map) hxVar.f567a)) != 0) {
            return a4;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(hxVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a3 = is.a(this.f572f, hxVar.f572f)) != 0) {
            return a3;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(hxVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (!j() || (a2 = is.a(this.f573g, hxVar.f573g)) == 0) {
            return 0;
        }
        return a2;
    }

    public String a() {
        return this.f568b;
    }

    /* renamed from: a  reason: collision with other method in class */
    public Map<String, String> m412a() {
        return this.f567a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m413a() {
        if (this.f568b == null) {
            throw new jd("Required field 'id' was not present! Struct: " + toString());
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
            r2 = 11
            switch(r1) {
                case 1: goto L_0x00b6;
                case 2: goto L_0x00a3;
                case 3: goto L_0x0098;
                case 4: goto L_0x008d;
                case 5: goto L_0x0082;
                case 6: goto L_0x0019;
                case 7: goto L_0x0071;
                case 8: goto L_0x0066;
                case 9: goto L_0x0038;
                case 10: goto L_0x002c;
                case 11: goto L_0x0020;
                default: goto L_0x0019;
            }
        L_0x0019:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r6, r0)
            goto L_0x00c0
        L_0x0020:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r6.a()
            r5.f573g = r0
            goto L_0x00c0
        L_0x002c:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r6.a()
            r5.f572f = r0
            goto L_0x00c0
        L_0x0038:
            byte r1 = r0.a
            r2 = 13
            if (r1 != r2) goto L_0x0019
            com.xiaomi.push.jb r0 = r6.a()
            java.util.HashMap r1 = new java.util.HashMap
            int r2 = r0.f788a
            int r2 = r2 * 2
            r1.<init>(r2)
            r5.f567a = r1
            r1 = 0
        L_0x004e:
            int r2 = r0.f788a
            if (r1 >= r2) goto L_0x0062
            java.lang.String r2 = r6.a()
            java.lang.String r3 = r6.a()
            java.util.Map<java.lang.String, java.lang.String> r4 = r5.f567a
            r4.put(r2, r3)
            int r1 = r1 + 1
            goto L_0x004e
        L_0x0062:
            r6.h()
            goto L_0x00c0
        L_0x0066:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r6.a()
            r5.f571e = r0
            goto L_0x00c0
        L_0x0071:
            byte r1 = r0.a
            r2 = 10
            if (r1 != r2) goto L_0x0019
            long r0 = r6.a()
            r5.f563a = r0
            r0 = 1
            r5.a((boolean) r0)
            goto L_0x00c0
        L_0x0082:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r6.a()
            r5.f570d = r0
            goto L_0x00c0
        L_0x008d:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r6.a()
            r5.f569c = r0
            goto L_0x00c0
        L_0x0098:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r6.a()
            r5.f568b = r0
            goto L_0x00c0
        L_0x00a3:
            byte r1 = r0.a
            r2 = 12
            if (r1 != r2) goto L_0x0019
            com.xiaomi.push.hv r0 = new com.xiaomi.push.hv
            r0.<init>()
            r5.f564a = r0
            com.xiaomi.push.hv r0 = r5.f564a
            r0.a((com.xiaomi.push.jc) r6)
            goto L_0x00c0
        L_0x00b6:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r6.a()
            r5.f565a = r0
        L_0x00c0:
            r6.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.hx.a(com.xiaomi.push.jc):void");
    }

    public void a(boolean z) {
        this.f566a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m414a() {
        return this.f565a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m415a(hx hxVar) {
        if (hxVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = hxVar.a();
        if ((a2 || a3) && (!a2 || !a3 || !this.f565a.equals(hxVar.f565a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = hxVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f564a.compareTo(hxVar.f564a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = hxVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f568b.equals(hxVar.f568b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = hxVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f569c.equals(hxVar.f569c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = hxVar.e();
        if ((e2 || e3) && (!e2 || !e3 || !this.f570d.equals(hxVar.f570d))) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = hxVar.f();
        if ((f2 || f3) && (!f2 || !f3 || this.f563a != hxVar.f563a)) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = hxVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f571e.equals(hxVar.f571e))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = hxVar.h();
        if ((h2 || h3) && (!h2 || !h3 || !this.f567a.equals(hxVar.f567a))) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = hxVar.i();
        if ((i2 || i3) && (!i2 || !i3 || !this.f572f.equals(hxVar.f572f))) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = hxVar.j();
        if (j2 || j3) {
            return j2 && j3 && this.f573g.equals(hxVar.f573g);
        }
        return true;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f562a);
        if (this.f565a != null && a()) {
            jcVar.a(a);
            jcVar.a(this.f565a);
            jcVar.b();
        }
        if (this.f564a != null && b()) {
            jcVar.a(b);
            this.f564a.b(jcVar);
            jcVar.b();
        }
        if (this.f568b != null) {
            jcVar.a(c);
            jcVar.a(this.f568b);
            jcVar.b();
        }
        if (this.f569c != null && d()) {
            jcVar.a(d);
            jcVar.a(this.f569c);
            jcVar.b();
        }
        if (this.f570d != null && e()) {
            jcVar.a(e);
            jcVar.a(this.f570d);
            jcVar.b();
        }
        if (f()) {
            jcVar.a(f);
            jcVar.a(this.f563a);
            jcVar.b();
        }
        if (this.f571e != null && g()) {
            jcVar.a(g);
            jcVar.a(this.f571e);
            jcVar.b();
        }
        if (this.f567a != null && h()) {
            jcVar.a(h);
            jcVar.a(new jb((byte) 11, (byte) 11, this.f567a.size()));
            for (Map.Entry next : this.f567a.entrySet()) {
                jcVar.a((String) next.getKey());
                jcVar.a((String) next.getValue());
            }
            jcVar.d();
            jcVar.b();
        }
        if (this.f572f != null && i()) {
            jcVar.a(i);
            jcVar.a(this.f572f);
            jcVar.b();
        }
        if (this.f573g != null && j()) {
            jcVar.a(j);
            jcVar.a(this.f573g);
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public boolean b() {
        return this.f564a != null;
    }

    public boolean c() {
        return this.f568b != null;
    }

    public boolean d() {
        return this.f569c != null;
    }

    public boolean e() {
        return this.f570d != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof hx)) {
            return compareTo((hx) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f566a.get(0);
    }

    public boolean g() {
        return this.f571e != null;
    }

    public boolean h() {
        return this.f567a != null;
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f572f != null;
    }

    public boolean j() {
        return this.f573g != null;
    }

    public String toString() {
        boolean z;
        StringBuilder sb = new StringBuilder("XmPushActionAckNotification(");
        if (a()) {
            sb.append("debug:");
            sb.append(this.f565a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f565a);
            z = false;
        } else {
            z = true;
        }
        if (b()) {
            if (!z) {
                sb.append(AVFSCacheConstants.COMMA_SEP);
            }
            sb.append("target:");
            if (this.f564a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f564a);
            }
            z = false;
        }
        if (!z) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
        }
        sb.append("id:");
        sb.append(this.f568b == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f568b);
        if (d()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("appId:");
            sb.append(this.f569c == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f569c);
        }
        if (e()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("type:");
            sb.append(this.f570d == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f570d);
        }
        if (f()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("errorCode:");
            sb.append(this.f563a);
        }
        if (g()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("reason:");
            sb.append(this.f571e == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f571e);
        }
        if (h()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("extra:");
            if (this.f567a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f567a);
            }
        }
        if (i()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("packageName:");
            sb.append(this.f572f == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f572f);
        }
        if (j()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("category:");
            sb.append(this.f573g == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f573g);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
