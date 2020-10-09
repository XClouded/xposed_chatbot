package com.taobao.weex.analyzer.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import androidx.annotation.NonNull;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class LifecycleAwareUtil implements Application.ActivityLifecycleCallbacks {
    private static final String TAG = "LifecycleAwareUtil";
    private static LifecycleAwareUtil sInstance;
    private Runnable check;
    /* access modifiers changed from: private */
    public boolean foreground = false;
    private Handler handler = new Handler();
    /* access modifiers changed from: private */
    public List<Listener> listeners = new CopyOnWriteArrayList();
    /* access modifiers changed from: private */
    public boolean paused = true;

    public interface Listener {
        void onPageBackground();

        void onPageForeground();
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }

    public static LifecycleAwareUtil init(@NonNull Context context) {
        if (sInstance == null) {
            sInstance = new LifecycleAwareUtil();
            if (context instanceof Application) {
                ((Application) context).registerActivityLifecycleCallbacks(sInstance);
            } else if (context.getApplicationContext() instanceof Application) {
                ((Application) context.getApplicationContext()).registerActivityLifecycleCallbacks(sInstance);
            } else {
                throw new IllegalStateException("LifecycleAwareUtil is not initialised.[can't obtain application object]");
            }
        }
        return sInstance;
    }

    public static LifecycleAwareUtil get() {
        if (sInstance != null) {
            return sInstance;
        }
        throw new IllegalStateException("LifecycleAwareUtil is not initialised.");
    }

    public static void destroy(@NonNull Context context) {
        if (sInstance != null) {
            sInstance.removeAllListeners();
            if (context instanceof Application) {
                ((Application) context).unregisterActivityLifecycleCallbacks(sInstance);
            } else if (context.getApplicationContext() instanceof Application) {
                ((Application) context.getApplicationContext()).unregisterActivityLifecycleCallbacks(sInstance);
            }
            sInstance = null;
        }
    }

    public boolean isForeground() {
        return this.foreground;
    }

    public boolean isBackground() {
        return !this.foreground;
    }

    public void addListener(Listener listener) {
        this.listeners.add(listener);
    }

    public void removeListener(Listener listener) {
        this.listeners.remove(listener);
    }

    public void removeAllListeners() {
        this.listeners.clear();
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
        Log.d(TAG, "onActivityCreated," + activity.getClass().getSimpleName());
    }

    public void onActivityResumed(Activity activity) {
        Log.d(TAG, "onActivityResumed," + activity.getClass().getSimpleName());
        this.paused = false;
        boolean z = this.foreground ^ true;
        this.foreground = true;
        if (this.check != null) {
            this.handler.removeCallbacks(this.check);
        }
        if (z) {
            for (Listener onPageForeground : this.listeners) {
                try {
                    onPageForeground.onPageForeground();
                } catch (Exception e) {
                    Log.e(TAG, "Listener threw exception!", e);
                }
            }
        }
    }

    public void onActivityPaused(Activity activity) {
        Log.d(TAG, "onActivityPaused," + activity.getClass().getSimpleName());
        this.paused = true;
        if (this.check != null) {
            this.handler.removeCallbacks(this.check);
        }
        Handler handler2 = this.handler;
        AnonymousClass1 r0 = new Runnable() {
            public void run() {
                if (LifecycleAwareUtil.this.foreground && LifecycleAwareUtil.this.paused) {
                    boolean unused = LifecycleAwareUtil.this.foreground = false;
                    for (Listener onPageBackground : LifecycleAwareUtil.this.listeners) {
                        try {
                            onPageBackground.onPageBackground();
                        } catch (Exception e) {
                            Log.e(LifecycleAwareUtil.TAG, "Listener threw exception!", e);
                        }
                    }
                }
            }
        };
        this.check = r0;
        handler2.post(r0);
    }

    public void onActivityDestroyed(Activity activity) {
        Log.d(TAG, "onActivityDestroyed," + activity.getClass().getSimpleName());
    }
}
