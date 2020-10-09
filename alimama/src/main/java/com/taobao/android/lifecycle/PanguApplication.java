package com.taobao.android.lifecycle;

import android.app.Activity;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.taobao.android.compat.ApplicationCompat;
import com.taobao.android.utils.Debuggable;
import com.taobao.weex.el.parse.Operators;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class PanguApplication extends ApplicationCompat {
    protected static final Handler mAppHandler = new Handler(Looper.getMainLooper());
    protected final AtomicInteger mCreationCount = new AtomicInteger();
    protected final List<CrossActivityLifecycleCallback> mCrossActivityLifecycleCallbacks = new CopyOnWriteArrayList();
    protected final AtomicInteger mStartCount = new AtomicInteger();
    protected WeakReference<Activity> mWeakActivity;

    public interface CrossActivityLifecycleCallback {
        void onCreated(Activity activity);

        void onDestroyed(Activity activity);

        void onStarted(Activity activity);

        void onStopped(Activity activity);
    }

    public void registerCrossActivityLifecycleCallback(CrossActivityLifecycleCallback crossActivityLifecycleCallback) {
        if (crossActivityLifecycleCallback == null) {
            RuntimeException runtimeException = new RuntimeException("registerCrossActivityLifecycleCallback must not be null");
            runtimeException.fillInStackTrace();
            Log.w("Pangu", "Called: " + this, runtimeException);
            return;
        }
        this.mCrossActivityLifecycleCallbacks.add(crossActivityLifecycleCallback);
        if (this.mCreationCount.get() > 0) {
            mAppHandler.post(new StickCrossRunnable(crossActivityLifecycleCallback, "onCreated"));
        }
        if (this.mStartCount.get() > 0) {
            mAppHandler.post(new StickCrossRunnable(crossActivityLifecycleCallback, "onStarted"));
        }
    }

    public void unregisterCrossActivityLifecycleCallback(CrossActivityLifecycleCallback crossActivityLifecycleCallback) {
        this.mCrossActivityLifecycleCallbacks.remove(crossActivityLifecycleCallback);
    }

    public static void runOnUiThread(Runnable runnable) {
        mAppHandler.post(runnable);
    }

    public void onCreate() {
        super.onCreate();
        Debuggable.init(this);
        registerActivityLifecycleCallbacks(new CrossActivityLifecycleCallbacks());
    }

    class StickCrossRunnable implements Runnable {
        private CrossActivityLifecycleCallback callback;
        private String method;

        public StickCrossRunnable(CrossActivityLifecycleCallback crossActivityLifecycleCallback, String str) {
            this.callback = crossActivityLifecycleCallback;
            this.method = str;
        }

        public void run() {
            Activity activity;
            if (!(PanguApplication.this.mWeakActivity == null || (activity = (Activity) PanguApplication.this.mWeakActivity.get()) == null || this.callback == null)) {
                if (Debuggable.isDebug()) {
                    PanguApplication.timeingCallbackMethod(this.callback, activity, this.method);
                } else if ("onCreated".equals(this.method)) {
                    this.callback.onCreated(activity);
                } else if ("onStarted".equals(this.method)) {
                    this.callback.onStarted(activity);
                }
            }
            this.callback = null;
            this.method = null;
        }
    }

    class CrossActivityLifecycleCallbacks implements ApplicationCompat.ActivityLifecycleCallbacksCompat {
        public void onActivityPaused(Activity activity) {
        }

        public void onActivityResumed(Activity activity) {
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        CrossActivityLifecycleCallbacks() {
        }

        public void onActivityCreated(Activity activity, Bundle bundle) {
            Log.d("TaobaoInitializer", "CrossActivityLifecycleCallbacks internal:" + PanguApplication.this.mCrossActivityLifecycleCallbacks.size() + Operators.SPACE_STR + PanguApplication.this.mCrossActivityLifecycleCallbacks.toString());
            PanguApplication.this.mWeakActivity = new WeakReference<>(activity);
            if (PanguApplication.this.mCreationCount.getAndIncrement() == 0 && !PanguApplication.this.mCrossActivityLifecycleCallbacks.isEmpty()) {
                for (CrossActivityLifecycleCallback next : PanguApplication.this.mCrossActivityLifecycleCallbacks) {
                    if (Debuggable.isDebug()) {
                        try {
                            PanguApplication.timeingCallbackMethod(next, activity, "onCreated");
                        } catch (Exception e) {
                            Log.e("TaobaoInitializer", next + "onCreated exception", e);
                        }
                    } else {
                        next.onCreated(activity);
                    }
                }
            }
        }

        public void onActivityStarted(Activity activity) {
            if (PanguApplication.this.mStartCount.getAndIncrement() == 0 && !PanguApplication.this.mCrossActivityLifecycleCallbacks.isEmpty()) {
                for (CrossActivityLifecycleCallback next : PanguApplication.this.mCrossActivityLifecycleCallbacks) {
                    if (Debuggable.isDebug()) {
                        PanguApplication.timeingCallbackMethod(next, activity, "onStarted");
                    } else {
                        next.onStarted(activity);
                    }
                }
            }
        }

        public void onActivityStopped(Activity activity) {
            if (PanguApplication.this.mStartCount.decrementAndGet() == 0 && !PanguApplication.this.mCrossActivityLifecycleCallbacks.isEmpty()) {
                for (CrossActivityLifecycleCallback next : PanguApplication.this.mCrossActivityLifecycleCallbacks) {
                    if (Debuggable.isDebug()) {
                        PanguApplication.timeingCallbackMethod(next, activity, "onStopped");
                    } else {
                        next.onStopped(activity);
                    }
                }
            }
        }

        public void onActivityDestroyed(Activity activity) {
            if (PanguApplication.this.mCreationCount.decrementAndGet() == 0 && !PanguApplication.this.mCrossActivityLifecycleCallbacks.isEmpty()) {
                for (CrossActivityLifecycleCallback next : PanguApplication.this.mCrossActivityLifecycleCallbacks) {
                    if (Debuggable.isDebug()) {
                        PanguApplication.timeingCallbackMethod(next, activity, "onDestroyed");
                    } else {
                        next.onDestroyed(activity);
                    }
                }
            }
        }
    }

    protected static void timeingCallbackMethod(CrossActivityLifecycleCallback crossActivityLifecycleCallback, Activity activity, String str) {
        long nanoTime = System.nanoTime();
        long threadCpuTimeNanos = Debug.threadCpuTimeNanos();
        if ("onCreated".equals(str)) {
            crossActivityLifecycleCallback.onCreated(activity);
        } else if ("onStarted".equals(str)) {
            crossActivityLifecycleCallback.onStarted(activity);
        } else if ("onStopped".equals(str)) {
            crossActivityLifecycleCallback.onStopped(activity);
        } else if ("onDestroyed".equals(str)) {
            crossActivityLifecycleCallback.onDestroyed(activity);
        }
        Log.i("Coord", "CrossLifeTiming - " + crossActivityLifecycleCallback.getClass().getName() + Operators.SPACE_STR + str + Operators.SPACE_STR + ((Debug.threadCpuTimeNanos() - threadCpuTimeNanos) / 1000000) + "ms (cpu) / " + ((System.nanoTime() - nanoTime) / 1000000) + "ms (real)");
    }
}
