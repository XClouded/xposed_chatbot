package com.ali.alihadeviceevaluator.old;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {
    private HardWareInfo mHardWareInfo = null;
    private short mIsHardWareStatus = 0;

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public ActivityLifecycle(HardWareInfo hardWareInfo) {
        this.mHardWareInfo = hardWareInfo;
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (this.mIsHardWareStatus < 2 && this.mHardWareInfo != null && this.mHardWareInfo.mGpuName == null) {
            this.mHardWareInfo.getGpuInfo(activity);
            this.mIsHardWareStatus = (short) (this.mIsHardWareStatus + 1);
        }
    }

    public void onActivityStopped(Activity activity) {
        if (this.mIsHardWareStatus == 2 && this.mHardWareInfo != null && this.mHardWareInfo.mOnlineGLSurfaceView != null) {
            this.mHardWareInfo.destroy();
        }
    }
}
