package com.alimama.moon.di.module;

import com.alimama.union.app.contact.model.ContactService;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class AppModule_ProvideContactServiceFactory implements Factory<ContactService> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final AppModule module;

    public AppModule_ProvideContactServiceFactory(AppModule appModule) {
        this.module = appModule;
    }

    public ContactService get() {
        return (ContactService) Preconditions.checkNotNull(this.module.provideContactService(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<ContactService> create(AppModule appModule) {
        return new AppModule_ProvideContactServiceFactory(appModule);
    }
}
