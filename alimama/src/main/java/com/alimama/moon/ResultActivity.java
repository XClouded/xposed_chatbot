package com.alimama.moon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.Toast;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.info.AlipayInfo;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.login.action.LoginResActions;
import com.ali.user.mobile.login.model.LoginConstant;
import com.ali.user.mobile.model.LoginParam;
import com.alimama.moon.ui.PageRouterActivity;
import com.taobao.android.TBSsoLogin;
import com.taobao.android.sso.v2.launch.ILoginListener;
import com.taobao.android.sso.v2.launch.SsoLogin;
import com.taobao.android.sso.v2.launch.exception.SSOException;
import com.taobao.android.sso.v2.launch.model.ISsoRemoteParam;

public class ResultActivity extends PageRouterActivity implements ILoginListener {
    private BroadcastReceiver mLoginReceiver;
    private boolean needToast = true;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mLoginReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (LoginResActions.LOGIN_SUCCESS_ACTION.equals(intent.getAction())) {
                    ResultActivity.this.finish();
                } else if (LoginResActions.LOGIN_FAIL_ACTION.equals(intent.getAction())) {
                    ResultActivity.this.finish();
                } else if (LoginResActions.LOGIN_NETWORK_ERROR.equals(intent.getAction())) {
                    ResultActivity.this.finish();
                } else if (LoginResActions.WEB_ACTIVITY_CANCEL.equals(intent.getAction())) {
                    ResultActivity.this.finish();
                }
            }
        };
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(LoginResActions.LOGIN_SUCCESS_ACTION);
        intentFilter.addAction(LoginResActions.LOGIN_FAIL_ACTION);
        intentFilter.addAction(LoginResActions.LOGIN_NETWORK_ERROR);
        intentFilter.addAction(LoginResActions.WEB_ACTIVITY_CANCEL);
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(this.mLoginReceiver, intentFilter);
        SsoLogin.handleResultIntent(this, getIntent());
    }

    /* access modifiers changed from: protected */
    public void onDestroy() {
        super.onDestroy();
        if (this.mLoginReceiver != null) {
            LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(this.mLoginReceiver);
        }
    }

    public void onSuccess(Intent intent) {
        TBSsoLogin.login(this, intent.getExtras(), new ISsoRemoteParam() {
            public String getAtlas() {
                return null;
            }

            public String getDeviceId() {
                return null;
            }

            public String getImei() {
                return null;
            }

            public String getImsi() {
                return null;
            }

            public String getServerTime() {
                return null;
            }

            public String getUmidToken() {
                return AppInfo.getInstance().getUmidToken();
            }

            public String getTtid() {
                return DataProviderFactory.getDataProvider().getTTID();
            }

            public String getAppKey() {
                return DataProviderFactory.getDataProvider().getAppkey();
            }

            public String getApdid() {
                return AlipayInfo.getInstance().getApdid();
            }
        });
    }

    public void onFail(SSOException sSOException) {
        if (this.needToast) {
            Toast.makeText(getApplicationContext(), "手淘免登失败", 0).show();
        }
        finish();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i != 257) {
            return;
        }
        if (i2 != 258 && i2 != 0 && i2 != 259) {
            return;
        }
        if (intent != null) {
            LoginParam loginParam = (LoginParam) intent.getSerializableExtra("loginParam");
            if (loginParam == null || loginParam.externParams == null || !LoginConstant.ACTION_CONTINUELOGIN.equals(loginParam.externParams.get(LoginConstant.EXT_ACTION))) {
                finish();
            } else {
                TBSsoLogin.loginAfterH5(this, loginParam);
            }
        } else {
            finish();
        }
    }
}
