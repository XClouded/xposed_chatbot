package com.taobao.android.dinamic.dinamic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DinamicPreRenderThreadExecutor {
    private static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    public static void executor(Runnable runnable) {
        singleThreadExecutor.execute(runnable);
    }
}
