package com.huawei.hianalytics.f.b;

import android.text.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class b {
    private String a;
    private String b;
    private String c;
    private String d;
    private String e = "";
    private String f;
    private String g;

    public JSONObject a() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("androidid", this.a);
            jSONObject.put("imei", this.b);
            jSONObject.put("uuid", this.c);
            jSONObject.put("udid", this.e);
            jSONObject.put("oaid", this.d);
            jSONObject.put("upid", this.f);
            jSONObject.put("sn", this.g);
        } catch (JSONException unused) {
            com.huawei.hianalytics.g.b.c("InfoData", " When toJsonObj() executed has JSON Exception happened");
        }
        return jSONObject;
    }

    public void a(String str) {
        if (TextUtils.isEmpty(str)) {
            this.a = "";
        } else {
            this.a = str;
        }
    }

    public void b(String str) {
        if (TextUtils.isEmpty(str)) {
            this.b = "";
        } else {
            this.b = str;
        }
    }

    public void c(String str) {
        if (TextUtils.isEmpty(str)) {
            this.c = "";
        } else {
            this.c = str;
        }
    }

    public void d(String str) {
        this.d = str;
    }

    public void e(String str) {
        if (str != null) {
            this.e = str;
        }
    }

    public void f(String str) {
        this.f = str;
    }

    public void g(String str) {
        this.g = str;
    }
}
