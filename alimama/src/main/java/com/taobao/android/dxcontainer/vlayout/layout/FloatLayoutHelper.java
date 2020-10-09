package com.taobao.android.dxcontainer.vlayout.layout;

import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.taobao.android.dxcontainer.vlayout.LayoutManagerHelper;
import com.taobao.android.dxcontainer.vlayout.OrientationHelperEx;
import com.taobao.android.dxcontainer.vlayout.VirtualLayoutManager;

public class FloatLayoutHelper extends FixAreaLayoutHelper {
    private static final String TAG = "FloatLayoutHelper";
    private boolean dragEnable = true;
    private int mAlignType = 0;
    protected boolean mDoNormalHandle = false;
    protected View mFixView = null;
    private int mPos = -1;
    /* access modifiers changed from: private */
    public int mTransitionX = 0;
    /* access modifiers changed from: private */
    public int mTransitionY = 0;
    private int mX = 0;
    private int mY = 0;
    private int mZIndex = 1;
    private final View.OnTouchListener touchDragListener = new View.OnTouchListener() {
        private int bottomMargin;
        private boolean isDrag;
        private int lastPosX;
        private int lastPosY;
        private int leftMargin;
        private int mTouchSlop;
        private final Rect parentLoction = new Rect();
        private int parentViewHeight;
        private int parentViewWidth;
        private int rightMargin;
        private int topMargin;

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (this.mTouchSlop == 0) {
                this.mTouchSlop = ViewConfiguration.get(view.getContext()).getScaledTouchSlop();
                this.parentViewHeight = ((View) view.getParent()).getHeight();
                this.parentViewWidth = ((View) view.getParent()).getWidth();
                ((View) view.getParent()).getGlobalVisibleRect(this.parentLoction);
                ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    this.leftMargin = marginLayoutParams.leftMargin;
                    this.topMargin = marginLayoutParams.topMargin;
                    this.rightMargin = marginLayoutParams.rightMargin;
                    this.bottomMargin = marginLayoutParams.bottomMargin;
                }
            }
            switch (motionEvent.getAction()) {
                case 0:
                    this.isDrag = false;
                    view.getParent().requestDisallowInterceptTouchEvent(true);
                    this.lastPosX = (int) motionEvent.getX();
                    this.lastPosY = (int) motionEvent.getY();
                    break;
                case 1:
                case 3:
                    doPullOverAnimation(view);
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                    view.setPressed(false);
                    break;
                case 2:
                    if (Math.abs(motionEvent.getX() - ((float) this.lastPosX)) > ((float) this.mTouchSlop) || Math.abs(motionEvent.getY() - ((float) this.lastPosY)) > ((float) this.mTouchSlop)) {
                        this.isDrag = true;
                    }
                    if (this.isDrag) {
                        int rawX = ((int) motionEvent.getRawX()) - this.parentLoction.left;
                        int rawY = ((int) motionEvent.getRawY()) - this.parentLoction.top;
                        view.setTranslationX((float) ((((rawX - (view.getWidth() / 2)) - view.getLeft()) - this.leftMargin) - FloatLayoutHelper.this.mAdjuster.left));
                        int height = ((rawY - (view.getHeight() / 2)) - view.getTop()) - this.topMargin;
                        if (view.getHeight() + height + view.getTop() + this.bottomMargin > this.parentViewHeight) {
                            height = ((this.parentViewHeight - view.getHeight()) - view.getTop()) - this.bottomMargin;
                        }
                        if ((view.getTop() + height) - this.topMargin < 0) {
                            height = (-view.getTop()) + this.topMargin;
                        }
                        view.setTranslationY((float) height);
                        break;
                    }
                    break;
            }
            return this.isDrag;
        }

        private void doPullOverAnimation(View view) {
            ObjectAnimator objectAnimator;
            if (view.getTranslationX() + ((float) (view.getWidth() / 2)) + ((float) view.getLeft()) > ((float) (this.parentViewWidth / 2))) {
                objectAnimator = ObjectAnimator.ofFloat(view, "translationX", new float[]{view.getTranslationX(), (float) ((((this.parentViewWidth - view.getWidth()) - view.getLeft()) - this.rightMargin) - FloatLayoutHelper.this.mAdjuster.right)});
                int unused = FloatLayoutHelper.this.mTransitionX = (((this.parentViewWidth - view.getWidth()) - view.getLeft()) - this.rightMargin) - FloatLayoutHelper.this.mAdjuster.right;
            } else {
                objectAnimator = ObjectAnimator.ofFloat(view, "translationX", new float[]{view.getTranslationX(), (float) ((-view.getLeft()) + this.leftMargin + FloatLayoutHelper.this.mAdjuster.left)});
                int unused2 = FloatLayoutHelper.this.mTransitionX = (-view.getLeft()) + this.leftMargin + FloatLayoutHelper.this.mAdjuster.left;
            }
            int unused3 = FloatLayoutHelper.this.mTransitionY = (int) view.getTranslationY();
            objectAnimator.setDuration(200);
            objectAnimator.start();
        }
    };

    public boolean requireLayoutView() {
        return false;
    }

    public void setBgColor(int i) {
    }

    /* access modifiers changed from: protected */
    public boolean shouldBeDraw(int i, int i2) {
        return true;
    }

    public void setDefaultLocation(int i, int i2) {
        this.mX = i;
        this.mY = i2;
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

    public void setItemCount(int i) {
        if (i > 0) {
            super.setItemCount(1);
        } else {
            super.setItemCount(0);
        }
    }

    public void onRangeChange(int i, int i2) {
        this.mPos = i;
    }

    public void layoutViews(RecyclerView.Recycler recycler, RecyclerView.State state, VirtualLayoutManager.LayoutStateWrapper layoutStateWrapper, LayoutChunkResult layoutChunkResult, LayoutManagerHelper layoutManagerHelper) {
        if (!isOutOfRange(layoutStateWrapper.getCurrentPosition())) {
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
            layoutManagerHelper.getChildViewHolder(view).setIsRecyclable(false);
            this.mDoNormalHandle = state.isPreLayout();
            if (this.mDoNormalHandle) {
                layoutManagerHelper.addChildView(layoutStateWrapper, view);
            }
            this.mFixView = view;
            this.mFixView.setClickable(true);
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
            layoutManagerHelper.recycleView(this.mFixView);
            this.mFixView.setOnTouchListener((View.OnTouchListener) null);
            this.mFixView = null;
        }
        this.mDoNormalHandle = false;
    }

    public void afterLayout(RecyclerView.Recycler recycler, RecyclerView.State state, int i, int i2, int i3, LayoutManagerHelper layoutManagerHelper) {
        super.afterLayout(recycler, state, i, i2, i3, layoutManagerHelper);
        if (this.mPos >= 0) {
            if (this.mDoNormalHandle) {
                this.mFixView = null;
            } else if (!shouldBeDraw(i, i2)) {
            } else {
                if (this.mFixView == null) {
                    this.mFixView = recycler.getViewForPosition(this.mPos);
                    layoutManagerHelper.getChildViewHolder(this.mFixView).setIsRecyclable(false);
                    doMeasureAndLayout(this.mFixView, layoutManagerHelper);
                    layoutManagerHelper.addFixedView(this.mFixView);
                    this.mFixView.setTranslationX((float) this.mTransitionX);
                    this.mFixView.setTranslationY((float) this.mTransitionY);
                    if (this.dragEnable) {
                        this.mFixView.setOnTouchListener(this.touchDragListener);
                    }
                } else if (this.mFixView.getParent() == null) {
                    layoutManagerHelper.addFixedView(this.mFixView);
                    if (this.dragEnable) {
                        this.mFixView.setOnTouchListener(this.touchDragListener);
                    }
                    this.mFixView.setTranslationX((float) this.mTransitionX);
                    this.mFixView.setTranslationY((float) this.mTransitionY);
                } else {
                    layoutManagerHelper.showView(this.mFixView);
                    if (this.dragEnable) {
                        this.mFixView.setOnTouchListener(this.touchDragListener);
                    }
                    layoutManagerHelper.addFixedView(this.mFixView);
                }
            }
        }
    }

    @Nullable
    public View getFixedView() {
        return this.mFixView;
    }

    public void onClear(LayoutManagerHelper layoutManagerHelper) {
        super.onClear(layoutManagerHelper);
        if (this.mFixView != null) {
            this.mFixView.setOnTouchListener((View.OnTouchListener) null);
            layoutManagerHelper.removeChildView(this.mFixView);
            layoutManagerHelper.recycleView(this.mFixView);
            this.mFixView = null;
        }
    }

    private void doMeasureAndLayout(View view, LayoutManagerHelper layoutManagerHelper) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        if (view != null && layoutManagerHelper != null) {
            VirtualLayoutManager.LayoutParams layoutParams = (VirtualLayoutManager.LayoutParams) view.getLayoutParams();
            boolean z = layoutManagerHelper.getOrientation() == 1;
            if (z) {
                int childMeasureSpec = layoutManagerHelper.getChildMeasureSpec((layoutManagerHelper.getContentWidth() - layoutManagerHelper.getPaddingLeft()) - layoutManagerHelper.getPaddingRight(), layoutParams.width, !z);
                if (!Float.isNaN(layoutParams.mAspectRatio) && layoutParams.mAspectRatio > 0.0f) {
                    i6 = layoutManagerHelper.getChildMeasureSpec((layoutManagerHelper.getContentHeight() - layoutManagerHelper.getPaddingTop()) - layoutManagerHelper.getPaddingBottom(), (int) ((((float) View.MeasureSpec.getSize(childMeasureSpec)) / layoutParams.mAspectRatio) + 0.5f), z);
                } else if (Float.isNaN(this.mAspectRatio) || this.mAspectRatio <= 0.0f) {
                    i6 = layoutManagerHelper.getChildMeasureSpec((layoutManagerHelper.getContentHeight() - layoutManagerHelper.getPaddingTop()) - layoutManagerHelper.getPaddingBottom(), layoutParams.height, z);
                } else {
                    i6 = layoutManagerHelper.getChildMeasureSpec((layoutManagerHelper.getContentHeight() - layoutManagerHelper.getPaddingTop()) - layoutManagerHelper.getPaddingBottom(), (int) ((((float) View.MeasureSpec.getSize(childMeasureSpec)) / this.mAspectRatio) + 0.5f), z);
                }
                layoutManagerHelper.measureChild(view, childMeasureSpec, i6);
            } else {
                int childMeasureSpec2 = layoutManagerHelper.getChildMeasureSpec((layoutManagerHelper.getContentHeight() - layoutManagerHelper.getPaddingTop()) - layoutManagerHelper.getPaddingBottom(), layoutParams.height, z);
                if (!Float.isNaN(layoutParams.mAspectRatio) && layoutParams.mAspectRatio > 0.0f) {
                    i5 = layoutManagerHelper.getChildMeasureSpec((layoutManagerHelper.getContentWidth() - layoutManagerHelper.getPaddingLeft()) - layoutManagerHelper.getPaddingRight(), (int) ((((float) View.MeasureSpec.getSize(childMeasureSpec2)) * layoutParams.mAspectRatio) + 0.5f), !z);
                } else if (Float.isNaN(this.mAspectRatio) || this.mAspectRatio <= 0.0f) {
                    i5 = layoutManagerHelper.getChildMeasureSpec((layoutManagerHelper.getContentWidth() - layoutManagerHelper.getPaddingLeft()) - layoutManagerHelper.getPaddingRight(), layoutParams.width, !z);
                } else {
                    i5 = layoutManagerHelper.getChildMeasureSpec((layoutManagerHelper.getContentWidth() - layoutManagerHelper.getPaddingLeft()) - layoutManagerHelper.getPaddingRight(), (int) ((((float) View.MeasureSpec.getSize(childMeasureSpec2)) * this.mAspectRatio) + 0.5f), !z);
                }
                layoutManagerHelper.measureChild(view, i5, childMeasureSpec2);
            }
            OrientationHelperEx mainOrientationHelper = layoutManagerHelper.getMainOrientationHelper();
            if (this.mAlignType == 1) {
                i4 = layoutManagerHelper.getPaddingTop() + this.mY + this.mAdjuster.top;
                i3 = ((layoutManagerHelper.getContentWidth() - layoutManagerHelper.getPaddingRight()) - this.mX) - this.mAdjuster.right;
                i2 = ((i3 - layoutParams.leftMargin) - layoutParams.rightMargin) - view.getMeasuredWidth();
                i = layoutParams.topMargin + i4 + layoutParams.bottomMargin + view.getMeasuredHeight();
            } else if (this.mAlignType == 2) {
                i2 = layoutManagerHelper.getPaddingLeft() + this.mX + this.mAdjuster.left;
                i = ((layoutManagerHelper.getContentHeight() - layoutManagerHelper.getPaddingBottom()) - this.mY) - this.mAdjuster.bottom;
                i3 = view.getMeasuredWidth() + layoutParams.leftMargin + i2 + layoutParams.rightMargin;
                i4 = ((i - view.getMeasuredHeight()) - layoutParams.topMargin) - layoutParams.bottomMargin;
            } else if (this.mAlignType == 3) {
                i3 = ((layoutManagerHelper.getContentWidth() - layoutManagerHelper.getPaddingRight()) - this.mX) - this.mAdjuster.right;
                i = ((layoutManagerHelper.getContentHeight() - layoutManagerHelper.getPaddingBottom()) - this.mY) - this.mAdjuster.bottom;
                i2 = i3 - (z ? mainOrientationHelper.getDecoratedMeasurementInOther(view) : mainOrientationHelper.getDecoratedMeasurement(view));
                i4 = i - (z ? mainOrientationHelper.getDecoratedMeasurement(view) : mainOrientationHelper.getDecoratedMeasurementInOther(view));
            } else {
                i2 = layoutManagerHelper.getPaddingLeft() + this.mX + this.mAdjuster.left;
                i4 = layoutManagerHelper.getPaddingTop() + this.mY + this.mAdjuster.top;
                i3 = (z ? mainOrientationHelper.getDecoratedMeasurementInOther(view) : mainOrientationHelper.getDecoratedMeasurement(view)) + i2;
                i = (z ? mainOrientationHelper.getDecoratedMeasurement(view) : mainOrientationHelper.getDecoratedMeasurementInOther(view)) + i4;
            }
            if (i2 < layoutManagerHelper.getPaddingLeft() + this.mAdjuster.left) {
                i2 = this.mAdjuster.left + layoutManagerHelper.getPaddingLeft();
                i3 = (z ? mainOrientationHelper.getDecoratedMeasurementInOther(view) : mainOrientationHelper.getDecoratedMeasurement(view)) + i2;
            }
            if (i3 > (layoutManagerHelper.getContentWidth() - layoutManagerHelper.getPaddingRight()) - this.mAdjuster.right) {
                i3 = (layoutManagerHelper.getContentWidth() - layoutManagerHelper.getPaddingRight()) - this.mAdjuster.right;
                i2 = ((i3 - layoutParams.leftMargin) - layoutParams.rightMargin) - view.getMeasuredWidth();
            }
            if (i4 < layoutManagerHelper.getPaddingTop() + this.mAdjuster.top) {
                i4 = this.mAdjuster.top + layoutManagerHelper.getPaddingTop();
                i = i4 + (z ? mainOrientationHelper.getDecoratedMeasurement(view) : mainOrientationHelper.getDecoratedMeasurementInOther(view));
            }
            if (i > (layoutManagerHelper.getContentHeight() - layoutManagerHelper.getPaddingBottom()) - this.mAdjuster.bottom) {
                int contentHeight = (layoutManagerHelper.getContentHeight() - layoutManagerHelper.getPaddingBottom()) - this.mAdjuster.bottom;
                i = contentHeight;
                i4 = contentHeight - (z ? mainOrientationHelper.getDecoratedMeasurement(view) : mainOrientationHelper.getDecoratedMeasurementInOther(view));
            }
            layoutChildWithMargin(view, i2, i4, i3, i, layoutManagerHelper);
        }
    }

    public void setDragEnable(boolean z) {
        this.dragEnable = z;
        if (this.mFixView != null) {
            this.mFixView.setOnTouchListener(z ? this.touchDragListener : null);
        }
    }
}
