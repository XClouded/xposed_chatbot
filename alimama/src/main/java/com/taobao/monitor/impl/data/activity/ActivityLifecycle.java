package com.taobao.monitor.impl.data.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.UiThread;
import androidx.fragment.app.FragmentActivity;
import com.ali.ha.fulltrace.dump.DumpManager;
import com.ali.ha.fulltrace.event.BackgroundEvent;
import com.ali.ha.fulltrace.event.ForegroundEvent;
import com.taobao.android.nav.Nav;
import com.taobao.android.tlog.protocol.Constants;
import com.taobao.android.tlog.protocol.model.joint.point.BackgroundJointPoint;
import com.taobao.application.common.data.ActivityCountHelper;
import com.taobao.application.common.data.AppLaunchHelper;
import com.taobao.application.common.impl.ApmImpl;
import com.taobao.monitor.impl.common.APMContext;
import com.taobao.monitor.impl.common.DynamicConstants;
import com.taobao.monitor.impl.common.Global;
import com.taobao.monitor.impl.data.GlobalStats;
import com.taobao.monitor.impl.data.fragment.FragmentLifecycle;
import com.taobao.monitor.impl.logger.DataLoggerUtils;
import com.taobao.monitor.impl.processor.launcher.LauncherProcessor;
import com.taobao.monitor.impl.processor.pageload.ProcedureManagerSetter;
import com.taobao.monitor.impl.trace.ApplicationBackgroundChangedDispatcher;
import com.taobao.monitor.impl.trace.DispatcherManager;
import com.taobao.monitor.impl.trace.IDispatcher;
import com.taobao.monitor.impl.util.ActivityUtils;
import com.taobao.monitor.impl.util.TimeUtils;
import com.taobao.monitor.procedure.IProcedure;
import java.util.HashMap;
import java.util.Map;

@UiThread
@TargetApi(14)
public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {
    static final String TAG = "ActivityLifeCycle";
    private final ActivityCountHelper activityCountHelper = new ActivityCountHelper();
    private int aliveActivityCount = 0;
    private final Application.ActivityLifecycleCallbacks asyncCallbackGroup = ApmImpl.instance().getAsyncCallbackGroup();
    private final BackgroundForegroundEventImpl backgroundForegroundEventImpl = new BackgroundForegroundEventImpl();
    private int count;
    protected Map<Activity, IPageLoadLifeCycle> map = new HashMap();
    private final Application.ActivityLifecycleCallbacks uiCallbackGroup = ApmImpl.instance().getUiCallbackGroup();

    interface IPageLoadLifeCycle {
        void onActivityCreated(Activity activity, Map<String, Object> map);

        void onActivityDestroyed(Activity activity);

        void onActivityPaused(Activity activity);

        void onActivityResumed(Activity activity);

        void onActivityStarted(Activity activity);

        void onActivityStopped(Activity activity);
    }

    public ActivityLifecycle() {
        this.activityCountHelper.setActivityCount(this.aliveActivityCount);
    }

    public void onActivityCreated(Activity activity, Bundle bundle) {
        ActivityCountHelper activityCountHelper2 = this.activityCountHelper;
        int i = this.aliveActivityCount + 1;
        this.aliveActivityCount = i;
        activityCountHelper2.setActivityCount(i);
        if (this.map.get(activity) == null) {
            GlobalStats.createdPageCount++;
            GlobalStats.activityStatusManager.setFirst(ActivityUtils.getPageName(activity));
            Intent intent = activity.getIntent();
            String str = null;
            if (intent != null) {
                str = intent.getDataString();
            }
            ActivityDataCollector activityDataCollector = new ActivityDataCollector(activity, str);
            this.map.put(activity, activityDataCollector);
            activityDataCollector.onActivityCreated(activity, intent2Map(activity.getIntent()));
            if ((activity instanceof FragmentActivity) && DynamicConstants.needFragment) {
                ((FragmentActivity) activity).getSupportFragmentManager().registerFragmentLifecycleCallbacks(new FragmentLifecycle(activity, activityDataCollector), true);
            }
        }
        DataLoggerUtils.log(TAG, Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_CREATED, activity.getClass().getSimpleName());
        ApmImpl.instance().setTopActivity(activity);
        this.uiCallbackGroup.onActivityCreated(activity, bundle);
        this.asyncCallbackGroup.onActivityCreated(activity, bundle);
    }

    public void onActivityStarted(Activity activity) {
        IPageLoadLifeCycle iPageLoadLifeCycle = this.map.get(activity);
        DataLoggerUtils.log(TAG, Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_STARTED, activity.getClass().getSimpleName());
        this.count++;
        if (this.count == 1) {
            IDispatcher dispatcher = DispatcherManager.getDispatcher(APMContext.APPLICATION_BACKGROUND_CHANGED_DISPATCHER);
            if (dispatcher instanceof ApplicationBackgroundChangedDispatcher) {
                ((ApplicationBackgroundChangedDispatcher) dispatcher).backgroundChanged(0, TimeUtils.currentTimeMillis());
            }
            DataLoggerUtils.log(TAG, "background2Foreground");
            this.backgroundForegroundEventImpl.background2Foreground();
            DumpManager.getInstance().append(new ForegroundEvent());
        }
        GlobalStats.isBackground = false;
        if (iPageLoadLifeCycle != null) {
            iPageLoadLifeCycle.onActivityStarted(activity);
        }
        ApmImpl.instance().setTopActivity(activity);
        this.uiCallbackGroup.onActivityStarted(activity);
        this.asyncCallbackGroup.onActivityStarted(activity);
    }

    public void onActivityResumed(Activity activity) {
        DataLoggerUtils.log(TAG, Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_RESUMED, activity.getClass().getSimpleName());
        IPageLoadLifeCycle iPageLoadLifeCycle = this.map.get(activity);
        if (iPageLoadLifeCycle != null) {
            iPageLoadLifeCycle.onActivityResumed(activity);
        }
        ApmImpl.instance().setTopActivity(activity);
        this.uiCallbackGroup.onActivityResumed(activity);
        this.asyncCallbackGroup.onActivityResumed(activity);
    }

    public void onActivityPaused(Activity activity) {
        DataLoggerUtils.log(TAG, Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_PAUSED, activity.getClass().getSimpleName());
        IPageLoadLifeCycle iPageLoadLifeCycle = this.map.get(activity);
        if (iPageLoadLifeCycle != null) {
            iPageLoadLifeCycle.onActivityPaused(activity);
        }
        this.uiCallbackGroup.onActivityPaused(activity);
        this.asyncCallbackGroup.onActivityPaused(activity);
    }

    public void onActivityStopped(Activity activity) {
        DataLoggerUtils.log(TAG, Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_STOPPED, activity.getClass().getSimpleName());
        IPageLoadLifeCycle iPageLoadLifeCycle = this.map.get(activity);
        if (iPageLoadLifeCycle != null) {
            iPageLoadLifeCycle.onActivityStopped(activity);
        }
        this.count--;
        if (this.count == 0) {
            GlobalStats.isBackground = true;
            ProcedureManagerSetter.instance().setCurrentActivityProcedure((IProcedure) null);
            ProcedureManagerSetter.instance().setCurrentFragmentProcedure((IProcedure) null);
            IDispatcher dispatcher = DispatcherManager.getDispatcher(APMContext.APPLICATION_BACKGROUND_CHANGED_DISPATCHER);
            if (dispatcher instanceof ApplicationBackgroundChangedDispatcher) {
                ((ApplicationBackgroundChangedDispatcher) dispatcher).backgroundChanged(1, TimeUtils.currentTimeMillis());
            }
            DataLoggerUtils.log(TAG, "foreground2Background");
            DumpManager.getInstance().append(new BackgroundEvent());
            GlobalStats.lastValidPage = BackgroundJointPoint.TYPE;
            GlobalStats.lastValidTime = -1;
            this.backgroundForegroundEventImpl.foreground2Background();
            saveTopActivity(ActivityUtils.getPageName(activity));
            new AppLaunchHelper().setLaunchType(LauncherProcessor.launcherType);
        }
        this.uiCallbackGroup.onActivityStopped(activity);
        this.asyncCallbackGroup.onActivityStopped(activity);
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
        this.uiCallbackGroup.onActivitySaveInstanceState(activity, bundle);
        this.asyncCallbackGroup.onActivitySaveInstanceState(activity, bundle);
    }

    public void onActivityDestroyed(Activity activity) {
        DataLoggerUtils.log(TAG, Constants.AndroidJointPointKey.LIFECYCLE_KEY_ACTIVITY_DESTROYED, activity.getClass().getSimpleName());
        IPageLoadLifeCycle iPageLoadLifeCycle = this.map.get(activity);
        if (iPageLoadLifeCycle != null) {
            iPageLoadLifeCycle.onActivityDestroyed(activity);
        }
        this.map.remove(activity);
        if (this.count == 0) {
            saveTopActivity("");
            ApmImpl.instance().setTopActivity((Activity) null);
        }
        this.uiCallbackGroup.onActivityDestroyed(activity);
        this.asyncCallbackGroup.onActivityDestroyed(activity);
        ActivityCountHelper activityCountHelper2 = this.activityCountHelper;
        int i = this.aliveActivityCount - 1;
        this.aliveActivityCount = i;
        activityCountHelper2.setActivityCount(i);
    }

    private Map<String, Object> intent2Map(Intent intent) {
        HashMap hashMap = new HashMap();
        if (intent != null) {
            hashMap.put("schemaUrl", intent.getDataString());
            hashMap.put("navStartTime", Long.valueOf(intent.getLongExtra(Nav.NAV_TO_URL_START_TIME, -1)));
            hashMap.put("navStartActivityTime", Long.valueOf(intent.getLongExtra(Nav.NAV_START_ACTIVITY_TIME, -1)));
        }
        return hashMap;
    }

    private void saveTopActivity(final String str) {
        Global.instance().handler().post(new Runnable() {
            public void run() {
                SharedPreferences.Editor edit = Global.instance().context().getSharedPreferences("apm", 0).edit();
                edit.putString(com.taobao.monitor.impl.common.Constants.LAST_TOP_ACTIVITY, str);
                edit.commit();
            }
        });
    }
}
