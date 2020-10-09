package com.alibaba.aliweex.adapter.adapter;

import android.text.TextUtils;
import com.alibaba.aliweex.AliWeex;
import com.alibaba.aliweex.IConfigAdapter;
import com.alibaba.aliweex.utils.WXInitConfigManager;
import com.taobao.weex.adapter.IWXConfigAdapter;

public class WXConfigAdapter implements IWXConfigAdapter {
    private static final String DEFAULT_WHITE_SCREEN_WHITE_LIST = "g.alicdn.com/tbsearch-segments/placeholder_bar/0.0.1/nx_placeholder_bar_segment.js;";

    public String getConfig(String str, String str2, String str3) {
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter == null) {
            return str3;
        }
        if (WXInitConfigManager.WXAPM_CONFIG_GROUP.equalsIgnoreCase(str) && "use_runtime_api".equalsIgnoreCase(str2)) {
            return WXInitConfigManager.getInstance().tryGetConfigFromSpAndOrange(str, str2, str3);
        }
        String config = configAdapter.getConfig(str, str2, str3);
        return (!TextUtils.isEmpty(config) || !WXInitConfigManager.WXAPM_CONFIG_GROUP.equalsIgnoreCase(str) || !"ws_white_list".equalsIgnoreCase(str2)) ? config : DEFAULT_WHITE_SCREEN_WHITE_LIST;
    }

    public String getConfigWhenInit(String str, String str2, String str3) {
        return WXInitConfigManager.getInstance().tryGetConfigFromSpAndOrange(str, str2, str3);
    }

    public boolean checkMode(String str) {
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter == null) {
            return false;
        }
        return configAdapter.checkMode(str);
    }
}
