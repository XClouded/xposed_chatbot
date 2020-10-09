package com.alimama.union.app.aalogin;

import android.content.Context;
import com.alimama.moon.eventbus.IEventBus;
import com.alimama.union.app.network.IWebService;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class TaobaoLogin_Factory implements Factory<TaobaoLogin> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<Context> appContextProvider;
    private final Provider<IEventBus> eventBusProvider;
    private final Provider<IWebService> webServiceProvider;

    public TaobaoLogin_Factory(Provider<Context> provider, Provider<IWebService> provider2, Provider<IEventBus> provider3) {
        this.appContextProvider = provider;
        this.webServiceProvider = provider2;
        this.eventBusProvider = provider3;
    }

    public TaobaoLogin get() {
        return new TaobaoLogin(this.appContextProvider.get(), this.webServiceProvider.get(), this.eventBusProvider.get());
    }

    public static Factory<TaobaoLogin> create(Provider<Context> provider, Provider<IWebService> provider2, Provider<IEventBus> provider3) {
        return new TaobaoLogin_Factory(provider, provider2, provider3);
    }
}
