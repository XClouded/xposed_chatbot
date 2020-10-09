package com.taobao.weex.analyzer.core.lint;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.analyzer.pojo.HealthReport;
import com.taobao.weex.analyzer.utils.SDKUtils;
import com.taobao.weex.analyzer.utils.ViewUtils;
import com.taobao.weex.dom.WXAttr;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXEmbed;
import com.taobao.weex.ui.component.WXScroller;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.component.list.WXCell;
import com.taobao.weex.ui.component.list.WXListComponent;
import com.taobao.weex.ui.view.WXFrameLayout;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

class DomTracker {
    private static final int START_LAYER_OF_REAL_DOM = 2;
    private static final int START_LAYER_OF_VDOM = 2;
    private static final String TAG = "VDomTracker";
    private Map<WXComponent, VDOMInfo> mCachedMap;
    private Deque<LayeredNode<WXComponent>> mLayeredQueue = new ArrayDeque();
    private OnTrackNodeListener mOnTrackNodeListener;
    private ObjectPool<LayeredNode<View>> mRealDomObjectPool = new ObjectPool<LayeredNode<View>>(15) {
        /* access modifiers changed from: package-private */
        public LayeredNode<View> newObject() {
            return new LayeredNode<>();
        }
    };
    private ObjectPool<LayeredNode<WXComponent>> mVDomObjectPool = new ObjectPool<LayeredNode<WXComponent>>(10) {
        /* access modifiers changed from: package-private */
        public LayeredNode<WXComponent> newObject() {
            return new LayeredNode<>();
        }
    };
    private WXSDKInstance mWxInstance;

    interface OnTrackNodeListener {
        void onTrackNode(@NonNull WXComponent wXComponent, int i);
    }

    DomTracker(@NonNull WXSDKInstance wXSDKInstance) {
        this.mWxInstance = wXSDKInstance;
    }

    /* access modifiers changed from: package-private */
    public void setOnTrackNodeListener(OnTrackNodeListener onTrackNodeListener) {
        this.mOnTrackNodeListener = onTrackNodeListener;
    }

    private VDOMInfo createNode(@NonNull LayeredNode<WXComponent> layeredNode) {
        VDOMInfo vDOMInfo = new VDOMInfo();
        vDOMInfo.simpleName = layeredNode.simpleName;
        vDOMInfo.realName = ((WXComponent) layeredNode.component).getClass().getName();
        WXAttr attrs = ((WXComponent) layeredNode.component).getAttrs();
        if (!attrs.isEmpty()) {
            vDOMInfo.attrs = Collections.unmodifiableMap(attrs);
        }
        return vDOMInfo;
    }

    /* access modifiers changed from: package-private */
    @Nullable
    public HealthReport traverse() {
        int i;
        HealthReport.ListDesc listDesc;
        long currentTimeMillis = System.currentTimeMillis();
        if (SDKUtils.isInUiThread()) {
            WXLogUtils.e(TAG, "illegal thread...");
            return null;
        }
        WXComponent rootComponent = this.mWxInstance.getRootComponent();
        if (rootComponent == null) {
            WXLogUtils.e(TAG, "god component not found");
            return null;
        }
        HealthReport healthReport = new HealthReport(this.mWxInstance.getBundleUrl());
        View hostView = rootComponent.getHostView();
        if (hostView != null) {
            healthReport.maxLayerOfRealDom = getRealDomMaxLayer(hostView);
            i = hostView.getMeasuredHeight();
        } else {
            i = 0;
        }
        LayeredNode obtain = this.mVDomObjectPool.obtain();
        obtain.set(rootComponent, ViewUtils.getComponentName(rootComponent), 2);
        this.mLayeredQueue.add(obtain);
        if (this.mCachedMap == null) {
            this.mCachedMap = new HashMap();
        }
        Map<WXComponent, VDOMInfo> map = this.mCachedMap;
        while (!this.mLayeredQueue.isEmpty()) {
            LayeredNode removeFirst = this.mLayeredQueue.removeFirst();
            WXComponent wXComponent = (WXComponent) removeFirst.component;
            int i2 = removeFirst.layer;
            VDOMInfo vDOMInfo = map.get(wXComponent);
            if (vDOMInfo == null) {
                vDOMInfo = createNode(removeFirst);
                map.put(wXComponent, vDOMInfo);
            }
            healthReport.maxLayer = Math.max(healthReport.maxLayer, i2);
            healthReport.estimateContentHeight = Math.max(healthReport.estimateContentHeight, ComponentHeightComputer.computeComponentContentHeight(wXComponent));
            if (!TextUtils.isEmpty(removeFirst.tint)) {
                for (HealthReport.EmbedDesc next : healthReport.embedDescList) {
                    if (next.src != null && next.src.equals(removeFirst.tint)) {
                        next.actualMaxLayer = Math.max(next.actualMaxLayer, i2 - next.beginLayer);
                    }
                }
            }
            if (this.mOnTrackNodeListener != null) {
                this.mOnTrackNodeListener.onTrackNode(wXComponent, i2);
            }
            if (wXComponent instanceof WXListComponent) {
                healthReport.hasList = true;
                if (healthReport.listDescMap == null) {
                    healthReport.listDescMap = new LinkedHashMap();
                }
                HealthReport.ListDesc listDesc2 = healthReport.listDescMap.get(wXComponent.getRef());
                if (listDesc2 == null) {
                    listDesc2 = new HealthReport.ListDesc();
                }
                listDesc2.ref = wXComponent.getRef();
                listDesc2.totalHeight = ComponentHeightComputer.computeComponentContentHeight(wXComponent);
                healthReport.listDescMap.put(listDesc2.ref, listDesc2);
            } else if (wXComponent instanceof WXScroller) {
                if (ViewUtils.isVerticalScroller((WXScroller) wXComponent)) {
                    healthReport.hasScroller = true;
                }
            } else if (wXComponent instanceof WXCell) {
                WXVContainer parent = wXComponent.getParent();
                if (!(parent == null || !(parent instanceof WXListComponent) || healthReport.listDescMap == null || (listDesc = healthReport.listDescMap.get(parent.getRef())) == null)) {
                    listDesc.cellNum++;
                }
                int componentNumOfNode = getComponentNumOfNode(wXComponent);
                healthReport.maxCellViewNum = Math.max(healthReport.maxCellViewNum, componentNumOfNode);
                WXCell wXCell = (WXCell) wXComponent;
                if (wXCell.getHostView() != null) {
                    healthReport.hasBigCell = isBigCell((float) ((WXFrameLayout) wXCell.getHostView()).getMeasuredHeight()) | healthReport.hasBigCell;
                    healthReport.componentNumOfBigCell = componentNumOfNode;
                }
            } else if (wXComponent instanceof WXEmbed) {
                healthReport.hasEmbed = true;
            }
            removeFirst.clear();
            this.mVDomObjectPool.recycle(removeFirst);
            if (wXComponent instanceof WXEmbed) {
                if (healthReport.embedDescList == null) {
                    healthReport.embedDescList = new ArrayList();
                }
                HealthReport.EmbedDesc embedDesc = new HealthReport.EmbedDesc();
                WXEmbed wXEmbed = (WXEmbed) wXComponent;
                embedDesc.src = wXEmbed.getSrc();
                embedDesc.beginLayer = i2;
                healthReport.embedDescList.add(embedDesc);
                WXComponent nestedRootComponent = ViewUtils.getNestedRootComponent(wXEmbed);
                if (nestedRootComponent != null) {
                    LayeredNode obtain2 = this.mVDomObjectPool.obtain();
                    obtain2.set(nestedRootComponent, ViewUtils.getComponentName(nestedRootComponent), i2 + 1);
                    this.mLayeredQueue.add(obtain2);
                    obtain2.tint = embedDesc.src;
                }
            } else if (wXComponent instanceof WXVContainer) {
                WXVContainer wXVContainer = (WXVContainer) wXComponent;
                int childCount = wXVContainer.childCount();
                int i3 = 0;
                while (i3 < childCount) {
                    WXComponent child = wXVContainer.getChild(i3);
                    LayeredNode obtain3 = this.mVDomObjectPool.obtain();
                    int i4 = childCount;
                    obtain3.set(child, ViewUtils.getComponentName(child), i2 + 1);
                    if (!TextUtils.isEmpty(removeFirst.tint)) {
                        obtain3.tint = removeFirst.tint;
                    }
                    this.mLayeredQueue.add(obtain3);
                    VDOMInfo createNode = createNode(obtain3);
                    if (vDOMInfo.children == null) {
                        vDOMInfo.children = new ArrayList();
                    }
                    vDOMInfo.children.add(createNode);
                    map.put(child, createNode);
                    i3++;
                    childCount = i4;
                }
            }
        }
        Context context = this.mWxInstance.getContext();
        if (context != null && i == 0) {
            i = ViewUtils.getScreenHeight(context);
        }
        if (i != 0) {
            Locale locale = Locale.CHINA;
            double d = (double) healthReport.estimateContentHeight;
            double d2 = (double) i;
            Double.isNaN(d);
            Double.isNaN(d2);
            healthReport.estimatePages = String.format(locale, "%.2f", new Object[]{Double.valueOf(d / d2)});
        } else {
            healthReport.estimatePages = "0";
        }
        map.clear();
        healthReport.tree = map.get(rootComponent);
        long currentTimeMillis2 = System.currentTimeMillis();
        WXLogUtils.d(TAG, "[traverse] elapse time :" + (currentTimeMillis2 - currentTimeMillis) + "ms");
        return healthReport;
    }

    private int getComponentNumOfNode(@NonNull WXComponent wXComponent) {
        ArrayDeque arrayDeque = new ArrayDeque();
        arrayDeque.add(wXComponent);
        int i = 0;
        while (!arrayDeque.isEmpty()) {
            WXComponent wXComponent2 = (WXComponent) arrayDeque.removeFirst();
            i++;
            if (wXComponent2 instanceof WXVContainer) {
                WXVContainer wXVContainer = (WXVContainer) wXComponent2;
                int childCount = wXVContainer.childCount();
                for (int i2 = 0; i2 < childCount; i2++) {
                    arrayDeque.add(wXVContainer.getChild(i2));
                }
            }
        }
        return i;
    }

    private int getRealDomMaxLayer(@NonNull View view) {
        ArrayDeque arrayDeque = new ArrayDeque();
        LayeredNode obtain = this.mRealDomObjectPool.obtain();
        obtain.set(view, (String) null, 2);
        arrayDeque.add(obtain);
        int i = 0;
        while (!arrayDeque.isEmpty()) {
            LayeredNode layeredNode = (LayeredNode) arrayDeque.removeFirst();
            i = Math.max(i, layeredNode.layer);
            View view2 = (View) layeredNode.component;
            int i2 = layeredNode.layer;
            layeredNode.clear();
            this.mRealDomObjectPool.recycle(layeredNode);
            if (view2 instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view2;
                if (viewGroup.getChildCount() > 0) {
                    int childCount = viewGroup.getChildCount();
                    for (int i3 = 0; i3 < childCount; i3++) {
                        View childAt = viewGroup.getChildAt(i3);
                        LayeredNode obtain2 = this.mRealDomObjectPool.obtain();
                        obtain2.set(childAt, (String) null, i2 + 1);
                        arrayDeque.add(obtain2);
                    }
                }
            }
        }
        return i;
    }

    private boolean isBigCell(float f) {
        if (f <= 0.0f) {
            return false;
        }
        double d = (double) f;
        double screenHeight = (double) (WXViewUtils.getScreenHeight() * 2);
        Double.isNaN(screenHeight);
        return d > screenHeight / 3.0d;
    }

    private static class LayeredNode<T> {
        T component;
        int layer;
        String simpleName;
        String tint;

        private LayeredNode() {
        }

        /* access modifiers changed from: package-private */
        public void set(T t, String str, int i) {
            this.component = t;
            this.layer = i;
            this.simpleName = str;
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            this.component = null;
            this.layer = -1;
            this.simpleName = null;
        }
    }

    private static abstract class ObjectPool<T> {
        private final Deque<T> mPool;

        /* access modifiers changed from: package-private */
        public abstract T newObject();

        ObjectPool(int i) {
            int max = Math.max(0, i);
            this.mPool = new ArrayDeque(max);
            for (int i2 = 0; i2 < max; i2++) {
                this.mPool.add(newObject());
            }
        }

        /* access modifiers changed from: package-private */
        public T obtain() {
            return this.mPool.isEmpty() ? newObject() : this.mPool.removeLast();
        }

        /* access modifiers changed from: package-private */
        public void recycle(@NonNull T t) {
            this.mPool.addLast(t);
        }
    }
}
