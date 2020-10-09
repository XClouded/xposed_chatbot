package com.alibaba.android.anynetwork.core;

import com.alibaba.android.anynetwork.core.common.ANConstants;
import com.alibaba.android.anynetwork.core.impl.UnSupportUninstallAnyNetworkImpl;
import com.alibaba.android.anynetwork.core.utils.ANLog;

public class AnyNetworkManager {
    private static final String TAG = "AnyNetworkManager";
    private static volatile IAnyNetwork sAnyNetwork;
    private static ANConfig sConfig;

    public static void setGlobalAnyNetwork(IAnyNetwork iAnyNetwork) {
        sAnyNetwork = iAnyNetwork;
    }

    public static IAnyNetwork getGlobalAnyNetwork() {
        if (sAnyNetwork == null) {
            synchronized (AnyNetworkManager.class) {
                if (sAnyNetwork == null) {
                    sAnyNetwork = new UnSupportUninstallAnyNetworkImpl();
                }
            }
        }
        return sAnyNetwork;
    }

    public static void setConfig(ANConfig aNConfig) {
        if (aNConfig != null) {
            ANConstants.DEBUG = aNConfig.isDebug();
            ANLog.setProxy(aNConfig.getLogProxy());
            sConfig = aNConfig;
            if (sAnyNetwork != null) {
                sAnyNetwork.updateAllConfig(aNConfig);
            }
            ANLog.d(TAG, "setConfig:" + sConfig.toString());
        }
    }

    public static ANConfig getConfig() {
        return sConfig;
    }
}
