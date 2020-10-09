package com.alimama.moon.web;

import alimama.com.unwrouter.UNWRouter;
import com.alimama.moon.eventbus.IEventBus;
import com.alimama.moon.ui.BaseActivity_MembersInjector;
import com.alimama.moon.ui.PageRouterActivity_MembersInjector;
import com.alimama.union.app.aalogin.ILogin;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class WebActivity_MembersInjector implements MembersInjector<WebActivity> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<IEventBus> eventBusProvider;
    private final Provider<ILogin> loginProvider;
    private final Provider<UNWRouter> pageRouterProvider;

    public WebActivity_MembersInjector(Provider<UNWRouter> provider, Provider<ILogin> provider2, Provider<IEventBus> provider3) {
        this.pageRouterProvider = provider;
        this.loginProvider = provider2;
        this.eventBusProvider = provider3;
    }

    public static MembersInjector<WebActivity> create(Provider<UNWRouter> provider, Provider<ILogin> provider2, Provider<IEventBus> provider3) {
        return new WebActivity_MembersInjector(provider, provider2, provider3);
    }

    public void injectMembers(WebActivity webActivity) {
        if (webActivity != null) {
            PageRouterActivity_MembersInjector.injectPageRouter(webActivity, this.pageRouterProvider);
            webActivity.login = this.loginProvider.get();
            BaseActivity_MembersInjector.injectEventBus(webActivity, this.eventBusProvider);
            webActivity.login = this.loginProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectLogin(WebActivity webActivity, Provider<ILogin> provider) {
        webActivity.login = provider.get();
    }
}
