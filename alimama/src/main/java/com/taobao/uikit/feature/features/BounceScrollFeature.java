package com.taobao.uikit.feature.features;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Scroller;
import com.taobao.uikit.R;
import com.taobao.uikit.feature.callback.MeasureCallback;
import com.taobao.uikit.feature.callback.ScrollCallback;
import com.taobao.uikit.feature.callback.TouchEventCallback;

public class BounceScrollFeature extends AbsFeature<ScrollView> implements ScrollCallback, MeasureCallback, TouchEventCallback {
    public static final String BOUNCE_TAG = "bounce";
    private View mBounceView;
    private int mInitHeadHeight = 0;
    private boolean mIsFirstMove = true;
    private float mLastY;
    private int mMaxHeadHeight = 0;
    private float mMaxRatio = 1.0f;
    private OnBounceHeightChangeListener mOnBounceHeightChangeListener;
    private Scroller mScroller;

    public interface OnBounceHeightChangeListener {
        void onHeightChanged(float f);
    }

    public void afterDispatchTouchEvent(MotionEvent motionEvent) {
    }

    public void afterOnScrollChanged(int i, int i2, int i3, int i4) {
    }

    public void afterOnTouchEvent(MotionEvent motionEvent) {
    }

    public void beforeComputeScroll() {
    }

    public void beforeDispatchTouchEvent(MotionEvent motionEvent) {
    }

    public void beforeOnMeasure(int i, int i2) {
    }

    public void beforeOnScrollChanged(int i, int i2, int i3, int i4) {
    }

    public void constructor(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes;
        if (attributeSet != null && (obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.BounceScrollFeature, i, 0)) != null) {
            this.mMaxRatio = obtainStyledAttributes.getFloat(R.styleable.BounceScrollFeature_uik_maxRatio, this.mMaxRatio);
            obtainStyledAttributes.recycle();
        }
    }

    public void afterComputeScroll() {
        if (this.mScroller.computeScrollOffset()) {
            int currY = this.mScroller.getCurrY();
            ViewGroup.LayoutParams layoutParams = this.mBounceView.getLayoutParams();
            layoutParams.height = currY;
            this.mBounceView.setLayoutParams(layoutParams);
            if (this.mOnBounceHeightChangeListener != null) {
                this.mOnBounceHeightChangeListener.onHeightChanged((float) currY);
            }
            ((ScrollView) getHost()).invalidate();
        }
    }

    public void setHost(ScrollView scrollView) {
        super.setHost(scrollView);
        this.mScroller = new Scroller(scrollView.getContext());
        ((ScrollView) getHost()).setOnHierarchyChangeListener(new ViewGroup.OnHierarchyChangeListener() {
            public void onChildViewAdded(View view, View view2) {
                BounceScrollFeature.this.findBounceView(view2);
            }

            public void onChildViewRemoved(View view, View view2) {
                BounceScrollFeature.this.findBounceView(view2);
            }
        });
    }

    /* access modifiers changed from: private */
    public void findBounceView(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            int childCount = viewGroup.getChildCount();
            int i = 0;
            while (i < childCount) {
                String str = (String) viewGroup.getChildAt(i).getContentDescription();
                if (str == null || !str.contains(BOUNCE_TAG)) {
                    if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                        findBounceView(viewGroup.getChildAt(i));
                    }
                    i++;
                } else {
                    this.mBounceView = viewGroup.getChildAt(i);
                    return;
                }
            }
            return;
        }
        String str2 = (String) view.getContentDescription();
        if (str2 != null && str2.contains(BOUNCE_TAG)) {
            this.mBounceView = view;
        }
    }

    public void setMaxRatio(float f) {
        this.mMaxRatio = f;
        ((ScrollView) getHost()).requestLayout();
    }

    public void afterOnMeasure(int i, int i2) {
        this.mMaxHeadHeight = (int) (((float) ((ScrollView) getHost()).getMeasuredWidth()) * this.mMaxRatio);
        if (this.mInitHeadHeight == 0) {
            this.mInitHeadHeight = this.mBounceView == null ? 0 : this.mBounceView.getMeasuredHeight();
        }
    }

    public void beforeOnTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction() & 255;
        if ((action == 1 || action == 3 || action == 4) && this.mBounceView != null) {
            ViewGroup.LayoutParams layoutParams = this.mBounceView.getLayoutParams();
            this.mScroller.startScroll(0, layoutParams.height, 0, this.mInitHeadHeight - layoutParams.height, 300);
            ((ScrollView) getHost()).computeScroll();
        }
        if (this.mBounceView != null && !this.mIsFirstMove && action == 2 && 0.0f < this.mLastY) {
            float y = this.mLastY - motionEvent.getY();
            if (((ScrollView) getHost()).getScrollY() <= 0 && 0.0f > y) {
                changeHeight(y);
            } else if (0.0f <= y && changeHeight(y)) {
                ((ScrollView) getHost()).scrollBy(0, (int) (-y));
            }
        }
        if (action == 2 && this.mIsFirstMove) {
            this.mIsFirstMove = false;
        } else if (action != 2) {
            this.mIsFirstMove = true;
        }
        this.mLastY = motionEvent.getY();
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0038  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean changeHeight(float r4) {
        /*
            r3 = this;
            android.view.View r0 = r3.mBounceView
            android.view.ViewGroup$LayoutParams r0 = r0.getLayoutParams()
            int r1 = r0.height
            float r1 = (float) r1
            float r1 = r1 - r4
            int r2 = r3.mInitHeadHeight
            float r2 = (float) r2
            int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
            if (r1 > 0) goto L_0x0016
            int r4 = r3.mInitHeadHeight
            r0.height = r4
            goto L_0x0025
        L_0x0016:
            int r1 = r0.height
            float r1 = (float) r1
            float r1 = r1 - r4
            int r2 = r3.mMaxHeadHeight
            float r2 = (float) r2
            int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
            if (r1 < 0) goto L_0x0027
            int r4 = r3.mMaxHeadHeight
            r0.height = r4
        L_0x0025:
            r4 = 0
            goto L_0x002f
        L_0x0027:
            int r1 = r0.height
            float r1 = (float) r1
            float r1 = r1 - r4
            int r4 = (int) r1
            r0.height = r4
            r4 = 1
        L_0x002f:
            android.view.View r1 = r3.mBounceView
            r1.setLayoutParams(r0)
            com.taobao.uikit.feature.features.BounceScrollFeature$OnBounceHeightChangeListener r1 = r3.mOnBounceHeightChangeListener
            if (r1 == 0) goto L_0x0040
            com.taobao.uikit.feature.features.BounceScrollFeature$OnBounceHeightChangeListener r1 = r3.mOnBounceHeightChangeListener
            int r0 = r0.height
            float r0 = (float) r0
            r1.onHeightChanged(r0)
        L_0x0040:
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.uikit.feature.features.BounceScrollFeature.changeHeight(float):boolean");
    }

    public void setOnBounceHeightChangeListener(OnBounceHeightChangeListener onBounceHeightChangeListener) {
        this.mOnBounceHeightChangeListener = onBounceHeightChangeListener;
    }
}
