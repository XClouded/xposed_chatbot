package com.alimama.union.app.infrastructure.executor;

import androidx.annotation.NonNull;
import java.util.concurrent.Callable;

public interface ITaskExecutor {
    <T> void submit(@NonNull Callable<T> callable, TaskCallback<T> taskCallback);
}
