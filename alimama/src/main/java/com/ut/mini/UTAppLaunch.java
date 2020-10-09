package com.ut.mini;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import com.alibaba.analytics.core.ClientVariables;
import com.alibaba.analytics.utils.AppInfoUtil;
import com.alibaba.analytics.utils.Logger;
import com.alibaba.analytics.utils.TaskExecutor;
import com.taobao.accs.data.Message;
import com.ut.mini.internal.UTOriginalCustomHitBuilder;
import com.ut.mini.module.appstatus.UTAppStatusCallbacks;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UTAppLaunch implements UTAppStatusCallbacks {
    private static final String IS_FIRST_TIME_LAUNCH = "_is_ft";
    private static final String IS_HOT_LAUNCH = "_is_hl";
    private static final String TAG = "UTAppLaunch";
    private static final String UT_DATABASE_NAME = "ut.db";
    private static boolean bCheckedFirstAppLaunch = false;
    private static boolean bEnable = true;
    /* access modifiers changed from: private */
    public static boolean bFirstAppLaunch = false;
    private static UTAppLaunch mInstance = new UTAppLaunch();
    private boolean bFirstSend = true;
    private boolean bMainProcess = false;

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

    public void onSwitchBackground() {
    }

    private UTAppLaunch() {
    }

    public static UTAppLaunch getInstance() {
        return mInstance;
    }

    public static void setEnable(boolean z) {
        bEnable = z;
    }

    static void checkFirstLaunch(Context context) {
        if (bEnable && !bCheckedFirstAppLaunch) {
            boolean z = true;
            bCheckedFirstAppLaunch = true;
            File databasePath = context.getDatabasePath("ut.db");
            if (databasePath != null && databasePath.exists()) {
                z = false;
            }
            bFirstAppLaunch = z;
        }
    }

    private void sendLaunch(Context context) {
        if (bEnable) {
            if (this.bFirstSend) {
                this.bMainProcess = AppInfoUtil.isMainProcess(context);
                sendFirstLaunch(context);
                this.bFirstSend = false;
            } else if (this.bMainProcess) {
                sendHotLaunch();
            }
        }
    }

    private void sendFirstLaunch(final Context context) {
        TaskExecutor.getInstance().submit(new Runnable() {
            public void run() {
                if (context != null) {
                    HashMap hashMap = new HashMap();
                    if (UTAppLaunch.bFirstAppLaunch) {
                        hashMap.put(UTAppLaunch.IS_FIRST_TIME_LAUNCH, "1");
                    } else {
                        hashMap.put(UTAppLaunch.IS_FIRST_TIME_LAUNCH, "0");
                    }
                    hashMap.put(UTAppLaunch.IS_HOT_LAUNCH, "0");
                    UTAppLaunch.this.send(hashMap);
                    Logger.d(UTAppLaunch.TAG, "sendAppLaunch _is_ft", Boolean.valueOf(UTAppLaunch.bFirstAppLaunch));
                }
            }
        });
    }

    private void sendHotLaunch() {
        HashMap hashMap = new HashMap();
        hashMap.put(IS_HOT_LAUNCH, "1");
        send(hashMap);
        Logger.d(TAG, "sendHotLaunch _is_hl", 1);
    }

    /* access modifiers changed from: private */
    public void send(Map<String, String> map) {
        UTAnalytics.getInstance().getDefaultTracker().send(new UTOriginalCustomHitBuilder("UT", Message.EXT_HEADER_VALUE_MAX_LEN, "/tracking.init.rdy", (String) null, (String) null, map).build());
    }

    public void onSwitchForeground() {
        sendLaunch(ClientVariables.getInstance().getContext());
    }
}
