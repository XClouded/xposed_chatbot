package com.alimama.moon.di.module;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;

public final class AppModule_ProviderExecutorFactory implements Factory<Executor> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final AppModule module;

    public AppModule_ProviderExecutorFactory(AppModule appModule) {
        this.module = appModule;
    }

    public Executor get() {
        return (Executor) Preconditions.checkNotNull(this.module.providerExecutor(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Executor> create(AppModule appModule) {
        return new AppModule_ProviderExecutorFactory(appModule);
    }
}
