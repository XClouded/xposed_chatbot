package com.xiaomi.push;

import android.os.Bundle;
import com.taobao.weex.el.parse.Operators;

public class gf extends gd {
    private int a = Integer.MIN_VALUE;

    /* renamed from: a  reason: collision with other field name */
    private a f407a = null;

    /* renamed from: a  reason: collision with other field name */
    private b f408a = b.available;
    private String b = null;

    public enum a {
        chat,
        available,
        away,
        xa,
        dnd
    }

    public enum b {
        available,
        unavailable,
        subscribe,
        subscribed,
        unsubscribe,
        unsubscribed,
        error,
        probe
    }

    public gf(Bundle bundle) {
        super(bundle);
        if (bundle.containsKey("ext_pres_type")) {
            this.f408a = b.valueOf(bundle.getString("ext_pres_type"));
        }
        if (bundle.containsKey("ext_pres_status")) {
            this.b = bundle.getString("ext_pres_status");
        }
        if (bundle.containsKey("ext_pres_prio")) {
            this.a = bundle.getInt("ext_pres_prio");
        }
        if (bundle.containsKey("ext_pres_mode")) {
            this.f407a = a.valueOf(bundle.getString("ext_pres_mode"));
        }
    }

    public gf(b bVar) {
        a(bVar);
    }

    public Bundle a() {
        Bundle a2 = super.a();
        if (this.f408a != null) {
            a2.putString("ext_pres_type", this.f408a.toString());
        }
        if (this.b != null) {
            a2.putString("ext_pres_status", this.b);
        }
        if (this.a != Integer.MIN_VALUE) {
            a2.putInt("ext_pres_prio", this.a);
        }
        if (!(this.f407a == null || this.f407a == a.available)) {
            a2.putString("ext_pres_mode", this.f407a.toString());
        }
        return a2;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m337a() {
        StringBuilder sb = new StringBuilder();
        sb.append("<presence");
        if (p() != null) {
            sb.append(" xmlns=\"");
            sb.append(p());
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
        if (this.f408a != null) {
            sb.append(" type=\"");
            sb.append(this.f408a);
            sb.append("\"");
        }
        sb.append(Operators.G);
        if (this.b != null) {
            sb.append("<status>");
            sb.append(go.a(this.b));
            sb.append("</status>");
        }
        if (this.a != Integer.MIN_VALUE) {
            sb.append("<priority>");
            sb.append(this.a);
            sb.append("</priority>");
        }
        if (!(this.f407a == null || this.f407a == a.available)) {
            sb.append("<show>");
            sb.append(this.f407a);
            sb.append("</show>");
        }
        sb.append(o());
        gh a2 = a();
        if (a2 != null) {
            sb.append(a2.a());
        }
        sb.append("</presence>");
        return sb.toString();
    }

    public void a(int i) {
        if (i < -128 || i > 128) {
            throw new IllegalArgumentException("Priority value " + i + " is not valid. Valid range is -128 through 128.");
        }
        this.a = i;
    }

    public void a(a aVar) {
        this.f407a = aVar;
    }

    public void a(b bVar) {
        if (bVar != null) {
            this.f408a = bVar;
            return;
        }
        throw new NullPointerException("Type cannot be null");
    }

    public void a(String str) {
        this.b = str;
    }
}
