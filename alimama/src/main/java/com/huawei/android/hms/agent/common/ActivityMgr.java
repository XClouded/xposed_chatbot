package com.huawei.android.hms.agent.common;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.List;

public final class ActivityMgr implements Application.ActivityLifecycleCallbacks {
    public static final ActivityMgr INST = new ActivityMgr();
    private static final Object LOCK_LASTACTIVITIES = new Object();
    private Application application;
    private List<Activity> curActivities = new ArrayList();
    private List<IActivityDestroyedCallback> destroyedCallbacks = new ArrayList();
    private List<IActivityPauseCallback> pauseCallbacks = new ArrayList();
    private List<IActivityResumeCallback> resumeCallbacks = new ArrayList();

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    private ActivityMgr() {
    }

    public void init(Application application2, Activity activity) {
        HMSAgentLog.d("init");
        if (this.application != null) {
            this.application.unregisterActivityLifecycleCallbacks(this);
        }
        this.application = application2;
        setCurActivity(activity);
        application2.registerActivityLifecycleCallbacks(this);
    }

    public void release() {
        HMSAgentLog.d("release");
        if (this.application != null) {
            this.application.unregisterActivityLifecycleCallbacks(this);
        }
        clearCurActivities();
        clearActivitResumeCallbacks();
        clearActivitPauseCallbacks();
        this.application = null;
    }

    public void registerActivitResumeEvent(IActivityResumeCallback iActivityResumeCallback) {
        HMSAgentLog.d("registerOnResume:" + StrUtils.objDesc(iActivityResumeCallback));
        this.resumeCallbacks.add(iActivityResumeCallback);
    }

    public void unRegisterActivitResumeEvent(IActivityResumeCallback iActivityResumeCallback) {
        HMSAgentLog.d("unRegisterOnResume:" + StrUtils.objDesc(iActivityResumeCallback));
        this.resumeCallbacks.remove(iActivityResumeCallback);
    }

    public void registerActivitPauseEvent(IActivityPauseCallback iActivityPauseCallback) {
        HMSAgentLog.d("registerOnPause:" + StrUtils.objDesc(iActivityPauseCallback));
        this.pauseCallbacks.add(iActivityPauseCallback);
    }

    public void unRegisterActivitPauseEvent(IActivityPauseCallback iActivityPauseCallback) {
        HMSAgentLog.d("unRegisterOnPause:" + StrUtils.objDesc(iActivityPauseCallback));
        this.pauseCallbacks.remove(iActivityPauseCallback);
    }

    public void registerActivitDestroyedEvent(IActivityDestroyedCallback iActivityDestroyedCallback) {
        HMSAgentLog.d("registerOnDestroyed:" + StrUtils.objDesc(iActivityDestroyedCallback));
        this.destroyedCallbacks.add(iActivityDestroyedCallback);
    }

    public void unRegisterActivitDestroyedEvent(IActivityDestroyedCallback iActivityDestroyedCallback) {
        HMSAgentLog.d("unRegisterOnDestroyed:" + StrUtils.objDesc(iActivityDestroyedCallback));
        this.destroyedCallbacks.remove(iActivityDestroyedCallback);
    }

    public void clearActivitResumeCallbacks() {
        HMSAgentLog.d("clearOnResumeCallback");
        this.resumeCallbacks.clear();
    }

    public void clearActivitPauseCallbacks() {
        HMSAgentLog.d("clearOnPauseCallback");
        this.pauseCallbacks.clear();
    }

    public Activity getLastActivity() {
        return getLastActivityInner();
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
        HMSAgentLog.d("onCreated:" + StrUtils.objDesc(activity));
        setCurActivity(activity);
    }

    public void onActivityStarted(Activity activity) {
        HMSAgentLog.d("onStarted:" + StrUtils.objDesc(activity));
        setCurActivity(activity);
    }

    public void onActivityResumed(Activity activity) {
        HMSAgentLog.d("onResumed:" + StrUtils.objDesc(activity));
        setCurActivity(activity);
        for (IActivityResumeCallback onActivityResume : new ArrayList(this.resumeCallbacks)) {
            onActivityResume.onActivityResume(activity);
        }
    }

    public void onActivityPaused(Activity activity) {
        HMSAgentLog.d("onPaused:" + StrUtils.objDesc(activity));
        for (IActivityPauseCallback onActivityPause : new ArrayList(this.pauseCallbacks)) {
            onActivityPause.onActivityPause(activity);
        }
    }

    public void onActivityStopped(Activity activity) {
        HMSAgentLog.d("onStopped:" + StrUtils.objDesc(activity));
    }

    public void onActivityDestroyed(Activity activity) {
        HMSAgentLog.d("onDestroyed:" + StrUtils.objDesc(activity));
        removeActivity(activity);
        for (IActivityDestroyedCallback onActivityDestroyed : new ArrayList(this.destroyedCallbacks)) {
            onActivityDestroyed.onActivityDestroyed(activity, getLastActivityInner());
        }
    }

    private void removeActivity(Activity activity) {
        synchronized (LOCK_LASTACTIVITIES) {
            this.curActivities.remove(activity);
        }
    }

    private void setCurActivity(Activity activity) {
        synchronized (LOCK_LASTACTIVITIES) {
            int indexOf = this.curActivities.indexOf(activity);
            if (indexOf == -1) {
                this.curActivities.add(activity);
            } else if (indexOf < this.curActivities.size() - 1) {
                this.curActivities.remove(activity);
                this.curActivities.add(activity);
            }
        }
    }

    private Activity getLastActivityInner() {
        synchronized (LOCK_LASTACTIVITIES) {
            if (this.curActivities.size() <= 0) {
                return null;
            }
            Activity activity = this.curActivities.get(this.curActivities.size() - 1);
            return activity;
        }
    }

    private void clearCurActivities() {
        synchronized (LOCK_LASTACTIVITIES) {
            this.curActivities.clear();
        }
    }
}
