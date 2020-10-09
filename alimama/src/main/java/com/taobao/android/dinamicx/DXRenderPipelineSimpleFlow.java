package com.taobao.android.dinamicx;

import android.view.View;
import com.taobao.android.dinamicx.DXRenderPipelineFlow;
import com.taobao.android.dinamicx.widget.DXWidgetNode;

public class DXRenderPipelineSimpleFlow extends DXRenderPipelineFlow {
    DXLayoutManager dxLayoutManager = new DXLayoutManager();
    DXSimpleRenderManager dxRenderManager = new DXSimpleRenderManager();
    DXWidgetNodeParser dxTemplateParser = new DXWidgetNodeParser();

    public DXRenderPipelineSimpleFlow() {
    }

    public DXRenderPipelineSimpleFlow(DXRenderPipelineFlow.RenderPipelineFlowListener renderPipelineFlowListener) {
        super(renderPipelineFlowListener);
    }

    /* access modifiers changed from: protected */
    public DXWidgetNode onParse() {
        if (this.widgetNode == null || this.runtimeContext == null) {
            return this.widgetNode;
        }
        if (this.pipelineMode == 1) {
            this.dxTemplateParser.parseInMeasure(this.widgetNode);
        } else {
            this.dxTemplateParser.parseWT(this.widgetNode);
        }
        return this.widgetNode;
    }

    /* access modifiers changed from: protected */
    public DXWidgetNode onMeasure() {
        if (this.widgetNode == null || this.runtimeContext == null) {
            return this.widgetNode;
        }
        this.dxLayoutManager.performMeasure(this.widgetNode, this.widthSpec, this.heightSpec, this.runtimeContext);
        return this.widgetNode;
    }

    /* access modifiers changed from: protected */
    public DXWidgetNode onLayout() {
        if (this.widgetNode == null || this.runtimeContext == null) {
            return this.widgetNode;
        }
        this.dxLayoutManager.performLayout(this.widgetNode, this.runtimeContext);
        return this.widgetNode;
    }

    /* access modifiers changed from: protected */
    public DXWidgetNode onFlatten() {
        if (this.widgetNode == null || this.runtimeContext == null) {
            return this.widgetNode;
        }
        boolean z = false;
        if (!(this.runtimeContext.getEngineContext() == null || this.runtimeContext.getEngineContext().getConfig() == null)) {
            z = this.runtimeContext.getEngineContext().getConfig().isDisabledFlatten();
        }
        return this.dxLayoutManager.performFlatten(this.widgetNode, this.runtimeContext, z);
    }

    /* access modifiers changed from: protected */
    public DXWidgetNode onDiff() {
        if (this.widgetNode == null || this.flattenWidgetNode == null || this.rootView == null) {
            return null;
        }
        return this.flattenWidgetNode;
    }

    /* access modifiers changed from: protected */
    public View onRender() {
        if (this.widgetNode == null || this.flattenWidgetNode == null || this.runtimeContext == null) {
            return null;
        }
        return this.dxRenderManager.renderWidget(this.widgetNode, this.flattenWidgetNode, this.rootView, this.runtimeContext);
    }
}
