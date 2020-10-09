package com.alibaba.android.prefetchx;

import android.net.Uri;
import android.os.Looper;
import android.text.TextUtils;
import androidx.annotation.Nullable;
import com.alibaba.aliweex.adapter.module.location.ILocatable;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import mtopsdk.common.util.SymbolExpUtil;

public class PFUtil {
    private static volatile Boolean isDebug;

    public static String getUrlKey(String str) {
        if (TextUtils.isEmpty(str)) {
            return "";
        }
        Uri parse = Uri.parse(str);
        String str2 = parse.getHost() + parse.getPath();
        return (str2.endsWith("\\") || str2.endsWith("/")) ? str2.substring(0, str2.length() - 1) : str2;
    }

    public static String replaceUrlParameter(String str, String str2, String str3) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        if (TextUtils.isEmpty(str2) || TextUtils.isEmpty(str3)) {
            return str;
        }
        try {
            int indexOf = str.indexOf(str2 + SymbolExpUtil.SYMBOL_EQUAL);
            String encode = URLEncoder.encode(str3);
            if (indexOf != -1) {
                sb.append(str.substring(0, indexOf));
                sb.append(str2);
                sb.append(SymbolExpUtil.SYMBOL_EQUAL);
                sb.append(encode);
                int indexOf2 = str.indexOf("&", indexOf);
                if (indexOf2 != -1) {
                    sb.append(str.substring(indexOf2));
                }
                return sb.toString();
            } else if (str.indexOf("?", indexOf) != -1) {
                sb.append(str);
                sb.append("&" + str2 + SymbolExpUtil.SYMBOL_EQUAL + encode);
                return sb.toString();
            } else {
                sb.append(str);
                sb.append("?" + str2 + SymbolExpUtil.SYMBOL_EQUAL + encode);
                return sb.toString();
            }
        } catch (Exception unused) {
            throw new RuntimeException("error in replace jsModuleUrl parameter");
        }
    }

    public static JSONObject getJSCallbackSuccess(String str) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("code", (Object) "WX_SUCCESS");
        jSONObject.put("data", (Object) str);
        return jSONObject;
    }

    public static JSONObject getJSCallbackError(String str, String str2, String str3) {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("data", (Object) str);
        jSONObject.put("code", (Object) "WX_FAILED");
        jSONObject.put("errorCode", (Object) str2);
        jSONObject.put(ILocatable.ERROR_MSG, (Object) str3);
        return jSONObject;
    }

    public static boolean isDebug() {
        if (isDebug != null) {
            return isDebug.booleanValue();
        }
        try {
            isDebug = Boolean.valueOf((PrefetchX.sContext.getApplicationInfo().flags & 2) != 0);
        } catch (Exception unused) {
            isDebug = false;
        }
        return isDebug.booleanValue();
    }

    @Nullable
    public static String s(Object... objArr) {
        if (objArr == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Object append : objArr) {
            sb.append(append);
        }
        return sb.toString();
    }

    public static boolean isUiThread() {
        return Thread.currentThread().getId() == Looper.getMainLooper().getThread().getId();
    }

    public static <T> List<List<T>> averageAssign(List<T> list, int i) {
        List<T> list2;
        ArrayList arrayList = new ArrayList();
        int size = list.size() % i;
        int size2 = list.size() / i;
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            if (size > 0) {
                list2 = list.subList((i3 * size2) + i2, ((i3 + 1) * size2) + i2 + 1);
                size--;
                i2++;
            } else {
                list2 = list.subList((i3 * size2) + i2, ((i3 + 1) * size2) + i2);
            }
            arrayList.add(list2);
        }
        return arrayList;
    }

    public static int compareVersion(String str, String str2) {
        if (str.equals(str2)) {
            return 0;
        }
        String[] split = str.split("\\.");
        String[] split2 = str2.split("\\.");
        int length = split.length;
        int length2 = split2.length;
        int i = length > length2 ? length2 : length;
        int i2 = 0;
        while (i2 < i) {
            if (split[i2].equals(split2[i2])) {
                i2++;
            } else if (Integer.parseInt(split[i2]) > Integer.parseInt(split2[i2])) {
                return 1;
            } else {
                return -1;
            }
        }
        if (length == length2) {
            return 0;
        }
        if (length > length2) {
            return 1;
        }
        return -1;
    }

    public static Object getJSONValueRecursion(Object obj, String str) {
        if (obj instanceof JSONArray) {
            JSONArray jSONArray = (JSONArray) obj;
            for (int i = 0; i < jSONArray.size(); i++) {
                Object jSONValueRecursion = getJSONValueRecursion(jSONArray.get(i), str);
                if (jSONValueRecursion != null) {
                    return jSONValueRecursion;
                }
            }
            return null;
        } else if (!(obj instanceof JSONObject)) {
            return null;
        } else {
            JSONObject jSONObject = (JSONObject) obj;
            for (String obj2 : jSONObject.keySet()) {
                Object obj3 = jSONObject.get(obj2.toString());
                if (obj3 instanceof JSONArray) {
                    Object jSONValueRecursion2 = getJSONValueRecursion((JSONArray) obj3, str);
                    if (jSONValueRecursion2 != null) {
                        return jSONValueRecursion2;
                    }
                } else if (obj3 instanceof JSONObject) {
                    Object jSONValueRecursion3 = getJSONValueRecursion((JSONObject) obj3, str);
                    if (jSONValueRecursion3 != null) {
                        return jSONValueRecursion3;
                    }
                } else if (obj3 != null && String.valueOf(obj3).contains(str)) {
                    return obj3;
                }
            }
            return null;
        }
    }
}
