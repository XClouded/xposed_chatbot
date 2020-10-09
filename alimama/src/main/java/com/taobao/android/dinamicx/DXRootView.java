package com.taobao.android.dinamicx;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import androidx.annotation.NonNull;
import com.alibaba.fastjson.JSONObject;
import com.taobao.android.dinamicx.bindingx.DXBindingXManager;
import com.taobao.android.dinamicx.exception.DXExceptionUtil;
import com.taobao.android.dinamicx.expression.event.DXMsgCenterEvent;
import com.taobao.android.dinamicx.expression.event.DXViewAppearEvent;
import com.taobao.android.dinamicx.expression.event.DXViewDisappearEvent;
import com.taobao.android.dinamicx.monitor.DXAppMonitor;
import com.taobao.android.dinamicx.monitor.DXMonitorConstant;
import com.taobao.android.dinamicx.template.download.DXTemplateItem;
import com.taobao.android.dinamicx.template.loader.binary.DXHashConstant;
import com.taobao.android.dinamicx.view.DXNativeFrameLayout;
import com.taobao.android.dinamicx.widget.DXWidgetNode;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class DXRootView extends DXNativeFrameLayout {
    List<DXWidgetNode> animationWidgets;
    WeakReference<DXBindingXManager> bindingXManagerWeakReference;
    JSONObject data;
    DXTemplateItem dxTemplateItem;
    int parentHeightSpec;
    int parentWidthSpec;
    private int position;
    DXRootViewLifeCycle rootViewLifeCycle;

    DXRootView(@NonNull Context context) {
        super(context);
    }

    DXRootView(@NonNull Context context, DXWidgetNode dXWidgetNode) {
        super(context);
        setExpandWidgetNode(dXWidgetNode);
    }

    public void setMeasureDimension(int i, int i2) {
        setMeasuredDimension(i, i2);
    }

    public DXTemplateItem getDxTemplateItem() {
        return this.dxTemplateItem;
    }

    public JSONObject getData() {
        return this.data;
    }

    public int getPosition() {
        return this.position;
    }

    public void setPosition(int i) {
        this.position = i;
    }

    public DXWidgetNode getExpandWidgetNode() {
        return (DXWidgetNode) getTag(DXPublicConstant.TAG_EXPANDED_WIDGET_ON_VIEW);
    }

    /* access modifiers changed from: package-private */
    public void setExpandWidgetNode(DXWidgetNode dXWidgetNode) {
        setTag(DXPublicConstant.TAG_EXPANDED_WIDGET_ON_VIEW, dXWidgetNode);
    }

    public DXWidgetNode getFlattenWidgetNode() {
        return (DXWidgetNode) getTag(DXWidgetNode.TAG_WIDGET_NODE);
    }

    /* access modifiers changed from: package-private */
    public void setFlattenWidgetNode(DXWidgetNode dXWidgetNode) {
        setTag(DXWidgetNode.TAG_WIDGET_NODE, dXWidgetNode);
    }

    public DXBindingXManager getBindingXManager() {
        if (this.bindingXManagerWeakReference == null) {
            return null;
        }
        return (DXBindingXManager) this.bindingXManagerWeakReference.get();
    }

    public void setBindingXManagerWeakReference(DXBindingXManager dXBindingXManager) {
        this.bindingXManagerWeakReference = new WeakReference<>(dXBindingXManager);
    }

    public void postMessage(Object obj) {
        JSONObject jSONObject;
        try {
            if (obj instanceof JSONObject) {
                JSONObject jSONObject2 = (JSONObject) obj;
                String string = jSONObject2.getString("type");
                if (!DXMsgConstant.DX_MSG_TYPE_BNDX.equalsIgnoreCase(string) || getBindingXManager() == null) {
                    DXWidgetNode expandWidgetNode = getExpandWidgetNode();
                    if (expandWidgetNode != null && (jSONObject = jSONObject2.getJSONObject("params")) != null) {
                        String string2 = jSONObject.getString(DXMsgConstant.DX_MSG_TARGET_ID);
                        DXMsgCenterEvent dXMsgCenterEvent = new DXMsgCenterEvent(DXHashConstant.DX_VIEW_EVENT_ON_MSG_CENTER_EVENT);
                        dXMsgCenterEvent.setParams(jSONObject);
                        dXMsgCenterEvent.setTargetId(string2);
                        dXMsgCenterEvent.setType(string);
                        DXWidgetNode queryWidgetNodeByUserId = expandWidgetNode.queryWidgetNodeByUserId(string2);
                        if (queryWidgetNodeByUserId == null || queryWidgetNodeByUserId.getReferenceNode() == null) {
                            expandWidgetNode.sendBroadcastEvent(dXMsgCenterEvent);
                        } else {
                            queryWidgetNodeByUserId.postEvent(dXMsgCenterEvent);
                        }
                    }
                } else {
                    getBindingXManager().processDXMsg(this, jSONObject2);
                }
            }
        } catch (Throwable th) {
            DXExceptionUtil.printStack(th);
            String str = null;
            if (getBindingXManager() != null) {
                str = getBindingXManager().bizType;
            }
            if (TextUtils.isEmpty(str)) {
                str = "dinamicx";
            }
            DXAppMonitor.trackerError(str, (DXTemplateItem) null, DXMonitorConstant.DX_MONITOR_BINDINGX, DXMonitorConstant.DX_BINDINGX_CRASH, DXError.BINDINGX_POST_MSG_CRASH, DXExceptionUtil.getStackTrace(th));
        }
    }

    /* access modifiers changed from: package-private */
    public void onRootViewAppear(int i) {
        DXViewAppearEvent dXViewAppearEvent = new DXViewAppearEvent(DXHashConstant.DX_VIEW_EVENT_ON_APPEAR);
        dXViewAppearEvent.setItemIndex(i);
        DXWidgetNode expandWidgetNode = getExpandWidgetNode();
        if (expandWidgetNode != null) {
            expandWidgetNode.sendBroadcastEvent(dXViewAppearEvent);
        }
    }

    /* access modifiers changed from: package-private */
    public void onRootViewDisappear(int i) {
        DXViewDisappearEvent dXViewDisappearEvent = new DXViewDisappearEvent(DXHashConstant.DX_VIEW_EVENT_ON_DISAPPEAR);
        dXViewDisappearEvent.setItemIndex(i);
        DXWidgetNode expandWidgetNode = getExpandWidgetNode();
        if (expandWidgetNode != null) {
            expandWidgetNode.sendBroadcastEvent(dXViewDisappearEvent);
        }
    }

    /* access modifiers changed from: package-private */
    public void registerDXRootViewLifeCycle(DXRootViewLifeCycle dXRootViewLifeCycle) {
        this.rootViewLifeCycle = dXRootViewLifeCycle;
    }

    public boolean hasDXRootViewLifeCycle() {
        return this.rootViewLifeCycle != null;
    }

    public void dispatchWindowVisibilityChanged(int i) {
        super.dispatchWindowVisibilityChanged(i);
        if (this.rootViewLifeCycle != null) {
            this.rootViewLifeCycle.dispatchWindowVisibilityChanged(this, i);
        }
    }

    /* access modifiers changed from: protected */
    public void onWindowVisibilityChanged(int i) {
        super.onWindowVisibilityChanged(i);
        if (this.rootViewLifeCycle != null) {
            this.rootViewLifeCycle.onWindowVisibilityChanged(this, i);
        }
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(@NonNull View view, int i) {
        super.onVisibilityChanged(view, i);
        if (this.rootViewLifeCycle != null) {
            this.rootViewLifeCycle.onVisibilityChanged(view, i);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.rootViewLifeCycle != null) {
            this.rootViewLifeCycle.onDetachedFromWindow(this);
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.rootViewLifeCycle != null) {
            this.rootViewLifeCycle.onAttachedToWindow(this);
        }
    }

    public void onStartTemporaryDetach() {
        super.onStartTemporaryDetach();
        if (this.rootViewLifeCycle != null) {
            this.rootViewLifeCycle.onStartTemporaryDetach(this);
        }
    }

    public void onFinishTemporaryDetach() {
        super.onFinishTemporaryDetach();
        if (this.rootViewLifeCycle != null) {
            this.rootViewLifeCycle.onFinishTemporaryDetach(this);
        }
    }

    public static abstract class DXRootViewLifeCycle {
        public void dispatchWindowVisibilityChanged(int i) {
        }

        /* access modifiers changed from: protected */
        public void onAttachedToWindow() {
        }

        /* access modifiers changed from: protected */
        public void onDetachedFromWindow() {
        }

        /* access modifiers changed from: protected */
        public void onFinishTemporaryDetach() {
        }

        /* access modifiers changed from: protected */
        public void onStartTemporaryDetach() {
        }

        /* access modifiers changed from: protected */
        public void onVisibilityChanged(@NonNull View view, int i) {
        }

        /* access modifiers changed from: protected */
        public void onWindowVisibilityChanged(int i) {
        }

        public void dispatchWindowVisibilityChanged(DXRootView dXRootView, int i) {
            dispatchWindowVisibilityChanged(i);
        }

        /* access modifiers changed from: protected */
        public void onWindowVisibilityChanged(DXRootView dXRootView, int i) {
            onWindowVisibilityChanged(i);
        }

        /* access modifiers changed from: protected */
        public void onDetachedFromWindow(DXRootView dXRootView) {
            onDetachedFromWindow();
        }

        /* access modifiers changed from: protected */
        public void onAttachedToWindow(DXRootView dXRootView) {
            onAttachedToWindow();
        }

        /* access modifiers changed from: protected */
        public void onStartTemporaryDetach(DXRootView dXRootView) {
            onStartTemporaryDetach();
        }

        /* access modifiers changed from: protected */
        public void onFinishTemporaryDetach(DXRootView dXRootView) {
            onFinishTemporaryDetach();
        }
    }

    public void _addAnimationWidget(DXWidgetNode dXWidgetNode) {
        if (dXWidgetNode != null) {
            if (this.animationWidgets == null) {
                this.animationWidgets = new ArrayList();
            }
            if (!this.animationWidgets.contains(dXWidgetNode)) {
                this.animationWidgets.add(dXWidgetNode);
            }
        }
    }

    public void _removeAnimationWidget(DXWidgetNode dXWidgetNode) {
        if (this.animationWidgets != null) {
            this.animationWidgets.remove(dXWidgetNode);
        }
    }

    public boolean _containAnimationWidget(DXWidgetNode dXWidgetNode) {
        if (dXWidgetNode == null || this.animationWidgets == null || this.animationWidgets.size() == 0) {
            return false;
        }
        return this.animationWidgets.contains(dXWidgetNode);
    }

    public List<DXWidgetNode> _getAnimationWidgets() {
        return this.animationWidgets;
    }

    public void _clearAnimationWidgets() {
        this.animationWidgets = new ArrayList();
    }
}
