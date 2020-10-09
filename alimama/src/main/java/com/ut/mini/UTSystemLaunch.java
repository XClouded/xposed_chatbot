package com.ut.mini;

import android.content.Context;
import android.os.SystemClock;
import com.alibaba.analytics.AnalyticsMgr;
import com.alibaba.analytics.utils.AppInfoUtil;
import com.alibaba.analytics.utils.TaskExecutor;
import com.ut.mini.UTHitBuilders;

class UTSystemLaunch {
    private static volatile boolean bSend = false;

    UTSystemLaunch() {
    }

    static void sendBootTime(final Context context) {
        synchronized (UTSystemLaunch.class) {
            if (!bSend) {
                bSend = true;
                TaskExecutor.getInstance().submit(new Runnable() {
                    public void run() {
                        if (context != null && AppInfoUtil.isMainProcess(context)) {
                            UTSystemLaunch.send();
                            try {
                                Thread.sleep(1000);
                                AnalyticsMgr.dispatchSaveCacheDataToLocal();
                                Thread.sleep(2000);
                                AnalyticsMgr.dispatchLocalHits();
                            } catch (Exception unused) {
                            }
                        }
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public static void send() {
        long currentTimeMillis = System.currentTimeMillis() - SystemClock.elapsedRealtime();
        UTHitBuilders.UTCustomHitBuilder uTCustomHitBuilder = new UTHitBuilders.UTCustomHitBuilder("BootTime");
        uTCustomHitBuilder.setProperty("bootTime", "" + currentTimeMillis);
        UTAnalytics.getInstance().getDefaultTracker().send(uTCustomHitBuilder.build());
    }
}
