package alimama.com.unwbase.tools;

import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadUtils {
    private static ExecutorService mLogicExecutor = Executors.newFixedThreadPool(5);
    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
    private static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
    private static Handler uiHandler;

    public static void runInMain(Runnable runnable) {
        if (uiHandler == null) {
            uiHandler = new Handler(Looper.getMainLooper());
        }
        uiHandler.post(runnable);
    }

    public static boolean isMainThread() {
        return Thread.currentThread() == Looper.getMainLooper().getThread();
    }

    public static void runInBackByFixThread(Runnable runnable) {
        mLogicExecutor.execute(runnable);
    }

    public static void runInBackBySingleThread(Runnable runnable) {
        singleThreadExecutor.execute(runnable);
    }

    public static void runInBackByScheduleThread(Runnable runnable, long j) {
        scheduledExecutorService.schedule(runnable, j, TimeUnit.MILLISECONDS);
    }

    public static void runInIdleThread(final Runnable runnable) {
        Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
            public boolean queueIdle() {
                runnable.run();
                return false;
            }
        });
    }
}
