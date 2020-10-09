package com.taobao.monitor.impl.data;

import android.annotation.TargetApi;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.annotation.UiThread;
import com.taobao.monitor.impl.common.Global;
import com.taobao.monitor.impl.util.TimeUtils;

@TargetApi(16)
public class SimplePageLoadCalculate implements IExecutor, ViewTreeObserver.OnDrawListener {
    private static final long DELAY_TIME = 3000;
    /* access modifiers changed from: private */
    public final View decorView;
    private volatile boolean isStopDetect = false;
    private volatile boolean isStopped = false;
    /* access modifiers changed from: private */
    public long lastDrawTime;
    /* access modifiers changed from: private */
    public long lastUsableTime;
    /* access modifiers changed from: private */
    public final SimplePageLoadListener listener;
    /* access modifiers changed from: private */
    public final Handler mainHandler = new Handler(Looper.getMainLooper());
    /* access modifiers changed from: private */
    public int usableCount = 0;
    private final Runnable usableRunnable = new Runnable() {
        @UiThread
        public void run() {
            SimplePageLoadCalculate.access$408(SimplePageLoadCalculate.this);
            if (SimplePageLoadCalculate.this.usableCount > 2) {
                long unused = SimplePageLoadCalculate.this.lastUsableTime = TimeUtils.currentTimeMillis();
                return;
            }
            SimplePageLoadCalculate.this.mainHandler.removeCallbacks(this);
            SimplePageLoadCalculate.this.mainHandler.postDelayed(this, 16);
        }
    };
    private final Runnable visibleRunnable = new Runnable() {
        public void run() {
            SimplePageLoadCalculate.this.stopVisibleDetect();
            SimplePageLoadCalculate.this.listener.onLastVisibleTime(SimplePageLoadCalculate.this.lastDrawTime);
            if (SimplePageLoadCalculate.this.lastUsableTime > SimplePageLoadCalculate.this.lastDrawTime) {
                SimplePageLoadCalculate.this.listener.onLastUsableTime(SimplePageLoadCalculate.this.lastUsableTime);
                SimplePageLoadCalculate.this.stop();
            }
        }
    };

    public interface SimplePageLoadListener {
        void onLastUsableTime(long j);

        void onLastVisibleTime(long j);
    }

    static /* synthetic */ int access$408(SimplePageLoadCalculate simplePageLoadCalculate) {
        int i = simplePageLoadCalculate.usableCount;
        simplePageLoadCalculate.usableCount = i + 1;
        return i;
    }

    public SimplePageLoadCalculate(View view, SimplePageLoadListener simplePageLoadListener) {
        if (view == null || simplePageLoadListener == null) {
            throw new IllegalArgumentException();
        }
        this.decorView = view;
        this.listener = simplePageLoadListener;
    }

    public void execute() {
        this.mainHandler.post(new Runnable() {
            public void run() {
                ViewTreeObserver viewTreeObserver = SimplePageLoadCalculate.this.decorView.getViewTreeObserver();
                if (viewTreeObserver != null) {
                    viewTreeObserver.addOnDrawListener(SimplePageLoadCalculate.this);
                }
            }
        });
        Global.instance().handler().postDelayed(this.visibleRunnable, 3000);
    }

    public void stop() {
        if (!this.isStopped) {
            this.isStopped = true;
            stopVisibleDetect();
            this.mainHandler.removeCallbacks(this.usableRunnable);
        }
    }

    /* access modifiers changed from: private */
    public void stopVisibleDetect() {
        if (!this.isStopDetect) {
            this.isStopDetect = true;
            this.mainHandler.post(new Runnable() {
                public void run() {
                    ViewTreeObserver viewTreeObserver = SimplePageLoadCalculate.this.decorView.getViewTreeObserver();
                    if (viewTreeObserver != null) {
                        viewTreeObserver.removeOnDrawListener(SimplePageLoadCalculate.this);
                    }
                }
            });
            Global.instance().handler().removeCallbacks(this.visibleRunnable);
        }
    }

    public void onDraw() {
        this.lastDrawTime = TimeUtils.currentTimeMillis();
        this.usableCount = 0;
        Global.instance().handler().removeCallbacks(this.visibleRunnable);
        Global.instance().handler().postDelayed(this.visibleRunnable, 3000);
        this.mainHandler.removeCallbacks(this.usableRunnable);
        this.mainHandler.postDelayed(this.usableRunnable, 16);
    }
}
