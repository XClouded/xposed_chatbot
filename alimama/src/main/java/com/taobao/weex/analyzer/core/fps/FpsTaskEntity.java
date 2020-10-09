package com.taobao.weex.analyzer.core.fps;

import android.annotation.TargetApi;
import android.view.Choreographer;
import androidx.annotation.NonNull;
import com.taobao.weex.analyzer.core.TaskEntity;

public class FpsTaskEntity implements TaskEntity<Double> {
    private FPSSampler mFpsChecker;

    @TargetApi(16)
    public void onTaskInit() {
        this.mFpsChecker = new FPSSampler(Choreographer.getInstance());
        this.mFpsChecker.reset();
        this.mFpsChecker.start();
    }

    @NonNull
    public Double onTaskRun() {
        if (this.mFpsChecker == null) {
            onTaskInit();
        }
        Double valueOf = Double.valueOf(this.mFpsChecker.getFPS());
        this.mFpsChecker.reset();
        return valueOf;
    }

    public void onTaskStop() {
        this.mFpsChecker.stop();
        this.mFpsChecker = null;
    }
}
