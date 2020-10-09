package com.alibaba.android.enhance.svg.event;

import android.content.Context;
import android.graphics.Rect;
import android.view.MotionEvent;
import androidx.annotation.NonNull;
import com.alibaba.android.enhance.svg.RenderableSVGVirtualComponent;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.view.gesture.WXGesture;
import com.taobao.weex.ui.view.gesture.WXGestureType;
import com.taobao.weex.utils.WXDataStructureUtil;
import com.taobao.weex.utils.WXViewUtils;
import java.util.HashMap;

public class GestureEventDispatcher extends WXGesture {
    private WXComponent mTargetComponent;

    public GestureEventDispatcher(WXComponent wXComponent, Context context) {
        super(wXComponent, context);
        this.mTargetComponent = wXComponent;
    }

    public boolean onSingleTapUp(MotionEvent motionEvent) {
        WXSDKInstance instance;
        if (this.mTargetComponent == null || (instance = this.mTargetComponent.getInstance()) == null || !this.mTargetComponent.containsEvent("click")) {
            return false;
        }
        HashMap newHashMapWithExpectedSize = WXDataStructureUtil.newHashMapWithExpectedSize(1);
        HashMap newHashMapWithExpectedSize2 = WXDataStructureUtil.newHashMapWithExpectedSize(4);
        if (this.mTargetComponent instanceof RenderableSVGVirtualComponent) {
            Rect bounds = ((RenderableSVGVirtualComponent) this.mTargetComponent).getBounds();
            newHashMapWithExpectedSize2.put(Constants.Name.X, Float.valueOf(WXViewUtils.getWebPxByWidth((float) bounds.left, instance.getInstanceViewPortWidth())));
            newHashMapWithExpectedSize2.put(Constants.Name.Y, Float.valueOf(WXViewUtils.getWebPxByWidth((float) bounds.top, instance.getInstanceViewPortWidth())));
            newHashMapWithExpectedSize2.put("width", Float.valueOf(WXViewUtils.getWebPxByWidth((float) bounds.width(), instance.getInstanceViewPortWidth())));
            newHashMapWithExpectedSize2.put("height", Float.valueOf(WXViewUtils.getWebPxByWidth((float) bounds.height(), instance.getInstanceViewPortWidth())));
        } else if (this.mTargetComponent.getHostView() != null) {
            int[] iArr = new int[2];
            this.mTargetComponent.getHostView().getLocationOnScreen(iArr);
            newHashMapWithExpectedSize2.put(Constants.Name.X, Float.valueOf(WXViewUtils.getWebPxByWidth((float) iArr[0], instance.getInstanceViewPortWidth())));
            newHashMapWithExpectedSize2.put(Constants.Name.Y, Float.valueOf(WXViewUtils.getWebPxByWidth((float) iArr[1], instance.getInstanceViewPortWidth())));
            newHashMapWithExpectedSize2.put("width", Float.valueOf(WXViewUtils.getWebPxByWidth(this.mTargetComponent.getLayoutWidth(), instance.getInstanceViewPortWidth())));
            newHashMapWithExpectedSize2.put("height", Float.valueOf(WXViewUtils.getWebPxByWidth(this.mTargetComponent.getLayoutHeight(), instance.getInstanceViewPortWidth())));
        }
        newHashMapWithExpectedSize.put("position", newHashMapWithExpectedSize2);
        this.mTargetComponent.fireEvent("click", newHashMapWithExpectedSize);
        return true;
    }

    public static boolean isGestureEvent(@NonNull String str) {
        for (WXGestureType.LowLevelGesture obj : WXGestureType.LowLevelGesture.values()) {
            if (str.equals(obj.toString())) {
                return true;
            }
        }
        for (WXGestureType.HighLevelGesture obj2 : WXGestureType.HighLevelGesture.values()) {
            if (str.equals(obj2.toString())) {
                return true;
            }
        }
        return Constants.Event.STOP_PROPAGATION.equals(str) || "click".equals(str);
    }
}
