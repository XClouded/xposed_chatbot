package com.xiaomi.miui.pushads.sdk;

import android.os.Bundle;
import com.xiaomi.push.ce;
import org.json.JSONObject;

public class h extends ce {
    public String a;
    public String b;
    public String c;
    public String d;
    public String e;
    public String f;
    public String g;

    public h() {
    }

    public h(h hVar) {
        super(hVar);
        this.a = hVar.a;
        this.b = hVar.b;
        this.c = hVar.c;
        this.d = hVar.d;
        this.e = hVar.e;
        this.f = hVar.f;
        this.g = hVar.g;
    }

    public Bundle a() {
        Bundle a2 = super.a();
        a2.putString("actionUrl", this.a);
        a2.putString("imgUrl", this.b);
        a2.putString("titText", this.c);
        a2.putString("priText", this.d);
        a2.putString("secText", this.e);
        a2.putString("type", this.f);
        a2.putString("actionText", this.g);
        return a2;
    }

    public void a(JSONObject jSONObject) {
        super.a(jSONObject);
        this.a = jSONObject.optString("actionUrl");
        this.b = jSONObject.optString("imgUrl");
        this.c = jSONObject.optString("titText");
        this.d = jSONObject.optString("priText");
        this.e = jSONObject.optString("secText");
        this.f = jSONObject.optString("type");
        this.g = jSONObject.optString("actionText");
    }

    public String toString() {
        try {
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("showType", this.a);
            jSONObject.put("lastShowTime", this.f175b);
            jSONObject.put("actionUrl", this.a);
            jSONObject.put("type", this.f);
            jSONObject.put("imgUrl", this.b);
            jSONObject.put("receiveUpperBound", this.c);
            jSONObject.put("downloadedPath", a());
            jSONObject.put("titText", this.c);
            jSONObject.put("priText", this.d);
            jSONObject.put("secText", this.e);
            jSONObject.put("actionText", this.g);
            return jSONObject.toString();
        } catch (Exception unused) {
            return null;
        }
    }
}
