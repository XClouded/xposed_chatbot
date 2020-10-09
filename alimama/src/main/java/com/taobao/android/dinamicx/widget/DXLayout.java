package com.taobao.android.dinamicx.widget;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import com.alibaba.fastjson.JSONArray;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.model.DXLayoutParamAttribute;
import com.taobao.android.dinamicx.template.loader.binary.DXHashConstant;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DXLayout extends DXWidgetNode {
    boolean disableFlatten;
    private JSONArray listData;

    public boolean isDisableFlatten() {
        return this.disableFlatten;
    }

    public void setDisableFlatten(boolean z) {
        this.disableFlatten = z;
    }

    /* access modifiers changed from: protected */
    public void measureChildWithMargins(DXWidgetNode dXWidgetNode, int i, int i2, int i3, int i4) {
        dXWidgetNode.measure(getChildMeasureSpec(i, this.paddingLeft + this.paddingRight + dXWidgetNode.marginLeft + dXWidgetNode.marginRight + i2, dXWidgetNode.layoutWidth), getChildMeasureSpec(i3, this.paddingTop + this.paddingBottom + dXWidgetNode.marginTop + dXWidgetNode.marginBottom + i4, dXWidgetNode.layoutHeight));
    }

    public boolean isLayoutRtl() {
        return getDirection() == 1;
    }

    public void onSetIntAttribute(long j, int i) {
        if (j == DXHashConstant.DX_LAYOUT_DISABLEFLATTEN) {
            boolean z = true;
            if (i != 1) {
                z = false;
            }
            this.disableFlatten = z;
            return;
        }
        super.onSetIntAttribute(j, i);
    }

    /* access modifiers changed from: protected */
    public void onSetListAttribute(long j, JSONArray jSONArray) {
        if (DXHashConstant.DX_LISTLAYOUT_LISTDATA == j) {
            this.listData = jSONArray;
            this.propertyInitFlag |= 2;
            return;
        }
        super.onSetListAttribute(j, jSONArray);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0036, code lost:
        if (r7 == -2) goto L_0x0033;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0021, code lost:
        if (r7 == -2) goto L_0x0033;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x002a, code lost:
        if (r7 == -2) goto L_0x003a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static int getChildMeasureSpec(int r5, int r6, int r7) {
        /*
            int r0 = com.taobao.android.dinamicx.widget.DXWidgetNode.DXMeasureSpec.getMode(r5)
            int r5 = com.taobao.android.dinamicx.widget.DXWidgetNode.DXMeasureSpec.getSize(r5)
            int r5 = r5 - r6
            r6 = 0
            int r5 = java.lang.Math.max(r6, r5)
            r1 = -2
            r2 = -1
            r3 = -2147483648(0xffffffff80000000, float:-0.0)
            r4 = 1073741824(0x40000000, float:2.0)
            if (r0 == r3) goto L_0x002d
            if (r0 == 0) goto L_0x0024
            if (r0 == r4) goto L_0x001b
            goto L_0x0039
        L_0x001b:
            if (r7 < 0) goto L_0x001e
            goto L_0x002f
        L_0x001e:
            if (r7 != r2) goto L_0x0021
            goto L_0x003b
        L_0x0021:
            if (r7 != r1) goto L_0x0039
            goto L_0x0033
        L_0x0024:
            if (r7 < 0) goto L_0x0027
            goto L_0x002f
        L_0x0027:
            if (r7 != r2) goto L_0x002a
        L_0x0029:
            goto L_0x003a
        L_0x002a:
            if (r7 != r1) goto L_0x0039
            goto L_0x0029
        L_0x002d:
            if (r7 < 0) goto L_0x0031
        L_0x002f:
            r5 = r7
            goto L_0x003b
        L_0x0031:
            if (r7 != r2) goto L_0x0036
        L_0x0033:
            r4 = -2147483648(0xffffffff80000000, float:-0.0)
            goto L_0x003b
        L_0x0036:
            if (r7 != r1) goto L_0x0039
            goto L_0x0033
        L_0x0039:
            r5 = 0
        L_0x003a:
            r4 = 0
        L_0x003b:
            int r5 = com.taobao.android.dinamicx.widget.DXWidgetNode.DXMeasureSpec.makeMeasureSpec(r5, r4)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.widget.DXLayout.getChildMeasureSpec(int, int, int):int");
    }

    public ViewGroup.LayoutParams generateLayoutParams(@NonNull DXLayoutParamAttribute dXLayoutParamAttribute) {
        return new ViewGroup.LayoutParams(dXLayoutParamAttribute.widthAttr, dXLayoutParamAttribute.heightAttr);
    }

    public ViewGroup.LayoutParams generateLayoutParams(@NonNull DXLayoutParamAttribute dXLayoutParamAttribute, @NonNull ViewGroup.LayoutParams layoutParams) {
        layoutParams.width = dXLayoutParamAttribute.widthAttr;
        layoutParams.height = dXLayoutParamAttribute.heightAttr;
        return layoutParams;
    }

    public boolean isClipChildren() {
        return this.clipChildren;
    }

    public void setClipChildren(boolean z) {
        this.clipChildren = z;
    }

    public void onClone(DXWidgetNode dXWidgetNode, boolean z) {
        super.onClone(dXWidgetNode, z);
        if (dXWidgetNode instanceof DXLayout) {
            DXLayout dXLayout = (DXLayout) dXWidgetNode;
            this.disableFlatten = dXLayout.disableFlatten;
            this.listData = dXLayout.listData;
        }
    }

    /* access modifiers changed from: protected */
    public void onRenderView(Context context, View view) {
        if (view != null) {
            if (view instanceof ViewGroup) {
                if (Build.VERSION.SDK_INT >= 18) {
                    ViewGroup viewGroup = (ViewGroup) view;
                    if (viewGroup.getClipChildren() != this.clipChildren) {
                        viewGroup.setClipChildren(this.clipChildren);
                    }
                } else {
                    ((ViewGroup) view).setClipChildren(this.clipChildren);
                }
            }
            super.onRenderView(context, view);
        }
    }

    /* access modifiers changed from: package-private */
    public void setAccessibility(View view) {
        if (Build.VERSION.SDK_INT < 16) {
            view.setContentDescription("");
        } else if (this.accessibility != -1) {
            if (this.accessibility == 3) {
                view.setImportantForAccessibility(1);
                view.setContentDescription((CharSequence) null);
                return;
            }
            if (this.accessibilityText != null) {
                view.setContentDescription(this.accessibilityText);
            }
            if (this.accessibility == 1) {
                view.setImportantForAccessibility(1);
                view.setFocusable(true);
            } else if (this.accessibility == 2) {
                view.setImportantForAccessibility(4);
            } else {
                view.setImportantForAccessibility(2);
            }
        }
    }

    public JSONArray getListData() {
        return this.listData;
    }

    public void setListData(JSONArray jSONArray) {
        this.listData = jSONArray;
        this.propertyInitFlag |= 2;
    }

    public void onBeforeBindChildData() {
        if ((this.propertyInitFlag & 2) != 0) {
            if (this.listData == null || this.listData.isEmpty() || getChildren() == null) {
                removeAllChild();
                return;
            }
            ArrayList arrayList = (ArrayList) getChildren();
            ArrayList arrayList2 = new ArrayList();
            for (int i = 0; i < this.listData.size(); i++) {
                Object obj = this.listData.get(i);
                if (i == 0) {
                    Iterator it = arrayList.iterator();
                    while (it.hasNext()) {
                        bindContext((DXWidgetNode) it.next(), obj, i);
                    }
                } else {
                    Iterator it2 = arrayList.iterator();
                    while (it2.hasNext()) {
                        DXWidgetNode dXWidgetNode = (DXWidgetNode) it2.next();
                        DXRuntimeContext cloneWithWidgetNode = dXWidgetNode.getDXRuntimeContext().cloneWithWidgetNode(dXWidgetNode);
                        cloneWithWidgetNode.setSubData(obj);
                        cloneWithWidgetNode.setSubdataIndex(i);
                        arrayList2.add(DXLayoutUtil.deepCopyChildNode(dXWidgetNode, cloneWithWidgetNode));
                    }
                }
            }
            for (int i2 = 0; i2 < arrayList2.size(); i2++) {
                addChild((DXWidgetNode) arrayList2.get(i2), false);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void bindContext(DXWidgetNode dXWidgetNode, Object obj, int i) {
        dXWidgetNode.getDXRuntimeContext().setSubData(obj);
        dXWidgetNode.getDXRuntimeContext().setSubdataIndex(i);
        if (dXWidgetNode.getSourceWidget() == null) {
            dXWidgetNode.setSourceWidget(dXWidgetNode);
        }
        List<DXWidgetNode> children = dXWidgetNode.getChildren();
        if (children != null && children.size() != 0) {
            for (DXWidgetNode bindContext : children) {
                bindContext(bindContext, obj, i);
            }
        }
    }
}
