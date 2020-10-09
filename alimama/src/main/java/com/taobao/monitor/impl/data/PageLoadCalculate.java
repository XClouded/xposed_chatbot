package com.taobao.monitor.impl.data;

import android.view.View;
import com.taobao.monitor.impl.common.Global;
import java.lang.ref.WeakReference;

public class PageLoadCalculate implements IExecutor, Runnable {
    private static final long INTERVAL = 75;
    private static final String TAG = "PageLoadCalculate";
    private final WeakReference<View> containRef;
    /* access modifiers changed from: private */
    public IPageLoadPercent lifecycle;
    private volatile boolean stopped = false;

    public interface IPageLoadPercent {
        void pageLoadPercent(float f);
    }

    public PageLoadCalculate(View view) {
        this.containRef = new WeakReference<>(view);
    }

    public PageLoadCalculate setLifecycle(IPageLoadPercent iPageLoadPercent) {
        this.lifecycle = iPageLoadPercent;
        return this;
    }

    public void execute() {
        Global.instance().getAsyncUiHandler().postDelayed(this, 50);
    }

    public void stop() {
        this.stopped = true;
        Global.instance().getAsyncUiHandler().removeCallbacks(this);
        Global.instance().handler().post(new Runnable() {
            public void run() {
                IPageLoadPercent unused = PageLoadCalculate.this.lifecycle = null;
            }
        });
    }

    public void run() {
        if (!this.stopped) {
            check();
            Global.instance().getAsyncUiHandler().postDelayed(this, 75);
        }
    }

    private void check() {
        View view = (View) this.containRef.get();
        if (view == null) {
            stop();
            return;
        }
        try {
            View findViewById = view.findViewById(view.getResources().getIdentifier("content", "id", "android"));
            if (findViewById == null) {
                findViewById = view;
            }
            if (findViewById.getHeight() * findViewById.getWidth() != 0) {
                calculateDraw(findViewById, view);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private void calculateDraw(View view, View view2) {
        if (this.lifecycle != null) {
            this.lifecycle.pageLoadPercent(new CanvasCalculator(view, view2).calculate());
        }
    }
}
