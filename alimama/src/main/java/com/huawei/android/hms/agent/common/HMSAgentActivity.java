package com.huawei.android.hms.agent.common;

import android.content.Intent;
import android.os.Bundle;
import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.hms.api.HuaweiApiAvailability;

public class HMSAgentActivity extends BaseAgentActivity {
    public static final String CONN_ERR_CODE_TAG = "HMSConnectionErrorCode";
    public static final String EXTRA_RESULT = "intent.extra.RESULT";
    private static final int REQUEST_HMS_RESOLVE_ERROR = 1000;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ApiClientMgr.INST.onActivityLunched();
        Intent intent = getIntent();
        if (intent != null) {
            int intExtra = intent.getIntExtra(CONN_ERR_CODE_TAG, 0);
            HMSAgentLog.d("dispose code:" + intExtra);
            HuaweiApiAvailability.getInstance().resolveError(this, intExtra, 1000);
            return;
        }
        HMSAgentLog.e("intent is null");
        finish();
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1000) {
            if (i2 == -1) {
                int intExtra = intent.getIntExtra("intent.extra.RESULT", -1);
                HMSAgentLog.d("dispose result:" + intExtra);
                ApiClientMgr.INST.onResolveErrorRst(intExtra);
            } else {
                HMSAgentLog.e("dispose error:" + i2);
                ApiClientMgr.INST.onResolveErrorRst(HMSAgent.AgentResultCode.ON_ACTIVITY_RESULT_ERROR);
            }
            finish();
        }
    }
}
