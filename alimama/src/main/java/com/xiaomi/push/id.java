package com.xiaomi.push;

import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class id implements ir<id, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 15, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f615a = new jh("XmPushActionCustomConfig");

    /* renamed from: a  reason: collision with other field name */
    public List<hr> f616a;

    /* JADX WARNING: type inference failed for: r0v2, types: [boolean] */
    /* JADX WARNING: type inference failed for: r1v1, types: [boolean] */
    /* renamed from: a */
    public int compareTo(id idVar) {
        int a2;
        if (!getClass().equals(idVar.getClass())) {
            return getClass().getName().compareTo(idVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(idVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() == null || (a2 = is.a((List) this.f616a, (List) idVar.f616a)) == 0) {
            return 0;
        }
        return a2;
    }

    public List<hr> a() {
        return this.f616a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m441a() {
        if (this.f616a == null) {
            throw new jd("Required field 'customConfigs' was not present! Struct: " + toString());
        }
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
                this.f616a = new ArrayList(a3.f787a);
                for (int i = 0; i < a3.f787a; i++) {
                    hr hrVar = new hr();
                    hrVar.a(jcVar);
                    this.f616a.add(hrVar);
                }
                jcVar.i();
            } else {
                jf.a(jcVar, a2.a);
            }
            jcVar.g();
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m442a() {
        return this.f616a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m443a(id idVar) {
        if (idVar == null) {
            return false;
        }
        List<hr> a2 = a();
        List<hr> a3 = idVar.a();
        if (a2 == null && a3 == null) {
            return true;
        }
        return (a2 == null || a3 == null || !this.f616a.equals(idVar.f616a)) ? false : true;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f615a);
        if (this.f616a != null) {
            jcVar.a(a);
            jcVar.a(new ja((byte) 12, this.f616a.size()));
            for (hr b : this.f616a) {
                b.b(jcVar);
            }
            jcVar.e();
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof id)) {
            return compareTo((id) obj);
        }
        return false;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("XmPushActionCustomConfig(");
        sb.append("customConfigs:");
        if (this.f616a == null) {
            sb.append(BuildConfig.buildJavascriptFrameworkVersion);
        } else {
            sb.append(this.f616a);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
