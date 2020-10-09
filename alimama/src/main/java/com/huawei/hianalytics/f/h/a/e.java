package com.huawei.hianalytics.f.h.a;

import android.text.TextUtils;
import com.huawei.hianalytics.f.h.b.a;
import com.huawei.hianalytics.g.b;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class e implements b {
    private long b(String str) {
        if (TextUtils.isEmpty(str)) {
            return -1;
        }
        try {
            return Long.parseLong(str) * 1000;
        } catch (NumberFormatException unused) {
            b.c("V1LifeCycleAdapter", "failed to bisdk convertDuration.");
            return -1;
        }
    }

    private void c(String str, String str2) {
        String[] split = str.split(",");
        if (split.length == 3) {
            String str3 = split[0];
            String str4 = split[1];
            String str5 = split[2];
            if (!TextUtils.isEmpty(str3) && !TextUtils.isEmpty(str4) && !TextUtils.isEmpty(str5)) {
                long longValue = a.a(str4).longValue();
                long b = b(str5);
                if (longValue > 0 && b >= 0) {
                    a.b(new com.huawei.hianalytics.f.h.c.a(str3, longValue), str2);
                    a.a(new com.huawei.hianalytics.f.h.c.a(str3, longValue + b, b), str2);
                }
            }
        }
    }

    public void a(String str) {
        a.a(str);
    }

    public void a(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            for (String c : str.split(";")) {
                c(c, str2);
            }
        }
    }

    public void b(String str, String str2) {
        if (!TextUtils.isEmpty(str)) {
            try {
                JSONArray jSONArray = new JSONObject(str).getJSONArray("termination");
                if (jSONArray != null) {
                    int length = jSONArray.length();
                    for (int i = 0; i < length; i++) {
                        JSONObject jSONObject = jSONArray.getJSONObject(i);
                        if (jSONObject != null) {
                            if (jSONObject.has("b")) {
                                JSONArray jSONArray2 = jSONObject.getJSONArray("b");
                                if (jSONArray2 != null) {
                                    int length2 = jSONArray2.length();
                                    for (int i2 = 0; i2 < length2; i2++) {
                                        c(jSONArray2.getString(i2), str2);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (JSONException unused) {
                b.d("V1LifeCycleAdapter", "Exception occurred in parsing file data.");
            }
        }
    }
}
