package com.ali.user.mobile.utils;

import com.ali.user.mobile.log.TLogAdapter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BackgroundExecutor {
    private static Executor executor = Executors.newCachedThreadPool();

    public static void execute(Runnable runnable) {
        try {
            executor.execute(runnable);
        } catch (Exception e) {
            TLogAdapter.e("login.BackgroundExecutor", "BackgroundExecutor.excute failed.", e);
            e.printStackTrace();
        }
    }
}
