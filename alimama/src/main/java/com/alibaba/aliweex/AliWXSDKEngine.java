package com.alibaba.aliweex;

import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.packageapp.WVPackageAppRuntime;
import android.taobao.windvane.packageapp.zipapp.ZCacheResourceResponse;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.text.TextUtils;
import com.alibaba.aliweex.adapter.WXCrashReportListener;
import com.alibaba.aliweex.adapter.adapter.AtlasBundleClassLoaderAdapter;
import com.alibaba.aliweex.adapter.adapter.PhenixBasedDrawableLoader;
import com.alibaba.aliweex.adapter.adapter.WXAPMGeneratorAdapter;
import com.alibaba.aliweex.adapter.adapter.WXAliFoldDeviceAdapter;
import com.alibaba.aliweex.adapter.adapter.WXConfigAdapter;
import com.alibaba.aliweex.adapter.adapter.WXExceptionAdapter;
import com.alibaba.aliweex.adapter.adapter.WXHttpAdapter;
import com.alibaba.aliweex.adapter.adapter.WXImgLoaderAdapter;
import com.alibaba.aliweex.adapter.adapter.WXJsFileLoaderAdapter;
import com.alibaba.aliweex.adapter.adapter.WXJscProcessManager;
import com.alibaba.aliweex.adapter.adapter.WXSoLoaderAdapter;
import com.alibaba.aliweex.adapter.adapter.WXUserTrackAdapter;
import com.alibaba.aliweex.adapter.adapter.WXWebSocketProvider;
import com.alibaba.aliweex.plugin.WMMtopPrefetch;
import com.alibaba.aliweex.utils.WXInitConfigManager;
import com.alibaba.aliweex.utils.WXUtil;
import com.alibaba.motu.crashreporter.IUTCrashCaughtListener;
import com.alibaba.motu.crashreporter.MotuCrashReporter;
import com.taobao.soloader.SoLoader;
import com.taobao.weaver.prefetch.WMLPrefetch;
import com.taobao.weex.InitConfig;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKEngine;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.adapter.ICrashInfoReporter;
import com.taobao.weex.adapter.IWXConfigAdapter;
import com.taobao.weex.bridge.ModuleFactory;
import com.taobao.weex.bridge.WXBridgeManager;
import com.taobao.weex.common.Constants;
import com.taobao.weex.common.WXException;
import com.taobao.weex.ui.IFComponentHolder;
import com.taobao.weex.ui.config.ConfigComponentHolder;
import com.taobao.weex.ui.config.ConfigModuleFactory;
import com.taobao.weex.utils.WXFileUtils;
import com.taobao.weex.utils.WXLogUtils;
import com.uc.webview.export.media.CommandID;
import java.util.HashMap;

public class AliWXSDKEngine {
    private static final String CONFIG_GROUP = "AliWXSDKEngine";
    public static String FRAMEWORK_JS_RAX_API_URL = ("http://h5.m.taobao.com/app/weex/" + WXEnvironment.WXSDK_VERSION + "/weex-rax-api.js");
    public static String FRAMEWORK_JS_URL = ("http://h5.m.taobao.com/app/weex/" + WXEnvironment.WXSDK_VERSION + "/weex.js");
    public static String RAX_FRAMEWORK_JS_URL = "http://h5.m.taobao.com/app/rax/rax.js";
    public static String RAX_FRAMEWORK_PKG_JS_URL = "https://g.alicdn.com/rax-pkg/jsservice/raxpkg.js";
    private static final String TAG = "weex.TBWXSDKEngine";
    public static String WX_AIR_ENV = "x-air-env";
    public static String WX_AIR_GERY = "x-air-grey";
    public static String WX_AIR_TAG = "wxAirTag";
    private static String WX_PROCESS_SWITCH = "weex_single_or_multi";
    /* access modifiers changed from: private */
    public static WXCrashReportListener mWXCrashReportListener;

    /* JADX WARNING: Can't wrap try/catch for region: R(14:0|(1:2)(1:3)|4|5|6|7|8|11|12|(1:17)(1:16)|18|19|22|23) */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0073, code lost:
        r0.printStackTrace();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x00b3, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x00b4, code lost:
        r0.printStackTrace();
        r0 = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0072, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Failed to process nested try/catch */
    /* JADX WARNING: Missing exception handler attribute for start block: B:18:0x00a7 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x006b */
    /* JADX WARNING: Removed duplicated region for block: B:16:0x0093 A[Catch:{ Throwable -> 0x00a7 }] */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x009b A[Catch:{ Throwable -> 0x00a7 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void initSDKEngine() {
        /*
            java.lang.String r0 = "[AliWXSDKEngine] initSDKEngine"
            com.taobao.weex.utils.WXLogUtils.d(r0)
            r0 = 1
            com.taobao.weex.WXEnvironment.sInAliWeex = r0
            com.alibaba.aliweex.utils.MemoryMonitor.listenMemory()
            com.alibaba.aliweex.utils.WXInitConfigManager r1 = com.alibaba.aliweex.utils.WXInitConfigManager.getInstance()
            r1.initConfigSettings()
            updateGlobalConfig()
            java.lang.String r1 = "appGroup"
            java.lang.String r2 = "AliApp"
            com.taobao.weex.WXSDKEngine.addCustomOptions(r1, r2)
            java.lang.String r1 = "AliWeexVersion"
            java.lang.String r2 = "0.0.26.66"
            com.taobao.weex.WXSDKEngine.addCustomOptions(r1, r2)
            java.lang.String r1 = "infoCollect"
            java.lang.String r2 = "false"
            com.taobao.weex.WXSDKEngine.addCustomOptions(r1, r2)
            java.lang.String r1 = "env_exclude_x86"
            java.lang.String r2 = java.lang.String.valueOf(r0)
            com.taobao.weex.WXSDKEngine.addCustomOptions(r1, r2)
            java.lang.String r1 = "isTabletDevice"
            boolean r2 = com.taobao.weex.utils.WXUtils.isTabletDevice()
            java.lang.String r2 = java.lang.String.valueOf(r2)
            com.taobao.weex.WXSDKEngine.addCustomOptions(r1, r2)
            com.alibaba.aliweex.AliWeex r1 = com.alibaba.aliweex.AliWeex.getInstance()
            android.app.Application r1 = r1.getApplication()
            boolean r1 = com.taobao.weex.utils.WXDeviceUtils.isAutoResize(r1)
            r2 = 0
            if (r1 == 0) goto L_0x0052
            com.taobao.weex.WXEnvironment.SETTING_FORCE_VERTICAL_SCREEN = r2
            goto L_0x0054
        L_0x0052:
            com.taobao.weex.WXEnvironment.SETTING_FORCE_VERTICAL_SCREEN = r0
        L_0x0054:
            initFramework()
            registerModulesAndComponents()
            android.taobao.windvane.jsbridge.WVJsBridge r0 = android.taobao.windvane.jsbridge.WVJsBridge.getInstance()     // Catch:{ Throwable -> 0x006b }
            r0.init()     // Catch:{ Throwable -> 0x006b }
            com.taobao.mtop.MtopWVPluginRegister.register()     // Catch:{ Throwable -> 0x006b }
            java.lang.String r0 = "WXAudioPlayer"
            java.lang.Class<com.alibaba.aliweex.plugin.SimpleAudioPlayer> r1 = com.alibaba.aliweex.plugin.SimpleAudioPlayer.class
            android.taobao.windvane.jsbridge.WVPluginManager.registerPlugin((java.lang.String) r0, (java.lang.Class<? extends android.taobao.windvane.jsbridge.WVApiPlugin>) r1)     // Catch:{ Throwable -> 0x006b }
        L_0x006b:
            loadRaxFramework()     // Catch:{ Throwable -> 0x0072 }
            loadRaxPkgFrameWork()     // Catch:{ Throwable -> 0x0072 }
            goto L_0x0076
        L_0x0072:
            r0 = move-exception
            r0.printStackTrace()
        L_0x0076:
            com.alibaba.aliweex.AliWXSDKEngine$1 r0 = new com.alibaba.aliweex.AliWXSDKEngine$1     // Catch:{ Throwable -> 0x00a7 }
            r0.<init>()     // Catch:{ Throwable -> 0x00a7 }
            java.lang.Runnable r0 = com.taobao.weex.common.WXThread.secure((java.lang.Runnable) r0)     // Catch:{ Throwable -> 0x00a7 }
            com.alibaba.aliweex.AliWeex r1 = com.alibaba.aliweex.AliWeex.getInstance()     // Catch:{ Throwable -> 0x00a7 }
            com.alibaba.aliweex.IConfigAdapter r1 = r1.getConfigAdapter()     // Catch:{ Throwable -> 0x00a7 }
            r3 = 3000(0xbb8, double:1.482E-320)
            if (r1 == 0) goto L_0x009b
            java.lang.String r5 = "mainBlock"
            boolean r1 = r1.checkMode(r5)     // Catch:{ Throwable -> 0x00a7 }
            if (r1 == 0) goto L_0x009b
            com.taobao.weex.bridge.WXBridgeManager r1 = com.taobao.weex.bridge.WXBridgeManager.getInstance()     // Catch:{ Throwable -> 0x00a7 }
            r1.postDelay(r0, r3)     // Catch:{ Throwable -> 0x00a7 }
            goto L_0x00a7
        L_0x009b:
            android.os.Handler r1 = new android.os.Handler     // Catch:{ Throwable -> 0x00a7 }
            android.os.Looper r5 = android.os.Looper.getMainLooper()     // Catch:{ Throwable -> 0x00a7 }
            r1.<init>(r5)     // Catch:{ Throwable -> 0x00a7 }
            r1.postDelayed(r0, r3)     // Catch:{ Throwable -> 0x00a7 }
        L_0x00a7:
            java.io.File r0 = new java.io.File     // Catch:{ Throwable -> 0x00b3 }
            java.lang.String r1 = "/data/local/tmp/.apm_online"
            r0.<init>(r1)     // Catch:{ Throwable -> 0x00b3 }
            boolean r0 = r0.exists()     // Catch:{ Throwable -> 0x00b3 }
            goto L_0x00b8
        L_0x00b3:
            r0 = move-exception
            r0.printStackTrace()
            r0 = 0
        L_0x00b8:
            com.alibaba.aliweex.adapter.adapter.WXAPMAdapter.showPerformanceInRelease = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.AliWXSDKEngine.initSDKEngine():void");
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(11:1|2|3|4|9|10|11|12|13|14|18) */
    /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|9|10|11|12|13|14|18) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0133 */
    /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x0136 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void registerModulesAndComponents() {
        /*
            java.lang.String r0 = "windvane"
            java.lang.Class<com.alibaba.aliweex.adapter.module.WXWindVaneModule> r1 = com.alibaba.aliweex.adapter.module.WXWindVaneModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "mtop"
            java.lang.Class<com.alibaba.aliweex.adapter.module.mtop.WXMtopModule> r1 = com.alibaba.aliweex.adapter.module.mtop.WXMtopModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "userTrack"
            java.lang.Class<com.alibaba.aliweex.adapter.module.WXUserTrackModule> r1 = com.alibaba.aliweex.adapter.module.WXUserTrackModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "share"
            java.lang.Class<com.alibaba.aliweex.adapter.module.WXShareModule> r1 = com.alibaba.aliweex.adapter.module.WXShareModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "user"
            java.lang.Class<com.alibaba.aliweex.adapter.module.WXUserModule> r1 = com.alibaba.aliweex.adapter.module.WXUserModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "geolocation"
            java.lang.Class<com.alibaba.aliweex.adapter.module.GeolocationModule> r1 = com.alibaba.aliweex.adapter.module.GeolocationModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "event"
            java.lang.Class<com.alibaba.aliweex.adapter.module.WXEventModule> r1 = com.alibaba.aliweex.adapter.module.WXEventModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "pageInfo"
            java.lang.Class<com.alibaba.aliweex.adapter.module.WXPageInfoModule> r1 = com.alibaba.aliweex.adapter.module.WXPageInfoModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "location"
            java.lang.Class<com.alibaba.aliweex.adapter.module.WXLocationModule> r1 = com.alibaba.aliweex.adapter.module.WXLocationModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "alipay"
            java.lang.Class<com.alibaba.aliweex.adapter.module.WXAliPayModule> r1 = com.alibaba.aliweex.adapter.module.WXAliPayModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "navigationBar"
            java.lang.Class<com.alibaba.aliweex.adapter.module.WXNavigationBarModule> r1 = com.alibaba.aliweex.adapter.module.WXNavigationBarModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "audio"
            java.lang.Class<com.alibaba.aliweex.adapter.module.audio.WXAudioModule> r1 = com.alibaba.aliweex.adapter.module.audio.WXAudioModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "connection"
            java.lang.Class<com.alibaba.aliweex.adapter.module.net.WXConnectionModule> r1 = com.alibaba.aliweex.adapter.module.net.WXConnectionModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "festival"
            java.lang.Class<com.alibaba.aliweex.adapter.module.WXFestivalModule> r1 = com.alibaba.aliweex.adapter.module.WXFestivalModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "cookie"
            java.lang.Class<com.alibaba.aliweex.adapter.module.WXCookieModule> r1 = com.alibaba.aliweex.adapter.module.WXCookieModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "blurEx"
            java.lang.Class<com.alibaba.aliweex.adapter.module.blur.WXBlurEXModule> r1 = com.alibaba.aliweex.adapter.module.blur.WXBlurEXModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "screen"
            java.lang.Class<com.alibaba.aliweex.adapter.module.WXScreenModule> r1 = com.alibaba.aliweex.adapter.module.WXScreenModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "calendar"
            java.lang.Class<com.alibaba.aliweex.adapter.module.WXCalendarModule> r1 = com.alibaba.aliweex.adapter.module.WXCalendarModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "navigator"
            java.lang.Class<com.alibaba.aliweex.adapter.module.AliWXNavigatorModule> r1 = com.alibaba.aliweex.adapter.module.AliWXNavigatorModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "navigationBar"
            java.lang.Class<com.alibaba.aliweex.adapter.module.WXNavigationBarModule> r1 = com.alibaba.aliweex.adapter.module.WXNavigationBarModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "location"
            java.lang.Class<com.alibaba.aliweex.adapter.module.AliWXLocationModule> r1 = com.alibaba.aliweex.adapter.module.AliWXLocationModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "wxapm"
            java.lang.Class<com.alibaba.aliweex.adapter.module.WXPerformanceModule> r1 = com.alibaba.aliweex.adapter.module.WXPerformanceModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "orange"
            java.lang.Class<com.alibaba.aliweex.adapter.module.WXConfigModule> r1 = com.alibaba.aliweex.adapter.module.WXConfigModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "web"
            java.lang.Class<com.alibaba.aliweex.adapter.component.WXWVWeb> r1 = com.alibaba.aliweex.adapter.component.WXWVWeb.class
            com.taobao.weex.WXSDKEngine.registerComponent((java.lang.String) r0, (java.lang.Class<? extends com.taobao.weex.ui.component.WXComponent>) r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "latestVisitView"
            java.lang.Class<com.alibaba.aliweex.adapter.component.WXLatestVisitView> r1 = com.alibaba.aliweex.adapter.component.WXLatestVisitView.class
            com.taobao.weex.WXSDKEngine.registerComponent((java.lang.String) r0, (java.lang.Class<? extends com.taobao.weex.ui.component.WXComponent>) r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "marquee"
            java.lang.Class<com.alibaba.aliweex.adapter.component.WXMarquee> r1 = com.alibaba.aliweex.adapter.component.WXMarquee.class
            com.taobao.weex.WXSDKEngine.registerComponent((java.lang.String) r0, (java.lang.Class<? extends com.taobao.weex.ui.component.WXComponent>) r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "countdown"
            java.lang.Class<com.alibaba.aliweex.adapter.component.WXCountDown> r1 = com.alibaba.aliweex.adapter.component.WXCountDown.class
            com.taobao.weex.WXSDKEngine.registerComponent((java.lang.String) r0, (java.lang.Class<? extends com.taobao.weex.ui.component.WXComponent>) r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "tabheader"
            java.lang.Class<com.alibaba.aliweex.adapter.component.WXTabHeader> r1 = com.alibaba.aliweex.adapter.component.WXTabHeader.class
            com.taobao.weex.WXSDKEngine.registerComponent((java.lang.String) r0, (java.lang.Class<? extends com.taobao.weex.ui.component.WXComponent>) r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "mask"
            java.lang.Class<com.alibaba.aliweex.adapter.component.WXMask> r1 = com.alibaba.aliweex.adapter.component.WXMask.class
            com.taobao.weex.WXSDKEngine.registerComponent((java.lang.String) r0, (java.lang.Class<? extends com.taobao.weex.ui.component.WXComponent>) r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "tabbar"
            java.lang.Class<com.alibaba.aliweex.adapter.component.WXTabbar> r1 = com.alibaba.aliweex.adapter.component.WXTabbar.class
            com.taobao.weex.WXSDKEngine.registerComponent((java.lang.String) r0, (java.lang.Class<? extends com.taobao.weex.ui.component.WXComponent>) r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "embed"
            java.lang.Class<com.alibaba.aliweex.adapter.component.AliWXEmbed> r1 = com.alibaba.aliweex.adapter.component.AliWXEmbed.class
            r2 = 1
            com.taobao.weex.WXSDKEngine.registerComponent((java.lang.String) r0, (java.lang.Class<? extends com.taobao.weex.ui.component.WXComponent>) r1, (boolean) r2)     // Catch:{ WXException -> 0x013a }
            com.taobao.weex.ui.SimpleComponentHolder r0 = new com.taobao.weex.ui.SimpleComponentHolder     // Catch:{ WXException -> 0x013a }
            java.lang.Class<com.alibaba.aliweex.adapter.component.AliWXImage> r1 = com.alibaba.aliweex.adapter.component.AliWXImage.class
            com.alibaba.aliweex.adapter.component.AliWXImage$Create r3 = new com.alibaba.aliweex.adapter.component.AliWXImage$Create     // Catch:{ WXException -> 0x013a }
            r3.<init>()     // Catch:{ WXException -> 0x013a }
            r0.<init>(r1, r3)     // Catch:{ WXException -> 0x013a }
            r1 = 2
            java.lang.String[] r1 = new java.lang.String[r1]     // Catch:{ WXException -> 0x013a }
            java.lang.String r3 = "image"
            r4 = 0
            r1[r4] = r3     // Catch:{ WXException -> 0x013a }
            java.lang.String r3 = "img"
            r1[r2] = r3     // Catch:{ WXException -> 0x013a }
            com.taobao.weex.WXSDKEngine.registerComponent((com.taobao.weex.ui.IFComponentHolder) r0, (boolean) r4, (java.lang.String[]) r1)     // Catch:{ WXException -> 0x013a }
            com.taobao.weex.ui.SimpleComponentHolder r0 = new com.taobao.weex.ui.SimpleComponentHolder     // Catch:{ WXException -> 0x013a }
            java.lang.Class<com.alibaba.aliweex.adapter.component.AliGifImage> r1 = com.alibaba.aliweex.adapter.component.AliGifImage.class
            com.alibaba.aliweex.adapter.component.AliGifImage$Create r3 = new com.alibaba.aliweex.adapter.component.AliGifImage$Create     // Catch:{ WXException -> 0x013a }
            r3.<init>()     // Catch:{ WXException -> 0x013a }
            r0.<init>(r1, r3)     // Catch:{ WXException -> 0x013a }
            java.lang.String[] r1 = new java.lang.String[r2]     // Catch:{ WXException -> 0x013a }
            java.lang.String r2 = "gif"
            r1[r4] = r2     // Catch:{ WXException -> 0x013a }
            com.taobao.weex.WXSDKEngine.registerComponent((com.taobao.weex.ui.IFComponentHolder) r0, (boolean) r4, (java.lang.String[]) r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "a"
            java.lang.Class<com.alibaba.aliweex.adapter.component.WXExtA> r1 = com.alibaba.aliweex.adapter.component.WXExtA.class
            com.taobao.weex.WXSDKEngine.registerComponent((java.lang.String) r0, (java.lang.Class<? extends com.taobao.weex.ui.component.WXComponent>) r1, (boolean) r4)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "device"
            java.lang.Class<com.alibaba.aliweex.adapter.module.WXDeviceModule> r1 = com.alibaba.aliweex.adapter.module.WXDeviceModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "broadcast"
            java.lang.Class<com.alibaba.aliweex.adapter.module.broadcast.WXBroadcastModule> r1 = com.alibaba.aliweex.adapter.module.broadcast.WXBroadcastModule.class
            com.taobao.weex.WXSDKEngine.registerModule(r0, r1)     // Catch:{ WXException -> 0x013a }
            java.lang.String r0 = "parallax"
            java.lang.Class<com.alibaba.aliweex.adapter.component.WXParallax> r1 = com.alibaba.aliweex.adapter.component.WXParallax.class
            com.taobao.weex.WXSDKEngine.registerComponent((java.lang.String) r0, (java.lang.Class<? extends com.taobao.weex.ui.component.WXComponent>) r1)     // Catch:{ WXException -> 0x013a }
            com.taobao.message.weex.MessgeBundleWeexModuleRegister.init()     // Catch:{ Throwable -> 0x012a }
            goto L_0x0130
        L_0x012a:
            r0 = move-exception
            java.lang.String r1 = "TBWXSDKEngine"
            com.taobao.weex.utils.WXLogUtils.e((java.lang.String) r1, (java.lang.Throwable) r0)     // Catch:{ WXException -> 0x013a }
        L_0x0130:
            com.alibaba.android.bindingx.plugin.weex.BindingX.register()     // Catch:{ Throwable -> 0x0133 }
        L_0x0133:
            com.alibaba.weex.plugin.gcanvas.GCanvasWeexRegister.register()     // Catch:{ Throwable -> 0x0136 }
        L_0x0136:
            registerInteractive()     // Catch:{ WXException -> 0x013a }
            goto L_0x0153
        L_0x013a:
            r0 = move-exception
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.String r2 = "[TBWXSDKEngine] registerModulesAndComponents:"
            r1.append(r2)
            java.lang.Throwable r0 = r0.getCause()
            r1.append(r0)
            java.lang.String r0 = r1.toString()
            com.taobao.weex.utils.WXLogUtils.e(r0)
        L_0x0153:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.aliweex.AliWXSDKEngine.registerModulesAndComponents():void");
    }

    private static void registerInteractive() {
        registerInteractiveModule(new String[]{"start"}, "videokeyboard", "com.taobao.android.interactive.wxplatform.module.WXKeyboardModule");
        registerInteractiveModule(new String[]{"openUrl", "getDisplayCutoutHeight", "getCPUName", "setContinuousPlayStatus", "getContinuousPlayStatus", "getContinuousPlayState", "setContinuousPlayState"}, "videoutils", "com.taobao.android.interactive.wxplatform.module.WXInteractiveUtils");
        registerInteractiveComponent(new String[]{"getDuration", Constants.Value.PLAY, "pause", "replay", "getScreenMode", "getMuted", CommandID.setMuted, "showOrHideTopMoreBtn", "showOrHideTopDanmakuBtn", "showOrHideTopShareBtn", Constants.Event.SLOT_LIFECYCLE.DESTORY, "callReportBtn", "callDanmakuBtn", "callShareBtn"}, "immersivevideo", "com.taobao.android.interactive.shortvideo.weex.IctImmersiveVideoWXComponent", false);
        registerInteractiveComponent(new String[]{"getCurrentTime", "setCurrentTime", "getDuration", Constants.Value.PLAY, "pause", CommandID.setMuted, "getMuted"}, "interactiveVideo", "com.taobao.android.interactive.wxplatform.component.InteractiveVideoComponent", false);
        registerInteractiveComponent(new String[]{"getCurrentTime", "setCurrentTime", "getDuration", Constants.Value.PLAY, "pause", CommandID.setMuted, "getMuted", "onShowcontrols", "setAnchors", "navToUrl"}, "interactiveVideoV2", "com.taobao.android.interactive.wxplatform.component.InteractiveVideoComponentV2", false);
        registerInteractiveComponent(new String[0], "videoshare", "com.taobao.android.interactive.wxplatform.component.InteractiveShareComponent", false);
    }

    private static void registerInteractiveModule(String[] strArr, String str, String str2) {
        ConfigModuleFactory configModuleFactory = new ConfigModuleFactory(str, str2, strArr);
        try {
            WXSDKEngine.registerModule(configModuleFactory.getName(), (ModuleFactory) configModuleFactory, false);
        } catch (WXException e) {
            e.printStackTrace();
        }
    }

    private static void registerInteractiveComponent(String[] strArr, String str, String str2, boolean z) {
        ConfigComponentHolder configComponentHolder = new ConfigComponentHolder(str, z, str2, strArr);
        try {
            WXSDKEngine.registerComponent((IFComponentHolder) configComponentHolder, configComponentHolder.isAppendTree(), configComponentHolder.getType());
        } catch (WXException e) {
            e.printStackTrace();
        }
    }

    private static void initFramework() {
        InitConfig initConfig;
        boolean z;
        if (GlobalConfig.context == null) {
            GlobalConfig.context = AliWeex.getInstance().getApplication();
        }
        String zCacheFromUrl = WXUtil.getZCacheFromUrl("weex", FRAMEWORK_JS_URL);
        if (TextUtils.isEmpty(zCacheFromUrl)) {
            WXLogUtils.e("TBWXSDKEngine", "TBWXSDKEngine: WV obtain  FRAMEWORK_JS failed");
        }
        AliWeex instance = AliWeex.getInstance();
        if (instance.getInitConfig() == null) {
            InitConfig.Builder foldDeviceAdapter = new InitConfig.Builder().setImgAdapter(instance.getImgLoaderAdapter() == null ? new WXImgLoaderAdapter() : instance.getImgLoaderAdapter()).setHttpAdapter(instance.getHttpAdapter() == null ? new WXHttpAdapter() : instance.getHttpAdapter()).setUtAdapter(new WXUserTrackAdapter()).setFramework(zCacheFromUrl).setDrawableLoader(new PhenixBasedDrawableLoader()).setWebSocketAdapterFactory(new WXWebSocketProvider()).setJSExceptionAdapter(new WXExceptionAdapter()).setApmGenerater(new WXAPMGeneratorAdapter()).setJsFileLoaderAdapter(new WXJsFileLoaderAdapter()).setJscProcessManager(new WXJscProcessManager()).setFoldDeviceAdapter(WXAliFoldDeviceAdapter.getFoldDeviceAdapter());
            try {
                Class.forName(SoLoader.class.getName());
                WXLogUtils.e("so loader existed");
                z = true;
            } catch (Throwable unused) {
                z = false;
            }
            String fromConfigKV = WXInitConfigManager.getInstance().getFromConfigKV(WXInitConfigManager.getInstance().c_enableSoLoader);
            if (z && "true".equals(fromConfigKV)) {
                WXLogUtils.e("use so loader");
                foldDeviceAdapter.setSoLoader(new WXSoLoaderAdapter());
            }
            if (instance.getNativeLibraryList() != null) {
                for (String addNativeLibrary : instance.getNativeLibraryList()) {
                    foldDeviceAdapter.addNativeLibrary(addNativeLibrary);
                }
            }
            initConfig = foldDeviceAdapter.build();
        } else {
            initConfig = instance.getInitConfig();
        }
        try {
            if (initConfig.getClassLoaderAdapter() == null) {
                if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.d("AliWeex", "Weex Auto Config Use Atlas Class Loader Adapter");
                }
                initConfig.setClassLoaderAdapter(new AtlasBundleClassLoaderAdapter());
            }
        } catch (Throwable th) {
            th.printStackTrace();
        }
        WXSDKEngine.initialize(instance.getApplication(), initConfig);
        try {
            mWXCrashReportListener = new WXCrashReportListener();
            MotuCrashReporter.getInstance().setCrashCaughtListener((IUTCrashCaughtListener) mWXCrashReportListener);
            WXSDKManager.getInstance().setCrashInfoReporter(new ICrashInfoReporter() {
                public void addCrashInfo(String str, String str2) {
                    MotuCrashReporter.getInstance().addNativeHeaderInfo(str, str2);
                    if (AliWXSDKEngine.mWXCrashReportListener != null) {
                        AliWXSDKEngine.mWXCrashReportListener.setCurCrashUrl(str2);
                    }
                }
            });
            WXSDKManager.getInstance().setWxConfigAdapter(new WXConfigAdapter());
        } catch (Throwable unused2) {
        }
    }

    private static void loadRaxFramework() {
        String streamByUrl = ZipAppUtils.getStreamByUrl("rax", RAX_FRAMEWORK_JS_URL);
        if (TextUtils.isEmpty(streamByUrl)) {
            streamByUrl = WXFileUtils.loadAsset("rax.js", WXEnvironment.getApplication());
        }
        boolean registerService = WXSDKEngine.registerService("rax", streamByUrl, new HashMap());
        WXLogUtils.d("rax framework init " + registerService);
    }

    private static void loadRaxPkgFrameWork() {
        IWXConfigAdapter wxConfigAdapter = WXSDKManager.getInstance().getWxConfigAdapter();
        if (wxConfigAdapter == null) {
            WXLogUtils.e("load raxPkg failed cause adapter is null");
        } else if (!"true".equalsIgnoreCase(wxConfigAdapter.getConfigWhenInit(WXInitConfigManager.WXAPM_CONFIG_GROUP, "loadRaxPkg", "true"))) {
            WXLogUtils.e("load raxPkg failed cause config is false");
        } else {
            ZCacheResourceResponse zCacheResourceResponse = WVPackageAppRuntime.getZCacheResourceResponse(RAX_FRAMEWORK_PKG_JS_URL);
            String str = null;
            if (zCacheResourceResponse != null && zCacheResourceResponse.isSuccess) {
                str = WXFileUtils.readStreamToString(zCacheResourceResponse.inputStream);
            }
            if (TextUtils.isEmpty(str)) {
                str = WXFileUtils.loadAsset("raxpkg.js", WXEnvironment.getApplication());
            }
            boolean registerService = WXSDKEngine.registerService("raxpkg", str, new HashMap());
            WXLogUtils.d("raxPkg framework init " + registerService);
        }
    }

    /* access modifiers changed from: private */
    public static void initWindmillTaobaoMtopPrefetch() {
        WMLPrefetch.getInstance().registerHandler(new WMMtopPrefetch());
    }

    public static synchronized void setCurCrashUrl(String str) {
        synchronized (AliWXSDKEngine.class) {
            if (mWXCrashReportListener != null) {
                mWXCrashReportListener.setCurCrashUrl(str);
            }
            MotuCrashReporter.getInstance().addNativeHeaderInfo(WXEnvironment.WEEX_CURRENT_KEY, str);
        }
    }

    public static synchronized void setCurInstanceId(String str) {
        synchronized (AliWXSDKEngine.class) {
            if (mWXCrashReportListener != null) {
                mWXCrashReportListener.setCurInstanceId(str);
            }
        }
    }

    public static void updateGlobalConfig() {
        IConfigAdapter configAdapter = AliWeex.getInstance().getConfigAdapter();
        if (configAdapter != null) {
            String config = configAdapter.getConfig(CONFIG_GROUP, "global_config", "");
            if (!TextUtils.isEmpty(config)) {
                WXBridgeManager.updateGlobalConfig(config);
            }
            WXBridgeManager.getInstance().setUseSingleProcess("true".equals(configAdapter.getConfig(WX_PROCESS_SWITCH, "enableSingleProcess", "false")));
        }
    }
}
