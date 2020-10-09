package com.taobao.weex.utils;

import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.IWXConfigAdapter;

public class FeatureSwitches {
    public static final String NAMESPACE_EXT_CONFIG = "android_weex_ext_config";

    public static boolean isOpen(String str, boolean z) {
        IWXConfigAdapter wxConfigAdapter = WXSDKManager.getInstance().getWxConfigAdapter();
        if (wxConfigAdapter == null) {
            return z;
        }
        return wxConfigAdapter.checkMode(str);
    }

    public static boolean isOpenWithConfig(String str, String str2, String str3, boolean z) {
        IWXConfigAdapter wxConfigAdapter = WXSDKManager.getInstance().getWxConfigAdapter();
        if (wxConfigAdapter == null) {
            return z;
        }
        if (wxConfigAdapter.checkMode(str)) {
            return true;
        }
        String config = wxConfigAdapter.getConfig(str2, str3, z ? "true" : "false");
        if ("true".equalsIgnoreCase(config) || "1".equalsIgnoreCase(config)) {
            return true;
        }
        return false;
    }
}
