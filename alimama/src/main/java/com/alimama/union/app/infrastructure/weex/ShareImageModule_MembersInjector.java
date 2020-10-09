package com.alimama.union.app.infrastructure.weex;

import com.alimama.union.app.infrastructure.permission.Permission;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class ShareImageModule_MembersInjector implements MembersInjector<ShareImageModule> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<Permission> permissionProvider;

    public ShareImageModule_MembersInjector(Provider<Permission> provider) {
        this.permissionProvider = provider;
    }

    public static MembersInjector<ShareImageModule> create(Provider<Permission> provider) {
        return new ShareImageModule_MembersInjector(provider);
    }

    public void injectMembers(ShareImageModule shareImageModule) {
        if (shareImageModule != null) {
            shareImageModule.permission = this.permissionProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectPermission(ShareImageModule shareImageModule, Provider<Permission> provider) {
        shareImageModule.permission = provider.get();
    }
}
