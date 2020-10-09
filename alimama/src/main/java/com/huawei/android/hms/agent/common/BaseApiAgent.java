package com.huawei.android.hms.agent.common;

public abstract class BaseApiAgent implements IClientConnectCallback {
    /* access modifiers changed from: protected */
    public void connect() {
        HMSAgentLog.d("connect");
        ApiClientMgr.INST.connect(this, true);
    }
}
