package android.taobao.windvane;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Build;
import android.taobao.windvane.cache.WVCacheManager;
import android.taobao.windvane.config.EnvEnum;
import android.taobao.windvane.config.GlobalConfig;
import android.taobao.windvane.config.WVAppParams;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.config.WVConfigHandler;
import android.taobao.windvane.config.WVConfigManager;
import android.taobao.windvane.config.WVConfigUpdateCallback;
import android.taobao.windvane.config.WVCookieConfig;
import android.taobao.windvane.config.WVDomainConfig;
import android.taobao.windvane.config.WVServerConfig;
import android.taobao.windvane.config.WVUCPrecacheManager;
import android.taobao.windvane.file.FileManager;
import android.taobao.windvane.monitor.UserTrackUtil;
import android.taobao.windvane.packageapp.WVPackageAppService;
import android.taobao.windvane.util.CommonUtils;
import android.taobao.windvane.util.ConfigStorage;
import android.taobao.windvane.util.EnvUtil;
import android.taobao.windvane.util.TaoLog;
import android.text.TextUtils;
import android.webkit.WebView;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

public class WindVaneSDK {
    private static final String SPNAME_ENV = "wv_evn";
    private static final String VALUE_NAME = "evn_value";
    private static final String WV_MULT = "wv_multi_";
    private static boolean initialized = false;

    public static void init(Context context, WVAppParams wVAppParams) {
        init(context, (String) null, 0, wVAppParams);
    }

    @Deprecated
    public static void init(Context context, String str, int i, WVAppParams wVAppParams) {
        init(context, str, wVAppParams);
    }

    public static void init(Context context, String str, WVAppParams wVAppParams) {
        if (initialized) {
            TaoLog.i("WindVaneSDK", "WindVaneSDK has already initialized");
            return;
        }
        TaoLog.i("WindVaneSDK", "WindVaneSDK init");
        if (context != null) {
            GlobalConfig.context = (Application) (context instanceof Application ? context : context.getApplicationContext());
            if (GlobalConfig.context != null) {
                if (EnvUtil.isAppDebug()) {
                    TaoLog.setLogSwitcher(true);
                }
                if (TextUtils.isEmpty(str)) {
                    str = "caches";
                }
                WVCacheManager.getInstance().init(context, str, 0);
                WVCookieManager.onCreate(context);
                AssetManager assets = GlobalConfig.context.getResources().getAssets();
                try {
                    File createFolder = FileManager.createFolder(GlobalConfig.context, "windvane/ucsdk");
                    File[] listFiles = createFolder.listFiles();
                    if (listFiles == null || listFiles.length == 0) {
                        FileManager.unzip(assets.open("uclibs.zip"), createFolder.getAbsolutePath());
                    }
                    wVAppParams.ucLibDir = createFolder.getAbsolutePath();
                    TaoLog.i("WindVaneSDK", "UC init by uclibs");
                } catch (IOException unused) {
                }
                GlobalConfig.getInstance().initParams(wVAppParams);
                ConfigStorage.initDirs();
                UserTrackUtil.init();
                initConfig();
                try {
                    if (Build.VERSION.SDK_INT >= 28) {
                        String processName = CommonUtils.getProcessName(context);
                        if (!TextUtils.isEmpty(processName) && !TextUtils.equals(processName, GlobalConfig.context.getPackageName())) {
                            String[] split = processName.split(":");
                            if (split.length == 2) {
                                WebView.setDataDirectorySuffix(WV_MULT + split[1]);
                            }
                        }
                    }
                } catch (Throwable unused2) {
                }
                try {
                    TaoLog.i("WindVaneSDK", "trying to init uc core");
                    Class.forName("android.taobao.windvane.extra.uc.WVUCWebView");
                    if (!wVAppParams.needSpeed) {
                        Class<?> cls = Class.forName("android.taobao.windvane.extra.uc.WVWebPushService");
                        Method declaredMethod = cls.getDeclaredMethod("getInstance", new Class[]{Context.class});
                        declaredMethod.setAccessible(true);
                        declaredMethod.invoke(cls, new Object[]{context});
                    }
                } catch (Throwable th) {
                    TaoLog.w("WindVaneSDK", "failed to load WVUCWebView", th, new Object[0]);
                }
                initialized = true;
                return;
            }
            throw new IllegalArgumentException("init error, context should be Application or its subclass");
        }
        throw new NullPointerException("init error, context is null");
    }

    public static void initConfig() {
        WVUCPrecacheManager.getInstance();
        WVDomainConfig.getInstance().init();
        WVCommonConfig.getInstance().init();
        WVConfigManager.getInstance().registerHandler("domain", new WVConfigHandler() {
            public void update(String str, WVConfigUpdateCallback wVConfigUpdateCallback) {
                WVDomainConfig.getInstance().updateDomainRule(wVConfigUpdateCallback, str, getSnapshotN());
            }
        });
        WVConfigManager.getInstance().registerHandler("common", new WVConfigHandler() {
            public void update(String str, WVConfigUpdateCallback wVConfigUpdateCallback) {
                WVCommonConfig.getInstance().updateCommonRule(wVConfigUpdateCallback, str, getSnapshotN());
            }
        });
        WVCookieConfig.getInstance().init();
        WVConfigManager.getInstance().registerConfigImpl(WVConfigManager.CONFIGNAME_COOKIE, WVCookieConfig.getInstance());
    }

    public static void initURLCache(Context context, String str, int i) {
        WVCacheManager.getInstance().init(context, str, i);
    }

    public static void openLog(boolean z) {
        TaoLog.setLogSwitcher(z);
    }

    public static boolean isTrustedUrl(String str) {
        return WVServerConfig.isTrustedUrl(str);
    }

    public static void setEnvMode(EnvEnum envEnum) {
        if (envEnum != null) {
            try {
                TaoLog.i(SPNAME_ENV, "setEnvMode : " + envEnum.getValue());
                GlobalConfig.env = envEnum;
                if (ConfigStorage.getLongVal(SPNAME_ENV, VALUE_NAME) != ((long) envEnum.getKey())) {
                    WVConfigManager.getInstance().resetConfig();
                    if (WVPackageAppService.getWvPackageAppConfig() != null) {
                        WVPackageAppService.getWvPackageAppConfig().getGlobalConfig().reset();
                    }
                    ConfigStorage.putLongVal(SPNAME_ENV, VALUE_NAME, (long) envEnum.getKey());
                    WVConfigManager.getInstance().updateConfig(WVConfigManager.WVConfigUpdateFromType.WVConfigUpdateFromTypeActive);
                }
            } catch (Throwable unused) {
            }
        }
    }

    public static boolean isInitialized() {
        return initialized;
    }
}
