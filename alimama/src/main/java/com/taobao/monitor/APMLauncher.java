package com.taobao.monitor;

import android.app.Application;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.os.Process;
import android.os.SystemClock;
import com.ali.alihadeviceevaluator.AliHAHardware;
import com.taobao.application.common.ApmHelper;
import com.taobao.application.common.data.AppLaunchHelper;
import com.taobao.application.common.data.DeviceHelper;
import com.taobao.monitor.impl.common.APMContext;
import com.taobao.monitor.impl.common.ActivityManagerHook;
import com.taobao.monitor.impl.common.DynamicConstants;
import com.taobao.monitor.impl.common.Global;
import com.taobao.monitor.impl.data.GlobalStats;
import com.taobao.monitor.impl.data.activity.ActivityLifecycle;
import com.taobao.monitor.impl.data.gc.GCCollector;
import com.taobao.monitor.impl.data.image.PhenixLifeCycleImpl;
import com.taobao.monitor.impl.data.network.NetworkLifecycleImpl;
import com.taobao.monitor.impl.processor.fragmentload.FragmentModelLifecycle;
import com.taobao.monitor.impl.processor.launcher.LauncherModelLifeCycle;
import com.taobao.monitor.impl.processor.launcher.LauncherProcessor;
import com.taobao.monitor.impl.processor.pageload.PageModelLifecycle;
import com.taobao.monitor.impl.processor.weex.WeexApmAdapterFactory;
import com.taobao.monitor.impl.trace.ActivityEventDispatcher;
import com.taobao.monitor.impl.trace.ActivityLifeCycleDispatcher;
import com.taobao.monitor.impl.trace.ApplicationBackgroundChangedDispatcher;
import com.taobao.monitor.impl.trace.ApplicationGCDispatcher;
import com.taobao.monitor.impl.trace.ApplicationLowMemoryDispatcher;
import com.taobao.monitor.impl.trace.DispatcherManager;
import com.taobao.monitor.impl.trace.FPSDispatcher;
import com.taobao.monitor.impl.trace.FragmentLifecycleDispatcher;
import com.taobao.monitor.impl.trace.ImageStageDispatcher;
import com.taobao.monitor.impl.trace.NetworkStageDispatcher;
import com.taobao.monitor.impl.trace.UsableVisibleDispatcher;
import com.taobao.monitor.impl.util.ProcessUtils;
import com.taobao.monitor.impl.util.TimeUtils;
import com.taobao.monitor.performance.APMAdapterFactoryProxy;
import com.taobao.network.lifecycle.MtopLifecycleManager;
import com.taobao.network.lifecycle.NetworkLifecycleManager;
import com.taobao.phenix.lifecycle.PhenixLifeCycleManager;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import java.util.Map;

public class APMLauncher {
    private static final String TAG = "APMLauncher";
    private static boolean init = false;
    /* access modifiers changed from: private */
    public static final AppLaunchHelper launchHelper = new AppLaunchHelper();

    private APMLauncher() {
    }

    public static void init(Application application, Map<String, Object> map) {
        if (!init) {
            init = true;
            initParams(application, map);
            initHotCold();
            initDispatcher();
            firstAsyncMessage();
            initLifecycle(application);
            initHookActivityManager();
            initApmImpl();
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:19:0x00c5  */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x00cf  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void initParams(android.app.Application r4, java.util.Map<java.lang.String, java.lang.Object> r5) {
        /*
            long r0 = com.taobao.monitor.impl.util.TimeUtils.currentTimeMillis()
            com.taobao.monitor.impl.data.GlobalStats.launchStartTime = r0
            com.taobao.application.common.data.AppLaunchHelper r0 = launchHelper
            java.lang.String r1 = "COLD"
            r0.setLaunchType(r1)
            com.taobao.application.common.data.AppLaunchHelper r0 = launchHelper
            long r1 = android.os.SystemClock.uptimeMillis()
            r0.setStartAppOnCreateSystemClockTime(r1)
            com.taobao.application.common.data.AppLaunchHelper r0 = launchHelper
            long r1 = java.lang.System.currentTimeMillis()
            r0.setStartAppOnCreateSystemTime(r1)
            java.lang.String r0 = "ALI_APM/device-id/monitor/procedure"
            if (r5 == 0) goto L_0x005a
            java.lang.String r1 = "appVersion"
            java.lang.Object r1 = r5.get(r1)
            java.lang.String r2 = "unknown"
            java.lang.String r1 = com.taobao.monitor.impl.util.SafeUtils.getSafeString(r1, r2)
            com.taobao.monitor.impl.data.GlobalStats.appVersion = r1
            java.lang.String r1 = "deviceId"
            java.lang.Object r5 = r5.get(r1)
            boolean r1 = r5 instanceof java.lang.String
            if (r1 == 0) goto L_0x005a
            java.lang.String r5 = (java.lang.String) r5
            java.lang.String r0 = "UTF-8"
            java.lang.String r0 = java.net.URLEncoder.encode(r5, r0)     // Catch:{ Exception -> 0x0044 }
            r5 = r0
        L_0x0044:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.String r1 = "ALI_APM/"
            r0.append(r1)
            r0.append(r5)
            java.lang.String r5 = "/monitor/procedure"
            r0.append(r5)
            java.lang.String r0 = r0.toString()
        L_0x005a:
            com.taobao.monitor.impl.common.Global r5 = com.taobao.monitor.impl.common.Global.instance()
            com.taobao.monitor.impl.common.Global r4 = r5.setContext(r4)
            r4.setNamespace(r0)
            com.taobao.monitor.impl.common.Global r4 = com.taobao.monitor.impl.common.Global.instance()
            android.content.Context r4 = r4.context()
            java.lang.String r5 = "apm"
            r0 = 0
            android.content.SharedPreferences r4 = r4.getSharedPreferences(r5, r0)
            java.lang.String r5 = "appVersion"
            java.lang.String r1 = ""
            java.lang.String r5 = r4.getString(r5, r1)
            android.content.SharedPreferences$Editor r1 = r4.edit()
            boolean r2 = android.text.TextUtils.isEmpty(r5)
            r3 = 1
            if (r2 == 0) goto L_0x0098
            com.taobao.monitor.impl.data.GlobalStats.isFirstInstall = r3
            com.taobao.monitor.impl.data.GlobalStats.isFirstLaunch = r3
            java.lang.String r5 = "NEW"
            com.taobao.monitor.impl.data.GlobalStats.installType = r5
            java.lang.String r5 = "appVersion"
            java.lang.String r0 = com.taobao.monitor.impl.data.GlobalStats.appVersion
            r1.putString(r5, r0)
        L_0x0096:
            r0 = 1
            goto L_0x00b3
        L_0x0098:
            com.taobao.monitor.impl.data.GlobalStats.isFirstInstall = r0
            java.lang.String r2 = com.taobao.monitor.impl.data.GlobalStats.appVersion
            boolean r5 = r5.equals(r2)
            r5 = r5 ^ r3
            com.taobao.monitor.impl.data.GlobalStats.isFirstLaunch = r5
            java.lang.String r5 = "UPDATE"
            com.taobao.monitor.impl.data.GlobalStats.installType = r5
            boolean r5 = com.taobao.monitor.impl.data.GlobalStats.isFirstLaunch
            if (r5 == 0) goto L_0x00b3
            java.lang.String r5 = "appVersion"
            java.lang.String r0 = com.taobao.monitor.impl.data.GlobalStats.appVersion
            r1.putString(r5, r0)
            goto L_0x0096
        L_0x00b3:
            java.lang.String r5 = "LAST_TOP_ACTIVITY"
            java.lang.String r2 = ""
            java.lang.String r4 = r4.getString(r5, r2)
            com.taobao.monitor.impl.data.GlobalStats.lastTopActivity = r4
            java.lang.String r4 = com.taobao.monitor.impl.data.GlobalStats.lastTopActivity
            boolean r4 = android.text.TextUtils.isEmpty(r4)
            if (r4 != 0) goto L_0x00cd
            java.lang.String r4 = "LAST_TOP_ACTIVITY"
            java.lang.String r5 = ""
            r1.putString(r4, r5)
            r0 = 1
        L_0x00cd:
            if (r0 == 0) goto L_0x00d2
            r1.apply()
        L_0x00d2:
            long r4 = com.taobao.application.common.data.AppLaunchHelper.LaunchTimeUtils.getLastLaunchTime()
            com.taobao.monitor.impl.data.GlobalStats.lastProcessStartTime = r4
            com.taobao.application.common.data.AppLaunchHelper r4 = launchHelper
            boolean r5 = com.taobao.monitor.impl.data.GlobalStats.isFirstLaunch
            r4.setIsFirstLaunch(r5)
            com.taobao.application.common.data.AppLaunchHelper r4 = launchHelper
            boolean r5 = com.taobao.monitor.impl.data.GlobalStats.isFirstInstall
            r4.setIsFullNewInstall(r5)
            com.taobao.application.common.data.AppLaunchHelper r4 = launchHelper
            long r0 = com.taobao.monitor.impl.data.GlobalStats.lastProcessStartTime
            r4.setLastProcessTime(r0)
            com.taobao.application.common.data.DeviceHelper r4 = new com.taobao.application.common.data.DeviceHelper
            r4.<init>()
            java.lang.String r5 = android.os.Build.MODEL
            r4.setMobileModel(r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.monitor.APMLauncher.initParams(android.app.Application, java.util.Map):void");
    }

    private static void initHotCold() {
        Global.instance().handler().postDelayed(new Runnable() {
            public void run() {
                Looper.getMainLooper();
                Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
                    public boolean queueIdle() {
                        if (GlobalStats.createdPageCount != 0) {
                            return false;
                        }
                        LauncherProcessor.launcherType = LauncherProcessor.HOT;
                        LauncherProcessor.isBackgroundLaunch = true;
                        APMLauncher.launchHelper.setLaunchType(LauncherProcessor.HOT);
                        return false;
                    }
                });
            }
        }, TBToast.Duration.MEDIUM);
    }

    private static void initLifecycle(Application application) {
        application.registerActivityLifecycleCallbacks(new ActivityLifecycle());
    }

    /* access modifiers changed from: private */
    public static void initWeex() {
        if (DynamicConstants.needWeex) {
            APMAdapterFactoryProxy.instance().setFactory(new WeexApmAdapterFactory());
        }
    }

    private static void initHookActivityManager() {
        if (Build.VERSION.SDK_INT <= 28) {
            runInMain(new Runnable() {
                public void run() {
                    ActivityManagerHook.start();
                }
            });
        }
    }

    private static void runInMain(Runnable runnable) {
        if (Thread.currentThread() == Looper.getMainLooper().getThread()) {
            runnable.run();
        } else {
            new Handler(Looper.getMainLooper()).post(runnable);
        }
    }

    /* access modifiers changed from: private */
    public static void initProcessStartTime() {
        if (Build.VERSION.SDK_INT >= 24) {
            GlobalStats.processStartTime = (TimeUtils.currentTimeMillis() + Process.getStartUptimeMillis()) - SystemClock.uptimeMillis();
            launchHelper.setStartProcessSystemTime(System.currentTimeMillis() - (SystemClock.uptimeMillis() - GlobalStats.processStartTime));
        } else {
            long processStartSystemTime = ProcessUtils.getProcessStartSystemTime();
            launchHelper.setStartProcessSystemTime(processStartSystemTime);
            if (processStartSystemTime != -1) {
                GlobalStats.processStartTime = TimeUtils.currentTimeMillis() - (System.currentTimeMillis() - processStartSystemTime);
            } else {
                GlobalStats.processStartTime = TimeUtils.currentTimeMillis() - Process.getElapsedCpuTime();
            }
        }
        launchHelper.setStartProcessSystemClockTime(GlobalStats.processStartTime);
    }

    /* access modifiers changed from: private */
    public static void initOppoCPUResource() {
        GlobalStats.oppoCPUResource = System.getProperty("oppoCPUResource", "false");
    }

    private static void firstAsyncMessage() {
        Global.instance().handler().post(new Runnable() {
            public void run() {
                APMLauncher.initOppoCPUResource();
                APMLauncher.initExecutor();
                APMLauncher.initWeex();
                APMLauncher.initProcessStartTime();
                DeviceHelper deviceHelper = new DeviceHelper();
                deviceHelper.setDeviceLevel(AliHAHardware.getInstance().getOutlineInfo().deviceLevel);
                deviceHelper.setCpuScore(AliHAHardware.getInstance().getCpuInfo().deviceLevel);
                deviceHelper.setMemScore(AliHAHardware.getInstance().getMemoryInfo().deviceLevel);
            }
        });
    }

    private static void initDispatcher() {
        DispatcherManager.addDispatcher(APMContext.APPLICATION_LOW_MEMORY_DISPATCHER, new ApplicationLowMemoryDispatcher());
        DispatcherManager.addDispatcher(APMContext.APPLICATION_GC_DISPATCHER, new ApplicationGCDispatcher());
        DispatcherManager.addDispatcher(APMContext.APPLICATION_BACKGROUND_CHANGED_DISPATCHER, new ApplicationBackgroundChangedDispatcher());
        DispatcherManager.addDispatcher(APMContext.ACTIVITY_FPS_DISPATCHER, new FPSDispatcher());
        ActivityLifeCycleDispatcher activityLifeCycleDispatcher = new ActivityLifeCycleDispatcher();
        activityLifeCycleDispatcher.addListener(new PageModelLifecycle());
        activityLifeCycleDispatcher.addListener(new LauncherModelLifeCycle());
        DispatcherManager.addDispatcher(APMContext.ACTIVITY_LIFECYCLE_DISPATCHER, activityLifeCycleDispatcher);
        DispatcherManager.addDispatcher(APMContext.ACTIVITY_EVENT_DISPATCHER, new ActivityEventDispatcher());
        DispatcherManager.addDispatcher(APMContext.ACTIVITY_USABLE_VISIBLE_DISPATCHER, new UsableVisibleDispatcher());
        FragmentLifecycleDispatcher fragmentLifecycleDispatcher = new FragmentLifecycleDispatcher();
        fragmentLifecycleDispatcher.addListener(new FragmentModelLifecycle());
        DispatcherManager.addDispatcher(APMContext.FRAGMENT_LIFECYCLE_DISPATCHER, fragmentLifecycleDispatcher);
        DispatcherManager.addDispatcher(APMContext.FRAGMENT_USABLE_VISIBLE_DISPATCHER, new UsableVisibleDispatcher());
        DispatcherManager.addDispatcher(APMContext.IMAGE_STAGE_DISPATCHER, new ImageStageDispatcher());
        PhenixLifeCycleManager.instance().addLifeCycle(new PhenixLifeCycleImpl());
        DispatcherManager.addDispatcher(APMContext.NETWORK_STAGE_DISPATCHER, new NetworkStageDispatcher());
        NetworkLifecycleManager.instance().setLifecycle(new NetworkLifecycleImpl());
        MtopLifecycleManager.instance().setLifecycle(new NetworkLifecycleImpl());
    }

    /* access modifiers changed from: private */
    public static void initExecutor() {
        new GCCollector().execute();
    }

    private static void initApmImpl() {
        ApmHelper.inject();
    }
}
