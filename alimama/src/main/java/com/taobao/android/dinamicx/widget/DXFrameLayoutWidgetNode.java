package com.taobao.android.dinamicx.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.android.dinamicx.model.DXLayoutParamAttribute;
import com.taobao.android.dinamicx.view.CLipRadiusHandler;
import com.taobao.android.dinamicx.view.DXNativeFrameLayout;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import java.util.ArrayList;

public class DXFrameLayoutWidgetNode extends DXLayout {
    private static final int DEFAULT_CHILD_GRAVITY = 0;
    private final ArrayList<DXWidgetNode> matchParentChildren = new ArrayList<>(1);
    boolean measureAllChildren = false;

    public static class Builder implements IDXBuilderWidgetNode {
        public DXWidgetNode build(@Nullable Object obj) {
            return new DXFrameLayoutWidgetNode();
        }
    }

    public DXWidgetNode build(@Nullable Object obj) {
        return new DXFrameLayoutWidgetNode();
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int i5 = i;
        int i6 = i2;
        int virtualChildCount = getVirtualChildCount();
        boolean z = (DXWidgetNode.DXMeasureSpec.getMode(i) == 1073741824 && DXWidgetNode.DXMeasureSpec.getMode(i2) == 1073741824) ? false : true;
        this.matchParentChildren.clear();
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        for (int i10 = 0; i10 < virtualChildCount; i10++) {
            DXWidgetNode childAt = getChildAt(i10);
            if (this.measureAllChildren || childAt.getVisibility() != 2) {
                DXWidgetNode dXWidgetNode = childAt;
                measureChildWithMargins(childAt, i, 0, i2, 0);
                DXWidgetNode dXWidgetNode2 = dXWidgetNode;
                int max = Math.max(i9, dXWidgetNode.getMeasuredWidth() + dXWidgetNode2.marginLeft + dXWidgetNode2.marginRight);
                int max2 = Math.max(i8, dXWidgetNode2.getMeasuredHeight() + dXWidgetNode2.marginTop + dXWidgetNode2.marginBottom);
                int combineMeasuredStates = combineMeasuredStates(i7, dXWidgetNode2.getMeasuredState());
                if (z && (dXWidgetNode2.layoutWidth == -1 || dXWidgetNode2.layoutHeight == -1)) {
                    this.matchParentChildren.add(dXWidgetNode2);
                }
                i9 = max;
                i8 = max2;
                i7 = combineMeasuredStates;
            }
        }
        int i11 = i7;
        setMeasuredDimension(resolveSizeAndState(Math.max(i9 + getPaddingLeftForMeasure() + getPaddingRightForMeasure(), getSuggestedMinimumWidth()), i5, i11), resolveSizeAndState(Math.max(i8 + getPaddingTopForMeasure() + getPaddingBottomForMeasure(), getSuggestedMinimumHeight()), i6, i11 << 16));
        int size = this.matchParentChildren.size();
        if (size > 1) {
            for (int i12 = 0; i12 < size; i12++) {
                DXWidgetNode dXWidgetNode3 = this.matchParentChildren.get(i12);
                if (dXWidgetNode3.layoutWidth == -1) {
                    i3 = DXWidgetNode.DXMeasureSpec.makeMeasureSpec(Math.max(0, (((getMeasuredWidth() - this.paddingLeft) - this.paddingRight) - dXWidgetNode3.marginLeft) - dXWidgetNode3.marginRight), 1073741824);
                } else {
                    i3 = getChildMeasureSpec(i5, this.paddingLeft + this.paddingRight + dXWidgetNode3.marginLeft + dXWidgetNode3.marginRight, dXWidgetNode3.layoutWidth);
                }
                if (dXWidgetNode3.layoutHeight == -1) {
                    i4 = DXWidgetNode.DXMeasureSpec.makeMeasureSpec(Math.max(0, (((getMeasuredHeight() - this.paddingTop) - this.paddingBottom) - dXWidgetNode3.marginTop) - dXWidgetNode3.marginBottom), 1073741824);
                } else {
                    i4 = getChildMeasureSpec(i6, this.paddingTop + this.paddingBottom + dXWidgetNode3.marginTop + dXWidgetNode3.marginBottom, dXWidgetNode3.layoutHeight);
                }
                dXWidgetNode3.measure(i3, i4);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        layoutChildren(i, i2, i3, i4, false);
    }

    private int getPaddingLeftForMeasure() {
        int paddingLeftWithDirection = getPaddingLeftWithDirection();
        if (paddingLeftWithDirection > 0) {
            return paddingLeftWithDirection;
        }
        return 0;
    }

    private int getPaddingRightForMeasure() {
        int paddingRightWithDirection = getPaddingRightWithDirection();
        if (paddingRightWithDirection > 0) {
            return paddingRightWithDirection;
        }
        return 0;
    }

    private int getPaddingTopForMeasure() {
        if (this.paddingTop > 0) {
            return this.paddingTop;
        }
        return 0;
    }

    private int getPaddingBottomForMeasure() {
        if (this.paddingBottom > 0) {
            return this.paddingBottom;
        }
        return 0;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void layoutChildren(int r18, int r19, int r20, int r21, boolean r22) {
        /*
            r17 = this;
            int r0 = r17.getVirtualChildCount()
            int r1 = r17.getDirection()
            int r2 = r17.getPaddingLeftForMeasure()
            int r3 = r20 - r18
            int r4 = r17.getPaddingRightForMeasure()
            int r3 = r3 - r4
            int r4 = r17.getPaddingTopForMeasure()
            int r5 = r21 - r19
            int r6 = r17.getPaddingBottomForMeasure()
            int r5 = r5 - r6
            r6 = 0
            r7 = 0
        L_0x0020:
            if (r7 >= r0) goto L_0x0091
            r8 = r17
            com.taobao.android.dinamicx.widget.DXWidgetNode r9 = r8.getChildAt(r7)
            int r10 = r9.getVisibility()
            r11 = 2
            if (r10 == r11) goto L_0x008e
            int r10 = r9.getMeasuredWidth()
            int r12 = r9.getMeasuredHeight()
            int r13 = r9.layoutGravity
            if (r13 != 0) goto L_0x0042
            int r14 = r9.propertyInitFlag
            r14 = r14 & 1
            if (r14 != 0) goto L_0x0042
            r13 = 0
        L_0x0042:
            int r13 = getAbsoluteGravity(r13, r1)
            switch(r13) {
                case 3: goto L_0x0055;
                case 4: goto L_0x0055;
                case 5: goto L_0x0055;
                case 6: goto L_0x004a;
                case 7: goto L_0x004a;
                case 8: goto L_0x004a;
                default: goto L_0x0049;
            }
        L_0x0049:
            goto L_0x0067
        L_0x004a:
            if (r22 != 0) goto L_0x0067
            int r15 = r3 - r10
            int r16 = r9.getRightMarginWithDirection()
            int r15 = r15 - r16
            goto L_0x006c
        L_0x0055:
            int r15 = r3 - r2
            int r15 = r15 - r10
            int r15 = r15 / r11
            int r15 = r15 + r2
            int r16 = r9.getLeftMarginWithDirection()
            int r15 = r15 + r16
            int r16 = r9.getRightMarginWithDirection()
            int r15 = r15 - r16
            goto L_0x006c
        L_0x0067:
            int r15 = r9.getLeftMarginWithDirection()
            int r15 = r15 + r2
        L_0x006c:
            switch(r13) {
                case 0: goto L_0x0086;
                case 1: goto L_0x0079;
                case 2: goto L_0x0073;
                case 3: goto L_0x0086;
                case 4: goto L_0x0079;
                case 5: goto L_0x0073;
                case 6: goto L_0x0086;
                case 7: goto L_0x0079;
                case 8: goto L_0x0073;
                default: goto L_0x006f;
            }
        L_0x006f:
            int r11 = r9.marginTop
            int r11 = r11 + r4
            goto L_0x0089
        L_0x0073:
            int r11 = r5 - r12
            int r13 = r9.marginBottom
            int r11 = r11 - r13
            goto L_0x0089
        L_0x0079:
            int r13 = r5 - r4
            int r13 = r13 - r12
            int r13 = r13 / r11
            int r13 = r13 + r4
            int r11 = r9.marginTop
            int r13 = r13 + r11
            int r11 = r9.marginBottom
            int r11 = r13 - r11
            goto L_0x0089
        L_0x0086:
            int r11 = r9.marginTop
            int r11 = r11 + r4
        L_0x0089:
            int r10 = r10 + r15
            int r12 = r12 + r11
            r9.layout(r15, r11, r10, r12)
        L_0x008e:
            int r7 = r7 + 1
            goto L_0x0020
        L_0x0091:
            r8 = r17
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.android.dinamicx.widget.DXFrameLayoutWidgetNode.layoutChildren(int, int, int, int, boolean):void");
    }

    public ViewGroup.LayoutParams generateLayoutParams(DXLayoutParamAttribute dXLayoutParamAttribute) {
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dXLayoutParamAttribute.widthAttr, dXLayoutParamAttribute.heightAttr);
        layoutParams.gravity = dXLayoutParamAttribute.layoutGravityAttr;
        return layoutParams;
    }

    public ViewGroup.LayoutParams generateLayoutParams(@NonNull DXLayoutParamAttribute dXLayoutParamAttribute, @NonNull ViewGroup.LayoutParams layoutParams) {
        if (layoutParams instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) layoutParams).gravity = dXLayoutParamAttribute.layoutGravityAttr;
        }
        layoutParams.width = dXLayoutParamAttribute.widthAttr;
        layoutParams.height = dXLayoutParamAttribute.heightAttr;
        return layoutParams;
    }

    /* access modifiers changed from: protected */
    public View onCreateView(Context context) {
        return new DXNativeFrameLayout(context);
    }

    /* access modifiers changed from: protected */
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public void setBackground(View view) {
        if (hasCornerRadius()) {
            DXNativeFrameLayout dXNativeFrameLayout = (DXNativeFrameLayout) view;
            CLipRadiusHandler cLipRadiusHandler = new CLipRadiusHandler();
            if (this.cornerRadius > 0) {
                cLipRadiusHandler.setRadius(view, (float) this.cornerRadius);
            } else {
                cLipRadiusHandler.setRadius(view, (float) this.cornerRadiusLeftTop, (float) this.cornerRadiusRightTop, (float) this.cornerRadiusLeftBottom, (float) this.cornerRadiusRightBottom);
            }
            dXNativeFrameLayout.setClipRadiusHandler(cLipRadiusHandler);
        } else {
            CLipRadiusHandler cLipRadiusHandler2 = ((DXNativeFrameLayout) view).getCLipRadiusHandler();
            if (cLipRadiusHandler2 != null) {
                cLipRadiusHandler2.setRadius(view, 0.0f);
            }
        }
        super.setBackground(view);
    }
}
