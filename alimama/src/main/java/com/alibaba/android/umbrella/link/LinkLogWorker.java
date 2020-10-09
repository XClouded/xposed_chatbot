package com.alibaba.android.umbrella.link;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.Nullable;

class LinkLogWorker {
    private static final String TAG = "LinkLogWorker";
    private static final String UM_SDK_LINK_LOG = "UM_SDK_LINK_LOG";
    /* access modifiers changed from: private */
    @Nullable
    public Handler handler = null;

    LinkLogWorker() {
        new HandlerThread(UM_SDK_LINK_LOG, 10) {
            /* access modifiers changed from: protected */
            public void onLooperPrepared() {
                super.onLooperPrepared();
                Looper looper = getLooper();
                if (looper == null) {
                    Log.w(LinkLogWorker.TAG, "onLooperPrepared:  but looper == null, ");
                } else {
                    Handler unused = LinkLogWorker.this.handler = new Handler(looper);
                }
            }
        }.start();
    }

    /* access modifiers changed from: package-private */
    public void runNonBlocking(SafetyRunnable safetyRunnable) {
        if (this.handler == null) {
            Log.w(TAG, "submit:  but handler == null");
            safetyRunnable.run();
            return;
        }
        this.handler.post(safetyRunnable);
    }

    static abstract class SafetyRunnable implements Runnable {
        private String childBiz;
        private String errorCode;
        private String featureType;
        private String mainBiz;
        private String point;

        /* access modifiers changed from: package-private */
        public abstract void runSafety();

        SafetyRunnable() {
        }

        /* access modifiers changed from: package-private */
        public void fillExceptionArgs(String str, String str2, String str3, String str4, String str5) {
            this.point = str;
            this.mainBiz = str2;
            this.childBiz = str3;
            this.featureType = str4;
            this.errorCode = str5;
        }

        public void run() {
            try {
                runSafety();
            } catch (Throwable th) {
                AppMonitorAlarm.commitInnerException(th, this.point, this.mainBiz, this.childBiz, this.featureType, this.errorCode);
            }
        }
    }
}
