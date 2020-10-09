package com.huawei.hianalytics.f.b;

import com.huawei.hianalytics.g.b;
import org.json.JSONException;
import org.json.JSONObject;

public class i {
    private String a;
    private String b = "";
    private String c;
    private String d;
    private String e;
    private String f;
    private String g;

    public String a() {
        return this.a;
    }

    public void a(String str) {
        this.f = str;
    }

    public JSONObject b() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("protocol_version", "1");
            jSONObject.put("compress_mode", "1");
            jSONObject.put("serviceid", this.d);
            jSONObject.put("appid", this.a);
            jSONObject.put("hmac", this.b);
            jSONObject.put("chifer", this.g);
            jSONObject.put("timestamp", this.c);
            jSONObject.put("servicetag", this.e);
            jSONObject.put("requestid", this.f);
        } catch (JSONException unused) {
            b.c("HeadData", "headData - toJsonObj():JSON structure Exception!");
        }
        return jSONObject;
    }

    public void b(String str) {
        this.e = str;
    }

    public void c(String str) {
        this.g = str;
    }

    public void d(String str) {
        this.d = str;
    }

    public void e(String str) {
        this.a = str;
    }

    public void f(String str) {
        this.b = str;
    }

    public void g(String str) {
        this.c = str;
    }
}
