package com.alimama.moon.ui;

import alimama.com.unwrouter.UNWRouter;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class PageRouterActivity_MembersInjector implements MembersInjector<PageRouterActivity> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<UNWRouter> pageRouterProvider;

    public PageRouterActivity_MembersInjector(Provider<UNWRouter> provider) {
        this.pageRouterProvider = provider;
    }

    public static MembersInjector<PageRouterActivity> create(Provider<UNWRouter> provider) {
        return new PageRouterActivity_MembersInjector(provider);
    }

    public void injectMembers(PageRouterActivity pageRouterActivity) {
        if (pageRouterActivity != null) {
            pageRouterActivity.pageRouter = this.pageRouterProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectPageRouter(PageRouterActivity pageRouterActivity, Provider<UNWRouter> provider) {
        pageRouterActivity.pageRouter = provider.get();
    }
}
