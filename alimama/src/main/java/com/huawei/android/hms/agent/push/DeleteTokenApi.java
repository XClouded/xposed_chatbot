package com.huawei.android.hms.agent.push;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.ApiClientMgr;
import com.huawei.android.hms.agent.common.BaseApiAgent;
import com.huawei.android.hms.agent.common.CallbackCodeRunnable;
import com.huawei.android.hms.agent.common.HMSAgentLog;
import com.huawei.android.hms.agent.common.StrUtils;
import com.huawei.android.hms.agent.common.ThreadUtil;
import com.huawei.android.hms.agent.push.handler.DeleteTokenHandler;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;

public class DeleteTokenApi extends BaseApiAgent {
    private DeleteTokenHandler handler;
    /* access modifiers changed from: private */
    public String token;

    public void onConnect(final int i, final HuaweiApiClient huaweiApiClient) {
        ThreadUtil.INST.excute(new Runnable() {
            public void run() {
                if (TextUtils.isEmpty(DeleteTokenApi.this.token)) {
                    HMSAgentLog.e("删除TOKEN失败: 要删除的token为空");
                    DeleteTokenApi.this.onDeleteTokenResult(HMSAgent.AgentResultCode.EMPTY_PARAM);
                } else if (huaweiApiClient == null || !ApiClientMgr.INST.isConnect(huaweiApiClient)) {
                    HMSAgentLog.e("client not connted");
                    DeleteTokenApi.this.onDeleteTokenResult(i);
                } else {
                    try {
                        HuaweiPush.HuaweiPushApi.deleteToken(huaweiApiClient, DeleteTokenApi.this.token);
                        DeleteTokenApi.this.onDeleteTokenResult(0);
                    } catch (Exception e) {
                        HMSAgentLog.e("删除TOKEN失败:" + e.getMessage());
                        DeleteTokenApi.this.onDeleteTokenResult(HMSAgent.AgentResultCode.CALL_EXCEPTION);
                    }
                }
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void onDeleteTokenResult(int i) {
        HMSAgentLog.i("deleteToken:callback=" + StrUtils.objDesc(this.handler) + " retCode=" + i);
        if (this.handler != null) {
            new Handler(Looper.getMainLooper()).post(new CallbackCodeRunnable(this.handler, i));
            this.handler = null;
        }
    }

    public void deleteToken(String str, DeleteTokenHandler deleteTokenHandler) {
        HMSAgentLog.i("deleteToken:token:" + StrUtils.objDesc(str) + " handler=" + StrUtils.objDesc(deleteTokenHandler));
        this.token = str;
        this.handler = deleteTokenHandler;
        connect();
    }
}
