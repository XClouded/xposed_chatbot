package com.ali.user.mobile.login.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.taobao.windvane.extra.uc.WVUCWebChromeClient;
import android.taobao.windvane.extra.uc.WVUCWebView;
import android.taobao.windvane.extra.uc.WVUCWebViewClient;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.app.init.Debuggable;
import com.ali.user.mobile.base.helper.BroadCastHelper;
import com.ali.user.mobile.base.ui.BaseActivity;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.action.LoginResActions;
import com.ali.user.mobile.login.model.LoginConstant;
import com.ali.user.mobile.security.biz.R;
import com.ali.user.mobile.ui.WebConstant;
import com.uc.webview.export.JsPromptResult;
import com.uc.webview.export.SslErrorHandler;
import com.uc.webview.export.WebView;
import java.lang.ref.WeakReference;
import java.util.Properties;

public class H5ContainerActivity extends BaseActivity {
    protected static final String CALLBACK = "https://www.alipay.com/webviewbridge";
    /* access modifiers changed from: private */
    public static String Tag = "login.H5ContainerActivity";
    public boolean allowReadTitle = true;
    protected boolean firstAlert = true;
    protected boolean isFromAccount;
    protected boolean isWebviewAlive = true;
    protected RelativeLayout mAPRelativeLayout;
    protected Activity mActivity;
    protected Context mContext;
    protected String mIVScene;
    protected String mLoginId;
    protected String mLoginType;
    protected ProgressBar mProgressBar;
    protected String mScene;
    protected String mSecurityId;
    protected boolean mSetResult = false;
    protected int mSite;
    protected String mToken;
    protected String mTokenType;
    protected String mUrl;
    protected String mUserId;
    protected WVUCWebView mWebView;
    protected boolean proceed = false;
    protected WebUrlHelper urlHelper;

    public void onCreate(Bundle bundle) {
        try {
            this.mToken = getIntent().getStringExtra("token");
            this.mUrl = getIntent().getStringExtra(WebConstant.WEBURL);
            this.mSetResult = getIntent().getBooleanExtra(WebConstant.WEB_IV_SET_RESULT, false);
            this.mUrl = addCallBack(this.mUrl);
            this.mSecurityId = getIntent().getStringExtra("securityId");
            this.mLoginId = getIntent().getStringExtra(WebConstant.WEB_LOGIN_ID);
            this.mScene = getIntent().getStringExtra("scene");
            this.isFromAccount = getIntent().getBooleanExtra(WebConstant.WEB_FROM_ACCOUNT, false);
            this.mLoginType = getIntent().getStringExtra(WebConstant.WEB_LOGIN_TYPE);
            this.mTokenType = getIntent().getStringExtra(WebConstant.WEB_LOGIN_TOKEN_TYPE);
            this.mSite = getIntent().getIntExtra(WebConstant.SITE, 0);
            this.mUserId = getIntent().getStringExtra("USERID");
            this.mIVScene = getIntent().getStringExtra(WebConstant.WEB_IV_SCENE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mContext = this;
        this.mActivity = this;
        this.urlHelper = new WebUrlHelper();
        super.onCreate(bundle);
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.aliuser_basewebview;
    }

    /* access modifiers changed from: protected */
    public void initViews() {
        this.mAPRelativeLayout = (RelativeLayout) findViewById(R.id.aliuser_webview_contain);
        this.mProgressBar = (ProgressBar) findViewById(R.id.aliuser_web_progress_bar);
        createWebView();
        init();
    }

    public void onBackPressed() {
        cancleOperation();
    }

    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i != 4) {
            return super.onKeyDown(i, keyEvent);
        }
        cancleOperation();
        return true;
    }

    public void cancleOperation() {
        setResult(0);
        BroadCastHelper.sendLocalBroadCast(new Intent(LoginResActions.WEB_ACTIVITY_CANCEL));
        finish();
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

    /* access modifiers changed from: protected */
    public void createWebView() {
        try {
            this.mWebView = new WVUCWebView(this);
        } catch (Exception e) {
            e.printStackTrace();
            finish();
        }
        if (this.mWebView == null) {
            TLogAdapter.e(Tag, "webview init failed, finish activity");
            finish();
        }
        this.mWebView.setLayoutParams(new RelativeLayout.LayoutParams(-2, -2));
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"SetJavaScriptEnabled", "NewApi"})
    public void init() {
        this.mAPRelativeLayout.addView(this.mWebView);
        WebUrlHelper.setWebView(this.mWebView);
        setWebChromClient();
        setWebViewClient();
        webViewLoadUrl(this.mUrl);
    }

    /* access modifiers changed from: protected */
    public void webViewLoadUrl(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mWebView.loadUrl(str);
        }
    }

    public static class LoginWebChromeClient extends WVUCWebChromeClient {
        WeakReference<H5ContainerActivity> reference;

        public LoginWebChromeClient(H5ContainerActivity h5ContainerActivity) {
            super(h5ContainerActivity);
            this.reference = new WeakReference<>(h5ContainerActivity);
        }

        public void onReceivedTitle(WebView webView, String str) {
            H5ContainerActivity h5ContainerActivity = (H5ContainerActivity) this.reference.get();
            if (!(h5ContainerActivity == null || !h5ContainerActivity.allowReadTitle || h5ContainerActivity.getSupportActionBar() == null)) {
                try {
                    h5ContainerActivity.getSupportActionBar().setTitle((CharSequence) str);
                } catch (Throwable th) {
                    th.printStackTrace();
                }
            }
            super.onReceivedTitle(webView, str);
        }

        public boolean onJsPrompt(WebView webView, String str, String str2, String str3, JsPromptResult jsPromptResult) {
            return super.onJsPrompt(webView, str, str2, str3, jsPromptResult);
        }
    }

    static class LoginWebViewClient extends WVUCWebViewClient {
        WeakReference<H5ContainerActivity> reference;

        public LoginWebViewClient(H5ContainerActivity h5ContainerActivity) {
            super(h5ContainerActivity);
            this.reference = new WeakReference<>(h5ContainerActivity);
        }

        /* JADX WARNING: Removed duplicated region for block: B:10:0x002b  */
        /* JADX WARNING: Removed duplicated region for block: B:9:0x002a A[RETURN] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean shouldOverrideUrlLoading(com.uc.webview.export.WebView r5, java.lang.String r6) {
            /*
                r4 = this;
                java.lang.ref.WeakReference<com.ali.user.mobile.login.ui.H5ContainerActivity> r0 = r4.reference
                java.lang.Object r0 = r0.get()
                com.ali.user.mobile.login.ui.H5ContainerActivity r0 = (com.ali.user.mobile.login.ui.H5ContainerActivity) r0
                if (r0 == 0) goto L_0x0026
                boolean r0 = r0.overrideUrlLoading(r5, r6)     // Catch:{ Exception -> 0x000f }
                goto L_0x0027
            L_0x000f:
                r0 = move-exception
                java.lang.String r1 = "H5ContainerActivity"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = "webview内跳转地址有问题"
                r2.append(r3)
                r2.append(r6)
                java.lang.String r2 = r2.toString()
                com.ali.user.mobile.log.TLogAdapter.e(r1, r2, r0)
            L_0x0026:
                r0 = 0
            L_0x0027:
                r1 = 1
                if (r0 != r1) goto L_0x002b
                return r0
            L_0x002b:
                boolean r5 = super.shouldOverrideUrlLoading(r5, r6)
                return r5
            */
            throw new UnsupportedOperationException("Method not decompiled: com.ali.user.mobile.login.ui.H5ContainerActivity.LoginWebViewClient.shouldOverrideUrlLoading(com.uc.webview.export.WebView, java.lang.String):boolean");
        }

        public void onReceivedSslError(WebView webView, final SslErrorHandler sslErrorHandler, SslError sslError) {
            TLogAdapter.e("H5ContainerActivity", "已忽略证书校验的错误！");
            Properties properties = new Properties();
            if (webView.getUrl() != null) {
                properties.setProperty("url", webView.getUrl());
            }
            if (!TextUtils.isEmpty(DataProviderFactory.getDataProvider().getAppkey())) {
                properties.setProperty("appName", DataProviderFactory.getDataProvider().getAppkey());
            }
            UserTrackAdapter.sendUT("Event_ReceivedSslError", properties);
            final H5ContainerActivity h5ContainerActivity = (H5ContainerActivity) this.reference.get();
            if (h5ContainerActivity == null || h5ContainerActivity.firstAlert) {
                AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
                String string = webView.getContext().getResources().getString(R.string.aliuser_ssl_error_info);
                builder.setPositiveButton(webView.getContext().getResources().getString(R.string.aliuser_confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.proceed();
                        if (h5ContainerActivity != null) {
                            h5ContainerActivity.proceed = true;
                        }
                    }
                });
                builder.setNeutralButton(webView.getContext().getResources().getString(R.string.aliuser_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.cancel();
                        if (h5ContainerActivity != null) {
                            h5ContainerActivity.proceed = false;
                        }
                    }
                });
                try {
                    AlertDialog create = builder.create();
                    create.setTitle(webView.getContext().getResources().getString(R.string.aliuser_ssl_error_title));
                    create.setMessage(string);
                    create.show();
                    if (h5ContainerActivity != null) {
                        h5ContainerActivity.firstAlert = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (h5ContainerActivity.proceed) {
                sslErrorHandler.proceed();
            } else {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
            }
        }

        public void onPageFinished(WebView webView, String str) {
            if (Debuggable.isDebug()) {
                String access$000 = H5ContainerActivity.Tag;
                TLogAdapter.d(access$000, "url= onPageFinished " + str);
            }
            H5ContainerActivity h5ContainerActivity = (H5ContainerActivity) this.reference.get();
            if (!(h5ContainerActivity == null || h5ContainerActivity.mProgressBar == null)) {
                h5ContainerActivity.mProgressBar.setVisibility(8);
            }
            super.onPageFinished(webView, str);
        }
    }

    /* access modifiers changed from: protected */
    public void setWebChromClient() {
        this.mWebView.setWebChromeClient(new LoginWebChromeClient(this));
    }

    /* access modifiers changed from: protected */
    public void setWebViewClient() {
        this.mWebView.setWebViewClient(new LoginWebViewClient(this));
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"NewApi"})
    public boolean overrideUrlLoading(WebView webView, String str) {
        return UrlTokenLogin.handleUrl(str, this.mContext, this.mActivity, this.mToken, this.mTokenType, this.mScene, this.mSecurityId, this.mSetResult);
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i2 != -1) {
            switch (i2) {
                case LoginConstant.RESULT_WINDWANE_CLOSEW:
                    webViewLoadUrl(intent.getStringExtra("windvane"));
                    break;
                case LoginConstant.RESULT_WINDVANE_CLOSEALL:
                    finish();
                    break;
                default:
                    if (this.mWebView != null) {
                        this.mWebView.onActivityResult(i, i2, intent);
                        break;
                    }
                    break;
            }
        } else if (this.mWebView != null) {
            this.mWebView.onActivityResult(i, i2, intent);
        }
        TLogAdapter.d(Tag, "call onActivityResult");
        super.onActivityResult(i, i2, intent);
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
        super.onDestroy();
    }

    public void onPause() {
        if (this.mWebView != null) {
            this.mWebView.onPause();
        }
        super.onPause();
    }

    public void onResume() {
        if (this.mWebView != null) {
            this.mWebView.onResume();
        }
        super.onResume();
    }
}
