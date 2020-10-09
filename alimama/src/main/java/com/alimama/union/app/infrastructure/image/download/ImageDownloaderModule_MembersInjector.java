package com.alimama.union.app.infrastructure.image.download;

import com.alimama.moon.eventbus.IEventBus;
import com.alimama.union.app.infrastructure.permission.Permission;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class ImageDownloaderModule_MembersInjector implements MembersInjector<ImageDownloaderModule> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<IEventBus> eventBusProvider;
    private final Provider<Permission> permissionProvider;

    public ImageDownloaderModule_MembersInjector(Provider<Permission> provider, Provider<IEventBus> provider2) {
        this.permissionProvider = provider;
        this.eventBusProvider = provider2;
    }

    public static MembersInjector<ImageDownloaderModule> create(Provider<Permission> provider, Provider<IEventBus> provider2) {
        return new ImageDownloaderModule_MembersInjector(provider, provider2);
    }

    public void injectMembers(ImageDownloaderModule imageDownloaderModule) {
        if (imageDownloaderModule != null) {
            imageDownloaderModule.permission = this.permissionProvider.get();
            imageDownloaderModule.eventBus = this.eventBusProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectPermission(ImageDownloaderModule imageDownloaderModule, Provider<Permission> provider) {
        imageDownloaderModule.permission = provider.get();
    }

    public static void injectEventBus(ImageDownloaderModule imageDownloaderModule, Provider<IEventBus> provider) {
        imageDownloaderModule.eventBus = provider.get();
    }
}
