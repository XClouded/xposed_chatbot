package com.taobao.onlinemonitor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SearchEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import com.taobao.onlinemonitor.OnLineMonitor;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class ActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {
    volatile Activity mActivity;
    long mActivityDestroyTime;
    String mActivityName;
    long mActivityOncreateTime;
    long mActivityPausedTime;
    long mActivityResumeTime;
    ArrayList<String> mActivityStackList = new ArrayList<>();
    long mActivityStartTime;
    long mActivityStopedTime;
    short mBootActivityIndex = 0;
    int mCreateIndex;
    int mCreateUsedTime;
    volatile View mDecorView;
    int mEventCount;
    int mEventUsedTime;
    GestureDetector mGestureDetector;
    boolean mHasFling;
    boolean mHasMoved;
    boolean mIsBootFinished;
    boolean mIsFirstMove = true;
    short mIsHardWareStatus;
    boolean mIsOnCreated;
    LoadTimeCalculate mLoadTimeCalculate;
    long mMaxDelayedTime;
    String mMoveDirection = "D";
    long mMoveRelativeTime = 0;
    OnLineMonitor mOnLineMonitor;
    MyOnPreDrawListener mOnPreDrawListener;
    ArrayList<Object> mRemoveGlobalListererList = new ArrayList<>(10);
    SmoothCalculate mSmoothCalculate;
    volatile short mStartCounter = 0;
    ViewTreeObserver mViewTreeObserver;

    public ActivityLifecycleCallback(Context context) {
    }

    /* access modifiers changed from: package-private */
    public String getActivityName(Activity activity) {
        String str = "";
        if (!(activity instanceof OnLineMonitor.OnDesignatedActivityName)) {
            return activity.getClass().getName();
        }
        Intent intent = activity.getIntent();
        if (intent != null) {
            str = intent.getStringExtra("ActivityName");
        }
        if (TextUtils.isEmpty(str)) {
            return activity.getClass().getName();
        }
        return str;
    }

    @SuppressLint({"NewApi"})
    public void onActivityCreated(Activity activity, Bundle bundle) {
        this.mActivityOncreateTime = System.nanoTime() / 1000000;
        if (this.mOnLineMonitor != null) {
            this.mActivityStackList.add(activity.toString());
            this.mActivityName = getActivityName(activity);
            this.mOnLineMonitor.mActivityName = this.mActivityName;
            if (OnLineMonitor.sIsTraceDetail) {
                this.mOnLineMonitor.doLifeCycleCheck(activity, 0);
            }
            if (this.mIsHardWareStatus < 3 && this.mOnLineMonitor.mHardWareInfo.mGpuName == null) {
                this.mOnLineMonitor.mHardWareInfo.getGpuInfo(activity);
                this.mIsHardWareStatus = (short) (this.mIsHardWareStatus + 1);
            }
            if (!this.mIsBootFinished || this.mOnLineMonitor.mIsInBackGround) {
                if (this.mOnLineMonitor.mApplicationContext == null) {
                    if (this.mOnLineMonitor.mMainThread == null) {
                        this.mOnLineMonitor.mMainThread = Thread.currentThread();
                    }
                    this.mOnLineMonitor.mApplicationContext = activity.getApplicationContext();
                    try {
                        this.mGestureDetector = new GestureDetector(this.mOnLineMonitor.mApplicationContext, new MyGestureDetector());
                    } catch (Throwable unused) {
                    }
                    this.mOnLineMonitor.back2ForeChanged();
                }
                if (OnLineMonitorApp.sFirstActivityTime < 0 || this.mOnLineMonitor.mIsInBackGround) {
                    if (OnLineMonitorApp.sColdBootCheck != null) {
                        OnLineMonitorApp.sColdBootCheck.stopChecker();
                        OnLineMonitorApp.sColdBootCheck = null;
                    }
                    if (OnLineMonitorApp.sFirstActivityTime > 0) {
                        this.mBootActivityIndex = 0;
                        OnLineMonitorApp.sIsCodeBoot = false;
                        this.mOnLineMonitor.mIsInBootStep = true;
                        OnLineMonitorApp.sBootExtraType = "0";
                        if (OnLineMonitorApp.sBootCorrectAry != null) {
                            for (int i = 0; i < OnLineMonitorApp.sBootCorrectAry.length; i++) {
                                OnLineMonitorApp.sBootCorrectAry[i] = false;
                            }
                        }
                    }
                    OnLineMonitorApp.sFirstActivityTime = this.mActivityOncreateTime;
                    if (!OnLineMonitorApp.sIsCodeBoot && this.mActivityOncreateTime - OnLineMonitorApp.sLaunchTime <= ((long) this.mOnLineMonitor.mColdBootOffsetTime)) {
                        OnLineMonitorApp.sIsCodeBoot = true;
                    }
                    if (!(this.mOnLineMonitor.mTraceDetail == null || this.mOnLineMonitor.mTraceDetail.mFieldThreadCount == null)) {
                        try {
                            this.mOnLineMonitor.mTraceDetail.mNewTheadCountAyr[1] = this.mOnLineMonitor.mTraceDetail.mFieldThreadCount.getInt(Thread.class);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (this.mBootActivityIndex < OnLineMonitorApp.sBootAcitvityCount) {
                    String str = OnLineMonitorApp.sBootActivityAry[this.mBootActivityIndex];
                    if (str == null || !str.equals(activity.getClass().getName())) {
                        this.mIsBootFinished = true;
                        this.mOnLineMonitor.mIsInBootStep = false;
                    } else {
                        OnLineMonitorApp.sBootCorrectAry[this.mBootActivityIndex] = true;
                        this.mIsBootFinished = false;
                    }
                }
                this.mBootActivityIndex = (short) (this.mBootActivityIndex + 1);
                if (!this.mIsBootFinished && this.mBootActivityIndex == OnLineMonitorApp.sBootAcitvityCount) {
                    this.mIsBootFinished = true;
                    if (OnLineMonitorApp.isBootCorrect()) {
                        long nanoTime = System.nanoTime() / 1000000;
                        long j = OnLineMonitorApp.sLaunchTime;
                        if (!OnLineMonitorApp.sIsCodeBoot) {
                            j = OnLineMonitorApp.sFirstActivityTime;
                        }
                        long elapsedRealtime = OnLineMonitorApp.sIsCodeBoot ? SystemClock.elapsedRealtime() - this.mOnLineMonitor.mProcessCpuTracker.mPidStartTime : 0;
                        long j2 = nanoTime - j;
                        if (OnLineMonitorApp.sIsCodeBoot) {
                            this.mOnLineMonitor.mOnLineStat.preparePidTime = (int) (elapsedRealtime - j2);
                        }
                        if (elapsedRealtime <= 0 || elapsedRealtime <= j2 || elapsedRealtime - j2 > 5000) {
                            this.mOnLineMonitor.onBootEnd(nanoTime, j2);
                        } else {
                            this.mOnLineMonitor.onBootEnd(nanoTime + ((long) (this.mOnLineMonitor.mOnLineStat.preparePidTime / 2)), elapsedRealtime);
                        }
                        if (!(this.mOnLineMonitor.mTraceDetail == null || this.mOnLineMonitor.mTraceDetail.mFieldThreadCount == null)) {
                            try {
                                this.mOnLineMonitor.mTraceDetail.mNewTheadCountAyr[2] = this.mOnLineMonitor.mTraceDetail.mFieldThreadCount.getInt(Thread.class);
                            } catch (Exception e2) {
                                e2.printStackTrace();
                            }
                        }
                        if (this.mOnLineMonitor.mOnLineStat != null) {
                            this.mOnLineMonitor.mOnLineStat.mHomeActivity = new WeakReference<>(activity);
                        }
                        if (this.mOnLineMonitor.mTraceDetail != null) {
                            this.mOnLineMonitor.mTraceDetail.onBootStep1();
                        }
                    } else {
                        this.mOnLineMonitor.mThreadHandler.sendEmptyMessageDelayed(13, 5000);
                        this.mOnLineMonitor.mIsInBootStep = false;
                    }
                }
            }
            this.mIsOnCreated = true;
            this.mOnLineMonitor.onActivityCreate(activity);
            if (this.mLoadTimeCalculate != null) {
                this.mLoadTimeCalculate.onActivityCreated(activity);
            }
            if (this.mSmoothCalculate != null) {
                this.mSmoothCalculate.onActivityCreated(activity);
            }
            this.mOnLineMonitor.notifyOnActivityLifeCycleList(activity, 1);
        }
    }

    public void onFragmentCreate() {
        if (this.mActivity != null) {
            this.mActivityOncreateTime = System.nanoTime() / 1000000;
            this.mIsOnCreated = true;
            if (this.mOnLineMonitor != null) {
                this.mOnLineMonitor.onActivityCreate(this.mActivity);
            }
            if (this.mLoadTimeCalculate != null) {
                this.mLoadTimeCalculate.onActivityCreated(this.mActivity);
            }
            if (this.mSmoothCalculate != null) {
                this.mSmoothCalculate.onActivityCreated(this.mActivity);
            }
        }
    }

    @SuppressLint({"NewApi"})
    public void onActivityResumed(Activity activity) {
        if (this.mOnLineMonitor != null && OnLineMonitor.sIsTraceDetail) {
            this.mOnLineMonitor.doLifeCycleCheck(activity, 2);
        }
        this.mActivityResumeTime = System.nanoTime() / 1000000;
        this.mActivity = activity;
        this.mActivityName = getActivityName(activity);
        this.mDecorView = activity.getWindow().getDecorView().getRootView();
        if (this.mOnLineMonitor != null) {
            this.mOnLineMonitor.mActivityName = this.mActivityName;
            this.mOnLineMonitor.onActivityResume(activity);
            this.mOnLineMonitor.notifyOnActivityLifeCycleList(activity, 3);
        }
        removeGlobalLayoutFromWeakHashMap();
    }

    @SuppressLint({"NewApi"})
    public boolean onDispatchTouchEvent(Window.Callback callback, MotionEvent motionEvent, KeyEvent keyEvent) {
        boolean z;
        if (motionEvent == null && keyEvent == null) {
            return false;
        }
        long nanoTime = System.nanoTime() / 1000000;
        if (!(this.mLoadTimeCalculate == null || this.mGestureDetector == null || motionEvent == null)) {
            this.mGestureDetector.onTouchEvent(motionEvent);
        }
        int action = motionEvent != null ? motionEvent.getAction() : keyEvent.getAction();
        if (action == 0) {
            this.mIsFirstMove = true;
            this.mHasMoved = false;
            this.mHasFling = false;
            this.mOnLineMonitor.mOnLineStat.isTouchMode = true;
            this.mOnLineMonitor.mOnLineStat.isActivityTouched = true;
            this.mSmoothCalculate.onTouchDown(motionEvent, nanoTime, this.mDecorView);
            this.mEventCount = 0;
            this.mEventUsedTime = 0;
            this.mMaxDelayedTime = 0;
            this.mOnLineMonitor.onTouchDown(nanoTime);
        }
        if (motionEvent != null) {
            z = callback.dispatchTouchEvent(motionEvent);
        } else {
            z = keyEvent != null ? callback.dispatchKeyEvent(keyEvent) : false;
        }
        if (this.mOnLineMonitor != null) {
            this.mOnLineMonitor.mCheckAnrTime = nanoTime;
            this.mSmoothCalculate.mLastTouchTime = nanoTime;
            long nanoTime2 = (System.nanoTime() / 1000000) - nanoTime;
            this.mEventCount++;
            this.mEventUsedTime = (int) (((long) this.mEventUsedTime) + nanoTime2);
            if (this.mMaxDelayedTime < nanoTime2) {
                this.mMaxDelayedTime = nanoTime2;
            }
        }
        switch (action) {
            case 1:
                if (z && this.mLoadTimeCalculate != null && !this.mHasMoved) {
                    this.mLoadTimeCalculate.stopOnClick();
                    break;
                }
            case 2:
                if (motionEvent != null && this.mIsFirstMove && this.mSmoothCalculate != null && this.mHasMoved) {
                    this.mIsFirstMove = false;
                    this.mSmoothCalculate.onTouchFirstMove(motionEvent, nanoTime);
                    break;
                }
            case 3:
                break;
        }
        this.mOnLineMonitor.mIsOnTouch = false;
        this.mOnLineMonitor.mOnLineStat.isTouchMode = false;
        if (this.mSmoothCalculate != null && (this.mSmoothCalculate.mIsTouchDownMode || this.mSmoothCalculate.mIsFlingStart)) {
            this.mSmoothCalculate.onTouchUp(motionEvent, nanoTime);
        }
        return z;
    }

    @SuppressLint({"NewApi"})
    public void onActivityStarted(Activity activity) {
        if (this.mOnLineMonitor != null && this.mLoadTimeCalculate != null) {
            this.mActivityStartTime = System.nanoTime() / 1000000;
            if (OnLineMonitor.sIsTraceDetail && !this.mIsOnCreated) {
                this.mOnLineMonitor.doLifeCycleCheck(activity, 1);
            }
            if (this.mIsOnCreated) {
                this.mCreateUsedTime = (int) (this.mActivityStartTime - this.mActivityOncreateTime);
            } else {
                this.mCreateUsedTime = 0;
            }
            if (this.mStartCounter == 0) {
                this.mOnLineMonitor.mOnLineStat.isInBackGround = false;
                this.mOnLineMonitor.notifyBackForGroundListener(20);
            }
            this.mStartCounter = (short) (this.mStartCounter + 1);
            if (!(activity instanceof TabActivity)) {
                if (!this.mOnLineMonitor.mIsActivityColdOpen) {
                    this.mActivityName = getActivityName(activity);
                    this.mOnLineMonitor.mActivityName = this.mActivityName;
                }
                try {
                    this.mDecorView = activity.getWindow().getDecorView().getRootView();
                } catch (Throwable unused) {
                }
                if (this.mDecorView != null) {
                    this.mViewTreeObserver = this.mDecorView.getViewTreeObserver();
                    if (this.mViewTreeObserver != null && this.mViewTreeObserver.isAlive()) {
                        if (this.mOnLineMonitor.mOnGlobalLayoutListener != null) {
                            OnLineMonitor onLineMonitor = this.mOnLineMonitor;
                            if (OnLineMonitor.sApiLevel >= 16) {
                                this.mViewTreeObserver.removeOnGlobalLayoutListener(this.mLoadTimeCalculate.mOnGlobalLayoutListener);
                                this.mViewTreeObserver.removeOnGlobalLayoutListener(this.mOnLineMonitor.mOnGlobalLayoutListener);
                            } else {
                                this.mViewTreeObserver.removeGlobalOnLayoutListener(this.mLoadTimeCalculate.mOnGlobalLayoutListener);
                                this.mViewTreeObserver.removeGlobalOnLayoutListener(this.mOnLineMonitor.mOnGlobalLayoutListener);
                            }
                            putGlobalLayoutToWeakHashMap();
                            this.mViewTreeObserver.removeOnPreDrawListener(this.mOnPreDrawListener);
                        }
                        this.mCreateIndex++;
                        this.mOnLineMonitor.mOnGlobalLayoutListener = this.mOnLineMonitor.createOnGlobalLayoutListener(this.mCreateIndex);
                        this.mLoadTimeCalculate.mOnGlobalLayoutListener = this.mLoadTimeCalculate.createOnGlobalLayoutListener(this.mCreateIndex);
                        this.mViewTreeObserver.addOnGlobalLayoutListener(this.mLoadTimeCalculate.mOnGlobalLayoutListener);
                        this.mViewTreeObserver.addOnGlobalLayoutListener(this.mOnLineMonitor.mOnGlobalLayoutListener);
                        this.mOnPreDrawListener = new MyOnPreDrawListener(this.mCreateIndex);
                        this.mViewTreeObserver.addOnPreDrawListener(this.mOnPreDrawListener);
                    }
                    if (this.mIsOnCreated) {
                        Window window = activity.getWindow();
                        Window.Callback callback = window.getCallback();
                        if (!(callback instanceof NewCallBack)) {
                            window.setCallback(new NewCallBack(callback));
                        }
                    }
                    if (this.mLoadTimeCalculate != null) {
                        this.mLoadTimeCalculate.onActivityStarted(activity, this.mDecorView);
                    }
                    if (this.mSmoothCalculate != null) {
                        this.mSmoothCalculate.onActivityStarted(activity);
                    }
                    if (this.mOnLineMonitor != null) {
                        this.mOnLineMonitor.notifyOnActivityLifeCycleList(activity, 2);
                    }
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void putGlobalLayoutToWeakHashMap() {
        if (this.mRemoveGlobalListererList != null) {
            this.mRemoveGlobalListererList.add(this.mLoadTimeCalculate.mOnGlobalLayoutListener);
            this.mRemoveGlobalListererList.add(this.mOnLineMonitor.mOnGlobalLayoutListener);
            this.mRemoveGlobalListererList.add(this.mOnPreDrawListener);
        }
    }

    /* access modifiers changed from: package-private */
    public void removeGlobalLayoutFromWeakHashMap() {
        if (this.mRemoveGlobalListererList != null && this.mRemoveGlobalListererList.size() > 0 && this.mViewTreeObserver != null && this.mViewTreeObserver.isAlive()) {
            for (int size = this.mRemoveGlobalListererList.size() - 1; size >= 0; size--) {
                Object remove = this.mRemoveGlobalListererList.remove(size);
                if (remove instanceof ViewTreeObserver.OnGlobalLayoutListener) {
                    OnLineMonitor onLineMonitor = this.mOnLineMonitor;
                    if (OnLineMonitor.sApiLevel >= 16) {
                        this.mViewTreeObserver.removeOnGlobalLayoutListener((ViewTreeObserver.OnGlobalLayoutListener) remove);
                    } else {
                        this.mViewTreeObserver.removeGlobalOnLayoutListener((ViewTreeObserver.OnGlobalLayoutListener) remove);
                    }
                } else if (remove instanceof ViewTreeObserver.OnPreDrawListener) {
                    this.mViewTreeObserver.removeOnPreDrawListener((ViewTreeObserver.OnPreDrawListener) remove);
                }
            }
        }
    }

    public void onFragmentStarted() {
        if (this.mActivity != null) {
            if (this.mOnLineMonitor.mActivityRuntimeInfo == null) {
                this.mOnLineMonitor.mActivityRuntimeInfo = new OnLineMonitor.ActivityRuntimeInfo();
            }
            if (this.mLoadTimeCalculate != null) {
                this.mLoadTimeCalculate.onActivityStarted(this.mActivity, this.mDecorView);
            }
            if (this.mSmoothCalculate != null) {
                this.mSmoothCalculate.onActivityStarted(this.mActivity);
            }
        }
    }

    public void setFragmentName(String str) {
        this.mActivityName = str;
        if (this.mOnLineMonitor != null) {
            this.mOnLineMonitor.mActivityName = this.mActivityName;
            if (this.mOnLineMonitor.mOnLineStat != null) {
                this.mOnLineMonitor.mOnLineStat.activityName = this.mActivityName;
            }
        }
        if (this.mOnLineMonitor.mActivityRuntimeInfo != null) {
            this.mOnLineMonitor.mActivityRuntimeInfo.activityName = this.mActivityName;
        }
    }

    public void onFragmentPaused() {
        if (this.mActivity != null) {
            if (this.mOnLineMonitor != null) {
                this.mOnLineMonitor.onActivityPause(this.mActivity);
            }
            if (this.mLoadTimeCalculate != null) {
                this.mLoadTimeCalculate.onActivityPaused(this.mActivity);
            }
            if (this.mSmoothCalculate != null) {
                this.mSmoothCalculate.onActivityPaused(this.mActivity);
            }
        }
    }

    @SuppressLint({"NewApi"})
    public void onActivityPaused(Activity activity) {
        this.mActivityPausedTime = System.nanoTime() / 1000000;
        if (OnLineMonitor.sIsTraceDetail) {
            this.mOnLineMonitor.doLifeCycleCheck(activity, 3);
        }
        this.mIsOnCreated = false;
        if (this.mSmoothCalculate != null && (this.mSmoothCalculate.mIsTouchDownMode || this.mSmoothCalculate.mIsFlingStart)) {
            this.mSmoothCalculate.stopSmoothSmCalculate();
        }
        if (this.mOnLineMonitor != null) {
            this.mOnLineMonitor.onActivityPause(activity);
        }
        if (this.mLoadTimeCalculate != null) {
            this.mLoadTimeCalculate.onActivityPaused(activity);
        }
        if (this.mSmoothCalculate != null) {
            this.mSmoothCalculate.onActivityPaused(activity);
        }
        this.mActivity = null;
        this.mViewTreeObserver = null;
        if (this.mOnLineMonitor != null) {
            this.mOnLineMonitor.notifyOnActivityLifeCycleList(activity, 4);
        }
    }

    public void onActivityStopped(Activity activity) {
        this.mActivityStopedTime = System.nanoTime() / 1000000;
        if (OnLineMonitor.sIsTraceDetail && !activity.isFinishing()) {
            this.mOnLineMonitor.doLifeCycleCheck(activity, 4);
        }
        this.mStartCounter = (short) (this.mStartCounter - 1);
        if (this.mStartCounter < 0) {
            this.mStartCounter = 0;
        }
        if (this.mOnLineMonitor != null) {
            if (this.mStartCounter == 0) {
                this.mDecorView = null;
                if (!this.mIsBootFinished || (this.mOnLineMonitor.mIsBootEndActivity && this.mOnLineMonitor.mBootActivityLoadTime <= 0)) {
                    OnLineMonitorApp.sBackInGroundOnBoot = true;
                }
                this.mSmoothCalculate.mWeakSmoothViewMap.clear();
                this.mSmoothCalculate.mLastSmoothView = null;
            }
            this.mOnLineMonitor.onActivityStopped(activity);
        }
        if (this.mOnLineMonitor != null) {
            this.mOnLineMonitor.notifyOnActivityLifeCycleList(activity, 5);
            if (this.mIsHardWareStatus == 4 && this.mOnLineMonitor.mHardWareInfo.mOnlineGLSurfaceView != null) {
                this.mOnLineMonitor.mHardWareInfo.destroy();
            }
        }
        this.mOnLineMonitor.mProblemCheck.checkQueuedWork();
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        if (this.mOnLineMonitor != null) {
            this.mOnLineMonitor.notifyOnlineRuntimeStat(4);
        }
    }

    public void onActivityDestroyed(Activity activity) {
        this.mActivityDestroyTime = System.nanoTime() / 1000000;
        if (OnLineMonitor.sIsTraceDetail) {
            this.mOnLineMonitor.doLifeCycleCheck(activity, 5);
        }
        if (this.mOnLineMonitor != null) {
            this.mOnLineMonitor.onActivityDestroyed(activity);
            this.mOnLineMonitor.notifyOnActivityLifeCycleList(activity, 6);
        }
        if (OnLineMonitor.sIsTraceDetail) {
            this.mOnLineMonitor.mTraceDetail.mDestroyedActivityName = getActivityName(activity);
        }
        this.mActivityStackList.remove(activity.toString());
    }

    class MyOnPreDrawListener implements ViewTreeObserver.OnPreDrawListener {
        int mIndex;

        public MyOnPreDrawListener(int i) {
            this.mIndex = i;
        }

        public boolean onPreDraw() {
            if (ActivityLifecycleCallback.this.mCreateIndex != this.mIndex) {
                return true;
            }
            long nanoTime = System.nanoTime() / 1000000;
            if (ActivityLifecycleCallback.this.mSmoothCalculate != null) {
                ActivityLifecycleCallback.this.mSmoothCalculate.onDraw(nanoTime);
            }
            return true;
        }
    }

    @SuppressLint({"NewApi"})
    class NewCallBack implements Window.Callback {
        Window.Callback mCallBack;

        public boolean dispatchFnKeyEvent(KeyEvent keyEvent) {
            return false;
        }

        public boolean onSearchRequested(SearchEvent searchEvent) {
            return false;
        }

        public NewCallBack(Window.Callback callback) {
            this.mCallBack = callback;
        }

        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            try {
                return ActivityLifecycleCallback.this.onDispatchTouchEvent(this.mCallBack, (MotionEvent) null, keyEvent);
            } catch (Throwable unused) {
                return false;
            }
        }

        public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
            return this.mCallBack.dispatchKeyShortcutEvent(keyEvent);
        }

        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            try {
                return ActivityLifecycleCallback.this.onDispatchTouchEvent(this.mCallBack, motionEvent, (KeyEvent) null);
            } catch (Throwable unused) {
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

        public ActionMode onWindowStartingActionMode(ActionMode.Callback callback, int i) {
            return this.mCallBack.onWindowStartingActionMode(callback, i);
        }

        public void onActionModeStarted(ActionMode actionMode) {
            this.mCallBack.onActionModeStarted(actionMode);
        }

        public void onActionModeFinished(ActionMode actionMode) {
            this.mCallBack.onActionModeFinished(actionMode);
        }
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        MyGestureDetector() {
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            if (!ActivityLifecycleCallback.this.mHasMoved) {
                ActivityLifecycleCallback.this.mHasMoved = true;
                if (Math.abs(f2) >= Math.abs(f)) {
                    if (f2 > 0.0f) {
                        ActivityLifecycleCallback.this.mMoveDirection = "U";
                    } else {
                        ActivityLifecycleCallback.this.mMoveDirection = "D";
                    }
                } else if (f > 0.0f) {
                    ActivityLifecycleCallback.this.mMoveDirection = "L";
                } else {
                    ActivityLifecycleCallback.this.mMoveDirection = "R";
                }
                ActivityLifecycleCallback.this.mMoveRelativeTime = (System.nanoTime() / 1000000) - ActivityLifecycleCallback.this.mActivityResumeTime;
                if (ActivityLifecycleCallback.this.mMoveRelativeTime < 0) {
                    ActivityLifecycleCallback.this.mMoveRelativeTime = 0;
                }
            }
            if (!OnLineMonitor.sIsDetailDebug) {
                return false;
            }
            Log.d("OnLineMonitor", "onScroll");
            return false;
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            ActivityLifecycleCallback.this.mHasMoved = true;
            ActivityLifecycleCallback.this.mHasFling = true;
            if (!(motionEvent == null || motionEvent2 == null)) {
                float y = motionEvent2.getY() - motionEvent.getY();
                float x = motionEvent2.getX() - motionEvent.getX();
                if (Math.abs(y) >= Math.abs(x)) {
                    if (y > 0.0f) {
                        ActivityLifecycleCallback.this.mMoveDirection = "U";
                    } else {
                        ActivityLifecycleCallback.this.mMoveDirection = "D";
                    }
                } else if (x > 0.0f) {
                    ActivityLifecycleCallback.this.mMoveDirection = "L";
                } else {
                    ActivityLifecycleCallback.this.mMoveDirection = "R";
                }
            }
            if (!OnLineMonitor.sIsDetailDebug) {
                return false;
            }
            Log.d("OnLineMonitor", "onFling");
            return false;
        }
    }
}
