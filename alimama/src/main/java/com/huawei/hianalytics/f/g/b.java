package com.huawei.hianalytics.f.g;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.DisplayMetrics;
import com.huawei.hianalytics.util.e;
import org.json.JSONException;
import org.json.JSONObject;

public class b {
    public static JSONObject a(Context context) {
        String b = b(context);
        String str = Build.MANUFACTURER == null ? "UNKNOWN" : Build.MANUFACTURER;
        String str2 = Build.VERSION.RELEASE;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int i = displayMetrics.heightPixels;
        int i2 = displayMetrics.widthPixels;
        String b2 = b("ro.product.CustCVersion", "");
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("_sys_language", b);
            jSONObject.put("_cust_version", b2);
            jSONObject.put("_manufacturer", str);
            jSONObject.put("_os", "android");
            jSONObject.put("_os_ver", str2);
            jSONObject.put("_screen_height", i);
            jSONObject.put("_screen_width", i2);
            return jSONObject;
        } catch (JSONException unused) {
            com.huawei.hianalytics.g.b.c("HiAnalytics/event", "getDeviceInfo() json Exc,A parameter error!");
            return null;
        }
    }

    public static JSONObject a(Context context, String str, String str2) {
        String str3;
        String str4;
        JSONObject a = a(context);
        if (a != null) {
            try {
                a.put("_start_type", str);
                a.put("_start_cmd", str2);
                return a;
            } catch (JSONException unused) {
                str4 = "HiAnalytics/event";
                str3 = "startType or startCMD error";
            }
        } else {
            str4 = "HiAnalytics/event";
            str3 = "getInfoJson is null";
            com.huawei.hianalytics.g.b.c(str4, str3);
            return null;
        }
    }

    public static JSONObject a(String str, String str2) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("_old_app_version", str2);
            jSONObject.put("_current_app_version", str);
            return jSONObject;
        } catch (JSONException unused) {
            com.huawei.hianalytics.g.b.c("HiAnalytics/event", "Json Exc : app ver error");
            return null;
        }
    }

    public static String b(Context context) {
        Configuration configuration = context.getResources().getConfiguration();
        return (configuration == null || configuration.locale == null) ? "" : configuration.locale.toString();
    }

    public static String b(String str, String str2) {
        return e.b(str, str2);
    }
}
