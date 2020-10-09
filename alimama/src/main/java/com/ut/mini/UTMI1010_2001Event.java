package com.ut.mini;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import com.alibaba.analytics.AnalyticsMgr;
import com.alibaba.analytics.core.ClientVariables;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.TaskExecutor;
import com.ut.mini.internal.UTOriginalCustomHitBuilder;
import com.ut.mini.module.appstatus.UTAppStatusDelayCallbacks;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

class UTMI1010_2001Event implements UTAppStatusDelayCallbacks {
    private static UTMI1010_2001Event mInstance = new UTMI1010_2001Event();
    private long mHowLongForegroundStay = 0;
    private long mToBackgroundTimestamp = 0;
    private long mToForegroundTimestamp = 0;

    public void onActivityCreated(Activity activity, Bundle bundle) {
    }

    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    public void onActivityStarted(Activity activity) {
    }

    public void onActivityStopped(Activity activity) {
    }

    private UTMI1010_2001Event() {
    }

    public static UTMI1010_2001Event getInstance() {
        return mInstance;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0051  */
    /* JADX WARNING: Removed duplicated region for block: B:18:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onEventArrive(java.lang.Object r8) {
        /*
            r7 = this;
            java.util.Map r8 = (java.util.Map) r8
            com.alibaba.analytics.core.model.LogField r0 = com.alibaba.analytics.core.model.LogField.EVENTID
            java.lang.String r0 = r0.toString()
            boolean r0 = r8.containsKey(r0)
            if (r0 == 0) goto L_0x0058
            com.alibaba.analytics.core.model.LogField r0 = com.alibaba.analytics.core.model.LogField.EVENTID
            java.lang.String r0 = r0.toString()
            java.lang.Object r0 = r8.get(r0)
            java.lang.String r0 = (java.lang.String) r0
            java.lang.String r1 = "2001"
            boolean r0 = r1.equals(r0)
            if (r0 == 0) goto L_0x0058
            com.alibaba.analytics.core.model.LogField r0 = com.alibaba.analytics.core.model.LogField.ARG3
            java.lang.String r0 = r0.toString()
            boolean r0 = r8.containsKey(r0)
            r1 = 0
            if (r0 == 0) goto L_0x0045
            com.alibaba.analytics.core.model.LogField r0 = com.alibaba.analytics.core.model.LogField.ARG3
            java.lang.String r0 = r0.toString()
            java.lang.Object r8 = r8.get(r0)
            java.lang.String r8 = (java.lang.String) r8
            long r3 = java.lang.Long.parseLong(r8)     // Catch:{ Exception -> 0x0041 }
            goto L_0x0046
        L_0x0041:
            r8 = move-exception
            r8.printStackTrace()
        L_0x0045:
            r3 = r1
        L_0x0046:
            long r5 = r7.mHowLongForegroundStay
            long r5 = r5 + r3
            r7.mHowLongForegroundStay = r5
            boolean r8 = _isSwitchBackgroundByGetTask()
            if (r8 == 0) goto L_0x0058
            long r3 = r7.mHowLongForegroundStay
            r7._send1010Hit(r3)
            r7.mHowLongForegroundStay = r1
        L_0x0058:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTMI1010_2001Event.onEventArrive(java.lang.Object):void");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0028, code lost:
        r1 = r1.get(0).topActivity;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean _isSwitchBackgroundByGetTask() {
        /*
            r0 = 0
            com.alibaba.analytics.core.ClientVariables r1 = com.alibaba.analytics.core.ClientVariables.getInstance()     // Catch:{ Exception -> 0x0042 }
            android.content.Context r1 = r1.getContext()     // Catch:{ Exception -> 0x0042 }
            r2 = 1
            if (r1 == 0) goto L_0x0041
            java.lang.String r3 = r1.getPackageName()     // Catch:{ Exception -> 0x0042 }
            if (r3 == 0) goto L_0x0041
            java.lang.String r4 = "activity"
            java.lang.Object r1 = r1.getSystemService(r4)     // Catch:{ Exception -> 0x0042 }
            android.app.ActivityManager r1 = (android.app.ActivityManager) r1     // Catch:{ Exception -> 0x0042 }
            if (r1 == 0) goto L_0x0041
            java.util.List r1 = r1.getRunningTasks(r2)     // Catch:{ Exception -> 0x003d }
            if (r1 == 0) goto L_0x0041
            int r4 = r1.size()     // Catch:{ Exception -> 0x003d }
            if (r4 <= 0) goto L_0x0041
            java.lang.Object r1 = r1.get(r0)     // Catch:{ Exception -> 0x003d }
            android.app.ActivityManager$RunningTaskInfo r1 = (android.app.ActivityManager.RunningTaskInfo) r1     // Catch:{ Exception -> 0x003d }
            android.content.ComponentName r1 = r1.topActivity     // Catch:{ Exception -> 0x003d }
            if (r1 == 0) goto L_0x0041
            java.lang.String r1 = r1.getPackageName()     // Catch:{ Exception -> 0x003d }
            boolean r1 = r3.contains(r1)     // Catch:{ Exception -> 0x003d }
            if (r1 == 0) goto L_0x0041
            return r0
        L_0x003d:
            r1 = move-exception
            r1.printStackTrace()     // Catch:{ Exception -> 0x0042 }
        L_0x0041:
            return r2
        L_0x0042:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.ut.mini.UTMI1010_2001Event._isSwitchBackgroundByGetTask():boolean");
    }

    private void _send1010Hit(long j) {
        if (!ClientVariables.getInstance().is1010AutoTrackClosed()) {
            long j2 = 0;
            if (j > 0) {
                if (0 != this.mToBackgroundTimestamp) {
                    j2 = SystemClock.elapsedRealtime() - this.mToBackgroundTimestamp;
                }
                UTOriginalCustomHitBuilder uTOriginalCustomHitBuilder = new UTOriginalCustomHitBuilder("UT", 1010, "" + j, "" + j2, (String) null, (Map<String, String>) null);
                uTOriginalCustomHitBuilder.setProperty("_priority", "5");
                UTTracker defaultTracker = UTAnalytics.getInstance().getDefaultTracker();
                if (defaultTracker != null) {
                    defaultTracker.send(uTOriginalCustomHitBuilder.build());
                    return;
                }
                Logger.d("Record app display event error", "Fatal Error,must call setRequestAuthentication method first.");
            }
        }
    }

    public void onSwitchBackground() {
        UTPageHitHelper.getInstance().pageSwitchBackground();
    }

    public void onSwitchBackgroundDelay() {
        _send1010Hit(SystemClock.elapsedRealtime() - this.mToForegroundTimestamp);
        this.mToBackgroundTimestamp = SystemClock.elapsedRealtime();
        AnalyticsMgr.dispatchSaveCacheDataToLocal();
        TaskExecutor.getInstance().schedule((ScheduledFuture) null, new Runnable() {
            public void run() {
                AnalyticsMgr.dispatchLocalHits();
            }
        }, 2000);
    }

    public void onSwitchForeground() {
        this.mToForegroundTimestamp = SystemClock.elapsedRealtime();
    }

    public void onActivityDestroyed(Activity activity) {
        UTPageHitHelper.getInstance().pageDestroyed(activity);
    }

    public void onActivityPaused(Activity activity) {
        UTPageHitHelper.getInstance().pageDisAppearByAuto(activity);
    }

    public void onActivityResumed(Activity activity) {
        UTPageHitHelper.getInstance().pageAppearByAuto(activity);
    }
}
