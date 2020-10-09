package com.alibaba.aliweex.bundle;

import android.net.Uri;
import android.taobao.windvane.config.WVServerConfig;
import android.text.TextUtils;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.IConfigAdapter;
import com.taobao.weex.utils.WXLogUtils;

public class UrlValidate {
    public static final String CONFIG_GROUP_URL_CHECK_SWITCH = "url_check_switch";
    public static final String CONFIG_GROUP_WEEX_BLACKURL_GROUP = "weex_config_render_black_urls";
    public static final String CONFIG_GROUP_WEEX_WHITEURL_GROUP = "weex_config_render_white_urls";
    public static final String CONFIG_GROUP_WEEX_WHITE_SCHEME_GROUP = "weex_config_render_white_schema";
    public static final String CONFIG_KEY_IS_CHECK = "is_check";
    public static final String CONFIG_KEY_IS_RENDER = "is_render";
    public static final String CONFIG_KEY_WEEX_BLACKURL = "blackurl";
    public static final String CONFIG_KEY_WEEX_WHITEURL = "whiteurl";
    public static final String CONFIG_KEY_WEEX_WHITE_SCHEME = "white_schema";

    public static boolean checkUrl(String str, String str2) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (TextUtils.isEmpty(str2)) {
            WXLogUtils.d("checkUrl expectedUrls is empty true");
            return false;
        }
        WXLogUtils.d("checkUrl hostUrl is " + str + "expectedUrls is " + str2);
        if (str2.startsWith("*")) {
            return str.endsWith(str2.substring(1));
        }
        return str.equals(str2);
    }

    public static boolean isWeexBlackUrl(String str) {
        try {
            if (TextUtils.isEmpty(str)) {
                WXLogUtils.d("urlHost is empty");
                return false;
            }
            IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
            if (configAdapter == null) {
                return false;
            }
            String config = configAdapter.getConfig(CONFIG_GROUP_WEEX_BLACKURL_GROUP, CONFIG_KEY_WEEX_BLACKURL, "");
            if (TextUtils.isEmpty(config)) {
                return false;
            }
            String[] split = config.split(",");
            if (split != null) {
                if (split.length != 0) {
                    for (String str2 : split) {
                        if (!TextUtils.isEmpty(str2) && checkUrl(str, str2)) {
                            return true;
                        }
                    }
                    return false;
                }
            }
            WXLogUtils.d("expectedUrls is empty");
            return false;
        } catch (Throwable th) {
            WXLogUtils.e(WXLogUtils.getStackTrace(th));
        }
    }

    public static boolean isWeexTrustedUrl(String str) {
        IConfigAdapter configAdapter;
        String[] split;
        try {
            if (TextUtils.isEmpty(str) || (configAdapter = AliWeex.getInstance().getConfigAdapter()) == null) {
                return false;
            }
            String config = configAdapter.getConfig(CONFIG_GROUP_WEEX_WHITEURL_GROUP, CONFIG_KEY_WEEX_WHITEURL, "*.m.taobao.com,xilivr.ewszjk.m.jaeapp.com,luckygiftphp.ewszjk.m.jaeapp.com,xuan.ews.m.jaeapp.com");
            if (!TextUtils.isEmpty(config) && (split = config.split(",")) != null) {
                if (split.length != 0) {
                    for (String str2 : split) {
                        if (!TextUtils.isEmpty(str2) && checkUrl(str, str2)) {
                            return true;
                        }
                    }
                    return false;
                }
            }
            return false;
        } catch (Throwable th) {
            WXLogUtils.e(WXLogUtils.getStackTrace(th));
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(9:15|16|17|18|19|20|(3:22|(1:39)(1:(2:25|(2:37|27)(1:40))(2:28|(2:38|30)(1:41)))|31)|36|35) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x003b */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0045 A[Catch:{ Throwable -> 0x0072 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean isWeexTrustedScheme(java.lang.String r9) {
        /*
            r0 = 0
            boolean r1 = android.text.TextUtils.isEmpty(r9)     // Catch:{ Throwable -> 0x0072 }
            if (r1 == 0) goto L_0x0008
            return r0
        L_0x0008:
            com.alibaba.aliweex.AliWeex r1 = com.alibaba.aliweex.AliWeex.getInstance()     // Catch:{ Throwable -> 0x0072 }
            com.alibaba.aliweex.IConfigAdapter r1 = r1.getConfigAdapter()     // Catch:{ Throwable -> 0x0072 }
            if (r1 != 0) goto L_0x0013
            return r0
        L_0x0013:
            java.lang.String r2 = "weex_config_render_white_schema"
            java.lang.String r3 = "white_schema"
            java.lang.String r4 = ""
            java.lang.String r1 = r1.getConfig(r2, r3, r4)     // Catch:{ Throwable -> 0x0072 }
            boolean r2 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Throwable -> 0x0072 }
            if (r2 == 0) goto L_0x0025
            return r0
        L_0x0025:
            java.lang.String r2 = ","
            java.lang.String[] r1 = r1.split(r2)     // Catch:{ Throwable -> 0x0072 }
            if (r1 == 0) goto L_0x0071
            int r2 = r1.length     // Catch:{ Throwable -> 0x0072 }
            if (r2 != 0) goto L_0x0031
            goto L_0x0071
        L_0x0031:
            r2 = 0
            android.net.Uri r3 = android.net.Uri.parse(r9)     // Catch:{ Throwable -> 0x003b }
            java.lang.String r3 = r3.getScheme()     // Catch:{ Throwable -> 0x003b }
            r2 = r3
        L_0x003b:
            boolean r3 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Throwable -> 0x0072 }
            r4 = 1
            r3 = r3 ^ r4
            int r5 = r1.length     // Catch:{ Throwable -> 0x0072 }
            r6 = 0
        L_0x0043:
            if (r6 >= r5) goto L_0x007a
            r7 = r1[r6]     // Catch:{ Throwable -> 0x0072 }
            boolean r8 = android.text.TextUtils.isEmpty(r7)     // Catch:{ Throwable -> 0x0072 }
            if (r8 != 0) goto L_0x006e
            if (r3 == 0) goto L_0x0056
            boolean r7 = android.text.TextUtils.equals(r7, r2)     // Catch:{ Throwable -> 0x0072 }
            if (r7 == 0) goto L_0x006e
            return r4
        L_0x0056:
            java.lang.StringBuilder r8 = new java.lang.StringBuilder     // Catch:{ Throwable -> 0x0072 }
            r8.<init>()     // Catch:{ Throwable -> 0x0072 }
            r8.append(r7)     // Catch:{ Throwable -> 0x0072 }
            java.lang.String r7 = "://"
            r8.append(r7)     // Catch:{ Throwable -> 0x0072 }
            java.lang.String r7 = r8.toString()     // Catch:{ Throwable -> 0x0072 }
            boolean r7 = r9.startsWith(r7)     // Catch:{ Throwable -> 0x0072 }
            if (r7 == 0) goto L_0x006e
            return r4
        L_0x006e:
            int r6 = r6 + 1
            goto L_0x0043
        L_0x0071:
            return r0
        L_0x0072:
            r9 = move-exception
            java.lang.String r9 = com.taobao.weex.utils.WXLogUtils.getStackTrace(r9)
            com.taobao.weex.utils.WXLogUtils.e(r9)
        L_0x007a:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.bundle.UrlValidate.isWeexTrustedScheme(java.lang.String):boolean");
    }

    public static boolean isUrlValid(String str) {
        if (WVServerConfig.isBlackUrl(str)) {
            return false;
        }
        String str2 = null;
        try {
            str2 = getUrlHost(str);
        } catch (Throwable th) {
            WXLogUtils.e(WXLogUtils.getStackTrace(th));
        }
        if (isWeexBlackUrl(str2)) {
            return false;
        }
        if (WVServerConfig.isTrustedUrl(str) || TextUtils.isEmpty(str2) || isWeexTrustedUrl(str2)) {
            return true;
        }
        return isWeexTrustedScheme(str);
    }

    public static boolean isValid(String str) {
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter != null) {
            boolean booleanValue = Boolean.valueOf(configAdapter.getConfig(CONFIG_GROUP_URL_CHECK_SWITCH, CONFIG_KEY_IS_CHECK, "")).booleanValue();
            boolean booleanValue2 = Boolean.valueOf(configAdapter.getConfig(CONFIG_GROUP_URL_CHECK_SWITCH, CONFIG_KEY_IS_RENDER, "")).booleanValue();
            if (booleanValue) {
                if (WVServerConfig.isBlackUrl(str)) {
                    return false;
                }
                String str2 = null;
                try {
                    str2 = getUrlHost(str);
                } catch (Throwable th) {
                    WXLogUtils.e(WXLogUtils.getStackTrace(th));
                }
                if (isWeexBlackUrl(str2)) {
                    return false;
                }
                if (!WVServerConfig.isTrustedUrl(str) && !TextUtils.isEmpty(str2) && !isWeexTrustedUrl(str2) && !isWeexTrustedScheme(str)) {
                    return booleanValue2;
                }
                return true;
            }
        }
        return true;
    }

    private static String getUrlHost(String str) {
        String str2;
        try {
            str2 = Uri.parse(str).getHost();
        } catch (Throwable unused) {
            str2 = null;
        }
        if (TextUtils.isEmpty(str2) && !TextUtils.isEmpty(str) && str.startsWith("/")) {
            String substring = str.substring(1);
            while (substring.startsWith("/")) {
                substring = substring.substring(1);
            }
            try {
                return Uri.parse("https://" + substring).getHost();
            } catch (Throwable unused2) {
            }
        }
        return str2;
    }

    public static boolean shouldShowInvalidUrlTips(String str) {
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter != null) {
            boolean booleanValue = Boolean.valueOf(configAdapter.getConfig(CONFIG_GROUP_URL_CHECK_SWITCH, CONFIG_KEY_IS_CHECK, "")).booleanValue();
            boolean booleanValue2 = Boolean.valueOf(configAdapter.getConfig(CONFIG_GROUP_URL_CHECK_SWITCH, CONFIG_KEY_IS_RENDER, "")).booleanValue();
            if (!booleanValue || WVServerConfig.isBlackUrl(str) || isUrlValid(str) || !booleanValue2) {
                return false;
            }
            return true;
        }
        return false;
    }
}
