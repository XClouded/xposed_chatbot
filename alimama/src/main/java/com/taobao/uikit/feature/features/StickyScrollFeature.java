package com.taobao.uikit.feature.features;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ScrollView;
import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.CanvasCallback;
import com.taobao.uikit.feature.callback.LayoutCallback;
import com.taobao.uikit.feature.callback.ScrollCallback;
import com.taobao.uikit.feature.callback.SetClipToPaddingCallBack;
import com.taobao.uikit.feature.callback.TouchEventCallback;
import java.util.ArrayList;

public class StickyScrollFeature extends AbsFeature<ScrollView> implements CanvasCallback, TouchEventCallback, LayoutCallback, ScrollCallback, SetClipToPaddingCallBack {
    private static final int DEFAULT_SHADOW_HEIGHT = 10;
    public static final String FLAG_HASTRANSPARANCY = "-hastransparancy";
    public static final String FLAG_NONCONSTANT = "-nonconstant";
    public static final String STICKY_TAG = "sticky";
    private boolean mClipToPadding;
    private boolean mClipToPaddingHasBeenSet;
    /* access modifiers changed from: private */
    public View mCurrentlyStickingView;
    private boolean mHasNotDoneActionDown = true;
    /* access modifiers changed from: private */
    public int mInvalidateCount = 0;
    private final Runnable mInvalidateRunnable = new Runnable() {
        public void run() {
            if (StickyScrollFeature.this.mCurrentlyStickingView != null) {
                ((ScrollView) StickyScrollFeature.this.getHost()).invalidate(StickyScrollFeature.this.getLeftForViewRelativeOnlyChild(StickyScrollFeature.this.mCurrentlyStickingView), StickyScrollFeature.this.getBottomForViewRelativeOnlyChild(StickyScrollFeature.this.mCurrentlyStickingView), StickyScrollFeature.this.getRightForViewRelativeOnlyChild(StickyScrollFeature.this.mCurrentlyStickingView), (int) (((float) ((ScrollView) StickyScrollFeature.this.getHost()).getScrollY()) + ((float) StickyScrollFeature.this.mCurrentlyStickingView.getHeight()) + StickyScrollFeature.this.mStickyViewTopOffset));
            }
            if (StickyScrollFeature.this.mInvalidateCount < 20) {
                StickyScrollFeature.access$508(StickyScrollFeature.this);
                ((ScrollView) StickyScrollFeature.this.getHost()).postDelayed(this, 16);
            }
        }
    };
    private boolean mRedirectTouchesToStickyView;
    private Drawable mShadowDrawable;
    private int mShadowHeight;
    private int mStickyViewLeftOffset;
    /* access modifiers changed from: private */
    public float mStickyViewTopOffset;
    private ArrayList<View> mStickyViews;

    public void afterComputeScroll() {
    }

    public void afterDispatchTouchEvent(MotionEvent motionEvent) {
    }

    public void afterDraw(Canvas canvas) {
    }

    public void afterOnDraw(Canvas canvas) {
    }

    public void afterOnTouchEvent(MotionEvent motionEvent) {
    }

    public void beforeComputeScroll() {
    }

    public void beforeDispatchDraw(Canvas canvas) {
    }

    public void beforeDraw(Canvas canvas) {
    }

    public void beforeOnDraw(Canvas canvas) {
    }

    public void beforeOnLayout(boolean z, int i, int i2, int i3, int i4) {
    }

    public void beforeOnScrollChanged(int i, int i2, int i3, int i4) {
    }

    public void beforeSetClipToPadding(boolean z) {
    }

    static /* synthetic */ int access$508(StickyScrollFeature stickyScrollFeature) {
        int i = stickyScrollFeature.mInvalidateCount;
        stickyScrollFeature.mInvalidateCount = i + 1;
        return i;
    }

    public void constructor(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes;
        if (attributeSet != null && (obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.StickyScrollFeature, i, 0)) != null) {
            this.mShadowHeight = obtainStyledAttributes.getDimensionPixelSize(R.styleable.StickyScrollFeature_uik_shadowHeight, (int) ((context.getResources().getDisplayMetrics().density * 10.0f) + 0.5f));
            this.mShadowDrawable = obtainStyledAttributes.getDrawable(R.styleable.StickyScrollFeature_uik_shadowDrawable);
            obtainStyledAttributes.recycle();
        }
    }

    public void setHost(ScrollView scrollView) {
        super.setHost(scrollView);
        setup();
        ((ScrollView) getHost()).setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            public void onChildViewRemoved(View view, View view2) {
            }

            public void onChildViewAdded(View view, View view2) {
                StickyScrollFeature.this.findStickyViews(view2);
            }
        });
    }

    public void afterDispatchDraw(Canvas canvas) {
        if (this.mCurrentlyStickingView != null) {
            canvas.save();
            canvas.translate((float) (((ScrollView) getHost()).getPaddingLeft() + this.mStickyViewLeftOffset), ((float) ((ScrollView) getHost()).getScrollY()) + this.mStickyViewTopOffset + ((float) (this.mClipToPadding ? ((ScrollView) getHost()).getPaddingTop() : 0)));
            canvas.clipRect(0.0f, this.mClipToPadding ? -this.mStickyViewTopOffset : 0.0f, (float) (((ScrollView) getHost()).getWidth() - this.mStickyViewLeftOffset), (float) (this.mCurrentlyStickingView.getHeight() + this.mShadowHeight + 1));
            if (this.mShadowDrawable != null) {
                this.mShadowDrawable.setBounds(0, this.mCurrentlyStickingView.getHeight(), this.mCurrentlyStickingView.getWidth(), this.mCurrentlyStickingView.getHeight() + this.mShadowHeight);
                this.mShadowDrawable.draw(canvas);
            }
            canvas.clipRect(0.0f, this.mClipToPadding ? -this.mStickyViewTopOffset : 0.0f, (float) ((ScrollView) getHost()).getWidth(), (float) this.mCurrentlyStickingView.getHeight());
            if (((String) this.mCurrentlyStickingView.getContentDescription()).contains(FLAG_HASTRANSPARANCY)) {
                showView(this.mCurrentlyStickingView);
                this.mCurrentlyStickingView.draw(canvas);
                hideView(this.mCurrentlyStickingView);
            } else {
                this.mCurrentlyStickingView.draw(canvas);
            }
            canvas.restore();
        }
    }

    public void afterOnScrollChanged(int i, int i2, int i3, int i4) {
        doTheStickyThing();
    }

    public void afterSetClipToPadding(boolean z) {
        this.mClipToPadding = z;
        this.mClipToPaddingHasBeenSet = true;
    }

    public void beforeOnTouchEvent(MotionEvent motionEvent) {
        if (this.mRedirectTouchesToStickyView) {
            motionEvent.offsetLocation(0.0f, (((float) ((ScrollView) getHost()).getScrollY()) + this.mStickyViewTopOffset) - ((float) getTopForViewRelativeOnlyChild(this.mCurrentlyStickingView)));
        }
        if (motionEvent.getAction() == 0) {
            this.mHasNotDoneActionDown = false;
        }
        if (this.mHasNotDoneActionDown) {
            MotionEvent.obtain(motionEvent).setAction(0);
            this.mHasNotDoneActionDown = false;
        } else if (motionEvent.getAction() == 1 || motionEvent.getAction() == 3) {
            this.mHasNotDoneActionDown = true;
        }
    }

    public void beforeDispatchTouchEvent(MotionEvent motionEvent) {
        boolean z = true;
        if (motionEvent.getAction() == 0) {
            this.mRedirectTouchesToStickyView = true;
        }
        if (this.mRedirectTouchesToStickyView) {
            this.mRedirectTouchesToStickyView = this.mCurrentlyStickingView != null;
            if (this.mRedirectTouchesToStickyView) {
                if (motionEvent.getY() > ((float) this.mCurrentlyStickingView.getHeight()) + this.mStickyViewTopOffset || motionEvent.getX() < ((float) getLeftForViewRelativeOnlyChild(this.mCurrentlyStickingView)) || motionEvent.getX() > ((float) getRightForViewRelativeOnlyChild(this.mCurrentlyStickingView))) {
                    z = false;
                }
                this.mRedirectTouchesToStickyView = z;
            }
        } else if (this.mCurrentlyStickingView == null) {
            this.mRedirectTouchesToStickyView = false;
        }
        if (this.mRedirectTouchesToStickyView) {
            motionEvent.offsetLocation(0.0f, ((((float) ((ScrollView) getHost()).getScrollY()) + this.mStickyViewTopOffset) - ((float) getTopForViewRelativeOnlyChild(this.mCurrentlyStickingView))) * -1.0f);
            if (((String) this.mCurrentlyStickingView.getContentDescription()).contains(FLAG_NONCONSTANT)) {
                this.mInvalidateCount = 0;
                ((ScrollView) getHost()).post(this.mInvalidateRunnable);
            }
        }
    }

    public void afterOnLayout(boolean z, int i, int i2, int i3, int i4) {
        if (!this.mClipToPaddingHasBeenSet) {
            this.mClipToPadding = true;
        }
        notifyHierarchyChanged();
    }

    @SuppressLint({"NewApi"})
    public void setup() {
        this.mStickyViews = new ArrayList<>();
        if (11 <= Build.VERSION.SDK_INT && Build.VERSION.SDK_INT < 18) {
            ((ScrollView) getHost()).setLayerType(1, (Paint) null);
        }
    }

    /* access modifiers changed from: private */
    public void findStickyViews(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            for (int i = 0; i < childCount; i++) {
                String str = (String) viewGroup.getChildAt(i).getContentDescription();
                if (str != null && str.contains("sticky")) {
                    this.mStickyViews.add(viewGroup.getChildAt(i));
                } else if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                    findStickyViews(viewGroup.getChildAt(i));
                }
            }
            return;
        }
        String str2 = (String) view.getContentDescription();
        if (str2 != null && str2.contains("sticky")) {
            this.mStickyViews.add(view);
        }
    }

    private void hideView(View view) {
        if (Build.VERSION.SDK_INT >= 11) {
            view.setAlpha(0.0f);
            return;
        }
        AlphaAnimation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
        alphaAnimation.setDuration(0);
        alphaAnimation.setFillAfter(true);
        view.startAnimation(alphaAnimation);
    }

    private void showView(View view) {
        if (Build.VERSION.SDK_INT >= 11) {
            view.setAlpha(1.0f);
            return;
        }
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(0);
        alphaAnimation.setFillAfter(true);
        view.startAnimation(alphaAnimation);
    }

    /* access modifiers changed from: private */
    public int getLeftForViewRelativeOnlyChild(View view) {
        int left = view.getLeft();
        while (view.getParent() != ((ScrollView) getHost()).getChildAt(0)) {
            view = (View) view.getParent();
            left += view.getLeft();
        }
        return left;
    }

    private int getTopForViewRelativeOnlyChild(View view) {
        int top = view.getTop();
        while (view.getParent() != ((ScrollView) getHost()).getChildAt(0)) {
            view = (View) view.getParent();
            top += view.getTop();
        }
        return top;
    }

    /* access modifiers changed from: private */
    public int getRightForViewRelativeOnlyChild(View view) {
        int right = view.getRight();
        while (view.getParent() != ((ScrollView) getHost()).getChildAt(0)) {
            view = (View) view.getParent();
            right += view.getRight();
        }
        return right;
    }

    /* access modifiers changed from: private */
    public int getBottomForViewRelativeOnlyChild(View view) {
        int bottom = view.getBottom();
        while (view.getParent() != ((ScrollView) getHost()).getChildAt(0)) {
            view = (View) view.getParent();
            bottom += view.getBottom();
        }
        return bottom;
    }

    private void doTheStickyThing() {
        float f;
        int i;
        int size = this.mStickyViews.size();
        View view = null;
        View view2 = null;
        for (int i2 = 0; i2 < size; i2++) {
            View view3 = this.mStickyViews.get(i2);
            int topForViewRelativeOnlyChild = (getTopForViewRelativeOnlyChild(view3) - ((ScrollView) getHost()).getScrollY()) + (this.mClipToPadding ? 0 : ((ScrollView) getHost()).getPaddingTop());
            if (topForViewRelativeOnlyChild <= 0) {
                if (view != null) {
                    if (topForViewRelativeOnlyChild <= (getTopForViewRelativeOnlyChild(view) - ((ScrollView) getHost()).getScrollY()) + (this.mClipToPadding ? 0 : ((ScrollView) getHost()).getPaddingTop())) {
                    }
                }
                view = view3;
            } else {
                if (view2 != null) {
                    if (topForViewRelativeOnlyChild >= (getTopForViewRelativeOnlyChild(view2) - ((ScrollView) getHost()).getScrollY()) + (this.mClipToPadding ? 0 : ((ScrollView) getHost()).getPaddingTop())) {
                    }
                }
                view2 = view3;
            }
        }
        if (view != null) {
            if (view2 == null) {
                f = 0.0f;
            } else {
                int topForViewRelativeOnlyChild2 = getTopForViewRelativeOnlyChild(view2) - ((ScrollView) getHost()).getScrollY();
                if (this.mClipToPadding) {
                    i = 0;
                } else {
                    i = ((ScrollView) getHost()).getPaddingTop();
                }
                f = (float) Math.min(0, (topForViewRelativeOnlyChild2 + i) - view.getHeight());
            }
            this.mStickyViewTopOffset = f;
            if (view != this.mCurrentlyStickingView) {
                if (this.mCurrentlyStickingView != null) {
                    stopStickingCurrentlyStickingView();
                }
                this.mStickyViewLeftOffset = getLeftForViewRelativeOnlyChild(view);
                startStickingView(view);
            }
        } else if (this.mCurrentlyStickingView != null) {
            stopStickingCurrentlyStickingView();
        }
    }

    private void startStickingView(View view) {
        this.mCurrentlyStickingView = view;
        if (((String) this.mCurrentlyStickingView.getContentDescription()).contains(FLAG_HASTRANSPARANCY)) {
            hideView(this.mCurrentlyStickingView);
        }
    }

    private void stopStickingCurrentlyStickingView() {
        if (((String) this.mCurrentlyStickingView.getContentDescription()).contains(FLAG_HASTRANSPARANCY)) {
            showView(this.mCurrentlyStickingView);
        }
        this.mCurrentlyStickingView = null;
    }

    private void notifyHierarchyChanged() {
        if (this.mCurrentlyStickingView != null) {
            stopStickingCurrentlyStickingView();
        }
        this.mStickyViews.clear();
        findStickyViews(((ScrollView) getHost()).getChildAt(0));
        doTheStickyThing();
        ((ScrollView) getHost()).invalidate();
    }

    public void setShadowHeight(int i) {
        this.mShadowHeight = i;
    }
}
