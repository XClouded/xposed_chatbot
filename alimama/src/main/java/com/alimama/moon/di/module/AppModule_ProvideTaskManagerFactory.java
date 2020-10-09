package com.alimama.moon.di.module;

import com.alimama.union.app.infrastructure.executor.AsyncTaskManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class AppModule_ProvideTaskManagerFactory implements Factory<AsyncTaskManager> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final AppModule module;

    public AppModule_ProvideTaskManagerFactory(AppModule appModule) {
        this.module = appModule;
    }

    public AsyncTaskManager get() {
        return (AsyncTaskManager) Preconditions.checkNotNull(this.module.provideTaskManager(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<AsyncTaskManager> create(AppModule appModule) {
        return new AppModule_ProvideTaskManagerFactory(appModule);
    }
}
