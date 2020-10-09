package com.alimama.union.app.infrastructure.permission;

import dagger.MembersInjector;
import javax.inject.Provider;

public final class WVPermissionPlugin_MembersInjector implements MembersInjector<WVPermissionPlugin> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<Permission> permissionProvider;

    public WVPermissionPlugin_MembersInjector(Provider<Permission> provider) {
        this.permissionProvider = provider;
    }

    public static MembersInjector<WVPermissionPlugin> create(Provider<Permission> provider) {
        return new WVPermissionPlugin_MembersInjector(provider);
    }

    public void injectMembers(WVPermissionPlugin wVPermissionPlugin) {
        if (wVPermissionPlugin != null) {
            wVPermissionPlugin.permission = this.permissionProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectPermission(WVPermissionPlugin wVPermissionPlugin, Provider<Permission> provider) {
        wVPermissionPlugin.permission = provider.get();
    }
}
