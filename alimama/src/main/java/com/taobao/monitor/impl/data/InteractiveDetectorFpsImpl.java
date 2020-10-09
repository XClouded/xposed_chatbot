package com.taobao.monitor.impl.data;

import android.annotation.TargetApi;
import android.view.Choreographer;
import com.taobao.monitor.impl.data.IInteractiveDetector;
import com.taobao.monitor.impl.util.TimeUtils;

@TargetApi(16)
public class InteractiveDetectorFpsImpl implements IInteractiveDetector, Choreographer.FrameCallback {
    private static final int FPS_INTERVAL = 17;
    private static final int INTERACTIVE_FPS = 50;
    private static final long ONE_SECOND = 1000;
    private static final long TOTAL_INTERACTIVE_DURATION = 5000;
    private IInteractiveDetector.IDetectorCallback callback;
    private int fpsCount = 0;
    private long interactiveDuration = 0;
    private long lastDetectorTime = TimeUtils.currentTimeMillis();
    private long oneSecFpsDuration = 0;
    private volatile boolean stopped = false;

    public void execute() {
        Choreographer.getInstance().postFrameCallback(this);
    }

    public void stop() {
        this.stopped = true;
    }

    public void setCallback(IInteractiveDetector.IDetectorCallback iDetectorCallback) {
        this.callback = iDetectorCallback;
    }

    private void doFPSDetect() {
        long currentTimeMillis = TimeUtils.currentTimeMillis();
        long j = currentTimeMillis - this.lastDetectorTime;
        this.interactiveDuration += j;
        this.fpsCount++;
        this.oneSecFpsDuration += j;
        if (ONE_SECOND / j < 50 && ((long) this.fpsCount) + ((ONE_SECOND - this.oneSecFpsDuration) / 17) <= 50) {
            this.interactiveDuration = 0;
            this.fpsCount = 0;
            this.oneSecFpsDuration = 0;
        } else if (this.fpsCount >= 17) {
            this.fpsCount = 0;
            this.oneSecFpsDuration = 0;
        }
        if (this.interactiveDuration < TOTAL_INTERACTIVE_DURATION) {
            Choreographer.getInstance().postFrameCallback(this);
            this.lastDetectorTime = currentTimeMillis;
        } else if (this.callback != null) {
            this.callback.completed(currentTimeMillis - this.interactiveDuration);
        }
    }

    public void doFrame(long j) {
        if (!this.stopped) {
            doFPSDetect();
        }
    }
}
