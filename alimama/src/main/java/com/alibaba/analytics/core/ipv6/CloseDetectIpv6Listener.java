package com.alibaba.analytics.core.ipv6;

import android.text.TextUtils;
import com.alibaba.analytics.core.config.SystemConfigMgr;
import com.alibaba.analytics.utils.Logger;

class CloseDetectIpv6Listener implements SystemConfigMgr.IKVChangeListener {
    private boolean mCloseDetect = false;

    public CloseDetectIpv6Listener() {
        parseConfig(SystemConfigMgr.getInstance().get(Ipv6ConfigConstant.CLOSE_DETECT_IPV6));
    }

    public void onChange(String str, String str2) {
        parseConfig(str2);
    }

    private void parseConfig(String str) {
        Logger.d("CloseDetectIpv6Listener", "parseConfig value", str);
        if (TextUtils.isEmpty(str)) {
            return;
        }
        if ("1".equalsIgnoreCase(str)) {
            this.mCloseDetect = true;
        } else {
            this.mCloseDetect = false;
        }
    }

    public boolean isCloseDetect() {
        return this.mCloseDetect;
    }
}
