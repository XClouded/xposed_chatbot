package com.alibaba.weex.plugin.gcanvas;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

final class GCanvasNativeGroup {
    private static volatile boolean sIsInitialized = false;
    /* access modifiers changed from: private */
    public static Handler sPostHandler = new Handler(Looper.getMainLooper());

    static native boolean createNativeViewGroup(int i);

    static native boolean tryDestroyGroup(int i);

    GCanvasNativeGroup() {
    }

    static class SharedGroupCleaner implements Runnable {
        private int key;

        public SharedGroupCleaner(int i) {
            this.key = i;
        }

        public void run() {
            if (GCanvasNativeGroup.tryDestroyGroup(this.key)) {
                GCanvasNativeGroup.sPostHandler.removeCallbacks(this);
            } else {
                GCanvasNativeGroup.sPostHandler.postDelayed(this, 8);
            }
        }
    }

    static void init(Context context) {
        if (context != null && !sIsInitialized) {
            ((Application) context.getApplicationContext()).registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                public void onActivityCreated(Activity activity, Bundle bundle) {
                }

                public void onActivityPaused(Activity activity) {
                }

                public void onActivityResumed(Activity activity) {
                }

                public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
                }

                public void onActivityStarted(Activity activity) {
                }

                public void onActivityStopped(Activity activity) {
                }

                public void onActivityDestroyed(Activity activity) {
                    if (!GCanvasNativeGroup.tryDestroyGroup(activity.hashCode())) {
                        GCanvasNativeGroup.sPostHandler.postDelayed(new SharedGroupCleaner(activity.hashCode()), 8);
                    }
                }
            });
            sIsInitialized = true;
        }
    }
}
