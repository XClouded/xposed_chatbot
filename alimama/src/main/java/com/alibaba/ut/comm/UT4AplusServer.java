package com.alibaba.ut.comm;

import android.app.Activity;
import com.alibaba.ut.comm.ActivityLifecycleCB;

public class UT4AplusServer {
    static UT4AplusServer instance = new UT4AplusServer();
    public final int MAX_PORT = 5999;
    public final int MIN_PORT = 5900;
    Thread mThread = null;

    public void init() {
    }

    public void start() {
    }

    public void stop() {
    }

    public static UT4AplusServer getInstance() {
        return instance;
    }

    class AppStatusMonitor implements ActivityLifecycleCB.ActivityResumedCallBack, ActivityLifecycleCB.ActivityPausedCallBack {
        public void onActivityPaused(Activity activity) {
        }

        public void onActivityResumed(Activity activity) {
        }

        AppStatusMonitor() {
        }
    }
}
