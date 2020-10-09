package com.huawei.hianalytics.f.b;

import android.os.Build;
import com.huawei.hianalytics.g.b;
import org.json.JSONException;
import org.json.JSONObject;

public class j {
    private String a;
    private String b;
    private String c;
    private String d;
    private String e;
    private String f;
    private String g;

    public JSONObject a() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("_rom_ver", Build.DISPLAY);
            jSONObject.put("_emui_ver", this.a);
            jSONObject.put("_model", Build.MODEL);
            jSONObject.put("_mcc", this.f);
            jSONObject.put("_mnc", this.g);
            jSONObject.put("_package_name", this.b);
            jSONObject.put("_app_ver", this.c);
            jSONObject.put("_lib_ver", "2.1.4.301");
            jSONObject.put("_channel", this.d);
            jSONObject.put("_lib_name", "hianalytics");
            jSONObject.put("_oaid_tracking_flag", this.e);
        } catch (JSONException unused) {
            b.c("RomInfoData", "toJsonObj(): JSON structure Exception: Rom info toJsonObj exception!");
        }
        return jSONObject;
    }

    public void a(String str) {
        this.e = str;
    }

    public void b(String str) {
        this.d = str;
    }

    public void c(String str) {
        this.a = str;
    }

    public void d(String str) {
        this.b = str;
    }

    public void e(String str) {
        this.c = str;
    }

    public void f(String str) {
        this.f = str;
    }

    public void g(String str) {
        this.g = str;
    }
}
