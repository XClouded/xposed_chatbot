package com.uc.webview.export;

import com.taobao.android.dinamic.DinamicConstant;
import com.uc.webview.export.annotations.Api;

@Api
/* compiled from: U4Source */
public class Build {
    public static String CORE_TIME = "200323191416";
    public static String CORE_VERSION = "4.0.0.1";
    public static String CPU_ARCH = a("@CPU_ARCH@", "armv7-a");
    public static boolean IS_INTERNATIONAL_VERSION = false;
    public static int PACK_TYPE = 0;
    public static String SDK_BMODE = a("@WEBVIEW_SDK_BMODE@", "WWW");
    public static String SDK_BTYPE = a("@WEBVIEW_SDK_BTYPE@", "UC");
    public static String SDK_FR = a("@WEBVIEW_SDK_FR@", "android");
    public static String SDK_LANG = a("@WEBVIEW_SDK_LANG@", "zh-CN");
    public static String SDK_PRD = a("@WEBVIEW_SDK_PRD@", "uc_webview_pro");
    public static String SDK_PROFILE_ID = a("@WEBVIEW_SDK_PFID@", "");
    public static String SDK_SUBVER = a("@WEBVIEW_SDK_SUBVER@", "release");
    public static String TIME = "200323191416";
    public static String TYPE = a("@WEBVIEW_SDK_TYPE@", "");
    public static String UCM_SUPPORT_SDK_MIN = "";
    public static String UCM_VERSION = a("@WEBVIEW_SDK_UCM_VERSION@", "");

    @Api
    /* compiled from: U4Source */
    public static class Version {
        public static int API_LEVEL = 21;
        public static int BUILD_SERIAL = 0;
        public static int MAJOR = 3;
        public static int MINOR = 21;
        public static String NAME = (MAJOR + "." + MINOR + "." + BUILD_SERIAL + "." + PATCH);
        public static int PATCH = 126;
        public static String SUPPORT_U4_MIN = Build.a("@WEBVIEW_SDK_SUPPORT_U4_MIN@", "3.19.0.30");
        public static String SUPPORT_UCM_MIN = "99999.99999.99999.99999";
    }

    static String a(String str, String str2) {
        return str.startsWith(DinamicConstant.DINAMIC_PREFIX_AT) ? str2 : str;
    }

    static {
        int i;
        if ("@USE_KERNEL_TYPE@".startsWith(DinamicConstant.DINAMIC_PREFIX_AT)) {
            i = 4;
        } else {
            i = Integer.parseInt("@USE_KERNEL_TYPE@");
        }
        PACK_TYPE = i;
    }
}
