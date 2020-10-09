package com.taobao.weex.analyzer.core.memory;

import android.content.Context;
import androidx.annotation.NonNull;
import com.taobao.weex.analyzer.core.TaskEntity;
import com.taobao.weex.analyzer.core.memory.PSSMemoryInfoSampler;

public class NativeMemoryTaskEntity implements TaskEntity<PSSMemoryInfoSampler.PssInfo> {
    private Context mContext;

    public void onTaskInit() {
    }

    public void onTaskStop() {
    }

    public NativeMemoryTaskEntity(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @NonNull
    public PSSMemoryInfoSampler.PssInfo onTaskRun() {
        if (this.mContext == null) {
            return new PSSMemoryInfoSampler.PssInfo();
        }
        return PSSMemoryInfoSampler.getAppPssInfo(this.mContext);
    }
}
