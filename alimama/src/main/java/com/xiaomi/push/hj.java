package com.xiaomi.push;

import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class hj implements ir<hj, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 15, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f453a = new jh("ClientUploadData");

    /* renamed from: a  reason: collision with other field name */
    public List<hk> f454a;

    public int a() {
        if (this.f454a == null) {
            return 0;
        }
        return this.f454a.size();
    }

    /* renamed from: a */
    public int compareTo(hj hjVar) {
        int a2;
        if (!getClass().equals(hjVar.getClass())) {
            return getClass().getName().compareTo(hjVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(hjVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (!a() || (a2 = is.a((List) this.f454a, (List) hjVar.f454a)) == 0) {
            return 0;
        }
        return a2;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m363a() {
        if (this.f454a == null) {
            throw new jd("Required field 'uploadDataItems' was not present! Struct: " + toString());
        }
    }

    public void a(hk hkVar) {
        if (this.f454a == null) {
            this.f454a = new ArrayList();
        }
        this.f454a.add(hkVar);
    }

    public void a(jc jcVar) {
        jcVar.a();
        while (true) {
            iz a2 = jcVar.a();
            if (a2.a == 0) {
                jcVar.f();
                a();
                return;
            }
            if (a2.f784a == 1 && a2.a == 15) {
                ja a3 = jcVar.a();
                this.f454a = new ArrayList(a3.f787a);
                for (int i = 0; i < a3.f787a; i++) {
                    hk hkVar = new hk();
                    hkVar.a(jcVar);
                    this.f454a.add(hkVar);
                }
                jcVar.i();
            } else {
                jf.a(jcVar, a2.a);
            }
            jcVar.g();
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m364a() {
        return this.f454a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m365a(hj hjVar) {
        if (hjVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = hjVar.a();
        if (a2 || a3) {
            return a2 && a3 && this.f454a.equals(hjVar.f454a);
        }
        return true;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f453a);
        if (this.f454a != null) {
            jcVar.a(a);
            jcVar.a(new ja((byte) 12, this.f454a.size()));
            for (hk b : this.f454a) {
                b.b(jcVar);
            }
            jcVar.e();
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof hj)) {
            return compareTo((hj) obj);
        }
        return false;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("ClientUploadData(");
        sb.append("uploadDataItems:");
        if (this.f454a == null) {
            sb.append(BuildConfig.buildJavascriptFrameworkVersion);
        } else {
            sb.append(this.f454a);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
