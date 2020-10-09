package com.taobao.weex.analyzer.core.debug;

import alimama.com.unwrouter.UNWRouter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.fastjson.JSON;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.core.AbstractLoopTask;
import com.taobao.weex.analyzer.core.TaskEntity;
import com.taobao.weex.analyzer.core.cpu.CpuTaskEntity;
import com.taobao.weex.analyzer.core.debug.IDataReporter;
import com.taobao.weex.analyzer.core.fps.FPSSampler;
import com.taobao.weex.analyzer.core.fps.FpsTaskEntity;
import com.taobao.weex.analyzer.core.inspector.network.NetworkEventInspector;
import com.taobao.weex.analyzer.core.lint.RemoteVDomMonitor;
import com.taobao.weex.analyzer.core.memory.MemoryTaskEntity;
import com.taobao.weex.analyzer.core.memory.NativeMemoryTaskEntity;
import com.taobao.weex.analyzer.core.memory.PSSMemoryInfoSampler;
import com.taobao.weex.analyzer.core.traffic.TrafficTaskEntity;
import com.taobao.weex.analyzer.core.weex.Performance;
import com.taobao.weex.analyzer.pojo.HealthReport;
import com.taobao.weex.el.parse.Operators;
import com.taobao.weex.utils.WXLogUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class DataRepository {
    public static final String ACTION_DISPATCH = "cmd.dispatch";
    /* access modifiers changed from: private */
    public boolean isCPUEnabled;
    /* access modifiers changed from: private */
    public boolean isFPSEnabled;
    /* access modifiers changed from: private */
    public boolean isJSExceptionEnabled;
    /* access modifiers changed from: private */
    public boolean isMemoryEnabled;
    /* access modifiers changed from: private */
    public boolean isNativeMemoryEnabled;
    /* access modifiers changed from: private */
    public boolean isNetworkInspectorEnabled;
    /* access modifiers changed from: private */
    public boolean isPerformanceEnabled;
    /* access modifiers changed from: private */
    public boolean isPerformanceV2Enabled;
    /* access modifiers changed from: private */
    public boolean isRenderAnalysisEnabled;
    /* access modifiers changed from: private */
    public boolean isTotalMemoryEnabled;
    /* access modifiers changed from: private */
    public boolean isTrafficEnabled;
    /* access modifiers changed from: private */
    public boolean isWindmillPerformanceEnabled;
    /* access modifiers changed from: private */
    public OnReceivedDataCallback mCallback;
    /* access modifiers changed from: private */
    public Context mContext;
    /* access modifiers changed from: private */
    public String mDeviceId;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private InnerDataReceiver mInnerDataReceiver;
    private NetworkEventInspector mInspector;
    private AtomicInteger mSequenceId = new AtomicInteger(0);
    private TaskImpl mTask;

    interface OnReceivedDataCallback {
        void onReceivedData(IDataReporter.ProcessedData processedData);
    }

    private DataRepository(Context context, String str) {
        this.mDeviceId = str;
        this.mContext = context;
    }

    public static DataRepository newInstance(@NonNull Context context, @NonNull String str) {
        return new DataRepository(context, str);
    }

    /* access modifiers changed from: package-private */
    public void setOnReceivedDataCallback(OnReceivedDataCallback onReceivedDataCallback) {
        this.mCallback = onReceivedDataCallback;
    }

    /* access modifiers changed from: package-private */
    public void setCPUEnabled(boolean z) {
        this.isCPUEnabled = z;
    }

    /* access modifiers changed from: package-private */
    public void setMemoryEnabled(boolean z) {
        this.isMemoryEnabled = z;
    }

    /* access modifiers changed from: package-private */
    public void setNativeMemoryEnabled(boolean z) {
        this.isNativeMemoryEnabled = z;
    }

    /* access modifiers changed from: package-private */
    public void setTotalMemoryEnabled(boolean z) {
        this.isTotalMemoryEnabled = z;
    }

    /* access modifiers changed from: package-private */
    public void setFPSEnabled(boolean z) {
        this.isFPSEnabled = z;
    }

    /* access modifiers changed from: package-private */
    public void setTrafficEnabled(boolean z) {
        this.isTrafficEnabled = z;
    }

    /* access modifiers changed from: package-private */
    public void setPerformanceEnabled(boolean z) {
        this.isPerformanceEnabled = z;
    }

    /* access modifiers changed from: package-private */
    public void setPerformanceV2Enabled(boolean z) {
        this.isPerformanceV2Enabled = z;
    }

    /* access modifiers changed from: package-private */
    public void setWindmillPerformanceEnabled(boolean z) {
        this.isWindmillPerformanceEnabled = z;
    }

    /* access modifiers changed from: package-private */
    public void setHighlightViewEnabled(boolean z) {
        Intent intent = new Intent(RemoteVDomMonitor.ACTION_HIGHLIGHT_VIEW);
        intent.putExtra("highlight_view", z);
        LocalBroadcastManager.getInstance(this.mContext).sendBroadcast(intent);
    }

    /* access modifiers changed from: package-private */
    public void setNetworkInspectorEnabled(boolean z) {
        this.isNetworkInspectorEnabled = z;
    }

    /* access modifiers changed from: package-private */
    public void setRenderAnalysisEnabled(boolean z, long j) {
        WXLogUtils.d("weex-analyzer", ">>>> will send render analysis command: (status:" + z + ",timeout:" + j + Operators.BRACKET_END_STR);
        long max = Math.max(TBToast.Duration.MEDIUM, j);
        this.isRenderAnalysisEnabled = z;
        final Intent intent = new Intent(RemoteVDomMonitor.ACTION_SHOULD_MONITOR);
        intent.putExtra(RemoteVDomMonitor.EXTRA_MONITOR, this.isRenderAnalysisEnabled);
        this.mHandler.postDelayed(new Runnable() {
            public void run() {
                LocalBroadcastManager.getInstance(DataRepository.this.mContext).sendBroadcast(intent);
                WXLogUtils.d("weex-analyzer", "send render analysis command success");
            }
        }, max);
    }

    public void setJSExceptionEnabled(boolean z) {
        this.isJSExceptionEnabled = z;
    }

    public void prepare(Context context) {
        if (this.mInspector != null) {
            this.mInspector.destroy();
        }
        this.mInspector = NetworkEventInspector.createInstance(this.mContext, (NetworkEventInspector.OnMessageReceivedListener) new NetworkEventInspector.OnMessageReceivedListener() {
            public void onMessageReceived(NetworkEventInspector.MessageBean messageBean) {
                if (DataRepository.this.isNetworkInspectorEnabled && DataRepository.this.mCallback != null && messageBean != null) {
                    Map<String, String> map = messageBean.extendProps;
                    if (map == null) {
                        DataRepository.this.mCallback.onReceivedData(new IDataReporter.ProcessedDataBuilder().sequenceId(DataRepository.this.generateSequenceId()).data(messageBean).deviceId(DataRepository.this.mDeviceId).type(Config.TYPE_MTOP_INSPECTOR).build());
                    } else if ("mtop".equalsIgnoreCase(map.get("bizType"))) {
                        DataRepository.this.mCallback.onReceivedData(new IDataReporter.ProcessedDataBuilder().sequenceId(DataRepository.this.generateSequenceId()).data(messageBean).deviceId(DataRepository.this.mDeviceId).type(Config.TYPE_MTOP_INSPECTOR).build());
                    }
                }
            }
        });
        if (this.mTask == null) {
            this.mTask = new TaskImpl(context, 1000);
            this.mTask.start();
        }
        if (this.mInnerDataReceiver != null) {
            LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this.mInnerDataReceiver);
        }
        this.mInnerDataReceiver = new InnerDataReceiver();
        LocalBroadcastManager.getInstance(this.mContext).registerReceiver(this.mInnerDataReceiver, new IntentFilter(ACTION_DISPATCH));
    }

    public void destroy() {
        if (this.mInspector != null) {
            this.mInspector.destroy();
        }
        if (this.mTask != null) {
            this.mTask.stop();
            this.mTask = null;
        }
        if (this.mInnerDataReceiver != null) {
            LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this.mInnerDataReceiver);
        }
    }

    private class TaskImpl extends AbstractLoopTask {
        private List<TaskEntity> mTaskEntities = new ArrayList();

        TaskImpl(Context context, int i) {
            super(false, i);
            this.mTaskEntities.add(new CpuTaskEntity());
            this.mTaskEntities.add(new TrafficTaskEntity(i));
            this.mTaskEntities.add(new MemoryTaskEntity());
            this.mTaskEntities.add(new NativeMemoryTaskEntity(context));
            if (FPSSampler.isSupported()) {
                this.mTaskEntities.add(new FpsTaskEntity());
            }
        }

        /* access modifiers changed from: protected */
        public void onStart() {
            if (this.mTaskEntities != null && !this.mTaskEntities.isEmpty()) {
                for (TaskEntity onTaskInit : this.mTaskEntities) {
                    onTaskInit.onTaskInit();
                }
            }
        }

        /* access modifiers changed from: protected */
        public void onRun() {
            if (DataRepository.this.mCallback != null && this.mTaskEntities != null && !this.mTaskEntities.isEmpty()) {
                for (TaskEntity next : this.mTaskEntities) {
                    if ((next instanceof CpuTaskEntity) && DataRepository.this.isCPUEnabled) {
                        OnReceivedDataCallback access$200 = DataRepository.this.mCallback;
                        IDataReporter.ProcessedDataBuilder deviceId = new IDataReporter.ProcessedDataBuilder().type(Config.TYPE_CPU).deviceId(DataRepository.this.mDeviceId);
                        double round = (double) Math.round(((CpuTaskEntity.CpuInfo) next.onTaskRun()).pidCpuUsage * 100.0d);
                        Double.isNaN(round);
                        access$200.onReceivedData(deviceId.data(Double.valueOf(round / 100.0d)).sequenceId(DataRepository.this.generateSequenceId()).build());
                    } else if ((next instanceof TrafficTaskEntity) && DataRepository.this.isTrafficEnabled) {
                        DataRepository.this.mCallback.onReceivedData(new IDataReporter.ProcessedDataBuilder().type(Config.TYPE_TRAFFIC).deviceId(DataRepository.this.mDeviceId).data((TrafficTaskEntity.TrafficInfo) next.onTaskRun()).sequenceId(DataRepository.this.generateSequenceId()).build());
                    } else if ((next instanceof MemoryTaskEntity) && DataRepository.this.isMemoryEnabled) {
                        DataRepository.this.mCallback.onReceivedData(new IDataReporter.ProcessedDataBuilder().type(Config.TYPE_MEMORY).deviceId(DataRepository.this.mDeviceId).data(Double.valueOf(((Double) next.onTaskRun()).doubleValue())).sequenceId(DataRepository.this.generateSequenceId()).build());
                    } else if ((next instanceof FpsTaskEntity) && DataRepository.this.isFPSEnabled) {
                        DataRepository.this.mCallback.onReceivedData(new IDataReporter.ProcessedDataBuilder().type(Config.TYPE_FPS).deviceId(DataRepository.this.mDeviceId).data(Double.valueOf(((Double) next.onTaskRun()).doubleValue())).sequenceId(DataRepository.this.generateSequenceId()).build());
                    } else if (next instanceof NativeMemoryTaskEntity) {
                        PSSMemoryInfoSampler.PssInfo pssInfo = (PSSMemoryInfoSampler.PssInfo) next.onTaskRun();
                        if (DataRepository.this.isNativeMemoryEnabled) {
                            DataRepository.this.mCallback.onReceivedData(new IDataReporter.ProcessedDataBuilder().type(Config.TYPE_NATIVE_MEMORY).deviceId(DataRepository.this.mDeviceId).data(Double.valueOf(pssInfo.nativePss)).sequenceId(DataRepository.this.generateSequenceId()).build());
                        } else if (DataRepository.this.isTotalMemoryEnabled) {
                            DataRepository.this.mCallback.onReceivedData(new IDataReporter.ProcessedDataBuilder().type(Config.TYPE_TOTAL_MEMORY).deviceId(DataRepository.this.mDeviceId).data(Double.valueOf(pssInfo.nativePss)).sequenceId(DataRepository.this.generateSequenceId()).build());
                        }
                    }
                }
            }
        }

        /* access modifiers changed from: protected */
        public void onStop() {
            if (this.mTaskEntities != null && !this.mTaskEntities.isEmpty()) {
                for (TaskEntity onTaskStop : this.mTaskEntities) {
                    onTaskStop.onTaskStop();
                }
            }
        }
    }

    private class InnerDataReceiver extends BroadcastReceiver {
        private InnerDataReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(DataRepository.ACTION_DISPATCH) && DataRepository.this.mCallback != null) {
                String stringExtra = intent.getStringExtra("type");
                if (Config.TYPE_WEEX_PERFORMANCE_STATISTICS.equals(stringExtra)) {
                    String stringExtra2 = intent.getStringExtra(Config.TYPE_WEEX_PERFORMANCE_STATISTICS);
                    if (!TextUtils.isEmpty(stringExtra2) && DataRepository.this.isPerformanceEnabled) {
                        DataRepository.this.mCallback.onReceivedData(new IDataReporter.ProcessedDataBuilder().deviceId(DataRepository.this.mDeviceId).data((Performance) JSON.parseObject(stringExtra2, Performance.class)).type(Config.TYPE_WEEX_PERFORMANCE_STATISTICS).build());
                    }
                } else if (Config.TYPE_WEEX_PERFORMANCE_STATISTICS_V2.equals(stringExtra)) {
                    String stringExtra3 = intent.getStringExtra(Config.TYPE_WEEX_PERFORMANCE_STATISTICS_V2);
                    if (!TextUtils.isEmpty(stringExtra3) && DataRepository.this.isPerformanceV2Enabled) {
                        DataRepository.this.mCallback.onReceivedData(new IDataReporter.ProcessedDataBuilder().deviceId(DataRepository.this.mDeviceId).data(stringExtra3).type(Config.TYPE_WEEX_PERFORMANCE_STATISTICS_V2).build());
                    }
                } else if ("lifecycle".equals(stringExtra)) {
                    String stringExtra4 = intent.getStringExtra("status");
                    String stringExtra5 = intent.getStringExtra(UNWRouter.PAGE_NAME);
                    if (!TextUtils.isEmpty(stringExtra4)) {
                        DataRepository.this.mCallback.onReceivedData(new IDataReporter.ProcessedDataBuilder().deviceId(DataRepository.this.mDeviceId).data(new LifecycleEvent(stringExtra, stringExtra4, stringExtra5)).type("lifecycle").build());
                    }
                } else if (Config.TYPE_RENDER_ANALYSIS.equals(stringExtra)) {
                    String stringExtra6 = intent.getStringExtra(Config.TYPE_RENDER_ANALYSIS);
                    if (!TextUtils.isEmpty(stringExtra6) && DataRepository.this.isRenderAnalysisEnabled) {
                        DataRepository.this.mCallback.onReceivedData(new IDataReporter.ProcessedDataBuilder().deviceId(DataRepository.this.mDeviceId).data((HealthReport) JSON.parseObject(stringExtra6, HealthReport.class)).type(Config.TYPE_RENDER_ANALYSIS).build());
                    }
                } else if (Config.TYPE_JS_EXCEPTION.equals(stringExtra)) {
                    String stringExtra7 = intent.getStringExtra(Config.TYPE_JS_EXCEPTION);
                    if (!TextUtils.isEmpty(stringExtra7) && DataRepository.this.isJSExceptionEnabled) {
                        DataRepository.this.mCallback.onReceivedData(new IDataReporter.ProcessedDataBuilder().deviceId(DataRepository.this.mDeviceId).data(JSON.parseObject(stringExtra7)).type(Config.TYPE_JS_EXCEPTION).build());
                    }
                } else if (Config.TYPE_WINDMILL_PERFORMANCE_STATISTICS.equals(stringExtra)) {
                    String stringExtra8 = intent.getStringExtra(Config.TYPE_WINDMILL_PERFORMANCE_STATISTICS);
                    if (!TextUtils.isEmpty(stringExtra8) && DataRepository.this.isWindmillPerformanceEnabled) {
                        DataRepository.this.mCallback.onReceivedData(new IDataReporter.ProcessedDataBuilder().deviceId(DataRepository.this.mDeviceId).data(JSON.parseObject(stringExtra8)).type(Config.TYPE_WINDMILL_PERFORMANCE_STATISTICS).build());
                    }
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public int generateSequenceId() {
        int andIncrement = this.mSequenceId.getAndIncrement();
        if (andIncrement >= Integer.MAX_VALUE) {
            return 0;
        }
        return andIncrement;
    }

    static class LifecycleEvent {
        public String pageName;
        public String status;
        public String type;

        LifecycleEvent(String str, String str2, String str3) {
            this.type = str;
            this.status = str2;
            this.pageName = str3;
        }
    }
}
