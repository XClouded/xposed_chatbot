package com.taobao.android.compat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import com.taobao.android.compat.ApplicationCompat;
import com.taobao.android.tlog.protocol.Constants;
import com.taobao.android.utils.Debuggable;
import com.taobao.weex.el.parse.Operators;

@TargetApi(14)
/* compiled from: ApplicationCompat */
class ActivityLifecycleCallbacksWrapper implements Application.ActivityLifecycleCallbacks {
    private final ApplicationCompat.ActivityLifecycleCallbacksCompat mCompat;

    public void onActivityCreated(Activity activity, Bundle bundle) {
        if (Debuggable.isDebug()) {
            timeingCallbackMethod(this.mCompat, activity, bundle, Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_CREATED);
        } else {
            this.mCompat.onActivityCreated(activity, bundle);
        }
    }

    public void onActivityStarted(Activity activity) {
        if (Debuggable.isDebug()) {
            timeingCallbackMethod(this.mCompat, activity, (Bundle) null, Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_STARTED);
        } else {
            this.mCompat.onActivityStarted(activity);
        }
    }

    public void onActivityResumed(Activity activity) {
        if (Debuggable.isDebug()) {
            timeingCallbackMethod(this.mCompat, activity, (Bundle) null, Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_RESUMED);
        } else {
            this.mCompat.onActivityResumed(activity);
        }
    }

    public void onActivityPaused(Activity activity) {
        if (Debuggable.isDebug()) {
            timeingCallbackMethod(this.mCompat, activity, (Bundle) null, Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_PAUSED);
        } else {
            this.mCompat.onActivityPaused(activity);
        }
    }

    public void onActivityStopped(Activity activity) {
        if (Debuggable.isDebug()) {
            timeingCallbackMethod(this.mCompat, activity, (Bundle) null, Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_STOPPED);
        } else {
            this.mCompat.onActivityStopped(activity);
        }
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        if (Debuggable.isDebug()) {
            timeingCallbackMethod(this.mCompat, activity, bundle, Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_SAVEINSTANCESTATE);
        } else {
            this.mCompat.onActivitySaveInstanceState(activity, bundle);
        }
    }

    public void onActivityDestroyed(Activity activity) {
        if (Debuggable.isDebug()) {
            timeingCallbackMethod(this.mCompat, activity, (Bundle) null, Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_DESTROYED);
        } else {
            this.mCompat.onActivityDestroyed(activity);
        }
    }

    public int hashCode() {
        return this.mCompat.hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ActivityLifecycleCallbacksWrapper)) {
            return false;
        }
        return this.mCompat.equals(((ActivityLifecycleCallbacksWrapper) obj).mCompat);
    }

    ActivityLifecycleCallbacksWrapper(ApplicationCompat.ActivityLifecycleCallbacksCompat activityLifecycleCallbacksCompat) {
        this.mCompat = activityLifecycleCallbacksCompat;
    }

    private static void timeingCallbackMethod(ApplicationCompat.ActivityLifecycleCallbacksCompat activityLifecycleCallbacksCompat, Activity activity, Bundle bundle, String str) {
        long nanoTime = System.nanoTime();
        long threadCpuTimeNanos = Debug.threadCpuTimeNanos();
        if (Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_CREATED.equals(str)) {
            activityLifecycleCallbacksCompat.onActivityCreated(activity, bundle);
        } else if (Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_STARTED.equals(str)) {
            activityLifecycleCallbacksCompat.onActivityStarted(activity);
        } else if (Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_RESUMED.equals(str)) {
            activityLifecycleCallbacksCompat.onActivityResumed(activity);
        } else if (Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_PAUSED.equals(str)) {
            activityLifecycleCallbacksCompat.onActivityPaused(activity);
        } else if (Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_STOPPED.equals(str)) {
            activityLifecycleCallbacksCompat.onActivityStopped(activity);
        } else if (Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_DESTROYED.equals(str)) {
            activityLifecycleCallbacksCompat.onActivityDestroyed(activity);
        } else if (Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_SAVEINSTANCESTATE.equals(str)) {
            activityLifecycleCallbacksCompat.onActivitySaveInstanceState(activity, bundle);
        }
        Log.i("Coord", "LifeTiming - " + activityLifecycleCallbacksCompat.getClass().getName() + Operators.SPACE_STR + str + Operators.SPACE_STR + ((Debug.threadCpuTimeNanos() - threadCpuTimeNanos) / 1000000) + "ms (cpu) / " + ((System.nanoTime() - nanoTime) / 1000000) + "ms (real)");
    }
}
