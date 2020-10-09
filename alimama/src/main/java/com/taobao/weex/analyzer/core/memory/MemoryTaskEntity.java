package com.taobao.weex.analyzer.core.memory;

import androidx.annotation.NonNull;
import com.taobao.weex.analyzer.core.TaskEntity;

public class MemoryTaskEntity implements TaskEntity<Double> {
    public void onTaskInit() {
    }

    public void onTaskStop() {
    }

    @NonNull
    public Double onTaskRun() {
        return Double.valueOf(MemorySampler.getMemoryUsage());
    }
}
