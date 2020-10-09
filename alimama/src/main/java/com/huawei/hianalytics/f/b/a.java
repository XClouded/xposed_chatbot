package com.huawei.hianalytics.f.b;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.huawei.hianalytics.f.g.c;
import com.huawei.hianalytics.f.g.g;
import com.huawei.hianalytics.g.b;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class a extends c {
    private Context h;

    public a() {
    }

    public a(Context context) {
        this.h = context;
    }

    public static Map<String, c[]> a(SharedPreferences sharedPreferences, Context context, String str, boolean z) {
        if (sharedPreferences == null || context == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        if (z) {
            Map<String, String> b = g.b(sharedPreferences);
            if (b.size() > 200 || b.size() == 0) {
                b.c("ActionData", "get state data ：The number of data obtained is too much! or No data");
                return hashMap;
            }
            for (Map.Entry<String, String> a : b.entrySet()) {
                a(a, context, hashMap);
            }
        } else {
            a(str, g.a(sharedPreferences, str), context, (Map<String, c[]>) hashMap);
        }
        return hashMap;
    }

    private static void a(String str, String str2, Context context, Map<String, c[]> map) {
        try {
            ArrayList arrayList = new ArrayList();
            if (TextUtils.isEmpty(str2)) {
                b.c("ActionData", "No data from cache sp!");
                return;
            }
            JSONArray jSONArray = new JSONArray(str2);
            int length = jSONArray.length();
            for (int i = 0; i < length; i++) {
                JSONObject optJSONObject = jSONArray.optJSONObject(i);
                a aVar = new a(context);
                aVar.a(optJSONObject);
                arrayList.add(aVar);
            }
            map.put(str, arrayList.toArray(new a[arrayList.size()]));
        } catch (JSONException unused) {
            b.c("ActionData", "readDataToAppAction() events is not json format");
        }
    }

    private static void a(Map.Entry<String, String> entry, Context context, Map<String, c[]> map) {
        a(entry.getKey(), entry.getValue(), context, map);
    }

    public JSONObject a() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("type", g());
            jSONObject.put("eventtime", this.a);
            jSONObject.put("event", this.b);
            jSONObject.put("event_session_name", this.f);
            jSONObject.put("first_session_event", this.g);
            jSONObject.put("content", c.b(h(), this.h));
        } catch (JSONException unused) {
            b.c("ActionData", "When toJsonStr() executed,properties parameter anomaly.JSON Exception has happen!");
        }
        return jSONObject;
    }

    public JSONObject a(boolean z) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("type", g());
            jSONObject.put("eventtime", this.a);
            jSONObject.put("event", this.b);
            jSONObject.put("event_session_name", this.f);
            jSONObject.put("first_session_event", this.g);
            jSONObject.put("properties", z ? c.b(h(), this.h) : new JSONObject(this.c));
        } catch (JSONException unused) {
            b.c("ActionData", "When toJsonObj() executed,properties parameter anomaly.JSON Exception has happen!");
        }
        return jSONObject;
    }

    public void a(JSONObject jSONObject) {
        if (jSONObject != null) {
            this.a = jSONObject.optString("eventtime", "");
            this.b = jSONObject.optString("event", "");
            f(c.a(jSONObject.optString("properties"), this.h));
            this.d = jSONObject.optString("type", "");
        }
    }

    public JSONObject b() {
        return a(false);
    }
}
