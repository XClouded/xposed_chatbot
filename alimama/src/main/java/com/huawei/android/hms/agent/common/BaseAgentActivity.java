package com.huawei.android.hms.agent.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

public class BaseAgentActivity extends Activity {
    public static final String EXTRA_IS_FULLSCREEN = "should_be_fullscreen";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestActivityTransparent();
    }

    private void requestActivityTransparent() {
        try {
            Intent intent = getIntent();
            if (intent != null && intent.getBooleanExtra(EXTRA_IS_FULLSCREEN, false)) {
                getWindow().setFlags(1024, 1024);
            }
            requestWindowFeature(1);
            Window window = getWindow();
            if (window != null) {
                window.addFlags(67108864);
            }
        } catch (Exception e) {
            HMSAgentLog.w("requestActivityTransparent exception:" + e.getMessage());
        }
    }
}
