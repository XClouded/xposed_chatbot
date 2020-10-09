package com.alimama.moon.di.module;

import android.content.Context;
import com.alimama.union.app.network.IWebService;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class AppModule_ProvideMtopServiceFactory implements Factory<IWebService> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<Context> contextProvider;
    private final AppModule module;

    public AppModule_ProvideMtopServiceFactory(AppModule appModule, Provider<Context> provider) {
        this.module = appModule;
        this.contextProvider = provider;
    }

    public IWebService get() {
        return (IWebService) Preconditions.checkNotNull(this.module.provideMtopService(this.contextProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<IWebService> create(AppModule appModule, Provider<Context> provider) {
        return new AppModule_ProvideMtopServiceFactory(appModule, provider);
    }
}
