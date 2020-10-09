package com.alimama.moon.di.module;

import com.alimama.union.app.infrastructure.image.capture.Capture;
import com.alimama.union.app.infrastructure.image.save.IImageSaver;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;

public final class AppModule_ProvideCaptureFactory implements Factory<Capture> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<IImageSaver> imageSaverProvider;
    private final AppModule module;

    public AppModule_ProvideCaptureFactory(AppModule appModule, Provider<IImageSaver> provider) {
        this.module = appModule;
        this.imageSaverProvider = provider;
    }

    public Capture get() {
        return (Capture) Preconditions.checkNotNull(this.module.provideCapture(this.imageSaverProvider.get()), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Capture> create(AppModule appModule, Provider<IImageSaver> provider) {
        return new AppModule_ProvideCaptureFactory(appModule, provider);
    }
}
