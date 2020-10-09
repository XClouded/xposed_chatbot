package com.alimama.moon.ui;

import alimama.com.unwrouter.UNWRouter;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class MonkeyActivity_MembersInjector implements MembersInjector<MonkeyActivity> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<UNWRouter> pageRouterProvider;

    public MonkeyActivity_MembersInjector(Provider<UNWRouter> provider) {
        this.pageRouterProvider = provider;
    }

    public static MembersInjector<MonkeyActivity> create(Provider<UNWRouter> provider) {
        return new MonkeyActivity_MembersInjector(provider);
    }

    public void injectMembers(MonkeyActivity monkeyActivity) {
        if (monkeyActivity != null) {
            monkeyActivity.pageRouter = this.pageRouterProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }
}
