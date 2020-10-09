package com.alimama.union.app.infrastructure.executor;

import androidx.annotation.NonNull;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AsyncTaskManager implements ITaskExecutor {
    private final ExecutorService executorService;

    private static class SingletonHolder {
        /* access modifiers changed from: private */
        public static final AsyncTaskManager instance = new AsyncTaskManager();

        private SingletonHolder() {
        }
    }

    public static AsyncTaskManager getInstance() {
        return SingletonHolder.instance;
    }

    private AsyncTaskManager() {
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
    }

    public void execute(@NonNull Runnable runnable) {
        this.executorService.execute(runnable);
    }

    public <T> void submit(@NonNull Callable<T> callable, TaskCallback<T> taskCallback) {
        this.executorService.submit(new Callable(callable, taskCallback) {
            private final /* synthetic */ Callable f$0;
            private final /* synthetic */ TaskCallback f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            public final Object call() {
                return AsyncTaskManager.lambda$submit$0(this.f$0, this.f$1);
            }
        });
    }

    static /* synthetic */ Object lambda$submit$0(Callable callable, TaskCallback taskCallback) throws Exception {
        Object obj;
        try {
            obj = callable.call();
            try {
                taskCallback.onSuccess(obj);
            } catch (Exception e) {
                e = e;
            }
        } catch (Exception e2) {
            e = e2;
            obj = null;
            taskCallback.onFailure(e);
            return obj;
        }
        return obj;
    }

    public ExecutorService getExecutorService() {
        return this.executorService;
    }
}
