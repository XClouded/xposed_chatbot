package com.alimama.union.app.personalCenter.viewmodel;

import com.alimama.union.app.aalogin.repository.UserRepository;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class MineViewModel_MembersInjector implements MembersInjector<MineViewModel> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<UserRepository> userRepositoryProvider;

    public MineViewModel_MembersInjector(Provider<UserRepository> provider) {
        this.userRepositoryProvider = provider;
    }

    public static MembersInjector<MineViewModel> create(Provider<UserRepository> provider) {
        return new MineViewModel_MembersInjector(provider);
    }

    public void injectMembers(MineViewModel mineViewModel) {
        if (mineViewModel != null) {
            mineViewModel.userRepository = this.userRepositoryProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectUserRepository(MineViewModel mineViewModel, Provider<UserRepository> provider) {
        mineViewModel.userRepository = provider.get();
    }
}
