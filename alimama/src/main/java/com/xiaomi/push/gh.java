package com.xiaomi.push;

import android.os.Bundle;
import android.os.Parcelable;
import com.taobao.weex.el.parse.Operators;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class gh {
    private int a;

    /* renamed from: a  reason: collision with other field name */
    private String f411a;

    /* renamed from: a  reason: collision with other field name */
    private List<ga> f412a = null;
    private String b;
    private String c;
    private String d;

    public static class a {
        public static final a a = new a("internal-server-error");
        public static final a b = new a("forbidden");
        public static final a c = new a("bad-request");
        public static final a d = new a("conflict");
        public static final a e = new a("feature-not-implemented");
        public static final a f = new a("gone");
        public static final a g = new a("item-not-found");
        public static final a h = new a("jid-malformed");
        public static final a i = new a("not-acceptable");
        public static final a j = new a("not-allowed");
        public static final a k = new a("not-authorized");
        public static final a l = new a("payment-required");
        public static final a m = new a("recipient-unavailable");
        public static final a n = new a("redirect");
        public static final a o = new a("registration-required");
        public static final a p = new a("remote-server-error");
        public static final a q = new a("remote-server-not-found");
        public static final a r = new a("remote-server-timeout");
        public static final a s = new a("resource-constraint");
        public static final a t = new a("service-unavailable");
        public static final a u = new a("subscription-required");
        public static final a v = new a("undefined-condition");
        public static final a w = new a("unexpected-request");
        public static final a x = new a("request-timeout");
        /* access modifiers changed from: private */

        /* renamed from: a  reason: collision with other field name */
        public String f413a;

        public a(String str) {
            this.f413a = str;
        }

        public String toString() {
            return this.f413a;
        }
    }

    public gh(int i, String str, String str2, String str3, String str4, List<ga> list) {
        this.a = i;
        this.f411a = str;
        this.c = str2;
        this.b = str3;
        this.d = str4;
        this.f412a = list;
    }

    public gh(Bundle bundle) {
        this.a = bundle.getInt("ext_err_code");
        if (bundle.containsKey("ext_err_type")) {
            this.f411a = bundle.getString("ext_err_type");
        }
        this.b = bundle.getString("ext_err_cond");
        this.c = bundle.getString("ext_err_reason");
        this.d = bundle.getString("ext_err_msg");
        Parcelable[] parcelableArray = bundle.getParcelableArray("ext_exts");
        if (parcelableArray != null) {
            this.f412a = new ArrayList(parcelableArray.length);
            for (Parcelable parcelable : parcelableArray) {
                ga a2 = ga.a((Bundle) parcelable);
                if (a2 != null) {
                    this.f412a.add(a2);
                }
            }
        }
    }

    public gh(a aVar) {
        a(aVar);
        this.d = null;
    }

    private void a(a aVar) {
        this.b = aVar.f413a;
    }

    public Bundle a() {
        Bundle bundle = new Bundle();
        if (this.f411a != null) {
            bundle.putString("ext_err_type", this.f411a);
        }
        bundle.putInt("ext_err_code", this.a);
        if (this.c != null) {
            bundle.putString("ext_err_reason", this.c);
        }
        if (this.b != null) {
            bundle.putString("ext_err_cond", this.b);
        }
        if (this.d != null) {
            bundle.putString("ext_err_msg", this.d);
        }
        if (this.f412a != null) {
            Bundle[] bundleArr = new Bundle[this.f412a.size()];
            int i = 0;
            for (ga a2 : this.f412a) {
                Bundle a3 = a2.a();
                if (a3 != null) {
                    bundleArr[i] = a3;
                    i++;
                }
            }
            bundle.putParcelableArray("ext_exts", bundleArr);
        }
        return bundle;
    }

    /* renamed from: a  reason: collision with other method in class */
    public String m338a() {
        StringBuilder sb = new StringBuilder();
        sb.append("<error code=\"");
        sb.append(this.a);
        sb.append("\"");
        if (this.f411a != null) {
            sb.append(" type=\"");
            sb.append(this.f411a);
            sb.append("\"");
        }
        if (this.c != null) {
            sb.append(" reason=\"");
            sb.append(this.c);
            sb.append("\"");
        }
        sb.append(Operators.G);
        if (this.b != null) {
            sb.append(Operators.L);
            sb.append(this.b);
            sb.append(" xmlns=\"urn:ietf:params:xml:ns:xmpp-stanzas\"/>");
        }
        if (this.d != null) {
            sb.append("<text xml:lang=\"en\" xmlns=\"urn:ietf:params:xml:ns:xmpp-stanzas\">");
            sb.append(this.d);
            sb.append("</text>");
        }
        for (ge d2 : a()) {
            sb.append(d2.d());
        }
        sb.append("</error>");
        return sb.toString();
    }

    /* renamed from: a  reason: collision with other method in class */
    public synchronized List<ga> m339a() {
        if (this.f412a == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(this.f412a);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (this.b != null) {
            sb.append(this.b);
        }
        sb.append(Operators.BRACKET_START_STR);
        sb.append(this.a);
        sb.append(Operators.BRACKET_END_STR);
        if (this.d != null) {
            sb.append(Operators.SPACE_STR);
            sb.append(this.d);
        }
        return sb.toString();
    }
}
