package com.ali.user.mobile.login.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.ali.user.mobile.base.ui.BaseActivity;
import com.ali.user.mobile.login.action.LoginResActions;

public class BaseAccountActivity extends BaseActivity {
    protected boolean isLoginObserver;
    private BroadcastReceiver mLoginReceiver;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (Build.VERSION.SDK_INT >= 11) {
            getWindow().addFlags(8192);
        }
        if (this.isLoginObserver) {
            this.mLoginReceiver = new BroadcastReceiver() {
                public void onReceive(Context context, Intent intent) {
                    if (LoginResActions.LOGIN_CANCEL_ACTION.equals(intent.getAction())) {
                        BaseAccountActivity.this.finish();
                    }
                }
            };
            LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(this.mLoginReceiver, new IntentFilter(LoginResActions.LOGIN_CANCEL_ACTION));
        }
    }

    public void onDestroy() {
        if (this.mLoginReceiver != null) {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(this.mLoginReceiver);
            this.mLoginReceiver = null;
        }
        super.onDestroy();
    }
}
