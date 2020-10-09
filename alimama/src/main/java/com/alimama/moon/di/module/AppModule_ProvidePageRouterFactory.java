package com.alimama.moon.di.module;

import alimama.com.unwrouter.UNWRouter;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class AppModule_ProvidePageRouterFactory implements Factory<UNWRouter> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final AppModule module;

    public AppModule_ProvidePageRouterFactory(AppModule appModule) {
        this.module = appModule;
    }

    public UNWRouter get() {
        return (UNWRouter) Preconditions.checkNotNull(this.module.providePageRouter(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<UNWRouter> create(AppModule appModule) {
        return new AppModule_ProvidePageRouterFactory(appModule);
    }
}
