package com.taobao.android.dinamicx;

import androidx.annotation.NonNull;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DXRenderPipelineBase extends DXBaseClass {
    public static final int DXPIPELINE_STAGE_DIFF = 6;
    public static final int DXPIPELINE_STAGE_END = 8;
    public static final int DXPIPELINE_STAGE_FLATTEN = 5;
    public static final int DXPIPELINE_STAGE_ID_LE = 0;
    public static final int DXPIPELINE_STAGE_LAYOUT = 4;
    public static final int DXPIPELINE_STAGE_LOAD = 1;
    public static final int DXPIPELINE_STAGE_MEASURE = 3;
    public static final int DXPIPELINE_STAGE_PARSE = 2;
    public static final int DXPIPELINE_STAGE_RENDER = 7;
    protected String identifier;
    protected DXRenderPipelineFlow pipelineFlow;
    protected int type;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DXPipelineStage {
    }

    public void setPipelineFlow(DXRenderPipelineFlow dXRenderPipelineFlow) {
        this.pipelineFlow = dXRenderPipelineFlow;
    }

    public DXRenderPipelineBase(@NonNull DXEngineContext dXEngineContext) {
        super(dXEngineContext);
    }

    protected DXRenderPipelineBase(@NonNull DXEngineContext dXEngineContext, int i, String str, DXRenderPipelineFlow dXRenderPipelineFlow) {
        super(dXEngineContext);
        this.type = i;
        this.identifier = str;
        this.pipelineFlow = dXRenderPipelineFlow;
    }

    protected DXRenderPipelineBase(@NonNull DXEngineContext dXEngineContext, int i, String str) {
        super(dXEngineContext);
        this.type = i;
        this.identifier = str;
    }

    public int getPipelineStageInWidget(DXWidgetNode dXWidgetNode, int i) {
        if (dXWidgetNode == null) {
            return 1;
        }
        if (dXWidgetNode.getStatInPrivateFlags(1024) || dXWidgetNode.getStatInPrivateFlags(1)) {
            return 2;
        }
        if (dXWidgetNode.getStatInPrivateFlags(4) || dXWidgetNode.getStatInPrivateFlags(16384)) {
            return 3;
        }
        if (dXWidgetNode.getStatInPrivateFlags(16)) {
            return 4;
        }
        if (dXWidgetNode.getStatInPrivateFlags(32)) {
            return 5;
        }
        return i;
    }
}
