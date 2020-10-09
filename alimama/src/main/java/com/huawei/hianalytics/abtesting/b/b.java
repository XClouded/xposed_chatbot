package com.huawei.hianalytics.abtesting.b;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import com.huawei.hianalytics.h.d;
import com.huawei.hianalytics.util.c;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

public class b implements Runnable {
    private Context a;
    private String b = com.huawei.hianalytics.abtesting.a.b.a().d();
    private String c = com.huawei.hianalytics.abtesting.a.b.a().e();
    private String d = com.huawei.hianalytics.abtesting.a.b.a().f();

    public b(Context context) {
        this.a = context;
    }

    private String a() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("userId", this.c);
        } catch (JSONException unused) {
            com.huawei.hianalytics.g.b.c("ABTest/SyncDataTask", "getBody: json exception");
        }
        return jSONObject.toString();
    }

    private Map<String, String> a(String str) {
        String str2 = this.b;
        String f = com.huawei.hianalytics.a.b.f();
        String valueOf = String.valueOf(System.currentTimeMillis());
        String replace = UUID.randomUUID().toString().replace("-", "");
        StringBuilder sb = new StringBuilder();
        sb.append(f);
        sb.append("|");
        sb.append(valueOf);
        sb.append("|");
        sb.append(replace);
        sb.append("|");
        int indexOf = str2.indexOf("?");
        String str3 = "";
        if (indexOf != -1) {
            str3 = str2.substring(indexOf + 1, str2.length());
        }
        sb.append(str3);
        sb.append("|");
        sb.append(str);
        String a2 = c.a(c.a(sb.toString(), this.d));
        HashMap hashMap = new HashMap();
        hashMap.put("appId", f);
        hashMap.put("timestamp", valueOf);
        hashMap.put("random", replace);
        hashMap.put("sign", a2);
        return hashMap;
    }

    public void run() {
        com.huawei.hianalytics.g.b.b("ABTest/SyncDataTask", "sync data running..");
        String a2 = a();
        Map<String, String> a3 = a(a2);
        try {
            d a4 = com.huawei.hianalytics.h.c.a(this.b + "/api/gateway/ab/api/service/shunting/hasdk/api/v1/getuserparameters", a2.getBytes("UTF-8"), a3);
            com.huawei.hianalytics.g.b.b("ABTest/SyncDataTask", "get experimental data request code : " + a4.a());
            String b2 = a4.b();
            if (!TextUtils.isEmpty(b2)) {
                try {
                    com.huawei.hianalytics.abtesting.a.b.a().a(b2);
                    com.huawei.hianalytics.g.b.b("ABTest/SyncDataTask", "Start caching data!");
                    SharedPreferences.Editor edit = com.huawei.hianalytics.util.d.a(this.a, "abtest").edit();
                    edit.putString("exp_data", com.huawei.hianalytics.f.g.c.b(b2, this.a));
                    edit.putLong("expdata_refresh_time", System.currentTimeMillis());
                    edit.commit();
                } catch (JSONException unused) {
                    com.huawei.hianalytics.g.b.c("ABTest/SyncDataTask", "experiment data error");
                }
            }
        } catch (IOException unused2) {
            com.huawei.hianalytics.g.b.c("ABTest/SyncDataTask", "getBody: body to bytes error!");
        }
        com.huawei.hianalytics.abtesting.a.b.a().a(true);
        com.huawei.hianalytics.abtesting.a.b.a().b(false);
    }
}
