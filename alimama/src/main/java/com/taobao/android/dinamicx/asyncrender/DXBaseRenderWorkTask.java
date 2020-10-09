package com.taobao.android.dinamicx.asyncrender;

import android.os.Looper;
import com.taobao.android.dinamicx.DXEngineConfig;
import com.taobao.android.dinamicx.DXEngineContext;
import com.taobao.android.dinamicx.DXPipelineCacheManager;
import com.taobao.android.dinamicx.DXRenderOptions;
import com.taobao.android.dinamicx.DXRenderPipeline;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.DXTemplateManager;
import com.taobao.android.dinamicx.widget.event.DXControlEventCenter;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

public class DXBaseRenderWorkTask implements Runnable {
    protected static ThreadLocal<DXRenderPipeline> pipelineThreadLocal = new ThreadLocal<>();
    protected DXEngineConfig config;
    protected WeakReference<DXControlEventCenter> controlEventCenterWeakReference;
    protected DXPipelineCacheManager dxPipelineCacheManager;
    protected DXEngineContext engineContext;
    protected DXRenderOptions options;
    public DXRuntimeContext runtimeContext;
    protected long taskId;
    protected String taskName;
    protected WeakReference<DXTemplateManager> templateManagerWeakReference;

    public DXBaseRenderWorkTask(DXRuntimeContext dXRuntimeContext, DXRenderOptions dXRenderOptions, DXTemplateManager dXTemplateManager, DXPipelineCacheManager dXPipelineCacheManager, DXEngineContext dXEngineContext, DXControlEventCenter dXControlEventCenter) {
        this.runtimeContext = dXRuntimeContext;
        this.dxPipelineCacheManager = dXPipelineCacheManager;
        this.options = dXRenderOptions;
        this.config = dXEngineContext.getConfig();
        this.engineContext = dXEngineContext;
        if (dXControlEventCenter != null) {
            this.controlEventCenterWeakReference = new WeakReference<>(dXControlEventCenter);
        }
        if (dXTemplateManager != null) {
            this.templateManagerWeakReference = new WeakReference<>(dXTemplateManager);
        }
        this.taskId = System.nanoTime();
    }

    public DXBaseRenderWorkTask() {
    }

    public void run() {
        try {
            Field declaredField = Looper.class.getDeclaredField("sThreadLocal");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(Looper.getMainLooper());
            if (obj instanceof ThreadLocal) {
                ((ThreadLocal) obj).set(Looper.getMainLooper());
            }
        } catch (Throwable unused) {
        }
    }
}
