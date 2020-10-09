package com.alibaba.android.prefetchx.adapter;

import com.alibaba.android.prefetchx.PFLog;
import com.alibaba.android.prefetchx.PFUtil;
import com.taobao.android.task.Coordinator;

public class DefaultThreadExecutorImpl implements IThreadExecutor {
    private static boolean isSKIExists = false;

    static {
        try {
            Class.forName("com.taobao.android.task.Coordinator");
        } catch (ClassNotFoundException unused) {
            PFLog.e("prefetchX", "[thread executor] ski sdk not found!");
        }
    }

    public void executeImmediately(Runnable runnable) {
        if (isSKIExists) {
            Coordinator.execute(newTaggedRunnable(runnable), 20);
        } else if (PFUtil.isDebug()) {
            throw new RuntimeException("ski sdk not found!");
        }
    }

    public void executeWithDelay(Runnable runnable, int i) {
        if (isSKIExists) {
            Coordinator.execute(newTaggedRunnable(runnable), 35, i);
        } else if (PFUtil.isDebug()) {
            throw new RuntimeException("ski sdk not found");
        }
    }

    private static Coordinator.TaggedRunnable newTaggedRunnable(Runnable runnable) {
        return new TaggedRunnable("PrefetchXTask", runnable);
    }

    static class TaggedRunnable extends Coordinator.TaggedRunnable {
        final Runnable mTask;

        TaggedRunnable(String str, Runnable runnable) {
            super(str);
            this.mTask = runnable;
        }

        public void run() {
            if (this.mTask != null) {
                this.mTask.run();
            }
        }
    }
}
