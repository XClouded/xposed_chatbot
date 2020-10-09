package com.alimama.moon.di.component;

import alimama.com.unwrouter.UNWRouter;
import android.content.Context;
import com.alimama.moon.App;
import com.alimama.moon.AppDatabase;
import com.alimama.moon.App_MembersInjector;
import com.alimama.moon.di.module.AppModule;
import com.alimama.moon.di.module.AppModule_ProvideAppDatabaseFactory;
import com.alimama.moon.di.module.AppModule_ProvideCameraAPermissionFactory;
import com.alimama.moon.di.module.AppModule_ProvideCaptureFactory;
import com.alimama.moon.di.module.AppModule_ProvideContactServiceFactory;
import com.alimama.moon.di.module.AppModule_ProvideContextFactory;
import com.alimama.moon.di.module.AppModule_ProvideEventBusFactory;
import com.alimama.moon.di.module.AppModule_ProvideImageSaverFactory;
import com.alimama.moon.di.module.AppModule_ProvideLoginFactory;
import com.alimama.moon.di.module.AppModule_ProvideMtopServiceFactory;
import com.alimama.moon.di.module.AppModule_ProvidePageRouterFactory;
import com.alimama.moon.di.module.AppModule_ProvideReadContactPermissionFactory;
import com.alimama.moon.di.module.AppModule_ProvideWriteExternalStoragePermissionFactory;
import com.alimama.moon.di.module.AppModule_ProviderExecutorFactory;
import com.alimama.moon.eventbus.IEventBus;
import com.alimama.moon.ui.BaseActivity;
import com.alimama.moon.ui.BaseActivity_MembersInjector;
import com.alimama.moon.ui.BottomNavActivity;
import com.alimama.moon.ui.BottomNavActivity_MembersInjector;
import com.alimama.moon.ui.MonkeyActivity;
import com.alimama.moon.ui.MonkeyActivity_MembersInjector;
import com.alimama.moon.ui.PageRouterActivity;
import com.alimama.moon.ui.PageRouterActivity_MembersInjector;
import com.alimama.moon.ui.WizardActivity;
import com.alimama.moon.ui.WizardActivity_MembersInjector;
import com.alimama.moon.ui.fragment.BaseFragment;
import com.alimama.moon.ui.fragment.BaseFragment_MembersInjector;
import com.alimama.moon.ui.splashad.SplashAdActivity;
import com.alimama.moon.ui.splashad.SplashAdActivity_MembersInjector;
import com.alimama.moon.update.UpdateActivity;
import com.alimama.moon.update.UpdateActivity_MembersInjector;
import com.alimama.moon.update.UpdateCenter;
import com.alimama.moon.update.UpdateCenter_Factory;
import com.alimama.moon.update.UpdateRemindDialog;
import com.alimama.moon.update.UpdateRemindDialog_MembersInjector;
import com.alimama.moon.web.MoonJSBridge;
import com.alimama.moon.web.MoonJSBridge_MembersInjector;
import com.alimama.moon.web.WebActivity;
import com.alimama.moon.web.WebActivity_MembersInjector;
import com.alimama.union.app.aalogin.ILogin;
import com.alimama.union.app.aalogin.repository.UserRepository;
import com.alimama.union.app.aalogin.repository.UserRepository_Factory;
import com.alimama.union.app.aalogin.view.LoginChooserActivity;
import com.alimama.union.app.aalogin.view.LoginChooserActivity_MembersInjector;
import com.alimama.union.app.contact.model.ContactService;
import com.alimama.union.app.contact.model.WVContactPlugin;
import com.alimama.union.app.contact.model.WVContactPlugin_MembersInjector;
import com.alimama.union.app.infrastructure.image.capture.Capture;
import com.alimama.union.app.infrastructure.image.capture.WVImagePlugin;
import com.alimama.union.app.infrastructure.image.capture.WVImagePlugin_MembersInjector;
import com.alimama.union.app.infrastructure.image.download.ImageDownloaderModule;
import com.alimama.union.app.infrastructure.image.download.ImageDownloaderModule_MembersInjector;
import com.alimama.union.app.infrastructure.image.download.StoragePermissionValidator;
import com.alimama.union.app.infrastructure.image.download.StoragePermissionValidator_MembersInjector;
import com.alimama.union.app.infrastructure.image.save.IImageSaver;
import com.alimama.union.app.infrastructure.permission.Permission;
import com.alimama.union.app.infrastructure.permission.WVPermissionPlugin;
import com.alimama.union.app.infrastructure.permission.WVPermissionPlugin_MembersInjector;
import com.alimama.union.app.infrastructure.socialShare.ShareModuleAdapter;
import com.alimama.union.app.infrastructure.socialShare.ShareModuleAdapter_MembersInjector;
import com.alimama.union.app.infrastructure.socialShare.WVSharePlugin;
import com.alimama.union.app.infrastructure.socialShare.WVSharePlugin_MembersInjector;
import com.alimama.union.app.infrastructure.weex.ShareImageModule;
import com.alimama.union.app.infrastructure.weex.ShareImageModule_MembersInjector;
import com.alimama.union.app.messageCenter.model.AlertMessageRepository;
import com.alimama.union.app.messageCenter.model.AlertMessageRepository_Factory;
import com.alimama.union.app.messageCenter.viewmodel.AlertMessageViewModel;
import com.alimama.union.app.messageCenter.viewmodel.AlertMessageViewModel_MembersInjector;
import com.alimama.union.app.network.IWebService;
import com.alimama.union.app.personalCenter.view.NewMineFragment;
import com.alimama.union.app.personalCenter.view.NewMineFragment_MembersInjector;
import com.alimama.union.app.personalCenter.viewmodel.MineViewModel;
import com.alimama.union.app.personalCenter.viewmodel.MineViewModel_MembersInjector;
import com.alimama.union.app.rxnetwork.RxRequestManager;
import com.alimama.union.app.share.ShareActivity;
import com.alimama.union.app.share.ShareActivity_MembersInjector;
import com.alimama.union.app.share.SharePosterActivity;
import com.alimama.union.app.share.SharePosterActivity_MembersInjector;
import dagger.MembersInjector;
import dagger.internal.DoubleCheck;
import dagger.internal.MembersInjectors;
import dagger.internal.Preconditions;
import java.util.concurrent.Executor;
import javax.inject.Provider;

public final class DaggerAppComponent implements AppComponent {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private Provider<AlertMessageRepository> alertMessageRepositoryProvider;
    private MembersInjector<AlertMessageViewModel> alertMessageViewModelMembersInjector;
    private MembersInjector<App> appMembersInjector;
    private MembersInjector<BaseActivity> baseActivityMembersInjector;
    private MembersInjector<BaseFragment> baseFragmentMembersInjector;
    private MembersInjector<BottomNavActivity> bottomNavActivityMembersInjector;
    private MembersInjector<ImageDownloaderModule> imageDownloaderModuleMembersInjector;
    private MembersInjector<LoginChooserActivity> loginChooserActivityMembersInjector;
    private MembersInjector<MineViewModel> mineViewModelMembersInjector;
    private MembersInjector<MonkeyActivity> monkeyActivityMembersInjector;
    private MembersInjector<MoonJSBridge> moonJSBridgeMembersInjector;
    private MembersInjector<NewMineFragment> newMineFragmentMembersInjector;
    private MembersInjector<PageRouterActivity> pageRouterActivityMembersInjector;
    private Provider<AppDatabase> provideAppDatabaseProvider;
    private Provider<Permission> provideCameraAPermissionProvider;
    private Provider<Capture> provideCaptureProvider;
    private Provider<ContactService> provideContactServiceProvider;
    private Provider<Context> provideContextProvider;
    private Provider<IEventBus> provideEventBusProvider;
    private Provider<IImageSaver> provideImageSaverProvider;
    private Provider<ILogin> provideLoginProvider;
    private Provider<IWebService> provideMtopServiceProvider;
    private Provider<UNWRouter> providePageRouterProvider;
    private Provider<Permission> provideReadContactPermissionProvider;
    private Provider<Permission> provideWriteExternalStoragePermissionProvider;
    private Provider<Executor> providerExecutorProvider;
    private MembersInjector<ShareActivity> shareActivityMembersInjector;
    private MembersInjector<ShareImageModule> shareImageModuleMembersInjector;
    private MembersInjector<ShareModuleAdapter> shareModuleAdapterMembersInjector;
    private MembersInjector<SharePosterActivity> sharePosterActivityMembersInjector;
    private MembersInjector<SplashAdActivity> splashAdActivityMembersInjector;
    private MembersInjector<StoragePermissionValidator> storagePermissionValidatorMembersInjector;
    private MembersInjector<UpdateActivity> updateActivityMembersInjector;
    private Provider<UpdateCenter> updateCenterProvider;
    private MembersInjector<UpdateRemindDialog> updateRemindDialogMembersInjector;
    private Provider<UserRepository> userRepositoryProvider;
    private MembersInjector<WVContactPlugin> wVContactPluginMembersInjector;
    private MembersInjector<WVImagePlugin> wVImagePluginMembersInjector;
    private MembersInjector<WVPermissionPlugin> wVPermissionPluginMembersInjector;
    private MembersInjector<WVSharePlugin> wVSharePluginMembersInjector;
    private MembersInjector<WebActivity> webActivityMembersInjector;
    private MembersInjector<WizardActivity> wizardActivityMembersInjector;

    private DaggerAppComponent(Builder builder) {
        initialize(builder);
    }

    public static Builder builder() {
        return new Builder();
    }

    private void initialize(Builder builder) {
        this.provideContextProvider = DoubleCheck.provider(AppModule_ProvideContextFactory.create(builder.appModule));
        this.provideAppDatabaseProvider = DoubleCheck.provider(AppModule_ProvideAppDatabaseFactory.create(builder.appModule));
        this.providerExecutorProvider = DoubleCheck.provider(AppModule_ProviderExecutorFactory.create(builder.appModule));
        this.provideMtopServiceProvider = DoubleCheck.provider(AppModule_ProvideMtopServiceFactory.create(builder.appModule, this.provideContextProvider));
        this.provideEventBusProvider = DoubleCheck.provider(AppModule_ProvideEventBusFactory.create(builder.appModule));
        this.provideLoginProvider = DoubleCheck.provider(AppModule_ProvideLoginFactory.create(builder.appModule, this.provideContextProvider, this.provideMtopServiceProvider, this.provideEventBusProvider));
        this.alertMessageRepositoryProvider = DoubleCheck.provider(AlertMessageRepository_Factory.create(this.provideContextProvider, this.provideAppDatabaseProvider, this.providerExecutorProvider, this.provideMtopServiceProvider, this.provideLoginProvider));
        this.moonJSBridgeMembersInjector = MoonJSBridge_MembersInjector.create(this.alertMessageRepositoryProvider, this.provideLoginProvider);
        this.provideReadContactPermissionProvider = DoubleCheck.provider(AppModule_ProvideReadContactPermissionFactory.create(builder.appModule));
        this.provideContactServiceProvider = DoubleCheck.provider(AppModule_ProvideContactServiceFactory.create(builder.appModule));
        this.wVContactPluginMembersInjector = WVContactPlugin_MembersInjector.create(this.provideReadContactPermissionProvider, this.provideContactServiceProvider, this.provideEventBusProvider);
        this.provideImageSaverProvider = DoubleCheck.provider(AppModule_ProvideImageSaverFactory.create(builder.appModule));
        this.provideCaptureProvider = DoubleCheck.provider(AppModule_ProvideCaptureFactory.create(builder.appModule, this.provideImageSaverProvider));
        this.provideWriteExternalStoragePermissionProvider = DoubleCheck.provider(AppModule_ProvideWriteExternalStoragePermissionFactory.create(builder.appModule));
        this.wVImagePluginMembersInjector = WVImagePlugin_MembersInjector.create(this.provideCaptureProvider, this.provideWriteExternalStoragePermissionProvider, this.provideEventBusProvider);
        this.wVPermissionPluginMembersInjector = WVPermissionPlugin_MembersInjector.create(this.provideReadContactPermissionProvider);
        this.imageDownloaderModuleMembersInjector = ImageDownloaderModule_MembersInjector.create(this.provideWriteExternalStoragePermissionProvider, this.provideEventBusProvider);
        this.shareImageModuleMembersInjector = ShareImageModule_MembersInjector.create(this.provideWriteExternalStoragePermissionProvider);
        this.shareModuleAdapterMembersInjector = ShareModuleAdapter_MembersInjector.create(this.provideWriteExternalStoragePermissionProvider);
        this.wVSharePluginMembersInjector = WVSharePlugin_MembersInjector.create(this.provideWriteExternalStoragePermissionProvider);
        this.alertMessageViewModelMembersInjector = AlertMessageViewModel_MembersInjector.create(this.alertMessageRepositoryProvider);
        this.appMembersInjector = App_MembersInjector.create(this.provideLoginProvider);
        this.providePageRouterProvider = DoubleCheck.provider(AppModule_ProvidePageRouterFactory.create(builder.appModule));
        this.baseActivityMembersInjector = BaseActivity_MembersInjector.create(this.providePageRouterProvider, this.provideLoginProvider, this.provideEventBusProvider);
        this.updateCenterProvider = DoubleCheck.provider(UpdateCenter_Factory.create(this.provideContextProvider, this.provideEventBusProvider));
        this.bottomNavActivityMembersInjector = BottomNavActivity_MembersInjector.create(this.providePageRouterProvider, this.provideLoginProvider, this.provideEventBusProvider, this.updateCenterProvider);
        this.webActivityMembersInjector = WebActivity_MembersInjector.create(this.providePageRouterProvider, this.provideLoginProvider, this.provideEventBusProvider);
        this.wizardActivityMembersInjector = WizardActivity_MembersInjector.create(this.providePageRouterProvider, this.provideLoginProvider);
        this.userRepositoryProvider = DoubleCheck.provider(UserRepository_Factory.create(this.provideAppDatabaseProvider, this.providerExecutorProvider, this.provideMtopServiceProvider, this.provideLoginProvider));
        this.mineViewModelMembersInjector = MineViewModel_MembersInjector.create(this.userRepositoryProvider);
        this.provideCameraAPermissionProvider = DoubleCheck.provider(AppModule_ProvideCameraAPermissionFactory.create(builder.appModule));
        this.newMineFragmentMembersInjector = NewMineFragment_MembersInjector.create(this.provideEventBusProvider, this.provideLoginProvider, this.provideCameraAPermissionProvider);
        this.loginChooserActivityMembersInjector = LoginChooserActivity_MembersInjector.create(this.providePageRouterProvider, this.provideLoginProvider, this.provideEventBusProvider);
        this.baseFragmentMembersInjector = BaseFragment_MembersInjector.create(this.provideEventBusProvider);
        this.pageRouterActivityMembersInjector = PageRouterActivity_MembersInjector.create(this.providePageRouterProvider);
        this.splashAdActivityMembersInjector = SplashAdActivity_MembersInjector.create(this.providePageRouterProvider, this.provideLoginProvider);
        this.shareActivityMembersInjector = ShareActivity_MembersInjector.create(this.providePageRouterProvider, this.provideLoginProvider, this.provideEventBusProvider, this.provideWriteExternalStoragePermissionProvider);
        this.sharePosterActivityMembersInjector = SharePosterActivity_MembersInjector.create(this.providePageRouterProvider, this.provideLoginProvider, this.provideEventBusProvider, this.provideWriteExternalStoragePermissionProvider);
        this.monkeyActivityMembersInjector = MonkeyActivity_MembersInjector.create(this.providePageRouterProvider);
        this.updateActivityMembersInjector = UpdateActivity_MembersInjector.create(this.providePageRouterProvider, this.provideWriteExternalStoragePermissionProvider);
        this.updateRemindDialogMembersInjector = UpdateRemindDialog_MembersInjector.create(this.provideWriteExternalStoragePermissionProvider);
        this.storagePermissionValidatorMembersInjector = StoragePermissionValidator_MembersInjector.create(this.provideWriteExternalStoragePermissionProvider);
    }

    public void inject(MoonJSBridge moonJSBridge) {
        this.moonJSBridgeMembersInjector.injectMembers(moonJSBridge);
    }

    public void inject(WVContactPlugin wVContactPlugin) {
        this.wVContactPluginMembersInjector.injectMembers(wVContactPlugin);
    }

    public void inject(WVImagePlugin wVImagePlugin) {
        this.wVImagePluginMembersInjector.injectMembers(wVImagePlugin);
    }

    public void inject(WVPermissionPlugin wVPermissionPlugin) {
        this.wVPermissionPluginMembersInjector.injectMembers(wVPermissionPlugin);
    }

    public void inject(ImageDownloaderModule imageDownloaderModule) {
        this.imageDownloaderModuleMembersInjector.injectMembers(imageDownloaderModule);
    }

    public void inject(ShareImageModule shareImageModule) {
        this.shareImageModuleMembersInjector.injectMembers(shareImageModule);
    }

    public void inject(ShareModuleAdapter shareModuleAdapter) {
        this.shareModuleAdapterMembersInjector.injectMembers(shareModuleAdapter);
    }

    public void inject(WVSharePlugin wVSharePlugin) {
        this.wVSharePluginMembersInjector.injectMembers(wVSharePlugin);
    }

    public void inject(AlertMessageViewModel alertMessageViewModel) {
        this.alertMessageViewModelMembersInjector.injectMembers(alertMessageViewModel);
    }

    public void inject(App app) {
        this.appMembersInjector.injectMembers(app);
    }

    public void inject(BaseActivity baseActivity) {
        this.baseActivityMembersInjector.injectMembers(baseActivity);
    }

    public void inject(BottomNavActivity bottomNavActivity) {
        this.bottomNavActivityMembersInjector.injectMembers(bottomNavActivity);
    }

    public void inject(WebActivity webActivity) {
        this.webActivityMembersInjector.injectMembers(webActivity);
    }

    public void inject(WizardActivity wizardActivity) {
        this.wizardActivityMembersInjector.injectMembers(wizardActivity);
    }

    public void inject(MineViewModel mineViewModel) {
        this.mineViewModelMembersInjector.injectMembers(mineViewModel);
    }

    public void inject(NewMineFragment newMineFragment) {
        this.newMineFragmentMembersInjector.injectMembers(newMineFragment);
    }

    public void inject(LoginChooserActivity loginChooserActivity) {
        this.loginChooserActivityMembersInjector.injectMembers(loginChooserActivity);
    }

    public void inject(BaseFragment baseFragment) {
        this.baseFragmentMembersInjector.injectMembers(baseFragment);
    }

    public void inject(PageRouterActivity pageRouterActivity) {
        this.pageRouterActivityMembersInjector.injectMembers(pageRouterActivity);
    }

    public void inject(SplashAdActivity splashAdActivity) {
        this.splashAdActivityMembersInjector.injectMembers(splashAdActivity);
    }

    public void inject(RxRequestManager rxRequestManager) {
        MembersInjectors.noOp().injectMembers(rxRequestManager);
    }

    public void inject(ShareActivity shareActivity) {
        this.shareActivityMembersInjector.injectMembers(shareActivity);
    }

    public void inject(SharePosterActivity sharePosterActivity) {
        this.sharePosterActivityMembersInjector.injectMembers(sharePosterActivity);
    }

    public void inject(MonkeyActivity monkeyActivity) {
        this.monkeyActivityMembersInjector.injectMembers(monkeyActivity);
    }

    public void inject(UpdateActivity updateActivity) {
        this.updateActivityMembersInjector.injectMembers(updateActivity);
    }

    public void inject(UpdateRemindDialog updateRemindDialog) {
        this.updateRemindDialogMembersInjector.injectMembers(updateRemindDialog);
    }

    public void inject(StoragePermissionValidator storagePermissionValidator) {
        this.storagePermissionValidatorMembersInjector.injectMembers(storagePermissionValidator);
    }

    public static final class Builder {
        /* access modifiers changed from: private */
        public AppModule appModule;

        private Builder() {
        }

        public AppComponent build() {
            if (this.appModule != null) {
                return new DaggerAppComponent(this);
            }
            throw new IllegalStateException(AppModule.class.getCanonicalName() + " must be set");
        }

        public Builder appModule(AppModule appModule2) {
            this.appModule = (AppModule) Preconditions.checkNotNull(appModule2);
            return this;
        }
    }
}
