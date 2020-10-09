package com.ali.user.mobile.login.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.UIBaseConstants;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.LoginType;
import com.ali.user.mobile.login.presenter.UserLoginPresenter;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.RegionInfo;
import com.ali.user.mobile.navigation.NavigatorManager;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;
import com.ali.user.mobile.ui.R;
import com.ali.user.mobile.utils.StringUtil;
import com.alibaba.fastjson.JSON;
import java.util.List;

public class AliUserCheckAuthFragment extends BaseLoginFragment implements UserLoginView {
    private boolean isInBindMode;
    protected TextView mAccountTV;
    protected boolean mActiveLogin = false;
    protected String mCurrentAccount;
    protected String mCurrentPassword;
    protected TextView mForgetPasswordTV;
    protected Button mLoginBtn;
    protected LinearLayout mLoginBtnLL;
    protected LoginParam mLoginParam;
    protected View mPasswordClearBtn;
    protected EditText mPasswordET;
    protected ImageView mShowPasswordIV;
    protected TextWatcher mTextWatcherAccount = null;
    protected TextWatcher mTextWatcherPassword = null;
    protected UserLoginPresenter mUserLoginPresenter;

    /* access modifiers changed from: protected */
    public String getPageName() {
        return ApiConstants.UTConstants.UT_PAGE_CHECK_AUTH_LOGIN;
    }

    /* access modifiers changed from: protected */
    public void onClearAccountBtnClickAction() {
    }

    /* access modifiers changed from: protected */
    public void onDeleteAccount() {
    }

    public void onGetRegion(List<RegionInfo> list) {
    }

    public void setLoginAccountInfo(String str) {
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        initParams();
    }

    private void initParams() {
        LoginParam loginParam;
        Bundle arguments = getArguments();
        if (arguments != null) {
            String str = (String) arguments.get(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM);
            arguments.putString(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM, "");
            if (!TextUtils.isEmpty(str)) {
                loginParam = (LoginParam) JSON.parseObject(str, LoginParam.class);
                this.mLoginParam = loginParam;
                this.mUserLoginPresenter = new UserLoginPresenter(this, loginParam);
            }
        }
        loginParam = null;
        this.mUserLoginPresenter = new UserLoginPresenter(this, loginParam);
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.aliuser_fragment_check_auth;
    }

    public void initViews(View view) {
        super.initViews(view);
        initTextWatcher();
        this.mAccountTV = (TextView) view.findViewById(R.id.aliuser_login_account_tv);
        if (this.mLoginParam != null) {
            if (!TextUtils.isEmpty(this.mLoginParam.loginAccount)) {
                this.mAccountTV.setText(StringUtil.hideAccount(this.mLoginParam.loginAccount));
            }
            if (!TextUtils.isEmpty(this.mLoginParam.headImg)) {
                updateAvatar(this.mLoginParam.headImg);
            }
        }
        this.mPasswordET = (EditText) view.findViewById(R.id.aliuser_login_password_et);
        this.mPasswordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
        this.mPasswordET.addTextChangedListener(this.mTextWatcherPassword);
        this.mPasswordET.setTypeface(Typeface.SANS_SERIF);
        this.mShowPasswordIV = (ImageView) view.findViewById(R.id.aliuser_login_show_password_btn);
        this.mForgetPasswordTV = (TextView) view.findViewById(R.id.aliuser_login_forgot_password_tv);
        this.mLoginBtnLL = (LinearLayout) view.findViewById(R.id.aliuser_login_login_btn_ll);
        this.mPasswordClearBtn = view.findViewById(R.id.aliuser_login_password_clear_iv);
        this.mLoginBtn = (Button) view.findViewById(R.id.aliuser_login_login_btn);
        this.mLoginBtn.setEnabled(false);
        setOnClickListener(this.mLoginBtn, this.mPasswordClearBtn, this.mForgetPasswordTV, this.mShowPasswordIV);
        this.mUserLoginPresenter.onStart();
        this.mAttachedActivity.setContainerBackground(R.color.aliuser_color_white);
        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
        }
    }

    private void initTextWatcher() {
        this.mTextWatcherPassword = new TextWatcher() {
            public void afterTextChanged(Editable editable) {
            }

            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                if (AliUserCheckAuthFragment.this.mPasswordClearBtn != null) {
                    if (charSequence == null || charSequence.length() == 0) {
                        if (AliUserCheckAuthFragment.this.mPasswordClearBtn.getVisibility() != 8) {
                            AliUserCheckAuthFragment.this.mPasswordClearBtn.setVisibility(8);
                        }
                    } else if (AliUserCheckAuthFragment.this.mPasswordClearBtn.getVisibility() != 0) {
                        AliUserCheckAuthFragment.this.mPasswordClearBtn.setVisibility(0);
                    }
                }
                if (AliUserCheckAuthFragment.this.mPasswordET != null) {
                    AliUserCheckAuthFragment.this.checkSignInable();
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    public void checkSignInable() {
        boolean z = !TextUtils.isEmpty(getAccountName()) && !TextUtils.isEmpty(this.mPasswordET.getText().toString());
        this.mLoginBtn.setEnabled(z);
        if (!z || !DataProviderFactory.getDataProvider().isTaobaoApp()) {
            this.mLoginBtnLL.setBackgroundDrawable((Drawable) null);
        } else {
            this.mLoginBtnLL.setBackgroundResource(R.drawable.aliuser_btn_shadow);
        }
    }

    /* access modifiers changed from: protected */
    public String getAccountName() {
        return this.mLoginParam != null ? this.mLoginParam.loginAccount : "";
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
        } else {
            super.onClick(view);
        }
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        this.mUserLoginPresenter.onActivityResult(i, i2, intent);
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
                    AliUserCheckAuthFragment.this.mActivityHelper.dismissAlertDialog();
                }
            }, (String) null, (DialogInterface.OnClickListener) null);
        } else {
            this.mUserLoginPresenter.fetchUrlAndToWebViewWithUserId(this.mAttachedActivity, accountName, this.mUserLoginActivity.mHistoryAccount.userId);
        }
    }

    /* access modifiers changed from: protected */
    public void showErrorMessage(int i) {
        toast(getString(i), 0);
    }

    public void onSuccess(LoginParam loginParam, RpcResponse rpcResponse) {
        UserTrackAdapter.sendUT(getPageName(), "LoginSuccess");
        dismissLoading();
        this.mUserLoginPresenter.onLoginSuccess(loginParam, rpcResponse);
    }

    public void onError(RpcResponse rpcResponse) {
        this.mUserLoginPresenter.onLoginFail(rpcResponse);
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

    public void onPwdError() {
        this.mPasswordET.setText("");
    }

    public void showFindPasswordAlert(LoginParam loginParam, RpcResponse<LoginReturnData> rpcResponse) {
        AnonymousClass3 r5;
        String str;
        String str2;
        if (rpcResponse == null || TextUtils.isEmpty(rpcResponse.codeGroup) || loginParam == null || TextUtils.isEmpty(loginParam.loginType) || ((!TextUtils.equals(ApiConstants.CodeGroup.PWD_ERROR, rpcResponse.codeGroup) && !TextUtils.equals("noRecord", rpcResponse.codeGroup)) || TextUtils.equals(LoginType.ALIPAY_ACCOUNT.getType(), loginParam.loginType))) {
            str = null;
            r5 = null;
        } else {
            String string = getResources().getString(R.string.aliuser_alert_findpwd);
            r5 = new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    UserTrackAdapter.sendControlUT(AliUserCheckAuthFragment.this.getPageName(), "Button-Alert-ResetPwd");
                    String str = "";
                    if (AliUserCheckAuthFragment.this.mLoginParam != null) {
                        str = AliUserCheckAuthFragment.this.mLoginParam.loginAccount;
                    }
                    AliUserCheckAuthFragment.this.mUserLoginPresenter.fetchUrlAndToWebView(AliUserCheckAuthFragment.this.mAttachedActivity, str);
                    AliUserCheckAuthFragment.this.dismissAlertDialog();
                }
            };
            str = string;
        }
        String string2 = getResources().getString(R.string.aliuser_common_ok);
        if (rpcResponse == null) {
            str2 = "";
        } else {
            str2 = rpcResponse.message;
        }
        alert("", str2, str, r5, string2, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                AliUserCheckAuthFragment.this.dismissAlertDialog();
                AliUserCheckAuthFragment.this.onPwdError();
            }
        });
    }

    public void clearPasswordInput() {
        this.mPasswordET.setText("");
    }

    public void setSnsToken(String str) {
        this.isInBindMode = true;
        if (this.mUserLoginPresenter != null) {
            this.mUserLoginPresenter.setSnsToken(str);
        }
    }

    public void onDestroyView() {
        this.mPasswordET.removeTextChangedListener(this.mTextWatcherPassword);
        super.onDestroyView();
    }

    public void onDestroy() {
        super.onDestroy();
        if (this.mUserLoginPresenter != null) {
            this.mUserLoginPresenter.onDestory();
        }
    }
}
