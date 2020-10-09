package com.alimama.moon.utils;

import android.net.Uri;
import android.text.TextUtils;
import com.taobao.ju.track.constants.Constants;
import com.taobao.vessel.utils.Utils;
import com.ut.mini.UTAnalytics;
import java.util.HashMap;
import java.util.Map;

public class SpmProcessor {
    public static String createPageRouterUrl(String str, Map<String, String> map) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        String str2 = "unionApp://" + str;
        if (map == null || map.size() == 0) {
            return str2;
        }
        Uri parse = Uri.parse(str2);
        for (Map.Entry next : map.entrySet()) {
            parse = Helper.appendQueryParameter(parse, (String) next.getKey(), (String) next.getValue());
        }
        return parse.toString();
    }

    public static String getDestUrl(String str, String str2, boolean z) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        if (str.startsWith("//")) {
            str = Utils.HTTPS_SCHEMA + str;
        }
        if (z) {
            return appendSpm(str, str2);
        }
        return (!TextUtils.isEmpty(UnionLensUtil.getUrlParameter(str, "spm")) || TextUtils.isEmpty(str2)) ? str : appendSpm(str, str2);
    }

    public static String appendSpm(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        return Helper.appendQueryParameter(Uri.parse(str), "spm", str2).toString();
    }

    public static void doNotSkipPage(Object obj, String str, String str2) {
        UTAnalytics.getInstance().getDefaultTracker().pageAppearDonotSkip(obj, str);
        HashMap hashMap = new HashMap();
        if (!TextUtils.isEmpty(str2)) {
            hashMap.put(Constants.PARAM_OUTER_SPM_CNT, str2);
        }
        if (UnionLensUtil.isUnionLensReport()) {
            hashMap.put(UnionLensUtil.UNION_LENS_LOG, UnionLensUtil.appendNaUnionLens(UTAnalytics.getInstance().getDefaultTracker().getPageProperties(obj)));
        }
        UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(obj, hashMap);
    }

    public static void pageAppear(Object obj, String str) {
        UTAnalytics.getInstance().getDefaultTracker().pageAppear(obj, str);
    }

    public static void pageDisappear(Object obj, String str) {
        HashMap hashMap = new HashMap();
        if (!TextUtils.isEmpty(str)) {
            hashMap.put(Constants.PARAM_OUTER_SPM_CNT, str);
        }
        if (UnionLensUtil.isUnionLensReport()) {
            hashMap.put(UnionLensUtil.UNION_LENS_LOG, UnionLensUtil.appendNaUnionLens(UTAnalytics.getInstance().getDefaultTracker().getPageProperties(obj)));
            hashMap.put("selfPvid", UnionLensUtil.prePvid);
            hashMap.put("prePvid", UnionLensUtil.lastPrePvid);
        }
        UTAnalytics.getInstance().getDefaultTracker().updatePageProperties(obj, hashMap);
        UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(obj);
    }

    public static void updateNextPageProperties(String str, Map<String, String> map) {
        if (!TextUtils.isEmpty(str)) {
            if (map == null) {
                map = new HashMap<>();
            }
            map.put("spm-url", str);
            UTAnalytics.getInstance().getDefaultTracker().updateNextPageProperties(map);
        }
    }
}
