package com.huawei.android.hms.agent.common;

import com.huawei.hms.api.HuaweiApiClient;

public class EmptyConnectCallback implements IClientConnectCallback {
    private String msgPre;

    public EmptyConnectCallback(String str) {
        this.msgPre = str;
    }

    public void onConnect(int i, HuaweiApiClient huaweiApiClient) {
        HMSAgentLog.d(this.msgPre + i);
    }
}
