package com.taobao.uikit.feature.features.internal.pullrefresh;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import androidx.core.view.MotionEventCompat;
import com.rd.animation.AbsAnimation;
import com.taobao.uikit.feature.features.PullToRefreshFeatureExt;
import com.taobao.uikit.feature.view.TScrollView;
import java.util.Date;

@Deprecated
public class RefreshControllerExt {
    public static final int DISTANCE_GAP = 20;
    public static final int DONE = 3;
    private static final int INVALID_POINTER = -1;
    public static final int JUMPED = 5;
    public static final int JUMPING = 4;
    public static final int PULL_TO_REFRESH = 1;
    public static final int REFRESHING = 2;
    public static final int RELEASE_TO_REFRESH = 0;
    protected int mActivePointerId = -1;
    private float mAlpha = 1.0f;
    private Context mContext;
    private int mDistance;
    public int mDistanceToJump = 50;
    private boolean mDownPullFinish = false;
    private IViewEdgeJudge mEdgeJudger;
    private boolean mEnableJump = false;
    private boolean mEnableScroll = true;
    private RefreshHeadViewManager mHeaderViewManager;
    private boolean mIsBack;
    private boolean mIsHeadViewHeightContainLogoImage = false;
    private boolean mIsMultiPointer = false;
    private boolean mIsRecored;
    private float mLastMotionX;
    private float mLastMotionY;
    private int mPositionX = 0;
    private int mPositionY = 0;
    private int mPreActivePointerId = -1;
    private int mPreDistance;
    private int mPrePositionX = 0;
    private int mPrePositionY = 0;
    private int mPullDownDistance = 0;
    private PullToRefreshFeatureExt.OnPullToRefreshListenerExt mPullRefreshListener;
    private Scroller mScroller;
    private int mStartX;
    private int mStartY;
    private int mState = 3;

    private int getTopRealScrollY(int i) {
        return i;
    }

    public RefreshControllerExt(Context context, Scroller scroller, IViewEdgeJudge iViewEdgeJudge) {
        this.mContext = context;
        this.mScroller = scroller;
        this.mEdgeJudger = iViewEdgeJudge;
        this.mState = 3;
    }

    public void enablePullDownRefresh(boolean z, int i, View view) {
        enablePullDownRefresh(z, i, view, true);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void enablePullDownRefresh(boolean r7, int r8, android.view.View r9, boolean r10) {
        /*
            r6 = this;
            if (r7 == 0) goto L_0x0045
            com.taobao.uikit.feature.features.internal.pullrefresh.RefreshHeadViewManager r7 = r6.mHeaderViewManager
            if (r7 != 0) goto L_0x004f
            if (r9 != 0) goto L_0x0014
            android.widget.ImageView r9 = new android.widget.ImageView
            android.content.Context r7 = r6.mContext
            r9.<init>(r7)
            android.widget.ImageView$ScaleType r7 = android.widget.ImageView.ScaleType.CENTER_INSIDE
            r9.setScaleType(r7)
        L_0x0014:
            r3 = r9
            com.taobao.uikit.feature.features.internal.pullrefresh.RefreshHeadViewManager r7 = new com.taobao.uikit.feature.features.internal.pullrefresh.RefreshHeadViewManager
            android.content.Context r1 = r6.mContext
            r5 = 1
            r0 = r7
            r2 = r8
            r4 = r10
            r0.<init>((android.content.Context) r1, (int) r2, (android.view.View) r3, (boolean) r4, (int) r5)
            r6.mHeaderViewManager = r7
            com.taobao.uikit.feature.features.internal.pullrefresh.RefreshHeadViewManager r7 = r6.mHeaderViewManager
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            java.lang.String r9 = "最近更新:"
            r8.append(r9)
            java.util.Date r9 = new java.util.Date
            r9.<init>()
            java.lang.String r9 = r9.toLocaleString()
            r8.append(r9)
            java.lang.String r8 = r8.toString()
            r7.setUpdatedTextView(r8)
            r6.addHeaderView()
            goto L_0x004f
        L_0x0045:
            com.taobao.uikit.feature.features.internal.pullrefresh.RefreshHeadViewManager r7 = r6.mHeaderViewManager
            if (r7 == 0) goto L_0x004f
            r6.removeHeaderView()
            r7 = 0
            r6.mHeaderViewManager = r7
        L_0x004f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.uikit.feature.features.internal.pullrefresh.RefreshControllerExt.enablePullDownRefresh(boolean, int, android.view.View, boolean):void");
    }

    public void addHeaderView() {
        if (this.mHeaderViewManager != null) {
            this.mEdgeJudger.setHeadView(this.mHeaderViewManager.getView());
        }
    }

    public void removeHeaderView() {
        if (this.mHeaderViewManager != null) {
            this.mEdgeJudger.removeHeaderView(this.mHeaderViewManager.getView());
        }
    }

    private boolean JudgeArrivedRecoredEdge(MotionEvent motionEvent) {
        if (this.mEdgeJudger == null || !this.mEdgeJudger.hasArrivedTopEdge() || this.mIsRecored) {
            return false;
        }
        this.mIsRecored = true;
        this.mStartY = (int) motionEvent.getY();
        this.mStartX = (int) motionEvent.getX();
        int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
        if (this.mActivePointerId == -1) {
            this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, actionIndex);
            this.mLastMotionY = motionEvent.getY();
            this.mLastMotionX = motionEvent.getX();
            this.mPreActivePointerId = this.mActivePointerId;
        }
        return true;
    }

    private void processActionMove(int i, int i2) {
        int i3;
        int i4;
        int i5;
        DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
        if (this.mState == 3) {
            if (i > 0 && this.mEdgeJudger.hasArrivedTopEdge()) {
                this.mState = 1;
                onRefreshStateChanged(this.mState, false);
                changeHeaderViewByState();
            }
        } else if (this.mState == 1) {
            if (this.mHeaderViewManager != null) {
                if (this.mIsHeadViewHeightContainLogoImage) {
                    i5 = this.mHeaderViewManager.getHeight();
                } else {
                    i5 = this.mHeaderViewManager.getImageHeight();
                }
                if (getTopRealScrollY(i) >= i5) {
                    this.mState = 0;
                    onRefreshStateChanged(this.mState, false);
                    this.mIsBack = true;
                } else if (i2 - this.mStartY <= 0) {
                    this.mState = 3;
                    onRefreshStateChanged(this.mState, false);
                } else {
                    changeHeaderProgressBarState(getTopRealScrollY(i));
                }
                changeHeaderViewByState();
            }
        } else if (this.mState == 0) {
            if (this.mHeaderViewManager != null) {
                if (this.mIsHeadViewHeightContainLogoImage) {
                    i4 = this.mHeaderViewManager.getHeight();
                } else {
                    i4 = this.mHeaderViewManager.getImageHeight();
                }
                if (getTopRealScrollY(i) < i4 && i2 - this.mStartY > 0) {
                    this.mState = 1;
                    onRefreshStateChanged(this.mState, false);
                    changeHeaderViewByState();
                } else if (this.mEnableJump && ((float) (getTopRealScrollY(i) - i4)) > displayMetrics.density * 20.0f && ((float) (getTopRealScrollY(i) - i4)) < displayMetrics.density * ((float) this.mDistanceToJump)) {
                    this.mState = 4;
                    onRefreshStateChanged(this.mState, false);
                    changeHeaderViewByState();
                }
            }
        } else if (this.mState == 4 && this.mHeaderViewManager != null) {
            if (this.mIsHeadViewHeightContainLogoImage) {
                i3 = this.mHeaderViewManager.getHeight();
            } else {
                i3 = this.mHeaderViewManager.getImageHeight();
            }
            if (((float) (getTopRealScrollY(i) - i3)) < displayMetrics.density * 20.0f) {
                this.mState = 0;
                onRefreshStateChanged(this.mState, false);
                changeHeaderViewByState();
            } else if (((float) (getTopRealScrollY(i) - i3)) > displayMetrics.density * ((float) this.mDistanceToJump)) {
                this.mState = 5;
                onRefreshStateChanged(this.mState, false);
                changeHeaderViewByState();
                if (this.mPullRefreshListener != null) {
                    this.mPullRefreshListener.onReadyToJump(true, 1.0f);
                }
            } else {
                changeHeaderAlpha(1.0f - ((((float) (getTopRealScrollY(i) - i3)) - (displayMetrics.density * 20.0f)) / (displayMetrics.density * 20.0f)));
                if (this.mPullRefreshListener != null) {
                    this.mPullRefreshListener.onReadyToJump(false, (((float) (getTopRealScrollY(i) - i3)) - (displayMetrics.density * 20.0f)) / (displayMetrics.density * ((float) (this.mDistanceToJump - 20))));
                }
            }
        }
        if ((this.mState == 1 || this.mState == 0 || this.mState == 4) && this.mHeaderViewManager != null) {
            this.mHeaderViewManager.setPadding(0, getTopRealScrollY(i) - this.mHeaderViewManager.getHeight(), 0, 0);
            Log.d("weina", "processActionMove: " + String.valueOf(getTopRealScrollY(i) - this.mHeaderViewManager.getHeight()));
        }
    }

    public void onTouchEvent(MotionEvent motionEvent) {
        int i;
        int i2 = 1;
        switch (motionEvent.getAction() & 255) {
            case 0:
                JudgeArrivedRecoredEdge(motionEvent);
                return;
            case 1:
            case 3:
            case 4:
                if (this.mState != 2) {
                    if (this.mState == 1) {
                        this.mState = 3;
                        onRefreshStateChanged(this.mState, true);
                        changeHeaderViewByState();
                    }
                    if (this.mState == 0 || this.mState == 4) {
                        this.mState = 2;
                        onRefreshStateChanged(this.mState, false);
                        changeHeaderViewByState();
                        onRefresh();
                    }
                }
                this.mIsRecored = false;
                this.mIsBack = false;
                this.mIsMultiPointer = false;
                this.mDistance = 0;
                this.mPositionY = 0;
                this.mPositionX = 0;
                this.mActivePointerId = -1;
                return;
            case 2:
                if (this.mActivePointerId == -1) {
                    this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, MotionEventCompat.getActionIndex(motionEvent));
                    this.mLastMotionY = motionEvent.getY();
                    this.mLastMotionX = motionEvent.getX();
                    this.mPreActivePointerId = this.mActivePointerId;
                }
                try {
                    int y = (int) MotionEventCompat.getY(motionEvent, getPointerIndex(motionEvent, this.mActivePointerId));
                    JudgeArrivedRecoredEdge(motionEvent);
                    if (!this.mIsMultiPointer) {
                        i = y - this.mStartY;
                        this.mDistance = i;
                        this.mPreDistance = i;
                        this.mPositionY = y;
                        this.mPrePositionY = y;
                    } else if (this.mPreActivePointerId == this.mActivePointerId) {
                        float f = (float) y;
                        i = (int) (((float) this.mDistance) + (f - this.mLastMotionY));
                        y = (int) (((float) this.mPositionY) + (f - this.mLastMotionY));
                        this.mPreDistance = i;
                        this.mPrePositionY = y;
                    } else {
                        float f2 = (float) y;
                        i = (int) (((float) this.mPreDistance) + (f2 - this.mLastMotionY));
                        y = (int) (((float) this.mPrePositionY) + (f2 - this.mLastMotionY));
                        this.mPreActivePointerId = this.mActivePointerId;
                        this.mDistance = this.mPreDistance;
                        this.mPositionY = this.mPrePositionY;
                    }
                    if (this.mState != 2 && this.mIsRecored) {
                        processActionMove(i, y);
                        this.mPullDownDistance = i;
                        return;
                    }
                    return;
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                    return;
                }
            case 5:
                int actionIndex = MotionEventCompat.getActionIndex(motionEvent);
                this.mLastMotionY = MotionEventCompat.getY(motionEvent, actionIndex);
                this.mLastMotionX = MotionEventCompat.getX(motionEvent, actionIndex);
                this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                this.mIsMultiPointer = true;
                return;
            case 6:
                int actionIndex2 = MotionEventCompat.getActionIndex(motionEvent);
                if (MotionEventCompat.getPointerId(motionEvent, actionIndex2) == this.mActivePointerId) {
                    if (actionIndex2 != 0) {
                        i2 = 0;
                    }
                    this.mLastMotionY = MotionEventCompat.getY(motionEvent, i2);
                    this.mLastMotionX = MotionEventCompat.getX(motionEvent, i2);
                    this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, i2);
                }
                int pointerIndex = getPointerIndex(motionEvent, this.mActivePointerId);
                if (this.mActivePointerId != -1) {
                    this.mLastMotionY = MotionEventCompat.getY(motionEvent, pointerIndex);
                    this.mLastMotionX = MotionEventCompat.getX(motionEvent, pointerIndex);
                    return;
                }
                return;
            default:
                return;
        }
    }

    public void onScrollerStateChanged(int i, boolean z) {
        if (this.mEnableScroll && z && this.mHeaderViewManager != null) {
            this.mHeaderViewManager.setPadding(0, i, 0, 0);
            Log.d("weina", "onScrollerStateChanged: " + i);
        }
    }

    private void changeHeaderViewByState() {
        if (this.mHeaderViewManager != null) {
            this.mHeaderViewManager.changeHeaderViewByState(this.mState);
            if (this.mState == 1 && this.mIsBack) {
                this.mIsBack = false;
            } else if (this.mState == 2) {
                resetHeadViewPadding(this.mState);
            } else if (this.mState == 3) {
                resetHeadViewPadding(this.mState);
            }
        }
    }

    private void resetHeadViewPadding(int i) {
        int height;
        int i2 = i;
        if (this.mHeaderViewManager != null && (height = this.mHeaderViewManager.getHeight()) != 0) {
            int i3 = 0;
            if (i2 != 2 && i2 == 3) {
                i3 = -height;
            }
            if (this.mIsHeadViewHeightContainLogoImage) {
                this.mScroller.startScroll(0, this.mHeaderViewManager.getPaddingTop(), 0, i3 - this.mHeaderViewManager.getPaddingTop(), AbsAnimation.DEFAULT_ANIMATION_TIME);
            } else if (i2 == 2) {
                this.mScroller.startScroll(0, this.mHeaderViewManager.getPaddingTop(), 0, (this.mHeaderViewManager.getImageHeight() - this.mHeaderViewManager.getPaddingTop()) - this.mHeaderViewManager.getHeight(), AbsAnimation.DEFAULT_ANIMATION_TIME);
            } else if (i2 == 3) {
                this.mScroller.startScroll(0, this.mHeaderViewManager.getPaddingTop(), 0, i3 - this.mHeaderViewManager.getPaddingTop(), AbsAnimation.DEFAULT_ANIMATION_TIME);
            }
            this.mEdgeJudger.trigger();
        }
    }

    private void changeHeaderProgressBarState(int i) {
        if (this.mHeaderViewManager != null) {
            this.mHeaderViewManager.changeProgressBarState(i);
        }
    }

    private void changeHeaderAlpha(float f) {
        if (this.mHeaderViewManager != null) {
            this.mHeaderViewManager.changeHeaderAlpha(f);
        }
    }

    private void onRefresh() {
        if (this.mDownPullFinish) {
            onRefreshComplete();
        } else if (this.mPullRefreshListener != null) {
            this.mPullRefreshListener.onPullDownToRefresh();
        }
    }

    public void onRefreshComplete() {
        this.mState = 3;
        onRefreshStateChanged(this.mState, false);
        if (this.mHeaderViewManager != null) {
            RefreshHeadViewManager refreshHeadViewManager = this.mHeaderViewManager;
            refreshHeadViewManager.setUpdatedTextView("最近更新:" + new Date().toLocaleString());
            changeHeaderViewByState();
        }
    }

    private int getPointerIndex(MotionEvent motionEvent, int i) {
        int findPointerIndex = MotionEventCompat.findPointerIndex(motionEvent, i);
        if (findPointerIndex == -1) {
            this.mActivePointerId = -1;
        }
        return findPointerIndex;
    }

    public void setDownRefreshFinish(boolean z) {
        if (this.mHeaderViewManager != null) {
            this.mDownPullFinish = z;
            this.mHeaderViewManager.setFinish(z);
        }
    }

    public void setOnRefreshListener(PullToRefreshFeatureExt.OnPullToRefreshListenerExt onPullToRefreshListenerExt) {
        this.mPullRefreshListener = onPullToRefreshListenerExt;
    }

    public void setIsDownRefreshing() {
        if (this.mHeaderViewManager != null) {
            this.mState = 2;
            onRefreshStateChanged(this.mState, false);
            changeHeaderViewByState();
            this.mHeaderViewManager.setPadding(0, 0, 0, 0);
        }
    }

    public int getState() {
        return this.mState;
    }

    public void setDownRefreshTips(String[] strArr) {
        if (this.mHeaderViewManager != null) {
            this.mHeaderViewManager.setTipArray(strArr);
        }
    }

    public int getPullDownDistance() {
        return this.mPullDownDistance;
    }

    public void setRefreshViewColor(int i) {
        if (this.mHeaderViewManager != null) {
            this.mHeaderViewManager.setRefreshViewColor(i);
        }
    }

    public void enableJump(boolean z) {
        this.mEnableJump = z;
    }

    public void isHeadViewHeightContainImage(boolean z) {
        if (this.mHeaderViewManager != null) {
            this.mHeaderViewManager.isHeadViewHeightContainImage(z);
        }
        this.mIsHeadViewHeightContainLogoImage = z;
    }

    public View getRefreshView() {
        if (this.mHeaderViewManager != null) {
            return this.mHeaderViewManager.getRefreshView();
        }
        return null;
    }

    public void reset() {
        if (this.mState == 5) {
            this.mState = 3;
            onRefreshStateChanged(this.mState, false);
            changeHeaderViewByState();
        }
    }

    public void onRefreshStateChanged(int i, boolean z) {
        if (this.mState != 3) {
            ((TScrollView) ((PullToRefreshFeatureExt) this.mEdgeJudger).getHost()).setScrollingEnabled(false);
            this.mEnableScroll = false;
        } else {
            ((TScrollView) ((PullToRefreshFeatureExt) this.mEdgeJudger).getHost()).setScrollingEnabled(true);
            this.mEnableScroll = true;
        }
        if (this.mPullRefreshListener != null) {
            this.mPullRefreshListener.onRefreshStateChanged(i, z);
        }
    }
}
