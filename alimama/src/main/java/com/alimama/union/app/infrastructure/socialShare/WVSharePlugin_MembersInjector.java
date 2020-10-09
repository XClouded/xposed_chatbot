package com.alimama.union.app.infrastructure.socialShare;

import com.alimama.union.app.infrastructure.permission.Permission;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class WVSharePlugin_MembersInjector implements MembersInjector<WVSharePlugin> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<Permission> permissionProvider;

    public WVSharePlugin_MembersInjector(Provider<Permission> provider) {
        this.permissionProvider = provider;
    }

    public static MembersInjector<WVSharePlugin> create(Provider<Permission> provider) {
        return new WVSharePlugin_MembersInjector(provider);
    }

    public void injectMembers(WVSharePlugin wVSharePlugin) {
        if (wVSharePlugin != null) {
            wVSharePlugin.permission = this.permissionProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectPermission(WVSharePlugin wVSharePlugin, Provider<Permission> provider) {
        wVSharePlugin.permission = provider.get();
    }
}
