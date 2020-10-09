package com.taobao.android.dinamicx.widget;

import com.taobao.android.dinamicx.DXRuntimeContext;
import java.util.ArrayList;
import java.util.List;

public class DXLayoutUtil {
    public static int generateCopyWidgetNodeAutoId(DXWidgetNode dXWidgetNode) {
        if (dXWidgetNode == null) {
            return 0;
        }
        return (dXWidgetNode.getAutoId() << 16) + dXWidgetNode.getLastAutoId();
    }

    public static DXWidgetNode deepCopyChildNode(DXWidgetNode dXWidgetNode, DXRuntimeContext dXRuntimeContext) {
        DXWidgetNode dXWidgetNode2 = (DXWidgetNode) dXWidgetNode.shallowClone(dXRuntimeContext, true);
        dXWidgetNode2.setSourceWidget(dXWidgetNode.getSourceWidget());
        bindCopiedWidgetAutoId(dXWidgetNode2);
        if (dXWidgetNode.getChildren() != null) {
            dXWidgetNode2.children = new ArrayList();
            for (int i = 0; i < dXWidgetNode.getChildrenCount(); i++) {
                dXWidgetNode2.addChild(deepCopyChildNode(dXWidgetNode.getChildAt(i), dXRuntimeContext));
            }
        }
        return dXWidgetNode2;
    }

    public static void bindCopiedWidgetAutoId(DXWidgetNode dXWidgetNode) {
        DXWidgetNode sourceWidget = dXWidgetNode.getSourceWidget();
        if (sourceWidget != null) {
            dXWidgetNode.setAutoId(generateCopyWidgetNodeAutoId(sourceWidget));
            sourceWidget.setLastAutoId(sourceWidget.getLastAutoId() + 1);
            List<DXWidgetNode> children = dXWidgetNode.getChildren();
            if (children != null && children.size() != 0) {
            }
        }
    }
}
