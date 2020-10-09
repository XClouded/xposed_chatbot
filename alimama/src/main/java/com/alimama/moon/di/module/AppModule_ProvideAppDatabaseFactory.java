package com.alimama.moon.di.module;

import com.alimama.moon.AppDatabase;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class AppModule_ProvideAppDatabaseFactory implements Factory<AppDatabase> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final AppModule module;

    public AppModule_ProvideAppDatabaseFactory(AppModule appModule) {
        this.module = appModule;
    }

    public AppDatabase get() {
        return (AppDatabase) Preconditions.checkNotNull(this.module.provideAppDatabase(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<AppDatabase> create(AppModule appModule) {
        return new AppModule_ProvideAppDatabaseFactory(appModule);
    }
}
