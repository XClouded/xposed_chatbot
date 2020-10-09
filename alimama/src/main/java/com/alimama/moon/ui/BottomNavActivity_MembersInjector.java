package com.alimama.moon.ui;

import alimama.com.unwrouter.UNWRouter;
import com.alimama.moon.eventbus.IEventBus;
import com.alimama.moon.update.UpdateCenter;
import com.alimama.union.app.aalogin.ILogin;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class BottomNavActivity_MembersInjector implements MembersInjector<BottomNavActivity> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<IEventBus> eventBusProvider;
    private final Provider<ILogin> loginProvider;
    private final Provider<UNWRouter> pageRouterProvider;
    private final Provider<UpdateCenter> updateCenterProvider;

    public BottomNavActivity_MembersInjector(Provider<UNWRouter> provider, Provider<ILogin> provider2, Provider<IEventBus> provider3, Provider<UpdateCenter> provider4) {
        this.pageRouterProvider = provider;
        this.loginProvider = provider2;
        this.eventBusProvider = provider3;
        this.updateCenterProvider = provider4;
    }

    public static MembersInjector<BottomNavActivity> create(Provider<UNWRouter> provider, Provider<ILogin> provider2, Provider<IEventBus> provider3, Provider<UpdateCenter> provider4) {
        return new BottomNavActivity_MembersInjector(provider, provider2, provider3, provider4);
    }

    public void injectMembers(BottomNavActivity bottomNavActivity) {
        if (bottomNavActivity != null) {
            bottomNavActivity.pageRouter = this.pageRouterProvider.get();
            bottomNavActivity.login = this.loginProvider.get();
            bottomNavActivity.eventBus = this.eventBusProvider.get();
            bottomNavActivity.login = this.loginProvider.get();
            bottomNavActivity.updateCenter = this.updateCenterProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectLogin(BottomNavActivity bottomNavActivity, Provider<ILogin> provider) {
        bottomNavActivity.login = provider.get();
    }

    public static void injectUpdateCenter(BottomNavActivity bottomNavActivity, Provider<UpdateCenter> provider) {
        bottomNavActivity.updateCenter = provider.get();
    }
}
