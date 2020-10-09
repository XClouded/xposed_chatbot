package com.huawei.android.hms.agent.common;

import com.huawei.hms.api.HuaweiApiClient;

public interface IClientConnectCallback {
    void onConnect(int i, HuaweiApiClient huaweiApiClient);
}
