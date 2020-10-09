package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.BitSet;
import java.util.List;

public class hp implements ir<hp, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 8, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f479a = new jh("NormalConfig");
    private static final iz b = new iz("", (byte) 15, 2);
    private static final iz c = new iz("", (byte) 8, 3);

    /* renamed from: a  reason: collision with other field name */
    public int f480a;

    /* renamed from: a  reason: collision with other field name */
    public hm f481a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f482a = new BitSet(1);

    /* renamed from: a  reason: collision with other field name */
    public List<hr> f483a;

    public int a() {
        return this.f480a;
    }

    /* renamed from: a */
    public int compareTo(hp hpVar) {
        int a2;
        int a3;
        int a4;
        if (!getClass().equals(hpVar.getClass())) {
            return getClass().getName().compareTo(hpVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(hpVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a4 = is.a(this.f480a, hpVar.f480a)) != 0) {
            return a4;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(hpVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a3 = is.a((List) this.f483a, (List) hpVar.f483a)) != 0) {
            return a3;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(hpVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (!c() || (a2 = is.a((Comparable) this.f481a, (Comparable) hpVar.f481a)) == 0) {
            return 0;
        }
        return a2;
    }

    /* renamed from: a  reason: collision with other method in class */
    public hm m378a() {
        return this.f481a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m379a() {
        if (this.f483a == null) {
            throw new jd("Required field 'configItems' was not present! Struct: " + toString());
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
            java.lang.String r1 = "Required field 'version' was not found in serialized data! Struct: "
            r0.append(r1)
            java.lang.String r1 = r4.toString()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r5.<init>(r0)
            throw r5
        L_0x0033:
            short r1 = r0.f784a
            r2 = 8
            switch(r1) {
                case 1: goto L_0x007b;
                case 2: goto L_0x004f;
                case 3: goto L_0x0040;
                default: goto L_0x003a;
            }
        L_0x003a:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r5, r0)
            goto L_0x0089
        L_0x0040:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x003a
            int r0 = r5.a()
            com.xiaomi.push.hm r0 = com.xiaomi.push.hm.a(r0)
            r4.f481a = r0
            goto L_0x0089
        L_0x004f:
            byte r1 = r0.a
            r2 = 15
            if (r1 != r2) goto L_0x003a
            com.xiaomi.push.ja r0 = r5.a()
            java.util.ArrayList r1 = new java.util.ArrayList
            int r2 = r0.f787a
            r1.<init>(r2)
            r4.f483a = r1
            r1 = 0
        L_0x0063:
            int r2 = r0.f787a
            if (r1 >= r2) goto L_0x0077
            com.xiaomi.push.hr r2 = new com.xiaomi.push.hr
            r2.<init>()
            r2.a((com.xiaomi.push.jc) r5)
            java.util.List<com.xiaomi.push.hr> r3 = r4.f483a
            r3.add(r2)
            int r1 = r1 + 1
            goto L_0x0063
        L_0x0077:
            r5.i()
            goto L_0x0089
        L_0x007b:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x003a
            int r0 = r5.a()
            r4.f480a = r0
            r0 = 1
            r4.a((boolean) r0)
        L_0x0089:
            r5.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.hp.a(com.xiaomi.push.jc):void");
    }

    public void a(boolean z) {
        this.f482a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m380a() {
        return this.f482a.get(0);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m381a(hp hpVar) {
        if (hpVar == null || this.f480a != hpVar.f480a) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = hpVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f483a.equals(hpVar.f483a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = hpVar.c();
        if (c2 || c3) {
            return c2 && c3 && this.f481a.equals(hpVar.f481a);
        }
        return true;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f479a);
        jcVar.a(a);
        jcVar.a(this.f480a);
        jcVar.b();
        if (this.f483a != null) {
            jcVar.a(b);
            jcVar.a(new ja((byte) 12, this.f483a.size()));
            for (hr b2 : this.f483a) {
                b2.b(jcVar);
            }
            jcVar.e();
            jcVar.b();
        }
        if (this.f481a != null && c()) {
            jcVar.a(c);
            jcVar.a(this.f481a.a());
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public boolean b() {
        return this.f483a != null;
    }

    public boolean c() {
        return this.f481a != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof hp)) {
            return compareTo((hp) obj);
        }
        return false;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("NormalConfig(");
        sb.append("version:");
        sb.append(this.f480a);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("configItems:");
        if (this.f483a == null) {
            sb.append(BuildConfig.buildJavascriptFrameworkVersion);
        } else {
            sb.append(this.f483a);
        }
        if (c()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("type:");
            if (this.f481a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f481a);
            }
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
