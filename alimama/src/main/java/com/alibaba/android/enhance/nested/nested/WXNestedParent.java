package com.alibaba.android.enhance.nested.nested;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.alibaba.android.enhance.nested.nested.WXNestedHeader;
import com.alibaba.android.enhance.nested.overscroll.WXNestedOverScrollCreator;
import com.alibaba.android.enhance.nested.overscroll.WXNestedOverScrollHelper;
import com.alibaba.android.enhance.nested.overscroll.WXNestedOverScrollLayout;
import com.google.android.material.appbar.AppBarLayout;
import com.taobao.weex.WXEnvironment;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXComponentProp;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class WXNestedParent extends WXVContainer<WXCoordinatorLayout> {
    private static final String ATTR_FORCE_SCROLL = "forceScroll";
    private static final String ATTR_HEAD_OFFSET = "offset";
    private static final String ATTR_HEAD_PASS_FLING_ENABLED = "headerPassFling";
    private static final String ATTR_HEAD_QUICK_RETURN = "quickReturn";
    private static final String ATTR_HEAD_SNAP_ENABLED = "snap";
    private static final String ATTR_OVER_SCROLL_ENABLED = "overScrollEnabled";
    private static final String TAG = "WXNestedParent";
    private WeakReference<WXVContainer> mActiveChildRef = null;
    private boolean mForceScroll = false;
    private int mHeaderHeight = 0;
    private float mHeaderOffset = 0.0f;
    private boolean mHeaderPassFling = false;
    private boolean mHeaderQuickReturnEnabled = false;
    private boolean mHeaderSnapEnabled = false;
    private AppBarLayout mHeaderView;
    private final WXNestedOverScrollHelper overScrollHelper;

    public WXNestedParent(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        this.overScrollHelper = new WXNestedOverScrollHelper(wXSDKInstance);
    }

    /* access modifiers changed from: protected */
    public WXCoordinatorLayout initComponentHostView(@NonNull Context context) {
        WXCoordinatorLayout wXCoordinatorLayout = new WXCoordinatorLayout(context);
        wXCoordinatorLayout.holdComponent(this);
        return wXCoordinatorLayout;
    }

    public void addSubView(View view, int i) {
        if (view != null && getRealView() != null) {
            ViewGroup realView = getRealView();
            int childCount = realView.getChildCount();
            if (childCount == 0) {
                if (view instanceof AppBarLayout) {
                    this.mHeaderView = (AppBarLayout) view;
                    CoordinatorLayout.LayoutParams layoutParams = new CoordinatorLayout.LayoutParams(-1, -2);
                    setHeadBehavior(layoutParams);
                    setSnapEnabled(this.mHeaderSnapEnabled);
                    setQuickReturn(this.mHeaderQuickReturnEnabled);
                    realView.addView(view, layoutParams);
                    setForceScroll(this.mForceScroll);
                } else if (WXEnvironment.isApkDebugable()) {
                    WXLogUtils.e(TAG, "only support nested-header to be the first child");
                    throw new UnsupportedOperationException("only support nested-header to be the first child");
                }
            } else if (childCount == 1) {
                WXNestedOverScrollLayout createContainerView = WXNestedOverScrollCreator.createContainerView(getContext(), this);
                createContainerView.setOverScrollHelper(this.overScrollHelper);
                CoordinatorLayout.LayoutParams layoutParams2 = new CoordinatorLayout.LayoutParams(-1, -1);
                layoutParams2.setBehavior(new AppBarLayout.ScrollingViewBehavior());
                realView.addView(createContainerView, layoutParams2);
                createContainerView.addView(view);
            } else if (WXEnvironment.isApkDebugable()) {
                WXLogUtils.e(TAG, "nested-parent's child count must not greater than 2");
                throw new UnsupportedOperationException("nested-parent's child count must not greater than 2");
            }
        }
    }

    public ViewGroup.LayoutParams getChildLayoutParams(WXComponent wXComponent, View view, int i, int i2, int i3, int i4, int i5, int i6) {
        boolean z = wXComponent instanceof WXNestedHeader;
        if (z) {
            this.mHeaderHeight = i2;
        }
        ViewGroup.LayoutParams layoutParams = null;
        if (view != null) {
            layoutParams = view.getLayoutParams();
        }
        if (layoutParams == null) {
            return new ViewGroup.LayoutParams(i, i2);
        }
        if (!z) {
            if (this.mHeaderHeight != 0) {
                i5 -= this.mHeaderHeight;
                float layoutHeight = wXComponent.getLayoutHeight();
                if (!(layoutHeight == ((float) i2) || layoutHeight == 0.0f)) {
                    i2 = (int) layoutHeight;
                }
            } else {
                i5 = 0;
            }
        }
        layoutParams.width = i;
        layoutParams.height = i2;
        if (!(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
            return layoutParams;
        }
        ((ViewGroup.MarginLayoutParams) layoutParams).setMargins(i3, i5, i4, i6);
        return layoutParams;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Can't fix incorrect switch cases order */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean setProperty(java.lang.String r4, java.lang.Object r5) {
        /*
            r3 = this;
            int r0 = r4.hashCode()
            r1 = 0
            r2 = 1
            switch(r0) {
                case -1019779949: goto L_0x0032;
                case -475709032: goto L_0x0028;
                case -432285888: goto L_0x001e;
                case 3534794: goto L_0x0014;
                case 144140669: goto L_0x000a;
                default: goto L_0x0009;
            }
        L_0x0009:
            goto L_0x003c
        L_0x000a:
            java.lang.String r0 = "quickReturn"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x003c
            r0 = 2
            goto L_0x003d
        L_0x0014:
            java.lang.String r0 = "snap"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x003c
            r0 = 1
            goto L_0x003d
        L_0x001e:
            java.lang.String r0 = "overScrollEnabled"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x003c
            r0 = 4
            goto L_0x003d
        L_0x0028:
            java.lang.String r0 = "forceScroll"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x003c
            r0 = 3
            goto L_0x003d
        L_0x0032:
            java.lang.String r0 = "offset"
            boolean r0 = r4.equals(r0)
            if (r0 == 0) goto L_0x003c
            r0 = 0
            goto L_0x003d
        L_0x003c:
            r0 = -1
        L_0x003d:
            switch(r0) {
                case 0: goto L_0x0087;
                case 1: goto L_0x0077;
                case 2: goto L_0x0067;
                case 3: goto L_0x0057;
                case 4: goto L_0x0045;
                default: goto L_0x0040;
            }
        L_0x0040:
            boolean r4 = super.setProperty(r4, r5)
            return r4
        L_0x0045:
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r1)
            java.lang.Boolean r4 = com.taobao.weex.utils.WXUtils.getBoolean(r5, r4)
            boolean r4 = r4.booleanValue()
            com.alibaba.android.enhance.nested.overscroll.WXNestedOverScrollHelper r5 = r3.overScrollHelper
            r5.setOverScrollProperty(r4)
            return r2
        L_0x0057:
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r1)
            java.lang.Boolean r4 = com.taobao.weex.utils.WXUtils.getBoolean(r5, r4)
            boolean r4 = r4.booleanValue()
            r3.setForceScroll(r4)
            return r2
        L_0x0067:
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r1)
            java.lang.Boolean r4 = com.taobao.weex.utils.WXUtils.getBoolean(r5, r4)
            boolean r4 = r4.booleanValue()
            r3.setQuickReturn(r4)
            return r2
        L_0x0077:
            java.lang.Boolean r4 = java.lang.Boolean.valueOf(r1)
            java.lang.Boolean r4 = com.taobao.weex.utils.WXUtils.getBoolean(r5, r4)
            boolean r4 = r4.booleanValue()
            r3.setSnapEnabled(r4)
            return r2
        L_0x0087:
            r4 = 0
            java.lang.Float r4 = com.taobao.weex.utils.WXUtils.getFloat(r5, r4)
            if (r4 == 0) goto L_0x0095
            float r4 = r4.floatValue()
            r3.setHeadOffset(r4)
        L_0x0095:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.enhance.nested.nested.WXNestedParent.setProperty(java.lang.String, java.lang.Object):boolean");
    }

    @WXComponentProp(name = "overScrollEnabled")
    public void setOverScrollEnabled(boolean z) {
        this.overScrollHelper.setOverScrollProperty(z);
    }

    @WXComponentProp(name = "snap")
    public void setSnapEnabled(boolean z) {
        WXComponent child;
        this.mHeaderSnapEnabled = z;
        if (getChildCount() > 0 && (child = getChild(0)) != null && (child instanceof WXNestedHeader)) {
            ((WXNestedHeader) child).setSnapEnabled(z);
        }
    }

    @WXComponentProp(name = "quickReturn")
    public void setQuickReturn(boolean z) {
        WXComponent child;
        this.mHeaderQuickReturnEnabled = z;
        if (getChildCount() > 0 && (child = getChild(0)) != null && (child instanceof WXNestedHeader)) {
            ((WXNestedHeader) child).setQuickReturn(z);
        }
    }

    @WXComponentProp(name = "offset")
    public void setHeadOffset(float f) {
        this.mHeaderOffset = f;
        if (this.mHeaderView != null) {
            setHeadOffsetInternal(f);
        }
    }

    @WXComponentProp(name = "forceScroll")
    public void setForceScroll(boolean z) {
        this.mForceScroll = z;
        setHeadDragCallbackOrNull(z ? new AppBarLayout.Behavior.DragCallback() {
            public boolean canDrag(@NonNull AppBarLayout appBarLayout) {
                return true;
            }
        } : null);
    }

    @WXComponentProp(name = "headerPassFling")
    public void setHeaderPassFling(boolean z) {
        this.mHeaderPassFling = z;
    }

    private void setHeadDragCallbackOrNull(@Nullable AppBarLayout.Behavior.DragCallback dragCallback) {
        WXComponent child;
        AppBarLayout appBarLayout;
        ViewGroup.LayoutParams layoutParams;
        CoordinatorLayout.Behavior behavior;
        if (getChildCount() > 0 && (child = getChild(0)) != null && (child instanceof WXNestedHeader) && (appBarLayout = (AppBarLayout) ((WXNestedHeader) child).getHostView()) != null && (layoutParams = appBarLayout.getLayoutParams()) != null && (layoutParams instanceof CoordinatorLayout.LayoutParams) && (behavior = ((CoordinatorLayout.LayoutParams) layoutParams).getBehavior()) != null && (behavior instanceof AppBarLayout.Behavior)) {
            ((AppBarLayout.Behavior) behavior).setDragCallback(dragCallback);
        }
    }

    /* access modifiers changed from: package-private */
    public void notifyOnNestedChildScrolling(int i, int i2, int i3) {
        if (shouldNotifyWhenNestedChildScrolling()) {
            int instanceViewPortWidth = getInstance().getInstanceViewPortWidth();
            HashMap hashMap = new HashMap(2);
            hashMap.put(Constants.Name.X, 0);
            hashMap.put(Constants.Name.Y, Float.valueOf(-WXViewUtils.getWebPxByWidth((float) i3, instanceViewPortWidth)));
            HashMap hashMap2 = new HashMap(2);
            hashMap2.put("width", Float.valueOf(WXViewUtils.getWebPxByWidth((float) i, instanceViewPortWidth)));
            hashMap2.put("height", Float.valueOf(WXViewUtils.getWebPxByWidth((float) i2, instanceViewPortWidth)));
            HashMap hashMap3 = new HashMap(2);
            hashMap3.put(Constants.Name.CONTENT_SIZE, hashMap2);
            hashMap3.put(Constants.Name.CONTENT_OFFSET, hashMap);
            fireEvent("nestedscroll", hashMap3);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean shouldNotifyWhenNestedChildScrolling() {
        return getEvents().contains("nestedscroll");
    }

    /* access modifiers changed from: package-private */
    public boolean shouldDisableHackFlingAnimation() {
        return WXUtils.getBoolean(getAttrs().get("disableHackFlingAnimation"), false).booleanValue();
    }

    public void holdActiveChild(WXVContainer wXVContainer) {
        this.mActiveChildRef = new WeakReference<>(wXVContainer);
    }

    @JSMethod
    public void scrollToTop(Map<String, String> map) {
        WXNestedHelper.scrollToTop((CoordinatorLayout) getHostView(), this.mActiveChildRef != null ? (WXVContainer) this.mActiveChildRef.get() : null, map);
    }

    /* access modifiers changed from: protected */
    public void setHeadBehavior(@NonNull CoordinatorLayout.LayoutParams layoutParams) {
        layoutParams.setBehavior(new WXNestedHeader.FlingBehavior(this.mHeaderPassFling));
        setHeadOffsetInternal(this.mHeaderOffset);
    }

    private void setHeadOffsetInternal(float f) {
        WXComponent child;
        float max = Math.max(f, 0.0f);
        if (getChildCount() > 0 && (child = getChild(0)) != null && (child instanceof WXNestedHeader)) {
            WXNestedHeader wXNestedHeader = (WXNestedHeader) child;
            if (wXNestedHeader.getHostView() != null && this.mHeaderView != null && ((AppBarLayout) wXNestedHeader.getHostView()).equals(this.mHeaderView)) {
                int realSubPxByWidth = (int) WXViewUtils.getRealSubPxByWidth(max, getInstance().getInstanceViewPortWidth());
                if (wXNestedHeader.getRealView() != null) {
                    wXNestedHeader.getRealView().setMinimumHeight(realSubPxByWidth);
                }
            }
        }
    }
}
