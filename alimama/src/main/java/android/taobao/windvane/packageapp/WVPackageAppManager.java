package android.taobao.windvane.packageapp;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Looper;
import android.taobao.windvane.WindvaneException;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.config.WVConfigHandler;
import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.config.WVConfigUpdateCallback;
import android.taobao.windvane.config.WVLocaleConfig;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVPluginManager;
import android.taobao.windvane.packageapp.adaptive.WVPackageApp;
import android.taobao.windvane.packageapp.adaptive.WVPackageAppWebViewClientFilter;
import android.taobao.windvane.packageapp.adaptive.ZCacheAdapter;
import android.taobao.windvane.packageapp.adaptive.ZCacheConfigManager;
import android.taobao.windvane.packageapp.adaptive.ZConfigRequestAdapter;
import android.taobao.windvane.packageapp.cleanup.InfoSnippet;
import android.taobao.windvane.packageapp.cleanup.WVPackageAppCleanup;
import android.taobao.windvane.packageapp.jsbridge.WVZCache;
import android.taobao.windvane.packageapp.monitor.AppInfoMonitor;
import android.taobao.windvane.packageapp.monitor.GlobalInfoMonitor;
import android.taobao.windvane.packageapp.zipapp.ConfigManager;
import android.taobao.windvane.packageapp.zipapp.ZipAppDownloaderQueue;
import android.taobao.windvane.packageapp.zipapp.ZipAppManager;
import android.taobao.windvane.packageapp.zipapp.ZipAppUpdateManager;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppInfo;
import android.taobao.windvane.packageapp.zipapp.data.ZipAppResultCode;
import android.taobao.windvane.packageapp.zipapp.data.ZipGlobalConfig;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppConstants;
import android.taobao.windvane.packageapp.zipapp.utils.ZipAppUtils;
import android.taobao.windvane.packageapp.zipdownload.DownLoadListener;
import android.taobao.windvane.service.WVEventService;
import android.taobao.windvane.thread.WVFixedThreadPool;
import android.taobao.windvane.thread.WVThreadPool;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import android.webkit.ValueCallback;

import com.taobao.android.zcache.dev.ZCacheDev;
import com.taobao.ju.track.constants.Constants;
import com.taobao.weex.el.parse.Operators;
import com.taobao.zcache.ZCacheParams;
import com.taobao.zcache.ZCacheSDK;
import com.taobao.zcache.config.ZCacheAdapterManager;
import com.uc.crashsdk.JNIBridge;

import java.util.List;
import java.util.Map;

public class WVPackageAppManager implements DownLoadListener {
    private static WVPackageAppManager appManager = null;
    public static boolean isInit = false;
    /* access modifiers changed from: private */
    public String TAG = "PackageApp-PackageAppManager";
    private Application mContext;
    public long pkgInitTime = 0;

    public static WVPackageAppManager getInstance() {
        if (appManager == null) {
            synchronized (WVPackageAppManager.class) {
                appManager = new WVPackageAppManager();
            }
        }
        return appManager;
    }

    private WVPackageAppManager() {
    }

    public void init(Context context, boolean z) {
        init(context, z, true);
    }

    public void init(Context context, boolean z, boolean z2) {
        if (!isInit && Build.VERSION.SDK_INT > 11) {
            WVEventService.getInstance().addEventListener(new WVPackageAppWebViewClientFilter(), WVEventService.WV_FORWARD_EVENT);
            this.pkgInitTime = System.currentTimeMillis();
            this.mContext = (Application) context.getApplicationContext();
            WVPluginManager.registerPlugin("ZCache", (Class<? extends WVApiPlugin>) WVZCache.class);
            ZipAppManager.getInstance().init();
            WVLocaleConfig.getInstance().init();
            isInit = true;
            WVConfigManager.getInstance().registerHandler(WVConfigManager.CONFIGNAME_PACKAGE, new WVConfigHandler() {
                public void update(String str, WVConfigUpdateCallback wVConfigUpdateCallback) {
                    if (!TextUtils.equals("3", GlobalConfig.zType) || ZCacheConfigManager.getInstance().useOldConfig()) {
                        WVPackageAppManager.this.updatePackageAppConfig(wVConfigUpdateCallback, str, getSnapshotN());
                    }
                }
            });
            WVConfigManager.getInstance().registerHandler(WVConfigManager.CONFIGNAME_PREFIXES, new WVConfigHandler() {
                public void update(String str, WVConfigUpdateCallback wVConfigUpdateCallback) {
                    if (!TextUtils.equals("3", GlobalConfig.zType) || ZCacheConfigManager.getInstance().useOldConfig()) {
                        WVPackageAppPrefixesConfig.getInstance().updatePrefixesInfos(str, wVConfigUpdateCallback, getSnapshotN());
                    }
                }
            });
            WVConfigManager.getInstance().registerHandler(WVConfigManager.CONFIGNAME_CUSTOM, new WVConfigHandler() {
                public void update(String str, WVConfigUpdateCallback wVConfigUpdateCallback) {
                    if (!TextUtils.equals("3", GlobalConfig.zType) || ZCacheConfigManager.getInstance().useOldConfig()) {
                        WVCustomPackageAppConfig.getInstance().updateCustomConfig(wVConfigUpdateCallback, str, getSnapshotN());
                    }
                }
            });
            ZCacheConfigManager.getInstance().init(context);
            GlobalConfig.zType = ZCacheConfigManager.getInstance().getzType();
            try {
                ZCacheAdapterManager.getInstance().setUpdateImpl(new ZCacheAdapter());
            } catch (Throwable unused) {
            }
            if (!"3".equals(GlobalConfig.zType)) {
                TaoLog.i("ZCache", "use ZCache 2.0");
                WVPackageAppCleanup.getInstance().init();
                WVPackageAppCleanup.getInstance().registerUninstallListener(new WVPackageAppCleanup.UninstallListener() {
                    public void onUninstall(List<String> list) {
                        WVPackageAppManager.this.cleanUp(list);
                    }
                });
                if (ZipAppUtils.isNeedPreInstall(this.mContext)) {
                    AnonymousClass5 r4 = new Runnable() {
                        public void run() {
                            boolean preloadZipInstall = ZipAppUpdateManager.preloadZipInstall(WVPackageApp.getPreunzipPackageName());
                            WVConfigManager.getInstance().resetConfig();
                            String access$100 = WVPackageAppManager.this.TAG;
                            TaoLog.i(access$100, "PackageAppforDebug 预制包解压:" + preloadZipInstall);
                        }
                    };
                    if (Looper.getMainLooper() == Looper.myLooper()) {
                        WVThreadPool.getInstance().execute(r4);
                    } else {
                        r4.run();
                    }
                }
            } else if (z2) {
                TaoLog.i("ZCache", "use ZCache 3.0");
                boolean z3 = false;
                if (ZCacheConfigManager.getInstance().useOldConfig()) {
                    TaoLog.i("ZCache", "use old AWP config");
                    ZConfigRequestAdapter zConfigRequestAdapter = new ZConfigRequestAdapter();
                    WVEventService.getInstance().addEventListener(zConfigRequestAdapter, WVEventService.WV_FORWARD_EVENT);
                    ZCacheAdapterManager.getInstance().setRequest(zConfigRequestAdapter);
                    z3 = true;
                } else {
                    TaoLog.i("ZCache", "use new AWP config");
                    WVConfigManager.getInstance().updateConfig(WVConfigManager.WVConfigUpdateFromType.WVConfigUpdateFromZCache3_0);
                }
                ZCacheParams zCacheParams = new ZCacheParams();
                zCacheParams.appKey = GlobalConfig.getInstance().getAppKey();
                zCacheParams.appVersion = GlobalConfig.getInstance().getAppVersion();
                zCacheParams.context = GlobalConfig.context;
                zCacheParams.env = GlobalConfig.env.getKey();
                zCacheParams.useOldPlatform = z3;
                if (!TextUtils.isEmpty(WVLocaleConfig.getInstance().mCurrentLocale)) {
                    zCacheParams.locale = WVLocaleConfig.getInstance().mCurrentLocale;
                }
                ZCacheSDK.init(zCacheParams);
                WVPluginManager.registerPlugin(ZCacheDev.PLUGIN_NAME, (Class<? extends WVApiPlugin>) android.taobao.windvane.packageapp.jsbridge.ZCacheDev.class);
            }
        }
    }

    public void updatePackageAppConfig(final WVConfigUpdateCallback wVConfigUpdateCallback, String str, String str2) {
        if (isInit) {
            if (WVCommonConfig.commonConfig.packageAppStatus != 2) {
                if (wVConfigUpdateCallback != null) {
                    wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.UPDATE_DISABLED, 0);
                }
            } else if (WVPackageAppService.getWvPackageAppConfig() != null) {
                WVPackageAppService.getWvPackageAppConfig().updateGlobalConfig(true, new ValueCallback<ZipGlobalConfig>() {
                    public void onReceiveValue(ZipGlobalConfig zipGlobalConfig) {
                        if (!"3".equals(ZCacheConfigManager.getInstance().getzType())) {
                            ZipAppUpdateManager.startUpdateApps(zipGlobalConfig);
                        }
                        if (wVConfigUpdateCallback == null) {
                            return;
                        }
                        if (zipGlobalConfig == null || zipGlobalConfig.getAppsTable() == null) {
                            wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS, 0);
                        } else {
                            wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS, zipGlobalConfig.getAppsTable().size());
                        }
                    }
                }, new ValueCallback<WindvaneException>() {
                    public void onReceiveValue(WindvaneException windvaneException) {
                        GlobalInfoMonitor.error(windvaneException.getErrorCode(), windvaneException.getMessage());
                        if (wVConfigUpdateCallback != null) {
                            wVConfigUpdateCallback.updateStatus(WVConfigUpdateCallback.CONFIG_UPDATE_STATUS.SUCCESS, 0);
                        }
                    }
                }, str2, str);
            }
        }
    }

    public void callback(String str, String str2, Map<String, String> map, int i, Object obj) {
        ZipAppInfo zipAppInfo = (ZipAppInfo) obj;
        zipAppInfo.status = ZipAppConstants.ZIP_NEWEST;
        boolean z = false;
        if (TextUtils.isEmpty(str2)) {
            String str3 = this.TAG;
            TaoLog.e(str3, "PackageAppforDebug download[" + str + "] fail: destFile is null");
        } else {
            if (zipAppInfo != null) {
                try {
                    JNIBridge.nativeAddHeaderInfo("wv_zip_origin_url", str);
                } catch (Throwable unused) {
                }
                try {
                    TaoLog.i("WVThread", "current thread = [" + Thread.currentThread().getName() + ";" + Thread.currentThread().getId() + Operators.ARRAY_END_STR);
                    if (i == 4) {
                        z = true;
                    }
                    installOrUpgrade(zipAppInfo, str2, z);
                    try {
                        JNIBridge.nativeAddHeaderInfo("wv_zip_origin_url", "");
                    } catch (Throwable unused2) {
                    }
                } catch (Throwable th) {
                    int i2 = ZipAppResultCode.ERR_SYSTEM;
                    AppInfoMonitor.error(zipAppInfo, i2, "ErrorMsg = ERR_SYSTEM : " + th.getMessage());
                    String str4 = this.TAG;
                    TaoLog.e(str4, "PackageAppforDebug call Throwable" + th.getMessage());
                }
            }
            z = true;
        }
        if (!zipAppInfo.isInstantApp) {
            ZipAppDownloaderQueue.getInstance().updateFinshCount(z);
            ZipAppDownloaderQueue.getInstance().updateState();
        }
    }

    /* access modifiers changed from: private */
    public void cleanUp(final List<String> list) {
        if (list != null && !list.isEmpty()) {
            WVThreadPool.getInstance().execute(new Runnable() {
                public void run() {
                    ZipGlobalConfig locGlobalConfig = ConfigManager.getLocGlobalConfig();
                    try {
                        for (Map.Entry<String, ZipAppInfo> value : locGlobalConfig.getAppsTable().entrySet()) {
                            ZipAppInfo zipAppInfo = (ZipAppInfo) value.getValue();
                            if (!zipAppInfo.isInstantApp) {
                                if (WVCommonConfig.commonConfig.isCheckCleanup) {
                                    if (list.contains(zipAppInfo.name)) {
                                        if (zipAppInfo.status == ZipAppConstants.ZIP_REMOVED) {
                                            zipAppInfo.status = ZipAppConstants.ZIP_NEWEST;
                                            String access$100 = WVPackageAppManager.this.TAG;
                                            TaoLog.i(access$100, "ZipApp 恢复App : " + zipAppInfo.name);
                                        }
                                    } else if (zipAppInfo.status == ZipAppConstants.ZIP_NEWEST) {
                                        zipAppInfo.status = ZipAppConstants.ZIP_REMOVED;
                                        String access$1002 = WVPackageAppManager.this.TAG;
                                        TaoLog.i(access$1002, "ZipApp 淘汰App : " + zipAppInfo.name);
                                    }
                                } else if (!zipAppInfo.isOptional && zipAppInfo.status == ZipAppConstants.ZIP_REMOVED) {
                                    zipAppInfo.status = ZipAppConstants.ZIP_NEWEST;
                                }
                            }
                        }
                    } catch (Throwable th) {
                        String access$1003 = WVPackageAppManager.this.TAG;
                        TaoLog.e(access$1003, "try clear up zipapp failed : " + th.getMessage());
                    }
                    try {
                        for (Map.Entry next : WVPackageAppCleanup.getInstance().getInfoMap().entrySet()) {
                            String str = (String) next.getKey();
                            InfoSnippet infoSnippet = (InfoSnippet) next.getValue();
                            if (infoSnippet.needReinstall) {
                                locGlobalConfig.getAppInfo(str).installedSeq = 0;
                                locGlobalConfig.getAppInfo(str).installedVersion = Constants.PARAM_OUTER_SPM_AB_OR_CD_NONE;
                            }
                            infoSnippet.needReinstall = false;
                            infoSnippet.failCount = 0;
                        }
                    } catch (Exception e) {
                        String access$1004 = WVPackageAppManager.this.TAG;
                        TaoLog.e(access$1004, "try Reinstall zipapp by clearUp failed : " + e.getMessage());
                    }
                    WVPackageAppCleanup.getInstance().saveInfoSnippetToDisk();
                    ConfigManager.saveGlobalConfigToloc(locGlobalConfig);
                }
            });
        }
    }

    private void installOrUpgrade(ZipAppInfo zipAppInfo, String str, boolean z) {
        int i;
        AppInfoMonitor.download(zipAppInfo.getNameandVersion());
        if (TaoLog.getLogStatus()) {
            TaoLog.d(this.TAG, "PackageAppforDebug 开始安装【" + zipAppInfo.name + "|" + zipAppInfo.v + "】");
        }
        try {
            i = ZipAppManager.getInstance().install(zipAppInfo, str, z, true);
        } catch (Exception e) {
            AppInfoMonitor.error(zipAppInfo, ZipAppResultCode.ERR_SYSTEM, "ErrorMsg = ERR_SYSTEM : " + e.getMessage());
            i = -1;
        }
        if (i == ZipAppResultCode.SECCUSS) {
            if (TaoLog.getLogStatus()) {
                TaoLog.d(this.TAG, "PackageAppforDebug 开始升级/安装【" + zipAppInfo.name + "】成功");
            }
            zipAppInfo.status = ZipAppConstants.ZIP_NEWEST;
            zipAppInfo.installedSeq = zipAppInfo.s;
            zipAppInfo.installedVersion = zipAppInfo.v;
            if (zipAppInfo.tempPriority != 0) {
                zipAppInfo.f |= 15;
                zipAppInfo.f &= (long) zipAppInfo.tempPriority;
                zipAppInfo.tempPriority = 0;
            }
            ConfigManager.updateGlobalConfig(zipAppInfo, (String) null, false);
            AppInfoMonitor.success(zipAppInfo);
            if (zipAppInfo.isInstantApp) {
                WVEventService.getInstance().onInstantEvent(6008, zipAppInfo.name, Long.valueOf(zipAppInfo.installedSeq), Boolean.valueOf(zipAppInfo.isPreViewApp));
            } else {
                WVEventService.getInstance().onEvent(6008, zipAppInfo.name, Long.valueOf(zipAppInfo.installedSeq), Boolean.valueOf(zipAppInfo.isPreViewApp));
            }
            if (ConfigManager.getLocGlobalConfig().isAllAppUpdated()) {
                if (TaoLog.getLogStatus()) {
                    String readGlobalConfig = ZipAppFileManager.getInstance().readGlobalConfig(false);
                    TaoLog.d(this.TAG, "PackageAppforDebug 所有更新升级/安装 成功+总控配置:【" + readGlobalConfig + "】");
                }
                WVEventService.getInstance().onEvent(6001);
                try {
                    WVFixedThreadPool.getInstance().reSetTempBuffer();
                } catch (Exception unused) {
                }
            }
            WVPackageApp.notifyPackageUpdateFinish(zipAppInfo.name);
        } else {
            zipAppInfo.status = i;
        }
        ZipAppFileManager.getInstance().clearTmpDir(zipAppInfo.name, true);
        if (TaoLog.getLogStatus()) {
            TaoLog.d(this.TAG, "PackageAppforDebug 清理临时目录【" + zipAppInfo.name + "】");
        }
    }

    public void setPackageZipPrefixAdapter(WVPackageAppService.IPackageZipPrefixAdapter iPackageZipPrefixAdapter) {
        WVPackageAppService.setPackageZipPrefixAdapter(iPackageZipPrefixAdapter);
    }
}
