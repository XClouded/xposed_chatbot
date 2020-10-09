package com.alimama.union.app.contact.model;

import com.alimama.moon.eventbus.IEventBus;
import com.alimama.union.app.infrastructure.permission.Permission;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class WVContactPlugin_MembersInjector implements MembersInjector<WVContactPlugin> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<ContactService> contactServiceProvider;
    private final Provider<IEventBus> eventBusProvider;
    private final Provider<Permission> permissionProvider;

    public WVContactPlugin_MembersInjector(Provider<Permission> provider, Provider<ContactService> provider2, Provider<IEventBus> provider3) {
        this.permissionProvider = provider;
        this.contactServiceProvider = provider2;
        this.eventBusProvider = provider3;
    }

    public static MembersInjector<WVContactPlugin> create(Provider<Permission> provider, Provider<ContactService> provider2, Provider<IEventBus> provider3) {
        return new WVContactPlugin_MembersInjector(provider, provider2, provider3);
    }

    public void injectMembers(WVContactPlugin wVContactPlugin) {
        if (wVContactPlugin != null) {
            wVContactPlugin.permission = this.permissionProvider.get();
            wVContactPlugin.contactService = this.contactServiceProvider.get();
            wVContactPlugin.eventBus = this.eventBusProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectPermission(WVContactPlugin wVContactPlugin, Provider<Permission> provider) {
        wVContactPlugin.permission = provider.get();
    }

    public static void injectContactService(WVContactPlugin wVContactPlugin, Provider<ContactService> provider) {
        wVContactPlugin.contactService = provider.get();
    }

    public static void injectEventBus(WVContactPlugin wVContactPlugin, Provider<IEventBus> provider) {
        wVContactPlugin.eventBus = provider.get();
    }
}
