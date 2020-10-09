package com.taobao.android.dinamicx;

import android.text.TextUtils;
import android.view.ViewGroup;
import com.taobao.android.dinamicx.asyncrender.DXBaseRenderWorkTask;
import com.taobao.android.dinamicx.asyncrender.DXViewPoolManager;
import com.taobao.android.dinamicx.asyncrender.ViewContext;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.monitor.DXAppMonitor;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.widget.event.DXControlEventCenter;
import java.lang.ref.WeakReference;

public class DXPreRenderWorkTask extends DXBaseRenderWorkTask {
    public static final String TAG = "DXPreRenderWorkTask";

    public DXPreRenderWorkTask(DXRuntimeContext dXRuntimeContext, DXRenderOptions dXRenderOptions, DXTemplateManager dXTemplateManager, DXPipelineCacheManager dXPipelineCacheManager, DXEngineContext dXEngineContext, DXControlEventCenter dXControlEventCenter) {
        super(dXRuntimeContext, dXRenderOptions, dXTemplateManager, dXPipelineCacheManager, dXEngineContext, dXControlEventCenter);
    }

    public void run() {
        try {
            super.run();
            DXRenderPipeline dXRenderPipeline = (DXRenderPipeline) pipelineThreadLocal.get();
            if (dXRenderPipeline == null || this.config.engineId != dXRenderPipeline.getConfig().engineId) {
                DXRenderPipeline dXRenderPipeline2 = new DXRenderPipeline(this.engineContext, new DXTemplateManager(this.engineContext, DinamicXEngine.getApplicationContext()));
                pipelineThreadLocal.set(dXRenderPipeline2);
                dXRenderPipeline = dXRenderPipeline2;
            }
            this.runtimeContext.setDxRenderPipeline(new WeakReference(dXRenderPipeline));
            this.runtimeContext.contextWeakReference = new WeakReference<>(new ViewContext(this.runtimeContext.getContext().getApplicationContext()));
            DXRootView dXRootView = new DXRootView(this.runtimeContext.getContext());
            dXRootView.setLayoutParams(new ViewGroup.LayoutParams(0, 0));
            dXRootView.dxTemplateItem = this.runtimeContext.getDxTemplateItem();
            this.runtimeContext.rootViewWeakReference = new WeakReference<>(dXRootView);
            DXResult<DXRootView> renderInRootView = dXRenderPipeline.renderInRootView(dXRootView, this.runtimeContext, -1, this.options);
            if (renderInRootView != null && renderInRootView.result != null) {
                DXViewPoolManager.getInstance().cacheV3View((DXRootView) renderInRootView.result, this.runtimeContext.getDxTemplateItem(), this.config.bizType);
            }
        } catch (Throwable th) {
            if (this.runtimeContext != null && !TextUtils.isEmpty(this.runtimeContext.bizType)) {
                DXAppMonitor.trackerError(this.runtimeContext.bizType, this.runtimeContext.getDxTemplateItem(), DXMonitorConstant.DX_MONITOR_ASYNC_RENDER, DXMonitorConstant.PRE_RENDER_3_0_CRASH, DXError.V3_PRE_RENDER_CRASH, DXExceptionUtil.getStackTrace(th));
            }
            DXExceptionUtil.printStack(th);
        }
    }
}
