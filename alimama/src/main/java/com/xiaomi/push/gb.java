package com.xiaomi.push;

import android.os.Bundle;
import java.util.HashMap;
import java.util.Map;

public class gb extends gd {
    private a a = a.a;

    /* renamed from: a  reason: collision with other field name */
    private final Map<String, String> f399a = new HashMap();

    public static class a {
        public static final a a = new a("get");
        public static final a b = new a("set");
        public static final a c = new a("result");
        public static final a d = new a("error");
        public static final a e = new a("command");

        /* renamed from: a  reason: collision with other field name */
        private String f400a;

        private a(String str) {
            this.f400a = str;
        }

        public static a a(String str) {
            if (str == null) {
                return null;
            }
            String lowerCase = str.toLowerCase();
            if (a.toString().equals(lowerCase)) {
                return a;
            }
            if (b.toString().equals(lowerCase)) {
                return b;
            }
            if (d.toString().equals(lowerCase)) {
                return d;
            }
            if (c.toString().equals(lowerCase)) {
                return c;
            }
            if (e.toString().equals(lowerCase)) {
                return e;
            }
            return null;
        }

        public String toString() {
            return this.f400a;
        }
    }

    public gb() {
    }

    public gb(Bundle bundle) {
        super(bundle);
        if (bundle.containsKey("ext_iq_type")) {
            this.a = a.a(bundle.getString("ext_iq_type"));
        }
    }

    public Bundle a() {
        Bundle a2 = super.a();
        if (this.a != null) {
            a2.putString("ext_iq_type", this.a.toString());
        }
        return a2;
    }

    /* renamed from: a  reason: collision with other method in class */
    public a m330a() {
        return this.a;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m331a() {
        String str;
        StringBuilder sb = new StringBuilder();
        sb.append("<iq ");
        if (j() != null) {
            sb.append("id=\"" + j() + "\" ");
        }
        if (l() != null) {
            sb.append("to=\"");
            sb.append(go.a(l()));
            sb.append("\" ");
        }
        if (m() != null) {
            sb.append("from=\"");
            sb.append(go.a(m()));
            sb.append("\" ");
        }
        if (k() != null) {
            sb.append("chid=\"");
            sb.append(go.a(k()));
            sb.append("\" ");
        }
        for (Map.Entry next : this.f399a.entrySet()) {
            sb.append(go.a((String) next.getKey()));
            sb.append("=\"");
            sb.append(go.a((String) next.getValue()));
            sb.append("\" ");
        }
        if (this.a == null) {
            str = "type=\"get\">";
        } else {
            sb.append("type=\"");
            sb.append(a());
            str = "\">";
        }
        sb.append(str);
        String b = b();
        if (b != null) {
            sb.append(b);
        }
        sb.append(o());
        gh a2 = a();
        if (a2 != null) {
            sb.append(a2.a());
        }
        sb.append("</iq>");
        return sb.toString();
    }

    public void a(a aVar) {
        if (aVar == null) {
            aVar = a.a;
        }
        this.a = aVar;
    }

    public synchronized void a(Map<String, String> map) {
        this.f399a.putAll(map);
    }

    public String b() {
        return null;
    }
}
