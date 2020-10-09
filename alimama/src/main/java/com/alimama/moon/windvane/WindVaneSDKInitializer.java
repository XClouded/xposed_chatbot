package com.alimama.moon.windvane;

import android.content.Context;
import android.taobao.windvane.WindVaneSDK;
import android.taobao.windvane.config.EnvEnum;
import android.taobao.windvane.config.WVAppParams;
import android.taobao.windvane.config.WVCommonConfig;
import android.taobao.windvane.extra.config.TBConfigManager;
import android.taobao.windvane.extra.jsbridge.TBJsApiManager;
import android.taobao.windvane.extra.jsbridge.TBUploadService;
import android.taobao.windvane.file.WVFileUtils;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVPluginManager;
import android.taobao.windvane.jsbridge.api.WVAPI;
import android.taobao.windvane.jsbridge.api.WVCamera;
import android.taobao.windvane.monitor.WVMonitor;
import android.taobao.windvane.packageapp.WVPackageAppConfig;
import android.taobao.windvane.packageapp.WVPackageAppManager;
import android.taobao.windvane.packageapp.WVPackageAppService;
import com.alimama.moon.BuildConfig;
import com.alimama.moon.init.InitConstants;
import com.alimama.moon.web.MoonJSBridge;
import com.alimama.moon.windvane.jsbridge.InfrastructureJSBridge;
import com.alimama.union.app.configproperties.EnvHelper;
import com.alimama.union.app.contact.model.WVContactPlugin;
import com.alimama.union.app.infrastructure.image.capture.WVImagePlugin;
import com.alimama.union.app.infrastructure.permission.WVPermissionPlugin;
import com.alimama.union.app.infrastructure.socialShare.WVClipboard;
import com.alimama.union.app.infrastructure.socialShare.WVSharePlugin;
import com.taobao.android.zcache.dev.ZCacheDevManager;
import com.taobao.android.zcache_monitor.ZMonitor;
import com.taobao.mtop.MtopWVPluginRegister;

public class WindVaneSDKInitializer {
    private static WindVaneSDKInitializer instance;
    private final Context appContext;
    private final String appKey;
    private boolean inited = false;
    final String[] ucsdkappkeySec = {"YJ7Sm8lhLWRkz32xDxL7Aax+QvNmsNKX1yu/r9lMLUjA1U1GTtzX7i+9FxxAEn5V963DcQ5SJ5TcQpJFgkgGOw=="};

    private WindVaneSDKInitializer(Context context, String str) {
        this.appContext = context;
        this.appKey = str;
    }

    public static WindVaneSDKInitializer getInstance(Context context, String str) {
        if (instance == null) {
            instance = new WindVaneSDKInitializer(context, str);
        }
        return instance;
    }

    public void init() {
        EnvEnum envEnum;
        if (!this.inited) {
            String currentApiEnv = EnvHelper.getInstance().getCurrentApiEnv();
            char c = 65535;
            int hashCode = currentApiEnv.hashCode();
            if (hashCode != -1012222381) {
                if (hashCode != -318370553) {
                    if (hashCode == 95458899 && currentApiEnv.equals("debug")) {
                        c = 2;
                    }
                } else if (currentApiEnv.equals(EnvHelper.API_ENV_PREPARE)) {
                    c = 0;
                }
            } else if (currentApiEnv.equals("online")) {
                c = 1;
            }
            switch (c) {
                case 0:
                    envEnum = EnvEnum.PRE;
                    break;
                case 1:
                    envEnum = EnvEnum.ONLINE;
                    break;
                case 2:
                    envEnum = EnvEnum.DAILY;
                    break;
                default:
                    envEnum = EnvEnum.ONLINE;
                    WindVaneSDK.openLog(false);
                    break;
            }
            WindVaneSDK.setEnvMode(envEnum);
            WVAppParams wVAppParams = new WVAppParams();
            wVAppParams.appKey = this.appKey;
            wVAppParams.ttid = BuildConfig.TTID;
            wVAppParams.appTag = "moon";
            wVAppParams.openUCDebug = false;
            wVAppParams.appVersion = BuildConfig.VERSION_NAME;
            wVAppParams.ucsdkappkeySec = this.ucsdkappkeySec;
            WVCommonConfig.commonConfig.useSystemWebView = false;
            WVCommonConfig.commonConfig.ucSkipOldKernel = false;
            WindVaneSDK.init(this.appContext, wVAppParams);
            WVMonitor.init();
            WVPackageAppService.registerWvPackageAppConfig(new WVPackageAppConfig());
            WVPackageAppManager.getInstance().init(this.appContext, true);
            WVAPI.setup();
            TBJsApiManager.initJsApi();
            TBConfigManager.getInstance().init(this.appContext);
            WVCamera.registerUploadService(TBUploadService.class);
            ZMonitor.getInstance().init();
            ZCacheDevManager.init();
            MtopWVPluginRegister.register();
            WVFileUtils.setAuthority(InitConstants.FILE_AUTHORITY);
            WVPluginManager.registerPlugin("MoonJSBridge", (Class<? extends WVApiPlugin>) MoonJSBridge.class);
            WVPluginManager.registerPlugin("InfrastructureJSBridge", (Class<? extends WVApiPlugin>) InfrastructureJSBridge.class);
            WVPluginManager.registerPlugin("WVContactPlugin", (Class<? extends WVApiPlugin>) WVContactPlugin.class);
            WVPluginManager.registerPlugin("WVImagePlugin", (Class<? extends WVApiPlugin>) WVImagePlugin.class);
            WVPluginManager.registerPlugin("WVPermissionPlugin", (Class<? extends WVApiPlugin>) WVPermissionPlugin.class);
            WVPluginManager.registerPlugin("WVSharePlugin", (Class<? extends WVApiPlugin>) WVSharePlugin.class);
            WVPluginManager.registerPlugin("WVClipboard", (Class<? extends WVApiPlugin>) WVClipboard.class);
            WVPluginManager.registerAlias("MoonJSBridge", "queryContacts", "WVContactPlugin", "queryContacts");
            WVPluginManager.registerAlias("MoonJSBridge", "sendSms", "WVContactPlugin", "sendSms");
            WVPluginManager.registerAlias("MoonJSBridge", "savePic", "WVImagePlugin", "savePic");
            WVPluginManager.registerAlias("MoonJSBridge", "openSettings", "WVPermissionPlugin", "openSettings");
            WVPluginManager.registerAlias("MoonJSBridge", "doShare", "WVSharePlugin", "doShare");
            WVPluginManager.registerAlias(WVAPI.PluginName.API_BASE, "copyToClipboard", "WVClipboard", "copyToClipboard");
            this.inited = true;
            initUploaderGlobal();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0048  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initUploaderGlobal() {
        /*
            r6 = this;
            android.content.Context r0 = r6.appContext
            com.uploader.export.UploaderGlobal.setContext(r0)
            com.alimama.union.app.configproperties.EnvHelper r0 = com.alimama.union.app.configproperties.EnvHelper.getInstance()
            java.lang.String r0 = r0.getCurrentApiEnv()
            int r1 = r0.hashCode()
            r2 = -1012222381(0xffffffffc3aab653, float:-341.4244)
            r3 = 2
            r4 = 1
            r5 = 0
            if (r1 == r2) goto L_0x0038
            r2 = -318370553(0xffffffffed060d07, float:-2.5929213E27)
            if (r1 == r2) goto L_0x002e
            r2 = 95458899(0x5b09653, float:1.6606181E-35)
            if (r1 == r2) goto L_0x0024
            goto L_0x0042
        L_0x0024:
            java.lang.String r1 = "debug"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0042
            r0 = 2
            goto L_0x0043
        L_0x002e:
            java.lang.String r1 = "prepare"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0042
            r0 = 0
            goto L_0x0043
        L_0x0038:
            java.lang.String r1 = "online"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x0042
            r0 = 1
            goto L_0x0043
        L_0x0042:
            r0 = -1
        L_0x0043:
            switch(r0) {
                case 0: goto L_0x0048;
                case 1: goto L_0x0046;
                case 2: goto L_0x0049;
                default: goto L_0x0046;
            }
        L_0x0046:
            r3 = 0
            goto L_0x0049
        L_0x0048:
            r3 = 1
        L_0x0049:
            java.lang.String r0 = r6.appKey
            com.uploader.export.UploaderGlobal.putElement((int) r5, (java.lang.String) r0)
            java.lang.String r0 = r6.appKey
            com.uploader.export.UploaderGlobal.putElement((int) r4, (java.lang.String) r0)
            com.uploader.portal.UploaderEnvironmentImpl2 r0 = new com.uploader.portal.UploaderEnvironmentImpl2
            android.content.Context r1 = r6.appContext
            r0.<init>(r1)
            r0.setEnvironment(r3)
            com.uploader.portal.UploaderDependencyImpl r1 = new com.uploader.portal.UploaderDependencyImpl
            android.content.Context r2 = r6.appContext
            r1.<init>(r2, r0)
            com.uploader.export.UploaderGlobal.putDependency(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.moon.windvane.WindVaneSDKInitializer.initUploaderGlobal():void");
    }
}
