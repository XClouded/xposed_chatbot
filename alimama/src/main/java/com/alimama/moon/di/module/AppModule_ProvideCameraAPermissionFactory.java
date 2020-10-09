package com.alimama.moon.di.module;

import com.alimama.union.app.infrastructure.permission.Permission;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class AppModule_ProvideCameraAPermissionFactory implements Factory<Permission> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final AppModule module;

    public AppModule_ProvideCameraAPermissionFactory(AppModule appModule) {
        this.module = appModule;
    }

    public Permission get() {
        return (Permission) Preconditions.checkNotNull(this.module.provideCameraAPermission(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Permission> create(AppModule appModule) {
        return new AppModule_ProvideCameraAPermissionFactory(appModule);
    }
}
