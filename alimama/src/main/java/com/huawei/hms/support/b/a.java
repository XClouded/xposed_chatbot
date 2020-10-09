package com.huawei.hms.support.b;

import android.content.Context;
import android.text.TextUtils;
import com.huawei.hianalytics.v2.HiAnalytics;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.huawei.hms.c.j;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: HiAnalyticsUtil */
public class a {
    private static final Object a = new Object();
    private static a b;

    public static a a() {
        a aVar;
        synchronized (a) {
            if (b == null) {
                b = new a();
            }
            aVar = b;
        }
        return aVar;
    }

    public void a(Context context, String str, Map<String, String> map) {
        String a2 = a(map);
        if (!TextUtils.isEmpty(a2)) {
            b(context, str, a2);
        }
    }

    public void a(Context context, String str, String str2) {
        if (!a().b() && context != null) {
            b(context, str, a(context, str2));
        }
    }

    public void b(Context context, String str, String str2) {
        if (context != null && HiAnalytics.getInitFlag()) {
            HiAnalytics.onEvent(context, str, str2);
        }
    }

    private String a(Context context, String str) {
        String packageName = context.getPackageName();
        return "01|" + "" + "|" + packageName + "|" + j.a(context) + "|" + HuaweiApiAvailability.HMS_SDK_VERSION_CODE + "|" + str;
    }

    private String a(Map<String, String> map) {
        if (map == null) {
            return null;
        }
        JSONObject jSONObject = new JSONObject();
        try {
            for (Map.Entry next : map.entrySet()) {
                jSONObject.put((String) next.getKey(), next.getValue());
            }
        } catch (JSONException e) {
            com.huawei.hms.support.log.a.d("HiAnalyticsUtil", "AnalyticsHelper create json exception" + e.getMessage());
        }
        return jSONObject.toString();
    }

    public boolean b() {
        if (j.a()) {
            return false;
        }
        com.huawei.hms.support.log.a.b("HiAnalyticsUtil", "not ChinaROM ");
        return true;
    }
}
