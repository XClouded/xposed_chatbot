package com.alibaba.aliweex.utils;

import android.net.Uri;
import android.taobao.windvane.config.WVServerConfig;
import android.text.TextUtils;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.IConfigAdapter;
import com.alibaba.aliweex.bundle.UrlValidate;
import com.alimama.unionwl.utils.CommonUtils;
import com.taobao.vessel.utils.Utils;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.performance.WXInstanceApm;
import com.taobao.weex.utils.WXLogUtils;
import java.net.URLDecoder;

public class WXUriUtil {
    public static String handleUTPageNameScheme(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                return "default";
            }
            Uri.Builder buildUpon = Uri.parse(str).buildUpon();
            buildUpon.scheme("");
            return buildUpon.toString().replaceFirst("^(/|://|:/|//)", "");
        } catch (Exception e) {
            WXLogUtils.e("pageNameError", (Throwable) e);
            return str;
        }
    }

    public static String getRealNameFromNameOrUrl(String str, boolean z) {
        try {
            String dealUrl = str.startsWith("http") ? dealUrl(str, z) : str;
            if (dealUrl != null) {
                return dealUrl;
            }
            int i = 0;
            if (str.startsWith(Utils.HTTPS_SCHEMA)) {
                i = 8;
            } else if (str.startsWith(CommonUtils.HTTP_PRE)) {
                i = 7;
            }
            return str.substring(i);
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    private static String dealUrl(String str, boolean z) {
        String str2;
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            Uri parse = Uri.parse(URLDecoder.decode(str, "utf-8"));
            String queryParameter = parse.getQueryParameter("_wx_tpl");
            if (queryParameter == null) {
                queryParameter = parse.getQueryParameter("weex_original_url");
            }
            if (queryParameter != null) {
                parse = Uri.parse(queryParameter);
            }
            if (z) {
                str2 = parse.getHost() + parse.getPath();
            } else {
                str2 = parse.toString();
            }
            return str2;
        } catch (Exception e) {
            e.printStackTrace();
            return str;
        }
    }

    public static void reportWPLHost(WXSDKInstance wXSDKInstance, String str) {
        WXInstanceApm apmForInstance;
        IConfigAdapter configAdapter;
        if (wXSDKInstance != null && !TextUtils.isEmpty(str) && (apmForInstance = wXSDKInstance.getApmForInstance()) != null && (configAdapter = AliWeex.getInstance().getConfigAdapter()) != null) {
            if (!"true".equals(configAdapter.getConfig("android_weex_ext_config", "enableReportUnsafeUrl", "true"))) {
                WXLogUtils.d("reportUnsafeHost shutdown");
                return;
            }
            boolean isUrlValid = UrlValidate.isUrlValid(str);
            if (!isUrlValid) {
                WXLogUtils.d("unsafe_url :" + str);
                apmForInstance.addProperty("trusted", Boolean.toString(isUrlValid));
                apmForInstance.addProperty("check_pattern", WVServerConfig.DOMAIN_PATTERN);
                apmForInstance.addProperty("unsafe_url", str);
            }
        }
    }
}
