package com.taobao.vessel.utils;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.mtl.appmonitor.AppMonitor;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class Utils {
    public static final String DOWN_GRADE = "downgrade_url";
    public static final String HTTPS_SCHEMA = "https:";
    public static final String NATIVE = "vessel";
    private static final AtomicInteger NEXT_GENERATED_ID = new AtomicInteger(2000);
    public static final String WH_WEEX = "wh_weex";
    public static final String WH_WEEX_TRUE = "wh_weex=true";
    public static final String WX_TPL = "_wx_tpl";

    public static VesselType getUrlType(String str) {
        if (checkUrlIfIsWeex(str)) {
            return VesselType.Weex;
        }
        if (checkUrlIfIsHttp(str)) {
            return VesselType.Web;
        }
        if (checkUrlIfIsNative(str)) {
            return VesselType.Native;
        }
        return VesselType.Web;
    }

    public static void commitFail(String str, String str2) {
        AppMonitor.Alarm.commitFail("vessel", "vessel", str, str2);
    }

    public static VesselType changeVesselType(VesselType vesselType) {
        if (vesselType == null) {
            return VesselType.Web;
        }
        if (vesselType == VesselType.Native) {
            return VesselType.Weex;
        }
        return vesselType == VesselType.Weex ? VesselType.Web : vesselType;
    }

    public static String parseUrlDowngradeParamter(String str) {
        if (!checkUrlParamterIfExist(str, DOWN_GRADE)) {
            return null;
        }
        Uri parse = Uri.parse(str);
        Uri.Builder buildUpon = Uri.parse(parse.getQueryParameter(DOWN_GRADE)).buildUpon();
        Set<String> queryParameterNames = parse.getQueryParameterNames();
        if (queryParameterNames != null && queryParameterNames.size() > 0) {
            for (String next : queryParameterNames) {
                if (!"_wx_tpl".equals(next)) {
                    buildUpon.appendQueryParameter(next, parse.getQueryParameter(next));
                }
            }
        }
        return buildUpon.toString();
    }

    public static String getWeexTemplateUrl(String str) {
        return (str != null && checkUrlIfIsWeex(str)) ? addWeexWxtplParameter(str) : str;
    }

    public static int generateViewId() {
        int i;
        int i2;
        do {
            i = NEXT_GENERATED_ID.get();
            i2 = i + 1;
            if (i2 > 16777215) {
                i2 = 1;
            }
        } while (!NEXT_GENERATED_ID.compareAndSet(i, i2));
        return i;
    }

    public static String mapToString(Object obj) {
        if (obj == null) {
            return VesselConstants.DEFAULT_URL;
        }
        Map hashMap = new HashMap();
        if (obj instanceof Map) {
            hashMap = (Map) obj;
        }
        JSONObject jSONObject = new JSONObject();
        for (String str : hashMap.keySet()) {
            jSONObject.put(str, hashMap.get(str));
        }
        return JSON.toJSONString(jSONObject);
    }

    public static String getClassName(String str) {
        if (str == null) {
            return str;
        }
        Uri parse = Uri.parse(str);
        if (parse != null) {
            return parse.getQueryParameter("classname");
        }
        return null;
    }

    public static String getBundleName(String str) {
        if (str == null) {
            return str;
        }
        Uri parse = Uri.parse(str);
        if (parse != null) {
            return parse.getHost();
        }
        return null;
    }

    private static boolean checkIfContainsSchema(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return !TextUtils.isEmpty(Uri.parse(str).getScheme());
    }

    private static String urlAddSchema(String str) {
        if (TextUtils.isEmpty(str) || !str.startsWith("//")) {
            return str;
        }
        return HTTPS_SCHEMA + str;
    }

    private static boolean checkUrlIfIsWeex(String str) {
        if (!checkUrlIfIsHttp(str)) {
            return false;
        }
        if (checkUrlParamterIfExist(str, "_wx_tpl") || str.contains(WH_WEEX_TRUE)) {
            return true;
        }
        return false;
    }

    private static boolean checkUrlIfIsNative(String str) {
        if (!checkIfContainsSchema(str)) {
            return false;
        }
        Uri parse = Uri.parse(str);
        if (parse.getScheme() == null || !"vessel".equals(parse.getScheme())) {
            return false;
        }
        return true;
    }

    private static String addWeexWxtplParameter(String str) {
        if (TextUtils.isEmpty(str)) {
            return VesselConstants.DEFAULT_URL;
        }
        Uri parse = Uri.parse(str);
        if (!TextUtils.isEmpty(parse.getQueryParameter("_wx_tpl"))) {
            Uri.Builder buildUpon = Uri.parse(parse.getQueryParameter("_wx_tpl")).buildUpon();
            Set<String> queryParameterNames = parse.getQueryParameterNames();
            if (queryParameterNames != null && queryParameterNames.size() > 0) {
                for (String next : queryParameterNames) {
                    if (!"_wx_tpl".equals(next)) {
                        buildUpon.appendQueryParameter(next, parse.getQueryParameter(next));
                    }
                }
            }
            return buildUpon.toString();
        } else if (TextUtils.equals("true", parse.getQueryParameter("wh_weex"))) {
            return parse.toString();
        } else {
            return null;
        }
    }

    private static boolean checkUrlParamterIfExist(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Uri parse = Uri.parse(str);
        if (parse.isHierarchical()) {
            return !TextUtils.isEmpty(parse.getQueryParameter(str2));
        }
        return false;
    }

    private static boolean checkUrlIfIsHttp(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (!checkIfContainsSchema(str)) {
            str = urlAddSchema(str);
        }
        Uri parse = Uri.parse(str);
        if ("http".equals(parse.getScheme()) || "https".equals(parse.getScheme())) {
            return true;
        }
        return false;
    }

    private static boolean checkUrlIfIsFilePath(String str) {
        if (!TextUtils.isEmpty(str) && "file".equals(Uri.parse(str).getScheme())) {
            return true;
        }
        return false;
    }

    public static String getPageNameFromOptions(Map<String, Object> map) {
        String str = "";
        if (map != null && !map.isEmpty()) {
            str = (String) map.get("bundleUrl");
        }
        return TextUtils.isEmpty(str) ? VesselConstants.DEFAULT_URL : str;
    }

    public static boolean checkActivityDestroy(Context context) {
        if (context == null || !(context instanceof Activity)) {
            return true;
        }
        Activity activity = (Activity) context;
        if (activity.isFinishing()) {
            return true;
        }
        if (Build.VERSION.SDK_INT < 17 || !activity.isDestroyed()) {
            return false;
        }
        return true;
    }
}
