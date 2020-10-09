package com.alimama.moon.di.module;

import com.alimama.union.app.network.IWebService;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class AppModule_ProvideMockServiceFactory implements Factory<IWebService> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final AppModule module;

    public AppModule_ProvideMockServiceFactory(AppModule appModule) {
        this.module = appModule;
    }

    public IWebService get() {
        return (IWebService) Preconditions.checkNotNull(this.module.provideMockService(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<IWebService> create(AppModule appModule) {
        return new AppModule_ProvideMockServiceFactory(appModule);
    }
}
