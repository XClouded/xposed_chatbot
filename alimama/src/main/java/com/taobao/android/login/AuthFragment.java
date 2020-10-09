package com.taobao.android.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.taobao.windvane.extra.uc.WVUCWebChromeClient;
import android.taobao.windvane.extra.uc.WVUCWebView;
import android.taobao.windvane.extra.uc.WVUCWebViewClient;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ali.user.mobile.app.dataprovider.DataProviderFactory;
import com.ali.user.mobile.base.BaseLogonFragment;
import com.ali.user.mobile.info.AlipayInfo;
import com.ali.user.mobile.info.AppInfo;
import com.ali.user.mobile.log.TLogAdapter;
import com.ali.user.mobile.log.UserTrackAdapter;
import com.ali.user.mobile.login.service.impl.UserLoginServiceImpl;
import com.ali.user.mobile.login.ui.UrlTokenLogin;
import com.ali.user.mobile.model.LoginParam;
import com.ali.user.mobile.rpc.RpcResponse;
import com.ali.user.mobile.rpc.login.model.LoginReturnData;
import com.ali.user.mobile.ui.WebConstant;
import com.ali.user.mobile.utils.BundleUtil;
import com.ali.user.mobile.utils.StringUtil;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.taobao.android.sso.R;
import com.taobao.android.sso.v2.launch.SsoLogin;
import com.taobao.android.sso.v2.launch.model.ISsoRemoteParam;
import com.taobao.android.tlog.protocol.Constants;
import com.taobao.login4android.constants.LoginEnvType;
import com.taobao.login4android.login.LoginResultHelper;
import com.taobao.login4android.session.SessionManager;
import com.taobao.weex.BuildConfig;
import com.uc.webview.export.SslErrorHandler;
import com.uc.webview.export.WebSettings;
import com.uc.webview.export.WebView;
import com.ut.mini.UTAnalytics;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Properties;

public class AuthFragment extends BaseLogonFragment implements View.OnClickListener {
    public static final String PAGE_NAME = "Page_Auth";
    protected static String Page_Name = "Page_AuthLogin";
    public static final String TAG = "login.AuthFragment";
    protected static final String errorHtml = "<html><body><h1>Page not find！</h1></body></html>";
    protected boolean firstAlert = true;
    protected boolean isWebviewAlive = true;
    protected RelativeLayout mAPRelativeLayout;
    protected AuthActivity mAttachedActivity;
    protected ImageView mBackImageView;
    protected String mIVScene;
    protected TextView mLineTextView;
    protected String mLoginId;
    protected String mLoginType;
    protected LinearLayout mMethod1LinearLayout;
    protected TextView mMethod1TextView;
    protected String mScene;
    protected String mSecurityId;
    protected boolean mSetResult = false;
    protected int mSite;
    protected ImageView mSsoImageView;
    protected LinearLayout mSsoLinearLayout;
    protected String mToken;
    protected String mTokenType;
    protected String mUrl;
    protected String mUserId;
    protected WVUCWebView mWebView;
    protected LinearLayout mWebViewLinearLayout;
    protected boolean proceed = false;
    ISsoRemoteParam ssoRemoteParam;

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.mAttachedActivity = (AuthActivity) activity;
    }

    public void onActivityCreated(Bundle bundle) {
        TLogAdapter.d(TAG, Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_CREATED);
        super.onActivityCreated(bundle);
        if (getArguments() != null) {
            try {
                this.mToken = getArguments().getString("token");
                this.mUrl = getArguments().getString(WebConstant.WEBURL);
                this.mSetResult = getArguments().getBoolean(WebConstant.WEB_IV_SET_RESULT, false);
                this.mSecurityId = getArguments().getString("securityId");
                this.mLoginId = getArguments().getString(WebConstant.WEB_LOGIN_ID);
                this.mScene = getArguments().getString("scene");
                this.mLoginType = getArguments().getString(WebConstant.WEB_LOGIN_TYPE);
                this.mTokenType = getArguments().getString(WebConstant.WEB_LOGIN_TOKEN_TYPE);
                this.mSite = getArguments().getInt(WebConstant.SITE, DataProviderFactory.getDataProvider().getSite());
                this.mUserId = getArguments().getString("USERID");
                this.mIVScene = getArguments().getString(WebConstant.WEB_IV_SCENE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(getLayoutContent(), viewGroup, false);
        initViews(inflate);
        return inflate;
    }

    public void onResume() {
        TLogAdapter.d(TAG, UmbrellaConstants.LIFECYCLE_RESUME);
        super.onResume();
        UTAnalytics.getInstance().getDefaultTracker().pageDisAppear(getActivity());
        UTAnalytics.getInstance().getDefaultTracker().pageAppearDonotSkip(getActivity(), PAGE_NAME);
    }

    /* access modifiers changed from: protected */
    public void initViews(View view) {
        this.mMethod1TextView = (TextView) view.findViewById(R.id.aliuser_auth_method1);
        this.mLineTextView = (TextView) view.findViewById(R.id.aliuser_auth_line);
        this.mSsoLinearLayout = (LinearLayout) view.findViewById(R.id.aliuser_auth_sso_ll);
        this.mSsoImageView = (ImageView) view.findViewById(R.id.aliuser_auth_taobao);
        this.mBackImageView = (ImageView) view.findViewById(R.id.aliuser_auth_back);
        if (this.mBackImageView != null) {
            this.mBackImageView.setOnClickListener(this);
        }
        this.ssoRemoteParam = new ISsoRemoteParam() {
            public String getServerTime() {
                return BuildConfig.buildJavascriptFrameworkVersion;
            }

            public String getTtid() {
                return DataProviderFactory.getDataProvider().getTTID();
            }

            public String getImei() {
                return DataProviderFactory.getDataProvider().getImei();
            }

            public String getImsi() {
                return DataProviderFactory.getDataProvider().getImsi();
            }

            public String getDeviceId() {
                return DataProviderFactory.getDataProvider().getDeviceId();
            }

            public String getAppKey() {
                return DataProviderFactory.getDataProvider().getAppkey();
            }

            public String getApdid() {
                return AlipayInfo.getInstance().getApdid();
            }

            public String getUmidToken() {
                return AppInfo.getInstance().getUmidToken();
            }

            public String getAtlas() {
                return DataProviderFactory.getDataProvider().getEnvType() == LoginEnvType.DEV.getSdkEnvType() ? "daily" : "";
            }
        };
        createWebView();
        this.mAPRelativeLayout = (RelativeLayout) view.findViewById(R.id.aliuser_webview_container);
        this.mMethod1LinearLayout = (LinearLayout) view.findViewById(R.id.aliuser_auth_method1_ll);
        this.mWebViewLinearLayout = (LinearLayout) view.findViewById(R.id.aliuser_auth_webview_ll);
        if (this.mAPRelativeLayout != null) {
            this.mAPRelativeLayout.addView(this.mWebView);
        }
        setWebView(this.mWebView);
        setWebChromClient();
        setWebViewClient();
        UserTrackAdapter.sendUT(Page_Name, "Kids_QrCode");
        webViewLoadUrl(DataProviderFactory.getDataProvider().getQrCodeUrl());
        if (!SsoLogin.isTaobaoAppInstalled(this.mAttachedActivity)) {
            if (this.mMethod1TextView != null) {
                this.mMethod1TextView.setVisibility(8);
            }
            if (this.mLineTextView != null) {
                this.mLineTextView.setVisibility(8);
            }
            if (this.mSsoLinearLayout != null) {
                this.mSsoLinearLayout.setVisibility(8);
            }
            int dimension = (int) getResources().getDimension(R.dimen.ali_user_auth_qrcode_width);
            if (this.mAPRelativeLayout != null) {
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(dimension, (int) getResources().getDimension(R.dimen.ali_user_auth_qrcode_width));
                layoutParams.gravity = 17;
                this.mAPRelativeLayout.setLayoutParams(layoutParams);
            }
            if (this.mMethod1LinearLayout != null) {
                LinearLayout.LayoutParams layoutParams2 = new LinearLayout.LayoutParams(dimension, -2);
                layoutParams2.gravity = 17;
                this.mMethod1LinearLayout.setLayoutParams(layoutParams2);
            }
            if (this.mWebViewLinearLayout != null) {
                this.mWebViewLinearLayout.setPadding(0, 0, 0, 0);
            }
        } else if (this.mSsoImageView != null) {
            this.mSsoImageView.setOnClickListener(this);
        }
    }

    /* access modifiers changed from: protected */
    public void setWebChromClient() {
        this.mWebView.setWebChromeClient(new WVUCWebChromeClient());
    }

    /* access modifiers changed from: protected */
    public void setWebViewClient() {
        this.mWebView.setWebViewClient(new LoginWebViewClient(this));
    }

    public static void setWebView(WVUCWebView wVUCWebView) {
        wVUCWebView.setVerticalScrollbarOverlay(true);
        WebSettings settings = wVUCWebView.getSettings();
        settings.setSupportMultipleWindows(false);
        settings.setJavaScriptEnabled(true);
        settings.setSavePassword(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setDomStorageEnabled(true);
        settings.setAppCachePath(DataProviderFactory.getApplicationContext().getDir("cache", 0).getPath());
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(-1);
        settings.setBuiltInZoomControls(false);
        Log.i("", "user agent=" + settings.getUserAgentString());
        if (Build.VERSION.SDK_INT >= 7) {
            try {
                Method method = wVUCWebView.getSettings().getClass().getMethod("setDomStorageEnabled", new Class[]{Boolean.TYPE});
                if (method != null) {
                    method.invoke(wVUCWebView.getSettings(), new Object[]{true});
                }
            } catch (Exception e) {
                TLogAdapter.e("", "2.2 setDomStorageEnabled Failed!", e);
            }
        }
        try {
            wVUCWebView.removeJavascriptInterface("searchBoxJavaBridge_");
            wVUCWebView.removeJavascriptInterface("accessibility");
            wVUCWebView.removeJavascriptInterface("accessibilityTraversal");
        } catch (Exception e2) {
            e2.printStackTrace();
        }
        wVUCWebView.setBackgroundColor(0);
        wVUCWebView.getBackground().setAlpha(0);
    }

    /* access modifiers changed from: protected */
    public void webViewLoadUrl(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mWebView.loadUrl(str);
        }
    }

    /* access modifiers changed from: protected */
    public void createWebView() {
        try {
            this.mWebView = new WVUCWebView(this.mAttachedActivity);
        } catch (Exception e) {
            e.printStackTrace();
            this.mAttachedActivity.finish();
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -1);
        int dimension = (int) getResources().getDimension(R.dimen.ali_user_space_20);
        this.mWebView.setPadding(dimension, dimension, dimension, dimension);
        this.mWebView.setLayoutParams(layoutParams);
    }

    /* access modifiers changed from: protected */
    public int getLayoutContent() {
        return R.layout.aliuser_auth_fragment;
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.aliuser_auth_taobao) {
            if (SsoLogin.isSupportTBAuthBind(this.mAttachedActivity)) {
                UserTrackAdapter.sendControlUT(Page_Name, "AuthTao");
                SsoLogin.bindAuth(this.mAttachedActivity, this.ssoRemoteParam, getDefaultCallbackClass());
            } else if (SsoLogin.isTaobaoAppInstalled(this.mAttachedActivity)) {
                toast(getResources().getString(R.string.aliuser_auth_upgrade_hint), 0);
            }
        } else if (id == R.id.aliuser_auth_back) {
            this.mAttachedActivity.finishCurrentAndNotify();
        }
    }

    /* access modifiers changed from: protected */
    public String getDefaultCallbackClass() {
        return this.mAttachedActivity.getPackageName() + ".ResultActivity";
    }

    static class LoginWebViewClient extends WVUCWebViewClient {
        WeakReference<AuthFragment> reference;

        public LoginWebViewClient(AuthFragment authFragment) {
            super(authFragment.getActivity());
            this.reference = new WeakReference<>(authFragment);
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            boolean overrideUrlLoading = this.reference.get() != null ? ((AuthFragment) this.reference.get()).overrideUrlLoading(webView, str) : false;
            if (overrideUrlLoading) {
                return overrideUrlLoading;
            }
            return super.shouldOverrideUrlLoading(webView, str);
        }

        public void onReceivedError(WebView webView, int i, String str, String str2) {
            Toast.makeText(DataProviderFactory.getApplicationContext(), webView.getResources().getString(R.string.aliuser_network_error), 0).show();
            webView.setVisibility(8);
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
            final AuthFragment authFragment = (AuthFragment) this.reference.get();
            if (authFragment == null || authFragment.firstAlert) {
                AlertDialog.Builder builder = new AlertDialog.Builder(webView.getContext());
                String string = webView.getContext().getResources().getString(com.ali.user.mobile.security.biz.R.string.aliuser_ssl_error_info);
                builder.setPositiveButton(webView.getContext().getResources().getString(com.ali.user.mobile.security.biz.R.string.aliuser_confirm), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.proceed();
                        if (authFragment != null) {
                            authFragment.proceed = true;
                        }
                    }
                });
                builder.setNeutralButton(webView.getContext().getResources().getString(com.ali.user.mobile.security.biz.R.string.aliuser_cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        sslErrorHandler.cancel();
                        if (authFragment != null) {
                            authFragment.proceed = false;
                        }
                    }
                });
                try {
                    AlertDialog create = builder.create();
                    create.setTitle(webView.getContext().getResources().getString(com.ali.user.mobile.security.biz.R.string.aliuser_ssl_error_title));
                    create.setMessage(string);
                    create.show();
                    if (authFragment != null) {
                        authFragment.firstAlert = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (authFragment.proceed) {
                sslErrorHandler.proceed();
            } else {
                super.onReceivedSslError(webView, sslErrorHandler, sslError);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean overrideUrlLoading(WebView webView, String str) {
        return handleQrUrl(str, this.mAttachedActivity, this.mAttachedActivity, webView);
    }

    private static void doQrTokenLogin(Context context, Activity activity, WebView webView, LoginParam loginParam) {
        String string = context.getResources().getString(com.ali.user.mobile.security.biz.R.string.aliuser_qrcode_login_fail);
        try {
            RpcResponse loginByQrToken = UserLoginServiceImpl.getInstance().loginByQrToken(loginParam);
            if (loginByQrToken == null || loginByQrToken.returnValue == null || loginByQrToken.code != 3000) {
                if (loginByQrToken != null && !TextUtils.isEmpty(loginByQrToken.message)) {
                    string = loginByQrToken.message;
                }
                handleQrLoginFail(context, webView, string);
                return;
            }
            UserTrackAdapter.sendUT("Page_Extend", "Qrcode_Login_SUCCESS");
            LoginResultHelper.saveLoginData((LoginReturnData) loginByQrToken.returnValue, SessionManager.getInstance(DataProviderFactory.getApplicationContext()));
        } catch (Exception e) {
            UrlTokenLogin.resetLoginFlag();
            e.printStackTrace();
            handleQrLoginFail(context, webView, string);
        }
    }

    private static void handleQrLoginFail(Context context, WebView webView, String str) {
        if (!TextUtils.isEmpty(str)) {
            Toast.makeText(context, str, 0).show();
        }
        webView.loadUrl(DataProviderFactory.getDataProvider().getQrCodeUrl());
    }

    public static boolean handleQrUrl(String str, Context context, Activity activity, WebView webView) {
        Bundle serialBundle = BundleUtil.serialBundle(Uri.parse(str).getQuery());
        if (serialBundle == null) {
            serialBundle = new Bundle();
        }
        if (str.startsWith(WVUCWebViewClient.SCHEME_SMS)) {
            try {
                context.startActivity(new Intent("android.intent.action.SENDTO", Uri.parse(str)));
            } catch (Exception unused) {
                TLogAdapter.e("PlaceHolderActivity", "sms exception" + str);
            }
            return true;
        } else if (!StringUtil.checkWebviewBridge(str)) {
            return false;
        } else {
            String string = serialBundle.getString("action");
            String string2 = serialBundle.getString("token");
            int i = serialBundle.getInt(WebConstant.SITE);
            if (!"QRLogin".equals(string)) {
                return false;
            }
            LoginParam loginParam = new LoginParam();
            loginParam.token = string2;
            loginParam.loginSite = i;
            doQrTokenLogin(context, activity, webView, loginParam);
            return true;
        }
    }

    public void onDestroy() {
        if (this.isWebviewAlive) {
            try {
                if (this.mAPRelativeLayout != null) {
                    this.mAPRelativeLayout.removeView(this.mWebView);
                }
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
}
