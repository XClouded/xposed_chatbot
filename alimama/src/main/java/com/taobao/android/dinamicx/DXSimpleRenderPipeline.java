package com.taobao.android.dinamicx;

import android.view.View;
import androidx.annotation.NonNull;
import com.taobao.android.dinamicx.view.DXNativeFrameLayout;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import com.taobao.android.dinamicx.widget.utils.DXScreenTool;

public class DXSimpleRenderPipeline extends DXRenderPipelineBase {
    public DXSimpleRenderPipeline(@NonNull DXEngineContext dXEngineContext, int i, String str) {
        super(dXEngineContext, i, str);
        setPipelineFlow(new DXRenderPipelineSimpleFlow());
    }

    public ItemSize sizeWithWidgetNode(DXWidgetNode dXWidgetNode, DXRuntimeContext dXRuntimeContext, int i, int i2, int i3) {
        if (dXWidgetNode == null) {
            return new ItemSize(0, 0);
        }
        if (i == 0) {
            i = DXScreenTool.getDefaultWidthSpec();
        }
        if (i2 == 0) {
            i2 = DXScreenTool.getDefaultHeightSpec();
        }
        dXRuntimeContext.setPipelineIdentifier(this.identifier);
        int pipelineStageInWidget = getPipelineStageInWidget(dXWidgetNode, 2);
        this.pipelineFlow.widgetNode = dXWidgetNode;
        this.pipelineFlow.widthSpec = i;
        this.pipelineFlow.heightSpec = i2;
        this.pipelineFlow.runtimeContext = dXRuntimeContext;
        this.pipelineFlow.pipelineMode = 1;
        this.pipelineFlow.run(pipelineStageInWidget, 3);
        return new ItemSize(dXWidgetNode.getMeasuredWidth(), dXWidgetNode.getMeasuredHeight());
    }

    public View renderWidgetNode(DXWidgetNode dXWidgetNode, DXWidgetNode dXWidgetNode2, View view, DXRuntimeContext dXRuntimeContext, int i, int i2, int i3, int i4, int i5) {
        if (dXWidgetNode == null) {
            return null;
        }
        if (view == null) {
            view = new DXNativeFrameLayout(dXRuntimeContext.getContext());
        }
        dXRuntimeContext.setPipelineIdentifier(this.identifier);
        int pipelineStageInWidget = getPipelineStageInWidget(dXWidgetNode, i);
        this.pipelineFlow.widthSpec = i3;
        this.pipelineFlow.heightSpec = i4;
        this.pipelineFlow.rootView = view;
        this.pipelineFlow.runtimeContext = dXRuntimeContext;
        this.pipelineFlow.widgetNode = dXWidgetNode;
        this.pipelineFlow.flattenWidgetNode = dXWidgetNode2;
        this.pipelineFlow.pipelineMode = 0;
        this.pipelineFlow.run(pipelineStageInWidget, i2);
        return view;
    }
}
