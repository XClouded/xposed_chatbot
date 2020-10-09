package com.ali.user.mobile.login.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import com.ali.user.mobile.login.presenter.UserMobileLoginPresenter;
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
import com.ali.user.mobile.security.SecurityGuardManagerWraper;
import com.ali.user.mobile.service.FaceService;
import com.ali.user.mobile.service.NavigatorService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.ui.R;
import com.ali.user.mobile.ui.widget.BottomMenuFragment;
import com.ali.user.mobile.ui.widget.CountDownButton;
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
import java.util.Properties;

public class AliUserMobileLoginFragment extends BaseLoginFragment implements UserMobileLoginView {
    private static final String TAG = "AliUserMobileLoginFragment";
    private boolean checkcodeUT = true;
    protected boolean isForceNormalMode = false;
    protected boolean isVoice = false;
    protected boolean mActiveLogin = false;
    protected String mCurrentAccount;
    protected String mCurrentSelectedAccount;
    protected FaceLoginPresenter mFaceLoginPresenter;
    protected LinearLayout mFirstLoginLL;
    protected LinearLayout mHistoryLoginLL;
    protected Button mLoginBtn;
    protected LinearLayout mLoginBtnShadow;
    protected View mMobileClearBtn;
    protected EditText mMobileET;
    protected UserMobileLoginPresenter mMobileLoginPresenter;
    protected TextView mMobileTV;
    protected TextWatcher mMobileTextWatcher;
    protected TextView mRegTV;
    protected RegionInfo mRegionInfo;
    protected TextView mRegionTV;
    protected LinearLayout mRootView;
    protected EditText mSMSCodeET;
    protected CountDownButton mSendSMSCodeBtn;
    protected TextWatcher mSmsCodeTextWatcher;
    protected TextView mSmsSuccessTipTV;
    protected TextView mSwitchFaceLoginBtn;
    protected TextView mSwitchMoreLoginBtn;
    protected TextView mSwitchPwdLoginBtn;
    protected LinearLayout mVoiceRR;
    protected TextView mVoiceTV;
    private boolean mobileUT = true;

    public void onSMSSendFail(RpcResponse rpcResponse) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initParams();
    }

    private void initParams() {
        Bundle arguments = getArguments();
        LoginParam loginParam = null;
        if (arguments != null) {
            String str = (String) arguments.get(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM);
            arguments.putString(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM, "");
            if (!TextUtils.isEmpty(str)) {
                loginParam = (LoginParam) JSON.parseObject(str, LoginParam.class);
            }
            this.isForceNormalMode = arguments.getBoolean(LoginConstant.FORCE_NORMAL_MODE);
            this.mCurrentAccount = arguments.getString(LoginConstant.ACCOUNT);
        }
        this.mMobileLoginPresenter = new UserMobileLoginPresenter(this, loginParam);
        this.mFaceLoginPresenter = new FaceLoginPresenter(this, loginParam);
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.aliuser_fragment_mobile_login;
    }

    public void initViews(View view) {
        super.initViews(view);
        this.mRootView = (LinearLayout) view.findViewById(R.id.aliuser_root_ll);
        this.mMobileET = (EditText) view.findViewById(R.id.aliuser_login_mobile_et);
        this.mMobileET.setSingleLine();
        this.mMobileTextWatcher = new LoginTextWatcher(this.mMobileET);
        this.mMobileET.addTextChangedListener(this.mMobileTextWatcher);
        this.mMobileClearBtn = view.findViewById(R.id.aliuser_login_mobile_clear_iv);
        this.mRegionTV = (TextView) view.findViewById(R.id.aliuser_region_tv);
        initRegionInfo();
        this.mSMSCodeET = (EditText) view.findViewById(R.id.aliuser_register_sms_code_et);
        this.mSmsCodeTextWatcher = new LoginTextWatcher(this.mSMSCodeET);
        this.mSMSCodeET.addTextChangedListener(this.mSmsCodeTextWatcher);
        this.mSendSMSCodeBtn = (CountDownButton) view.findViewById(R.id.aliuser_login_send_smscode_btn);
        this.mHistoryLoginLL = (LinearLayout) view.findViewById(R.id.aliuser_login_history_ll);
        this.mFirstLoginLL = (LinearLayout) view.findViewById(R.id.aliuser_login_normal_ll);
        this.mMobileTV = (TextView) view.findViewById(R.id.aliuser_login_mobile_tv);
        this.mLoginBtn = (Button) view.findViewById(R.id.aliuser_login_login_btn);
        this.mSwitchPwdLoginBtn = (TextView) view.findViewById(R.id.aliuser_login_switch_pwdlogin);
        this.mSwitchMoreLoginBtn = (TextView) view.findViewById(R.id.aliuser_login_switch_more_login);
        this.mSwitchFaceLoginBtn = (TextView) view.findViewById(R.id.aliuser_login_switch_face_login);
        this.mRegTV = (TextView) view.findViewById(R.id.aliuser_reg_tv);
        if (!(this.mRegTV == null || AliUserLogin.mAppreanceExtentions == null || !AliUserLogin.mAppreanceExtentions.needRegister())) {
            this.mRegTV.setVisibility(0);
        }
        this.mSmsSuccessTipTV = (TextView) view.findViewById(R.id.aliuser_send_sms_success_tip);
        try {
            this.mVoiceRR = (LinearLayout) view.findViewById(R.id.aliuser_login_voice_rr);
            this.mVoiceTV = (TextView) view.findViewById(R.id.aliuser_login_send_voicecode_tv);
            if (this.mVoiceTV != null) {
                this.mVoiceTV.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        AliUserMobileLoginFragment.this.isVoice = true;
                        UserTrackAdapter.sendControlUT(AliUserMobileLoginFragment.this.getPageName(), "Button-SendVoiceCode");
                        AliUserMobileLoginFragment.this.onSendSMSAction();
                    }
                });
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        this.mLoginBtnShadow = (LinearLayout) view.findViewById(R.id.aliuser_login_login_btn_ll);
        setOnClickListener(this.mLoginBtn, this.mSendSMSCodeBtn, this.mRegTV, this.mSwitchPwdLoginBtn, this.mRegionTV, this.mMobileClearBtn, this.mSwitchMoreLoginBtn, this.mSwitchFaceLoginBtn);
        this.mMobileLoginPresenter.onStart();
        initMode();
        showPushLogoutAlertIfHas();
        BroadCastHelper.sendLocalBroadCast(new Intent(LoginResActions.LOGIN_OPEN_ACTION));
    }

    /* access modifiers changed from: protected */
    public void initMode() {
        if (this.isForceNormalMode) {
            this.isHistoryMode = false;
            if (TextUtils.isEmpty(this.mCurrentAccount)) {
                switchMode(this.isHistoryMode, (HistoryAccount) null);
            } else {
                this.mMobileET.setText(this.mCurrentAccount);
            }
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

    /* access modifiers changed from: protected */
    public void initRegionInfo() {
        String str;
        LoginApprearanceExtensions loginApprearanceExtensions = AliUserLogin.mAppreanceExtentions;
        if (loginApprearanceExtensions == null || loginApprearanceExtensions.needCountryModule()) {
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
                this.mRegionTV.setVisibility(0);
                this.mRegionTV.setText(this.mRegionInfo.code);
                resizeMobileETPadding();
            }
        } else {
            this.mRegionTV.setVisibility(8);
        }
        adjustMobileETMaxLength();
    }

    @TargetApi(21)
    private class MobileTextWatcher extends PhoneNumberFormattingTextWatcher {
        private WeakReference<EditText> editText;

        private MobileTextWatcher(EditText editText2, String str) {
            super(str);
            this.editText = new WeakReference<>(editText2);
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            AliUserMobileLoginFragment.this.onAccountTextChanged((EditText) this.editText.get(), charSequence);
        }
    }

    private class LoginTextWatcher implements TextWatcher {
        private WeakReference<EditText> editText;

        public void afterTextChanged(Editable editable) {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        private LoginTextWatcher(EditText editText2) {
            this.editText = new WeakReference<>(editText2);
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            AliUserMobileLoginFragment.this.onAccountTextChanged((EditText) this.editText.get(), charSequence);
        }
    }

    /* access modifiers changed from: protected */
    public void onAccountTextChanged(EditText editText, CharSequence charSequence) {
        if (editText.getId() != R.id.aliuser_login_mobile_et || this.mMobileClearBtn == null) {
            if (editText.getId() == R.id.aliuser_register_sms_code_et && charSequence != null && charSequence.length() > 0 && this.checkcodeUT) {
                this.checkcodeUT = false;
                UserTrackAdapter.sendUT(getPageName(), "InputCode");
            }
        } else if (charSequence != null && charSequence.length() != 0) {
            if (this.mobileUT) {
                this.mobileUT = false;
                UserTrackAdapter.sendUT(getPageName(), "InputPhone");
            }
            if (this.mMobileClearBtn.getVisibility() != 0 && this.mMobileClearBtn.isEnabled()) {
                this.mMobileClearBtn.setVisibility(0);
            }
        } else if (this.mMobileClearBtn.getVisibility() != 8) {
            this.mMobileClearBtn.setVisibility(8);
        }
        checkSignInable(editText);
    }

    /* access modifiers changed from: protected */
    public void checkSignInable(EditText editText) {
        boolean z = false;
        if (this.mMobileET == null || this.mSMSCodeET == null) {
            this.mLoginBtn.setEnabled(false);
        } else if (editText != null) {
            String obj = this.mMobileET.getText().toString();
            if (this.isHistoryMode) {
                obj = this.mMobileTV.getText().toString();
            }
            if (editText.getId() == R.id.aliuser_login_mobile_et) {
                if (TextUtils.isEmpty(obj) || this.mSendSMSCodeBtn.isCountDowning()) {
                    this.mSendSMSCodeBtn.setEnabled(false);
                } else {
                    this.mSendSMSCodeBtn.setEnabled(true);
                }
            }
            String obj2 = this.mSMSCodeET.getText().toString();
            if (!TextUtils.isEmpty(obj) && !TextUtils.isEmpty(obj2) && obj2.length() >= 4) {
                z = true;
            }
            this.mLoginBtn.setEnabled(z);
            if (!z || !DataProviderFactory.getDataProvider().isTaobaoApp()) {
                this.mLoginBtnShadow.setBackgroundDrawable((Drawable) null);
            } else {
                this.mLoginBtnShadow.setBackgroundResource(R.drawable.aliuser_btn_shadow);
            }
        }
    }

    public void setLoginAccountInfo(String str) {
        this.mMobileET.setText(str);
    }

    public void onNeedVerification(String str, int i) {
        NavigatorManager.getInstance().navToVerificationPage(getActivity(), str, i);
    }

    public void onSendSMSSuccess(long j, boolean z) {
        if (z) {
            this.mSmsSuccessTipTV.setVisibility(0);
        }
        this.mSendSMSCodeBtn.startCountDown(j, 1000);
        this.mSMSCodeET.postDelayed(new Runnable() {
            public void run() {
                try {
                    AliUserMobileLoginFragment.this.mSMSCodeET.requestFocus();
                    ((InputMethodManager) AliUserMobileLoginFragment.this.mAttachedActivity.getSystemService("input_method")).showSoftInput(AliUserMobileLoginFragment.this.mSMSCodeET, 0);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 100);
        if (this.isVoice) {
            if (this.mVoiceRR != null) {
                this.mVoiceRR.setVisibility(8);
            }
            this.mSmsSuccessTipTV.setText(R.string.aliuser_voice_code_success_hint);
            return;
        }
        this.mSmsSuccessTipTV.setText(getString(R.string.aliuser_sms_code_success_hint));
        if (DataProviderFactory.getDataProvider().isEnableVoiceMsg() && !"86".equals(getPhoneCode())) {
            this.mSendSMSCodeBtn.setTickListener(new CountDownButton.CountListener() {
                public void onTick(long j) {
                    String trim = AliUserMobileLoginFragment.this.mSMSCodeET.getText().toString().trim();
                    if (!AliUserMobileLoginFragment.this.isVoice && 57 == j / 1000 && !"86".equals(AliUserMobileLoginFragment.this.getPhoneCode()) && TextUtils.isEmpty(trim)) {
                        AliUserMobileLoginFragment.this.mSmsSuccessTipTV.setVisibility(8);
                        if (AliUserMobileLoginFragment.this.mVoiceRR != null) {
                            AliUserMobileLoginFragment.this.mVoiceRR.setVisibility(0);
                        }
                    }
                }
            });
        }
    }

    public void onNeedReg(String str, final String str2) {
        if (isActive()) {
            RegProtocolDialog regProtocolDialog = getRegProtocolDialog();
            regProtocolDialog.setRegTip(str);
            regProtocolDialog.setPositive(getString(R.string.aliuser_agree_and_reg), new View.OnClickListener() {
                public void onClick(View view) {
                    UserTrackAdapter.sendControlUT(AliUserMobileLoginFragment.this.getPageName(), ApiConstants.UTConstants.UT_SMS_ARGREE_REGISTER);
                    AliUserMobileLoginFragment.this.mMobileLoginPresenter.directRegister(str2, false);
                }
            });
            if (DataProviderFactory.getDataProvider().getSite() == 18 || DataProviderFactory.getDataProvider().getSite() == 21) {
                regProtocolDialog.setNagetive(new View.OnClickListener() {
                    public void onClick(View view) {
                        UserTrackAdapter.sendControlUT(AliUserMobileLoginFragment.this.getPageName(), ApiConstants.UTConstants.UT_SMS_DISAGREE_REGISTER);
                        AliUserMobileLoginFragment.this.mMobileET.setText("");
                        AliUserMobileLoginFragment.this.mSMSCodeET.setText("");
                        AliUserMobileLoginFragment.this.mSendSMSCodeBtn.cancelCountDown();
                        AliUserMobileLoginFragment.this.mSendSMSCodeBtn.setText(AliUserMobileLoginFragment.this.getContext().getString(R.string.aliuser_signup_verification_getCode));
                        AliUserMobileLoginFragment.this.mSendSMSCodeBtn.setEnabled(true);
                    }
                });
            }
            regProtocolDialog.show(getActivity().getSupportFragmentManager(), "RegProtocolDialog");
        }
    }

    /* access modifiers changed from: protected */
    public RegProtocolDialog getRegProtocolDialog() {
        return new RegProtocolDialog();
    }

    public void onNeedShowFamilyAccount(String str, final String str2) {
        if (isActive()) {
            RegProtocolDialog regProtocolDialog = new RegProtocolDialog();
            regProtocolDialog.setRegTip(str);
            regProtocolDialog.setShowInset(true);
            regProtocolDialog.setPositive(getString(R.string.aliuser_agree), new View.OnClickListener() {
                public void onClick(View view) {
                    UserTrackAdapter.sendControlUT(AliUserMobileLoginFragment.this.getPageName(), ApiConstants.UTConstants.UT_SMS_ARGREE_REGISTER_FAMILY);
                    AliUserMobileLoginFragment.this.mMobileLoginPresenter.directRegister(str2, true);
                }
            });
            regProtocolDialog.show(getActivity().getSupportFragmentManager(), "RegProtocolDialog");
        }
    }

    /* access modifiers changed from: protected */
    public String getAccountName() {
        if (this.isHistoryMode) {
            return this.mCurrentSelectedAccount;
        }
        return this.mMobileET.getText().toString().trim().replaceAll(Operators.SPACE_STR, "");
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.aliuser_login_login_btn) {
            this.mActiveLogin = true;
            UserTrackAdapter.sendControlUT(getPageName(), "Button-Login");
            onLoginAction();
        } else if (id == R.id.aliuser_login_send_smscode_btn) {
            UserTrackAdapter.sendControlUT(getPageName(), "Button-GetCode");
            this.isVoice = false;
            onSendSMSAction();
        } else if (id == R.id.aliuser_login_switch_pwdlogin) {
            UserTrackAdapter.sendControlUT(getPageName(), "Button-ChoosePwdLogin");
            switchToPwdLogin();
        } else if (id == R.id.aliuser_login_switch_more_login) {
            UserTrackAdapter.sendControlUT(getPageName(), "Button-ChooseMoreLogin");
            showMoreLoginBottomMenu();
        } else if (id == R.id.aliuser_login_switch_face_login) {
            UserTrackAdapter.sendControlUT(getPageName(), "Button-ChooseFaceLogin");
            faceLogin();
        } else if (id == R.id.aliuser_region_tv) {
            UserTrackAdapter.sendControlUT(getPageName(), "Button-Region");
            if (DataProviderFactory.getDataProvider().useRegionFragment()) {
                applyRegion();
                return;
            }
            Intent intent = new Intent(this.mAttachedActivity, AliUserRegisterChoiceRegionActivity.class);
            intent.putExtra("from_login", true);
            this.mAttachedActivity.startActivityForResult(intent, 2001);
        } else if (id == R.id.aliuser_login_mobile_clear_iv) {
            onClearAccountBtnClickAction();
        } else {
            super.onClick(view);
        }
    }

    /* access modifiers changed from: protected */
    public void onClearAccountBtnClickAction() {
        if (DataProviderFactory.getDataProvider().isShowHistoryFragment() || TextUtils.isEmpty(this.mMobileET.getText()) || this.mMobileET.isEnabled()) {
            this.mMobileET.getEditableText().clear();
            this.mMobileET.setEnabled(true);
            this.isVoice = false;
            this.mSmsSuccessTipTV.setVisibility(4);
            if (this.mVoiceRR != null) {
                this.mVoiceRR.setVisibility(8);
                return;
            }
            return;
        }
        onDeleteAccount();
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
                    UserTrackAdapter.sendControlUT(AliUserMobileLoginFragment.this.getPageName(), "Button-ChooseOtherAccountLogin");
                    Intent intent = new Intent();
                    intent.putExtra(LoginConstant.FORCE_NORMAL_MODE, true);
                    if (!(AliUserMobileLoginFragment.this.mMobileLoginPresenter == null || AliUserMobileLoginFragment.this.mMobileLoginPresenter.getLoginParam() == null)) {
                        LoginParam loginParam = new LoginParam();
                        loginParam.source = AliUserMobileLoginFragment.this.mMobileLoginPresenter.getLoginParam().source;
                        intent.putExtra(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM, JSON.toJSONString(loginParam));
                    }
                    AliUserMobileLoginFragment.this.mUserLoginActivity.gotoMobileLoginFragment(intent);
                }
            });
            MenuItem menuItem2 = new MenuItem();
            menuItem2.setText(getString(R.string.aliuser_reg));
            menuItem2.setMenuItemOnClickListener(new MenuItemOnClickListener(bottomMenuFragment, menuItem2) {
                public void onClickMenuItem(View view, MenuItem menuItem) {
                    UserTrackAdapter.sendControlUT(AliUserMobileLoginFragment.this.getPageName(), "Button-Reg");
                    RegistParam registParam = new RegistParam();
                    registParam.registSite = AliUserMobileLoginFragment.this.getLoginSite();
                    ((NavigatorService) ServiceFactory.getService(NavigatorService.class)).openRegisterPage(AliUserMobileLoginFragment.this.mAttachedActivity, registParam);
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
                        if (AliUserMobileLoginFragment.this.isActive()) {
                            AliUserMobileLoginFragment.this.openHelp();
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
            menuItem.setText(getString(R.string.aliuser_login_pwd_login));
            menuItem.setMenuItemOnClickListener(new MenuItemOnClickListener(bottomMenuFragment, menuItem) {
                public void onClickMenuItem(View view, MenuItem menuItem) {
                    UserTrackAdapter.sendControlUT(AliUserMobileLoginFragment.this.getPageName(), "Button-ChooseOtherAccountLogin");
                    AliUserMobileLoginFragment.this.switchToPwdLogin();
                }
            });
            MenuItem menuItem2 = new MenuItem();
            menuItem2.setText(getString(R.string.aliuser_scan_login_text));
            menuItem2.setMenuItemOnClickListener(new MenuItemOnClickListener(bottomMenuFragment, menuItem2) {
                public void onClickMenuItem(View view, MenuItem menuItem) {
                    UserTrackAdapter.sendControlUT(AliUserMobileLoginFragment.this.getPageName(), "Button-FaceLogin");
                    AliUserMobileLoginFragment.this.faceLogin();
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

    /* access modifiers changed from: private */
    public void onSendSMSAction() {
        this.mCurrentAccount = getAccountName();
        if (TextUtils.isEmpty(this.mCurrentAccount) || !isMobileValid(this.mCurrentAccount)) {
            showErrorMessage(R.string.aliuser_login_mobile_verify_hint);
            return;
        }
        Properties properties = new Properties();
        properties.setProperty("result", "CheckPass");
        UserTrackAdapter.sendUT(getPageName(), "CheckPhoneResult", (String) null, (String) null, properties);
        this.mMobileLoginPresenter.buildSMSLoginParam(this.mCurrentAccount, (String) null, this.isVoice);
        this.mMobileLoginPresenter.sendSMS();
    }

    public String getCountryCode() {
        return (this.mRegionInfo == null || TextUtils.isEmpty(this.mRegionInfo.domain)) ? "CN" : this.mRegionInfo.domain;
    }

    public String getPhoneCode() {
        if (!this.isForceNormalMode && this.mUserLoginActivity.hadReadHistory && this.mUserLoginActivity.mHistoryAccount != null) {
            String str = this.mUserLoginActivity.mHistoryAccount.mobile;
            if (!TextUtils.isEmpty(str) && str.contains("-")) {
                String[] split = str.split("-");
                if (split.length > 0) {
                    return split[0];
                }
            }
        }
        return (this.mRegionInfo == null || TextUtils.isEmpty(this.mRegionInfo.code)) ? "86" : this.mRegionInfo.code.replace("+", "");
    }

    /* access modifiers changed from: protected */
    public void switchToPwdLogin() {
        Intent intent = new Intent();
        intent.putExtra(LoginConstant.FORCE_NORMAL_MODE, !this.isHistoryMode);
        if (!(this.mMobileLoginPresenter == null || this.mMobileLoginPresenter.getLoginParam() == null)) {
            LoginParam loginParam = new LoginParam();
            loginParam.source = this.mMobileLoginPresenter.getLoginParam().source;
            intent.putExtra(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM, JSON.toJSONString(loginParam));
        }
        this.mUserLoginActivity.gotoPwdLoginFragment(intent);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 != -1 || i != 2001) {
            this.mMobileLoginPresenter.onActivityResult(i, i2, intent);
        } else if (intent != null) {
            this.mRegionInfo = (RegionInfo) intent.getParcelableExtra(RegistConstants.REGION_INFO);
            if (this.mRegionInfo != null) {
                this.mRegionTV.setText(this.mRegionInfo.code);
                resizeMobileETPadding();
                adjustMobileETMaxLength();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void adjustMobileETMaxLength() {
        if (this.mRegionInfo == null || !TextUtils.equals("CN", this.mRegionInfo.domain)) {
            this.mMobileET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
            if (Build.VERSION.SDK_INT >= 21) {
                this.mMobileTextWatcher = new LoginTextWatcher(this.mMobileET);
                this.mMobileET.addTextChangedListener(this.mMobileTextWatcher);
                return;
            }
            return;
        }
        this.mMobileET.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        if (Build.VERSION.SDK_INT >= 21) {
            this.mMobileTextWatcher = new MobileTextWatcher(this.mMobileET, Locale.CHINA.getCountry());
            this.mMobileET.addTextChangedListener(this.mMobileTextWatcher);
        }
    }

    /* access modifiers changed from: protected */
    public void resizeMobileETPadding() {
        this.mRegionTV.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                AliUserMobileLoginFragment.this.mRegionTV.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                AliUserMobileLoginFragment.this.mMobileET.setPadding(AliUserMobileLoginFragment.this.mRegionTV.getWidth(), AliUserMobileLoginFragment.this.mMobileET.getPaddingTop(), AliUserMobileLoginFragment.this.mMobileClearBtn.getWidth() + 30, AliUserMobileLoginFragment.this.mMobileET.getPaddingBottom());
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onLoginAction() {
        this.mCurrentAccount = getAccountName();
        String trim = this.mSMSCodeET.getText().toString().trim();
        if (TextUtils.isEmpty(this.mCurrentAccount) || !isMobileValid(this.mCurrentAccount)) {
            showErrorMessage(R.string.aliuser_login_mobile_verify_hint);
        } else if (TextUtils.isEmpty(trim)) {
            showErrorMessage(R.string.aliuser_login_sms_code_hint);
        } else if (this.mMobileLoginPresenter.getLoginParam() == null || (this.mMobileLoginPresenter.getLoginParam() != null && TextUtils.isEmpty(this.mMobileLoginPresenter.getLoginParam().smsSid))) {
            toast(getString(R.string.aliuser_send_sms_first), 0);
        } else {
            if (this.mActivityHelper != null) {
                this.mActivityHelper.hideInputMethod();
            }
            this.mMobileLoginPresenter.buildSMSLoginParam(this.mCurrentAccount, trim, this.isVoice);
            this.mMobileLoginPresenter.login();
        }
    }

    /* access modifiers changed from: protected */
    public boolean isMobileValid(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (!this.isHistoryMode && this.mRegionInfo != null && !TextUtils.isEmpty(this.mRegionInfo.checkPattern)) {
            String replaceAll = str.replaceAll(Operators.SPACE_STR, "");
            String replace = this.mRegionInfo.code.replace("+", "");
            return (replace + replaceAll).matches(this.mRegionInfo.checkPattern);
        } else if (str.length() < 6 || str.length() > 20) {
            return false;
        } else {
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void showErrorMessage(int i) {
        toast(getString(i), 0);
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
        this.mMobileLoginPresenter.onLoginSuccess(loginParam, rpcResponse);
    }

    public void onError(RpcResponse rpcResponse) {
        this.mSmsSuccessTipTV.setVisibility(4);
        this.mMobileLoginPresenter.onLoginFail(rpcResponse);
    }

    public void onCheckCodeError() {
        this.mSMSCodeET.setText("");
    }

    private void readAccountFromHistory() {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, LoginHistory>() {
            /* access modifiers changed from: protected */
            public LoginHistory doInBackground(Object... objArr) {
                return SecurityGuardManagerWraper.getLoginHistory();
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(LoginHistory loginHistory) {
                AliUserMobileLoginFragment.this.mUserLoginActivity.hadReadHistory = true;
                if (loginHistory != null && loginHistory.accountHistory != null && loginHistory.accountHistory.size() > 0) {
                    AliUserMobileLoginFragment.this.isHistoryMode = true;
                    if (AliUserMobileLoginFragment.this.mMobileLoginPresenter.getLoginParam() == null || (AliUserMobileLoginFragment.this.mMobileLoginPresenter.getLoginParam() != null && TextUtils.isEmpty(AliUserMobileLoginFragment.this.mMobileLoginPresenter.getLoginParam().loginAccount))) {
                        int i = loginHistory.index;
                        if (i < 0 || i >= loginHistory.accountHistory.size()) {
                            i = loginHistory.accountHistory.size() - 1;
                        }
                        AliUserMobileLoginFragment.this.mUserLoginActivity.mHistoryAccount = loginHistory.accountHistory.get(i);
                        AliUserMobileLoginFragment.this.switchToHistoryMode(AliUserMobileLoginFragment.this.mUserLoginActivity.mHistoryAccount);
                    }
                }
            }
        }, new Object[0]);
    }

    /* access modifiers changed from: protected */
    public void switchToHistoryMode(HistoryAccount historyAccount) {
        if (isActivityAvaiable()) {
            this.mCurrentSelectedAccount = historyAccount.mobile;
            String hideAccount = StringUtil.hideAccount(this.mCurrentSelectedAccount);
            if (!TextUtils.isEmpty(hideAccount)) {
                switchMode(this.isHistoryMode, historyAccount);
                if (!TextUtils.isEmpty(historyAccount.headImg)) {
                    updateAvatar(historyAccount.headImg);
                }
                this.mMobileTV.setText(hideAccount);
                this.mSendSMSCodeBtn.setEnabled(true);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void switchMode(boolean z, HistoryAccount historyAccount) {
        boolean z2 = true;
        if (z) {
            this.mFirstLoginLL.setVisibility(8);
            this.mHistoryLoginLL.setVisibility(0);
            boolean z3 = historyAccount.hasPwd == 1 && DataProviderFactory.getDataProvider().supportPwdLogin();
            if (!DataProviderFactory.getDataProvider().supportFaceLogin() || (!this.mUserLoginActivity.isFaceLoginEnvEnable && !this.mUserLoginActivity.isFaceLoginActivate)) {
                z2 = false;
            }
            if (z3 && z2) {
                this.mSwitchPwdLoginBtn.setVisibility(8);
                this.mSwitchMoreLoginBtn.setVisibility(0);
                this.mSwitchFaceLoginBtn.setVisibility(8);
                ((RelativeLayout.LayoutParams) this.mSwitchMoreLoginBtn.getLayoutParams()).addRule(14, -1);
            } else if (z3) {
                this.mSwitchPwdLoginBtn.setVisibility(0);
                this.mSwitchMoreLoginBtn.setVisibility(8);
                this.mSwitchFaceLoginBtn.setVisibility(8);
                ((RelativeLayout.LayoutParams) this.mSwitchPwdLoginBtn.getLayoutParams()).addRule(14, -1);
            } else if (z2) {
                this.mSwitchPwdLoginBtn.setVisibility(8);
                this.mSwitchMoreLoginBtn.setVisibility(8);
                this.mSwitchFaceLoginBtn.setVisibility(0);
                ((RelativeLayout.LayoutParams) this.mSwitchFaceLoginBtn.getLayoutParams()).addRule(14, -1);
            } else {
                this.mSwitchPwdLoginBtn.setVisibility(8);
                this.mSwitchMoreLoginBtn.setVisibility(8);
                this.mSwitchFaceLoginBtn.setVisibility(8);
            }
            this.mRegTV.setVisibility(8);
        } else {
            this.mFirstLoginLL.setVisibility(0);
            resizeMobileETPadding();
            this.mHistoryLoginLL.setVisibility(8);
            if (DataProviderFactory.getDataProvider().supportPwdLogin()) {
                this.mSwitchPwdLoginBtn.setVisibility(0);
                ((RelativeLayout.LayoutParams) this.mSwitchPwdLoginBtn.getLayoutParams()).addRule(9);
            } else {
                this.mSwitchPwdLoginBtn.setVisibility(8);
            }
            this.mSwitchMoreLoginBtn.setVisibility(8);
            this.mSwitchFaceLoginBtn.setVisibility(8);
            if (!(this.mRegTV == null || AliUserLogin.mAppreanceExtentions == null || !AliUserLogin.mAppreanceExtentions.needRegister())) {
                this.mRegTV.setVisibility(0);
            }
            if (!(DataProviderFactory.getDataProvider().isShowHistoryFragment() || this.mUserLoginActivity == null || this.mMobileET == null)) {
                if (this.mUserLoginActivity.mHistoryAccount == null || TextUtils.isEmpty(this.mUserLoginActivity.mHistoryAccount.loginPhone)) {
                    this.mMobileET.setText("");
                    this.mMobileET.setEnabled(true);
                    this.mMobileET.requestFocus();
                } else {
                    this.mMobileET.setText(this.mUserLoginActivity.mHistoryAccount.mobile);
                    this.mMobileET.setEnabled(false);
                }
            }
        }
        if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }

    /* access modifiers changed from: protected */
    public void applyRegion() {
        this.mMobileLoginPresenter.region();
    }

    public void onGetRegion(List<RegionInfo> list) {
        if (isActive()) {
            RegionDialogFragment regionDialogFragment = new RegionDialogFragment();
            regionDialogFragment.setList(list);
            regionDialogFragment.setRegionListener(new RegionListener() {
                public void onClick(RegionInfo regionInfo) {
                    AliUserMobileLoginFragment.this.mRegionInfo = regionInfo;
                    if (AliUserMobileLoginFragment.this.mRegionInfo != null) {
                        AliUserMobileLoginFragment.this.mRegionTV.setText(AliUserMobileLoginFragment.this.mRegionInfo.code);
                        AliUserMobileLoginFragment.this.resizeMobileETPadding();
                        AliUserMobileLoginFragment.this.adjustMobileETMaxLength();
                    }
                }
            });
            regionDialogFragment.setCurrentRegion(this.mRegionInfo);
            regionDialogFragment.setupAdapter(getActivity());
            regionDialogFragment.show(getActivity().getSupportFragmentManager(), "MobileRegionDialog");
        }
    }

    /* access modifiers changed from: protected */
    public String getPageName() {
        return this.isHistoryMode ? ApiConstants.UTConstants.UT_PAGE_SMS_LOGIN2 : ApiConstants.UTConstants.UT_PAGE_SMS_LOGIN1;
    }

    public void onDestroyView() {
        this.mMobileET.removeTextChangedListener(this.mMobileTextWatcher);
        this.mSMSCodeET.removeTextChangedListener(this.mSmsCodeTextWatcher);
        super.onDestroyView();
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mMobileLoginPresenter != null) {
            this.mMobileLoginPresenter.onDestory();
        }
        if (this.mSendSMSCodeBtn != null) {
            this.mSendSMSCodeBtn.cancelCountDown();
        }
    }

    public boolean isHistoryMode() {
        return this.isHistoryMode;
    }
}
