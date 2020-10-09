package com.xiaomi.miui.pushads.sdk;

import android.os.Bundle;
import com.xiaomi.push.ce;
import org.json.JSONObject;

public class a extends ce {
    public String a;

    public Bundle a() {
        Bundle a2 = super.a();
        a2.putString("content", this.a);
        return a2;
    }

    public void a(JSONObject jSONObject) {
        super.a(jSONObject);
        this.a = jSONObject.optString("content");
    }

    public String toString() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("id", this.f173a);
            jSONObject.put("showType", this.a);
            jSONObject.put("lastShowTime", this.f175b);
            jSONObject.put("content", this.a);
            return jSONObject.toString();
        } catch (Exception unused) {
            return "";
        }
    }
}
