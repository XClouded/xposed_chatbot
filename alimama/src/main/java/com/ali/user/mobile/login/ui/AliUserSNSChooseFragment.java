package com.ali.user.mobile.login.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.UIBaseConstants;
import com.ali.user.mobile.base.ui.BaseActivity;
import com.ali.user.mobile.base.ui.BaseFragment;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.LoginType;
import com.ali.user.mobile.login.presenter.BaseLoginPresenter;
import com.ali.user.mobile.login.presenter.FastRegPresenter;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.RegionInfo;
import com.ali.user.mobile.navigation.NavigatorManager;
import com.ali.user.mobile.register.ProtocolModel;
import com.ali.user.mobile.register.model.OceanRegisterParam;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.HistoryAccount;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.ui.R;
import com.ali.user.mobile.utils.ProtocolHelper;
import com.ali.user.mobile.utils.UTConstans;
import com.alibaba.fastjson.JSON;
import com.ut.mini.UTAnalytics;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.regex.Pattern;

public class AliUserSNSChooseFragment extends BaseFragment implements FastRegView, BaseLoginView, View.OnClickListener {
    public static final String PAGE_NAME = "Page_SNS_Register";
    public static Pattern pattern = Pattern.compile("^[_a-z0-9-]+(\\.[_a-z0-9-]*)*@[a-z0-9-]+([\\.][a-z0-9-]+)+$");
    protected View mAccountClearBtn;
    protected String mEmail;
    protected TextView mEmailExistTV;
    protected TextView mGoLoginHint;
    protected TextView mGoLoginTV;
    protected TextView mInputEmailHint;
    protected TextView mInputEmailTitle;
    protected LoginParam mIntentParam;
    protected BaseLoginPresenter mLoginPresenter;
    protected FastRegPresenter mPresenter;
    protected TextView mProtocolTV;
    protected TextView mProtocolTV2;
    protected Button mQButton;
    protected String mSNSToken;
    protected String mSNSType;
    protected TextWatcher mTextWatcherAccount = null;
    protected EditText mUserInputEmail;

    /* access modifiers changed from: private */
    public void checkSignInable() {
    }

    public HistoryAccount getHistoryAccount() {
        return null;
    }

    public boolean isHistoryMode() {
        return false;
    }

    public void onGetRegion(List<RegionInfo> list) {
    }

    public void setLoginAccountInfo(String str) {
    }

    private void initAccountWatcher() {
        this.mTextWatcherAccount = new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (AliUserSNSChooseFragment.this.mAccountClearBtn != null) {
                    if (charSequence == null || charSequence.length() == 0) {
                        if (AliUserSNSChooseFragment.this.mAccountClearBtn.getVisibility() != 8) {
                            AliUserSNSChooseFragment.this.mAccountClearBtn.setVisibility(8);
                        }
                    } else if (AliUserSNSChooseFragment.this.mAccountClearBtn.getVisibility() != 0 && AliUserSNSChooseFragment.this.mAccountClearBtn.isEnabled()) {
                        AliUserSNSChooseFragment.this.mAccountClearBtn.setVisibility(0);
                    }
                }
                AliUserSNSChooseFragment.this.checkSignInable();
            }
        };
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mPresenter = new FastRegPresenter(this);
        initParams();
    }

    private void initParams() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            String str = (String) arguments.get(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM);
            arguments.putString(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM, "");
            if (!TextUtils.isEmpty(str)) {
                LoginParam loginParam = (LoginParam) JSON.parseObject(str, LoginParam.class);
                if (loginParam != null) {
                    this.mSNSToken = loginParam.snsToken;
                    this.mEmail = loginParam.loginAccount;
                    this.mSNSType = loginParam.snsType;
                }
                this.mIntentParam = loginParam;
            }
        }
    }

    public void onResume() {
        super.onResume();
        UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(getActivity());
        UTAnalytics.getInstance().getDefaultTracker().pageAppearDonotSkip(getActivity(), "Page_SNS_Register");
    }

    public static boolean isEmailValid(String str) {
        return pattern.matcher(str).matches();
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.aliuser_fragment_sns_choose;
    }

    public void initViews(View view) {
        super.initViews(view);
        this.mAccountClearBtn = view.findViewById(R.id.aliuser_sns_password_clear_iv);
        this.mInputEmailTitle = (TextView) view.findViewById(R.id.aliuser_sns_add_email_title);
        this.mInputEmailHint = (TextView) view.findViewById(R.id.aliuser_sns_add_email_hint);
        this.mEmailExistTV = (TextView) view.findViewById(R.id.aliuser_sns_title_exist);
        this.mGoLoginHint = (TextView) view.findViewById(R.id.aliuser_sns_already_hint);
        this.mQButton = (Button) view.findViewById(R.id.aliuser_sns_q);
        this.mGoLoginTV = (TextView) view.findViewById(R.id.aliuser_sns_go_login);
        this.mUserInputEmail = (EditText) view.findViewById(R.id.aliuser_sns_email_input);
        this.mProtocolTV = (TextView) view.findViewById(R.id.aliuser_sns_protocol_tv);
        this.mProtocolTV2 = (TextView) view.findViewById(R.id.aliuser_sns_protocol_tv2);
        ProtocolHelper.generateProtocol(getProtocolModel(this.mAttachedActivity), this.mAttachedActivity, this.mProtocolTV, "Page_SNS_Register", true);
        setOnClickListener(this.mQButton, this.mGoLoginTV, this.mAccountClearBtn);
        if (!TextUtils.isEmpty(this.mEmail)) {
            this.mEmailExistTV.setText(this.mEmail);
            this.mInputEmailTitle.setText(getString(R.string.aliuser_sns_welcome_title));
            this.mInputEmailHint.setText(getString(R.string.aliuser_sns_reg_hint));
            this.mUserInputEmail.setVisibility(8);
            this.mAccountClearBtn.setVisibility(8);
            this.mEmailExistTV.setVisibility(0);
            this.mGoLoginHint.setVisibility(8);
            this.mGoLoginTV.setVisibility(8);
            this.mQButton.setEnabled(true);
            this.mProtocolTV.setVisibility(8);
            this.mProtocolTV2.setVisibility(0);
            ProtocolHelper.generateProtocol(getProtocolModel(this.mAttachedActivity), this.mAttachedActivity, this.mProtocolTV2, "Page_SNS_Register", true);
        } else {
            this.mUserInputEmail.setVisibility(0);
            this.mEmailExistTV.setVisibility(8);
            this.mProtocolTV.setVisibility(0);
            this.mProtocolTV2.setVisibility(8);
            initAccountWatcher();
            this.mUserInputEmail.addTextChangedListener(this.mTextWatcherAccount);
            ProtocolHelper.generateProtocol(getProtocolModel(this.mAttachedActivity), this.mAttachedActivity, this.mProtocolTV, "Page_SNS_Register", true);
        }
        this.mAttachedActivity.setContainerBackground(R.color.aliuser_color_white);
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
        }
    }

    public static ProtocolModel getProtocolModel(Context context) {
        ProtocolModel protocolModel = new ProtocolModel();
        protocolModel.protocolTitle = context.getString(R.string.aliuser_protocal_text);
        HashMap hashMap = new HashMap();
        hashMap.put(context.getString(R.string.aliuser_tb_protocal), context.getString(R.string.aliuser_tb_protocal_url));
        hashMap.put(context.getString(R.string.aliuser_policy_protocal), context.getString(R.string.aliuser_policy_protocal_url));
        hashMap.put(context.getString(R.string.aliuser_law_protocal), context.getString(R.string.aliuser_law_protocal_url));
        hashMap.put(context.getString(R.string.aliuser_alipay_protocal), context.getString(R.string.aliuser_alipay_protocal_url));
        protocolModel.protocolItems = hashMap;
        protocolModel.protocolItemColor = R.color.aliuser_default_text_color;
        return protocolModel;
    }

    /* access modifiers changed from: protected */
    public void setOnClickListener(View... viewArr) {
        for (View view : viewArr) {
            if (view != null) {
                view.setClickable(true);
                view.setOnClickListener(this);
            }
        }
    }

    public boolean isActive() {
        return isActivityAvaiable();
    }

    public void showLoading() {
        showProgress("");
    }

    public void dismissLoading() {
        if (isActivityAvaiable()) {
            dismissProgress();
        }
    }

    public void toast(String str, int i) {
        super.toast(str, i);
    }

    public void onRegisterSuccess(String str) {
        Properties properties = new Properties();
        properties.setProperty("snsType", this.mSNSType);
        UserTrackAdapter.sendUT("Page_SNS_Register", "RegSuccess", properties);
        LoginParam loginParam = new LoginParam();
        loginParam.token = str;
        loginParam.scene = "1012";
        loginParam.tokenType = ApiConstants.UTConstants.UT_TYPE_SMS_LOGIN_TO_REG;
        loginParam.loginSite = DataProviderFactory.getDataProvider().getSite();
        this.mLoginPresenter = new BaseLoginPresenter(this, loginParam);
        this.mLoginPresenter.login();
    }

    public void onRegisterFail(int i, String str) {
        Properties properties = new Properties();
        if (!TextUtils.isEmpty(str)) {
            properties.setProperty("regFailMsg", str);
        }
        UserTrackAdapter.sendUT("Page_SNS_Register", UTConstans.CustomEvent.UT_REGISTER_RESULT, String.valueOf(i), properties);
        if (TextUtils.isEmpty(str)) {
            str = getString(R.string.aliuser_sever_error);
        }
        toast(str, 0);
    }

    public BaseActivity getBaseActivity() {
        return this.mAttachedActivity;
    }

    public void onSuccess(LoginParam loginParam, RpcResponse rpcResponse) {
        Properties properties = new Properties();
        properties.setProperty("snsType", this.mSNSType);
        UserTrackAdapter.sendUT("Page_SNS_Register", "LoginSuccess", properties);
        dismissLoading();
        if (this.mLoginPresenter != null) {
            this.mLoginPresenter.onLoginSuccess(loginParam, rpcResponse);
        }
    }

    public void onError(RpcResponse rpcResponse) {
        if (this.mLoginPresenter != null) {
            this.mLoginPresenter.onLoginFail(rpcResponse);
        }
    }

    public void alert(String str, String str2, String str3, DialogInterface.OnClickListener onClickListener, String str4, DialogInterface.OnClickListener onClickListener2) {
        super.alert(str, str2, str3, onClickListener, str4, onClickListener2);
    }

    public void dismissAlertDialog() {
        super.dismissAlertDialog();
    }

    public void onNeedVerification(String str, int i) {
        NavigatorManager.getInstance().navToVerificationPage(getActivity(), str, i);
    }

    public LoginType getLoginType() {
        return LoginType.TAOBAO_ACCOUNT;
    }

    public int getLoginSite() {
        return DataProviderFactory.getDataProvider().getSite();
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.aliuser_sns_q) {
            UserTrackAdapter.sendControlUT("Page_SNS_Register", "Button-Reg");
            String str = this.mEmail;
            boolean z = false;
            if (TextUtils.isEmpty(this.mEmail)) {
                str = this.mUserInputEmail.getText().toString();
                if (!TextUtils.isEmpty(str) && !isEmailValid(str)) {
                    Toast.makeText(getBaseActivity(), R.string.aliuser_sns_email_invalid, 0).show();
                    return;
                }
            }
            OceanRegisterParam oceanRegisterParam = new OceanRegisterParam();
            oceanRegisterParam.email = str;
            if (!TextUtils.isEmpty(this.mEmail) && TextUtils.equals(this.mEmail, oceanRegisterParam.email)) {
                z = true;
                oceanRegisterParam.thirdType = this.mSNSType;
            }
            this.mPresenter.fastReg(oceanRegisterParam, this.mSNSToken, z);
        } else if (id == R.id.aliuser_sns_go_login) {
            UserTrackAdapter.sendControlUT("Page_SNS_Register", "Button-GoLogin");
            UserLoginActivity userLoginActivity = (UserLoginActivity) this.mAttachedActivity;
            Intent intent = new Intent();
            if (this.mIntentParam != null) {
                intent.putExtra(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM, JSON.toJSONString(this.mIntentParam));
            }
            userLoginActivity.goPwdOrSMSFragment(intent);
        } else if (id == R.id.aliuser_sns_password_clear_iv) {
            onClearAccountBtnClickAction();
        }
    }

    private void onClearAccountBtnClickAction() {
        this.mUserInputEmail.getEditableText().clear();
        this.mUserInputEmail.setEnabled(true);
    }
}
