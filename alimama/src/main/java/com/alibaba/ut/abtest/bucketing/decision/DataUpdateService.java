package com.alibaba.ut.abtest.bucketing.decision;

import android.app.Activity;
import android.os.Bundle;
import com.alibaba.ut.abtest.internal.ABContext;
import com.alibaba.ut.abtest.internal.util.LogUtils;
import com.alibaba.ut.abtest.internal.util.TaskExecutor;
import com.ut.mini.module.appstatus.UTAppStatusCallbacks;
import com.ut.mini.module.appstatus.UTAppStatusRegHelper;

public class DataUpdateService implements UTAppStatusCallbacks {
    private static final int REQUEST_INTERVAL_DEBUG = 10000;
    private static final String TAG = "DataUpdateService";

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

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }

    protected DataUpdateService() {
    }

    public static void register() {
        try {
            UTAppStatusRegHelper.registerAppStatusCallbacks(new DataUpdateService());
        } catch (Throwable th) {
            LogUtils.logE(TAG, "注册触发更新失败", th);
        }
    }

    /* access modifiers changed from: protected */
    public void updateData() {
        if (ABContext.getInstance().getConfigService().isDataTriggerEnabled()) {
            TaskExecutor.executeBackground(new Runnable() {
                public void run() {
                    if (ABContext.getInstance().getConfigService().isSdkEnabled()) {
                        try {
                            if (System.currentTimeMillis() - ABContext.getInstance().getDecisionService().getLastRequestTimestamp() < (ABContext.getInstance().isDebugMode() ? 10000 : ABContext.getInstance().getConfigService().getRequestExperimentDataIntervalTime())) {
                                LogUtils.logD(DataUpdateService.TAG, "不满足数据更新检查条件，取消本次检查。");
                            } else {
                                ABContext.getInstance().getDecisionService().syncExperiments(false);
                            }
                        } catch (Exception e) {
                            LogUtils.logE(DataUpdateService.TAG, e.getMessage(), e);
                        }
                    }
                }
            });
        }
    }

    public void onSwitchBackground() {
        LogUtils.logD(TAG, "onSwitchBackground");
        try {
            ABContext.getInstance().getPushService().cancelSyncCrowd();
        } catch (Throwable th) {
            LogUtils.logE(TAG, th.getMessage(), th);
        }
    }

    public void onSwitchForeground() {
        LogUtils.logD(TAG, "onSwitchForeground");
        updateData();
    }
}
