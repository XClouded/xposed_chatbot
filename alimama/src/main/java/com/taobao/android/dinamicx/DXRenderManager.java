package com.taobao.android.dinamicx;

import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import com.taobao.android.dinamicx.DXError;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.monitor.DXAppMonitor;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.render.diff.DXAbsDiff;
import com.taobao.android.dinamicx.render.diff.DXPipelineDiff;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

public class DXRenderManager {
    protected DXAbsDiff absDiff = new DXPipelineDiff();

    public View renderWidget(DXWidgetNode dXWidgetNode, DXWidgetNode dXWidgetNode2, View view, DXRuntimeContext dXRuntimeContext) {
        if (dXWidgetNode == null || dXWidgetNode2 == null || view == null) {
            return null;
        }
        try {
            DXWidgetNode dXWidgetNode3 = (DXWidgetNode) view.getTag(DXWidgetNode.TAG_WIDGET_NODE);
            long nanoTime = System.nanoTime();
            this.absDiff.diff(dXWidgetNode2, dXWidgetNode3);
            trackerPerform(dXRuntimeContext, DXMonitorConstant.DX_MONITOR_SERVICE_ID_DETAIL_RENDER_DIFF, System.nanoTime() - nanoTime, (Map<String, String>) null);
            dXWidgetNode2.setWRView(new WeakReference(view));
            long nanoTime2 = System.nanoTime();
            renderFlatten(dXRuntimeContext, dXWidgetNode2, dXWidgetNode, (View) null, 0);
            if (dXWidgetNode2.getAccessibility() == 3) {
                if (Build.VERSION.SDK_INT >= 16) {
                    view.setImportantForAccessibility(1);
                }
            } else if (Build.VERSION.SDK_INT >= 16) {
                view.setImportantForAccessibility(2);
            }
            view.setTag(DXWidgetNode.TAG_WIDGET_NODE, dXWidgetNode2);
            if (!(dXWidgetNode3 == null || dXWidgetNode3.getParentWidget() == null)) {
                dXWidgetNode3.getParentWidget().replaceChild(dXWidgetNode2, dXWidgetNode3);
            }
            trackerPerform(dXRuntimeContext, DXMonitorConstant.DX_MONITOR_SERVICE_ID_DETAIL_RENDER_RECURSION_RENDER_WT, System.nanoTime() - nanoTime2, (Map<String, String>) null);
        } catch (Exception e) {
            DXExceptionUtil.printStack(e);
            if (!(dXRuntimeContext == null || dXRuntimeContext.getDxError() == null || dXRuntimeContext.getDxError().dxErrorInfoList == null)) {
                DXError.DXErrorInfo dXErrorInfo = new DXError.DXErrorInfo(DXMonitorConstant.DX_MONITOR_PIPELINE_DETAIL, DXMonitorConstant.DX_MONITOR_SERVICE_ID_RENDER_DETAIL, 90001);
                dXErrorInfo.reason = "DXLayoutManager#renderWidget " + DXExceptionUtil.getStackTrace(e);
                dXRuntimeContext.getDxError().dxErrorInfoList.add(dXErrorInfo);
            }
        }
        return view;
    }

    private void renderFlatten(DXRuntimeContext dXRuntimeContext, DXWidgetNode dXWidgetNode, DXWidgetNode dXWidgetNode2, View view, int i) {
        try {
            View realView = getRealView(dXWidgetNode);
            if (realView != null) {
                if (view == null) {
                    realView.setTag(DXPublicConstant.TAG_EXPANDED_WIDGET_ON_VIEW, dXWidgetNode2);
                }
                realView.setTag(DXWidgetNode.TAG_WIDGET_NODE, dXWidgetNode);
                renderDetail(dXRuntimeContext, dXWidgetNode, dXWidgetNode2, realView);
                return;
            }
            View createView = dXWidgetNode.createView(dXRuntimeContext.getContext());
            if (view == null) {
                createView.setTag(DXPublicConstant.TAG_EXPANDED_WIDGET_ON_VIEW, dXWidgetNode2);
            }
            renderDetail(dXRuntimeContext, dXWidgetNode, dXWidgetNode2, createView);
            if (view != null && (view instanceof ViewGroup)) {
                ((ViewGroup) view).addView(createView, i);
            }
        } catch (Throwable th) {
            if (dXRuntimeContext != null) {
                DXAppMonitor.trackerError(dXRuntimeContext.bizType, dXRuntimeContext.getDxTemplateItem(), DXMonitorConstant.DX_MONITOR_RENDER, DXMonitorConstant.RENDER_FLATTEN_CRASH, DXError.DXERROR_RENDER_FLATTEN, DXExceptionUtil.getStackTrace(th));
            }
            DXExceptionUtil.printStack(th);
        }
    }

    private void renderDetail(DXRuntimeContext dXRuntimeContext, DXWidgetNode dXWidgetNode, DXWidgetNode dXWidgetNode2, View view) {
        dXWidgetNode.setRealViewLayoutParam(view);
        dXWidgetNode.bindEvent(dXRuntimeContext.getContext());
        dXWidgetNode.setNeedRender(dXRuntimeContext.getContext());
        List<DXWidgetNode> children = dXWidgetNode.getChildren();
        if (children != null) {
            for (int i = 0; i < dXWidgetNode.getChildrenCount(); i++) {
                renderFlatten(dXRuntimeContext, children.get(i), dXWidgetNode2, view, i);
            }
        }
    }

    private View getRealView(DXWidgetNode dXWidgetNode) {
        WeakReference<View> wRView;
        if (dXWidgetNode == null || (wRView = dXWidgetNode.getWRView()) == null) {
            return null;
        }
        return (View) wRView.get();
    }

    private void trackerPerform(DXRuntimeContext dXRuntimeContext, String str, long j, Map<String, String> map) {
        DXTemplateItem dxTemplateItem;
        try {
            String str2 = dXRuntimeContext.config.bizType;
            if (dXRuntimeContext == null) {
                dxTemplateItem = null;
            } else {
                dxTemplateItem = dXRuntimeContext.getDxTemplateItem();
            }
            DXAppMonitor.trackerPerform(3, str2, DXMonitorConstant.DX_MONITOR_SERVICE_ID_RENDER_DETAIL, str, dxTemplateItem, map, (double) j, true);
        } catch (Exception e) {
            DXExceptionUtil.printStack(e);
        }
    }
}
