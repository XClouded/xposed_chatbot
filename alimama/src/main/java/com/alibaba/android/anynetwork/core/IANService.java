package com.alibaba.android.anynetwork.core;

public interface IANService {
    ANRequestId asyncRequest(ANRequest aNRequest);

    boolean cancelRequest(ANRequestId aNRequestId);

    Object getDataByKey(String str);

    void init(ANConfig aNConfig);

    boolean isSupportRequest(ANRequest aNRequest);

    ANResponse syncRequest(ANRequest aNRequest);

    void updateConfig(ANConfig aNConfig);
}
