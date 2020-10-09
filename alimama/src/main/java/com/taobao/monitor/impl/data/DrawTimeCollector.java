package com.taobao.monitor.impl.data;

import android.annotation.TargetApi;
import android.view.ViewTreeObserver;
import com.taobao.monitor.impl.common.APMContext;
import com.taobao.monitor.impl.trace.DispatcherManager;
import com.taobao.monitor.impl.trace.FPSDispatcher;
import com.taobao.monitor.impl.trace.IDispatcher;
import com.taobao.monitor.impl.util.TimeUtils;

@TargetApi(16)
public class DrawTimeCollector implements ViewTreeObserver.OnDrawListener {
    private static final String TAG = "DrawTimeCollector";
    private int fps = 0;
    private FPSDispatcher fpsDispatcher;
    private int jankCount = 0;
    private long lastDrawTime = TimeUtils.currentTimeMillis();
    private long moveTime;
    private long totalTime = 0;

    public DrawTimeCollector() {
        IDispatcher dispatcher = DispatcherManager.getDispatcher(APMContext.ACTIVITY_FPS_DISPATCHER);
        if (dispatcher instanceof FPSDispatcher) {
            this.fpsDispatcher = (FPSDispatcher) dispatcher;
        }
    }

    public void moveAction() {
        this.moveTime = TimeUtils.currentTimeMillis();
    }

    public void onDraw() {
        long currentTimeMillis = TimeUtils.currentTimeMillis();
        if (currentTimeMillis - this.moveTime <= 2000) {
            long j = currentTimeMillis - this.lastDrawTime;
            if (j < 200) {
                this.totalTime += j;
                this.fps++;
                if (j > 32) {
                    this.jankCount++;
                }
                if (this.totalTime > 1000) {
                    if (this.fps > 60) {
                        this.fps = 60;
                    }
                    if (!DispatcherManager.isEmpty(this.fpsDispatcher)) {
                        this.fpsDispatcher.fps(this.fps);
                        this.fpsDispatcher.jank(this.jankCount);
                    }
                    this.totalTime = 0;
                    this.fps = 0;
                    this.jankCount = 0;
                }
            }
            this.lastDrawTime = currentTimeMillis;
        }
    }
}
