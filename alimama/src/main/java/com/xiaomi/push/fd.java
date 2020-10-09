package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.List;

public class fd implements ir<fd, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 11, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f343a = new jh("StatsEvents");
    private static final iz b = new iz("", (byte) 11, 2);
    private static final iz c = new iz("", (byte) 15, 3);

    /* renamed from: a  reason: collision with other field name */
    public String f344a;

    /* renamed from: a  reason: collision with other field name */
    public List<fc> f345a;

    /* renamed from: b  reason: collision with other field name */
    public String f346b;

    public fd() {
    }

    public fd(String str, List<fc> list) {
        this();
        this.f344a = str;
        this.f345a = list;
    }

    /* renamed from: a */
    public int compareTo(fd fdVar) {
        int a2;
        int a3;
        int a4;
        if (!getClass().equals(fdVar.getClass())) {
            return getClass().getName().compareTo(fdVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(fdVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a4 = is.a(this.f344a, fdVar.f344a)) != 0) {
            return a4;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(fdVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a3 = is.a(this.f346b, fdVar.f346b)) != 0) {
            return a3;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(fdVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (!c() || (a2 = is.a((List) this.f345a, (List) fdVar.f345a)) == 0) {
            return 0;
        }
        return a2;
    }

    public fd a(String str) {
        this.f346b = str;
        return this;
    }

    public void a() {
        if (this.f344a == null) {
            throw new jd("Required field 'uuid' was not present! Struct: " + toString());
        } else if (this.f345a == null) {
            throw new jd("Required field 'events' was not present! Struct: " + toString());
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
            if (r1 != 0) goto L_0x0012
            r5.f()
            r4.a()
            return
        L_0x0012:
            short r1 = r0.f784a
            r2 = 11
            switch(r1) {
                case 1: goto L_0x0056;
                case 2: goto L_0x004b;
                case 3: goto L_0x001f;
                default: goto L_0x0019;
            }
        L_0x0019:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r5, r0)
            goto L_0x0060
        L_0x001f:
            byte r1 = r0.a
            r2 = 15
            if (r1 != r2) goto L_0x0019
            com.xiaomi.push.ja r0 = r5.a()
            java.util.ArrayList r1 = new java.util.ArrayList
            int r2 = r0.f787a
            r1.<init>(r2)
            r4.f345a = r1
            r1 = 0
        L_0x0033:
            int r2 = r0.f787a
            if (r1 >= r2) goto L_0x0047
            com.xiaomi.push.fc r2 = new com.xiaomi.push.fc
            r2.<init>()
            r2.a((com.xiaomi.push.jc) r5)
            java.util.List<com.xiaomi.push.fc> r3 = r4.f345a
            r3.add(r2)
            int r1 = r1 + 1
            goto L_0x0033
        L_0x0047:
            r5.i()
            goto L_0x0060
        L_0x004b:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r5.a()
            r4.f346b = r0
            goto L_0x0060
        L_0x0056:
            byte r1 = r0.a
            if (r1 != r2) goto L_0x0019
            java.lang.String r0 = r5.a()
            r4.f344a = r0
        L_0x0060:
            r5.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.fd.a(com.xiaomi.push.jc):void");
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m288a() {
        return this.f344a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m289a(fd fdVar) {
        if (fdVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = fdVar.a();
        if ((a2 || a3) && (!a2 || !a3 || !this.f344a.equals(fdVar.f344a))) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = fdVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f346b.equals(fdVar.f346b))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = fdVar.c();
        if (c2 || c3) {
            return c2 && c3 && this.f345a.equals(fdVar.f345a);
        }
        return true;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f343a);
        if (this.f344a != null) {
            jcVar.a(a);
            jcVar.a(this.f344a);
            jcVar.b();
        }
        if (this.f346b != null && b()) {
            jcVar.a(b);
            jcVar.a(this.f346b);
            jcVar.b();
        }
        if (this.f345a != null) {
            jcVar.a(c);
            jcVar.a(new ja((byte) 12, this.f345a.size()));
            for (fc b2 : this.f345a) {
                b2.b(jcVar);
            }
            jcVar.e();
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public boolean b() {
        return this.f346b != null;
    }

    public boolean c() {
        return this.f345a != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof fd)) {
            return compareTo((fd) obj);
        }
        return false;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("StatsEvents(");
        sb.append("uuid:");
        sb.append(this.f344a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f344a);
        if (b()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("operator:");
            sb.append(this.f346b == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f346b);
        }
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("events:");
        if (this.f345a == null) {
            sb.append(BuildConfig.buildJavascriptFrameworkVersion);
        } else {
            sb.append(this.f345a);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
