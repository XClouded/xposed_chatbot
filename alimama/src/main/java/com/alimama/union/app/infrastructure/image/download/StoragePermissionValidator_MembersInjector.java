package com.alimama.union.app.infrastructure.image.download;

import com.alimama.union.app.infrastructure.permission.Permission;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class StoragePermissionValidator_MembersInjector implements MembersInjector<StoragePermissionValidator> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<Permission> permissionProvider;

    public StoragePermissionValidator_MembersInjector(Provider<Permission> provider) {
        this.permissionProvider = provider;
    }

    public static MembersInjector<StoragePermissionValidator> create(Provider<Permission> provider) {
        return new StoragePermissionValidator_MembersInjector(provider);
    }

    public void injectMembers(StoragePermissionValidator storagePermissionValidator) {
        if (storagePermissionValidator != null) {
            storagePermissionValidator.permission = this.permissionProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectPermission(StoragePermissionValidator storagePermissionValidator, Provider<Permission> provider) {
        storagePermissionValidator.permission = provider.get();
    }
}
