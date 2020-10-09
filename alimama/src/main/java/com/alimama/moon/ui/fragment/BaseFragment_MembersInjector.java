package com.alimama.moon.ui.fragment;

import com.alimama.moon.eventbus.IEventBus;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class BaseFragment_MembersInjector implements MembersInjector<BaseFragment> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<IEventBus> eventBusProvider;

    public BaseFragment_MembersInjector(Provider<IEventBus> provider) {
        this.eventBusProvider = provider;
    }

    public static MembersInjector<BaseFragment> create(Provider<IEventBus> provider) {
        return new BaseFragment_MembersInjector(provider);
    }

    public void injectMembers(BaseFragment baseFragment) {
        if (baseFragment != null) {
            baseFragment.eventBus = this.eventBusProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectEventBus(BaseFragment baseFragment, Provider<IEventBus> provider) {
        baseFragment.eventBus = provider.get();
    }
}
