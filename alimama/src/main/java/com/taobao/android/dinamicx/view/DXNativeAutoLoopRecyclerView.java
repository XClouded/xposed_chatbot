package com.taobao.android.dinamicx.view;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.PagerSnapHelper;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.timer.DXTimerListener;
import java.lang.ref.WeakReference;

public class DXNativeAutoLoopRecyclerView extends DXNativeRecyclerView {
    private boolean autoPlay;
    private int currentIndex;
    private DinamicXEngine dinamicXEngine;
    private int interval;
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("android.intent.action.SCREEN_OFF".equals(action)) {
                DXNativeAutoLoopRecyclerView.this.stopTimer();
            } else if (!"android.intent.action.USER_PRESENT".equals(action)) {
            } else {
                if (DXNativeAutoLoopRecyclerView.this.isShown()) {
                    DXNativeAutoLoopRecyclerView.this.startTimer();
                } else {
                    DXNativeAutoLoopRecyclerView.this.stopTimer();
                }
            }
        }
    };
    private boolean manualSwitchEnabled = true;
    private boolean needProcessViewLifeCycle = true;
    private OnPageChangeListener onPageChangeListener;
    private LooperRunnable runnable;

    public interface OnPageChangeListener {
        void onPageSelected(int i);
    }

    public boolean isSlider() {
        return true;
    }

    public void setDinamicXEngine(DinamicXEngine dinamicXEngine2) {
        this.dinamicXEngine = dinamicXEngine2;
    }

    public DXNativeAutoLoopRecyclerView(@NonNull Context context) {
        super(context);
        new PagerSnapHelper().attachToRecyclerView(this);
    }

    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (!this.autoPlay) {
            return super.dispatchTouchEvent(motionEvent);
        }
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        if (this.manualSwitchEnabled) {
            int action = motionEvent.getAction();
            if (action != 3) {
                switch (action) {
                    case 0:
                        stopTimer();
                        break;
                    case 1:
                        startTimer();
                        break;
                }
            } else {
                startTimer();
            }
        }
        return dispatchTouchEvent;
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (this.manualSwitchEnabled) {
            return super.onTouchEvent(motionEvent);
        }
        return true;
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.manualSwitchEnabled && super.onInterceptTouchEvent(motionEvent);
    }

    public void stopTimer() {
        if (this.autoPlay && this.dinamicXEngine != null) {
            this.dinamicXEngine.unregisterTimerListener(this.runnable);
        }
    }

    public void startTimer() {
        if (this.autoPlay) {
            if (this.runnable == null) {
                this.runnable = new LooperRunnable(this);
            }
            if (this.dinamicXEngine != null) {
                this.dinamicXEngine.registerTimerListener(this.runnable, (long) this.interval);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.needProcessViewLifeCycle && this.autoPlay) {
            startTimer();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            intentFilter.addAction("android.intent.action.USER_PRESENT");
            getContext().registerReceiver(this.mReceiver, intentFilter);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.needProcessViewLifeCycle && this.autoPlay) {
            stopTimer();
            getContext().unregisterReceiver(this.mReceiver);
        }
    }

    /* access modifiers changed from: protected */
    public void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (this.needProcessViewLifeCycle && this.autoPlay) {
            if (i == 0) {
                startTimer();
            } else {
                stopTimer();
            }
        }
    }

    public int getInterval() {
        return this.interval;
    }

    public void setInterval(int i) {
        this.interval = i;
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public void setCurrentIndex(int i) {
        this.currentIndex = i;
    }

    public boolean isAutoPlay() {
        return this.autoPlay;
    }

    public void setAutoPlay(boolean z) {
        this.autoPlay = z;
    }

    public void setManualSwitchEnabled(boolean z) {
        this.manualSwitchEnabled = z;
    }

    public int increaseCurrentIndex() {
        int i = this.currentIndex + 1;
        this.currentIndex = i;
        return i;
    }

    public OnPageChangeListener getOnPageChangeListener() {
        return this.onPageChangeListener;
    }

    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener2) {
        this.onPageChangeListener = onPageChangeListener2;
    }

    public void setNeedProcessViewLifeCycle(boolean z) {
        this.needProcessViewLifeCycle = z;
    }

    public static class LooperRunnable implements DXTimerListener {
        private WeakReference<DXNativeAutoLoopRecyclerView> recyclerViewWeakReference;

        public LooperRunnable(DXNativeAutoLoopRecyclerView dXNativeAutoLoopRecyclerView) {
            this.recyclerViewWeakReference = new WeakReference<>(dXNativeAutoLoopRecyclerView);
        }

        public void onTimerCallback() {
            DXNativeAutoLoopRecyclerView dXNativeAutoLoopRecyclerView = (DXNativeAutoLoopRecyclerView) this.recyclerViewWeakReference.get();
            if (dXNativeAutoLoopRecyclerView != null) {
                dXNativeAutoLoopRecyclerView.smoothScrollToPosition(dXNativeAutoLoopRecyclerView.increaseCurrentIndex());
                OnPageChangeListener onPageChangeListener = dXNativeAutoLoopRecyclerView.getOnPageChangeListener();
                if (onPageChangeListener != null) {
                    onPageChangeListener.onPageSelected(dXNativeAutoLoopRecyclerView.getCurrentIndex());
                }
            }
        }
    }
}
