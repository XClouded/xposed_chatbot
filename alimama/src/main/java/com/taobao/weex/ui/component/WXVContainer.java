package com.taobao.weex.ui.component;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.common.Constants;
import com.taobao.weex.dom.CSSShorthand;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.view.WXImageView;
import com.taobao.weex.utils.WXLogUtils;
import com.taobao.weex.utils.WXUtils;
import com.taobao.weex.utils.WXViewUtils;
import java.util.ArrayList;
import java.util.Iterator;

public abstract class WXVContainer<T extends ViewGroup> extends WXComponent<T> {
    private static final String TAG = "WXVContainer";
    private WXVContainer<T>.BoxShadowHost mBoxShadowHost;
    protected ArrayList<WXComponent> mChildren;

    public void appendTreeCreateFinish() {
    }

    /* access modifiers changed from: protected */
    public int getChildrenLayoutTopOffset() {
        return 0;
    }

    @Deprecated
    public WXVContainer(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, String str, boolean z, BasicComponentData basicComponentData) {
        this(wXSDKInstance, wXVContainer, z, basicComponentData);
    }

    @Deprecated
    public WXVContainer(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, boolean z, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        this.mChildren = new ArrayList<>();
    }

    public WXVContainer(WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        this.mChildren = new ArrayList<>();
    }

    public void interceptFocus() {
        ViewGroup viewGroup = (ViewGroup) getHostView();
        if (viewGroup != null) {
            viewGroup.setFocusable(true);
            viewGroup.setFocusableInTouchMode(true);
            viewGroup.setDescendantFocusability(131072);
            viewGroup.requestFocus();
        }
    }

    public void ignoreFocus() {
        ViewGroup viewGroup = (ViewGroup) getHostView();
        if (viewGroup != null) {
            viewGroup.setFocusable(false);
            viewGroup.setFocusableInTouchMode(false);
            viewGroup.clearFocus();
        }
    }

    @Deprecated
    public ViewGroup getView() {
        return (ViewGroup) getHostView();
    }

    public void applyLayoutAndEvent(WXComponent wXComponent) {
        if (!isLazy()) {
            if (wXComponent == null) {
                wXComponent = this;
            }
            super.applyLayoutAndEvent(wXComponent);
            int childCount = childCount();
            for (int i = 0; i < childCount; i++) {
                getChild(i).applyLayoutAndEvent(((WXVContainer) wXComponent).getChild(i));
            }
        }
    }

    public ViewGroup.LayoutParams getChildLayoutParams(WXComponent wXComponent, View view, int i, int i2, int i3, int i4, int i5, int i6) {
        ViewGroup.LayoutParams layoutParams = view != null ? view.getLayoutParams() : null;
        if (layoutParams == null) {
            return new ViewGroup.LayoutParams(i, i2);
        }
        layoutParams.width = i;
        layoutParams.height = i2;
        if (!(layoutParams instanceof ViewGroup.MarginLayoutParams)) {
            return layoutParams;
        }
        setMarginsSupportRTL((ViewGroup.MarginLayoutParams) layoutParams, i3, i5, i4, i6);
        return layoutParams;
    }

    public Scrollable getFirstScroller() {
        if (this instanceof Scrollable) {
            return (Scrollable) this;
        }
        for (int i = 0; i < getChildCount(); i++) {
            Scrollable firstScroller = getChild(i).getFirstScroller();
            if (firstScroller != null) {
                return firstScroller;
            }
        }
        return null;
    }

    public void bindData(WXComponent wXComponent) {
        if (!isLazy()) {
            if (wXComponent == null) {
                wXComponent = this;
            }
            super.bindData(wXComponent);
            int childCount = childCount();
            for (int i = 0; i < childCount; i++) {
                getChild(i).bindData(((WXVContainer) wXComponent).getChild(i));
            }
        }
    }

    public void refreshData(WXComponent wXComponent) {
        if (wXComponent == null) {
            wXComponent = this;
        }
        super.refreshData(wXComponent);
        int childCount = childCount();
        for (int i = 0; i < childCount; i++) {
            getChild(i).refreshData(((WXVContainer) wXComponent).getChild(i));
        }
    }

    public ViewGroup getRealView() {
        return (ViewGroup) super.getRealView();
    }

    public void createViewImpl() {
        super.createViewImpl();
        int childCount = childCount();
        for (int i = 0; i < childCount; i++) {
            createChildViewAt(i);
        }
        if (getHostView() != null) {
            ((ViewGroup) getHostView()).setClipToPadding(false);
        }
    }

    public void destroy() {
        if (this.mChildren != null) {
            int size = this.mChildren.size();
            for (int i = 0; i < size; i++) {
                this.mChildren.get(i).destroy();
            }
            this.mChildren.clear();
        }
        super.destroy();
    }

    public void recycled() {
        if (this.mChildren != null && !isFixed() && getAttrs().canRecycled()) {
            int size = this.mChildren.size();
            for (int i = 0; i < size; i++) {
                this.mChildren.get(i).recycled();
            }
        }
        super.recycled();
    }

    public View detachViewAndClearPreInfo() {
        View detachViewAndClearPreInfo = super.detachViewAndClearPreInfo();
        if (this.mChildren != null) {
            int childCount = childCount();
            for (int i = 0; i < childCount; i++) {
                this.mChildren.get(i).detachViewAndClearPreInfo();
            }
        }
        return detachViewAndClearPreInfo;
    }

    public int childCount() {
        if (this.mChildren == null) {
            return 0;
        }
        return this.mChildren.size();
    }

    public WXComponent getChild(int i) {
        if (this.mChildren == null || i < 0 || i >= this.mChildren.size()) {
            return null;
        }
        return this.mChildren.get(i);
    }

    public int getChildCount() {
        return childCount();
    }

    public void addChild(WXComponent wXComponent) {
        addChild(wXComponent, -1);
    }

    public void addChild(WXComponent wXComponent, int i) {
        if (wXComponent != null && i >= -1) {
            wXComponent.mDeepInComponentTree = this.mDeepInComponentTree + 1;
            getInstance().setMaxDomDeep(wXComponent.mDeepInComponentTree);
            if (i >= this.mChildren.size()) {
                i = -1;
            }
            if (i == -1) {
                this.mChildren.add(wXComponent);
            } else {
                this.mChildren.add(i, wXComponent);
            }
        }
    }

    public final int indexOf(WXComponent wXComponent) {
        return this.mChildren.indexOf(wXComponent);
    }

    public void createChildViewAt(int i) {
        Pair<WXComponent, Integer> rearrangeIndexAndGetChild = rearrangeIndexAndGetChild(i);
        if (rearrangeIndexAndGetChild.first != null) {
            WXComponent wXComponent = (WXComponent) rearrangeIndexAndGetChild.first;
            wXComponent.createView();
            if (!wXComponent.isVirtualComponent()) {
                addSubView(wXComponent.getHostView(), ((Integer) rearrangeIndexAndGetChild.second).intValue());
            }
        }
    }

    /* access modifiers changed from: protected */
    public Pair<WXComponent, Integer> rearrangeIndexAndGetChild(int i) {
        if (i < 0) {
            i = childCount() - 1;
        }
        if (i < 0) {
            return new Pair<>((Object) null, Integer.valueOf(i));
        }
        return new Pair<>(getChild(i), Integer.valueOf(i));
    }

    public void addSubView(View view, int i) {
        if (view != null && getRealView() != null) {
            if (i >= getRealView().getChildCount()) {
                i = -1;
            }
            if (i == -1) {
                getRealView().addView(view);
            } else {
                getRealView().addView(view, i);
            }
            WXSDKInstance instance = getInstance();
            if (instance != null) {
                instance.getApmForInstance().hasAddView = true;
            }
        }
    }

    public void remove(WXComponent wXComponent, boolean z) {
        if (wXComponent != null && this.mChildren != null && this.mChildren.size() != 0) {
            this.mChildren.remove(wXComponent);
            if (getInstance() != null && getInstance().getRootView() != null && wXComponent.isFixed()) {
                getInstance().removeFixedView(wXComponent.getHostView());
            } else if (getRealView() != null) {
                if (!wXComponent.isVirtualComponent()) {
                    ViewParent viewParent = null;
                    if ((wXComponent.getParent() instanceof WXScroller) && wXComponent.getHostView() != null) {
                        viewParent = wXComponent.getHostView().getParent();
                    }
                    if (viewParent == null || !(viewParent instanceof ViewGroup)) {
                        getRealView().removeView(wXComponent.getHostView());
                    } else {
                        ((ViewGroup) viewParent).removeView(wXComponent.getHostView());
                    }
                } else {
                    wXComponent.removeVirtualComponent();
                }
            }
            if (z) {
                wXComponent.destroy();
            }
        }
    }

    public void notifyAppearStateChange(String str, String str2) {
        super.notifyAppearStateChange(str, str2);
        if (getHostView() != null && this.mChildren != null) {
            Iterator<WXComponent> it = this.mChildren.iterator();
            while (it.hasNext()) {
                WXComponent next = it.next();
                if (!(next.getHostView() == null || next.getHostView().getVisibility() == 0)) {
                    str = Constants.Event.DISAPPEAR;
                }
                next.notifyAppearStateChange(str, str2);
            }
        }
    }

    public void onActivityCreate() {
        super.onActivityCreate();
        int childCount = childCount();
        for (int i = 0; i < childCount; i++) {
            getChild(i).onActivityCreate();
        }
    }

    public void onActivityStart() {
        super.onActivityStart();
        int childCount = childCount();
        for (int i = 0; i < childCount; i++) {
            getChild(i).onActivityStart();
        }
    }

    public void onActivityPause() {
        super.onActivityPause();
        int childCount = childCount();
        for (int i = 0; i < childCount; i++) {
            getChild(i).onActivityPause();
        }
    }

    public void onActivityResume() {
        super.onActivityResume();
        int childCount = childCount();
        for (int i = 0; i < childCount; i++) {
            getChild(i).onActivityResume();
        }
    }

    public void onActivityStop() {
        super.onActivityStop();
        int childCount = childCount();
        for (int i = 0; i < childCount; i++) {
            getChild(i).onActivityStop();
        }
    }

    public void onActivityDestroy() {
        super.onActivityDestroy();
        int childCount = childCount();
        for (int i = 0; i < childCount; i++) {
            getChild(i).onActivityDestroy();
        }
    }

    public boolean onActivityBack() {
        super.onActivityBack();
        int childCount = childCount();
        for (int i = 0; i < childCount; i++) {
            getChild(i).onActivityBack();
        }
        return false;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        int childCount = childCount();
        for (int i3 = 0; i3 < childCount; i3++) {
            getChild(i3).onActivityResult(i, i2, intent);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        int childCount = childCount();
        for (int i = 0; i < childCount; i++) {
            getChild(i).onCreateOptionsMenu(menu);
        }
        return false;
    }

    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        int childCount = childCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            getChild(i2).onRequestPermissionsResult(i, strArr, iArr);
        }
    }

    public void onRenderFinish(int i) {
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            WXComponent child = getChild(i2);
            child.mTraceInfo.uiQueueTime = this.mTraceInfo.uiQueueTime;
            child.onRenderFinish(i);
        }
        super.onRenderFinish(i);
    }

    @JSMethod
    public void releaseImageList(String str) {
        if (getHostView() != null && ViewCompat.isAttachedToWindow(getHostView()) && (getHostView() instanceof ViewGroup)) {
            if (WXUtils.getBoolean(str, false).booleanValue()) {
                doViewTreeRecycleImageView((ViewGroup) getHostView(), true);
                return;
            }
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                WXComponent child = getChild(i);
                if ((child instanceof WXImage) && (((WXImage) child).getHostView() instanceof WXImageView)) {
                    WXImageView wXImageView = (WXImageView) child.getHostView();
                    if (wXImageView != null && ViewCompat.isAttachedToWindow(wXImageView)) {
                        wXImageView.autoReleaseImage();
                    }
                } else if (child instanceof WXVContainer) {
                    ((WXVContainer) child).releaseImageList(str);
                }
            }
        }
    }

    @JSMethod
    public void recoverImageList(String str) {
        if (getHostView() != null && ViewCompat.isAttachedToWindow(getHostView()) && (getHostView() instanceof ViewGroup)) {
            if (WXUtils.getBoolean(str, false).booleanValue()) {
                doViewTreeRecycleImageView((ViewGroup) getHostView(), false);
                return;
            }
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                WXComponent child = getChild(i);
                if ((child instanceof WXImage) && (((WXImage) child).getHostView() instanceof WXImageView)) {
                    WXImageView wXImageView = (WXImageView) child.getHostView();
                    if (wXImageView != null && ViewCompat.isAttachedToWindow(wXImageView)) {
                        wXImageView.autoRecoverImage();
                    }
                } else if (child instanceof WXVContainer) {
                    ((WXVContainer) child).recoverImageList(str);
                }
            }
        }
    }

    private void doViewTreeRecycleImageView(ViewGroup viewGroup, boolean z) {
        int childCount = viewGroup.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childAt = viewGroup.getChildAt(i);
            if (childAt instanceof WXImageView) {
                if (z) {
                    ((WXImageView) childAt).autoReleaseImage();
                } else {
                    ((WXImageView) childAt).autoRecoverImage();
                }
            } else if (childAt instanceof ViewGroup) {
                doViewTreeRecycleImageView((ViewGroup) childAt, z);
            }
        }
    }

    public void requestDisallowInterceptTouchEvent(boolean z) {
        if (this.mGesture != null) {
            if (!this.mGesture.isRequestDisallowInterceptTouchEvent()) {
                this.mGesture.setRequestDisallowInterceptTouchEvent(z);
            } else {
                return;
            }
        }
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(z);
        }
    }

    @Nullable
    public View getBoxShadowHost(boolean z) {
        if (z) {
            return this.mBoxShadowHost;
        }
        ViewGroup viewGroup = (ViewGroup) getHostView();
        if (viewGroup == null) {
            return null;
        }
        try {
            if (WXBasicComponentType.DIV.equals(getComponentType())) {
                WXLogUtils.d("BoxShadow", "Draw box-shadow with BoxShadowHost on div: " + toString());
                if (this.mBoxShadowHost == null) {
                    this.mBoxShadowHost = new BoxShadowHost(getContext());
                    WXViewUtils.setBackGround(this.mBoxShadowHost, (Drawable) null, this);
                    viewGroup.addView(this.mBoxShadowHost);
                }
                CSSShorthand padding = getPadding();
                CSSShorthand border = getBorder();
                ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(viewGroup.getLayoutParams());
                setMarginsSupportRTL(marginLayoutParams, -((int) (padding.get(CSSShorthand.EDGE.LEFT) + border.get(CSSShorthand.EDGE.LEFT))), -((int) (padding.get(CSSShorthand.EDGE.TOP) + border.get(CSSShorthand.EDGE.TOP))), -((int) (padding.get(CSSShorthand.EDGE.RIGHT) + border.get(CSSShorthand.EDGE.RIGHT))), -((int) (padding.get(CSSShorthand.EDGE.BOTTOM) + border.get(CSSShorthand.EDGE.BOTTOM))));
                this.mBoxShadowHost.setLayoutParams(marginLayoutParams);
                viewGroup.removeView(this.mBoxShadowHost);
                viewGroup.addView(this.mBoxShadowHost);
                return this.mBoxShadowHost;
            }
        } catch (Throwable th) {
            WXLogUtils.w("BoxShadow", th);
        }
        return viewGroup;
    }

    private class BoxShadowHost extends View {
        public BoxShadowHost(Context context) {
            super(context);
        }
    }
}
