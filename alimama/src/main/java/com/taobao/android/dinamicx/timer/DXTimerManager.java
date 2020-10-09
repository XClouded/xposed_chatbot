package com.taobao.android.dinamicx.timer;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

public class DXTimerManager {
    private static final int MSG = 1;
    /* access modifiers changed from: private */
    public boolean cancelled = true;
    /* access modifiers changed from: private */
    public long finalTickInterval;
    private DXHandlerTimer handler;
    private ArrayList<DXTimerListenerWrapper> listenerWrappers;

    public final void cancel() {
        this.cancelled = true;
        this.handler.removeMessages(1);
    }

    public DXTimerManager(long j) {
        this.finalTickInterval = j;
        this.handler = new DXHandlerTimer(this);
    }

    public final void onTick() {
        if (this.listenerWrappers == null || this.listenerWrappers.size() == 0) {
            cancel();
            return;
        }
        long elapsedRealtime = SystemClock.elapsedRealtime();
        Iterator<DXTimerListenerWrapper> it = this.listenerWrappers.iterator();
        while (it.hasNext()) {
            DXTimerListenerWrapper next = it.next();
            int i = (int) ((elapsedRealtime - next.startTime) / next.interval);
            if (i >= next.repeatCount + 1) {
                next.timerListener.onTimerCallback();
                next.repeatCount = i;
            }
        }
    }

    public void registerListener(DXTimerListener dXTimerListener, long j) {
        if (dXTimerListener != null && j > 0) {
            if (this.listenerWrappers == null) {
                this.listenerWrappers = new ArrayList<>(5);
            }
            Iterator<DXTimerListenerWrapper> it = this.listenerWrappers.iterator();
            while (it.hasNext()) {
                if (it.next().timerListener == dXTimerListener) {
                    return;
                }
            }
            DXTimerListenerWrapper dXTimerListenerWrapper = new DXTimerListenerWrapper();
            dXTimerListenerWrapper.timerListener = dXTimerListener;
            if (j <= this.finalTickInterval) {
                j = this.finalTickInterval;
            }
            dXTimerListenerWrapper.interval = j;
            dXTimerListenerWrapper.startTime = SystemClock.elapsedRealtime();
            this.listenerWrappers.add(dXTimerListenerWrapper);
            start();
        }
    }

    public void unregisterListener(DXTimerListener dXTimerListener) {
        if (dXTimerListener != null) {
            if (this.listenerWrappers == null) {
                cancel();
                return;
            }
            Iterator<DXTimerListenerWrapper> it = this.listenerWrappers.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                DXTimerListenerWrapper next = it.next();
                if (next.timerListener == dXTimerListener) {
                    this.listenerWrappers.remove(next);
                    break;
                }
            }
            if (this.listenerWrappers.size() == 0) {
                cancel();
            }
        }
    }

    public final void start() {
        if (this.cancelled) {
            this.cancelled = false;
            this.handler.setStartTimer(SystemClock.elapsedRealtime());
            this.handler.sendMessage(this.handler.obtainMessage(1));
        }
    }

    public final void onDestroy() {
        if (this.listenerWrappers != null) {
            this.listenerWrappers.clear();
        }
        cancel();
    }

    public static class DXHandlerTimer extends Handler {
        private WeakReference<DXTimerManager> managerWeakReference;
        private long startTimer;

        public void setStartTimer(long j) {
            this.startTimer = j;
        }

        DXHandlerTimer(DXTimerManager dXTimerManager) {
            super(Looper.getMainLooper());
            this.managerWeakReference = new WeakReference<>(dXTimerManager);
        }

        public void handleMessage(Message message) {
            DXTimerManager dXTimerManager = (DXTimerManager) this.managerWeakReference.get();
            if (dXTimerManager != null && !dXTimerManager.cancelled) {
                dXTimerManager.onTick();
                long elapsedRealtime = (SystemClock.elapsedRealtime() - this.startTimer) % dXTimerManager.finalTickInterval;
                sendMessageDelayed(obtainMessage(1), dXTimerManager.finalTickInterval - elapsedRealtime);
            }
        }
    }
}
