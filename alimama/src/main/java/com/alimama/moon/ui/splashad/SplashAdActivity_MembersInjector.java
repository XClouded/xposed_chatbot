package com.alimama.moon.ui.splashad;

import alimama.com.unwrouter.UNWRouter;
import com.alimama.moon.ui.PageRouterActivity_MembersInjector;
import com.alimama.union.app.aalogin.ILogin;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class SplashAdActivity_MembersInjector implements MembersInjector<SplashAdActivity> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<ILogin> loginProvider;
    private final Provider<UNWRouter> pageRouterProvider;

    public SplashAdActivity_MembersInjector(Provider<UNWRouter> provider, Provider<ILogin> provider2) {
        this.pageRouterProvider = provider;
        this.loginProvider = provider2;
    }

    public static MembersInjector<SplashAdActivity> create(Provider<UNWRouter> provider, Provider<ILogin> provider2) {
        return new SplashAdActivity_MembersInjector(provider, provider2);
    }

    public void injectMembers(SplashAdActivity splashAdActivity) {
        if (splashAdActivity != null) {
            PageRouterActivity_MembersInjector.injectPageRouter(splashAdActivity, this.pageRouterProvider);
            splashAdActivity.pageRouter = this.pageRouterProvider.get();
            splashAdActivity.login = this.loginProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectPageRouter(SplashAdActivity splashAdActivity, Provider<UNWRouter> provider) {
        splashAdActivity.pageRouter = provider.get();
    }

    public static void injectLogin(SplashAdActivity splashAdActivity, Provider<ILogin> provider) {
        splashAdActivity.login = provider.get();
    }
}
