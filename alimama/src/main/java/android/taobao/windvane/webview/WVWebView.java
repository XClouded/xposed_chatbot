package android.taobao.windvane.webview;

import android.annotation.TargetApi;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVDomainConfig;
import android.taobao.windvane.config.WVServerConfig;
import android.taobao.windvane.filter.WVSecurityFilter;
import android.taobao.windvane.jsbridge.WVAppEvent;
import android.taobao.windvane.jsbridge.WVBridgeEngine;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVJsBridge;
import android.taobao.windvane.jsbridge.WVPluginEntryManager;
import android.taobao.windvane.jspatch.WVJsPatchListener;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.runtimepermission.PermissionProposer;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.urlintercept.WVURLInterceptService;
import android.taobao.windvane.util.EnvUtil;
import android.taobao.windvane.util.ImageTool;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.taobao.windvane.util.WVUrlUtil;
import android.taobao.windvane.view.PopupWindowController;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import com.taobao.weex.el.parse.Operators;

import java.util.HashMap;
import java.util.Map;

public class WVWebView extends WebView implements Handler.Callback, IWVWebView {
    private static final String TAG = "WVWebView";
    private static boolean evaluateJavascriptSupported = (Build.VERSION.SDK_INT >= 19);
    private final String WVURL_SUFFIX = "?wvFackUrlState=";
    public String bizCode = "";
    protected Context context;
    private String currentUrl = null;
    private String dataOnActive = null;
    float dx;
    float dy;
    protected WVPluginEntryManager entryManager;
    protected boolean isAlive;
    boolean isUser;
    private WVJsPatchListener jsPatchListener = null;
    /* access modifiers changed from: private */
    public boolean longPressSaveImage = true;
    SparseArray<MotionEvent> mEventSparseArray;
    protected Handler mHandler = null;
    /* access modifiers changed from: private */
    public String mImageUrl;
    private View.OnLongClickListener mLongClickListener = null;
    /* access modifiers changed from: private */
    public View.OnClickListener mPopupClickListener;
    /* access modifiers changed from: private */
    public PopupWindowController mPopupController = null;
    /* access modifiers changed from: private */
    public String[] mPopupMenuTags;
    private int mWvNativeCallbackId = 1000;
    private long onErrorTime = 0;
    protected boolean supportDownload = true;
    protected WVWebChromeClient webChromeClient;
    protected WVWebViewClient webViewClient;
    private WVSecurityFilter wvSecurityFilter = null;
    private boolean wvSupportFileSchema = EnvUtil.isDebug();
    private boolean wvSupportNativeJs = false;
    private WVUIModel wvUIModel = null;

    public View getView() {
        return this;
    }

    public WVWebView(Context context2, AttributeSet attributeSet, int i) {
        super(context2, attributeSet, i);
        String[] strArr = new String[1];
        strArr[0] = EnvUtil.isCN() ? "保存到相册" : "Save picture to album";
        this.mPopupMenuTags = strArr;
        this.mPopupClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                if (WVWebView.this.mPopupMenuTags != null && WVWebView.this.mPopupMenuTags.length > 0 && WVWebView.this.mPopupMenuTags[0].equals(view.getTag())) {
                    try {
                        PermissionProposer.buildPermissionTask(WVWebView.this.context, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}).setTaskOnPermissionGranted(new Runnable() {
                            public void run() {
                                ImageTool.saveImageToDCIM(WVWebView.this.context.getApplicationContext(), WVWebView.this.mImageUrl, WVWebView.this.mHandler);
                            }
                        }).setTaskOnPermissionDenied(new Runnable() {
                            public void run() {
                                WVWebView.this.mHandler.sendEmptyMessage(405);
                            }
                        }).execute();
                    } catch (Exception unused) {
                    }
                }
                if (WVWebView.this.mPopupController != null) {
                    WVWebView.this.mPopupController.hide();
                }
            }
        };
        this.isUser = true;
        this.mEventSparseArray = new SparseArray<>();
        this.context = context2;
        init();
    }

    public WVWebView(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet);
        String[] strArr = new String[1];
        strArr[0] = EnvUtil.isCN() ? "保存到相册" : "Save picture to album";
        this.mPopupMenuTags = strArr;
        this.mPopupClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                if (WVWebView.this.mPopupMenuTags != null && WVWebView.this.mPopupMenuTags.length > 0 && WVWebView.this.mPopupMenuTags[0].equals(view.getTag())) {
                    try {
                        PermissionProposer.buildPermissionTask(WVWebView.this.context, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}).setTaskOnPermissionGranted(new Runnable() {
                            public void run() {
                                ImageTool.saveImageToDCIM(WVWebView.this.context.getApplicationContext(), WVWebView.this.mImageUrl, WVWebView.this.mHandler);
                            }
                        }).setTaskOnPermissionDenied(new Runnable() {
                            public void run() {
                                WVWebView.this.mHandler.sendEmptyMessage(405);
                            }
                        }).execute();
                    } catch (Exception unused) {
                    }
                }
                if (WVWebView.this.mPopupController != null) {
                    WVWebView.this.mPopupController.hide();
                }
            }
        };
        this.isUser = true;
        this.mEventSparseArray = new SparseArray<>();
        this.context = context2;
        init();
    }

    public WVWebView(Context context2) {
        super(context2);
        String[] strArr = new String[1];
        strArr[0] = EnvUtil.isCN() ? "保存到相册" : "Save picture to album";
        this.mPopupMenuTags = strArr;
        this.mPopupClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                if (WVWebView.this.mPopupMenuTags != null && WVWebView.this.mPopupMenuTags.length > 0 && WVWebView.this.mPopupMenuTags[0].equals(view.getTag())) {
                    try {
                        PermissionProposer.buildPermissionTask(WVWebView.this.context, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}).setTaskOnPermissionGranted(new Runnable() {
                            public void run() {
                                ImageTool.saveImageToDCIM(WVWebView.this.context.getApplicationContext(), WVWebView.this.mImageUrl, WVWebView.this.mHandler);
                            }
                        }).setTaskOnPermissionDenied(new Runnable() {
                            public void run() {
                                WVWebView.this.mHandler.sendEmptyMessage(405);
                            }
                        }).execute();
                    } catch (Exception unused) {
                    }
                }
                if (WVWebView.this.mPopupController != null) {
                    WVWebView.this.mPopupController.hide();
                }
            }
        };
        this.isUser = true;
        this.mEventSparseArray = new SparseArray<>();
        this.context = context2;
        init();
    }

    public int getContentHeight() {
        return (int) (((float) super.getContentHeight()) * super.getScale());
    }

    public void loadData(String str, String str2, String str3) {
        if (this.isAlive) {
            super.loadData(str, str2, str3);
        }
    }

    public void reload() {
        super.reload();
    }

    public void loadDataWithBaseURL(String str, String str2, String str3, String str4, String str5) {
        if (this.isAlive) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(TAG, "loadDataWithBaseURL: baseUrl=" + str);
            }
            super.loadDataWithBaseURL(str, str2, str3, str4, str5);
        }
    }

    public void loadUrl(String str) {
        if (!WVUrlUtil.isCommonUrl(str) || !WVServerConfig.isBlackUrl(str)) {
            WVEventService.getInstance().onEvent(3010);
            if (this.isAlive && str != null) {
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(TAG, "loadUrl: url=" + str);
                }
                WVSchemeIntercepterInterface wVSchemeIntercepter = WVSchemeInterceptService.getWVSchemeIntercepter();
                if (wVSchemeIntercepter != null) {
                    str = wVSchemeIntercepter.dealUrlScheme(str);
                }
                try {
                    if (WVURLInterceptService.getWVABTestHandler() != null && WVUrlUtil.shouldTryABTest(str)) {
                        String aBTestUrl = WVURLInterceptService.getWVABTestHandler().toABTestUrl(str);
                        if (!TextUtils.isEmpty(aBTestUrl) && !aBTestUrl.equals(str)) {
                            TaoLog.i(TAG, str + " abTestUrl to : " + aBTestUrl);
                            str = aBTestUrl;
                        }
                    }
                    super.loadUrl(str);
                } catch (Exception e) {
                    TaoLog.e(TAG, e.getMessage());
                }
            }
        } else {
            String forbiddenDomainRedirectURL = WVDomainConfig.getInstance().getForbiddenDomainRedirectURL();
            if (TextUtils.isEmpty(forbiddenDomainRedirectURL)) {
                HashMap hashMap = new HashMap(2);
                hashMap.put("cause", "GET_ACCESS_FORBIDDEN");
                hashMap.put("url", str);
                onMessage(402, hashMap);
                return;
            }
            try {
                super.loadUrl(forbiddenDomainRedirectURL);
            } catch (Exception e2) {
                TaoLog.e(TAG, e2.getMessage());
            }
        }
    }

    public void refresh() {
        reload();
    }

    public void superLoadUrl(String str) {
        if (this.isAlive) {
            super.loadUrl(str);
        }
    }

    public void loadUrl(String str, Map<String, String> map) {
        if (this.isAlive && str != null) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(TAG, "loadUrl with headers: url=" + str);
            }
            super.loadUrl(str, map);
        }
    }

    public void postUrl(String str, byte[] bArr) {
        if (this.isAlive && str != null) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(TAG, "postUrl: url=" + str);
            }
            super.postUrl(str, bArr);
        }
    }

    private void init() {
        if (EnvUtil.isDebug()) {
            WVEventService.getInstance().onEvent(3008);
        }
        this.mHandler = new Handler(Looper.getMainLooper(), this);
        this.webViewClient = new WVWebViewClient(this.context);
        super.setWebViewClient(this.webViewClient);
        this.webChromeClient = new WVWebChromeClient(this.context);
        super.setWebChromeClient(this.webChromeClient);
        this.webChromeClient.mWebView = this;
        setVerticalScrollBarEnabled(false);
        requestFocus();
        CookieManager.getInstance().setAcceptCookie(true);
        if (Build.VERSION.SDK_INT >= 21) {
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true);
        }
        WVRenderPolicy.disableAccessibility(this.context);
        WebSettings settings = getSettings();
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
        String appTag = GlobalConfig.getInstance().getAppTag();
        String appVersion = GlobalConfig.getInstance().getAppVersion();
        String userAgentString = settings.getUserAgentString();
        if (userAgentString != null) {
            if (!TextUtils.isEmpty(appTag) && !TextUtils.isEmpty(appVersion)) {
                userAgentString = userAgentString + " AliApp(" + appTag + "/" + appVersion + Operators.BRACKET_END_STR;
            }
            if (!userAgentString.contains("TTID/") && !TextUtils.isEmpty(GlobalConfig.getInstance().getTtid())) {
                userAgentString = userAgentString + " TTID/" + GlobalConfig.getInstance().getTtid();
            }
        }
        settings.setUserAgentString(userAgentString + GlobalConfig.DEFAULT_UA);
        settings.setCacheMode(-1);
        if (Build.VERSION.SDK_INT >= 5) {
            settings.setDatabaseEnabled(false);
            String str = "/data/data/" + this.context.getPackageName() + "/databases";
            settings.setDatabasePath(str);
            settings.setGeolocationEnabled(true);
            settings.setGeolocationDatabasePath(str);
        }
        if (Build.VERSION.SDK_INT >= 7) {
            settings.setDomStorageEnabled(true);
            settings.setAppCacheEnabled(true);
            if (!(this.context == null || this.context.getCacheDir() == null)) {
                settings.setAppCachePath(this.context.getCacheDir().getAbsolutePath());
            }
        }
        if (Build.VERSION.SDK_INT < 18) {
            settings.setSavePassword(false);
        }
        if (Build.VERSION.SDK_INT >= 14) {
            settings.setTextZoom(100);
        } else {
            settings.setTextSize(WebSettings.TextSize.NORMAL);
        }
        if (TaoLog.getLogStatus() && Build.VERSION.SDK_INT >= 19) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        WVJsBridge.getInstance().init();
        this.entryManager = new WVPluginEntryManager(this.context, this);
        WVAppEvent wVAppEvent = new WVAppEvent();
        wVAppEvent.initialize(this.context, this);
        addJsObject("AppEvent", wVAppEvent);
        if (Build.VERSION.SDK_INT > 10 && Build.VERSION.SDK_INT < 17) {
            try {
                removeJavascriptInterface("searchBoxJavaBridge_");
                removeJavascriptInterface("accessibility");
                removeJavascriptInterface("accessibilityTraversal");
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        this.wvSecurityFilter = new WVSecurityFilter();
        WVEventService.getInstance().addEventListener(this.wvSecurityFilter, WVEventService.WV_FORWARD_EVENT);
        this.jsPatchListener = new WVJsPatchListener(this);
        WVEventService.getInstance().addEventListener(this.jsPatchListener, WVEventService.WV_BACKWARD_EVENT);
        if (Build.VERSION.SDK_INT > 15) {
            try {
                ClipboardManager clipboardManager = (ClipboardManager) this.context.getSystemService("clipboard");
                if (clipboardManager != null) {
                    ClipData primaryClip = clipboardManager.getPrimaryClip();
                    if (primaryClip == null) {
                        clipboardManager.setPrimaryClip(ClipData.newPlainText("初始化", ""));
                    } else if ("intent:#Intent;S.K_1171477665=;end".equals(primaryClip.getItemAt(0).coerceToText(this.context).toString())) {
                        clipboardManager.setPrimaryClip(ClipData.newPlainText("初始化", ""));
                    }
                }
            } catch (Exception unused) {
            }
        }
        this.wvUIModel = new WVUIModel(this.context, this);
        this.mLongClickListener = new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                WebView.HitTestResult hitTestResult;
                try {
                    hitTestResult = WVWebView.this.getHitTestResult();
                } catch (Exception unused) {
                    hitTestResult = null;
                }
                if (hitTestResult == null || !WVWebView.this.longPressSaveImage) {
                    return false;
                }
                if (TaoLog.getLogStatus()) {
                    TaoLog.d(WVWebView.TAG, "Long click on WebView, " + hitTestResult.getExtra());
                }
                if (hitTestResult.getType() != 8 && hitTestResult.getType() != 5) {
                    return false;
                }
                String unused2 = WVWebView.this.mImageUrl = hitTestResult.getExtra();
                PopupWindowController unused3 = WVWebView.this.mPopupController = new PopupWindowController(WVWebView.this.context, WVWebView.this, WVWebView.this.mPopupMenuTags, WVWebView.this.mPopupClickListener);
                WVWebView.this.mPopupController.show();
                return true;
            }
        };
        setOnLongClickListener(this.mLongClickListener);
        setDownloadListener(new WVDownLoadListener());
        WVTweakWebCoreHandler.tryTweakWebCoreHandler();
        this.isAlive = true;
        if (WVMonitorService.getPackageMonitorInterface() != null) {
            WVMonitorService.getPerformanceMonitor().didWebViewInitAtTime(System.currentTimeMillis());
        }
        if (Build.VERSION.SDK_INT >= 11 && WVRenderPolicy.shouldDisableHardwareRenderInLayer()) {
            try {
                setLayerType(1, (Paint) null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        addJavascriptInterface(new WVBridgeEngine(this), "__windvane__");
    }

    public void setWebViewClient(WebViewClient webViewClient2) {
        if (webViewClient2 instanceof WVWebViewClient) {
            this.webViewClient = (WVWebViewClient) webViewClient2;
            super.setWebViewClient(webViewClient2);
        } else if (this.webViewClient != null) {
            this.webViewClient.extraWebViewClient = webViewClient2;
        }
    }

    public void setWebChromeClient(WebChromeClient webChromeClient2) {
        if (webChromeClient2 instanceof WVWebChromeClient) {
            this.webChromeClient = (WVWebChromeClient) webChromeClient2;
            this.webChromeClient.mWebView = this;
            super.setWebChromeClient(webChromeClient2);
        } else if (this.webChromeClient != null) {
            this.webChromeClient.extraWebChromeClient = webChromeClient2;
        }
    }

    @TargetApi(19)
    public boolean _post(Runnable runnable) {
        if (isAttachedToWindow() || Build.VERSION.SDK_INT >= 24) {
            return post(runnable);
        }
        TaoLog.d(TAG, " webview has not attach to window");
        taskQueue.add(runnable);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        for (Runnable run : taskQueue) {
            run.run();
        }
        taskQueue.clear();
        super.onAttachedToWindow();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (taskQueue.size() != 0) {
            taskQueue.clear();
        }
    }

    public void destroy() {
        if (this.isAlive) {
            this.isAlive = false;
            InputMethodManager inputMethodManager = (InputMethodManager) this.context.getSystemService("input_method");
            if (inputMethodManager.isActive()) {
                inputMethodManager.hideSoftInputFromWindow(getApplicationWindowToken(), 0);
            }
            super.setWebViewClient((WebViewClient) null);
            super.setWebChromeClient((WebChromeClient) null);
            this.webViewClient = null;
            this.webChromeClient = null;
            WVJsBridge.getInstance().tryToRunTailBridges();
            this.entryManager.onDestroy();
            if (this.mHandler != null) {
                this.mHandler.removeCallbacksAndMessages((Object) null);
                this.mHandler = null;
            }
            WVEventService.getInstance().onEvent(3003);
            WVEventService.getInstance().removeEventListener(this.wvSecurityFilter);
            WVEventService.getInstance().removeEventListener(this.jsPatchListener);
            removeAllViews();
            this.mPopupController = null;
            this.mPopupClickListener = null;
            this.mLongClickListener = null;
            setOnLongClickListener((View.OnLongClickListener) null);
            if (JsbridgeHis != null) {
                JsbridgeHis.clear();
            }
            try {
                super.destroy();
            } catch (Exception unused) {
            }
        }
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    public WVUIModel getWvUIModel() {
        return this.wvUIModel;
    }

    public void onMessage(int i, Object obj) {
        if (this.mHandler != null) {
            Message obtain = Message.obtain();
            obtain.what = i;
            obtain.obj = obj;
            this.mHandler.sendMessage(obtain);
        }
    }

    public boolean handleMessage(Message message) {
        boolean z = false;
        switch (message.what) {
            case 400:
                if (this.wvUIModel != null) {
                    z = true;
                }
                if (this.wvUIModel.isShowLoading() && z) {
                    this.wvUIModel.showLoadingView();
                    this.wvUIModel.switchNaviBar(1);
                }
                return true;
            case 401:
                if (this.wvUIModel != null) {
                    z = true;
                }
                if (this.wvUIModel.isShowLoading() && z) {
                    this.wvUIModel.hideLoadingView();
                    this.wvUIModel.resetNaviBar();
                }
                if (this.onErrorTime != 0 && System.currentTimeMillis() - this.onErrorTime > TBToast.Duration.MEDIUM) {
                    this.wvUIModel.hideErrorPage();
                }
                return true;
            case 402:
                this.wvUIModel.loadErrorPage();
                this.onErrorTime = System.currentTimeMillis();
                if (this.wvUIModel != null) {
                    z = true;
                }
                if (this.wvUIModel.isShowLoading() && z) {
                    this.wvUIModel.hideLoadingView();
                }
                return true;
            case 403:
                if (this.wvUIModel != null) {
                    z = true;
                }
                if (this.wvUIModel.isShowLoading() && z) {
                    this.wvUIModel.hideLoadingView();
                }
                return true;
            case 404:
                Toast.makeText(this.context, EnvUtil.isCN() ? "图片保存到相册成功" : "Success to save picture", 1).show();
                return true;
            case 405:
                Toast.makeText(this.context, EnvUtil.isCN() ? "图片保存到相册失败" : "Failed to save picture", 1).show();
                return true;
            default:
                return false;
        }
    }

    /* access modifiers changed from: protected */
    public void onScrollChanged(int i, int i2, int i3, int i4) {
        if (this.entryManager != null) {
            this.entryManager.onScrollChanged(i, i2, i3, i4);
        }
        try {
            super.onScrollChanged(i, i2, i3, i4);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(11)
    public void onPause() {
        if (this.entryManager != null) {
            this.entryManager.onPause();
        }
        if (Build.VERSION.SDK_INT >= 11) {
            super.onPause();
        }
        WVEventService.getInstance().onEvent(3001);
    }

    public void pauseTimers() {
        super.pauseTimers();
        if (TaoLog.getLogStatus()) {
            TaoLog.e(TAG, "You  must be careful  to Call pauseTimers ,It's Global");
        }
    }

    public void resumeTimers() {
        super.resumeTimers();
        if (TaoLog.getLogStatus()) {
            TaoLog.e(TAG, "You  must be careful  to Call resumeTimers ,It's Global");
        }
    }

    @TargetApi(11)
    public void onResume() {
        if (this.entryManager != null) {
            this.entryManager.onResume();
        }
        if (Build.VERSION.SDK_INT >= 11) {
            super.onResume();
        }
        WVEventService.getInstance().onEvent(3002, this, getUrl(), new Object[0]);
    }

    public Handler getWVHandler() {
        return this.mHandler;
    }

    class WVDownLoadListener implements DownloadListener {
        WVDownLoadListener() {
        }

        public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(WVWebView.TAG, "Download start, url: " + str + " contentDisposition: " + str3 + " mimetype: " + str4 + " contentLength: " + j);
            }
            if (!WVWebView.this.supportDownload) {
                TaoLog.w(WVWebView.TAG, "DownloadListener is not support for webview.");
                return;
            }
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
            intent.setFlags(268435456);
            try {
                WVWebView.this.context.startActivity(intent);
            } catch (ActivityNotFoundException unused) {
                Toast.makeText(WVWebView.this.context, "对不起，您的设备找不到相应的程序", 1).show();
                TaoLog.e(WVWebView.TAG, "DownloadListener not found activity to open this url.");
            }
        }
    }

    public void setSupportDownload(boolean z) {
        this.supportDownload = z;
    }

    public void setSupportFileSchema(boolean z) {
        this.wvSupportFileSchema = z;
    }

    public boolean isSupportFileSchema() {
        return this.wvSupportFileSchema;
    }

    public void addJavascriptInterface(Object obj, String str) {
        if (!"accessibility".equals(str) && !"accessibilityTraversal".equals(str)) {
            if (this.wvSupportNativeJs || Build.VERSION.SDK_INT >= 17) {
                super.addJavascriptInterface(obj, str);
            } else {
                TaoLog.e(TAG, "addJavascriptInterface is disabled before API level 17 for security reason.");
            }
        }
    }

    public void supportJavascriptInterface(boolean z) {
        this.wvSupportNativeJs = z;
    }

    public void addJsObject(String str, Object obj) {
        if (this.entryManager != null) {
            this.entryManager.addEntry(str, obj);
        }
    }

    public Object getJsObject(String str) {
        if (this.entryManager == null) {
            return null;
        }
        return this.entryManager.getEntry(str);
    }

    public Context _getContext() {
        return getContext();
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (this.entryManager != null) {
            this.entryManager.onActivityResult(i, i2, intent);
        }
    }

    public void script2NativeCallback(String str, ValueCallback<String> valueCallback) {
        int i = this.mWvNativeCallbackId + 1;
        this.mWvNativeCallbackId = i;
        WVNativeCallbackUtil.putNativeCallbak(String.valueOf(i), valueCallback);
        loadUrl("javascript:console.log('wvNativeCallback/" + i + "/'+function(){var s = " + str + "; return (typeof s === 'object' ? JSON.stringify(s) : typeof s === 'string' ? '\"' + s + '\"' : s);}())");
    }

    public void evaluateJavascript(String str) {
        evaluateJavascript(str, (ValueCallback<String>) null);
    }

    public void evaluateJavascript(String str, ValueCallback<String> valueCallback) {
        if (str != null && str.length() > 10 && "javascript:".equals(str.substring(0, 11).toLowerCase())) {
            str = str.substring(11);
        }
        if (evaluateJavascriptSupported) {
            try {
                super.evaluateJavascript(str, valueCallback);
            } catch (NoSuchMethodError unused) {
                evaluateJavascriptSupported = false;
                evaluateJavascript(str, valueCallback);
            } catch (IllegalStateException unused2) {
                evaluateJavascriptSupported = false;
                evaluateJavascript(str, valueCallback);
            }
        } else if (valueCallback == null) {
            loadUrl("javascript:" + str);
        } else {
            script2NativeCallback(str, valueCallback);
        }
    }

    public void openLongPressSaveImage() {
        this.longPressSaveImage = true;
    }

    public void closeLongPressSaveImage() {
        this.longPressSaveImage = false;
    }

    public String getUrl() {
        return getCurrentUrl();
    }

    public String getCurrentUrl() {
        String url = super.getUrl();
        if (url == null) {
            TaoLog.v(TAG, "getUrl by currentUrl: " + this.currentUrl);
            return this.currentUrl;
        }
        TaoLog.v(TAG, "getUrl by webview: " + url);
        return url;
    }

    public void setCurrentUrl(String str, String str2) {
        this.currentUrl = str;
        TaoLog.v(TAG, "setCurrentUrl: " + str + " state : " + str2);
    }

    public boolean canGoBack() {
        if (WVEventService.getInstance().onEvent(3004).isSuccess) {
            return false;
        }
        return super.canGoBack();
    }

    public boolean back() {
        if (!canGoBack()) {
            return false;
        }
        goBack();
        return true;
    }

    public void setDataOnActive(String str) {
        this.dataOnActive = str;
    }

    public String getDataOnActive() {
        return this.dataOnActive;
    }

    public void fireEvent(String str) {
        fireEvent(str, "{}");
    }

    public void fireEvent(String str, String str2) {
        getWVCallBackContext().fireEvent(str, str2);
    }

    @Deprecated
    public WVCallBackContext getWVCallBackContext() {
        return new WVCallBackContext(this);
    }

    public void showLoadingView() {
        if (this.wvUIModel != null) {
            this.wvUIModel.showLoadingView();
        }
    }

    public void hideLoadingView() {
        if (this.wvUIModel != null) {
            this.wvUIModel.hideLoadingView();
        }
    }

    public void clearCache() {
        super.clearCache(true);
    }

    public String getUserAgentString() {
        return getSettings().getUserAgentString();
    }

    public void setUserAgentString(String str) {
        getSettings().setUserAgentString(str);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        int pointerId = motionEvent.getPointerId(motionEvent.getActionIndex());
        if (action == 0) {
            this.dx = motionEvent.getX();
            this.dy = motionEvent.getY();
            if (!this.isUser) {
                this.mEventSparseArray.put(pointerId, MotionEvent.obtain(motionEvent));
                return true;
            }
        } else if (action == 2) {
            if (!this.isUser && Math.abs(motionEvent.getY() - this.dy) > 5.0f) {
                return true;
            }
        } else if (action == 1) {
            if (this.isUser || Math.abs(motionEvent.getY() - this.dy) <= 5.0f) {
                MotionEvent motionEvent2 = this.mEventSparseArray.get(pointerId);
                if (motionEvent2 != null) {
                    super.onTouchEvent(motionEvent2);
                    motionEvent2.recycle();
                    this.mEventSparseArray.remove(pointerId);
                }
            } else {
                this.isUser = true;
                return true;
            }
        }
        return super.onTouchEvent(motionEvent);
    }
}
