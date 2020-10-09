package com.taobao.android.dinamicx;

import android.view.View;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class DXRenderPipelineFlow {
    public static final int DX_PIPELINE_MODE_RENDER = 0;
    public static final int DX_PIPELINE_MODE_SIZE = 1;
    protected DXWidgetNode flattenWidgetNode;
    private RenderPipelineFlowListener flowListener;
    protected int heightSpec;
    protected int pipelineMode;
    protected View rootView;
    protected DXRuntimeContext runtimeContext;
    protected int stage;
    protected int stageFrom;
    protected int stageTo;
    protected DXWidgetNode widgetNode;
    protected int widthSpec;

    @Retention(RetentionPolicy.SOURCE)
    public @interface DXPipelineMode {
    }

    public interface RenderPipelineFlowListener {
        void renderDidDiff();

        void renderDidFlatten();

        void renderDidLayout();

        void renderDidLoad();

        void renderDidMeasure();

        void renderDidParse();

        void renderDidRender();

        void renderWillDiff();

        void renderWillFlatten();

        void renderWillLayout();

        void renderWillLoad();

        void renderWillMeasure();

        void renderWillParse();

        void renderWillRender();
    }

    public DXRenderPipelineFlow() {
    }

    public DXRenderPipelineFlow(RenderPipelineFlowListener renderPipelineFlowListener) {
        this.flowListener = renderPipelineFlowListener;
    }

    public void run(int i, int i2) {
        if (i <= i2) {
            try {
                this.stageFrom = i;
                this.stageTo = i2;
                this.stage = i;
                while (this.stage <= i2) {
                    switch (this.stage) {
                        case 0:
                        case 1:
                            if (doLoad() != null) {
                                break;
                            } else {
                                return;
                            }
                        case 2:
                            if (doParse() != null) {
                                break;
                            } else {
                                return;
                            }
                        case 3:
                            if (doMeasure() != null) {
                                break;
                            } else {
                                return;
                            }
                        case 4:
                            if (doLayout() != null) {
                                break;
                            } else {
                                return;
                            }
                        case 5:
                            if (doFlatten() != null) {
                                break;
                            } else {
                                return;
                            }
                        case 6:
                            if (doDiff() != null) {
                                break;
                            } else {
                                return;
                            }
                        case 7:
                            if (doRender() != null) {
                                break;
                            } else {
                                return;
                            }
                    }
                    this.stage++;
                }
            } catch (Throwable th) {
                DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE, DXMonitorConstant.DX_SIMPLE_PIPELINE_CRASH, DXError.DX_SIMPLE_PIPELINE_CRASH);
                dXErrorInfo.reason = DXExceptionUtil.getStackTrace(th);
                if (this.runtimeContext != null && this.runtimeContext.getDxError() != null && this.runtimeContext.getDxError().dxErrorInfoList != null) {
                    this.runtimeContext.getDxError().dxErrorInfoList.add(dXErrorInfo);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public final DXWidgetNode doLoad() {
        if (this.flowListener != null) {
            this.flowListener.renderWillLoad();
        }
        this.widgetNode = onLoad();
        if (this.flowListener != null) {
            this.flowListener.renderDidLoad();
        }
        return this.widgetNode;
    }

    /* access modifiers changed from: protected */
    public final DXWidgetNode doParse() {
        if (this.flowListener != null) {
            this.flowListener.renderWillParse();
        }
        this.widgetNode = onParse();
        if (this.flowListener != null) {
            this.flowListener.renderDidParse();
        }
        return this.widgetNode;
    }

    /* access modifiers changed from: protected */
    public final DXWidgetNode doMeasure() {
        if (this.flowListener != null) {
            this.flowListener.renderWillMeasure();
        }
        this.widgetNode = onMeasure();
        if (this.flowListener != null) {
            this.flowListener.renderDidMeasure();
        }
        return this.widgetNode;
    }

    /* access modifiers changed from: protected */
    public final DXWidgetNode doLayout() {
        if (this.flowListener != null) {
            this.flowListener.renderWillLayout();
        }
        this.widgetNode = onLayout();
        if (this.flowListener != null) {
            this.flowListener.renderDidLayout();
        }
        return this.widgetNode;
    }

    /* access modifiers changed from: protected */
    public final DXWidgetNode doFlatten() {
        if (this.flowListener != null) {
            this.flowListener.renderWillFlatten();
        }
        this.flattenWidgetNode = onFlatten();
        if (this.flowListener != null) {
            this.flowListener.renderDidFlatten();
        }
        return this.flattenWidgetNode;
    }

    /* access modifiers changed from: protected */
    public final DXWidgetNode doDiff() {
        if (this.flowListener != null) {
            this.flowListener.renderWillDiff();
        }
        this.flattenWidgetNode = onDiff();
        if (this.flowListener != null) {
            this.flowListener.renderDidDiff();
        }
        return this.flattenWidgetNode;
    }

    /* access modifiers changed from: protected */
    public final View doRender() {
        if (this.flowListener != null) {
            this.flowListener.renderWillRender();
        }
        this.rootView = onRender();
        if (this.flowListener != null) {
            this.flowListener.renderDidRender();
        }
        return this.rootView;
    }

    /* access modifiers changed from: protected */
    public DXWidgetNode onLoad() {
        return this.widgetNode;
    }

    /* access modifiers changed from: protected */
    public DXWidgetNode onParse() {
        return this.widgetNode;
    }

    /* access modifiers changed from: protected */
    public DXWidgetNode onMeasure() {
        return this.widgetNode;
    }

    /* access modifiers changed from: protected */
    public DXWidgetNode onLayout() {
        return this.widgetNode;
    }

    /* access modifiers changed from: protected */
    public DXWidgetNode onFlatten() {
        return this.flattenWidgetNode;
    }

    /* access modifiers changed from: protected */
    public DXWidgetNode onDiff() {
        return this.flattenWidgetNode;
    }

    /* access modifiers changed from: protected */
    public View onRender() {
        return this.rootView;
    }
}
