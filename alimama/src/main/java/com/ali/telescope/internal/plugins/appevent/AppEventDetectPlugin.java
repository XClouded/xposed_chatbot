package com.ali.telescope.internal.plugins.appevent;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import com.ali.telescope.base.event.ActivityEvent;
import com.ali.telescope.base.event.AppEvent;
import com.ali.telescope.base.event.Event;
import com.ali.telescope.base.plugin.INameConverter;
import com.ali.telescope.base.plugin.ITelescopeContext;
import com.ali.telescope.base.plugin.Plugin;
import com.ali.telescope.base.report.IBeanReport;
import com.ali.telescope.data.PageGetter;
import com.ali.telescope.util.TelescopeLog;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import org.json.JSONObject;

public class AppEventDetectPlugin extends Plugin {
    private static final String TAG = "EventDetectPlugin";
    Application.ActivityLifecycleCallbacks mActivityLifecycleCallbacks = new Application.ActivityLifecycleCallbacks() {
        private HashMap<Activity, String> mActivityToCode = new HashMap<>();
        private HashMap<Activity, String> mActivityToName = new HashMap<>();
        private HashMap<Activity, Long> pendingCreateResult = new HashMap<>();
        private Set<Activity> startMonitored = new HashSet();

        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        }

        public void onActivityCreated(Activity activity, Bundle bundle) {
            AppEventDetectPlugin.this.mITelescopeContext.broadcastEvent(ActivityEvent.obtain(1, activity));
            this.pendingCreateResult.put(activity, Long.valueOf(System.currentTimeMillis()));
            String pageName = PageGetter.getPageName(activity, AppEventDetectPlugin.this.mINameConvert);
            String pageHashCode = PageGetter.getPageHashCode(activity);
            this.mActivityToName.put(activity, pageName);
            this.mActivityToCode.put(activity, pageHashCode);
        }

        public void onActivityStarted(Activity activity) {
            AppEventDetectPlugin.this.mITelescopeContext.broadcastEvent(ActivityEvent.obtain(2, activity));
            String str = this.mActivityToName.get(activity);
            String str2 = this.mActivityToCode.get(activity);
            if (this.pendingCreateResult.containsKey(activity)) {
                AppEventDetectPlugin.this.mIBeanReport.send(new ActivityEventBean(this.pendingCreateResult.get(activity).longValue(), str, str2, 1));
                this.pendingCreateResult.remove(activity);
            }
            AppEventDetectPlugin.this.mIBeanReport.send(new ActivityEventBean(System.currentTimeMillis(), str, str2, 2));
            this.startMonitored.add(activity);
            AppEventDetectPlugin.access$308(AppEventDetectPlugin.this);
            if (!AppEventDetectPlugin.this.mIsAppForegroundLastTime) {
                boolean unused = AppEventDetectPlugin.this.mIsAppForegroundLastTime = true;
                AppEventDetectPlugin.this.mITelescopeContext.broadcastEvent(AppEvent.obtain(2));
                AppEventDetectPlugin.this.mIBeanReport.send(new AppEventBean(2, System.currentTimeMillis()));
                TelescopeLog.d(AppEventDetectPlugin.TAG, "APP ENTER FOREGROUND");
            }
        }

        public void onActivityResumed(Activity activity) {
            AppEventDetectPlugin.this.mITelescopeContext.broadcastEvent(ActivityEvent.obtain(3, activity));
            AppEventDetectPlugin.this.mIBeanReport.send(new ActivityEventBean(System.currentTimeMillis(), this.mActivityToName.get(activity), this.mActivityToCode.get(activity), 3));
        }

        public void onActivityPaused(Activity activity) {
            AppEventDetectPlugin.this.mITelescopeContext.broadcastEvent(ActivityEvent.obtain(4, activity));
            AppEventDetectPlugin.this.mIBeanReport.send(new ActivityEventBean(System.currentTimeMillis(), this.mActivityToName.get(activity), this.mActivityToCode.get(activity), 4));
        }

        public void onActivityStopped(Activity activity) {
            AppEventDetectPlugin.this.mITelescopeContext.broadcastEvent(ActivityEvent.obtain(5, activity));
            AppEventDetectPlugin.this.mIBeanReport.send(new ActivityEventBean(System.currentTimeMillis(), this.mActivityToName.get(activity), this.mActivityToCode.get(activity), 5));
            if (this.startMonitored.contains(activity)) {
                AppEventDetectPlugin.access$310(AppEventDetectPlugin.this);
                this.startMonitored.remove(activity);
            }
            if (AppEventDetectPlugin.this.mLivePageCount <= 0) {
                int unused = AppEventDetectPlugin.this.mLivePageCount = 0;
                boolean unused2 = AppEventDetectPlugin.this.mIsAppForegroundLastTime = false;
                AppEventDetectPlugin.this.mITelescopeContext.broadcastEvent(AppEvent.obtain(1));
                AppEventDetectPlugin.this.mIBeanReport.send(new AppEventBean(1, System.currentTimeMillis()));
                TelescopeLog.d(AppEventDetectPlugin.TAG, "APP ENTER BACKGROUND");
            }
        }

        public void onActivityDestroyed(Activity activity) {
            AppEventDetectPlugin.this.mITelescopeContext.broadcastEvent(ActivityEvent.obtain(6, activity));
            AppEventDetectPlugin.this.mIBeanReport.send(new ActivityEventBean(System.currentTimeMillis(), this.mActivityToName.remove(activity), this.mActivityToCode.remove(activity), 6));
        }
    };
    private Application mApplication;
    /* access modifiers changed from: private */
    public IBeanReport mIBeanReport;
    /* access modifiers changed from: private */
    public INameConverter mINameConvert;
    /* access modifiers changed from: private */
    public ITelescopeContext mITelescopeContext;
    /* access modifiers changed from: private */
    public boolean mIsAppForegroundLastTime;
    /* access modifiers changed from: private */
    public int mLivePageCount;

    public void onEvent(int i, Event event) {
    }

    public void onPause(int i, int i2) {
    }

    public void onResume(int i, int i2) {
    }

    static /* synthetic */ int access$308(AppEventDetectPlugin appEventDetectPlugin) {
        int i = appEventDetectPlugin.mLivePageCount;
        appEventDetectPlugin.mLivePageCount = i + 1;
        return i;
    }

    static /* synthetic */ int access$310(AppEventDetectPlugin appEventDetectPlugin) {
        int i = appEventDetectPlugin.mLivePageCount;
        appEventDetectPlugin.mLivePageCount = i - 1;
        return i;
    }

    public void onCreate(Application application, ITelescopeContext iTelescopeContext, JSONObject jSONObject) {
        this.boundType = 0;
        this.mApplication = application;
        this.mITelescopeContext = iTelescopeContext;
        this.mIBeanReport = iTelescopeContext.getBeanReport();
        this.mINameConvert = iTelescopeContext.getNameConverter();
        registerActivityLifeCycle(application);
    }

    public void onDestroy() {
        this.mApplication.unregisterActivityLifecycleCallbacks(this.mActivityLifecycleCallbacks);
    }

    private void registerActivityLifeCycle(Application application) {
        application.registerActivityLifecycleCallbacks(this.mActivityLifecycleCallbacks);
    }
}
