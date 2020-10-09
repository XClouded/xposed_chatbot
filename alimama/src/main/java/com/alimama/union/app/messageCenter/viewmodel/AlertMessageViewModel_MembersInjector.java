package com.alimama.union.app.messageCenter.viewmodel;

import com.alimama.union.app.messageCenter.model.AlertMessageRepository;
import dagger.MembersInjector;
import javax.inject.Provider;

public final class AlertMessageViewModel_MembersInjector implements MembersInjector<AlertMessageViewModel> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<AlertMessageRepository> alertMessageRepositoryProvider;

    public AlertMessageViewModel_MembersInjector(Provider<AlertMessageRepository> provider) {
        this.alertMessageRepositoryProvider = provider;
    }

    public static MembersInjector<AlertMessageViewModel> create(Provider<AlertMessageRepository> provider) {
        return new AlertMessageViewModel_MembersInjector(provider);
    }

    public void injectMembers(AlertMessageViewModel alertMessageViewModel) {
        if (alertMessageViewModel != null) {
            alertMessageViewModel.alertMessageRepository = this.alertMessageRepositoryProvider.get();
            return;
        }
        throw new NullPointerException("Cannot inject members into a null reference");
    }

    public static void injectAlertMessageRepository(AlertMessageViewModel alertMessageViewModel, Provider<AlertMessageRepository> provider) {
        alertMessageViewModel.alertMessageRepository = provider.get();
    }
}
