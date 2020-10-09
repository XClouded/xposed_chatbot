package com.ali.telescope.internal.plugins.smooth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.TabActivity;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Choreographer;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.webkit.WebView;
import android.widget.AbsListView;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import androidx.annotation.Nullable;
import com.ali.telescope.base.event.Event;
import com.ali.telescope.base.plugin.ITelescopeContext;
import com.ali.telescope.base.plugin.Plugin;
import com.ali.telescope.data.PageGetter;
import com.ali.telescope.util.TelescopeLog;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import org.json.JSONObject;

public class SmoothPlugin extends Plugin {
    static final int MIN_FRAME_COUNT = 5;
    private static final String TAG = "SmoothPlugin";
    public int[] activityBadSmoothStepCount = new int[20];
    Application application;
    ArrayList<SmStat> dragList = new ArrayList<>(20);
    ArrayList<SmStat> fpsList = new ArrayList<>(20);
    int mActivityDragFlingCount;
    int mActivityTotalBadSmCount;
    int mActivityTotalBadSmUsedTime;
    int mActivityTotalSmCount;
    int mActivityTotalSmUsedTime;
    int mBadSmCount;
    Class mClassRecyclerView;
    Class mClassViewPager;
    int mCreateIndex;
    volatile View mDecorView;
    int mDepth;
    int mDragFpsCount;
    int mDrawTimesOnDrag;
    int mDrawTimesOnFling;
    int mEventCount;
    int mEventUsedTime;
    boolean mFetchSmoothView = false;
    int mFlingFpsCount;
    long mFlyFrameStartTime = 0;
    MyFrameCallback mFrameCallback;
    long mFrameEndTime = 0;
    long mFrameStartTime = 0;
    long mFrameTimeArrayStartTime;
    short[] mFrameTimeByteArray;
    short mFrameTimeIndex;
    boolean mHasMoved;
    boolean mIsActivityPaused;
    boolean mIsFirstMove = true;
    boolean mIsFlingStart;
    boolean mIsTouchDownMode;
    long mLastFrameTimeNanos;
    volatile View mLastSmoothView;
    long mLastTouchDownTime;
    long mLastTouchTime;
    long mMaxDelayTimeOnFling;
    long mMaxDelayedTime;
    long mMaxSMInterval;
    boolean mNeedScrollView;
    MyOnPreDrawListener mOnPreDrawListener;
    /* access modifiers changed from: private */
    public String mPageHashCode;
    /* access modifiers changed from: private */
    public String mPageName;
    Rect mRectView = new Rect();
    int mTotalBadSmTime;
    int mTotalSmCount;
    long mTotalTimeOnFling;
    long mTouchCount;
    String mViewName;
    ViewTreeObserver mViewTreeObserver;
    WeakHashMap<View, Integer> mWeakSmoothViewMap = new WeakHashMap<>();
    int sSmoothStepInterval = 16;
    ITelescopeContext tcontext;

    public static class SmStat implements Serializable {
        public short badSmCount;
        public short drawCount;
        public short eventCount;
        public short eventMaxDelaytime;
        public short eventRate;
        public short eventUseTime;
        public int index;
        public short layoutTimes;
        public short maxSMInterval;
        public short sm;
        public short totalBadSmTime;
        public short totalSmCount;
        public short usetime;
        public String viewName;
    }

    public interface SmoothView {
    }

    public boolean isPaused() {
        return false;
    }

    public void onDestroy() {
    }

    public void onEvent(int i, Event event) {
    }

    public void onPause(int i, int i2) {
    }

    public void onResume(int i, int i2) {
    }

    class MyOnPreDrawListener implements ViewTreeObserver.OnPreDrawListener {
        int mIndex;

        public MyOnPreDrawListener(int i) {
            this.mIndex = i;
        }

        public boolean onPreDraw() {
            if (SmoothPlugin.this.mCreateIndex != this.mIndex) {
                return true;
            }
            SmoothPlugin.this.onDraw(System.nanoTime() / 1000000);
            return true;
        }
    }

    public void onCreate(Application application2, final ITelescopeContext iTelescopeContext, JSONObject jSONObject) {
        this.tcontext = iTelescopeContext;
        this.application = application2;
        this.application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            short mStartCounter = 0;

            public void onActivityDestroyed(Activity activity) {
            }

            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            public void onActivityCreated(Activity activity, Bundle bundle) {
                SmoothPlugin.this.onActivityCreated(activity);
                TelescopeLog.w("SmoothPlugin", "onCreate - > onActivityCreated");
                String unused = SmoothPlugin.this.mPageName = PageGetter.getPageName(activity, iTelescopeContext.getNameConverter());
                String unused2 = SmoothPlugin.this.mPageHashCode = PageGetter.getPageHashCode(activity);
            }

            public void onActivityStarted(Activity activity) {
                this.mStartCounter = (short) (this.mStartCounter + 1);
                if (!(activity instanceof TabActivity)) {
                    try {
                        SmoothPlugin.this.mDecorView = activity.getWindow().getDecorView().getRootView();
                    } catch (Throwable unused) {
                    }
                    if (SmoothPlugin.this.mDecorView != null) {
                        SmoothPlugin.this.mViewTreeObserver = SmoothPlugin.this.mDecorView.getViewTreeObserver();
                        if (SmoothPlugin.this.mViewTreeObserver != null && SmoothPlugin.this.mViewTreeObserver.isAlive()) {
                            SmoothPlugin.this.mViewTreeObserver.removeOnPreDrawListener(SmoothPlugin.this.mOnPreDrawListener);
                            SmoothPlugin.this.mCreateIndex++;
                            SmoothPlugin.this.mOnPreDrawListener = new MyOnPreDrawListener(SmoothPlugin.this.mCreateIndex);
                            SmoothPlugin.this.mViewTreeObserver.addOnPreDrawListener(SmoothPlugin.this.mOnPreDrawListener);
                        }
                        Window window = activity.getWindow();
                        Window.Callback callback = window.getCallback();
                        if (!(callback instanceof NewCallBack)) {
                            window.setCallback(new NewCallBack(callback));
                        }
                    } else {
                        return;
                    }
                }
                SmoothPlugin.this.onActivityStarted(activity);
            }

            public void onActivityResumed(Activity activity) {
                SmoothPlugin.this.mDecorView = activity.getWindow().getDecorView().getRootView();
            }

            public void onActivityPaused(Activity activity) {
                if (SmoothPlugin.this.mIsTouchDownMode || SmoothPlugin.this.mIsFlingStart) {
                    SmoothPlugin.this.stopSmoothSmCalculate();
                }
                SmoothPlugin.this.onActivityPaused(activity);
            }

            public void onActivityStopped(Activity activity) {
                this.mStartCounter = (short) (this.mStartCounter - 1);
                if (this.mStartCounter == 0) {
                    SmoothPlugin.this.mDecorView = null;
                    SmoothPlugin.this.mWeakSmoothViewMap.clear();
                    SmoothPlugin.this.mLastSmoothView = null;
                }
            }
        });
    }

    /* access modifiers changed from: protected */
    public void onActivityCreated(Activity activity) {
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
        commitOnActivityPaused();
        this.mViewName = null;
        this.mFrameStartTime = 0;
        this.mFrameTimeArrayStartTime = 0;
        this.mFrameEndTime = 0;
        this.mNeedScrollView = false;
        this.mTouchCount = 0;
        this.mLastSmoothView = null;
        this.mActivityTotalSmCount = 0;
        this.mActivityTotalBadSmCount = 0;
        this.mActivityTotalSmUsedTime = 0;
        this.mActivityDragFlingCount = 0;
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

    private void commitOnActivityPaused() {
        if (this.mActivityTotalSmUsedTime != 0 && this.mActivityTotalSmCount != 0) {
            SmoothBean smoothBean = new SmoothBean();
            smoothBean.pageName = this.mPageName;
            smoothBean.pageHashCode = this.mPageHashCode;
            smoothBean.time = System.currentTimeMillis();
            int i = this.mActivityTotalSmUsedTime == 0 ? 0 : (this.mActivityTotalSmCount * 1000) / this.mActivityTotalSmUsedTime;
            if (i >= 60) {
                if (this.mTotalBadSmTime >= 0) {
                    this.mActivityTotalSmUsedTime = (this.mActivityTotalSmCount * 1000) / 60;
                    this.mActivityTotalSmUsedTime += this.mTotalBadSmTime;
                }
                i = this.mActivityTotalSmUsedTime == 0 ? 0 : (this.mActivityTotalSmCount * 1000) / this.mActivityTotalSmUsedTime;
            }
            smoothBean.avgSm = i;
            smoothBean.dragFlingCount = this.mActivityDragFlingCount;
            if (this.mActivityTotalSmUsedTime > 0 && this.mActivityTotalSmUsedTime < 600000) {
                smoothBean.activityTotalSmCount = this.mActivityTotalSmCount;
                smoothBean.activityTotalSmUsedTime = this.mActivityTotalSmUsedTime;
                smoothBean.activityTotalBadSmUsedTime = this.mActivityTotalBadSmUsedTime;
                smoothBean.activityTotalBadSmCount = this.mActivityTotalBadSmCount;
            }
            TelescopeLog.i("SmoothPlugin", "avgSm : " + smoothBean.avgSm + ", dragFlingCount : " + smoothBean.dragFlingCount + ", activityTotalSmCount : " + smoothBean.activityTotalSmCount + ", activityTotalSmUsedTime : " + smoothBean.activityTotalSmUsedTime + ", activityTotalBadSmUsedTime : " + smoothBean.activityTotalBadSmUsedTime + ", activityTotalBadSmCount : " + smoothBean.activityTotalBadSmUsedTime);
            this.tcontext.getBeanReport().send(smoothBean);
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityStarted(Activity activity) {
        if (Build.VERSION.SDK_INT >= 16 && this.mFrameCallback == null) {
            this.mFrameCallback = new MyFrameCallback();
        }
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
        if (Build.VERSION.SDK_INT >= 16) {
            Choreographer.getInstance().postFrameCallback(this.mFrameCallback);
        }
        if (!this.mFetchSmoothView && view != null) {
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
    @SuppressLint({"NewApi"})
    public void onTouchUp(long j) {
        startSmCalculate();
    }

    /* access modifiers changed from: package-private */
    @SuppressLint({"NewApi"})
    public void startSmCalculate() {
        if (Build.VERSION.SDK_INT >= 16) {
            Log.e("SmoothPlugin", "startSmCalculate");
            this.mTotalSmCount = 0;
            this.mTotalBadSmTime = 0;
            this.mBadSmCount = 0;
            this.mMaxSMInterval = 0;
            this.mFrameStartTime = this.mLastFrameTimeNanos;
            this.mFlyFrameStartTime = System.nanoTime() / 1000000;
            if (this.mFrameStartTime == 0) {
                this.mFrameStartTime = this.mFlyFrameStartTime;
            }
            this.mFrameEndTime = 0;
            this.mIsFlingStart = true;
            this.mDrawTimesOnFling = 0;
            this.mMaxDelayTimeOnFling = 0;
            this.mFrameCallback.mInnerDrawCount = 0;
            Choreographer.getInstance().postFrameCallback(this.mFrameCallback);
        }
    }

    /* access modifiers changed from: package-private */
    @SuppressLint({"NewApi"})
    public void stopSmoothSmCalculate() {
        if (Build.VERSION.SDK_INT >= 16) {
            onScrollFinished();
            Log.e("SmoothPlugin", "stopSmoothSmCalculate");
            this.mIsFlingStart = false;
            if (this.mFrameCallback != null) {
                Choreographer.getInstance().removeFrameCallback(this.mFrameCallback);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void findSmoothView(MotionEvent motionEvent) {
        View view;
        int x = (int) motionEvent.getX();
        int y = (int) motionEvent.getY();
        if (!this.mIsActivityPaused && this.mWeakSmoothViewMap.size() > 0) {
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
        if ((view instanceof AbsListView) || (view instanceof ScrollView) || (view instanceof HorizontalScrollView) || (view instanceof SmoothView) || (view instanceof WebView)) {
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

    public static String getSimpleName(String str) {
        if (str == null) {
            return "";
        }
        int lastIndexOf = str.lastIndexOf(46);
        if (lastIndexOf < 0) {
            lastIndexOf = str.lastIndexOf(36);
        }
        if (lastIndexOf < 0) {
            return str;
        }
        return str.substring(lastIndexOf + 1);
    }

    /* access modifiers changed from: package-private */
    public void onScrollFinished() {
        this.mViewName = this.mLastSmoothView != null ? getSimpleName(this.mLastSmoothView.getClass().getName()) : "";
        this.mTotalTimeOnFling = (this.mFrameEndTime - this.mFrameStartTime) / 1000000;
        commitActivityFlingFps(this.mTotalSmCount, this.mTotalTimeOnFling, this.mMaxSMInterval, this.mLastSmoothView, this.mBadSmCount);
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

    public boolean onDispatchTouchEvent(Window.Callback callback, MotionEvent motionEvent, KeyEvent keyEvent) {
        boolean z;
        if (motionEvent == null && keyEvent == null) {
            return false;
        }
        long nanoTime = System.nanoTime() / 1000000;
        int action = motionEvent != null ? motionEvent.getAction() : keyEvent.getAction();
        if (action == 0) {
            this.mIsFirstMove = true;
            this.mHasMoved = false;
            onTouchDown(motionEvent, nanoTime, this.mDecorView);
            this.mEventCount = 0;
            this.mEventUsedTime = 0;
            this.mMaxDelayedTime = 0;
        }
        if (motionEvent != null) {
            z = callback.dispatchTouchEvent(motionEvent);
        } else {
            z = keyEvent != null ? callback.dispatchKeyEvent(keyEvent) : false;
        }
        this.mLastTouchTime = nanoTime;
        long nanoTime2 = (System.nanoTime() / 1000000) - nanoTime;
        this.mEventCount++;
        this.mEventUsedTime = (int) (((long) this.mEventUsedTime) + nanoTime2);
        if (this.mMaxDelayedTime < nanoTime2) {
            this.mMaxDelayedTime = nanoTime2;
        }
        switch (action) {
            case 1:
            case 3:
                if (this.mIsTouchDownMode || this.mIsFlingStart) {
                    onTouchUp(nanoTime);
                    break;
                }
            case 2:
                if (motionEvent != null && this.mIsFirstMove && this.mHasMoved) {
                    this.mIsFirstMove = false;
                    onTouchFirstMove(motionEvent, nanoTime);
                    break;
                }
        }
        return z;
    }

    /* access modifiers changed from: package-private */
    public int commitActivityDragFps(String str, int i, long j, long j2, int i2, long j3, View view) {
        if (i == 0) {
            return 0;
        }
        long j4 = j3 - this.mLastTouchDownTime;
        if (j4 == 0 || j4 >= 60000) {
            return 0;
        }
        if (((int) (((long) (this.mTotalSmCount * 1000)) / j4)) >= 60 && this.mTotalBadSmTime >= 0) {
            j4 = ((long) ((this.mTotalSmCount * 1000) / 60)) + ((long) this.mTotalBadSmTime);
        }
        int i3 = (int) (((long) (this.mTotalSmCount * 1000)) / j4);
        if (i3 > 60) {
            i3 = 60;
        }
        this.mActivityTotalSmCount += this.mTotalSmCount;
        this.mActivityTotalSmUsedTime = (int) (((long) this.mActivityTotalSmUsedTime) + j4);
        this.mActivityTotalBadSmUsedTime += this.mTotalBadSmTime;
        this.mActivityTotalBadSmCount += this.mBadSmCount;
        this.mActivityDragFlingCount++;
        if (TelescopeLog.isLogOpen()) {
            SmStat smStat = new SmStat();
            smStat.index = this.mDragFpsCount;
            smStat.eventCount = (short) i;
            smStat.eventUseTime = (short) ((int) j);
            smStat.drawCount = (short) i2;
            smStat.eventMaxDelaytime = (short) ((int) j2);
            smStat.maxSMInterval = (short) ((int) this.mMaxSMInterval);
            smStat.usetime = (short) ((int) j4);
            smStat.sm = (short) i3;
            smStat.badSmCount = (short) this.mBadSmCount;
            smStat.totalSmCount = (short) this.mTotalSmCount;
            smStat.totalBadSmTime = (short) this.mTotalBadSmTime;
            if (view != null) {
                smStat.viewName = getSimpleName(view.getClass().getName());
            }
            this.dragList.add(smStat);
        }
        this.mDragFpsCount++;
        TelescopeLog.d("SmoothPlugin", "index=" + this.mDragFpsCount + ", DrawTimes=" + i2);
        TelescopeLog.d("SmoothPlugin", "TotalTime=" + j4 + ", SM=" + i3 + ", TotalSmCount=" + this.mTotalSmCount + ", BadSmCount=" + this.mBadSmCount + ", MaxSMInterval=" + this.mMaxSMInterval);
        return i3;
    }

    /* access modifiers changed from: package-private */
    public void commitActivityFlingFps(int i, long j, long j2, View view, int i2) {
        if (j != 0 && j < 60000 && i != 0) {
            int i3 = i * 1000;
            long j3 = (long) i3;
            if (((int) (j3 / j)) >= 60 && this.mTotalBadSmTime >= 0) {
                j = ((long) (i3 / 60)) + ((long) this.mTotalBadSmTime);
            }
            int i4 = (int) (j3 / j);
            if (i4 > 60) {
                i4 = 60;
            }
            this.mActivityTotalSmCount += i;
            this.mActivityTotalSmUsedTime = (int) (((long) this.mActivityTotalSmUsedTime) + j);
            this.mActivityDragFlingCount++;
            this.mActivityTotalBadSmUsedTime += this.mTotalBadSmTime;
            this.mActivityTotalBadSmCount += this.mBadSmCount;
            if (TelescopeLog.sLogLevel == 3) {
                SmStat smStat = new SmStat();
                smStat.index = this.mFlingFpsCount;
                smStat.eventCount = 0;
                smStat.eventRate = 0;
                smStat.drawCount = 0;
                smStat.eventMaxDelaytime = (short) ((int) j2);
                smStat.maxSMInterval = (short) ((int) this.mMaxSMInterval);
                smStat.usetime = (short) ((int) j);
                smStat.sm = (short) i4;
                smStat.badSmCount = (short) this.mBadSmCount;
                smStat.totalSmCount = (short) this.mTotalSmCount;
                smStat.totalBadSmTime = (short) this.mTotalBadSmTime;
                if (view != null) {
                    smStat.viewName = getSimpleName(view.getClass().getName());
                }
                TelescopeLog.d("SmoothPlugin", "fling TotalTime=" + j + ", SM=" + i4 + ", TotalSmCount=" + this.mTotalSmCount + ", BadSmCount=" + this.mBadSmCount + ", MaxSMInterval=" + this.mMaxSMInterval);
                this.fpsList.add(smStat);
            }
            this.mFlingFpsCount++;
        }
    }

    @SuppressLint({"NewApi"})
    public class MyFrameCallback implements Choreographer.FrameCallback {
        int mInnerDrawCount = 0;

        public MyFrameCallback() {
        }

        public void doFrame(long j) {
            SmoothPlugin.this.mLastFrameTimeNanos = j;
            long nanoTime = System.nanoTime();
            SmoothPlugin.this.mTotalSmCount++;
            if (SmoothPlugin.this.mFrameEndTime > 0) {
                float f = ((float) (nanoTime - SmoothPlugin.this.mFrameEndTime)) / 1000000.0f;
                if (f >= 16.7f) {
                    SmoothPlugin.this.mBadSmCount++;
                    SmoothPlugin smoothPlugin = SmoothPlugin.this;
                    smoothPlugin.mTotalBadSmTime = (int) (((float) smoothPlugin.mTotalBadSmTime) + (f - 16.6f));
                }
                if (SmoothPlugin.this.mFrameTimeByteArray != null && SmoothPlugin.this.mFrameTimeIndex < SmoothPlugin.this.mFrameTimeByteArray.length) {
                    SmoothPlugin.this.mFrameTimeByteArray[SmoothPlugin.this.mFrameTimeIndex] = (short) ((int) f);
                    SmoothPlugin smoothPlugin2 = SmoothPlugin.this;
                    smoothPlugin2.mFrameTimeIndex = (short) (smoothPlugin2.mFrameTimeIndex + 1);
                }
                long j2 = (long) f;
                if (SmoothPlugin.this.mMaxSMInterval < j2) {
                    SmoothPlugin.this.mMaxSMInterval = j2;
                }
                if (f >= 16.7f) {
                    int i = (((int) f) / SmoothPlugin.this.sSmoothStepInterval) - 1;
                    if (i > SmoothPlugin.this.activityBadSmoothStepCount.length - 1) {
                        i = SmoothPlugin.this.activityBadSmoothStepCount.length - 1;
                    }
                    if (i >= 0) {
                        int[] iArr = SmoothPlugin.this.activityBadSmoothStepCount;
                        iArr[i] = iArr[i] + 1;
                    }
                }
            }
            SmoothPlugin.this.mFrameEndTime = nanoTime;
            if (SmoothPlugin.this.mIsFlingStart) {
                this.mInnerDrawCount++;
                int i2 = this.mInnerDrawCount - SmoothPlugin.this.mDrawTimesOnFling;
                if (i2 >= 2 || i2 <= -2) {
                    TelescopeLog.d("SmoothPlugin", "停止滑动统计 , stopFrame=" + i2);
                    SmoothPlugin.this.stopSmoothSmCalculate();
                }
                long nanoTime2 = System.nanoTime() / 1000000;
                if (i2 == 1 && nanoTime2 - SmoothPlugin.this.mFlyFrameStartTime > 10000) {
                    SmoothPlugin.this.mFlyFrameStartTime = nanoTime2;
                    TelescopeLog.e("SmoothPlugin", "界面有不停的刷新，可能有视频或者动画!");
                    SmoothPlugin.this.stopSmoothSmCalculate();
                }
            }
            if (SmoothPlugin.this.mIsTouchDownMode || SmoothPlugin.this.mIsFlingStart) {
                Choreographer.getInstance().postFrameCallback(SmoothPlugin.this.mFrameCallback);
            }
        }
    }

    @SuppressLint({"NewApi"})
    class NewCallBack implements Window.Callback {
        Window.Callback mCallBack;

        public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, @Nullable Menu menu, int i) {
        }

        public boolean onSearchRequested(SearchEvent searchEvent) {
            return false;
        }

        public NewCallBack(Window.Callback callback) {
            this.mCallBack = callback;
        }

        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            try {
                return SmoothPlugin.this.onDispatchTouchEvent(this.mCallBack, (MotionEvent) null, keyEvent);
            } catch (Throwable th) {
                TelescopeLog.e("SmoothPluginfindbug", Log.getStackTraceString(th));
                return false;
            }
        }

        public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
            return this.mCallBack.dispatchKeyShortcutEvent(keyEvent);
        }

        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            try {
                return SmoothPlugin.this.onDispatchTouchEvent(this.mCallBack, motionEvent, (KeyEvent) null);
            } catch (Throwable th) {
                TelescopeLog.e("SmoothPluginfindbug", Log.getStackTraceString(th));
                return false;
            }
        }

        public boolean dispatchTrackballEvent(MotionEvent motionEvent) {
            return this.mCallBack.dispatchTrackballEvent(motionEvent);
        }

        public boolean dispatchGenericMotionEvent(MotionEvent motionEvent) {
            return this.mCallBack.dispatchGenericMotionEvent(motionEvent);
        }

        public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
            return this.mCallBack.dispatchPopulateAccessibilityEvent(accessibilityEvent);
        }

        public View onCreatePanelView(int i) {
            return this.mCallBack.onCreatePanelView(i);
        }

        public boolean onCreatePanelMenu(int i, Menu menu) {
            return this.mCallBack.onCreatePanelMenu(i, menu);
        }

        public boolean onPreparePanel(int i, View view, Menu menu) {
            return this.mCallBack.onPreparePanel(i, view, menu);
        }

        public boolean onMenuOpened(int i, Menu menu) {
            return this.mCallBack.onMenuOpened(i, menu);
        }

        public boolean onMenuItemSelected(int i, MenuItem menuItem) {
            return this.mCallBack.onMenuItemSelected(i, menuItem);
        }

        public void onWindowAttributesChanged(WindowManager.LayoutParams layoutParams) {
            this.mCallBack.onWindowAttributesChanged(layoutParams);
        }

        public void onContentChanged() {
            this.mCallBack.onContentChanged();
        }

        public void onWindowFocusChanged(boolean z) {
            this.mCallBack.onWindowFocusChanged(z);
        }

        public void onAttachedToWindow() {
            this.mCallBack.onAttachedToWindow();
        }

        public void onDetachedFromWindow() {
            this.mCallBack.onDetachedFromWindow();
        }

        public void onPanelClosed(int i, Menu menu) {
            this.mCallBack.onPanelClosed(i, menu);
        }

        public boolean onSearchRequested() {
            return this.mCallBack.onSearchRequested();
        }

        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
            return this.mCallBack.onWindowStartingActionMode(callback);
        }

        public void onActionModeStarted(ActionMode actionMode) {
            this.mCallBack.onActionModeStarted(actionMode);
        }

        public void onActionModeFinished(ActionMode actionMode) {
            this.mCallBack.onActionModeFinished(actionMode);
        }

        @Nullable
        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int i) {
            return this.mCallBack.onWindowStartingActionMode(callback, i);
        }
    }
}
