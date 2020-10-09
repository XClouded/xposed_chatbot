package com.taobao.phenix.compat.stat;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;

public class NavigationLifecycleObserver implements Application.ActivityLifecycleCallbacks, NavigationInfoObtainer {
    private static final NavigationLifecycleObserver sNavigationLifecycleObserver = new NavigationLifecycleObserver();
    private String mCurrentUrl;
    private String mCurrentWindowName;

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public static NavigationLifecycleObserver getInstance() {
        return sNavigationLifecycleObserver;
    }

    public void onActivityResumed(Activity activity) {
        if (activity != null) {
            this.mCurrentWindowName = activity.getLocalClassName();
            Intent intent = activity.getIntent();
            if (intent != null) {
                this.mCurrentUrl = intent.getDataString();
            }
        }
    }

    public String getCurrentWindowName() {
        return this.mCurrentWindowName;
    }

    public String getCurrentUrl() {
        return this.mCurrentUrl;
    }
}
