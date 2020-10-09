package com.alimama.union.app.personalCenter.view;

import com.alimama.moon.eventbus.IEventBus;
import com.alimama.moon.ui.fragment.BaseFragment_MembersInjector;
import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.infrastructure.permission.Permission;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class NewMineFragment_MembersInjector implements MembersInjector<NewMineFragment> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<IEventBus> eventBusProvider;
    private final Provider<ILogin> loginProvider;
    private final Provider<Permission> permissionProvider;

    public NewMineFragment_MembersInjector(Provider<IEventBus> provider, Provider<ILogin> provider2, Provider<Permission> provider3) {
        this.eventBusProvider = provider;
        this.loginProvider = provider2;
        this.permissionProvider = provider3;
    }

    public static MembersInjector<NewMineFragment> create(Provider<IEventBus> provider, Provider<ILogin> provider2, Provider<Permission> provider3) {
        return new NewMineFragment_MembersInjector(provider, provider2, provider3);
    }

    public void injectMembers(NewMineFragment newMineFragment) {
        if (newMineFragment != null) {
            BaseFragment_MembersInjector.injectEventBus(newMineFragment, this.eventBusProvider);
            newMineFragment.login = this.loginProvider.get();
            newMineFragment.permission = this.permissionProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectLogin(NewMineFragment newMineFragment, Provider<ILogin> provider) {
        newMineFragment.login = provider.get();
    }

    public static void injectPermission(NewMineFragment newMineFragment, Provider<Permission> provider) {
        newMineFragment.permission = provider.get();
    }
}
