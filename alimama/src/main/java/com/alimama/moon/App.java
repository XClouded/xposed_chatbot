package com.alimama.moon;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.ISecurity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.multidex.MultiDexApplication;
import com.alibaba.aliweex.AliWXSDKEngine;
import com.alibaba.aliweex.AliWeex;
import com.alimama.moon.config.MoonConfigCenter;
import com.alimama.moon.dao.SettingManager;
import com.alimama.moon.di.component.AppComponent;
import com.alimama.moon.di.component.DaggerAppComponent;
import com.alimama.moon.di.module.AppModule;
import com.alimama.moon.emas.FlutterApmReporter;
import com.alimama.moon.init.MoonInit;
import com.alimama.moon.push.NotificationChannelUtils;
import com.alimama.moon.service.BeanContext;
import com.alimama.moon.update.UpdateCenter;
import com.alimama.moon.utils.ETaoDraweeHelper;
import com.alimama.moon.utils.ISSharedPreferences;
import com.alimama.moon.utils.PhenixHelper;
import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.configcenter.ConfigCenterCache;
import com.alimama.union.app.configcenter.ConfigCenterHelper;
import com.alimama.union.app.configcenter.ConfigKeyList;
import com.alimama.union.app.configproperties.ConfigProperties;
import com.alimama.union.app.infrastructure.image.download.ImageDownloaderModule;
import com.alimama.union.app.infrastructure.image.request.TaoImageLoader;
import com.alimama.union.app.infrastructure.image.request.WXImgLoaderAdapter;
import com.alimama.union.app.infrastructure.socialShare.ShareModuleAdapter;
import com.alimama.union.app.infrastructure.weex.NavigatorModule;
import com.alimama.union.app.infrastructure.weex.ShareImageModule;
import com.alimama.union.app.infrastructure.weex.UserModuleAdapter;
import com.alimama.union.app.infrastructure.weex.WeexEventModuleAdapter;
import com.alimama.union.app.rxnetwork.EtaoDiskLruCache;
import com.alimama.union.app.taotokenConvert.TaoCodeTransferPresenter;
import com.alimama.unionwl.uiframe.views.text.ISIconFontTextView;
import com.alimama.unionwl.utils.LocalDisplay;
import com.alimama.unionwl.utils.UiUtils;
import com.alimamaunion.base.configcenter.EtaoConfigCenter;
import com.alimamaunion.base.configcenter.IConfigCenterCache;
import com.taobao.phenix.cache.disk.DiskCacheSupplier;
import com.taobao.phenix.compat.alivfs.AlivfsDiskCacheSupplier;
import com.taobao.phenix.intf.Phenix;
import com.taobao.weex.WXSDKEngine;
import io.flutter.view.FlutterMain;
import java.util.List;
import javax.inject.Inject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class App extends MultiDexApplication {
    private static final String TAG = "App";
    private static AppComponent appComponent;
    public static String appKey;
    public static boolean appStart = false;
    private static final Logger logger = LoggerFactory.getLogger((Class<?>) App.class);
    public static App sApplication;
    @Inject
    ILogin login;
    /* access modifiers changed from: private */
    public TaoCodeTransferPresenter mTaoCodeTransferPresenter;

    public class ForegroundBackgroundListener implements LifecycleObserver {
        public ForegroundBackgroundListener() {
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        public void onEnterForeground() {
            App.this.mTaoCodeTransferPresenter.onAppForegrounded();
        }
    }

    public TaoCodeTransferPresenter getTaoCodeTransferPresenter() {
        return this.mTaoCodeTransferPresenter;
    }

    public void onCreate() {
        super.onCreate();
        sApplication = this;
        appStart = true;
        FlutterMain.startInitialization(getApplicationContext());
        ISSharedPreferences.init(this);
        ConfigProperties.init(this);
        LocalDisplay.init(this);
        EtaoDiskLruCache.init(this);
        registerBeans();
        MoonInit.getsInstance().execInit(sApplication);
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        appComponent.inject(this);
        initAliweex(this.login);
        if (isMainProcess()) {
            ETaoDraweeHelper.initFresco(sApplication);
            PhenixHelper.initPhenixForCart(sApplication);
        }
        TaoImageLoader.init(this);
        EtaoConfigCenter.getInstance().setConfigCenterCache((IConfigCenterCache) new ConfigCenterCache(this));
        ConfigCenterHelper.getInstance().initConfigCenter(this, ConfigKeyList.list);
        this.mTaoCodeTransferPresenter = new TaoCodeTransferPresenter(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(new ForegroundBackgroundListener());
        UiUtils.init(this);
        ISIconFontTextView.initIconFont(this);
        UpdateCenter.init(this);
        NotificationChannelUtils.createNotificationChannel(this, NotificationChannelUtils.CHANNEL_ID, NotificationChannelUtils.CHANNEL_NAME);
        FlutterApmReporter.init();
    }

    public static AppComponent getAppComponent() {
        return appComponent;
    }

    private void initAliweex(ILogin iLogin) {
        Phenix.instance().diskCacheBuilder().with((DiskCacheSupplier) new AlivfsDiskCacheSupplier());
        try {
            AliWeex.getInstance().initWithConfig(this, new AliWeex.Config.Builder().setShareModuleAdapter(new ShareModuleAdapter()).setUserModuleAdapter(new UserModuleAdapter(iLogin)).setImgLoaderAdapter(new WXImgLoaderAdapter()).setEventModuleAdapter(new WeexEventModuleAdapter()).build());
            WXSDKEngine.addCustomOptions("bundle_js_version", MoonConfigCenter.getBundleJsVersion());
            WXSDKEngine.addCustomOptions("env", generateWeexEnv());
            AliWXSDKEngine.initSDKEngine();
            WXSDKEngine.registerModule("imageDownloader", ImageDownloaderModule.class);
            WXSDKEngine.registerModule("navigator", NavigatorModule.class);
            WXSDKEngine.registerModule("shareImage", ShareImageModule.class);
        } catch (Exception unused) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x003e A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0041 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0044 A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x0047 A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.lang.String generateWeexEnv() {
        /*
            r3 = this;
            com.alimama.union.app.configproperties.EnvHelper r0 = com.alimama.union.app.configproperties.EnvHelper.getInstance()
            java.lang.String r0 = r0.getCurrentApiEnv()
            int r1 = r0.hashCode()
            r2 = -1012222381(0xffffffffc3aab653, float:-341.4244)
            if (r1 == r2) goto L_0x0030
            r2 = -318370553(0xffffffffed060d07, float:-2.5929213E27)
            if (r1 == r2) goto L_0x0026
            r2 = 95458899(0x5b09653, float:1.6606181E-35)
            if (r1 == r2) goto L_0x001c
            goto L_0x003a
        L_0x001c:
            java.lang.String r1 = "debug"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x003a
            r0 = 2
            goto L_0x003b
        L_0x0026:
            java.lang.String r1 = "prepare"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x003a
            r0 = 1
            goto L_0x003b
        L_0x0030:
            java.lang.String r1 = "online"
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L_0x003a
            r0 = 0
            goto L_0x003b
        L_0x003a:
            r0 = -1
        L_0x003b:
            switch(r0) {
                case 0: goto L_0x0047;
                case 1: goto L_0x0044;
                case 2: goto L_0x0041;
                default: goto L_0x003e;
            }
        L_0x003e:
            java.lang.String r0 = "online"
            return r0
        L_0x0041:
            java.lang.String r0 = "daily"
            return r0
        L_0x0044:
            java.lang.String r0 = "pre"
            return r0
        L_0x0047:
            java.lang.String r0 = "online"
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alimama.moon.App.generateWeexEnv():java.lang.String");
    }

    public String getAppKey() {
        ISecurity iSecurity = (ISecurity) UNWManager.getInstance().getService(ISecurity.class);
        return iSecurity != null ? iSecurity.getAppkey() : "";
    }

    private void registerBeans() {
        BeanContext.register(SettingManager.class, SettingManager.getInstance(getApplicationContext()));
        BeanContext.register(DisplayMetrics.class, getResources().getDisplayMetrics());
        BeanContext.register(LocalBroadcastManager.class, LocalBroadcastManager.getInstance(getApplicationContext()));
        BeanContext.register(Context.class, getApplicationContext());
    }

    private static boolean isMainProcess() {
        return TextUtils.equals(sApplication.getPackageName(), getProcessName(sApplication, Process.myPid()));
    }

    public static String getProcessName(Context context, int i) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService("activity");
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager != null ? activityManager.getRunningAppProcesses() : null;
        if (runningAppProcesses == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo next : runningAppProcesses) {
            if (next.pid == i) {
                return next.processName;
            }
        }
        return null;
    }
}
