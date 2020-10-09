package com.taobao.monitor.impl.processor.launcher;

import android.app.Activity;
import com.taobao.monitor.impl.processor.IProcessor;
import com.taobao.monitor.impl.trace.ActivityLifeCycleDispatcher;
import java.util.Map;

public class LauncherModelLifeCycle implements ActivityLifeCycleDispatcher.IActivityLifeCycle, IProcessor.IProcessorLifeCycle {
    private int count = 0;
    private final LauncherProcessorFactory factory = new LauncherProcessorFactory();
    private int fbCount = 0;
    private LauncherProcessor launcherProcessor = null;
    private boolean onForeground = false;

    public void processorOnStart(IProcessor iProcessor) {
    }

    public void onActivityCreated(Activity activity, Map<String, Object> map, long j) {
        if (this.count == 0) {
            this.launcherProcessor = createLauncherProcessor(activity);
            if (this.launcherProcessor != null) {
                this.launcherProcessor.setLifeCycle(this);
            }
        } else if (!this.onForeground) {
            this.launcherProcessor = this.factory.createLinkB2FManagerProcessor();
            if (this.launcherProcessor != null) {
                this.launcherProcessor.setLifeCycle(this);
            }
        }
        if (this.launcherProcessor != null) {
            this.launcherProcessor.onActivityCreated(activity, map, j);
        }
        this.count++;
    }

    public void onActivityStarted(Activity activity, long j) {
        this.fbCount++;
        if (this.fbCount == 1) {
            this.onForeground = true;
        }
        if (this.launcherProcessor != null) {
            this.launcherProcessor.onActivityStarted(activity, j);
        }
    }

    public void onActivityResumed(Activity activity, long j) {
        if (this.launcherProcessor != null) {
            this.launcherProcessor.onActivityResumed(activity, j);
        }
    }

    public void onActivityPaused(Activity activity, long j) {
        if (this.launcherProcessor != null) {
            this.launcherProcessor.onActivityPaused(activity, j);
        }
    }

    public void onActivityStopped(Activity activity, long j) {
        this.fbCount--;
        if (this.fbCount == 0) {
            this.onForeground = false;
        }
        if (this.launcherProcessor != null) {
            this.launcherProcessor.onActivityStopped(activity, j);
        }
    }

    public void onActivityDestroyed(Activity activity, long j) {
        if (this.launcherProcessor != null) {
            this.launcherProcessor.onActivityDestroyed(activity, j);
        }
        this.count--;
    }

    private LauncherProcessor createLauncherProcessor(Activity activity) {
        if ("TbFlowInActivity".equals(activity.getClass().getSimpleName())) {
            return this.factory.createLinkManagerProcessor();
        }
        return this.factory.createProcessor();
    }

    public void processorOnEnd(IProcessor iProcessor) {
        this.launcherProcessor = null;
    }
}
