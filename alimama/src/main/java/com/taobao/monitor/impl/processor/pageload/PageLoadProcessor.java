package com.taobao.monitor.impl.processor.pageload;

import alimama.com.unwrouter.UNWRouter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import androidx.fragment.app.Fragment;
import com.ali.alihadeviceevaluator.AliHAHardware;
import com.ali.ha.fulltrace.dump.DumpManager;
import com.ali.ha.fulltrace.event.DisplayedEvent;
import com.ali.ha.fulltrace.event.FPSEvent;
import com.ali.ha.fulltrace.event.FinishLoadPageEvent;
import com.ali.ha.fulltrace.event.GCEvent;
import com.ali.ha.fulltrace.event.JankEvent;
import com.ali.ha.fulltrace.event.OpenPageEvent;
import com.ali.ha.fulltrace.event.ReceiverLowMemoryEvent;
import com.ali.ha.fulltrace.event.UsableEvent;
import com.alipay.sdk.widget.j;
import com.huawei.updatesdk.service.otaupdate.UpdateKey;
import com.taobao.android.nav.Nav;
import com.taobao.android.tlog.protocol.Constants;
import com.taobao.monitor.impl.common.APMContext;
import com.taobao.monitor.impl.data.GlobalStats;
import com.taobao.monitor.impl.data.OnUsableVisibleListener;
import com.taobao.monitor.impl.data.traffic.TrafficTracker;
import com.taobao.monitor.impl.processor.AbsProcessor;
import com.taobao.monitor.impl.processor.pageload.PageModelLifecycle;
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
import com.taobao.monitor.procedure.IPageNameTransfer;
import com.taobao.monitor.procedure.IProcedure;
import com.taobao.monitor.procedure.ProcedureConfig;
import com.taobao.monitor.procedure.ProcedureFactoryProxy;
import com.taobao.tao.image.ImageStrategyConfig;
import com.taobao.weex.analyzer.Config;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@TargetApi(16)
public class PageLoadProcessor extends AbsProcessor implements PageModelLifecycle.IPageLoadLifeCycle, ApplicationLowMemoryDispatcher.LowMemoryListener, OnUsableVisibleListener<Activity>, FragmentFunctionListener, FPSDispatcher.FPSListener, ApplicationGCDispatcher.ApplicationGCListener, ApplicationBackgroundChangedDispatcher.BackgroundChangedListener, ActivityEventDispatcher.OnEventListener, ImageStageDispatcher.IImageStageListener, NetworkStageDispatcher.INetworkStageListener {
    private static final String TAG = "PageLoadProcessor";
    private static String lastPage = "";
    private static List<String> linksPage = new ArrayList(4);
    private IDispatcher backgroundChangedDispatcher;
    private IDispatcher eventDispatcher;
    private IDispatcher fpsDispatcher;
    private FPSEvent fpsEvent = new FPSEvent();
    private List<Integer> fpsList = new ArrayList();
    private int fpsLoadIndex = 0;
    private HashMap<String, Integer> functionCounts = new HashMap<>();
    private int gcCount = 0;
    private IDispatcher gcDispatcher;
    private int imageCanceledCount;
    private IDispatcher imageDispatcher;
    private int imageFailedCount;
    private int imageRequestedCount;
    private int imageSuccessCount;
    private boolean isFirst = true;
    private boolean isFirstDraw = true;
    private boolean isFirstFullUsable = true;
    private boolean isFirstFullVisible = true;
    private boolean isFirstTouch = true;
    private boolean isVisible = true;
    private int jankCount = 0;
    private long lastOnStartTime = -1;
    private long loadStartTime;
    private IDispatcher lowMemoryDispatcher;
    private int networkCanceledCount;
    private IDispatcher networkDispatcher;
    private int networkFailedCount;
    private int networkRequestedCount;
    private int networkSuccessCount;
    private String pageName;
    private IProcedure procedure;
    private boolean stopped = false;
    private Activity targetPageActivity = null;
    private long[] tempTraffic;
    private long[] totalTraffic = new long[2];
    private long totalVisibleDuration = 0;
    private IDispatcher usableVisibleDispatcher;

    public PageLoadProcessor() {
        super(false);
    }

    /* access modifiers changed from: protected */
    public void startProcessor() {
        super.startProcessor();
        this.procedure = ProcedureFactoryProxy.PROXY.createProcedure(TopicUtils.getFullTopic("/pageLoad"), new ProcedureConfig.Builder().setIndependent(false).setUpload(true).setParentNeedStats(true).setParent((IProcedure) null).build());
        this.procedure.begin();
        this.eventDispatcher = getDispatcher(APMContext.ACTIVITY_EVENT_DISPATCHER);
        this.lowMemoryDispatcher = getDispatcher(APMContext.APPLICATION_LOW_MEMORY_DISPATCHER);
        this.usableVisibleDispatcher = getDispatcher(APMContext.ACTIVITY_USABLE_VISIBLE_DISPATCHER);
        this.fpsDispatcher = getDispatcher(APMContext.ACTIVITY_FPS_DISPATCHER);
        this.gcDispatcher = getDispatcher(APMContext.APPLICATION_GC_DISPATCHER);
        this.backgroundChangedDispatcher = getDispatcher(APMContext.APPLICATION_BACKGROUND_CHANGED_DISPATCHER);
        this.networkDispatcher = getDispatcher(APMContext.NETWORK_STAGE_DISPATCHER);
        this.imageDispatcher = getDispatcher(APMContext.IMAGE_STAGE_DISPATCHER);
        this.gcDispatcher.addListener(this);
        this.lowMemoryDispatcher.addListener(this);
        this.eventDispatcher.addListener(this);
        this.usableVisibleDispatcher.addListener(this);
        this.fpsDispatcher.addListener(this);
        this.backgroundChangedDispatcher.addListener(this);
        this.networkDispatcher.addListener(this);
        this.imageDispatcher.addListener(this);
        FragmentFunctionDispatcher.INSTANCE.addListener(this);
        initLauncherProperties();
        this.totalTraffic[0] = 0;
        this.totalTraffic[1] = 0;
    }

    private void initLauncherProperties() {
        this.procedure.stage("procedureStartTime", TimeUtils.currentTimeMillis());
        this.procedure.addProperty("errorCode", 1);
        this.procedure.addProperty(UpdateKey.MARKET_INSTALL_TYPE, GlobalStats.installType);
        this.procedure.addProperty("leaveType", "other");
    }

    public void onActivityCreated(Activity activity, Map<String, Object> map, long j) {
        this.loadStartTime = j;
        startProcessor();
        this.procedure.stage("loadStartTime", this.loadStartTime);
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(this.loadStartTime));
        this.procedure.event(Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_CREATED, hashMap);
        this.targetPageActivity = activity;
        ProcedureManagerSetter.instance().setCurrentActivityProcedure(this.procedure);
        initPageProperties(activity, map);
        this.tempTraffic = TrafficTracker.getFlowBean();
        OpenPageEvent openPageEvent = new OpenPageEvent();
        openPageEvent.pageName = ActivityUtils.getSimpleName(activity);
        DumpManager.getInstance().append(openPageEvent);
    }

    private void initPageProperties(Activity activity, Map<String, Object> map) {
        this.pageName = ActivityUtils.getSimpleName(activity);
        if (linksPage.size() < 10) {
            linksPage.add(this.pageName);
        }
        if (activity instanceof IPageNameTransfer) {
            this.procedure.addProperty(UNWRouter.PAGE_NAME, ((IPageNameTransfer) activity).alias());
            this.procedure.addProperty("container", this.pageName);
        } else {
            this.procedure.addProperty(UNWRouter.PAGE_NAME, this.pageName);
        }
        this.procedure.addProperty("fullPageName", ActivityUtils.getPageName(activity));
        if (!TextUtils.isEmpty(lastPage)) {
            this.procedure.addProperty("fromPageName", lastPage);
        }
        try {
            Object obj = map.get("schemaUrl");
            if (obj != null) {
                this.procedure.addProperty("schemaUrl", obj);
            }
            this.procedure.addProperty("navStartTime", SafeUtils.getSafeString(map.get(Nav.NAV_TO_URL_START_TIME), "-1"));
            this.procedure.addProperty("navStartActivityTime", SafeUtils.getSafeString(map.get(Nav.NAV_START_ACTIVITY_TIME), "-1"));
        } catch (Throwable th) {
            th.printStackTrace();
        }
        this.procedure.addProperty("isFirstLaunch", Boolean.valueOf(GlobalStats.isFirstLaunch));
        this.procedure.addProperty("isFirstLoad", Boolean.valueOf(GlobalStats.activityStatusManager.isFirst(ActivityUtils.getPageName(activity))));
        this.procedure.addProperty("jumpTime", Long.valueOf(GlobalStats.jumpTime));
        GlobalStats.jumpTime = -1;
        this.procedure.addProperty("lastValidTime", Long.valueOf(GlobalStats.lastValidTime));
        this.procedure.addProperty("lastValidLinksPage", linksPage.toString());
        this.procedure.addProperty("lastValidPage", GlobalStats.lastValidPage);
        this.procedure.addProperty("loadType", "push");
    }

    public void onActivityStarted(Activity activity, long j) {
        this.isVisible = true;
        this.lastOnStartTime = j;
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(j));
        this.procedure.event(Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_STARTED, hashMap);
        ProcedureManagerSetter.instance().setCurrentActivityProcedure(this.procedure);
        lastPage = this.pageName;
        if (this.isFirst) {
            this.isFirst = false;
            long[] flowBean = TrafficTracker.getFlowBean();
            long[] jArr = this.totalTraffic;
            jArr[0] = jArr[0] + (flowBean[0] - this.tempTraffic[0]);
            long[] jArr2 = this.totalTraffic;
            jArr2[1] = jArr2[1] + (flowBean[1] - this.tempTraffic[1]);
        }
        this.tempTraffic = TrafficTracker.getFlowBean();
        GlobalStats.lastValidPage = this.pageName;
        GlobalStats.lastValidTime = j;
    }

    public void onActivityResumed(Activity activity, long j) {
        ProcedureManagerSetter.instance().setCurrentActivityProcedure(this.procedure);
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(j));
        this.procedure.event(Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_RESUMED, hashMap);
    }

    public void onActivityPaused(Activity activity, long j) {
        this.isVisible = false;
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(j));
        this.procedure.event(Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_PAUSED, hashMap);
    }

    public void onActivityStopped(Activity activity, long j) {
        this.totalVisibleDuration += j - this.lastOnStartTime;
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(j));
        this.procedure.event(Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_STOPPED, hashMap);
        long[] flowBean = TrafficTracker.getFlowBean();
        long[] jArr = this.totalTraffic;
        jArr[0] = jArr[0] + (flowBean[0] - this.tempTraffic[0]);
        long[] jArr2 = this.totalTraffic;
        jArr2[1] = jArr2[1] + (flowBean[1] - this.tempTraffic[1]);
        this.tempTraffic = flowBean;
        if (this.fpsList != null && this.fpsLoadIndex > this.fpsList.size()) {
            Integer num = 0;
            for (int i = this.fpsLoadIndex; i < this.fpsList.size(); i++) {
                num = Integer.valueOf(num.intValue() + this.fpsList.get(i).intValue());
            }
            this.fpsEvent.averageUseFps = (float) (num.intValue() / (this.fpsList.size() - this.fpsLoadIndex));
        }
        DumpManager.getInstance().append(this.fpsEvent);
    }

    public void onActivityDestroyed(Activity activity, long j) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(j));
        this.procedure.event(Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_DESTROYED, hashMap);
        long[] flowBean = TrafficTracker.getFlowBean();
        long[] jArr = this.totalTraffic;
        jArr[0] = jArr[0] + (flowBean[0] - this.tempTraffic[0]);
        long[] jArr2 = this.totalTraffic;
        jArr2[1] = jArr2[1] + (flowBean[1] - this.tempTraffic[1]);
        FinishLoadPageEvent finishLoadPageEvent = new FinishLoadPageEvent();
        finishLoadPageEvent.pageName = ActivityUtils.getSimpleName(activity);
        DumpManager.getInstance().append(finishLoadPageEvent);
        stopProcessor();
    }

    public void onLowMemory() {
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(TimeUtils.currentTimeMillis()));
        this.procedure.event("onLowMemory", hashMap);
        ReceiverLowMemoryEvent receiverLowMemoryEvent = new ReceiverLowMemoryEvent();
        receiverLowMemoryEvent.level = 1.0f;
        DumpManager.getInstance().append(receiverLowMemoryEvent);
    }

    public void onTouch(Activity activity, MotionEvent motionEvent, long j) {
        if (activity == this.targetPageActivity) {
            if (this.isFirstTouch) {
                this.procedure.stage("firstInteractiveTime", j);
                this.procedure.addProperty("firstInteractiveDuration", Long.valueOf(j - this.loadStartTime));
                this.procedure.addProperty("leaveType", "touch");
                this.isFirstTouch = false;
                this.procedure.addProperty("errorCode", 0);
            }
            linksPage.clear();
            linksPage.add(this.pageName);
            GlobalStats.lastValidPage = this.pageName;
            GlobalStats.lastValidTime = j;
        }
    }

    public void onRenderStart(Activity activity, long j) {
        if (this.isFirstDraw && activity == this.targetPageActivity) {
            this.procedure.addProperty("pageInitDuration", Long.valueOf(j - this.loadStartTime));
            this.procedure.stage("renderStartTime", j);
            this.isFirstDraw = false;
        }
    }

    public void onRenderPercent(Activity activity, float f, long j) {
        if (activity == this.targetPageActivity) {
            this.procedure.addProperty("onRenderPercent", Float.valueOf(f));
            this.procedure.addProperty("drawPercentTime", Long.valueOf(j));
        }
    }

    public void onUsableChanged(Activity activity, int i, long j) {
        if (this.isFirstFullUsable && activity == this.targetPageActivity && i == 2) {
            this.procedure.addProperty("interactiveDuration", Long.valueOf(j - this.loadStartTime));
            this.procedure.addProperty("loadDuration", Long.valueOf(j - this.loadStartTime));
            this.procedure.stage("interactiveTime", j);
            this.procedure.addProperty("errorCode", 0);
            this.procedure.addStatistic("totalRx", Long.valueOf(this.totalTraffic[0]));
            this.procedure.addStatistic("totalTx", Long.valueOf(this.totalTraffic[1]));
            this.isFirstFullUsable = false;
            UsableEvent usableEvent = new UsableEvent();
            usableEvent.duration = (float) (j - this.loadStartTime);
            DumpManager.getInstance().append(usableEvent);
            if (this.fpsList != null && this.fpsList.size() != 0) {
                Integer num = 0;
                for (Integer intValue : this.fpsList) {
                    num = Integer.valueOf(num.intValue() + intValue.intValue());
                }
                this.fpsEvent.averageLoadFps = (float) (num.intValue() / this.fpsList.size());
                this.fpsLoadIndex = this.fpsList.size();
            }
        }
    }

    public void onVisibleChanged(Activity activity, int i, long j) {
        if (this.isFirstFullVisible && activity == this.targetPageActivity && i == 2) {
            this.procedure.addProperty("displayDuration", Long.valueOf(j - this.loadStartTime));
            this.procedure.stage("displayedTime", j);
            DumpManager.getInstance().append(new DisplayedEvent());
            this.isFirstFullVisible = false;
        }
    }

    /* access modifiers changed from: protected */
    public void stopProcessor() {
        if (!this.stopped) {
            this.stopped = true;
            this.procedure.addProperty("totalVisibleDuration", Long.valueOf(this.totalVisibleDuration));
            this.procedure.addProperty("deviceLevel", Integer.valueOf(AliHAHardware.getInstance().getOutlineInfo().deviceLevel));
            this.procedure.addProperty("runtimeLevel", Integer.valueOf(AliHAHardware.getInstance().getOutlineInfo().runtimeLevel));
            this.procedure.addProperty("cpuUsageOfDevcie", Float.valueOf(AliHAHardware.getInstance().getCpuInfo().cpuUsageOfDevcie));
            this.procedure.addProperty("memoryRuntimeLevel", Integer.valueOf(AliHAHardware.getInstance().getMemoryInfo().runtimeLevel));
            this.procedure.stage("procedureEndTime", TimeUtils.currentTimeMillis());
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
            this.lowMemoryDispatcher.removeListener(this);
            this.eventDispatcher.removeListener(this);
            this.usableVisibleDispatcher.removeListener(this);
            this.fpsDispatcher.removeListener(this);
            this.gcDispatcher.removeListener(this);
            this.backgroundChangedDispatcher.removeListener(this);
            this.imageDispatcher.removeListener(this);
            this.networkDispatcher.removeListener(this);
            FragmentFunctionDispatcher.INSTANCE.removeListener(this);
            this.procedure.end();
            super.stopProcessor();
        }
    }

    public void fps(int i) {
        if (this.fpsList.size() < 200 && this.isVisible) {
            this.fpsList.add(Integer.valueOf(i));
        }
    }

    public void jank(int i) {
        if (this.isVisible) {
            this.jankCount += i;
            DumpManager.getInstance().append(new JankEvent());
        }
    }

    public void gc() {
        if (this.isVisible) {
            this.gcCount++;
            DumpManager.getInstance().append(new GCEvent());
        }
    }

    public void onChanged(int i, long j) {
        if (i == 1) {
            HashMap hashMap = new HashMap(1);
            hashMap.put("timestamp", Long.valueOf(j));
            this.procedure.event("foreground2Background", hashMap);
            stopProcessor();
            return;
        }
        HashMap hashMap2 = new HashMap(1);
        hashMap2.put("timestamp", Long.valueOf(j));
        this.procedure.event("background2Foreground", hashMap2);
    }

    public void onKey(Activity activity, KeyEvent keyEvent, long j) {
        if (activity == this.targetPageActivity) {
            int action = keyEvent.getAction();
            int keyCode = keyEvent.getKeyCode();
            if (action != 0) {
                return;
            }
            if (keyCode == 4 || keyCode == 3) {
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
    }

    public void onImageStage(int i) {
        if (!this.isVisible) {
            return;
        }
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
        if (!this.isVisible) {
            return;
        }
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
        if (fragment != null && activity == this.targetPageActivity) {
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
