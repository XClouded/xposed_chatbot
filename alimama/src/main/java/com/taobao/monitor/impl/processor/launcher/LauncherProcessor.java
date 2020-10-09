package com.taobao.monitor.impl.processor.launcher;

import alimama.com.unwrouter.UNWRouter;
import android.app.Activity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import androidx.fragment.app.Fragment;
import com.ali.alihadeviceevaluator.AliHAHardware;
import com.ali.ha.fulltrace.dump.DumpManager;
import com.ali.ha.fulltrace.event.DisplayedEvent;
import com.ali.ha.fulltrace.event.FirstDrawEvent;
import com.ali.ha.fulltrace.event.FirstInteractionEvent;
import com.ali.ha.fulltrace.event.LauncherUsableEvent;
import com.ali.ha.fulltrace.event.OpenAppFromURL;
import com.ali.ha.fulltrace.event.StartUpBeginEvent;
import com.ali.ha.fulltrace.event.StartUpEndEvent;
import com.alipay.sdk.widget.j;
import com.huawei.updatesdk.service.otaupdate.UpdateKey;
import com.taobao.android.tlog.protocol.Constants;
import com.taobao.application.common.IAppLaunchListener;
import com.taobao.application.common.impl.ApmImpl;
import com.taobao.monitor.impl.common.APMContext;
import com.taobao.monitor.impl.data.GlobalStats;
import com.taobao.monitor.impl.data.OnUsableVisibleListener;
import com.taobao.monitor.impl.data.traffic.TrafficTracker;
import com.taobao.monitor.impl.processor.AbsProcessor;
import com.taobao.monitor.impl.processor.pageload.PageModelLifecycle;
import com.taobao.monitor.impl.processor.pageload.ProcedureManagerSetter;
import com.taobao.monitor.impl.trace.ActivityEventDispatcher;
import com.taobao.monitor.impl.trace.ApplicationBackgroundChangedDispatcher;
import com.taobao.monitor.impl.trace.ApplicationGCDispatcher;
import com.taobao.monitor.impl.trace.ApplicationLowMemoryDispatcher;
import com.taobao.monitor.impl.trace.FPSDispatcher;
import com.taobao.monitor.impl.trace.FragmentFunctionDispatcher;
import com.taobao.monitor.impl.trace.FragmentFunctionListener;
import com.taobao.monitor.impl.trace.IDispatcher;
import com.taobao.monitor.impl.trace.ImageStageDispatcher;
import com.taobao.monitor.impl.trace.NetworkStageDispatcher;
import com.taobao.monitor.impl.util.ActivityUtils;
import com.taobao.monitor.impl.util.SafeUtils;
import com.taobao.monitor.impl.util.TimeUtils;
import com.taobao.monitor.impl.util.TopicUtils;
import com.taobao.monitor.procedure.IProcedure;
import com.taobao.monitor.procedure.ProcedureConfig;
import com.taobao.monitor.procedure.ProcedureFactoryProxy;
import com.taobao.monitor.procedure.ProcedureManagerProxy;
import com.taobao.tao.image.ImageStrategyConfig;
import com.taobao.weex.analyzer.Config;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LauncherProcessor extends AbsProcessor implements PageModelLifecycle.IPageLoadLifeCycle, ApplicationLowMemoryDispatcher.LowMemoryListener, OnUsableVisibleListener<Activity>, FPSDispatcher.FPSListener, ApplicationGCDispatcher.ApplicationGCListener, ApplicationBackgroundChangedDispatcher.BackgroundChangedListener, ActivityEventDispatcher.OnEventListener, FragmentFunctionListener, ImageStageDispatcher.IImageStageListener, NetworkStageDispatcher.INetworkStageListener {
    public static final String COLD = "COLD";
    public static final String HOT = "HOT";
    private static final String TAG = "LauncherProcessor";
    public static boolean isBackgroundLaunch = false;
    public static volatile String launcherType = "COLD";
    private IDispatcher backgroundChangedDispatcher;
    private String currentPageName;
    private IDispatcher eventDispatcher;
    private IDispatcher fpsDispatcher;
    private List<Integer> fpsList = new ArrayList();
    private HashMap<String, Integer> functionCounts = new HashMap<>();
    private int gcCount = 0;
    private IDispatcher gcDispatcher;
    private int imageCanceledCount;
    private IDispatcher imageDispatcher;
    private int imageFailedCount;
    private int imageRequestedCount;
    private int imageSuccessCount;
    private boolean isFirstDraw = true;
    private boolean isFirstFullUsable = true;
    private boolean isFirstFullVisible = true;
    private boolean isFirstTouch = true;
    private boolean isLaunched = false;
    private int jankCount = 0;
    IAppLaunchListener launchListener = ApmImpl.instance().getLaunchListenerGroup();
    protected String launcherActivityName;
    private List<String> linksPageName = new ArrayList(4);
    private List<String> linksPageUrl = new ArrayList(4);
    private IDispatcher lowMemoryDispatcher;
    private int networkCanceledCount;
    private IDispatcher networkDispatcher;
    private int networkFailedCount;
    private int networkRequestedCount;
    private int networkSuccessCount;
    private IProcedure procedure;
    private volatile boolean sendCompleted = false;
    private boolean stopped = false;
    private long tempLauncherStartTime;
    private long[] tempTraffic;
    private String thisLaunchType = launcherType;
    private IDispatcher usableVisibleDispatcher;

    public LauncherProcessor() {
        super(false);
    }

    /* access modifiers changed from: protected */
    public void startProcessor() {
        super.startProcessor();
        this.tempTraffic = TrafficTracker.getFlowBean();
        this.procedure = ProcedureManagerProxy.PROXY.getLauncherProcedure();
        if (this.procedure == null || !this.procedure.isAlive()) {
            this.procedure = ProcedureFactoryProxy.PROXY.createProcedure(TopicUtils.getFullTopic("/startup"), new ProcedureConfig.Builder().setIndependent(false).setUpload(true).setParentNeedStats(true).setParent((IProcedure) null).build());
            this.procedure.begin();
            ProcedureManagerSetter.instance().setCurrentLauncherProcedure(this.procedure);
        }
        this.procedure.stage("procedureStartTime", TimeUtils.currentTimeMillis());
        this.eventDispatcher = getDispatcher(APMContext.ACTIVITY_EVENT_DISPATCHER);
        this.lowMemoryDispatcher = getDispatcher(APMContext.APPLICATION_LOW_MEMORY_DISPATCHER);
        this.usableVisibleDispatcher = getDispatcher(APMContext.ACTIVITY_USABLE_VISIBLE_DISPATCHER);
        this.fpsDispatcher = getDispatcher(APMContext.ACTIVITY_FPS_DISPATCHER);
        this.gcDispatcher = getDispatcher(APMContext.APPLICATION_GC_DISPATCHER);
        this.backgroundChangedDispatcher = getDispatcher(APMContext.APPLICATION_BACKGROUND_CHANGED_DISPATCHER);
        this.networkDispatcher = getDispatcher(APMContext.NETWORK_STAGE_DISPATCHER);
        this.imageDispatcher = getDispatcher(APMContext.IMAGE_STAGE_DISPATCHER);
        this.lowMemoryDispatcher.addListener(this);
        this.fpsDispatcher.addListener(this);
        this.gcDispatcher.addListener(this);
        this.eventDispatcher.addListener(this);
        this.usableVisibleDispatcher.addListener(this);
        this.backgroundChangedDispatcher.addListener(this);
        this.networkDispatcher.addListener(this);
        this.imageDispatcher.addListener(this);
        FragmentFunctionDispatcher.INSTANCE.addListener(this);
        initLauncherProperties();
        StartUpBeginEvent startUpBeginEvent = new StartUpBeginEvent();
        startUpBeginEvent.firstInstall = GlobalStats.isFirstInstall;
        startUpBeginEvent.launchType = launcherType;
        startUpBeginEvent.isBackgroundLaunch = isBackgroundLaunch;
        DumpManager.getInstance().append(startUpBeginEvent);
        isBackgroundLaunch = false;
    }

    private void initLauncherProperties() {
        this.tempLauncherStartTime = COLD.equals(launcherType) ? GlobalStats.launchStartTime : TimeUtils.currentTimeMillis();
        this.procedure.addProperty("errorCode", 1);
        this.procedure.addProperty("launchType", launcherType);
        this.procedure.addProperty("isFirstInstall", Boolean.valueOf(GlobalStats.isFirstInstall));
        this.procedure.addProperty("isFirstLaunch", Boolean.valueOf(GlobalStats.isFirstLaunch));
        this.procedure.addProperty(UpdateKey.MARKET_INSTALL_TYPE, GlobalStats.installType);
        this.procedure.addProperty("oppoCPUResource", GlobalStats.oppoCPUResource);
        this.procedure.addProperty("leaveType", "other");
        this.procedure.addProperty("lastProcessStartTime", Long.valueOf(GlobalStats.lastProcessStartTime));
        this.procedure.addProperty("systemInitDuration", Long.valueOf(GlobalStats.launchStartTime - GlobalStats.processStartTime));
        this.procedure.stage("processStartTime", GlobalStats.processStartTime);
        this.procedure.stage("launchStartTime", GlobalStats.launchStartTime);
    }

    /* access modifiers changed from: protected */
    public void changeLauncherType(String str) {
        this.procedure.addProperty("launchType", str);
    }

    public void onActivityCreated(Activity activity, Map<String, Object> map, long j) {
        String simpleName = ActivityUtils.getSimpleName(activity);
        this.currentPageName = ActivityUtils.getPageName(activity);
        String safeString = SafeUtils.getSafeString(map.get("schemaUrl"), "");
        if (!this.isLaunched) {
            startProcessor();
            this.procedure.addProperty("systemRecovery", false);
            if (COLD.equals(launcherType) && this.currentPageName.equals(GlobalStats.lastTopActivity)) {
                this.procedure.addProperty("systemRecovery", true);
                this.launcherActivityName = this.currentPageName;
                this.linksPageName.add(simpleName);
            }
            if (!TextUtils.isEmpty(safeString)) {
                this.procedure.addProperty("schemaUrl", safeString);
                OpenAppFromURL openAppFromURL = new OpenAppFromURL();
                openAppFromURL.url = safeString;
                openAppFromURL.time = j;
                DumpManager.getInstance().append(openAppFromURL);
            }
            this.procedure.addProperty("firstPageName", simpleName);
            this.procedure.stage("firstPageCreateTime", j);
            this.thisLaunchType = launcherType;
            launcherType = HOT;
            this.isLaunched = true;
        }
        if (this.linksPageName.size() < 10) {
            if (TextUtils.isEmpty(this.launcherActivityName)) {
                this.linksPageName.add(simpleName);
            }
            if (!TextUtils.isEmpty(safeString)) {
                this.linksPageUrl.add(safeString);
            }
        }
        if (TextUtils.isEmpty(this.launcherActivityName) && (PageList.isWhiteListEmpty() || PageList.inWhiteList(this.currentPageName))) {
            this.launcherActivityName = this.currentPageName;
        }
        HashMap hashMap = new HashMap(2);
        hashMap.put("timestamp", Long.valueOf(j));
        hashMap.put(UNWRouter.PAGE_NAME, simpleName);
        this.procedure.event(Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_CREATED, hashMap);
    }

    public void onActivityStarted(Activity activity, long j) {
        HashMap hashMap = new HashMap(2);
        hashMap.put("timestamp", Long.valueOf(j));
        hashMap.put(UNWRouter.PAGE_NAME, ActivityUtils.getSimpleName(activity));
        this.procedure.event(Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_STARTED, hashMap);
    }

    public void onActivityResumed(Activity activity, long j) {
        HashMap hashMap = new HashMap(2);
        hashMap.put("timestamp", Long.valueOf(j));
        hashMap.put(UNWRouter.PAGE_NAME, ActivityUtils.getSimpleName(activity));
        this.procedure.event(Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_RESUMED, hashMap);
    }

    public void onActivityPaused(Activity activity, long j) {
        HashMap hashMap = new HashMap(2);
        hashMap.put("timestamp", Long.valueOf(j));
        hashMap.put(UNWRouter.PAGE_NAME, ActivityUtils.getSimpleName(activity));
        this.procedure.event(Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_PAUSED, hashMap);
    }

    public void onActivityStopped(Activity activity, long j) {
        HashMap hashMap = new HashMap(2);
        hashMap.put("timestamp", Long.valueOf(j));
        hashMap.put(UNWRouter.PAGE_NAME, ActivityUtils.getSimpleName(activity));
        this.procedure.event(Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_STOPPED, hashMap);
        if (isTargetActivity(activity)) {
            stopProcessor();
        }
    }

    public void onActivityDestroyed(Activity activity, long j) {
        HashMap hashMap = new HashMap(2);
        hashMap.put("timestamp", Long.valueOf(j));
        hashMap.put(UNWRouter.PAGE_NAME, ActivityUtils.getSimpleName(activity));
        this.procedure.event(Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_DESTROYED, hashMap);
        if (isTargetActivity(activity)) {
            this.isFirstDraw = true;
            stopProcessor();
        }
    }

    public void onLowMemory() {
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(TimeUtils.currentTimeMillis()));
        this.procedure.event("onLowMemory", hashMap);
    }

    public void onTouch(Activity activity, MotionEvent motionEvent, long j) {
        if (this.isFirstTouch && !PageList.inBlackList(ActivityUtils.getPageName(activity))) {
            if (TextUtils.isEmpty(this.launcherActivityName)) {
                this.launcherActivityName = ActivityUtils.getPageName(activity);
            }
            if (isTargetActivity(activity)) {
                this.procedure.stage("firstInteractiveTime", j);
                this.procedure.addProperty("firstInteractiveDuration", Long.valueOf(j - this.tempLauncherStartTime));
                this.procedure.addProperty("leaveType", "touch");
                this.procedure.addProperty("errorCode", 0);
                DumpManager.getInstance().append(new FirstInteractionEvent());
                this.isFirstTouch = false;
            }
        }
    }

    public void onRenderStart(Activity activity, long j) {
        if (this.isFirstDraw && isTargetActivity(activity)) {
            this.procedure.addProperty("appInitDuration", Long.valueOf(j - this.tempLauncherStartTime));
            this.procedure.stage("renderStartTime", j);
            DumpManager.getInstance().append(new FirstDrawEvent());
            this.isFirstDraw = false;
            this.launchListener.onLaunchChanged(stringLaunchType2Code(), 0);
        }
    }

    private int stringLaunchType2Code() {
        return this.thisLaunchType.equals(COLD) ^ true ? 1 : 0;
    }

    public void onRenderPercent(Activity activity, float f, long j) {
        if (isTargetActivity(activity)) {
            this.procedure.addProperty("onRenderPercent", Float.valueOf(f));
            this.procedure.addProperty("drawPercentTime", Long.valueOf(j));
        }
    }

    public void onUsableChanged(Activity activity, int i, long j) {
        if (this.isFirstFullUsable && isTargetActivity(activity) && i == 2) {
            this.procedure.addProperty("errorCode", 0);
            this.procedure.addProperty("interactiveDuration", Long.valueOf(j - this.tempLauncherStartTime));
            this.procedure.addProperty("launchDuration", Long.valueOf(j - this.tempLauncherStartTime));
            this.procedure.stage("interactiveTime", j);
            LauncherUsableEvent launcherUsableEvent = new LauncherUsableEvent();
            launcherUsableEvent.duration = (float) (j - this.tempLauncherStartTime);
            DumpManager.getInstance().append(launcherUsableEvent);
            this.launchListener.onLaunchChanged(stringLaunchType2Code(), 2);
            sendLaunchCompleted();
            this.isFirstFullUsable = false;
        }
    }

    public void onVisibleChanged(Activity activity, int i, long j) {
        if (this.isFirstFullVisible) {
            if (i == 2 && !PageList.inBlackList(this.currentPageName) && TextUtils.isEmpty(this.launcherActivityName)) {
                this.launcherActivityName = this.currentPageName;
            }
            if (isTargetActivity(activity) && i == 2) {
                this.procedure.addProperty("displayDuration", Long.valueOf(j - this.tempLauncherStartTime));
                this.procedure.stage("displayedTime", j);
                DumpManager.getInstance().append(new DisplayedEvent());
                this.launchListener.onLaunchChanged(stringLaunchType2Code(), 1);
                this.isFirstFullVisible = false;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void stopProcessor() {
        if (!this.stopped) {
            this.stopped = true;
            sendLaunchCompleted();
            if (!TextUtils.isEmpty(this.launcherActivityName)) {
                this.procedure.addProperty("currentPageName", this.launcherActivityName.substring(this.launcherActivityName.lastIndexOf(".") + 1));
                this.procedure.addProperty("fullPageName", this.launcherActivityName);
            }
            this.procedure.addProperty("linkPageName", this.linksPageName.toString());
            this.procedure.addProperty("linkPageUrl", this.linksPageUrl.toString());
            this.linksPageName.clear();
            this.linksPageUrl.clear();
            this.procedure.addProperty("deviceLevel", Integer.valueOf(AliHAHardware.getInstance().getOutlineInfo().deviceLevel));
            this.procedure.addProperty("runtimeLevel", Integer.valueOf(AliHAHardware.getInstance().getOutlineInfo().runtimeLevel));
            this.procedure.addProperty("cpuUsageOfDevcie", Float.valueOf(AliHAHardware.getInstance().getCpuInfo().cpuUsageOfDevcie));
            this.procedure.addProperty("memoryRuntimeLevel", Integer.valueOf(AliHAHardware.getInstance().getMemoryInfo().runtimeLevel));
            this.procedure.addProperty("hasSplash", Boolean.valueOf(GlobalStats.hasSplash));
            this.procedure.addStatistic("gcCount", Integer.valueOf(this.gcCount));
            this.procedure.addStatistic(Config.TYPE_FPS, this.fpsList.toString());
            this.procedure.addStatistic("jankCount", Integer.valueOf(this.jankCount));
            this.procedure.addStatistic("image", Integer.valueOf(this.imageRequestedCount));
            this.procedure.addStatistic("imageOnRequest", Integer.valueOf(this.imageRequestedCount));
            this.procedure.addStatistic("imageSuccessCount", Integer.valueOf(this.imageSuccessCount));
            this.procedure.addStatistic("imageFailedCount", Integer.valueOf(this.imageFailedCount));
            this.procedure.addStatistic("imageCanceledCount", Integer.valueOf(this.imageCanceledCount));
            this.procedure.addStatistic("network", Integer.valueOf(this.networkRequestedCount));
            this.procedure.addStatistic("networkOnRequest", Integer.valueOf(this.networkRequestedCount));
            this.procedure.addStatistic("networkSuccessCount", Integer.valueOf(this.networkSuccessCount));
            this.procedure.addStatistic("networkFailedCount", Integer.valueOf(this.networkFailedCount));
            this.procedure.addStatistic("networkCanceledCount", Integer.valueOf(this.networkCanceledCount));
            long[] flowBean = TrafficTracker.getFlowBean();
            this.procedure.addStatistic("totalRx", Long.valueOf(flowBean[0] - this.tempTraffic[0]));
            this.procedure.addStatistic("totalTx", Long.valueOf(flowBean[1] - this.tempTraffic[1]));
            this.procedure.stage("procedureEndTime", TimeUtils.currentTimeMillis());
            GlobalStats.hasSplash = false;
            this.backgroundChangedDispatcher.removeListener(this);
            this.lowMemoryDispatcher.removeListener(this);
            this.gcDispatcher.removeListener(this);
            this.fpsDispatcher.removeListener(this);
            this.eventDispatcher.removeListener(this);
            this.usableVisibleDispatcher.removeListener(this);
            this.imageDispatcher.removeListener(this);
            this.networkDispatcher.removeListener(this);
            FragmentFunctionDispatcher.INSTANCE.removeListener(this);
            this.procedure.end();
            DumpManager.getInstance().append(new StartUpEndEvent());
            super.stopProcessor();
        }
    }

    /* access modifiers changed from: protected */
    public boolean isTargetActivity(Activity activity) {
        return ActivityUtils.getPageName(activity).equals(this.launcherActivityName);
    }

    public void fps(int i) {
        if (this.fpsList.size() < 200) {
            this.fpsList.add(Integer.valueOf(i));
        }
    }

    public void jank(int i) {
        this.jankCount += i;
    }

    public void gc() {
        this.gcCount++;
    }

    public void onChanged(int i, long j) {
        if (i == 1) {
            HashMap hashMap = new HashMap(1);
            hashMap.put("timestamp", Long.valueOf(j));
            this.procedure.event("foreground2Background", hashMap);
            stopProcessor();
        }
    }

    private void sendLaunchCompleted() {
        if (!this.sendCompleted) {
            this.launchListener.onLaunchChanged(this.thisLaunchType.equals(COLD) ^ true ? 1 : 0, 4);
            this.sendCompleted = true;
        }
    }

    public void onKey(Activity activity, KeyEvent keyEvent, long j) {
        int action = keyEvent.getAction();
        int keyCode = keyEvent.getKeyCode();
        if (action != 0) {
            return;
        }
        if (keyCode == 4 || keyCode == 3) {
            if (TextUtils.isEmpty(this.launcherActivityName)) {
                this.launcherActivityName = ActivityUtils.getPageName(activity);
            }
            if (keyCode == 3) {
                this.procedure.addProperty("leaveType", ImageStrategyConfig.HOME);
            } else {
                this.procedure.addProperty("leaveType", j.j);
            }
            HashMap hashMap = new HashMap(2);
            hashMap.put("timestamp", Long.valueOf(j));
            hashMap.put("key", Integer.valueOf(keyEvent.getKeyCode()));
            this.procedure.event("keyEvent", hashMap);
        }
    }

    public void onImageStage(int i) {
        if (i == 0) {
            this.imageRequestedCount++;
        } else if (i == 1) {
            this.imageSuccessCount++;
        } else if (i == 2) {
            this.imageFailedCount++;
        } else if (i == 3) {
            this.imageCanceledCount++;
        }
    }

    public void onNetworkStage(int i) {
        if (i == 0) {
            this.networkRequestedCount++;
        } else if (i == 1) {
            this.networkSuccessCount++;
        } else if (i == 2) {
            this.networkFailedCount++;
        } else if (i == 3) {
            this.networkCanceledCount++;
        }
    }

    public void onFunction(Activity activity, Fragment fragment, String str, long j) {
        int i;
        if (fragment != null && activity != null && TextUtils.equals(activity.getClass().getName(), this.currentPageName)) {
            String str2 = fragment.getClass().getSimpleName() + "_" + str;
            Integer num = this.functionCounts.get(str2);
            if (num == null) {
                i = 0;
            } else {
                i = Integer.valueOf(num.intValue() + 1);
            }
            this.functionCounts.put(str2, i);
            this.procedure.stage(str2 + i, j);
        }
    }
}
