package com.alibaba.ut.biz;

import alimama.com.unweventparse.constants.EventConstants;
import alimama.com.unwrouter.UNWRouter;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.analytics.core.config.UTTPKItem;
import com.alibaba.ut.utils.Logger;
import com.ut.mini.UTAnalytics;
import com.ut.mini.internal.UTOriginalCustomHitBuilder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class UTAdpater {
    private static String globalProperty;
    private static Map<String, JSONObject> mAplusParam = new HashMap();
    private static Map<String, Object> mDeviceInfo = new HashMap();
    private static Map<String, Object> mParam = new HashMap();

    public static void setAplus4UT(Context context) {
    }

    public static void pageAppear(Context context, String str) {
        Logger.e("params", str);
        try {
            Map<String, String> StringToMap = StringToMap(str);
            if (context != null) {
                if ("true".equals(StringToMap.get("isSPA"))) {
                    UTAnalytics.getInstance().getDefaultTracker().pageAppearDonotSkip(context);
                }
                UTAnalytics.getInstance().getDefaultTracker().pageAppearDonotSkip(context);
            } else {
                Logger.e((String) null, "cannot get pageObject");
            }
            handlePageRefresh(context, StringToMap);
            handleSetTagForUT4Aplus(context);
        } catch (Throwable th) {
            Log.e("ut4aplus", "", th);
        }
    }

    public static void pageDisAppear(Context context, String str) {
        Logger.e((String) null, "params", str);
        try {
            new JSONObject(str);
        } catch (Exception e) {
            Log.e("ut4aplus", "", e);
        }
        UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(context);
    }

    public static void updatePageProperties(Context context, String str) {
        Logger.e((String) null, "params", str);
        try {
            UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(context, StringToMap(str));
        } catch (Throwable th) {
            Log.e("ut4aplus", "", th);
        }
    }

    private static String getPageName(Map<String, String> map) {
        String str = null;
        if (map == null) {
            return null;
        }
        String str2 = map.get("_h5url");
        if (!TextUtils.isEmpty(str2)) {
            int indexOf = str2.indexOf("?");
            str = indexOf != 1 ? str2.substring(0, indexOf) : str2;
        }
        String str3 = map.get("urlpagename");
        return !TextUtils.isEmpty(str3) ? str3 : str;
    }

    public static void updateNextPageProperties(String str) {
        Logger.e((String) null, "params", str);
        try {
            UTAnalytics.getInstance().getDefaultTracker().updateNextPageProperties(StringToMap(str));
        } catch (Throwable th) {
            Log.e("ut4aplus", "", th);
        }
    }

    public static void updatePageUtparam(Context context, String str) {
        Logger.d((String) null, "params", str);
        try {
            UTAnalytics.getInstance().getDefaultTracker().updatePageUtparam(context, str);
        } catch (Throwable th) {
            Log.e("ut4aplus", "", th);
        }
    }

    public static void updateNextPageUtparam(String str) {
        Logger.e((String) null, "params", str);
        try {
            UTAnalytics.getInstance().getDefaultTracker().updateNextPageUtparam(str);
        } catch (Throwable th) {
            Log.e("ut4aplus", "", th);
        }
    }

    public static void setAplusParams(String str, String str2) {
        try {
            mAplusParam.put(str, new JSONObject(str2));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static Object getAplusParams(String str) {
        return mAplusParam.get(str);
    }

    public static Map getParam() {
        return mParam;
    }

    public static void removeAplusParams(String str) {
        mAplusParam.remove(str);
    }

    public static Map getDeviceInfo() {
        return mDeviceInfo;
    }

    public static Map<String, String> StringToMap(String str) {
        HashMap hashMap = new HashMap();
        try {
            JSONObject jSONObject = new JSONObject(str);
            Iterator<String> keys = jSONObject.keys();
            while (keys.hasNext()) {
                String next = keys.next();
                if (jSONObject.get(next) != null) {
                    hashMap.put(next, jSONObject.get(next) + "");
                } else {
                    hashMap.put(next, "");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return hashMap;
    }

    public static void updatePageName(Context context, String str) {
        Logger.e((String) null, "params", str);
        try {
            UTAnalytics.getInstance().getDefaultTracker().updatePageName(context, StringToMap(str).get(UNWRouter.PAGE_NAME));
        } catch (Throwable th) {
            Log.e("ut4aplus", "", th);
        }
    }

    public static void updatePageURL(Context context, String str) {
        Logger.e((String) null, "params", str);
        try {
            String str2 = StringToMap(str).get("pageURL");
            if (!TextUtils.isEmpty(str2)) {
                UTAnalytics.getInstance().getDefaultTracker().updatePageUrl(context, Uri.parse(str2));
            }
        } catch (Throwable th) {
            Log.e("ut4aplus", "", th);
        }
    }

    private static String mapToJson(Map<String, Object> map) {
        return new JSONObject(map).toString();
    }

    private static void handlePageRefresh(Context context, Map<String, String> map) {
        try {
            if (map.containsKey("isRefresh")) {
                HashMap hashMap = new HashMap(1);
                hashMap.put("_h5_refresh", "1");
                UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(context, hashMap);
            }
        } catch (Throwable th) {
            Log.e("ut4aplus", "", th);
        }
    }

    private static void handleSetTagForUT4Aplus(Context context) {
        try {
            HashMap hashMap = new HashMap(1);
            hashMap.put("ut4aplus", "1");
            UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(context, hashMap);
        } catch (Throwable th) {
            Log.e("ut4aplus", "", th);
        }
    }

    private static void handlePointParam(Context context, Map<String, String> map) {
        if (map.containsKey("point")) {
            HashMap hashMap = new HashMap(1);
            hashMap.put("issb", "1");
            UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(context, hashMap);
        }
    }

    public static Object getPageSpmUrl(Context context) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("pageSpmUrl", UTAnalytics.getInstance().getDefaultTracker().getPageSpmUrl((Activity) context));
        } catch (Exception e) {
            Log.e("ut4aplus", "", e);
        }
        return jSONObject;
    }

    public static Object getPageSpmPre(Context context) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("pageSpmPre", UTAnalytics.getInstance().getDefaultTracker().getPageSpmPre((Activity) context));
        } catch (Exception e) {
            Log.e("ut4aplus", "", e);
        }
        return jSONObject;
    }

    public static void utCustomEvent(String str) {
        Map hashMap;
        try {
            Map<String, String> StringToMap = StringToMap(str);
            if (StringToMap != null) {
                String remove = StringToMap.remove(UNWRouter.PAGE_NAME);
                int intValue = Integer.valueOf(StringToMap.remove(EventConstants.EVENTID) + "").intValue();
                String remove2 = StringToMap.remove(EventConstants.UT.ARG1);
                String remove3 = StringToMap.remove("arg2");
                String remove4 = StringToMap.remove("arg3");
                String remove5 = StringToMap.remove("args");
                if (!TextUtils.isEmpty(remove5)) {
                    hashMap = StringToMap(remove5);
                } else {
                    hashMap = new HashMap();
                }
                UTAnalytics.getInstance().getDefaultTracker().send(new UTOriginalCustomHitBuilder(remove, intValue, remove2, remove3, remove4, hashMap).build());
            }
        } catch (Throwable th) {
            Log.e("ut4aplus", "", th);
        }
    }

    public static void turnOnRealTimeDebug(String str) {
        UTAnalytics.getInstance().turnOnRealTimeDebug(StringToMap(str));
    }

    public static void userRegister(String str) {
        UTAnalytics.getInstance().userRegister(StringToMap(str).get("userNick"));
    }

    public static void updateUserAccount(String str) {
        Map<String, String> StringToMap = StringToMap(str);
        UTAnalytics.getInstance().updateUserAccount(StringToMap.get("userNick"), StringToMap.get("userId"), StringToMap.get("openId"));
    }

    public static void addTPKItem(String str) {
        Map<String, String> StringToMap = StringToMap(str);
        if (StringToMap != null && !StringToMap.isEmpty()) {
            UTTPKItem uTTPKItem = new UTTPKItem();
            uTTPKItem.setKname(StringToMap.get("kn"));
            uTTPKItem.setKvalue(StringToMap.get("v"));
            uTTPKItem.setType(StringToMap.get(UTTPKItem.TYPE_FAR));
            UTAnalytics.getInstance().getDefaultTracker().addTPKItem(uTTPKItem);
        }
    }

    public static void updateSessionProperties(String str) {
        UTAnalytics.getInstance().updateSessionProperties(StringToMap(str));
    }

    public static void setGlobalProperty(String str) {
        Map<String, String> StringToMap = StringToMap(str);
        if (StringToMap != null && !StringToMap.isEmpty()) {
            for (String next : StringToMap.keySet()) {
                UTAnalytics.getInstance().getDefaultTracker().setGlobalProperty(next, StringToMap.get(next));
            }
        }
    }
}
