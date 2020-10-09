package com.taobao.ju.track.impl;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.taobao.ju.track.constants.Constants;
import com.taobao.ju.track.csv.CsvFileUtil;
import com.taobao.ju.track.impl.interfaces.IPageTrack;
import com.taobao.ju.track.interfaces.IJPageTrackProvider;
import com.taobao.ju.track.util.BundleUtil;
import com.taobao.ju.track.util.JsonUtil;
import com.taobao.ju.track.util.ParamUtil;
import com.taobao.ju.track.util.UriUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

public class PageTrackImpl extends TrackImpl implements IPageTrack {
    private static final String DEFAULT_FILE_NAME = "ut_page.csv";
    private static final String EMPTY_PAGE_NAME = "NullActivity";
    private static final String PARAM_INTERNAL_ARGS = "_args";

    public PageTrackImpl(Context context) {
        super(context, DEFAULT_FILE_NAME);
    }

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public PageTrackImpl(Context context, String str) {
        super(context, !CsvFileUtil.isCSV(str) ? DEFAULT_FILE_NAME : str);
    }

    public String getPageName(String str) {
        String paramValue = str != null ? getParamValue(str, Constants.CSV_PARAM_INTERNAL_KEY, str) : null;
        return paramValue != null ? paramValue : EMPTY_PAGE_NAME;
    }

    public String getPageName(Activity activity) {
        String pageName = activity instanceof IJPageTrackProvider ? ((IJPageTrackProvider) activity).getPageName() : null;
        if (activity != null) {
            String simpleName = activity.getClass().getSimpleName();
            if (pageName != null) {
                simpleName = pageName;
            }
            pageName = getParamValue(simpleName, Constants.CSV_PARAM_INTERNAL_KEY, simpleName);
        }
        return pageName != null ? pageName : EMPTY_PAGE_NAME;
    }

    public String getSpm(String str) {
        Map<String, String> paramMap = getParamMap(str);
        if (paramMap == null || paramMap.size() == 0) {
            paramMap = getParamMap(getPageName(str));
        }
        return super.getSpm(paramMap);
    }

    public String getSpm(Activity activity) {
        return getSpm(getPageName(activity));
    }

    public Properties getSpmAsProp(Activity activity) {
        Properties properties = new Properties();
        if (activity != null) {
            String pageName = getPageName(activity);
            Intent intent = activity.getIntent();
            if (ParamUtil.hasExtra(intent, "spm")) {
                properties.put("spm", ParamUtil.getStringExtra(intent, "spm"));
            }
            properties.put(Constants.PARAM_OUTER_SPM_CNT, getSpm(pageName));
        }
        return properties;
    }

    public HashMap<String, String> getSpmAsMap(Activity activity) {
        HashMap<String, String> hashMap = new HashMap<>();
        if (activity != null) {
            String pageName = getPageName(activity);
            Intent intent = activity.getIntent();
            if (ParamUtil.hasExtra(intent, "spm")) {
                hashMap.put("spm", ParamUtil.getStringExtra(intent, "spm"));
            }
            hashMap.put(Constants.PARAM_OUTER_SPM_CNT, getSpm(pageName));
        }
        return hashMap;
    }

    public String getSpmAB(String str) {
        Map<String, String> paramMap = getParamMap(str);
        if (paramMap == null || paramMap.size() == 0) {
            paramMap = getParamMap(getPageName(str));
        }
        return super.getSpmAB(paramMap);
    }

    public String getSpmAB(Activity activity) {
        return getSpmAB(getPageName(activity));
    }

    public String getArgs(String str) {
        String paramValue = getParamValue(str, PARAM_INTERNAL_ARGS);
        return paramValue == null ? getParamValue(getPageName(str), PARAM_INTERNAL_ARGS) : paramValue;
    }

    public String getArgs(Activity activity) {
        if (activity != null) {
            return getArgs(activity.getClass().getSimpleName());
        }
        return null;
    }

    public Map<String, String> getArgsMap(String str, Bundle bundle) {
        return doBundleValueMapping(JsonUtil.jsonToMap(getArgs(str)), bundle);
    }

    public Map<String, String> getArgsMap(Activity activity, Bundle bundle) {
        return doBundleValueMapping(JsonUtil.jsonToMap(getArgs(activity)), bundle);
    }

    public Map<String, String> getArgsMap(String str, Uri uri) {
        return doBundleValueMapping(JsonUtil.jsonToMap(getArgs(str)), uri);
    }

    public Map<String, String> getArgsMap(Activity activity, Uri uri) {
        return doBundleValueMapping(JsonUtil.jsonToMap(getArgs(activity)), uri);
    }

    private Map<String, String> doBundleValueMapping(Map<String, String> map, Bundle bundle) {
        if (map != null && map.size() > 0) {
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry next = it.next();
                String str = (String) next.getKey();
                String str2 = (String) next.getValue();
                if (!TextUtils.isEmpty(str2) && str2.startsWith("${") && str2.endsWith("}") && str2.length() > 2) {
                    String string = BundleUtil.getString(bundle, str2.substring(2, str2.length() - 1), "");
                    if (!TextUtils.isEmpty(string)) {
                        map.put(str, string);
                    } else {
                        it.remove();
                    }
                }
            }
        }
        return map;
    }

    private Map<String, String> doBundleValueMapping(Map<String, String> map, Uri uri) {
        if (map != null && map.size() > 0) {
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry next = it.next();
                String str = (String) next.getKey();
                String str2 = (String) next.getValue();
                if (!TextUtils.isEmpty(str2) && str2.startsWith("${") && str2.endsWith("}") && str2.length() > 2) {
                    String string = UriUtil.getString(uri, str2.substring(2, str2.length() - 1), "");
                    if (!TextUtils.isEmpty(string)) {
                        map.put(str, string);
                    } else {
                        it.remove();
                    }
                }
            }
        }
        return map;
    }

    @Deprecated
    private String getDefaultPageName(Object obj) {
        if (obj == null) {
            return EMPTY_PAGE_NAME;
        }
        String valueOf = obj instanceof String ? String.valueOf(obj) : obj.getClass().getSimpleName();
        return (valueOf == null || !valueOf.toLowerCase().endsWith("activity")) ? valueOf : valueOf.substring(0, valueOf.length() - 8);
    }
}
