package com.huawei.hianalytics.f.b;

import android.annotation.SuppressLint;
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

public class f extends c {
    private static final String h = "f";

    public static Map<String, c[]> a(SharedPreferences sharedPreferences, Context context, String str, boolean z) {
        if (sharedPreferences == null || context == null) {
            return null;
        }
        HashMap hashMap = new HashMap();
        if (z) {
            Map<String, String> b = g.b(sharedPreferences);
            if (b.size() > 200 || b.size() == 0) {
                String str2 = h;
                b.c(str2, "get state data ：The number of data obtained is too much! or No data : " + b.size());
                return hashMap;
            }
            for (Map.Entry<String, String> a : b.entrySet()) {
                a(a, context, hashMap);
            }
        } else {
            a(str, (String) g.b(sharedPreferences, str, ""), context, (Map<String, c[]>) hashMap);
        }
        return hashMap;
    }

    private static void a(String str, String str2, Context context, Map<String, c[]> map) {
        ArrayList arrayList = new ArrayList();
        JSONArray jSONArray = null;
        try {
            if (!TextUtils.isEmpty(str2)) {
                jSONArray = new JSONArray(str2);
            }
        } catch (JSONException unused) {
            b.c(h, "When events turn to JSONArray,JSON Exception has happened");
        }
        if (jSONArray != null && jSONArray.length() != 0) {
            for (int i = 0; i < jSONArray.length(); i++) {
                f fVar = new f();
                try {
                    JSONObject jSONObject = jSONArray.getJSONObject(i);
                    if (jSONObject != null) {
                        fVar.b = jSONObject.optString("event");
                        fVar.c = c.a(jSONObject.optString("content"), context);
                        fVar.a = jSONObject.optString("eventtime");
                        fVar.d = jSONObject.optString("type");
                        if (jSONObject.has("event_session_name")) {
                            fVar.f = jSONObject.getString("event_session_name");
                            fVar.g = jSONObject.getString("first_session_event");
                        }
                        arrayList.add(fVar);
                    }
                } catch (JSONException unused2) {
                    arrayList.add(fVar);
                    b.c(h, "JSON Exception happened when create data for report - readDataToRecord");
                }
            }
            map.put(str, arrayList.toArray(new f[arrayList.size()]));
        }
    }

    private static void a(Map.Entry<String, String> entry, Context context, Map<String, c[]> map) {
        a(entry.getKey(), entry.getValue(), context, map);
    }

    @SuppressLint({"ApplySharedPref"})
    public void a(SharedPreferences sharedPreferences) {
        if (sharedPreferences == null || this.e == null) {
            b.c(h, "saveInSp() eventTag Can't be null");
            return;
        }
        String str = this.e;
        if (!this.e.equals("_default_config_tag")) {
            str = str + "-" + this.d;
        }
        String str2 = (String) g.b(sharedPreferences, str, "");
        try {
            JSONArray jSONArray = TextUtils.isEmpty(str2) ? new JSONArray() : new JSONArray(str2);
            JSONObject jSONObject = new JSONObject();
            jSONObject.put("event", this.b);
            jSONObject.put("content", this.c);
            jSONObject.put("eventtime", this.a);
            jSONObject.put("type", this.d);
            jSONObject.put("event_session_name", this.f);
            jSONObject.put("first_session_event", this.g);
            jSONArray.put(jSONObject);
            String jSONArray2 = jSONArray.toString();
            SharedPreferences.Editor edit = sharedPreferences.edit();
            edit.remove(str);
            edit.putString(str, jSONArray2);
            edit.commit();
        } catch (JSONException unused) {
            b.c(h, "When saveInSp() executed, JSON Exception has happened");
        }
    }
}
