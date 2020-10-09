package com.taobao.monitor.impl.data.activity;

import android.app.Activity;
import android.os.Build;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import com.taobao.monitor.impl.common.APMContext;
import com.taobao.monitor.impl.data.AbstractDataCollector;
import com.taobao.monitor.impl.data.DrawTimeCollector;
import com.taobao.monitor.impl.data.GlobalStats;
import com.taobao.monitor.impl.data.activity.ActivityLifecycle;
import com.taobao.monitor.impl.data.activity.WindowCallbackProxy;
import com.taobao.monitor.impl.processor.launcher.PageList;
import com.taobao.monitor.impl.trace.ActivityEventDispatcher;
import com.taobao.monitor.impl.trace.ActivityLifeCycleDispatcher;
import com.taobao.monitor.impl.trace.DispatcherManager;
import com.taobao.monitor.impl.trace.IDispatcher;
import com.taobao.monitor.impl.util.ActivityUtils;
import com.taobao.monitor.impl.util.TimeUtils;
import java.lang.reflect.Proxy;
import java.util.Map;

public class ActivityDataCollector extends AbstractDataCollector<Activity> implements ActivityLifecycle.IPageLoadLifeCycle, WindowCallbackProxy.DispatchEventListener {
    private static final String TAG = "ActivityDataCollector";
    private final Activity activity;
    private DrawTimeCollector drawTimeCollector;
    private ActivityEventDispatcher eventDispatcher = null;
    private ActivityLifeCycleDispatcher lifeCycleDispatcher = null;
    private WindowCallbackProxy proxy;

    ActivityDataCollector(Activity activity2, String str) {
        super(activity2, str);
        this.activity = activity2;
        if (Build.VERSION.SDK_INT >= 16) {
            this.drawTimeCollector = new DrawTimeCollector();
        }
        initDispatcher();
    }

    /* access modifiers changed from: protected */
    public void initDispatcher() {
        super.initDispatcher();
        IDispatcher dispatcher = APMContext.getDispatcher(APMContext.ACTIVITY_LIFECYCLE_DISPATCHER);
        if (dispatcher instanceof ActivityLifeCycleDispatcher) {
            this.lifeCycleDispatcher = (ActivityLifeCycleDispatcher) dispatcher;
        }
        IDispatcher dispatcher2 = APMContext.getDispatcher(APMContext.ACTIVITY_EVENT_DISPATCHER);
        if (dispatcher2 instanceof ActivityEventDispatcher) {
            this.eventDispatcher = (ActivityEventDispatcher) dispatcher2;
        }
    }

    public void onActivityCreated(Activity activity2, Map<String, Object> map) {
        initDispatcher();
        if (!DispatcherManager.isEmpty(this.lifeCycleDispatcher)) {
            this.lifeCycleDispatcher.onActivityCreated(activity2, map, TimeUtils.currentTimeMillis());
        }
    }

    public void onActivityStarted(Activity activity2) {
        Window.Callback callback;
        if (!DispatcherManager.isEmpty(this.lifeCycleDispatcher)) {
            this.lifeCycleDispatcher.onActivityStarted(activity2, TimeUtils.currentTimeMillis());
        }
        Window window = activity2.getWindow();
        if (window != null && this.proxy == null && (callback = window.getCallback()) != null) {
            this.proxy = new WindowCallbackProxy(callback);
            try {
                window.setCallback((Window.Callback) Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{Window.Callback.class}, this.proxy));
            } catch (Exception e) {
                e.printStackTrace();
            }
            this.proxy.addListener(this);
        }
    }

    public void onActivityResumed(Activity activity2) {
        View decorView;
        if (!DispatcherManager.isEmpty(this.lifeCycleDispatcher)) {
            this.lifeCycleDispatcher.onActivityResumed(activity2, TimeUtils.currentTimeMillis());
        }
        Window window = activity2.getWindow();
        if (window != null && (decorView = window.getDecorView()) != null) {
            if (!PageList.inBlackList(ActivityUtils.getPageName(activity2))) {
                startPageCalculateExecutor(decorView);
            }
            if (Build.VERSION.SDK_INT >= 16) {
                decorView.getViewTreeObserver().addOnDrawListener(this.drawTimeCollector);
            }
        }
    }

    public void onActivityPaused(Activity activity2) {
        if (!DispatcherManager.isEmpty(this.lifeCycleDispatcher)) {
            this.lifeCycleDispatcher.onActivityPaused(activity2, TimeUtils.currentTimeMillis());
        }
        if (Build.VERSION.SDK_INT >= 16) {
            activity2.getWindow().getDecorView().getViewTreeObserver().removeOnDrawListener(this.drawTimeCollector);
        }
    }

    public void onActivityStopped(Activity activity2) {
        if (!DispatcherManager.isEmpty(this.lifeCycleDispatcher)) {
            this.lifeCycleDispatcher.onActivityStopped(activity2, TimeUtils.currentTimeMillis());
        }
        if (!PageList.inBlackList(ActivityUtils.getPageName(activity2))) {
            stopPageCalculateExecutor();
        }
    }

    public void onActivityDestroyed(Activity activity2) {
        if (!DispatcherManager.isEmpty(this.lifeCycleDispatcher)) {
            this.lifeCycleDispatcher.onActivityDestroyed(activity2, TimeUtils.currentTimeMillis());
        }
    }

    public void dispatchTouchEvent(MotionEvent motionEvent) {
        super.dispatchTouchEvent(motionEvent);
        GlobalStats.lastTouchTime = TimeUtils.currentTimeMillis();
        if (!DispatcherManager.isEmpty(this.eventDispatcher)) {
            this.eventDispatcher.onTouch(this.activity, motionEvent, TimeUtils.currentTimeMillis());
        }
        dispatchUsableChanged(TimeUtils.currentTimeMillis());
    }

    public void dispatchKeyEvent(KeyEvent keyEvent) {
        super.dispatchKeyEvent(keyEvent);
        if (!DispatcherManager.isEmpty(this.eventDispatcher)) {
            this.eventDispatcher.onKey(this.activity, keyEvent, TimeUtils.currentTimeMillis());
        }
    }

    public WindowCallbackProxy getWindowCallbackProxy() {
        return this.proxy;
    }
}
