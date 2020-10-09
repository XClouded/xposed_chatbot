package com.taobao.monitor.impl.data;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.taobao.application.common.IPageListener;
import com.taobao.application.common.impl.ApmImpl;
import com.taobao.monitor.impl.common.APMContext;
import com.taobao.monitor.impl.common.DynamicConstants;
import com.taobao.monitor.impl.common.Global;
import com.taobao.monitor.impl.data.PageLoadCalculate;
import com.taobao.monitor.impl.data.SimplePageLoadCalculate;
import com.taobao.monitor.impl.data.activity.WindowCallbackProxy;
import com.taobao.monitor.impl.data.newvisible.NewIVDetector;
import com.taobao.monitor.impl.data.newvisible.PageData;
import com.taobao.monitor.impl.logger.DataLoggerUtils;
import com.taobao.monitor.impl.logger.Logger;
import com.taobao.monitor.impl.processor.launcher.PageList;
import com.taobao.monitor.impl.trace.DispatcherManager;
import com.taobao.monitor.impl.trace.IDispatcher;
import com.taobao.monitor.impl.trace.UsableVisibleDispatcher;
import com.taobao.monitor.impl.util.TimeUtils;
import com.taobao.monitor.procedure.IPageNameTransfer;

public class AbstractDataCollector<T> implements Runnable, PageLoadCalculate.IPageLoadPercent, SimplePageLoadCalculate.SimplePageLoadListener, WindowCallbackProxy.DispatchEventListener {
    private static final String ACTIVITY_FRAGMENT_PAGE_NAME = "page_name";
    private static final String ACTIVITY_FRAGMENT_TYPE = "type";
    private static final String ACTIVITY_FRAGMENT_VISIBLE_ACTION = "ACTIVITY_FRAGMENT_VISIBLE_ACTION";
    private static final String ACTIVITY_FRAGMENT_VISIBLE_STATUS = "status";
    private static final int DRAW_TIME_OUT = 20000;
    private static final float PAGE_LOAD_PERCENT = 0.8f;
    private static final String TAG = "AbstractDataCollector";
    private final T activityOrFragment;
    private int count = 0;
    private final long createdTime = TimeUtils.currentTimeMillis();
    private final boolean isActivity;
    private volatile boolean isPageLoadCreated = false;
    private boolean isStopped = false;
    private NewIVDetector ivExecutor;
    private float oldDrawPercent = 0.0f;
    private final IPageListener pageListener = ApmImpl.instance().getPageListenerGroup();
    private IExecutor pageLoadCalculate;
    private final String pageName;
    private IExecutor simplePageCalculate;
    private final Runnable timeoutRunnable = new Runnable() {
        public void run() {
            AbstractDataCollector.this.releasePageLoadCalculate();
        }
    };
    private final String url;
    private boolean usableDispatched = false;
    private UsableVisibleDispatcher usableVisibleDispatcher = null;
    private boolean visibleDispatched = false;

    public void dispatchKeyEvent(KeyEvent keyEvent) {
    }

    protected AbstractDataCollector(T t, String str) {
        boolean z = t instanceof Activity;
        if (z || (t instanceof Fragment)) {
            this.url = str;
            this.activityOrFragment = t;
            this.isActivity = z;
            this.pageName = t.getClass().getName();
            this.pageListener.onPageChanged(this.pageName, 0, TimeUtils.currentTimeMillis());
            Logger.i(TAG, "visibleStart", this.pageName);
            return;
        }
        throw new IllegalArgumentException();
    }

    public void run() {
        this.count++;
        if (this.count > 2) {
            dispatchUsableChanged(TimeUtils.currentTimeMillis());
            return;
        }
        Handler handler = new Handler(Looper.getMainLooper());
        handler.removeCallbacks(this);
        handler.postDelayed(this, 16);
    }

    /* access modifiers changed from: protected */
    public void initDispatcher() {
        IDispatcher iDispatcher;
        if (this.activityOrFragment instanceof Activity) {
            iDispatcher = APMContext.getDispatcher(APMContext.ACTIVITY_USABLE_VISIBLE_DISPATCHER);
        } else {
            iDispatcher = APMContext.getDispatcher(APMContext.FRAGMENT_USABLE_VISIBLE_DISPATCHER);
        }
        if (iDispatcher instanceof UsableVisibleDispatcher) {
            this.usableVisibleDispatcher = (UsableVisibleDispatcher) iDispatcher;
        }
    }

    /* access modifiers changed from: protected */
    public void startPageCalculateExecutor(View view) {
        this.isStopped = false;
        if (!this.isPageLoadCreated) {
            if (!DispatcherManager.isEmpty(this.usableVisibleDispatcher)) {
                this.usableVisibleDispatcher.onRenderStart(this.activityOrFragment, TimeUtils.currentTimeMillis());
            }
            this.pageLoadCalculate = new PageLoadCalculate(view);
            ((PageLoadCalculate) this.pageLoadCalculate).setLifecycle(this);
            this.pageLoadCalculate.execute();
            if (!PageList.inComplexPage(this.activityOrFragment.getClass().getName()) && Build.VERSION.SDK_INT >= 16) {
                this.simplePageCalculate = new SimplePageLoadCalculate(view, this);
                this.simplePageCalculate.execute();
            }
            Global.instance().handler().postDelayed(this.timeoutRunnable, UmbrellaConstants.PERFORMANCE_DATA_ALIVE);
            this.pageListener.onPageChanged(this.pageName, 1, TimeUtils.currentTimeMillis());
            this.isPageLoadCreated = true;
            if (DynamicConstants.needNewApm) {
                String str = null;
                if (this.activityOrFragment instanceof IPageNameTransfer) {
                    str = ((IPageNameTransfer) this.activityOrFragment).alias();
                }
                View view2 = view;
                this.ivExecutor = new NewIVDetector(view2, this.pageName, this.url, this.createdTime, PageData.getPageVisiblePercent(this.activityOrFragment.getClass(), str));
                this.ivExecutor.execute();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void stopPageCalculateExecutor() {
        if (this.ivExecutor != null) {
            this.ivExecutor.stop();
            this.ivExecutor = null;
        }
        releasePageLoadCalculate();
        this.isStopped = !this.isActivity;
    }

    /* access modifiers changed from: protected */
    public void dispatchUsableChanged(long j) {
        if (!this.usableDispatched && !this.isStopped) {
            DataLoggerUtils.log(TAG, "usable", this.pageName);
            Logger.i(TAG, this.pageName, " usable", Long.valueOf(j));
            if (!DispatcherManager.isEmpty(this.usableVisibleDispatcher)) {
                this.usableVisibleDispatcher.onUsableChanged(this.activityOrFragment, 2, j);
            }
            releasePageLoadCalculate();
            this.pageListener.onPageChanged(this.pageName, 3, j);
            this.usableDispatched = true;
        }
    }

    public void pageLoadPercent(float f) {
        Logger.i(TAG, "visiblePercent", Float.valueOf(f), this.pageName);
        if (Math.abs(f - this.oldDrawPercent) > 0.05f || f > PAGE_LOAD_PERCENT) {
            if (!DispatcherManager.isEmpty(this.usableVisibleDispatcher)) {
                this.usableVisibleDispatcher.onRenderPercent(this.activityOrFragment, f, TimeUtils.currentTimeMillis());
            }
            DataLoggerUtils.log(TAG, "visiblePercent", Float.valueOf(f), this.pageName);
            if (f > PAGE_LOAD_PERCENT) {
                dispatchVisibleChanged(TimeUtils.currentTimeMillis());
                run();
            }
            this.oldDrawPercent = f;
        }
    }

    public void onLastVisibleTime(long j) {
        dispatchVisibleChanged(j);
    }

    private void dispatchVisibleChanged(long j) {
        if (!this.visibleDispatched && !this.isStopped) {
            if (!DispatcherManager.isEmpty(this.usableVisibleDispatcher)) {
                Logger.i(TAG, this.pageName, " visible", Long.valueOf(j));
                this.usableVisibleDispatcher.onVisibleChanged(this.activityOrFragment, 2, j);
            }
            this.pageListener.onPageChanged(this.pageName, 2, j);
            releasePageLoadCalculate();
            this.visibleDispatched = true;
        }
    }

    public void onLastUsableTime(long j) {
        dispatchUsableChanged(j);
    }

    /* access modifiers changed from: private */
    public void releasePageLoadCalculate() {
        if (this.pageLoadCalculate != null) {
            synchronized (this) {
                if (!(this.pageLoadCalculate == null && this.simplePageCalculate == null)) {
                    Global.instance().handler().removeCallbacks(this.timeoutRunnable);
                    if (this.pageLoadCalculate != null) {
                        this.pageLoadCalculate.stop();
                    }
                    if (this.simplePageCalculate != null) {
                        this.simplePageCalculate.stop();
                    }
                    doSendPageFinishedEvent();
                    this.pageLoadCalculate = null;
                    this.simplePageCalculate = null;
                }
            }
        }
    }

    private void doSendPageFinishedEvent() {
        LocalBroadcastManager instance = LocalBroadcastManager.getInstance(Global.instance().context());
        Intent intent = new Intent(ACTIVITY_FRAGMENT_VISIBLE_ACTION);
        intent.putExtra("page_name", this.pageName);
        if (this.activityOrFragment instanceof Activity) {
            intent.putExtra("type", "activity");
        } else if (this.activityOrFragment instanceof Fragment) {
            intent.putExtra("type", "fragment");
        } else {
            intent.putExtra("type", "unknown");
        }
        intent.putExtra("status", 1);
        instance.sendBroadcastSync(intent);
        Logger.i(TAG, "doSendPageFinishedEvent:" + this.pageName);
    }

    public void dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0 && this.ivExecutor != null) {
            this.ivExecutor.visibleProxyAction();
        }
    }
}
