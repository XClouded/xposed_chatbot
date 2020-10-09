package com.xiaomi.push;

import com.taobao.weex.BuildConfig;
import com.taobao.weex.el.parse.Operators;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class hz implements ir<hz, Object>, Serializable, Cloneable {
    private static final iz a = new iz("", (byte) 15, 1);

    /* renamed from: a  reason: collision with other field name */
    private static final jh f578a = new jh("XmPushActionCollectData");

    /* renamed from: a  reason: collision with other field name */
    public List<ho> f579a;

    /* renamed from: a */
    public int compareTo(hz hzVar) {
        int a2;
        if (!getClass().equals(hzVar.getClass())) {
            return getClass().getName().compareTo(hzVar.getClass().getName());
        }
        int compareTo = Boolean.valueOf(a()).compareTo(Boolean.valueOf(hzVar.a()));
        if (compareTo != 0) {
            return compareTo;
        }
        if (!a() || (a2 = is.a((List) this.f579a, (List) hzVar.f579a)) == 0) {
            return 0;
        }
        return a2;
    }

    public hz a(List<ho> list) {
        this.f579a = list;
        return this;
    }

    public void a() {
        if (this.f579a == null) {
            throw new jd("Required field 'dataCollectionItems' was not present! Struct: " + toString());
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
                this.f579a = new ArrayList(a3.f787a);
                for (int i = 0; i < a3.f787a; i++) {
                    ho hoVar = new ho();
                    hoVar.a(jcVar);
                    this.f579a.add(hoVar);
                }
                jcVar.i();
            } else {
                jf.a(jcVar, a2.a);
            }
            jcVar.g();
        }
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m418a() {
        return this.f579a != null;
    }

    /* renamed from: a  reason: collision with other method in class */
    public boolean m419a(hz hzVar) {
        if (hzVar == null) {
            return false;
        }
        boolean a2 = a();
        boolean a3 = hzVar.a();
        if (a2 || a3) {
            return a2 && a3 && this.f579a.equals(hzVar.f579a);
        }
        return true;
    }

    public void b(jc jcVar) {
        a();
        jcVar.a(f578a);
        if (this.f579a != null) {
            jcVar.a(a);
            jcVar.a(new ja((byte) 12, this.f579a.size()));
            for (ho b : this.f579a) {
                b.b(jcVar);
            }
            jcVar.e();
            jcVar.b();
        }
        jcVar.c();
        jcVar.a();
    }

    public boolean equals(Object obj) {
        if (obj != null && (obj instanceof hz)) {
            return compareTo((hz) obj);
        }
        return false;
    }

    public int hashCode() {
        return 0;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("XmPushActionCollectData(");
        sb.append("dataCollectionItems:");
        if (this.f579a == null) {
            sb.append(BuildConfig.buildJavascriptFrameworkVersion);
        } else {
            sb.append(this.f579a);
        }
        sb.append(Operators.BRACKET_END_STR);
        return sb.toString();
    }
}
