package com.alimama.moon.ui;

import alimama.com.unwrouter.UNWRouter;
import com.alimama.moon.eventbus.IEventBus;
import com.alimama.union.app.aalogin.ILogin;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class BaseActivity_MembersInjector implements MembersInjector<BaseActivity> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<IEventBus> eventBusProvider;
    private final Provider<ILogin> loginProvider;
    private final Provider<UNWRouter> pageRouterProvider;

    public BaseActivity_MembersInjector(Provider<UNWRouter> provider, Provider<ILogin> provider2, Provider<IEventBus> provider3) {
        this.pageRouterProvider = provider;
        this.loginProvider = provider2;
        this.eventBusProvider = provider3;
    }

    public static MembersInjector<BaseActivity> create(Provider<UNWRouter> provider, Provider<ILogin> provider2, Provider<IEventBus> provider3) {
        return new BaseActivity_MembersInjector(provider, provider2, provider3);
    }

    public void injectMembers(BaseActivity baseActivity) {
        if (baseActivity != null) {
            baseActivity.pageRouter = this.pageRouterProvider.get();
            baseActivity.login = this.loginProvider.get();
            baseActivity.eventBus = this.eventBusProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectEventBus(BaseActivity baseActivity, Provider<IEventBus> provider) {
        baseActivity.eventBus = provider.get();
    }
}
