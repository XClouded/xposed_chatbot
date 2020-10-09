package alimama.com.unwwindvane;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IAppEnvironment;
import alimama.com.unwbase.interfaces.ISecurity;
import alimama.com.unwbase.interfaces.IWindVane;
import alimama.com.unwbase.tools.UNWLog;
import alimama.com.unwwindvane.etaojsbridge.H5LogBridge;
import android.app.Application;
import android.os.Build;
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
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;
import com.alibaba.ut.UT4Aplus;
import com.taobao.android.zcache.dev.ZCacheDevManager;
import com.taobao.android.zcache_monitor.ZMonitor;
import org.jetbrains.annotations.NotNull;

public class WindVaneImpl implements IWindVane {
    private static final String TAG = "WindVaneImpl";
    private String appTag;
    private String appVersion;
    private Application application = UNWManager.getInstance().application;
    private String cachedir;
    private String fileAuthority;
    private String ttid = "";
    private String[] ucsdkappkeySec;

    public WindVaneImpl(@NotNull String str, String str2, String[] strArr, @NotNull String str3, String str4, boolean z) {
        this.appTag = str;
        this.appVersion = str2;
        this.fileAuthority = str3;
        this.cachedir = str4;
        this.ucsdkappkeySec = strArr;
        IAppEnvironment iAppEnvironment = (IAppEnvironment) UNWManager.getInstance().getService(IAppEnvironment.class);
        if (iAppEnvironment != null) {
            this.ttid = iAppEnvironment.getTTid();
        }
        H5LogBridge.setIsH5Log(z);
    }

    public void init() {
        setWindVaneEnv();
        WVCommonConfig.commonConfig.useSystemWebView = false;
        WindVaneSDK.init(this.application, this.cachedir, getWVAppParams(this.appTag, this.appVersion, this.ttid, this.ucsdkappkeySec));
        WVFileUtils.setAuthority(this.fileAuthority);
        if (UNWManager.getInstance().getDebuggable()) {
            WindVaneSDK.openLog(true);
            if (Build.VERSION.SDK_INT >= 19) {
                WebView.setWebContentsDebuggingEnabled(true);
                Log.d("windvaneinit", "open debug func");
            }
        }
        WVCamera.registerUploadService(TBUploadService.class);
        WVPackageAppService.registerWvPackageAppConfig(new WVPackageAppConfig());
        WVPackageAppManager.getInstance().init(this.application, true);
        TBJsApiManager.initJsApi();
        WVAPI.setup();
        TBConfigManager.getInstance().init(this.application);
        UT4Aplus.init(this.application);
        WVMonitor.init();
        ZMonitor.getInstance().init();
        ZCacheDevManager.init();
        JsBridgeManager.register();
    }

    private WVAppParams getWVAppParams(String str, String str2, String str3, String[] strArr) {
        ISecurity iSecurity = (ISecurity) UNWManager.getInstance().getService(ISecurity.class);
        if (iSecurity == null) {
            return null;
        }
        UNWLog.error(TAG, "appKey=" + iSecurity.getAppkey());
        UNWLog.error(TAG, "ttid=" + str3);
        WVAppParams wVAppParams = new WVAppParams();
        wVAppParams.deviceId = UNWManager.getInstance().getDeviceId();
        wVAppParams.appKey = iSecurity.getAppkey();
        wVAppParams.ttid = str3;
        wVAppParams.appTag = str;
        wVAppParams.appVersion = str2;
        wVAppParams.openUCDebug = false;
        wVAppParams.ucsdkappkeySec = strArr;
        WVCommonConfig.commonConfig.ucSkipOldKernel = false;
        return wVAppParams;
    }

    private void setWindVaneEnv() {
        IAppEnvironment iAppEnvironment = (IAppEnvironment) UNWManager.getInstance().getService(IAppEnvironment.class);
        if (iAppEnvironment == null) {
            WindVaneSDK.setEnvMode(EnvEnum.ONLINE);
        } else if (iAppEnvironment.isPre()) {
            WindVaneSDK.setEnvMode(EnvEnum.PRE);
        } else if (iAppEnvironment.isProd()) {
            WindVaneSDK.setEnvMode(EnvEnum.ONLINE);
        } else if (iAppEnvironment.isDaily()) {
            WindVaneSDK.setEnvMode(EnvEnum.DAILY);
        }
    }

    public void registerPlugin(String str, Class<? extends WVApiPlugin> cls) {
        if (!TextUtils.isEmpty(str) && cls != null) {
            WVPluginManager.registerPlugin(str, cls);
        }
    }
}
