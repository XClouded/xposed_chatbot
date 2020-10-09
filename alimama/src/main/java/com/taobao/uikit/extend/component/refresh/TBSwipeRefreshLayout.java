package com.taobao.uikit.extend.component.refresh;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import androidx.core.view.MotionEventCompat;
import androidx.core.view.ViewCompat;
import com.taobao.tao.util.SystemBarDecorator;
import com.taobao.tao.util.TBSoundPlayer;
import com.taobao.uikit.extend.R;
import com.taobao.uikit.extend.component.refresh.TBLoadMoreFooter;
import com.taobao.uikit.extend.component.refresh.TBRefreshHeader;
import com.taobao.uikit.extend.utils.DisplayUtil;
import com.taobao.uikit.extend.utils.NavigationBarUtils;

@SuppressLint({"ClickableViewAccessibility"})
public class TBSwipeRefreshLayout extends ViewGroup {
    private static final int ANIMATE_TO_BOTTOM_DURATION = 300;
    private static final int ANIMATE_TO_START_DURATION = 300;
    private static final int ANIMATE_TO_TRIGGER_DURATION = 300;
    private static final float DECELERATE_INTERPOLATION_FACTOR = 2.0f;
    private static final float DRAG_RATE = 1.0f;
    private static int FOOTER_VIEW_HEIGHT = 50;
    private static int FOOTER_VIEW_MAX_HEIGHT = 100;
    private static int HEADER_VIEW_HEIGHT = 72;
    private static final int INVALID_POINTER = -1;
    private static final int[] LAYOUT_ATTRS = {R.attr.uik_swipeRefreshPullRefresh, R.attr.uik_swipeRefreshPushLoad, R.attr.uik_swipeRefreshSecondFloor, R.attr.uik_swipeRefreshHeaderHeight, R.attr.uik_swipeRefreshFooterHeight, R.attr.uik_swipeRefreshLazyPullRefresh, R.attr.uik_swipeRefreshLazyPushLoad};
    private static int MIN_GAP_DISTANCE_TO_SECOND_FLOOR = 20;
    private static final String TAG = "TBSwipeRefreshLayout";
    private int mActivePointerId;
    /* access modifiers changed from: private */
    public long mAutoRefreshDuration;
    private int mContentHeight;
    protected int mCurrentTargetOffsetTop;
    private DecelerateInterpolator mDecelerateInterpolator;
    protected float mDensity;
    protected DisplayMetrics mDisplayMetrics;
    private int mDistance;
    private float mDragRate;
    private boolean mEnableTargetOffset;
    /* access modifiers changed from: private */
    public TBLoadMoreFooter mFooterView;
    protected int mFooterViewHeight;
    private int mFooterViewIndex;
    protected int mFooterViewWidth;
    protected int mFrom;
    /* access modifiers changed from: private */
    public TBRefreshHeader mHeaderView;
    protected int mHeaderViewHeight;
    private int mHeaderViewIndex;
    protected int mHeaderViewWidth;
    private float mInitialMotionY;
    private boolean mIsBeingDragged;
    private boolean mIsMultiPointer;
    private float mLastMotionY;
    private boolean mLazyLoadMoreEnable;
    private boolean mLazyPullRefreshEnable;
    private boolean mLoadMoreEnabled;
    /* access modifiers changed from: private */
    public boolean mLoadingMore;
    private int mMaxPushDistance;
    private int mNavigationBarHeight;
    /* access modifiers changed from: private */
    public boolean mNotify;
    /* access modifiers changed from: private */
    public OnPushLoadMoreListener mOnPushLoadMoreListener;
    private boolean mOriginalOffsetCalculated;
    protected int mOriginalOffsetTop;
    private int mPositionY;
    private int mPreActivePointerId;
    private int mPreDistance;
    private int mPrePositionY;
    private boolean mPullRefreshEnabled;
    /* access modifiers changed from: private */
    public OnPullRefreshListener mPullRefreshListener;
    /* access modifiers changed from: private */
    public int mPushDistance;
    /* access modifiers changed from: private */
    public Animator.AnimatorListener mRefreshListener;
    protected int mRefreshOffset;
    /* access modifiers changed from: private */
    public boolean mRefreshing;
    private int mSecondFloorDistance;
    private boolean mSecondFloorEnabled;
    private int mStartY;
    private View mTarget;
    private boolean mTargetScrollWithLayout;
    private int mTotalDragDistance;
    protected int mTouchSlop;

    public interface OnPullRefreshListener {
        void onPullDistance(int i);

        void onRefresh();

        void onRefreshStateChanged(TBRefreshHeader.RefreshState refreshState, TBRefreshHeader.RefreshState refreshState2);
    }

    public interface OnPushLoadMoreListener {
        void onLoadMore();

        void onLoadMoreStateChanged(TBLoadMoreFooter.LoadMoreState loadMoreState, TBLoadMoreFooter.LoadMoreState loadMoreState2);

        void onPushDistance(int i);
    }

    public TBSwipeRefreshLayout(Context context) {
        this(context, (AttributeSet) null);
    }

    public TBSwipeRefreshLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mHeaderViewIndex = -1;
        this.mFooterViewIndex = -1;
        this.mLazyPullRefreshEnable = false;
        this.mLazyLoadMoreEnable = false;
        this.mTargetScrollWithLayout = true;
        this.mActivePointerId = -1;
        this.mDragRate = 1.0f;
        this.mTotalDragDistance = -1;
        this.mSecondFloorDistance = -1;
        this.mEnableTargetOffset = true;
        this.mPushDistance = 0;
        this.mAutoRefreshDuration = 2000;
        this.mIsMultiPointer = false;
        this.mPositionY = 0;
        this.mPrePositionY = 0;
        this.mPreActivePointerId = -1;
        this.mContentHeight = -1;
        this.mNavigationBarHeight = -1;
        this.mRefreshListener = new Animator.AnimatorListener() {
            public void onAnimationCancel(Animator animator) {
            }

            public void onAnimationRepeat(Animator animator) {
            }

            public void onAnimationStart(Animator animator) {
            }

            public void onAnimationEnd(Animator animator) {
                if (TBSwipeRefreshLayout.this.mHeaderView != null) {
                    if (TBSwipeRefreshLayout.this.mRefreshing) {
                        if (TBSwipeRefreshLayout.this.mNotify && TBSwipeRefreshLayout.this.mPullRefreshListener != null) {
                            TBSwipeRefreshLayout.this.mPullRefreshListener.onRefresh();
                        }
                        TBSwipeRefreshLayout.this.mHeaderView.changeToState(TBRefreshHeader.RefreshState.REFRESHING);
                    } else {
                        TBSwipeRefreshLayout.this.updateHeaderPosition(TBSwipeRefreshLayout.this.mOriginalOffsetTop - TBSwipeRefreshLayout.this.mCurrentTargetOffsetTop);
                    }
                    TBSwipeRefreshLayout.this.mCurrentTargetOffsetTop = TBSwipeRefreshLayout.this.mHeaderView.getTop();
                    TBSwipeRefreshLayout.this.updatePullListenerCallBack();
                }
            }
        };
        setWillNotDraw(false);
        this.mDisplayMetrics = DisplayUtil.getDisplayMetrics(getContext());
        this.mDensity = this.mDisplayMetrics.density;
        HEADER_VIEW_HEIGHT = (int) getResources().getDimension(R.dimen.uik_refresh_header_height);
        MIN_GAP_DISTANCE_TO_SECOND_FLOOR = (int) getResources().getDimension(R.dimen.uik_refresh_second_floor_gap);
        FOOTER_VIEW_HEIGHT = (int) getResources().getDimension(R.dimen.uik_refresh_footer_height);
        FOOTER_VIEW_MAX_HEIGHT = (int) getResources().getDimension(R.dimen.uik_refresh_footer_max_height);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, LAYOUT_ATTRS);
        this.mPullRefreshEnabled = obtainStyledAttributes.getBoolean(0, false);
        this.mLoadMoreEnabled = obtainStyledAttributes.getBoolean(1, false);
        this.mSecondFloorEnabled = obtainStyledAttributes.getBoolean(2, false);
        this.mLazyPullRefreshEnable = obtainStyledAttributes.getBoolean(5, false);
        this.mLazyLoadMoreEnable = obtainStyledAttributes.getBoolean(6, false);
        if (!this.mSecondFloorEnabled || this.mPullRefreshEnabled) {
            this.mHeaderViewWidth = this.mDisplayMetrics.widthPixels;
            this.mHeaderViewHeight = (int) obtainStyledAttributes.getDimension(3, (float) HEADER_VIEW_HEIGHT);
            this.mFooterViewWidth = this.mDisplayMetrics.widthPixels;
            this.mFooterViewHeight = (int) obtainStyledAttributes.getDimension(4, (float) FOOTER_VIEW_HEIGHT);
            obtainStyledAttributes.recycle();
            if (this.mNavigationBarHeight == -1) {
                this.mNavigationBarHeight = NavigationBarUtils.getNavigationBarHeight((Activity) getContext());
            }
            if (this.mContentHeight == -1) {
                this.mContentHeight = NavigationBarUtils.getContentHeight((Activity) getContext());
            }
            this.mDecelerateInterpolator = new DecelerateInterpolator(DECELERATE_INTERPOLATION_FACTOR);
            this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
            if (!this.mLazyPullRefreshEnable) {
                createHeaderView();
            }
            if (!this.mLazyLoadMoreEnable) {
                createFooterView();
            }
            ViewCompat.setChildrenDrawingOrderEnabled(this, true);
            this.mRefreshOffset = 0;
            this.mTotalDragDistance = HEADER_VIEW_HEIGHT;
            this.mSecondFloorDistance = this.mTotalDragDistance + MIN_GAP_DISTANCE_TO_SECOND_FLOOR;
            this.mMaxPushDistance = FOOTER_VIEW_MAX_HEIGHT;
            int i = (-this.mContentHeight) + this.mRefreshOffset;
            this.mOriginalOffsetTop = i;
            this.mCurrentTargetOffsetTop = i;
            return;
        }
        Log.e(TAG, "Cannot enable second floor when pull to refresh disabled!");
    }

    /* access modifiers changed from: protected */
    public void createHeaderView() {
        this.mHeaderView = new TBOldRefreshHeader(getContext());
        if (this.mPullRefreshListener != null) {
            this.mHeaderView.setPullRefreshListener(this.mPullRefreshListener);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, -2);
        layoutParams.addRule(14);
        layoutParams.addRule(12);
        addView(this.mHeaderView, layoutParams);
    }

    /* access modifiers changed from: protected */
    public void createFooterView() {
        this.mFooterView = new TBDefaultLoadMoreFooter(getContext());
        if (this.mOnPushLoadMoreListener != null) {
            this.mFooterView.setPushLoadMoreListener(this.mOnPushLoadMoreListener);
        }
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(-1, this.mFooterViewHeight);
        layoutParams.addRule(14);
        layoutParams.addRule(10);
        addView(this.mFooterView, layoutParams);
    }

    /* access modifiers changed from: protected */
    public int getChildDrawingOrder(int i, int i2) {
        if (this.mHeaderViewIndex < 0 && this.mFooterViewIndex < 0) {
            return i2;
        }
        if (this.mHeaderViewIndex < 0 || this.mFooterViewIndex < 0) {
            int i3 = this.mHeaderViewIndex < 0 ? this.mFooterViewIndex : this.mHeaderViewIndex;
            if (i2 == i - 1) {
                return i3;
            }
            return i2 >= i3 ? i2 + 1 : i2;
        } else if (i2 == i - 2) {
            return this.mHeaderViewIndex;
        } else {
            if (i2 == i - 1) {
                return this.mFooterViewIndex;
            }
            int i4 = this.mFooterViewIndex > this.mHeaderViewIndex ? this.mFooterViewIndex : this.mHeaderViewIndex;
            if (i2 < (this.mFooterViewIndex < this.mHeaderViewIndex ? this.mFooterViewIndex : this.mHeaderViewIndex) || i2 >= i4 - 1) {
                return (i2 >= i4 || i2 == i4 + -1) ? i2 + 2 : i2;
            }
            return i2 + 1;
        }
    }

    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mTarget == null) {
            ensureTarget();
        }
        if (this.mTarget != null) {
            this.mTarget.measure(View.MeasureSpec.makeMeasureSpec((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), 1073741824), View.MeasureSpec.makeMeasureSpec((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), 1073741824));
            this.mHeaderViewIndex = -1;
            if (this.mHeaderView != null) {
                this.mHeaderViewWidth = DisplayUtil.getScreenWidth(getContext());
                this.mHeaderView.measure(View.MeasureSpec.makeMeasureSpec(this.mHeaderViewWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mContentHeight, 1073741824));
                int i3 = 0;
                while (true) {
                    if (i3 >= getChildCount()) {
                        break;
                    } else if (getChildAt(i3) == this.mHeaderView) {
                        this.mHeaderViewIndex = i3;
                        break;
                    } else {
                        i3++;
                    }
                }
            }
            this.mFooterViewIndex = -1;
            if (this.mFooterView != null) {
                this.mFooterViewWidth = DisplayUtil.getScreenWidth(getContext());
                this.mFooterView.measure(View.MeasureSpec.makeMeasureSpec(this.mFooterViewWidth, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mMaxPushDistance, 1073741824));
                for (int i4 = 0; i4 < getChildCount(); i4++) {
                    if (getChildAt(i4) == this.mFooterView) {
                        this.mFooterViewIndex = i4;
                        return;
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (getChildCount() != 0) {
            if (this.mTarget == null) {
                ensureTarget();
            }
            if (this.mTarget != null) {
                int i5 = this.mCurrentTargetOffsetTop + this.mContentHeight;
                if (!this.mTargetScrollWithLayout) {
                    i5 = 0;
                }
                View view = this.mTarget;
                int paddingLeft = getPaddingLeft();
                int paddingTop = (getPaddingTop() + i5) - this.mPushDistance;
                view.layout(paddingLeft, this.mEnableTargetOffset ? paddingTop : paddingTop - this.mRefreshOffset, ((measuredWidth - getPaddingLeft()) - getPaddingRight()) + paddingLeft, (paddingTop + ((measuredHeight - getPaddingTop()) - getPaddingBottom())) - this.mRefreshOffset);
                if (this.mHeaderView != null) {
                    this.mHeaderViewWidth = DisplayUtil.getScreenWidth(getContext());
                    this.mHeaderView.layout(0, this.mCurrentTargetOffsetTop, this.mHeaderViewWidth, this.mCurrentTargetOffsetTop + this.mContentHeight);
                }
                if (!this.mSecondFloorEnabled && this.mHeaderView != null) {
                    this.mHeaderView.getSecondFloorView().setVisibility(8);
                }
                if (this.mFooterView != null) {
                    this.mFooterViewWidth = DisplayUtil.getScreenWidth(getContext());
                    this.mFooterView.layout(0, measuredHeight - this.mPushDistance, this.mFooterViewWidth, (measuredHeight + this.mMaxPushDistance) - this.mPushDistance);
                }
            }
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        ensureTarget();
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        boolean isChildScrollToTop = this.mPullRefreshEnabled ? isChildScrollToTop() : false;
        if (!(isChildScrollToTop || this.mHeaderView == null || this.mHeaderView.getCurrentState() == TBRefreshHeader.RefreshState.NONE)) {
            isChildScrollToTop = true;
        }
        if (this.mHeaderView == null || this.mHeaderView.getCurrentState() == TBRefreshHeader.RefreshState.SECOND_FLOOR_START || this.mHeaderView.getCurrentState() == TBRefreshHeader.RefreshState.SECOND_FLOOR_END) {
            isChildScrollToTop = false;
        }
        boolean isChildScrollToBottom = (this.mFooterView != null && this.mLoadMoreEnabled) ? isChildScrollToBottom() : false;
        if (!(isChildScrollToBottom || this.mFooterView == null || this.mFooterView.getCurrentState() == TBLoadMoreFooter.LoadMoreState.NONE)) {
            isChildScrollToBottom = true;
        }
        if (!isChildScrollToTop && !isChildScrollToBottom) {
            return false;
        }
        if (actionMasked != 6) {
            switch (actionMasked) {
                case 0:
                    this.mStartY = (int) motionEvent.getY();
                    int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
                    if (this.mActivePointerId == -1) {
                        this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                        this.mLastMotionY = motionEvent.getY();
                        this.mPreActivePointerId = this.mActivePointerId;
                    }
                    this.mIsBeingDragged = false;
                    float motionEventY = getMotionEventY(motionEvent, this.mActivePointerId);
                    if (motionEventY != -1.0f) {
                        this.mInitialMotionY = motionEventY;
                        if (this.mHeaderView != null && this.mHeaderView.getCurrentState() == TBRefreshHeader.RefreshState.REFRESHING) {
                            setRefreshing(false);
                        }
                        if (this.mFooterView != null && this.mFooterView.getCurrentState() == TBLoadMoreFooter.LoadMoreState.LOADING) {
                            setLoadMore(false);
                            break;
                        }
                    } else {
                        return false;
                    }
                case 1:
                case 3:
                    this.mIsBeingDragged = false;
                    this.mActivePointerId = -1;
                    break;
                case 2:
                    if (this.mActivePointerId != -1) {
                        float motionEventY2 = getMotionEventY(motionEvent, this.mActivePointerId);
                        if (motionEventY2 != -1.0f) {
                            if (!isChildScrollToBottom()) {
                                if (isChildScrollToTop() && motionEventY2 - this.mInitialMotionY > ((float) this.mTouchSlop) && !this.mIsBeingDragged) {
                                    this.mIsBeingDragged = true;
                                    break;
                                }
                            } else if (this.mInitialMotionY - motionEventY2 > ((float) this.mTouchSlop) && !this.mIsBeingDragged) {
                                this.mIsBeingDragged = true;
                                break;
                            }
                        } else {
                            return false;
                        }
                    } else {
                        Log.e(TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
                        return false;
                    }
            }
        } else {
            onSecondaryPointerUp(motionEvent);
        }
        return this.mIsBeingDragged;
    }

    private float getMotionEventY(MotionEvent motionEvent, int i) {
        int findPointerIndex = MotionEventCompat.findPointerIndex(motionEvent, i);
        if (findPointerIndex < 0) {
            return -1.0f;
        }
        return MotionEventCompat.getY(motionEvent, findPointerIndex);
    }

    public boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = MotionEventCompat.getActionMasked(motionEvent);
        boolean isChildScrollToTop = this.mPullRefreshEnabled ? isChildScrollToTop() : false;
        boolean z = true;
        if (!(isChildScrollToTop || this.mHeaderView == null || this.mHeaderView.getCurrentState() == TBRefreshHeader.RefreshState.NONE)) {
            isChildScrollToTop = true;
        }
        if (this.mHeaderView == null || this.mHeaderView.getCurrentState() == TBRefreshHeader.RefreshState.SECOND_FLOOR_START || this.mHeaderView.getCurrentState() == TBRefreshHeader.RefreshState.SECOND_FLOOR_END) {
            isChildScrollToTop = false;
        }
        boolean isChildScrollToBottom = (this.mFooterView != null && this.mLoadMoreEnabled) ? isChildScrollToBottom() : false;
        if (isChildScrollToBottom || this.mFooterView == null || this.mFooterView.getCurrentState() == TBLoadMoreFooter.LoadMoreState.NONE) {
            z = isChildScrollToBottom;
        }
        if (!isChildScrollToTop && !z) {
            return false;
        }
        if (isChildScrollToTop) {
            return handlePullTouchEvent(motionEvent, actionMasked);
        }
        if (z) {
            return handlePushTouchEvent(motionEvent, actionMasked);
        }
        return false;
    }

    private boolean handlePullTouchEvent(MotionEvent motionEvent, int i) {
        int i2;
        switch (i) {
            case 1:
            case 3:
                if (this.mActivePointerId == -1) {
                    if (i == 1) {
                        Log.e(TAG, "Got ACTION_UP event but don't have an active pointer id.");
                    }
                    return false;
                }
                this.mIsBeingDragged = false;
                if (this.mHeaderView.getCurrentState() == TBRefreshHeader.RefreshState.PREPARE_TO_SECOND_FLOOR && this.mSecondFloorEnabled) {
                    this.mHeaderView.changeToState(TBRefreshHeader.RefreshState.SECOND_FLOOR_START);
                    animateOffsetToBottomPosition(this.mCurrentTargetOffsetTop, new Animator.AnimatorListener() {
                        public void onAnimationCancel(Animator animator) {
                        }

                        public void onAnimationRepeat(Animator animator) {
                        }

                        public void onAnimationStart(Animator animator) {
                        }

                        public void onAnimationEnd(Animator animator) {
                            TBSwipeRefreshLayout.this.mHeaderView.changeToState(TBRefreshHeader.RefreshState.SECOND_FLOOR_END);
                        }
                    });
                } else if (this.mHeaderView.getCurrentState() == TBRefreshHeader.RefreshState.RELEASE_TO_REFRESH) {
                    setRefreshing(true, true);
                } else {
                    this.mRefreshing = false;
                    this.mHeaderView.changeToState(TBRefreshHeader.RefreshState.NONE);
                    animateOffsetToStartPosition(this.mCurrentTargetOffsetTop, (Animator.AnimatorListener) null);
                }
                this.mActivePointerId = -1;
                this.mIsMultiPointer = false;
                this.mDistance = 0;
                this.mPositionY = 0;
                return false;
            case 2:
                if (this.mActivePointerId == -1) {
                    this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, MotionEventCompat.getActionIndex(motionEvent));
                    this.mLastMotionY = motionEvent.getY();
                    this.mPreActivePointerId = this.mActivePointerId;
                }
                try {
                    int y = (int) MotionEventCompat.getY(motionEvent, getPointerIndex(motionEvent, this.mActivePointerId));
                    if (!this.mIsMultiPointer) {
                        i2 = y - this.mStartY;
                        this.mDistance = i2;
                        this.mPreDistance = i2;
                        this.mPositionY = y;
                        this.mPrePositionY = y;
                    } else if (this.mPreActivePointerId == this.mActivePointerId) {
                        float f = (float) y;
                        i2 = (int) (((float) this.mDistance) + (f - this.mLastMotionY));
                        this.mPreDistance = i2;
                        this.mPrePositionY = (int) (((float) this.mPositionY) + (f - this.mLastMotionY));
                    } else {
                        i2 = (int) (((float) this.mPreDistance) + (((float) y) - this.mLastMotionY));
                        int i3 = this.mPrePositionY;
                        float f2 = this.mLastMotionY;
                        this.mPreActivePointerId = this.mActivePointerId;
                        this.mDistance = this.mPreDistance;
                        this.mPositionY = this.mPrePositionY;
                    }
                    if (this.mIsBeingDragged) {
                        int i4 = (int) (((float) i2) * this.mDragRate);
                        double d = (double) (((float) this.mDisplayMetrics.heightPixels) / ((float) (this.mDisplayMetrics.heightPixels + i4)));
                        Double.isNaN(d);
                        int i5 = this.mDisplayMetrics.heightPixels;
                        int min = Math.min(i5, (int) (((float) i4) * ((float) (d / 1.1d))));
                        float f3 = (((float) min) * 1.0f) / ((float) this.mTotalDragDistance);
                        if (f3 >= 0.0f) {
                            float min2 = Math.min(1.0f, Math.abs(f3));
                            if (min < this.mTotalDragDistance) {
                                this.mHeaderView.changeToState(TBRefreshHeader.RefreshState.PULL_TO_REFRESH);
                            } else if (!this.mSecondFloorEnabled) {
                                this.mHeaderView.changeToState(TBRefreshHeader.RefreshState.RELEASE_TO_REFRESH);
                            } else if (min > this.mSecondFloorDistance) {
                                this.mHeaderView.changeToState(TBRefreshHeader.RefreshState.PREPARE_TO_SECOND_FLOOR);
                            } else {
                                this.mHeaderView.changeToState(TBRefreshHeader.RefreshState.RELEASE_TO_REFRESH);
                            }
                            this.mHeaderView.setProgress(min2);
                            updateHeaderPosition(min - (this.mCurrentTargetOffsetTop - this.mOriginalOffsetTop));
                            break;
                        } else {
                            return false;
                        }
                    }
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    break;
                }
                break;
            case 5:
                int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
                if (actionIndex >= 0) {
                    this.mLastMotionY = MotionEventCompat.getY(motionEvent, actionIndex);
                    this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                    this.mIsMultiPointer = true;
                    break;
                } else {
                    return false;
                }
            case 6:
                onSecondaryPointerUp(motionEvent);
                break;
        }
        return true;
    }

    private boolean handlePushTouchEvent(MotionEvent motionEvent, int i) {
        boolean z = false;
        switch (i) {
            case 0:
                this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, 0);
                this.mIsBeingDragged = false;
                break;
            case 1:
            case 3:
                if (this.mActivePointerId == -1) {
                    if (i == 1) {
                        Log.e(TAG, "Got ACTION_UP event but don't have an active pointer id.");
                    }
                    return false;
                }
                int findPointerIndex = MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId);
                if (findPointerIndex < 0) {
                    return false;
                }
                float min = Math.min((this.mInitialMotionY - MotionEventCompat.getY(motionEvent, findPointerIndex)) * this.mDragRate, (float) this.mMaxPushDistance);
                this.mIsBeingDragged = false;
                this.mActivePointerId = -1;
                if (min < ((float) this.mFooterViewHeight) || this.mOnPushLoadMoreListener == null) {
                    this.mPushDistance = 0;
                } else {
                    this.mPushDistance = this.mFooterViewHeight;
                }
                animatorFooterToBottom((int) min, this.mPushDistance);
                return false;
            case 2:
                int findPointerIndex2 = MotionEventCompat.findPointerIndex(motionEvent, this.mActivePointerId);
                if (findPointerIndex2 >= 0) {
                    float y = (this.mInitialMotionY - MotionEventCompat.getY(motionEvent, findPointerIndex2)) * this.mDragRate;
                    if (this.mIsBeingDragged) {
                        this.mPushDistance = Math.min((int) y, this.mMaxPushDistance);
                        updateFooterPosition();
                        if (this.mOnPushLoadMoreListener != null) {
                            if (this.mPushDistance >= this.mFooterViewHeight) {
                                z = true;
                            }
                            if (!z) {
                                this.mFooterView.changeToState(TBLoadMoreFooter.LoadMoreState.PUSH_TO_LOAD);
                                break;
                            } else {
                                this.mFooterView.changeToState(TBLoadMoreFooter.LoadMoreState.RELEASE_TO_LOAD);
                                break;
                            }
                        }
                    }
                } else {
                    Log.e(TAG, "Got ACTION_MOVE event but have an invalid active pointer id.");
                    return false;
                }
                break;
            case 5:
                this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, MotionEventCompat.getActionIndex(motionEvent));
                break;
            case 6:
                onSecondaryPointerUp(motionEvent);
                break;
        }
        return true;
    }

    private void ensureTarget() {
        if (this.mTarget == null) {
            int i = 0;
            while (i < getChildCount()) {
                View childAt = getChildAt(i);
                if (childAt.equals(this.mHeaderView) || childAt.equals(this.mFooterView)) {
                    i++;
                } else {
                    this.mTarget = childAt;
                    return;
                }
            }
        }
    }

    public boolean isChildScrollToTop() {
        return !ViewCompat.canScrollVertically(this.mTarget, -1);
    }

    /* JADX WARNING: Removed duplicated region for block: B:45:0x00ca A[RETURN] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isChildScrollToBottom() {
        /*
            r6 = this;
            boolean r0 = r6.isChildScrollToTop()
            r1 = 0
            if (r0 == 0) goto L_0x0008
            return r1
        L_0x0008:
            android.view.View r0 = r6.mTarget
            boolean r0 = r0 instanceof androidx.recyclerview.widget.RecyclerView
            r2 = 1
            if (r0 == 0) goto L_0x0048
            android.view.View r0 = r6.mTarget
            androidx.recyclerview.widget.RecyclerView r0 = (androidx.recyclerview.widget.RecyclerView) r0
            androidx.recyclerview.widget.RecyclerView$LayoutManager r3 = r0.getLayoutManager()
            androidx.recyclerview.widget.RecyclerView$Adapter r0 = r0.getAdapter()
            int r0 = r0.getItemCount()
            boolean r4 = r3 instanceof androidx.recyclerview.widget.LinearLayoutManager
            if (r4 == 0) goto L_0x002f
            if (r0 <= 0) goto L_0x002f
            androidx.recyclerview.widget.LinearLayoutManager r3 = (androidx.recyclerview.widget.LinearLayoutManager) r3
            int r3 = r3.findLastCompletelyVisibleItemPosition()
            int r0 = r0 - r2
            if (r3 != r0) goto L_0x0047
            return r2
        L_0x002f:
            boolean r4 = r3 instanceof androidx.recyclerview.widget.StaggeredGridLayoutManager
            if (r4 == 0) goto L_0x0047
            androidx.recyclerview.widget.StaggeredGridLayoutManager r3 = (androidx.recyclerview.widget.StaggeredGridLayoutManager) r3
            r4 = 2
            int[] r4 = new int[r4]
            r3.findLastCompletelyVisibleItemPositions(r4)
            r3 = r4[r1]
            r4 = r4[r2]
            int r3 = java.lang.Math.max(r3, r4)
            int r0 = r0 - r2
            if (r3 != r0) goto L_0x0047
            return r2
        L_0x0047:
            return r1
        L_0x0048:
            android.view.View r0 = r6.mTarget
            boolean r0 = r0 instanceof android.widget.AbsListView
            if (r0 == 0) goto L_0x007e
            android.view.View r0 = r6.mTarget
            android.widget.AbsListView r0 = (android.widget.AbsListView) r0
            android.widget.Adapter r3 = r0.getAdapter()
            android.widget.ListAdapter r3 = (android.widget.ListAdapter) r3
            int r3 = r3.getCount()
            int r4 = r0.getFirstVisiblePosition()
            if (r4 != 0) goto L_0x0071
            android.view.View r4 = r0.getChildAt(r1)
            int r4 = r4.getTop()
            int r5 = r0.getPaddingTop()
            if (r4 < r5) goto L_0x0071
            return r1
        L_0x0071:
            int r0 = r0.getLastVisiblePosition()
            if (r0 <= 0) goto L_0x007d
            if (r3 <= 0) goto L_0x007d
            int r3 = r3 - r2
            if (r0 != r3) goto L_0x007d
            return r2
        L_0x007d:
            return r1
        L_0x007e:
            android.view.View r0 = r6.mTarget
            boolean r0 = r0 instanceof android.widget.ScrollView
            if (r0 == 0) goto L_0x00a4
            android.view.View r0 = r6.mTarget
            android.widget.ScrollView r0 = (android.widget.ScrollView) r0
            int r3 = r0.getChildCount()
            int r3 = r3 - r2
            android.view.View r3 = r0.getChildAt(r3)
            if (r3 == 0) goto L_0x00ca
            int r3 = r3.getBottom()
            int r4 = r0.getHeight()
            int r0 = r0.getScrollY()
            int r4 = r4 + r0
            int r3 = r3 - r4
            if (r3 != 0) goto L_0x00ca
            return r2
        L_0x00a4:
            android.view.View r0 = r6.mTarget
            boolean r0 = r0 instanceof androidx.core.widget.NestedScrollView
            if (r0 == 0) goto L_0x00ca
            android.view.View r0 = r6.mTarget
            androidx.core.widget.NestedScrollView r0 = (androidx.core.widget.NestedScrollView) r0
            int r3 = r0.getChildCount()
            int r3 = r3 - r2
            android.view.View r3 = r0.getChildAt(r3)
            if (r3 == 0) goto L_0x00ca
            int r3 = r3.getBottom()
            int r4 = r0.getHeight()
            int r0 = r0.getScrollY()
            int r4 = r4 + r0
            int r3 = r3 - r4
            if (r3 != 0) goto L_0x00ca
            return r2
        L_0x00ca:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.uikit.extend.component.refresh.TBSwipeRefreshLayout.isChildScrollToBottom():boolean");
    }

    public void setRefreshing(boolean z) {
        if (this.mHeaderView != null) {
            if (!z || this.mRefreshing == z) {
                setRefreshing(z, false);
                return;
            }
            this.mRefreshing = z;
            updateHeaderPosition((this.mHeaderViewHeight + this.mOriginalOffsetTop) - this.mCurrentTargetOffsetTop);
            this.mNotify = false;
            startScaleUpAnimation(this.mRefreshListener);
        }
    }

    public void setAutoRefreshing(boolean z) {
        if (this.mHeaderView != null) {
            this.mNotify = z;
            ensureTarget();
            this.mRefreshing = true;
            this.mHeaderView.changeToState(TBRefreshHeader.RefreshState.REFRESHING);
            animateOffsetToCorrectPosition(this.mCurrentTargetOffsetTop, new Animator.AnimatorListener() {
                public void onAnimationCancel(Animator animator) {
                }

                public void onAnimationRepeat(Animator animator) {
                }

                public void onAnimationStart(Animator animator) {
                }

                public void onAnimationEnd(Animator animator) {
                    TBSwipeRefreshLayout.this.mRefreshListener.onAnimationEnd(animator);
                    new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                        public void run() {
                            TBSwipeRefreshLayout.this.setRefreshing(false);
                        }
                    }, TBSwipeRefreshLayout.this.mAutoRefreshDuration);
                }
            });
        }
    }

    public void setAutoRefreshingDuration(long j) {
        if (j > 0) {
            this.mAutoRefreshDuration = j;
        }
    }

    private void setRefreshing(boolean z, boolean z2) {
        if (this.mHeaderView != null) {
            if (this.mRefreshing != z) {
                this.mNotify = z2;
                ensureTarget();
                this.mRefreshing = z;
                if (this.mRefreshing) {
                    this.mHeaderView.changeToState(TBRefreshHeader.RefreshState.REFRESHING);
                    animateOffsetToCorrectPosition(this.mCurrentTargetOffsetTop, this.mRefreshListener);
                    return;
                }
                this.mHeaderView.changeToState(TBRefreshHeader.RefreshState.NONE);
                animateOffsetToStartPosition(this.mCurrentTargetOffsetTop, this.mRefreshListener);
                TBSoundPlayer.getInstance().playScene(2);
            } else if (this.mHeaderView.getCurrentState() == TBRefreshHeader.RefreshState.SECOND_FLOOR_END) {
                this.mRefreshing = false;
                this.mHeaderView.changeToState(TBRefreshHeader.RefreshState.NONE);
                animateOffsetToStartPosition(this.mCurrentTargetOffsetTop, this.mRefreshListener);
            }
        }
    }

    public void setLoadMore(boolean z) {
        if (this.mFooterView != null && !z && this.mLoadingMore) {
            animatorFooterToBottom(this.mFooterViewHeight, 0);
        }
    }

    private void startScaleUpAnimation(Animator.AnimatorListener animatorListener) {
        this.mHeaderView.setVisibility(0);
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{0, 0});
        ofInt.setDuration((long) getResources().getInteger(17694721));
        if (animatorListener != null) {
            ofInt.addListener(animatorListener);
        }
        ofInt.start();
    }

    public boolean isRefreshing() {
        return (getRefresHeader() == null || getRefresHeader().getCurrentState() == TBRefreshHeader.RefreshState.NONE) ? false : true;
    }

    @TargetApi(11)
    private void animatorFooterToBottom(int i, final int i2) {
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{i, i2});
        ofInt.setDuration(150);
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int unused = TBSwipeRefreshLayout.this.mPushDistance = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                TBSwipeRefreshLayout.this.updateFooterPosition();
            }
        });
        ofInt.addListener(new AnimatorListenerAdapter() {
            public void onAnimationEnd(Animator animator) {
                if (i2 <= 0 || TBSwipeRefreshLayout.this.mOnPushLoadMoreListener == null) {
                    boolean unused = TBSwipeRefreshLayout.this.mLoadingMore = false;
                    TBSwipeRefreshLayout.this.mFooterView.changeToState(TBLoadMoreFooter.LoadMoreState.NONE);
                    return;
                }
                boolean unused2 = TBSwipeRefreshLayout.this.mLoadingMore = true;
                TBSwipeRefreshLayout.this.mFooterView.changeToState(TBLoadMoreFooter.LoadMoreState.LOADING);
                TBSwipeRefreshLayout.this.mOnPushLoadMoreListener.onLoadMore();
            }
        });
        ofInt.setInterpolator(this.mDecelerateInterpolator);
        ofInt.start();
    }

    private void animateOffsetToCorrectPosition(int i, Animator.AnimatorListener animatorListener) {
        int i2;
        this.mFrom = i;
        if (this.mEnableTargetOffset) {
            i2 = (this.mHeaderViewHeight - Math.abs(this.mOriginalOffsetTop)) - this.mRefreshOffset;
        } else {
            i2 = this.mHeaderViewHeight - Math.abs(this.mOriginalOffsetTop);
        }
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{this.mFrom, i2});
        if (animatorListener != null) {
            ofInt.addListener(animatorListener);
        }
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                TBSwipeRefreshLayout.this.updateHeaderPosition(((Integer) valueAnimator.getAnimatedValue()).intValue() - TBSwipeRefreshLayout.this.mHeaderView.getTop());
            }
        });
        ofInt.setDuration(300);
        ofInt.setInterpolator(this.mDecelerateInterpolator);
        ofInt.start();
    }

    private void animateOffsetToStartPosition(int i, Animator.AnimatorListener animatorListener) {
        this.mFrom = i;
        ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{this.mFrom, this.mOriginalOffsetTop});
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                TBSwipeRefreshLayout.this.mHeaderView.setProgress(((float) (intValue - TBSwipeRefreshLayout.this.mFrom)) / (((float) (TBSwipeRefreshLayout.this.mOriginalOffsetTop - TBSwipeRefreshLayout.this.mFrom)) * 1.0f));
                TBSwipeRefreshLayout.this.updateHeaderPosition(intValue - TBSwipeRefreshLayout.this.mHeaderView.getTop());
            }
        });
        ofInt.setDuration(300);
        ofInt.setInterpolator(this.mDecelerateInterpolator);
        if (animatorListener != null) {
            ofInt.addListener(animatorListener);
        }
        ofInt.start();
    }

    private void animateOffsetToBottomPosition(int i, Animator.AnimatorListener animatorListener) {
        this.mFrom = i;
        new ValueAnimator();
        final ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{this.mFrom, 0});
        ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                TBSwipeRefreshLayout.this.updateHeaderPosition(((Integer) ofInt.getAnimatedValue()).intValue() - TBSwipeRefreshLayout.this.mHeaderView.getTop());
            }
        });
        ofInt.setDuration(300);
        ofInt.setInterpolator(this.mDecelerateInterpolator);
        if (animatorListener != null) {
            ofInt.addListener(animatorListener);
        }
        ofInt.start();
    }

    /* access modifiers changed from: private */
    public void updateHeaderPosition(int i) {
        this.mHeaderView.bringToFront();
        this.mHeaderView.offsetTopAndBottom(i);
        this.mTarget.offsetTopAndBottom(i);
        this.mCurrentTargetOffsetTop = this.mHeaderView.getTop();
        updatePullListenerCallBack();
    }

    /* access modifiers changed from: private */
    public void updateFooterPosition() {
        this.mFooterView.setVisibility(0);
        this.mFooterView.bringToFront();
        if (Build.VERSION.SDK_INT < 19) {
            this.mFooterView.getParent().requestLayout();
        }
        this.mFooterView.offsetTopAndBottom(-this.mPushDistance);
        updateLoadMoreListener();
    }

    /* access modifiers changed from: private */
    public void updatePullListenerCallBack() {
        int i = this.mCurrentTargetOffsetTop - this.mOriginalOffsetTop;
        if (this.mPullRefreshListener != null) {
            this.mPullRefreshListener.onPullDistance(i);
        }
    }

    private void updateLoadMoreListener() {
        if (this.mOnPushLoadMoreListener != null) {
            this.mOnPushLoadMoreListener.onPushDistance(this.mPushDistance);
        }
    }

    private void onSecondaryPointerUp(MotionEvent motionEvent) {
        int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
        if (MotionEventCompat.getPointerId(motionEvent, actionIndex) == this.mActivePointerId) {
            int i = actionIndex == 0 ? 1 : 0;
            this.mLastMotionY = MotionEventCompat.getY(motionEvent, i);
            this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, i);
        }
        int pointerIndex = getPointerIndex(motionEvent, this.mActivePointerId);
        if (this.mActivePointerId != -1) {
            this.mLastMotionY = MotionEventCompat.getY(motionEvent, pointerIndex);
        }
    }

    public boolean isTargetScrollWithLayout() {
        return this.mTargetScrollWithLayout;
    }

    public void setTargetScrollWithLayout(boolean z) {
        this.mTargetScrollWithLayout = z;
    }

    public void enablePullRefresh(boolean z) {
        this.mPullRefreshEnabled = z;
        if (z && this.mHeaderView == null) {
            createHeaderView();
        }
    }

    public void enableLoadMore(boolean z) {
        this.mLoadMoreEnabled = z;
        if (z && this.mFooterView == null) {
            createFooterView();
        }
    }

    public void enableSecondFloor(boolean z) {
        this.mSecondFloorEnabled = z;
    }

    public void setDistanceToRefresh(int i) {
        float f = (float) i;
        if (((int) (this.mDensity * f)) >= this.mHeaderViewHeight) {
            this.mTotalDragDistance = (int) (f * this.mDensity);
            if (this.mSecondFloorDistance - this.mTotalDragDistance < MIN_GAP_DISTANCE_TO_SECOND_FLOOR) {
                this.mSecondFloorDistance = this.mTotalDragDistance + MIN_GAP_DISTANCE_TO_SECOND_FLOOR;
            }
        }
    }

    public float getDistanceToRefresh() {
        return (float) this.mTotalDragDistance;
    }

    public void setDistanceToSecondFloor(int i) {
        float f = (float) i;
        if (((int) (this.mDensity * f)) - this.mTotalDragDistance < MIN_GAP_DISTANCE_TO_SECOND_FLOOR) {
            Log.e(TAG, "Distance to second floor must be more than 50dp longer than distance to refresh");
        } else {
            this.mSecondFloorDistance = (int) (f * this.mDensity);
        }
    }

    public float getDistanceToSecondFloor() {
        return (float) this.mSecondFloorDistance;
    }

    public void setMaxPushDistance(int i) {
        float f = (float) i;
        if (((int) (this.mDensity * f)) < this.mFooterViewHeight) {
            Log.e(TAG, "Max push distance must be larger than footer view height!");
        } else {
            this.mMaxPushDistance = (int) (f * this.mDensity);
        }
    }

    public void setRefreshOffset(int i) {
        setRefreshOffset(i, false);
    }

    public void setRefreshOffset(int i, boolean z) {
        if (z) {
            this.mRefreshOffset = ((int) (((float) i) * getResources().getDisplayMetrics().density)) + SystemBarDecorator.getStatusBarHeight(getContext());
            this.mHeaderViewHeight += SystemBarDecorator.getStatusBarHeight(getContext());
        } else {
            this.mRefreshOffset = (int) (((float) i) * getResources().getDisplayMetrics().density);
        }
        int i2 = (-this.mContentHeight) + this.mRefreshOffset;
        this.mOriginalOffsetTop = i2;
        this.mCurrentTargetOffsetTop = i2;
        if (this.mHeaderViewHeight < this.mRefreshOffset) {
            Log.e(TAG, "Refresh offset cannot be larger than header view height.");
            this.mHeaderViewHeight = this.mRefreshOffset + ((int) (getResources().getDisplayMetrics().density * 24.0f));
        }
        if (this.mTotalDragDistance < this.mHeaderViewHeight) {
            this.mTotalDragDistance = this.mHeaderViewHeight;
        }
        if (this.mSecondFloorDistance < this.mTotalDragDistance) {
            this.mSecondFloorDistance = this.mTotalDragDistance + MIN_GAP_DISTANCE_TO_SECOND_FLOOR;
        }
    }

    public void enableTargetOffset(boolean z) {
        this.mEnableTargetOffset = z;
    }

    public int getRefreshOffset() {
        return this.mRefreshOffset;
    }

    public void setOnPullRefreshListener(OnPullRefreshListener onPullRefreshListener) {
        this.mPullRefreshListener = onPullRefreshListener;
        if (this.mHeaderView != null) {
            this.mHeaderView.setPullRefreshListener(this.mPullRefreshListener);
        }
    }

    public void setOnPushLoadMoreListener(OnPushLoadMoreListener onPushLoadMoreListener) {
        this.mOnPushLoadMoreListener = onPushLoadMoreListener;
        if (this.mFooterView != null) {
            this.mFooterView.setPushLoadMoreListener(this.mOnPushLoadMoreListener);
        }
    }

    public void setDragRate(float f) {
        if (f <= 0.0f || f >= 1.0f) {
            Log.e(TAG, "Drag rate must be larger than 0 and smaller than 1!");
        } else {
            this.mDragRate = f;
        }
    }

    public void setHeaderView(TBRefreshHeader tBRefreshHeader) {
        if (tBRefreshHeader != null) {
            int indexOfChild = indexOfChild(this.mHeaderView);
            if (!(this.mHeaderView == null || indexOfChild == -1)) {
                removeView(this.mHeaderView);
            }
            this.mHeaderView = tBRefreshHeader;
            this.mHeaderView.setPullRefreshListener(this.mPullRefreshListener);
            addView(this.mHeaderView, indexOfChild, new ViewGroup.LayoutParams(-1, this.mHeaderViewHeight));
        }
    }

    public void setHeaderViewHeight(int i) {
        float f = (float) i;
        if (((int) (this.mDensity * f)) < this.mRefreshOffset) {
            Log.d(TAG, "HeaderView height cannot be smaller than refresh offset.");
            return;
        }
        this.mHeaderViewHeight = (int) (f * this.mDensity);
        if (this.mTotalDragDistance < this.mHeaderViewHeight) {
            this.mTotalDragDistance = this.mHeaderViewHeight;
        }
        if (this.mSecondFloorDistance < this.mTotalDragDistance) {
            this.mSecondFloorDistance = this.mTotalDragDistance + MIN_GAP_DISTANCE_TO_SECOND_FLOOR;
        }
    }

    public int getHeaderViewHeight() {
        return this.mHeaderViewHeight;
    }

    public void setFooterView(TBLoadMoreFooter tBLoadMoreFooter) {
        if (tBLoadMoreFooter != null) {
            int indexOfChild = indexOfChild(this.mFooterView);
            if (!(this.mFooterView == null || indexOfChild == -1)) {
                removeView(this.mFooterView);
            }
            this.mFooterView = tBLoadMoreFooter;
            this.mFooterView.setPushLoadMoreListener(this.mOnPushLoadMoreListener);
            addView(this.mFooterView, indexOfChild, new ViewGroup.LayoutParams(-1, this.mFooterViewHeight));
        }
    }

    public void setFooterViewHeight(int i) {
        float f = (float) i;
        if (((int) (this.mDensity * f)) > this.mMaxPushDistance) {
            this.mMaxPushDistance = (int) (this.mDensity * f);
        }
        this.mFooterViewHeight = (int) (f * this.mDensity);
    }

    public int getFooterViewHeight() {
        return this.mFooterViewHeight;
    }

    public TBRefreshHeader getRefresHeader() {
        return this.mHeaderView;
    }

    public TBLoadMoreFooter getLoadMoreFooter() {
        return this.mFooterView;
    }

    private int getPointerIndex(MotionEvent motionEvent, int i) {
        int findPointerIndex = MotionEventCompat.findPointerIndex(motionEvent, i);
        if (findPointerIndex == -1) {
            this.mActivePointerId = -1;
        }
        return findPointerIndex;
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        DisplayMetrics displayMetrics = DisplayUtil.getDisplayMetrics(getContext());
        this.mDensity = displayMetrics.density;
        this.mHeaderViewHeight = displayMetrics.widthPixels;
        this.mFooterViewHeight = displayMetrics.widthPixels;
        requestLayout();
    }
}
