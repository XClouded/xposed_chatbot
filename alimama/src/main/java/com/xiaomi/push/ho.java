package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.BitSet;

public class ho implements ir<ho, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 10, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f474a = new jh("DataCollectionItem");
    private static final iz b = new iz("", (byte) 8, 2);
    private static final iz c = new iz("", (byte) 11, 3);

    /* renamed from: a  reason: collision with other field name */
    public long f475a;

    /* renamed from: a  reason: collision with other field name */
    public hi f476a;

    /* renamed from: a  reason: collision with other field name */
    public String f477a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f478a = new BitSet(1);

    /* renamed from: a */
    public int compareTo(ho hoVar) {
        int a2;
        int a3;
        int a4;
        if (!getClass().equals(hoVar.getClass())) {
            return getClass().getName().compareTo(hoVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(hoVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a4 = is.a(this.f475a, hoVar.f475a)) != 0) {
            return a4;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(hoVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a3 = is.a((Comparable) this.f476a, (Comparable) hoVar.f476a)) != 0) {
            return a3;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(hoVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (!c() || (a2 = is.a(this.f477a, hoVar.f477a)) == 0) {
            return 0;
        }
        return a2;
    }

    public ho a(long j) {
        this.f475a = j;
        a(true);
        return this;
    }

    public ho a(hi hiVar) {
        this.f476a = hiVar;
        return this;
    }

    public ho a(String str) {
        this.f477a = str;
        return this;
    }

    public String a() {
        return this.f477a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m375a() {
        if (this.f476a == null) {
            throw new jd("Required field 'collectionType' was not present! Struct: " + toString());
        } else if (this.f477a == null) {
            throw new jd("Required field 'content' was not present! Struct: " + toString());
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(com.xiaomi.push.jc r4) {
        /*
            r3 = this;
            r4.a()
        L_0x0003:
            com.xiaomi.push.iz r0 = r4.a()
            byte r1 = r0.a
            if (r1 != 0) goto L_0x0033
            r4.f()
            boolean r4 = r3.a()
            if (r4 == 0) goto L_0x0018
            r3.a()
            return
        L_0x0018:
            com.xiaomi.push.jd r4 = new com.xiaomi.push.jd
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Required field 'collectedAt' was not found in serialized data! Struct: "
            r0.append(r1)
            java.lang.String r1 = r3.toString()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r4.<init>(r0)
            throw r4
        L_0x0033:
            short r1 = r0.f784a
            switch(r1) {
                case 1: goto L_0x005c;
                case 2: goto L_0x004b;
                case 3: goto L_0x003e;
                default: goto L_0x0038;
            }
        L_0x0038:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r4, r0)
            goto L_0x006c
        L_0x003e:
            byte r1 = r0.a
            r2 = 11
            if (r1 != r2) goto L_0x0038
            java.lang.String r0 = r4.a()
            r3.f477a = r0
            goto L_0x006c
        L_0x004b:
            byte r1 = r0.a
            r2 = 8
            if (r1 != r2) goto L_0x0038
            int r0 = r4.a()
            com.xiaomi.push.hi r0 = com.xiaomi.push.hi.a(r0)
            r3.f476a = r0
            goto L_0x006c
        L_0x005c:
            byte r1 = r0.a
            r2 = 10
            if (r1 != r2) goto L_0x0038
            long r0 = r4.a()
            r3.f475a = r0
            r0 = 1
            r3.a((boolean) r0)
        L_0x006c:
            r4.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.ho.a(com.xiaomi.push.jc):void");
    }

    public void a(boolean z) {
        this.f478a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m376a() {
        return this.f478a.get(0);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m377a(ho hoVar) {
        if (hoVar == null || this.f475a != hoVar.f475a) {
            return false;
        }
        boolean b2 = b();
        boolean b3 = hoVar.b();
        if ((b2 || b3) && (!b2 || !b3 || !this.f476a.equals(hoVar.f476a))) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = hoVar.c();
        if (c2 || c3) {
            return c2 && c3 && this.f477a.equals(hoVar.f477a);
        }
        return true;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f474a);
        jcVar.a(a);
        jcVar.a(this.f475a);
        jcVar.b();
        if (this.f476a != null) {
            jcVar.a(b);
            jcVar.a(this.f476a.a());
            jcVar.b();
        }
        if (this.f477a != null) {
            jcVar.a(c);
            jcVar.a(this.f477a);
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public boolean b() {
        return this.f476a != null;
    }

    public boolean c() {
        return this.f477a != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ho)) {
            return compareTo((ho) obj);
        }
        return false;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("DataCollectionItem(");
        sb.append("collectedAt:");
        sb.append(this.f475a);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("collectionType:");
        if (this.f476a == null) {
            sb.append(BuildConfig.buildJavascriptFrameworkVersion);
        } else {
            sb.append(this.f476a);
        }
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("content:");
        sb.append(this.f477a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f477a);
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
