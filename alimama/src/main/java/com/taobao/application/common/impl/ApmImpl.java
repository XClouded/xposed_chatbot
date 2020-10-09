package com.taobao.application.common.impl;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.taobao.application.common.Apm;
import com.taobao.application.common.IApmEventListener;
import com.taobao.application.common.IAppLaunchListener;
import com.taobao.application.common.IAppPreferences;
import com.taobao.application.common.IApplicationMonitor;
import com.taobao.application.common.IPageListener;
import com.taobao.monitor.impl.logger.Logger;
import java.util.concurrent.ConcurrentHashMap;

public class ApmImpl implements Apm, IApplicationMonitor {
    private static final String TAG = "ApmImpl";
    private final IListenerGroup<IApmEventListener> apmEventListenerGroup;
    private final ICallbackGroup<Application.ActivityLifecycleCallbacks> asyncCallbackGroup;
    private ConcurrentHashMap<Application.ActivityLifecycleCallbacks, Boolean> concurrentHashMap;
    private final IListenerGroup<IAppLaunchListener> launchListenerGroup;
    private final IListenerGroup<IPageListener> pageListenerGroup;
    private final Handler secHandler;
    private volatile Activity topActivity;
    private final ICallbackGroup<Application.ActivityLifecycleCallbacks> uiCallbackGroup;

    private <T> T castClass(Object obj) {
        return obj;
    }

    private ApmImpl() {
        this.uiCallbackGroup = new MainApplicationCallbackGroup();
        this.asyncCallbackGroup = new ApplicationCallbackGroup();
        this.pageListenerGroup = new PageListenerGroup();
        this.launchListenerGroup = new AppLaunchListenerGroup();
        this.apmEventListenerGroup = new ApmEventListenerGroup();
        this.concurrentHashMap = new ConcurrentHashMap<>();
        HandlerThread handlerThread = new HandlerThread("Apm-Sec");
        handlerThread.start();
        this.secHandler = new Handler(handlerThread.getLooper());
        Logger.e(TAG, "init");
    }

    public static ApmImpl instance() {
        return Holder.INSTANCE;
    }

    @TargetApi(14)
    public void addActivityLifecycle(Application.ActivityLifecycleCallbacks activityLifecycleCallbacks, boolean z) {
        if (activityLifecycleCallbacks == null) {
            throw new IllegalArgumentException();
        } else if (this.concurrentHashMap.put(activityLifecycleCallbacks, Boolean.valueOf(z)) != null) {
            throw new IllegalArgumentException();
        } else if (z) {
            this.uiCallbackGroup.addCallback(activityLifecycleCallbacks);
        } else {
            this.asyncCallbackGroup.addCallback(activityLifecycleCallbacks);
        }
    }

    public void removeActivityLifecycle(Application.ActivityLifecycleCallbacks activityLifecycleCallbacks) {
        if (activityLifecycleCallbacks != null) {
            Boolean bool = this.concurrentHashMap.get(activityLifecycleCallbacks);
            if (bool != null) {
                boolean booleanValue = bool.booleanValue();
                this.concurrentHashMap.remove(activityLifecycleCallbacks);
                if (booleanValue) {
                    this.uiCallbackGroup.removeCallback(activityLifecycleCallbacks);
                } else {
                    this.asyncCallbackGroup.removeCallback(activityLifecycleCallbacks);
                }
            } else {
                throw new IllegalArgumentException();
            }
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void addPageListener(IPageListener iPageListener) {
        this.pageListenerGroup.addListener(iPageListener);
    }

    public void removePageListener(IPageListener iPageListener) {
        this.pageListenerGroup.removeListener(iPageListener);
    }

    public void addAppLaunchListener(IAppLaunchListener iAppLaunchListener) {
        this.launchListenerGroup.addListener(iAppLaunchListener);
    }

    public void removeAppLaunchListener(IAppLaunchListener iAppLaunchListener) {
        this.launchListenerGroup.removeListener(iAppLaunchListener);
    }

    public void addApmEventListener(IApmEventListener iApmEventListener) {
        this.apmEventListenerGroup.addListener(iApmEventListener);
    }

    public void removeApmEventListener(IApmEventListener iApmEventListener) {
        this.apmEventListenerGroup.removeListener(iApmEventListener);
    }

    public IAppPreferences getAppPreferences() {
        return AppPreferencesImpl.instance();
    }

    public Activity getTopActivity() {
        return this.topActivity;
    }

    public Looper getAsyncLooper() {
        return this.secHandler.getLooper();
    }

    public Handler getAsyncHandler() {
        return this.secHandler;
    }

    @TargetApi(14)
    public Application.ActivityLifecycleCallbacks getUiCallbackGroup() {
        return (Application.ActivityLifecycleCallbacks) castClass(this.uiCallbackGroup);
    }

    @TargetApi(14)
    public Application.ActivityLifecycleCallbacks getAsyncCallbackGroup() {
        return (Application.ActivityLifecycleCallbacks) castClass(this.asyncCallbackGroup);
    }

    public IPageListener getPageListenerGroup() {
        return (IPageListener) castClass(this.pageListenerGroup);
    }

    public IAppLaunchListener getLaunchListenerGroup() {
        return (IAppLaunchListener) castClass(this.launchListenerGroup);
    }

    public IApmEventListener getApmEventListenerGroup() {
        return (IApmEventListener) castClass(this.apmEventListenerGroup);
    }

    public void setTopActivity(Activity activity) {
        this.topActivity = activity;
    }

    public void secHandler(Runnable runnable) {
        this.secHandler.post(runnable);
    }

    private static class Holder {
        static final ApmImpl INSTANCE = new ApmImpl();

        private Holder() {
        }
    }
}
