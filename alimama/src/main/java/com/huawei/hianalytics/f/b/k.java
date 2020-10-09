package com.huawei.hianalytics.f.b;

import org.json.JSONObject;

public class k extends d {
    private String a;

    public void a(JSONObject jSONObject) {
        if (jSONObject != null) {
            super.a(jSONObject);
            this.a = jSONObject.optString("serverUrl");
        }
    }

    public String b() {
        return this.a;
    }
}
