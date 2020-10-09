package com.taobao.onlinemonitor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Rect;
import android.util.Log;
import android.view.Choreographer;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import com.taobao.onlinemonitor.OnLineMonitor;
import com.taobao.onlinemonitor.OnLineMonitorApp;
import com.taobao.onlinemonitor.TraceDetail;
import java.util.Map;
import java.util.WeakHashMap;

public class SmoothCalculate {
    static final int CLICK_OPERATOR_MAX_COUNT = 4;
    static final int FPS_OPERATOR_MAX_COUNT = 10;
    static final int MIN_FRAME_COUNT = 5;
    int mActivityClickCount;
    int mActivityDragCount;
    int mActivityFlingCount;
    int mActivityTotalBadSmCount;
    int mActivityTotalBadSmUsedTime;
    int mActivityTotalFlingCount;
    int mActivityTotalFpsCount;
    int mActivityTotalSmCount;
    int mActivityTotalSmLayoutTimes;
    int mActivityTotalSmUsedTime;
    int mBadSmCount;
    Class mClassRecyclerView;
    Class mClassViewPager;
    int mDepth;
    int mDragFpsCount;
    int mDrawTimesOnDrag;
    int mDrawTimesOnFling;
    boolean mFetchSmoothView = false;
    int mFlingFpsCount;
    long mFlyFrameStartTime = 0;
    MyFrameCallback mFrameCallback;
    long mFrameEndTime = 0;
    long mFrameStartTime = 0;
    long mFrameTimeArrayStartTime;
    short[] mFrameTimeByteArray;
    short mFrameTimeIndex;
    boolean mIsActivityPaused;
    boolean mIsFlingStart;
    boolean mIsTouchDownMode;
    long mLastFrameTimeNanos;
    volatile View mLastSmoothView;
    long mLastTouchDownTime;
    long mLastTouchTime;
    long mMaxDelayTimeOnFling;
    long mMaxSMInterval;
    float mMotionEventXOnDown;
    float mMotionEventYOnDown;
    String mMovetype = "Drag";
    boolean mNeedScrollView;
    OnLineMonitor mOnLineMonitor;
    Rect mRectView = new Rect();
    TraceDetail.SmStat mSmStat;
    boolean mStartActivityOnTouch;
    int mTotalBadSmTime;
    int mTotalSmCount;
    long mTotalTimeOnFling;
    long mTouchCount;
    String mViewName;
    WeakHashMap<View, Integer> mWeakSmoothViewMap = new WeakHashMap<>();

    protected SmoothCalculate(OnLineMonitor onLineMonitor) {
        this.mOnLineMonitor = onLineMonitor;
    }

    /* access modifiers changed from: protected */
    public void onActivityCreated(Activity activity) {
        if (OnLineMonitor.sApiLevel >= 16) {
            this.mFrameCallback = new MyFrameCallback();
        }
        try {
            this.mClassViewPager = Class.forName("androidx.viewpager.widget.ViewPager");
        } catch (Throwable unused) {
        }
        try {
            this.mClassRecyclerView = Class.forName("androidx.recyclerview.widget.RecyclerView");
        } catch (Throwable unused2) {
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityPaused(Activity activity) {
        this.mViewName = null;
        this.mFrameStartTime = 0;
        this.mFrameTimeArrayStartTime = 0;
        this.mFrameEndTime = 0;
        this.mNeedScrollView = false;
        this.mTouchCount = 0;
        this.mLastSmoothView = null;
        this.mActivityTotalSmCount = 0;
        this.mActivityTotalFpsCount = 0;
        this.mActivityTotalFlingCount = 0;
        this.mActivityTotalBadSmCount = 0;
        this.mActivityTotalSmUsedTime = 0;
        this.mActivityFlingCount = 0;
        this.mActivityDragCount = 0;
        this.mActivityClickCount = 0;
        this.mActivityTotalSmLayoutTimes = 0;
        this.mActivityTotalBadSmUsedTime = 0;
        this.mDrawTimesOnFling = 0;
        this.mDrawTimesOnDrag = 0;
        this.mTotalTimeOnFling = 0;
        this.mMaxSMInterval = 0;
        this.mBadSmCount = 0;
        this.mTotalSmCount = 0;
        this.mTotalBadSmTime = 0;
        this.mIsActivityPaused = true;
        this.mWeakSmoothViewMap.clear();
    }

    /* access modifiers changed from: protected */
    public void onActivityStarted(Activity activity) {
        this.mIsActivityPaused = false;
        this.mFetchSmoothView = false;
        this.mDragFpsCount = 0;
        this.mFlingFpsCount = 0;
    }

    /* access modifiers changed from: package-private */
    @SuppressLint({"NewApi"})
    public void onTouchDown(MotionEvent motionEvent, long j, View view) {
        if (this.mIsFlingStart) {
            stopSmoothSmCalculate();
        }
        clearSmoothStep();
        if (motionEvent != null) {
            this.mMotionEventYOnDown = motionEvent.getY();
            this.mMotionEventXOnDown = motionEvent.getX();
        }
        this.mStartActivityOnTouch = false;
        this.mIsTouchDownMode = true;
        this.mLastTouchDownTime = j;
        this.mDrawTimesOnDrag = 0;
        this.mDrawTimesOnFling = 0;
        this.mMaxDelayTimeOnFling = 0;
        this.mTotalTimeOnFling = 0;
        this.mTouchCount++;
        this.mIsFlingStart = false;
        this.mFrameStartTime = 0;
        if (this.mFrameTimeByteArray != null) {
            for (int i = 0; i < this.mFrameTimeByteArray.length; i++) {
                this.mFrameTimeByteArray[i] = 0;
            }
            this.mFrameTimeIndex = 0;
            this.mFrameTimeArrayStartTime = System.nanoTime() / 1000000;
        }
        this.mFrameEndTime = 0;
        this.mNeedScrollView = false;
        this.mTotalSmCount = 0;
        this.mTotalBadSmTime = 0;
        this.mBadSmCount = 0;
        this.mMaxSMInterval = 0;
        this.mLastFrameTimeNanos = System.nanoTime();
        if (this.mOnLineMonitor != null) {
            this.mOnLineMonitor.mLayoutTimes = 0;
        }
        if (OnLineMonitor.sApiLevel >= 16 && this.mOnLineMonitor.mLoadTimeCalculate.mChoreographer != null) {
            Choreographer.getInstance().postFrameCallback(this.mFrameCallback);
        }
        if (OnLineMonitor.sIsTraceDetail && !this.mFetchSmoothView && view != null) {
            this.mDepth = 0;
            addSmoothView(view, 0);
            this.mFetchSmoothView = true;
        }
    }

    /* access modifiers changed from: protected */
    public void onTouchFirstMove(MotionEvent motionEvent, long j) {
        this.mNeedScrollView = true;
        findSmoothView(motionEvent);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0071  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x007f  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x009b  */
    /* JADX WARNING: Removed duplicated region for block: B:34:0x009f A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x00a0  */
    @android.annotation.SuppressLint({"NewApi"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onTouchUp(android.view.MotionEvent r15, long r16) {
        /*
            r14 = this;
            r11 = r14
            com.taobao.onlinemonitor.OnLineMonitor r0 = r11.mOnLineMonitor
            com.taobao.onlinemonitor.ActivityLifecycleCallback r0 = r0.mActivityLifecycleCallback
            r12 = 0
            r11.mIsTouchDownMode = r12
            int r1 = com.taobao.onlinemonitor.OnLineMonitor.sApiLevel
            r2 = 16
            if (r1 < r2) goto L_0x001f
            com.taobao.onlinemonitor.OnLineMonitor r1 = r11.mOnLineMonitor
            com.taobao.onlinemonitor.LoadTimeCalculate r1 = r1.mLoadTimeCalculate
            android.view.Choreographer r1 = r1.mChoreographer
            if (r1 == 0) goto L_0x001f
            android.view.Choreographer r1 = android.view.Choreographer.getInstance()
            com.taobao.onlinemonitor.SmoothCalculate$MyFrameCallback r2 = r11.mFrameCallback
            r1.removeFrameCallback(r2)
        L_0x001f:
            boolean r1 = r0.mHasMoved
            r2 = 1
            if (r1 == 0) goto L_0x002d
            int r1 = r0.mEventUsedTime
            r3 = 2000(0x7d0, float:2.803E-42)
            if (r1 <= r3) goto L_0x002b
            goto L_0x002d
        L_0x002b:
            r1 = 1
            goto L_0x002e
        L_0x002d:
            r1 = 0
        L_0x002e:
            boolean r3 = r0.mHasMoved
            r4 = 5
            if (r3 == 0) goto L_0x0067
            if (r15 == 0) goto L_0x0067
            float r5 = r11.mMotionEventYOnDown
            float r6 = r15.getY()
            float r5 = r5 - r6
            float r5 = java.lang.Math.abs(r5)
            float r6 = r11.mMotionEventXOnDown
            float r3 = r15.getX()
            float r6 = r6 - r3
            float r3 = java.lang.Math.abs(r6)
            com.taobao.onlinemonitor.OnLineMonitor r6 = r11.mOnLineMonitor
            com.taobao.onlinemonitor.LoadTimeCalculate r6 = r6.mLoadTimeCalculate
            int r6 = r6.mScreenHeight
            int r6 = r6 / 10
            com.taobao.onlinemonitor.OnLineMonitor r7 = r11.mOnLineMonitor
            com.taobao.onlinemonitor.LoadTimeCalculate r7 = r7.mLoadTimeCalculate
            int r7 = r7.mScreenWidth
            int r7 = r7 / r4
            float r6 = (float) r6
            int r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1))
            if (r5 >= 0) goto L_0x0067
            float r5 = (float) r7
            int r3 = (r3 > r5 ? 1 : (r3 == r5 ? 0 : -1))
            if (r3 >= 0) goto L_0x0067
            r1 = 0
            r13 = 1
            goto L_0x0068
        L_0x0067:
            r13 = 0
        L_0x0068:
            r2 = 0
            r11.mMotionEventYOnDown = r2
            r11.mMotionEventXOnDown = r2
            boolean r2 = r11.mStartActivityOnTouch
            if (r2 == 0) goto L_0x0074
            r11.mStartActivityOnTouch = r12
            r1 = 0
        L_0x0074:
            boolean r2 = r0.mHasMoved
            if (r2 == 0) goto L_0x007d
            int r2 = r11.mDrawTimesOnDrag
            if (r2 >= r4) goto L_0x007d
            r1 = 0
        L_0x007d:
            if (r1 == 0) goto L_0x0094
            com.taobao.onlinemonitor.OnLineMonitor r1 = r11.mOnLineMonitor
            java.lang.String r1 = r1.mActivityName
            int r2 = r0.mEventCount
            int r3 = r0.mEventUsedTime
            long r3 = (long) r3
            long r5 = r0.mMaxDelayedTime
            int r7 = r11.mDrawTimesOnDrag
            android.view.View r10 = r11.mLastSmoothView
            r0 = r14
            r8 = r16
            r0.commitActivityDragFps(r1, r2, r3, r5, r7, r8, r10)
        L_0x0094:
            r14.clearSmoothStep()
            android.view.View r0 = r11.mLastSmoothView
            if (r0 != 0) goto L_0x009d
            r11.mFetchSmoothView = r12
        L_0x009d:
            if (r13 == 0) goto L_0x00a0
            return
        L_0x00a0:
            r14.startSmCalculate()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.SmoothCalculate.onTouchUp(android.view.MotionEvent, long):void");
    }

    /* access modifiers changed from: package-private */
    @SuppressLint({"NewApi"})
    public void startSmCalculate() {
        if (OnLineMonitor.sApiLevel >= 16 && this.mOnLineMonitor != null && this.mOnLineMonitor.mLoadTimeCalculate != null && this.mOnLineMonitor.mLoadTimeCalculate.mChoreographer != null) {
            if (OnLineMonitor.sIsDetailDebug) {
                Log.e("OnLineMonitor", "startSmCalculate");
            }
            this.mTotalSmCount = 0;
            this.mTotalBadSmTime = 0;
            this.mBadSmCount = 0;
            this.mMaxSMInterval = 0;
            this.mFrameStartTime = this.mLastFrameTimeNanos;
            this.mFlyFrameStartTime = System.nanoTime() / 1000000;
            if (this.mFrameStartTime == 0) {
                this.mFrameStartTime = this.mFlyFrameStartTime;
            }
            if (!(this.mOnLineMonitor.mSmoothDetailDataNotify == null || this.mFrameTimeByteArray == null)) {
                this.mFrameTimeArrayStartTime = this.mFlyFrameStartTime;
                for (int i = 0; i < this.mFrameTimeByteArray.length; i++) {
                    this.mFrameTimeByteArray[i] = 0;
                }
                this.mFrameTimeIndex = 0;
            }
            this.mFrameEndTime = 0;
            this.mIsFlingStart = true;
            this.mDrawTimesOnFling = 0;
            this.mMaxDelayTimeOnFling = 0;
            this.mFrameCallback.mInnerDrawCount = 0;
            this.mOnLineMonitor.mLayoutTimes = 0;
            if (this.mOnLineMonitor.mOnLineStat != null) {
                this.mOnLineMonitor.mOnLineStat.isFlingMode = true;
            }
            Choreographer.getInstance().postFrameCallback(this.mFrameCallback);
        }
    }

    /* access modifiers changed from: package-private */
    @SuppressLint({"NewApi"})
    public void stopSmoothSmCalculate() {
        if (OnLineMonitor.sApiLevel >= 16 && this.mOnLineMonitor.mLoadTimeCalculate.mChoreographer != null) {
            onScrollFinished();
            if (OnLineMonitor.sIsDetailDebug) {
                Log.e("OnLineMonitor", "stopSmoothSmCalculate");
            }
            this.mOnLineMonitor.mOnLineStat.isFlingMode = false;
            this.mIsFlingStart = false;
            Choreographer.getInstance().removeFrameCallback(this.mFrameCallback);
        }
    }

    /* access modifiers changed from: package-private */
    public void findSmoothView(MotionEvent motionEvent) {
        View view;
        if (OnLineMonitor.sIsTraceDetail) {
            int x = (int) motionEvent.getX();
            int y = (int) motionEvent.getY();
            if (!this.mIsActivityPaused) {
                if (this.mWeakSmoothViewMap.size() > 0) {
                    int i = -1;
                    for (Map.Entry next : this.mWeakSmoothViewMap.entrySet()) {
                        if (!(next == null || (view = (View) next.getKey()) == null)) {
                            view.getGlobalVisibleRect(this.mRectView);
                            if (this.mRectView.contains(x, y)) {
                                int intValue = ((Integer) next.getValue()).intValue();
                                if (i == -1 || i < intValue) {
                                    this.mLastSmoothView = view;
                                    i = intValue;
                                }
                            }
                        }
                    }
                }
                if (OnLineMonitor.sIsDetailDebug) {
                    Log.e("OnLineMonitor", "LastSmoothView=" + this.mLastSmoothView);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void addSmoothView(View view, int i) {
        if (isSmoothView(view)) {
            this.mWeakSmoothViewMap.put(view, Integer.valueOf(i));
        }
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (this.mDepth < i) {
                this.mDepth = i;
            }
            int childCount = viewGroup.getChildCount();
            for (int i2 = 0; i2 < childCount; i2++) {
                addSmoothView(viewGroup.getChildAt(i2), i + 1);
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean isSmoothView(View view) {
        if ((view instanceof AbsListView) || (view instanceof ScrollView) || (view instanceof HorizontalScrollView) || (view instanceof OnLineMonitorApp.SmoothView) || (view instanceof WebView)) {
            return true;
        }
        if (this.mClassViewPager != null && this.mClassViewPager.getClass().isAssignableFrom(view.getClass())) {
            return true;
        }
        if (this.mClassRecyclerView == null || !this.mClassRecyclerView.isAssignableFrom(view.getClass())) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void onScrollFinished() {
        String str;
        if (this.mLastSmoothView != null) {
            OnLineMonitor onLineMonitor = this.mOnLineMonitor;
            str = OnLineMonitor.getSimpleName(this.mLastSmoothView.getClass().getName());
        } else {
            str = "";
        }
        this.mViewName = str;
        this.mTotalTimeOnFling = (this.mFrameEndTime - this.mFrameStartTime) / 1000000;
        boolean z = true;
        if (this.mStartActivityOnTouch) {
            this.mStartActivityOnTouch = false;
            z = false;
        }
        if ((this.mOnLineMonitor.mActivityLifecycleCallback.mHasFling && this.mDrawTimesOnFling < 5) || (!this.mOnLineMonitor.mActivityLifecycleCallback.mHasFling && this.mDrawTimesOnFling < 3)) {
            z = false;
        }
        if (z) {
            commitActivityFlingFps(this.mTotalSmCount, this.mTotalTimeOnFling, this.mMaxSMInterval, this.mLastSmoothView, this.mBadSmCount);
        }
        clearSmoothStep();
        this.mIsFlingStart = false;
    }

    /* access modifiers changed from: package-private */
    public void onDraw(long j) {
        if (this.mIsTouchDownMode) {
            this.mDrawTimesOnDrag++;
        }
        if (this.mIsFlingStart) {
            this.mDrawTimesOnFling++;
        }
    }

    /* access modifiers changed from: package-private */
    public int commitActivityDragFps(String str, int i, long j, long j2, int i2, long j3, View view) {
        long j4;
        long j5;
        int i3;
        int i4;
        int i5;
        long j6;
        int i6 = i2;
        if (i == 0) {
            return 0;
        }
        long j7 = j3 - this.mLastTouchDownTime;
        if (this.mOnLineMonitor.mActivityLifecycleCallback.mHasMoved) {
            if (this.mActivityDragCount >= 10) {
                return 0;
            }
        } else if (this.mActivityClickCount >= 4) {
            return 0;
        }
        if (j7 == 0 || j7 >= 60000) {
            return 0;
        }
        if (((int) (((long) (this.mTotalSmCount * 1000)) / j7)) >= 60) {
            long j8 = (long) ((this.mTotalSmCount * 1000) / 60);
            if (this.mTotalBadSmTime >= 0) {
                j8 += (long) this.mTotalBadSmTime;
            }
            j4 = j8;
        } else {
            j4 = j7;
        }
        int i7 = (int) (((long) (this.mTotalSmCount * 1000)) / j4);
        int i8 = i7 > 60 ? 60 : i7;
        this.mActivityTotalSmCount += this.mTotalSmCount;
        this.mActivityTotalSmUsedTime = (int) (((long) this.mActivityTotalSmUsedTime) + j4);
        this.mActivityTotalBadSmUsedTime += this.mTotalBadSmTime;
        this.mActivityTotalBadSmCount += this.mBadSmCount;
        this.mActivityTotalSmLayoutTimes += this.mOnLineMonitor.mLayoutTimes;
        this.mActivityTotalFpsCount++;
        int i9 = this.mActivityDragCount;
        if (this.mOnLineMonitor.mActivityLifecycleCallback.mHasMoved) {
            this.mActivityDragCount++;
            this.mMovetype = "Drag";
        } else {
            i9 = this.mActivityClickCount;
            this.mActivityClickCount++;
            this.mMovetype = "Click";
        }
        int i10 = i9;
        mergeSmoothStep();
        if (i10 < 0) {
            i3 = i8;
            j5 = j4;
            i4 = i6;
            notifySmoothStop(j4, this.mTotalSmCount, i10, this.mOnLineMonitor.mActivityLifecycleCallback.mMoveDirection, (int) (this.mOnLineMonitor.mActivityLifecycleCallback.mMoveRelativeTime / 1000), j2, this.mBadSmCount, this.mTotalBadSmTime, this.mMovetype, this.mOnLineMonitor.mLayoutTimes, i3);
        } else {
            i3 = i8;
            j5 = j4;
            i4 = i6;
        }
        clearSmoothStep();
        if (this.mOnLineMonitor.mActivityLifecycleCallback.mHasMoved && i4 >= 5) {
            this.mOnLineMonitor.mLoadTimeCalculate.stopLoadTimeCalculate();
        }
        if (!OnLineMonitor.sIsTraceDetail || this.mOnLineMonitor.mActivityRuntimeInfo == null) {
            i5 = i3;
            j6 = j5;
        } else {
            TraceDetail.SmStat smStat = new TraceDetail.SmStat();
            smStat.index = this.mDragFpsCount;
            smStat.eventCount = (short) i;
            smStat.eventUseTime = (short) ((int) j);
            smStat.drawCount = (short) i4;
            smStat.layoutTimes = this.mOnLineMonitor.mLayoutTimes;
            smStat.eventMaxDelaytime = (short) ((int) j2);
            smStat.maxSMInterval = (short) ((int) this.mMaxSMInterval);
            j6 = j5;
            smStat.usetime = (short) ((int) j6);
            i5 = i3;
            smStat.sm = (short) i5;
            smStat.badSmCount = (short) this.mBadSmCount;
            smStat.totalSmCount = (short) this.mTotalSmCount;
            smStat.totalBadSmTime = (short) this.mTotalBadSmTime;
            if (view != null) {
                smStat.viewName = OnLineMonitor.getSimpleName(view.getClass().getName());
            }
            this.mOnLineMonitor.mActivityRuntimeInfo.dragList.add(smStat);
        }
        this.mDragFpsCount++;
        if (OnLineMonitor.sIsTraceDetail) {
            Log.e("OnLineMonitor", this.mMovetype + ", FPS=" + i5 + ", TotalSmCount=" + this.mTotalSmCount + ", TotalTime=" + j6 + ", MaxSMInterval=" + this.mMaxSMInterval);
        }
        notifySmoothDetailData(1, this.mFrameTimeArrayStartTime, j6, this.mFrameTimeIndex, this.mFrameTimeByteArray);
        return i5;
    }

    /* access modifiers changed from: package-private */
    public void commitActivityFlingFps(int i, long j, long j2, View view, int i2) {
        int i3;
        long j3;
        long j4;
        int i4;
        boolean z;
        long j5;
        int i5 = i;
        if (this.mOnLineMonitor.mActivityName != null) {
            int i6 = 3;
            boolean z2 = this.mOnLineMonitor.mActivityLifecycleCallback.mHasFling;
            int i7 = this.mActivityFlingCount;
            if (z2) {
                if (this.mActivityFlingCount < 10) {
                    this.mMovetype = "Fling";
                    i3 = i7;
                } else {
                    return;
                }
            } else if (this.mActivityClickCount < 4) {
                int i8 = this.mActivityClickCount;
                this.mMovetype = "Click";
                i3 = i8;
                i6 = 2;
            } else {
                return;
            }
            if (j != 0 && j < 60000 && i5 != 0) {
                this.mOnLineMonitor.startMemoryMonitor();
                int i9 = i5 * 1000;
                long j6 = (long) i9;
                if (((int) (j6 / j)) >= 60) {
                    long j7 = (long) (i9 / 60);
                    if (this.mTotalBadSmTime >= 0) {
                        j7 += (long) this.mTotalBadSmTime;
                    }
                    j3 = j7;
                } else {
                    j3 = j;
                }
                int i10 = (int) (j6 / j3);
                int i11 = i10 > 60 ? 60 : i10;
                this.mActivityTotalSmCount += i5;
                this.mActivityTotalSmUsedTime = (int) (((long) this.mActivityTotalSmUsedTime) + j3);
                this.mActivityTotalBadSmUsedTime += this.mTotalBadSmTime;
                this.mActivityTotalBadSmCount += this.mBadSmCount;
                this.mActivityTotalSmLayoutTimes += this.mOnLineMonitor.mLayoutTimes;
                this.mActivityTotalFpsCount++;
                mergeSmoothStep();
                if (i3 < i6) {
                    i4 = i11;
                    j4 = j3;
                    z = z2;
                    notifySmoothStop(j3, i, i3, this.mOnLineMonitor.mActivityLifecycleCallback.mMoveDirection, (int) (this.mOnLineMonitor.mActivityLifecycleCallback.mMoveRelativeTime / 1000), j2, this.mBadSmCount, this.mTotalBadSmTime, this.mMovetype, this.mOnLineMonitor.mLayoutTimes, i4);
                } else {
                    i4 = i11;
                    j4 = j3;
                    z = z2;
                }
                clearSmoothStep();
                if (z) {
                    this.mActivityTotalFlingCount++;
                    this.mActivityFlingCount++;
                } else {
                    this.mActivityClickCount++;
                }
                if (!OnLineMonitor.sIsTraceDetail || this.mOnLineMonitor.mActivityRuntimeInfo == null) {
                    j5 = j4;
                } else {
                    TraceDetail.SmStat smStat = new TraceDetail.SmStat();
                    smStat.index = this.mFlingFpsCount;
                    smStat.eventCount = 0;
                    smStat.eventRate = 0;
                    smStat.drawCount = 0;
                    smStat.layoutTimes = this.mOnLineMonitor.mLayoutTimes;
                    smStat.eventMaxDelaytime = (short) ((int) j2);
                    smStat.maxSMInterval = (short) ((int) this.mMaxSMInterval);
                    j5 = j4;
                    smStat.usetime = (short) ((int) j5);
                    int i12 = i4;
                    smStat.sm = (short) i12;
                    smStat.badSmCount = (short) this.mBadSmCount;
                    smStat.totalSmCount = (short) this.mTotalSmCount;
                    smStat.totalBadSmTime = (short) this.mTotalBadSmTime;
                    if (view != null) {
                        smStat.viewName = OnLineMonitor.getSimpleName(view.getClass().getName());
                    }
                    if (OnLineMonitor.sIsTraceDetail) {
                        Log.e("OnLineMonitor", this.mMovetype + ", FPS=" + i12 + ", TotalFPSCount=" + this.mTotalSmCount + ", TotalTime=" + j5 + ", MaxSMInterval=" + this.mMaxSMInterval);
                    }
                    if (!(!OnLineMonitor.sIsTraceDetail || this.mOnLineMonitor.mActivityRuntimeInfo == null || this.mOnLineMonitor.mActivityRuntimeInfo.fpsList == null)) {
                        this.mOnLineMonitor.mActivityRuntimeInfo.fpsList.add(smStat);
                    }
                }
                if (this.mOnLineMonitor.mActivityLifecycleCallback.mHasMoved && i5 >= 5) {
                    this.mOnLineMonitor.mLoadTimeCalculate.stopLoadTimeCalculate();
                }
                this.mFlingFpsCount++;
                notifySmoothDetailData(2, this.mFrameTimeArrayStartTime, j5, this.mFrameTimeIndex, this.mFrameTimeByteArray);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void notifySmoothStop(long j, int i, int i2, String str, int i3, long j2, int i4, int i5, String str2, int i6, int i7) {
        if (this.mOnLineMonitor.mIsActivityColdOpen && this.mOnLineMonitor.mOnlineStatistics != null && this.mOnLineMonitor.mActivityRuntimeInfo != null) {
            int size = this.mOnLineMonitor.mOnlineStatistics.size();
            for (int i8 = 0; i8 < size; i8++) {
                OnlineStatistics onlineStatistics = this.mOnLineMonitor.mOnlineStatistics.get(i8);
                if (onlineStatistics != null) {
                    onlineStatistics.onSmoothStop(this.mOnLineMonitor.mOnLineStat, this.mOnLineMonitor.mActivityRuntimeInfo, j, i, this.mOnLineMonitor.mActivityRuntimeInfo.activitySingleBadSmoothStepCount, i2, str, i3, j2, i4, i5, str2, i6);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void notifySmoothDetailData(int i, long j, long j2, short s, short[] sArr) {
        if (this.mOnLineMonitor != null && this.mOnLineMonitor.mSmoothDetailDataNotify != null && s > 0) {
            this.mOnLineMonitor.mSmoothDetailDataNotify.onSmoothDetailNotify(i, this.mOnLineMonitor.mOnLineStat, j, j2, s, sArr);
        }
    }

    /* access modifiers changed from: package-private */
    public void clearSmoothStep() {
        int[] iArr;
        if (this.mOnLineMonitor != null && this.mOnLineMonitor.mActivityRuntimeInfo != null && (iArr = this.mOnLineMonitor.mActivityRuntimeInfo.activitySingleBadSmoothStepCount) != null) {
            for (int i = 0; i < iArr.length; i++) {
                iArr[i] = 0;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void mergeSmoothStep() {
        OnLineMonitor.ActivityRuntimeInfo activityRuntimeInfo = this.mOnLineMonitor.mActivityRuntimeInfo;
        if (activityRuntimeInfo != null) {
            int[] iArr = activityRuntimeInfo.activityBadSmoothStepCount;
            int[] iArr2 = activityRuntimeInfo.activitySingleBadSmoothStepCount;
            if (iArr != null && iArr2 != null) {
                for (int i = 0; i < iArr.length; i++) {
                    iArr[i] = iArr[i] + iArr2[i];
                }
            }
        }
    }

    @SuppressLint({"NewApi"})
    public class MyFrameCallback implements Choreographer.FrameCallback {
        int mInnerDrawCount = 0;

        public MyFrameCallback() {
        }

        public void doFrame(long j) {
            SmoothCalculate.this.mLastFrameTimeNanos = j;
            long nanoTime = System.nanoTime();
            SmoothCalculate.this.mTotalSmCount++;
            if (SmoothCalculate.this.mFrameEndTime > 0) {
                float f = ((float) (nanoTime - SmoothCalculate.this.mFrameEndTime)) / 1000000.0f;
                if (SmoothCalculate.this.mFrameTimeByteArray != null && SmoothCalculate.this.mFrameTimeIndex < SmoothCalculate.this.mFrameTimeByteArray.length) {
                    SmoothCalculate.this.mFrameTimeByteArray[SmoothCalculate.this.mFrameTimeIndex] = (short) ((int) f);
                    SmoothCalculate smoothCalculate = SmoothCalculate.this;
                    smoothCalculate.mFrameTimeIndex = (short) (smoothCalculate.mFrameTimeIndex + 1);
                }
                long j2 = (long) f;
                if (SmoothCalculate.this.mMaxSMInterval < j2) {
                    SmoothCalculate.this.mMaxSMInterval = j2;
                }
                if (SmoothCalculate.this.mOnLineMonitor.mActivityRuntimeInfo != null && f >= 16.8f) {
                    int[] iArr = SmoothCalculate.this.mOnLineMonitor.mActivityRuntimeInfo.activitySingleBadSmoothStepCount;
                    int i = (((int) f) / OnLineMonitorApp.sSmoothStepInterval) - 1;
                    if (i > iArr.length - 1) {
                        i = iArr.length - 1;
                    }
                    if (i >= 0) {
                        SmoothCalculate.this.mBadSmCount++;
                        SmoothCalculate smoothCalculate2 = SmoothCalculate.this;
                        smoothCalculate2.mTotalBadSmTime = (int) (((float) smoothCalculate2.mTotalBadSmTime) + (f - 16.8f));
                        iArr[i] = iArr[i] + 1;
                    }
                }
            }
            SmoothCalculate.this.mFrameEndTime = nanoTime;
            if (SmoothCalculate.this.mIsFlingStart) {
                this.mInnerDrawCount++;
                int i2 = this.mInnerDrawCount - SmoothCalculate.this.mDrawTimesOnFling;
                if (i2 >= 2 || i2 <= -2) {
                    if (OnLineMonitor.sIsDetailDebug) {
                        Log.e("OnLineMonitor", "停止滑动统计 , stopFrame=" + i2);
                    }
                    SmoothCalculate.this.stopSmoothSmCalculate();
                }
                long nanoTime2 = System.nanoTime() / 1000000;
                if (i2 == 1 && nanoTime2 - SmoothCalculate.this.mFlyFrameStartTime > 5000) {
                    SmoothCalculate.this.mFlyFrameStartTime = nanoTime2;
                    Log.e("OnLineMonitor", "界面有不停的刷新，可能有视频或者动画!");
                    SmoothCalculate.this.stopSmoothSmCalculate();
                }
            }
            if (SmoothCalculate.this.mIsTouchDownMode || SmoothCalculate.this.mIsFlingStart) {
                Choreographer.getInstance().postFrameCallback(SmoothCalculate.this.mFrameCallback);
            }
        }
    }
}
