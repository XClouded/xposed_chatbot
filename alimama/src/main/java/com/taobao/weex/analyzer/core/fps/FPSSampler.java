package com.taobao.weex.analyzer.core.fps;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.Choreographer;
import androidx.annotation.NonNull;

@TargetApi(16)
public class FPSSampler implements Choreographer.FrameCallback {
    private static final float DEVICE_REFRESH_RATE_IN_MS = 16.67f;
    private boolean bShouldStop = false;
    private Choreographer mChoreographer;
    private long mFirstFrameTime = -1;
    private long mLastFrameTime = -1;
    private int mNumFrameCallbacks = 0;

    public FPSSampler(@NonNull Choreographer choreographer) {
        this.mChoreographer = choreographer;
    }

    public void start() {
        this.bShouldStop = false;
        this.mChoreographer.postFrameCallback(this);
    }

    public void stop() {
        this.bShouldStop = true;
        this.mChoreographer.removeFrameCallback(this);
    }

    public void reset() {
        this.mFirstFrameTime = -1;
        this.mLastFrameTime = -1;
        this.mNumFrameCallbacks = 0;
    }

    public void doFrame(long j) {
        if (!this.bShouldStop) {
            if (this.mFirstFrameTime == -1) {
                this.mFirstFrameTime = j;
            } else {
                this.mNumFrameCallbacks++;
            }
            this.mLastFrameTime = j;
            this.mChoreographer.postFrameCallback(this);
        }
    }

    public int getExpectedNumFrames() {
        double d = (double) this.mLastFrameTime;
        double d2 = (double) this.mFirstFrameTime;
        Double.isNaN(d);
        Double.isNaN(d2);
        double d3 = (double) (((int) (d - d2)) / 1000000);
        Double.isNaN(d3);
        return (int) (d3 / 16.670000076293945d);
    }

    public int getNumFrames() {
        return this.mNumFrameCallbacks;
    }

    public double getFPS() {
        if (this.mLastFrameTime == this.mFirstFrameTime) {
            return 0.0d;
        }
        double numFrames = (double) getNumFrames();
        Double.isNaN(numFrames);
        double d = (double) (this.mLastFrameTime - this.mFirstFrameTime);
        Double.isNaN(d);
        return (numFrames * 1.0E9d) / d;
    }

    public static boolean isSupported() {
        return Build.VERSION.SDK_INT >= 16;
    }
}
