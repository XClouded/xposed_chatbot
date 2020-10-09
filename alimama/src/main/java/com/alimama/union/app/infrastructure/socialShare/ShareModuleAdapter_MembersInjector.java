package com.alimama.union.app.infrastructure.socialShare;

import com.alimama.union.app.infrastructure.permission.Permission;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class ShareModuleAdapter_MembersInjector implements MembersInjector<ShareModuleAdapter> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<Permission> permissionProvider;

    public ShareModuleAdapter_MembersInjector(Provider<Permission> provider) {
        this.permissionProvider = provider;
    }

    public static MembersInjector<ShareModuleAdapter> create(Provider<Permission> provider) {
        return new ShareModuleAdapter_MembersInjector(provider);
    }

    public void injectMembers(ShareModuleAdapter shareModuleAdapter) {
        if (shareModuleAdapter != null) {
            shareModuleAdapter.permission = this.permissionProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectPermission(ShareModuleAdapter shareModuleAdapter, Provider<Permission> provider) {
        shareModuleAdapter.permission = provider.get();
    }
}
