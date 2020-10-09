package com.alibaba.analytics.core.ipv6;

import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.config.SystemConfigMgr;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.StringUtils;
import com.ta.audid.Constants;
import com.ta.utdid2.device.UTDevice;

class SampleIpv6Listener implements SystemConfigMgr.IKVChangeListener {
    private int mSample = 0;

    public SampleIpv6Listener() {
        parseConfig(SystemConfigMgr.getInstance().get(Ipv6ConfigConstant.SAMPLE_IPV6));
    }

    public void onChange(String str, String str2) {
        parseConfig(str2);
    }

    private void parseConfig(String str) {
        Logger.d("SampleIpv6Listener", "parseConfig value", str);
        if (!TextUtils.isEmpty(str)) {
            try {
                this.mSample = Integer.parseInt(str);
            } catch (Exception unused) {
                this.mSample = 0;
            }
        }
    }

    public boolean isEnableBySample() {
        String utdid = UTDevice.getUtdid(Variables.getInstance().getContext());
        if (utdid == null || utdid.equals(Constants.UTDID_INVALID)) {
            return false;
        }
        int abs = Math.abs(StringUtils.hashCode(utdid));
        Logger.d("SampleIpv6Listener", "hashcode", Integer.valueOf(abs), "sample", Integer.valueOf(this.mSample));
        if (abs % 10000 < this.mSample) {
            return true;
        }
        return false;
    }
}
