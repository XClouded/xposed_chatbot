package com.alibaba.android.enhance.nested.nested;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.appbar.AppBarLayout;
import com.taobao.weex.ui.view.IRenderStatus;
import com.taobao.weex.ui.view.listview.WXRecyclerView;
import com.taobao.weex.ui.view.refresh.core.WXSwipeLayout;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class WXCoordinatorLayout extends CoordinatorLayout implements IRenderStatus<WXNestedParent> {
    private Field mCachedHackField = null;
    private List<WeakReference<RecyclerView>> mScrollingChildHolder = new ArrayList();
    private WeakReference<WXNestedParent> mTargetComponentRef = null;

    public WXCoordinatorLayout(Context context) {
        super(context);
    }

    public WXCoordinatorLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public WXCoordinatorLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void holdComponent(WXNestedParent wXNestedParent) {
        this.mTargetComponentRef = new WeakReference<>(wXNestedParent);
    }

    @Nullable
    public WXNestedParent getComponent() {
        if (this.mTargetComponentRef != null) {
            return (WXNestedParent) this.mTargetComponentRef.get();
        }
        return null;
    }

    public void onNestedPreScroll(View view, int i, int i2, int[] iArr) {
        super.onNestedPreScroll(view, i, i2, iArr);
        if (getComponent() != null) {
            if (!getComponent().shouldDisableHackFlingAnimation()) {
                cancelHeaderFlingAnimation(view);
            }
            if (getComponent().shouldNotifyWhenNestedChildScrolling()) {
                registerChildOnScrollListener(view);
            }
        }
    }

    public void onNestedPreScroll(View view, int i, int i2, int[] iArr, int i3) {
        super.onNestedPreScroll(view, i, i2, iArr, i3);
        if (getComponent() != null) {
            if (!getComponent().shouldDisableHackFlingAnimation()) {
                cancelHeaderFlingAnimation(view);
            }
            if (getComponent().shouldNotifyWhenNestedChildScrolling()) {
                registerChildOnScrollListener(view);
            }
        }
    }

    private void cancelHeaderFlingAnimation(View view) {
        Runnable runnable;
        if (((view instanceof WXSwipeLayout) || (view instanceof WXRecyclerView)) && getChildCount() > 0 && (getChildAt(0) instanceof AppBarLayout)) {
            AppBarLayout appBarLayout = (AppBarLayout) getChildAt(0);
            AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) ((CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams()).getBehavior();
            if (behavior != null) {
                try {
                    Field field = this.mCachedHackField;
                    if (field == null) {
                        field = getFieldByReflection(behavior.getClass(), "mFlingRunnable");
                        this.mCachedHackField = field;
                    }
                    if (field != null && (runnable = (Runnable) field.get(behavior)) != null) {
                        appBarLayout.removeCallbacks(runnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Field getFieldByReflection(@NonNull Class cls, @NonNull String str) {
        while (cls != null) {
            try {
                Field declaredField = cls.getDeclaredField(str);
                if (declaredField != null) {
                    declaredField.setAccessible(true);
                    return declaredField;
                }
            } catch (NoSuchFieldException unused) {
                cls = cls.getSuperclass();
            }
        }
        return null;
    }

    private boolean contains(RecyclerView recyclerView) {
        for (WeakReference<RecyclerView> weakReference : this.mScrollingChildHolder) {
            if (recyclerView == weakReference.get()) {
                return true;
            }
        }
        return false;
    }

    private void registerChildOnScrollListener(View view) {
        if (view instanceof WXSwipeLayout) {
            View childAt = ((WXSwipeLayout) view).getChildAt(0);
            if (childAt != null && (childAt instanceof RecyclerView)) {
                RecyclerView recyclerView = (RecyclerView) childAt;
                if (!contains(recyclerView)) {
                    this.mScrollingChildHolder.add(new WeakReference(recyclerView));
                    recyclerView.addOnScrollListener(new OnScrollListener(this.mTargetComponentRef));
                }
            }
        } else if (view instanceof RecyclerView) {
            RecyclerView recyclerView2 = (RecyclerView) view;
            if (!contains(recyclerView2)) {
                this.mScrollingChildHolder.add(new WeakReference(recyclerView2));
                recyclerView2.addOnScrollListener(new OnScrollListener(this.mTargetComponentRef));
            }
        }
    }

    static class OnScrollListener extends RecyclerView.OnScrollListener {
        private WeakReference<WXNestedParent> componentRef;
        private int contentOffset = 0;
        private int lastContentOffset = 0;

        OnScrollListener(WeakReference<WXNestedParent> weakReference) {
            this.componentRef = weakReference;
        }

        public void onScrolled(RecyclerView recyclerView, int i, int i2) {
            WXNestedParent wXNestedParent;
            this.contentOffset += i2;
            if (this.lastContentOffset != this.contentOffset) {
                if (!(this.componentRef == null || (wXNestedParent = (WXNestedParent) this.componentRef.get()) == null)) {
                    wXNestedParent.notifyOnNestedChildScrolling(recyclerView.getMeasuredWidth() + recyclerView.computeHorizontalScrollRange(), recyclerView.getMeasuredHeight() + recyclerView.computeVerticalScrollRange(), this.contentOffset);
                }
                this.lastContentOffset = this.contentOffset;
            }
        }
    }
}
