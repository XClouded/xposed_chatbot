package com.xiaomi.push;

import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ie implements ir<ie, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 15, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f617a = new jh("XmPushActionNormalConfig");

    /* renamed from: a  reason: collision with other field name */
    public List<hp> f618a;

    /* JADX WARNING: type inference failed for: r0v2, types: [boolean] */
    /* JADX WARNING: type inference failed for: r1v1, types: [boolean] */
    /* renamed from: a */
    public int compareTo(ie ieVar) {
        int a2;
        if (!getClass().equals(ieVar.getClass())) {
            return getClass().getName().compareTo(ieVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(ieVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (a() == null || (a2 = is.a((List) this.f618a, (List) ieVar.f618a)) == 0) {
            return 0;
        }
        return a2;
    }

    public List<hp> a() {
        return this.f618a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public void m444a() {
        if (this.f618a == null) {
            throw new jd("Required field 'normalConfigs' was not present! Struct: " + toString());
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
                this.f618a = new ArrayList(a3.f787a);
                for (int i = 0; i < a3.f787a; i++) {
                    hp hpVar = new hp();
                    hpVar.a(jcVar);
                    this.f618a.add(hpVar);
                }
                jcVar.i();
            } else {
                jf.a(jcVar, a2.a);
            }
            jcVar.g();
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m445a() {
        return this.f618a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m446a(ie ieVar) {
        if (ieVar == null) {
            return false;
        }
        List<hp> a2 = a();
        List<hp> a3 = ieVar.a();
        if (a2 == null && a3 == null) {
            return true;
        }
        return (a2 == null || a3 == null || !this.f618a.equals(ieVar.f618a)) ? false : true;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f617a);
        if (this.f618a != null) {
            jcVar.a(a);
            jcVar.a(new ja((byte) 12, this.f618a.size()));
            for (hp b : this.f618a) {
                b.b(jcVar);
            }
            jcVar.e();
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof ie)) {
            return compareTo((ie) obj);
        }
        return false;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("XmPushActionNormalConfig(");
        sb.append("normalConfigs:");
        if (this.f618a == null) {
            sb.append(BuildConfig.buildJavascriptFrameworkVersion);
        } else {
            sb.append(this.f618a);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
