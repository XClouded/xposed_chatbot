package com.huawei.hianalytics.f.b;

import android.text.TextUtils;
import com.huawei.hianalytics.g.b;
import org.json.JSONException;
import org.json.JSONObject;

public class d {
    private String a;

    public String a() {
        return this.a;
    }

    public void a(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                a(new JSONObject(str));
            } catch (JSONException unused) {
                b.c("ResponseResult", "When fromJson() executed ,JSON Exception has happened");
            }
        }
    }

    public void a(JSONObject jSONObject) {
        if (jSONObject != null) {
            this.a = jSONObject.optString("resultcode");
        }
    }
}
