package com.ut.mini;

import android.app.Application;
import android.os.Build;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import com.alibaba.analytics.AnalyticsMgr;
import com.alibaba.analytics.core.ClientVariables;
import com.alibaba.analytics.core.config.UTClientConfigMgr;
import com.alibaba.analytics.core.config.UTTPKBiz;
import com.alibaba.analytics.utils.AppInfoUtil;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.SpSetting;
import com.alibaba.analytics.utils.StringUtils;
import com.alibaba.analytics.version.UTBuildInfo;
import com.ut.mini.anti_cheat.AntiCheatTracker;
import com.ut.mini.core.sign.IUTRequestAuthentication;
import com.ut.mini.core.sign.UTBaseRequestAuthentication;
import com.ut.mini.core.sign.UTSecurityThridRequestAuthentication;
import com.ut.mini.crashhandler.UTMiniCrashHandler;
import com.ut.mini.exposure.TrackerManager;
import com.ut.mini.extend.TLogExtend;
import com.ut.mini.extend.UTExtendSwitch;
import com.ut.mini.extend.WindvaneExtend;
import com.ut.mini.internal.RealtimeDebugSwitch;
import com.ut.mini.internal.UTOriginalCustomHitBuilder;
import com.ut.mini.module.UTOperationStack;
import com.ut.mini.module.appstatus.UTAppBackgroundTimeoutDetector;
import com.ut.mini.module.appstatus.UTAppStatusRegHelper;
import com.ut.mini.module.plugin.UTPlugin;
import com.ut.mini.module.plugin.UTPluginMgr;
import com.ut.mini.module.process.AbsMultiProcessAdapter;
import com.ut.mini.module.process.MultiProcessManager;
import java.util.HashMap;
import java.util.Map;

public class UTAnalytics {
    private static final String TAG = "UTAnalytics";
    private static volatile boolean mInit = false;
    private static volatile boolean mInit4app = false;
    private static boolean mIsMainProcess = true;
    private static UTAnalytics s_instance = new UTAnalytics();
    private HashMap<String, UTTracker> mAppkeyTrackMap = new HashMap<>();
    private UTTracker mDefaultTracker;
    private Map<String, UTTracker> mTrackerMap = new HashMap();

    public static void setDisableWindvane(boolean z) {
        UTExtendSwitch.bWindvaneExtend = !z;
    }

    private UTAnalytics() {
    }

    public static UTAnalytics getInstance() {
        return s_instance;
    }

    public boolean isInit() {
        return mInit;
    }

    private void initialize(Application application, IUTApplication iUTApplication, boolean z) {
        Log.i(TAG, "initialize start...");
        setAppVersion(iUTApplication.getUTAppVersion());
        setChannel(iUTApplication.getUTChannel());
        if (iUTApplication.isAliyunOsSystem()) {
            getInstance().setToAliyunOsPlatform();
        }
        if (iUTApplication.isUTCrashHandlerDisable()) {
            UTMiniCrashHandler.getInstance().turnOff();
        } else {
            UTMiniCrashHandler.getInstance().turnOn(application.getApplicationContext());
            if (iUTApplication.getUTCrashCraughtListener() != null) {
                UTMiniCrashHandler.getInstance().setCrashCaughtListener(iUTApplication.getUTCrashCraughtListener());
            }
        }
        if (iUTApplication.isUTLogEnable()) {
            turnOnDebug();
        }
        if (!mInit || z) {
            setRequestAuthentication(iUTApplication.getUTRequestAuthInstance());
        }
        mIsMainProcess = AppInfoUtil.isMainProcess(application.getApplicationContext());
        if (!mInit) {
            if (Build.VERSION.SDK_INT >= 14 && mIsMainProcess) {
                AbsMultiProcessAdapter multiProcessAdapter = MultiProcessManager.getMultiProcessAdapter();
                if (multiProcessAdapter != null) {
                    try {
                        multiProcessAdapter.registerActivityLifecycleCallbacks();
                    } catch (Exception e) {
                        UTAppStatusRegHelper.registeActivityLifecycleCallbacks(application);
                        e.printStackTrace();
                    }
                } else {
                    UTAppStatusRegHelper.registeActivityLifecycleCallbacks(application);
                }
                UTAppStatusRegHelper.registerAppStatusCallbacks(UTAppBackgroundTimeoutDetector.getInstance());
                UTAppStatusRegHelper.registerAppStatusCallbacks(UTMI1010_2001Event.getInstance());
                UTAppStatusRegHelper.registerAppStatusCallbacks(new RealtimeDebugSwitch());
                UTAppStatusRegHelper.registerAppStatusCallbacks(UTAppLaunch.getInstance());
                AntiCheatTracker.getInstance().init(application);
                TrackerManager.getInstance().init(application);
            }
            if (mIsMainProcess) {
                RepeatExposurePageMgr.getInstance().init();
            }
        }
    }

    public void registerWindvane() {
        WindvaneExtend.registerWindvane(mInit);
    }

    public synchronized void setAppApplicationInstance(Application application, IUTApplication iUTApplication) {
        try {
            if (!mInit4app) {
                if (!(application == null || iUTApplication == null)) {
                    if (application.getBaseContext() != null) {
                        ClientVariables.getInstance().setContext(application.getBaseContext());
                        UTAppLaunch.checkFirstLaunch(application);
                        UTClientConfigMgr.getInstance().init();
                        TLogExtend.registerTLog();
                        AnalyticsMgr.init(application);
                        initialize(application, iUTApplication, true);
                        registerWindvane();
                        mInit = true;
                        mInit4app = true;
                        Log.i(TAG, "setAppApplicationInstance success!!!");
                        UTSystemLaunch.sendBootTime(application);
                    }
                }
                throw new IllegalArgumentException("application and callback must not be null");
            }
            return;
        } catch (Throwable th) {
            Log.e(TAG, th.toString());
        }
        return;
    }

    public synchronized void setAppApplicationInstance4sdk(Application application, IUTApplication iUTApplication) {
        try {
            if (!mInit) {
                if (!(application == null || iUTApplication == null)) {
                    if (application.getBaseContext() != null) {
                        ClientVariables.getInstance().setContext(application.getBaseContext());
                        UTAppLaunch.checkFirstLaunch(application);
                        UTClientConfigMgr.getInstance().init();
                        TLogExtend.registerTLog();
                        AnalyticsMgr.init(application);
                        initialize(application, iUTApplication, false);
                        registerWindvane();
                        mInit = true;
                        UTSystemLaunch.sendBootTime(application);
                    }
                }
                throw new IllegalArgumentException("application and callback must not be null");
            }
            return;
        } catch (Throwable th) {
            Log.e(TAG, th.toString());
        }
    }

    private boolean checkInit() {
        if (!AnalyticsMgr.isInit) {
            Logger.w("Please call setAppApplicationInstance() before call other method", new Object[0]);
        }
        return AnalyticsMgr.isInit;
    }

    private void setRequestAuthentication(IUTRequestAuthentication iUTRequestAuthentication) {
        boolean z;
        String str;
        String str2;
        boolean z2 = false;
        Logger.i(TAG, "[setRequestAuthentication] start...", UTBuildInfo.getInstance().getFullSDKVersion(), Boolean.valueOf(AnalyticsMgr.isInit));
        if (iUTRequestAuthentication != null) {
            if (iUTRequestAuthentication instanceof UTSecurityThridRequestAuthentication) {
                UTSecurityThridRequestAuthentication uTSecurityThridRequestAuthentication = (UTSecurityThridRequestAuthentication) iUTRequestAuthentication;
                str2 = uTSecurityThridRequestAuthentication.getAppkey();
                str = uTSecurityThridRequestAuthentication.getAuthcode();
                z = false;
                z2 = true;
            } else if (iUTRequestAuthentication instanceof UTBaseRequestAuthentication) {
                UTBaseRequestAuthentication uTBaseRequestAuthentication = (UTBaseRequestAuthentication) iUTRequestAuthentication;
                str2 = uTBaseRequestAuthentication.getAppkey();
                str = uTBaseRequestAuthentication.getAppSecret();
                z = uTBaseRequestAuthentication.isEncode();
            } else {
                throw new IllegalArgumentException("此签名方式暂不支持!请使用 UTSecuritySDKRequestAuthentication 或 UTBaseRequestAuthentication 设置签名!");
            }
            ClientVariables.getInstance().setAppKey(str2);
            AnalyticsMgr.setRequestAuthInfo(z2, z, str2, str);
            return;
        }
        throw new NullPointerException("签名不能为空!");
    }

    private void setAppVersion(String str) {
        AnalyticsMgr.setAppVersion(str);
    }

    private void setChannel(final String str) {
        AnalyticsMgr.setChanel(str);
        try {
            AnalyticsMgr.handler.postWatingTask(new Runnable() {
                public void run() {
                    SpSetting.put(ClientVariables.getInstance().getContext(), "channel", str);
                }
            });
        } catch (Throwable unused) {
        }
    }

    private void turnOffCrashHandler() {
        UTMiniCrashHandler.getInstance().turnOff();
    }

    private void turnOnDebug() {
        AnalyticsMgr.turnOnDebug();
    }

    public void registerPlugin(UTPlugin uTPlugin) {
        UTPluginMgr.getInstance().registerPlugin(uTPlugin);
    }

    public void unregisterPlugin(UTPlugin uTPlugin) {
        UTPluginMgr.getInstance().unregisterPlugin(uTPlugin);
    }

    @Deprecated
    public void updateUserAccount(String str, String str2) {
        try {
            if (!Logger.isDebug()) {
                updateUserAccount(str, str2, (String) null);
                return;
            }
            throw new Exception("this interface is Deprecated，please call UTAnalytics.getInstance().updateUserAccount(String aUsernick, String aUserid,String openid)");
        } catch (Throwable th) {
            Log.w(TAG, "", th);
        }
    }

    public void updateUserAccount(String str, String str2, String str3) {
        AnalyticsMgr.updateUserAccount(str, str2, str3);
        if (!StringUtils.isEmpty(str)) {
            UTOriginalCustomHitBuilder uTOriginalCustomHitBuilder = new UTOriginalCustomHitBuilder("UT", 1007, str, str2, (String) null, (Map<String, String>) null);
            uTOriginalCustomHitBuilder.setProperty("_priority", "5");
            getInstance().getDefaultTracker().send(uTOriginalCustomHitBuilder.build());
        }
    }

    public void userRegister(String str) {
        if (!StringUtils.isEmpty(str)) {
            getDefaultTracker().send(new UTOriginalCustomHitBuilder("UT", 1006, str, (String) null, (String) null, (Map<String, String>) null).build());
            return;
        }
        throw new IllegalArgumentException("Usernick can not be null or empty!");
    }

    public void updateSessionProperties(Map<String, String> map) {
        AnalyticsMgr.updateSessionProperties(map);
    }

    public void sessionTimeout() {
        UTTPKBiz.getInstance().sessionTimeout();
        AnalyticsMgr.setSessionProperties(new HashMap());
        AnalyticsMgr.sessionTimeout();
    }

    public void turnOffAutoPageTrack() {
        UTPageHitHelper.getInstance().turnOffAutoPageTrack();
    }

    public synchronized UTTracker getDefaultTracker() {
        Class cls;
        UTTracker uTTracker;
        if (this.mDefaultTracker == null && !TextUtils.isEmpty(ClientVariables.getInstance().getAppKey())) {
            AbsMultiProcessAdapter multiProcessAdapter = MultiProcessManager.getMultiProcessAdapter();
            if (multiProcessAdapter != null) {
                cls = multiProcessAdapter.isUiSubProcess() ? multiProcessAdapter.getSubProcessUTTrackerClass() : UTTracker.class;
            } else {
                cls = null;
            }
            if (cls == null) {
                cls = UTTracker.class;
            }
            try {
                uTTracker = (UTTracker) cls.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                uTTracker = null;
            }
            if (uTTracker == null) {
                this.mDefaultTracker = new UTTracker();
            } else {
                this.mDefaultTracker = uTTracker;
            }
        }
        if (this.mDefaultTracker == null) {
            throw new RuntimeException("getDefaultTracker error,must call setRequestAuthentication method first");
        }
        return this.mDefaultTracker;
    }

    public synchronized UTTracker getTracker(String str) {
        if (StringUtils.isEmpty(str)) {
            throw new IllegalArgumentException("TrackId is null");
        } else if (this.mTrackerMap.containsKey(str)) {
            return this.mTrackerMap.get(str);
        } else {
            UTTracker uTTracker = new UTTracker();
            uTTracker.setTrackId(str);
            this.mTrackerMap.put(str, uTTracker);
            return uTTracker;
        }
    }

    public synchronized UTTracker getTrackerByAppkey(String str) {
        if (StringUtils.isEmpty(str)) {
            throw new IllegalArgumentException("appkey is null");
        } else if (this.mAppkeyTrackMap.containsKey(str)) {
            return this.mAppkeyTrackMap.get(str);
        } else {
            UTTracker uTTracker = new UTTracker();
            uTTracker.setAppKey(str);
            this.mAppkeyTrackMap.put(str, uTTracker);
            return uTTracker;
        }
    }

    /* access modifiers changed from: protected */
    public void transferLog(Map<String, String> map) {
        if (checkInit()) {
            AnalyticsMgr.handler.postWatingTask(createTransferLogTask(map));
        }
    }

    public void turnOnRealTimeDebug(Map<String, String> map) {
        AnalyticsMgr.turnOnRealTimeDebug(map);
    }

    public void turnOffRealTimeDebug() {
        AnalyticsMgr.turnOffRealTimeDebug();
    }

    public void setToAliyunOsPlatform() {
        ClientVariables.getInstance().setToAliyunOSPlatform();
    }

    public String getOperationHistory(int i, String str) {
        return UTOperationStack.getInstance().getOperationHistory(i, str);
    }

    public void dispatchLocalHits() {
        if (checkInit()) {
            AnalyticsMgr.handler.postWatingTask(new Runnable() {
                public void run() {
                    try {
                        AnalyticsMgr.iAnalytics.dispatchLocalHits();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Deprecated
    public void saveCacheDataToLocal() {
        if (checkInit()) {
            AnalyticsMgr.handler.postWatingTask(new Runnable() {
                public void run() {
                    try {
                        AnalyticsMgr.iAnalytics.saveCacheDataToLocal();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    public String selfCheck(String str) {
        if (!checkInit()) {
            return "local not init";
        }
        if (AnalyticsMgr.iAnalytics == null) {
            return "not bind remote service，waitting 10 second";
        }
        try {
            return AnalyticsMgr.iAnalytics.selfCheck(str);
        } catch (RemoteException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Runnable createTransferLogTask(final Map<String, String> map) {
        return new Runnable() {
            public void run() {
                try {
                    AnalyticsMgr.iAnalytics.transferLog(map);
                } catch (Throwable unused) {
                }
            }
        };
    }
}
