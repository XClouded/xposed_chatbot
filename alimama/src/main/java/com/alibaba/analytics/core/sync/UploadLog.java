package com.alibaba.analytics.core.sync;

import android.text.TextUtils;
import com.alibaba.analytics.core.Variables;
import com.alibaba.analytics.core.config.UTBaseConfMgr;
import com.alibaba.analytics.core.network.NetworkUtil;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.json.JSONObject;

public class UploadLog {
    protected NetworkStatus mAllowedNetworkStatus = NetworkStatus.ALL;
    protected IUploadExcuted mIUploadExcuted = null;
    protected int mMaxUploadTimes = 3;

    public enum NetworkStatus {
        ALL,
        WIFI,
        TWO_GENERATION,
        THRID_GENERATION,
        FOUR_GENERATION,
        NONE
    }

    public void setUploadTimes(int i) {
        this.mMaxUploadTimes = i;
    }

    public void setAllowedNetworkStatus(NetworkStatus networkStatus) {
        this.mAllowedNetworkStatus = networkStatus;
    }

    public void setIUploadExcuted(IUploadExcuted iUploadExcuted) {
        this.mIUploadExcuted = iUploadExcuted;
    }

    /* access modifiers changed from: protected */
    public NetworkStatus getNetworkStatus() {
        String networkType = NetworkUtil.getNetworkType();
        if ("2G".equalsIgnoreCase(networkType)) {
            return NetworkStatus.TWO_GENERATION;
        }
        if ("3G".equalsIgnoreCase(networkType)) {
            return NetworkStatus.THRID_GENERATION;
        }
        if ("4G".equalsIgnoreCase(networkType)) {
            return NetworkStatus.FOUR_GENERATION;
        }
        if ("Wi-Fi".equalsIgnoreCase(networkType)) {
            return NetworkStatus.WIFI;
        }
        return NetworkStatus.NONE;
    }

    public void parserConfig(String str) {
        JSONObject jSONObject;
        Iterator<String> keys;
        String str2;
        if (!TextUtils.isEmpty(str)) {
            try {
                UTBaseConfMgr confMgr = Variables.getInstance().getConfMgr();
                if (confMgr != null && (jSONObject = new JSONObject(str).getJSONObject(BindingXConstants.KEY_CONFIG)) != null) {
                    Iterator<String> keys2 = jSONObject.keys();
                    if (keys2 == null || !keys2.hasNext()) {
                        Logger.i((String) null, "No Config Update");
                        return;
                    }
                    while (keys2.hasNext()) {
                        String next = keys2.next();
                        if (!TextUtils.isEmpty(next)) {
                            HashMap hashMap = new HashMap();
                            JSONObject jSONObject2 = jSONObject.getJSONObject(next);
                            if (!(jSONObject2 == null || (keys = jSONObject2.keys()) == null)) {
                                while (keys.hasNext()) {
                                    String next2 = keys.next();
                                    if (jSONObject2.get(next2) == null) {
                                        str2 = null;
                                    } else {
                                        str2 = jSONObject2.get(next2) + "";
                                    }
                                    hashMap.put(next2, str2);
                                }
                            }
                            Logger.d("Config Update", "namespace", next, "configs", hashMap);
                            confMgr.updateAndDispatch(next, (Map<String, String>) hashMap);
                        }
                    }
                    UTBaseConfMgr.sendConfigTimeStamp("1");
                }
            } catch (Throwable th) {
                Logger.e("", th, new Object[0]);
            }
        } else {
            Logger.w((String) null, "Config Is Empty");
        }
    }
}
