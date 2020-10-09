package com.alipay.sdk.data;

import android.content.Context;
import android.text.TextUtils;
import com.alipay.sdk.sys.b;
import com.alipay.sdk.util.c;
import com.alipay.sdk.util.j;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public final class a {
    public static final int a = 3500;
    public static final String b = "https://h5.m.taobao.com/mlapp/olist.html";
    public static final int c = 10;
    public static final boolean d = true;
    public static final boolean e = true;
    public static final int f = 1000;
    public static final int g = 20000;
    public static final String h = "alipay_cashier_dynamic_config";
    public static final String i = "timeout";
    public static final String j = "st_sdk_config";
    public static final String k = "tbreturl";
    public static final String l = "launchAppSwitch";
    public static final String m = "configQueryInterval";
    public static final String n = "scheme_pay";
    public static final String o = "scheme_pay_2";
    public static final String p = "intercept_batch";
    private static a x;
    public boolean q = false;
    private int r = a;
    private String s = b;
    private int t = 10;
    private boolean u = true;
    private boolean v = true;
    private List<C0001a> w = null;

    public int a() {
        if (this.r < 1000 || this.r > 20000) {
            c.b("", "DynamicConfig::getJumpTimeout(default) >3500");
            return a;
        }
        c.b("", "DynamicConfig::getJumpTimeout >" + this.r);
        return this.r;
    }

    public boolean b() {
        return this.u;
    }

    public boolean c() {
        return this.v;
    }

    public String d() {
        return this.s;
    }

    public int e() {
        return this.t;
    }

    public List<C0001a> f() {
        return this.w;
    }

    public void a(boolean z) {
        this.q = z;
    }

    public static a g() {
        if (x == null) {
            x = new a();
            x.h();
        }
        return x;
    }

    private void h() {
        a(j.b(b.a().b(), h, (String) null));
    }

    private void a(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject jSONObject = new JSONObject(str);
                this.r = jSONObject.optInt("timeout", a);
                this.s = jSONObject.optString(k, b).trim();
                this.t = jSONObject.optInt(m, 10);
                this.w = C0001a.a(jSONObject.optJSONArray(l));
                this.u = jSONObject.optBoolean(o, true);
                this.v = jSONObject.optBoolean(p, true);
            } catch (Throwable th) {
                c.a(th);
            }
        }
    }

    /* access modifiers changed from: private */
    public void i() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("timeout", a());
            jSONObject.put(k, d());
            jSONObject.put(m, e());
            jSONObject.put(l, C0001a.a(f()));
            jSONObject.put(o, b());
            jSONObject.put(p, c());
            j.a(b.a().b(), h, jSONObject.toString());
        } catch (Exception e2) {
            c.a(e2);
        }
    }

    /* access modifiers changed from: private */
    public void b(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONObject optJSONObject = new JSONObject(str).optJSONObject(j);
                if (optJSONObject != null) {
                    this.r = optJSONObject.optInt("timeout", a);
                    this.s = optJSONObject.optString(k, b).trim();
                    this.t = optJSONObject.optInt(m, 10);
                    this.w = C0001a.a(optJSONObject.optJSONArray(l));
                    this.u = optJSONObject.optBoolean(o, true);
                    this.v = optJSONObject.optBoolean(p, true);
                    return;
                }
                c.d("msp", "config is null");
            } catch (Throwable th) {
                c.a(th);
            }
        }
    }

    public void a(Context context) {
        new Thread(new b(this, context)).start();
    }

    /* renamed from: com.alipay.sdk.data.a$a  reason: collision with other inner class name */
    public static final class C0001a {
        public final String a;
        public final int b;
        public final String c;

        public C0001a(String str, int i, String str2) {
            this.a = str;
            this.b = i;
            this.c = str2;
        }

        public static C0001a a(JSONObject jSONObject) {
            if (jSONObject == null) {
                return null;
            }
            return new C0001a(jSONObject.optString("pn"), jSONObject.optInt("v", 0), jSONObject.optString("pk"));
        }

        public static List<C0001a> a(JSONArray jSONArray) {
            if (jSONArray == null) {
                return null;
            }
            ArrayList arrayList = new ArrayList();
            int length = jSONArray.length();
            for (int i = 0; i < length; i++) {
                C0001a a2 = a(jSONArray.optJSONObject(i));
                if (a2 != null) {
                    arrayList.add(a2);
                }
            }
            return arrayList;
        }

        public static JSONObject a(C0001a aVar) {
            if (aVar == null) {
                return null;
            }
            try {
                return new JSONObject().put("pn", aVar.a).put("v", aVar.b).put("pk", aVar.c);
            } catch (JSONException e) {
                c.a(e);
                return null;
            }
        }

        public static JSONArray a(List<C0001a> list) {
            if (list == null) {
                return null;
            }
            JSONArray jSONArray = new JSONArray();
            for (C0001a a2 : list) {
                jSONArray.put(a(a2));
            }
            return jSONArray;
        }

        public String toString() {
            return String.valueOf(a(this));
        }
    }
}
