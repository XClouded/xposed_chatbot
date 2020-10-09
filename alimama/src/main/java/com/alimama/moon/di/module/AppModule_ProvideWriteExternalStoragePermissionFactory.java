package com.alimama.moon.di.module;

import com.alimama.union.app.infrastructure.permission.Permission;
import dagger.internal.Factory;
import dagger.internal.Preconditions;

public final class AppModule_ProvideWriteExternalStoragePermissionFactory implements Factory<Permission> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final AppModule module;

    public AppModule_ProvideWriteExternalStoragePermissionFactory(AppModule appModule) {
        this.module = appModule;
    }

    public Permission get() {
        return (Permission) Preconditions.checkNotNull(this.module.provideWriteExternalStoragePermission(), "Cannot return null from a non-@Nullable @Provides method");
    }

    public static Factory<Permission> create(AppModule appModule) {
        return new AppModule_ProvideWriteExternalStoragePermissionFactory(appModule);
    }
}
