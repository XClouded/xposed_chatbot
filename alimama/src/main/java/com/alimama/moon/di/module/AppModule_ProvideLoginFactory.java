package com.alimama.moon.di.module;

import android.content.Context;
import com.alimama.moon.eventbus.IEventBus;
import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.network.IWebService;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class AppModule_ProvideLoginFactory implements Factory<ILogin> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<Context> contextProvider;
    private final Provider<IEventBus> eventBusProvider;
    private final AppModule module;
    private final Provider<IWebService> webServiceProvider;

    public AppModule_ProvideLoginFactory(AppModule appModule, Provider<Context> provider, Provider<IWebService> provider2, Provider<IEventBus> provider3) {
        this.module = appModule;
        this.contextProvider = provider;
        this.webServiceProvider = provider2;
        this.eventBusProvider = provider3;
    }

    public ILogin get() {
        return (ILogin) Preconditions.checkNotNull(this.module.provideLogin(this.contextProvider.get(), this.webServiceProvider.get(), this.eventBusProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<ILogin> create(AppModule appModule, Provider<Context> provider, Provider<IWebService> provider2, Provider<IEventBus> provider3) {
        return new AppModule_ProvideLoginFactory(appModule, provider, provider2, provider3);
    }
}
