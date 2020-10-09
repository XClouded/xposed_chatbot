package com.alimama.union.app.infrastructure.image.capture;

import com.alimama.union.app.infrastructure.image.save.IImageSaver;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class BitmapCapture_Factory implements Factory<BitmapCapture> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<IImageSaver> imageSaverProvider;

    public BitmapCapture_Factory(Provider<IImageSaver> provider) {
        this.imageSaverProvider = provider;
    }

    public BitmapCapture get() {
        return new BitmapCapture(this.imageSaverProvider.get());
    }

    public static Factory<BitmapCapture> create(Provider<IImageSaver> provider) {
        return new BitmapCapture_Factory(provider);
    }
}
