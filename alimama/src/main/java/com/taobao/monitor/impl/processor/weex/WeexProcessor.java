package com.taobao.monitor.impl.processor.weex;

import android.app.Activity;
import com.ali.alihadeviceevaluator.AliHAHardware;
import com.alibaba.android.bindingx.core.internal.BindingXConstants;
import com.taobao.monitor.impl.common.APMContext;
import com.taobao.monitor.impl.common.Global;
import com.taobao.monitor.impl.data.OnUsableVisibleListener;
import com.taobao.monitor.impl.processor.AbsProcessor;
import com.taobao.monitor.impl.trace.ApplicationBackgroundChangedDispatcher;
import com.taobao.monitor.impl.trace.ApplicationGCDispatcher;
import com.taobao.monitor.impl.trace.ApplicationLowMemoryDispatcher;
import com.taobao.monitor.impl.trace.FPSDispatcher;
import com.taobao.monitor.impl.trace.IDispatcher;
import com.taobao.monitor.impl.trace.ImageStageDispatcher;
import com.taobao.monitor.impl.trace.NetworkStageDispatcher;
import com.taobao.monitor.impl.util.TimeUtils;
import com.taobao.monitor.impl.util.TopicUtils;
import com.taobao.monitor.performance.IWXApmAdapter;
import com.taobao.monitor.procedure.IProcedure;
import com.taobao.monitor.procedure.ProcedureConfig;
import com.taobao.monitor.procedure.ProcedureFactoryProxy;
import com.taobao.monitor.procedure.ProcedureManagerProxy;
import com.taobao.weex.analyzer.Config;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeexProcessor extends AbsProcessor implements IWXApmAdapter, FPSDispatcher.FPSListener, ApplicationGCDispatcher.ApplicationGCListener, ApplicationLowMemoryDispatcher.LowMemoryListener, ApplicationBackgroundChangedDispatcher.BackgroundChangedListener, ImageStageDispatcher.IImageStageListener, NetworkStageDispatcher.INetworkStageListener, OnUsableVisibleListener<Activity> {
    private static final String TAG = "WeexProcessor";
    private IDispatcher backgroundChangedDispatcher;
    private IDispatcher eventDispatcher;
    private IDispatcher fpsDispatcher;
    private List<Integer> fpsList = new ArrayList();
    private int gcCount = 0;
    private IDispatcher gcDispatcher;
    private int imageCanceledCount;
    private int imageFailedCount;
    private int imageRequestedCount;
    private int imageSuccessCount;
    private boolean isFirstDraw = true;
    private boolean isFirstFullUsable = true;
    private boolean isFirstFullVisible = true;
    private boolean isStopped = false;
    private boolean isVisible = true;
    private int jankCount = 0;
    private long loadStartTime;
    private IDispatcher lowMemoryDispatcher;
    private int networkCanceledCount;
    private int networkFailedCount;
    private int networkRequestedCount;
    private int networkSuccessCount;
    private final IProcedure procedure;
    private final String type;
    private IDispatcher usableDispatcher;

    public WeexProcessor(String str) {
        super(false);
        this.type = str;
        ProcedureConfig build = new ProcedureConfig.Builder().setIndependent(true).setUpload(true).setParentNeedStats(true).setParent(ProcedureManagerProxy.PROXY.getCurrentActivityProcedure()).build();
        ProcedureFactoryProxy procedureFactoryProxy = ProcedureFactoryProxy.PROXY;
        this.procedure = procedureFactoryProxy.createProcedure(TopicUtils.getFullTopic("/" + str), build);
    }

    /* access modifiers changed from: protected */
    public void startProcessor() {
        super.startProcessor();
        this.loadStartTime = TimeUtils.currentTimeMillis();
        this.procedure.begin();
        this.procedure.stage("procedureStartTime", TimeUtils.currentTimeMillis());
        this.eventDispatcher = getDispatcher(APMContext.ACTIVITY_EVENT_DISPATCHER);
        this.lowMemoryDispatcher = getDispatcher(APMContext.APPLICATION_LOW_MEMORY_DISPATCHER);
        this.fpsDispatcher = getDispatcher(APMContext.ACTIVITY_FPS_DISPATCHER);
        this.gcDispatcher = getDispatcher(APMContext.APPLICATION_GC_DISPATCHER);
        this.backgroundChangedDispatcher = getDispatcher(APMContext.APPLICATION_BACKGROUND_CHANGED_DISPATCHER);
        this.usableDispatcher = getDispatcher(APMContext.ACTIVITY_USABLE_VISIBLE_DISPATCHER);
        this.gcDispatcher.addListener(this);
        this.lowMemoryDispatcher.addListener(this);
        this.eventDispatcher.addListener(this);
        this.fpsDispatcher.addListener(this);
        this.backgroundChangedDispatcher.addListener(this);
        this.usableDispatcher.addListener(this);
    }

    /* access modifiers changed from: protected */
    public void stopProcessor() {
        if (!this.isStopped) {
            this.procedure.stage("procedureEndTime", TimeUtils.currentTimeMillis());
            this.procedure.addStatistic("gcCount", Integer.valueOf(this.gcCount));
            this.procedure.addStatistic(Config.TYPE_FPS, this.fpsList.toString());
            this.procedure.addStatistic("jankCount", Integer.valueOf(this.jankCount));
            this.procedure.addProperty("deviceLevel", Integer.valueOf(AliHAHardware.getInstance().getOutlineInfo().deviceLevel));
            this.procedure.addProperty("runtimeLevel", Integer.valueOf(AliHAHardware.getInstance().getOutlineInfo().runtimeLevel));
            this.procedure.addProperty("cpuUsageOfDevcie", Float.valueOf(AliHAHardware.getInstance().getCpuInfo().cpuUsageOfDevcie));
            this.procedure.addProperty("memoryRuntimeLevel", Integer.valueOf(AliHAHardware.getInstance().getMemoryInfo().runtimeLevel));
            this.procedure.addStatistic("imgLoadCount", Integer.valueOf(this.imageRequestedCount));
            this.procedure.addStatistic("imgLoadSuccessCount", Integer.valueOf(this.imageSuccessCount));
            this.procedure.addStatistic("imgLoadFailCount", Integer.valueOf(this.imageFailedCount));
            this.procedure.addStatistic("imgLoadCancelCount", Integer.valueOf(this.imageCanceledCount));
            this.procedure.addStatistic("networkRequestCount", Integer.valueOf(this.networkRequestedCount));
            this.procedure.addStatistic("networkRequestSuccessCount", Integer.valueOf(this.networkSuccessCount));
            this.procedure.addStatistic("networkRequestFailCount", Integer.valueOf(this.networkFailedCount));
            this.procedure.addStatistic("networkRequestCancelCount", Integer.valueOf(this.networkCanceledCount));
            this.lowMemoryDispatcher.removeListener(this);
            this.eventDispatcher.removeListener(this);
            this.fpsDispatcher.removeListener(this);
            this.gcDispatcher.removeListener(this);
            this.backgroundChangedDispatcher.removeListener(this);
            this.usableDispatcher.removeListener(this);
            this.procedure.end();
            super.stopProcessor();
        }
        this.isStopped = true;
    }

    public void onStart(String str) {
        startProcessor();
        this.procedure.addProperty(BindingXConstants.KEY_INSTANCE_ID, str);
    }

    public void onEnd() {
        stopProcessor();
    }

    public void onEvent(String str, Object obj) {
        HashMap hashMap = new HashMap();
        hashMap.put(str, obj);
        this.procedure.event(str, hashMap);
    }

    public void onStage(String str, long j) {
        this.procedure.stage(str, j);
    }

    public void addProperty(String str, Object obj) {
        this.procedure.addProperty(str, obj);
    }

    public void addStatistic(String str, double d) {
        this.procedure.addStatistic(str, Double.valueOf(d));
    }

    public void addBiz(String str, Map<String, Object> map) {
        this.procedure.addBiz(str, map);
    }

    public void addBizAbTest(String str, Map<String, Object> map) {
        this.procedure.addBizAbTest(str, map);
    }

    public void addBizStage(String str, Map<String, Object> map) {
        this.procedure.addBizStage(str, map);
    }

    public void onStart() {
        this.isVisible = true;
    }

    public void onStop() {
        this.isVisible = false;
    }

    public void onLowMemory() {
        HashMap hashMap = new HashMap(1);
        hashMap.put("timestamp", Long.valueOf(TimeUtils.currentTimeMillis()));
        this.procedure.event("onLowMemory", hashMap);
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
        this.gcCount++;
    }

    public void onChanged(int i, long j) {
        if (i == 1) {
            HashMap hashMap = new HashMap(1);
            hashMap.put("timestamp", Long.valueOf(j));
            this.procedure.event("foreground2Background", hashMap);
            Global.instance().handler().post(new Runnable() {
                public void run() {
                    WeexProcessor.this.stopProcessor();
                }
            });
            return;
        }
        HashMap hashMap2 = new HashMap(1);
        hashMap2.put("timestamp", Long.valueOf(j));
        this.procedure.event("background2Foreground", hashMap2);
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

    public void onVisibleChanged(Activity activity, int i, long j) {
        if (this.isFirstFullVisible && this.isVisible && i == 2) {
            this.procedure.addProperty("displayDuration", Long.valueOf(j - this.loadStartTime));
            this.procedure.stage("displayedTime", j);
            this.isFirstFullVisible = false;
        }
    }

    public void onUsableChanged(Activity activity, int i, long j) {
        if (this.isFirstFullUsable && this.isVisible && i == 2) {
            this.procedure.addProperty("interactiveDuration", Long.valueOf(j - this.loadStartTime));
            this.procedure.addProperty("loadDuration", Long.valueOf(j - this.loadStartTime));
            this.procedure.stage("interactiveTime", j);
            this.isFirstFullUsable = false;
        }
    }

    public void onRenderStart(Activity activity, long j) {
        if (this.isFirstDraw && this.isVisible) {
            this.procedure.addProperty("pageInitDuration", Long.valueOf(j - this.loadStartTime));
            this.procedure.stage("renderStartTime", j);
            this.isFirstDraw = false;
        }
    }

    public void onRenderPercent(Activity activity, float f, long j) {
        if (this.isVisible) {
            this.procedure.addProperty("onRenderPercent", Float.valueOf(f));
            this.procedure.addProperty("drawPercentTime", Long.valueOf(j));
        }
    }
}
