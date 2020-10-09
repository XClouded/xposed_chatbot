package com.taobao.android.dinamicx.thread;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import com.taobao.android.dinamicx.template.download.DXPriorityExecutor;
import com.taobao.android.dinamicx.template.download.DXPriorityRunnable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class DXRunnableManager {
    private static String MONITOR_THREAD = "monitor_thread";
    private static String RENDER_THREAD = "render_thread";
    private DXPriorityExecutor asyncPreRenderExecutor;
    private DXPriorityExecutor asyncRenderExecutor;
    private HandlerThread asyncRenderScheduledThread;
    private DXPriorityExecutor commonExecutor;
    private DXPriorityExecutor downloadExecutor;
    private Handler mainHandler;
    private Handler monitorHandler;
    private HandlerThread monitorHandlerTread;
    private ScheduledExecutorService scheduledExecutorService;

    private DXRunnableManager() {
        this.mainHandler = new Handler(Looper.getMainLooper());
        this.commonExecutor = new DXPriorityExecutor(true);
        this.downloadExecutor = new DXPriorityExecutor(true);
        this.scheduledExecutorService = new ScheduledThreadPoolExecutor(1);
        this.monitorHandlerTread = new HandlerThread(MONITOR_THREAD);
        this.monitorHandlerTread.start();
        this.monitorHandler = new Handler(this.monitorHandlerTread.getLooper());
        this.asyncRenderScheduledThread = new HandlerThread(RENDER_THREAD);
        this.asyncRenderScheduledThread.start();
        this.asyncRenderExecutor = new DXPriorityExecutor(1, true);
        this.asyncPreRenderExecutor = new DXPriorityExecutor(1, true);
    }

    private static class DXWorkHandlerHolder {
        /* access modifiers changed from: private */
        public static final DXRunnableManager INSTANCE = new DXRunnableManager();

        private DXWorkHandlerHolder() {
        }
    }

    public static DXRunnableManager getInstance() {
        return DXWorkHandlerHolder.INSTANCE;
    }

    public static void runOnUIThread(Runnable runnable) {
        getInstance().mainHandler.post(runnable);
    }

    public static void runOnWorkThread(Runnable runnable) {
        getInstance().commonExecutor.execute(runnable);
    }

    public static void runForDownLoad(DXDownLoadRunnable dXDownLoadRunnable) {
        getInstance().downloadExecutor.execute(dXDownLoadRunnable);
    }

    public static void runForMonitor(DXMonitorRunnable dXMonitorRunnable) {
        getInstance().monitorHandler.post(dXMonitorRunnable);
    }

    public static void runForPrefetch(DXPriorityRunnable dXPriorityRunnable) {
        getInstance().asyncRenderExecutor.execute(dXPriorityRunnable);
    }

    public static HandlerThread getAsyncRenderScheduledThread() {
        return getInstance().asyncRenderScheduledThread;
    }

    public static void clearAsyncRenderTasks() {
        getInstance().asyncRenderExecutor.clear();
    }

    public static void runForPreRender(DXPriorityRunnable dXPriorityRunnable) {
        getInstance().asyncPreRenderExecutor.execute(dXPriorityRunnable);
    }

    public static ScheduledExecutorService scheduledExecutorService() {
        return getInstance().scheduledExecutorService;
    }

    public static <Params, Progress, Result> void scheduledAsyncTask(AsyncTask<Params, Progress, Result> asyncTask, Params... paramsArr) {
        if (asyncTask != null) {
            asyncTask.executeOnExecutor(getInstance().commonExecutor, paramsArr);
        }
    }
}
