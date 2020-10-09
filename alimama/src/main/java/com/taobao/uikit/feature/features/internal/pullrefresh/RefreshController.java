package com.taobao.uikit.feature.features.internal.pullrefresh;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Scroller;
import androidx.core.view.MotionEventCompat;
import com.rd.animation.AbsAnimation;
import com.taobao.tao.util.TBSoundPlayer;
import com.taobao.uikit.feature.features.DragToRefreshFeature;
import com.taobao.uikit.feature.features.PullToRefreshFeature;
import com.taobao.uikit.utils.UIKITLog;
import java.util.Date;

@Deprecated
public class RefreshController {
    public static final int DONE = 3;
    public static final int DOWN_PULL = 4;
    private static final int INVALID_POINTER = -1;
    public static final int LEFT_PULL = 7;
    public static final int PULL_TO_REFRESH = 1;
    private static final int RATIO = 3;
    private static final int RATIO_X = 1;
    public static final int REFRESHING = 2;
    public static final int RELEASE_TO_REFRESH = 0;
    public static final int RIGHT_PULL = 6;
    private static final String TAG = "DownRefreshControler";
    public static final int UP_PULL = 5;
    protected int mActivePointerId = -1;
    private OnPullDownRefreshCancle mCancle;
    private Context mContext;
    private int mDistance;
    private boolean mDownPullFinish = false;
    private DragToRefreshFeature.OnDragToRefreshListener mDragRefreshListener;
    private IViewEdgeJudge mEdgeJudge;
    private RefreshHeadViewManager mFooterViewManager;
    private RefreshHeadViewManager mHeaderViewManager;
    private boolean mIsAutoLoading = false;
    private boolean mIsBack;
    private boolean mIsHeadViewHeightContainLogoImage = true;
    private boolean mIsMultiPointer = false;
    private boolean mIsNeedPullUpToRefresh = true;
    private boolean mIsRecored;
    private boolean mIsRefreshable;
    private boolean mIsScrolling = false;
    private float mLastMotionX;
    private float mLastMotionY;
    private int mOrientation = 1;
    private int mPositionX = 0;
    private int mPositionY = 0;
    private int mPreActivePointerId = -1;
    private int mPreDistance;
    private int mPrePositionX = 0;
    private int mPrePositionY = 0;
    private int mPullDirection;
    private int mPullDownDistance = 0;
    private PullToRefreshFeature.OnPullToRefreshListener mPullRefreshListener;
    private Scroller mScroller;
    private int mStartX;
    private int mStartY;
    private int mState;
    private boolean mUpPullFinish = false;

    public interface OnPullDownRefreshCancle {
        void onRefreshCancle();
    }

    public RefreshController(IViewEdgeJudge iViewEdgeJudge, Context context, Scroller scroller) {
        this.mEdgeJudge = iViewEdgeJudge;
        this.mScroller = scroller;
        this.mContext = context;
        this.mState = 3;
        this.mIsRefreshable = true;
    }

    public RefreshController(IViewEdgeJudge iViewEdgeJudge, Context context, Scroller scroller, int i) {
        this.mEdgeJudge = iViewEdgeJudge;
        this.mScroller = scroller;
        this.mContext = context;
        this.mState = 3;
        this.mIsRefreshable = true;
        this.mOrientation = i;
    }

    public void setRefreshCancle(OnPullDownRefreshCancle onPullDownRefreshCancle) {
        this.mCancle = onPullDownRefreshCancle;
    }

    public void setDownRefreshTips(String[] strArr) {
        if (this.mHeaderViewManager != null) {
            this.mHeaderViewManager.setTipArray(strArr);
        }
    }

    public void setUpRefreshTips(String[] strArr) {
        if (this.mFooterViewManager != null) {
            this.mFooterViewManager.setTipArray(strArr);
        }
    }

    public void enablePullDownRefresh(boolean z, int i, View view) {
        enablePullDownRefresh(z, i, view, true);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void enablePullDownRefresh(boolean r7, int r8, android.view.View r9, boolean r10) {
        /*
            r6 = this;
            if (r7 == 0) goto L_0x004d
            com.taobao.uikit.feature.features.internal.pullrefresh.RefreshHeadViewManager r7 = r6.mHeaderViewManager
            if (r7 != 0) goto L_0x0057
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
            int r9 = r6.mOrientation
            r0 = 1
            if (r9 != r0) goto L_0x0020
            r5 = 1
            goto L_0x0022
        L_0x0020:
            r9 = 3
            r5 = 3
        L_0x0022:
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
            goto L_0x0057
        L_0x004d:
            com.taobao.uikit.feature.features.internal.pullrefresh.RefreshHeadViewManager r7 = r6.mHeaderViewManager
            if (r7 == 0) goto L_0x0057
            r6.removeHeaderView()
            r7 = 0
            r6.mHeaderViewManager = r7
        L_0x0057:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.uikit.feature.features.internal.pullrefresh.RefreshController.enablePullDownRefresh(boolean, int, android.view.View, boolean):void");
    }

    public void isHeadViewHeightContainImage(boolean z) {
        if (this.mHeaderViewManager != null) {
            this.mHeaderViewManager.isHeadViewHeightContainImage(z);
        }
        this.mIsHeadViewHeightContainLogoImage = z;
    }

    public void setRefreshViewColor(int i) {
        if (this.mHeaderViewManager != null) {
            this.mHeaderViewManager.setRefreshViewColor(i);
        }
        if (this.mFooterViewManager != null) {
            this.mFooterViewManager.setRefreshViewColor(i);
        }
    }

    public void setUpRefreshBackgroundColor(int i) {
        if (this.mFooterViewManager != null) {
            this.mFooterViewManager.setRefreshBackground(i);
        }
    }

    public void setDownRefreshBackgroundColor(int i) {
        if (this.mHeaderViewManager != null) {
            this.mHeaderViewManager.setRefreshBackground(i);
        }
    }

    public void addHeaderView() {
        if (this.mHeaderViewManager != null) {
            this.mEdgeJudge.setHeadView(this.mHeaderViewManager.getView());
        }
    }

    public void removeHeaderView() {
        if (this.mHeaderViewManager != null) {
            this.mEdgeJudge.removeHeaderView(this.mHeaderViewManager.getView());
        }
    }

    public void removeFooterView() {
        if (this.mFooterViewManager != null) {
            this.mEdgeJudge.removeFooterView(this.mFooterViewManager.getView());
        }
    }

    public void enablePullUpRefresh(boolean z, int i, View view) {
        enablePullUpRefresh(z, i, view, true);
    }

    public void enablePullUpRefresh(boolean z, int i, View view, boolean z2) {
        if (z) {
            if (this.mFooterViewManager == null) {
                this.mFooterViewManager = new RefreshHeadViewManager(this.mContext, i, view, z2, this.mOrientation == 1 ? 2 : 4);
                RefreshHeadViewManager refreshHeadViewManager = this.mFooterViewManager;
                refreshHeadViewManager.setUpdatedTextView("最近更新:" + new Date().toLocaleString());
                addFooterView();
            }
        } else if (this.mFooterViewManager != null) {
            removeFooterView();
            this.mFooterViewManager = null;
        }
    }

    public void addFooterView() {
        if (this.mFooterViewManager != null) {
            this.mEdgeJudge.setFooterView(this.mFooterViewManager.getView());
        }
    }

    public void setPullUpRefreshAuto(boolean z) {
        this.mIsNeedPullUpToRefresh = !z;
        if (this.mFooterViewManager != null) {
            this.mFooterViewManager.setProgressBarInitState(z);
            this.mFooterViewManager.changeHeaderViewByState(1);
        }
    }

    public void autoLoadingData() {
        if (!this.mIsNeedPullUpToRefresh && !this.mIsAutoLoading && !this.mUpPullFinish) {
            this.mIsAutoLoading = true;
            this.mPullDirection = 5;
            if (this.mFooterViewManager != null) {
                this.mFooterViewManager.changeHeaderViewByState(2);
            }
            if (this.mPullRefreshListener != null) {
                this.mPullRefreshListener.onPullUpToRefresh();
            }
            if (this.mDragRefreshListener != null) {
                this.mDragRefreshListener.onDragNegative();
            }
            if (this.mFooterViewManager != null) {
                this.mFooterViewManager.setViewPadding(true);
            }
        }
    }

    public boolean isScrollStop() {
        return this.mScroller.isFinished();
    }

    private int getTopRealScrollY(int i) {
        DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
        if (this.mHeaderViewManager == null) {
            return i;
        }
        double height = (double) (((float) displayMetrics.heightPixels) / ((float) (displayMetrics.heightPixels + (this.mHeaderViewManager.getHeight() + this.mHeaderViewManager.getPaddingTop()))));
        Double.isNaN(height);
        return (int) (((float) (height / 1.3d)) * ((float) i));
    }

    private int getLeftRealScrollX(int i) {
        DisplayMetrics displayMetrics = this.mContext.getResources().getDisplayMetrics();
        if (this.mHeaderViewManager == null) {
            return i;
        }
        double width = (double) (((float) displayMetrics.widthPixels) / ((float) (displayMetrics.widthPixels + (this.mHeaderViewManager.getWidth() + this.mHeaderViewManager.getPaddingLeft()))));
        Double.isNaN(width);
        return (int) (((float) (width / 1.3d)) * ((float) i));
    }

    private boolean JudgeArrivedRecoredEdge(MotionEvent motionEvent) {
        if (this.mEdgeJudge == null) {
            return false;
        }
        if (this.mEdgeJudge.hasArrivedTopEdge() && !this.mIsRecored) {
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
        } else if (!this.mEdgeJudge.hasArrivedBottomEdge() || this.mIsRecored) {
            return false;
        } else {
            this.mIsRecored = true;
            this.mStartY = (int) motionEvent.getY();
            this.mStartX = (int) motionEvent.getX();
            int actionIndex2 = MotionEventCompat.getActionIndex(motionEvent);
            if (this.mActivePointerId == -1) {
                this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, actionIndex2);
                this.mLastMotionY = motionEvent.getY();
                this.mLastMotionX = motionEvent.getX();
                this.mPreActivePointerId = this.mActivePointerId;
            }
            return true;
        }
    }

    private void processActionMove(int i, int i2) {
        int i3;
        int i4;
        if (this.mState == 0) {
            if (this.mPullDirection == 4 && this.mHeaderViewManager != null) {
                this.mEdgeJudge.keepTop();
                if (this.mIsHeadViewHeightContainLogoImage) {
                    i4 = this.mHeaderViewManager.getHeight();
                } else {
                    i4 = this.mHeaderViewManager.getImageHeight();
                }
                if (getTopRealScrollY(i) < i4 && i2 - this.mStartY > 0) {
                    this.mState = 1;
                    changeHeaderViewByState();
                }
            } else if (this.mPullDirection == 5 && this.mFooterViewManager != null) {
                this.mEdgeJudge.keepBottom();
                if (Math.abs(i / 3) < this.mFooterViewManager.getHeight() && i2 - this.mStartY < 0) {
                    this.mState = 1;
                    if (this.mIsNeedPullUpToRefresh) {
                        changeFooterViewByState();
                    }
                }
            } else if (this.mPullDirection == 6 && this.mHeaderViewManager != null) {
                this.mEdgeJudge.keepTop();
                if (getLeftRealScrollX(i) < this.mHeaderViewManager.getWidth() && i2 - this.mStartX > 0) {
                    this.mState = 1;
                    changeHeaderViewByState();
                }
            } else if (this.mPullDirection == 7 && this.mFooterViewManager != null) {
                this.mEdgeJudge.keepBottom();
                if (Math.abs(i / 1) < this.mFooterViewManager.getWidth() && i2 - this.mStartX < 0) {
                    this.mState = 1;
                    if (this.mIsNeedPullUpToRefresh) {
                        changeFooterViewByState();
                    }
                }
            }
        } else if (this.mState == 1) {
            if (this.mPullDirection == 4 && this.mHeaderViewManager != null) {
                this.mEdgeJudge.keepTop();
                if (this.mIsHeadViewHeightContainLogoImage) {
                    i3 = this.mHeaderViewManager.getHeight();
                } else {
                    i3 = this.mHeaderViewManager.getImageHeight();
                }
                if (getTopRealScrollY(i) >= i3) {
                    this.mState = 0;
                    this.mIsBack = true;
                } else if (i2 - this.mStartY <= 0) {
                    this.mState = 3;
                }
                changeHeaderViewByState();
                changeHeaderProgressBarState(getTopRealScrollY(i));
            } else if (this.mPullDirection == 5 && this.mFooterViewManager != null) {
                this.mEdgeJudge.keepBottom();
                if (i / 3 <= this.mFooterViewManager.getHeight() * -1) {
                    this.mState = 0;
                    this.mIsBack = true;
                } else if (i2 - this.mStartY >= 0) {
                    this.mState = 3;
                }
                if (this.mIsNeedPullUpToRefresh) {
                    changeFooterViewByState();
                    changeFooterProgressBarState((-i) / 3);
                }
            } else if (this.mPullDirection == 6 && this.mHeaderViewManager != null) {
                this.mEdgeJudge.keepTop();
                if (getLeftRealScrollX(i) >= this.mHeaderViewManager.getWidth()) {
                    this.mState = 0;
                    this.mIsBack = true;
                } else if (i2 - this.mStartX <= 0) {
                    this.mState = 3;
                }
                changeHeaderViewByState();
                changeHeaderProgressBarState(getLeftRealScrollX(i));
            } else if (this.mPullDirection == 7 && this.mFooterViewManager != null) {
                this.mEdgeJudge.keepBottom();
                if (i / 1 <= this.mFooterViewManager.getWidth() * -1) {
                    this.mState = 0;
                    this.mIsBack = true;
                } else if (i2 - this.mStartX >= 0) {
                    this.mState = 3;
                }
                if (this.mIsNeedPullUpToRefresh) {
                    changeFooterViewByState();
                    changeFooterProgressBarState((-i) / 1);
                }
            }
        } else if (this.mState == 3) {
            if (i > 0 && this.mEdgeJudge.hasArrivedTopEdge()) {
                this.mPullDirection = this.mOrientation == 1 ? 4 : 6;
                this.mState = 1;
                changeHeaderViewByState();
            } else if (i < 0 && this.mEdgeJudge.hasArrivedBottomEdge()) {
                this.mPullDirection = this.mOrientation == 1 ? 5 : 7;
                if (this.mIsNeedPullUpToRefresh) {
                    this.mState = 1;
                    changeFooterViewByState();
                } else {
                    autoLoadingData();
                }
            }
        }
        if (this.mState != 1 && this.mState != 0) {
            return;
        }
        if (this.mPullDirection == 4 && this.mHeaderViewManager != null) {
            this.mHeaderViewManager.setPadding(0, getTopRealScrollY(i) - this.mHeaderViewManager.getHeight(), 0, 0);
        } else if (this.mPullDirection != 5 || this.mFooterViewManager == null || this.mUpPullFinish) {
            if (this.mPullDirection == 6 && this.mHeaderViewManager != null) {
                this.mHeaderViewManager.setPadding((this.mHeaderViewManager.getWidth() * -1) + getLeftRealScrollX(i), 0, 0, 0);
            } else if (this.mPullDirection == 7 && this.mFooterViewManager != null && this.mIsNeedPullUpToRefresh) {
                this.mFooterViewManager.setPadding(0, 0, (this.mFooterViewManager.getWidth() * -1) - (i / 1), 0);
            }
        } else if (this.mIsNeedPullUpToRefresh) {
            this.mFooterViewManager.setPadding(0, 0, 0, (this.mFooterViewManager.getHeight() * -1) - (i / 3));
        }
    }

    public void onTouchEvent(MotionEvent motionEvent) {
        int i;
        if (this.mIsRefreshable && !this.mIsScrolling) {
            switch (motionEvent.getAction() & 255) {
                case 0:
                    JudgeArrivedRecoredEdge(motionEvent);
                    return;
                case 1:
                case 3:
                case 4:
                    if (this.mState != 2) {
                        if (this.mPullDirection == 4) {
                            if (this.mState == 1) {
                                this.mState = 3;
                                changeHeaderViewByState();
                                if (this.mCancle != null) {
                                    this.mCancle.onRefreshCancle();
                                }
                            }
                            if (this.mState == 0) {
                                this.mState = 2;
                                changeHeaderViewByState();
                                onRefresh();
                            }
                        } else if (this.mPullDirection == 5) {
                            if (this.mState == 1) {
                                this.mState = 3;
                                if (this.mIsNeedPullUpToRefresh) {
                                    changeFooterViewByState();
                                }
                            }
                            if (this.mState == 0) {
                                this.mState = 2;
                                if (this.mIsNeedPullUpToRefresh) {
                                    changeFooterViewByState();
                                }
                                onRefresh();
                            }
                        } else if (this.mPullDirection == 6) {
                            if (this.mState == 1) {
                                this.mState = 3;
                                changeHeaderViewByState();
                                if (this.mCancle != null) {
                                    this.mCancle.onRefreshCancle();
                                }
                            }
                            if (this.mState == 0) {
                                this.mState = 2;
                                changeHeaderViewByState();
                                onRefresh();
                            }
                        } else if (this.mPullDirection == 7) {
                            if (this.mState == 1) {
                                this.mState = 3;
                                if (this.mIsNeedPullUpToRefresh) {
                                    changeFooterViewByState();
                                }
                            }
                            if (this.mState == 0) {
                                this.mState = 2;
                                if (this.mIsNeedPullUpToRefresh) {
                                    changeFooterViewByState();
                                }
                                onRefresh();
                            }
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
                        int pointerIndex = getPointerIndex(motionEvent, this.mActivePointerId);
                        int y = (int) MotionEventCompat.getY(motionEvent, pointerIndex);
                        int x = (int) MotionEventCompat.getX(motionEvent, pointerIndex);
                        JudgeArrivedRecoredEdge(motionEvent);
                        if (this.mIsMultiPointer) {
                            if (this.mPreActivePointerId == this.mActivePointerId) {
                                if (1 == this.mOrientation) {
                                    float f = (float) y;
                                    i = (int) (((float) this.mDistance) + (f - this.mLastMotionY));
                                    y = (int) (((float) this.mPositionY) + (f - this.mLastMotionY));
                                    this.mPreDistance = i;
                                    this.mPrePositionY = y;
                                } else {
                                    float f2 = (float) x;
                                    i = (int) (((float) this.mDistance) + (f2 - this.mLastMotionX));
                                    x = (int) (((float) this.mPositionX) + (f2 - this.mLastMotionX));
                                    this.mPreDistance = i;
                                    this.mPrePositionX = x;
                                }
                            } else if (1 == this.mOrientation) {
                                float f3 = (float) y;
                                i = (int) (((float) this.mPreDistance) + (f3 - this.mLastMotionY));
                                y = (int) (((float) this.mPrePositionY) + (f3 - this.mLastMotionY));
                                this.mPreActivePointerId = this.mActivePointerId;
                                this.mDistance = this.mPreDistance;
                                this.mPositionY = this.mPrePositionY;
                            } else {
                                float f4 = (float) x;
                                i = (int) (((float) this.mPreDistance) + (f4 - this.mLastMotionX));
                                x = (int) (((float) this.mPrePositionX) + (f4 - this.mLastMotionX));
                                this.mPreActivePointerId = this.mActivePointerId;
                                this.mDistance = this.mPreDistance;
                                this.mPositionX = this.mPrePositionX;
                            }
                        } else if (1 == this.mOrientation) {
                            i = y - this.mStartY;
                            this.mDistance = i;
                            this.mPreDistance = i;
                            this.mPositionY = y;
                            this.mPrePositionY = y;
                        } else {
                            i = x - this.mStartX;
                            this.mDistance = i;
                            this.mPreDistance = i;
                            this.mPositionX = x;
                            this.mPrePositionX = x;
                        }
                        if (this.mState != 2 && this.mIsRecored) {
                            if (this.mOrientation == 1) {
                                x = y;
                            }
                            processActionMove(i, x);
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
                    UIKITLog.i(TAG, "ACTION_POINTER_DOWN : mActivePointerId %d  position : %f", Integer.valueOf(this.mActivePointerId), Float.valueOf(this.mLastMotionY));
                    this.mLastMotionY = MotionEventCompat.getY(motionEvent, actionIndex);
                    this.mLastMotionX = MotionEventCompat.getX(motionEvent, actionIndex);
                    this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, actionIndex);
                    this.mIsMultiPointer = true;
                    return;
                case 6:
                    int actionIndex2 = MotionEventCompat.getActionIndex(motionEvent);
                    if (MotionEventCompat.getPointerId(motionEvent, actionIndex2) == this.mActivePointerId) {
                        int i2 = actionIndex2 == 0 ? 1 : 0;
                        this.mLastMotionY = MotionEventCompat.getY(motionEvent, i2);
                        this.mLastMotionX = MotionEventCompat.getX(motionEvent, i2);
                        this.mActivePointerId = MotionEventCompat.getPointerId(motionEvent, i2);
                    }
                    int pointerIndex2 = getPointerIndex(motionEvent, this.mActivePointerId);
                    if (this.mActivePointerId != -1) {
                        this.mLastMotionY = MotionEventCompat.getY(motionEvent, pointerIndex2);
                        this.mLastMotionX = MotionEventCompat.getX(motionEvent, pointerIndex2);
                        UIKITLog.i(TAG, "ACTION_POINTER_UP : mActivePointerId %d mLastMotionY position : %f", Integer.valueOf(this.mActivePointerId), Float.valueOf(this.mLastMotionY));
                        return;
                    }
                    return;
                default:
                    return;
            }
        }
    }

    public int getPullDownDistance() {
        return this.mPullDownDistance;
    }

    private int getPointerIndex(MotionEvent motionEvent, int i) {
        int findPointerIndex = MotionEventCompat.findPointerIndex(motionEvent, i);
        if (findPointerIndex == -1) {
            this.mActivePointerId = -1;
        }
        return findPointerIndex;
    }

    private void changeHeaderProgressBarState(int i) {
        if (this.mHeaderViewManager != null) {
            this.mHeaderViewManager.changeProgressBarState(i);
        }
    }

    private void changeFooterProgressBarState(int i) {
        if (this.mFooterViewManager != null) {
            this.mFooterViewManager.changeProgressBarState(i);
        }
    }

    public void onScrollerStateChanged(int i, boolean z) {
        if (this.mPullDirection == 4) {
            if (!this.mIsScrolling) {
                return;
            }
            if (!z || this.mHeaderViewManager == null) {
                this.mIsScrolling = false;
            } else {
                this.mHeaderViewManager.setPadding(0, i, 0, 0);
            }
        } else if (this.mPullDirection == 5) {
            if (!this.mIsScrolling) {
                return;
            }
            if (!z || this.mFooterViewManager == null) {
                this.mIsScrolling = false;
            } else {
                this.mFooterViewManager.setPadding(0, 0, 0, i);
            }
        } else if (this.mPullDirection == 6) {
            if (!this.mIsScrolling) {
                return;
            }
            if (!z || this.mHeaderViewManager == null) {
                this.mIsScrolling = false;
            } else {
                this.mHeaderViewManager.setPadding(i, 0, 0, 0);
            }
        } else if (this.mPullDirection == 7 && this.mIsScrolling) {
            if (!z || this.mFooterViewManager == null) {
                this.mIsScrolling = false;
            } else {
                this.mFooterViewManager.setPadding(0, 0, i, 0);
            }
        }
    }

    private void changeHeaderViewByState() {
        if (this.mHeaderViewManager != null) {
            this.mHeaderViewManager.changeHeaderViewByState(this.mState);
            if (this.mState == 1 && this.mIsBack) {
                this.mIsBack = false;
            } else if (this.mState == 2) {
                UIKITLog.v(TAG, "刷新造成scroll", new Object[0]);
                resetHeadViewPadding(this.mState);
            } else if (this.mState == 3) {
                UIKITLog.v(TAG, "不需要刷新或者刷新完成造成scroll", new Object[0]);
                resetHeadViewPadding(this.mState);
            }
        }
    }

    private void changeFooterViewByState() {
        if (this.mFooterViewManager != null) {
            this.mFooterViewManager.changeHeaderViewByState(this.mState);
            if (this.mState == 1 && this.mIsBack) {
                this.mIsBack = false;
            } else if (this.mState == 2) {
                UIKITLog.v(TAG, "刷新造成scroll", new Object[0]);
                resetFooterViewPadding(this.mState);
            } else if (this.mState == 3) {
                UIKITLog.v(TAG, "不需要刷新或者刷新完成造成scroll", new Object[0]);
                resetFooterViewPadding(this.mState);
            }
        }
    }

    public void resetFooterViewPadding(int i) {
        int i2 = i;
        if (this.mFooterViewManager != null) {
            int height = this.mOrientation == 1 ? this.mFooterViewManager.getHeight() : this.mFooterViewManager.getWidth();
            if (height != 0) {
                int i3 = 0;
                if (i2 != 2 && i2 == 3) {
                    i3 = -height;
                }
                this.mIsScrolling = true;
                if (this.mOrientation == 1) {
                    this.mScroller.startScroll(0, this.mFooterViewManager.getPaddingBottom(), 0, i3 - this.mFooterViewManager.getPaddingBottom(), AbsAnimation.DEFAULT_ANIMATION_TIME);
                } else {
                    this.mScroller.startScroll(this.mFooterViewManager.getPaddingRight(), 0, i3 - this.mFooterViewManager.getPaddingRight(), 0, AbsAnimation.DEFAULT_ANIMATION_TIME);
                }
                this.mEdgeJudge.trigger();
            }
        }
    }

    public void setDownRefreshFinish(boolean z) {
        if (this.mHeaderViewManager != null) {
            this.mDownPullFinish = z;
            this.mHeaderViewManager.setFinish(z);
        }
    }

    public void setUpRefreshFinish(boolean z) {
        if (this.mFooterViewManager != null) {
            this.mUpPullFinish = z;
            this.mFooterViewManager.setFinish(z);
        }
        this.mIsAutoLoading = false;
    }

    private void resetHeadViewPadding(int i) {
        if (this.mHeaderViewManager != null) {
            int height = this.mOrientation == 1 ? this.mHeaderViewManager.getHeight() : this.mHeaderViewManager.getWidth();
            if (height != 0) {
                int i2 = 0;
                if (i != 2 && i == 3) {
                    i2 = -height;
                }
                this.mIsScrolling = true;
                if (this.mOrientation != 1) {
                    this.mScroller.startScroll(this.mHeaderViewManager.getPaddingLeft(), 0, i2 - this.mHeaderViewManager.getPaddingLeft(), 0, AbsAnimation.DEFAULT_ANIMATION_TIME);
                } else if (this.mIsHeadViewHeightContainLogoImage) {
                    this.mScroller.startScroll(0, this.mHeaderViewManager.getPaddingTop(), 0, i2 - this.mHeaderViewManager.getPaddingTop(), AbsAnimation.DEFAULT_ANIMATION_TIME);
                } else if (i == 2) {
                    this.mScroller.startScroll(0, this.mHeaderViewManager.getPaddingTop(), 0, (this.mHeaderViewManager.getImageHeight() - this.mHeaderViewManager.getPaddingTop()) - this.mHeaderViewManager.getHeight(), AbsAnimation.DEFAULT_ANIMATION_TIME);
                } else if (i == 3) {
                    this.mScroller.startScroll(0, this.mHeaderViewManager.getPaddingTop(), 0, i2 - this.mHeaderViewManager.getPaddingTop(), AbsAnimation.DEFAULT_ANIMATION_TIME);
                }
                this.mEdgeJudge.trigger();
            }
        }
    }

    public void setOnRefreshListener(PullToRefreshFeature.OnPullToRefreshListener onPullToRefreshListener) {
        this.mPullRefreshListener = onPullToRefreshListener;
        this.mIsRefreshable = true;
    }

    public void setOnRefreshListener(DragToRefreshFeature.OnDragToRefreshListener onDragToRefreshListener) {
        this.mDragRefreshListener = onDragToRefreshListener;
        this.mIsRefreshable = true;
    }

    private void onRefresh() {
        if (this.mPullDirection == 4 || this.mPullDirection == 6) {
            if (this.mDownPullFinish) {
                onRefreshComplete();
                return;
            }
            if (this.mPullRefreshListener != null) {
                this.mPullRefreshListener.onPullDownToRefresh();
            }
            if (this.mDragRefreshListener != null) {
                this.mDragRefreshListener.onDragPositive();
            }
        } else if (this.mPullDirection != 5 && this.mPullDirection != 7) {
        } else {
            if (this.mUpPullFinish) {
                onRefreshComplete();
            } else if (this.mIsNeedPullUpToRefresh) {
                if (this.mPullRefreshListener != null) {
                    this.mPullRefreshListener.onPullUpToRefresh();
                }
                if (this.mDragRefreshListener != null) {
                    this.mDragRefreshListener.onDragNegative();
                }
            }
        }
    }

    public void onRefreshComplete() {
        this.mState = 3;
        if (this.mPullDirection == 4 || this.mPullDirection == 6) {
            TBSoundPlayer.getInstance().playScene(2);
            if (this.mHeaderViewManager != null) {
                RefreshHeadViewManager refreshHeadViewManager = this.mHeaderViewManager;
                refreshHeadViewManager.setUpdatedTextView("最近更新:" + new Date().toLocaleString());
                changeHeaderViewByState();
            }
        } else if ((this.mPullDirection == 5 || this.mPullDirection == 7) && this.mFooterViewManager != null) {
            RefreshHeadViewManager refreshHeadViewManager2 = this.mFooterViewManager;
            refreshHeadViewManager2.setUpdatedTextView("最近更新:" + new Date().toLocaleString());
            if (this.mIsNeedPullUpToRefresh) {
                changeFooterViewByState();
                return;
            }
            this.mIsAutoLoading = false;
            if (!this.mUpPullFinish) {
                this.mFooterViewManager.changeHeaderViewByState(3);
                resetFooterViewPadding(3);
            }
        }
    }

    public void setIsDownRefreshing() {
        if (this.mHeaderViewManager != null) {
            this.mState = 2;
            changeHeaderViewByState();
            this.mHeaderViewManager.setPadding(0, 0, 0, 0);
            this.mPullDirection = 4;
        }
    }

    public void setIsUpRefreshing() {
        if (this.mFooterViewManager != null) {
            this.mPullDirection = 5;
            this.mState = 2;
            changeHeaderViewByState();
            this.mFooterViewManager.setPadding(0, 0, 0, 0);
        }
    }

    public int getPullDirection() {
        return this.mPullDirection;
    }

    public void destroy() {
        this.mPullRefreshListener = null;
        this.mDragRefreshListener = null;
    }

    public int getState() {
        return this.mState;
    }
}
