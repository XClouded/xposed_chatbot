package com.alimama.union.app.aalogin.view;

import alimama.com.unwrouter.UNWRouter;
import com.alimama.moon.eventbus.IEventBus;
import com.alimama.moon.ui.PageRouterActivity_MembersInjector;
import com.alimama.union.app.aalogin.ILogin;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class LoginChooserActivity_MembersInjector implements MembersInjector<LoginChooserActivity> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<IEventBus> eventBusProvider;
    private final Provider<ILogin> loginProvider;
    private final Provider<UNWRouter> pageRouterProvider;

    public LoginChooserActivity_MembersInjector(Provider<UNWRouter> provider, Provider<ILogin> provider2, Provider<IEventBus> provider3) {
        this.pageRouterProvider = provider;
        this.loginProvider = provider2;
        this.eventBusProvider = provider3;
    }

    public static MembersInjector<LoginChooserActivity> create(Provider<UNWRouter> provider, Provider<ILogin> provider2, Provider<IEventBus> provider3) {
        return new LoginChooserActivity_MembersInjector(provider, provider2, provider3);
    }

    public void injectMembers(LoginChooserActivity loginChooserActivity) {
        if (loginChooserActivity != null) {
            PageRouterActivity_MembersInjector.injectPageRouter(loginChooserActivity, this.pageRouterProvider);
            loginChooserActivity.login = this.loginProvider.get();
            loginChooserActivity.eventBus = this.eventBusProvider.get();
            loginChooserActivity.pageRouter = this.pageRouterProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectLogin(LoginChooserActivity loginChooserActivity, Provider<ILogin> provider) {
        loginChooserActivity.login = provider.get();
    }

    public static void injectEventBus(LoginChooserActivity loginChooserActivity, Provider<IEventBus> provider) {
        loginChooserActivity.eventBus = provider.get();
    }

    public static void injectPageRouter(LoginChooserActivity loginChooserActivity, Provider<UNWRouter> provider) {
        loginChooserActivity.pageRouter = provider.get();
    }
}
