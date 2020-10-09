package com.huawei.android.hms.agent.push;

import android.os.Handler;
import android.os.Looper;
import com.huawei.android.hms.agent.common.ApiClientMgr;
import com.huawei.android.hms.agent.common.BaseApiAgent;
import com.huawei.android.hms.agent.common.CallbackCodeRunnable;
import com.huawei.android.hms.agent.common.HMSAgentLog;
import com.huawei.android.hms.agent.common.StrUtils;
import com.huawei.android.hms.agent.common.ThreadUtil;
import com.huawei.android.hms.agent.push.handler.QueryAgreementHandler;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.push.HuaweiPush;

public class QueryAgreementApi extends BaseApiAgent {
    private QueryAgreementHandler handler;

    public void onConnect(final int i, final HuaweiApiClient huaweiApiClient) {
        ThreadUtil.INST.excute(new Runnable() {
            public void run() {
                if (huaweiApiClient == null || !ApiClientMgr.INST.isConnect(huaweiApiClient)) {
                    HMSAgentLog.e("client not connted");
                    QueryAgreementApi.this.onQueryAgreementResult(i);
                    return;
                }
                HuaweiPush.HuaweiPushApi.queryAgreement(huaweiApiClient);
                QueryAgreementApi.this.onQueryAgreementResult(0);
            }
        });
    }

    /* access modifiers changed from: package-private */
    public void onQueryAgreementResult(int i) {
        HMSAgentLog.i("queryAgreement:callback=" + StrUtils.objDesc(this.handler) + " retCode=" + i);
        if (this.handler != null) {
            new Handler(Looper.getMainLooper()).post(new CallbackCodeRunnable(this.handler, i));
            this.handler = null;
        }
    }

    public void queryAgreement(QueryAgreementHandler queryAgreementHandler) {
        HMSAgentLog.i("queryAgreement:handler=" + StrUtils.objDesc(queryAgreementHandler));
        this.handler = queryAgreementHandler;
        connect();
    }
}
