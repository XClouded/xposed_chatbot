package alimama.com.unwbase.tools;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConvertUtils {
    public static Map<String, String> json2map(JSONObject jSONObject) {
        HashMap hashMap = new HashMap();
        if (jSONObject != null) {
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                String optString = jSONObject.optString(next);
                if (!TextUtils.isEmpty(optString)) {
                    hashMap.put(next, optString);
                }
            }
        }
        return hashMap;
    }

    public static Map<String, String> fastjson2map(com.alibaba.fastjson.JSONObject jSONObject) {
        HashMap hashMap = new HashMap();
        if (jSONObject != null) {
            for (String next : jSONObject.keySet()) {
                String string = jSONObject.getString(next);
                if (!TextUtils.isEmpty(string)) {
                    hashMap.put(next, string);
                }
            }
        }
        return hashMap;
    }

    public static boolean str2boolean(String str) {
        return TextUtils.equals(str, "true") || TextUtils.equals(str, "1");
    }

    public static Bundle json2bundle(JSONObject jSONObject) {
        Bundle bundle = new Bundle();
        if (jSONObject != null) {
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                String optString = jSONObject.optString(next);
                if (!TextUtils.isEmpty(optString)) {
                    bundle.putString(next, optString);
                }
            }
        }
        return bundle;
    }

    public static List<String> jsonarry2list(JSONArray jSONArray) {
        ArrayList arrayList = new ArrayList();
        if (jSONArray != null) {
            for (int i = 0; i < jSONArray.length(); i++) {
                try {
                    arrayList.add(jSONArray.getString(i));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return arrayList;
    }

    public static long getSafeLongValue(String str) {
        return getSafeLongValue(str, 0);
    }

    public static long getSafeLongValue(String str, long j) {
        try {
            return Long.valueOf(str).longValue();
        } catch (Exception unused) {
            return j;
        }
    }

    public static boolean getSafeBoolValue(String str) {
        return getSafeBoolValue(str, false);
    }

    public static boolean getSafeBoolValue(String str, boolean z) {
        try {
            return Boolean.valueOf(str).booleanValue();
        } catch (Exception unused) {
            return z;
        }
    }

    public static int getSafeIntValue(String str) {
        return getSafeIntValue(str, 0);
    }

    public static int getSafeIntValue(String str, int i) {
        try {
            return Integer.valueOf(str).intValue();
        } catch (Exception unused) {
            return i;
        }
    }

    public static double getSafeDoubleValue(String str) {
        return getSafeDoubleValue(str, 0.0d);
    }

    public static double getSafeDoubleValue(String str, double d) {
        try {
            return Double.valueOf(str).doubleValue();
        } catch (Exception unused) {
            return d;
        }
    }

    public static String addSpm(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2) || !Uri.parse(str).isHierarchical()) {
            return str;
        }
        Uri parse = Uri.parse(str);
        String query = parse.getQuery();
        String queryParameter = parse.getQueryParameter("spm");
        StringBuilder sb = new StringBuilder();
        sb.append(TextUtils.isEmpty(query) ? "?" : "&");
        sb.append(str2);
        String sb2 = sb.toString();
        if (!TextUtils.isEmpty(queryParameter)) {
            sb2 = "";
        }
        return str + sb2;
    }

    public static ByteArrayOutputStream readAssertByte(Context context, String str) {
        try {
            if (str.startsWith(File.separator)) {
                str = str.substring(File.separator.length());
            }
            DataInputStream dataInputStream = new DataInputStream(context.getAssets().open(str));
            byte[] bArr = new byte[dataInputStream.available()];
            dataInputStream.readFully(bArr);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byteArrayOutputStream.write(bArr);
            dataInputStream.close();
            return byteArrayOutputStream;
        } catch (Exception unused) {
            return null;
        }
    }

    @NonNull
    public static Map<String, String> array2Map(String... strArr) {
        HashMap hashMap = new HashMap();
        if (strArr.length > 0 && strArr.length % 2 == 0) {
            int i = 0;
            while (true) {
                int i2 = i + 1;
                if (i2 >= strArr.length) {
                    break;
                }
                hashMap.put(strArr[i], strArr[i2]);
                i += 2;
            }
        }
        return hashMap;
    }
}
