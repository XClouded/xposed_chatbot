package com.taobao.weex.analyzer.core.lint;

import android.graphics.Color;
import android.view.View;
import androidx.annotation.NonNull;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.analyzer.core.AbstractLoopTask;
import com.taobao.weex.analyzer.core.lint.DomTracker;
import com.taobao.weex.analyzer.pojo.HealthReport;
import com.taobao.weex.analyzer.utils.SDKUtils;
import com.taobao.weex.analyzer.view.highlight.MutipleViewHighlighter;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.utils.WXLogUtils;
import java.lang.ref.WeakReference;

@Deprecated
public class PollingVDomMonitor implements IVDomMonitor {
    public static boolean shouldHighlight = false;
    public static boolean shouldStop;
    private PollingTask mTask;

    public void monitor(@NonNull WXSDKInstance wXSDKInstance) {
        if (this.mTask != null) {
            this.mTask.stop();
        }
        this.mTask = new PollingTask(wXSDKInstance);
        this.mTask.start();
    }

    public void destroy() {
        if (this.mTask != null) {
            this.mTask.stop();
        }
    }

    private static class PollingTask extends AbstractLoopTask implements DomTracker.OnTrackNodeListener {
        static final int MAX_VDOM_LAYER = 14;
        private WeakReference<WXSDKInstance> instanceRef;
        MutipleViewHighlighter mViewHighlighter;

        PollingTask(WXSDKInstance wXSDKInstance) {
            super(false, 1500);
            this.instanceRef = new WeakReference<>(wXSDKInstance);
            if (PollingVDomMonitor.shouldHighlight) {
                this.mViewHighlighter = MutipleViewHighlighter.newInstance();
                this.mViewHighlighter.setColor(Color.parseColor("#420000ff"));
            }
        }

        /* access modifiers changed from: protected */
        public void onRun() {
            if (PollingVDomMonitor.shouldStop) {
                WXLogUtils.e("weex-analyzer", "polling service is destroyed");
                stop();
                return;
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
                        traverse.writeToConsole();
                    }
                } catch (Exception e) {
                    WXLogUtils.e(e.getMessage());
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
            if (i >= 14 && (hostView = wXComponent.getHostView()) != null && this.mViewHighlighter != null) {
                this.mViewHighlighter.addHighlightedView(hostView);
            }
        }
    }
}
