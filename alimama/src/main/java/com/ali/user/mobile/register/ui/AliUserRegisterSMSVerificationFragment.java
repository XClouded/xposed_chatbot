package com.ali.user.mobile.register.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.ui.BaseActivity;
import com.ali.user.mobile.base.ui.BaseFragment;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.RegionInfo;
import com.ali.user.mobile.navigation.NavigatorManager;
import com.ali.user.mobile.register.RegistConstants;
import com.ali.user.mobile.register.model.OceanRegisterParam;
import com.ali.user.mobile.register.presenter.MobileRegisterPresenter;
import com.ali.user.mobile.register.ui.AliUserSmsCodeView;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.service.NavigatorService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.ui.R;
import com.ali.user.mobile.ui.widget.CountDownButton;
import com.ali.user.mobile.utils.MainThreadExecutor;
import com.ali.user.mobile.utils.UTConstans;
import com.alibaba.fastjson.JSON;
import com.taobao.weex.el.parse.Operators;
import java.util.List;
import java.util.Properties;

public class AliUserRegisterSMSVerificationFragment extends BaseFragment implements RegisterFormView, View.OnClickListener {
    public static final String PAGE_NAME = "Page_Reg";
    public static final String TAG = "login.numAuthReg";
    protected String mAreaCode;
    protected String mCountryCode;
    protected String mMobileNum;
    protected MobileRegisterPresenter mPresenter;
    protected CountDownButton mSendSMSCodeBtn;
    protected String mSessionId;
    protected AliUserSmsCodeView mSmsCodeView;

    public String getPageName() {
        return UTConstans.PageName.UT_PAGE_SMS_CODE;
    }

    public void onCheckAuthNumFail() {
    }

    public void onCheckAuthNumSuccess() {
    }

    public void onGetRegion(List<RegionInfo> list) {
    }

    public void onNumAuthRegisterFail(RpcResponse rpcResponse) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initParams();
    }

    private void initParams() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mAreaCode = arguments.getString(RegistConstants.REGISTER_AREA_CODE);
            this.mMobileNum = arguments.getString(RegistConstants.REGISTER_MOBILE_NUM);
            this.mCountryCode = arguments.getString(RegistConstants.REGISTER_COUNTRY_CODE);
            this.mSessionId = arguments.getString(RegistConstants.REGISTER_SESSION_ID);
        }
        this.mPresenter = new MobileRegisterPresenter(this);
        if (!TextUtils.isEmpty(this.mSessionId)) {
            this.mPresenter.setSessionId(this.mSessionId);
        }
    }

    public void onResume() {
        super.onResume();
        UserTrackAdapter.updatePageName(getActivity(), getPageName());
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.aliuser_fragment_register_sms_verification;
    }

    /* access modifiers changed from: protected */
    public void initViews(View view) {
        super.initViews(view);
        ((BaseActivity) getActivity()).getSupportActionBar().setTitle((CharSequence) "");
        ((BaseActivity) getActivity()).setNavigationBackIcon();
        if (!TextUtils.isEmpty(this.mAreaCode) && !TextUtils.isEmpty(this.mMobileNum)) {
            int i = R.string.aliuser_sms_code_secondary_title;
            ((TextView) view.findViewById(R.id.aliuser_register_sms_code_secondary_title_tv)).setText(getString(i, Operators.SPACE_STR + this.mAreaCode + "  " + this.mMobileNum + Operators.SPACE_STR));
        }
        this.mSmsCodeView = (AliUserSmsCodeView) view.findViewById(R.id.aliuser_register_sms_code_view);
        this.mSmsCodeView.setOnCompletedListener(new AliUserSmsCodeView.OnCompletedListener() {
            public void onCompleted(String str) {
                AliUserRegisterSMSVerificationFragment.this.submitRegisterForm();
            }
        });
        this.mSmsCodeView.focus();
        this.mSendSMSCodeBtn = (CountDownButton) view.findViewById(R.id.aliuser_register_send_smscode_btn);
        this.mSendSMSCodeBtn.setOnClickListener(this);
        this.mSendSMSCodeBtn.setGetCodeTitle(R.string.aliuser_signup_verification_reGetCode2);
        this.mSendSMSCodeBtn.setTickTitleRes(R.string.aliuser_sms_code_success_hint2);
        this.mSendSMSCodeBtn.startCountDown(60000, 1000);
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
        if (this.mSmsCodeView != null) {
            this.mSmsCodeView.clearText();
        }
        UserTrackAdapter.sendUT(getPageName(), UTConstans.CustomEvent.UT_REGISTER_RESULT, String.valueOf(i), properties);
        if (TextUtils.isEmpty(str)) {
            str = getString(R.string.aliuser_sever_error);
        }
        toast(str, 0);
    }

    public void onSendSMSSuccess(long j) {
        this.mSendSMSCodeBtn.startCountDown(j, 1000);
    }

    public void toast(String str, int i) {
        if (getActivity() != null) {
            Toast.makeText(getActivity(), str, i).show();
        }
    }

    public void onSMSSendFail(RpcResponse rpcResponse) {
        if (this.mSmsCodeView != null) {
            this.mSmsCodeView.clearText();
        }
    }

    public void onNeedVerification(String str, int i) {
        NavigatorManager.getInstance().navToVerificationPage(this.mAttachedActivity, str, 1001);
    }

    public void onH5(String str) {
        NavigatorManager.getInstance().navToRegWebViewPage(this.mAttachedActivity, str, getPageName());
    }

    /* access modifiers changed from: protected */
    public void sendCodeAction() {
        MainThreadExecutor.execute(new Runnable() {
            public void run() {
                if (AliUserRegisterSMSVerificationFragment.this.mSmsCodeView != null) {
                    AliUserRegisterSMSVerificationFragment.this.mSmsCodeView.clearText();
                }
                UserTrackAdapter.sendControlUT(AliUserRegisterSMSVerificationFragment.this.getPageName(), "Button-SendSms", TextUtils.isEmpty(AliUserRegisterSMSVerificationFragment.this.mMobileNum) ? "" : AliUserRegisterSMSVerificationFragment.this.mMobileNum);
                try {
                    AliUserRegisterSMSVerificationFragment.this.onSendSMSAction();
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onSendSMSAction() {
        this.mPresenter.sendSMS(buildRegisterParam());
    }

    /* access modifiers changed from: protected */
    public OceanRegisterParam buildRegisterParam() {
        OceanRegisterParam oceanRegisterParam = new OceanRegisterParam();
        oceanRegisterParam.mobileNum = this.mMobileNum;
        oceanRegisterParam.checkCode = this.mSmsCodeView.getText();
        oceanRegisterParam.countryCode = this.mCountryCode;
        oceanRegisterParam.sessionId = this.mSessionId;
        return oceanRegisterParam;
    }

    public void submitRegisterForm() {
        UserTrackAdapter.sendControlUT(getPageName(), "Button-Regist");
        this.mPresenter.register(buildRegisterParam());
    }

    public void onClick(View view) {
        if (view.getId() == R.id.aliuser_register_send_smscode_btn) {
            sendCodeAction();
        }
    }

    public boolean isActive() {
        return isActivityAvaiable();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i2 == -1 && i == 1001) {
            Properties properties = new Properties();
            properties.put("result", ApiConstants.UTConstants.UT_SLIDE_SUCCESS);
            UserTrackAdapter.sendUT(getPageName(), ApiConstants.UTConstants.UT_SLIDE_RESULT, properties);
            onSendSMSSuccess(60000);
        }
    }

    public boolean onBackPressed() {
        alert("", getString(R.string.aliuser_exit_smscode_hint), getString(R.string.aliuser_wait_a_moment), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                UserTrackAdapter.sendControlUT(AliUserRegisterSMSVerificationFragment.this.getPageName(), UTConstans.Controls.UT_REG_BACK_BUTTON_CANCEL);
            }
        }, getString(R.string.aliuser_text_back), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                UserTrackAdapter.sendControlUT(AliUserRegisterSMSVerificationFragment.this.getPageName(), UTConstans.Controls.UT_REG_BACK_BUTTON_CLICK);
                if (AliUserRegisterSMSVerificationFragment.this.getActivity() != null) {
                    Intent intent = new Intent();
                    intent.putExtra(RegistConstants.REGISTER_COUNTRY_CODE, AliUserRegisterSMSVerificationFragment.this.mCountryCode);
                    intent.putExtra(RegistConstants.REGISTER_MOBILE_NUM, AliUserRegisterSMSVerificationFragment.this.mMobileNum);
                    intent.putExtra(RegistConstants.REGISTER_AREA_CODE, AliUserRegisterSMSVerificationFragment.this.mAreaCode);
                    ((AliUserRegisterActivity) AliUserRegisterSMSVerificationFragment.this.getActivity()).changeFragmentByConfig(intent);
                }
            }
        });
        return true;
    }
}
