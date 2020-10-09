package com.taobao.downloader.util;

import android.annotation.TargetApi;
import android.os.Process;
import com.taobao.downloader.Configuration;
import java.util.Timer;
import java.util.TimerTask;

public class ThreadUtil {
    @TargetApi(3)
    public static void execute(final Runnable runnable, boolean z) {
        if (Configuration.threadExecutor != null) {
            Configuration.threadExecutor.execute(runnable, z);
        } else {
            new Thread(new Runnable() {
                public void run() {
                    Process.setThreadPriority(10);
                    runnable.run();
                }
            }).start();
        }
    }

    public static void postDelayed(final Runnable runnable, long j) {
        if (Configuration.threadExecutor != null) {
            Configuration.threadExecutor.postDelayed(runnable, j);
        } else {
            new Timer("download-sdk").schedule(new TimerTask() {
                public void run() {
                    runnable.run();
                }
            }, j);
        }
    }
}
