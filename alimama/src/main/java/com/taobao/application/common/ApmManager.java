package com.taobao.application.common;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;
import com.taobao.application.common.Apm;

public class ApmManager {
    private static IApplicationMonitor apmDelegate;

    public static void addActivityLifecycle(Apm.OnActivityLifecycleCallbacks onActivityLifecycleCallbacks, boolean z) {
        if (apmDelegate != null) {
            apmDelegate.addActivityLifecycle(onActivityLifecycleCallbacks, z);
        }
    }

    public static void removeActivityLifecycle(Apm.OnActivityLifecycleCallbacks onActivityLifecycleCallbacks) {
        if (apmDelegate != null) {
            apmDelegate.removeActivityLifecycle(onActivityLifecycleCallbacks);
        }
    }

    public static void addPageListener(Apm.OnPageListener onPageListener) {
        if (apmDelegate != null) {
            apmDelegate.addPageListener(onPageListener);
        }
    }

    public static void removePageListener(Apm.OnPageListener onPageListener) {
        if (apmDelegate != null) {
            apmDelegate.removePageListener(onPageListener);
        }
    }

    public static void addAppLaunchListener(Apm.OnAppLaunchListener onAppLaunchListener) {
        if (apmDelegate != null) {
            apmDelegate.addAppLaunchListener(onAppLaunchListener);
        }
    }

    public static void removeAppLaunchListener(Apm.OnAppLaunchListener onAppLaunchListener) {
        if (apmDelegate != null) {
            apmDelegate.removeAppLaunchListener(onAppLaunchListener);
        }
    }

    public static IAppPreferences getAppPreferences() {
        if (apmDelegate != null) {
            return apmDelegate.getAppPreferences();
        }
        return IAppPreferences.DEFAULT;
    }

    public static Activity getTopActivity() {
        if (apmDelegate != null) {
            return apmDelegate.getTopActivity();
        }
        return null;
    }

    public static void addApmEventListener(IApmEventListener iApmEventListener) {
        if (apmDelegate != null) {
            apmDelegate.addApmEventListener(iApmEventListener);
        }
    }

    public static void removeApmEventListener(IApmEventListener iApmEventListener) {
        if (apmDelegate != null) {
            apmDelegate.removeApmEventListener(iApmEventListener);
        }
    }

    public static Handler getAsyncHandler() {
        if (apmDelegate != null) {
            return apmDelegate.getAsyncHandler();
        }
        return null;
    }

    public static Looper getAsyncLooper() {
        if (apmDelegate != null) {
            return apmDelegate.getAsyncLooper();
        }
        return null;
    }

    static void setApmDelegate(IApplicationMonitor iApplicationMonitor) {
        apmDelegate = iApplicationMonitor;
    }
}
