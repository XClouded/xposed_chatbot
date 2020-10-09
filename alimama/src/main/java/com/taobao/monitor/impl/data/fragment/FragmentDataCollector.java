package com.taobao.monitor.impl.data.fragment;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import androidx.fragment.app.Fragment;
import com.taobao.monitor.impl.common.APMContext;
import com.taobao.monitor.impl.data.AbstractDataCollector;
import com.taobao.monitor.impl.data.activity.ActivityDataCollector;
import com.taobao.monitor.impl.data.activity.WindowCallbackProxy;
import com.taobao.monitor.impl.data.fragment.FragmentLifecycle;
import com.taobao.monitor.impl.trace.DispatcherManager;
import com.taobao.monitor.impl.trace.FragmentLifecycleDispatcher;
import com.taobao.monitor.impl.trace.IDispatcher;
import com.taobao.monitor.impl.util.TimeUtils;

public class FragmentDataCollector extends AbstractDataCollector<Fragment> implements FragmentLifecycle.IFragmentLoadLifeCycle {
    private static final String TAG = "FragmentDataCollector";
    private final Activity activity;
    private ActivityDataCollector activityDataCollector;
    private FragmentLifecycleDispatcher lifeCycleDispatcher;

    FragmentDataCollector(Activity activity2, Fragment fragment, ActivityDataCollector activityDataCollector2) {
        super(fragment, (String) null);
        this.activity = activity2;
        this.activityDataCollector = activityDataCollector2;
        initDispatcher();
    }

    /* access modifiers changed from: protected */
    public void initDispatcher() {
        super.initDispatcher();
        IDispatcher dispatcher = APMContext.getDispatcher(APMContext.FRAGMENT_LIFECYCLE_DISPATCHER);
        if (dispatcher instanceof FragmentLifecycleDispatcher) {
            this.lifeCycleDispatcher = (FragmentLifecycleDispatcher) dispatcher;
        }
    }

    public void onFragmentPreAttached(Fragment fragment) {
        if (!DispatcherManager.isEmpty(this.lifeCycleDispatcher)) {
            this.lifeCycleDispatcher.dispatchFragmentPreAttached(fragment, TimeUtils.currentTimeMillis());
        }
    }

    public void onFragmentAttached(Fragment fragment) {
        if (!DispatcherManager.isEmpty(this.lifeCycleDispatcher)) {
            this.lifeCycleDispatcher.dispatchFragmentAttached(fragment, TimeUtils.currentTimeMillis());
        }
    }

    public void onFragmentPreCreated(Fragment fragment) {
        if (!DispatcherManager.isEmpty(this.lifeCycleDispatcher)) {
            this.lifeCycleDispatcher.dispatchFragmentPreCreated(fragment, TimeUtils.currentTimeMillis());
        }
    }

    public void onFragmentCreated(Fragment fragment) {
        if (!DispatcherManager.isEmpty(this.lifeCycleDispatcher)) {
            this.lifeCycleDispatcher.dispatchFragmentCreated(fragment, TimeUtils.currentTimeMillis());
        }
    }

    public void onFragmentActivityCreated(Fragment fragment) {
        if (!DispatcherManager.isEmpty(this.lifeCycleDispatcher)) {
            this.lifeCycleDispatcher.dispatchFragmentActivityCreated(fragment, TimeUtils.currentTimeMillis());
        }
    }

    public void onFragmentViewCreated(Fragment fragment) {
        if (!DispatcherManager.isEmpty(this.lifeCycleDispatcher)) {
            this.lifeCycleDispatcher.dispatchFragmentViewCreated(fragment, TimeUtils.currentTimeMillis());
        }
    }

    public void onFragmentStarted(Fragment fragment) {
        if (!DispatcherManager.isEmpty(this.lifeCycleDispatcher)) {
            this.lifeCycleDispatcher.dispatchFragmentStarted(fragment, TimeUtils.currentTimeMillis());
        }
    }

    public void onFragmentResumed(Fragment fragment) {
        Window window;
        View decorView;
        if (!DispatcherManager.isEmpty(this.lifeCycleDispatcher)) {
            this.lifeCycleDispatcher.dispatchFragmentResumed(fragment, TimeUtils.currentTimeMillis());
        }
        if (this.activity != null && (window = this.activity.getWindow()) != null && (decorView = window.getDecorView()) != null) {
            WindowCallbackProxy windowCallbackProxy = this.activityDataCollector.getWindowCallbackProxy();
            if (windowCallbackProxy != null) {
                windowCallbackProxy.addListener(this);
            }
            startPageCalculateExecutor(decorView);
        }
    }

    public void onFragmentPaused(Fragment fragment) {
        if (!DispatcherManager.isEmpty(this.lifeCycleDispatcher)) {
            this.lifeCycleDispatcher.dispatchFragmentPaused(fragment, TimeUtils.currentTimeMillis());
        }
    }

    public void onFragmentStopped(Fragment fragment) {
        if (!DispatcherManager.isEmpty(this.lifeCycleDispatcher)) {
            this.lifeCycleDispatcher.dispatchFragmentStopped(fragment, TimeUtils.currentTimeMillis());
        }
        stopPageCalculateExecutor();
        WindowCallbackProxy windowCallbackProxy = this.activityDataCollector.getWindowCallbackProxy();
        if (windowCallbackProxy != null) {
            windowCallbackProxy.removeListener(this);
        }
    }

    public void onFragmentSaveInstanceState(Fragment fragment) {
        if (!DispatcherManager.isEmpty(this.lifeCycleDispatcher)) {
            this.lifeCycleDispatcher.dispatchFragmentSaveInstanceState(fragment, TimeUtils.currentTimeMillis());
        }
    }

    public void onFragmentViewDestroyed(Fragment fragment) {
        if (!DispatcherManager.isEmpty(this.lifeCycleDispatcher)) {
            this.lifeCycleDispatcher.dispatchFragmentViewDestroyed(fragment, TimeUtils.currentTimeMillis());
        }
    }

    public void onFragmentDestroyed(Fragment fragment) {
        if (!DispatcherManager.isEmpty(this.lifeCycleDispatcher)) {
            this.lifeCycleDispatcher.dispatchFragmentDestroyed(fragment, TimeUtils.currentTimeMillis());
        }
    }

    public void onFragmentDetached(Fragment fragment) {
        if (!DispatcherManager.isEmpty(this.lifeCycleDispatcher)) {
            this.lifeCycleDispatcher.dispatchFragmentDetached(fragment, TimeUtils.currentTimeMillis());
        }
    }
}
