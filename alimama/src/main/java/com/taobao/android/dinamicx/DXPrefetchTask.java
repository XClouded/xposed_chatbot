package com.taobao.android.dinamicx;

import android.view.View;
import com.taobao.android.dinamicx.asyncrender.DXBaseRenderWorkTask;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.event.DXControlEventCenter;
import java.lang.ref.WeakReference;

public class DXPrefetchTask extends DXBaseRenderWorkTask {
    public boolean isDone;

    public DXPrefetchTask(DXRuntimeContext dXRuntimeContext, DXRenderOptions dXRenderOptions, DXTemplateManager dXTemplateManager, DXPipelineCacheManager dXPipelineCacheManager, DXEngineContext dXEngineContext, DXControlEventCenter dXControlEventCenter) {
        super(dXRuntimeContext, dXRenderOptions, dXTemplateManager, dXPipelineCacheManager, dXEngineContext, dXControlEventCenter);
    }

    public void run() {
        DXRenderPipeline dXRenderPipeline;
        super.run();
        try {
            DXRenderPipeline dXRenderPipeline2 = (DXRenderPipeline) pipelineThreadLocal.get();
            if (dXRenderPipeline2 != null) {
                if (this.config.engineId == dXRenderPipeline2.getConfig().engineId) {
                    dXRenderPipeline = dXRenderPipeline2;
                    this.runtimeContext.setDxRenderPipeline(new WeakReference(dXRenderPipeline));
                    dXRenderPipeline.renderWidget((DXWidgetNode) null, (DXWidgetNode) null, (View) null, this.runtimeContext, this.options);
                    this.isDone = true;
                }
            }
            DXRenderPipeline dXRenderPipeline3 = new DXRenderPipeline(this.engineContext, new DXTemplateManager(this.engineContext, DinamicXEngine.getApplicationContext()));
            pipelineThreadLocal.set(dXRenderPipeline3);
            dXRenderPipeline = dXRenderPipeline3;
            this.runtimeContext.setDxRenderPipeline(new WeakReference(dXRenderPipeline));
            dXRenderPipeline.renderWidget((DXWidgetNode) null, (DXWidgetNode) null, (View) null, this.runtimeContext, this.options);
            this.isDone = true;
        } catch (Throwable th) {
            DXExceptionUtil.printStack(th);
        }
    }
}
