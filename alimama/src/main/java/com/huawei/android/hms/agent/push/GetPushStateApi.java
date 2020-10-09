package com.huawei.android.hms.agent.push;

import android.os.Handler;
import android.os.Looper;
import com.huawei.android.hms.agent.common.ApiClientMgr;
import com.huawei.android.hms.agent.common.BaseApiAgent;
import com.huawei.android.hms.agent.common.CallbackCodeRunnable;
import com.huawei.android.hms.agent.common.HMSAgentLog;
import com.huawei.android.hms.agent.common.StrUtils;
import com.huawei.android.hms.agent.common.ThreadUtil;
import com.huawei.android.hms.agent.push.handler.GetPushStateHandler;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;

public class GetPushStateApi extends BaseApiAgent {
    private GetPushStateHandler handler;

    public void onConnect(final int i, final HuaweiApiClient huaweiApiClient) {
        ThreadUtil.INST.excute(new Runnable() {
            public void run() {
                if (huaweiApiClient == null || !ApiClientMgr.INST.isConnect(huaweiApiClient)) {
                    HMSAgentLog.e("client not connted");
                    GetPushStateApi.this.onGetPushStateResult(i);
                    return;
                }
                HuaweiPush.HuaweiPushApi.getPushState(huaweiApiClient);
                GetPushStateApi.this.onGetPushStateResult(0);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void onGetPushStateResult(int i) {
        HMSAgentLog.i("getPushState:callback=" + StrUtils.objDesc(this.handler) + " retCode=" + i);
        if (this.handler != null) {
            new Handler(Looper.getMainLooper()).post(new CallbackCodeRunnable(this.handler, i));
            this.handler = null;
        }
    }

    public void getPushState(GetPushStateHandler getPushStateHandler) {
        HMSAgentLog.i("getPushState:handler=" + StrUtils.objDesc(getPushStateHandler));
        this.handler = getPushStateHandler;
        connect();
    }
}
