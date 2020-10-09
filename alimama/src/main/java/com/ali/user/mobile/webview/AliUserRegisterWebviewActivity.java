package com.ali.user.mobile.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.ali.user.mobile.app.init.Debuggable;
import com.ali.user.mobile.log.AppMonitorAdapter;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.model.LoginConstant;
import com.ali.user.mobile.register.ui.AliUserRegisterActivity;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.ui.WebConstant;
import com.ali.user.mobile.utils.BundleUtil;
import com.taobao.weex.analyzer.WeexDevOptions;
import com.uc.webview.export.WebView;
import com.uc.webview.export.extension.UCCore;
import java.util.Properties;

public class AliUserRegisterWebviewActivity extends WebViewActivity {
    public static final String page = "Page_RegH5";
    private String active_url = "_ap_action=registerActive";
    private Properties mUTProperties = new Properties();

    public static Intent getCallingIntent(Activity activity, String str, String str2) {
        Intent intent = new Intent(activity, AliUserRegisterWebviewActivity.class);
        intent.putExtra(WebConstant.WEBURL, str);
        intent.putExtra("pageFrom", str2);
        return intent;
    }

    public void onCreate(Bundle bundle) {
        this.isLoginObserver = true;
        super.onCreate(bundle);
        if (getIntent() != null) {
            String stringExtra = getIntent().getStringExtra("pageFrom");
            if (!TextUtils.isEmpty(stringExtra)) {
                this.mUTProperties.put("scene", stringExtra);
            }
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"NewApi"})
    public boolean overrideUrlLoading(WebView webView, String str) {
        String str2;
        String str3 = str;
        if (this.urlHelper.checkWebviewBridge(str3) || str3.contains(this.active_url)) {
            this.allowReadTitle = true;
            Bundle serialBundle = BundleUtil.serialBundle(Uri.parse(str).getQuery());
            if (serialBundle == null) {
                serialBundle = new Bundle();
            }
            String string = serialBundle.getString("action");
            String string2 = serialBundle.getString("loginId");
            String string3 = serialBundle.getString("title");
            String string4 = serialBundle.getString(LoginConstant.EXT_ACTION);
            serialBundle.getString("msg");
            if (!TextUtils.isEmpty(string4)) {
                string = string4;
            }
            String string5 = serialBundle.getString("token");
            String string6 = serialBundle.getString(WeexDevOptions.EXTRA_FROM);
            String string7 = serialBundle.getString("conflict");
            String string8 = serialBundle.getString("isVerification");
            if (Debuggable.isDebug()) {
                if (("registe webview, from=" + string6 + ", loginId=" + string2 + ", token=" + string5) != null) {
                    if ((string5 + ", mToken=" + this.mToken) != null) {
                        str2 = this.mToken;
                        TLogAdapter.d("login.AliUserRegisterWebviewActivity", str2);
                    }
                }
                str2 = "";
                TLogAdapter.d("login.AliUserRegisterWebviewActivity", str2);
            }
            if (!TextUtils.isEmpty(string)) {
                if ("login".equals(string)) {
                    UserTrackAdapter.sendUT(page, "RegH5_Login", this.mUTProperties);
                    AppMonitorAdapter.commitSuccess("Page_Member_Register", "Register_Result_Login");
                    finish();
                    goLogin(string2, (String) null, "taobao", false, (String) null, (String) null, !"Reg_JoinFailed".equals(string6), false, false, (String) null);
                } else if ("registerActive".equals(string)) {
                    UserTrackAdapter.sendUT(page, "RegH5_RgisterActive", this.mUTProperties);
                    return super.overrideUrlLoading(webView, str);
                } else if ("register".equals(string)) {
                    UserTrackAdapter.sendUT(page, "RegH5_Register", this.mUTProperties);
                    finish();
                    goRegister();
                } else if ("loginAfterRegister".equals(string)) {
                    loginAfterRegisterUT(string7, string8);
                    AppMonitorAdapter.commitSuccess("Page_Member_Register", "Register_Result_AutoLogin");
                    finish();
                    goLogin(string2, string5, "taobao", true, "1012", (String) null, true, false, false, ApiConstants.UTConstants.UT_TYPE_SMS_LOGIN_TO_REG);
                }
                return true;
            } else if (!TextUtils.isEmpty(string3)) {
                this.allowReadTitle = false;
                if (getSupportActionBar() != null) {
                    try {
                        getSupportActionBar().setTitle((CharSequence) string3);
                    } catch (Throwable th) {
                        th.printStackTrace();
                    }
                }
                webView.loadUrl(str);
                return true;
            }
        }
        return super.overrideUrlLoading(webView, str);
    }

    private void loginAfterRegisterUT(String str, String str2) {
        String str3;
        String str4 = "";
        try {
            if (TextUtils.isEmpty(str)) {
                str3 = "register";
            } else {
                str3 = "login";
                str4 = "true".equals(str2) ? "isVerification=Y" : "isVerification=N";
            }
            UserTrackAdapter.sendUT(page, "RegH5_LoginAfterRegister", str3, str4, this.mUTProperties);
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void goRegister() {
        Intent callingIntent = AliUserRegisterActivity.getCallingIntent(this);
        callingIntent.addFlags(67108864);
        callingIntent.addFlags(UCCore.VERIFY_POLICY_PAK_QUICK);
        finish();
        startActivity(callingIntent);
    }

    public void onPause() {
        super.onPause();
        UserTrackAdapter.pageDisAppear(this);
    }

    public void onResume() {
        super.onResume();
        UserTrackAdapter.updatePageName(this, page);
    }
}
