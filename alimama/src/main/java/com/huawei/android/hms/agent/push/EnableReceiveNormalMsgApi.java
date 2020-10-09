package com.huawei.android.hms.agent.push;

import android.os.Handler;
import android.os.Looper;
import com.huawei.android.hms.agent.common.ApiClientMgr;
import com.huawei.android.hms.agent.common.BaseApiAgent;
import com.huawei.android.hms.agent.common.CallbackCodeRunnable;
import com.huawei.android.hms.agent.common.HMSAgentLog;
import com.huawei.android.hms.agent.common.StrUtils;
import com.huawei.android.hms.agent.common.ThreadUtil;
import com.huawei.android.hms.agent.push.handler.EnableReceiveNormalMsgHandler;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;

public class EnableReceiveNormalMsgApi extends BaseApiAgent {
    boolean enable;
    private EnableReceiveNormalMsgHandler handler;

    public void onConnect(final int i, final HuaweiApiClient huaweiApiClient) {
        ThreadUtil.INST.excute(new Runnable() {
            public void run() {
                if (huaweiApiClient == null || !ApiClientMgr.INST.isConnect(huaweiApiClient)) {
                    HMSAgentLog.e("client not connted");
                    EnableReceiveNormalMsgApi.this.onEnableReceiveNormalMsgResult(i);
                    return;
                }
                HuaweiPush.HuaweiPushApi.enableReceiveNormalMsg(huaweiApiClient, EnableReceiveNormalMsgApi.this.enable);
                EnableReceiveNormalMsgApi.this.onEnableReceiveNormalMsgResult(0);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void onEnableReceiveNormalMsgResult(int i) {
        HMSAgentLog.i("enableReceiveNormalMsg:callback=" + StrUtils.objDesc(this.handler) + " retCode=" + i);
        if (this.handler != null) {
            new Handler(Looper.getMainLooper()).post(new CallbackCodeRunnable(this.handler, i));
            this.handler = null;
        }
    }

    public void enableReceiveNormalMsg(boolean z, EnableReceiveNormalMsgHandler enableReceiveNormalMsgHandler) {
        HMSAgentLog.i("enableReceiveNormalMsg:enable=" + z + "  handler=" + StrUtils.objDesc(enableReceiveNormalMsgHandler));
        this.enable = z;
        this.handler = enableReceiveNormalMsgHandler;
        connect();
    }
}
