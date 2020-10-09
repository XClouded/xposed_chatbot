package com.huawei.hianalytics.log.e;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import com.huawei.hianalytics.a.b;
import com.huawei.hianalytics.a.d;
import com.huawei.hianalytics.c.c;
import com.huawei.hianalytics.d.a;
import com.taobao.accs.common.Constants;
import com.taobao.weex.devtools.debug.WXDebugConstants;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

public class e {
    public static String a(Context context) {
        String b = b(context);
        String str = Build.MODEL;
        String str2 = Build.DISPLAY;
        String b2 = c.b();
        String str3 = Build.VERSION.RELEASE;
        String h = d.h();
        return String.format(Locale.getDefault(), "%s=%s&%s=%s&%s=%s&%s=%s&%s=%s&%s=%s", new Object[]{"shaSN", b, Constants.KEY_MODEL, str, "romVersion", str2, "emuiVersion", b2, WXDebugConstants.ENV_OS_VERSION, str3, "countryCode", h});
    }

    public static String a(Context context, boolean z, boolean z2) {
        StringBuilder sb;
        String str;
        String str2;
        String format = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        String c = a.a().c();
        if (TextUtils.isEmpty(c)) {
            c = b.i();
            if (TextUtils.isEmpty(c)) {
                c = c(context);
                b.c(c);
            }
        }
        String a = b.a(c);
        if (z) {
            sb = new StringBuilder();
            sb.append("/Eventid_");
            sb.append(a);
            sb.append("_");
            sb.append(format);
            str2 = "_ALL.zip";
        } else {
            if (z2) {
                sb = new StringBuilder();
                str = "/Crash_";
            } else {
                sb = new StringBuilder();
                str = "/Eventid_";
            }
            sb.append(str);
            sb.append(a);
            sb.append("_");
            sb.append(format);
            str2 = ".zip";
        }
        sb.append(str2);
        return sb.toString();
    }

    public static String a(String str, String str2) {
        JSONObject jSONObject;
        try {
            jSONObject = new JSONObject(str2);
        } catch (JSONException unused) {
            com.huawei.hianalytics.g.b.b("LogStringUtil", "collectErrorLog() MetaData is not a JSON format!");
            jSONObject = new JSONObject();
            try {
                jSONObject.put(str, str2);
            } catch (JSONException unused2) {
                com.huawei.hianalytics.g.b.c("LogStringUtil", "checkMetaMsg() An exception occurred in json.put ");
            }
        }
        return jSONObject.toString();
    }

    public static String b(Context context) {
        return b.a(c(context));
    }

    public static String c(Context context) {
        SharedPreferences a = c.a(context, "global_v2");
        if (a == null) {
            return UUID.randomUUID().toString();
        }
        String str = (String) c.b(a, "uuid", "");
        if (!TextUtils.isEmpty(str)) {
            return str;
        }
        String replace = UUID.randomUUID().toString().replace("-", "");
        a.edit().putString("uuid", replace).apply();
        return replace;
    }
}
