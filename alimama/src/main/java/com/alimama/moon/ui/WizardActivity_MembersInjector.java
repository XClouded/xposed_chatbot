package com.alimama.moon.ui;

import alimama.com.unwrouter.UNWRouter;
import com.alimama.union.app.aalogin.ILogin;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class WizardActivity_MembersInjector implements MembersInjector<WizardActivity> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<ILogin> loginProvider;
    private final Provider<UNWRouter> pageRouterProvider;

    public WizardActivity_MembersInjector(Provider<UNWRouter> provider, Provider<ILogin> provider2) {
        this.pageRouterProvider = provider;
        this.loginProvider = provider2;
    }

    public static MembersInjector<WizardActivity> create(Provider<UNWRouter> provider, Provider<ILogin> provider2) {
        return new WizardActivity_MembersInjector(provider, provider2);
    }

    public void injectMembers(WizardActivity wizardActivity) {
        if (wizardActivity != null) {
            wizardActivity.pageRouter = this.pageRouterProvider.get();
            wizardActivity.login = this.loginProvider.get();
            wizardActivity.pageRouter = this.pageRouterProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectLogin(WizardActivity wizardActivity, Provider<ILogin> provider) {
        wizardActivity.login = provider.get();
    }

    public static void injectPageRouter(WizardActivity wizardActivity, Provider<UNWRouter> provider) {
        wizardActivity.pageRouter = provider.get();
    }
}
