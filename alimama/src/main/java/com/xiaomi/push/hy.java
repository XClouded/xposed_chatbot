package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.BitSet;

public class hy implements ir<hy, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 8, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f574a = new jh("XmPushActionCheckClientInfo");
    private static final iz b = new iz("", (byte) 8, 2);

    /* renamed from: a  reason: collision with other field name */
    public int f575a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f576a = new BitSet(2);

    /* renamed from: b  reason: collision with other field name */
    public int f577b;

    /* renamed from: a */
    public int compareTo(hy hyVar) {
        int a2;
        int a3;
        if (!getClass().equals(hyVar.getClass())) {
            return getClass().getName().compareTo(hyVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(hyVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a3 = is.a(this.f575a, hyVar.f575a)) != 0) {
            return a3;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(hyVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (!b() || (a2 = is.a(this.f577b, hyVar.f577b)) == 0) {
            return 0;
        }
        return a2;
    }

    public hy a(int i) {
        this.f575a = i;
        a(true);
        return this;
    }

    public void a() {
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
            if (r1 != 0) goto L_0x0054
            r5.f()
            boolean r5 = r4.a()
            if (r5 == 0) goto L_0x0039
            boolean r5 = r4.b()
            if (r5 == 0) goto L_0x001e
            r4.a()
            return
        L_0x001e:
            com.xiaomi.push.jd r5 = new com.xiaomi.push.jd
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Required field 'pluginConfigVersion' was not found in serialized data! Struct: "
            r0.append(r1)
            java.lang.String r1 = r4.toString()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r5.<init>(r0)
            throw r5
        L_0x0039:
            com.xiaomi.push.jd r5 = new com.xiaomi.push.jd
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Required field 'miscConfigVersion' was not found in serialized data! Struct: "
            r0.append(r1)
            java.lang.String r1 = r4.toString()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r5.<init>(r0)
            throw r5
        L_0x0054:
            short r1 = r0.f784a
            r2 = 1
            r3 = 8
            switch(r1) {
                case 1: goto L_0x0070;
                case 2: goto L_0x0062;
                default: goto L_0x005c;
            }
        L_0x005c:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r5, r0)
            goto L_0x007d
        L_0x0062:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x005c
            int r0 = r5.a()
            r4.f577b = r0
            r4.b((boolean) r2)
            goto L_0x007d
        L_0x0070:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x005c
            int r0 = r5.a()
            r4.f575a = r0
            r4.a((boolean) r2)
        L_0x007d:
            r5.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.hy.a(com.xiaomi.push.jc):void");
    }

    public void a(boolean z) {
        this.f576a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m416a() {
        return this.f576a.get(0);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m417a(hy hyVar) {
        return hyVar != null && this.f575a == hyVar.f575a && this.f577b == hyVar.f577b;
    }

    public hy b(int i) {
        this.f577b = i;
        b(true);
        return this;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f574a);
        jcVar.a(a);
        jcVar.a(this.f575a);
        jcVar.b();
        jcVar.a(b);
        jcVar.a(this.f577b);
        jcVar.b();
        jcVar.c();
        jcVar.a();
    }

    public void b(boolean z) {
        this.f576a.set(1, z);
    }

    public boolean b() {
        return this.f576a.get(1);
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof hy)) {
            return compareTo((hy) obj);
        }
        return false;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        return "XmPushActionCheckClientInfo(" + "miscConfigVersion:" + this.f575a + AVFSCacheConstants.COMMA_SEP + "pluginConfigVersion:" + this.f577b + Operators.BRACKET_END_STR;
    }
}
