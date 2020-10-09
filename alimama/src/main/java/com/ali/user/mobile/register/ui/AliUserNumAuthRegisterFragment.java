package com.ali.user.mobile.register.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.taobao.windvane.runtimepermission.PermissionProposer;
import android.text.TextUtils;
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
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.presenter.RegionPresenter;
import com.ali.user.mobile.model.AliValidRequest;
import com.ali.user.mobile.model.NumAuthCallback;
import com.ali.user.mobile.navigation.NavigatorManager;
import com.ali.user.mobile.register.RegistConstants;
import com.ali.user.mobile.register.presenter.MobileRegisterPresenter;
import com.ali.user.mobile.register.ui.AliUserMobileRegisterFragment;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.service.NumberAuthService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.ui.R;
import com.ali.user.mobile.utils.MainThreadExecutor;
import com.ali.user.mobile.utils.ProtocolHelper;
import com.ali.user.mobile.utils.UTConstans;
import com.taobao.login4android.log.LoginTLogAdapter;
import java.util.Properties;

public class AliUserNumAuthRegisterFragment extends AliUserMobileRegisterFragment {
    public static final String NUM_AUTH_REG_PERCENT = "num_auth_reg_percent";
    public static final String TAG = "login.numAuthReg";
    protected long clickTime;
    protected boolean isNumAuthSDKInitSuccess = false;
    protected String mAreaCode;
    protected String mCountryCode;
    protected String mMobileNum;

    public String getPageName() {
        return UTConstans.PageName.UT_PAGE_ONEKEY_REG;
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
        }
    }

    public void onResume() {
        super.onResume();
        UserTrackAdapter.updatePageName(getActivity(), getPageName());
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.aliuser_fragment_num_auth_register;
    }

    /* access modifiers changed from: protected */
    public void initViews(View view) {
        try {
            ((BaseActivity) getActivity()).getSupportActionBar().setTitle((CharSequence) "");
            ((BaseActivity) getActivity()).setNavigationCloseIcon();
        } catch (Throwable th) {
            th.printStackTrace();
        }
        this.mMobileET = (EditText) view.findViewById(R.id.aliuser_register_mobile_et);
        this.mMobileTextWatcher = new AliUserMobileRegisterFragment.RegTextWatcher(this.mMobileET);
        this.mMobileET.addTextChangedListener(this.mMobileTextWatcher);
        this.mRegBtnLL = (LinearLayout) view.findViewById(R.id.aliuser_register_reg_btn_ll);
        this.mRegBtn = (Button) view.findViewById(R.id.aliuser_register_reg_btn);
        this.mRegBtn.setOnClickListener(this);
        ((RelativeLayout) view.findViewById(R.id.aliuser_region_rl)).setOnClickListener(this);
        this.mRegionTV = (TextView) view.findViewById(R.id.aliuser_region_tv);
        this.mRegionTV.setOnClickListener(this);
        initRegionInfo();
        this.mProtocolTV = (TextView) view.findViewById(R.id.aliuser_protocol_tv);
        generateProtocol();
        this.mMobileClearBtn = view.findViewById(R.id.aliuser_login_mobile_clear_iv);
        if (this.mMobileClearBtn != null) {
            this.mMobileClearBtn.setOnClickListener(this);
        }
        if (!TextUtils.isEmpty(this.mMobileNum)) {
            this.mMobileET.setText(this.mMobileNum);
        }
        getMobileFromSim();
    }

    /* access modifiers changed from: protected */
    public void getMobileFromSim() {
        if (Math.abs(AppInfo.getInstance().getUtdid().hashCode()) % 10000 < AliUserRegisterActivity.getSwitch(NUM_AUTH_REG_PERCENT, -1)) {
            UserTrackAdapter.sendUT("Page_Reg", UTConstans.CustomEvent.UT_GET_AUTH_READ_SIM);
            try {
                PermissionProposer.buildPermissionTask(this.mAttachedActivity, new String[]{"android.permission.READ_PHONE_STATE"}).setTaskOnPermissionGranted(new Runnable() {
                    public void run() {
                        if (ServiceFactory.getService(NumberAuthService.class) != null) {
                            ((NumberAuthService) ServiceFactory.getService(NumberAuthService.class)).init(new NumAuthCallback() {
                                public void onInit(boolean z) {
                                    AliUserNumAuthRegisterFragment.this.isNumAuthSDKInitSuccess = z;
                                    try {
                                        Properties properties = new Properties();
                                        properties.setProperty("sessionId", AliUserNumAuthRegisterFragment.this.mPresenter.getSessionId() + "");
                                        if (z) {
                                            UserTrackAdapter.sendUT(AliUserNumAuthRegisterFragment.this.getPageName(), UTConstans.CustomEvent.UT_GET_AUTH_INIT_SUCCESS, properties);
                                        } else {
                                            UserTrackAdapter.sendUT(AliUserNumAuthRegisterFragment.this.getPageName(), UTConstans.CustomEvent.UT_GET_AUTH_INIT_FAIL, properties);
                                        }
                                    } catch (Throwable th) {
                                        th.printStackTrace();
                                    }
                                }

                                public void onGetAuthTokenSuccess(String str) {
                                    long currentTimeMillis = System.currentTimeMillis() - AliUserNumAuthRegisterFragment.this.clickTime;
                                    LoginTLogAdapter.e("login.numAuthReg", "onGetTokenSucc=" + currentTimeMillis);
                                    Properties properties = new Properties();
                                    properties.setProperty("time", String.valueOf(currentTimeMillis));
                                    properties.setProperty("sessionId", AliUserNumAuthRegisterFragment.this.mPresenter.getSessionId() + "");
                                    UserTrackAdapter.sendUT(AliUserNumAuthRegisterFragment.this.getPageName(), UTConstans.CustomEvent.UT_REG_GET_ACCESSCODE_SUCCESS, properties);
                                    try {
                                        AliValidRequest aliValidRequest = new AliValidRequest();
                                        aliValidRequest.accessCode = str;
                                        AliUserNumAuthRegisterFragment.this.mPresenter.numAuthRegister(aliValidRequest, AliUserNumAuthRegisterFragment.this.buildRegisterParam());
                                    } catch (Throwable th) {
                                        th.printStackTrace();
                                        properties.setProperty("time", String.valueOf(System.currentTimeMillis() - AliUserNumAuthRegisterFragment.this.clickTime));
                                        String mobile = AliUserNumAuthRegisterFragment.this.getMobile();
                                        if (!TextUtils.isEmpty(mobile)) {
                                            properties.setProperty(ApiConstants.ApiField.MOBILE, mobile);
                                        }
                                        properties.setProperty("message", th.getMessage());
                                        properties.setProperty("sessionId", AliUserNumAuthRegisterFragment.this.mPresenter.getSessionId() + "");
                                        UserTrackAdapter.sendUT(AliUserNumAuthRegisterFragment.this.getPageName(), UTConstans.CustomEvent.UT_REG_GET_ACCESSCODE_FAIL, properties);
                                        AliUserNumAuthRegisterFragment.this.sendCodeAction();
                                    }
                                }

                                public void onGetAuthTokenFail(int i, String str) {
                                    long currentTimeMillis = System.currentTimeMillis() - AliUserNumAuthRegisterFragment.this.clickTime;
                                    Properties properties = new Properties();
                                    properties.setProperty("time", String.valueOf(currentTimeMillis));
                                    properties.setProperty("code", String.valueOf(i));
                                    if (TextUtils.isEmpty(str)) {
                                        str = "get token failed";
                                    }
                                    properties.setProperty("message", str);
                                    properties.setProperty("sessionId", AliUserNumAuthRegisterFragment.this.mPresenter.getSessionId() + "");
                                    UserTrackAdapter.sendUT(AliUserNumAuthRegisterFragment.this.getPageName(), UTConstans.CustomEvent.UT_REG_GET_ACCESSCODE_FAIL, properties);
                                    AliUserNumAuthRegisterFragment.this.sendCodeAction();
                                }
                            });
                            return;
                        }
                        AliUserNumAuthRegisterFragment.this.sendCodeAction();
                        LoginTLogAdapter.e("login.numAuthReg", "num auth service is null");
                    }
                }).setTaskOnPermissionDenied(new Runnable() {
                    public void run() {
                        AliUserNumAuthRegisterFragment.this.sendCodeAction();
                    }
                }).execute();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void createPresenter() {
        this.mPresenter = new MobileRegisterPresenter(this);
        this.mRegionPresenter = new RegionPresenter(this);
    }

    /* access modifiers changed from: protected */
    public void sendCodeAction() {
        MainThreadExecutor.execute(new Runnable() {
            public void run() {
                AliUserNumAuthRegisterFragment.this.mMobileStr = AliUserNumAuthRegisterFragment.this.getMobile();
                UserTrackAdapter.sendControlUT(AliUserNumAuthRegisterFragment.this.getPageName(), "Button-SendSms", TextUtils.isEmpty(AliUserNumAuthRegisterFragment.this.mMobileStr) ? "" : AliUserNumAuthRegisterFragment.this.mMobileStr);
                try {
                    AliUserNumAuthRegisterFragment.this.onSendSMSAction(false);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        });
    }

    public void onSendSMSSuccess(long j) {
        if (getActivity() != null) {
            Intent intent = new Intent();
            intent.putExtra(RegistConstants.REGISTER_COUNTRY_CODE, getCountryCode());
            intent.putExtra(RegistConstants.REGISTER_MOBILE_NUM, getMobile());
            intent.putExtra(RegistConstants.REGISTER_AREA_CODE, this.mRegionInfo.code);
            intent.putExtra(RegistConstants.REGISTER_SESSION_ID, this.mPresenter.getSessionId());
            ((AliUserRegisterActivity) getActivity()).gotoSmsCodeFragment(intent);
        }
    }

    public void onSMSSendFail(RpcResponse rpcResponse) {
        if (rpcResponse == null || rpcResponse.code == 4) {
            toast(getString(R.string.aliuser_sever_error), 0);
        } else {
            toast(rpcResponse.message, 0);
        }
    }

    public void onNumAuthRegisterFail(RpcResponse rpcResponse) {
        Properties properties = new Properties();
        properties.setProperty("time", String.valueOf(System.currentTimeMillis() - this.clickTime));
        if (!TextUtils.isEmpty(getMobile())) {
            properties.setProperty(ApiConstants.ApiField.MOBILE, getMobile());
        }
        UserTrackAdapter.sendUT(getPageName(), UTConstans.CustomEvent.UT_GET_AUTH_CONFIG_REGISTER_FAIL, properties);
        sendCodeAction();
    }

    public void onNeedVerification(String str, int i) {
        NavigatorManager.getInstance().navToVerificationPage(this.mAttachedActivity, str, i);
    }

    /* access modifiers changed from: protected */
    public void registerAction() {
        try {
            UserTrackAdapter.sendControlUT(getPageName(), UTConstans.Controls.UT_REG_BTN);
        } catch (Throwable th) {
            th.printStackTrace();
        }
        if (this.isNumAuthSDKInitSuccess) {
            this.mMobileStr = getMobile();
            this.clickTime = System.currentTimeMillis();
            UserTrackAdapter.sendControlUT(getPageName(), "Button-NumAuthRegist", TextUtils.isEmpty(this.mMobileStr) ? "" : this.mMobileStr);
            checkQuickReg();
            return;
        }
        sendCodeAction();
    }

    /* access modifiers changed from: protected */
    public void checkQuickReg() {
        this.mMobileStr = getMobile();
        if (!isMobileValid(this.mMobileStr)) {
            Toast.makeText(getBaseActivity(), R.string.aliuser_phone_number_invalidate, 0).show();
        } else {
            this.mPresenter.checkCanAuthNumRegister(buildRegisterParam());
        }
    }

    public void onCheckAuthNumFail() {
        sendCodeAction();
    }

    public void onCheckAuthNumSuccess() {
        Properties properties = new Properties();
        properties.setProperty("sessionId", this.mPresenter.getSessionId() + "");
        UserTrackAdapter.sendUT(getPageName(), UTConstans.CustomEvent.UT_REG_AUTH_ALERT_SHOW, properties);
        alert("", getString(R.string.aliuser_yunyingshang), getString(R.string.aliuser_allow), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                ((NumberAuthService) ServiceFactory.getService(NumberAuthService.class)).getToken();
                try {
                    UserTrackAdapter.sendControlUT(AliUserNumAuthRegisterFragment.this.getPageName(), UTConstans.Controls.UT_REG_AUTH_ALERT_CONFIRM);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        }, getString(R.string.aliuser_disallow), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                AliUserNumAuthRegisterFragment.this.sendCodeAction();
                try {
                    UserTrackAdapter.sendControlUT(AliUserNumAuthRegisterFragment.this.getPageName(), UTConstans.Controls.UT_REG_AUTH_ALERT_CANCEL);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void clearMobile() {
        this.mMobileET.getEditableText().clear();
        this.mMobileET.setEnabled(true);
        this.isVoice = false;
        this.mRegBtn.setText(getResources().getString(R.string.aliuser_agree_and_reg));
    }

    /* access modifiers changed from: protected */
    public void checkRegAble(EditText editText) {
        if (isMobileValid(this.mMobileET.getText().toString())) {
            this.mRegBtn.setEnabled(true);
            if (DataProviderFactory.getDataProvider().isTaobaoApp()) {
                this.mRegBtnLL.setBackgroundResource(R.drawable.aliuser_btn_shadow);
                return;
            }
            return;
        }
        this.mRegBtn.setEnabled(false);
        this.mRegBtnLL.setBackgroundDrawable((Drawable) null);
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 == -1 && i == 1002) {
            String stringExtra = intent.getStringExtra("registerToken");
            String stringExtra2 = intent.getStringExtra("actionType");
            Properties properties = new Properties();
            properties.put("result", ApiConstants.UTConstants.UT_SLIDE_SUCCESS);
            if (!TextUtils.isEmpty(stringExtra)) {
                properties.put("token", stringExtra);
                this.mPresenter.directRegister(stringExtra, false);
            } else if (TextUtils.equals(stringExtra2, ApiConstants.ResultActionType.TOAST)) {
                if (intent != null && !TextUtils.isEmpty(intent.getStringExtra("sessionId"))) {
                    this.mPresenter.setSessionId(intent.getStringExtra("sessionId"));
                }
                onSendSMSSuccess(60000);
            } else {
                sendCodeAction();
            }
            UserTrackAdapter.sendUT(getPageName(), ApiConstants.UTConstants.UT_SLIDE_RESULT, properties);
            return;
        }
        this.mPresenter.setSessionId("");
        super.onActivityResult(i, i2, intent);
    }

    /* access modifiers changed from: protected */
    public void resizeMobileETPadding() {
        this.mRegionTV.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                AliUserNumAuthRegisterFragment.this.mRegionTV.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                AliUserNumAuthRegisterFragment.this.mMobileET.setPadding(AliUserNumAuthRegisterFragment.this.mRegionTV.getWidth() + 30, AliUserNumAuthRegisterFragment.this.mMobileET.getPaddingTop(), AliUserNumAuthRegisterFragment.this.mRegionTV.getWidth() + 30, AliUserNumAuthRegisterFragment.this.mMobileET.getPaddingBottom());
            }
        });
    }

    /* access modifiers changed from: protected */
    public void generateProtocol() {
        ProtocolHelper.generateProtocol(getProtocolModel(), this.mAttachedActivity, this.mProtocolTV, "Page_Reg", false);
    }

    public boolean onBackPressed() {
        alert("", getString(R.string.aliuser_reg_retain_title), getString(R.string.aliuser_reg_continue), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                UserTrackAdapter.sendControlUT(AliUserNumAuthRegisterFragment.this.getPageName(), UTConstans.Controls.UT_REG_BACK_BUTTON_CANCEL);
            }
        }, getString(R.string.aliuser_exit), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                UserTrackAdapter.sendControlUT(AliUserNumAuthRegisterFragment.this.getPageName(), UTConstans.Controls.UT_REG_BACK_BUTTON_CLICK);
                if (AliUserNumAuthRegisterFragment.this.getActivity() != null) {
                    AliUserNumAuthRegisterFragment.this.getActivity().finish();
                }
            }
        });
        return true;
    }
}
