package com.alibaba.android.anynetwork.core;

public interface IAnyNetwork {
    ANRequestId asyncRequest(ANRequest aNRequest);

    boolean cancelRequest(ANRequestId aNRequestId);

    Object getDataFromService(String str, String str2);

    void installService(String str, IANService iANService);

    ANResponse syncRequest(ANRequest aNRequest);

    void uninstallService(String str);

    void updateAllConfig(ANConfig aNConfig);

    void updateConfig(String str, ANConfig aNConfig);
}
