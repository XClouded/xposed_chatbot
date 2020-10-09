package com.taobao.android.dinamicx.render.diff;

import android.util.SparseArray;
import android.view.View;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import java.util.ArrayList;
import java.util.List;

public class DXSimplePipelineDiff extends DXAbsDiff {
    public void diff(DXWidgetNode dXWidgetNode, DXWidgetNode dXWidgetNode2) {
        if (dXWidgetNode != dXWidgetNode2 && dXWidgetNode2 != null) {
            if (dXWidgetNode == null) {
                removeFromSuperView(dXWidgetNode2.getWRView());
                return;
            }
            int childrenCount = dXWidgetNode2.getChildrenCount();
            int childrenCount2 = dXWidgetNode.getChildrenCount();
            if (childrenCount != 0 || childrenCount2 != 0) {
                if (childrenCount == 0 && childrenCount2 > 0) {
                    return;
                }
                if (childrenCount <= 0 || childrenCount2 != 0) {
                    SparseArray sparseArray = new SparseArray();
                    for (int i = 0; i < dXWidgetNode.getChildrenCount(); i++) {
                        DXWidgetNode sourceWidget = dXWidgetNode.getChildAt(i).getSourceWidget();
                        if (sourceWidget != null) {
                            int autoId = sourceWidget.getAutoId();
                            List list = (List) sparseArray.get(autoId);
                            if (list == null) {
                                ArrayList arrayList = new ArrayList();
                                arrayList.add(Integer.valueOf(i));
                                sparseArray.put(autoId, arrayList);
                            } else {
                                list.add(Integer.valueOf(i));
                            }
                        }
                    }
                    int[] iArr = new int[childrenCount2];
                    for (int i2 = 0; i2 < childrenCount2; i2++) {
                        iArr[i2] = -1;
                    }
                    for (int i3 = 0; i3 < dXWidgetNode2.getChildrenCount(); i3++) {
                        DXWidgetNode childAt = dXWidgetNode2.getChildAt(i3);
                        if (childAt.getSourceWidget() == null || sparseArray.size() == 0) {
                            removeFromSuperView(childAt.getWRView());
                        } else {
                            List list2 = (List) sparseArray.get(childAt.getSourceWidget().getAutoId());
                            if (list2 == null || list2.isEmpty()) {
                                removeFromSuperView(childAt.getWRView());
                            } else {
                                iArr[((Integer) list2.get(0)).intValue()] = i3;
                                list2.remove(0);
                            }
                        }
                    }
                    for (int i4 = 0; i4 < childrenCount2; i4++) {
                        int i5 = iArr[i4];
                        DXWidgetNode childAt2 = dXWidgetNode.getChildAt(i4);
                        if (i5 != -1) {
                            DXWidgetNode childAt3 = dXWidgetNode2.getChildAt(i5);
                            childAt2.setWRView(childAt3.getWRView());
                            if (!(childAt2.getWRView() == null || childAt2.getWRView().get() == null)) {
                                ((View) childAt2.getWRView().get()).setTag(DXWidgetNode.TAG_WIDGET_NODE, childAt2);
                            }
                            if (childAt2.getChildrenCount() > 0 || childAt3.getChildrenCount() > 0) {
                                diff(childAt2, childAt3);
                            }
                        }
                    }
                    return;
                }
                removeAllChildView(dXWidgetNode2.getWRView());
            }
        }
    }
}
