package com.taobao.android.dinamicx.widget;

import android.content.Context;
import android.view.View;
import com.taobao.android.dinamicx.DXRuntimeContext;
import com.taobao.android.dinamicx.DXSimpleRenderPipeline;
import com.taobao.android.dinamicx.DXWidgetNodeParser;
import com.taobao.android.dinamicx.expression.event.DXEvent;
import com.taobao.android.dinamicx.expression.event.DXViewEvent;
import com.taobao.android.dinamicx.template.loader.binary.DXHashConstant;
import com.taobao.android.dinamicx.view.CLipRadiusHandler;
import com.taobao.android.dinamicx.view.DXNativeRecyclerView;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class DXScrollLayoutBase extends DXLinearLayoutWidgetNode {
    public static final long DX_SCROLL_LAYOUT_BASE = -116275953006946184L;
    public static final long DX_SCROLL_LAYOUT_BASE_INDICATOR_ID = 7196296497982840181L;
    public static final long DX_SCROLL_LAYOUT_BASE_ON_PAGE_APPEAR = -8975334121118753601L;
    public static final long DX_SCROLL_LAYOUT_BASE_ON_PAGE_DISAPPEAR = -5201408949358043646L;
    public static final long DX_SCROLL_LAYOUT_BASE_ON_SCROLL = 5288751146867425108L;
    public static final long DX_SCROLL_LAYOUT_BASE_ON_SCROLL_BEGIN = 9144262755562405950L;
    public static final long DX_SCROLL_LAYOUT_BASE_ON_SCROLL_END = 2691126191158604142L;
    public static final long DX_SCROLL_LAYOUT_BASE_SCROLL_ENABLED = -8352681166307095225L;
    public static final long DX_SCROLL_LAYOUT_BASE_SHOW_INDICATOR = -3765027987112450965L;
    private List<DXWidgetNode> appearWidgets;
    protected int contentHorizontalLength;
    protected int contentVerticalLength;
    protected String indicatorId;
    protected DXWidgetNode indicatorWidgetNode;
    protected ArrayList<DXWidgetNode> itemWidgetNodes;
    protected DXSimpleRenderPipeline pipeline;
    protected boolean scrollEnabled = true;
    protected boolean showIndicator = true;

    public void onClone(DXWidgetNode dXWidgetNode, boolean z) {
        super.onClone(dXWidgetNode, z);
        if (dXWidgetNode instanceof DXScrollLayoutBase) {
            DXScrollLayoutBase dXScrollLayoutBase = (DXScrollLayoutBase) dXWidgetNode;
            this.indicatorId = dXScrollLayoutBase.indicatorId;
            this.scrollEnabled = dXScrollLayoutBase.scrollEnabled;
            this.showIndicator = dXScrollLayoutBase.showIndicator;
            this.itemWidgetNodes = dXScrollLayoutBase.itemWidgetNodes;
            this.indicatorWidgetNode = dXScrollLayoutBase.indicatorWidgetNode;
            this.pipeline = dXScrollLayoutBase.pipeline;
            this.contentHorizontalLength = dXScrollLayoutBase.contentHorizontalLength;
            this.contentVerticalLength = dXScrollLayoutBase.contentVerticalLength;
            this.appearWidgets = dXScrollLayoutBase.appearWidgets;
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        if (this.pipeline == null) {
            this.pipeline = new DXSimpleRenderPipeline(getDXRuntimeContext().getEngineContext(), 3, UUID.randomUUID().toString());
        }
        removeAllChild();
        super.onMeasure(i, i2);
    }

    public void onBeforeBindChildData() {
        super.onBeforeBindChildData();
        DXWidgetNode queryIndicatorNodeByUserId = queryIndicatorNodeByUserId(this.indicatorId);
        if (queryIndicatorNodeByUserId != null) {
            DXWidgetNodeParser.isWidgetNodeGone(queryIndicatorNodeByUserId);
            if (this.showIndicator) {
                queryIndicatorNodeByUserId.setVisibility(0);
                this.indicatorWidgetNode = queryIndicatorNodeByUserId;
            } else {
                queryIndicatorNodeByUserId.setVisibility(2);
            }
        }
        ArrayList<DXWidgetNode> arrayList = new ArrayList<>();
        for (DXWidgetNode add : getChildren()) {
            arrayList.add(add);
        }
        this.itemWidgetNodes = arrayList;
        setDisableFlatten(true);
    }

    /* access modifiers changed from: protected */
    public void onRenderView(Context context, View view) {
        DXWidgetNode widgetNode;
        super.onRenderView(context, view);
        if (getChildrenCount() > 0 && (widgetNode = getDXRuntimeContext().getWidgetNode()) != null) {
            widgetNode.removeAllChild();
            removeAllChild();
        }
    }

    public void setBackground(View view) {
        if (hasCornerRadius()) {
            DXNativeRecyclerView dXNativeRecyclerView = (DXNativeRecyclerView) view;
            CLipRadiusHandler cLipRadiusHandler = new CLipRadiusHandler();
            if (this.cornerRadius > 0) {
                cLipRadiusHandler.setRadius(view, (float) this.cornerRadius);
            } else {
                cLipRadiusHandler.setRadius(view, (float) this.cornerRadiusLeftTop, (float) this.cornerRadiusRightTop, (float) this.cornerRadiusLeftBottom, (float) this.cornerRadiusRightBottom);
            }
            dXNativeRecyclerView.setClipRadiusHandler(cLipRadiusHandler);
        } else {
            CLipRadiusHandler cLipRadiusHandler2 = ((DXNativeRecyclerView) view).getCLipRadiusHandler();
            if (cLipRadiusHandler2 != null) {
                cLipRadiusHandler2.setRadius(view, 0.0f);
            }
        }
        super.setBackground(view);
    }

    /* access modifiers changed from: protected */
    public void measureHorizontal(int i, int i2) {
        boolean z;
        int i3;
        int i4;
        boolean z2;
        boolean z3;
        DXWidgetNode dXWidgetNode;
        int i5 = i;
        this.mTotalLength = 0;
        int mode = DXWidgetNode.DXMeasureSpec.getMode(i2);
        if (this.itemWidgetNodes != null) {
            Iterator<DXWidgetNode> it = this.itemWidgetNodes.iterator();
            z2 = true;
            i4 = 0;
            i3 = 0;
            z = false;
            while (it.hasNext()) {
                DXWidgetNode next = it.next();
                if (!(next == null || next.visibility == 2)) {
                    DXWidgetNode dXWidgetNode2 = next;
                    measureChildWithMargins(next, measureSpecForChild(next.layoutWidth, i5), 0, i2, 0);
                    if (mode == 1073741824 || dXWidgetNode2.getLayoutHeight() != -1) {
                        dXWidgetNode = dXWidgetNode2;
                        z3 = false;
                    } else {
                        dXWidgetNode = dXWidgetNode2;
                        z3 = true;
                        z = true;
                    }
                    int i6 = dXWidgetNode.marginTop + dXWidgetNode.marginBottom;
                    int measuredHeight = dXWidgetNode.getMeasuredHeight() + i6;
                    i3 = Math.max(i3, measuredHeight);
                    z2 = z2 && dXWidgetNode.layoutHeight == -1;
                    if (!z3) {
                        i6 = measuredHeight;
                    }
                    i4 = Math.max(i4, i6);
                    this.contentHorizontalLength += dXWidgetNode.getMeasuredWidth() + dXWidgetNode.marginLeft + dXWidgetNode.marginRight;
                }
            }
        } else {
            z2 = true;
            i4 = 0;
            i3 = 0;
            z = false;
        }
        int resolveSize = resolveSize(Math.max(getLayoutWidth(), getMinWidth()), i5);
        if (z2 || mode == 1073741824) {
            i4 = i3;
        }
        setMeasuredDimension(resolveSize, resolveSize(Math.max(i4 + getPaddingTop() + getPaddingBottom(), getMinHeight()), i2));
        if (z && this.itemWidgetNodes != null) {
            forceUniformHeight(this.itemWidgetNodes, i5);
        }
    }

    private void forceUniformHeight(List<DXWidgetNode> list, int i) {
        int makeMeasureSpec = DXWidgetNode.DXMeasureSpec.makeMeasureSpec(getMeasuredHeight(), 1073741824);
        for (DXWidgetNode next : list) {
            if (!(next == null || next.getVisibility() == 2 || next.layoutHeight != -1)) {
                int i2 = next.layoutWidth;
                next.layoutWidth = next.getMeasuredWidth();
                measureChildWithMargins(next, i, 0, makeMeasureSpec, 0);
                next.layoutWidth = i2;
            }
        }
    }

    public int measureSpecForChild(int i, int i2) {
        return i == -2 ? DXWidgetNode.DXMeasureSpec.makeMeasureSpec(8388607, 0) : i2;
    }

    /* access modifiers changed from: protected */
    public void measureVertical(int i, int i2) {
        boolean z;
        int i3;
        int i4;
        boolean z2;
        boolean z3;
        DXWidgetNode dXWidgetNode;
        int i5 = i2;
        this.mTotalLength = 0;
        int mode = DXWidgetNode.DXMeasureSpec.getMode(i);
        if (this.itemWidgetNodes != null) {
            Iterator<DXWidgetNode> it = this.itemWidgetNodes.iterator();
            z2 = true;
            i4 = 0;
            i3 = 0;
            z = false;
            while (it.hasNext()) {
                DXWidgetNode next = it.next();
                if (!(next == null || next.visibility == 2)) {
                    DXWidgetNode dXWidgetNode2 = next;
                    measureChildWithMargins(next, i, 0, measureSpecForChild(next.layoutHeight, i5), 0);
                    if (mode == 1073741824 || dXWidgetNode2.getLayoutWidth() != -1) {
                        dXWidgetNode = dXWidgetNode2;
                        z3 = false;
                    } else {
                        dXWidgetNode = dXWidgetNode2;
                        z3 = true;
                        z = true;
                    }
                    int i6 = dXWidgetNode.marginLeft + dXWidgetNode.marginRight;
                    int measuredWidth = dXWidgetNode.getMeasuredWidth() + i6;
                    i3 = Math.max(i3, measuredWidth);
                    z2 = z2 && dXWidgetNode.layoutWidth == -1;
                    if (!z3) {
                        i6 = measuredWidth;
                    }
                    i4 = Math.max(i4, i6);
                    this.contentVerticalLength += dXWidgetNode.getMeasuredHeight() + dXWidgetNode.marginTop + dXWidgetNode.marginBottom;
                }
            }
        } else {
            z2 = true;
            i4 = 0;
            i3 = 0;
            z = false;
        }
        int resolveSize = resolveSize(Math.max(getLayoutHeight(), getMinHeight()), i5);
        if (z2 || mode == 1073741824) {
            i4 = i3;
        }
        setMeasuredDimension(resolveSize(Math.max(i4 + getPaddingLeft() + getPaddingRight(), getMinWidth()), i), resolveSize);
        if (z && this.itemWidgetNodes != null) {
            forceUniformWidth(this.itemWidgetNodes, i5);
        }
    }

    private void forceUniformWidth(List<DXWidgetNode> list, int i) {
        int makeMeasureSpec = DXWidgetNode.DXMeasureSpec.makeMeasureSpec(getMeasuredWidth(), 1073741824);
        for (DXWidgetNode next : list) {
            if (!(next == null || next.getVisibility() == 2 || next.layoutWidth != -1)) {
                int i2 = next.layoutHeight;
                next.layoutHeight = next.getMeasuredHeight();
                measureChildWithMargins(next, makeMeasureSpec, 0, i, 0);
                next.layoutHeight = i2;
            }
        }
    }

    public void onSetIntAttribute(long j, int i) {
        boolean z = false;
        if (j == DX_SCROLL_LAYOUT_BASE_SHOW_INDICATOR) {
            if (i != 0) {
                z = true;
            }
            this.showIndicator = z;
        } else if (j == DX_SCROLL_LAYOUT_BASE_SCROLL_ENABLED) {
            if (i != 0) {
                z = true;
            }
            this.scrollEnabled = z;
        } else {
            super.onSetIntAttribute(j, i);
        }
    }

    public int getDefaultValueForIntAttr(long j) {
        if (j == DX_SCROLL_LAYOUT_BASE_SCROLL_ENABLED || j == DX_SCROLL_LAYOUT_BASE_SHOW_INDICATOR) {
            return 1;
        }
        return super.getDefaultValueForIntAttr(j);
    }

    /* access modifiers changed from: protected */
    public void onSetStringAttribute(long j, String str) {
        if (j == DX_SCROLL_LAYOUT_BASE_INDICATOR_ID) {
            this.indicatorId = str;
        } else {
            super.onSetStringAttribute(j, str);
        }
    }

    public DXWidgetNode queryWTByAutoId(int i) {
        DXWidgetNode queryWTByAutoId = super.queryWTByAutoId(i);
        if (queryWTByAutoId == null) {
            if (this.itemWidgetNodes == null) {
                return null;
            }
            Iterator<DXWidgetNode> it = this.itemWidgetNodes.iterator();
            while (it.hasNext()) {
                queryWTByAutoId = it.next().queryWTByAutoId(i);
                if (queryWTByAutoId != null) {
                    return queryWTByAutoId;
                }
            }
        }
        return queryWTByAutoId;
    }

    public DXWidgetNode queryWTByUserId(String str) {
        DXWidgetNode queryWTByUserId = super.queryWTByUserId(str);
        if (queryWTByUserId == null) {
            if (this.itemWidgetNodes == null) {
                return null;
            }
            Iterator<DXWidgetNode> it = this.itemWidgetNodes.iterator();
            while (it.hasNext()) {
                queryWTByUserId = it.next().queryWTByUserId(str);
                if (queryWTByUserId != null) {
                    return queryWTByUserId;
                }
            }
        }
        return queryWTByUserId;
    }

    private DXWidgetNode queryIndicatorNodeByUserId(String str) {
        DXWidgetNode parentWidget;
        if (str == null || (parentWidget = getParentWidget()) == null) {
            return null;
        }
        int i = 0;
        DXWidgetNode dXWidgetNode = null;
        int i2 = -1;
        int i3 = -1;
        for (DXWidgetNode next : parentWidget.getChildren()) {
            if (next == this) {
                i2 = i;
            } else if (str.equals(next.getUserId())) {
                i3 = i;
                dXWidgetNode = next;
            }
            if (i2 != -1 && i3 != -1) {
                return dXWidgetNode;
            }
            i++;
        }
        return null;
    }

    public void bindRuntimeContext(DXRuntimeContext dXRuntimeContext, boolean z) {
        super.bindRuntimeContext(dXRuntimeContext, z);
        if (dXRuntimeContext != null && this.itemWidgetNodes != null) {
            Iterator<DXWidgetNode> it = this.itemWidgetNodes.iterator();
            while (it.hasNext()) {
                it.next().bindRuntimeContext(dXRuntimeContext, z);
            }
        }
    }

    public void sendBroadcastEvent(DXEvent dXEvent) {
        if (dXEvent != null) {
            if (DXHashConstant.DX_VIEW_EVENT_ON_APPEAR == dXEvent.getEventId()) {
                postEvent(dXEvent);
                if (this.appearWidgets != null && this.appearWidgets.size() != 0) {
                    for (DXWidgetNode next : this.appearWidgets) {
                        DXViewEvent dXViewEvent = new DXViewEvent(DX_SCROLL_LAYOUT_BASE_ON_PAGE_APPEAR);
                        dXViewEvent.setItemIndex(next.getDXRuntimeContext().getSubdataIndex());
                        next.sendBroadcastEvent(dXViewEvent);
                    }
                }
            } else if (DXHashConstant.DX_VIEW_EVENT_ON_DISAPPEAR == dXEvent.getEventId()) {
                postEvent(dXEvent);
                if (this.appearWidgets != null && this.appearWidgets.size() != 0) {
                    for (DXWidgetNode next2 : this.appearWidgets) {
                        DXViewEvent dXViewEvent2 = new DXViewEvent(DX_SCROLL_LAYOUT_BASE_ON_PAGE_DISAPPEAR);
                        dXViewEvent2.setItemIndex(next2.getDXRuntimeContext().getSubdataIndex());
                        next2.sendBroadcastEvent(dXViewEvent2);
                    }
                }
            } else {
                postEvent(dXEvent);
                if (this.appearWidgets != null && this.appearWidgets.size() > 0) {
                    for (DXWidgetNode sendBroadcastEvent : this.appearWidgets) {
                        sendBroadcastEvent.sendBroadcastEvent(dXEvent);
                    }
                }
            }
        }
    }

    public void addAppearWidget(DXWidgetNode dXWidgetNode) {
        if (dXWidgetNode != null) {
            if (this.appearWidgets == null) {
                this.appearWidgets = new ArrayList();
            }
            this.appearWidgets.add(dXWidgetNode);
        }
    }

    public boolean removeAppearWidget(DXWidgetNode dXWidgetNode) {
        if (dXWidgetNode == null || this.appearWidgets == null) {
            return false;
        }
        return this.appearWidgets.remove(dXWidgetNode);
    }
}
