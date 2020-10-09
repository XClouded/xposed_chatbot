package com.taobao.weex.adapter;

import android.text.TextUtils;
import com.alibaba.aliweex.IConfigAdapter;
import com.alibaba.aliweex.bundle.UrlValidate;
import com.alibaba.aliweex.hc.bundle.WXHCNavBarAdapter;
import com.taobao.android.speed.TBSpeed;
import com.taobao.orange.OrangeConfig;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.utils.TBWXConfigManger;
import java.util.Map;

public class TBConfigAdapter implements IConfigAdapter {
    public String getConfig(String str, String str2, String str3) {
        if (UrlValidate.CONFIG_GROUP_URL_CHECK_SWITCH.equals(str)) {
            if (UrlValidate.CONFIG_KEY_IS_CHECK.equals(str2)) {
                str = TBWXConfigManger.WX_NAMEPACE_CHECK_URL;
                str2 = TBWXConfigManger.WX_CHECK_URL_KEY;
                if (TextUtils.isEmpty(str3)) {
                    str3 = "true";
                }
            } else if (UrlValidate.CONFIG_KEY_IS_RENDER.equals(str2)) {
                str = TBWXConfigManger.WX_NAMESPACE_RENDER;
                str2 = TBWXConfigManger.WX_RENDER_KEY;
                if (TextUtils.isEmpty(str3)) {
                    str3 = "true";
                }
            }
        } else if (WXHCNavBarAdapter.CONFIG_GROUP_WEEX_HC.equals(str) && "weex_main_hc_domain".equals(str2)) {
            str = TBWXConfigManger.HC_CONFIG;
            str2 = "weex_main_hc_domain";
        }
        return OrangeConfig.getInstance().getConfig(str, str2, str3);
    }

    public Map<String, String> getConfigs(String str) {
        return OrangeConfig.getInstance().getConfigs(str);
    }

    public boolean checkMode(String str) {
        try {
            return TBSpeed.isSpeedEdition(WXEnvironment.getApplication(), str);
        } catch (Throwable th) {
            th.printStackTrace();
            return false;
        }
    }
}
