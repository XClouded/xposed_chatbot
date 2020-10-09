package com.alimama.moon.di.module;

import com.alimama.union.app.infrastructure.image.save.IImageSaver;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class AppModule_ProvideImageSaverFactory implements Factory<IImageSaver> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final AppModule module;

    public AppModule_ProvideImageSaverFactory(AppModule appModule) {
        this.module = appModule;
    }

    public IImageSaver get() {
        return (IImageSaver) Preconditions.checkNotNull(this.module.provideImageSaver(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<IImageSaver> create(AppModule appModule) {
        return new AppModule_ProvideImageSaverFactory(appModule);
    }
}
