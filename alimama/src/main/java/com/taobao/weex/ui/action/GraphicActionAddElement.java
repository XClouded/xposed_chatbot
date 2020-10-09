package com.taobao.weex.ui.action;

import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.WorkerThread;
import androidx.collection.ArrayMap;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.WXSDKManager;
import com.taobao.weex.common.WXErrorCode;
import com.taobao.weex.dom.transition.WXTransition;
import com.taobao.weex.performance.WXAnalyzerDataTransfer;
import com.taobao.weex.performance.WXStateRecord;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXExceptionUtils;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class GraphicActionAddElement extends GraphicActionAbstractAddElement {
    private WXComponent child;
    private boolean isLayoutRTL;
    private GraphicPosition layoutPosition;
    private GraphicSize layoutSize;
    private WXVContainer parent;

    public GraphicActionAddElement(@NonNull WXSDKInstance wXSDKInstance, String str, String str2, String str3, int i, Map<String, String> map, Map<String, String> map2, Set<String> set, float[] fArr, float[] fArr2, float[] fArr3) {
        super(wXSDKInstance, str);
        this.mComponentType = str2;
        this.mParentRef = str3;
        this.mIndex = i;
        this.mStyle = map;
        this.mAttributes = map2;
        this.mEvents = set;
        this.mPaddings = fArr2;
        this.mMargins = fArr;
        this.mBorders = fArr3;
        if (wXSDKInstance.getContext() != null) {
            if (WXAnalyzerDataTransfer.isInteractionLogOpen()) {
                Log.d(WXAnalyzerDataTransfer.INTERACTION_TAG, "[client][addelementStart]" + wXSDKInstance.getInstanceId() + "," + str2 + "," + str);
            }
            try {
                this.parent = (WXVContainer) WXSDKManager.getInstance().getWXRenderManager().getWXComponent(getPageId(), this.mParentRef);
                long fixUnixTime = WXUtils.getFixUnixTime();
                this.child = createComponent(wXSDKInstance, this.parent, new BasicComponentData(str, this.mComponentType, this.mParentRef));
                this.child.setTransition(WXTransition.fromMap(this.child.getStyles(), this.child));
                wXSDKInstance.getApmForInstance().componentCreateTime += WXUtils.getFixUnixTime() - fixUnixTime;
                if (this.parent != null && this.parent.isIgnoreInteraction) {
                    this.child.isIgnoreInteraction = true;
                }
                if (!this.child.isIgnoreInteraction) {
                    Object obj = this.child.getAttrs() != null ? this.child.getAttrs().get("ignoreInteraction") : null;
                    if (!"false".equals(obj)) {
                        if (!"0".equals(obj)) {
                            if ("1".equals(obj) || "true".equals(obj) || this.child.isFixed()) {
                                this.child.isIgnoreInteraction = true;
                            }
                        }
                    }
                    this.child.isIgnoreInteraction = false;
                }
                WXStateRecord.getInstance().recordAction(wXSDKInstance.getInstanceId(), "addElement");
            } catch (ClassCastException unused) {
                ArrayMap arrayMap = new ArrayMap();
                WXComponent wXComponent = WXSDKManager.getInstance().getWXRenderManager().getWXComponent(getPageId(), this.mParentRef);
                if (this.mStyle != null && !this.mStyle.isEmpty()) {
                    arrayMap.put("child.style", this.mStyle.toString());
                }
                if (!(wXComponent == null || wXComponent.getStyles() == null || wXComponent.getStyles().isEmpty())) {
                    arrayMap.put("parent.style", wXComponent.getStyles().toString());
                }
                if (this.mAttributes != null && !this.mAttributes.isEmpty()) {
                    arrayMap.put("child.attr", this.mAttributes.toString());
                }
                if (!(wXComponent == null || wXComponent.getAttrs() == null || wXComponent.getAttrs().isEmpty())) {
                    arrayMap.put("parent.attr", wXComponent.getAttrs().toString());
                }
                if (this.mEvents != null && !this.mEvents.isEmpty()) {
                    arrayMap.put("child.event", this.mEvents.toString());
                }
                if (!(wXComponent == null || wXComponent.getEvents() == null || wXComponent.getEvents().isEmpty())) {
                    arrayMap.put("parent.event", wXComponent.getEvents().toString());
                }
                if (this.mMargins != null && this.mMargins.length > 0) {
                    arrayMap.put("child.margin", Arrays.toString(this.mMargins));
                }
                if (!(wXComponent == null || wXComponent.getMargin() == null)) {
                    arrayMap.put("parent.margin", wXComponent.getMargin().toString());
                }
                if (this.mPaddings != null && this.mPaddings.length > 0) {
                    arrayMap.put("child.padding", Arrays.toString(this.mPaddings));
                }
                if (!(wXComponent == null || wXComponent.getPadding() == null)) {
                    arrayMap.put("parent.padding", wXComponent.getPadding().toString());
                }
                if (this.mBorders != null && this.mBorders.length > 0) {
                    arrayMap.put("child.border", Arrays.toString(this.mBorders));
                }
                if (!(wXComponent == null || wXComponent.getBorder() == null)) {
                    arrayMap.put("parent.border", wXComponent.getBorder().toString());
                }
                WXExceptionUtils.commitCriticalExceptionRT(wXSDKInstance.getInstanceId(), WXErrorCode.WX_RENDER_ERR_CONTAINER_TYPE, "GraphicActionAddElement", String.format(Locale.ENGLISH, "You are trying to add a %s to a %2$s, which is illegal as %2$s is not a container", new Object[]{str2, WXSDKManager.getInstance().getWXRenderManager().getWXComponent(getPageId(), this.mParentRef).getComponentType()}), arrayMap);
            }
        }
    }

    @WorkerThread
    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public void setRTL(boolean z) {
        this.isLayoutRTL = z;
    }

    @WorkerThread
    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public void setSize(GraphicSize graphicSize) {
        this.layoutSize = graphicSize;
    }

    @WorkerThread
    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public void setPosition(GraphicPosition graphicPosition) {
        this.layoutPosition = graphicPosition;
    }

    @WorkerThread
    @RestrictTo({RestrictTo.Scope.LIBRARY})
    public void setIndex(int i) {
        this.mIndex = i;
    }

    public void executeAction() {
        super.executeAction();
        try {
            if (!TextUtils.equals(this.mComponentType, "video") && !TextUtils.equals(this.mComponentType, "videoplus")) {
                this.child.mIsAddElementToTree = true;
            }
            long fixUnixTime = WXUtils.getFixUnixTime();
            this.parent.addChild(this.child, this.mIndex);
            this.parent.createChildViewAt(this.mIndex);
            this.child.setIsLayoutRTL(this.isLayoutRTL);
            if (!(this.layoutPosition == null || this.layoutSize == null)) {
                this.child.setDemission(this.layoutSize, this.layoutPosition);
            }
            this.child.applyLayoutAndEvent(this.child);
            this.child.bindData(this.child);
            long fixUnixTime2 = WXUtils.getFixUnixTime() - fixUnixTime;
            if (getWXSDKIntance() != null) {
                getWXSDKIntance().getApmForInstance().viewCreateTime += fixUnixTime2;
            }
        } catch (Exception e) {
            WXLogUtils.e("add component failed.", (Throwable) e);
        }
    }
}
