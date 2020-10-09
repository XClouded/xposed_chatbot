package anet.channel.util;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import anet.channel.AwcnConfig;
import anet.channel.GlobalAppRuntimeInfo;
import anet.channel.thread.ThreadPoolExecutorFactory;
import com.taobao.android.tlog.protocol.model.joint.point.ForegroundJointPoint;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArraySet;

public class AppLifecycle {
    public static volatile long lastEnterBackgroundTime = 0;
    /* access modifiers changed from: private */
    public static CopyOnWriteArraySet<AppLifecycleListener> listeners = new CopyOnWriteArraySet<>();
    private static Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        public void onActivityCreated(Activity activity, Bundle bundle) {
        }

        public void onActivityDestroyed(Activity activity) {
        }

        public void onActivityPaused(Activity activity) {
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        public void onActivityStarted(Activity activity) {
        }

        public void onActivityStopped(Activity activity) {
        }

        public void onActivityResumed(Activity activity) {
            AppLifecycle.onForeground();
        }
    };
    private static ComponentCallbacks2 mComponentCallbacks2 = new ComponentCallbacks2() {
        public static final String TAG = "awcn.ComponentCallbacks2";

        public void onConfigurationChanged(Configuration configuration) {
        }

        public void onLowMemory() {
        }

        public void onTrimMemory(int i) {
            ALog.i(TAG, "onTrimMemory", (String) null, "level", Integer.valueOf(i));
            if (i == 20) {
                AppLifecycle.onBackground();
            }
        }
    };

    public interface AppLifecycleListener {
        void background();

        void forground();
    }

    private AppLifecycle() {
    }

    public static void initialize() {
        if (Build.VERSION.SDK_INT >= 14 && AwcnConfig.isAppLifeCycleListenerEnable()) {
            ((Application) GlobalAppRuntimeInfo.getContext().getApplicationContext()).registerActivityLifecycleCallbacks(mActivityLifecycleCallbacks);
            GlobalAppRuntimeInfo.getContext().registerComponentCallbacks(mComponentCallbacks2);
        }
    }

    public static void registerLifecycleListener(AppLifecycleListener appLifecycleListener) {
        if (appLifecycleListener != null) {
            listeners.add(appLifecycleListener);
        }
    }

    public static void unregisterLifecycleListener(AppLifecycleListener appLifecycleListener) {
        listeners.remove(appLifecycleListener);
    }

    public static void onForeground() {
        if (GlobalAppRuntimeInfo.isAppBackground()) {
            GlobalAppRuntimeInfo.setBackground(false);
            notifyListener(true);
        }
    }

    public static void onBackground() {
        if (!GlobalAppRuntimeInfo.isAppBackground()) {
            GlobalAppRuntimeInfo.setBackground(true);
            lastEnterBackgroundTime = System.currentTimeMillis();
            notifyListener(false);
        }
    }

    private static void notifyListener(final boolean z) {
        ALog.i("awcn.AppLifeCycle", "notifyListener", (String) null, ForegroundJointPoint.TYPE, Boolean.valueOf(z));
        ThreadPoolExecutorFactory.submitScheduledTask(new Runnable() {
            public void run() {
                Iterator it = AppLifecycle.listeners.iterator();
                while (it.hasNext()) {
                    AppLifecycleListener appLifecycleListener = (AppLifecycleListener) it.next();
                    if (z) {
                        appLifecycleListener.forground();
                    } else {
                        appLifecycleListener.background();
                    }
                }
            }
        });
    }
}
