package com.taobao.monitor.impl.data;

import android.annotation.TargetApi;
import android.util.Log;
import android.view.Choreographer;
import com.taobao.monitor.impl.data.IInteractiveDetector;
import com.taobao.monitor.impl.util.TimeUtils;

@TargetApi(16)
public class InteractiveDetectorVarianceImpl implements IInteractiveDetector, Choreographer.FrameCallback {
    private static final int ARRAY_LENGTH = 300;
    private static final int STANDARD_DEVIATION = 4;
    private static final String TAG = "InteractiveDetectorVarianceImpl";
    private static final long TOTAL_INTERACTIVE_DURATION = 5000;
    private IInteractiveDetector.IDetectorCallback callback;
    private final int[] historyTimes = new int[300];
    private int index = 0;
    private long interactiveDuration = 0;
    private long lastDetectorTime = TimeUtils.currentTimeMillis();
    private volatile boolean stopped = false;

    public void execute() {
        doFPSDetect();
    }

    public void stop() {
        this.stopped = true;
    }

    public void setCallback(IInteractiveDetector.IDetectorCallback iDetectorCallback) {
        this.callback = iDetectorCallback;
    }

    private void doFPSDetect() {
        long currentTimeMillis = TimeUtils.currentTimeMillis();
        int i = (int) (currentTimeMillis - this.lastDetectorTime);
        this.interactiveDuration += (long) (i - this.historyTimes[this.index % 300]);
        int[] iArr = this.historyTimes;
        int i2 = this.index;
        this.index = i2 + 1;
        iArr[i2 % 300] = i;
        if (this.index >= 300) {
            int calStandardDeviation = calStandardDeviation(this.historyTimes);
            Log.i(TAG, "var:" + calStandardDeviation);
            if (calStandardDeviation <= 4) {
                if (this.callback != null) {
                    this.callback.completed(currentTimeMillis - this.interactiveDuration);
                    return;
                }
                return;
            }
        }
        Choreographer.getInstance().postFrameCallback(this);
        this.lastDetectorTime = currentTimeMillis;
    }

    public void doFrame(long j) {
        if (!this.stopped) {
            doFPSDetect();
        }
    }

    private double calVariance(int[] iArr) {
        long j = 0;
        for (int i : iArr) {
            j += (long) i;
        }
        double d = (double) (j / ((long) r0));
        double d2 = 0.0d;
        for (int i2 = 0; i2 < r0; i2++) {
            double d3 = (double) iArr[i2];
            Double.isNaN(d3);
            Double.isNaN(d);
            double d4 = (double) iArr[i2];
            Double.isNaN(d4);
            Double.isNaN(d);
            d2 += (d3 - d) * (d4 - d);
        }
        double d5 = (double) r0;
        Double.isNaN(d5);
        return d2 / d5;
    }

    private int calStandardDeviation(int[] iArr) {
        return (int) Math.sqrt(calVariance(iArr));
    }
}
