package android.taobao.windvane.extra.uc;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.MutableContextWrapper;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.taobao.windvane.WindVaneSDKForTB;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.UCParamData;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.config.WVCookieConfig;
import android.taobao.windvane.config.WVDomainConfig;
import android.taobao.windvane.config.WVServerConfig;
import android.taobao.windvane.config.WVUCPrecacheManager;
import android.taobao.windvane.extra.config.TBConfigManager;
import android.taobao.windvane.extra.core.WVCore;
import android.taobao.windvane.extra.ha.UCHAManager;
import android.taobao.windvane.extra.jsbridge.WVUCBase;
import android.taobao.windvane.extra.performance2.WVPerformance;
import android.taobao.windvane.filter.WVSecurityFilter;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVAppEvent;
import android.taobao.windvane.jsbridge.WVBridgeEngine;
import android.taobao.windvane.jsbridge.WVCallBackContext;
import android.taobao.windvane.jsbridge.WVJsBridge;
import android.taobao.windvane.jsbridge.WVPluginEntryManager;
import android.taobao.windvane.jsbridge.WVPluginManager;
import android.taobao.windvane.jspatch.WVJsPatchListener;
import android.taobao.windvane.monitor.AppMonitorUtil;
import android.taobao.windvane.monitor.UserTrackUtil;
import android.taobao.windvane.monitor.WVMonitorService;
import android.taobao.windvane.runtimepermission.PermissionProposer;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.thread.WVThreadPool;
import android.taobao.windvane.util.CommonUtils;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.EnvUtil;
import android.taobao.windvane.util.ImageTool;
import android.taobao.windvane.util.NetWork;
import android.taobao.windvane.util.TaoLog;
import android.taobao.windvane.util.WVNativeCallbackUtil;
import android.taobao.windvane.util.WVUrlUtil;
import android.taobao.windvane.view.PopupWindowController;
import android.taobao.windvane.webview.CoreEventCallback;
import android.taobao.windvane.webview.IWVWebView;
import android.taobao.windvane.webview.WVRenderPolicy;
import android.taobao.windvane.webview.WVSchemeInterceptService;
import android.taobao.windvane.webview.WVSchemeIntercepterInterface;
import android.taobao.windvane.webview.WVUIModel;
import android.taobao.windvane.webview.WindVaneError;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.ValueCallback;
import android.widget.Toast;

import androidx.annotation.Keep;

import com.alibaba.analytics.core.sync.UploadQueueMgr;
import com.taobao.orange.OrangeConfig;
import com.taobao.uc.UCSoSettings;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import com.taobao.weaver.prefetch.WMLPrefetch;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.ui.component.WXComponent;
import com.uc.webview.export.CDParamKeys;
import com.uc.webview.export.ServiceWorkerController;
import com.uc.webview.export.WebChromeClient;
import com.uc.webview.export.WebResourceRequest;
import com.uc.webview.export.WebResourceResponse;
import com.uc.webview.export.WebSettings;
import com.uc.webview.export.WebView;
import com.uc.webview.export.WebViewClient;
import com.uc.webview.export.extension.SettingKeys;
import com.uc.webview.export.extension.UCCore;
import com.uc.webview.export.extension.UCExtension;
import com.uc.webview.export.extension.UCSettings;
import com.uc.webview.export.utility.SetupTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Keep
public class WVUCWebView extends WebView implements Handler.Callback, IWVWebView {
    public static boolean INNER = false;
    private static final String STATIC_WEBVIEW_URL = "about:blank?static";
    private static final String TAG = "WVUCWebView";
    public static String UC_CORE_URL = UC_CORE_URL_32;
    public static String UC_CORE_URL_32 = UCSoSettings.getInstance().UC_CORE_URL_32;
    public static String UC_CORE_URL_64 = UCSoSettings.getInstance().UC_CORE_URL_64;
    private static String UC_CORE_URL_DEBUG_32 = UCSoSettings.getInstance().UC_CORE_URL_DEBUG_32;
    private static String UC_CORE_URL_DEBUG_64 = UCSoSettings.getInstance().UC_CORE_URL_DEBUG_64;
    private static String UC_CORE_URL_DEBUG_X86 = UCSoSettings.getInstance().UC_CORE_URL_DEBUG_X86;
    private static String UC_PLAYER_URL = UCSoSettings.getInstance().UC_PLAYER_URL;
    public static final String WINDVANE = "windvane";
    public static String bizId = "windvane";
    /* access modifiers changed from: private */
    public static CoreEventCallback coreEventCallback = null;
    private static boolean evaluateJavascriptSupported = false;
    private static int fromType = 70;
    /* access modifiers changed from: private */
    public static boolean isSWInit = false;
    public static boolean isStop = false;
    private static boolean mDegradeAliNetwork = false;
    private static boolean mUseAliNetwork = true;
    private static boolean mUseSystemWebView = false;
    private static boolean needDownLoad = false;
    private static boolean openUCDebug = false;
    private static Pattern pattern;
    /* access modifiers changed from: private */
    public static final AtomicBoolean sCoreInitialized = new AtomicBoolean(false);
    private static WVUCWebView sStaticUCWebView;
    /* access modifiers changed from: private */
    public static final AtomicBoolean shouldUCLibInit = new AtomicBoolean(false);
    private AliNetworkAdapter aliRequestAdapter = null;
    public String bizCode = "";
    protected Context context;
    private String currentUrl = null;
    private String dataOnActive = null;
    float dx;
    float dy;
    protected WVPluginEntryManager entryManager;
    private boolean flag4commit = false;
    StringBuilder injectJs;
    private boolean isLive = false;
    private boolean isRealDestroyed = false;
    boolean isUser;
    private WVJsPatchListener jsPatchListener = null;
    /* access modifiers changed from: private */
    public boolean longPressSaveImage = true;
    SparseArray<MotionEvent> mEventSparseArray;
    private Hashtable<String, Hashtable<String, String>> mH5MonitorCache;
    protected Handler mHandler = null;
    /* access modifiers changed from: private */
    public String mImageUrl;
    private boolean mIsCoreDestroy = false;
    private boolean mIsStaticWebView;
    private View.OnLongClickListener mLongClickListener;
    public long mPageStart;
    /* access modifiers changed from: private */
    public PopupWindowController mPopupController;
    /* access modifiers changed from: private */
    public String[] mPopupMenuTags;
    private int mWvNativeCallbackId = 1000;
    private long onErrorTime;
    private WVPerformance performanceDelegate;
    /* access modifiers changed from: private */
    public View.OnClickListener popupClickListener;
    protected boolean supportDownload = true;
    private String ucParam = "";
    protected WVUCWebChromeClient webChromeClient;
    protected WVUCWebViewClient webViewClient;
    private WVSecurityFilter wvSecurityFilter = null;
    private WVUIModel wvUIModel;

    public interface whiteScreenCallback {
        void isPageEmpty(String str);
    }

    private static boolean checkOldCoreVersion(String str) {
        return false;
    }

    public void onLowMemory() {
    }

    static {
        boolean z = true;
        if (Build.VERSION.SDK_INT < 19) {
            z = false;
        }
        evaluateJavascriptSupported = z;
        TaoLog.d(TAG, "static init uc core");
        if (!GlobalConfig.getInstance().isUcCoreOuterControl()) {
            initUCCore();
        }
    }

    public int getContentHeight() {
        return (int) (((float) super.getContentHeight()) * super.getScale());
    }

    @Deprecated
    @Keep
    public static void initUCCore() {
        initUCCore(GlobalConfig.context);
    }

    public void setPerformanceDelegate(WVPerformance wVPerformance) {
        this.performanceDelegate = wVPerformance;
    }

    public WVPerformance getPerformanceDelegate() {
        return this.performanceDelegate;
    }

    private static boolean isUseSystemWebView(Context context2) {
        boolean z = mUseSystemWebView;
        if (!mUseSystemWebView) {
            try {
                String config = OrangeConfig.getInstance().getConfig(TBConfigManager.WINDVANE_COMMMON_CONFIG, "useSysWebViewBizList", "");
                if (!TextUtils.isEmpty(config)) {
                    String name = context2.getClass().getName();
                    for (String equals : config.split(";")) {
                        if (name.equals(equals)) {
                            z = true;
                        }
                    }
                }
            } catch (Throwable unused) {
            }
        }
        return z;
    }

    public WVUCWebView(Context context2, AttributeSet attributeSet, int i) {
        super(context2, attributeSet, i, isUseSystemWebView(context2));
        String[] strArr = new String[1];
        strArr[0] = EnvUtil.isCN() ? "保存到相册" : "Save to album";
        this.mPopupMenuTags = strArr;
        this.mLongClickListener = null;
        this.popupClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                if (WVUCWebView.this.mPopupMenuTags != null && WVUCWebView.this.mPopupMenuTags.length > 0 && WVUCWebView.this.mPopupMenuTags[0].equals(view.getTag())) {
                    try {
                        PermissionProposer.buildPermissionTask(WVUCWebView.this.context, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}).setTaskOnPermissionGranted(new Runnable() {
                            public void run() {
                                if (WVUCWebView.this.context != null) {
                                    ImageTool.saveImageToDCIM(WVUCWebView.this.context.getApplicationContext(), WVUCWebView.this.mImageUrl, WVUCWebView.this.mHandler);
                                }
                            }
                        }).setTaskOnPermissionDenied(new Runnable() {
                            public void run() {
                                WVUCWebView.this.mHandler.sendEmptyMessage(405);
                            }
                        }).execute();
                    } catch (Exception unused) {
                    }
                }
                if (WVUCWebView.this.mPopupController != null) {
                    WVUCWebView.this.mPopupController.hide();
                }
            }
        };
        this.wvUIModel = null;
        this.onErrorTime = 0;
        this.mIsStaticWebView = false;
        this.isUser = true;
        this.mEventSparseArray = new SparseArray<>();
        this.mH5MonitorCache = null;
        this.mPageStart = 0;
        this.injectJs = new StringBuilder("javascript:");
        this.context = context2 instanceof MutableContextWrapper ? ((MutableContextWrapper) context2).getBaseContext() : context2;
        init();
    }

    public WVUCWebView(Context context2, AttributeSet attributeSet) {
        super(context2, attributeSet, isUseSystemWebView(context2));
        String[] strArr = new String[1];
        strArr[0] = EnvUtil.isCN() ? "保存到相册" : "Save to album";
        this.mPopupMenuTags = strArr;
        this.mLongClickListener = null;
        this.popupClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                if (WVUCWebView.this.mPopupMenuTags != null && WVUCWebView.this.mPopupMenuTags.length > 0 && WVUCWebView.this.mPopupMenuTags[0].equals(view.getTag())) {
                    try {
                        PermissionProposer.buildPermissionTask(WVUCWebView.this.context, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}).setTaskOnPermissionGranted(new Runnable() {
                            public void run() {
                                if (WVUCWebView.this.context != null) {
                                    ImageTool.saveImageToDCIM(WVUCWebView.this.context.getApplicationContext(), WVUCWebView.this.mImageUrl, WVUCWebView.this.mHandler);
                                }
                            }
                        }).setTaskOnPermissionDenied(new Runnable() {
                            public void run() {
                                WVUCWebView.this.mHandler.sendEmptyMessage(405);
                            }
                        }).execute();
                    } catch (Exception unused) {
                    }
                }
                if (WVUCWebView.this.mPopupController != null) {
                    WVUCWebView.this.mPopupController.hide();
                }
            }
        };
        this.wvUIModel = null;
        this.onErrorTime = 0;
        this.mIsStaticWebView = false;
        this.isUser = true;
        this.mEventSparseArray = new SparseArray<>();
        this.mH5MonitorCache = null;
        this.mPageStart = 0;
        this.injectJs = new StringBuilder("javascript:");
        this.context = context2 instanceof MutableContextWrapper ? ((MutableContextWrapper) context2).getBaseContext() : context2;
        init();
    }

    public WVUCWebView(Context context2) {
        super(context2, isUseSystemWebView(context2));
        String[] strArr = new String[1];
        strArr[0] = EnvUtil.isCN() ? "保存到相册" : "Save to album";
        this.mPopupMenuTags = strArr;
        this.mLongClickListener = null;
        this.popupClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                if (WVUCWebView.this.mPopupMenuTags != null && WVUCWebView.this.mPopupMenuTags.length > 0 && WVUCWebView.this.mPopupMenuTags[0].equals(view.getTag())) {
                    try {
                        PermissionProposer.buildPermissionTask(WVUCWebView.this.context, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}).setTaskOnPermissionGranted(new Runnable() {
                            public void run() {
                                if (WVUCWebView.this.context != null) {
                                    ImageTool.saveImageToDCIM(WVUCWebView.this.context.getApplicationContext(), WVUCWebView.this.mImageUrl, WVUCWebView.this.mHandler);
                                }
                            }
                        }).setTaskOnPermissionDenied(new Runnable() {
                            public void run() {
                                WVUCWebView.this.mHandler.sendEmptyMessage(405);
                            }
                        }).execute();
                    } catch (Exception unused) {
                    }
                }
                if (WVUCWebView.this.mPopupController != null) {
                    WVUCWebView.this.mPopupController.hide();
                }
            }
        };
        this.wvUIModel = null;
        this.onErrorTime = 0;
        this.mIsStaticWebView = false;
        this.isUser = true;
        this.mEventSparseArray = new SparseArray<>();
        this.mH5MonitorCache = null;
        this.mPageStart = 0;
        this.injectJs = new StringBuilder("javascript:");
        this.context = context2 instanceof MutableContextWrapper ? ((MutableContextWrapper) context2).getBaseContext() : context2;
        init();
    }

    public boolean isLive() {
        return this.isLive;
    }

    public static void setUcCoreUrl(String str) {
        if (!TextUtils.isEmpty(str)) {
            UC_CORE_URL = str;
        }
    }

    public static int getFromType() {
        fromType = 70;
        if (WVCore.getInstance().isUCSupport()) {
            fromType = getUseTaobaoNetwork() ? 6 : 5;
        } else if (!mUseSystemWebView) {
            fromType = 71;
        }
        return fromType;
    }

    @Deprecated
    public static boolean getUCSDKSupport() {
        return WVCore.getInstance().isUCSupport();
    }

    public static void setUseSystemWebView(boolean z) {
        mUseSystemWebView = z;
        fromType = 70;
    }

    public static void setDegradeAliNetwork(boolean z) {
        mDegradeAliNetwork = z;
    }

    public static boolean getDegradeAliNetwork() {
        return mDegradeAliNetwork;
    }

    public static boolean getUseTaobaoNetwork() {
        return WVCore.getInstance().isUCSupport() && mUseAliNetwork;
    }

    public static void setUseTaobaoNetwork(boolean z) {
        mUseAliNetwork = z;
    }

    @Deprecated
    public static void setCoreEventCallback(CoreEventCallback coreEventCallback2) {
        coreEventCallback = coreEventCallback2;
    }

    /* access modifiers changed from: private */
    public static void onUCMCoreSwitched(Context context2, long j) {
        TaoLog.i(TAG, "UCSDK init onUCMCoreSwitched: " + getCoreType());
        if (!WVCore.getInstance().isUCSupport() && getCoreType() == 3) {
            TaoLog.i(TAG, "CorePreparedCallback   isUCSDKSupport = true");
            WVCore.getInstance().setUCSupport(true);
            if (coreEventCallback != null) {
                WVCoreSettings.getInstance().setCoreEventCallback(coreEventCallback);
            }
            if (WVCoreSettings.getInstance().coreEventCallbacks != null) {
                for (CoreEventCallback onUCCorePrepared : WVCoreSettings.getInstance().coreEventCallbacks) {
                    onUCCorePrepared.onUCCorePrepared();
                }
            }
            new UCHAManager().initHAParam(GlobalConfig.getInstance().getUcHASettings());
            WVMonitorService.getWvMonitorInterface().commitCoreInitTime(System.currentTimeMillis() - j, String.valueOf(WVCommonConfig.commonConfig.initUCCorePolicy));
            WVEventService.getInstance().onEvent(3016);
            if (!GlobalConfig.getInstance().needSpeed() && !isSWInit) {
                checkSW();
            }
            if (mUseAliNetwork) {
                UCCore.setThirdNetwork(new AliNetworkAdapter(context2.getApplicationContext(), bizId), new AliNetworkDecider());
            }
            try {
                UCCore.updateUCPlayer(GlobalConfig.context, UC_PLAYER_URL, new DownloadEnv(GlobalConfig.context));
            } catch (Exception e) {
                TaoLog.e("UCCore", "UCCore update UCPlayer failed:" + e.getMessage());
            }
        }
    }

    @Deprecated
    public static boolean initUCLIb(Context context2) {
        if (!shouldUCLibInit.get()) {
            RuntimeException runtimeException = new RuntimeException("init uclib outer");
            runtimeException.fillInStackTrace();
            TaoLog.e(TAG, runtimeException.getStackTrace()[0].toString() + "\n" + runtimeException.getStackTrace()[1].toString() + "\n" + runtimeException.getStackTrace()[2].toString());
            return false;
        } else if (context2 == null) {
            return false;
        } else {
            return initUCLIb(GlobalConfig.getInstance().getUcsdkappkeySec(), context2.getApplicationContext());
        }
    }

    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:33:0x00b6 */
    /* JADX WARNING: Removed duplicated region for block: B:36:0x00c8 A[Catch:{ Throwable -> 0x00d9 }] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x00d2 A[Catch:{ Throwable -> 0x00d9 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void initUCCore(android.content.Context r4) {
        /*
            android.taobao.windvane.config.GlobalConfig r4 = android.taobao.windvane.config.GlobalConfig.getInstance()
            java.lang.String[] r4 = r4.getUcsdkappkeySec()
            if (r4 != 0) goto L_0x0015
            android.util.AndroidRuntimeException r4 = new android.util.AndroidRuntimeException
            java.lang.String r0 = "WVUCWebView: can not init uc core for uc key is null"
            r4.<init>(r0)
            r4.printStackTrace()
            return
        L_0x0015:
            android.app.Application r4 = android.taobao.windvane.config.GlobalConfig.context
            if (r4 != 0) goto L_0x0024
            android.util.AndroidRuntimeException r4 = new android.util.AndroidRuntimeException
            java.lang.String r0 = "WVUCWebView: can not init uc core for context is nulll"
            r4.<init>(r0)
            r4.printStackTrace()
            return
        L_0x0024:
            java.util.concurrent.atomic.AtomicBoolean r4 = sCoreInitialized
            r0 = 0
            r1 = 1
            boolean r4 = r4.compareAndSet(r0, r1)
            if (r4 == 0) goto L_0x00df
            boolean r4 = android.taobao.windvane.extra.uc.WVUCUtils.is64Bit()     // Catch:{ Exception -> 0x00b6 }
            if (r4 != 0) goto L_0x004d
            java.lang.String r0 = "x86"
            boolean r0 = android.taobao.windvane.extra.uc.WVUCUtils.isArchContains(r0)     // Catch:{ Exception -> 0x00b6 }
            if (r0 == 0) goto L_0x004d
            java.lang.String r4 = UC_CORE_URL_DEBUG_X86     // Catch:{ Exception -> 0x00b6 }
            UC_CORE_URL = r4     // Catch:{ Exception -> 0x00b6 }
            java.lang.String r4 = "WVUCWebView"
            java.lang.String r0 = "UCCore use x86 core"
            android.taobao.windvane.util.TaoLog.i(r4, r0)     // Catch:{ Exception -> 0x00b6 }
            java.util.concurrent.atomic.AtomicBoolean r4 = sCoreInitialized     // Catch:{ Exception -> 0x00b6 }
            r4.set(r1)     // Catch:{ Exception -> 0x00b6 }
            return
        L_0x004d:
            boolean r0 = android.taobao.windvane.util.EnvUtil.isAppDebug()     // Catch:{ Exception -> 0x00b6 }
            if (r0 == 0) goto L_0x0092
            java.lang.String r0 = "true"
            com.taobao.uc.UCSoSettings r2 = com.taobao.uc.UCSoSettings.getInstance()     // Catch:{ Exception -> 0x00b6 }
            java.lang.String r2 = r2.UC_CORE_THICK     // Catch:{ Exception -> 0x00b6 }
            boolean r0 = android.text.TextUtils.equals(r0, r2)     // Catch:{ Exception -> 0x00b6 }
            if (r0 == 0) goto L_0x006d
            android.taobao.windvane.config.GlobalConfig r0 = android.taobao.windvane.config.GlobalConfig.getInstance()     // Catch:{ Exception -> 0x00b6 }
            boolean r0 = r0.openUCDebug()     // Catch:{ Exception -> 0x00b6 }
            if (r0 == 0) goto L_0x006d
            openUCDebug = r1     // Catch:{ Exception -> 0x00b6 }
        L_0x006d:
            if (r4 == 0) goto L_0x0072
            java.lang.String r0 = UC_CORE_URL_DEBUG_64     // Catch:{ Exception -> 0x00b6 }
            goto L_0x0074
        L_0x0072:
            java.lang.String r0 = UC_CORE_URL_DEBUG_32     // Catch:{ Exception -> 0x00b6 }
        L_0x0074:
            UC_CORE_URL = r0     // Catch:{ Exception -> 0x00b6 }
            java.lang.String r0 = "WVUCWebView"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b6 }
            r2.<init>()     // Catch:{ Exception -> 0x00b6 }
            java.lang.String r3 = "use 3.0 debug core, use 64bit = ["
            r2.append(r3)     // Catch:{ Exception -> 0x00b6 }
            r2.append(r4)     // Catch:{ Exception -> 0x00b6 }
            java.lang.String r4 = "]"
            r2.append(r4)     // Catch:{ Exception -> 0x00b6 }
            java.lang.String r4 = r2.toString()     // Catch:{ Exception -> 0x00b6 }
            android.taobao.windvane.util.TaoLog.i(r0, r4)     // Catch:{ Exception -> 0x00b6 }
            goto L_0x00b6
        L_0x0092:
            java.lang.String r0 = "WVUCWebView"
            java.lang.StringBuilder r2 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x00b6 }
            r2.<init>()     // Catch:{ Exception -> 0x00b6 }
            java.lang.String r3 = "use 3.0 release core, use 64bit = ["
            r2.append(r3)     // Catch:{ Exception -> 0x00b6 }
            r2.append(r4)     // Catch:{ Exception -> 0x00b6 }
            java.lang.String r3 = "]"
            r2.append(r3)     // Catch:{ Exception -> 0x00b6 }
            java.lang.String r2 = r2.toString()     // Catch:{ Exception -> 0x00b6 }
            android.taobao.windvane.util.TaoLog.i(r0, r2)     // Catch:{ Exception -> 0x00b6 }
            if (r4 == 0) goto L_0x00b2
            java.lang.String r4 = UC_CORE_URL_64     // Catch:{ Exception -> 0x00b6 }
            goto L_0x00b4
        L_0x00b2:
            java.lang.String r4 = UC_CORE_URL_32     // Catch:{ Exception -> 0x00b6 }
        L_0x00b4:
            UC_CORE_URL = r4     // Catch:{ Exception -> 0x00b6 }
        L_0x00b6:
            java.util.concurrent.atomic.AtomicBoolean r4 = shouldUCLibInit     // Catch:{ Throwable -> 0x00d9 }
            r4.set(r1)     // Catch:{ Throwable -> 0x00d9 }
            java.lang.String r4 = "WVUCWebView"
            java.lang.String r0 = "init uclib inner"
            android.taobao.windvane.util.TaoLog.e(r4, r0)     // Catch:{ Throwable -> 0x00d9 }
            android.taobao.windvane.config.GlobalConfig r4 = android.taobao.windvane.config.GlobalConfig.getInstance()     // Catch:{ Throwable -> 0x00d9 }
            if (r4 == 0) goto L_0x00d2
            java.lang.String[] r4 = r4.getUcsdkappkeySec()     // Catch:{ Throwable -> 0x00d9 }
            android.app.Application r0 = android.taobao.windvane.config.GlobalConfig.context     // Catch:{ Throwable -> 0x00d9 }
            initUCLIb(r4, r0)     // Catch:{ Throwable -> 0x00d9 }
            goto L_0x00e6
        L_0x00d2:
            r4 = 0
            android.app.Application r0 = android.taobao.windvane.config.GlobalConfig.context     // Catch:{ Throwable -> 0x00d9 }
            initUCLIb(r4, r0)     // Catch:{ Throwable -> 0x00d9 }
            goto L_0x00e6
        L_0x00d9:
            android.app.Application r4 = android.taobao.windvane.config.GlobalConfig.context
            initUCLIb(r4)
            goto L_0x00e6
        L_0x00df:
            java.lang.String r4 = "WVUCWebView"
            java.lang.String r0 = "uc core has been initialized"
            android.taobao.windvane.util.TaoLog.d(r4, r0)
        L_0x00e6:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.extra.uc.WVUCWebView.initUCCore(android.content.Context):void");
    }

    private static boolean initUCLIb(String[] strArr, Context context2) {
        TaoLog.d(TAG, "UCSDK initUCLib begin ");
        setUseSystemWebView(WVCommonConfig.commonConfig.useSystemWebView);
        if (EnvUtil.isAppDebug()) {
            UCCore.setPrintLog(true);
        } else {
            UCCore.setPrintLog(false);
        }
        TaoLog.d(TAG, "UCSDK initUCLib UseSystemWebView " + mUseSystemWebView);
        if (WVCore.getInstance().isUCSupport()) {
            return true;
        }
        try {
            setUcCoreUrl(WVCommonConfig.commonConfig.ucCoreUrl);
            if (strArr == null && EnvUtil.isTaobao()) {
                strArr = WindVaneSDKForTB.TB_UC_SDK_APP_KEY_SEC;
            }
            Object[] objArr = {true, false, new ValueCallback<Object[]>() {
                public void onReceiveValue(Object[] objArr) {
                    StringBuffer stringBuffer = new StringBuffer();
                    stringBuffer.append(":");
                    stringBuffer.append(objArr[1]);
                    stringBuffer.append(":");
                    stringBuffer.append(objArr[2]);
                    stringBuffer.append(":");
                    stringBuffer.append(objArr[5]);
                    String stringBuffer2 = stringBuffer.toString();
                    if (objArr[6] != null) {
                        if ("v".equals(objArr[3])) {
                            TaoLog.v(objArr[4], stringBuffer2, objArr[6], new Object[0]);
                        } else if ("d".equals(objArr[3])) {
                            TaoLog.d(objArr[4], stringBuffer2, objArr[6], new Object[0]);
                        } else if (UploadQueueMgr.MSGTYPE_INTERVAL.equals(objArr[3])) {
                            TaoLog.i(objArr[4], stringBuffer2, objArr[6], new Object[0]);
                        } else if (WXComponent.PROP_FS_WRAP_CONTENT.equals(objArr[3])) {
                            TaoLog.w(objArr[4], stringBuffer2, objArr[6], new Object[0]);
                        } else if ("e".equals(objArr[3])) {
                            TaoLog.e(objArr[4], stringBuffer2, objArr[6], new Object[0]);
                        }
                    } else if ("v".equals(objArr[3])) {
                        TaoLog.v(objArr[4], stringBuffer2);
                    } else if ("d".equals(objArr[3])) {
                        TaoLog.d(objArr[4], stringBuffer2);
                    } else if (UploadQueueMgr.MSGTYPE_INTERVAL.equals(objArr[3])) {
                        TaoLog.i(objArr[4], stringBuffer2);
                    } else if (WXComponent.PROP_FS_WRAP_CONTENT.equals(objArr[3])) {
                        TaoLog.w(objArr[4], stringBuffer2);
                    } else if ("e".equals(objArr[3])) {
                        TaoLog.e(objArr[4], stringBuffer2);
                    }
                }
            }, "[all]", "[all]"};
            File file = new File(ucCore7ZFilePath(context2));
            TaoLog.i(TAG, "uccore policy:[" + WVCommonConfig.commonConfig.initUCCorePolicy + Operators.ARRAY_END_STR);
            TaoLog.i(TAG, "sandbox:policy [" + WVCommonConfig.commonConfig.webMultiPolicy + "];timeout [" + WVCommonConfig.commonConfig.ucMultiTimeOut + Operators.ARRAY_END_STR + Operators.ARRAY_END_STR);
            String processName = CommonUtils.getProcessName(context2);
            if (!TextUtils.equals(processName, context2.getPackageName() + ":sandboxed_privilege_process0")) {
                String processName2 = CommonUtils.getProcessName(context2);
                if (!TextUtils.equals(processName2, context2.getPackageName() + ":sandboxed_process0")) {
                    String processName3 = CommonUtils.getProcessName(context2);
                    if (!TextUtils.equals(processName3, context2.getPackageName() + ":gpu_process")) {
                        if (WVCommonConfig.commonConfig.initUCCorePolicy != 0 || !file.exists()) {
                            TaoLog.i(TAG, "下载uc初始化");
                            return initUCLibByDownload(strArr, context2, objArr);
                        }
                        TaoLog.i(TAG, "内置uc初始化");
                        return initUCLibBy7Z(strArr, context2, objArr);
                    }
                }
            }
            return false;
        } catch (Exception e) {
            TaoLog.e(TAG, "UCCore init fail " + e.getMessage());
            return false;
        }
    }

    private static String ucCore7ZFilePath(Context context2) {
        String uc7ZPath = GlobalConfig.getInstance().getUc7ZPath();
        if (!TextUtils.isEmpty(uc7ZPath) && new File(uc7ZPath).exists()) {
            return uc7ZPath;
        }
        return context2.getApplicationInfo().nativeLibraryDir + "/" + "libkernelu4_7z_uc.so";
    }

    private static String getOld7zDecompressPath(Context context2) {
        String stringVal = ConfigStorage.getStringVal(TBConfigManager.WINDVANE_COMMMON_CONFIG, "UC_PATH");
        TaoLog.i(TAG, "get dex path:[" + stringVal + Operators.ARRAY_END_STR);
        return stringVal;
    }

    /* access modifiers changed from: private */
    public static void saveUCCoeDexDirPath(String str) {
        TaoLog.i(TAG, "save dex path:[" + str + Operators.ARRAY_END_STR);
        ConfigStorage.putStringVal(TBConfigManager.WINDVANE_COMMMON_CONFIG, "UC_PATH", str);
    }

    private static class OnInitStart implements ValueCallback<Bundle> {
        private OnInitStart() {
        }

        public void onReceiveValue(Bundle bundle) {
            TaoLog.i(WVUCWebView.TAG, "on init start:[" + bundle + Operators.ARRAY_END_STR);
            if (bundle != null) {
                UCCore.BUSINESS_INIT_BY_OLD_CORE_DEX_DIR.equals(bundle.getString(UCCore.OPTION_BUSINESS_INIT_TYPE));
            }
        }
    }

    private static String getCore7zDecompressDir(Context context2, String str) {
        return UCCore.getExtractDirPath(context2, str);
    }

    private static String getBusinessDecompressRootDir(Context context2) {
        return UCCore.getExtractRootDirPath(context2);
    }

    private static final class OldCoreVersionCallable implements UCCore.Callable<Boolean, String> {
        private OldCoreVersionCallable() {
        }

        public Boolean call(String str) {
            TaoLog.i(WVUCWebView.TAG, "version callable value:" + str);
            return false;
        }
    }

    private static final class DecompressCallable implements UCCore.Callable<Boolean, Bundle> {
        private Context mContext;

        public DecompressCallable(Context context) {
            this.mContext = context.getApplicationContext();
        }

        public Boolean call(Bundle bundle) throws Exception {
            TaoLog.d(WVUCWebView.TAG, "decompress parameters:" + bundle);
            ProcessLockUtil processLockUtil = new ProcessLockUtil(this.mContext.getCacheDir() + "/.taobaoDec7zSo.lock");
            try {
                processLockUtil.lock();
                long currentTimeMillis = System.currentTimeMillis();
                this.mContext.getDir("h5container", 0);
                if (WVUCWebView.isMainProcess()) {
                    TaoLog.d(WVUCWebView.TAG, "init on main process, mark uc not init!");
                }
                String string = bundle.getString("decDirPath");
                boolean extractWebCoreLibraryIfNeeded = UCCore.extractWebCoreLibraryIfNeeded(this.mContext, bundle.getString("zipFilePath"), bundle.getString("zipFileType"), string, bundle.getBoolean("deleteAfterExtract"));
                TaoLog.d(WVUCWebView.TAG, "taobaoDec7zSo elapse " + (System.currentTimeMillis() - currentTimeMillis));
                Boolean valueOf = Boolean.valueOf(extractWebCoreLibraryIfNeeded);
                processLockUtil.unlock();
                return valueOf;
            } catch (Exception e) {
                TaoLog.e(WVUCWebView.TAG, "catch exception ", e, new Object[0]);
                throw e;
            } catch (Throwable th) {
                processLockUtil.unlock();
                throw th;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:30:0x0056  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x0058  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean initUCLibBy7Z(java.lang.String[] r10, android.content.Context r11, java.lang.Object[] r12) {
        /*
            r0 = 1
            INNER = r0
            java.lang.String r1 = ucCore7ZFilePath(r11)
            java.lang.String r2 = getOld7zDecompressPath(r11)
            java.lang.String r3 = getCore7zDecompressDir(r11, r1)
            boolean r3 = r2.equals(r3)
            if (r3 == 0) goto L_0x0017
            java.lang.String r2 = ""
        L_0x0017:
            android.taobao.windvane.config.WVCommonConfigData r3 = android.taobao.windvane.config.WVCommonConfig.commonConfig
            int r3 = r3.webMultiPolicy
            r4 = 0
            if (r3 != r0) goto L_0x0020
            r3 = 1
            goto L_0x0021
        L_0x0020:
            r3 = 0
        L_0x0021:
            android.taobao.windvane.config.WVCommonConfigData r5 = android.taobao.windvane.config.WVCommonConfig.commonConfig
            int r5 = r5.webMultiPolicy
            r6 = 2
            if (r5 != r6) goto L_0x002a
            r5 = 1
            goto L_0x002b
        L_0x002a:
            r5 = 0
        L_0x002b:
            android.taobao.windvane.config.WVCommonConfigData r7 = android.taobao.windvane.config.WVCommonConfig.commonConfig
            int r7 = r7.gpuMultiPolicy
            if (r7 != r0) goto L_0x0033
            r7 = 1
            goto L_0x0034
        L_0x0033:
            r7 = 0
        L_0x0034:
            android.taobao.windvane.config.WVCommonConfigData r8 = android.taobao.windvane.config.WVCommonConfig.commonConfig
            int r8 = r8.gpuMultiPolicy
            if (r8 != r6) goto L_0x003c
            r8 = 1
            goto L_0x003d
        L_0x003c:
            r8 = 0
        L_0x003d:
            boolean r9 = isMainProcess()
            if (r9 == 0) goto L_0x004b
            if (r3 == 0) goto L_0x0047
            r5 = 1
            goto L_0x004c
        L_0x0047:
            if (r5 == 0) goto L_0x004b
            r5 = 2
            goto L_0x004c
        L_0x004b:
            r5 = 0
        L_0x004c:
            boolean r9 = isMainProcess()
            if (r9 == 0) goto L_0x005c
            if (r3 == 0) goto L_0x005c
            if (r7 == 0) goto L_0x0058
            r3 = 1
            goto L_0x005d
        L_0x0058:
            if (r8 == 0) goto L_0x005c
            r3 = 2
            goto L_0x005d
        L_0x005c:
            r3 = 0
        L_0x005d:
            java.lang.String r7 = "WVUCWebView"
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = "UCSDK initUCLibBy7Z zipPath: "
            r8.append(r9)
            r8.append(r1)
            java.lang.String r8 = r8.toString()
            android.taobao.windvane.util.TaoLog.i(r7, r8)
            java.lang.String r7 = "CONTEXT"
            android.content.Context r8 = r11.getApplicationContext()     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r7 = com.uc.webview.export.business.BusinessWrapper.setup(r7, r8)     // Catch:{ Exception -> 0x0261 }
            java.lang.String r8 = "bo_dec_r_p"
            java.lang.String r9 = getBusinessDecompressRootDir(r11)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r8 = r7.setup((java.lang.String) r8, (java.lang.Object) r9)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r8 = (com.uc.webview.export.utility.SetupTask) r8     // Catch:{ Exception -> 0x0261 }
            java.lang.String r9 = "webview_multi_process"
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r5 = r8.setup((java.lang.String) r9, (java.lang.Object) r5)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r5 = (com.uc.webview.export.utility.SetupTask) r5     // Catch:{ Exception -> 0x0261 }
            java.lang.String r8 = "gpu_process_mode"
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r3 = r5.setup((java.lang.String) r8, (java.lang.Object) r3)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r3 = (com.uc.webview.export.utility.SetupTask) r3     // Catch:{ Exception -> 0x0261 }
            java.lang.String r5 = "webview_multi_process_fallback_timeout"
            android.taobao.windvane.config.WVCommonConfigData r8 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Exception -> 0x0261 }
            int r8 = r8.ucMultiTimeOut     // Catch:{ Exception -> 0x0261 }
            java.lang.Integer r8 = java.lang.Integer.valueOf(r8)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r3 = r3.setup((java.lang.String) r5, (java.lang.Object) r8)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r3 = (com.uc.webview.export.utility.SetupTask) r3     // Catch:{ Exception -> 0x0261 }
            java.lang.String r5 = "webview_multi_process_enable_service_speedup"
            android.taobao.windvane.config.WVCommonConfigData r8 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Exception -> 0x0261 }
            boolean r8 = r8.ucMultiServiceSpeedUp     // Catch:{ Exception -> 0x0261 }
            java.lang.Boolean r8 = java.lang.Boolean.valueOf(r8)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r3 = r3.setup((java.lang.String) r5, (java.lang.Object) r8)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r3 = (com.uc.webview.export.utility.SetupTask) r3     // Catch:{ Exception -> 0x0261 }
            java.lang.String r5 = "bo_f_u_dec_r_p"
            java.lang.Boolean r8 = java.lang.Boolean.valueOf(r0)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r3 = r3.setup((java.lang.String) r5, (java.lang.Object) r8)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r3 = (com.uc.webview.export.utility.SetupTask) r3     // Catch:{ Exception -> 0x0261 }
            java.lang.String r5 = "first_use_sw"
            android.taobao.windvane.config.WVCommonConfigData r8 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Exception -> 0x0261 }
            boolean r8 = r8.firstUseSystemWebViewOn7zInit     // Catch:{ Exception -> 0x0261 }
            java.lang.Boolean r8 = java.lang.Boolean.valueOf(r8)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r3 = r3.setup((java.lang.String) r5, (java.lang.Object) r8)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r3 = (com.uc.webview.export.utility.SetupTask) r3     // Catch:{ Exception -> 0x0261 }
            java.lang.String r5 = "bo_new_ucm_zf"
            com.uc.webview.export.internal.setup.BaseSetupTask r3 = r3.setup((java.lang.String) r5, (java.lang.Object) r1)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r3 = (com.uc.webview.export.utility.SetupTask) r3     // Catch:{ Exception -> 0x0261 }
            java.lang.String r5 = "bo_new_ucm_z_type"
            java.lang.String r8 = "7z"
            com.uc.webview.export.internal.setup.BaseSetupTask r3 = r3.setup((java.lang.String) r5, (java.lang.Object) r8)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r3 = (com.uc.webview.export.utility.SetupTask) r3     // Catch:{ Exception -> 0x0261 }
            java.lang.String r5 = "bo_old_dex_dp"
            com.uc.webview.export.internal.setup.BaseSetupTask r2 = r3.setup((java.lang.String) r5, (java.lang.Object) r2)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r2 = (com.uc.webview.export.utility.SetupTask) r2     // Catch:{ Exception -> 0x0261 }
            java.lang.String r3 = "bo_dex_old_dex_dir"
            boolean r5 = isMainProcess()     // Catch:{ Exception -> 0x0261 }
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r5)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r2 = r2.setup((java.lang.String) r3, (java.lang.Object) r5)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r2 = (com.uc.webview.export.utility.SetupTask) r2     // Catch:{ Exception -> 0x0261 }
            java.lang.String r3 = "bo_skip_io_dc"
            java.lang.Boolean r5 = java.lang.Boolean.valueOf(r0)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r2 = r2.setup((java.lang.String) r3, (java.lang.Object) r5)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r2 = (com.uc.webview.export.utility.SetupTask) r2     // Catch:{ Exception -> 0x0261 }
            java.lang.String r3 = "bo_prom_sp_v_c_i"
            android.taobao.windvane.extra.uc.WVUCWebView$OldCoreVersionCallable r5 = new android.taobao.windvane.extra.uc.WVUCWebView$OldCoreVersionCallable     // Catch:{ Exception -> 0x0261 }
            r8 = 0
            r5.<init>()     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r2 = r2.setup((java.lang.String) r3, (java.lang.Object) r5)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r2 = (com.uc.webview.export.utility.SetupTask) r2     // Catch:{ Exception -> 0x0261 }
            java.lang.String r3 = "bo_s_i_uc_core"
            android.taobao.windvane.extra.uc.WVUCWebView$OnInitStart r5 = new android.taobao.windvane.extra.uc.WVUCWebView$OnInitStart     // Catch:{ Exception -> 0x0261 }
            r5.<init>()     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r2 = r2.setup((java.lang.String) r3, (java.lang.Object) r5)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r2 = (com.uc.webview.export.utility.SetupTask) r2     // Catch:{ Exception -> 0x0261 }
            java.lang.String r3 = "bo_dec_cl"
            android.taobao.windvane.extra.uc.WVUCWebView$DecompressCallable r5 = new android.taobao.windvane.extra.uc.WVUCWebView$DecompressCallable     // Catch:{ Exception -> 0x0261 }
            r5.<init>(r11)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r2 = r2.setup((java.lang.String) r3, (java.lang.Object) r5)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r2 = (com.uc.webview.export.utility.SetupTask) r2     // Catch:{ Exception -> 0x0261 }
            java.lang.String r3 = "be_init_success"
            android.taobao.windvane.extra.uc.WVUCWebView$3 r5 = new android.taobao.windvane.extra.uc.WVUCWebView$3     // Catch:{ Exception -> 0x0261 }
            r5.<init>()     // Catch:{ Exception -> 0x0261 }
            r2.onEvent((java.lang.String) r3, r5)     // Catch:{ Exception -> 0x0261 }
            setupUCParam(r1)     // Catch:{ Exception -> 0x0261 }
            java.lang.String r1 = "CONTEXT"
            android.content.Context r11 = r11.getApplicationContext()     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r11 = r7.setup((java.lang.String) r1, (java.lang.Object) r11)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r11 = (com.uc.webview.export.utility.SetupTask) r11     // Catch:{ Exception -> 0x0261 }
            java.lang.String r1 = "provided_keys"
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r11.setup((java.lang.String) r1, (java.lang.Object) r10)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x0261 }
            java.lang.String r11 = "VIDEO_AC"
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r0)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r1)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x0261 }
            java.lang.String r11 = "AC"
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r0)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r1)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x0261 }
            java.lang.String r11 = "core_ver_excludes"
            android.taobao.windvane.config.WVCommonConfigData r1 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Exception -> 0x0261 }
            java.lang.String r1 = r1.excludeUCVersions     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r1)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x0261 }
            java.lang.String r11 = "MULTI_CORE_TYPE"
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r0)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r1)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x0261 }
            java.lang.String r11 = "SYSTEM_WEBVIEW"
            boolean r1 = mUseSystemWebView     // Catch:{ Exception -> 0x0261 }
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r1)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x0261 }
            java.lang.String r11 = "WEBVIEW_POLICY"
            java.lang.Integer r1 = java.lang.Integer.valueOf(r6)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r1)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x0261 }
            java.lang.String r11 = "loadPolicy"
            java.lang.String r1 = "SPECIFIED_ONLY"
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r1)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x0261 }
            java.lang.String r11 = "VERIFY_POLICY"
            java.lang.Integer r1 = java.lang.Integer.valueOf(r4)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r1)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x0261 }
            java.lang.String r11 = "delete_core"
            java.lang.Integer r1 = java.lang.Integer.valueOf(r4)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r1)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x0261 }
            java.lang.String r11 = "log_conf"
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r12)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x0261 }
            java.lang.String r11 = "wait_fallback_sys"
            r12 = 4000(0xfa0, float:5.605E-42)
            java.lang.Integer r12 = java.lang.Integer.valueOf(r12)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r12)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x0261 }
            java.lang.String r11 = "ucPlayer"
            android.taobao.windvane.config.WVCommonConfigData r12 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Exception -> 0x0261 }
            boolean r12 = r12.useUCPlayer     // Catch:{ Exception -> 0x0261 }
            java.lang.Boolean r12 = java.lang.Boolean.valueOf(r12)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r12)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x0261 }
            java.lang.String r11 = "skip_old_extra_kernel"
            android.taobao.windvane.config.WVCommonConfigData r12 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Exception -> 0x0261 }
            boolean r12 = r12.ucSkipOldKernel     // Catch:{ Exception -> 0x0261 }
            java.lang.Boolean r12 = java.lang.Boolean.valueOf(r12)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r12)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x0261 }
            java.lang.String r11 = "sdk_international_env"
            android.taobao.windvane.config.GlobalConfig r12 = android.taobao.windvane.config.GlobalConfig.getInstance()     // Catch:{ Exception -> 0x0261 }
            boolean r12 = r12.isUcSdkInternationalEnv()     // Catch:{ Exception -> 0x0261 }
            java.lang.Boolean r12 = java.lang.Boolean.valueOf(r12)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r12)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x0261 }
            java.lang.String r11 = "startup_policy"
            android.taobao.windvane.config.WVCommonConfigData r12 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Exception -> 0x0261 }
            int r12 = r12.initWebPolicy     // Catch:{ Exception -> 0x0261 }
            java.lang.Integer r12 = java.lang.Integer.valueOf(r12)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r12)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x0261 }
            java.lang.String r11 = "PRIVATE_DATA_DIRECTORY_SUFFIX"
            java.lang.String r12 = getMultiProcessPrivateDataDirectorySuffix()     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r12)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x0261 }
            java.lang.String r11 = "exception"
            android.taobao.windvane.extra.uc.WVUCWebView$ExceptionValueCallback r12 = new android.taobao.windvane.extra.uc.WVUCWebView$ExceptionValueCallback     // Catch:{ Exception -> 0x0261 }
            r12.<init>()     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.onEvent((java.lang.String) r11, r12)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x0261 }
            java.lang.String r11 = "success"
            android.taobao.windvane.extra.uc.WVUCWebView$CorePreparedCallback r12 = new android.taobao.windvane.extra.uc.WVUCWebView$CorePreparedCallback     // Catch:{ Exception -> 0x0261 }
            long r1 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x0261 }
            r12.<init>(r1)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.onEvent((java.lang.String) r11, r12)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x0261 }
            java.lang.String r11 = "switch"
            android.taobao.windvane.extra.uc.WVUCWebView$SwitchValueCallback r12 = new android.taobao.windvane.extra.uc.WVUCWebView$SwitchValueCallback     // Catch:{ Exception -> 0x0261 }
            r12.<init>()     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.onEvent((java.lang.String) r11, r12)     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x0261 }
            com.uc.webview.export.utility.SetupTask r10 = r10.setAsDefault()     // Catch:{ Exception -> 0x0261 }
            r10.start()     // Catch:{ Exception -> 0x0261 }
            goto L_0x027c
        L_0x0261:
            r10 = move-exception
            java.lang.String r11 = "WVUCWebView"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r1 = "initUCLibBy7Z fail "
            r12.append(r1)
            java.lang.String r10 = r10.getMessage()
            r12.append(r10)
            java.lang.String r10 = r12.toString()
            android.taobao.windvane.util.TaoLog.e(r11, r10)
        L_0x027c:
            boolean r10 = mUseSystemWebView
            r10 = r10 ^ r0
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.extra.uc.WVUCWebView.initUCLibBy7Z(java.lang.String[], android.content.Context, java.lang.Object[]):boolean");
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x007f A[Catch:{ Exception -> 0x01fc }] */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x0081 A[Catch:{ Exception -> 0x01fc }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean initUCLibByDownload(java.lang.String[] r10, android.content.Context r11, java.lang.Object[] r12) {
        /*
            r0 = 1
            android.taobao.windvane.config.WVCommonConfigData r1 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Exception -> 0x01fc }
            java.lang.String r1 = r1.ucCoreUrl     // Catch:{ Exception -> 0x01fc }
            setUcCoreUrl(r1)     // Catch:{ Exception -> 0x01fc }
            android.taobao.windvane.config.GlobalConfig r1 = android.taobao.windvane.config.GlobalConfig.getInstance()     // Catch:{ Exception -> 0x01fc }
            java.lang.String r1 = r1.getUcLibDir()     // Catch:{ Exception -> 0x01fc }
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch:{ Exception -> 0x01fc }
            r2 = 0
            if (r1 != 0) goto L_0x0026
            java.lang.String r1 = "dexFilePath"
            android.taobao.windvane.config.GlobalConfig r3 = android.taobao.windvane.config.GlobalConfig.getInstance()     // Catch:{ Exception -> 0x01fc }
            java.lang.String r3 = r3.getUcLibDir()     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r1 = com.uc.webview.export.extension.UCCore.setup(r1, r3)     // Catch:{ Exception -> 0x01fc }
            goto L_0x0040
        L_0x0026:
            setupUCParam(r2)     // Catch:{ Exception -> 0x01fc }
            java.lang.String r1 = "dlChecker"
            android.taobao.windvane.extra.uc.WVUCWebView$DownloadEnv r3 = new android.taobao.windvane.extra.uc.WVUCWebView$DownloadEnv     // Catch:{ Exception -> 0x01fc }
            android.app.Application r4 = android.taobao.windvane.config.GlobalConfig.context     // Catch:{ Exception -> 0x01fc }
            r3.<init>(r4)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r1 = com.uc.webview.export.extension.UCCore.setup(r1, r3)     // Catch:{ Exception -> 0x01fc }
            java.lang.String r3 = "ucmUpdUrl"
            java.lang.String r4 = UC_CORE_URL     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r1 = r1.setup((java.lang.String) r3, (java.lang.Object) r4)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r1 = (com.uc.webview.export.utility.SetupTask) r1     // Catch:{ Exception -> 0x01fc }
        L_0x0040:
            android.taobao.windvane.config.WVCommonConfigData r3 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Exception -> 0x01fc }
            int r3 = r3.webMultiPolicy     // Catch:{ Exception -> 0x01fc }
            r4 = 0
            if (r3 != r0) goto L_0x0049
            r3 = 1
            goto L_0x004a
        L_0x0049:
            r3 = 0
        L_0x004a:
            android.taobao.windvane.config.WVCommonConfigData r5 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Exception -> 0x01fc }
            int r5 = r5.webMultiPolicy     // Catch:{ Exception -> 0x01fc }
            r6 = 2
            if (r5 != r6) goto L_0x0053
            r5 = 1
            goto L_0x0054
        L_0x0053:
            r5 = 0
        L_0x0054:
            android.taobao.windvane.config.WVCommonConfigData r7 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Exception -> 0x01fc }
            int r7 = r7.gpuMultiPolicy     // Catch:{ Exception -> 0x01fc }
            if (r7 != r0) goto L_0x005c
            r7 = 1
            goto L_0x005d
        L_0x005c:
            r7 = 0
        L_0x005d:
            android.taobao.windvane.config.WVCommonConfigData r8 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Exception -> 0x01fc }
            int r8 = r8.gpuMultiPolicy     // Catch:{ Exception -> 0x01fc }
            if (r8 != r6) goto L_0x0065
            r8 = 1
            goto L_0x0066
        L_0x0065:
            r8 = 0
        L_0x0066:
            boolean r9 = isMainProcess()     // Catch:{ Exception -> 0x01fc }
            if (r9 == 0) goto L_0x0074
            if (r3 == 0) goto L_0x0070
            r5 = 1
            goto L_0x0075
        L_0x0070:
            if (r5 == 0) goto L_0x0074
            r5 = 2
            goto L_0x0075
        L_0x0074:
            r5 = 0
        L_0x0075:
            boolean r9 = isMainProcess()     // Catch:{ Exception -> 0x01fc }
            if (r9 == 0) goto L_0x0085
            if (r3 == 0) goto L_0x0085
            if (r7 == 0) goto L_0x0081
            r3 = 1
            goto L_0x0086
        L_0x0081:
            if (r8 == 0) goto L_0x0085
            r3 = 2
            goto L_0x0086
        L_0x0085:
            r3 = 0
        L_0x0086:
            java.lang.String r7 = "CONTEXT"
            android.content.Context r11 = r11.getApplicationContext()     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r11 = r1.setup((java.lang.String) r7, (java.lang.Object) r11)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r11 = (com.uc.webview.export.utility.SetupTask) r11     // Catch:{ Exception -> 0x01fc }
            java.lang.String r1 = "webview_multi_process"
            java.lang.Integer r5 = java.lang.Integer.valueOf(r5)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r11 = r11.setup((java.lang.String) r1, (java.lang.Object) r5)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r11 = (com.uc.webview.export.utility.SetupTask) r11     // Catch:{ Exception -> 0x01fc }
            java.lang.String r1 = "gpu_process_mode"
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r11 = r11.setup((java.lang.String) r1, (java.lang.Object) r3)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r11 = (com.uc.webview.export.utility.SetupTask) r11     // Catch:{ Exception -> 0x01fc }
            java.lang.String r1 = "webview_multi_process_fallback_timeout"
            android.taobao.windvane.config.WVCommonConfigData r3 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Exception -> 0x01fc }
            int r3 = r3.ucMultiTimeOut     // Catch:{ Exception -> 0x01fc }
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r11 = r11.setup((java.lang.String) r1, (java.lang.Object) r3)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r11 = (com.uc.webview.export.utility.SetupTask) r11     // Catch:{ Exception -> 0x01fc }
            java.lang.String r1 = "webview_multi_process_enable_service_speedup"
            android.taobao.windvane.config.WVCommonConfigData r3 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Exception -> 0x01fc }
            boolean r3 = r3.ucMultiServiceSpeedUp     // Catch:{ Exception -> 0x01fc }
            java.lang.Boolean r3 = java.lang.Boolean.valueOf(r3)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r11 = r11.setup((java.lang.String) r1, (java.lang.Object) r3)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r11 = (com.uc.webview.export.utility.SetupTask) r11     // Catch:{ Exception -> 0x01fc }
            java.lang.String r1 = "provided_keys"
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r11.setup((java.lang.String) r1, (java.lang.Object) r10)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x01fc }
            java.lang.String r11 = "VIDEO_AC"
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r0)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r1)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x01fc }
            java.lang.String r11 = "AC"
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r0)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r1)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x01fc }
            java.lang.String r11 = "core_ver_excludes"
            android.taobao.windvane.config.WVCommonConfigData r1 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Exception -> 0x01fc }
            java.lang.String r1 = r1.excludeUCVersions     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r1)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x01fc }
            java.lang.String r11 = "MULTI_CORE_TYPE"
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r0)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r1)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x01fc }
            java.lang.String r11 = "SYSTEM_WEBVIEW"
            boolean r1 = mUseSystemWebView     // Catch:{ Exception -> 0x01fc }
            java.lang.Boolean r1 = java.lang.Boolean.valueOf(r1)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r1)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x01fc }
            java.lang.String r11 = "WEBVIEW_POLICY"
            java.lang.Integer r1 = java.lang.Integer.valueOf(r6)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r1)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x01fc }
            java.lang.String r11 = "loadPolicy"
            java.lang.String r1 = "SPECIFIED_ONLY"
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r1)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x01fc }
            java.lang.String r11 = "VERIFY_POLICY"
            java.lang.Integer r1 = java.lang.Integer.valueOf(r4)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r1)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x01fc }
            java.lang.String r11 = "delete_core"
            java.lang.Integer r1 = java.lang.Integer.valueOf(r4)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r1)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x01fc }
            java.lang.String r11 = "log_conf"
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r12)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x01fc }
            java.lang.String r11 = "wait_fallback_sys"
            r12 = 4000(0xfa0, float:5.605E-42)
            java.lang.Integer r12 = java.lang.Integer.valueOf(r12)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r12)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x01fc }
            java.lang.String r11 = "ucPlayer"
            android.taobao.windvane.config.WVCommonConfigData r12 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Exception -> 0x01fc }
            boolean r12 = r12.useUCPlayer     // Catch:{ Exception -> 0x01fc }
            java.lang.Boolean r12 = java.lang.Boolean.valueOf(r12)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r12)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x01fc }
            java.lang.String r11 = "skip_old_extra_kernel"
            android.taobao.windvane.config.WVCommonConfigData r12 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Exception -> 0x01fc }
            boolean r12 = r12.ucSkipOldKernel     // Catch:{ Exception -> 0x01fc }
            java.lang.Boolean r12 = java.lang.Boolean.valueOf(r12)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r12)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x01fc }
            java.lang.String r11 = "sdk_international_env"
            android.taobao.windvane.config.GlobalConfig r12 = android.taobao.windvane.config.GlobalConfig.getInstance()     // Catch:{ Exception -> 0x01fc }
            boolean r12 = r12.isUcSdkInternationalEnv()     // Catch:{ Exception -> 0x01fc }
            java.lang.Boolean r12 = java.lang.Boolean.valueOf(r12)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r12)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x01fc }
            java.lang.String r11 = "startup_policy"
            android.taobao.windvane.config.WVCommonConfigData r12 = android.taobao.windvane.config.WVCommonConfig.commonConfig     // Catch:{ Exception -> 0x01fc }
            int r12 = r12.initWebPolicy     // Catch:{ Exception -> 0x01fc }
            java.lang.Integer r12 = java.lang.Integer.valueOf(r12)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r12)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x01fc }
            java.lang.String r11 = "PRIVATE_DATA_DIRECTORY_SUFFIX"
            java.lang.String r12 = getMultiProcessPrivateDataDirectorySuffix()     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.setup((java.lang.String) r11, (java.lang.Object) r12)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x01fc }
            java.lang.String r11 = "updateProgress"
            android.taobao.windvane.extra.uc.WVUCWebView$DownLoadCallback r12 = new android.taobao.windvane.extra.uc.WVUCWebView$DownLoadCallback     // Catch:{ Exception -> 0x01fc }
            r12.<init>()     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.onEvent((java.lang.String) r11, r12)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x01fc }
            java.lang.String r11 = "exception"
            android.taobao.windvane.extra.uc.WVUCWebView$ExceptionValueCallback r12 = new android.taobao.windvane.extra.uc.WVUCWebView$ExceptionValueCallback     // Catch:{ Exception -> 0x01fc }
            r12.<init>()     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.onEvent((java.lang.String) r11, r12)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x01fc }
            java.lang.String r11 = "success"
            android.taobao.windvane.extra.uc.WVUCWebView$CorePreparedCallback r12 = new android.taobao.windvane.extra.uc.WVUCWebView$CorePreparedCallback     // Catch:{ Exception -> 0x01fc }
            long r3 = java.lang.System.currentTimeMillis()     // Catch:{ Exception -> 0x01fc }
            r12.<init>(r3)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.onEvent((java.lang.String) r11, r12)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x01fc }
            java.lang.String r11 = "switch"
            android.taobao.windvane.extra.uc.WVUCWebView$SwitchValueCallback r12 = new android.taobao.windvane.extra.uc.WVUCWebView$SwitchValueCallback     // Catch:{ Exception -> 0x01fc }
            r12.<init>()     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.internal.setup.BaseSetupTask r10 = r10.onEvent((java.lang.String) r11, r12)     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = (com.uc.webview.export.utility.SetupTask) r10     // Catch:{ Exception -> 0x01fc }
            com.uc.webview.export.utility.SetupTask r10 = r10.setAsDefault()     // Catch:{ Exception -> 0x01fc }
            r10.start()     // Catch:{ Exception -> 0x01fc }
            java.lang.String r10 = "UCCore"
            java.lang.StringBuilder r11 = new java.lang.StringBuilder     // Catch:{ Exception -> 0x01fc }
            r11.<init>()     // Catch:{ Exception -> 0x01fc }
            java.lang.String r12 = "final UCCore:"
            r11.append(r12)     // Catch:{ Exception -> 0x01fc }
            java.lang.String r12 = UC_CORE_URL     // Catch:{ Exception -> 0x01fc }
            r11.append(r12)     // Catch:{ Exception -> 0x01fc }
            java.lang.String r11 = r11.toString()     // Catch:{ Exception -> 0x01fc }
            android.taobao.windvane.util.TaoLog.i(r10, r11)     // Catch:{ Exception -> 0x01fc }
            goto L_0x0217
        L_0x01fc:
            r10 = move-exception
            java.lang.String r11 = "UCCore"
            java.lang.StringBuilder r12 = new java.lang.StringBuilder
            r12.<init>()
            java.lang.String r1 = "UCCore init fail "
            r12.append(r1)
            java.lang.String r10 = r10.getMessage()
            r12.append(r10)
            java.lang.String r10 = r12.toString()
            android.taobao.windvane.util.TaoLog.e(r11, r10)
        L_0x0217:
            boolean r10 = mUseSystemWebView
            r10 = r10 ^ r0
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.extra.uc.WVUCWebView.initUCLibByDownload(java.lang.String[], android.content.Context, java.lang.Object[]):boolean");
    }

    /* access modifiers changed from: private */
    public static boolean isMainProcess() {
        boolean isMainProcess = CommonUtils.isMainProcess(GlobalConfig.context);
        TaoLog.i(TAG, "是否在主进程:" + isMainProcess);
        return isMainProcess;
    }

    private static String getMultiProcessPrivateDataDirectorySuffix() {
        if (isMainProcess()) {
            return "0";
        }
        String substring = CommonUtils.getProcessName(GlobalConfig.context).substring(GlobalConfig.context.getPackageName().length() + ":".length());
        if (substring != null && substring.length() > 0) {
            return substring;
        }
        throw new RuntimeException(String.format("%s getMultiProcessPrivateDataDirectorySuffix failure!Subprocess name: %s illegal.", new Object[]{TAG, CommonUtils.getProcessName(GlobalConfig.context)}));
    }

    public static boolean isNeedCookie(String str) {
        Matcher matcher;
        try {
            if (pattern == null || (matcher = pattern.matcher(str)) == null || !matcher.matches()) {
                return true;
            }
            return false;
        } catch (Exception e) {
            TaoLog.e(TAG, "Pattern complile Exception" + e.getMessage());
            return true;
        }
    }

    public WVUCWebView(Context context2, boolean z) {
        super(context2, isUseSystemWebView(context2));
        String[] strArr = new String[1];
        strArr[0] = EnvUtil.isCN() ? "保存到相册" : "Save to album";
        this.mPopupMenuTags = strArr;
        this.mLongClickListener = null;
        this.popupClickListener = new View.OnClickListener() {
            public void onClick(View view) {
                if (WVUCWebView.this.mPopupMenuTags != null && WVUCWebView.this.mPopupMenuTags.length > 0 && WVUCWebView.this.mPopupMenuTags[0].equals(view.getTag())) {
                    try {
                        PermissionProposer.buildPermissionTask(WVUCWebView.this.context, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}).setTaskOnPermissionGranted(new Runnable() {
                            public void run() {
                                if (WVUCWebView.this.context != null) {
                                    ImageTool.saveImageToDCIM(WVUCWebView.this.context.getApplicationContext(), WVUCWebView.this.mImageUrl, WVUCWebView.this.mHandler);
                                }
                            }
                        }).setTaskOnPermissionDenied(new Runnable() {
                            public void run() {
                                WVUCWebView.this.mHandler.sendEmptyMessage(405);
                            }
                        }).execute();
                    } catch (Exception unused) {
                    }
                }
                if (WVUCWebView.this.mPopupController != null) {
                    WVUCWebView.this.mPopupController.hide();
                }
            }
        };
        this.wvUIModel = null;
        this.onErrorTime = 0;
        this.mIsStaticWebView = false;
        this.isUser = true;
        this.mEventSparseArray = new SparseArray<>();
        this.mH5MonitorCache = null;
        this.mPageStart = 0;
        this.injectJs = new StringBuilder("javascript:");
        this.context = context2;
        this.mIsStaticWebView = z;
        if (WVMonitorService.getWvMonitorInterface() != null) {
            WVMonitorService.getWvMonitorInterface().WebViewWrapType(context2.getClass().getSimpleName());
        }
        if (z) {
            setWebViewClient(new WVUCWebViewClient(context2));
            setWebChromeClient(new WVUCWebChromeClient(context2));
            UCExtension uCExtension = getUCExtension();
            if (uCExtension != null) {
                uCExtension.setClient(new WVUCClient(this));
                return;
            }
            return;
        }
        init();
    }

    public boolean isStaticWebView() {
        return this.mIsStaticWebView;
    }

    public static void createStaticWebViewIfNeeded(final Context context2) {
        if (isWebViewMultiProcessEnabled()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    WVUCWebView.createStaticWebViewOnMainThread(context2);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public static void createStaticWebViewOnMainThread(Context context2) {
        TaoLog.i("sandbox", "createStaticWebViewOnMainThread");
        if (sStaticUCWebView == null) {
            sStaticUCWebView = new WVUCWebView(context2.getApplicationContext(), true);
            sStaticUCWebView.loadUrl(STATIC_WEBVIEW_URL);
        }
    }

    public static void destroyStaticWebViewIfNeeded() {
        if (isWebViewMultiProcessEnabled()) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                public void run() {
                    WVUCWebView.destroyStaticWebViewOnMainThread();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public static void destroyStaticWebViewOnMainThread() {
        TaoLog.i("sandbox", "destroyStaticWebViewOnMainThread");
        if (sStaticUCWebView != null) {
            sStaticUCWebView.destroy();
            sStaticUCWebView = null;
        }
    }

    public static boolean isWebViewMultiProcessEnabled() {
        return WVCommonConfig.commonConfig.webMultiPolicy > 0;
    }

    public Context getCurrentContext() {
        return this.context;
    }

    public void postUrl(String str, byte[] bArr) {
        if (str != null) {
            if (!WVUrlUtil.isCommonUrl(str) || !WVServerConfig.isBlackUrl(str)) {
                WVSchemeIntercepterInterface wVSchemeIntercepter = WVSchemeInterceptService.getWVSchemeIntercepter();
                if (wVSchemeIntercepter != null) {
                    str = wVSchemeIntercepter.dealUrlScheme(str);
                }
                try {
                    UCNetworkDelegate.getInstance().onUrlChange(this, str);
                    TaoLog.i(TAG, "postUrl : " + str);
                    super.postUrl(str, bArr);
                } catch (Exception e) {
                    TaoLog.e(TAG, e.getMessage());
                }
            } else {
                String forbiddenDomainRedirectURL = WVDomainConfig.getInstance().getForbiddenDomainRedirectURL();
                if (TextUtils.isEmpty(forbiddenDomainRedirectURL)) {
                    HashMap hashMap = new HashMap(2);
                    hashMap.put("cause", "POST_ACCESS_FORBIDDEN");
                    hashMap.put("url", str);
                    onMessage(402, hashMap);
                    return;
                }
                try {
                    loadUrl(forbiddenDomainRedirectURL);
                } catch (Exception e2) {
                    TaoLog.e(TAG, e2.getMessage());
                }
            }
        }
    }

    public void loadUrl(String str) {
        if (str != null) {
            boolean isCommonUrl = WVUrlUtil.isCommonUrl(str);
            if (!isCommonUrl || !WVServerConfig.isBlackUrl(str)) {
                if (isCommonUrl) {
                    try {
                        HashMap hashMap = new HashMap();
                        hashMap.put("userAgent", getUserAgentString());
                        String prefetchData = WMLPrefetch.getInstance().prefetchData(str, hashMap);
                        if (!TextUtils.isEmpty(prefetchData)) {
                            str = prefetchData;
                        }
                    } catch (Throwable th) {
                        TaoLog.e(TAG, "failed to call prefetch: " + th.getMessage());
                        th.getStackTrace();
                    }
                }
                WVSchemeIntercepterInterface wVSchemeIntercepter = WVSchemeInterceptService.getWVSchemeIntercepter();
                if (wVSchemeIntercepter != null) {
                    str = wVSchemeIntercepter.dealUrlScheme(str);
                }
                if (isCommonUrl && WebView.getCoreType() == 3) {
                    tryPrcacheDocument(str);
                }
                try {
                    UCNetworkDelegate.getInstance().onUrlChange(this, str);
                    TaoLog.i(TAG, "loadUrl : " + str);
                    super.loadUrl(str);
                } catch (Exception e) {
                    TaoLog.e(TAG, e.getMessage());
                }
            } else {
                String forbiddenDomainRedirectURL = WVDomainConfig.getInstance().getForbiddenDomainRedirectURL();
                if (TextUtils.isEmpty(forbiddenDomainRedirectURL)) {
                    HashMap hashMap2 = new HashMap(2);
                    hashMap2.put("cause", "GET_ACCESS_FORBIDDEN");
                    hashMap2.put("url", str);
                    onMessage(402, hashMap2);
                    return;
                }
                try {
                    super.loadUrl(forbiddenDomainRedirectURL);
                } catch (Exception e2) {
                    TaoLog.e(TAG, e2.getMessage());
                }
            }
        }
    }

    private void tryPrcacheDocument(String str) {
        if (WVUCPrecacheManager.getInstance().canPrecacheDoc(str) && this.webViewClient != null) {
            WebResourceResponse shouldInterceptRequest = this.webViewClient.shouldInterceptRequest((WebView) this, new WebResourceRequest(str, new HashMap()));
            if (shouldInterceptRequest != null) {
                HashMap hashMap = new HashMap();
                hashMap.put(str, shouldInterceptRequest);
                HashMap hashMap2 = new HashMap();
                hashMap2.put("maxAge", "10");
                hashMap2.put("ignoreQuery", "0");
                UCCore.precacheResources(hashMap, hashMap2);
                return;
            }
            WVUCPrecacheManager.getInstance().addPrecacheDoc(str);
        }
    }

    public void refresh() {
        reload();
    }

    public void reload() {
        try {
            HashMap hashMap = new HashMap();
            hashMap.put("userAgent", getUserAgentString());
            WMLPrefetch.getInstance().prefetchData(getCurrentUrl(), hashMap);
        } catch (Throwable th) {
            TaoLog.e(TAG, "failed to call prefetch: " + th.getMessage());
            th.printStackTrace();
        }
        super.reload();
    }

    public String getUrl() {
        return getCurrentUrl();
    }

    public boolean back() {
        if (!canGoBack()) {
            return false;
        }
        goBack();
        return true;
    }

    public void setPageCacheCapacity(int i) {
        if (getUCExtension() != null) {
            getUCExtension().getUCSettings();
            UCSettings.setGlobalIntValue("CachePageNumber", i);
        }
    }

    @SuppressLint({"SetJavaScriptEnabled"})
    private void init() {
        if (!sCoreInitialized.get()) {
            Log.e(TAG, "uc compensation initialization");
            initUCCore();
        }
        if (WVMonitorService.getWvMonitorInterface() != null) {
            WVMonitorService.getWvMonitorInterface().WebViewWrapType(this.context.getClass().getSimpleName());
        }
        this.mIsCoreDestroy = false;
        TaoLog.i(TAG, "uc webview init ");
        setContentDescription(TAG);
        this.mHandler = new Handler(Looper.getMainLooper(), this);
        if (GlobalConfig.getInstance().needSpeed() && !isSWInit) {
            checkSW();
        }
        if (getCoreType() == 3 && EnvUtil.isAppDebug()) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        this.isLive = true;
        WVCommonConfig.getInstance();
        setUseTaobaoNetwork(!mDegradeAliNetwork && WVCommonConfig.commonConfig.ucsdk_alinetwork_rate > Math.random());
        TaoLog.d(TAG, "Webview init setUseTaobaoNetwork =" + getUseTaobaoNetwork());
        WVCommonConfig.getInstance();
        if (!TextUtils.isEmpty(WVCommonConfig.commonConfig.ucCoreUrl)) {
            String str = UC_CORE_URL;
            WVCommonConfig.getInstance();
            if (!str.equals(WVCommonConfig.commonConfig.ucCoreUrl)) {
                Application application = GlobalConfig.context;
                WVCommonConfig.getInstance();
                UCCore.update(application, WVCommonConfig.commonConfig.ucCoreUrl, new Callable<Boolean>() {
                    public Boolean call() throws Exception {
                        return Boolean.valueOf(NetWork.isConnectionInexpensive());
                    }
                });
            }
        }
        try {
            WVCommonConfig.getInstance();
            if (!TextUtils.isEmpty(WVCommonConfig.commonConfig.cookieUrlRule)) {
                pattern = Pattern.compile(WVCommonConfig.commonConfig.cookieUrlRule);
            }
        } catch (Exception e) {
            TaoLog.e(TAG, "Pattern complile Exception" + e.getMessage());
        }
        WVRenderPolicy.disableAccessibility(this.context);
        WebSettings settings = getSettings();
        if (getCurrentViewCoreType() == 2 && Build.VERSION.SDK_INT >= 21 && settings != null) {
            settings.setMixedContentMode(0);
        }
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT < 18) {
            settings.setSavePassword(false);
        }
        settings.setDatabaseEnabled(false);
        settings.setDatabasePath(this.context.getApplicationInfo().dataDir + "/localstorage");
        settings.setGeolocationEnabled(true);
        String appTag = GlobalConfig.getInstance().getAppTag();
        String appVersion = GlobalConfig.getInstance().getAppVersion();
        String userAgentString = settings.getUserAgentString();
        if (userAgentString != null) {
            if (!TextUtils.isEmpty(appTag) && !TextUtils.isEmpty(appVersion)) {
                userAgentString = userAgentString + " AliApp(" + appTag + "/" + appVersion + Operators.BRACKET_END_STR;
            }
            if (!userAgentString.contains("UCBS/") && getCurrentViewCoreType() == 3) {
                userAgentString = userAgentString + " UCBS/2.11.1.1";
            }
            if (!userAgentString.contains("TTID/") && !TextUtils.isEmpty(GlobalConfig.getInstance().getTtid())) {
                userAgentString = userAgentString + " TTID/" + GlobalConfig.getInstance().getTtid();
            }
        }
        settings.setUserAgentString(userAgentString + GlobalConfig.DEFAULT_UA);
        settings.setCacheMode(-1);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        UCCore.setGlobalOption(UCCore.ADAPTER_BUILD_VERSOPM, appVersion);
        if (Build.VERSION.SDK_INT >= 14) {
            settings.setTextZoom(WebSettings.TextSize.NORMAL.value);
        }
        if (mUseAliNetwork && getUCExtension() != null) {
            getUCExtension().getUCSettings();
            UCSettings.setGlobalIntValue(SettingKeys.UCCookieType, 1);
        }
        if (getCurrentViewCoreType() == 1 || getCurrentViewCoreType() == 3) {
            TaoLog.d(TAG, "init  CurrentViewCoreType()= " + getCurrentViewCoreType());
            WVCore.getInstance().setUCSupport(true);
            if (mUseAliNetwork) {
                this.aliRequestAdapter = new AliNetworkAdapter(this.context.getApplicationContext(), bizId);
                UCCore.setThirdNetwork(this.aliRequestAdapter, new AliNetworkDecider());
            }
            if (WVCommonConfig.commonConfig.openLog) {
                UCCore.setNetLogger(new UCLog());
            }
            UCSettings.setGlobalBoolValue(SettingKeys.EnableCustomErrPage, true);
            UCSettings.updateBussinessInfo(1, 1, "u4_focus_auto_popup_input_list", WVCommonConfig.commonConfig.ucParam.u4FocusAutoPopupInputHostList);
            UCSettings.updateBussinessInfo(1, 1, "crwp_embed_surface_embed_view_enable_list", WVCommonConfig.commonConfig.ucParam.cdResourceEmbedSurfaceEmbedViewEnableList);
            UCSettings.updateBussinessInfo(1, 1, "crwp_embed_view_reattach_list", "map");
            UCSettings.updateBussinessInfo(1, 1, "PageTimerCountLimit", WVCommonConfig.commonConfig.ucParam.ucPageTimerCount);
            setPageCacheCapacity(5);
            try {
                String str2 = WVCookieConfig.getInstance().cookieBlackList;
                if (!TextUtils.isEmpty(str2)) {
                    TaoLog.i(TAG, "set cookie black list = " + str2 + " to uc");
                    UCSettings.setGlobalStringValue("CookiesBlacklistForJs", str2);
                }
            } catch (Throwable unused) {
            }
            AppMonitorUtil.commitSuccess(AppMonitorUtil.MONITOR_POINT_WEB_CORE_TYPE, (String) null);
        } else {
            AppMonitorUtil.commitFail(AppMonitorUtil.MONITOR_POINT_WEB_CORE_TYPE, getCoreType(), "", "");
        }
        UCCore.setStatDataCallback(new WVValueCallback());
        setWebViewClient(new WVUCWebViewClient(this.context));
        setWebChromeClient(new WVUCWebChromeClient(this.context));
        UCExtension uCExtension = getUCExtension();
        if (uCExtension != null) {
            uCExtension.setClient(new WVUCClient(this));
        }
        this.wvUIModel = new WVUIModel(this.context, this);
        WVJsBridge.getInstance().init();
        this.entryManager = new WVPluginEntryManager(this.context, this);
        WVAppEvent wVAppEvent = new WVAppEvent();
        wVAppEvent.initialize(this.context, (IWVWebView) this);
        addJsObject("AppEvent", wVAppEvent);
        WVPluginManager.registerPlugin("WVUCBase", (Class<? extends WVApiPlugin>) WVUCBase.class);
        this.wvSecurityFilter = new WVSecurityFilter();
        WVEventService.getInstance().addEventListener(this.wvSecurityFilter, WVEventService.WV_FORWARD_EVENT);
        this.jsPatchListener = new WVJsPatchListener(this);
        WVEventService.getInstance().addEventListener(this.jsPatchListener, WVEventService.WV_BACKWARD_EVENT);
        if (Build.VERSION.SDK_INT > 10 && Build.VERSION.SDK_INT < 17) {
            try {
                removeJavascriptInterface("searchBoxJavaBridge_");
                removeJavascriptInterface("accessibility");
                removeJavascriptInterface("accessibilityTraversal");
            } catch (Throwable th) {
                TaoLog.d(TAG, "removeJavascriptInterface " + th.getMessage());
            }
        }
        this.mLongClickListener = new View.OnLongClickListener() {
            public boolean onLongClick(View view) {
                try {
                    WebView.HitTestResult hitTestResult = WVUCWebView.this.getHitTestResult();
                    if (hitTestResult == null || !WVUCWebView.this.longPressSaveImage) {
                        return false;
                    }
                    if (TaoLog.getLogStatus()) {
                        TaoLog.d(WVUCWebView.TAG, "Long click on WebView, " + hitTestResult.getExtra());
                    }
                    if (hitTestResult.getType() != 8 && hitTestResult.getType() != 5) {
                        return false;
                    }
                    String unused = WVUCWebView.this.mImageUrl = hitTestResult.getExtra();
                    PopupWindowController unused2 = WVUCWebView.this.mPopupController = new PopupWindowController(WVUCWebView.this.context, WVUCWebView.this, WVUCWebView.this.mPopupMenuTags, WVUCWebView.this.popupClickListener);
                    WVUCWebView.this.mPopupController.show();
                    return true;
                } catch (Exception e) {
                    TaoLog.e(WVUCWebView.TAG, "getHitTestResult error:" + e.getMessage());
                    return false;
                }
            }
        };
        setOnLongClickListener(this.mLongClickListener);
        if (WVMonitorService.getPackageMonitorInterface() != null) {
            WVMonitorService.getPerformanceMonitor().didWebViewInitAtTime(System.currentTimeMillis());
        }
        if (Build.VERSION.SDK_INT >= 11 && WVRenderPolicy.shouldDisableHardwareRenderInLayer()) {
            try {
                setLayerType(1, (Paint) null);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        try {
            CookieManager.getInstance().setAcceptCookie(true);
        } catch (Throwable unused2) {
        }
        setAcceptThirdPartyCookies();
        addJavascriptInterface(new WVBridgeEngine(this), "__windvane__");
        injectJsEarly(WVBridgeEngine.WINDVANE_CORE_JS);
    }

    public void addJavascriptInterface(Object obj, String str) {
        if (!"accessibility".equals(str) && !"accessibilityTraversal".equals(str)) {
            super.addJavascriptInterface(obj, str);
        }
    }

    public void evaluateJavascript(String str) {
        evaluateJavascript(str, (ValueCallback<String>) null);
    }

    public void evaluateJavascript(String str, ValueCallback<String> valueCallback) {
        TaoLog.d(TAG, "evaluateJavascript : " + str);
        if (this.isLive) {
            if (str.length() > 10 && "javascript:".equals(str.substring(0, 11).toLowerCase())) {
                str = str.substring(11);
            }
            if (evaluateJavascriptSupported || getCurrentViewCoreType() == 3) {
                try {
                    super.evaluateJavascript(str, valueCallback);
                } catch (NoSuchMethodError unused) {
                    if (getCurrentViewCoreType() != 3) {
                        evaluateJavascriptSupported = false;
                        evaluateJavascript(str, valueCallback);
                    }
                } catch (Exception unused2) {
                    if (getCurrentViewCoreType() != 3) {
                        evaluateJavascriptSupported = false;
                        evaluateJavascript(str, valueCallback);
                        AppMonitorUtil.commitUCWebviewError("2", str, UCCore.EVENT_EXCEPTION);
                    }
                }
            } else if (valueCallback == null) {
                loadUrl("javascript:" + str);
            } else {
                script2NativeCallback(str, valueCallback);
            }
        }
    }

    public void script2NativeCallback(String str, ValueCallback<String> valueCallback) {
        int i = this.mWvNativeCallbackId + 1;
        this.mWvNativeCallbackId = i;
        WVNativeCallbackUtil.putNativeCallbak(String.valueOf(i), valueCallback);
        loadUrl("javascript:console.log('wvNativeCallback/" + i + "/'+function(){var s = " + str + "; return (typeof s === 'object' ? JSON.stringify(s) : typeof s === 'string' ? '\"' + s + '\"' : s);}())");
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
        Context context2 = getCoreView().getContext();
        return context2 instanceof ContextWrapper ? ((ContextWrapper) context2).getBaseContext() : context2;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 15 && this.webChromeClient.mFilePathCallback != null) {
            this.webChromeClient.mFilePathCallback.onReceiveValue(WebChromeClient.FileChooserParams.parseResult(i2, intent));
            this.webChromeClient.mFilePathCallback = null;
        }
        if (this.entryManager != null) {
            this.entryManager.onActivityResult(i, i2, intent);
        }
    }

    public void setWebViewClient(WebViewClient webViewClient2) {
        if (webViewClient2 instanceof WVUCWebViewClient) {
            this.webViewClient = (WVUCWebViewClient) webViewClient2;
            super.setWebViewClient(webViewClient2);
            return;
        }
        throw new WindVaneError("Your WebViewClient must be extended from WVUCWebViewClient");
    }

    public void setWebChromeClient(WebChromeClient webChromeClient2) {
        if (webChromeClient2 instanceof WVUCWebChromeClient) {
            this.webChromeClient = (WVUCWebChromeClient) webChromeClient2;
            this.webChromeClient.mWebView = this;
            super.setWebChromeClient(webChromeClient2);
            return;
        }
        throw new WindVaneError("Your WebChromeClient must be extended from WVUCWebChromeClient");
    }

    /* access modifiers changed from: protected */
    public void onWindowVisibilityChanged(int i) {
        Window window;
        if (i == 0 && Build.VERSION.SDK_INT > 18) {
            Context context2 = getContext();
            if ((context2 instanceof Activity) && (window = ((Activity) context2).getWindow()) != null) {
                final View decorView = window.getDecorView();
                decorView.postDelayed(new Runnable() {
                    public void run() {
                        decorView.requestLayout();
                    }
                }, 100);
            }
        }
        super.onWindowVisibilityChanged(i);
    }

    public void coreDestroy() {
        try {
            if (!this.mIsCoreDestroy) {
                this.mIsCoreDestroy = true;
                TaoLog.e(TAG, "call core destroy");
                InputMethodManager inputMethodManager = (InputMethodManager) this.context.getSystemService("input_method");
                if (inputMethodManager.isActive()) {
                    inputMethodManager.hideSoftInputFromWindow(getApplicationWindowToken(), 0);
                }
                super.setWebViewClient((WebViewClient) null);
                super.setWebChromeClient((WebChromeClient) null);
                this.webViewClient = null;
                this.webChromeClient = null;
                this.context = null;
                WVJsBridge.getInstance().tryToRunTailBridges();
                this.entryManager.onDestroy();
                setOnLongClickListener((View.OnLongClickListener) null);
                this.mLongClickListener = null;
                WVEventService.getInstance().onEvent(3003);
                WVEventService.getInstance().removeEventListener(this.wvSecurityFilter);
                WVEventService.getInstance().removeEventListener(this.jsPatchListener);
                removeAllViews();
                if (JsbridgeHis != null) {
                    JsbridgeHis.clear();
                }
                this.isLive = false;
                if (getParent() != null && (getParent() instanceof ViewGroup)) {
                    ((ViewGroup) getParent()).removeView(this);
                }
            }
        } catch (Throwable th) {
            TaoLog.i(TAG, "WVUCWebView::coreDestroy Exception:" + th.getMessage());
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

    public void OnScrollChanged(int i, int i2, int i3, int i4) {
        if (this.entryManager != null) {
            this.entryManager.onScrollChanged(i, i2, i3, i4);
        }
        try {
            super.onScrollChanged(i, i2, i3, i4);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void coreOnScrollChanged(int i, int i2, int i3, int i4) {
        OnScrollChanged(i, i2, i3, i4);
        super.coreOnScrollChanged(i, i2, i3, i4);
    }

    @TargetApi(11)
    public void onResume() {
        if (this.entryManager != null) {
            this.entryManager.onResume();
        }
        if (Build.VERSION.SDK_INT >= 11) {
            try {
                super.onResume();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
        WVEventService.getInstance().onEvent(3002, this, getUrl(), new Object[0]);
        this.isLive = true;
    }

    public String getCurrentUrl() {
        String str;
        try {
            str = super.getUrl();
        } catch (Exception unused) {
            TaoLog.w(TAG, "WebView had destroyed,forbid to be called getUrl. currentUrl : " + this.currentUrl);
            str = null;
        }
        if (str == null) {
            TaoLog.v(TAG, "getUrl by currentUrl: " + this.currentUrl);
            return this.currentUrl;
        }
        TaoLog.v(TAG, "getUrl by webview: " + str);
        return str;
    }

    public void setCurrentUrl(String str, String str2) {
        this.currentUrl = str;
        TaoLog.v(TAG, "setCurrentUrl: " + str + " state : " + str2);
    }

    public String getDataOnActive() {
        return this.dataOnActive;
    }

    public void setDataOnActive(String str) {
        this.dataOnActive = str;
    }

    public void fireEvent(String str) {
        getWVCallBackContext().fireEvent(str, "{}");
    }

    public void fireEvent(String str, String str2) {
        getWVCallBackContext().fireEvent(str, str2);
    }

    public WVCallBackContext getWVCallBackContext() {
        return new WVCallBackContext(this);
    }

    public View getView() {
        return super.getCoreView();
    }

    public WVUIModel getWvUIModel() {
        return this.wvUIModel;
    }

    public void setWvUIModel(WVUIModel wVUIModel) {
        this.wvUIModel = wVUIModel;
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

    public void stopLoading() {
        isStop = true;
        super.stopLoading();
    }

    public void clearCache() {
        super.clearCache(true);
    }

    public void setSupportDownload(boolean z) {
        this.supportDownload = z;
    }

    public String getUserAgentString() {
        return getSettings().getUserAgentString();
    }

    public void setUserAgentString(String str) {
        getSettings().setUserAgentString(str);
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
                try {
                    Toast.makeText(this.context, EnvUtil.isCN() ? "图片保存到相册成功" : "Success to save picture", 1).show();
                } catch (Exception e) {
                    TaoLog.e(TAG, "NOTIFY_SAVE_IMAGE_SUCCESS fail " + e.getMessage());
                }
                return true;
            case 405:
                try {
                    Toast.makeText(this.context, EnvUtil.isCN() ? "图片保存到相册失败" : "Failed to save picture", 1).show();
                } catch (Exception e2) {
                    TaoLog.e(TAG, "NOTIFY_SAVE_IMAGE_FAIL fail " + e2.getMessage());
                }
                return true;
            default:
                return false;
        }
    }

    public boolean canGoBack() {
        if (WVEventService.getInstance().onEvent(3004).isSuccess) {
            return false;
        }
        return super.canGoBack();
    }

    private void setAcceptThirdPartyCookies() {
        if (Build.VERSION.SDK_INT >= 21 && getCoreType() != 1 && getCoreType() != 3) {
            try {
                View view = getView();
                if (view != null && (view instanceof android.webkit.WebView)) {
                    CookieManager.getInstance().setAcceptThirdPartyCookies((android.webkit.WebView) view, true);
                }
            } catch (Throwable unused) {
            }
        }
    }

    public boolean coreDispatchTouchEvent(MotionEvent motionEvent) {
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
                    super.coreDispatchTouchEvent(motionEvent2);
                    motionEvent2.recycle();
                    this.mEventSparseArray.remove(pointerId);
                }
            } else {
                this.isUser = true;
                return true;
            }
        }
        return super.coreDispatchTouchEvent(motionEvent);
    }

    private static final class ExceptionValueCallback implements ValueCallback<SetupTask> {
        private ExceptionValueCallback() {
        }

        public void onReceiveValue(SetupTask setupTask) {
            WVUCWebView.shouldUCLibInit.set(false);
            WVUCWebView.sCoreInitialized.set(false);
            try {
                if (setupTask.getException() != null) {
                    StringWriter stringWriter = new StringWriter();
                    setupTask.getException().printStackTrace(new PrintWriter(stringWriter));
                    TaoLog.e("UCCore", "UC ExceptionValueCallback : " + stringWriter.toString());
                }
                if (WVCore.getInstance().getCoreDownLoadBack() != null) {
                    WVCore.getInstance().getCoreDownLoadBack().initError();
                }
            } catch (Throwable th) {
                TaoLog.e(WVUCWebView.TAG, "UC ExceptionValueCallback Throwable : " + th.getMessage());
            }
        }
    }

    private static final class DownLoadCallback implements ValueCallback<SetupTask> {
        private DownLoadCallback() {
        }

        public void onReceiveValue(SetupTask setupTask) {
            int percent = setupTask.getPercent();
            if (WVCore.getInstance().getCoreDownLoadBack() != null) {
                WVCore.getInstance().getCoreDownLoadBack().progress(percent);
            }
            TaoLog.i("UCCore", "download progress:[" + percent + "]%");
        }
    }

    public static boolean isNeedDownLoad() {
        return needDownLoad;
    }

    private static final class CorePreparedCallback implements ValueCallback<SetupTask> {
        long startTime = 0;

        CorePreparedCallback(long j) {
            this.startTime = j;
        }

        public void onReceiveValue(SetupTask setupTask) {
            if (GlobalConfig.context != null) {
                WVUCWebView.onUCMCoreSwitched(GlobalConfig.context, this.startTime);
            }
        }
    }

    private static final class SwitchValueCallback implements ValueCallback<SetupTask> {
        private SwitchValueCallback() {
        }

        public void onReceiveValue(SetupTask setupTask) {
            WVThreadPool.getInstance().execute(new Runnable() {
                public void run() {
                    if (WVUCWebView.coreEventCallback != null) {
                        WVCoreSettings.getInstance().setCoreEventCallback(WVUCWebView.coreEventCallback);
                    }
                    if (WVCoreSettings.getInstance().coreEventCallbacks != null) {
                        for (CoreEventCallback onCoreSwitch : WVCoreSettings.getInstance().coreEventCallbacks) {
                            onCoreSwitch.onCoreSwitch();
                        }
                    }
                    WVEventService.getInstance().onEvent(3017);
                    if (!WVCore.getInstance().isUCSupport() && WebView.getCoreType() == 3) {
                        WVEventService.getInstance().onEvent(3016);
                        TaoLog.i(WVUCWebView.TAG, "UCSDK onUCMCoreSwitched: " + WebView.getCoreType());
                        WVCore.getInstance().setUCSupport(true);
                        if (WVCoreSettings.getInstance().coreEventCallbacks != null) {
                            for (CoreEventCallback onUCCorePrepared : WVCoreSettings.getInstance().coreEventCallbacks) {
                                onUCCorePrepared.onUCCorePrepared();
                            }
                        }
                        if (!GlobalConfig.getInstance().needSpeed() && !WVUCWebView.isSWInit) {
                            WVUCWebView.checkSW();
                        }
                    } else if (WebView.getCoreType() == 2) {
                        WVCore.getInstance().setUCSupport(false);
                    }
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public static void checkSW() {
        try {
            TaoLog.d(TAG, "start to set ServiceWorker client");
            ServiceWorkerController instance = ServiceWorkerController.getInstance();
            if (instance != null) {
                instance.setServiceWorkerClient(new WVUCServiceWorkerClient());
            }
            isSWInit = true;
        } catch (Throwable unused) {
            isSWInit = false;
            TaoLog.w(TAG, "failed to set ServiceWorker client");
        }
    }

    public void insertH5MonitorData(String str, String str2, String str3) {
        if (!TextUtils.isEmpty(str)) {
            if (this.mH5MonitorCache == null) {
                this.mH5MonitorCache = new Hashtable<>();
            }
            Hashtable hashtable = this.mH5MonitorCache.get(str);
            if (hashtable == null) {
                hashtable = new Hashtable();
                this.mH5MonitorCache.put(str, hashtable);
            }
            if (TextUtils.isEmpty(str3)) {
                str3 = "";
            }
            hashtable.put(str2, str3);
        }
    }

    public String getH5MonitorData(String str, String str2) {
        Hashtable hashtable;
        if (this.mH5MonitorCache == null || (hashtable = this.mH5MonitorCache.get(str)) == null) {
            return null;
        }
        return (String) hashtable.get(str2);
    }

    public JSONObject getH5MonitorDatas() throws JSONException {
        if (this.mH5MonitorCache == null) {
            return new JSONObject();
        }
        JSONArray jSONArray = new JSONArray();
        for (String str : this.mH5MonitorCache.keySet()) {
            Hashtable hashtable = this.mH5MonitorCache.get(str);
            JSONObject jSONObject = new JSONObject();
            Enumeration keys = hashtable.keys();
            while (keys.hasMoreElements()) {
                String str2 = (String) keys.nextElement();
                jSONObject.put(str2, hashtable.get(str2));
            }
            jSONArray.put(jSONObject);
        }
        JSONObject jSONObject2 = new JSONObject();
        jSONObject2.put("resources", jSONArray);
        return jSONObject2;
    }

    public void clearH5MonitorData() {
        if (this.mH5MonitorCache != null) {
            this.mH5MonitorCache.clear();
        }
    }

    public boolean containsH5MonitorData(String str) {
        if (this.mH5MonitorCache == null) {
            return false;
        }
        return this.mH5MonitorCache.containsKey(str);
    }

    @TargetApi(19)
    public boolean _post(Runnable runnable) {
        if (isAttachedToWindow() || Build.VERSION.SDK_INT >= 24) {
            return post(runnable);
        }
        TaoLog.d(TAG, " wait webview attach to window");
        taskQueue.add(runnable);
        return true;
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        TaoLog.d(TAG, " webview attach to window, and execute remain task");
        for (Runnable run : taskQueue) {
            run.run();
        }
        taskQueue.clear();
        super.onAttachedToWindow();
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        UCNetworkDelegate.getInstance().removeWebview(this);
        if (taskQueue.size() != 0) {
            taskQueue.clear();
        }
        if (this.mIsCoreDestroy && !this.isRealDestroyed) {
            TaoLog.i(TAG, "destroy real core");
            try {
                if (getCoreType() != 2) {
                    if (Build.VERSION.SDK_INT >= 19 || !WVUCUtils.isArchContains("x86")) {
                        super.coreDestroy();
                        super.destroy();
                        this.isRealDestroyed = true;
                    }
                }
                getSettings().setJavaScriptEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        WVUCWebView.super.coreDestroy();
                    }
                }, 50);
            } catch (Throwable unused) {
            }
            this.isRealDestroyed = true;
        }
    }

    public void destroy() {
        if (!isDestroied()) {
            try {
                if (getCoreType() != 2) {
                    if (Build.VERSION.SDK_INT >= 19 || !WVUCUtils.isArchContains("x86")) {
                        super.coreDestroy();
                        super.destroy();
                        this.isRealDestroyed = true;
                    }
                }
                getSettings().setJavaScriptEnabled(false);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        WVUCWebView.super.coreDestroy();
                    }
                }, 50);
            } catch (Throwable unused) {
            }
            this.isRealDestroyed = true;
        }
    }

    /* access modifiers changed from: protected */
    public void setOnErrorTime(long j) {
        this.onErrorTime = j;
    }

    private static void setupUCParam(String str) {
        UCParamData uCParamData = WVCommonConfig.commonConfig.ucParam;
        if (!WVCommonConfig.commonConfig.enableUcShareCore || uCParamData == null || (!UCParamData.hostApp() && !UCParamData.needLoadNeedShareCoreApp())) {
            TaoLog.w(TAG, "not taobao, or shared core disabled by config, or uc param is empty.");
            return;
        }
        try {
            JSONObject jSONObject = new JSONObject();
            if (UCParamData.hostApp() && TextUtils.isEmpty(uCParamData.scLoadPolicyCd) && !NetWork.isConnectionInexpensive()) {
                uCParamData.scLoadPolicyCd = CDParamKeys.CD_VALUE_LOAD_POLICY_SHARE_CORE;
                uCParamData.scWaitMilts = "1";
            }
            if (!TextUtils.isEmpty(uCParamData.scWaitMilts)) {
                jSONObject.put(CDParamKeys.CD_KEY_SHARE_CORE_HOST_UPD_SETUP_TASK_WAIT_MILIS, uCParamData.scWaitMilts);
            }
            if (UCParamData.hostApp() && uCParamData.validShareCoreToSdcardParams()) {
                jSONObject.put(CDParamKeys.CD_KEY_SHARE_CORE_COMMONALITY_TARGET_FPATH, uCParamData.sdCopyPathCd);
                jSONObject.put(CDParamKeys.CD_KEY_SHARE_CORE_HOST_COPY_SDCARD, uCParamData.scCopyToSdcardCd);
                jSONObject.put(CDParamKeys.CD_KEY_SHARE_CORE_HOST_PUSH_UCM_VERSIONS, uCParamData.hostUcmVersionsCd);
                jSONObject.put(CDParamKeys.CD_KEY_SHARE_CORE_HOST_UPDATE_STILL, uCParamData.scStillUpd);
                if (!TextUtils.isEmpty(str)) {
                    jSONObject.put(CDParamKeys.CD_KEY_SHARE_CORE_HOST_COMPRESSED_CORE_FILE_PATH, str);
                }
            }
            if (uCParamData.validShareCoreFromSdcardParams()) {
                jSONObject.put(CDParamKeys.CD_KEY_SHARE_CORE_COMMONALITY_TARGET_FPATH, uCParamData.sdCopyPathCd);
                jSONObject.put(CDParamKeys.CD_KEY_SHARE_CORE_CLIENT_UCM_VERSIONS, uCParamData.thirtyUcmVersionsCd);
                jSONObject.put(CDParamKeys.CD_KEY_SHARE_CORE_CLIENT_LOAD_POLICY, uCParamData.scLoadPolicyCd);
                jSONObject.put(CDParamKeys.CD_KEY_SHARE_CORE_CLIENT_SPECIAL_HOST_PKG_NAME_LIST, uCParamData.scPkgNames);
            }
            String str2 = "JSON_CMD" + jSONObject.toString();
            TaoLog.d(TAG, str2);
            UCCore.setParam(str2);
        } catch (Throwable th) {
            TaoLog.w(TAG, "failed to setup uc param", th, new Object[0]);
        }
    }

    public static void setBizCode(String str) {
        bizId = str;
    }

    public static class WVValueCallback implements ValueCallback<String> {
        public void onReceiveValue(String str) {
            TaoLog.i(WVUCWebView.TAG, "support : " + WVCore.getInstance().isUCSupport() + " UC SDK Callback : " + str);
            try {
                UserTrackUtil.commitEvent(UserTrackUtil.EVENTID_PA_UCSDK, String.valueOf(WVCore.getInstance().isUCSupport()), String.valueOf(WVUCWebView.getUseTaobaoNetwork()), str);
            } catch (Throwable th) {
                TaoLog.e(WVUCWebView.TAG, "UC commitEvent failed : " + th.getMessage());
            }
        }
    }

    protected static final class DownloadEnv implements Callable<Boolean> {
        Context context;

        DownloadEnv(Context context2) {
            this.context = context2;
        }

        /* JADX WARNING: Removed duplicated region for block: B:13:0x0041  */
        /* JADX WARNING: Removed duplicated region for block: B:14:0x0057  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Boolean call() throws java.lang.Exception {
            /*
                r5 = this;
                android.taobao.windvane.extra.core.WVCore r0 = android.taobao.windvane.extra.core.WVCore.getInstance()
                boolean r0 = r0.isOpen4GDownload()
                r1 = 1
                r2 = 0
                if (r0 == 0) goto L_0x0034
                android.content.Context r0 = r5.context
                android.content.Context r0 = r0.getApplicationContext()
                java.util.Map r0 = android.taobao.windvane.util.NetWork.getNetWorkSubTypeMap(r0)
                java.lang.String r3 = "type"
                java.lang.Object r3 = r0.get(r3)
                java.lang.CharSequence r3 = (java.lang.CharSequence) r3
                boolean r3 = android.text.TextUtils.isEmpty(r3)
                if (r3 != 0) goto L_0x0034
                java.lang.String r3 = "4G"
                java.lang.String r4 = "type"
                java.lang.Object r0 = r0.get(r4)
                boolean r0 = r3.equals(r0)
                if (r0 == 0) goto L_0x0034
                r0 = 1
                goto L_0x0035
            L_0x0034:
                r0 = 0
            L_0x0035:
                if (r0 != 0) goto L_0x003f
                boolean r0 = android.taobao.windvane.util.NetWork.isConnectionInexpensive()
                if (r0 == 0) goto L_0x003e
                goto L_0x003f
            L_0x003e:
                r1 = 0
            L_0x003f:
                if (r1 != 0) goto L_0x0057
                java.util.concurrent.atomic.AtomicBoolean r0 = android.taobao.windvane.extra.uc.WVUCWebView.sCoreInitialized
                r0.set(r2)
                java.util.concurrent.atomic.AtomicBoolean r0 = android.taobao.windvane.extra.uc.WVUCWebView.shouldUCLibInit
                r0.set(r2)
                java.lang.String r0 = "UCCore"
                java.lang.String r2 = "current env cannot download u4 core"
                android.taobao.windvane.util.TaoLog.e(r0, r2)
                goto L_0x007a
            L_0x0057:
                java.lang.String r0 = "UCCore"
                java.lang.StringBuilder r2 = new java.lang.StringBuilder
                r2.<init>()
                java.lang.String r3 = "start download u4 core,is4G=["
                r2.append(r3)
                android.taobao.windvane.extra.core.WVCore r3 = android.taobao.windvane.extra.core.WVCore.getInstance()
                boolean r3 = r3.isOpen4GDownload()
                r2.append(r3)
                java.lang.String r3 = "]"
                r2.append(r3)
                java.lang.String r2 = r2.toString()
                android.taobao.windvane.util.TaoLog.i(r0, r2)
            L_0x007a:
                java.lang.Boolean r0 = java.lang.Boolean.valueOf(r1)
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: android.taobao.windvane.extra.uc.WVUCWebView.DownloadEnv.call():java.lang.Boolean");
        }
    }

    protected class WVDownLoadListener implements DownloadListener {
        protected WVDownLoadListener() {
        }

        public void onDownloadStart(String str, String str2, String str3, String str4, long j) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(WVUCWebView.TAG, "Download start, url: " + str + " contentDisposition: " + str3 + " mimetype: " + str4 + " contentLength: " + j);
            }
            if (!WVUCWebView.this.supportDownload) {
                TaoLog.w(WVUCWebView.TAG, "DownloadListener is not support for webview.");
                return;
            }
            Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(str));
            intent.setFlags(268435456);
            try {
                WVUCWebView.this.context.startActivity(intent);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(WVUCWebView.this.context, EnvUtil.isCN() ? "对不起，您的设备找不到相应的程序" : "Can not find the corresponding application", 1).show();
                TaoLog.e(WVUCWebView.TAG, "DownloadListener not found activity to open this url." + e.getMessage());
            }
        }
    }

    public void injectJsEarly(String str) {
        if (str.startsWith("javascript:")) {
            str = str.replace("javascript:", "");
        }
        StringBuilder sb = this.injectJs;
        sb.append(str);
        sb.append(";\n");
        if (getUCExtension() != null) {
            getUCExtension().setInjectJSProvider(new UCExtension.InjectJSProvider() {
                public String getJS(int i) {
                    return WVUCWebView.this.injectJs.toString();
                }
            }, 1);
        }
    }

    public void isPageEmpty(final whiteScreenCallback whitescreencallback) {
        evaluateJavascript("(function(d){var filteredTagNames={'IFRAME':1,'STYLE':1,'HTML':1,'BODY':1,'HEAD':1,'SCRIPT':1,'TITLE':1};if(d.body){for(var nodes=d.body.childNodes,i=0;i<nodes.length;i++){var node=nodes[i];if(node!=undefined){if(node.nodeType==1&&filteredTagNames[node.tagName]!=1&&node.style.display!='none'){return'0'}else if(node.nodeType==3&&node.nodeValue.trim().length>0){return'0'}}}}return'1'}(document))", new ValueCallback<String>() {
            public void onReceiveValue(String str) {
                if (whitescreencallback != null) {
                    whitescreencallback.isPageEmpty(str);
                }
            }
        });
    }
}
