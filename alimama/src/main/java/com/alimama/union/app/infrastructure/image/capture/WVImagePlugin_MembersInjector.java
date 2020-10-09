package com.alimama.union.app.infrastructure.image.capture;

import com.alimama.moon.eventbus.IEventBus;
import com.alimama.union.app.infrastructure.permission.Permission;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class WVImagePlugin_MembersInjector implements MembersInjector<WVImagePlugin> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<Capture> captureProvider;
    private final Provider<IEventBus> eventBusProvider;
    private final Provider<Permission> permissionProvider;

    public WVImagePlugin_MembersInjector(Provider<Capture> provider, Provider<Permission> provider2, Provider<IEventBus> provider3) {
        this.captureProvider = provider;
        this.permissionProvider = provider2;
        this.eventBusProvider = provider3;
    }

    public static MembersInjector<WVImagePlugin> create(Provider<Capture> provider, Provider<Permission> provider2, Provider<IEventBus> provider3) {
        return new WVImagePlugin_MembersInjector(provider, provider2, provider3);
    }

    public void injectMembers(WVImagePlugin wVImagePlugin) {
        if (wVImagePlugin != null) {
            wVImagePlugin.capture = this.captureProvider.get();
            wVImagePlugin.permission = this.permissionProvider.get();
            wVImagePlugin.eventBus = this.eventBusProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectCapture(WVImagePlugin wVImagePlugin, Provider<Capture> provider) {
        wVImagePlugin.capture = provider.get();
    }

    public static void injectPermission(WVImagePlugin wVImagePlugin, Provider<Permission> provider) {
        wVImagePlugin.permission = provider.get();
    }

    public static void injectEventBus(WVImagePlugin wVImagePlugin, Provider<IEventBus> provider) {
        wVImagePlugin.eventBus = provider.get();
    }
}
