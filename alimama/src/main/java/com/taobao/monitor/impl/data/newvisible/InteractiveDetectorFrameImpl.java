package com.taobao.monitor.impl.data.newvisible;

import android.view.Choreographer;
import com.taobao.monitor.impl.data.IInteractiveDetector;
import com.taobao.monitor.impl.logger.Logger;
import com.taobao.monitor.impl.util.TimeUtils;
import com.taobao.monitor.procedure.IProcedure;
import java.util.ArrayList;
import java.util.List;

public class InteractiveDetectorFrameImpl implements IInteractiveDetector, Choreographer.FrameCallback {
    private static final long CONTINUOUS_OBSERVER_DURATION = 5000;
    private static final String TAG = "InteractiveDetectorFrameImpl";
    private IInteractiveDetector.IDetectorCallback callback;
    private long lastDetectedTime = TimeUtils.currentTimeMillis();
    private long lastValidTime = TimeUtils.currentTimeMillis();
    private List<Long> lastValidTimes = new ArrayList(32);
    private final long peopleFeelingTime;
    private final IProcedure procedure;
    private volatile boolean stopped = false;
    private long visibleTime = Long.MAX_VALUE;

    public InteractiveDetectorFrameImpl(long j, IProcedure iProcedure) {
        this.peopleFeelingTime = j;
        this.procedure = iProcedure;
    }

    public void setVisibleTime(long j) {
        if (this.visibleTime == Long.MAX_VALUE) {
            this.visibleTime = j;
        }
    }

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
        long j = currentTimeMillis - this.lastDetectedTime;
        if (j > this.peopleFeelingTime) {
            this.lastValidTime = TimeUtils.currentTimeMillis();
            this.procedure.addStatistic("time" + this.lastValidTime, Long.valueOf(j));
            Logger.d(TAG, "currentCostTime:" + j);
        }
        long j2 = currentTimeMillis - this.lastValidTime;
        if (j2 > CONTINUOUS_OBSERVER_DURATION) {
            this.lastValidTimes.add(Long.valueOf(this.lastValidTime));
            this.lastValidTime += Math.max(j2 - CONTINUOUS_OBSERVER_DURATION, 16);
        }
        if (this.visibleTime == Long.MAX_VALUE || this.lastValidTimes.size() == 0 || this.lastValidTimes.get(this.lastValidTimes.size() - 1).longValue() <= this.visibleTime) {
            Choreographer.getInstance().postFrameCallback(this);
            this.lastDetectedTime = currentTimeMillis;
            return;
        }
        if (this.callback != null) {
            this.callback.completed(getUsableTime());
        }
        stop();
    }

    public void doFrame(long j) {
        if (!this.stopped) {
            doFPSDetect();
        }
    }

    public long getUsableTime() {
        for (Long next : this.lastValidTimes) {
            if (next.longValue() > this.visibleTime) {
                return next.longValue();
            }
        }
        return -1;
    }
}
