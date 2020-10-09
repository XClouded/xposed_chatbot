package android.taobao.windvane.packageapp.zipapp.utils;

import org.json.JSONObject;

public class AppResInfo {
    public String mHash = null;
    public JSONObject mHeaders = null;

    AppResInfo(String str, JSONObject jSONObject) {
        this.mHash = str;
        this.mHeaders = jSONObject;
    }
}
