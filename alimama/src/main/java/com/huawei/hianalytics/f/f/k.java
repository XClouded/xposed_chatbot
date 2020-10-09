package com.huawei.hianalytics.f.f;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.huawei.hianalytics.e.d;
import com.huawei.hianalytics.f.b.f;
import com.huawei.hianalytics.f.g.c;
import com.huawei.hianalytics.f.g.g;
import com.huawei.hianalytics.f.g.h;
import com.huawei.hianalytics.f.g.j;
import com.huawei.hianalytics.g.b;
import com.huawei.hianalytics.i.a;
import com.taobao.android.ultron.datamodel.imp.ProtocolConst;
import java.util.Map;
import java.util.UUID;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class k implements a {
    private Context a;

    public k(Context context) {
        this.a = context;
    }

    private static void a(Context context) {
        d dVar;
        boolean z;
        SharedPreferences a2 = com.huawei.hianalytics.util.d.a(context, "global_v2");
        String str = (String) com.huawei.hianalytics.util.d.b(a2, "uuid", "");
        if (TextUtils.isEmpty(str)) {
            String replace = UUID.randomUUID().toString().replace("-", "");
            com.huawei.hianalytics.e.a.a().f().k(replace);
            a2.edit().putString("uuid", replace).apply();
        } else {
            com.huawei.hianalytics.e.a.a().f().k(str);
        }
        String str2 = (String) com.huawei.hianalytics.util.d.b(a2, "upload_url", "");
        long longValue = ((Long) com.huawei.hianalytics.util.d.b(a2, "upload_url_time", 0L)).longValue();
        long currentTimeMillis = System.currentTimeMillis();
        if (TextUtils.isEmpty(str2) || currentTimeMillis - longValue > 86400000) {
            dVar = com.huawei.hianalytics.e.a.a().f();
            z = true;
        } else {
            com.huawei.hianalytics.e.a.a().f().n(str2);
            dVar = com.huawei.hianalytics.e.a.a().f();
            z = false;
        }
        dVar.a(z);
    }

    private void a(Context context, String str) {
        SharedPreferences.Editor edit = g.c(context, str).edit();
        edit.clear();
        edit.apply();
    }

    private void a(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONArray jSONArray = new JSONObject(str).getJSONArray(ProtocolConst.KEY_EVENTS);
                if (jSONArray != null) {
                    if (jSONArray.length() != 0) {
                        a(jSONArray);
                        return;
                    }
                }
                b.b("InitInfoV2support", "No V2CacheFile Data!");
            } catch (JSONException unused) {
                b.c("InitInfoV2support", "parseV2CacheData() eventJsonArray.cacheData No json !");
            }
        }
    }

    private void a(String str, String str2) {
        b(str);
        a(str2);
    }

    private void a(Map<String, ?> map, String str) {
        map.remove(str);
        b.b("InitInfoV2support", "remove this unusualData, backup data size : " + map.size());
    }

    private void a(JSONArray jSONArray) {
        for (int i = 0; i < jSONArray.length(); i++) {
            try {
                JSONObject jSONObject = jSONArray.getJSONObject(i);
                if (jSONObject != null) {
                    String optString = jSONObject.optString("event");
                    String a2 = c.a(jSONObject.getString("content"), this.a);
                    String optString2 = jSONObject.optString("eventtime");
                    String optString3 = jSONObject.optString("type");
                    if (!TextUtils.isEmpty(optString) && !TextUtils.isEmpty(a2) && !TextUtils.isEmpty(optString2)) {
                        if (!TextUtils.isEmpty(optString3)) {
                            j.a((a) new d(this.a, "_default_config_tag", h.a(optString3), optString, a2, Long.parseLong(optString2)));
                        }
                    }
                    b.c("InitInfoV2support", "parseV2SPData() Data anomaly! Discarding data.");
                    return;
                }
                continue;
            } catch (JSONException unused) {
                b.c("InitInfoV2support", "parseV2SPData() eventJsonArray.getJSONObject() error !");
            }
        }
    }

    private void a(boolean z) {
        String b = com.huawei.hianalytics.a.b.b();
        String g = com.huawei.hianalytics.a.b.g();
        if (TextUtils.isEmpty(b)) {
            b.b("InitInfoV2support", "app ver is first save!");
        } else if (!b.equals(g)) {
            b.b("InitInfoV2support", "the appVers are different!");
            h.a().a("", this.a, "", b);
        } else if (z) {
            b.b("InitInfoV2support", "report backup data!");
            h.a().a("", this.a, "", g);
        }
    }

    private boolean a() {
        JSONArray jSONArray;
        b.b("InitInfoV2support", "begin handler backup data...");
        SharedPreferences c = g.c(this.a, "backup_event");
        SharedPreferences c2 = g.c(this.a, "stat_v2_1");
        Map<String, ?> all = c.getAll();
        if (all.size() == 0) {
            b.b("InitInfoV2support", "No backup data needed to be processed");
            return false;
        }
        String string = g.c(this.a, "global_v2").getString("request_id", "");
        if (!com.huawei.hianalytics.a.b.k()) {
            b.b("InitInfoV2support", "UnusualDataIgnored is false,begin handler unusualData,spKey: " + string);
            String[] split = string.split("#");
            String str = split[0];
            String str2 = split[1];
            Map<String, com.huawei.hianalytics.f.b.c[]> a2 = f.a(c, this.a, string, false);
            if (a2.size() != 0) {
                for (Map.Entry<String, com.huawei.hianalytics.f.b.c[]> value : a2.entrySet()) {
                    new c(this.a, str, (com.huawei.hianalytics.f.b.c[]) value.getValue(), com.huawei.hianalytics.a.b.b(), str2).a();
                }
            } else {
                b.b("InitInfoV2support", "backupUnusualData is empty");
            }
        }
        a(all, string);
        boolean z = false;
        for (Map.Entry next : all.entrySet()) {
            String str3 = (String) next.getKey();
            String str4 = str3.split("#")[0];
            try {
                jSONArray = new JSONArray(c2.getString(str4, ""));
            } catch (JSONException unused) {
                b.b("InitInfoV2support", "handler unusualData: stat sp data is error,spKey: " + str4);
                jSONArray = new JSONArray();
            }
            if (next.getValue() instanceof String) {
                try {
                    JSONArray jSONArray2 = new JSONArray((String) next.getValue());
                    for (int i = 0; i < jSONArray2.length(); i++) {
                        jSONArray.put(jSONArray2.getJSONObject(i));
                    }
                    g.a(c2, str4, (Object) jSONArray.toString());
                    z = true;
                } catch (JSONException unused2) {
                    b.c("InitInfoV2support", "backup data is error! spKey: " + str3);
                }
            }
        }
        SharedPreferences.Editor edit = c.edit();
        edit.clear();
        edit.apply();
        return z;
    }

    private void b() {
        if (!g.b(this.a, "stat_v2").exists()) {
            b.b("InitInfoV2support", "No V2 data supporting!");
            return;
        }
        SharedPreferences c = g.c(this.a, "stat_v2");
        if (c == null) {
            b.c("InitInfoV2support", "getV2StateSP error!");
            return;
        }
        String str = (String) g.b(c, ProtocolConst.KEY_EVENTS, "");
        String a2 = c.a(com.huawei.hianalytics.f.g.d.a(this.a, "cached_v2"), this.a);
        c();
        if (!TextUtils.isEmpty(str) || !TextUtils.isEmpty(a2)) {
            a(str, a2);
        } else {
            b.b("InitInfoV2support", " No cached V2 data found.");
        }
    }

    private void b(String str) {
        JSONArray jSONArray = null;
        try {
            if (!TextUtils.isEmpty(str)) {
                jSONArray = new JSONArray(str);
            }
        } catch (JSONException unused) {
            b.c("InitInfoV2support", "parseV2SPData:When events turn to JSONArray,json Exception");
        }
        if (jSONArray == null || jSONArray.length() == 0) {
            b.b("InitInfoV2support", "No V2State Data!");
        } else {
            a(jSONArray);
        }
    }

    private void c() {
        a(this.a, "stat_v2");
        com.huawei.hianalytics.f.g.d.b(this.a, "cached_v2");
    }

    private void d() {
        com.huawei.hianalytics.e.a.a().f().c(com.huawei.hianalytics.c.c.b());
        a(this.a);
    }

    public void run() {
        d();
        a(a());
        SharedPreferences c = g.c(this.a, "global_v2");
        if (((Boolean) g.b(c, "v2cacheHandlerFlag", false)).booleanValue()) {
            b.b("InitInfoV2support", "cached data by HASDKV2 has already handled.");
            return;
        }
        g.a(c, "v2cacheHandlerFlag", (Object) true);
        b();
    }
}
