package com.taobao.android.dxcontainer.vlayout.layout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.ViewPropertyAnimator;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper;
import com.taobao.android.dxcontainer.vlayout.OrientationHelperEx;
import com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager;

public class FixLayoutHelper extends FixAreaLayoutHelper {
    public static final int BOTTOM_LEFT = 2;
    public static final int BOTTOM_RIGHT = 3;
    private static final String TAG = "FixLayoutHelper";
    public static final int TOP_LEFT = 0;
    public static final int TOP_RIGHT = 1;
    /* access modifiers changed from: private */
    public boolean isAddFixViewImmediately;
    /* access modifiers changed from: private */
    public boolean isRemoveFixViewImmediately;
    private int mAlignType;
    protected boolean mDoNormalHandle;
    protected View mFixView;
    private FixViewAppearAnimatorListener mFixViewAppearAnimatorListener;
    private FixViewDisappearAnimatorListener mFixViewDisappearAnimatorListener;
    /* access modifiers changed from: private */
    public int mPos;
    private boolean mShouldDrawn;
    private boolean mSketchMeasure;
    protected int mX;
    protected int mY;

    public boolean requireLayoutView() {
        return false;
    }

    public void setMargin(int i, int i2, int i3, int i4) {
    }

    /* access modifiers changed from: protected */
    public boolean shouldBeDraw(LayoutManagerHelper layoutManagerHelper, int i, int i2, int i3) {
        return true;
    }

    public FixLayoutHelper(int i, int i2) {
        this(0, i, i2);
    }

    public FixLayoutHelper(int i, int i2, int i3) {
        this.mPos = -1;
        this.mAlignType = 0;
        this.mX = 0;
        this.mY = 0;
        this.mSketchMeasure = false;
        this.mFixView = null;
        this.mDoNormalHandle = false;
        this.mShouldDrawn = true;
        this.isAddFixViewImmediately = false;
        this.isRemoveFixViewImmediately = true;
        this.mFixViewAppearAnimatorListener = new FixViewAppearAnimatorListener();
        this.mFixViewDisappearAnimatorListener = new FixViewDisappearAnimatorListener();
        this.mAlignType = i;
        this.mX = i2;
        this.mY = i3;
        setItemCount(1);
    }

    public void setItemCount(int i) {
        if (i > 0) {
            super.setItemCount(1);
        } else {
            super.setItemCount(0);
        }
    }

    public void setX(int i) {
        this.mX = i;
    }

    public void setY(int i) {
        this.mY = i;
    }

    public void setAlignType(int i) {
        this.mAlignType = i;
    }

    public void setSketchMeasure(boolean z) {
        this.mSketchMeasure = z;
    }

    public void onRangeChange(int i, int i2) {
        this.mPos = i;
    }

    public void layoutViews(RecyclerView.Recycler recycler, RecyclerView.State state, VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutChunkResult layoutChunkResult, LayoutManagerHelper layoutManagerHelper) {
        if (!isOutOfRange(layoutStateWrapper.getCurrentPosition())) {
            if (!this.mShouldDrawn) {
                layoutStateWrapper.skipCurrentPosition();
                return;
            }
            View view = this.mFixView;
            if (view == null) {
                view = layoutStateWrapper.next(recycler);
            } else {
                layoutStateWrapper.skipCurrentPosition();
            }
            if (view == null) {
                layoutChunkResult.mFinished = true;
                return;
            }
            this.mDoNormalHandle = state.isPreLayout();
            if (this.mDoNormalHandle) {
                layoutManagerHelper.addChildView(layoutStateWrapper, view);
            }
            this.mFixView = view;
            doMeasureAndLayout(view, layoutManagerHelper);
            layoutChunkResult.mConsumed = 0;
            layoutChunkResult.mIgnoreConsumed = true;
            handleStateOnResult(layoutChunkResult, view);
        }
    }

    public void beforeLayout(RecyclerView.Recycler recycler, RecyclerView.State state, LayoutManagerHelper layoutManagerHelper) {
        super.beforeLayout(recycler, state, layoutManagerHelper);
        if (this.mFixView != null && layoutManagerHelper.isViewHolderUpdated(this.mFixView)) {
            layoutManagerHelper.removeChildView(this.mFixView);
            recycler.recycleView(this.mFixView);
            this.mFixView = null;
            this.isAddFixViewImmediately = true;
        }
        this.mDoNormalHandle = false;
    }

    public void afterLayout(final RecyclerView.Recycler recycler, RecyclerView.State state, int i, int i2, int i3, final LayoutManagerHelper layoutManagerHelper) {
        super.afterLayout(recycler, state, i, i2, i3, layoutManagerHelper);
        if (this.mPos >= 0) {
            if (this.mDoNormalHandle && state.isPreLayout()) {
                if (this.mFixView != null) {
                    layoutManagerHelper.removeChildView(this.mFixView);
                    recycler.recycleView(this.mFixView);
                    this.isAddFixViewImmediately = false;
                }
                this.mFixView = null;
            } else if (shouldBeDraw(layoutManagerHelper, i, i2, i3)) {
                this.mShouldDrawn = true;
                if (this.mFixView == null) {
                    AnonymousClass1 r5 = new Runnable() {
                        public void run() {
                            FixLayoutHelper.this.mFixView = recycler.getViewForPosition(FixLayoutHelper.this.mPos);
                            FixLayoutHelper.this.doMeasureAndLayout(FixLayoutHelper.this.mFixView, layoutManagerHelper);
                            if (FixLayoutHelper.this.isAddFixViewImmediately) {
                                layoutManagerHelper.addFixedView(FixLayoutHelper.this.mFixView);
                                boolean unused = FixLayoutHelper.this.isRemoveFixViewImmediately = false;
                                return;
                            }
                            FixLayoutHelper.this.addFixViewWithAnimator(layoutManagerHelper, FixLayoutHelper.this.mFixView);
                        }
                    };
                    if (this.mFixViewDisappearAnimatorListener.isAnimating()) {
                        this.mFixViewDisappearAnimatorListener.withEndAction(r5);
                    } else {
                        r5.run();
                    }
                } else if (this.mFixView.getParent() == null) {
                    addFixViewWithAnimator(layoutManagerHelper, this.mFixView);
                } else {
                    layoutManagerHelper.addFixedView(this.mFixView);
                    this.isRemoveFixViewImmediately = false;
                }
            } else {
                this.mShouldDrawn = false;
                if (this.mFixView != null) {
                    removeFixViewWithAnimator(recycler, layoutManagerHelper, this.mFixView);
                    this.mFixView = null;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void addFixViewWithAnimator(LayoutManagerHelper layoutManagerHelper, View view) {
        if (this.mFixViewAnimatorHelper != null) {
            ViewPropertyAnimator onGetFixViewAppearAnimator = this.mFixViewAnimatorHelper.onGetFixViewAppearAnimator(view);
            if (onGetFixViewAppearAnimator != null) {
                view.setVisibility(4);
                layoutManagerHelper.addFixedView(view);
                this.mFixViewAppearAnimatorListener.bindAction(layoutManagerHelper, view);
                onGetFixViewAppearAnimator.setListener(this.mFixViewAppearAnimatorListener).start();
            } else {
                layoutManagerHelper.addFixedView(view);
            }
        } else {
            layoutManagerHelper.addFixedView(view);
        }
        this.isRemoveFixViewImmediately = false;
    }

    private void removeFixViewWithAnimator(RecyclerView.Recycler recycler, LayoutManagerHelper layoutManagerHelper, View view) {
        if (this.isRemoveFixViewImmediately || this.mFixViewAnimatorHelper == null) {
            layoutManagerHelper.removeChildView(view);
            recycler.recycleView(view);
            this.isAddFixViewImmediately = false;
            return;
        }
        ViewPropertyAnimator onGetFixViewDisappearAnimator = this.mFixViewAnimatorHelper.onGetFixViewDisappearAnimator(view);
        if (onGetFixViewDisappearAnimator != null) {
            this.mFixViewDisappearAnimatorListener.bindAction(recycler, layoutManagerHelper, view);
            onGetFixViewDisappearAnimator.setListener(this.mFixViewDisappearAnimatorListener).start();
            this.isAddFixViewImmediately = false;
            return;
        }
        layoutManagerHelper.removeChildView(view);
        recycler.recycleView(view);
        this.isAddFixViewImmediately = false;
    }

    public View getFixedView() {
        return this.mFixView;
    }

    public void onClear(LayoutManagerHelper layoutManagerHelper) {
        super.onClear(layoutManagerHelper);
        if (this.mFixView != null) {
            layoutManagerHelper.removeChildView(this.mFixView);
            layoutManagerHelper.recycleView(this.mFixView);
            this.mFixView.animate().cancel();
            this.mFixView = null;
            this.isAddFixViewImmediately = false;
        }
    }

    /* access modifiers changed from: private */
    public void doMeasureAndLayout(View view, LayoutManagerHelper layoutManagerHelper) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        int contentWidth;
        int contentHeight;
        int measuredWidth;
        int measuredHeight;
        int i7;
        int i8;
        if (view != null && layoutManagerHelper != null) {
            VirtualLayoutManager.LayoutParams layoutParams = (VirtualLayoutManager.LayoutParams) view.getLayoutParams();
            OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
            boolean z = layoutManagerHelper.getOrientation() == 1;
            int i9 = -1;
            if (z) {
                int childMeasureSpec = layoutManagerHelper.getChildMeasureSpec((layoutManagerHelper.getContentWidth() - layoutManagerHelper.getPaddingLeft()) - layoutManagerHelper.getPaddingRight(), layoutParams.width >= 0 ? layoutParams.width : (!this.mSketchMeasure || !z) ? -2 : -1, false);
                if (!Float.isNaN(layoutParams.mAspectRatio) && layoutParams.mAspectRatio > 0.0f) {
                    i8 = layoutManagerHelper.getChildMeasureSpec((layoutManagerHelper.getContentHeight() - layoutManagerHelper.getPaddingTop()) - layoutManagerHelper.getPaddingBottom(), (int) ((((float) View.MeasureSpec.getSize(childMeasureSpec)) / layoutParams.mAspectRatio) + 0.5f), false);
                } else if (Float.isNaN(this.mAspectRatio) || this.mAspectRatio <= 0.0f) {
                    int contentHeight2 = (layoutManagerHelper.getContentHeight() - layoutManagerHelper.getPaddingTop()) - layoutManagerHelper.getPaddingBottom();
                    if (layoutParams.height >= 0) {
                        i9 = layoutParams.height;
                    } else if (!this.mSketchMeasure || z) {
                        i9 = -2;
                    }
                    i8 = layoutManagerHelper.getChildMeasureSpec(contentHeight2, i9, false);
                } else {
                    i8 = layoutManagerHelper.getChildMeasureSpec((layoutManagerHelper.getContentHeight() - layoutManagerHelper.getPaddingTop()) - layoutManagerHelper.getPaddingBottom(), (int) ((((float) View.MeasureSpec.getSize(childMeasureSpec)) / this.mAspectRatio) + 0.5f), false);
                }
                layoutManagerHelper.measureChildWithMargins(view, childMeasureSpec, i8);
            } else {
                int childMeasureSpec2 = layoutManagerHelper.getChildMeasureSpec((layoutManagerHelper.getContentHeight() - layoutManagerHelper.getPaddingTop()) - layoutManagerHelper.getPaddingBottom(), layoutParams.height >= 0 ? layoutParams.height : (!this.mSketchMeasure || z) ? -2 : -1, false);
                if (!Float.isNaN(layoutParams.mAspectRatio) && layoutParams.mAspectRatio > 0.0f) {
                    i7 = layoutManagerHelper.getChildMeasureSpec((layoutManagerHelper.getContentWidth() - layoutManagerHelper.getPaddingLeft()) - layoutManagerHelper.getPaddingRight(), (int) ((((float) View.MeasureSpec.getSize(childMeasureSpec2)) * layoutParams.mAspectRatio) + 0.5f), false);
                } else if (Float.isNaN(this.mAspectRatio) || this.mAspectRatio <= 0.0f) {
                    int contentWidth2 = (layoutManagerHelper.getContentWidth() - layoutManagerHelper.getPaddingLeft()) - layoutManagerHelper.getPaddingRight();
                    if (layoutParams.width >= 0) {
                        i9 = layoutParams.width;
                    } else if (!this.mSketchMeasure || !z) {
                        i9 = -2;
                    }
                    i7 = layoutManagerHelper.getChildMeasureSpec(contentWidth2, i9, false);
                } else {
                    i7 = layoutManagerHelper.getChildMeasureSpec((layoutManagerHelper.getContentWidth() - layoutManagerHelper.getPaddingLeft()) - layoutManagerHelper.getPaddingRight(), (int) ((((float) View.MeasureSpec.getSize(childMeasureSpec2)) * this.mAspectRatio) + 0.5f), false);
                }
                layoutManagerHelper.measureChildWithMargins(view, i7, childMeasureSpec2);
            }
            if (this.mAlignType == 1) {
                measuredHeight = layoutManagerHelper.getPaddingTop() + this.mY + this.mAdjuster.top;
                contentWidth = ((layoutManagerHelper.getContentWidth() - layoutManagerHelper.getPaddingRight()) - this.mX) - this.mAdjuster.right;
                measuredWidth = ((contentWidth - layoutParams.leftMargin) - layoutParams.rightMargin) - view.getMeasuredWidth();
                contentHeight = layoutParams.topMargin + measuredHeight + layoutParams.bottomMargin + view.getMeasuredHeight();
            } else if (this.mAlignType == 2) {
                measuredWidth = layoutManagerHelper.getPaddingLeft() + this.mX + this.mAdjuster.left;
                contentHeight = ((layoutManagerHelper.getContentHeight() - layoutManagerHelper.getPaddingBottom()) - this.mY) - this.mAdjuster.bottom;
                contentWidth = view.getMeasuredWidth() + layoutParams.leftMargin + measuredWidth + layoutParams.rightMargin;
                measuredHeight = ((contentHeight - view.getMeasuredHeight()) - layoutParams.topMargin) - layoutParams.bottomMargin;
            } else if (this.mAlignType == 3) {
                contentWidth = ((layoutManagerHelper.getContentWidth() - layoutManagerHelper.getPaddingRight()) - this.mX) - this.mAdjuster.right;
                contentHeight = ((layoutManagerHelper.getContentHeight() - layoutManagerHelper.getPaddingBottom()) - this.mY) - this.mAdjuster.bottom;
                measuredWidth = ((contentWidth - layoutParams.leftMargin) - layoutParams.rightMargin) - view.getMeasuredWidth();
                measuredHeight = ((contentHeight - view.getMeasuredHeight()) - layoutParams.topMargin) - layoutParams.bottomMargin;
            } else {
                int paddingLeft = this.mAdjuster.left + layoutManagerHelper.getPaddingLeft() + this.mX;
                int paddingTop = layoutManagerHelper.getPaddingTop() + this.mY + this.mAdjuster.top;
                if (z) {
                    i5 = mainOrientationHelper.getDecoratedMeasurementInOther(view);
                } else {
                    i5 = mainOrientationHelper.getDecoratedMeasurement(view);
                }
                int i10 = i5 + paddingLeft;
                if (z) {
                    i6 = mainOrientationHelper.getDecoratedMeasurement(view);
                } else {
                    i6 = mainOrientationHelper.getDecoratedMeasurementInOther(view);
                }
                i3 = paddingTop;
                i = i6 + paddingTop;
                i4 = paddingLeft;
                i2 = i10;
                layoutChildWithMargin(view, i4, i3, i2, i, layoutManagerHelper);
            }
            i3 = measuredHeight;
            i2 = contentWidth;
            i4 = measuredWidth;
            i = contentHeight;
            layoutChildWithMargin(view, i4, i3, i2, i, layoutManagerHelper);
        }
    }

    private static class FixViewAppearAnimatorListener extends AnimatorListenerAdapter {
        private View mFixView;
        private LayoutManagerHelper mLayoutManagerHelper;

        public void onAnimationEnd(Animator animator) {
        }

        private FixViewAppearAnimatorListener() {
        }

        public void bindAction(LayoutManagerHelper layoutManagerHelper, View view) {
            this.mLayoutManagerHelper = layoutManagerHelper;
            this.mFixView = view;
        }

        public void onAnimationStart(Animator animator) {
            this.mFixView.setVisibility(0);
        }
    }

    private static class FixViewDisappearAnimatorListener extends AnimatorListenerAdapter {
        private boolean isAnimating;
        private Runnable mEndAction;
        private View mFixView;
        private LayoutManagerHelper mLayoutManagerHelper;
        private RecyclerView.Recycler mRecycler;

        public void onAnimationStart(Animator animator) {
        }

        private FixViewDisappearAnimatorListener() {
        }

        public void bindAction(RecyclerView.Recycler recycler, LayoutManagerHelper layoutManagerHelper, View view) {
            this.isAnimating = true;
            this.mRecycler = recycler;
            this.mLayoutManagerHelper = layoutManagerHelper;
            this.mFixView = view;
        }

        public void onAnimationEnd(Animator animator) {
            this.mLayoutManagerHelper.removeChildView(this.mFixView);
            this.mRecycler.recycleView(this.mFixView);
            this.isAnimating = false;
            if (this.mEndAction != null) {
                this.mEndAction.run();
                this.mEndAction = null;
            }
        }

        public boolean isAnimating() {
            return this.isAnimating;
        }

        public void withEndAction(Runnable runnable) {
            this.mEndAction = runnable;
        }
    }
}
