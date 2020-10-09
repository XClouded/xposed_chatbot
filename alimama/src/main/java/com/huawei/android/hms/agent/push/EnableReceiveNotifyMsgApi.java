package com.huawei.android.hms.agent.push;

import android.os.Handler;
import android.os.Looper;
import com.huawei.android.hms.agent.common.ApiClientMgr;
import com.huawei.android.hms.agent.common.BaseApiAgent;
import com.huawei.android.hms.agent.common.CallbackCodeRunnable;
import com.huawei.android.hms.agent.common.HMSAgentLog;
import com.huawei.android.hms.agent.common.StrUtils;
import com.huawei.android.hms.agent.common.ThreadUtil;
import com.huawei.android.hms.agent.push.handler.EnableReceiveNotifyMsgHandler;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;

public class EnableReceiveNotifyMsgApi extends BaseApiAgent {
    boolean enable;
    private EnableReceiveNotifyMsgHandler handler;

    public void onConnect(final int i, final HuaweiApiClient huaweiApiClient) {
        ThreadUtil.INST.excute(new Runnable() {
            public void run() {
                if (huaweiApiClient == null || !ApiClientMgr.INST.isConnect(huaweiApiClient)) {
                    HMSAgentLog.e("client not connted");
                    EnableReceiveNotifyMsgApi.this.onEnableReceiveNotifyMsgResult(i);
                    return;
                }
                HuaweiPush.HuaweiPushApi.enableReceiveNotifyMsg(huaweiApiClient, EnableReceiveNotifyMsgApi.this.enable);
                EnableReceiveNotifyMsgApi.this.onEnableReceiveNotifyMsgResult(0);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void onEnableReceiveNotifyMsgResult(int i) {
        HMSAgentLog.i("enableReceiveNotifyMsg:callback=" + StrUtils.objDesc(this.handler) + " retCode=" + i);
        if (this.handler != null) {
            new Handler(Looper.getMainLooper()).post(new CallbackCodeRunnable(this.handler, i));
            this.handler = null;
        }
    }

    public void enableReceiveNotifyMsg(boolean z, EnableReceiveNotifyMsgHandler enableReceiveNotifyMsgHandler) {
        HMSAgentLog.i("enableReceiveNotifyMsg:enable=" + z + " handler=" + StrUtils.objDesc(enableReceiveNotifyMsgHandler));
        this.enable = z;
        this.handler = enableReceiveNotifyMsgHandler;
        connect();
    }
}
