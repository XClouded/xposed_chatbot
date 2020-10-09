package com.taobao.monitor.impl.processor.pageload;

import android.app.Activity;
import com.taobao.monitor.impl.processor.IProcessorFactory;
import com.taobao.monitor.impl.trace.ActivityLifeCycleDispatcher;
import java.util.HashMap;
import java.util.Map;

public class PageModelLifecycle implements ActivityLifeCycleDispatcher.IActivityLifeCycle {
    private int activityCount = 0;
    private Map<Activity, IPageLoadLifeCycle> map = new HashMap();
    private final IProcessorFactory<PageLoadProcessor> pageFactory = new PageLoadProcessorFactory();
    private final IProcessorFactory<PageLoadPopProcessor> pagePopFactory = new PageLoadPopProcessorFactory();
    private Map<Activity, IPopLifeCycle> popMap = new HashMap();
    private Activity topActivity = null;

    public interface IPageLoadLifeCycle {
        void onActivityCreated(Activity activity, Map<String, Object> map, long j);

        void onActivityDestroyed(Activity activity, long j);

        void onActivityPaused(Activity activity, long j);

        void onActivityResumed(Activity activity, long j);

        void onActivityStarted(Activity activity, long j);

        void onActivityStopped(Activity activity, long j);
    }

    public interface IPopLifeCycle {
        void onActivityStarted(Activity activity);

        void onActivityStopped(Activity activity);
    }

    public void onActivityCreated(Activity activity, Map<String, Object> map2, long j) {
        PageLoadProcessor createProcessor = this.pageFactory.createProcessor();
        if (createProcessor != null) {
            this.map.put(activity, createProcessor);
            createProcessor.onActivityCreated(activity, map2, j);
        }
        this.topActivity = activity;
    }

    public void onActivityStarted(Activity activity, long j) {
        IPopLifeCycle createProcessor;
        this.activityCount++;
        IPageLoadLifeCycle iPageLoadLifeCycle = this.map.get(activity);
        if (iPageLoadLifeCycle != null) {
            iPageLoadLifeCycle.onActivityStarted(activity, j);
        }
        if (!(this.topActivity == activity || (createProcessor = this.pagePopFactory.createProcessor()) == null)) {
            createProcessor.onActivityStarted(activity);
            this.popMap.put(activity, createProcessor);
        }
        this.topActivity = activity;
    }

    public void onActivityResumed(Activity activity, long j) {
        IPageLoadLifeCycle iPageLoadLifeCycle = this.map.get(activity);
        if (iPageLoadLifeCycle != null) {
            iPageLoadLifeCycle.onActivityResumed(activity, j);
        }
    }

    public void onActivityPaused(Activity activity, long j) {
        IPageLoadLifeCycle iPageLoadLifeCycle = this.map.get(activity);
        if (iPageLoadLifeCycle != null) {
            iPageLoadLifeCycle.onActivityPaused(activity, j);
        }
    }

    public void onActivityStopped(Activity activity, long j) {
        this.activityCount--;
        IPageLoadLifeCycle iPageLoadLifeCycle = this.map.get(activity);
        if (iPageLoadLifeCycle != null) {
            iPageLoadLifeCycle.onActivityStopped(activity, j);
        }
        IPopLifeCycle iPopLifeCycle = this.popMap.get(activity);
        if (iPopLifeCycle != null) {
            iPopLifeCycle.onActivityStopped(activity);
            this.popMap.remove(activity);
        }
        if (this.activityCount == 0) {
            this.topActivity = null;
        }
    }

    public void onActivityDestroyed(Activity activity, long j) {
        IPageLoadLifeCycle iPageLoadLifeCycle = this.map.get(activity);
        if (iPageLoadLifeCycle != null) {
            iPageLoadLifeCycle.onActivityDestroyed(activity, j);
        }
        this.map.remove(activity);
        if (activity == this.topActivity) {
            this.topActivity = null;
        }
    }
}
