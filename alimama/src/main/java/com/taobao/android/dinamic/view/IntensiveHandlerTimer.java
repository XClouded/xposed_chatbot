package com.taobao.android.dinamic.view;

import android.os.Handler;
import com.taobao.android.dinamic.view.HandlerTimer;

public class IntensiveHandlerTimer extends HandlerTimer {
    public long originInterval;
    private long startTime;

    public IntensiveHandlerTimer(Runnable runnable) {
        super(runnable);
    }

    public IntensiveHandlerTimer(long j, Runnable runnable) {
        super(j, runnable);
        this.originInterval = j;
    }

    public IntensiveHandlerTimer(long j, Runnable runnable, Handler handler) {
        super(j, runnable, handler);
    }

    public void start() {
        this.startTime = System.currentTimeMillis();
        super.start();
    }

    public void start(int i) {
        this.startTime = System.currentTimeMillis();
        long j = (long) i;
        this.interval = j;
        super.start(j);
    }

    public void runOver() {
        this.interval = this.originInterval;
        this.startTime = System.currentTimeMillis();
    }

    public void pause() {
        if (this.status != HandlerTimer.TimerStatus.Paused) {
            this.interval -= System.currentTimeMillis() - this.startTime;
            super.pause();
        }
    }

    public void restart() {
        if (this.status != HandlerTimer.TimerStatus.Running) {
            this.startTime = System.currentTimeMillis();
            if (this.interval < 0) {
                this.interval = this.originInterval / 2;
            }
            super.restart();
        }
    }

    public void stop() {
        if (this.status != HandlerTimer.TimerStatus.Stopped) {
            super.stop();
        }
    }

    public void cancel() {
        super.cancel();
    }

    public long getInterval() {
        return this.interval;
    }

    public void updateInterval(long j) {
        this.interval = j;
    }
}
