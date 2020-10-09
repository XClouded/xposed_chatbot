package com.ali.telescope.internal.plugins.pageload;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.app.TabActivity;
import android.os.Bundle;
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
import com.ali.telescope.data.DeviceInfoManager;
import com.ali.telescope.util.TelescopeLog;
import java.util.ArrayList;

public class ActivityLifecycleCallback implements Application.ActivityLifecycleCallbacks {
    static final String TAG = "pageload@ActivityLifecycleCallback";
    volatile Activity mActivity;
    long mActivityOnCreate;
    long mActivityOnDestroy;
    long mActivityOnPaused;
    long mActivityOnResume;
    long mActivityOnStart;
    long mActivityOnStopped;
    ArrayList<String> mActivityStackList = new ArrayList<>();
    Application mApplication;
    short mBootActivityIndex = 0;
    int mCreateIndex;
    int mCreateUsedTime;
    volatile View mDecorView;
    int mEventCount;
    int mEventUsedTime;
    GestureDetector mGestureDetector;
    boolean mHasMoved;
    boolean mIsBootFinished;
    boolean mIsFirstMove = true;
    boolean mIsOnCreated;
    LoadTimeCalculate mLoadTimeCalculate;
    long mMaxDelayedTime;
    MyOnPreDrawListener mOnPreDrawListener;
    PageLoadMonitor mPageLoadMonitor;
    volatile short mStartCounter = 0;
    ViewTreeObserver mViewTreeObserver;

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public ActivityLifecycleCallback(Application application) {
        this.mApplication = application;
        this.mApplication.registerActivityLifecycleCallbacks(this);
    }

    @SuppressLint({"NewApi"})
    public void onActivityCreated(Activity activity, Bundle bundle) {
        this.mActivityOnCreate = System.nanoTime() / 1000000;
        if (this.mPageLoadMonitor != null) {
            this.mActivityStackList.add(activity.toString());
            if (!this.mIsBootFinished || TsAppStat.mIsInBackGround) {
                try {
                    this.mGestureDetector = new GestureDetector(this.mApplication, new MyGestureDetector());
                } catch (Throwable unused) {
                }
                if (TsAppStat.sFirstActivityTime < 0 || TsAppStat.mIsInBackGround) {
                    if (TsAppStat.sFirstActivityTime > 0) {
                        this.mBootActivityIndex = 0;
                        TsAppStat.sIsCodeBoot = false;
                        this.mPageLoadMonitor.mIsInBootStep = true;
                    }
                    TsAppStat.sFirstActivityTime = this.mActivityOnCreate;
                    if (!TsAppStat.sIsCodeBoot && this.mActivityOnCreate - TsAppStat.sLaunchTime <= ((long) this.mPageLoadMonitor.mColdBootOffsetTime)) {
                        TsAppStat.sIsCodeBoot = true;
                    }
                }
                if (this.mBootActivityIndex < TsAppStat.sBootAcitvityCount) {
                    String str = TsAppStat.sBootActivityAry[this.mBootActivityIndex];
                    if (str == null || !str.equals(activity.getClass().getName())) {
                        this.mIsBootFinished = true;
                        this.mPageLoadMonitor.mIsInBootStep = false;
                    } else {
                        this.mIsBootFinished = false;
                    }
                }
                this.mBootActivityIndex = (short) (this.mBootActivityIndex + 1);
                if (!this.mIsBootFinished && this.mBootActivityIndex == TsAppStat.sBootAcitvityCount) {
                    this.mIsBootFinished = true;
                }
            }
            this.mIsOnCreated = true;
            this.mPageLoadMonitor.onActivityCreate(activity);
            if (this.mLoadTimeCalculate != null) {
                this.mLoadTimeCalculate.onActivityCreated(activity);
            }
        }
    }

    @SuppressLint({"NewApi"})
    public void onActivityResumed(Activity activity) {
        this.mActivityOnResume = System.nanoTime() / 1000000;
        this.mActivity = activity;
        this.mDecorView = activity.getWindow().getDecorView().getRootView();
        if (this.mPageLoadMonitor != null) {
            this.mPageLoadMonitor.onActivityResume(activity);
        }
    }

    @SuppressLint({"NewApi"})
    public void onActivityStarted(Activity activity) {
        if (this.mPageLoadMonitor != null && this.mLoadTimeCalculate != null) {
            this.mActivityOnStart = System.nanoTime() / 1000000;
            if (this.mIsOnCreated) {
                this.mCreateUsedTime = (int) (this.mActivityOnStart - this.mActivityOnCreate);
            } else {
                this.mCreateUsedTime = 0;
            }
            this.mStartCounter = (short) (this.mStartCounter + 1);
            if (!(activity instanceof TabActivity)) {
                try {
                    this.mDecorView = activity.getWindow().getDecorView().getRootView();
                } catch (Throwable unused) {
                }
                if (this.mDecorView != null) {
                    this.mViewTreeObserver = this.mDecorView.getViewTreeObserver();
                    if (this.mViewTreeObserver != null && this.mViewTreeObserver.isAlive()) {
                        if (this.mPageLoadMonitor.mOnGlobalLayoutListener != null) {
                            if (DeviceInfoManager.instance().getApiLevel() >= 16) {
                                this.mViewTreeObserver.removeOnGlobalLayoutListener(this.mLoadTimeCalculate.mOnGlobalLayoutListener);
                                this.mViewTreeObserver.removeOnGlobalLayoutListener(this.mPageLoadMonitor.mOnGlobalLayoutListener);
                            } else {
                                this.mViewTreeObserver.removeGlobalOnLayoutListener(this.mLoadTimeCalculate.mOnGlobalLayoutListener);
                                this.mViewTreeObserver.removeGlobalOnLayoutListener(this.mPageLoadMonitor.mOnGlobalLayoutListener);
                            }
                            this.mViewTreeObserver.removeOnPreDrawListener(this.mOnPreDrawListener);
                        }
                        this.mCreateIndex++;
                        this.mPageLoadMonitor.mOnGlobalLayoutListener = this.mPageLoadMonitor.createOnGlobalLayoutListener(this.mCreateIndex);
                        this.mLoadTimeCalculate.mOnGlobalLayoutListener = this.mLoadTimeCalculate.createOnGlobalLayoutListener(this.mCreateIndex);
                        this.mViewTreeObserver.addOnGlobalLayoutListener(this.mLoadTimeCalculate.mOnGlobalLayoutListener);
                        this.mViewTreeObserver.addOnGlobalLayoutListener(this.mPageLoadMonitor.mOnGlobalLayoutListener);
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
                    this.mPageLoadMonitor.mPageStat.pageName = this.mPageLoadMonitor.getPageName(activity);
                    this.mPageLoadMonitor.mPageStat.pageHashCode = this.mPageLoadMonitor.getPageHashCode(activity);
                    if (this.mLoadTimeCalculate != null) {
                        this.mLoadTimeCalculate.onActivityStarted(activity, this.mDecorView);
                    }
                }
            }
        }
    }

    @SuppressLint({"NewApi"})
    public void onActivityPaused(Activity activity) {
        this.mActivityOnPaused = System.nanoTime() / 1000000;
        this.mIsOnCreated = false;
        if (this.mPageLoadMonitor != null) {
            this.mPageLoadMonitor.onActivityPause(activity);
        }
        if (this.mLoadTimeCalculate != null) {
            this.mLoadTimeCalculate.onActivityPaused(activity);
        }
        this.mActivity = null;
        this.mViewTreeObserver = null;
    }

    public void onActivityStopped(Activity activity) {
        this.mActivityOnStopped = System.nanoTime() / 1000000;
        this.mStartCounter = (short) (this.mStartCounter - 1);
        if (this.mStartCounter < 0) {
            this.mStartCounter = 0;
        }
        if (this.mPageLoadMonitor != null && this.mStartCounter == 0) {
            this.mDecorView = null;
        }
    }

    public void onActivityDestroyed(Activity activity) {
        this.mActivityOnDestroy = System.nanoTime() / 1000000;
        this.mActivityStackList.remove(activity.toString());
    }

    @SuppressLint({"NewApi"})
    public boolean onDispatchTouchEvent(Window.Callback callback, MotionEvent motionEvent, KeyEvent keyEvent) {
        boolean z = false;
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
        }
        if (motionEvent != null) {
            z = callback.dispatchTouchEvent(motionEvent);
        } else if (keyEvent != null) {
            z = callback.dispatchKeyEvent(keyEvent);
        }
        if (this.mPageLoadMonitor != null) {
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
        }
        return z;
    }

    class MyOnPreDrawListener implements ViewTreeObserver.OnPreDrawListener {
        int mIndex;

        public MyOnPreDrawListener(int i) {
            this.mIndex = i;
        }

        public boolean onPreDraw() {
            return ActivityLifecycleCallback.this.mCreateIndex != this.mIndex ? true : true;
        }
    }

    @SuppressLint({"NewApi"})
    class NewCallBack implements Window.Callback {
        Window.Callback mCallBack;

        public boolean onSearchRequested(SearchEvent searchEvent) {
            return false;
        }

        public NewCallBack(Window.Callback callback) {
            this.mCallBack = callback;
        }

        public boolean dispatchKeyEvent(KeyEvent keyEvent) {
            try {
                return ActivityLifecycleCallback.this.onDispatchTouchEvent(this.mCallBack, (MotionEvent) null, keyEvent);
            } catch (Throwable th) {
                TelescopeLog.e("pageload@ActivityLifecycleCallbackfindbug", Log.getStackTraceString(th));
                return false;
            }
        }

        public boolean dispatchKeyShortcutEvent(KeyEvent keyEvent) {
            return this.mCallBack.dispatchKeyShortcutEvent(keyEvent);
        }

        public boolean dispatchTouchEvent(MotionEvent motionEvent) {
            try {
                return ActivityLifecycleCallback.this.onDispatchTouchEvent(this.mCallBack, motionEvent, (KeyEvent) null);
            } catch (Throwable th) {
                TelescopeLog.e("pageload@ActivityLifecycleCallbackfindbug", Log.getStackTraceString(th));
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
            ActivityLifecycleCallback.this.mHasMoved = true;
            return false;
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            ActivityLifecycleCallback.this.mHasMoved = true;
            return false;
        }
    }
}
