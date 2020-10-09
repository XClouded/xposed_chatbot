package com.taobao.weex.utils;

import android.text.TextUtils;
import com.taobao.orange.OrangeConfig;

public class TBWXConfigManger {
    public static final String HC_CONFIG = "android_weex_huichang_config";
    public static final String HC_DOMAIN = "weex_main_hc_domain";
    public static final String WX_CHECK_URL_KEY = "weex_check_url";
    public static final String WX_DEGRADE_KEY = "weex_degrade";
    public static final String WX_GET_DEEP_VIEW_LAYER = "get_deep_view_layer";
    public static final String WX_GROUP_NAME = "android_weex_degrade";
    public static final String WX_NAMEPACE_CHECK_URL = "android_weex_check_url";
    public static final String WX_NAMESPACE_EXT_CONFIG = "wx_namespace_ext_config";
    public static final String WX_NAMESPACE_RENDER = "android_weex_render";
    public static final String WX_NAMESPACE_TABLET = "weex_android_tablet";
    public static final String WX_RENDER_KEY = "weex_render";
    public static final String WX_SUPPORT_KEY = "support";
    private static TBWXConfigManger ourInstance = new TBWXConfigManger();

    public static TBWXConfigManger getInstance() {
        return ourInstance;
    }

    private TBWXConfigManger() {
    }

    public synchronized boolean isDegrade() {
        String config = OrangeConfig.getInstance().getConfig(WX_GROUP_NAME, WX_DEGRADE_KEY, "false");
        if (TextUtils.isEmpty(config) || !"true".equals(config)) {
            return false;
        }
        return true;
    }

    public synchronized boolean isCheckUrl() {
        if (TextUtils.equals("true", OrangeConfig.getInstance().getConfig(WX_NAMEPACE_CHECK_URL, WX_CHECK_URL_KEY, "true"))) {
            return true;
        }
        return false;
    }

    public synchronized boolean isRender() {
        if (TextUtils.equals("true", OrangeConfig.getInstance().getConfig(WX_NAMESPACE_RENDER, WX_RENDER_KEY, "true"))) {
            return true;
        }
        return false;
    }

    public synchronized boolean isGetDeepViewLayer() {
        if (TextUtils.equals("true", OrangeConfig.getInstance().getConfig(WX_NAMESPACE_EXT_CONFIG, WX_GET_DEEP_VIEW_LAYER, "false"))) {
            return true;
        }
        return false;
    }

    public synchronized String getMainHCUrls() {
        return OrangeConfig.getInstance().getConfig(HC_CONFIG, "weex_main_hc_domain", "");
    }

    public synchronized boolean isSupporTablet() {
        if ("true".equals(OrangeConfig.getInstance().getConfig(WX_NAMESPACE_TABLET, WX_SUPPORT_KEY, "false"))) {
            return true;
        }
        return false;
    }
}
