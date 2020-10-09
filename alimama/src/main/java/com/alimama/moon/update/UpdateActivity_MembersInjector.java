package com.alimama.moon.update;

import alimama.com.unwrouter.UNWRouter;
import com.alimama.moon.ui.PageRouterActivity_MembersInjector;
import com.alimama.union.app.infrastructure.permission.Permission;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class UpdateActivity_MembersInjector implements MembersInjector<UpdateActivity> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<UNWRouter> pageRouterProvider;
    private final Provider<Permission> storagePermissionProvider;

    public UpdateActivity_MembersInjector(Provider<UNWRouter> provider, Provider<Permission> provider2) {
        this.pageRouterProvider = provider;
        this.storagePermissionProvider = provider2;
    }

    public static MembersInjector<UpdateActivity> create(Provider<UNWRouter> provider, Provider<Permission> provider2) {
        return new UpdateActivity_MembersInjector(provider, provider2);
    }

    public void injectMembers(UpdateActivity updateActivity) {
        if (updateActivity != null) {
            PageRouterActivity_MembersInjector.injectPageRouter(updateActivity, this.pageRouterProvider);
            updateActivity.storagePermission = this.storagePermissionProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectStoragePermission(UpdateActivity updateActivity, Provider<Permission> provider) {
        updateActivity.storagePermission = provider.get();
    }
}
