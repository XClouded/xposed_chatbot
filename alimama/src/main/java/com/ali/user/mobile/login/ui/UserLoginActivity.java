package com.ali.user.mobile.login.ui;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import com.ali.user.mobile.app.constant.FragmentConstant;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.UIBaseConstants;
import com.ali.user.mobile.base.helper.BroadCastHelper;
import com.ali.user.mobile.base.ui.BaseActivity;
import com.ali.user.mobile.base.ui.BaseFragment;
import com.ali.user.mobile.common.api.AliUserLogin;
import com.ali.user.mobile.common.api.LoginApprearanceExtensions;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.filter.LoginFilterCallback;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.action.LoginResActions;
import com.ali.user.mobile.login.model.AppLaunchInfo;
import com.ali.user.mobile.login.model.LoginConstant;
import com.ali.user.mobile.login.model.PreCheckResult;
import com.ali.user.mobile.login.service.impl.UserLoginServiceImpl;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.rpc.HistoryAccount;
import com.ali.user.mobile.rpc.LoginHistory;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.ali.user.mobile.service.FaceService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.ui.R;
import com.ali.user.mobile.utils.SharedPreferencesUtil;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.taobao.login4android.constants.LoginStatus;
import com.taobao.login4android.session.SessionManager;
import com.taobao.statistic.TBS;
import java.util.List;
import java.util.Map;

public class UserLoginActivity extends BaseActivity {
    public static final String RESET_LOGIN_STATUS = "NOTIFY_LOGIN_STATUS_RESET";
    private static final String TAG = "login.UserLoginActivity";
    private long endOpenTime;
    public boolean hadReadHistory = false;
    public boolean isFaceLoginActivate;
    public boolean isFaceLoginEnvEnable;
    private boolean isOpenMobileLoginPage;
    protected String mCurrentFragmentTag = FragmentConstant.PWD_LOGIN_FRAGMENT_TAG;
    protected FragmentManager mFragmentManager;
    public HistoryAccount mHistoryAccount;
    public boolean mOpenGuide;
    public boolean mUserOpenFaceLogin = false;
    /* access modifiers changed from: private */
    public long startOpenTime;
    private long startTime;

    /* access modifiers changed from: protected */
    public boolean isShowToolbarInFragment() {
        return false;
    }

    public void onRestoreInstanceState(Bundle bundle) {
    }

    public void onSaveInstanceState(Bundle bundle) {
    }

    public UserLoginActivity() {
        this.mOpenGuide = LoginStatus.enableSsoAlways ? true : DataProviderFactory.getDataProvider().getAppInfoFromServer();
        this.isFaceLoginEnvEnable = false;
        this.isFaceLoginActivate = false;
        this.isOpenMobileLoginPage = false;
    }

    public static Intent getCallingIntent(Context context, String str, boolean z, boolean z2) {
        Intent intent = new Intent(context, UserLoginActivity.class);
        intent.putExtra(LoginConstant.LAUCNH_MOBILE_LOGIN_FRAGMENT_LABEL, z);
        intent.putExtra(LoginConstant.LAUNCH_PASS_GUIDE_FRAGMENT, z2);
        intent.putExtra(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM, str);
        return intent;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        TLogAdapter.d(TAG, UmbrellaConstants.LIFECYCLE_CREATE);
        initParam(getIntent());
        try {
            if (Build.VERSION.SDK_INT >= 26) {
                getWindow().getDecorView().setImportantForAutofill(8);
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        super.onCreate(bundle);
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initParam(intent);
        openFragmentByConfig(intent);
    }

    private void initParam(Intent intent) {
        this.isLoginObserver = true;
        UserTrackAdapter.skipPage(this);
        this.startTime = System.currentTimeMillis();
        if (intent != null) {
            this.isOpenMobileLoginPage = intent.getBooleanExtra(LoginConstant.LAUCNH_MOBILE_LOGIN_FRAGMENT_LABEL, false);
        }
        this.mFragmentManager = getSupportFragmentManager();
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.aliuser_activity_frame_content;
    }

    /* access modifiers changed from: protected */
    public void initViews() {
        super.initViews();
        try {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle((CharSequence) "");
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        openFragmentByConfig(getIntent());
    }

    /* access modifiers changed from: protected */
    public boolean isShowNavIcon() {
        return AliUserLogin.mAppreanceExtentions == null || AliUserLogin.mAppreanceExtentions.needLoginBackButton();
    }

    public void openFragmentByConfig(final Intent intent) {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, LoginHistory>() {
            /* access modifiers changed from: protected */
            public void onPreExecute() {
                super.onPreExecute();
                UserLoginActivity.this.showProgress("");
            }

            /* access modifiers changed from: protected */
            public LoginHistory doInBackground(Object... objArr) {
                LoginHistory loginHistory = SecurityGuardManagerWraper.getLoginHistory();
                boolean z = true;
                UserLoginActivity.this.hadReadHistory = true;
                if (loginHistory == null || loginHistory.accountHistory == null || loginHistory.accountHistory.size() <= 0) {
                    UserLoginActivity.this.mHistoryAccount = null;
                } else {
                    int i = loginHistory.index;
                    if (i < 0 || i >= loginHistory.accountHistory.size()) {
                        i = loginHistory.accountHistory.size() - 1;
                    }
                    UserLoginActivity.this.mHistoryAccount = loginHistory.accountHistory.get(i);
                }
                long unused = UserLoginActivity.this.startOpenTime = System.currentTimeMillis();
                try {
                    if (UserLoginActivity.this.mHistoryAccount != null && DataProviderFactory.getDataProvider().supportFaceLogin()) {
                        LoginParam loginParam = new LoginParam();
                        loginParam.havanaId = UserLoginActivity.this.mHistoryAccount.userId;
                        loginParam.loginSite = DataProviderFactory.getDataProvider().getSite();
                        RpcResponse precheckScanLogin = UserLoginServiceImpl.getInstance().precheckScanLogin(loginParam);
                        if (!(precheckScanLogin == null || precheckScanLogin.returnValue == null)) {
                            UserLoginActivity.this.isFaceLoginActivate = ((PreCheckResult) precheckScanLogin.returnValue).verify;
                            UserLoginActivity.this.isFaceLoginEnvEnable = ((PreCheckResult) precheckScanLogin.returnValue).preCheckVerify;
                        }
                    }
                    UserLoginActivity userLoginActivity = UserLoginActivity.this;
                    if (ServiceFactory.getService(FaceService.class) == null || UserLoginActivity.this.mHistoryAccount == null || !UserLoginActivity.this.isFaceLoginActivate) {
                        z = false;
                    }
                    userLoginActivity.mUserOpenFaceLogin = z;
                    UserLoginActivity.this.setOpenGuide();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
                return loginHistory;
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(LoginHistory loginHistory) {
                UserLoginActivity.this.dismissProgressDialog();
                UserLoginActivity.this.openFragmentByIntent(intent);
            }
        }, new Object[0]);
    }

    /* access modifiers changed from: private */
    public void setOpenGuide() {
        try {
            if (DataProviderFactory.getDataProvider().getAppInfoFromServer() && LoginStatus.askServerForGuide) {
                RpcResponse appLaunchInfo = UserLoginServiceImpl.getInstance().getAppLaunchInfo(new LoginParam());
                if (appLaunchInfo == null || appLaunchInfo.returnValue == null) {
                    this.mOpenGuide = false;
                    return;
                }
                this.mOpenGuide = ((AppLaunchInfo) appLaunchInfo.returnValue).fromOversea;
                DataProviderFactory.getDataProvider().setAppInfoFromServer(this.mOpenGuide);
                LoginStatus.askServerForGuide = false;
            }
        } catch (Throwable th) {
            th.printStackTrace();
            this.mOpenGuide = false;
        }
    }

    /* access modifiers changed from: private */
    public void openFragmentByIntent(Intent intent) {
        try {
            this.endOpenTime = System.currentTimeMillis();
            TLogAdapter.d(TAG, "open login activity delta = " + (this.endOpenTime - this.startOpenTime));
            if (!this.mUserOpenFaceLogin || this.mHistoryAccount == null || intent == null || intent.getBooleanExtra(LoginConstant.FORCE_NOT_FACE, false) || AliUserLogin.mAppreanceExtentions == null || AliUserLogin.mAppreanceExtentions.getFullyCustomizedFaceLoginFragment() == null) {
                LoginApprearanceExtensions loginApprearanceExtensions = AliUserLogin.mAppreanceExtentions;
                if (loginApprearanceExtensions == null || loginApprearanceExtensions.getFullyCustomizeGuideFragment() == null || !this.mOpenGuide || intent == null || intent.getBooleanExtra(LoginConstant.LAUNCH_PASS_GUIDE_FRAGMENT, false)) {
                    goPwdOrSMSFragment(intent);
                } else {
                    gotoGuideFragment(loginApprearanceExtensions);
                }
            } else {
                goFaceFragment(intent);
            }
        } catch (Throwable unused) {
        }
    }

    public void goPwdOrSMSFragment(Intent intent) {
        String str = (String) SharedPreferencesUtil.getData(getApplicationContext(), LoginConstant.LOGIN_TYPE, "");
        if (this.mHistoryAccount != null) {
            if (TextUtils.equals(str, LoginConstant.LOGIN_TYPE_SMS) && DataProviderFactory.getDataProvider().supportMobileLogin()) {
                gotoMobileLoginFragment(intent);
            } else if (this.isOpenMobileLoginPage && DataProviderFactory.getDataProvider().supportMobileLogin()) {
                if (this.mHistoryAccount.hasPwd != 0) {
                    intent.putExtra(LoginConstant.FORCE_NORMAL_MODE, true);
                }
                gotoMobileLoginFragment(intent);
            } else if (TextUtils.equals(str, "password") && DataProviderFactory.getDataProvider().supportPwdLogin()) {
                gotoPwdLoginFragment(intent);
            } else if (DataProviderFactory.getDataProvider().isSmsLoginPriority() && !TextUtils.isEmpty(this.mHistoryAccount.loginPhone) && DataProviderFactory.getDataProvider().supportMobileLogin()) {
                gotoMobileLoginFragment(intent);
            } else if (this.mHistoryAccount.hasPwd != 0 || !DataProviderFactory.getDataProvider().supportMobileLogin()) {
                gotoPwdLoginFragment(intent);
            } else {
                gotoMobileLoginFragment(intent);
            }
        } else if (TextUtils.equals(str, "password") && DataProviderFactory.getDataProvider().supportPwdLogin()) {
            gotoPwdLoginFragment(intent);
        } else if (TextUtils.equals(str, LoginConstant.LOGIN_TYPE_SMS) && DataProviderFactory.getDataProvider().supportMobileLogin()) {
            gotoMobileLoginFragment(intent);
        } else if (!TextUtils.isEmpty(SessionManager.getInstance(getApplicationContext()).getOldUserId()) && DataProviderFactory.getDataProvider().supportPwdLogin()) {
            gotoPwdLoginFragment(intent);
        } else if (DataProviderFactory.getDataProvider().isSmsLoginPriority() && DataProviderFactory.getDataProvider().supportMobileLogin()) {
            gotoMobileLoginFragment(intent);
        } else if (!this.isOpenMobileLoginPage || !DataProviderFactory.getDataProvider().supportMobileLogin()) {
            gotoPwdLoginFragment(intent);
        } else {
            gotoMobileLoginFragment(intent);
        }
    }

    private void gotoGuideFragment(LoginApprearanceExtensions loginApprearanceExtensions) {
        try {
            addFragment((Fragment) loginApprearanceExtensions.getFullyCustomizeGuideFragment().newInstance(), FragmentConstant.GUIDE_FRAGMENT_TAG);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goFaceFragment(Intent intent) {
        try {
            Fragment fragment = (Fragment) AliUserLogin.mAppreanceExtentions.getFullyCustomizedFaceLoginFragment().newInstance();
            if (intent != null) {
                fragment.setArguments(intent.getExtras());
            }
            addFragment(fragment, FragmentConstant.FACE_LOGIN_FRAGMENT_TAG);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0062 A[Catch:{ Exception -> 0x0088 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void gotoMobileLoginFragment(android.content.Intent r7) {
        /*
            r6 = this;
            com.ali.user.mobile.common.api.LoginApprearanceExtensions r0 = com.ali.user.mobile.common.api.AliUserLogin.mAppreanceExtentions     // Catch:{ Exception -> 0x0088 }
            if (r0 == 0) goto L_0x0015
            java.lang.Class r1 = r0.getFullyCustomizeMobileLoginFragment()     // Catch:{ Exception -> 0x0088 }
            if (r1 == 0) goto L_0x0015
            java.lang.Class r0 = r0.getFullyCustomizeMobileLoginFragment()     // Catch:{ Exception -> 0x0088 }
            java.lang.Object r0 = r0.newInstance()     // Catch:{ Exception -> 0x0088 }
            com.ali.user.mobile.login.ui.AliUserMobileLoginFragment r0 = (com.ali.user.mobile.login.ui.AliUserMobileLoginFragment) r0     // Catch:{ Exception -> 0x0088 }
            goto L_0x001a
        L_0x0015:
            com.ali.user.mobile.login.ui.AliUserMobileLoginFragment r0 = new com.ali.user.mobile.login.ui.AliUserMobileLoginFragment     // Catch:{ Exception -> 0x0088 }
            r0.<init>()     // Catch:{ Exception -> 0x0088 }
        L_0x001a:
            android.os.Bundle r1 = new android.os.Bundle     // Catch:{ Exception -> 0x0088 }
            r1.<init>()     // Catch:{ Exception -> 0x0088 }
            java.lang.String r2 = "forceNormalMode"
            r3 = 0
            boolean r2 = r7.getBooleanExtra(r2, r3)     // Catch:{ Exception -> 0x0088 }
            r4 = 1
            if (r2 != 0) goto L_0x0036
            com.ali.user.mobile.app.dataprovider.IDataProvider r2 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getDataProvider()     // Catch:{ Exception -> 0x0088 }
            boolean r2 = r2.isShowHistoryFragment()     // Catch:{ Exception -> 0x0088 }
            if (r2 != 0) goto L_0x0034
            goto L_0x0036
        L_0x0034:
            r2 = 0
            goto L_0x0037
        L_0x0036:
            r2 = 1
        L_0x0037:
            com.ali.user.mobile.rpc.HistoryAccount r5 = r6.mHistoryAccount     // Catch:{ Exception -> 0x0088 }
            if (r5 == 0) goto L_0x0046
            com.ali.user.mobile.rpc.HistoryAccount r5 = r6.mHistoryAccount     // Catch:{ Exception -> 0x0088 }
            java.lang.String r5 = r5.loginPhone     // Catch:{ Exception -> 0x0088 }
            boolean r5 = android.text.TextUtils.isEmpty(r5)     // Catch:{ Exception -> 0x0088 }
            if (r5 == 0) goto L_0x0046
            r2 = 1
        L_0x0046:
            java.lang.String r4 = "forceNormalMode"
            r1.putBoolean(r4, r2)     // Catch:{ Exception -> 0x0088 }
            java.lang.String r2 = "account"
            java.lang.String r4 = "account"
            java.lang.String r4 = r7.getStringExtra(r4)     // Catch:{ Exception -> 0x0088 }
            r1.putString(r2, r4)     // Catch:{ Exception -> 0x0088 }
            java.lang.String r2 = "PARAM_LOGIN_PARAM"
            java.lang.String r2 = r7.getStringExtra(r2)     // Catch:{ Exception -> 0x0088 }
            boolean r2 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x0088 }
            if (r2 != 0) goto L_0x007f
            java.lang.String r2 = "ut_from_register"
            java.lang.String r4 = "ut_from_register"
            boolean r3 = r7.getBooleanExtra(r4, r3)     // Catch:{ Exception -> 0x0088 }
            r1.putBoolean(r2, r3)     // Catch:{ Exception -> 0x0088 }
            java.lang.String r2 = "startTime"
            long r3 = r6.startTime     // Catch:{ Exception -> 0x0088 }
            r1.putLong(r2, r3)     // Catch:{ Exception -> 0x0088 }
            java.lang.String r2 = "PARAM_LOGIN_PARAM"
            java.lang.String r3 = "PARAM_LOGIN_PARAM"
            java.lang.String r7 = r7.getStringExtra(r3)     // Catch:{ Exception -> 0x0088 }
            r1.putString(r2, r7)     // Catch:{ Exception -> 0x0088 }
        L_0x007f:
            r0.setArguments(r1)     // Catch:{ Exception -> 0x0088 }
            java.lang.String r7 = "aliuser_mobile_login"
            r6.addFragment(r0, r7)     // Catch:{ Exception -> 0x0088 }
            goto L_0x008c
        L_0x0088:
            r7 = move-exception
            r7.printStackTrace()
        L_0x008c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.login.ui.UserLoginActivity.gotoMobileLoginFragment(android.content.Intent):void");
    }

    /* JADX WARNING: Removed duplicated region for block: B:21:0x0053 A[Catch:{ Exception -> 0x0079 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void gotoPwdLoginFragment(android.content.Intent r7) {
        /*
            r6 = this;
            com.ali.user.mobile.common.api.LoginApprearanceExtensions r0 = com.ali.user.mobile.common.api.AliUserLogin.mAppreanceExtentions     // Catch:{ Exception -> 0x0079 }
            if (r0 == 0) goto L_0x0015
            java.lang.Class r1 = r0.getFullyCustomizeLoginFragment()     // Catch:{ Exception -> 0x0079 }
            if (r1 == 0) goto L_0x0015
            java.lang.Class r0 = r0.getFullyCustomizeLoginFragment()     // Catch:{ Exception -> 0x0079 }
            java.lang.Object r0 = r0.newInstance()     // Catch:{ Exception -> 0x0079 }
            com.ali.user.mobile.login.ui.AliUserLoginFragment r0 = (com.ali.user.mobile.login.ui.AliUserLoginFragment) r0     // Catch:{ Exception -> 0x0079 }
            goto L_0x001a
        L_0x0015:
            com.ali.user.mobile.login.ui.AliUserLoginFragment r0 = new com.ali.user.mobile.login.ui.AliUserLoginFragment     // Catch:{ Exception -> 0x0079 }
            r0.<init>()     // Catch:{ Exception -> 0x0079 }
        L_0x001a:
            android.os.Bundle r1 = new android.os.Bundle     // Catch:{ Exception -> 0x0079 }
            r1.<init>()     // Catch:{ Exception -> 0x0079 }
            java.lang.String r2 = "forceNormalMode"
            r3 = 0
            boolean r2 = r7.getBooleanExtra(r2, r3)     // Catch:{ Exception -> 0x0079 }
            r4 = 1
            if (r2 != 0) goto L_0x0036
            com.ali.user.mobile.app.dataprovider.IDataProvider r2 = com.ali.user.mobile.app.dataprovider.DataProviderFactory.getDataProvider()     // Catch:{ Exception -> 0x0079 }
            boolean r2 = r2.isShowHistoryFragment()     // Catch:{ Exception -> 0x0079 }
            if (r2 != 0) goto L_0x0034
            goto L_0x0036
        L_0x0034:
            r2 = 0
            goto L_0x0037
        L_0x0036:
            r2 = 1
        L_0x0037:
            com.ali.user.mobile.rpc.HistoryAccount r5 = r6.mHistoryAccount     // Catch:{ Exception -> 0x0079 }
            if (r5 == 0) goto L_0x0042
            com.ali.user.mobile.rpc.HistoryAccount r5 = r6.mHistoryAccount     // Catch:{ Exception -> 0x0079 }
            int r5 = r5.hasPwd     // Catch:{ Exception -> 0x0079 }
            if (r5 != 0) goto L_0x0042
            r2 = 1
        L_0x0042:
            java.lang.String r4 = "forceNormalMode"
            r1.putBoolean(r4, r2)     // Catch:{ Exception -> 0x0079 }
            java.lang.String r2 = "PARAM_LOGIN_PARAM"
            java.lang.String r2 = r7.getStringExtra(r2)     // Catch:{ Exception -> 0x0079 }
            boolean r2 = android.text.TextUtils.isEmpty(r2)     // Catch:{ Exception -> 0x0079 }
            if (r2 != 0) goto L_0x0070
            java.lang.String r2 = "ut_from_register"
            java.lang.String r4 = "ut_from_register"
            boolean r3 = r7.getBooleanExtra(r4, r3)     // Catch:{ Exception -> 0x0079 }
            r1.putBoolean(r2, r3)     // Catch:{ Exception -> 0x0079 }
            java.lang.String r2 = "startTime"
            long r3 = r6.startTime     // Catch:{ Exception -> 0x0079 }
            r1.putLong(r2, r3)     // Catch:{ Exception -> 0x0079 }
            java.lang.String r2 = "PARAM_LOGIN_PARAM"
            java.lang.String r3 = "PARAM_LOGIN_PARAM"
            java.lang.String r7 = r7.getStringExtra(r3)     // Catch:{ Exception -> 0x0079 }
            r1.putString(r2, r7)     // Catch:{ Exception -> 0x0079 }
        L_0x0070:
            r0.setArguments(r1)     // Catch:{ Exception -> 0x0079 }
            java.lang.String r7 = "aliuser_pwd_login"
            r6.addFragment(r0, r7)     // Catch:{ Exception -> 0x0079 }
            goto L_0x007d
        L_0x0079:
            r7 = move-exception
            r7.printStackTrace()
        L_0x007d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.login.ui.UserLoginActivity.gotoPwdLoginFragment(android.content.Intent):void");
    }

    public void gotoCheckAuthFragment(Intent intent) {
        AliUserCheckAuthFragment aliUserCheckAuthFragment;
        try {
            LoginApprearanceExtensions loginApprearanceExtensions = AliUserLogin.mAppreanceExtentions;
            if (loginApprearanceExtensions == null || loginApprearanceExtensions.getFullyCustomizedAuthCheckFragment() == null) {
                aliUserCheckAuthFragment = new AliUserCheckAuthFragment();
            } else {
                aliUserCheckAuthFragment = (AliUserCheckAuthFragment) loginApprearanceExtensions.getFullyCustomizedAuthFragment().newInstance();
            }
            Bundle bundle = new Bundle();
            if (!TextUtils.isEmpty(intent.getStringExtra(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM))) {
                bundle.putString(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM, intent.getStringExtra(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM));
            }
            aliUserCheckAuthFragment.setArguments(bundle);
            addFragment(aliUserCheckAuthFragment, FragmentConstant.PWD_AUTH_WITH_FIXED_NICK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AliUserLoginFragment gotoLoginFragmentFromGuide(Intent intent) {
        Fragment findFragmentByTag = this.mFragmentManager.findFragmentByTag(FragmentConstant.GUIDE_FRAGMENT_TAG);
        if (findFragmentByTag != null && findFragmentByTag.isVisible()) {
            this.mFragmentManager.beginTransaction().hide(findFragmentByTag).commitAllowingStateLoss();
        }
        gotoPwdLoginFragment(intent);
        this.mFragmentManager.executePendingTransactions();
        Fragment findFragmentByTag2 = this.mFragmentManager.findFragmentByTag("aliuser_login");
        if (findFragmentByTag2 == null || !(findFragmentByTag2 instanceof AliUserLoginFragment)) {
            return null;
        }
        return (AliUserLoginFragment) findFragmentByTag2;
    }

    public void gotoFastRegOrLoginBind(Intent intent) {
        AliUserSNSChooseFragment aliUserSNSChooseFragment;
        try {
            LoginApprearanceExtensions loginApprearanceExtensions = AliUserLogin.mAppreanceExtentions;
            if (loginApprearanceExtensions == null || loginApprearanceExtensions.getFullyCustomizedAuthCheckFragment() == null) {
                aliUserSNSChooseFragment = new AliUserSNSChooseFragment();
            } else {
                aliUserSNSChooseFragment = (AliUserSNSChooseFragment) loginApprearanceExtensions.getFullyCustomizedSNSChooseFragment().newInstance();
            }
            Bundle bundle = new Bundle();
            if (!TextUtils.isEmpty(intent.getStringExtra(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM))) {
                bundle.putString(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM, intent.getStringExtra(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM));
            }
            aliUserSNSChooseFragment.setArguments(bundle);
            addFragment(aliUserSNSChooseFragment, FragmentConstant.SNS_FAST_REG_OR_LOGIN_BIND);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public AliUserCheckAuthFragment gotoAuthCheckFragmentFromGuide(Intent intent) {
        Fragment findFragmentByTag = this.mFragmentManager.findFragmentByTag(FragmentConstant.GUIDE_FRAGMENT_TAG);
        if (findFragmentByTag != null && findFragmentByTag.isVisible()) {
            this.mFragmentManager.beginTransaction().hide(findFragmentByTag).commitAllowingStateLoss();
        }
        gotoCheckAuthFragment(intent);
        this.mFragmentManager.executePendingTransactions();
        Fragment findFragmentByTag2 = this.mFragmentManager.findFragmentByTag(FragmentConstant.PWD_AUTH_WITH_FIXED_NICK);
        if (findFragmentByTag2 == null || !(findFragmentByTag2 instanceof AliUserCheckAuthFragment)) {
            return null;
        }
        return (AliUserCheckAuthFragment) findFragmentByTag2;
    }

    private void addFragment(Fragment fragment, String str) {
        hideAllFragment();
        Fragment findFragmentByTag = this.mFragmentManager.findFragmentByTag(str);
        if (findFragmentByTag != null) {
            this.mFragmentManager.beginTransaction().remove(findFragmentByTag).commitAllowingStateLoss();
        }
        this.mFragmentManager.beginTransaction().add(R.id.aliuser_content_frame, fragment, str).commitAllowingStateLoss();
        this.mFragmentManager.beginTransaction().show(fragment).commit();
        this.mCurrentFragmentTag = str;
    }

    private void hideAllFragment() {
        List<String> fragmentTagList = FragmentConstant.getFragmentTagList();
        if (fragmentTagList != null) {
            for (String findFragmentByTag : fragmentTagList) {
                Fragment findFragmentByTag2 = this.mFragmentManager.findFragmentByTag(findFragmentByTag);
                if (findFragmentByTag2 != null) {
                    this.mFragmentManager.beginTransaction().hide(findFragmentByTag2).commitAllowingStateLoss();
                }
            }
        }
    }

    private void sendCancelBroadcast() {
        BroadCastHelper.sendLocalBroadCast(new Intent("NOTIFY_LOGIN_STATUS_RESET"));
    }

    public void finishCurrentAndNotify() {
        if (this.mFragmentManager != null && !this.mFragmentManager.isDestroyed()) {
            if (!this.supportTaobaoOrAlipay || TextUtils.equals(this.mCurrentFragmentTag, FragmentConstant.GUIDE_FRAGMENT_TAG) || !this.mOpenGuide) {
                Fragment findFragmentByTag = this.mFragmentManager.findFragmentByTag(this.mCurrentFragmentTag);
                if (findFragmentByTag != null && (findFragmentByTag instanceof BaseFragment) && ((BaseFragment) findFragmentByTag).onBackPressed()) {
                    return;
                }
            } else {
                gotoGuideFragment(AliUserLogin.mAppreanceExtentions);
                return;
            }
        }
        BroadCastHelper.sendLocalBroadCast(new Intent(LoginResActions.LOGIN_CANCEL_ACTION));
        dismissProgressDialog();
        try {
            finish();
        } catch (Throwable unused) {
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        Fragment findFragmentByTag = this.mFragmentManager.findFragmentByTag(this.mCurrentFragmentTag);
        if (findFragmentByTag != null && findFragmentByTag.isVisible()) {
            findFragmentByTag.onActivityResult(i, i2, intent);
        }
    }

    /* access modifiers changed from: protected */
    public void finishWhenLoginSuccess() {
        if (AliUserLogin.mLoginFilter != null) {
            AliUserLogin.mLoginFilter.onLoginSuccess(this, new LoginFilterCallback() {
                public void onSuccess() {
                    UserLoginActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            Log.e(UserLoginActivity.TAG, "finish after filter.onLoginSuccess");
                            UserLoginActivity.this.finish();
                        }
                    });
                }

                public void onFail(int i, Map<String, String> map) {
                    UserLoginActivity.this.runOnUiThread(new Runnable() {
                        public void run() {
                            UserLoginActivity.this.finish();
                        }
                    });
                }
            });
        } else {
            finish();
        }
    }

    public void onBackPressed() {
        TBS.Page.buttonClicked("Button_back");
        finishCurrentAndNotify();
    }

    /* access modifiers changed from: protected */
    public void onStart() {
        try {
            super.onStart();
        } catch (Throwable th) {
            th.printStackTrace();
            finish();
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
    public void onStop() {
        try {
            super.onStop();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    public void onDestroy() {
        try {
            sendCancelBroadcast();
            this.mFragmentManager = null;
            this.hadReadHistory = false;
            this.mHistoryAccount = null;
            super.onDestroy();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }
}
