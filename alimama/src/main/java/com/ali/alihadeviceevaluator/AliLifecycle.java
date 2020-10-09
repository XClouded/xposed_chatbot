package com.ali.alihadeviceevaluator;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;
import com.ali.alihadeviceevaluator.util.Global;

class AliLifecycle implements Application.ActivityLifecycleCallbacks {
    private static AliLifecycle lifecycle;
    private int mActivityCount = 0;
    private AliAIHardware mAliAIHaredware;

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public void onActivityDestroyed(Activity activity) {
    }

    public void onActivityPaused(Activity activity) {
    }

    public void onActivityResumed(Activity activity) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    private AliLifecycle(AliAIHardware aliAIHardware) {
        this.mAliAIHaredware = aliAIHardware;
    }

    static void registerLifeCycle(Application application, AliAIHardware aliAIHardware) {
        if (application != null) {
            if (lifecycle != null) {
                Log.e(Global.TAG, "register twice!!");
                return;
            }
            lifecycle = new AliLifecycle(aliAIHardware);
            application.registerActivityLifecycleCallbacks(lifecycle);
        }
    }

    public void onActivityStarted(Activity activity) {
        this.mActivityCount++;
        if (1 == this.mActivityCount) {
            AliHAHardware.getInstance().onAppForeGround();
            this.mAliAIHaredware.onForeGround();
        }
    }

    public void onActivityStopped(Activity activity) {
        this.mActivityCount--;
        if (this.mActivityCount == 0) {
            AliHAHardware.getInstance().onAppBackGround();
        }
    }
}
