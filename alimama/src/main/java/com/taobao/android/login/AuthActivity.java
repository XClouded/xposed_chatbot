package com.taobao.android.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.ali.user.mobile.app.constant.FragmentConstant;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.helper.BroadCastHelper;
import com.ali.user.mobile.base.ui.BaseActivity;
import com.ali.user.mobile.common.api.AliUserLogin;
import com.ali.user.mobile.common.api.LoginApprearanceExtensions;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.login.action.LoginResActions;
import com.ali.user.mobile.login.model.LoginConstant;
import com.ali.user.mobile.model.LoginParam;
import com.alibaba.fastjson.JSON;
import com.taobao.android.sso.R;
import com.taobao.login4android.constants.LoginStatus;
import com.taobao.statistic.TBS;
import com.ut.mini.UTAnalytics;

public class AuthActivity extends BaseActivity {
    private static final String TAG = "login.AuthActivity";
    protected FragmentManager mFragmentManager;
    protected LoginParam mLoginParam;
    private long startTime;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        TLogAdapter.d(TAG, "AuthActivity onCreate: " + System.currentTimeMillis());
        this.isLoginObserver = true;
        getWindow().setFlags(1024, 1024);
        UTAnalytics.getInstance().getDefaultTracker().skipPage(this);
        this.startTime = System.currentTimeMillis();
        if (bundle != null) {
            String string = bundle.getString(LoginConstant.KEY_LOGIN_PARAM);
            if (!TextUtils.isEmpty(string)) {
                try {
                    this.mLoginParam = (LoginParam) JSON.parseObject(string, LoginParam.class);
                } catch (Exception unused) {
                }
            }
        }
        this.mFragmentManager = getSupportFragmentManager();
        super.onCreate(bundle);
        openFragment(getIntent());
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.user_login_fragment;
    }

    public void setOrientation() {
        if (DataProviderFactory.getDataProvider().getOrientation() != 0) {
            if (getResources().getConfiguration().orientation == 2) {
                setRequestedOrientation(1);
            }
            getWindow().setSoftInputMode(18);
        } else if (getResources().getConfiguration().orientation == 1) {
            setRequestedOrientation(0);
        }
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        openFragment(intent);
    }

    /* access modifiers changed from: protected */
    public void openFragment(Intent intent) {
        Fragment fragment;
        LoginApprearanceExtensions loginApprearanceExtensions = AliUserLogin.mAppreanceExtentions;
        if (loginApprearanceExtensions != null) {
            Class<?> fullyCustomizedAuthFragment = loginApprearanceExtensions.getFullyCustomizedAuthFragment();
            if (fullyCustomizedAuthFragment != null) {
                try {
                    fragment = (Fragment) fullyCustomizedAuthFragment.newInstance();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            } else {
                Log.e(TAG, "null customized fragment");
                fragment = null;
            }
            if (fragment == null) {
                fragment = new AuthFragment();
            }
            Fragment findFragmentByTag = this.mFragmentManager.findFragmentByTag(FragmentConstant.AUTH_FRAGMENT_TAG);
            if (findFragmentByTag != null) {
                this.mFragmentManager.beginTransaction().remove(findFragmentByTag).commitAllowingStateLoss();
            }
            this.mFragmentManager.beginTransaction().add(R.id.loginContainer, fragment, FragmentConstant.AUTH_FRAGMENT_TAG).commitAllowingStateLoss();
        }
    }

    public static Intent getCallingIntent(Context context, String str) {
        Intent intent = new Intent(context, AuthActivity.class);
        intent.putExtra(LoginConstant.KEY_LOGIN_PARAM, str);
        return intent;
    }

    public void finishCurrentAndNotify() {
        dismissProgressDialog();
        try {
            finish();
        } catch (Throwable unused) {
        }
        LoginStatus.resetLoginFlag();
        BroadCastHelper.sendLocalBroadCast(new Intent(LoginResActions.LOGIN_CANCEL_ACTION));
    }

    public void onBackPressed() {
        TBS.Page.buttonClicked("Button_back");
        finishCurrentAndNotify();
    }
}
