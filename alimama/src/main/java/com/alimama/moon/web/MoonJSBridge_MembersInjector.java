package com.alimama.moon.web;

import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.messageCenter.model.AlertMessageRepository;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class MoonJSBridge_MembersInjector implements MembersInjector<MoonJSBridge> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<AlertMessageRepository> alertMessageRepositoryProvider;
    private final Provider<ILogin> loginProvider;

    public MoonJSBridge_MembersInjector(Provider<AlertMessageRepository> provider, Provider<ILogin> provider2) {
        this.alertMessageRepositoryProvider = provider;
        this.loginProvider = provider2;
    }

    public static MembersInjector<MoonJSBridge> create(Provider<AlertMessageRepository> provider, Provider<ILogin> provider2) {
        return new MoonJSBridge_MembersInjector(provider, provider2);
    }

    public void injectMembers(MoonJSBridge moonJSBridge) {
        if (moonJSBridge != null) {
            moonJSBridge.alertMessageRepository = this.alertMessageRepositoryProvider.get();
            moonJSBridge.login = this.loginProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectAlertMessageRepository(MoonJSBridge moonJSBridge, Provider<AlertMessageRepository> provider) {
        moonJSBridge.alertMessageRepository = provider.get();
    }

    public static void injectLogin(MoonJSBridge moonJSBridge, Provider<ILogin> provider) {
        moonJSBridge.login = provider.get();
    }
}
