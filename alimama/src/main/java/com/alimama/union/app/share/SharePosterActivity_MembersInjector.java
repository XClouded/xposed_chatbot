package com.alimama.union.app.share;

import alimama.com.unwrouter.UNWRouter;
import com.alimama.moon.eventbus.IEventBus;
import com.alimama.moon.ui.BaseActivity_MembersInjector;
import com.alimama.moon.ui.PageRouterActivity_MembersInjector;
import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.infrastructure.permission.Permission;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class SharePosterActivity_MembersInjector implements MembersInjector<SharePosterActivity> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<IEventBus> eventBusProvider;
    private final Provider<ILogin> loginProvider;
    private final Provider<UNWRouter> pageRouterProvider;
    private final Provider<Permission> storagePermissionProvider;

    public SharePosterActivity_MembersInjector(Provider<UNWRouter> provider, Provider<ILogin> provider2, Provider<IEventBus> provider3, Provider<Permission> provider4) {
        this.pageRouterProvider = provider;
        this.loginProvider = provider2;
        this.eventBusProvider = provider3;
        this.storagePermissionProvider = provider4;
    }

    public static MembersInjector<SharePosterActivity> create(Provider<UNWRouter> provider, Provider<ILogin> provider2, Provider<IEventBus> provider3, Provider<Permission> provider4) {
        return new SharePosterActivity_MembersInjector(provider, provider2, provider3, provider4);
    }

    public void injectMembers(SharePosterActivity sharePosterActivity) {
        if (sharePosterActivity != null) {
            PageRouterActivity_MembersInjector.injectPageRouter(sharePosterActivity, this.pageRouterProvider);
            sharePosterActivity.login = this.loginProvider.get();
            BaseActivity_MembersInjector.injectEventBus(sharePosterActivity, this.eventBusProvider);
            sharePosterActivity.storagePermission = this.storagePermissionProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectStoragePermission(SharePosterActivity sharePosterActivity, Provider<Permission> provider) {
        sharePosterActivity.storagePermission = provider.get();
    }
}
