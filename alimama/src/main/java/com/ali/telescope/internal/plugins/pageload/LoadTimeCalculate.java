package com.ali.telescope.internal.plugins.pageload;

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
import android.view.Choreographer;
import android.view.View;
import android.view.ViewTreeObserver;
import com.ali.telescope.data.DeviceInfoManager;
import com.ali.telescope.data.DeviceStatus;
import com.ali.telescope.internal.plugins.pageload.PageLoadMonitor;
import com.ali.telescope.util.TelescopeLog;
import com.alibaba.android.umbrella.utils.UmbrellaConstants;
import com.taobao.uikit.extend.component.unify.Toast.TBToast;
import java.lang.reflect.Field;
import java.util.Arrays;

public class LoadTimeCalculate {
    protected static final int CHECK_LAYOUT_FITER = 1000;
    protected static final int CHECK_LAYOUT_TIMER = 100;
    protected static final int LAYOUT_FITER = 20000;
    static final String TAG = "pageload@LoadTimeCalculate";
    int mActivityViewCount;
    int mActivityVisibleViewCount;
    int mAllScreenLoadTimes = 0;
    int mBadSmCount;
    boolean mCanCheckOverDraw;
    Choreographer mChoreographer;
    Class mClassV4DrawableWrapper;
    protected Context mContext = null;
    protected volatile View mDecorView;
    boolean mEditTextViewFocused;
    int mEmptyTextView = 0;
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
    int mHasfScreenLoadTimes = 0;
    byte[] mHeightLocation;
    boolean mIsLayouted = false;
    boolean mIsWaitDataFill = false;
    byte[] mLargeLocation;
    Field mLastFrameTimeField;
    long mLastFrameTimeNanos;
    int mLastHeightPercent;
    int mLastHeightPercentEqualTimes;
    long mLastLayoutTime = 0;
    int mLastViewGroupCount = 0;
    int mLastWidthPercent;
    short mLayoutTimesOnLoad;
    long mLoadStartTime;
    long mLoadTime = 0;
    boolean mLoadTimeGetted;
    int[] mLocationPos = new int[2];
    short mMaxLayoutDepth = 1;
    long mMaxLayoutUseTime;
    short mMaxRelativeLayoutDepth;
    short mMeasureTimes;
    ViewTreeObserver.OnGlobalLayoutListener mOnGlobalLayoutListener;
    protected PageLoadMonitor mPageLoadMonitor;
    PageLoadMonitor.PageStat mPageStat;
    PageLoadMonitor.PageStat mPageStatOld;
    Rect mRectCurrent = new Rect();
    Rect mRectResult = new Rect();
    Rect mRectScreen = new Rect();
    short mRedundantLayout;
    int mScreenArea;
    int mScreenAreaIn10;
    int mScreenHeight = -1;
    int mScreenWidth = -1;
    byte[] mSmallLocation;
    int mSmoothViewOutRevLayoutDepth;
    long mStartActivityTime = 0;
    short mSuspectRelativeLayout;
    Choreographer.FrameCallback mTimeFrameCallback;
    int mTotalBadSmTime;
    short mTotalLayOutTimes;
    long mTotalLayoutUseTime;
    int mTotalSmCount;
    int mViewEqualTimes = 0;
    int mViewGroupCount = 0;
    int mViewGroupCountEqualTimes = 0;
    volatile View mViewWaitDataFill;
    int mVisibleArea = 0;
    byte[] mWidthLocation;

    protected LoadTimeCalculate() {
        try {
            this.mClassV4DrawableWrapper = Class.forName("androidx.core.graphics.drawable.DrawableWrapper");
        } catch (Throwable unused) {
        }
    }

    /* access modifiers changed from: package-private */
    @SuppressLint({"NewApi"})
    public void initGetLastFrameTimeField() {
        if (DeviceInfoManager.instance().getApiLevel() >= 16 && this.mChoreographer == null) {
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
        if (DeviceInfoManager.instance().getApiLevel() >= 16 && this.mChoreographer != null && this.mLastFrameTimeField != null) {
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
        if (DeviceInfoManager.instance().getApiLevel() >= 16 && this.mChoreographer != null) {
            this.mChoreographer.postFrameCallback(this.mFrameCallback);
        }
    }

    /* access modifiers changed from: protected */
    @SuppressLint({"NewApi"})
    public void onActivityCreated(Activity activity) {
        if (this.mContext == null) {
            this.mContext = activity.getApplicationContext();
            initGetLastFrameTimeField();
            if (this.mScreenWidth <= 0) {
                this.mScreenWidth = DeviceInfoManager.instance().getScreenWidth();
                this.mScreenHeight = DeviceInfoManager.instance().getScreenHeight();
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
        this.mPageStat = this.mPageLoadMonitor.mPageStat;
        if (DeviceInfoManager.instance().getApiLevel() >= 16 && this.mFrameCallback == null) {
            this.mFrameCallback = new MyFrameCallback();
        }
        if (this.mFrameTimeByteArray != null) {
            for (int i = 0; i < this.mFrameTimeByteArray.length; i++) {
                this.mFrameTimeByteArray[i] = 0;
            }
            this.mFrameTimeIndex = 0;
            this.mFrameTimeArrayStartTime = System.nanoTime() / 1000000;
        }
        if (DeviceInfoManager.instance().getApiLevel() >= 16 && this.mChoreographer != null) {
            if (this.mTimeFrameCallback == null) {
                this.mTimeFrameCallback = new GetFrameTimeCallback();
            }
            this.mFrameStartTime = 0;
            this.mFrameEndTime = 0;
            this.mChoreographer.removeFrameCallback(this.mTimeFrameCallback);
            this.mChoreographer.postFrameCallback(this.mTimeFrameCallback);
        }
        this.mLoadStartTime = this.mPageLoadMonitor.mActivityLifecycleCallback.mActivityOnCreate;
        this.mCanCheckOverDraw = false;
        this.mViewWaitDataFill = null;
    }

    /* access modifiers changed from: protected */
    public void onActivityStarted(Activity activity, View view) {
        this.mDecorView = view;
        if (!this.mPageLoadMonitor.mPageStat.isColdOpen) {
            this.mLoadStartTime = System.nanoTime() / 1000000;
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityPaused(Activity activity) {
        this.mViewWaitDataFill = null;
        this.mDecorView = null;
        this.mPageStatOld = this.mPageStat;
        this.mPageStat = this.mPageLoadMonitor.mPageStat;
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
        if ((DeviceInfoManager.instance().getApiLevel() < 23 || !(drawable instanceof DrawableWrapper)) && !(drawable instanceof ColorDrawable) && !(drawable instanceof ShapeDrawable) && !(drawable instanceof DrawableContainer)) {
            return this.mClassV4DrawableWrapper != null && this.mClassV4DrawableWrapper.isAssignableFrom(drawable.getClass());
        }
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:76:0x0119, code lost:
        if (r11 >= (r8.mScreenHeight / 4)) goto L_0x012e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:79:0x0121, code lost:
        if (r10 >= (r8.mScreenWidth / 4)) goto L_0x012e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:83:0x012c, code lost:
        if (r9.getBackground() != null) goto L_0x012e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:94:0x0159, code lost:
        if (r11 >= (r8.mScreenHeight / 4)) goto L_0x012e;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:97:0x0161, code lost:
        if (r10 >= (r8.mScreenWidth / 4)) goto L_0x012e;
     */
    /* JADX WARNING: Removed duplicated region for block: B:141:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:99:0x0166  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int getViewGroupCount(android.view.View r9, short r10, short r11, short r12) {
        /*
            r8 = this;
            r0 = 1
            int r10 = r10 + r0
            short r10 = (short) r10
            boolean r1 = r9 instanceof android.view.ViewGroup
            r2 = 0
            if (r1 == 0) goto L_0x009d
            r1 = r9
            android.view.ViewGroup r1 = (android.view.ViewGroup) r1
            int r3 = r1.getChildCount()
            int r4 = r9.getVisibility()
            if (r4 != 0) goto L_0x0017
            r4 = 1
            goto L_0x0018
        L_0x0017:
            r4 = 0
        L_0x0018:
            if (r3 != r0) goto L_0x0020
            short r5 = r8.mRedundantLayout
            int r5 = r5 + r0
            short r5 = (short) r5
            r8.mRedundantLayout = r5
        L_0x0020:
            boolean r5 = r9 instanceof android.widget.RelativeLayout
            if (r5 == 0) goto L_0x0036
            if (r4 == 0) goto L_0x0055
            int r11 = r11 + 1
            short r11 = (short) r11
            short r9 = r8.mFirstRelativeLayoutDepth
            if (r9 != 0) goto L_0x002f
            r8.mFirstRelativeLayoutDepth = r10
        L_0x002f:
            short r9 = r8.mMaxRelativeLayoutDepth
            if (r9 >= r11) goto L_0x0055
            r8.mMaxRelativeLayoutDepth = r11
            goto L_0x0055
        L_0x0036:
            boolean r9 = r9 instanceof android.widget.LinearLayout
            if (r9 == 0) goto L_0x0055
            r9 = 0
        L_0x003b:
            if (r9 >= r3) goto L_0x0055
            android.view.View r5 = r1.getChildAt(r9)
            android.view.ViewGroup$LayoutParams r5 = r5.getLayoutParams()
            android.widget.LinearLayout$LayoutParams r5 = (android.widget.LinearLayout.LayoutParams) r5
            float r5 = r5.weight
            r6 = 0
            int r5 = (r5 > r6 ? 1 : (r5 == r6 ? 0 : -1))
            if (r5 <= 0) goto L_0x0052
            int r12 = r12 + 1
            short r12 = (short) r12
            goto L_0x0055
        L_0x0052:
            int r9 = r9 + 1
            goto L_0x003b
        L_0x0055:
            short r9 = r8.mMaxLayoutDepth
            if (r9 >= r10) goto L_0x005b
            r8.mMaxLayoutDepth = r10
        L_0x005b:
            r9 = 0
            r5 = 0
        L_0x005d:
            if (r2 >= r3) goto L_0x0075
            android.view.View r6 = r1.getChildAt(r2)
            if (r4 == 0) goto L_0x006d
            int r7 = r6.getVisibility()
            if (r7 != 0) goto L_0x006d
            int r9 = r9 + 1
        L_0x006d:
            int r6 = r8.getViewGroupCount(r6, r10, r11, r12)
            int r5 = r5 + r6
            int r2 = r2 + 1
            goto L_0x005d
        L_0x0075:
            int r11 = r11 + r12
            if (r11 <= 0) goto L_0x0085
            short r10 = r8.mMeasureTimes
            int r12 = r9 * 2
            int r12 = r12 * r11
            short r11 = (short) r12
            int r11 = r11 + r0
            int r10 = r10 + r11
            short r10 = (short) r10
            r8.mMeasureTimes = r10
            goto L_0x008d
        L_0x0085:
            short r10 = r8.mMeasureTimes
            short r11 = (short) r9
            int r11 = r11 + r0
            int r10 = r10 + r11
            short r10 = (short) r10
            r8.mMeasureTimes = r10
        L_0x008d:
            int r10 = r8.mActivityViewCount
            int r3 = r3 + r0
            int r10 = r10 + r3
            r8.mActivityViewCount = r10
            int r2 = r5 + 1
            int r10 = r8.mActivityVisibleViewCount
            int r9 = r9 + r0
            int r10 = r10 + r9
            r8.mActivityVisibleViewCount = r10
            goto L_0x01b2
        L_0x009d:
            boolean r10 = r8.mFrameIsTotalLoad
            if (r10 != 0) goto L_0x01b2
            if (r9 == 0) goto L_0x01b2
            int r10 = r9.getVisibility()
            if (r10 != 0) goto L_0x01b2
            int r10 = r9.getWidth()
            int r11 = r9.getHeight()
            if (r10 <= 0) goto L_0x01b2
            if (r11 <= 0) goto L_0x01b2
            boolean r12 = r9 instanceof android.widget.EditText
            if (r12 == 0) goto L_0x00c5
            r8.mHasEditTextView = r0
            boolean r12 = r9.isFocused()
            if (r12 == 0) goto L_0x0130
            r8.mEditTextViewFocused = r0
            goto L_0x0130
        L_0x00c5:
            boolean r12 = r9 instanceof android.widget.CompoundButton
            if (r12 == 0) goto L_0x00ca
            goto L_0x0130
        L_0x00ca:
            boolean r12 = r9 instanceof android.widget.TextView
            if (r12 == 0) goto L_0x00f1
            r12 = r9
            android.widget.TextView r12 = (android.widget.TextView) r12
            android.graphics.drawable.Drawable r1 = r12.getBackground()
            boolean r1 = r8.isViewIgnore(r1)
            if (r1 == 0) goto L_0x0130
            java.lang.CharSequence r1 = r12.getText()
            boolean r1 = android.text.TextUtils.isEmpty(r1)
            if (r1 == 0) goto L_0x0130
            android.graphics.drawable.Drawable[] r12 = r12.getCompoundDrawables()
            if (r12 != 0) goto L_0x0130
            int r12 = r8.mEmptyTextView
            int r12 = r12 + r0
            r8.mEmptyTextView = r12
            goto L_0x012e
        L_0x00f1:
            boolean r12 = r9 instanceof android.widget.ImageView
            r1 = 10
            if (r12 == 0) goto L_0x0124
            r12 = r9
            android.widget.ImageView r12 = (android.widget.ImageView) r12
            android.graphics.drawable.Drawable r3 = r12.getBackground()
            boolean r3 = r8.isViewIgnore(r3)
            if (r3 == 0) goto L_0x0110
            android.graphics.drawable.Drawable r12 = r12.getDrawable()
            boolean r12 = r8.isViewIgnore(r12)
            if (r12 == 0) goto L_0x0110
            r12 = 1
            goto L_0x0111
        L_0x0110:
            r12 = 0
        L_0x0111:
            if (r12 != 0) goto L_0x0164
            if (r10 >= r1) goto L_0x011b
            int r3 = r8.mScreenHeight
            int r3 = r3 / 4
            if (r11 >= r3) goto L_0x012e
        L_0x011b:
            if (r11 >= r1) goto L_0x0164
            int r1 = r8.mScreenWidth
            int r1 = r1 / 4
            if (r10 < r1) goto L_0x0164
            goto L_0x012e
        L_0x0124:
            boolean r12 = r9 instanceof android.widget.AbsSeekBar
            if (r12 == 0) goto L_0x0132
            android.graphics.drawable.Drawable r12 = r9.getBackground()
            if (r12 == 0) goto L_0x0130
        L_0x012e:
            r12 = 1
            goto L_0x0164
        L_0x0130:
            r12 = 0
            goto L_0x0164
        L_0x0132:
            java.lang.CharSequence r12 = r9.getContentDescription()
            if (r12 == 0) goto L_0x0149
            java.lang.Class r12 = r9.getClass()
            java.lang.String r12 = r12.getName()
            java.lang.String r3 = "android.view.View"
            boolean r12 = r12.equals(r3)
            if (r12 == 0) goto L_0x0149
            goto L_0x0130
        L_0x0149:
            android.graphics.drawable.Drawable r12 = r9.getBackground()
            boolean r12 = r8.isViewIgnore(r12)
            if (r12 != 0) goto L_0x0164
            if (r10 >= r1) goto L_0x015b
            int r3 = r8.mScreenHeight
            int r3 = r3 / 4
            if (r11 >= r3) goto L_0x012e
        L_0x015b:
            if (r11 >= r1) goto L_0x0164
            int r1 = r8.mScreenWidth
            int r1 = r1 / 4
            if (r10 < r1) goto L_0x0164
            goto L_0x012e
        L_0x0164:
            if (r12 != 0) goto L_0x01b2
            int[] r12 = r8.mLocationPos
            r9.getLocationOnScreen(r12)
            r9 = r12[r2]
            if (r9 < 0) goto L_0x0172
            r9 = r12[r2]
            goto L_0x0173
        L_0x0172:
            r9 = 0
        L_0x0173:
            r1 = r12[r2]
            int r10 = r10 + r1
            int r1 = r8.mScreenWidth
            int r1 = r1 - r0
            if (r10 <= r1) goto L_0x017e
            int r10 = r8.mScreenWidth
            int r10 = r10 - r0
        L_0x017e:
            if (r10 <= r9) goto L_0x018f
            byte[] r1 = r8.mWidthLocation     // Catch:{ Exception -> 0x018e }
            int r1 = r1.length     // Catch:{ Exception -> 0x018e }
            if (r10 <= r1) goto L_0x0188
            byte[] r10 = r8.mWidthLocation     // Catch:{ Exception -> 0x018e }
            int r10 = r10.length     // Catch:{ Exception -> 0x018e }
        L_0x0188:
            byte[] r1 = r8.mWidthLocation     // Catch:{ Exception -> 0x018e }
            java.util.Arrays.fill(r1, r9, r10, r0)     // Catch:{ Exception -> 0x018e }
            goto L_0x018f
        L_0x018e:
        L_0x018f:
            r9 = r12[r0]
            if (r9 < 0) goto L_0x0196
            r9 = r12[r0]
            goto L_0x0197
        L_0x0196:
            r9 = 0
        L_0x0197:
            r10 = r12[r0]
            int r11 = r11 + r10
            int r10 = r8.mScreenHeight
            int r10 = r10 - r0
            if (r11 <= r10) goto L_0x01a3
            int r10 = r8.mScreenHeight
            int r11 = r10 + -1
        L_0x01a3:
            if (r11 <= r9) goto L_0x01b2
            byte[] r10 = r8.mHeightLocation     // Catch:{ Exception -> 0x01b2 }
            int r10 = r10.length     // Catch:{ Exception -> 0x01b2 }
            if (r11 <= r10) goto L_0x01ad
            byte[] r10 = r8.mHeightLocation     // Catch:{ Exception -> 0x01b2 }
            int r11 = r10.length     // Catch:{ Exception -> 0x01b2 }
        L_0x01ad:
            byte[] r10 = r8.mHeightLocation     // Catch:{ Exception -> 0x01b2 }
            java.util.Arrays.fill(r10, r9, r11, r0)     // Catch:{ Exception -> 0x01b2 }
        L_0x01b2:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ali.telescope.internal.plugins.pageload.LoadTimeCalculate.getViewGroupCount(android.view.View, short, short, short):int");
    }

    /* access modifiers changed from: package-private */
    @SuppressLint({"NewApi"})
    public void removeFromChoreographer() {
        if (DeviceInfoManager.instance().getApiLevel() > 16) {
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

    /* access modifiers changed from: package-private */
    public void setActivityStat(PageLoadMonitor.PageStat pageStat) {
        if (pageStat != null) {
            pageStat.loadStartTime = this.mLoadStartTime;
            pageStat.totalLayoutUseTime = this.mTotalLayoutUseTime;
            pageStat.layoutTimesOnLoad = this.mLayoutTimesOnLoad;
            pageStat.maxLayoutUseTime = this.mMaxLayoutUseTime;
            pageStat.measureTimes = this.mMeasureTimes;
            pageStat.suspectRelativeLayout = this.mSuspectRelativeLayout;
            pageStat.maxLayoutDepth = this.mMaxLayoutDepth;
            pageStat.redundantLayout = this.mRedundantLayout;
            pageStat.loadTime = (int) this.mLoadTime;
            pageStat.firstRelativeLayoutDepth = this.mFirstRelativeLayoutDepth;
            pageStat.maxRelativeLayoutDepth = this.mMaxRelativeLayoutDepth;
            pageStat.activityViewCount = this.mActivityViewCount;
            pageStat.activityVisibleViewCount = this.mActivityVisibleViewCount;
            if (this.mPageStat != null) {
                this.mPageLoadMonitor.mPageLoadPlugin.mITelescopeContext.getBeanReport().send(new PageLoadFinishBean(System.currentTimeMillis(), this.mPageStat.pageName, this.mPageStat.pageHashCode, (long) this.mPageStat.loadTime));
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void stopOnClick() {
        if (!this.mLoadTimeGetted && !this.mHasSmoothView && !this.mIsWaitDataFill && this.mLastHeightPercent > 10) {
            stopLoadTimeCalculate();
        }
    }

    /* access modifiers changed from: package-private */
    @SuppressLint({"NewApi"})
    public void stopLoadTimeCalculate() {
        if (!this.mLoadTimeGetted && this.mPageLoadMonitor != null) {
            this.mLoadTimeGetted = true;
            PageLoadMonitor.PageStat pageStat = this.mPageLoadMonitor.mPageStat;
            if (pageStat != null) {
                if (!pageStat.isColdOpen || this.mStartActivityTime <= 0 || this.mLoadStartTime <= this.mStartActivityTime || this.mLoadStartTime - this.mStartActivityTime >= 2000) {
                    this.mLoadTime = this.mLastLayoutTime - this.mLoadStartTime;
                } else {
                    this.mLoadTime = this.mLastLayoutTime - this.mStartActivityTime;
                    long j = this.mLoadStartTime;
                    long j2 = this.mStartActivityTime;
                    this.mStartActivityTime = 0;
                }
                if (this.mLoadTime < 0) {
                    this.mLoadTime = 0;
                }
                if (!(DeviceInfoManager.instance().getApiLevel() < 16 || this.mTimeFrameCallback == null || this.mChoreographer == null)) {
                    try {
                        this.mChoreographer.removeFrameCallback(this.mTimeFrameCallback);
                    } catch (Exception unused) {
                    }
                }
                setActivityStat(pageStat);
                if (this.mPageLoadMonitor.mThreadHandler != null) {
                    this.mPageLoadMonitor.mThreadHandler.removeMessages(16);
                }
                if (this.mPageStat != null) {
                    TelescopeLog.w(TAG, "time cost", "pageName=" + this.mPageStat.pageName, "pageLoadTime=" + this.mPageStat.loadTime, "isColdOpen=" + this.mPageStat.isColdOpen, "pageStartTime=" + this.mPageStat.loadStartTime);
                } else {
                    TelescopeLog.e(TAG, "mPageStat is null !!!");
                }
                this.mPageLoadMonitor.mIsInBootStep = false;
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
            if ((LoadTimeCalculate.this.mPageLoadMonitor.mActivityLifecycleCallback == null || this.mIndex == LoadTimeCalculate.this.mPageLoadMonitor.mActivityLifecycleCallback.mCreateIndex) && !LoadTimeCalculate.this.mLoadTimeGetted && LoadTimeCalculate.this.mDecorView != null && LoadTimeCalculate.this.mDecorView.getViewTreeObserver().isAlive()) {
                if (LoadTimeCalculate.isOriatationPortrait(LoadTimeCalculate.this.mDecorView.getContext())) {
                    LoadTimeCalculate.this.mHeightLocation = LoadTimeCalculate.this.mLargeLocation;
                    LoadTimeCalculate.this.mWidthLocation = LoadTimeCalculate.this.mSmallLocation;
                } else {
                    LoadTimeCalculate.this.mHeightLocation = LoadTimeCalculate.this.mSmallLocation;
                    LoadTimeCalculate.this.mWidthLocation = LoadTimeCalculate.this.mLargeLocation;
                }
                LoadTimeCalculate.this.mIsLayouted = true;
                LoadTimeCalculate loadTimeCalculate = LoadTimeCalculate.this;
                loadTimeCalculate.mTotalLayOutTimes = (short) (loadTimeCalculate.mTotalLayOutTimes + 1);
                if (LoadTimeCalculate.this.mPageLoadMonitor.mThreadHandler != null) {
                    LoadTimeCalculate.this.mPageLoadMonitor.mThreadHandler.removeMessages(16);
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
                LoadTimeCalculate.this.getLastFrameTime();
                LoadTimeCalculate.this.postFrameCallback();
                if (DeviceInfoManager.instance().getApiLevel() < 16) {
                    LoadTimeCalculate.this.doOnEndOfLayout(false);
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void doOnEndOfLayout(boolean z) {
        int i;
        if (this.mDecorView != null) {
            if ((this.mWidthLocation == null || this.mHeightLocation == null) && this.mScreenWidth > 0 && this.mScreenHeight > 0) {
                this.mWidthLocation = new byte[this.mScreenWidth];
                this.mHeightLocation = new byte[this.mScreenHeight];
            }
            if (this.mWidthLocation != null && this.mHeightLocation != null) {
                int i2 = this.mActivityViewCount;
                int i3 = this.mActivityVisibleViewCount;
                try {
                    this.mViewGroupCount = getViewGroupCount(this.mDecorView, 0, 0, 0);
                } catch (Throwable unused) {
                }
                long nanoTime = System.nanoTime() / 1000000;
                if (this.mLastLayoutTime <= 0) {
                    this.mLastLayoutTime = nanoTime;
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
                int statusBarHeight = DeviceStatus.getStatusBarHeight(this.mContext);
                int length = (i4 * 100) / this.mWidthLocation.length;
                int length2 = ((i5 + statusBarHeight) * 100) / this.mHeightLocation.length;
                if (length <= 60 || length2 < 80) {
                    int length3 = this.mHeightLocation.length / 20;
                    int length4 = this.mWidthLocation.length / 20;
                    if (length < 50) {
                        i = 0;
                        for (int i6 = 0; i6 < this.mWidthLocation.length; i6 += length4) {
                            int i7 = 0;
                            int i8 = 0;
                            while (true) {
                                if (i7 >= length4) {
                                    break;
                                }
                                int i9 = i6 + i7;
                                if (i9 < this.mWidthLocation.length && this.mWidthLocation[i9] > 0) {
                                    int i10 = i8 + 1;
                                    if (i10 == 10) {
                                        i += length4;
                                        break;
                                    }
                                    i8 = i10;
                                }
                                i7++;
                            }
                        }
                    } else {
                        i = 0;
                    }
                    for (int i11 = 0; i11 < statusBarHeight; i11++) {
                        this.mHeightLocation[i11] = 1;
                    }
                    int i12 = 0;
                    for (int i13 = 0; i13 < this.mHeightLocation.length; i13 += length3) {
                        int i14 = 0;
                        int i15 = 0;
                        while (true) {
                            if (i14 < length3) {
                                int i16 = i13 + i14;
                                if (i16 < this.mHeightLocation.length && this.mHeightLocation[i16] > 0 && (i15 = i15 + 1) == 10) {
                                    i12 += length3;
                                    break;
                                }
                                i14++;
                            } else {
                                break;
                            }
                        }
                    }
                    if (length < 50) {
                        length = (i * 100) / this.mWidthLocation.length;
                    }
                    int length5 = (i12 * 100) / this.mHeightLocation.length;
                    if (!z && this.mLastHeightPercent == length5) {
                        this.mLastHeightPercentEqualTimes++;
                    }
                    if (length > 60 && (length5 >= 90 || (length5 >= 80 && this.mLastHeightPercentEqualTimes >= 3 && this.mViewGroupCountEqualTimes > 0))) {
                        this.mLastLayoutTime = nanoTime;
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
                            checkLoadTimeOnLayout(nanoTime);
                            if (this.mLastViewGroupCount == this.mViewGroupCount) {
                                this.mViewGroupCountEqualTimes++;
                            } else {
                                this.mViewGroupCountEqualTimes = 0;
                            }
                        }
                        int i17 = this.mLastViewGroupCount;
                        this.mLastViewGroupCount = this.mViewGroupCount;
                        if (!this.mFrameIsLoad || this.mHasfScreenLoadTimes < 2 || this.mViewGroupCount <= i17 || i17 <= 0 || ((this.mViewGroupCount - i17) * 100) / i17 < 90) {
                            if (!z && (i17 <= this.mViewGroupCount || nanoTime - this.mLastLayoutTime <= 1000)) {
                                this.mLastLayoutTime = nanoTime;
                            }
                            if (!this.mLoadTimeGetted && this.mPageLoadMonitor.mThreadHandler != null) {
                                this.mPageLoadMonitor.mThreadHandler.sendEmptyMessageDelayed(16, 100);
                                return;
                            }
                            return;
                        }
                        stopLoadTimeCalculate();
                    } else {
                        stopLoadTimeCalculate();
                    }
                } else {
                    this.mLastLayoutTime = nanoTime;
                    stopLoadTimeCalculate();
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void needStopLoadTimeCalculate(boolean z) {
        if (!this.mLoadTimeGetted) {
            if ((this.mTotalLayOutTimes != 0 || !this.mPageLoadMonitor.mPageStat.isColdOpen) && this.mDecorView != null) {
                long nanoTime = System.nanoTime() / 1000000;
                long j = nanoTime - this.mLastLayoutTime;
                doOnEndOfLayout(true);
                if (!this.mLoadTimeGetted) {
                    checkLoadTimeOnLayout(nanoTime);
                    if (!this.mLoadTimeGetted) {
                        if (!z) {
                            if (this.mFrameIsTotalLoad || this.mFrameIsLoad) {
                                if (this.mFrameIsLoad && j >= 2000) {
                                    stopLoadTimeCalculate();
                                    return;
                                } else if (this.mFrameIsTotalLoad && j >= 1000) {
                                    stopLoadTimeCalculate();
                                    return;
                                }
                            } else if (this.mTotalLayOutTimes == 1 && this.mActivityVisibleViewCount > this.mViewGroupCount && !this.mIsWaitDataFill && j >= TBToast.Duration.MEDIUM && this.mLastWidthPercent >= 33 && this.mLastHeightPercent >= 10) {
                                stopLoadTimeCalculate();
                                return;
                            } else if (this.mTotalLayOutTimes > 1 && !this.mIsWaitDataFill && j >= 5000 && this.mLastWidthPercent >= 33 && this.mLastHeightPercent >= 5) {
                                stopLoadTimeCalculate();
                                return;
                            }
                            if (this.mPageLoadMonitor.mThreadHandler != null) {
                                this.mPageLoadMonitor.mThreadHandler.removeMessages(16);
                                this.mPageLoadMonitor.mThreadHandler.sendEmptyMessageDelayed(16, 100);
                            }
                        } else if (this.mTotalLayOutTimes >= 1 && !this.mIsWaitDataFill && this.mActivityVisibleViewCount > this.mViewGroupCount && this.mLastWidthPercent >= 10 && this.mLastHeightPercent >= 5) {
                            stopLoadTimeCalculate();
                        }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void checkLoadTimeOnLayout(long j) {
        if (j - this.mLastLayoutTime <= UmbrellaConstants.PERFORMANCE_DATA_ALIVE || this.mTotalLayOutTimes <= 0) {
            int i = (this.mActivityViewCount / 100) + 1;
            if (i % 100 > 0) {
                i++;
            }
            if (this.mFrameIsTotalLoad && this.mAllScreenLoadTimes > i) {
                stopLoadTimeCalculate();
                return;
            }
            return;
        }
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
                if (DeviceInfoManager.instance().getApiLevel() >= 16) {
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
            if (LoadTimeCalculate.this.mPageLoadMonitor != null) {
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
