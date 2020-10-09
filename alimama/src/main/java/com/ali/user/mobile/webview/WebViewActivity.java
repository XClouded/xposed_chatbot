package com.ali.user.mobile.webview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.taobao.windvane.extra.uc.WVUCWebChromeClient;
import android.taobao.windvane.extra.uc.WVUCWebViewClient;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVPluginManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.app.init.Debuggable;
import com.ali.user.mobile.base.UIBaseConstants;
import com.ali.user.mobile.base.helper.BroadCastHelper;
import com.ali.user.mobile.base.ui.BaseActivity;
import com.ali.user.mobile.common.api.AliUserLogin;
import com.ali.user.mobile.coordinator.CoordinatorWrapper;
import com.ali.user.mobile.log.AppMonitorAdapter;
import com.ali.user.mobile.log.LongLifeCycleUserTrack;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.action.LoginResActions;
import com.ali.user.mobile.login.model.LoginConstant;
import com.ali.user.mobile.login.ui.UrlTokenLogin;
import com.ali.user.mobile.login.ui.UserLoginActivity;
import com.ali.user.mobile.login.ui.WebUrlHelper;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.model.UrlParam;
import com.ali.user.mobile.rpc.ApiConstants;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;
import com.ali.user.mobile.service.NavigatorService;
import com.ali.user.mobile.service.ServiceFactory;
import com.ali.user.mobile.ui.R;
import com.ali.user.mobile.ui.WebConstant;
import com.ali.user.mobile.utils.BundleUtil;
import com.ali.user.mobile.verify.impl.VerifyServiceImpl;
import com.ali.user.mobile.verify.model.VerifyParam;
import com.ali.user.mobile.verify.model.VerifyTokenConsumedResponse;
import com.ali.user.mobile.windvane.SecurityGuardBridge;
import com.alibaba.fastjson.JSON;
import com.taobao.login4android.biz.unifysso.UnifySsoLogin;
import com.taobao.login4android.broadcast.LoginAction;
import com.taobao.login4android.session.SessionManager;
import com.taobao.weex.BuildConfig;
import com.uc.webview.export.JsPromptResult;
import com.uc.webview.export.WebView;
import com.uc.webview.export.extension.UCCore;
import com.ut.mini.UTAnalytics;
import java.lang.ref.WeakReference;
import java.util.HashMap;

public class WebViewActivity extends BaseActivity {
    protected static final String CALLBACK = "https://www.alipay.com/webviewbridge";
    public static final String PAGE_NAME = "Page_LoginH5";
    public static final String TAG = "login.web";
    private static String Tag = "login.WebViewActivity";
    protected static final String WINDVANECLOSEALL = "aliusersdkwindvane=closeAll";
    public boolean allowReadTitle = true;
    protected boolean isFromAccount;
    private boolean isShowHelpMenu = false;
    protected boolean isWebviewAlive = true;
    protected RelativeLayout mAPRelativeLayout;
    private String mHelpUrl;
    protected String mIVScene;
    protected String mLoginId;
    protected String mLoginType;
    protected ProgressBar mProgressBar;
    protected String mSNSTrustLoginToken;
    protected String mScene;
    protected String mSecurityId;
    protected int mSite;
    protected String mToken;
    protected String mTokenType;
    protected String mUrl;
    protected String mUserId;
    protected LoginWebView mWebView;
    private LoginParam ssoLoginParam;
    protected WebUrlHelper urlHelper;

    public static Intent getCallingIntent(Activity activity, String str, LoginParam loginParam, LoginReturnData loginReturnData) {
        Intent intent = new Intent(activity, WebViewActivity.class);
        intent.putExtra(WebConstant.WEBURL, str);
        if (loginParam != null) {
            intent.putExtra(WebConstant.WEB_FROM_ACCOUNT, loginParam.isFromAccount);
            intent.putExtra(WebConstant.WEB_LOGIN_TYPE, loginParam.loginType);
            intent.putExtra(WebConstant.WEB_SNS_TRUST_LOGIN_TOKEN, loginParam.snsToken);
            intent.putExtra(WebConstant.WEB_LOGIN_TOKEN_TYPE, loginParam.tokenType);
        }
        if (loginReturnData != null) {
            intent.putExtra("scene", loginReturnData.scene);
            intent.putExtra("token", loginReturnData.token);
            if (loginReturnData.showLoginId != null) {
                intent.putExtra(WebConstant.WEB_LOGIN_ID, loginReturnData.showLoginId);
            } else if (loginParam != null && !TextUtils.isEmpty(loginParam.loginAccount)) {
                intent.putExtra(WebConstant.WEB_LOGIN_ID, loginParam.loginAccount);
            }
            intent.putExtra(WebConstant.SITE, loginReturnData.site);
        }
        return intent;
    }

    public void onCreate(Bundle bundle) {
        try {
            Intent intent = getIntent();
            this.mToken = intent.getStringExtra("token");
            this.mTokenType = intent.getStringExtra(WebConstant.WEB_LOGIN_TOKEN_TYPE);
            this.mUrl = intent.getStringExtra(WebConstant.WEBURL);
            this.mSecurityId = intent.getStringExtra("securityId");
            this.mLoginId = intent.getStringExtra(WebConstant.WEB_LOGIN_ID);
            this.mScene = intent.getStringExtra("scene");
            this.isFromAccount = intent.getBooleanExtra(WebConstant.WEB_FROM_ACCOUNT, false);
            this.mLoginType = intent.getStringExtra(WebConstant.WEB_LOGIN_TYPE);
            this.mSNSTrustLoginToken = intent.getStringExtra(WebConstant.WEB_SNS_TRUST_LOGIN_TOKEN);
            this.mSite = getIntent().getIntExtra(WebConstant.SITE, DataProviderFactory.getDataProvider().getSite());
            this.mTokenType = getIntent().getStringExtra(WebConstant.WEB_LOGIN_TOKEN_TYPE);
            this.mIVScene = intent.getStringExtra(WebConstant.WEB_IV_SCENE);
            this.mUserId = getIntent().getStringExtra("USERID");
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.urlHelper = new WebUrlHelper();
        registerPlugin();
        initDataFromIntent();
        super.onCreate(bundle);
    }

    /* access modifiers changed from: protected */
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initDataFromIntent();
        loadUrl(this.mUrl);
    }

    /* access modifiers changed from: protected */
    public void registerPlugin() {
        WVPluginManager.registerPlugin("Verify", (Class<? extends WVApiPlugin>) SecurityGuardBridge.class);
    }

    private void initDataFromIntent() {
        try {
            this.ssoLoginParam = (LoginParam) getIntent().getSerializableExtra("loginParam");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.aliuser_activity_webview;
    }

    /* access modifiers changed from: protected */
    public void initViews() {
        this.mAPRelativeLayout = (RelativeLayout) findViewById(R.id.aliuser_id_webview);
        this.mProgressBar = (ProgressBar) findViewById(R.id.aliuser_web_progress_bar);
        createWebView();
        init();
    }

    /* access modifiers changed from: protected */
    public void createWebView() {
        this.mWebView = new LoginWebView(this);
        this.mWebView.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    public void init() {
        this.mAPRelativeLayout.addView(this.mWebView);
        this.mWebView.init();
        setWebChromClient();
        setWebViewClient();
        loadUrl(this.mUrl);
    }

    /* access modifiers changed from: protected */
    public void loadUrl(String str) {
        if (!TextUtils.isEmpty(str)) {
            if (Debuggable.isDebug()) {
                TLogAdapter.d(TAG, "load url=" + str);
            }
            this.mWebView.loadUrl(str);
        }
    }

    public static class LoginWebChromeClient extends WVUCWebChromeClient {
        WeakReference<WebViewActivity> reference;

        public LoginWebChromeClient(WebViewActivity webViewActivity) {
            super(webViewActivity);
            this.reference = new WeakReference<>(webViewActivity);
        }

        public void onReceivedTitle(WebView webView, String str) {
            WebViewActivity webViewActivity = (WebViewActivity) this.reference.get();
            if (!(webViewActivity == null || !webViewActivity.allowReadTitle || webViewActivity.getSupportActionBar() == null)) {
                try {
                    webViewActivity.getSupportActionBar().setTitle((CharSequence) str);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
            super.onReceivedTitle(webView, str);
        }

        public boolean onJsPrompt(WebView webView, String str, String str2, String str3, JsPromptResult jsPromptResult) {
            try {
                return super.onJsPrompt(webView, str, str2, str3, jsPromptResult);
            } catch (Throwable unused) {
                return false;
            }
        }
    }

    static class MyLoginWebViewClient extends LoginWebViewClient {
        WeakReference<WebViewActivity> reference;

        public MyLoginWebViewClient(WebViewActivity webViewActivity) {
            super(webViewActivity);
            this.reference = new WeakReference<>(webViewActivity);
        }

        /* access modifiers changed from: protected */
        public boolean overrideUrlLoading(WebView webView, String str) {
            WebViewActivity webViewActivity = (WebViewActivity) this.reference.get();
            if (webViewActivity != null) {
                return webViewActivity.overrideUrlLoading(webView, str);
            }
            return false;
        }

        public void onPageFinished(WebView webView, String str) {
            WebViewActivity webViewActivity = (WebViewActivity) this.reference.get();
            if (!(webViewActivity == null || webViewActivity.mProgressBar == null)) {
                webViewActivity.mProgressBar.setVisibility(8);
            }
            super.onPageFinished(webView, str);
        }
    }

    /* access modifiers changed from: protected */
    public void setWebViewClient() {
        try {
            this.mWebView.setWebViewClient(new MyLoginWebViewClient(this));
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void setWebChromClient() {
        this.mWebView.setWebChromeClient(new LoginWebChromeClient(this));
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"NewApi"})
    public boolean overrideUrlLoading(WebView webView, String str) {
        String str2;
        String str3;
        String str4 = str;
        if (Debuggable.isDebug()) {
            TLogAdapter.d(TAG, "override url=" + str4);
        }
        Uri parse = Uri.parse(str);
        Bundle serialBundle = BundleUtil.serialBundle(parse.getQuery());
        if (serialBundle == null) {
            serialBundle = new Bundle();
        }
        serialBundle.putInt(WebConstant.SITE, this.mSite);
        String string = serialBundle.getString("havana_mobile_reg_otherWebView");
        if (str4.startsWith(WVUCWebViewClient.SCHEME_SMS)) {
            UserTrackAdapter.sendUT(PAGE_NAME, "SMS");
            try {
                startActivity(new Intent("android.intent.action.SENDTO", Uri.parse(str)));
            } catch (Exception unused) {
                TLogAdapter.e("WebViewActivity", "sms exception" + str4);
            }
            return true;
        } else if (this.urlHelper.checkWebviewBridge(str4)) {
            if (!TextUtils.isEmpty(this.mSecurityId)) {
                serialBundle.putString("securityId", this.mSecurityId);
            }
            String string2 = serialBundle.getString("action");
            String string3 = serialBundle.getString("loginId");
            String string4 = serialBundle.getString("token");
            String string5 = serialBundle.getString("scene");
            String string6 = serialBundle.getString("actionType");
            String string7 = serialBundle.getString("mergedAccount");
            if ("open_password_logincheck".equals(string6)) {
                goLogin(string3, serialBundle.getString("havana_iv_token"), (String) null, true, "open_password_logincheck", parse.getQuery(), false, true, false, this.mTokenType);
                return true;
            } else if (TextUtils.isEmpty(string2) || "quit".equals(string2)) {
                UserTrackAdapter.sendUT(PAGE_NAME, "LoginH5_Quit");
                Intent intent = new Intent(LoginResActions.WEB_ACTIVITY_RESULT);
                intent.putExtras(serialBundle);
                BroadCastHelper.sendLocalBroadCast(intent);
                serialBundle.putSerializable("loginParam", this.ssoLoginParam);
                setResult(WebConstant.OPEN_WEB_RESCODE, getIntent().putExtras(serialBundle));
                finish();
                return true;
            } else if ("relogin".equals(string2)) {
                UserTrackAdapter.sendUT(PAGE_NAME, "LoginH5_Relogin");
                serialBundle.putString("aliusersdk_h5querystring", parse.getQuery());
                setResult(WebConstant.WEBVIEW_CAPTCHA_RELOGIN, getIntent().putExtras(serialBundle));
                finish();
                return true;
            } else if ("mobile_confirm_login".equals(string2)) {
                UserTrackAdapter.sendUT(PAGE_NAME, "LoginH5_MobileConfirmLogin");
                if (TextUtils.isEmpty(string3) || string3.equals(BuildConfig.buildJavascriptFrameworkVersion)) {
                    str3 = this.mLoginId;
                } else {
                    str3 = string3;
                }
                goLogin(str3, string4, this.mLoginType, true, "1014", (String) null, false, true, false, this.mTokenType);
                return true;
            } else if (ApiConstants.ApiField.TRUST_LOGIN.equals(string2)) {
                UserTrackAdapter.sendUT(PAGE_NAME, "LoginH5_TrustLogin");
                if (TextUtils.isEmpty(string3) || string3.equals(BuildConfig.buildJavascriptFrameworkVersion)) {
                    str2 = this.mLoginId;
                } else {
                    str2 = string3;
                }
                String str5 = TextUtils.isEmpty(string5) ? this.mScene : string5;
                if ("true".equals(string7)) {
                    BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_ACCOUNT_MERGERED_SUCCESS);
                    this.mTokenType = "mergeAccount";
                } else {
                    BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_FOUND_PASSWORD_SUCCESS);
                }
                goLogin(str2, string4, this.mLoginType, true, str5, (String) null, false, true, false, this.mTokenType);
                return true;
            } else if (TextUtils.equals(LoginConstant.ACTION_CONTINUELOGIN, string2)) {
                UserTrackAdapter.sendUT(PAGE_NAME, "LoginH5_ContinueLogin");
                goLogin(this.mLoginId, this.mToken, this.mLoginType, true, this.mScene, parse.getQuery(), false, true, false, this.mTokenType);
                return true;
            } else if (TextUtils.equals("passIVToken", string2)) {
                UserTrackAdapter.sendUT(PAGE_NAME, "LoginH5_passIVToken");
                String string8 = serialBundle.getString("havana_iv_token");
                HashMap hashMap = new HashMap();
                hashMap.put("token", string8);
                BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_IV_SUCCESS, false, 0, "", hashMap, "");
                finish();
                return true;
            } else if (TextUtils.equals("consumeIVToken", string2)) {
                VerifyParam verifyParam = new VerifyParam();
                verifyParam.userId = this.mUserId;
                verifyParam.actionType = this.mIVScene;
                verifyParam.ivToken = serialBundle.getString("havana_iv_token");
                verifyParam.fromSite = DataProviderFactory.getDataProvider().getSite();
                goConsumeIV(verifyParam);
                return true;
            } else if (TextUtils.equals("testAccountSso", string2)) {
                UnifySsoLogin.tokenLogin(DataProviderFactory.getDataProvider().getSite(), string4, SessionManager.getInstance(DataProviderFactory.getApplicationContext()));
                finish();
                return true;
            } else if (TextUtils.equals("unityTrustLogin", string2)) {
                UnifySsoLogin.tokenLogin(DataProviderFactory.getDataProvider().getSite(), string4, SessionManager.getInstance(DataProviderFactory.getApplicationContext()));
                finish();
                return true;
            } else if (!"loginAfterRegister".equals(string2)) {
                return false;
            } else {
                AppMonitorAdapter.commitSuccess("Page_Member_Register", "Register_Result_AutoLogin");
                finish();
                goLogin(string3, serialBundle.getString("token"), "", true, "1012", (String) null, true, false, false, ApiConstants.UTConstants.UT_TYPE_SMS_LOGIN_TO_REG);
                return true;
            }
        } else if (isProtocalUrl(str4)) {
            Intent intent2 = new Intent();
            intent2.setClass(this, HtmlActivity.class);
            intent2.putExtra(WebConstant.WEBURL, str4);
            startActivity(intent2);
            return true;
        } else if (string != null && "true".equals(string)) {
            Intent intent3 = new Intent();
            intent3.setClass(this, HtmlActivity.class);
            intent3.putExtra(WebConstant.WEBURL, str4);
            startActivity(intent3);
            return true;
        } else if (!str4.contains(WINDVANECLOSEALL)) {
            return false;
        } else {
            finish();
            return true;
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 != -1) {
            switch (i2) {
                case LoginConstant.RESULT_WINDWANE_CLOSEW:
                    String stringExtra = intent.getStringExtra("windvane");
                    loadUrl(stringExtra);
                    String str = Tag;
                    TLogAdapter.d(str, "w: url = " + stringExtra + ",request = " + i + " ; result =" + i2);
                    break;
                case LoginConstant.RESULT_WINDVANE_CLOSEALL:
                    finish();
                    String str2 = Tag;
                    TLogAdapter.d(str2, "all request = " + i + " ; result =" + i2);
                    break;
                default:
                    if (this.mWebView != null) {
                        this.mWebView.onActivityResult(i, i2, intent);
                        break;
                    }
                    break;
            }
        } else {
            String str3 = Tag;
            TLogAdapter.d(str3, "request = " + i + " ; result =" + i2);
            if (this.mWebView != null) {
                this.mWebView.onActivityResult(i, i2, intent);
            }
        }
        TLogAdapter.d(Tag, "call onActivityResult");
        super.onActivityResult(i, i2, intent);
    }

    public void cancleOperation() {
        if (!isProtocalUrl(this.mWebView.getUrl())) {
            setResult(0);
            if (!TextUtils.isEmpty(this.mIVScene)) {
                BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_IV_FAIL, false, -5, LoginConstant.FETCH_IV_FAIL_CANCEL, "");
            } else {
                BroadCastHelper.sendLocalBroadCast(new Intent(LoginResActions.WEB_ACTIVITY_CANCEL));
            }
            if (!TextUtils.isEmpty(LongLifeCycleUserTrack.getResultScene())) {
                LongLifeCycleUserTrack.sendUT(LongLifeCycleUserTrack.getResultScene() + "_FAILURE");
            }
            finish();
        } else if (this.mWebView.canGoBack()) {
            this.mWebView.goBack();
        } else {
            finish();
        }
    }

    public void onBackPressed() {
        cancleOperation();
    }

    private boolean isProtocalUrl(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        if (str.startsWith("http://ab.alipay.com/agreement/contract.htm") || str.startsWith("http://www.taobao.com/go/chn/member/agreement.php") || str.startsWith("https://rule.alibaba.com/rule/detail/2042.htm")) {
            return true;
        }
        return false;
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4) {
            return super.onKeyDown(i, keyEvent);
        }
        cancleOperation();
        return true;
    }

    public String addCallBack(String str) {
        if (str == null || str.indexOf(CALLBACK) >= 0) {
            return str;
        }
        if (str.indexOf("?") > -1) {
            return str + "&callback=" + CALLBACK;
        }
        return str + "?callback=" + CALLBACK;
    }

    public void onDestroy() {
        if (this.isWebviewAlive) {
            try {
                this.mAPRelativeLayout.removeView(this.mWebView);
                this.mWebView.removeAllViews();
                this.mWebView.setVisibility(8);
                this.mWebView.destroy();
            } catch (Throwable th) {
                this.isWebviewAlive = false;
                throw th;
            }
            this.isWebviewAlive = false;
        }
        UrlTokenLogin.resetLoginFlag();
        WVPluginManager.unregisterPlugin("Verify");
        super.onDestroy();
    }

    public void onPause() {
        UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(this);
        if (this.mWebView != null) {
            this.mWebView.onPause();
        }
        super.onPause();
    }

    public void onResume() {
        UTAnalytics.getInstance().getDefaultTracker().updatePageName(this, PAGE_NAME);
        if (this.mWebView != null) {
            this.mWebView.onResume();
        }
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public void goLogin(String str, String str2, String str3, boolean z, String str4, String str5, boolean z2, boolean z3, boolean z4, String str6) {
        LoginParam loginParam = new LoginParam();
        if (str != null && !str.equals(BuildConfig.buildJavascriptFrameworkVersion)) {
            loginParam.loginAccount = str;
        }
        loginParam.loginType = str3;
        loginParam.h5QueryString = str5;
        loginParam.isFromRegister = z2;
        loginParam.isFoundPassword = z3;
        loginParam.loginSite = this.mSite;
        if (str2 != null) {
            loginParam.token = str2;
            loginParam.scene = str4;
            loginParam.tokenType = str6;
        }
        loginParam.snsToken = this.mSNSTrustLoginToken;
        Intent intent = new Intent(this, UserLoginActivity.class);
        intent.putExtra(LoginConstant.LAUNCH_PASS_GUIDE_FRAGMENT, true);
        intent.putExtra(LoginConstant.LAUCNH_MOBILE_LOGIN_FRAGMENT_LABEL, true);
        intent.putExtra("ut_from_register", z2);
        try {
            intent.putExtra(UIBaseConstants.IntentExtrasNamesConstants.PARAM_LOGIN_PARAM, JSON.toJSONString(loginParam));
        } catch (Exception unused) {
        }
        if (z) {
            intent.addFlags(67108864);
            intent.addFlags(UCCore.VERIFY_POLICY_PAK_QUICK);
            finish();
        }
        intent.putExtra(LoginConstant.FORCE_NORMAL_MODE, true);
        startActivity(intent);
    }

    /* access modifiers changed from: protected */
    public void goConsumeIV(final VerifyParam verifyParam) {
        new CoordinatorWrapper().execute(new AsyncTask<Object, Void, VerifyTokenConsumedResponse>() {
            /* access modifiers changed from: protected */
            public VerifyTokenConsumedResponse doInBackground(Object[] objArr) {
                try {
                    return VerifyServiceImpl.getInstance().goNonLoginConsume(verifyParam);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(VerifyTokenConsumedResponse verifyTokenConsumedResponse) {
                if (verifyTokenConsumedResponse == null) {
                    BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_IV_FAIL, false, -1, "RET_NULL", "");
                    WebViewActivity.this.finish();
                } else if (verifyTokenConsumedResponse.code != 3000) {
                    BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_IV_FAIL, false, verifyTokenConsumedResponse.code, verifyTokenConsumedResponse.message, "");
                    WebViewActivity.this.finish();
                } else {
                    BroadCastHelper.sendBroadcast(LoginAction.NOTIFY_IV_SUCCESS);
                    WebViewActivity.this.finish();
                }
            }
        }, new Object[0]);
    }

    @SuppressLint({"RestrictedApi"})
    public void switchHelpMenu(boolean z, String str) {
        this.mHelpUrl = str;
        this.isShowHelpMenu = z;
        invalidateOptionsMenu();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.aliuser_login_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        if (!(menu == null || menu.findItem(R.id.aliuser_menu_item_more) == null || menu.findItem(R.id.aliuser_menu_item_help) == null)) {
            menu.findItem(R.id.aliuser_menu_item_more).setVisible(false);
            if (!this.isShowHelpMenu || TextUtils.isEmpty(this.mHelpUrl)) {
                menu.findItem(R.id.aliuser_menu_item_help).setVisible(false);
            } else if (AliUserLogin.mAppreanceExtentions == null || AliUserLogin.mAppreanceExtentions.needHelp()) {
                menu.findItem(R.id.aliuser_menu_item_help).setVisible(true);
            } else {
                menu.findItem(R.id.aliuser_menu_item_help).setVisible(false);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.aliuser_menu_item_help) {
            UrlParam urlParam = new UrlParam();
            urlParam.scene = "";
            urlParam.url = this.mHelpUrl;
            urlParam.site = DataProviderFactory.getDataProvider().getSite();
            ((NavigatorService) ServiceFactory.getService(NavigatorService.class)).openWebViewPage(this, urlParam);
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
