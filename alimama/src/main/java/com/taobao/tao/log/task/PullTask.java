package com.taobao.tao.log.task;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import com.taobao.tao.log.CommandDataCenter;
import com.taobao.tao.log.TLogInitializer;
import com.taobao.tao.log.message.MessageReponse;
import com.taobao.tao.log.message.SendMessage;
import com.taobao.tao.log.monitor.TLogStage;

public class PullTask {
    private static final int PULL_MSG = 0;
    /* access modifiers changed from: private */
    public static String TAG = "TLOG.PullTask";
    /* access modifiers changed from: private */
    public Handler mHandler;
    private HandlerThread mHandlerThread;

    private PullTask() {
    }

    private static class CreateInstance {
        /* access modifiers changed from: private */
        public static PullTask instance = new PullTask();

        private CreateInstance() {
        }
    }

    public static PullTask getInstance() {
        return CreateInstance.instance;
    }

    public void pull() {
        try {
            this.mHandler.sendEmptyMessage(0);
        } catch (Exception e) {
            Log.e(TAG, "pull task error", e);
        }
    }

    public void start() {
        this.mHandlerThread = new HandlerThread("tlog_pull_msg", 19);
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper()) {
            public void handleMessage(Message message) {
                if (message.what == 0) {
                    TLogInitializer.getInstance().gettLogMonitor().stageInfo(TLogStage.MSG_PULL, PullTask.TAG, "消息拉取：主动发送消息，拉取任务");
                    SendMessage.pull(TLogInitializer.getInstance().getContext());
                }
            }
        };
        this.mHandler.sendEmptyMessage(0);
        Application application = TLogInitializer.getInstance().getApplication();
        if (application == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= 14) {
            application.registerActivityLifecycleCallbacks(new AdapterActivityLifeCycle());
            return;
        }
        Log.w(TAG, String.format("build version %s not suppert, registerActivityLifecycleCallbacks failed", new Object[]{Integer.valueOf(Build.VERSION.SDK_INT)}));
    }

    public void handle(MessageReponse messageReponse) {
        CommandDataCenter.getInstance().onData(messageReponse.serviceId, messageReponse.userId, messageReponse.dataId, messageReponse.result.getBytes());
    }

    @TargetApi(14)
    private class AdapterActivityLifeCycle implements Application.ActivityLifecycleCallbacks {
        private int mActivitiesActive;
        private boolean mIsInForeground;

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

        private AdapterActivityLifeCycle() {
            this.mActivitiesActive = 0;
            this.mIsInForeground = false;
        }

        public void onActivityStarted(Activity activity) {
            this.mActivitiesActive++;
            if (!this.mIsInForeground) {
                PullTask.this.mHandler.sendEmptyMessage(0);
            }
            this.mIsInForeground = true;
        }

        public void onActivityStopped(Activity activity) {
            this.mActivitiesActive--;
            if (this.mActivitiesActive == 0) {
                this.mIsInForeground = false;
                PullTask.this.mHandler.sendEmptyMessage(0);
            }
        }
    }
}
