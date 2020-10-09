package com.taobao.monitor.impl.processor.fragmentload;

import alimama.com.unwrouter.UNWRouter;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.ali.ha.fulltrace.dump.DumpManager;
import com.ali.ha.fulltrace.event.DisplayedEvent;
import com.ali.ha.fulltrace.event.FPSEvent;
import com.ali.ha.fulltrace.event.FinishLoadPageEvent;
import com.ali.ha.fulltrace.event.OpenPageEvent;
import com.ali.ha.fulltrace.event.UsableEvent;
import com.alipay.sdk.widget.j;
import com.huawei.updatesdk.service.otaupdate.UpdateKey;
import com.taobao.monitor.impl.common.APMContext;
import com.taobao.monitor.impl.data.GlobalStats;
import com.taobao.monitor.impl.data.OnUsableVisibleListener;
import com.taobao.monitor.impl.data.traffic.TrafficTracker;
import com.taobao.monitor.impl.processor.AbsProcessor;
import com.taobao.monitor.impl.processor.fragmentload.FragmentModelLifecycle;
import com.taobao.monitor.impl.processor.pageload.ProcedureManagerSetter;
import com.taobao.monitor.impl.trace.ActivityEventDispatcher;
import com.taobao.monitor.impl.trace.ApplicationBackgroundChangedDispatcher;
import com.taobao.monitor.impl.trace.ApplicationGCDispatcher;
import com.taobao.monitor.impl.trace.ApplicationLowMemoryDispatcher;
import com.taobao.monitor.impl.trace.FPSDispatcher;
import com.taobao.monitor.impl.trace.IDispatcher;
import com.taobao.monitor.impl.trace.ImageStageDispatcher;
import com.taobao.monitor.impl.trace.NetworkStageDispatcher;
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

class FragmentProcessor extends AbsProcessor implements FragmentModelLifecycle.IFragmentLoadLifeCycle, ApplicationLowMemoryDispatcher.LowMemoryListener, OnUsableVisibleListener<Fragment>, FPSDispatcher.FPSListener, ApplicationGCDispatcher.ApplicationGCListener, ApplicationBackgroundChangedDispatcher.BackgroundChangedListener, ActivityEventDispatcher.OnEventListener, ImageStageDispatcher.IImageStageListener, NetworkStageDispatcher.INetworkStageListener {
    private static final String TAG = "FragmentProcessor";
    private IDispatcher backgroundChangedDispatcher;
    private IDispatcher eventDispatcher;
    private IDispatcher fpsDispatcher;
    private FPSEvent fpsEvent = new FPSEvent();
    private List<Integer> fpsList = new ArrayList();
    private int fpsLoadIndex = 0;
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
    private Fragment targetFragment = null;
    private long[] tempTraffic;
    private long[] totalTraffic = new long[2];
    private long totalVisibleDuration = 0;
    private IDispatcher usableVisibleDispatcher;

    public FragmentProcessor() {
        super(false);
    }

    /* access modifiers changed from: protected */
    public void startProcessor() {
        super.startProcessor();
        this.procedure = ProcedureFactoryProxy.PROXY.createProcedure(TopicUtils.getFullTopic("/pageLoad"), new ProcedureConfig.Builder().setIndependent(false).setUpload(true).setParentNeedStats(true).setParent((IProcedure) null).build());
        this.procedure.begin();
        this.eventDispatcher = getDispatcher(APMContext.ACTIVITY_EVENT_DISPATCHER);
        this.lowMemoryDispatcher = getDispatcher(APMContext.APPLICATION_LOW_MEMORY_DISPATCHER);
        this.usableVisibleDispatcher = getDispatcher(APMContext.FRAGMENT_USABLE_VISIBLE_DISPATCHER);
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

    private void initPageProperties(Fragment fragment) {
        this.pageName = fragment.getClass().getSimpleName();
        if (fragment instanceof IPageNameTransfer) {
            this.procedure.addProperty(UNWRouter.PAGE_NAME, ((IPageNameTransfer) fragment).alias());
            this.procedure.addProperty("container", this.pageName);
        } else {
            this.procedure.addProperty(UNWRouter.PAGE_NAME, this.pageName);
        }
        this.procedure.addProperty("fullPageName", fragment.getClass().getName());
        FragmentActivity activity = fragment.getActivity();
        if (activity != null) {
            Intent intent = activity.getIntent();
            if (intent != null) {
                String dataString = intent.getDataString();
                if (!TextUtils.isEmpty(dataString)) {
                    this.procedure.addProperty("schemaUrl", dataString);
                }
            }
            this.procedure.addProperty("activityName", activity.getClass().getSimpleName());
        }
        this.procedure.addProperty("isInterpretiveExecution", false);
        this.procedure.addProperty("isFirstLaunch", Boolean.valueOf(GlobalStats.isFirstLaunch));
        this.procedure.addProperty("isFirstLoad", Boolean.valueOf(GlobalStats.activityStatusManager.isFirst(fragment.getClass().getName())));
        this.procedure.addProperty("lastValidTime", Long.valueOf(GlobalStats.lastValidTime));
        this.procedure.addProperty("lastValidPage", GlobalStats.lastValidPage);
        this.procedure.addProperty("loadType", "push");
    }

    public void onLowMemory() {
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(TimeUtils.currentTimeMillis()));
        this.procedure.event("onLowMemory", hashMap);
    }

    public void onTouch(Activity activity, MotionEvent motionEvent, long j) {
        if (this.targetFragment != null) {
            try {
                if (activity == this.targetFragment.getActivity() && this.isFirstTouch) {
                    this.procedure.stage("firstInteractiveTime", j);
                    this.procedure.addProperty("firstInteractiveDuration", Long.valueOf(j - this.loadStartTime));
                    this.procedure.addProperty("leaveType", "touch");
                    this.procedure.addProperty("errorCode", 0);
                    this.isFirstTouch = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onRenderStart(Fragment fragment, long j) {
        if (this.isFirstDraw && fragment == this.targetFragment) {
            this.procedure.addProperty("pageInitDuration", Long.valueOf(j - this.loadStartTime));
            this.procedure.stage("renderStartTime", j);
            this.isFirstDraw = false;
        }
    }

    public void onRenderPercent(Fragment fragment, float f, long j) {
        if (fragment == this.targetFragment) {
            this.procedure.addProperty("onRenderPercent", Float.valueOf(f));
            this.procedure.addProperty("drawPercentTime", Long.valueOf(j));
        }
    }

    public void onUsableChanged(Fragment fragment, int i, long j) {
        if (this.isFirstFullUsable && fragment == this.targetFragment && i == 2) {
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

    public void onVisibleChanged(Fragment fragment, int i, long j) {
        if (this.isFirstFullVisible && fragment == this.targetFragment && i == 2) {
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
        }
    }

    public void gc() {
        if (this.isVisible) {
            this.gcCount++;
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
        if (this.targetFragment != null) {
            FragmentActivity fragmentActivity = null;
            try {
                fragmentActivity = this.targetFragment.getActivity();
            } catch (Exception unused) {
            }
            if (activity == fragmentActivity) {
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
    }

    public void onFragmentPreAttached(Fragment fragment, long j) {
        startProcessor();
        ProcedureManagerSetter.instance().setCurrentFragmentProcedure(this.procedure);
        this.procedure.stage("loadStartTime", j);
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(j));
        this.procedure.event("onFragmentPreAttached", hashMap);
        this.targetFragment = fragment;
        this.loadStartTime = j;
        initPageProperties(fragment);
        this.tempTraffic = TrafficTracker.getFlowBean();
        OpenPageEvent openPageEvent = new OpenPageEvent();
        openPageEvent.pageName = fragment.getClass().getSimpleName();
        DumpManager.getInstance().append(openPageEvent);
    }

    public void onFragmentAttached(Fragment fragment, long j) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(j));
        this.procedure.event("onFragmentAttached", hashMap);
    }

    public void onFragmentPreCreated(Fragment fragment, long j) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(j));
        this.procedure.event("onFragmentPreCreated", hashMap);
    }

    public void onFragmentCreated(Fragment fragment, long j) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(j));
        this.procedure.event("onFragmentCreated", hashMap);
    }

    public void onFragmentActivityCreated(Fragment fragment, long j) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(j));
        this.procedure.event("onFragmentActivityCreated", hashMap);
    }

    public void onFragmentViewCreated(Fragment fragment, long j) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(j));
        this.procedure.event("onFragmentViewCreated", hashMap);
    }

    public void onFragmentStarted(Fragment fragment, long j) {
        ProcedureManagerSetter.instance().setCurrentFragmentProcedure(this.procedure);
        this.isVisible = true;
        this.lastOnStartTime = j;
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(j));
        this.procedure.event("onFragmentStarted", hashMap);
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

    public void onFragmentResumed(Fragment fragment, long j) {
        ProcedureManagerSetter.instance().setCurrentFragmentProcedure(this.procedure);
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(j));
        this.procedure.event("onFragmentResumed", hashMap);
    }

    public void onFragmentPaused(Fragment fragment, long j) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(j));
        this.procedure.event("onFragmentPaused", hashMap);
    }

    public void onFragmentStopped(Fragment fragment, long j) {
        this.isVisible = false;
        this.totalVisibleDuration += j - this.lastOnStartTime;
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(j));
        this.procedure.event("onFragmentStopped", hashMap);
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

    public void onFragmentSaveInstanceState(Fragment fragment, long j) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(j));
        this.procedure.event("onFragmentSaveInstanceState", hashMap);
    }

    public void onFragmentViewDestroyed(Fragment fragment, long j) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(j));
        this.procedure.event("onFragmentViewDestroyed", hashMap);
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

    public void onFragmentDestroyed(Fragment fragment, long j) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(j));
        this.procedure.event("onFragmentDestroyed", hashMap);
    }

    public void onFragmentDetached(Fragment fragment, long j) {
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(j));
        this.procedure.event("onFragmentDetached", hashMap);
        long[] flowBean = TrafficTracker.getFlowBean();
        long[] jArr = this.totalTraffic;
        jArr[0] = jArr[0] + (flowBean[0] - this.tempTraffic[0]);
        long[] jArr2 = this.totalTraffic;
        jArr2[1] = jArr2[1] + (flowBean[1] - this.tempTraffic[1]);
        FinishLoadPageEvent finishLoadPageEvent = new FinishLoadPageEvent();
        finishLoadPageEvent.pageName = fragment.getClass().getSimpleName();
        DumpManager.getInstance().append(finishLoadPageEvent);
        stopProcessor();
    }
}
