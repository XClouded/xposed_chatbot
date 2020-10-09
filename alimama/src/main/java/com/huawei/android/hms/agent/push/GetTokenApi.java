package com.huawei.android.hms.agent.push;

import android.os.Handler;
import android.os.Looper;
import com.huawei.android.hms.agent.common.ApiClientMgr;
import com.huawei.android.hms.agent.common.BaseApiAgent;
import com.huawei.android.hms.agent.common.CallbackCodeRunnable;
import com.huawei.android.hms.agent.common.HMSAgentLog;
import com.huawei.android.hms.agent.common.StrUtils;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.client.Status;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;

public class GetTokenApi extends BaseApiAgent {
    private static final int MAX_RETRY_TIMES = 1;
    private GetTokenHandler handler;
    /* access modifiers changed from: private */
    public int retryTimes = 1;

    static /* synthetic */ int access$010(GetTokenApi getTokenApi) {
        int i = getTokenApi.retryTimes;
        getTokenApi.retryTimes = i - 1;
        return i;
    }

    public void onConnect(int i, HuaweiApiClient huaweiApiClient) {
        if (huaweiApiClient == null || !ApiClientMgr.INST.isConnect(huaweiApiClient)) {
            HMSAgentLog.e("client not connted");
            onPushTokenResult(i, (TokenResult) null);
            return;
        }
        HuaweiPush.HuaweiPushApi.getToken(huaweiApiClient).setResultCallback(new ResultCallback<TokenResult>() {
            public void onResult(TokenResult tokenResult) {
                if (tokenResult == null) {
                    HMSAgentLog.e("result is null");
                    GetTokenApi.this.onPushTokenResult(-1002, (TokenResult) null);
                    return;
                }
                Status status = tokenResult.getStatus();
                if (status == null) {
                    HMSAgentLog.e("status is null");
                    GetTokenApi.this.onPushTokenResult(-1003, (TokenResult) null);
                    return;
                }
                int statusCode = status.getStatusCode();
                HMSAgentLog.d("status=" + status);
                if ((statusCode == 907135006 || statusCode == 907135003) && GetTokenApi.this.retryTimes > 0) {
                    GetTokenApi.access$010(GetTokenApi.this);
                    GetTokenApi.this.connect();
                    return;
                }
                GetTokenApi.this.onPushTokenResult(statusCode, tokenResult);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void onPushTokenResult(int i, TokenResult tokenResult) {
        HMSAgentLog.i("getToken:callback=" + StrUtils.objDesc(this.handler) + " retCode=" + i);
        if (this.handler != null) {
            new Handler(Looper.getMainLooper()).post(new CallbackCodeRunnable(this.handler, i));
            this.handler = null;
        }
        this.retryTimes = 1;
    }

    public void getToken(GetTokenHandler getTokenHandler) {
        HMSAgentLog.i("getToken:handler=" + StrUtils.objDesc(getTokenHandler));
        this.handler = getTokenHandler;
        this.retryTimes = 1;
        connect();
    }
}
