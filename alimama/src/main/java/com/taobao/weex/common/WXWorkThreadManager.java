package com.taobao.weex.common;

import androidx.annotation.RestrictTo;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
public class WXWorkThreadManager {
    private ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    public void post(Runnable runnable) {
        if (this.singleThreadExecutor != null) {
            this.singleThreadExecutor.execute(runnable);
        }
    }

    public void destroy() {
        if (this.singleThreadExecutor != null) {
            this.singleThreadExecutor.shutdown();
        }
        this.singleThreadExecutor = null;
    }
}
