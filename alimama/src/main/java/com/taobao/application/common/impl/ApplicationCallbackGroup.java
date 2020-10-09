package com.taobao.application.common.impl;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import java.util.ArrayList;
import java.util.Iterator;

@TargetApi(14)
class ApplicationCallbackGroup implements Application.ActivityLifecycleCallbacks, ICallbackGroup<Application.ActivityLifecycleCallbacks> {
    /* access modifiers changed from: private */
    public final ArrayList<Application.ActivityLifecycleCallbacks> callbackGroup = new ArrayList<>();

    ApplicationCallbackGroup() {
    }

    public void onActivityCreated(final Activity activity, final Bundle bundle) {
        innerRunnable(new Runnable() {
            public void run() {
                Iterator it = ApplicationCallbackGroup.this.callbackGroup.iterator();
                while (it.hasNext()) {
                    ((Application.ActivityLifecycleCallbacks) it.next()).onActivityCreated(activity, bundle);
                }
            }
        });
    }

    public void onActivityStarted(final Activity activity) {
        innerRunnable(new Runnable() {
            public void run() {
                Iterator it = ApplicationCallbackGroup.this.callbackGroup.iterator();
                while (it.hasNext()) {
                    ((Application.ActivityLifecycleCallbacks) it.next()).onActivityStarted(activity);
                }
            }
        });
    }

    public void onActivityResumed(final Activity activity) {
        innerRunnable(new Runnable() {
            public void run() {
                Iterator it = ApplicationCallbackGroup.this.callbackGroup.iterator();
                while (it.hasNext()) {
                    ((Application.ActivityLifecycleCallbacks) it.next()).onActivityResumed(activity);
                }
            }
        });
    }

    public void onActivityPaused(final Activity activity) {
        innerRunnable(new Runnable() {
            public void run() {
                Iterator it = ApplicationCallbackGroup.this.callbackGroup.iterator();
                while (it.hasNext()) {
                    ((Application.ActivityLifecycleCallbacks) it.next()).onActivityPaused(activity);
                }
            }
        });
    }

    public void onActivityStopped(final Activity activity) {
        innerRunnable(new Runnable() {
            public void run() {
                Iterator it = ApplicationCallbackGroup.this.callbackGroup.iterator();
                while (it.hasNext()) {
                    ((Application.ActivityLifecycleCallbacks) it.next()).onActivityStopped(activity);
                }
            }
        });
    }

    public void onActivitySaveInstanceState(final Activity activity, final Bundle bundle) {
        innerRunnable(new Runnable() {
            public void run() {
                Iterator it = ApplicationCallbackGroup.this.callbackGroup.iterator();
                while (it.hasNext()) {
                    ((Application.ActivityLifecycleCallbacks) it.next()).onActivitySaveInstanceState(activity, bundle);
                }
            }
        });
    }

    public void onActivityDestroyed(final Activity activity) {
        innerRunnable(new Runnable() {
            public void run() {
                Iterator it = ApplicationCallbackGroup.this.callbackGroup.iterator();
                while (it.hasNext()) {
                    ((Application.ActivityLifecycleCallbacks) it.next()).onActivityDestroyed(activity);
                }
            }
        });
    }

    public void addCallback(final Application.ActivityLifecycleCallbacks activityLifecycleCallbacks) {
        if (activityLifecycleCallbacks != null) {
            innerRunnable(new Runnable() {
                public void run() {
                    if (!ApplicationCallbackGroup.this.callbackGroup.contains(activityLifecycleCallbacks)) {
                        ApplicationCallbackGroup.this.callbackGroup.add(activityLifecycleCallbacks);
                    }
                }
            });
            return;
        }
        throw new IllegalArgumentException();
    }

    public void removeCallback(final Application.ActivityLifecycleCallbacks activityLifecycleCallbacks) {
        if (activityLifecycleCallbacks != null) {
            innerRunnable(new Runnable() {
                public void run() {
                    ApplicationCallbackGroup.this.callbackGroup.remove(activityLifecycleCallbacks);
                }
            });
            return;
        }
        throw new IllegalArgumentException();
    }

    private void innerRunnable(Runnable runnable) {
        ApmImpl.instance().secHandler(runnable);
    }
}
