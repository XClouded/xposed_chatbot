package com.alibaba.android.prefetchx;

import androidx.annotation.Nullable;
import com.alibaba.android.prefetchx.PFConstant;
import com.alibaba.android.prefetchx.PFMonitor;
import com.alibaba.android.prefetchx.adapter.IThreadExecutor;

class ThreadExecutorProxy implements IThreadExecutor {
    private IThreadExecutor executorImpl;

    static IThreadExecutor wrap(IThreadExecutor iThreadExecutor) {
        return new ThreadExecutorProxy(iThreadExecutor);
    }

    private ThreadExecutorProxy(IThreadExecutor iThreadExecutor) {
        this.executorImpl = iThreadExecutor;
    }

    public void executeImmediately(Runnable runnable) {
        if (runnable != null) {
            this.executorImpl.executeImmediately(new GuardedRunnable(runnable));
        }
    }

    public void executeWithDelay(Runnable runnable, int i) {
        if (runnable != null) {
            this.executorImpl.executeWithDelay(new GuardedRunnable(runnable), i);
        }
    }

    static class GuardedRunnable implements Runnable {
        private Runnable runnable;

        GuardedRunnable(@Nullable Runnable runnable2) {
            this.runnable = runnable2;
        }

        public void run() {
            if (this.runnable != null) {
                try {
                    this.runnable.run();
                } catch (Throwable th) {
                    PFLog.w("PrefetchX", "exception in report NOExceptionRunnable. what a mass!", th);
                    PFMonitor.JSModule.fail(PFConstant.JSModule.PF_JSMODULE_EXCEPTION, "error in NOExceptionRunnable_2", th.getMessage());
                }
            }
        }
    }
}
