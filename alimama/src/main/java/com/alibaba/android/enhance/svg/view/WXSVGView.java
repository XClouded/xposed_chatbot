package com.alibaba.android.enhance.svg.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.alibaba.android.enhance.svg.AbstractSVGVirtualComponent;
import com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent;
import com.alibaba.android.enhance.svg.component.SVGViewComponent;
import com.alibaba.android.enhance.svg.event.GestureEventDispatcher;
import com.taobao.weex.common.Constants;
import java.util.LinkedList;
import java.util.List;

public class WXSVGView extends FrameLayout {
    private boolean isParentConsumedGesture;
    private LinkedList<AbstractSVGVirtualComponent> mConsumedGestureComponentList = new LinkedList<>();
    private SVGViewComponent mShadowTarget;
    private boolean shouldStopPropagation;

    public WXSVGView(@NonNull Context context) {
        super(context);
        init();
    }

    public WXSVGView(@NonNull Context context, @Nullable AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public WXSVGView(@NonNull Context context, @Nullable AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    private void init() {
        setWillNotDraw(false);
    }

    public void setShadowComponent(@Nullable SVGViewComponent sVGViewComponent) {
        this.mShadowTarget = sVGViewComponent;
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mShadowTarget != null) {
            this.mShadowTarget.drawChildren(canvas);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0043, code lost:
        r4 = (com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent) r2;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean dispatchTouchEvent(android.view.MotionEvent r7) {
        /*
            r6 = this;
            com.alibaba.android.enhance.svg.component.SVGViewComponent r0 = r6.mShadowTarget
            if (r0 == 0) goto L_0x007e
            boolean r0 = r6.isParentConsumedGesture
            if (r0 == 0) goto L_0x000a
            goto L_0x007e
        L_0x000a:
            int r0 = r7.getActionMasked()
            r1 = 3
            if (r0 == r1) goto L_0x0072
            switch(r0) {
                case 0: goto L_0x001d;
                case 1: goto L_0x0072;
                default: goto L_0x0014;
            }
        L_0x0014:
            java.util.LinkedList<com.alibaba.android.enhance.svg.AbstractSVGVirtualComponent> r0 = r6.mConsumedGestureComponentList
            boolean r1 = r6.shouldStopPropagation
            boolean r7 = r6.dispatchToChild(r0, r7, r1)
            return r7
        L_0x001d:
            r0 = 0
            com.alibaba.android.enhance.svg.component.SVGViewComponent r1 = r6.mShadowTarget
            float r2 = r7.getX()
            float r3 = r7.getY()
            java.util.List r1 = r1.hitTest(r2, r3)
            java.util.Iterator r1 = r1.iterator()
        L_0x0030:
            boolean r2 = r1.hasNext()
            r3 = 1
            if (r2 == 0) goto L_0x0063
            java.lang.Object r2 = r1.next()
            com.alibaba.android.enhance.svg.AbstractSVGVirtualComponent r2 = (com.alibaba.android.enhance.svg.AbstractSVGVirtualComponent) r2
            if (r2 == 0) goto L_0x0030
            boolean r4 = r2 instanceof com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent
            if (r4 == 0) goto L_0x0030
            r4 = r2
            com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent r4 = (com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent) r4
            com.alibaba.android.enhance.svg.event.GestureEventDispatcher r5 = r4.getGestureDispatcher()
            if (r5 == 0) goto L_0x0030
            boolean r5 = r5.onTouch(r6, r7)
            if (r5 == 0) goto L_0x0030
            java.util.LinkedList<com.alibaba.android.enhance.svg.AbstractSVGVirtualComponent> r0 = r6.mConsumedGestureComponentList
            r0.add(r2)
            boolean r0 = r6.shouldStopPropagation(r4, r7)
            if (r0 == 0) goto L_0x0061
            r6.shouldStopPropagation = r3
            r0 = 1
            goto L_0x0063
        L_0x0061:
            r0 = 1
            goto L_0x0030
        L_0x0063:
            boolean r1 = r6.shouldStopPropagation
            if (r1 == 0) goto L_0x0068
            return r3
        L_0x0068:
            boolean r7 = r6.dispatchTouchEventMySelf(r7)
            if (r0 != 0) goto L_0x0071
            r6.isParentConsumedGesture = r7
            return r7
        L_0x0071:
            return r3
        L_0x0072:
            java.util.LinkedList<com.alibaba.android.enhance.svg.AbstractSVGVirtualComponent> r0 = r6.mConsumedGestureComponentList
            boolean r1 = r6.shouldStopPropagation
            boolean r0 = r6.dispatchToChild(r0, r7, r1)
            r6.resetFlagWhenGestureEnd(r7)
            return r0
        L_0x007e:
            r6.resetFlagWhenGestureEnd(r7)
            boolean r7 = r6.dispatchTouchEventMySelf(r7)
            return r7
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.enhance.svg.view.WXSVGView.dispatchTouchEvent(android.view.MotionEvent):boolean");
    }

    private boolean dispatchToChild(List<AbstractSVGVirtualComponent> list, MotionEvent motionEvent, boolean z) {
        GestureEventDispatcher gestureDispatcher;
        boolean z2 = false;
        if (list != null && list.size() > 0) {
            for (AbstractSVGVirtualComponent next : list) {
                if (!(next == null || !(next instanceof RenderableSVGVirtualComponent) || (gestureDispatcher = ((RenderableSVGVirtualComponent) next).getGestureDispatcher()) == null)) {
                    z2 |= gestureDispatcher.onTouch(this, motionEvent);
                }
            }
        }
        return !z ? z2 | dispatchTouchEventMySelf(motionEvent) : z2;
    }

    private boolean shouldStopPropagation(RenderableSVGVirtualComponent renderableSVGVirtualComponent, MotionEvent motionEvent) {
        return renderableSVGVirtualComponent.containsEvent(Constants.Event.STOP_PROPAGATION) || renderableSVGVirtualComponent.containsEvent("click");
    }

    private boolean dispatchTouchEventMySelf(MotionEvent motionEvent) {
        if (this.mShadowTarget == null) {
            return super.dispatchTouchEvent(motionEvent);
        }
        GestureEventDispatcher gestureDispatcher = this.mShadowTarget.getGestureDispatcher();
        if (gestureDispatcher == null) {
            return super.dispatchTouchEvent(motionEvent);
        }
        gestureDispatcher.setRequestDisallowInterceptTouchEvent(false);
        return super.dispatchTouchEvent(motionEvent) | gestureDispatcher.onTouch(this, motionEvent);
    }

    private void resetFlagWhenGestureEnd(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 1 || actionMasked == 3) {
            this.isParentConsumedGesture = false;
            this.shouldStopPropagation = false;
            if (this.mConsumedGestureComponentList != null) {
                this.mConsumedGestureComponentList.clear();
            }
        }
    }
}
