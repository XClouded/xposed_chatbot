package com.alimama.moon.di.module;

import com.alimama.moon.eventbus.IEventBus;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class AppModule_ProvideEventBusFactory implements Factory<IEventBus> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final AppModule module;

    public AppModule_ProvideEventBusFactory(AppModule appModule) {
        this.module = appModule;
    }

    public IEventBus get() {
        return (IEventBus) Preconditions.checkNotNull(this.module.provideEventBus(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<IEventBus> create(AppModule appModule) {
        return new AppModule_ProvideEventBusFactory(appModule);
    }
}
