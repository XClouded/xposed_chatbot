package com.huawei.android.hms.agent.common;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import com.huawei.android.hms.agent.common.handler.CheckUpdateHandler;
import com.huawei.hms.api.CheckUpdatelistener;
import com.huawei.hms.api.HuaweiApiClient;

public class CheckUpdateApi extends BaseApiAgent implements CheckUpdatelistener {
    private Activity activity;
    private CheckUpdateHandler handler;

    public void onConnect(int i, HuaweiApiClient huaweiApiClient) {
        HMSAgentLog.d("onConnect:" + i);
        Activity lastActivity = ActivityMgr.INST.getLastActivity();
        if (lastActivity != null && huaweiApiClient != null) {
            huaweiApiClient.checkUpdate(lastActivity, this);
        } else if (this.activity == null || huaweiApiClient == null) {
            HMSAgentLog.e("no activity to checkUpdate");
            onCheckUpdateResult(-1001);
        } else {
            huaweiApiClient.checkUpdate(this.activity, this);
        }
    }

    public void onResult(int i) {
        onCheckUpdateResult(i);
    }

    private void onCheckUpdateResult(int i) {
        HMSAgentLog.i("checkUpdate:callback=" + StrUtils.objDesc(this.handler) + " retCode=" + i);
        if (this.handler != null) {
            new Handler(Looper.getMainLooper()).post(new CallbackCodeRunnable(this.handler, i));
            this.handler = null;
        }
        this.activity = null;
    }

    public void checkUpdate(Activity activity2, CheckUpdateHandler checkUpdateHandler) {
        HMSAgentLog.i("checkUpdate:handler=" + StrUtils.objDesc(checkUpdateHandler));
        this.handler = checkUpdateHandler;
        this.activity = activity2;
        connect();
    }
}
