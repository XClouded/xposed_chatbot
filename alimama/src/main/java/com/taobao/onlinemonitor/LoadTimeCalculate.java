package com.taobao.onlinemonitor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.graphics.drawable.DrawableWrapper;
import android.graphics.drawable.PictureDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.taobao.onlinemonitor.OnLineMonitor;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import java.lang.reflect.Field;
import java.util.Arrays;

public class LoadTimeCalculate {
    protected static final int CHECK_LAYOUT_FITER = 1000;
    protected static final int CHECK_LAYOUT_TIMER = 100;
    protected static final int LAYOUT_FITER = 20000;
    OnLineMonitor.ActivityRuntimeInfo mActivityRuntimeInfo;
    OnLineMonitor.ActivityRuntimeInfo mActivityRuntimeInfoOld;
    int mActivityViewCount;
    int mActivityVisibleViewCount;
    int mAllScreenLoadTimes;
    int mBadSmCount;
    Choreographer mChoreographer;
    Class mClassV4DrawableWrapper;
    protected Context mContext;
    protected volatile View mDecorView;
    boolean mEditTextViewFocused;
    int mEmptyTextView;
    short mFirstRelativeLayoutDepth;
    Choreographer.FrameCallback mFrameCallback;
    long mFrameEndTime;
    boolean mFrameIsLoad;
    boolean mFrameIsTotalLoad;
    long mFrameStartTime;
    long mFrameTimeArrayStartTime;
    short[] mFrameTimeByteArray;
    short mFrameTimeIndex;
    int mHalfScreenArea;
    boolean mHasEditTextView;
    boolean mHasSmoothView;
    int mHasfScreenLoadTimes;
    byte[] mHeightLocation;
    boolean mIsLayouted;
    boolean mIsPortrait = true;
    boolean mIsWaitDataFill;
    byte[] mLargeLocation;
    Field mLastFrameTimeField;
    long mLastFrameTimeNanos;
    int mLastHeightPercent;
    int mLastHeightPercentEqualTimes;
    long mLastLayoutTime = 0;
    int mLastViewGroupCount;
    int mLastWidthPercent;
    long mLayoutCheckFreqControl;
    int mLayoutMinTimeControl;
    short mLayoutTimesOnLoad;
    long mLoadStartTime;
    long mLoadTime = 0;
    boolean mLoadTimeGetted;
    int[] mLocationPos;
    short mMaxLayoutDepth;
    long mMaxLayoutUseTime;
    short mMaxRelativeLayoutDepth;
    short mMeasureTimes;
    ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;
    protected OnLineMonitor mOnLineMonitor;
    Rect mRectCurrent;
    Rect[] mRectRelativeChild;
    Rect mRectResult;
    Rect mRectScreen;
    short mRedundantLayout;
    int mScreenArea;
    int mScreenAreaIn10;
    int mScreenHeight;
    int mScreenWidth;
    byte[] mSmallLocation;
    int mSmoothViewOutRevLayoutDepth;
    long mStartActivityTime = 0;
    short mSuspectRelativeLayout;
    Choreographer.FrameCallback mTimeFrameCallback;
    int mTotalBadSmTime;
    short mTotalLayOutTimes;
    long mTotalLayoutUseTime;
    int mTotalSmCount;
    int mViewEqualTimes;
    int mViewGroupCount;
    int mViewGroupCountEqualTimes;
    volatile View mViewWaitDataFill;
    int mVisibleArea;
    byte[] mWidthLocation;

    protected LoadTimeCalculate(OnLineMonitor onLineMonitor) {
        this.mLastViewGroupCount = 0;
        this.mViewGroupCount = 0;
        this.mHasfScreenLoadTimes = 0;
        this.mViewGroupCountEqualTimes = 0;
        this.mViewEqualTimes = 0;
        this.mAllScreenLoadTimes = 0;
        this.mContext = null;
        this.mIsWaitDataFill = false;
        this.mScreenWidth = -1;
        this.mScreenHeight = -1;
        this.mMaxLayoutDepth = 1;
        this.mRectResult = new Rect();
        this.mRectCurrent = new Rect();
        this.mRectScreen = new Rect();
        this.mIsLayouted = false;
        this.mLocationPos = new int[2];
        this.mVisibleArea = 0;
        this.mLayoutCheckFreqControl = 100;
        this.mLayoutMinTimeControl = 50;
        this.mEmptyTextView = 0;
        this.mOnLineMonitor = onLineMonitor;
        if (OnLineMonitor.sIsTraceDetail) {
            this.mRectRelativeChild = new Rect[4];
            for (int i = 0; i < 4; i++) {
                this.mRectRelativeChild[i] = new Rect();
            }
        }
        try {
            this.mClassV4DrawableWrapper = Class.forName("androidx.core.graphics.drawable.DrawableWrapper");
        } catch (Throwable unused) {
        }
    }

    /* access modifiers changed from: package-private */
    @SuppressLint({"NewApi"})
    public void initGetLastFrameTimeField() {
        OnLineMonitor onLineMonitor = this.mOnLineMonitor;
        if (OnLineMonitor.sApiLevel >= 16 && this.mChoreographer == null) {
            try {
                this.mChoreographer = Choreographer.getInstance();
                this.mLastFrameTimeField = this.mChoreographer.getClass().getDeclaredField("mLastFrameTimeNanos");
                if (this.mLastFrameTimeField != null) {
                    this.mLastFrameTimeField.setAccessible(true);
                }
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: package-private */
    @SuppressLint({"NewApi"})
    public void getLastFrameTime() {
        OnLineMonitor onLineMonitor = this.mOnLineMonitor;
        if (OnLineMonitor.sApiLevel >= 16 && this.mChoreographer != null && this.mLastFrameTimeField != null) {
            try {
                this.mLastFrameTimeNanos = this.mLastFrameTimeField.getLong(this.mChoreographer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: package-private */
    @SuppressLint({"NewApi"})
    public void postFrameCallback() {
        OnLineMonitor onLineMonitor = this.mOnLineMonitor;
        if (OnLineMonitor.sApiLevel >= 16 && this.mChoreographer != null) {
            this.mChoreographer.postFrameCallback(this.mFrameCallback);
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"NewApi"})
    public void onActivityCreated(Activity activity) {
        float f;
        if (this.mContext == null) {
            this.mContext = activity.getApplicationContext();
            initGetLastFrameTimeField();
            if (this.mScreenWidth <= 0) {
                try {
                    this.mScreenWidth = activity.getResources().getDisplayMetrics().widthPixels;
                    this.mScreenHeight = activity.getResources().getDisplayMetrics().heightPixels;
                    f = activity.getResources().getDisplayMetrics().density;
                } catch (Throwable unused) {
                    WindowManager windowManager = activity.getWindowManager();
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
                    this.mScreenWidth = displayMetrics.widthPixels;
                    this.mScreenHeight = displayMetrics.heightPixels;
                    f = displayMetrics.density;
                }
                if (this.mOnLineMonitor.mOnLineStat.deviceInfo.screenWidth == 0 || this.mOnLineMonitor.mOnLineStat.deviceInfo.screenHeght == 0) {
                    this.mOnLineMonitor.mOnLineStat.deviceInfo.screenHeght = this.mScreenHeight;
                    this.mOnLineMonitor.mOnLineStat.deviceInfo.screenWidth = this.mScreenWidth;
                    this.mOnLineMonitor.mOnLineStat.deviceInfo.density = f;
                }
                this.mScreenArea = (int) (((float) (this.mScreenWidth * this.mScreenHeight)) * 0.8f);
                this.mHalfScreenArea = (this.mScreenWidth * this.mScreenHeight) / 2;
                this.mScreenAreaIn10 = (this.mScreenWidth * this.mScreenHeight) / 10;
                this.mRectScreen.left = 0;
                this.mRectScreen.right = this.mScreenWidth;
                this.mRectScreen.top = 0;
                this.mRectScreen.bottom = this.mScreenHeight;
                this.mWidthLocation = new byte[this.mScreenWidth];
                this.mHeightLocation = new byte[this.mScreenHeight];
                if (this.mScreenHeight > this.mScreenWidth) {
                    this.mLargeLocation = this.mHeightLocation;
                    this.mSmallLocation = this.mWidthLocation;
                } else {
                    this.mLargeLocation = this.mWidthLocation;
                    this.mSmallLocation = this.mHeightLocation;
                }
            }
        }
        this.mActivityRuntimeInfo = this.mOnLineMonitor.mActivityRuntimeInfo;
        OnLineMonitor onLineMonitor = this.mOnLineMonitor;
        if (OnLineMonitor.sApiLevel >= 16 && this.mFrameCallback == null) {
            this.mFrameCallback = new MyFrameCallback();
        }
        if (this.mFrameTimeByteArray != null) {
            for (int i = 0; i < this.mFrameTimeByteArray.length; i++) {
                this.mFrameTimeByteArray[i] = 0;
            }
            this.mFrameTimeIndex = 0;
            this.mFrameTimeArrayStartTime = System.nanoTime() / 1000000;
        }
        OnLineMonitor onLineMonitor2 = this.mOnLineMonitor;
        if (OnLineMonitor.sApiLevel >= 16 && this.mChoreographer != null) {
            if (this.mTimeFrameCallback == null) {
                this.mTimeFrameCallback = new GetFrameTimeCallback();
            }
            this.mFrameStartTime = 0;
            this.mFrameEndTime = 0;
            this.mChoreographer.removeFrameCallback(this.mTimeFrameCallback);
            this.mChoreographer.postFrameCallback(this.mTimeFrameCallback);
        }
        this.mLoadStartTime = this.mOnLineMonitor.mActivityLifecycleCallback.mActivityOncreateTime;
        this.mViewWaitDataFill = null;
    }

    /* access modifiers changed from: protected */
    public void onActivityStarted(Activity activity, View view) {
        this.mDecorView = view;
        if (!this.mOnLineMonitor.mIsActivityColdOpen) {
            this.mLoadStartTime = System.nanoTime() / 1000000;
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityPaused(Activity activity) {
        this.mViewWaitDataFill = null;
        this.mDecorView = null;
        this.mActivityRuntimeInfoOld = this.mActivityRuntimeInfo;
        this.mMaxLayoutDepth = 1;
        this.mLoadTime = 0;
        this.mLastLayoutTime = 0;
        this.mLastViewGroupCount = 0;
        this.mRedundantLayout = 0;
        this.mMaxRelativeLayoutDepth = 0;
        this.mSuspectRelativeLayout = 0;
        this.mLoadTimeGetted = false;
        this.mIsWaitDataFill = false;
        this.mFrameIsTotalLoad = false;
        this.mFrameIsLoad = false;
        this.mLayoutTimesOnLoad = 0;
        this.mTotalLayoutUseTime = 0;
        this.mMaxLayoutUseTime = 0;
        this.mActivityViewCount = 0;
        this.mAllScreenLoadTimes = 0;
        this.mHasfScreenLoadTimes = 0;
        this.mViewGroupCountEqualTimes = 0;
        this.mViewEqualTimes = 0;
        this.mIsLayouted = false;
        this.mTotalLayOutTimes = 0;
        this.mHasfScreenLoadTimes = 0;
        this.mEmptyTextView = 0;
        if (this.mWidthLocation != null) {
            Arrays.fill(this.mWidthLocation, 0, this.mWidthLocation.length, (byte) 0);
            Arrays.fill(this.mHeightLocation, 0, this.mHeightLocation.length, (byte) 0);
        }
        this.mLastWidthPercent = 0;
        this.mLastHeightPercent = 0;
        this.mLastHeightPercentEqualTimes = 0;
        this.mBadSmCount = 0;
        this.mTotalSmCount = 0;
        this.mTotalBadSmTime = 0;
        this.mFrameEndTime = 0;
        this.mFrameStartTime = 0;
        removeFromChoreographer();
    }

    /* access modifiers changed from: package-private */
    @SuppressLint({"NewApi"})
    public boolean isViewIgnore(Drawable drawable) {
        if (drawable == null) {
            return true;
        }
        if ((drawable instanceof AnimationDrawable) || (drawable instanceof PictureDrawable)) {
            return false;
        }
        OnLineMonitor onLineMonitor = this.mOnLineMonitor;
        if (OnLineMonitor.sApiLevel >= 23 && (drawable instanceof DrawableWrapper)) {
            return true;
        }
        boolean z = drawable instanceof ShapeDrawable;
        if (z && "ImageDrawable".equals(drawable.getClass().getSimpleName())) {
            return false;
        }
        if ((drawable instanceof ColorDrawable) || z || (drawable instanceof DrawableContainer)) {
            return true;
        }
        return this.mClassV4DrawableWrapper != null && this.mClassV4DrawableWrapper.isAssignableFrom(drawable.getClass());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:121:0x01cd, code lost:
        if (r4 >= (r0.mScreenHeight / 4)) goto L_0x01e1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:124:0x01d4, code lost:
        if (r3 >= (r0.mScreenWidth / 4)) goto L_0x01e1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:128:0x01df, code lost:
        if (r18.getBackground() != null) goto L_0x01e1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:139:0x020b, code lost:
        if (r4 >= (r0.mScreenHeight / 4)) goto L_0x01e1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:142:0x0212, code lost:
        if (r3 >= (r0.mScreenWidth / 4)) goto L_0x01e1;
     */
    /* JADX WARNING: Removed duplicated region for block: B:144:0x0217  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getViewGroupCount(android.view.View r18, short r19, short r20, short r21) {
        /*
            r17 = this;
            r0 = r17
            r1 = r18
            r2 = 1
            int r3 = r19 + 1
            short r3 = (short) r3
            boolean r4 = r1 instanceof android.view.ViewGroup
            r5 = 4
            if (r4 == 0) goto L_0x0141
            r4 = r1
            android.view.ViewGroup r4 = (android.view.ViewGroup) r4
            int r7 = r4.getChildCount()
            int r8 = r18.getVisibility()
            if (r8 != 0) goto L_0x001c
            r8 = 1
            goto L_0x001d
        L_0x001c:
            r8 = 0
        L_0x001d:
            if (r7 != r2) goto L_0x0025
            short r9 = r0.mRedundantLayout
            int r9 = r9 + r2
            short r9 = (short) r9
            r0.mRedundantLayout = r9
        L_0x0025:
            boolean r9 = r1 instanceof android.widget.RelativeLayout
            if (r9 == 0) goto L_0x0097
            if (r8 == 0) goto L_0x003b
            int r9 = r20 + 1
            short r9 = (short) r9
            short r10 = r0.mFirstRelativeLayoutDepth
            if (r10 != 0) goto L_0x0034
            r0.mFirstRelativeLayoutDepth = r3
        L_0x0034:
            short r10 = r0.mMaxRelativeLayoutDepth
            if (r10 >= r9) goto L_0x003d
            r0.mMaxRelativeLayoutDepth = r9
            goto L_0x003d
        L_0x003b:
            r9 = r20
        L_0x003d:
            boolean r10 = com.taobao.onlinemonitor.OnLineMonitor.sIsTraceDetail
            if (r10 == 0) goto L_0x00ba
            if (r7 > r5) goto L_0x00ba
            r10 = 0
            r11 = 0
        L_0x0045:
            if (r10 >= r7) goto L_0x0081
            android.view.View r12 = r4.getChildAt(r10)
            boolean r13 = r12 instanceof android.view.ViewGroup
            if (r13 == 0) goto L_0x0051
            r11 = 1
            goto L_0x0081
        L_0x0051:
            android.graphics.Rect[] r13 = r0.mRectRelativeChild
            r13 = r13[r10]
            int r14 = r12.getLeft()
            int r15 = r12.getTop()
            int r6 = r12.getRight()
            int r12 = r12.getBottom()
            r13.set(r14, r15, r6, r12)
            r6 = 0
        L_0x0069:
            if (r6 >= r10) goto L_0x007e
            android.graphics.Rect[] r12 = r0.mRectRelativeChild
            r12 = r12[r10]
            android.graphics.Rect[] r13 = r0.mRectRelativeChild
            r13 = r13[r6]
            boolean r12 = r12.intersect(r13)
            if (r12 == 0) goto L_0x007b
            r11 = 1
            goto L_0x007e
        L_0x007b:
            int r6 = r6 + 1
            goto L_0x0069
        L_0x007e:
            int r10 = r10 + 1
            goto L_0x0045
        L_0x0081:
            if (r11 != 0) goto L_0x0089
            short r6 = r0.mSuspectRelativeLayout
            int r6 = r6 + r2
            short r6 = (short) r6
            r0.mSuspectRelativeLayout = r6
        L_0x0089:
            r6 = 0
        L_0x008a:
            if (r6 >= r5) goto L_0x00ba
            android.graphics.Rect[] r10 = r0.mRectRelativeChild
            r10 = r10[r6]
            r11 = 0
            r10.set(r11, r11, r11, r11)
            int r6 = r6 + 1
            goto L_0x008a
        L_0x0097:
            boolean r5 = r1 instanceof android.widget.LinearLayout
            if (r5 == 0) goto L_0x00b8
            r5 = 0
        L_0x009c:
            if (r5 >= r7) goto L_0x00b8
            android.view.View r6 = r4.getChildAt(r5)
            android.view.ViewGroup$LayoutParams r6 = r6.getLayoutParams()
            android.widget.LinearLayout$LayoutParams r6 = (android.widget.LinearLayout.LayoutParams) r6
            float r6 = r6.weight
            r10 = 0
            int r6 = (r6 > r10 ? 1 : (r6 == r10 ? 0 : -1))
            if (r6 <= 0) goto L_0x00b5
            int r5 = r21 + 1
            short r5 = (short) r5
            r9 = r20
            goto L_0x00bc
        L_0x00b5:
            int r5 = r5 + 1
            goto L_0x009c
        L_0x00b8:
            r9 = r20
        L_0x00ba:
            r5 = r21
        L_0x00bc:
            if (r7 != 0) goto L_0x00e9
            if (r8 == 0) goto L_0x00e9
            boolean r6 = r0.mIsWaitDataFill
            if (r6 != 0) goto L_0x00e9
            com.taobao.onlinemonitor.OnLineMonitor r6 = r0.mOnLineMonitor
            com.taobao.onlinemonitor.SmoothCalculate r6 = r6.mSmoothCalculate
            boolean r6 = r6.isSmoothView(r1)
            if (r6 == 0) goto L_0x00e9
            r0.mHasSmoothView = r2
            int r6 = r0.mSmoothViewOutRevLayoutDepth
            if (r6 >= r9) goto L_0x00d6
            r0.mSmoothViewOutRevLayoutDepth = r9
        L_0x00d6:
            android.graphics.Rect r6 = r0.mRectCurrent
            r1.getGlobalVisibleRect(r6)
            android.graphics.Rect r6 = r0.mRectScreen
            android.graphics.Rect r10 = r0.mRectCurrent
            boolean r6 = r6.intersect(r10)
            if (r6 == 0) goto L_0x00e9
            r0.mIsWaitDataFill = r2
            r0.mViewWaitDataFill = r1
        L_0x00e9:
            short r1 = r0.mMaxLayoutDepth
            if (r1 >= r3) goto L_0x00ef
            r0.mMaxLayoutDepth = r3
        L_0x00ef:
            if (r8 == 0) goto L_0x0137
            r1 = 0
            r6 = 0
            r16 = 0
        L_0x00f5:
            if (r1 >= r7) goto L_0x0115
            android.view.View r10 = r4.getChildAt(r1)
            if (r8 == 0) goto L_0x0105
            int r11 = r10.getVisibility()
            if (r11 != 0) goto L_0x0105
            int r6 = r6 + 1
        L_0x0105:
            int r11 = r10.getVisibility()
            if (r11 == 0) goto L_0x010c
            goto L_0x0112
        L_0x010c:
            int r10 = r0.getViewGroupCount(r10, r3, r9, r5)
            int r16 = r16 + r10
        L_0x0112:
            int r1 = r1 + 1
            goto L_0x00f5
        L_0x0115:
            int r9 = r9 + r5
            if (r9 <= 0) goto L_0x0125
            short r1 = r0.mMeasureTimes
            int r3 = r6 * 2
            int r3 = r3 * r9
            short r3 = (short) r3
            int r3 = r3 + r2
            int r1 = r1 + r3
            short r1 = (short) r1
            r0.mMeasureTimes = r1
            goto L_0x012d
        L_0x0125:
            short r1 = r0.mMeasureTimes
            short r3 = (short) r6
            int r3 = r3 + r2
            int r1 = r1 + r3
            short r1 = (short) r1
            r0.mMeasureTimes = r1
        L_0x012d:
            int r1 = r16 + 1
            int r3 = r0.mActivityVisibleViewCount
            int r6 = r6 + r2
            int r3 = r3 + r6
            r0.mActivityVisibleViewCount = r3
            r6 = r1
            goto L_0x0138
        L_0x0137:
            r6 = 1
        L_0x0138:
            int r1 = r0.mActivityViewCount
            int r7 = r7 + r2
            int r1 = r1 + r7
            r0.mActivityViewCount = r1
            r11 = r6
            goto L_0x027f
        L_0x0141:
            boolean r3 = r0.mFrameIsTotalLoad
            if (r3 != 0) goto L_0x027e
            if (r1 == 0) goto L_0x027e
            int r3 = r18.getVisibility()
            if (r3 != 0) goto L_0x027e
            int r3 = r18.getWidth()
            int r4 = r18.getHeight()
            if (r3 <= 0) goto L_0x027e
            if (r4 <= 0) goto L_0x027e
            boolean r6 = r1 instanceof android.widget.EditText
            if (r6 == 0) goto L_0x0174
            r0.mHasEditTextView = r2
            boolean r5 = r18.isFocused()
            if (r5 == 0) goto L_0x01e3
            r0.mEditTextViewFocused = r2
            boolean r5 = com.taobao.onlinemonitor.OnLineMonitor.sIsDetailDebug
            if (r5 == 0) goto L_0x01e3
            java.lang.String r5 = "OnLineMonitor"
            java.lang.String r6 = "编辑控件已获得焦点"
            android.util.Log.e(r5, r6)
            goto L_0x01e3
        L_0x0174:
            boolean r6 = r1 instanceof android.widget.CompoundButton
            if (r6 == 0) goto L_0x017a
            goto L_0x01e3
        L_0x017a:
            boolean r6 = r1 instanceof android.widget.TextView
            if (r6 == 0) goto L_0x01a6
            r5 = r1
            android.widget.TextView r5 = (android.widget.TextView) r5
            android.graphics.drawable.Drawable r6 = r5.getBackground()
            boolean r6 = r0.isViewIgnore(r6)
            if (r6 == 0) goto L_0x01a2
            java.lang.CharSequence r6 = r5.getText()
            boolean r6 = android.text.TextUtils.isEmpty(r6)
            if (r6 == 0) goto L_0x01a2
            android.graphics.drawable.Drawable[] r5 = r5.getCompoundDrawables()
            if (r5 != 0) goto L_0x01a2
            int r5 = r0.mEmptyTextView
            int r5 = r5 + r2
            r0.mEmptyTextView = r5
            r5 = 1
            goto L_0x01a3
        L_0x01a2:
            r5 = 0
        L_0x01a3:
            r6 = r5
            goto L_0x0215
        L_0x01a6:
            boolean r6 = r1 instanceof android.widget.ImageView
            r7 = 10
            if (r6 == 0) goto L_0x01d7
            r6 = r1
            android.widget.ImageView r6 = (android.widget.ImageView) r6
            android.graphics.drawable.Drawable r8 = r6.getBackground()
            boolean r8 = r0.isViewIgnore(r8)
            if (r8 == 0) goto L_0x01c5
            android.graphics.drawable.Drawable r6 = r6.getDrawable()
            boolean r6 = r0.isViewIgnore(r6)
            if (r6 == 0) goto L_0x01c5
            r6 = 1
            goto L_0x01c6
        L_0x01c5:
            r6 = 0
        L_0x01c6:
            if (r6 != 0) goto L_0x0215
            if (r3 >= r7) goto L_0x01cf
            int r8 = r0.mScreenHeight
            int r8 = r8 / r5
            if (r4 >= r8) goto L_0x01e1
        L_0x01cf:
            if (r4 >= r7) goto L_0x0215
            int r7 = r0.mScreenWidth
            int r7 = r7 / r5
            if (r3 < r7) goto L_0x0215
            goto L_0x01e1
        L_0x01d7:
            boolean r6 = r1 instanceof android.widget.AbsSeekBar
            if (r6 == 0) goto L_0x01e5
            android.graphics.drawable.Drawable r5 = r18.getBackground()
            if (r5 == 0) goto L_0x01e3
        L_0x01e1:
            r6 = 1
            goto L_0x0215
        L_0x01e3:
            r6 = 0
            goto L_0x0215
        L_0x01e5:
            java.lang.CharSequence r6 = r18.getContentDescription()
            if (r6 == 0) goto L_0x01fc
            java.lang.Class r6 = r18.getClass()
            java.lang.String r6 = r6.getName()
            java.lang.String r8 = "android.view.View"
            boolean r6 = r6.equals(r8)
            if (r6 == 0) goto L_0x01fc
            goto L_0x01e3
        L_0x01fc:
            android.graphics.drawable.Drawable r6 = r18.getBackground()
            boolean r6 = r0.isViewIgnore(r6)
            if (r6 != 0) goto L_0x0215
            if (r3 >= r7) goto L_0x020d
            int r8 = r0.mScreenHeight
            int r8 = r8 / r5
            if (r4 >= r8) goto L_0x01e1
        L_0x020d:
            if (r4 >= r7) goto L_0x0215
            int r7 = r0.mScreenWidth
            int r7 = r7 / r5
            if (r3 < r7) goto L_0x0215
            goto L_0x01e1
        L_0x0215:
            if (r6 != 0) goto L_0x027e
            int r5 = r0.mScreenWidth
            int r6 = r0.mScreenHeight
            boolean r7 = r0.mIsPortrait
            if (r7 != 0) goto L_0x0223
            int r6 = r0.mScreenWidth
            int r5 = r0.mScreenHeight
        L_0x0223:
            int[] r7 = r0.mLocationPos
            r1.getLocationOnScreen(r7)
            r11 = 0
            r1 = r7[r11]
            if (r1 > r5) goto L_0x027f
            r1 = r7[r11]
            int r1 = r1 + r3
            if (r1 >= 0) goto L_0x0233
            goto L_0x027f
        L_0x0233:
            r1 = r7[r11]
            if (r1 < 0) goto L_0x023a
            r1 = r7[r11]
            goto L_0x023b
        L_0x023a:
            r1 = 0
        L_0x023b:
            r8 = r7[r11]
            int r3 = r3 + r8
            int r5 = r5 - r2
            if (r3 <= r5) goto L_0x0242
            r3 = r5
        L_0x0242:
            if (r3 <= r1) goto L_0x0253
            byte[] r5 = r0.mWidthLocation     // Catch:{ Exception -> 0x0252 }
            int r5 = r5.length     // Catch:{ Exception -> 0x0252 }
            if (r3 <= r5) goto L_0x024c
            byte[] r3 = r0.mWidthLocation     // Catch:{ Exception -> 0x0252 }
            int r3 = r3.length     // Catch:{ Exception -> 0x0252 }
        L_0x024c:
            byte[] r5 = r0.mWidthLocation     // Catch:{ Exception -> 0x0252 }
            java.util.Arrays.fill(r5, r1, r3, r2)     // Catch:{ Exception -> 0x0252 }
            goto L_0x0253
        L_0x0252:
        L_0x0253:
            r1 = r7[r2]
            if (r1 <= r6) goto L_0x025d
            r1 = r7[r2]
            int r1 = r1 + r4
            if (r1 >= 0) goto L_0x025d
            goto L_0x027f
        L_0x025d:
            r1 = r7[r2]
            if (r1 < 0) goto L_0x0264
            r1 = r7[r2]
            goto L_0x0265
        L_0x0264:
            r1 = 0
        L_0x0265:
            r3 = r7[r2]
            int r4 = r4 + r3
            int r3 = r6 + -1
            if (r4 <= r3) goto L_0x026d
            goto L_0x026e
        L_0x026d:
            r3 = r4
        L_0x026e:
            if (r3 <= r1) goto L_0x027f
            byte[] r4 = r0.mHeightLocation     // Catch:{ Exception -> 0x027f }
            int r4 = r4.length     // Catch:{ Exception -> 0x027f }
            if (r3 <= r4) goto L_0x0278
            byte[] r3 = r0.mHeightLocation     // Catch:{ Exception -> 0x027f }
            int r3 = r3.length     // Catch:{ Exception -> 0x027f }
        L_0x0278:
            byte[] r4 = r0.mHeightLocation     // Catch:{ Exception -> 0x027f }
            java.util.Arrays.fill(r4, r1, r3, r2)     // Catch:{ Exception -> 0x027f }
            goto L_0x027f
        L_0x027e:
            r11 = 0
        L_0x027f:
            return r11
        */
        throw new UnsupportedOperationException("Method not decompiled: com.taobao.onlinemonitor.LoadTimeCalculate.getViewGroupCount(android.view.View, short, short, short):int");
    }

    /* access modifiers changed from: package-private */
    @SuppressLint({"NewApi"})
    public void removeFromChoreographer() {
        OnLineMonitor onLineMonitor = this.mOnLineMonitor;
        if (OnLineMonitor.sIsTraceDetail) {
            OnLineMonitor onLineMonitor2 = this.mOnLineMonitor;
            if (OnLineMonitor.sApiLevel >= 16) {
                try {
                    if (this.mFrameCallback != null) {
                        Choreographer.getInstance().removeFrameCallback(this.mFrameCallback);
                    }
                    if (this.mTimeFrameCallback != null) {
                        Choreographer.getInstance().removeFrameCallback(this.mTimeFrameCallback);
                    }
                } catch (Exception unused) {
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setActivityInfo(OnLineMonitor.ActivityRuntimeInfo activityRuntimeInfo) {
        if (activityRuntimeInfo != null) {
            activityRuntimeInfo.totalLayoutUseTime = this.mTotalLayoutUseTime;
            activityRuntimeInfo.layoutTimesOnLoad = this.mLayoutTimesOnLoad;
            activityRuntimeInfo.maxLayoutUseTime = this.mMaxLayoutUseTime;
            activityRuntimeInfo.measureTimes = this.mMeasureTimes;
            activityRuntimeInfo.suspectRelativeLayout = this.mSuspectRelativeLayout;
            activityRuntimeInfo.maxLayoutDepth = this.mMaxLayoutDepth;
            activityRuntimeInfo.redundantLayout = this.mRedundantLayout;
            activityRuntimeInfo.loadTime = (int) this.mLoadTime;
            activityRuntimeInfo.firstRelativeLayoutDepth = this.mFirstRelativeLayoutDepth;
            activityRuntimeInfo.maxRelativeLayoutDepth = this.mMaxRelativeLayoutDepth;
            activityRuntimeInfo.activityViewCount = this.mActivityViewCount;
            activityRuntimeInfo.activityVisibleViewCount = this.mActivityVisibleViewCount;
            activityRuntimeInfo.smoothViewOutRevLayoutDepth = this.mSmoothViewOutRevLayoutDepth;
            if (OnLineMonitor.sIsTraceDetail && this.mIsWaitDataFill && this.mViewWaitDataFill != null) {
                activityRuntimeInfo.loadRelason = OnLineMonitor.getSimpleName(this.mViewWaitDataFill.getClass().getName()) + " 未有数据填充!";
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void stopOnClick() {
        if (!this.mLoadTimeGetted && !this.mHasSmoothView && !this.mIsWaitDataFill && this.mLastHeightPercent > 10) {
            if (OnLineMonitor.sIsDetailDebug) {
                Log.e("OnLineMonitor", "点击停止 LastHeightPercent=" + this.mLastHeightPercent);
            }
            stopLoadTimeCalculate();
        }
    }

    /* access modifiers changed from: package-private */
    @SuppressLint({"NewApi"})
    public void stopLoadTimeCalculate() {
        long j;
        if (!this.mLoadTimeGetted && this.mOnLineMonitor != null) {
            this.mLoadTimeGetted = true;
            OnLineMonitor.ActivityRuntimeInfo activityRuntimeInfo = this.mOnLineMonitor.mActivityRuntimeInfo;
            if (activityRuntimeInfo != null) {
                if (!activityRuntimeInfo.isColdOpen || this.mStartActivityTime <= 0 || this.mLoadStartTime <= this.mStartActivityTime || this.mLoadStartTime - this.mStartActivityTime >= 2000) {
                    this.mLoadTime = this.mLastLayoutTime - this.mLoadStartTime;
                    j = 0;
                } else {
                    this.mLoadTime = this.mLastLayoutTime - this.mStartActivityTime;
                    j = this.mLoadStartTime - this.mStartActivityTime;
                    this.mStartActivityTime = 0;
                }
                if (this.mLoadTime < 0) {
                    this.mLoadTime = 0;
                }
                OnLineMonitor onLineMonitor = this.mOnLineMonitor;
                if (!(OnLineMonitor.sApiLevel < 16 || this.mTimeFrameCallback == null || this.mChoreographer == null)) {
                    try {
                        this.mChoreographer.removeFrameCallback(this.mTimeFrameCallback);
                    } catch (Exception unused) {
                    }
                }
                setActivityInfo(activityRuntimeInfo);
                activityRuntimeInfo.startActivityTime = j;
                if (this.mTotalSmCount > 0 && this.mOnLineMonitor.mIsActivityColdOpen && this.mTotalSmCount < 600000) {
                    activityRuntimeInfo.activityLoadSmUsedTime = (int) ((this.mFrameEndTime - this.mFrameStartTime) / 1000000);
                    activityRuntimeInfo.activityLoadBadSmCount = this.mBadSmCount;
                    activityRuntimeInfo.activityLoadSmCount = this.mTotalSmCount;
                    activityRuntimeInfo.activityLoadBadSmUsedTime = this.mTotalBadSmTime;
                }
                if (this.mLoadTimeGetted && this.mOnLineMonitor.mIsBootEndActivity) {
                    this.mOnLineMonitor.onHomePageLoadEnd((int) this.mLoadTime, (int) j);
                }
                if (this.mOnLineMonitor.mThreadHandler != null) {
                    this.mOnLineMonitor.mThreadHandler.removeMessages(16);
                }
                this.mOnLineMonitor.notifyActivityLoadFinish();
                if (OnLineMonitor.sIsNormalDebug || OnLineMonitorApp.sIsDebug) {
                    Log.e("OnLineMonitor", OnLineMonitor.getSimpleName(this.mOnLineMonitor.mActivityName) + " LoadTime=" + this.mLoadTime + ",startActivityTime=" + j + ",LayoutTimesOnLoad=" + this.mLayoutTimesOnLoad + ",TotalLayoutTime=" + this.mTotalLayoutUseTime);
                }
                if (OnLineMonitor.sIsDetailDebug) {
                    Log.e("OnLineMonitor", "mMaxLayoutUseTime=" + this.mMaxLayoutUseTime + ",mTotalLayoutUseTime=" + this.mTotalLayoutUseTime + ",mLayoutTimesOnLoad=" + this.mLayoutTimesOnLoad);
                }
                if (this.mOnLineMonitor.mSmoothDetailDataNotify != null) {
                    this.mOnLineMonitor.mSmoothCalculate.notifySmoothDetailData(0, this.mFrameTimeArrayStartTime, (System.nanoTime() / 1000000) - this.mFrameTimeArrayStartTime, this.mFrameTimeIndex, this.mFrameTimeByteArray);
                }
            }
        }
    }

    public static boolean isOriatationPortrait(Context context) {
        return context.getResources().getConfiguration().orientation == 1;
    }

    /* access modifiers changed from: package-private */
    public ViewTreeObserver.OnGlobalLayoutListener createOnGlobalLayoutListener(int i) {
        return new LoadTimeOnGlobalLayoutListener(i);
    }

    class LoadTimeOnGlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        int mIndex;

        public LoadTimeOnGlobalLayoutListener(int i) {
            this.mIndex = i;
        }

        public void onGlobalLayout() {
            if ((LoadTimeCalculate.this.mOnLineMonitor.mActivityLifecycleCallback == null || this.mIndex == LoadTimeCalculate.this.mOnLineMonitor.mActivityLifecycleCallback.mCreateIndex) && !LoadTimeCalculate.this.mLoadTimeGetted && LoadTimeCalculate.this.mDecorView != null && LoadTimeCalculate.this.mDecorView.getViewTreeObserver().isAlive()) {
                LoadTimeCalculate.this.mIsPortrait = LoadTimeCalculate.isOriatationPortrait(LoadTimeCalculate.this.mDecorView.getContext());
                if (LoadTimeCalculate.this.mIsPortrait) {
                    LoadTimeCalculate.this.mHeightLocation = LoadTimeCalculate.this.mLargeLocation;
                    LoadTimeCalculate.this.mWidthLocation = LoadTimeCalculate.this.mSmallLocation;
                } else {
                    LoadTimeCalculate.this.mHeightLocation = LoadTimeCalculate.this.mSmallLocation;
                    LoadTimeCalculate.this.mWidthLocation = LoadTimeCalculate.this.mLargeLocation;
                }
                LoadTimeCalculate.this.mIsLayouted = true;
                LoadTimeCalculate loadTimeCalculate = LoadTimeCalculate.this;
                loadTimeCalculate.mTotalLayOutTimes = (short) (loadTimeCalculate.mTotalLayOutTimes + 1);
                if (LoadTimeCalculate.this.mOnLineMonitor.mThreadHandler != null) {
                    LoadTimeCalculate.this.mOnLineMonitor.mThreadHandler.removeMessages(16);
                }
                LoadTimeCalculate.this.mMaxLayoutDepth = 1;
                LoadTimeCalculate.this.mRedundantLayout = 0;
                LoadTimeCalculate.this.mMaxRelativeLayoutDepth = 0;
                LoadTimeCalculate.this.mSuspectRelativeLayout = 0;
                LoadTimeCalculate.this.mVisibleArea = 0;
                LoadTimeCalculate.this.mIsWaitDataFill = false;
                LoadTimeCalculate.this.mRectResult.set(0, 0, 0, 0);
                LoadTimeCalculate.this.mFirstRelativeLayoutDepth = 0;
                LoadTimeCalculate.this.mMeasureTimes = 0;
                LoadTimeCalculate.this.mActivityViewCount = 0;
                LoadTimeCalculate.this.mActivityVisibleViewCount = 0;
                LoadTimeCalculate.this.mHasEditTextView = false;
                LoadTimeCalculate.this.mViewWaitDataFill = null;
                if (!LoadTimeCalculate.this.mLoadTimeGetted) {
                    LoadTimeCalculate loadTimeCalculate2 = LoadTimeCalculate.this;
                    loadTimeCalculate2.mLayoutTimesOnLoad = (short) (loadTimeCalculate2.mLayoutTimesOnLoad + 1);
                }
                LoadTimeCalculate.this.mSmoothViewOutRevLayoutDepth = 0;
                LoadTimeCalculate.this.mHasSmoothView = false;
                LoadTimeCalculate.this.mEditTextViewFocused = false;
                if (LoadTimeCalculate.this.mOnLineMonitor.mActivityRuntimeInfo != null) {
                    LoadTimeCalculate.this.mOnLineMonitor.mActivityRuntimeInfo.overDraw3xCount = 0;
                    LoadTimeCalculate.this.mOnLineMonitor.mActivityRuntimeInfo.overDraw4xCount = 0;
                }
                LoadTimeCalculate.this.getLastFrameTime();
                LoadTimeCalculate.this.postFrameCallback();
                OnLineMonitor onLineMonitor = LoadTimeCalculate.this.mOnLineMonitor;
                if (OnLineMonitor.sApiLevel < 16) {
                    LoadTimeCalculate.this.doOnEndOfLayout(false);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void doOnEndOfLayout(boolean z) {
        int i;
        if (this.mDecorView != null && this.mWidthLocation.length != 0 && this.mHeightLocation.length != 0) {
            if ((this.mWidthLocation == null || this.mHeightLocation == null) && this.mScreenWidth > 0 && this.mScreenHeight > 0) {
                this.mWidthLocation = new byte[this.mScreenWidth];
                this.mHeightLocation = new byte[this.mScreenHeight];
            }
            if (this.mWidthLocation != null && this.mHeightLocation != null) {
                int i2 = this.mActivityViewCount;
                int i3 = this.mActivityVisibleViewCount;
                this.mOnLineMonitor.notifyOnCheckViewTree(0);
                long nanoTime = System.nanoTime() / 1000000;
                try {
                    this.mViewGroupCount = getViewGroupCount(this.mDecorView, 0, 0, 0);
                } catch (Throwable unused) {
                }
                long nanoTime2 = System.nanoTime() / 1000000;
                this.mOnLineMonitor.notifyOnCheckViewTree(1);
                if (this.mLastLayoutTime <= 0) {
                    this.mLastLayoutTime = nanoTime2;
                }
                if (this.mFrameIsLoad) {
                    this.mHasfScreenLoadTimes++;
                }
                if (this.mFrameIsTotalLoad) {
                    this.mAllScreenLoadTimes++;
                }
                if (!z) {
                    if (i2 == this.mActivityViewCount && i3 == this.mActivityVisibleViewCount) {
                        this.mViewEqualTimes++;
                    } else {
                        this.mViewEqualTimes = 0;
                    }
                }
                int i4 = 0;
                for (byte b : this.mWidthLocation) {
                    if (b > 0) {
                        i4++;
                    }
                }
                int i5 = 0;
                for (byte b2 : this.mHeightLocation) {
                    if (b2 > 0) {
                        i5++;
                    }
                }
                int i6 = i5 + this.mOnLineMonitor.mStatusBarHeight;
                int length = (i4 * 100) / this.mWidthLocation.length;
                int length2 = (i6 * 100) / this.mHeightLocation.length;
                if (OnLineMonitor.sIsDetailDebug) {
                    Log.e("OnLineMonitor", "ViewGroupCount=" + this.mViewGroupCount + ",LastViewGroupCount=" + this.mLastViewGroupCount + ",time=" + (nanoTime2 - this.mLastLayoutTime) + ", widthPercent=" + length + ",heightPercent=" + length2 + ",检测usetime=" + (nanoTime2 - nanoTime));
                }
                if (length <= 60 || length2 < 80) {
                    int length3 = this.mHeightLocation.length / 20;
                    int length4 = this.mWidthLocation.length / 20;
                    if (length < 50) {
                        i = 0;
                        for (int i7 = 0; i7 < this.mWidthLocation.length; i7 += length4) {
                            int i8 = 0;
                            int i9 = 0;
                            while (true) {
                                if (i8 >= length4) {
                                    break;
                                }
                                int i10 = i7 + i8;
                                if (i10 < this.mWidthLocation.length && this.mWidthLocation[i10] > 0) {
                                    int i11 = i9 + 1;
                                    if (i11 == 10) {
                                        i += length4;
                                        break;
                                    }
                                    i9 = i11;
                                }
                                i8++;
                            }
                        }
                    } else {
                        i = 0;
                    }
                    for (int i12 = 0; i12 < this.mOnLineMonitor.mStatusBarHeight; i12++) {
                        this.mHeightLocation[i12] = 1;
                    }
                    int i13 = 0;
                    for (int i14 = 0; i14 < this.mHeightLocation.length; i14 += length3) {
                        int i15 = 0;
                        int i16 = 0;
                        while (true) {
                            if (i15 < length3) {
                                int i17 = i14 + i15;
                                if (i17 < this.mHeightLocation.length && this.mHeightLocation[i17] > 0 && (i16 = i16 + 1) == 10) {
                                    i13 += length3;
                                    break;
                                }
                                i15++;
                            } else {
                                break;
                            }
                        }
                    }
                    if (length < 50) {
                        length = (i * 100) / this.mWidthLocation.length;
                    }
                    int length5 = (i13 * 100) / this.mHeightLocation.length;
                    if (OnLineMonitor.sIsDetailDebug) {
                        Log.e("OnLineMonitor", "采样计算法：widthP=" + length + ", heightP=" + length5);
                    }
                    if (!z && this.mLastHeightPercent == length5) {
                        this.mLastHeightPercentEqualTimes++;
                    }
                    if (length > 60 && (length5 >= 90 || (length5 >= 80 && this.mLastHeightPercentEqualTimes >= 3 && this.mViewGroupCountEqualTimes > 0))) {
                        if (OnLineMonitor.sIsDetailDebug) {
                            Log.e("OnLineMonitor", "采样达到50%高度达到90%或者80%3次不变，认为结束");
                        }
                        this.mLastLayoutTime = nanoTime2;
                        stopLoadTimeCalculate();
                    } else if (!this.mHasEditTextView || !this.mEditTextViewFocused) {
                        if (!this.mFrameIsLoad && length >= 50 && length5 >= 50) {
                            this.mFrameIsLoad = true;
                        }
                        if (!this.mFrameIsTotalLoad && length >= 50 && length5 >= 70) {
                            this.mFrameIsTotalLoad = true;
                        }
                        this.mLastWidthPercent = length;
                        this.mLastHeightPercent = length5;
                        if (!z) {
                            checkLoadTimeOnLayout(nanoTime2);
                            if (this.mLastViewGroupCount == this.mViewGroupCount) {
                                this.mViewGroupCountEqualTimes++;
                            } else {
                                this.mViewGroupCountEqualTimes = 0;
                            }
                        }
                        int i18 = this.mLastViewGroupCount;
                        this.mLastViewGroupCount = this.mViewGroupCount;
                        if (!this.mFrameIsLoad || this.mHasfScreenLoadTimes < 2 || this.mViewGroupCount <= i18 || i18 <= 0 || ((this.mViewGroupCount - i18) * 100) / i18 < 90) {
                            if (!z && (i18 <= this.mViewGroupCount || nanoTime2 - this.mLastLayoutTime <= 1000)) {
                                this.mLastLayoutTime = nanoTime2;
                            }
                            if (!this.mLoadTimeGetted && this.mOnLineMonitor.mThreadHandler != null) {
                                this.mOnLineMonitor.mThreadHandler.sendEmptyMessageDelayed(16, 100);
                                return;
                            }
                            return;
                        }
                        if (OnLineMonitor.sIsDetailDebug) {
                            Log.e("OnLineMonitor", "再添加90%的View树，认为结束");
                        }
                        stopLoadTimeCalculate();
                    } else {
                        if (OnLineMonitor.sIsDetailDebug) {
                            Log.e("OnLineMonitor", "编辑控件已经聚焦，认为结束");
                        }
                        stopLoadTimeCalculate();
                    }
                } else {
                    if (OnLineMonitor.sIsDetailDebug) {
                        Log.e("OnLineMonitor", "宽度80%以上，高度达到80%，认为结束");
                    }
                    this.mLastLayoutTime = nanoTime2;
                    stopLoadTimeCalculate();
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void checkWhiteSreen() {
        if (OnLineMonitorApp.sWhiteScreenMaxWidthPercent > this.mLastWidthPercent && OnLineMonitorApp.sWhiteScreenMaxHeightPercent > this.mLastHeightPercent && this.mOnLineMonitor != null && this.mOnLineMonitor.mOnlineStatistics != null && !this.mOnLineMonitor.mIsInBootStep) {
            long nanoTime = (System.nanoTime() / 1000000) - this.mLoadStartTime;
            int size = this.mOnLineMonitor.mOnlineStatistics.size();
            for (int i = 0; i < size; i++) {
                OnlineStatistics onlineStatistics = this.mOnLineMonitor.mOnlineStatistics.get(i);
                if (onlineStatistics != null) {
                    onlineStatistics.onWhiteScreen(this.mOnLineMonitor.mOnLineStat, this.mOnLineMonitor.mActivityName, this.mLastWidthPercent, this.mLastHeightPercent, (int) nanoTime);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void needStopLoadTimeCalculate(boolean z) {
        if (!this.mLoadTimeGetted) {
            if ((this.mTotalLayOutTimes != 0 || !this.mOnLineMonitor.mIsActivityColdOpen) && this.mDecorView != null) {
                long nanoTime = System.nanoTime() / 1000000;
                long j = nanoTime - this.mLastLayoutTime;
                doOnEndOfLayout(true);
                if (!this.mLoadTimeGetted) {
                    checkLoadTimeOnLayout(nanoTime);
                    if (!this.mLoadTimeGetted) {
                        if (z) {
                            checkWhiteSreen();
                            if ((this.mTotalLayOutTimes >= 1 || (this.mTotalLayOutTimes == 0 && !this.mOnLineMonitor.mIsActivityColdOpen)) && !this.mIsWaitDataFill && this.mActivityVisibleViewCount > this.mViewGroupCount && this.mLastWidthPercent >= 10 && this.mLastHeightPercent >= 5) {
                                if (OnLineMonitor.sIsDetailDebug) {
                                    Log.e("OnLineMonitor", "可能静态界面，结束在pause函数");
                                }
                                stopLoadTimeCalculate();
                                return;
                            }
                            return;
                        }
                        if (this.mFrameIsTotalLoad || this.mFrameIsLoad) {
                            if (this.mFrameIsLoad && j >= 2000) {
                                if (OnLineMonitor.sIsDetailDebug) {
                                    Log.e("OnLineMonitor", "已经有一半区域，且2秒没有变化，结束");
                                }
                                stopLoadTimeCalculate();
                                return;
                            } else if (this.mFrameIsTotalLoad && j >= 1000) {
                                if (OnLineMonitor.sIsDetailDebug) {
                                    Log.e("OnLineMonitor", "已经有70%区域，且1秒没有变化，结束");
                                }
                                stopLoadTimeCalculate();
                                return;
                            }
                        } else if (this.mTotalLayOutTimes == 1 && this.mActivityVisibleViewCount > this.mViewGroupCount && !this.mIsWaitDataFill && j >= TBToast.Duration.MEDIUM && this.mLastWidthPercent >= 33 && this.mLastHeightPercent >= 10) {
                            if (OnLineMonitor.sIsDetailDebug) {
                                Log.e("OnLineMonitor", "3s只有一次布局的，且没有等待数据的控件!");
                            }
                            stopLoadTimeCalculate();
                            return;
                        } else if (this.mTotalLayOutTimes > 1 && !this.mIsWaitDataFill && j >= 5000 && this.mLastWidthPercent >= 33 && this.mLastHeightPercent >= 5) {
                            if (OnLineMonitor.sIsDetailDebug) {
                                Log.e("OnLineMonitor", "5s有一次以上布局的，且没有等待数据的控件!");
                            }
                            stopLoadTimeCalculate();
                            return;
                        }
                        if (this.mOnLineMonitor.mThreadHandler != null) {
                            this.mOnLineMonitor.mThreadHandler.removeMessages(16);
                            this.mOnLineMonitor.mThreadHandler.sendEmptyMessageDelayed(16, 100);
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void checkLoadTimeOnLayout(long j) {
        if (j - this.mLastLayoutTime <= UmbrellaConstants.PERFORMANCE_DATA_ALIVE || (this.mTotalLayOutTimes <= 0 && (this.mTotalLayOutTimes != 0 || this.mOnLineMonitor.mIsActivityColdOpen))) {
            int i = (this.mActivityViewCount / 100) + 1;
            if (i % 100 > 0) {
                i++;
            }
            if (this.mFrameIsTotalLoad && this.mAllScreenLoadTimes > i) {
                if (OnLineMonitor.sIsDetailDebug) {
                    Log.e("OnLineMonitor", "70%加载了，布局次数达到最高限制，认为结束,LayoutLoadTimes=" + this.mAllScreenLoadTimes);
                }
                stopLoadTimeCalculate();
                return;
            }
            return;
        }
        if (OnLineMonitor.sIsDetailDebug) {
            Log.e("OnLineMonitor", "超过20s的，认为已经结束！");
        }
        checkWhiteSreen();
        stopLoadTimeCalculate();
    }

    @SuppressLint({"NewApi"})
    public class MyFrameCallback implements Choreographer.FrameCallback {
        public MyFrameCallback() {
        }

        public void doFrame(long j) {
            if (LoadTimeCalculate.this.mIsLayouted) {
                LoadTimeCalculate.this.mIsLayouted = false;
                long nanoTime = (System.nanoTime() - LoadTimeCalculate.this.mLastFrameTimeNanos) / 1000000;
                if (LoadTimeCalculate.this.mMaxLayoutUseTime < nanoTime) {
                    LoadTimeCalculate.this.mMaxLayoutUseTime = nanoTime;
                }
                LoadTimeCalculate.this.mTotalLayoutUseTime += nanoTime;
                OnLineMonitor onLineMonitor = LoadTimeCalculate.this.mOnLineMonitor;
                if (OnLineMonitor.sApiLevel >= 16) {
                    LoadTimeCalculate.this.doOnEndOfLayout(false);
                }
            }
        }
    }

    @SuppressLint({"NewApi"})
    public class GetFrameTimeCallback implements Choreographer.FrameCallback {
        public GetFrameTimeCallback() {
        }

        public void doFrame(long j) {
            if (LoadTimeCalculate.this.mOnLineMonitor != null && LoadTimeCalculate.this.mOnLineMonitor.mSmoothCalculate != null) {
                long nanoTime = System.nanoTime();
                LoadTimeCalculate.this.mTotalSmCount++;
                if (LoadTimeCalculate.this.mFrameStartTime == 0) {
                    LoadTimeCalculate.this.mFrameStartTime = nanoTime;
                }
                if (LoadTimeCalculate.this.mFrameEndTime > 0) {
                    float f = ((float) (nanoTime - LoadTimeCalculate.this.mFrameEndTime)) / 1000000.0f;
                    if (LoadTimeCalculate.this.mFrameTimeByteArray != null && LoadTimeCalculate.this.mFrameTimeIndex < LoadTimeCalculate.this.mFrameTimeByteArray.length) {
                        LoadTimeCalculate.this.mFrameTimeByteArray[LoadTimeCalculate.this.mFrameTimeIndex] = (short) ((int) f);
                        LoadTimeCalculate loadTimeCalculate = LoadTimeCalculate.this;
                        loadTimeCalculate.mFrameTimeIndex = (short) (loadTimeCalculate.mFrameTimeIndex + 1);
                    }
                }
                Choreographer.getInstance().postFrameCallback(LoadTimeCalculate.this.mTimeFrameCallback);
                LoadTimeCalculate.this.mFrameEndTime = nanoTime;
            }
        }
    }
}
