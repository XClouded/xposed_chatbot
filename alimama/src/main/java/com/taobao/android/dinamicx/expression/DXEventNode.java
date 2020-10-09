package com.taobao.android.dinamicx.expression;

import androidx.annotation.Nullable;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.DXRootView;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.DinamicXEngine;
import com.taobao.android.dinamicx.IDXEventHandler;
import com.taobao.android.dinamicx.expression.event.DXEvent;
import com.taobao.android.dinamicx.monitor.DXAppMonitor;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.widget.DXWidgetNode;

public class DXEventNode extends DXExprNode {
    DXRuntimeContext expandedWTRuntimeContext;
    IDXEventHandler handler;

    public byte getType() {
        return 6;
    }

    public Object evaluate(@Nullable DXEvent dXEvent, DXRuntimeContext dXRuntimeContext) {
        if (dXRuntimeContext != null) {
            try {
                if (dXRuntimeContext.getWidgetNode() != null) {
                    this.handler = dXRuntimeContext.getEventHandlerWithId(this.exprId);
                    if (this.handler == null) {
                        if (dXEvent != null && !dXEvent.isPrepareBind()) {
                            DXError dXError = new DXError(dXRuntimeContext.getBizType());
                            if (dXRuntimeContext.getDxTemplateItem() != null) {
                                dXError.dxTemplateItem = dXRuntimeContext.getDxTemplateItem();
                            }
                            DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_AST_NODE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_AST_NODE_EVENT_HANDLER, DXError.DX_ERROR_CODE_AST_EVENT_HANDLER_NOT_FOUND);
                            dXErrorInfo.reason = "找不到用户注册的eventHandle  hashcode 为: " + this.exprId;
                            dXError.dxErrorInfoList.add(dXErrorInfo);
                            DXAppMonitor.trackerError(dXError);
                        }
                        return null;
                    }
                    int size = this.children != null ? this.children.size() : 0;
                    Object[] objArr = new Object[size];
                    for (int i = 0; i < size; i++) {
                        objArr[i] = ((DXExprNode) this.children.get(i)).evaluate(dXEvent, dXRuntimeContext);
                    }
                    if (dXEvent == null || !dXEvent.isPrepareBind()) {
                        this.handler.handleEvent(dXEvent, objArr, dXRuntimeContext.getWidgetNode().getDXRuntimeContext());
                    } else {
                        this.handler.prepareBindEventWithArgs(objArr, dXRuntimeContext.getWidgetNode().getDXRuntimeContext());
                    }
                    return null;
                }
            } catch (Exception e) {
                if (DinamicXEngine.isDebug()) {
                    e.printStackTrace();
                }
                DXError dXError2 = new DXError(dXRuntimeContext.getBizType());
                DXError.DXErrorInfo dXErrorInfo2 = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_AST_NODE, DXMonitorConstant.DX_MONITOR_SERVICE_ID_AST_NODE_EVENT_HANDLER, DXError.DX_ERROR_CODE_AST_EVENT_EXECUTE_EXCEPTION);
                if (dXEvent != null) {
                    dXErrorInfo2.reason = "eventId: " + dXEvent.getEventId() + " isPrepareBind: " + dXEvent.isPrepareBind();
                }
                dXError2.dxErrorInfoList.add(dXErrorInfo2);
                DXAppMonitor.trackerError(dXError2);
            }
        }
        return null;
    }

    private DXRuntimeContext getExpandedRuntimeContext(DXRuntimeContext dXRuntimeContext) {
        DXWidgetNode expandWidgetNode;
        DXWidgetNode queryWidgetNodeByAutoId;
        DXRootView rootView = dXRuntimeContext.getRootView();
        if (rootView == null || (expandWidgetNode = rootView.getExpandWidgetNode()) == null || (queryWidgetNodeByAutoId = expandWidgetNode.queryWidgetNodeByAutoId(dXRuntimeContext.getWidgetNode().getAutoId())) == null) {
            return null;
        }
        return queryWidgetNodeByAutoId.getDXRuntimeContext();
    }
}
