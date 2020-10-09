package com.xiaomi.push;

import com.taobao.alivfssdk.utils.AVFSCacheConstants;
import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class ht implements ir<ht, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 11, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f515a = new jh("PushMetaInfo");
    private static final iz b = new iz("", (byte) 10, 2);
    private static final iz c = new iz("", (byte) 11, 3);
    private static final iz d = new iz("", (byte) 11, 4);
    private static final iz e = new iz("", (byte) 11, 5);
    private static final iz f = new iz("", (byte) 8, 6);
    private static final iz g = new iz("", (byte) 11, 7);
    private static final iz h = new iz("", (byte) 8, 8);
    private static final iz i = new iz("", (byte) 8, 9);
    private static final iz j = new iz("", (byte) 13, 10);
    private static final iz k = new iz("", (byte) 13, 11);
    private static final iz l = new iz("", (byte) 2, 12);
    private static final iz m = new iz("", (byte) 13, 13);

    /* renamed from: a  reason: collision with other field name */
    public int f516a;

    /* renamed from: a  reason: collision with other field name */
    public long f517a;

    /* renamed from: a  reason: collision with other field name */
    public String f518a;

    /* renamed from: a  reason: collision with other field name */
    private BitSet f519a;

    /* renamed from: a  reason: collision with other field name */
    public Map<String, String> f520a;

    /* renamed from: a  reason: collision with other field name */
    public boolean f521a;

    /* renamed from: b  reason: collision with other field name */
    public int f522b;

    /* renamed from: b  reason: collision with other field name */
    public String f523b;

    /* renamed from: b  reason: collision with other field name */
    public Map<String, String> f524b;

    /* renamed from: c  reason: collision with other field name */
    public int f525c;

    /* renamed from: c  reason: collision with other field name */
    public String f526c;

    /* renamed from: c  reason: collision with other field name */
    public Map<String, String> f527c;

    /* renamed from: d  reason: collision with other field name */
    public String f528d;

    /* renamed from: e  reason: collision with other field name */
    public String f529e;

    public ht() {
        this.f519a = new BitSet(5);
        this.f521a = false;
    }

    public ht(ht htVar) {
        this.f519a = new BitSet(5);
        this.f519a.clear();
        this.f519a.or(htVar.f519a);
        if (htVar.a()) {
            this.f518a = htVar.f518a;
        }
        this.f517a = htVar.f517a;
        if (htVar.c()) {
            this.f523b = htVar.f523b;
        }
        if (htVar.d()) {
            this.f526c = htVar.f526c;
        }
        if (htVar.e()) {
            this.f528d = htVar.f528d;
        }
        this.f516a = htVar.f516a;
        if (htVar.g()) {
            this.f529e = htVar.f529e;
        }
        this.f522b = htVar.f522b;
        this.f525c = htVar.f525c;
        if (htVar.j()) {
            HashMap hashMap = new HashMap();
            for (Map.Entry next : htVar.f520a.entrySet()) {
                hashMap.put((String) next.getKey(), (String) next.getValue());
            }
            this.f520a = hashMap;
        }
        if (htVar.k()) {
            HashMap hashMap2 = new HashMap();
            for (Map.Entry next2 : htVar.f524b.entrySet()) {
                hashMap2.put((String) next2.getKey(), (String) next2.getValue());
            }
            this.f524b = hashMap2;
        }
        this.f521a = htVar.f521a;
        if (htVar.n()) {
            HashMap hashMap3 = new HashMap();
            for (Map.Entry next3 : htVar.f527c.entrySet()) {
                hashMap3.put((String) next3.getKey(), (String) next3.getValue());
            }
            this.f527c = hashMap3;
        }
    }

    public int a() {
        return this.f516a;
    }

    /* renamed from: a */
    public int compareTo(ht htVar) {
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
        int a14;
        if (!getClass().equals(htVar.getClass())) {
            return getClass().getName().compareTo(htVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(htVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() && (a14 = is.a(this.f518a, htVar.f518a)) != 0) {
            return a14;
        }
        int compareTo2 = Boolean.valueOf(b()).compareTo(Boolean.valueOf(htVar.b()));
        if (compareTo2 != 0) {
            return compareTo2;
        }
        if (b() && (a13 = is.a(this.f517a, htVar.f517a)) != 0) {
            return a13;
        }
        int compareTo3 = Boolean.valueOf(c()).compareTo(Boolean.valueOf(htVar.c()));
        if (compareTo3 != 0) {
            return compareTo3;
        }
        if (c() && (a12 = is.a(this.f523b, htVar.f523b)) != 0) {
            return a12;
        }
        int compareTo4 = Boolean.valueOf(d()).compareTo(Boolean.valueOf(htVar.d()));
        if (compareTo4 != 0) {
            return compareTo4;
        }
        if (d() && (a11 = is.a(this.f526c, htVar.f526c)) != 0) {
            return a11;
        }
        int compareTo5 = Boolean.valueOf(e()).compareTo(Boolean.valueOf(htVar.e()));
        if (compareTo5 != 0) {
            return compareTo5;
        }
        if (e() && (a10 = is.a(this.f528d, htVar.f528d)) != 0) {
            return a10;
        }
        int compareTo6 = Boolean.valueOf(f()).compareTo(Boolean.valueOf(htVar.f()));
        if (compareTo6 != 0) {
            return compareTo6;
        }
        if (f() && (a9 = is.a(this.f516a, htVar.f516a)) != 0) {
            return a9;
        }
        int compareTo7 = Boolean.valueOf(g()).compareTo(Boolean.valueOf(htVar.g()));
        if (compareTo7 != 0) {
            return compareTo7;
        }
        if (g() && (a8 = is.a(this.f529e, htVar.f529e)) != 0) {
            return a8;
        }
        int compareTo8 = Boolean.valueOf(h()).compareTo(Boolean.valueOf(htVar.h()));
        if (compareTo8 != 0) {
            return compareTo8;
        }
        if (h() && (a7 = is.a(this.f522b, htVar.f522b)) != 0) {
            return a7;
        }
        int compareTo9 = Boolean.valueOf(i()).compareTo(Boolean.valueOf(htVar.i()));
        if (compareTo9 != 0) {
            return compareTo9;
        }
        if (i() && (a6 = is.a(this.f525c, htVar.f525c)) != 0) {
            return a6;
        }
        int compareTo10 = Boolean.valueOf(j()).compareTo(Boolean.valueOf(htVar.j()));
        if (compareTo10 != 0) {
            return compareTo10;
        }
        if (j() && (a5 = is.a((Map) this.f520a, (Map) htVar.f520a)) != 0) {
            return a5;
        }
        int compareTo11 = Boolean.valueOf(k()).compareTo(Boolean.valueOf(htVar.k()));
        if (compareTo11 != 0) {
            return compareTo11;
        }
        if (k() && (a4 = is.a((Map) this.f524b, (Map) htVar.f524b)) != 0) {
            return a4;
        }
        int compareTo12 = Boolean.valueOf(m()).compareTo(Boolean.valueOf(htVar.m()));
        if (compareTo12 != 0) {
            return compareTo12;
        }
        if (m() && (a3 = is.a(this.f521a, htVar.f521a)) != 0) {
            return a3;
        }
        int compareTo13 = Boolean.valueOf(n()).compareTo(Boolean.valueOf(htVar.n()));
        if (compareTo13 != 0) {
            return compareTo13;
        }
        if (!n() || (a2 = is.a((Map) this.f527c, (Map) htVar.f527c)) == 0) {
            return 0;
        }
        return a2;
    }

    /* renamed from: a  reason: collision with other method in class */
    public long m395a() {
        return this.f517a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public ht m396a() {
        return new ht(this);
    }

    public ht a(int i2) {
        this.f516a = i2;
        b(true);
        return this;
    }

    public ht a(String str) {
        this.f518a = str;
        return this;
    }

    public ht a(Map<String, String> map) {
        this.f520a = map;
        return this;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m397a() {
        return this.f518a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public Map<String, String> m398a() {
        return this.f520a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m399a() {
        if (this.f518a == null) {
            throw new jd("Required field 'id' was not present! Struct: " + toString());
        }
    }

    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00cc, code lost:
        r9.h();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void a(com.xiaomi.push.jc r9) {
        /*
            r8 = this;
            r9.a()
        L_0x0003:
            com.xiaomi.push.iz r0 = r9.a()
            byte r1 = r0.a
            if (r1 != 0) goto L_0x0033
            r9.f()
            boolean r9 = r8.b()
            if (r9 == 0) goto L_0x0018
            r8.a()
            return
        L_0x0018:
            com.xiaomi.push.jd r9 = new com.xiaomi.push.jd
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "Required field 'messageTs' was not found in serialized data! Struct: "
            r0.append(r1)
            java.lang.String r1 = r8.toString()
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r9.<init>(r0)
            throw r9
        L_0x0033:
            short r1 = r0.f784a
            r2 = 0
            r3 = 13
            r4 = 8
            r5 = 2
            r6 = 11
            r7 = 1
            switch(r1) {
                case 1: goto L_0x0137;
                case 2: goto L_0x0127;
                case 3: goto L_0x011c;
                case 4: goto L_0x0111;
                case 5: goto L_0x0106;
                case 6: goto L_0x00f8;
                case 7: goto L_0x00ed;
                case 8: goto L_0x00df;
                case 9: goto L_0x00d1;
                case 10: goto L_0x00a5;
                case 11: goto L_0x007e;
                case 12: goto L_0x006f;
                case 13: goto L_0x0048;
                default: goto L_0x0041;
            }
        L_0x0041:
            byte r0 = r0.a
            com.xiaomi.push.jf.a(r9, r0)
            goto L_0x0141
        L_0x0048:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x0041
            com.xiaomi.push.jb r0 = r9.a()
            java.util.HashMap r1 = new java.util.HashMap
            int r3 = r0.f788a
            int r3 = r3 * 2
            r1.<init>(r3)
            r8.f527c = r1
        L_0x005b:
            int r1 = r0.f788a
            if (r2 >= r1) goto L_0x00cc
            java.lang.String r1 = r9.a()
            java.lang.String r3 = r9.a()
            java.util.Map<java.lang.String, java.lang.String> r4 = r8.f527c
            r4.put(r1, r3)
            int r2 = r2 + 1
            goto L_0x005b
        L_0x006f:
            byte r1 = r0.a
            if (r1 != r5) goto L_0x0041
            boolean r0 = r9.a()
            r8.f521a = r0
            r8.e(r7)
            goto L_0x0141
        L_0x007e:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x0041
            com.xiaomi.push.jb r0 = r9.a()
            java.util.HashMap r1 = new java.util.HashMap
            int r3 = r0.f788a
            int r3 = r3 * 2
            r1.<init>(r3)
            r8.f524b = r1
        L_0x0091:
            int r1 = r0.f788a
            if (r2 >= r1) goto L_0x00cc
            java.lang.String r1 = r9.a()
            java.lang.String r3 = r9.a()
            java.util.Map<java.lang.String, java.lang.String> r4 = r8.f524b
            r4.put(r1, r3)
            int r2 = r2 + 1
            goto L_0x0091
        L_0x00a5:
            byte r1 = r0.a
            if (r1 != r3) goto L_0x0041
            com.xiaomi.push.jb r0 = r9.a()
            java.util.HashMap r1 = new java.util.HashMap
            int r3 = r0.f788a
            int r3 = r3 * 2
            r1.<init>(r3)
            r8.f520a = r1
        L_0x00b8:
            int r1 = r0.f788a
            if (r2 >= r1) goto L_0x00cc
            java.lang.String r1 = r9.a()
            java.lang.String r3 = r9.a()
            java.util.Map<java.lang.String, java.lang.String> r4 = r8.f520a
            r4.put(r1, r3)
            int r2 = r2 + 1
            goto L_0x00b8
        L_0x00cc:
            r9.h()
            goto L_0x0141
        L_0x00d1:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x0041
            int r0 = r9.a()
            r8.f525c = r0
            r8.d((boolean) r7)
            goto L_0x0141
        L_0x00df:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x0041
            int r0 = r9.a()
            r8.f522b = r0
            r8.c((boolean) r7)
            goto L_0x0141
        L_0x00ed:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0041
            java.lang.String r0 = r9.a()
            r8.f529e = r0
            goto L_0x0141
        L_0x00f8:
            byte r1 = r0.a
            if (r1 != r4) goto L_0x0041
            int r0 = r9.a()
            r8.f516a = r0
            r8.b((boolean) r7)
            goto L_0x0141
        L_0x0106:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0041
            java.lang.String r0 = r9.a()
            r8.f528d = r0
            goto L_0x0141
        L_0x0111:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0041
            java.lang.String r0 = r9.a()
            r8.f526c = r0
            goto L_0x0141
        L_0x011c:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0041
            java.lang.String r0 = r9.a()
            r8.f523b = r0
            goto L_0x0141
        L_0x0127:
            byte r1 = r0.a
            r2 = 10
            if (r1 != r2) goto L_0x0041
            long r0 = r9.a()
            r8.f517a = r0
            r8.a((boolean) r7)
            goto L_0x0141
        L_0x0137:
            byte r1 = r0.a
            if (r1 != r6) goto L_0x0041
            java.lang.String r0 = r9.a()
            r8.f518a = r0
        L_0x0141:
            r9.g()
            goto L_0x0003
        */
        throw new UnsupportedOperationException("Method not decompiled: com.xiaomi.push.ht.a(com.xiaomi.push.jc):void");
    }

    public void a(String str, String str2) {
        if (this.f520a == null) {
            this.f520a = new HashMap();
        }
        this.f520a.put(str, str2);
    }

    public void a(boolean z) {
        this.f519a.set(0, z);
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m400a() {
        return this.f518a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m401a(ht htVar) {
        if (htVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = htVar.a();
        if (((a2 || a3) && (!a2 || !a3 || !this.f518a.equals(htVar.f518a))) || this.f517a != htVar.f517a) {
            return false;
        }
        boolean c2 = c();
        boolean c3 = htVar.c();
        if ((c2 || c3) && (!c2 || !c3 || !this.f523b.equals(htVar.f523b))) {
            return false;
        }
        boolean d2 = d();
        boolean d3 = htVar.d();
        if ((d2 || d3) && (!d2 || !d3 || !this.f526c.equals(htVar.f526c))) {
            return false;
        }
        boolean e2 = e();
        boolean e3 = htVar.e();
        if ((e2 || e3) && (!e2 || !e3 || !this.f528d.equals(htVar.f528d))) {
            return false;
        }
        boolean f2 = f();
        boolean f3 = htVar.f();
        if ((f2 || f3) && (!f2 || !f3 || this.f516a != htVar.f516a)) {
            return false;
        }
        boolean g2 = g();
        boolean g3 = htVar.g();
        if ((g2 || g3) && (!g2 || !g3 || !this.f529e.equals(htVar.f529e))) {
            return false;
        }
        boolean h2 = h();
        boolean h3 = htVar.h();
        if ((h2 || h3) && (!h2 || !h3 || this.f522b != htVar.f522b)) {
            return false;
        }
        boolean i2 = i();
        boolean i3 = htVar.i();
        if ((i2 || i3) && (!i2 || !i3 || this.f525c != htVar.f525c)) {
            return false;
        }
        boolean j2 = j();
        boolean j3 = htVar.j();
        if ((j2 || j3) && (!j2 || !j3 || !this.f520a.equals(htVar.f520a))) {
            return false;
        }
        boolean k2 = k();
        boolean k3 = htVar.k();
        if ((k2 || k3) && (!k2 || !k3 || !this.f524b.equals(htVar.f524b))) {
            return false;
        }
        boolean m2 = m();
        boolean m3 = htVar.m();
        if ((m2 || m3) && (!m2 || !m3 || this.f521a != htVar.f521a)) {
            return false;
        }
        boolean n = n();
        boolean n2 = htVar.n();
        if (n || n2) {
            return n && n2 && this.f527c.equals(htVar.f527c);
        }
        return true;
    }

    public int b() {
        return this.f522b;
    }

    public ht b(int i2) {
        this.f522b = i2;
        c(true);
        return this;
    }

    public ht b(String str) {
        this.f523b = str;
        return this;
    }

    /* renamed from: b  reason: collision with other method in class */
    public String m402b() {
        return this.f523b;
    }

    /* renamed from: b  reason: collision with other method in class */
    public Map<String, String> m403b() {
        return this.f524b;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f515a);
        if (this.f518a != null) {
            jcVar.a(a);
            jcVar.a(this.f518a);
            jcVar.b();
        }
        jcVar.a(b);
        jcVar.a(this.f517a);
        jcVar.b();
        if (this.f523b != null && c()) {
            jcVar.a(c);
            jcVar.a(this.f523b);
            jcVar.b();
        }
        if (this.f526c != null && d()) {
            jcVar.a(d);
            jcVar.a(this.f526c);
            jcVar.b();
        }
        if (this.f528d != null && e()) {
            jcVar.a(e);
            jcVar.a(this.f528d);
            jcVar.b();
        }
        if (f()) {
            jcVar.a(f);
            jcVar.a(this.f516a);
            jcVar.b();
        }
        if (this.f529e != null && g()) {
            jcVar.a(g);
            jcVar.a(this.f529e);
            jcVar.b();
        }
        if (h()) {
            jcVar.a(h);
            jcVar.a(this.f522b);
            jcVar.b();
        }
        if (i()) {
            jcVar.a(i);
            jcVar.a(this.f525c);
            jcVar.b();
        }
        if (this.f520a != null && j()) {
            jcVar.a(j);
            jcVar.a(new jb((byte) 11, (byte) 11, this.f520a.size()));
            for (Map.Entry next : this.f520a.entrySet()) {
                jcVar.a((String) next.getKey());
                jcVar.a((String) next.getValue());
            }
            jcVar.d();
            jcVar.b();
        }
        if (this.f524b != null && k()) {
            jcVar.a(k);
            jcVar.a(new jb((byte) 11, (byte) 11, this.f524b.size()));
            for (Map.Entry next2 : this.f524b.entrySet()) {
                jcVar.a((String) next2.getKey());
                jcVar.a((String) next2.getValue());
            }
            jcVar.d();
            jcVar.b();
        }
        if (m()) {
            jcVar.a(l);
            jcVar.a(this.f521a);
            jcVar.b();
        }
        if (this.f527c != null && n()) {
            jcVar.a(m);
            jcVar.a(new jb((byte) 11, (byte) 11, this.f527c.size()));
            for (Map.Entry next3 : this.f527c.entrySet()) {
                jcVar.a((String) next3.getKey());
                jcVar.a((String) next3.getValue());
            }
            jcVar.d();
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public void b(String str, String str2) {
        if (this.f524b == null) {
            this.f524b = new HashMap();
        }
        this.f524b.put(str, str2);
    }

    public void b(boolean z) {
        this.f519a.set(1, z);
    }

    /* renamed from: b  reason: collision with other method in class */
    public boolean m404b() {
        return this.f519a.get(0);
    }

    public int c() {
        return this.f525c;
    }

    public ht c(int i2) {
        this.f525c = i2;
        d(true);
        return this;
    }

    public ht c(String str) {
        this.f526c = str;
        return this;
    }

    /* renamed from: c  reason: collision with other method in class */
    public String m405c() {
        return this.f526c;
    }

    public void c(boolean z) {
        this.f519a.set(2, z);
    }

    /* renamed from: c  reason: collision with other method in class */
    public boolean m406c() {
        return this.f523b != null;
    }

    public ht d(String str) {
        this.f528d = str;
        return this;
    }

    public String d() {
        return this.f528d;
    }

    public void d(boolean z) {
        this.f519a.set(3, z);
    }

    /* renamed from: d  reason: collision with other method in class */
    public boolean m407d() {
        return this.f526c != null;
    }

    public void e(boolean z) {
        this.f519a.set(4, z);
    }

    public boolean e() {
        return this.f528d != null;
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ht)) {
            return compareTo((ht) obj);
        }
        return false;
    }

    public boolean f() {
        return this.f519a.get(1);
    }

    public boolean g() {
        return this.f529e != null;
    }

    public boolean h() {
        return this.f519a.get(2);
    }

    public int hashCode() {
        return 0;
    }

    public boolean i() {
        return this.f519a.get(3);
    }

    public boolean j() {
        return this.f520a != null;
    }

    public boolean k() {
        return this.f524b != null;
    }

    public boolean l() {
        return this.f521a;
    }

    public boolean m() {
        return this.f519a.get(4);
    }

    public boolean n() {
        return this.f527c != null;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("PushMetaInfo(");
        sb.append("id:");
        sb.append(this.f518a == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f518a);
        sb.append(AVFSCacheConstants.COMMA_SEP);
        sb.append("messageTs:");
        sb.append(this.f517a);
        if (c()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("topic:");
            sb.append(this.f523b == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f523b);
        }
        if (d()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("title:");
            sb.append(this.f526c == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f526c);
        }
        if (e()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("description:");
            sb.append(this.f528d == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f528d);
        }
        if (f()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("notifyType:");
            sb.append(this.f516a);
        }
        if (g()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("url:");
            sb.append(this.f529e == null ? BuildConfig.buildJavascriptFrameworkVersion : this.f529e);
        }
        if (h()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("passThrough:");
            sb.append(this.f522b);
        }
        if (i()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("notifyId:");
            sb.append(this.f525c);
        }
        if (j()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("extra:");
            if (this.f520a == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f520a);
            }
        }
        if (k()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("internal:");
            if (this.f524b == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f524b);
            }
        }
        if (m()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("ignoreRegInfo:");
            sb.append(this.f521a);
        }
        if (n()) {
            sb.append(AVFSCacheConstants.COMMA_SEP);
            sb.append("apsProperFields:");
            if (this.f527c == null) {
                sb.append(BuildConfig.buildJavascriptFrameworkVersion);
            } else {
                sb.append(this.f527c);
            }
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
