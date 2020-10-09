package com.alimama.moon.update;

import android.content.Context;
import com.alimama.moon.eventbus.IEventBus;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class UpdateCenter_Factory implements Factory<UpdateCenter> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<Context> appContextProvider;
    private final Provider<IEventBus> eventBusProvider;

    public UpdateCenter_Factory(Provider<Context> provider, Provider<IEventBus> provider2) {
        this.appContextProvider = provider;
        this.eventBusProvider = provider2;
    }

    public UpdateCenter get() {
        return new UpdateCenter(this.appContextProvider.get(), this.eventBusProvider.get());
    }

    public static Factory<UpdateCenter> create(Provider<Context> provider, Provider<IEventBus> provider2) {
        return new UpdateCenter_Factory(provider, provider2);
    }
}
