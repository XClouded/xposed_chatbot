package com.ali.user.mobile.register.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.ui.BaseActivity;
import com.ali.user.mobile.base.ui.BaseFragment;
import com.ali.user.mobile.common.api.AliUserLogin;
import com.ali.user.mobile.common.api.LoginApprearanceExtensions;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.model.LoginConstant;
import com.ali.user.mobile.login.presenter.RegionPresenter;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.RegionInfo;
import com.ali.user.mobile.navigation.NavigatorManager;
import com.ali.user.mobile.register.ProtocolModel;
import com.ali.user.mobile.register.RegistConstants;
import com.ali.user.mobile.register.model.OceanRegisterParam;
import com.ali.user.mobile.register.presenter.MobileRegisterPresenter;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.service.NavigatorService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.ui.R;
import com.ali.user.mobile.ui.WebConstant;
import com.ali.user.mobile.ui.widget.CountDownButton;
import com.ali.user.mobile.utils.CountryUtil;
import com.ali.user.mobile.utils.ProtocolHelper;
import com.ali.user.mobile.utils.UTConstans;
import com.ali.user.mobile.webview.WebViewActivity;
import com.alibaba.fastjson.JSON;
import com.taobao.orange.OrangeConfig;
import com.taobao.weex.el.parse.Operators;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.regex.Pattern;

public class AliUserMobileRegisterFragment extends BaseFragment implements RegisterFormView, View.OnClickListener {
    public static final String PAGE_NAME = "Page_Reg";
    public static final Pattern REG_EMAIL = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
    public static final int SMS2VOCIE_TIME = 57;
    protected Pattern REG_PASSWORD = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)(?![`~!@#$%^&*()_\\-+=[{]};:',<.>/?|\\\"]+$)[!-~]{6,20}$");
    /* access modifiers changed from: private */
    public boolean checkcodeUT = true;
    protected boolean isVoice = false;
    protected EditText mEmailET;
    protected View mMobileClearBtn;
    protected EditText mMobileET;
    protected String mMobileStr;
    protected TextWatcher mMobileTextWatcher;
    protected EditText mPasswordET;
    protected TextView mPasswordHint;
    protected MobileRegisterPresenter mPresenter;
    protected TextView mProtocolTV;
    protected Button mRegBtn;
    protected LinearLayout mRegBtnLL;
    protected RegionInfo mRegionInfo;
    protected RegionPresenter mRegionPresenter;
    protected TextView mRegionTV;
    protected EditText mSMSCodeET;
    protected CountDownButton mSendSMSCodeBtn;
    protected TextWatcher mSmsCodeTextWatcher;
    protected TextView mSmsSuccessTipTV;
    protected LinearLayout mVoiceRR;
    protected TextView mVoiceTV;
    /* access modifiers changed from: private */
    public boolean mobileUT = true;

    public String getPageName() {
        return "Page_Reg";
    }

    public void onCheckAuthNumFail() {
    }

    public void onCheckAuthNumSuccess() {
    }

    public void onNumAuthRegisterFail(RpcResponse rpcResponse) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getArguments();
        createPresenter();
    }

    /* access modifiers changed from: protected */
    public void createPresenter() {
        this.mPresenter = new MobileRegisterPresenter(this);
        this.mRegionPresenter = new RegionPresenter(this);
    }

    public void onResume() {
        super.onResume();
        UserTrackAdapter.updatePageName(getActivity(), "Page_Reg");
    }

    /* access modifiers changed from: protected */
    public void initViews(View view) {
        super.initViews(view);
        this.mMobileET = (EditText) view.findViewById(R.id.aliuser_register_mobile_et);
        this.mMobileTextWatcher = new RegTextWatcher(this.mMobileET);
        this.mMobileET.addTextChangedListener(this.mMobileTextWatcher);
        this.mSMSCodeET = (EditText) view.findViewById(R.id.aliuser_register_sms_code_et);
        this.mSmsCodeTextWatcher = new RegTextWatcher(this.mSMSCodeET);
        this.mSMSCodeET.addTextChangedListener(this.mSmsCodeTextWatcher);
        this.mSendSMSCodeBtn = (CountDownButton) view.findViewById(R.id.aliuser_register_send_smscode_btn);
        this.mSendSMSCodeBtn.setOnClickListener(this);
        this.mRegBtn = (Button) view.findViewById(R.id.aliuser_register_reg_btn);
        this.mRegBtn.setOnClickListener(this);
        this.mRegBtnLL = (LinearLayout) view.findViewById(R.id.aliuser_register_reg_btn_ll);
        this.mSmsSuccessTipTV = (TextView) view.findViewById(R.id.aliuser_send_sms_success_tip);
        try {
            this.mVoiceRR = (LinearLayout) view.findViewById(R.id.aliuser_register_voice_rr);
            this.mVoiceTV = (TextView) view.findViewById(R.id.aliuser_register_send_voicecode_tv);
            this.mVoiceTV.setOnClickListener(this);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        ((RelativeLayout) view.findViewById(R.id.aliuser_region_rl)).setOnClickListener(this);
        this.mRegionTV = (TextView) view.findViewById(R.id.aliuser_region_tv);
        initRegionInfo();
        this.mRegionTV.setOnClickListener(this);
        this.mProtocolTV = (TextView) view.findViewById(R.id.aliuser_protocol_tv);
        try {
            this.mEmailET = (EditText) view.findViewById(R.id.aliuser_register_email_et);
            this.mPasswordET = (EditText) view.findViewById(R.id.aliuser_register_password_et);
            this.mPasswordHint = (TextView) view.findViewById(R.id.aliuser_register_password_tip);
        } catch (Throwable unused) {
        }
        if (DataProviderFactory.getDataProvider().enableRegEmailCheck() && this.mEmailET != null) {
            this.mEmailET.setVisibility(0);
        }
        if (DataProviderFactory.getDataProvider().enableRegPwdCheck() && this.mPasswordET != null) {
            this.mPasswordET.setVisibility(0);
        }
        if (DataProviderFactory.getDataProvider().enableRegPwdCheck() && this.mPasswordHint != null) {
            this.mPasswordHint.setVisibility(0);
        }
        generateProtocol();
        this.mMobileClearBtn = view.findViewById(R.id.aliuser_login_mobile_clear_iv);
        if (this.mMobileClearBtn != null) {
            this.mMobileClearBtn.setOnClickListener(this);
        }
    }

    /* access modifiers changed from: protected */
    public void generateProtocol() {
        ProtocolHelper.generateProtocol(getProtocolModel(), this.mAttachedActivity, this.mProtocolTV, "Page_Reg", true);
    }

    /* access modifiers changed from: protected */
    public ProtocolModel getProtocolModel() {
        String str;
        String str2;
        Throwable th;
        Throwable th2;
        ProtocolModel protocolModel = new ProtocolModel();
        HashMap hashMap = new HashMap();
        hashMap.put(getString(R.string.aliuser_tb_protocal), getString(R.string.aliuser_tb_protocal_url));
        hashMap.put(getString(R.string.aliuser_policy_protocal), getString(R.string.aliuser_policy_protocal_url));
        hashMap.put(getString(R.string.aliuser_law_protocal), getString(R.string.aliuser_law_protocal_url));
        String string = getString(R.string.aliuser_alipay_protocal_url);
        String string2 = getString(R.string.aliuser_protocal_text);
        String string3 = getString(R.string.aliuser_alipay_protocal);
        try {
            str = OrangeConfig.getInstance().getConfig("login4android", "alipay_protocol", getString(R.string.aliuser_alipay_protocal_url));
            try {
                str2 = OrangeConfig.getInstance().getConfig("login4android", "alipay_protocol_text", getString(R.string.aliuser_alipay_protocal));
            } catch (Throwable th3) {
                th = th3;
                th.printStackTrace();
                str2 = string3;
                protocolModel.protocolTitle = string2;
                hashMap.put(str2, str);
                protocolModel.protocolItems = hashMap;
                protocolModel.protocolItemColor = R.color.aliuser_edittext_bg_color_activated;
                return protocolModel;
            }
            try {
                string2 = string2 + str2;
            } catch (Throwable th4) {
                th2 = th4;
                string3 = str2;
                th = th2;
                th.printStackTrace();
                str2 = string3;
                protocolModel.protocolTitle = string2;
                hashMap.put(str2, str);
                protocolModel.protocolItems = hashMap;
                protocolModel.protocolItemColor = R.color.aliuser_edittext_bg_color_activated;
                return protocolModel;
            }
        } catch (Throwable th5) {
            th2 = th5;
            str = string;
            th = th2;
            th.printStackTrace();
            str2 = string3;
            protocolModel.protocolTitle = string2;
            hashMap.put(str2, str);
            protocolModel.protocolItems = hashMap;
            protocolModel.protocolItemColor = R.color.aliuser_edittext_bg_color_activated;
            return protocolModel;
        }
        protocolModel.protocolTitle = string2;
        hashMap.put(str2, str);
        protocolModel.protocolItems = hashMap;
        protocolModel.protocolItemColor = R.color.aliuser_edittext_bg_color_activated;
        return protocolModel;
    }

    /* access modifiers changed from: protected */
    public void initRegionInfo() {
        String str;
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
                resizeMobileETPadding();
            } else {
                this.mRegionTV.setVisibility(8);
            }
        }
        adjustMobileETMaxLength();
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.aliuser_fragment_mobile_register;
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.aliuser_register_send_smscode_btn) {
            sendCodeAction();
        } else if (id == R.id.aliuser_register_reg_btn) {
            registerAction();
        } else if (id == R.id.aliuser_region_rl || id == R.id.aliuser_region_tv) {
            UserTrackAdapter.sendControlUT("Page_Reg", "Button-ChooseCountry");
            if (DataProviderFactory.getDataProvider().useRegionFragment()) {
                applyRegion();
                return;
            }
            this.mAttachedActivity.startActivityForResult(new Intent(this.mAttachedActivity, AliUserRegisterChoiceRegionActivity.class), 2001);
        } else if (id == R.id.aliuser_login_mobile_clear_iv) {
            clearMobile();
        } else if (id == R.id.aliuser_register_send_voicecode_tv) {
            this.isVoice = true;
            UserTrackAdapter.sendControlUT("Page_Reg", "Button-SendVoiceSms");
            this.mSendSMSCodeBtn.cancelCountDown();
            onSendSMSAction(true);
        }
    }

    /* access modifiers changed from: protected */
    public void registerAction() {
        UserTrackAdapter.sendControlUT("Page_Reg", "Button-Regist");
        submitRegisterForm();
    }

    /* access modifiers changed from: protected */
    public void sendCodeAction() {
        this.isVoice = false;
        UserTrackAdapter.sendControlUT("Page_Reg", "Button-SendSms");
        try {
            onSendSMSAction(false);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void clearMobile() {
        this.mMobileET.getEditableText().clear();
        this.mMobileET.setEnabled(true);
        this.isVoice = false;
        this.mSmsSuccessTipTV.setVisibility(4);
        if (this.mVoiceRR != null) {
            this.mVoiceRR.setVisibility(8);
        }
    }

    private void applyRegion() {
        if (isActive()) {
            this.mRegionPresenter.region(1);
        }
    }

    /* access modifiers changed from: protected */
    public void onSendSMSAction(boolean z) {
        this.mMobileStr = getMobile();
        if (!isMobileValid(this.mMobileStr)) {
            Toast.makeText(getBaseActivity(), R.string.aliuser_phone_number_invalidate, 0).show();
            return;
        }
        if (this.mSMSCodeET != null) {
            this.mSMSCodeET.requestFocus();
        }
        this.mPresenter.sendSMS(buildRegisterParam());
    }

    /* access modifiers changed from: protected */
    public boolean isMobileValid(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (this.mRegionInfo != null && !TextUtils.isEmpty(this.mRegionInfo.checkPattern)) {
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
    public boolean isEmailValid(String str) {
        return !TextUtils.isEmpty(str) && REG_EMAIL.matcher(str).matches();
    }

    /* access modifiers changed from: protected */
    public boolean isPasswordValid(String str) {
        return !TextUtils.isEmpty(str) && this.REG_PASSWORD.matcher(str).matches();
    }

    public boolean submitRegisterForm() {
        if (DataProviderFactory.getDataProvider().enableRegPwdCheck() && this.mPasswordET != null && !isPasswordValid(this.mPasswordET.getText().toString().trim())) {
            toast("密码格式错误", 0);
            return false;
        } else if (DataProviderFactory.getDataProvider().enableRegEmailCheck() && this.mEmailET != null && !isEmailValid(this.mEmailET.getText().toString().trim())) {
            toast("邮箱格式错误", 0);
            return false;
        } else if (TextUtils.isEmpty(this.mPresenter.getSessionId())) {
            toast(getString(R.string.aliuser_send_sms_first), 0);
            return false;
        } else if (TextUtils.isEmpty(this.mSMSCodeET.getText())) {
            toast(getString(R.string.aliuser_sms_code_hint), 0);
            return false;
        } else {
            this.mPresenter.register(buildRegisterParam());
            return true;
        }
    }

    public String getCountryCode() {
        return (this.mRegionInfo == null || TextUtils.isEmpty(this.mRegionInfo.domain)) ? "CN" : this.mRegionInfo.domain;
    }

    /* access modifiers changed from: protected */
    public OceanRegisterParam buildRegisterParam() {
        OceanRegisterParam oceanRegisterParam = new OceanRegisterParam();
        oceanRegisterParam.mobileNum = getMobile();
        if (this.mSMSCodeET != null) {
            oceanRegisterParam.checkCode = this.mSMSCodeET.getText().toString().trim();
        }
        if (this.mRegionInfo != null) {
            oceanRegisterParam.countryCode = this.mRegionInfo.domain;
        } else {
            oceanRegisterParam.countryCode = "CN";
        }
        if (DataProviderFactory.getDataProvider().enableRegEmailCheck() && this.mEmailET != null) {
            oceanRegisterParam.email = this.mEmailET.getText().toString().trim();
        }
        if (DataProviderFactory.getDataProvider().enableRegPwdCheck() && this.mPasswordET != null) {
            oceanRegisterParam.password = this.mPasswordET.getText().toString().trim();
        }
        if (this.isVoice) {
            oceanRegisterParam.voice = "true";
        }
        return oceanRegisterParam;
    }

    /* access modifiers changed from: protected */
    public String getMobile() {
        return this.mMobileET.getText().toString().trim().replaceAll(Operators.SPACE_STR, "");
    }

    public void onRegisterSuccess(String str) {
        LoginParam loginParam = new LoginParam();
        loginParam.token = str;
        loginParam.scene = "1012";
        loginParam.tokenType = ApiConstants.UTConstants.UT_TYPE_SMS_LOGIN_TO_REG;
        loginParam.loginSite = DataProviderFactory.getDataProvider().getSite();
        ((NavigatorService) ServiceFactory.getService(NavigatorService.class)).navToLoginPage(this.mAttachedActivity, JSON.toJSONString(loginParam), true, true);
    }

    public void onRegisterFail(int i, String str) {
        Properties properties = new Properties();
        if (!TextUtils.isEmpty(str)) {
            properties.setProperty("regFailMsg", str);
        }
        UserTrackAdapter.sendUT("Page_Reg", UTConstans.CustomEvent.UT_REGISTER_RESULT, String.valueOf(i), properties);
        if (TextUtils.isEmpty(str)) {
            str = getString(R.string.aliuser_sever_error);
        }
        toast(str, 0);
    }

    public void onSendSMSSuccess(long j) {
        this.mSmsSuccessTipTV.setVisibility(0);
        this.mSendSMSCodeBtn.startCountDown(j, 1000);
        if (!this.isVoice) {
            this.mSmsSuccessTipTV.setText(getString(R.string.aliuser_sms_code_success_hint));
            if (DataProviderFactory.getDataProvider().isEnableVoiceMsg() && !"CN".equals(getCountryCode())) {
                this.mSendSMSCodeBtn.setTickListener(new CountDownButton.CountListener() {
                    public void onTick(long j) {
                        String trim = AliUserMobileRegisterFragment.this.mSMSCodeET.getText().toString().trim();
                        if (!AliUserMobileRegisterFragment.this.isVoice && 57 == j / 1000 && !"CN".equals(AliUserMobileRegisterFragment.this.getCountryCode()) && TextUtils.isEmpty(trim)) {
                            AliUserMobileRegisterFragment.this.mSmsSuccessTipTV.setVisibility(8);
                            if (AliUserMobileRegisterFragment.this.mVoiceRR != null) {
                                AliUserMobileRegisterFragment.this.mVoiceRR.setVisibility(0);
                            }
                        }
                    }
                });
                return;
            }
            return;
        }
        if (this.mVoiceRR != null) {
            this.mVoiceRR.setVisibility(8);
        }
        this.mSmsSuccessTipTV.setText(R.string.aliuser_voice_code_success_hint);
    }

    @TargetApi(21)
    private class MobileTextWatcher extends PhoneNumberFormattingTextWatcher {
        private WeakReference<EditText> editText;

        private MobileTextWatcher(EditText editText2, String str) {
            super(str);
            this.editText = new WeakReference<>(editText2);
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            super.onTextChanged(charSequence, i, i2, i3);
            AliUserMobileRegisterFragment.this.checkRegAble((EditText) this.editText.get());
        }
    }

    public class RegTextWatcher implements TextWatcher {
        private WeakReference<EditText> editText;

        public void afterTextChanged(Editable editable) {
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }

        public RegTextWatcher(EditText editText2) {
            this.editText = new WeakReference<>(editText2);
        }

        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            if (((EditText) this.editText.get()).getId() != R.id.aliuser_register_mobile_et || AliUserMobileRegisterFragment.this.mMobileClearBtn == null) {
                if (((EditText) this.editText.get()).getId() == R.id.aliuser_register_sms_code_et && charSequence != null && charSequence.length() > 0 && AliUserMobileRegisterFragment.this.checkcodeUT) {
                    boolean unused = AliUserMobileRegisterFragment.this.checkcodeUT = false;
                    UserTrackAdapter.sendUT("Page_Reg", "InputCode");
                }
            } else if (charSequence != null && charSequence.length() != 0) {
                if (AliUserMobileRegisterFragment.this.mobileUT) {
                    boolean unused2 = AliUserMobileRegisterFragment.this.mobileUT = false;
                    UserTrackAdapter.sendUT("Page_Reg", "InputPhone");
                }
                if (AliUserMobileRegisterFragment.this.mMobileClearBtn.getVisibility() != 0 && AliUserMobileRegisterFragment.this.mMobileClearBtn.isEnabled()) {
                    AliUserMobileRegisterFragment.this.mMobileClearBtn.setVisibility(0);
                }
            } else if (AliUserMobileRegisterFragment.this.mMobileClearBtn.getVisibility() != 8) {
                AliUserMobileRegisterFragment.this.mMobileClearBtn.setVisibility(8);
            }
            AliUserMobileRegisterFragment.this.checkRegAble((EditText) this.editText.get());
        }
    }

    /* access modifiers changed from: protected */
    public void checkRegAble(EditText editText) {
        if (editText.getId() == R.id.aliuser_register_mobile_et) {
            if (TextUtils.isEmpty(this.mMobileET.getText().toString()) || this.mSendSMSCodeBtn.isCountDowning()) {
                this.mSendSMSCodeBtn.setEnabled(false);
            } else {
                this.mSendSMSCodeBtn.setEnabled(true);
            }
        }
        String obj = this.mSMSCodeET.getText().toString();
        if (TextUtils.isEmpty(this.mMobileET.getText().toString()) || TextUtils.isEmpty(obj) || obj.length() < 4) {
            this.mRegBtn.setEnabled(false);
            this.mRegBtnLL.setBackgroundDrawable((Drawable) null);
            return;
        }
        this.mRegBtn.setEnabled(true);
        if (DataProviderFactory.getDataProvider().isTaobaoApp()) {
            this.mRegBtnLL.setBackgroundResource(R.drawable.aliuser_btn_shadow);
        }
    }

    public void onSMSSendFail(RpcResponse rpcResponse) {
        if (rpcResponse == null || rpcResponse.code == 4) {
            toast(getString(R.string.aliuser_sever_error), 0);
            this.mSmsSuccessTipTV.setVisibility(4);
            return;
        }
        toast(rpcResponse.message, 0);
    }

    public void onNeedVerification(String str, int i) {
        NavigatorManager.getInstance().navToVerificationPage(this.mAttachedActivity, str, 1001);
    }

    public void onH5(String str) {
        NavigatorManager.getInstance().navToRegWebViewPage(this.mAttachedActivity, str, getPageName());
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 != -1) {
            return;
        }
        if (i == 1001) {
            Properties properties = new Properties();
            properties.put("result", ApiConstants.UTConstants.UT_SLIDE_SUCCESS);
            UserTrackAdapter.sendUT("Page_Reg", ApiConstants.UTConstants.UT_SLIDE_RESULT, properties);
            if (intent != null && !TextUtils.isEmpty(intent.getStringExtra("sessionId"))) {
                this.mPresenter.setSessionId(intent.getStringExtra("sessionId"));
            }
            onSendSMSSuccess(60000);
        } else if (i == 2001 && intent != null) {
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
                this.mMobileTextWatcher = new RegTextWatcher(this.mMobileET);
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
                AliUserMobileRegisterFragment.this.mRegionTV.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                AliUserMobileRegisterFragment.this.mMobileET.setPadding(AliUserMobileRegisterFragment.this.mMobileET.getPaddingLeft(), AliUserMobileRegisterFragment.this.mMobileET.getPaddingTop(), AliUserMobileRegisterFragment.this.mRegionTV.getWidth() + 30, AliUserMobileRegisterFragment.this.mMobileET.getPaddingBottom());
            }
        });
    }

    public boolean isActive() {
        return isActivityAvaiable();
    }

    public BaseActivity getBaseActivity() {
        return this.mAttachedActivity;
    }

    public void toast(String str, int i) {
        super.toast(str, i);
    }

    public void onGetRegion(List list) {
        if (isActive()) {
            RegionDialogFragment regionDialogFragment = new RegionDialogFragment();
            regionDialogFragment.setList(list);
            regionDialogFragment.setRegionListener(new RegionListener() {
                public void onClick(RegionInfo regionInfo) {
                    AliUserMobileRegisterFragment.this.mRegionInfo = regionInfo;
                    if (AliUserMobileRegisterFragment.this.mRegionInfo != null) {
                        AliUserMobileRegisterFragment.this.mRegionTV.setText(AliUserMobileRegisterFragment.this.mRegionInfo.code);
                        AliUserMobileRegisterFragment.this.resizeMobileETPadding();
                        AliUserMobileRegisterFragment.this.adjustMobileETMaxLength();
                    }
                }
            });
            regionDialogFragment.setCurrentRegion(this.mRegionInfo);
            regionDialogFragment.setupAdapter(getActivity());
            try {
                regionDialogFragment.show(getActivity().getSupportFragmentManager(), "MobileRegisterRegionDialog");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showLoading() {
        showProgress("");
    }

    public void dismissLoading() {
        if (isActivityAvaiable()) {
            dismissProgress();
        }
    }

    public void onDestroy() {
        this.mPresenter.onDestory();
        this.mRegionPresenter.onDestory();
        this.mMobileET.removeTextChangedListener(this.mMobileTextWatcher);
        if (this.mSMSCodeET != null) {
            this.mSMSCodeET.removeTextChangedListener(this.mSmsCodeTextWatcher);
        }
        if (this.mSendSMSCodeBtn != null) {
            this.mSendSMSCodeBtn.cancelCountDown();
        }
        super.onDestroy();
    }

    public void onPrepareOptionsMenu(Menu menu) {
        if (!(menu.findItem(R.id.aliuser_menu_item_help) == null || menu.findItem(R.id.aliuser_menu_item_more) == null)) {
            menu.findItem(R.id.aliuser_menu_item_more).setVisible(false);
            if (AliUserLogin.mAppreanceExtentions == null || AliUserLogin.mAppreanceExtentions.needHelp()) {
                menu.findItem(R.id.aliuser_menu_item_help).setVisible(true);
            } else {
                menu.findItem(R.id.aliuser_menu_item_help).setVisible(false);
            }
        }
        super.onPrepareOptionsMenu(menu);
    }

    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menu.clear();
        menuInflater.inflate(R.menu.aliuser_login_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.aliuser_menu_item_help) {
            UserTrackAdapter.sendControlUT("Page_Reg", "Button-Help");
            String str = "https://ihelp.taobao.com/pocket/visitorServicePortal.htm?from=n_registration_inputphone";
            if (DataProviderFactory.getDataProvider().getSite() == 3) {
                str = LoginConstant.CBU_HELP_URL;
            }
            Intent intent = new Intent(getBaseActivity(), WebViewActivity.class);
            intent.putExtra(WebConstant.WEBURL, str);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
