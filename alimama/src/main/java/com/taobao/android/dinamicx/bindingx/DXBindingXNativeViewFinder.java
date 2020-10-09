package com.taobao.android.dinamicx.bindingx;

import android.text.TextUtils;
import android.view.View;
import com.alibaba.android.bindingx.plugin.android.NativeViewFinder;
import com.taobao.android.dinamicx.DXPublicConstant;
import com.taobao.android.dinamicx.DXRootView;
import com.taobao.android.dinamicx.widget.DXWidgetNode;

public class DXBindingXNativeViewFinder implements NativeViewFinder {
    public View findViewBy(View view, String str) {
        DXWidgetNode flattenWidget;
        DXWidgetNode referenceNode;
        if (view == null || TextUtils.isEmpty(str) || (flattenWidget = getFlattenWidget(view)) == null || flattenWidget.getReferenceNode() == null || (referenceNode = flattenWidget.getReferenceNode()) != view.getTag(DXPublicConstant.TAG_ANIMATION_EXPANDED_WIDGET_ON_VIEW)) {
            return null;
        }
        DXWidgetNode queryRootWidgetNode = referenceNode.queryRootWidgetNode();
        DXRootView rootView = (referenceNode == null || referenceNode.getDXRuntimeContext() == null) ? null : referenceNode.getDXRuntimeContext().getRootView();
        if (rootView == null || rootView.getExpandWidgetNode() != queryRootWidgetNode) {
            return null;
        }
        String splitElement = DXBindingXManager.splitElement(str);
        if (!"this".equalsIgnoreCase(splitElement)) {
            DXWidgetNode queryWTByUserId = referenceNode.queryWTByUserId(splitElement);
            if (queryWTByUserId == null || queryWTByUserId.getReferenceNode() == null) {
                queryWTByUserId = referenceNode.queryWidgetNodeByUserId(splitElement);
            }
            if (queryWTByUserId == null || queryWTByUserId.getReferenceNode() == null || queryWTByUserId.getReferenceNode().getWRView() == null) {
                return null;
            }
            return (View) queryWTByUserId.getReferenceNode().getWRView().get();
        } else if (referenceNode.getReferenceNode() == null || referenceNode.getReferenceNode().getWRView() == null) {
            return null;
        } else {
            return (View) referenceNode.getReferenceNode().getWRView().get();
        }
    }

    private DXWidgetNode getFlattenWidget(View view) {
        if (view == null) {
            return null;
        }
        if (view instanceof DXRootView) {
            return ((DXRootView) view).getFlattenWidgetNode();
        }
        Object tag = view.getTag(DXWidgetNode.TAG_WIDGET_NODE);
        if (!(tag instanceof DXWidgetNode)) {
            return null;
        }
        return (DXWidgetNode) tag;
    }

    private DXRootView getDXRootView(View view) {
        if (view == null) {
            return null;
        }
        if (view instanceof DXRootView) {
            return (DXRootView) view;
        }
        while (view.getParent() != null && !(view instanceof DXRootView)) {
            view = (View) view.getParent();
        }
        if (view instanceof DXRootView) {
            return (DXRootView) view;
        }
        return null;
    }
}
