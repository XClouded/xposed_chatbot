package com.taobao.application.common.impl;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Iterator;

@TargetApi(14)
class MainApplicationCallbackGroup implements Application.ActivityLifecycleCallbacks, ICallbackGroup<Application.ActivityLifecycleCallbacks> {
    private final ArrayList<Application.ActivityLifecycleCallbacks> callbackGroup = new ArrayList<>();

    MainApplicationCallbackGroup() {
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
        synchronized (this.callbackGroup) {
            Iterator<Application.ActivityLifecycleCallbacks> it = this.callbackGroup.iterator();
            while (it.hasNext()) {
                it.next().onActivityCreated(activity, bundle);
            }
        }
    }

    public void onActivityStarted(Activity activity) {
        synchronized (this.callbackGroup) {
            Iterator<Application.ActivityLifecycleCallbacks> it = this.callbackGroup.iterator();
            while (it.hasNext()) {
                it.next().onActivityStarted(activity);
            }
        }
    }

    public void onActivityResumed(Activity activity) {
        synchronized (this.callbackGroup) {
            Iterator<Application.ActivityLifecycleCallbacks> it = this.callbackGroup.iterator();
            while (it.hasNext()) {
                it.next().onActivityResumed(activity);
            }
        }
    }

    public void onActivityPaused(Activity activity) {
        synchronized (this.callbackGroup) {
            Iterator<Application.ActivityLifecycleCallbacks> it = this.callbackGroup.iterator();
            while (it.hasNext()) {
                it.next().onActivityPaused(activity);
            }
        }
    }

    public void onActivityStopped(Activity activity) {
        synchronized (this.callbackGroup) {
            Iterator<Application.ActivityLifecycleCallbacks> it = this.callbackGroup.iterator();
            while (it.hasNext()) {
                it.next().onActivityStopped(activity);
            }
        }
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        synchronized (this.callbackGroup) {
            Iterator<Application.ActivityLifecycleCallbacks> it = this.callbackGroup.iterator();
            while (it.hasNext()) {
                it.next().onActivitySaveInstanceState(activity, bundle);
            }
        }
    }

    public void onActivityDestroyed(Activity activity) {
        synchronized (this.callbackGroup) {
            Iterator<Application.ActivityLifecycleCallbacks> it = this.callbackGroup.iterator();
            while (it.hasNext()) {
                it.next().onActivityDestroyed(activity);
            }
        }
    }

    public void addCallback(Application.ActivityLifecycleCallbacks activityLifecycleCallbacks) {
        if (activityLifecycleCallbacks != null) {
            synchronized (this.callbackGroup) {
                if (!this.callbackGroup.contains(activityLifecycleCallbacks)) {
                    this.callbackGroup.add(activityLifecycleCallbacks);
                }
            }
            return;
        }
        throw new IllegalArgumentException();
    }

    public void removeCallback(Application.ActivityLifecycleCallbacks activityLifecycleCallbacks) {
        if (activityLifecycleCallbacks != null) {
            synchronized (this.callbackGroup) {
                this.callbackGroup.remove(activityLifecycleCallbacks);
            }
            return;
        }
        throw new IllegalArgumentException();
    }
}
