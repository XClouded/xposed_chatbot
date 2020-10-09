package com.taobao.weex.analyzer.core;

import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import com.taobao.weex.analyzer.view.overlay.IOverlayView;

public abstract class AbstractLoopTask implements IOverlayView.ITask, Runnable {
    private static final int DEFAULT_DELAY_MILLIS = 500;
    private final boolean isRunInMainThread;
    private boolean isStop;
    protected int mDelayMillis;
    private HandlerThreadWrapper mHandlerThreadWrapper;
    private Handler mUIHandler;

    /* access modifiers changed from: protected */
    public abstract void onRun();

    /* access modifiers changed from: protected */
    public void onStart() {
    }

    /* access modifiers changed from: protected */
    public abstract void onStop();

    public AbstractLoopTask(boolean z) {
        this.isStop = true;
        this.mUIHandler = new Handler(Looper.getMainLooper());
        this.mDelayMillis = 500;
        this.isRunInMainThread = z;
    }

    public AbstractLoopTask(boolean z, int i) {
        this.isStop = true;
        this.mUIHandler = new Handler(Looper.getMainLooper());
        this.isRunInMainThread = z;
        this.mDelayMillis = i;
    }

    public void setDelayInMillis(int i) {
        this.mDelayMillis = i;
    }

    public int getDelayInMillis() {
        return this.mDelayMillis;
    }

    public void run() {
        if (!this.isStop) {
            try {
                onRun();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (this.isRunInMainThread) {
                this.mUIHandler.postDelayed(this, (long) this.mDelayMillis);
            } else if (this.mHandlerThreadWrapper != null && this.mHandlerThreadWrapper.isAlive()) {
                this.mHandlerThreadWrapper.getHandler().postDelayed(this, (long) this.mDelayMillis);
            }
        }
    }

    public void start() {
        if (!this.isStop) {
            stop();
        }
        this.isStop = false;
        try {
            onStart();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (this.isRunInMainThread) {
            this.mUIHandler.post(this);
            return;
        }
        if (this.mHandlerThreadWrapper == null) {
            this.mHandlerThreadWrapper = new HandlerThreadWrapper("wx-analyzer-" + getClass().getSimpleName());
        } else if (!this.mHandlerThreadWrapper.isAlive()) {
            this.mHandlerThreadWrapper = new HandlerThreadWrapper("wx-analyzer-" + getClass().getSimpleName());
        } else {
            this.mHandlerThreadWrapper.getHandler().removeCallbacksAndMessages((Object) null);
        }
        this.mHandlerThreadWrapper.getHandler().post(this);
    }

    public void stop() {
        this.isStop = true;
        onStop();
        if (this.mHandlerThreadWrapper != null) {
            this.mHandlerThreadWrapper.quit();
            this.mHandlerThreadWrapper = null;
        }
        this.mUIHandler.removeCallbacksAndMessages((Object) null);
    }

    /* access modifiers changed from: protected */
    public void runOnUIThread(@NonNull final Runnable runnable) {
        if (this.mUIHandler != null) {
            this.mUIHandler.post(new Runnable() {
                public void run() {
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}
