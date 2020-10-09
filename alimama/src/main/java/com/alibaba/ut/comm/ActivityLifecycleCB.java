package com.alibaba.ut.comm;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.alibaba.ut.utils.Logger;
import java.util.ArrayList;
import java.util.Iterator;

public class ActivityLifecycleCB {
    public static ActivityLifecycleCB instance = new ActivityLifecycleCB();
    public ArrayList<ActivityDestroyCallBack> mAdcbs = new ArrayList<>();
    public ArrayList<ActivityPausedCallBack> mApcbs = new ArrayList<>();
    public ArrayList<ActivityResumedCallBack> mArcbs = new ArrayList<>();

    public interface ActivityDestroyCallBack {
        void onActivityDestroyed(Activity activity);
    }

    public interface ActivityPausedCallBack {
        void onActivityPaused(Activity activity);
    }

    public interface ActivityResumedCallBack {
        void onActivityResumed(Activity activity);
    }

    public static ActivityLifecycleCB getInstance() {
        return instance;
    }

    public void init(Application application) {
        application.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            public void onActivityCreated(Activity activity, Bundle bundle) {
            }

            public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            }

            public void onActivityStarted(Activity activity) {
            }

            public void onActivityStopped(Activity activity) {
            }

            public void onActivityResumed(Activity activity) {
                Logger.e((String) null, "activity", activity);
                Iterator<ActivityResumedCallBack> it = ActivityLifecycleCB.this.mArcbs.iterator();
                while (it.hasNext()) {
                    it.next().onActivityResumed(activity);
                }
            }

            public void onActivityPaused(Activity activity) {
                Logger.e((String) null, "activity", activity);
                Iterator<ActivityPausedCallBack> it = ActivityLifecycleCB.this.mApcbs.iterator();
                while (it.hasNext()) {
                    it.next().onActivityPaused(activity);
                }
            }

            public void onActivityDestroyed(Activity activity) {
                Iterator<ActivityDestroyCallBack> it = ActivityLifecycleCB.this.mAdcbs.iterator();
                while (it.hasNext()) {
                    it.next().onActivityDestroyed(activity);
                }
            }
        });
    }

    public void addResumedCallback(ActivityResumedCallBack activityResumedCallBack) {
        if (!this.mArcbs.contains(activityResumedCallBack)) {
            this.mArcbs.add(activityResumedCallBack);
        }
    }

    public void addPauseCallback(ActivityPausedCallBack activityPausedCallBack) {
        if (!this.mApcbs.contains(activityPausedCallBack)) {
            this.mApcbs.add(activityPausedCallBack);
        }
    }

    public void addDestroyCallback(ActivityDestroyCallBack activityDestroyCallBack) {
        if (!this.mAdcbs.contains(activityDestroyCallBack)) {
            this.mAdcbs.add(activityDestroyCallBack);
        }
    }
}
