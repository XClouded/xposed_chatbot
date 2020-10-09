package com.alimama.union.app.network;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;

public final class MtopService_Factory implements Factory<MtopService> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<Context> appContextProvider;

    public MtopService_Factory(Provider<Context> provider) {
        this.appContextProvider = provider;
    }

    public MtopService get() {
        return new MtopService(this.appContextProvider.get());
    }

    public static Factory<MtopService> create(Provider<Context> provider) {
        return new MtopService_Factory(provider);
    }
}
