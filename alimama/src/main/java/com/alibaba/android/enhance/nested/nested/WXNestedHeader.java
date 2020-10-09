package com.alibaba.android.enhance.nested.nested;

import android.animation.StateListAnimator;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.AppBarLayout;
import com.taobao.weex.WXSDKInstance;
import com.taobao.weex.common.Constants;
import com.taobao.weex.ui.action.BasicComponentData;
import com.taobao.weex.ui.component.WXComponent;
import com.taobao.weex.ui.component.WXVContainer;
import com.taobao.weex.ui.view.refresh.core.WXSwipeLayout;
import com.taobao.weex.utils.WXViewUtils;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class WXNestedHeader extends WXVContainer<AppBarLayout> {
    private boolean isQuickReturnEnabled = false;
    private boolean isSnapEnabled = false;
    private ViewTreeObserver.OnGlobalLayoutListener mGlobalLayoutListener;
    private AppBarLayout.LayoutParams mHeadParamsHolder;
    private ViewTreeObserver mObserver;
    private final List<OnNestedRefreshOffsetChangedListener> mOffsetChangedListeners = new LinkedList();
    private WXSwipeLayout.OnRefreshOffsetChangedListener mOnRefreshOffsetChangedListener = new WXSwipeLayout.OnRefreshOffsetChangedListener() {
        public void onOffsetChanged(final int i) {
            if (Looper.getMainLooper() != Looper.myLooper()) {
                WXNestedHeader.this.mUIHandler.post(new Runnable() {
                    public void run() {
                        WXNestedHeader.this.notifyOnRefreshOffsetChangedListener(i);
                    }
                });
            } else {
                WXNestedHeader.this.notifyOnRefreshOffsetChangedListener(i);
            }
        }
    };
    private ViewGroup mRealView;
    /* access modifiers changed from: private */
    public final Handler mUIHandler = new Handler(Looper.getMainLooper());

    public interface OnNestedRefreshOffsetChangedListener {
        void onOffsetChanged(int i);
    }

    public interface OnPullDownRefresh {
        void addOnRefreshOffsetChangedListener(WXSwipeLayout.OnRefreshOffsetChangedListener onRefreshOffsetChangedListener);
    }

    public WXNestedHeader(final WXSDKInstance wXSDKInstance, WXVContainer wXVContainer, BasicComponentData basicComponentData) {
        super(wXSDKInstance, wXVContainer, basicComponentData);
        final View decorView = ((Activity) getContext()).getWindow().getDecorView();
        if (decorView != null) {
            this.mObserver = decorView.getViewTreeObserver();
            if (this.mObserver != null) {
                this.mGlobalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                    public void onGlobalLayout() {
                        WXNestedHeader.this.findSwipeLayoutViewInWeex(decorView);
                        WXNestedHeader.this.findPullDownComponetViewInRiver(wXSDKInstance.getRootComponent());
                    }
                };
                this.mObserver.addOnGlobalLayoutListener(this.mGlobalLayoutListener);
            }
        }
    }

    /* access modifiers changed from: private */
    public void findPullDownComponetViewInRiver(WXComponent wXComponent) {
        if (wXComponent instanceof WXVContainer) {
            WXVContainer wXVContainer = (WXVContainer) wXComponent;
            for (int i = 0; i < wXVContainer.getChildCount(); i++) {
                WXComponent child = wXVContainer.getChild(i);
                if (child instanceof OnPullDownRefresh) {
                    ((OnPullDownRefresh) child).addOnRefreshOffsetChangedListener(this.mOnRefreshOffsetChangedListener);
                } else {
                    findPullDownComponetViewInRiver(child);
                }
            }
        }
    }

    public void registerOnPullDownEventDirectly(OnPullDownRefresh onPullDownRefresh) {
        if (onPullDownRefresh != null) {
            onPullDownRefresh.addOnRefreshOffsetChangedListener(this.mOnRefreshOffsetChangedListener);
        }
    }

    /* access modifiers changed from: private */
    public void findSwipeLayoutViewInWeex(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childAt = viewGroup.getChildAt(i);
                if (childAt instanceof WXSwipeLayout) {
                    ((WXSwipeLayout) childAt).addOnRefreshOffsetChangedListener(this.mOnRefreshOffsetChangedListener);
                } else {
                    findSwipeLayoutViewInWeex(childAt);
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public AppBarLayout initComponentHostView(@NonNull Context context) {
        AppBarLayout appBarLayout = new AppBarLayout(context);
        if (Build.VERSION.SDK_INT >= 21) {
            appBarLayout.setStateListAnimator((StateListAnimator) null);
        }
        FrameLayout frameLayout = new FrameLayout(context);
        AppBarLayout.LayoutParams layoutParams = new AppBarLayout.LayoutParams(-1, -1);
        this.mHeadParamsHolder = layoutParams;
        layoutParams.setScrollFlags(3);
        appBarLayout.addView(frameLayout, layoutParams);
        this.mRealView = frameLayout;
        appBarLayout.setBackgroundColor(0);
        removeShadowOn(appBarLayout);
        notifyOnOffsetChanged(appBarLayout);
        return appBarLayout;
    }

    private void removeShadowOn(@NonNull AppBarLayout appBarLayout) {
        ViewCompat.setElevation(appBarLayout, 0.0f);
        if (Build.VERSION.SDK_INT >= 21) {
            appBarLayout.setOutlineProvider((ViewOutlineProvider) null);
        }
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
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0019, code lost:
        r1 = (com.google.android.material.appbar.AppBarLayout.Behavior) r1.getBehavior();
     */
    @com.taobao.weex.annotation.JSMethod
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void animateManually(@androidx.annotation.Nullable java.util.Map<java.lang.String, java.lang.Object> r7) {
        /*
            r6 = this;
            android.view.View r0 = r6.getHostView()
            com.google.android.material.appbar.AppBarLayout r0 = (com.google.android.material.appbar.AppBarLayout) r0
            android.view.ViewGroup r1 = r6.getRealView()
            if (r0 == 0) goto L_0x00bb
            if (r1 != 0) goto L_0x0010
            goto L_0x00bb
        L_0x0010:
            android.view.ViewGroup$LayoutParams r1 = r0.getLayoutParams()
            androidx.coordinatorlayout.widget.CoordinatorLayout$LayoutParams r1 = (androidx.coordinatorlayout.widget.CoordinatorLayout.LayoutParams) r1
            if (r1 != 0) goto L_0x0019
            return
        L_0x0019:
            androidx.coordinatorlayout.widget.CoordinatorLayout$Behavior r1 = r1.getBehavior()
            com.google.android.material.appbar.AppBarLayout$Behavior r1 = (com.google.android.material.appbar.AppBarLayout.Behavior) r1
            if (r1 != 0) goto L_0x0022
            return
        L_0x0022:
            r2 = -1
            if (r7 != 0) goto L_0x0027
            r3 = -1
            goto L_0x0039
        L_0x0027:
            java.lang.String r3 = "from"
            java.lang.Object r3 = r7.get(r3)
            java.lang.Integer r4 = java.lang.Integer.valueOf(r2)
            java.lang.Integer r3 = com.taobao.weex.utils.WXUtils.getInteger(r3, r4)
            int r3 = r3.intValue()
        L_0x0039:
            if (r7 != 0) goto L_0x003d
            r4 = -1
            goto L_0x004f
        L_0x003d:
            java.lang.String r4 = "to"
            java.lang.Object r4 = r7.get(r4)
            java.lang.Integer r5 = java.lang.Integer.valueOf(r2)
            java.lang.Integer r4 = com.taobao.weex.utils.WXUtils.getInteger(r4, r5)
            int r4 = r4.intValue()
        L_0x004f:
            if (r3 != r2) goto L_0x0056
            int r3 = r1.getTopAndBottomOffset()
            goto L_0x0065
        L_0x0056:
            float r3 = (float) r3
            com.taobao.weex.WXSDKInstance r5 = r6.getInstance()
            int r5 = r5.getInstanceViewPortWidth()
            float r3 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r3, r5)
            float r3 = -r3
            int r3 = (int) r3
        L_0x0065:
            if (r4 != r2) goto L_0x006d
            int r2 = r0.getHeight()
            int r2 = -r2
            goto L_0x007c
        L_0x006d:
            float r2 = (float) r4
            com.taobao.weex.WXSDKInstance r4 = r6.getInstance()
            int r4 = r4.getInstanceViewPortWidth()
            float r2 = com.taobao.weex.utils.WXViewUtils.getRealPxByWidth(r2, r4)
            int r2 = (int) r2
            int r2 = -r2
        L_0x007c:
            if (r3 != r2) goto L_0x007f
            return
        L_0x007f:
            r4 = 300(0x12c, float:4.2E-43)
            if (r7 != 0) goto L_0x0084
            goto L_0x0096
        L_0x0084:
            java.lang.String r5 = "duration"
            java.lang.Object r7 = r7.get(r5)
            java.lang.Integer r4 = java.lang.Integer.valueOf(r4)
            java.lang.Integer r7 = com.taobao.weex.utils.WXUtils.getInteger(r7, r4)
            int r4 = r7.intValue()
        L_0x0096:
            r7 = 2
            int[] r7 = new int[r7]
            r5 = 0
            r7[r5] = r3
            r3 = 1
            r7[r3] = r2
            android.animation.ValueAnimator r7 = android.animation.ValueAnimator.ofInt(r7)
            android.view.animation.AccelerateInterpolator r2 = new android.view.animation.AccelerateInterpolator
            r2.<init>()
            r7.setInterpolator(r2)
            com.alibaba.android.enhance.nested.nested.WXNestedHeader$3 r2 = new com.alibaba.android.enhance.nested.nested.WXNestedHeader$3
            r2.<init>(r1, r0)
            r7.addUpdateListener(r2)
            long r0 = (long) r4
            r7.setDuration(r0)
            r7.start()
            return
        L_0x00bb:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.alibaba.android.enhance.nested.nested.WXNestedHeader.animateManually(java.util.Map):void");
    }

    /* access modifiers changed from: protected */
    public boolean setProperty(String str, Object obj) {
        return super.setProperty(str, obj);
    }

    public ViewGroup getRealView() {
        return this.mRealView;
    }

    public void setSnapEnabled(boolean z) {
        this.isSnapEnabled = z;
        judgeBehavior(this.isSnapEnabled, this.isQuickReturnEnabled);
    }

    public void setQuickReturn(boolean z) {
        this.isQuickReturnEnabled = z;
        judgeBehavior(this.isSnapEnabled, this.isQuickReturnEnabled);
    }

    private void judgeBehavior(boolean z, boolean z2) {
        if (this.mHeadParamsHolder != null) {
            int i = 3;
            if (z) {
                i = 19;
            }
            if (z2) {
                i |= 4;
            }
            this.mHeadParamsHolder.setScrollFlags(i);
        }
    }

    public void addOnHeadOffsetChangedListener(final AppBarLayout.OnOffsetChangedListener onOffsetChangedListener) {
        if (getHostView() != null) {
            ((AppBarLayout) getHostView()).addOnOffsetChangedListener((AppBarLayout.OnOffsetChangedListener) new AppBarLayout.OnOffsetChangedListener() {
                public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
                    if (onOffsetChangedListener != null) {
                        onOffsetChangedListener.onOffsetChanged(appBarLayout, i);
                    }
                }
            });
        }
    }

    private void notifyOnOffsetChanged(@NonNull AppBarLayout appBarLayout) {
        appBarLayout.addOnOffsetChangedListener((AppBarLayout.OnOffsetChangedListener) new InnerOffsetChangedListener());
    }

    class InnerOffsetChangedListener implements AppBarLayout.OnOffsetChangedListener {
        private int mLastOffset = -1;

        InnerOffsetChangedListener() {
        }

        public void onOffsetChanged(AppBarLayout appBarLayout, int i) {
            if (i != this.mLastOffset) {
                this.mLastOffset = i;
                HashMap hashMap = new HashMap(2);
                HashMap hashMap2 = new HashMap(2);
                int instanceViewPortWidth = WXNestedHeader.this.getInstance().getInstanceViewPortWidth();
                hashMap2.put(Constants.Name.X, Float.valueOf(0.0f));
                hashMap2.put(Constants.Name.Y, Float.valueOf(WXViewUtils.getWebPxByWidth((float) i, instanceViewPortWidth)));
                hashMap.put(Constants.Name.CONTENT_OFFSET, hashMap2);
                WXNestedHeader.this.fireEvent("scroll", hashMap);
            }
        }
    }

    public static class FlingBehavior extends AppBarLayout.Behavior {
        WXNestedHeaderHelper mWXNestedHeaderHelper;

        public FlingBehavior(boolean z) {
            if (z) {
                this.mWXNestedHeaderHelper = new WXNestedHeaderHelper(this);
            }
        }

        public FlingBehavior(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
        }

        public boolean onNestedFling(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, float f, float f2, boolean z) {
            boolean z2;
            boolean z3;
            if (view instanceof RecyclerView) {
                z2 = f2 > 0.0f || ((RecyclerView) view).computeVerticalScrollOffset() > 0;
            } else {
                z2 = z;
            }
            if (view instanceof WXSwipeLayout) {
                boolean z4 = z2;
                int i = 0;
                while (true) {
                    WXSwipeLayout wXSwipeLayout = (WXSwipeLayout) view;
                    if (i >= wXSwipeLayout.getChildCount()) {
                        break;
                    }
                    View childAt = wXSwipeLayout.getChildAt(i);
                    if (childAt instanceof RecyclerView) {
                        z4 = f2 > 0.0f || ((RecyclerView) childAt).computeVerticalScrollOffset() > 0;
                    }
                    i++;
                }
                z3 = z4;
            } else {
                z3 = z2;
            }
            try {
                return super.onNestedFling(coordinatorLayout, appBarLayout, view, f, f2, z3);
            } catch (Throwable unused) {
                return false;
            }
        }

        public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, View view, View view2, int i, int i2) {
            if (this.mWXNestedHeaderHelper != null) {
                this.mWXNestedHeaderHelper.onStartNestedScroll(appBarLayout);
            }
            return super.onStartNestedScroll(coordinatorLayout, appBarLayout, view, view2, i, i2);
        }

        public boolean onTouchEvent(CoordinatorLayout coordinatorLayout, AppBarLayout appBarLayout, MotionEvent motionEvent) {
            if (this.mWXNestedHeaderHelper != null) {
                this.mWXNestedHeaderHelper.onTouchEvent(coordinatorLayout, appBarLayout, motionEvent);
            }
            return super.onTouchEvent(coordinatorLayout, appBarLayout, motionEvent);
        }
    }

    public void onActivityDestroy() {
        super.onActivityDestroy();
        if (this.mObserver != null && this.mGlobalLayoutListener != null) {
            try {
                if (Build.VERSION.SDK_INT >= 16) {
                    this.mObserver.removeOnGlobalLayoutListener(this.mGlobalLayoutListener);
                } else {
                    this.mObserver.removeGlobalOnLayoutListener(this.mGlobalLayoutListener);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void addOnRefreshOffsetChangedListener(@Nullable OnNestedRefreshOffsetChangedListener onNestedRefreshOffsetChangedListener) {
        if (onNestedRefreshOffsetChangedListener != null) {
            this.mOffsetChangedListeners.add(onNestedRefreshOffsetChangedListener);
        }
    }

    public boolean removeOnRefreshOffsetChangedListener(@Nullable OnNestedRefreshOffsetChangedListener onNestedRefreshOffsetChangedListener) {
        if (onNestedRefreshOffsetChangedListener != null) {
            return this.mOffsetChangedListeners.remove(onNestedRefreshOffsetChangedListener);
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void notifyOnRefreshOffsetChangedListener(int i) {
        for (OnNestedRefreshOffsetChangedListener next : this.mOffsetChangedListeners) {
            if (next != null) {
                next.onOffsetChanged(i);
            }
        }
        HashMap hashMap = new HashMap(2);
        HashMap hashMap2 = new HashMap(2);
        int instanceViewPortWidth = getInstance().getInstanceViewPortWidth();
        hashMap2.put(Constants.Name.X, Float.valueOf(0.0f));
        hashMap2.put(Constants.Name.Y, Float.valueOf(WXViewUtils.getWebPxByWidth((float) i, instanceViewPortWidth)));
        hashMap.put(Constants.Name.CONTENT_OFFSET, hashMap2);
        fireEvent("nestedpull", hashMap);
    }
}
