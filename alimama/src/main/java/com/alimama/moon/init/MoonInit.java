package com.alimama.moon.init;

import alimama.com.unwbase.UNWManager;
import alimama.com.unwbase.interfaces.IAliHa;
import alimama.com.unwbase.interfaces.IAppEnvironment;
import alimama.com.unwbase.interfaces.ICrashReport;
import alimama.com.unwbase.interfaces.IDiskCache;
import alimama.com.unwbase.interfaces.IEtaoLogger;
import alimama.com.unwbase.interfaces.IEvent;
import alimama.com.unwbase.interfaces.IMtop;
import alimama.com.unwbase.interfaces.IOrange;
import alimama.com.unwbase.interfaces.IPush;
import alimama.com.unwbase.interfaces.IRouter;
import alimama.com.unwbase.interfaces.IRxRequestManager;
import alimama.com.unwbase.interfaces.ISecurity;
import alimama.com.unwbase.interfaces.ISharedPreference;
import alimama.com.unwbase.interfaces.ITlog;
import alimama.com.unwbase.interfaces.IUTAction;
import alimama.com.unwbase.interfaces.IUpdateManager;
import alimama.com.unwbase.interfaces.IWindVane;
import alimama.com.unwbase.tools.ThreadUtils;
import alimama.com.unwbaseimpl.UNWAlihaImpl;
import alimama.com.unwbaseimpl.UNWCrashReport;
import alimama.com.unwbaseimpl.UNWMtop;
import alimama.com.unwbaseimpl.UNWOrange;
import alimama.com.unwbaseimpl.UNWSecurityImpl;
import alimama.com.unwbaseimpl.UNWSharePreference;
import alimama.com.unwbaseimpl.UNWTlog;
import alimama.com.unwbaseimpl.UWNUTImpl;
import alimama.com.unwbaseimpl.accs.UNWPushImpl;
import alimama.com.unwcache.DiskCacheImpl;
import alimama.com.unwetaologger.EtaoLoggerImpl;
import alimama.com.unweventparse.UNWEventImpl;
import alimama.com.unwimage.interfaces.IImageViewCreater;
import alimama.com.unwnetwork.RxRequestManager;
import alimama.com.unwrouter.UNWRouter;
import alimama.com.unwrouter.interfaces.PageNeedLoginAction;
import alimama.com.unwupdate.IDialogShow;
import alimama.com.unwupdate.UpdateManager;
import alimama.com.unwviewbase.abstractview.UNWAbstractDialog;
import alimama.com.unwviewbase.abstractview.UNWSysDialogWrap;
import alimama.com.unwviewbase.marketController.UNWDialogController;
import alimama.com.unwwindvane.WindVaneImpl;
import android.app.AlertDialog;
import android.taobao.windvane.jsbridge.WVApiPlugin;
import android.taobao.windvane.jsbridge.WVPluginManager;
import android.taobao.windvane.jsbridge.api.WVAPI;
import anet.channel.util.ALog;
import com.ali.alihadeviceevaluator.AliHardwareInitializer;
import com.alibaba.analytics.core.Constants;
import com.alibaba.motu.crashreporter.IUTCrashCaughtListener;
import com.alimama.moon.App;
import com.alimama.moon.BuildConfig;
import com.alimama.moon.config.OrangeConfigCenterManager;
import com.alimama.moon.init.DialogConstants;
import com.alimama.moon.init.InitConstants;
import com.alimama.moon.network.MtopRequestListener;
import com.alimama.moon.push.AppReceiverImpl;
import com.alimama.moon.ui.BottomNavActivity;
import com.alimama.moon.utils.UnionLensUtil;
import com.alimama.moon.web.MoonJSBridge;
import com.alimama.moon.windvane.jsbridge.InfrastructureJSBridge;
import com.alimama.union.app.configproperties.MoonAppEnvironmentImpl;
import com.alimama.union.app.contact.model.WVContactPlugin;
import com.alimama.union.app.infrastructure.image.capture.WVImagePlugin;
import com.alimama.union.app.infrastructure.permission.WVPermissionPlugin;
import com.alimama.union.app.infrastructure.socialShare.WVClipboard;
import com.alimama.union.app.infrastructure.socialShare.WVSharePlugin;
import com.alimama.union.app.logger.NewMonitorLogger;
import com.alimama.union.app.pagerouter.AppPageInfo;
import com.alimama.union.app.pagerouter.MoonJumpIterceptor;
import com.alimama.unwdinamicxcontainer.UNWDinamicXContainerManager;
import com.ut.mini.internal.UTTeamWork;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MoonInit {
    public static App sApplication;
    public static MoonInit sInstance;

    private void initDeveloper() {
    }

    private MoonInit() {
    }

    public static MoonInit getsInstance() {
        if (sInstance != null) {
            return sInstance;
        }
        sInstance = new MoonInit();
        return sInstance;
    }

    public void execInit(App app) {
        sApplication = app;
        UNWManager.getInstance().init(app);
        UNWManager.getInstance().registerService(ISharedPreference.class, new UNWSharePreference());
        UNWManager.getInstance().registerService(IAppEnvironment.class, new MoonAppEnvironmentImpl());
        UNWManager.getInstance().registerService(ISecurity.class, new UNWSecurityImpl());
        UNWManager.getInstance().registerService(IUTAction.class, new UWNUTImpl());
        initPush();
        UNWManager.getInstance().registerService(IMtop.class, new UNWMtop());
        UnionLensUtil.init(app, getAppKey());
        initCrashReporter();
        UNWManager.getInstance().registerService(IEtaoLogger.class, new EtaoLoggerImpl());
        initDeveloper();
        UNWManager.getInstance().registerService(IOrange.class, new UNWOrange());
        OrangeConfigCenterManager.getInstance().initOrange(app);
        initRouter();
        UNWManager.getInstance().registerService(IDiskCache.class, new DiskCacheImpl("moon"));
        UNWManager.getInstance().registerService(IRxRequestManager.class, new RxRequestManager(new MtopRequestListener()));
        initUpdate();
        UNWManager.getInstance().registerService(ITlog.class, new UNWTlog("moon", InitConstants.Tlog.rsaPublishKey));
        UNWManager.getInstance().registerService(IImageViewCreater.class, new MoonImageViewCreater());
        UNWManager.getInstance().registerService(IEvent.class, new UNWEventImpl());
        UNWManager.getInstance().registerService(UNWDinamicXContainerManager.class, new UNWDinamicXContainerManager());
        AppPageInfo.init();
        initWindvane();
    }

    private void enableUtDebug() {
        HashMap hashMap = new HashMap();
        hashMap.put(Constants.RealTimeDebug.DEBUG_API_URL, "https://service-usertrack.alibaba-inc.com/upload_records_from_client");
        hashMap.put("debug_key", "unionApp_ut_test");
        hashMap.put(Constants.RealTimeDebug.DEBUG_SAMPLING_OPTION, "true");
        UTTeamWork.getInstance().turnOnRealTimeDebug(hashMap);
    }

    private String getAppKey() {
        ISecurity iSecurity = (ISecurity) UNWManager.getInstance().getService(ISecurity.class);
        return iSecurity != null ? iSecurity.getAppkey() : "";
    }

    private void initPush() {
        ThreadUtils.runInBackByFixThread(new Runnable() {
            public void run() {
                UNWManager.getInstance().registerService(IPush.class, new UNWPushImpl(false, AppReceiverImpl.getInstance()));
                ALog.setUseTlog(true);
                IPush iPush = (IPush) UNWManager.getInstance().getService(IPush.class);
                if (iPush != null) {
                    iPush.registerHuaWei();
                    iPush.registerMi(InitConstants.Push.XIAOMI_APP_ID, InitConstants.Push.XIAOMI_APP_KEY);
                    iPush.registerOppo(InitConstants.Push.OPPO_APP_KEY, InitConstants.Push.OPPO_APP_SECRET);
                    iPush.registerViVo();
                }
            }
        });
    }

    private void initCrashReporter() {
        UNWCrashReport uNWCrashReport = new UNWCrashReport();
        uNWCrashReport.setCrashCaughtListener(new IUTCrashCaughtListener() {
            public Map<String, Object> onCrashCaught(Thread thread, Throwable th) {
                HashMap hashMap = new HashMap();
                if (!(th == null || thread == null)) {
                    try {
                        NewMonitorLogger.Crash.crash(th.toString(), thread.toString());
                    } catch (Exception unused) {
                    }
                }
                return hashMap;
            }
        });
        UNWManager.getInstance().registerService(ICrashReport.class, uNWCrashReport);
        ArrayList arrayList = new ArrayList();
        arrayList.add("com.alimama.moon.ui.splashad.SplashAdActivity");
        arrayList.add("com.alimama.moon.ui.WizardActivity");
        ArrayList arrayList2 = new ArrayList();
        arrayList2.add("com.alimama.moon.ui.BottomNavActivity");
        UNWManager.getInstance().registerService(IAliHa.class, new UNWAlihaImpl(arrayList, arrayList2, (AliHardwareInitializer.HardwareListener) null, sApplication.getPackageName()));
    }

    private void initRouter() {
        UNWRouter uNWRouter = new UNWRouter((PageNeedLoginAction) null, "unionApp");
        uNWRouter.setHomeActivity(BottomNavActivity.class);
        uNWRouter.setInterceptor(new MoonJumpIterceptor());
        UNWManager.getInstance().registerService(IRouter.class, uNWRouter);
    }

    private void initWindvane() {
        UNWManager.getInstance().registerService(IWindVane.class, new WindVaneImpl("moon", BuildConfig.VERSION_NAME, InitConstants.Windvane.ucsdkappkeySec, InitConstants.FILE_AUTHORITY, (String) null, true));
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
    }

    private void initUpdate() {
        UNWManager.getInstance().registerService(IUpdateManager.class, new UpdateManager(sApplication.getResources().getIdentifier("ic_launcher", "drawable", sApplication.getPackageName()), new IDialogShow() {
            public void show(boolean z, AlertDialog alertDialog) {
                if (alertDialog != null) {
                    UNWSysDialogWrap uNWSysDialogWrap = new UNWSysDialogWrap(alertDialog.getContext());
                    uNWSysDialogWrap.setDialog(alertDialog);
                    uNWSysDialogWrap.priority = (z ? DialogConstants.TYPE.FORCEUPADATE : DialogConstants.TYPE.UPDATE).getPriority();
                    uNWSysDialogWrap.type = (z ? DialogConstants.TYPE.FORCEUPADATE : DialogConstants.TYPE.UPDATE).name();
                    uNWSysDialogWrap.fatigueTime = 0;
                    uNWSysDialogWrap.uuid = uNWSysDialogWrap.type;
                    UNWDialogController.getInstance().commit((UNWAbstractDialog) uNWSysDialogWrap);
                }
            }
        }));
    }
}
