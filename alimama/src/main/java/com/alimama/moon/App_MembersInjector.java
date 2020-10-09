package com.alimama.moon;

import com.alimama.union.app.aalogin.ILogin;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class App_MembersInjector implements MembersInjector<App> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<ILogin> loginProvider;

    public App_MembersInjector(Provider<ILogin> provider) {
        this.loginProvider = provider;
    }

    public static MembersInjector<App> create(Provider<ILogin> provider) {
        return new App_MembersInjector(provider);
    }

    public void injectMembers(App app) {
        if (app != null) {
            app.login = this.loginProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectLogin(App app, Provider<ILogin> provider) {
        app.login = provider.get();
    }
}
