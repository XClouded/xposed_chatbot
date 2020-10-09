package com.alimama.union.app.aalogin.repository;

import com.alimama.moon.AppDatabase;
import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.network.IWebService;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class UserRepository_Factory implements Factory<UserRepository> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<AppDatabase> appDatabaseProvider;
    private final Provider<Executor> executorProvider;
    private final Provider<ILogin> loginProvider;
    private final Provider<IWebService> webServiceProvider;

    public UserRepository_Factory(Provider<AppDatabase> provider, Provider<Executor> provider2, Provider<IWebService> provider3, Provider<ILogin> provider4) {
        this.appDatabaseProvider = provider;
        this.executorProvider = provider2;
        this.webServiceProvider = provider3;
        this.loginProvider = provider4;
    }

    public UserRepository get() {
        return new UserRepository(this.appDatabaseProvider.get(), this.executorProvider.get(), this.webServiceProvider.get(), this.loginProvider.get());
    }

    public static Factory<UserRepository> create(Provider<AppDatabase> provider, Provider<Executor> provider2, Provider<IWebService> provider3, Provider<ILogin> provider4) {
        return new UserRepository_Factory(provider, provider2, provider3, provider4);
    }
}
