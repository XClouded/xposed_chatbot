package com.alimama.moon.update;

import com.alimama.union.app.infrastructure.permission.Permission;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class UpdateRemindDialog_MembersInjector implements MembersInjector<UpdateRemindDialog> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<Permission> storagePermissionProvider;

    public UpdateRemindDialog_MembersInjector(Provider<Permission> provider) {
        this.storagePermissionProvider = provider;
    }

    public static MembersInjector<UpdateRemindDialog> create(Provider<Permission> provider) {
        return new UpdateRemindDialog_MembersInjector(provider);
    }

    public void injectMembers(UpdateRemindDialog updateRemindDialog) {
        if (updateRemindDialog != null) {
            updateRemindDialog.storagePermission = this.storagePermissionProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectStoragePermission(UpdateRemindDialog updateRemindDialog, Provider<Permission> provider) {
        updateRemindDialog.storagePermission = provider.get();
    }
}
