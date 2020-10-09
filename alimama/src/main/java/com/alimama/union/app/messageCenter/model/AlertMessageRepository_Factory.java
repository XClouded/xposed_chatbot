package com.alimama.union.app.messageCenter.model;

import android.content.Context;
import com.alimama.moon.AppDatabase;
import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.network.IWebService;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class AlertMessageRepository_Factory implements Factory<AlertMessageRepository> {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private final Provider<AppDatabase> appDatabaseProvider;
    private final Provider<Context> contextProvider;
    private final Provider<Executor> executorProvider;
    private final Provider<ILogin> loginProvider;
    private final Provider<IWebService> webServiceProvider;

    public AlertMessageRepository_Factory(Provider<Context> provider, Provider<AppDatabase> provider2, Provider<Executor> provider3, Provider<IWebService> provider4, Provider<ILogin> provider5) {
        this.contextProvider = provider;
        this.appDatabaseProvider = provider2;
        this.executorProvider = provider3;
        this.webServiceProvider = provider4;
        this.loginProvider = provider5;
    }

    public AlertMessageRepository get() {
        return new AlertMessageRepository(this.contextProvider.get(), this.appDatabaseProvider.get(), this.executorProvider.get(), this.webServiceProvider.get(), this.loginProvider.get());
    }

    public static Factory<AlertMessageRepository> create(Provider<Context> provider, Provider<AppDatabase> provider2, Provider<Executor> provider3, Provider<IWebService> provider4, Provider<ILogin> provider5) {
        return new AlertMessageRepository_Factory(provider, provider2, provider3, provider4, provider5);
    }
}
