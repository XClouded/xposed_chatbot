package com.taobao.monitor.impl.processor.pageload;

import alimama.com.unwrouter.UNWRouter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MotionEvent;
import com.huawei.updatesdk.service.otaupdate.UpdateKey;
import com.taobao.android.tlog.protocol.Constants;
import com.taobao.monitor.impl.common.APMContext;
import com.taobao.monitor.impl.data.GlobalStats;
import com.taobao.monitor.impl.data.traffic.TrafficTracker;
import com.taobao.monitor.impl.processor.AbsProcessor;
import com.taobao.monitor.impl.processor.pageload.PageModelLifecycle;
import com.taobao.monitor.impl.trace.ActivityEventDispatcher;
import com.taobao.monitor.impl.trace.ApplicationGCDispatcher;
import com.taobao.monitor.impl.trace.ApplicationLowMemoryDispatcher;
import com.taobao.monitor.impl.trace.FPSDispatcher;
import com.taobao.monitor.impl.trace.IDispatcher;
import com.taobao.monitor.impl.util.ActivityUtils;
import com.taobao.monitor.impl.util.TimeUtils;
import com.taobao.monitor.impl.util.TopicUtils;
import com.taobao.monitor.procedure.IProcedure;
import com.taobao.monitor.procedure.ProcedureConfig;
import com.taobao.monitor.procedure.ProcedureFactoryProxy;
import com.taobao.weex.analyzer.Config;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@TargetApi(16)
public class PageLoadPopProcessor extends AbsProcessor implements PageModelLifecycle.IPopLifeCycle, ApplicationLowMemoryDispatcher.LowMemoryListener, FPSDispatcher.FPSListener, ApplicationGCDispatcher.ApplicationGCListener, ActivityEventDispatcher.OnEventListener {
    private static final String TAG = "PageLoadPopProcessor";
    private IDispatcher eventDispatcher;
    private IDispatcher fpsDispatcher;
    private List<Integer> fpsList = new ArrayList();
    private int gcCount = 0;
    private IDispatcher gcDispatcher;
    private boolean isFirstTouch = true;
    private int jankCount = 0;
    private long lastOnStartTime = -1;
    private long loadStartTime;
    private IDispatcher lowMemoryDispatcher;
    private String pageName;
    private IProcedure procedure;
    private Activity targetPageActivity = null;
    private long[] totalTraffic = new long[2];
    private long totalVisibleDuration = 0;

    public PageLoadPopProcessor() {
        super(false);
    }

    /* access modifiers changed from: protected */
    public void startProcessor() {
        super.startProcessor();
        this.procedure = ProcedureFactoryProxy.PROXY.createProcedure(TopicUtils.getFullTopic("/pageLoad"), new ProcedureConfig.Builder().setIndependent(false).setUpload(true).setParentNeedStats(false).setParent((IProcedure) null).build());
        this.procedure.begin();
        this.eventDispatcher = getDispatcher(APMContext.ACTIVITY_EVENT_DISPATCHER);
        this.lowMemoryDispatcher = getDispatcher(APMContext.APPLICATION_LOW_MEMORY_DISPATCHER);
        this.fpsDispatcher = getDispatcher(APMContext.ACTIVITY_FPS_DISPATCHER);
        this.gcDispatcher = getDispatcher(APMContext.APPLICATION_GC_DISPATCHER);
        this.gcDispatcher.addListener(this);
        this.lowMemoryDispatcher.addListener(this);
        this.eventDispatcher.addListener(this);
        this.fpsDispatcher.addListener(this);
        initLauncherProperties();
    }

    private void initLauncherProperties() {
        this.procedure.stage("procedureStartTime", TimeUtils.currentTimeMillis());
        this.procedure.addProperty("errorCode", 1);
        this.procedure.addProperty(UpdateKey.MARKET_INSTALL_TYPE, GlobalStats.installType);
    }

    private void initPageProperties(Activity activity) {
        this.pageName = ActivityUtils.getSimpleName(activity);
        this.procedure.addProperty(UNWRouter.PAGE_NAME, this.pageName);
        this.procedure.addProperty("fullPageName", activity.getClass().getName());
        Intent intent = activity.getIntent();
        if (intent != null) {
            String dataString = intent.getDataString();
            if (TextUtils.isEmpty(dataString)) {
                this.procedure.addProperty("schemaUrl", dataString);
            }
        }
        this.procedure.addProperty("isInterpretiveExecution", false);
        this.procedure.addProperty("isFirstLaunch", Boolean.valueOf(GlobalStats.isFirstLaunch));
        this.procedure.addProperty("isFirstLoad", Boolean.valueOf(GlobalStats.activityStatusManager.isFirst(ActivityUtils.getPageName(activity))));
        this.procedure.addProperty("jumpTime", Long.valueOf(GlobalStats.jumpTime));
        this.procedure.addProperty("lastValidTime", Long.valueOf(GlobalStats.lastValidTime));
        this.procedure.addProperty("lastValidPage", GlobalStats.lastValidPage);
        this.procedure.addProperty("loadType", "pop");
    }

    public void onActivityStarted(Activity activity) {
        startProcessor();
        this.loadStartTime = TimeUtils.currentTimeMillis();
        initPageProperties(activity);
        this.lastOnStartTime = this.loadStartTime;
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(TimeUtils.currentTimeMillis()));
        this.procedure.event(Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_STARTED, hashMap);
        long[] flowBean = TrafficTracker.getFlowBean();
        this.totalTraffic[0] = flowBean[0];
        this.totalTraffic[1] = flowBean[1];
        this.procedure.stage("loadStartTime", this.loadStartTime);
        long currentTimeMillis = TimeUtils.currentTimeMillis();
        this.procedure.addProperty("pageInitDuration", Long.valueOf(currentTimeMillis - this.loadStartTime));
        this.procedure.stage("renderStartTime", currentTimeMillis);
        long currentTimeMillis2 = TimeUtils.currentTimeMillis();
        this.procedure.addProperty("interactiveDuration", Long.valueOf(currentTimeMillis2 - this.loadStartTime));
        this.procedure.addProperty("loadDuration", Long.valueOf(currentTimeMillis2 - this.loadStartTime));
        this.procedure.stage("interactiveTime", currentTimeMillis2);
        this.procedure.addProperty("displayDuration", Long.valueOf(TimeUtils.currentTimeMillis() - this.loadStartTime));
        this.procedure.stage("displayedTime", this.loadStartTime);
    }

    public void onActivityStopped(Activity activity) {
        this.totalVisibleDuration += TimeUtils.currentTimeMillis() - this.lastOnStartTime;
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(TimeUtils.currentTimeMillis()));
        this.procedure.event(Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_STOPPED, hashMap);
        long[] flowBean = TrafficTracker.getFlowBean();
        this.totalTraffic[0] = flowBean[0] - this.totalTraffic[0];
        this.totalTraffic[1] = flowBean[1] - this.totalTraffic[1];
        this.procedure.addProperty("totalVisibleDuration", Long.valueOf(this.totalVisibleDuration));
        this.procedure.addProperty("errorCode", 0);
        this.procedure.addStatistic("totalRx", Long.valueOf(this.totalTraffic[0]));
        this.procedure.addStatistic("totalTx", Long.valueOf(this.totalTraffic[1]));
        stopProcessor();
    }

    public void onLowMemory() {
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(TimeUtils.currentTimeMillis()));
        this.procedure.event("onLowMemory", hashMap);
    }

    public void onTouch(Activity activity, MotionEvent motionEvent, long j) {
        if (activity == this.targetPageActivity && this.isFirstTouch) {
            this.procedure.stage("firstInteractiveTime", j);
            this.procedure.addProperty("firstInteractiveDuration", Long.valueOf(j - this.loadStartTime));
            this.isFirstTouch = false;
        }
    }

    /* access modifiers changed from: protected */
    public void stopProcessor() {
        this.procedure.stage("procedureEndTime", TimeUtils.currentTimeMillis());
        this.procedure.addStatistic("gcCount", Integer.valueOf(this.gcCount));
        this.procedure.addStatistic(Config.TYPE_FPS, this.fpsList.toString());
        this.procedure.addStatistic("jankCount", Integer.valueOf(this.jankCount));
        this.lowMemoryDispatcher.removeListener(this);
        this.eventDispatcher.removeListener(this);
        this.fpsDispatcher.removeListener(this);
        this.gcDispatcher.removeListener(this);
        this.procedure.end();
        super.stopProcessor();
    }

    public void fps(int i) {
        if (this.fpsList.size() < 60) {
            this.fpsList.add(Integer.valueOf(i));
        }
    }

    public void jank(int i) {
        this.jankCount += i;
    }

    public void gc() {
        this.gcCount++;
    }

    public void onKey(Activity activity, KeyEvent keyEvent, long j) {
        if (keyEvent.getAction() != 0) {
            return;
        }
        if (keyEvent.getKeyCode() == 4 || keyEvent.getKeyCode() == 3) {
            HashMap hashMap = new HashMap(2);
            hashMap.put("timestamp", Long.valueOf(j));
            hashMap.put("key", Integer.valueOf(keyEvent.getKeyCode()));
            this.procedure.event("keyEvent", hashMap);
        }
    }
}
