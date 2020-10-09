package com.alimama.moon.di.module;

import android.content.Context;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class AppModule_ProvideContextFactory implements Factory<Context> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final AppModule module;

    public AppModule_ProvideContextFactory(AppModule appModule) {
        this.module = appModule;
    }

    public Context get() {
        return (Context) Preconditions.checkNotNull(this.module.provideContext(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Context> create(AppModule appModule) {
        return new AppModule_ProvideContextFactory(appModule);
    }
}
