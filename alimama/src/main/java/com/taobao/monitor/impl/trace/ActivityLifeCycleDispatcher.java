package com.taobao.monitor.impl.trace;

import android.app.Activity;
import com.taobao.monitor.impl.trace.AbsDispatcher;
import java.util.Map;

public class ActivityLifeCycleDispatcher extends AbsDispatcher<IActivityLifeCycle> {

    public interface IActivityLifeCycle {
        void onActivityCreated(Activity activity, Map<String, Object> map, long j);

        void onActivityDestroyed(Activity activity, long j);

        void onActivityPaused(Activity activity, long j);

        void onActivityResumed(Activity activity, long j);

        void onActivityStarted(Activity activity, long j);

        void onActivityStopped(Activity activity, long j);
    }

    public void onActivityCreated(Activity activity, Map<String, Object> map, long j) {
        final Activity activity2 = activity;
        final Map<String, Object> map2 = map;
        final long j2 = j;
        foreach(new AbsDispatcher.ListenerCaller<IActivityLifeCycle>() {
            public void callListener(IActivityLifeCycle iActivityLifeCycle) {
                iActivityLifeCycle.onActivityCreated(activity2, map2, j2);
            }
        });
    }

    public void onActivityStarted(final Activity activity, final long j) {
        foreach(new AbsDispatcher.ListenerCaller<IActivityLifeCycle>() {
            public void callListener(IActivityLifeCycle iActivityLifeCycle) {
                iActivityLifeCycle.onActivityStarted(activity, j);
            }
        });
    }

    public void onActivityResumed(final Activity activity, final long j) {
        foreach(new AbsDispatcher.ListenerCaller<IActivityLifeCycle>() {
            public void callListener(IActivityLifeCycle iActivityLifeCycle) {
                iActivityLifeCycle.onActivityResumed(activity, j);
            }
        });
    }

    public void onActivityPaused(final Activity activity, final long j) {
        foreach(new AbsDispatcher.ListenerCaller<IActivityLifeCycle>() {
            public void callListener(IActivityLifeCycle iActivityLifeCycle) {
                iActivityLifeCycle.onActivityPaused(activity, j);
            }
        });
    }

    public void onActivityStopped(final Activity activity, final long j) {
        foreach(new AbsDispatcher.ListenerCaller<IActivityLifeCycle>() {
            public void callListener(IActivityLifeCycle iActivityLifeCycle) {
                iActivityLifeCycle.onActivityStopped(activity, j);
            }
        });
    }

    public void onActivityDestroyed(final Activity activity, final long j) {
        foreach(new AbsDispatcher.ListenerCaller<IActivityLifeCycle>() {
            public void callListener(IActivityLifeCycle iActivityLifeCycle) {
                iActivityLifeCycle.onActivityDestroyed(activity, j);
            }
        });
    }
}
