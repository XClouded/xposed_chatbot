package com.xiaomi.push;

import android.os.Bundle;
import android.text.TextUtils;
import com.taobao.weex.el.parse.Operators;

public class gc extends gd {
    private boolean a = false;
    private String b = null;

    /* renamed from: b  reason: collision with other field name */
    private boolean f401b = false;
    private String c = null;
    private String d;
    private String e;
    private String f;
    private String g;
    private String h;
    private String i = "";
    private String j = "";
    private String k = "";
    private String l = "";

    public gc() {
    }

    public gc(Bundle bundle) {
        super(bundle);
        this.b = bundle.getString("ext_msg_type");
        this.d = bundle.getString("ext_msg_lang");
        this.c = bundle.getString("ext_msg_thread");
        this.e = bundle.getString("ext_msg_sub");
        this.f = bundle.getString("ext_msg_body");
        this.g = bundle.getString("ext_body_encode");
        this.h = bundle.getString("ext_msg_appid");
        this.a = bundle.getBoolean("ext_msg_trans", false);
        this.f401b = bundle.getBoolean("ext_msg_encrypt", false);
        this.i = bundle.getString("ext_msg_seq");
        this.j = bundle.getString("ext_msg_mseq");
        this.k = bundle.getString("ext_msg_fseq");
        this.l = bundle.getString("ext_msg_status");
    }

    public Bundle a() {
        Bundle a2 = super.a();
        if (!TextUtils.isEmpty(this.b)) {
            a2.putString("ext_msg_type", this.b);
        }
        if (this.d != null) {
            a2.putString("ext_msg_lang", this.d);
        }
        if (this.e != null) {
            a2.putString("ext_msg_sub", this.e);
        }
        if (this.f != null) {
            a2.putString("ext_msg_body", this.f);
        }
        if (!TextUtils.isEmpty(this.g)) {
            a2.putString("ext_body_encode", this.g);
        }
        if (this.c != null) {
            a2.putString("ext_msg_thread", this.c);
        }
        if (this.h != null) {
            a2.putString("ext_msg_appid", this.h);
        }
        if (this.a) {
            a2.putBoolean("ext_msg_trans", true);
        }
        if (!TextUtils.isEmpty(this.i)) {
            a2.putString("ext_msg_seq", this.i);
        }
        if (!TextUtils.isEmpty(this.j)) {
            a2.putString("ext_msg_mseq", this.j);
        }
        if (!TextUtils.isEmpty(this.k)) {
            a2.putString("ext_msg_fseq", this.k);
        }
        if (this.f401b) {
            a2.putBoolean("ext_msg_encrypt", true);
        }
        if (!TextUtils.isEmpty(this.l)) {
            a2.putString("ext_msg_status", this.l);
        }
        return a2;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m332a() {
        gh a2;
        StringBuilder sb = new StringBuilder();
        sb.append("<message");
        if (p() != null) {
            sb.append(" xmlns=\"");
            sb.append(p());
            sb.append("\"");
        }
        if (this.d != null) {
            sb.append(" xml:lang=\"");
            sb.append(h());
            sb.append("\"");
        }
        if (j() != null) {
            sb.append(" id=\"");
            sb.append(j());
            sb.append("\"");
        }
        if (l() != null) {
            sb.append(" to=\"");
            sb.append(go.a(l()));
            sb.append("\"");
        }
        if (!TextUtils.isEmpty(d())) {
            sb.append(" seq=\"");
            sb.append(d());
            sb.append("\"");
        }
        if (!TextUtils.isEmpty(e())) {
            sb.append(" mseq=\"");
            sb.append(e());
            sb.append("\"");
        }
        if (!TextUtils.isEmpty(f())) {
            sb.append(" fseq=\"");
            sb.append(f());
            sb.append("\"");
        }
        if (!TextUtils.isEmpty(g())) {
            sb.append(" status=\"");
            sb.append(g());
            sb.append("\"");
        }
        if (m() != null) {
            sb.append(" from=\"");
            sb.append(go.a(m()));
            sb.append("\"");
        }
        if (k() != null) {
            sb.append(" chid=\"");
            sb.append(go.a(k()));
            sb.append("\"");
        }
        if (this.a) {
            sb.append(" transient=\"true\"");
        }
        if (!TextUtils.isEmpty(this.h)) {
            sb.append(" appid=\"");
            sb.append(c());
            sb.append("\"");
        }
        if (!TextUtils.isEmpty(this.b)) {
            sb.append(" type=\"");
            sb.append(this.b);
            sb.append("\"");
        }
        if (this.f401b) {
            sb.append(" s=\"1\"");
        }
        sb.append(Operators.G);
        if (this.e != null) {
            sb.append("<subject>");
            sb.append(go.a(this.e));
            sb.append("</subject>");
        }
        if (this.f != null) {
            sb.append("<body");
            if (!TextUtils.isEmpty(this.g)) {
                sb.append(" encode=\"");
                sb.append(this.g);
                sb.append("\"");
            }
            sb.append(Operators.G);
            sb.append(go.a(this.f));
            sb.append("</body>");
        }
        if (this.c != null) {
            sb.append("<thread>");
            sb.append(this.c);
            sb.append("</thread>");
        }
        if ("error".equalsIgnoreCase(this.b) && (a2 = a()) != null) {
            sb.append(a2.a());
        }
        sb.append(o());
        sb.append("</message>");
        return sb.toString();
    }

    public void a(String str) {
        this.h = str;
    }

    public void a(String str, String str2) {
        this.f = str;
        this.g = str2;
    }

    public void a(boolean z) {
        this.a = z;
    }

    public String b() {
        return this.b;
    }

    public void b(String str) {
        this.i = str;
    }

    public void b(boolean z) {
        this.f401b = z;
    }

    public String c() {
        return this.h;
    }

    public void c(String str) {
        this.j = str;
    }

    public String d() {
        return this.i;
    }

    public void d(String str) {
        this.k = str;
    }

    public String e() {
        return this.j;
    }

    public void e(String str) {
        this.l = str;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        gc gcVar = (gc) obj;
        if (!super.equals(gcVar)) {
            return false;
        }
        if (this.f == null ? gcVar.f != null : !this.f.equals(gcVar.f)) {
            return false;
        }
        if (this.d == null ? gcVar.d != null : !this.d.equals(gcVar.d)) {
            return false;
        }
        if (this.e == null ? gcVar.e != null : !this.e.equals(gcVar.e)) {
            return false;
        }
        if (this.c == null ? gcVar.c == null : this.c.equals(gcVar.c)) {
            return this.b == gcVar.b;
        }
        return false;
    }

    public String f() {
        return this.k;
    }

    public void f(String str) {
        this.b = str;
    }

    public String g() {
        return this.l;
    }

    public void g(String str) {
        this.e = str;
    }

    public String h() {
        return this.d;
    }

    public void h(String str) {
        this.f = str;
    }

    public int hashCode() {
        int i2 = 0;
        int hashCode = (((((((this.b != null ? this.b.hashCode() : 0) * 31) + (this.f != null ? this.f.hashCode() : 0)) * 31) + (this.c != null ? this.c.hashCode() : 0)) * 31) + (this.d != null ? this.d.hashCode() : 0)) * 31;
        if (this.e != null) {
            i2 = this.e.hashCode();
        }
        return hashCode + i2;
    }

    public void i(String str) {
        this.c = str;
    }

    public void j(String str) {
        this.d = str;
    }
}
