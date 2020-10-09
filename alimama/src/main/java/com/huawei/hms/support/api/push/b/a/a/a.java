package com.huawei.hms.support.api.push.b.a.a;

import android.content.Context;
import android.taobao.windvane.config.WVConfigManager;
import android.text.TextUtils;
import androidx.core.app.NotificationCompat;
import com.huawei.hms.api.HuaweiApiAvailability;
import com.huawei.hms.support.api.client.ApiClient;
import com.huawei.hms.support.api.client.SubAppInfo;
import com.taobao.tao.image.Logger;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/* compiled from: BaseUtil */
public abstract class a {
    private static final char[] a = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', Logger.LEVEL_D, Logger.LEVEL_E, 'F'};

    public static JSONArray a(String str) {
        if (TextUtils.isEmpty(str)) {
            com.huawei.hms.support.log.a.a("BaseUtil", "jsonString is null");
            return null;
        }
        try {
            return new JSONArray(str);
        } catch (JSONException unused) {
            com.huawei.hms.support.log.a.a("BaseUtil", "cast jsonString to jsonArray error");
            return null;
        }
    }

    public static String a(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        if (bArr.length == 0) {
            return "";
        }
        char[] cArr = new char[(bArr.length * 2)];
        for (int i = 0; i < bArr.length; i++) {
            byte b = bArr[i];
            int i2 = i * 2;
            cArr[i2] = a[(b & 240) >> 4];
            cArr[i2 + 1] = a[b & 15];
        }
        return new String(cArr);
    }

    public static byte[] b(String str) {
        byte[] bArr = new byte[(str.length() / 2)];
        try {
            byte[] bytes = str.getBytes("UTF-8");
            for (int i = 0; i < bArr.length; i++) {
                StringBuilder sb = new StringBuilder();
                sb.append("0x");
                int i2 = i * 2;
                sb.append(new String(new byte[]{bytes[i2]}, "UTF-8"));
                bArr[i] = (byte) (((byte) (Byte.decode(sb.toString()).byteValue() << 4)) ^ Byte.decode("0x" + new String(new byte[]{bytes[i2 + 1]}, "UTF-8")).byteValue());
            }
        } catch (UnsupportedEncodingException e) {
            com.huawei.hms.support.log.a.a("BaseUtil", "hexString2ByteArray error" + e.getMessage());
        }
        return bArr;
    }

    public static JSONArray a(List<String> list, Context context) throws JSONException {
        JSONArray jSONArray = new JSONArray();
        c cVar = new c(context, "tags_info");
        for (String next : list) {
            if (cVar.c(next)) {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("tagKey", next);
                jSONObject.put("opType", 2);
                if (jSONObject.length() > 0) {
                    jSONArray.put(jSONObject);
                }
            } else {
                com.huawei.hms.support.log.a.a("BaseUtil", next + " not exist, need not to remove");
            }
        }
        return jSONArray;
    }

    public static void a(ApiClient apiClient, String str) {
        if (!com.huawei.hms.support.b.a.a().b() && apiClient != null) {
            HashMap hashMap = new HashMap();
            hashMap.put(WVConfigManager.CONFIGNAME_PACKAGE, apiClient.getPackageName());
            hashMap.put("sdk_ver", String.valueOf(HuaweiApiAvailability.HMS_SDK_VERSION_CODE));
            String str2 = null;
            SubAppInfo subAppInfo = apiClient.getSubAppInfo();
            if (subAppInfo != null) {
                str2 = subAppInfo.getSubAppID();
            }
            if (str2 == null) {
                str2 = apiClient.getAppID();
            }
            hashMap.put("app_id", str2);
            String[] split = str.split("\\.");
            if (split.length == 2) {
                hashMap.put(NotificationCompat.CATEGORY_SERVICE, split[0]);
                hashMap.put("api_name", split[1]);
            }
            hashMap.put("result", String.valueOf(0));
            hashMap.put("cost_time", String.valueOf(0));
            com.huawei.hms.support.b.a.a().a(apiClient.getContext(), "HMS_SDK_API_CALL", (Map<String, String>) hashMap);
        }
    }
}
