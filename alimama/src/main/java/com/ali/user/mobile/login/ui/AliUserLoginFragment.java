package com.ali.user.mobile.login.ui;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.UIBaseConstants;
import com.ali.user.mobile.base.helper.BroadCastHelper;
import com.ali.user.mobile.common.api.AliUserLogin;
import com.ali.user.mobile.common.api.LoginApprearanceExtensions;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.LoginType;
import com.ali.user.mobile.login.action.LoginResActions;
import com.ali.user.mobile.login.model.LoginConstant;
import com.ali.user.mobile.login.presenter.FaceLoginPresenter;
import com.ali.user.mobile.login.presenter.RegionPresenter;
import com.ali.user.mobile.login.presenter.UserLoginPresenter;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.RegionInfo;
import com.ali.user.mobile.model.RegistParam;
import com.ali.user.mobile.navigation.NavigatorManager;
import com.ali.user.mobile.register.RegistConstants;
import com.ali.user.mobile.register.ui.AliUserRegisterChoiceRegionActivity;
import com.ali.user.mobile.register.ui.RegionDialogFragment;
import com.ali.user.mobile.register.ui.RegionListener;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.HistoryAccount;
import com.ali.user.mobile.rpc.LoginHistory;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.ali.user.mobile.service.FaceService;
import com.ali.user.mobile.service.NavigatorService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.ui.R;
import com.ali.user.mobile.ui.widget.BottomMenuFragment;
import com.ali.user.mobile.ui.widget.MenuItem;
import com.ali.user.mobile.ui.widget.MenuItemOnClickListener;
import com.ali.user.mobile.utils.CountryUtil;
import com.ali.user.mobile.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import com.taobao.weex.el.parse.Operators;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AliUserLoginFragment extends BaseLoginFragment implements UserLoginView {
    private static final String TAG = "AliUserLoginFragment";
    protected boolean isForceNormalMode = false;
    private boolean isInBindMode;
    protected View mAccountClearBtn;
    protected EditText mAccountET;
    protected TextView mAccountTV;
    protected boolean mActiveLogin = false;
    protected String mCurrentAccount;
    protected String mCurrentPassword;
    protected String mCurrentSelectedAccount;
    protected FaceLoginPresenter mFaceLoginPresenter;
    protected LinearLayout mFirstLoginLL;
    protected TextView mForgetPasswordTV;
    protected LinearLayout mHistoryLoginLL;
    protected Button mLoginBtn;
    protected LinearLayout mLoginBtnLL;
    protected LinearLayout mLoginLL;
    protected View mPasswordClearBtn;
    protected EditText mPasswordET;
    protected TextView mRegTV;
    protected RegionInfo mRegionInfo;
    protected RegionPresenter mRegionPresenter;
    protected TextView mRegionTV;
    protected ImageView mShowPasswordIV;
    protected TextView mSwitchFaceLoginBtn;
    protected TextView mSwitchMoreLoginBtn;
    protected TextView mSwitchSmsLoginBtn;
    protected TextWatcher mTextWatcherAccount = null;
    protected TextWatcher mTextWatcherPassword = null;
    public UserLoginPresenter mUserLoginPresenter;

    public void hideForSNS() {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initParams();
    }

    /* access modifiers changed from: protected */
    public void initParams() {
        Bundle arguments = getArguments();
        LoginParam loginParam = null;
        if (arguments != null) {
            String str = (String) arguments.get(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM);
            arguments.putString(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM, "");
            if (!TextUtils.isEmpty(str)) {
                loginParam = (LoginParam) JSON.parseObject(str, LoginParam.class);
            }
            this.isForceNormalMode = arguments.getBoolean(LoginConstant.FORCE_NORMAL_MODE);
        }
        this.mUserLoginPresenter = new UserLoginPresenter(this, loginParam);
        this.mRegionPresenter = new RegionPresenter(this);
        this.mFaceLoginPresenter = new FaceLoginPresenter(this, loginParam);
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.aliuser_fragment_user_login;
    }

    public void initViews(View view) {
        super.initViews(view);
        initTextWatcher();
        this.mLoginLL = (LinearLayout) view.findViewById(R.id.aliuser_user_login_ll);
        this.mAccountET = (EditText) view.findViewById(R.id.aliuser_login_account_et);
        this.mAccountET.setSingleLine();
        this.mAccountClearBtn = view.findViewById(R.id.aliuser_login_account_clear_iv);
        this.mHistoryLoginLL = (LinearLayout) view.findViewById(R.id.aliuser_login_history_ll);
        this.mFirstLoginLL = (LinearLayout) view.findViewById(R.id.aliuser_login_normal_ll);
        this.mAccountTV = (TextView) view.findViewById(R.id.aliuser_login_account_tv);
        this.mAccountET.addTextChangedListener(this.mTextWatcherAccount);
        this.mPasswordET = (EditText) view.findViewById(R.id.aliuser_login_password_et);
        this.mPasswordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.mPasswordET.addTextChangedListener(this.mTextWatcherPassword);
        this.mAccountET.setTypeface(Typeface.SANS_SERIF);
        this.mPasswordET.setTypeface(Typeface.SANS_SERIF);
        this.mPasswordClearBtn = view.findViewById(R.id.aliuser_login_password_clear_iv);
        this.mLoginBtn = (Button) view.findViewById(R.id.aliuser_login_login_btn);
        this.mLoginBtn.setEnabled(false);
        this.mForgetPasswordTV = (TextView) view.findViewById(R.id.aliuser_login_forgot_password_tv);
        this.mShowPasswordIV = (ImageView) view.findViewById(R.id.aliuser_login_show_password_btn);
        this.mRegionTV = (TextView) view.findViewById(R.id.aliuser_region_tv);
        initRegionInfo();
        this.mLoginBtnLL = (LinearLayout) view.findViewById(R.id.aliuser_login_login_btn_ll);
        this.mRegTV = (TextView) view.findViewById(R.id.aliuser_reg_tv);
        if (!(this.mRegTV == null || AliUserLogin.mAppreanceExtentions == null || AliUserLogin.mAppreanceExtentions.needRegister())) {
            this.mRegTV.setVisibility(8);
        }
        this.mSwitchSmsLoginBtn = (TextView) view.findViewById(R.id.aliuser_login_switch_smslogin);
        try {
            this.mSwitchMoreLoginBtn = (TextView) view.findViewById(R.id.aliuser_login_switch_more_login);
            this.mSwitchFaceLoginBtn = (TextView) view.findViewById(R.id.aliuser_login_switch_face_login);
        } catch (Throwable unused) {
        }
        this.mShowPasswordIV.setOnClickListener(this);
        setOnClickListener(this.mLoginBtn, this.mForgetPasswordTV, this.mRegionTV, this.mAccountClearBtn, this.mPasswordClearBtn, this.mRegTV, this.mSwitchSmsLoginBtn, this.mSwitchMoreLoginBtn, this.mSwitchFaceLoginBtn);
        this.mUserLoginPresenter.onStart();
        this.mRegionPresenter.onStart();
        initMode();
        showPushLogoutAlertIfHas();
        BroadCastHelper.sendLocalBroadCast(new Intent(LoginResActions.LOGIN_OPEN_ACTION));
    }

    /* access modifiers changed from: protected */
    public void initRegionInfo() {
        String str;
        if (this.mRegionTV != null && DataProviderFactory.getDataProvider().enableMobilePwdLogin()) {
            this.mRegionTV.setVisibility(0);
            RegionInfo currentRegion = DataProviderFactory.getDataProvider().getCurrentRegion();
            if (currentRegion == null || TextUtils.isEmpty(currentRegion.domain) || TextUtils.isEmpty(currentRegion.name) || TextUtils.isEmpty(currentRegion.code)) {
                if (currentRegion == null) {
                    str = "";
                } else {
                    str = currentRegion.domain;
                }
                currentRegion = CountryUtil.matchRegionFromLocal(getContext(), str);
            }
            if (currentRegion != null) {
                this.mRegionInfo = currentRegion;
                LoginApprearanceExtensions loginApprearanceExtensions = AliUserLogin.mAppreanceExtentions;
                if (loginApprearanceExtensions == null || loginApprearanceExtensions.needCountryModule()) {
                    this.mRegionTV.setVisibility(0);
                    this.mRegionTV.setText(this.mRegionInfo.code);
                } else {
                    this.mRegionTV.setVisibility(8);
                }
            }
            adjustMobileETMaxLength();
        }
    }

    /* access modifiers changed from: protected */
    public void initMode() {
        if (this.isForceNormalMode) {
            this.isHistoryMode = false;
            switchMode(this.isHistoryMode, (HistoryAccount) null);
        } else if (!this.mUserLoginActivity.hadReadHistory) {
            readAccountFromHistory();
        } else if (this.mUserLoginActivity.mHistoryAccount != null) {
            this.isHistoryMode = true;
            switchToHistoryMode(this.mUserLoginActivity.mHistoryAccount);
        } else {
            this.isHistoryMode = false;
            switchMode(this.isHistoryMode, (HistoryAccount) null);
        }
    }

    private void initTextWatcher() {
        initAccountWatcher();
        this.mTextWatcherPassword = new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (AliUserLoginFragment.this.mPasswordClearBtn != null) {
                    if (charSequence == null || charSequence.length() == 0) {
                        if (AliUserLoginFragment.this.mPasswordClearBtn.getVisibility() != 8) {
                            AliUserLoginFragment.this.mPasswordClearBtn.setVisibility(8);
                        }
                    } else if (AliUserLoginFragment.this.mPasswordClearBtn.getVisibility() != 0) {
                        AliUserLoginFragment.this.mPasswordClearBtn.setVisibility(0);
                    }
                }
                if (AliUserLoginFragment.this.mPasswordET != null) {
                    AliUserLoginFragment.this.checkSignInable();
                }
            }
        };
    }

    private void initAccountWatcher() {
        this.mTextWatcherAccount = new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (AliUserLoginFragment.this.mAccountClearBtn != null) {
                    if (charSequence == null || charSequence.length() == 0) {
                        if (AliUserLoginFragment.this.mAccountClearBtn.getVisibility() != 8) {
                            AliUserLoginFragment.this.mAccountClearBtn.setVisibility(8);
                        }
                    } else if (AliUserLoginFragment.this.mAccountClearBtn.getVisibility() != 0 && AliUserLoginFragment.this.mAccountClearBtn.isEnabled()) {
                        AliUserLoginFragment.this.mAccountClearBtn.setVisibility(0);
                    }
                }
                AliUserLoginFragment.this.checkSignInable();
            }
        };
    }

    public void setLoginAccountInfo(String str) {
        this.mAccountET.setText(str);
    }

    public void clearPasswordInput() {
        this.mPasswordET.setText("");
    }

    /* access modifiers changed from: protected */
    public String getAccountName() {
        if (this.isHistoryMode) {
            return this.mCurrentSelectedAccount;
        }
        if (this.mRegionInfo == null || "+86".equals(this.mRegionInfo.code)) {
            return this.mAccountET.getText().toString().trim().replaceAll(Operators.SPACE_STR, "");
        }
        return this.mRegionInfo.code.replace("+", "") + "-" + this.mAccountET.getText().toString().trim().replaceAll(Operators.SPACE_STR, "");
    }

    /* access modifiers changed from: protected */
    public void checkSignInable() {
        String obj = this.mAccountET.getText().toString();
        if (this.isHistoryMode) {
            obj = this.mAccountTV.getText().toString();
        }
        boolean z = !TextUtils.isEmpty(obj) && !TextUtils.isEmpty(this.mPasswordET.getText().toString());
        this.mLoginBtn.setEnabled(z);
        if (!z || !DataProviderFactory.getDataProvider().isTaobaoApp()) {
            this.mLoginBtnLL.setBackgroundDrawable((Drawable) null);
        } else {
            this.mLoginBtnLL.setBackgroundResource(R.drawable.aliuser_btn_shadow);
        }
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.aliuser_login_login_btn) {
            this.mActiveLogin = true;
            UserTrackAdapter.sendControlUT(getPageName(), "Button-Login");
            onLoginAction();
        } else if (id == R.id.aliuser_login_forgot_password_tv) {
            UserTrackAdapter.sendControlUT(getPageName(), "Button-ResetPwd");
            onForgetPasswordAction();
        } else if (id == R.id.aliuser_login_account_clear_iv) {
            onClearAccountBtnClickAction();
        } else if (id == R.id.aliuser_login_password_clear_iv) {
            this.mPasswordET.getEditableText().clear();
        } else if (id == R.id.aliuser_login_show_password_btn) {
            int selectionEnd = this.mPasswordET.getSelectionEnd();
            if (view.getTag() == null || !((Boolean) view.getTag()).booleanValue()) {
                this.mPasswordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                this.mShowPasswordIV.setImageResource(R.drawable.aliuser_ic_visibility);
                this.mShowPasswordIV.setContentDescription(getString(R.string.aliuser_assist_password_show));
                view.setTag(true);
                UserTrackAdapter.sendControlUT(getPageName(), "Button-ShowPwd");
            } else {
                this.mPasswordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                this.mShowPasswordIV.setImageResource(R.drawable.aliuser_ic_visibility_off);
                this.mShowPasswordIV.setContentDescription(getString(R.string.aliuser_assist_password_hide));
                view.setTag(false);
            }
            if (selectionEnd > 0) {
                this.mPasswordET.setSelection(selectionEnd);
            }
            this.mPasswordET.postInvalidate();
        } else if (id == R.id.aliuser_login_switch_smslogin) {
            UserTrackAdapter.sendControlUT(getPageName(), "Button-ChooseSMSLogin");
            switchToSmsLogin();
        } else if (id == R.id.aliuser_login_switch_face_login) {
            UserTrackAdapter.sendControlUT(getPageName(), "Button-ChooseFaceLogin");
            faceLogin();
        } else if (id == R.id.aliuser_login_switch_more_login) {
            UserTrackAdapter.sendControlUT(getPageName(), "Button-ChooseMoreLogin");
            showMoreLoginBottomMenu();
        } else if (id == R.id.aliuser_region_tv) {
            UserTrackAdapter.sendControlUT(getPageName(), "Button-Region");
            if (DataProviderFactory.getDataProvider().useRegionFragment()) {
                applyRegion();
                return;
            }
            Intent intent = new Intent(this.mAttachedActivity, AliUserRegisterChoiceRegionActivity.class);
            intent.putExtra("from_login", true);
            this.mAttachedActivity.startActivityForResult(intent, 2001);
        } else {
            super.onClick(view);
        }
    }

    /* access modifiers changed from: protected */
    public void applyRegion() {
        this.mRegionPresenter.region(0);
    }

    /* access modifiers changed from: protected */
    public void onClearAccountBtnClickAction() {
        if (DataProviderFactory.getDataProvider().isShowHistoryFragment() || TextUtils.isEmpty(this.mAccountET.getText()) || this.mAccountET.isEnabled()) {
            this.mAccountET.getEditableText().clear();
            this.mAccountET.setEnabled(true);
            return;
        }
        onDeleteAccount();
        this.mAccountET.setEnabled(true);
    }

    /* access modifiers changed from: protected */
    public void showBottomMenu() {
        if (isActive() && isVisible()) {
            BottomMenuFragment bottomMenuFragment = new BottomMenuFragment();
            ArrayList arrayList = new ArrayList();
            MenuItem menuItem = new MenuItem();
            menuItem.setText(getString(R.string.aliuser_other_account_login));
            menuItem.setMenuItemOnClickListener(new MenuItemOnClickListener(bottomMenuFragment, menuItem) {
                public void onClickMenuItem(View view, MenuItem menuItem) {
                    UserTrackAdapter.sendControlUT(AliUserLoginFragment.this.getPageName(), "Button-ChooseOtherAccountLogin");
                    Intent intent = new Intent();
                    intent.putExtra(LoginConstant.FORCE_NORMAL_MODE, true);
                    if (!(AliUserLoginFragment.this.mUserLoginPresenter == null || AliUserLoginFragment.this.mUserLoginPresenter.getLoginParam() == null)) {
                        LoginParam loginParam = new LoginParam();
                        loginParam.source = AliUserLoginFragment.this.mUserLoginPresenter.getLoginParam().source;
                        intent.putExtra(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM, JSON.toJSONString(loginParam));
                    }
                    AliUserLoginFragment.this.mUserLoginActivity.gotoPwdLoginFragment(intent);
                }
            });
            MenuItem menuItem2 = new MenuItem();
            menuItem2.setText(getString(R.string.aliuser_reg));
            menuItem2.setMenuItemOnClickListener(new MenuItemOnClickListener(bottomMenuFragment, menuItem2) {
                public void onClickMenuItem(View view, MenuItem menuItem) {
                    UserTrackAdapter.sendControlUT(AliUserLoginFragment.this.getPageName(), "Button-Reg");
                    RegistParam registParam = new RegistParam();
                    registParam.registSite = AliUserLoginFragment.this.getLoginSite();
                    ((NavigatorService) ServiceFactory.getService(NavigatorService.class)).openRegisterPage(AliUserLoginFragment.this.mAttachedActivity, registParam);
                }
            });
            arrayList.add(menuItem);
            if (AliUserLogin.mAppreanceExtentions != null && AliUserLogin.mAppreanceExtentions.needRegister()) {
                arrayList.add(menuItem2);
            }
            if (AliUserLogin.mAppreanceExtentions == null || AliUserLogin.mAppreanceExtentions.needHelp()) {
                MenuItem menuItem3 = new MenuItem();
                menuItem3.setText(getString(R.string.aliuser_help));
                menuItem3.setMenuItemOnClickListener(new MenuItemOnClickListener(bottomMenuFragment, menuItem3) {
                    public void onClickMenuItem(View view, MenuItem menuItem) {
                        if (AliUserLoginFragment.this.isActive()) {
                            AliUserLoginFragment.this.openHelp();
                        }
                    }
                });
                arrayList.add(menuItem3);
            }
            bottomMenuFragment.setMenuItems(arrayList);
            bottomMenuFragment.show(getFragmentManager(), getPageName());
        }
    }

    /* access modifiers changed from: protected */
    public void showMoreLoginBottomMenu() {
        if (isActive()) {
            BottomMenuFragment bottomMenuFragment = new BottomMenuFragment();
            ArrayList arrayList = new ArrayList();
            MenuItem menuItem = new MenuItem();
            menuItem.setText(getString(R.string.aliuser_login_sms_login));
            menuItem.setMenuItemOnClickListener(new MenuItemOnClickListener(bottomMenuFragment, menuItem) {
                public void onClickMenuItem(View view, MenuItem menuItem) {
                    UserTrackAdapter.sendControlUT(AliUserLoginFragment.this.getPageName(), "Button-ChooseOtherAccountLogin");
                    AliUserLoginFragment.this.switchToSmsLogin();
                }
            });
            MenuItem menuItem2 = new MenuItem();
            menuItem2.setText(getString(R.string.aliuser_scan_login_text));
            menuItem2.setMenuItemOnClickListener(new MenuItemOnClickListener(bottomMenuFragment, menuItem2) {
                public void onClickMenuItem(View view, MenuItem menuItem) {
                    UserTrackAdapter.sendControlUT(AliUserLoginFragment.this.getPageName(), "Button-FaceLogin");
                    AliUserLoginFragment.this.faceLogin();
                }
            });
            arrayList.add(menuItem);
            arrayList.add(menuItem2);
            bottomMenuFragment.setMenuItems(arrayList);
            bottomMenuFragment.show(getFragmentManager(), getPageName());
        }
    }

    /* access modifiers changed from: protected */
    public void faceLogin() {
        if (ServiceFactory.getService(FaceService.class) != null && this.mFaceLoginPresenter != null) {
            LoginParam loginParam = new LoginParam();
            loginParam.havanaId = this.mUserLoginActivity.mHistoryAccount.userId;
            loginParam.deviceTokenKey = this.mUserLoginActivity.mHistoryAccount.tokenKey;
            this.mFaceLoginPresenter.activeFaceLogin(loginParam);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 != -1 || i != 2001) {
            this.mUserLoginPresenter.onActivityResult(i, i2, intent);
        } else if (intent != null) {
            this.mRegionInfo = (RegionInfo) intent.getParcelableExtra(RegistConstants.REGION_INFO);
            if (this.mRegionInfo != null && this.mRegionTV != null) {
                this.mRegionTV.setText(this.mRegionInfo.code);
                adjustMobileETMaxLength();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void adjustMobileETMaxLength() {
        if (this.mRegionInfo == null || !TextUtils.equals("CN", this.mRegionInfo.domain) || !DataProviderFactory.getDataProvider().enableMobilePwdLogin()) {
            this.mAccountET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
            if (Build.VERSION.SDK_INT >= 21) {
                initAccountWatcher();
                this.mAccountET.addTextChangedListener(this.mTextWatcherAccount);
                return;
            }
            return;
        }
        this.mAccountET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        if (Build.VERSION.SDK_INT >= 21) {
            this.mTextWatcherAccount = new MobileTextWatcher(this.mAccountET, Locale.CHINA.getCountry());
            this.mAccountET.addTextChangedListener(this.mTextWatcherAccount);
        }
    }

    @TargetApi(21)
    public class MobileTextWatcher extends PhoneNumberFormattingTextWatcher {
        private WeakReference<EditText> editText;

        private MobileTextWatcher(EditText editText2, String str) {
            super(str);
            this.editText = new WeakReference<>(editText2);
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (((EditText) this.editText.get()).getId() == R.id.aliuser_login_mobile_et && AliUserLoginFragment.this.mAccountClearBtn != null) {
                if (charSequence == null || charSequence.length() == 0) {
                    if (AliUserLoginFragment.this.mAccountClearBtn.getVisibility() != 8) {
                        AliUserLoginFragment.this.mAccountClearBtn.setVisibility(8);
                    }
                } else if (AliUserLoginFragment.this.mAccountClearBtn.getVisibility() != 0 && AliUserLoginFragment.this.mAccountClearBtn.isEnabled()) {
                    AliUserLoginFragment.this.mAccountClearBtn.setVisibility(0);
                }
            }
            AliUserLoginFragment.this.checkSignInable();
        }
    }

    /* access modifiers changed from: protected */
    public void switchToSmsLogin() {
        Intent intent = new Intent();
        intent.putExtra(LoginConstant.FORCE_NORMAL_MODE, !this.isHistoryMode);
        if (!this.isHistoryMode) {
            String obj = this.mAccountET.getText().toString();
            if (isMobileValid(obj)) {
                intent.putExtra(LoginConstant.ACCOUNT, obj);
            }
        }
        if (!(this.mUserLoginPresenter == null || this.mUserLoginPresenter.getLoginParam() == null)) {
            LoginParam loginParam = new LoginParam();
            loginParam.source = this.mUserLoginPresenter.getLoginParam().source;
            intent.putExtra(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM, JSON.toJSONString(loginParam));
        }
        this.mUserLoginActivity.gotoMobileLoginFragment(intent);
    }

    /* access modifiers changed from: protected */
    public boolean isMobileValid(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        return str.matches("^1[0-9]{10}$");
    }

    /* access modifiers changed from: protected */
    public void onLoginAction() {
        this.mCurrentAccount = getAccountName();
        this.mCurrentPassword = this.mPasswordET.getText().toString().trim();
        if (TextUtils.isEmpty(this.mCurrentAccount)) {
            showErrorMessage(R.string.aliuser_sign_in_account_hint);
        } else if (TextUtils.isEmpty(this.mCurrentPassword)) {
            showErrorMessage(R.string.aliuser_sign_in_please_enter_password);
        } else {
            if (this.mActivityHelper != null) {
                this.mActivityHelper.hideInputMethod();
            }
            this.mUserLoginPresenter.buildLoginParam(this.mCurrentAccount, this.mCurrentPassword);
            this.mUserLoginPresenter.login();
        }
    }

    private void onForgetPasswordAction() {
        String accountName = getAccountName();
        if (!this.isHistoryMode || this.mUserLoginActivity.mHistoryAccount == null) {
            this.mUserLoginPresenter.fetchUrlAndToWebView(this.mAttachedActivity, accountName);
        } else if (this.mUserLoginActivity.mHistoryAccount.alipayHid != 0) {
            alert("", this.mAttachedActivity.getResources().getString(R.string.aliuser_alipay_findpwd), this.mAttachedActivity.getResources().getString(R.string.aliuser_confirm), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    AliUserLoginFragment.this.mActivityHelper.dismissAlertDialog();
                }
            }, (String) null, (DialogInterface.OnClickListener) null);
        } else if (TextUtils.isEmpty(this.mUserLoginActivity.mHistoryAccount.tokenKey) || getLoginSite() != 0) {
            this.mUserLoginPresenter.fetchUrlAndToWebView(this.mAttachedActivity, accountName);
        } else {
            this.mUserLoginPresenter.fetchUrlAndToWebViewWithUserId(this.mAttachedActivity, accountName, this.mUserLoginActivity.mHistoryAccount.userId);
        }
    }

    /* access modifiers changed from: protected */
    public void showErrorMessage(int i) {
        toast(getString(i), 0);
    }

    public void showFindPasswordAlert(final LoginParam loginParam, RpcResponse<LoginReturnData> rpcResponse) {
        AnonymousClass9 r6;
        String str;
        String str2;
        if (rpcResponse == null || TextUtils.isEmpty(rpcResponse.codeGroup) || loginParam == null || TextUtils.isEmpty(loginParam.loginType) || ((!TextUtils.equals(ApiConstants.CodeGroup.PWD_ERROR, rpcResponse.codeGroup) && !TextUtils.equals("noRecord", rpcResponse.codeGroup)) || TextUtils.equals(LoginType.ALIPAY_ACCOUNT.getType(), loginParam.loginType))) {
            str = null;
            r6 = null;
        } else {
            str = getResources().getString(R.string.aliuser_alert_findpwd);
            r6 = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    UserTrackAdapter.sendControlUT(AliUserLoginFragment.this.getPageName(), "Button-Alert-ResetPwd");
                    AliUserLoginFragment.this.mUserLoginPresenter.fetchUrlAndToWebView(AliUserLoginFragment.this.mAttachedActivity, loginParam.loginAccount);
                    AliUserLoginFragment.this.dismissAlertDialog();
                }
            };
        }
        String string = getResources().getString(R.string.aliuser_common_ok);
        if (rpcResponse == null) {
            str2 = "";
        } else {
            str2 = rpcResponse.message;
        }
        alert("", str2, str, r6, string, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                AliUserLoginFragment.this.dismissAlertDialog();
                AliUserLoginFragment.this.onPwdError();
            }
        });
    }

    public void onNeedVerification(String str, int i) {
        NavigatorManager.getInstance().navToVerificationPage(getActivity(), str, i);
    }

    public LoginType getLoginType() {
        return LoginType.TAOBAO_ACCOUNT;
    }

    public int getLoginSite() {
        if (!this.isHistoryMode || this.mUserLoginActivity.mHistoryAccount == null) {
            return DataProviderFactory.getDataProvider().getSite();
        }
        return this.mUserLoginActivity.mHistoryAccount.getLoginSite();
    }

    public void onSuccess(LoginParam loginParam, RpcResponse rpcResponse) {
        dismissLoading();
        this.mUserLoginPresenter.onLoginSuccess(loginParam, rpcResponse);
    }

    public void onError(RpcResponse rpcResponse) {
        this.mUserLoginPresenter.onLoginFail(rpcResponse);
    }

    public void onPwdError() {
        this.mPasswordET.setText("");
    }

    private void readAccountFromHistory() {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, LoginHistory>() {
            /* access modifiers changed from: protected */
            public LoginHistory doInBackground(Object... objArr) {
                return SecurityGuardManagerWraper.getLoginHistory();
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(LoginHistory loginHistory) {
                if (AliUserLoginFragment.this.mUserLoginActivity != null) {
                    AliUserLoginFragment.this.mUserLoginActivity.hadReadHistory = true;
                    if (loginHistory == null || loginHistory.accountHistory == null || loginHistory.accountHistory.size() <= 0) {
                        AliUserLoginFragment.this.isHistoryMode = false;
                        AliUserLoginFragment.this.switchMode(AliUserLoginFragment.this.isHistoryMode, (HistoryAccount) null);
                        return;
                    }
                    AliUserLoginFragment.this.isHistoryMode = true;
                    if (AliUserLoginFragment.this.mUserLoginPresenter.getLoginParam() == null || (AliUserLoginFragment.this.mUserLoginPresenter.getLoginParam() != null && TextUtils.isEmpty(AliUserLoginFragment.this.mUserLoginPresenter.getLoginParam().loginAccount))) {
                        int i = loginHistory.index;
                        if (i < 0 || i >= loginHistory.accountHistory.size()) {
                            i = loginHistory.accountHistory.size() - 1;
                        }
                        AliUserLoginFragment.this.mUserLoginActivity.mHistoryAccount = loginHistory.accountHistory.get(i);
                        AliUserLoginFragment.this.switchToHistoryMode(AliUserLoginFragment.this.mUserLoginActivity.mHistoryAccount);
                    }
                }
            }
        }, new Object[0]);
    }

    /* access modifiers changed from: protected */
    public void switchToHistoryMode(HistoryAccount historyAccount) {
        if (isActivityAvaiable()) {
            this.mCurrentSelectedAccount = historyAccount.userInputName;
            String hideAccount = StringUtil.hideAccount(this.mCurrentSelectedAccount);
            if (!TextUtils.isEmpty(hideAccount)) {
                this.mAccountTV.setText(hideAccount);
                updateAvatar(historyAccount.headImg);
                switchMode(this.isHistoryMode, historyAccount);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void switchMode(boolean z, HistoryAccount historyAccount) {
        boolean z2 = true;
        if (z) {
            this.mFirstLoginLL.setVisibility(8);
            this.mHistoryLoginLL.setVisibility(0);
            boolean z3 = historyAccount != null && !TextUtils.isEmpty(historyAccount.loginPhone) && historyAccount.alipayHid == 0 && DataProviderFactory.getDataProvider().supportMobileLogin();
            if (!DataProviderFactory.getDataProvider().supportFaceLogin() || (!this.mUserLoginActivity.isFaceLoginEnvEnable && !this.mUserLoginActivity.isFaceLoginActivate)) {
                z2 = false;
            }
            if (z3 && z2) {
                this.mSwitchSmsLoginBtn.setVisibility(8);
                this.mSwitchMoreLoginBtn.setVisibility(0);
                this.mSwitchFaceLoginBtn.setVisibility(8);
                ((RelativeLayout.LayoutParams) this.mSwitchMoreLoginBtn.getLayoutParams()).addRule(14, -1);
            } else if (z3) {
                this.mSwitchSmsLoginBtn.setVisibility(0);
                this.mSwitchMoreLoginBtn.setVisibility(8);
                this.mSwitchFaceLoginBtn.setVisibility(8);
                ((RelativeLayout.LayoutParams) this.mSwitchSmsLoginBtn.getLayoutParams()).addRule(14, -1);
            } else if (z2) {
                this.mSwitchSmsLoginBtn.setVisibility(8);
                this.mSwitchMoreLoginBtn.setVisibility(8);
                this.mSwitchFaceLoginBtn.setVisibility(0);
                ((RelativeLayout.LayoutParams) this.mSwitchFaceLoginBtn.getLayoutParams()).addRule(14, -1);
            } else {
                this.mSwitchSmsLoginBtn.setVisibility(8);
                this.mSwitchMoreLoginBtn.setVisibility(8);
                this.mSwitchFaceLoginBtn.setVisibility(8);
            }
            this.mRegTV.setVisibility(8);
        } else {
            this.mFirstLoginLL.setVisibility(0);
            this.mHistoryLoginLL.setVisibility(8);
            if (DataProviderFactory.getDataProvider().supportMobileLogin()) {
                this.mSwitchSmsLoginBtn.setVisibility(0);
                ((RelativeLayout.LayoutParams) this.mSwitchSmsLoginBtn.getLayoutParams()).addRule(9);
            } else {
                this.mSwitchSmsLoginBtn.setVisibility(8);
            }
            this.mSwitchMoreLoginBtn.setVisibility(8);
            this.mSwitchFaceLoginBtn.setVisibility(8);
            if (!(this.mRegTV == null || AliUserLogin.mAppreanceExtentions == null || !AliUserLogin.mAppreanceExtentions.needRegister())) {
                this.mRegTV.setVisibility(0);
            }
            if (!(DataProviderFactory.getDataProvider().isShowHistoryFragment() || this.mUserLoginActivity == null || this.mAccountET == null)) {
                if (this.mUserLoginActivity.mHistoryAccount == null || this.mUserLoginActivity.mHistoryAccount.hasPwd != 1) {
                    this.mAccountET.setText("");
                    this.mAccountET.setEnabled(true);
                    this.mAccountET.requestFocus();
                } else {
                    this.mAccountET.setText(this.mUserLoginActivity.mHistoryAccount.userInputName);
                    this.mAccountET.setEnabled(false);
                }
            }
        }
        if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }

    /* access modifiers changed from: protected */
    public String getPageName() {
        return this.isHistoryMode ? ApiConstants.UTConstants.UT_PAGE_HISTORY_LOGIN : ApiConstants.UTConstants.UT_PAGE_FIRST_LOGIN;
    }

    public void setSnsToken(String str) {
        this.isInBindMode = true;
        if (this.mUserLoginPresenter != null) {
            this.mUserLoginPresenter.setSnsToken(str);
        }
    }

    public void onDestroyView() {
        this.mAccountET.removeTextChangedListener(this.mTextWatcherAccount);
        this.mPasswordET.removeTextChangedListener(this.mTextWatcherPassword);
        super.onDestroyView();
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mUserLoginPresenter != null) {
            this.mUserLoginPresenter.onDestory();
        }
    }

    public boolean isHistoryMode() {
        return this.isHistoryMode;
    }

    public void onGetRegion(List<RegionInfo> list) {
        if (isActive()) {
            RegionDialogFragment regionDialogFragment = new RegionDialogFragment();
            regionDialogFragment.setList(list);
            regionDialogFragment.setRegionListener(new RegionListener() {
                public void onClick(RegionInfo regionInfo) {
                    AliUserLoginFragment.this.mRegionInfo = regionInfo;
                    if (AliUserLoginFragment.this.mRegionInfo != null) {
                        AliUserLoginFragment.this.mRegionTV.setText(AliUserLoginFragment.this.mRegionInfo.code);
                        AliUserLoginFragment.this.adjustMobileETMaxLength();
                    }
                }
            });
            regionDialogFragment.setCurrentRegion(this.mRegionInfo);
            regionDialogFragment.setupAdapter(getActivity());
            regionDialogFragment.show(getActivity().getSupportFragmentManager(), "UserRegionDialog");
        }
    }
}
