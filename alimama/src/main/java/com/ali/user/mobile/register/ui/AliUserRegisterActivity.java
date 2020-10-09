package com.ali.user.mobile.register.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.fragment.app.Fragment;
import com.ali.user.mobile.app.constant.FragmentConstant;
import com.ali.user.mobile.app.dataprovider.DataProvider;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.helper.BroadCastHelper;
import com.ali.user.mobile.base.ui.BaseActivity;
import com.ali.user.mobile.base.ui.BaseFragment;
import com.ali.user.mobile.common.api.AliUserLogin;
import com.ali.user.mobile.common.api.LoginApprearanceExtensions;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.action.LoginResActions;
import com.ali.user.mobile.rpc.register.model.RegisterCountryModel;
import com.ali.user.mobile.ui.R;
import com.ali.user.mobile.utils.UTConstans;
import com.taobao.login4android.log.LoginTLogAdapter;
import com.taobao.login4android.session.SessionManager;
import com.taobao.orange.OrangeConfig;

public class AliUserRegisterActivity extends BaseActivity {
    public static final String CONFIG_GROUP_LOGIN = "login4android";
    public static final String NEW_REGISTER_PERCENT = "new_register_percent";
    private static final String TAG = "login.AliUserRegister";
    private RegisterCountryModel mCountryData;
    private Fragment mCurrentFragment;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, AliUserRegisterActivity.class);
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        this.isLoginObserver = true;
        try {
            initParam(getIntent());
        } catch (Throwable th) {
            th.printStackTrace();
        }
        UserTrackAdapter.skipPage(this);
        super.onCreate(bundle);
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        try {
            initParam(getIntent());
            initViews();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void initParam(Intent intent) {
        DataProvider dataProvider = (DataProvider) DataProviderFactory.getDataProvider();
        if (dataProvider.getDefaultCountry() != null) {
            this.mCountryData = new RegisterCountryModel();
            this.mCountryData.countryName = dataProvider.getDefaultCountry().countryName;
            this.mCountryData.countryCode = dataProvider.getDefaultCountry().countryCode;
            this.mCountryData.areaCode = dataProvider.getDefaultCountry().areaCode;
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        try {
            UserTrackAdapter.pageDisAppear(this);
            super.onPause();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.aliuser_activity_frame_content;
    }

    /* access modifiers changed from: protected */
    public void initViews() {
        try {
            if (AliUserLogin.mAppreanceExtentions != null && !AliUserLogin.mAppreanceExtentions.isNeedToolbar()) {
                getSupportActionBar().hide();
            }
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.aliuser_signup_page_title);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        changeFragmentByConfig(getIntent());
    }

    public void changeFragmentByConfig(Intent intent) {
        try {
            Fragment mobileRegisterFragment = getMobileRegisterFragment(AliUserLogin.mAppreanceExtentions);
            if (intent != null) {
                mobileRegisterFragment.setArguments(intent.getExtras());
            }
            this.mCurrentFragment = mobileRegisterFragment;
            Fragment findFragmentByTag = getSupportFragmentManager().findFragmentByTag(FragmentConstant.REG_FRAGMENT_TAG);
            if (findFragmentByTag != null) {
                getSupportFragmentManager().beginTransaction().remove(findFragmentByTag).commitAllowingStateLoss();
            }
            getSupportFragmentManager().beginTransaction().add(R.id.aliuser_content_frame, this.mCurrentFragment, FragmentConstant.REG_FRAGMENT_TAG).commitAllowingStateLoss();
            getSupportFragmentManager().beginTransaction().show(this.mCurrentFragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Fragment getMobileRegisterFragment(LoginApprearanceExtensions loginApprearanceExtensions) throws InstantiationException, IllegalAccessException {
        int i = getSwitch(NEW_REGISTER_PERCENT, -1);
        int abs = Math.abs(AppInfo.getInstance().getUtdid().hashCode()) % 10000;
        if (SessionManager.isDebug()) {
            TLogAdapter.e(TAG, "random num = " + abs + ",percent=" + i);
        }
        if (abs < i) {
            if (loginApprearanceExtensions == null || loginApprearanceExtensions.getFullyCustomizeTwoStepMobileRegisterFragment() == null) {
                return new AliUserNumAuthRegisterFragment();
            }
            return (Fragment) loginApprearanceExtensions.getFullyCustomizeTwoStepMobileRegisterFragment().newInstance();
        } else if (loginApprearanceExtensions == null || loginApprearanceExtensions.getFullyCustomizeMobileRegisterFragment() == null) {
            return new AliUserMobileRegisterFragment();
        } else {
            return (Fragment) loginApprearanceExtensions.getFullyCustomizeMobileRegisterFragment().newInstance();
        }
    }

    public void gotoSmsCodeFragment(Intent intent) {
        AliUserRegisterSMSVerificationFragment aliUserRegisterSMSVerificationFragment;
        try {
            LoginApprearanceExtensions loginApprearanceExtensions = AliUserLogin.mAppreanceExtentions;
            if (loginApprearanceExtensions == null || loginApprearanceExtensions.getFullyCustomizedSmsCodeFragment() == null) {
                aliUserRegisterSMSVerificationFragment = new AliUserRegisterSMSVerificationFragment();
            } else {
                aliUserRegisterSMSVerificationFragment = (AliUserRegisterSMSVerificationFragment) loginApprearanceExtensions.getFullyCustomizeLoginFragment().newInstance();
            }
            aliUserRegisterSMSVerificationFragment.setArguments(intent.getExtras());
            Fragment findFragmentByTag = getSupportFragmentManager().findFragmentByTag(FragmentConstant.REG_FRAGMENT_TAG);
            if (findFragmentByTag != null) {
                getSupportFragmentManager().beginTransaction().remove(findFragmentByTag).commitAllowingStateLoss();
            }
            this.mCurrentFragment = aliUserRegisterSMSVerificationFragment;
            getSupportFragmentManager().beginTransaction().add(R.id.aliuser_content_frame, this.mCurrentFragment, FragmentConstant.REG_SMSCODE_FRAGMENT_TAG).commitAllowingStateLoss();
            getSupportFragmentManager().beginTransaction().show(this.mCurrentFragment).commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int getSwitch(String str, int i) {
        if (TextUtils.isEmpty(str)) {
            return i;
        }
        try {
            OrangeConfig instance = OrangeConfig.getInstance();
            String config = instance.getConfig("login4android", str, i + "");
            LoginTLogAdapter.e(TAG, "LoginSwitch:getSwitch, switchName=" + str + ", value=" + config);
            return Integer.parseInt(config);
        } catch (Throwable th) {
            th.printStackTrace();
            LoginTLogAdapter.e(TAG, "LoginSwitch:getSwitch, switchName=" + str, th);
            return i;
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        this.mCurrentFragment.onActivityResult(i, i2, intent);
        super.onActivityResult(i, i2, intent);
    }

    public void onBackPressed() {
        finishCurrentAndNotify();
    }

    public void finishCurrentAndNotify() {
        if (getSupportFragmentManager() == null || getSupportFragmentManager().isDestroyed() || this.mCurrentFragment == null || !((BaseFragment) this.mCurrentFragment).onBackPressed()) {
            try {
                UserTrackAdapter.sendControlUT("Page_Reg", UTConstans.Controls.UT_BTN_BACK);
                BroadCastHelper.sendLocalBroadCast(new Intent(LoginResActions.REG_CANCEL));
                finish();
            } catch (Throwable unused) {
            }
        }
    }
}
