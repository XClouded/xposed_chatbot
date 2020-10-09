package com.taobao.weex.analyzer.core.lint;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.fastjson.JSON;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.analyzer.Config;
import com.taobao.weex.analyzer.core.AbstractLoopTask;
import com.taobao.weex.analyzer.core.debug.DataRepository;
import com.taobao.weex.analyzer.core.lint.DomTracker;
import com.taobao.weex.analyzer.pojo.HealthReport;
import com.taobao.weex.analyzer.utils.SDKUtils;
import com.taobao.weex.analyzer.view.highlight.MutipleViewHighlighter;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.utils.WXLogUtils;
import java.lang.ref.WeakReference;

public class RemoteVDomMonitor implements IVDomMonitor {
    public static final String ACTION_HIGHLIGHT_VIEW = "action_highlight_view";
    public static final String ACTION_SHOULD_MONITOR = "action_should_monitor";
    public static final String EXTRA_HIGHLIGHT_VIEW = "highlight_view";
    public static final String EXTRA_MONITOR = "extra_monitor";
    /* access modifiers changed from: private */
    public Context mContext;
    private InnerReceiver mInnerReceiver;
    private InnerReceiverForHighlight mInnerReceiverForHighlight;
    private WXSDKInstance mInstance;
    private PollingTask mTask;
    /* access modifiers changed from: private */
    public boolean shouldHightlight = false;
    /* access modifiers changed from: private */
    public boolean shouldMonitor = false;

    public RemoteVDomMonitor(@NonNull Context context) {
        this.mContext = context;
        this.mInnerReceiver = new InnerReceiver();
        this.mInnerReceiverForHighlight = new InnerReceiverForHighlight();
        LocalBroadcastManager.getInstance(context).registerReceiver(this.mInnerReceiver, new IntentFilter(ACTION_SHOULD_MONITOR));
        LocalBroadcastManager.getInstance(context).registerReceiver(this.mInnerReceiverForHighlight, new IntentFilter(ACTION_HIGHLIGHT_VIEW));
    }

    public void monitor(@NonNull WXSDKInstance wXSDKInstance) {
        this.mInstance = wXSDKInstance;
        tryStartTask();
    }

    /* access modifiers changed from: private */
    public void tryStartTask() {
        WXLogUtils.d("weex-analyzer", "should monitor render performance data?" + this.shouldMonitor);
        if (this.shouldMonitor) {
            if (this.mTask != null) {
                this.mTask.stop();
            }
            this.mTask = new PollingTask(this.mInstance);
            this.mTask.start();
        }
    }

    public void destroy() {
        if (this.mInnerReceiver != null) {
            LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this.mInnerReceiver);
        }
        if (this.mInnerReceiverForHighlight != null) {
            LocalBroadcastManager.getInstance(this.mContext).unregisterReceiver(this.mInnerReceiverForHighlight);
        }
        this.mInnerReceiverForHighlight = null;
        this.mInnerReceiver = null;
        if (this.mTask != null) {
            this.mTask.stop();
        }
    }

    private class InnerReceiver extends BroadcastReceiver {
        private InnerReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (RemoteVDomMonitor.ACTION_SHOULD_MONITOR.equals(intent.getAction())) {
                boolean unused = RemoteVDomMonitor.this.shouldMonitor = intent.getBooleanExtra(RemoteVDomMonitor.EXTRA_MONITOR, false);
                if (RemoteVDomMonitor.this.shouldMonitor) {
                    RemoteVDomMonitor.this.tryStartTask();
                }
            }
        }
    }

    private class InnerReceiverForHighlight extends BroadcastReceiver {
        private InnerReceiverForHighlight() {
        }

        public void onReceive(Context context, Intent intent) {
            if (RemoteVDomMonitor.ACTION_HIGHLIGHT_VIEW.equals(intent.getAction())) {
                boolean unused = RemoteVDomMonitor.this.shouldHightlight = intent.getBooleanExtra("highlight_view", false);
            }
        }
    }

    private class PollingTask extends AbstractLoopTask implements DomTracker.OnTrackNodeListener {
        static final int MAX_VDOM_LAYER = 14;
        private WeakReference<WXSDKInstance> instanceRef;
        MutipleViewHighlighter mViewHighlighter = MutipleViewHighlighter.newInstance();

        PollingTask(WXSDKInstance wXSDKInstance) {
            super(false, 1500);
            this.instanceRef = new WeakReference<>(wXSDKInstance);
            this.mViewHighlighter.setColor(Color.parseColor("#420000ff"));
        }

        /* access modifiers changed from: protected */
        public void onRun() {
            if (!RemoteVDomMonitor.this.shouldMonitor) {
                stop();
                return;
            }
            if (!RemoteVDomMonitor.this.shouldHightlight && this.mViewHighlighter != null) {
                this.mViewHighlighter.clearHighlight();
                this.mViewHighlighter = null;
            }
            WXSDKInstance wXSDKInstance = (WXSDKInstance) this.instanceRef.get();
            if (wXSDKInstance == null) {
                WXLogUtils.e("weex-analyzer", "weex instance is destroyed");
                stop();
            } else if (wXSDKInstance.getContext() == null || SDKUtils.isInteractive(wXSDKInstance.getContext())) {
                try {
                    DomTracker domTracker = new DomTracker(wXSDKInstance);
                    domTracker.setOnTrackNodeListener(this);
                    HealthReport traverse = domTracker.traverse();
                    if (traverse != null) {
                        String jSONString = JSON.toJSONString(traverse);
                        Intent intent = new Intent(DataRepository.ACTION_DISPATCH);
                        intent.putExtra(Config.TYPE_RENDER_ANALYSIS, jSONString);
                        intent.putExtra("type", Config.TYPE_RENDER_ANALYSIS);
                        LocalBroadcastManager.getInstance(RemoteVDomMonitor.this.mContext).sendBroadcast(intent);
                    }
                } catch (Exception e) {
                    WXLogUtils.e("weex-analyzer", e.getMessage());
                }
            } else {
                WXLogUtils.e("weex-analyzer", "polling service is destroyed because we are in background or killed");
                stop();
            }
        }

        /* access modifiers changed from: protected */
        public void onStop() {
            if (this.mViewHighlighter != null) {
                this.mViewHighlighter.clearHighlight();
            }
        }

        public void onTrackNode(@NonNull WXComponent wXComponent, int i) {
            View hostView;
            if (i >= 14 && (hostView = wXComponent.getHostView()) != null && this.mViewHighlighter != null && RemoteVDomMonitor.this.shouldHightlight) {
                this.mViewHighlighter.addHighlightedView(hostView);
            }
        }
    }
}
